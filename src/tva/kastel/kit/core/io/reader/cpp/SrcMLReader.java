package tva.kastel.kit.core.io.reader.cpp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import tva.kastel.kit.core.model.enums.NodeType;
import tva.kastel.kit.core.model.impl.AttributeImpl;
import tva.kastel.kit.core.model.impl.NodeImpl;
import tva.kastel.kit.core.model.impl.StringValueImpl;
import tva.kastel.kit.core.model.impl.TreeImpl;
import tva.kastel.kit.core.io.reader.AbstractArtifactReader;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.interfaces.Tree;
import tva.kastel.kit.core.io.reader.cpp.adjust.AdjustAll;


public class SrcMLReader extends AbstractArtifactReader {


    public static String[] SUPPORTED_FILE_ENDINGS = {"cpp"};

    private AbstractSAXHandler saxHandler;

    public SrcMLReader() {
        super(SUPPORTED_FILE_ENDINGS);
        saxHandler = new SAXHandler();
    }


    public Tree readArtifact(File element, String rootName) {
        Node rootNode = null;

        if (element.isDirectory()) {
            rootNode = createDirectory(element, rootName);
            for (File childElement : element.listFiles()) {
                createHierarchy(childElement, rootNode);
            }

        } else {
            if (isFileSupported(Files.getFileExtension(element.getName()))) {
                rootNode = readArtifactRoot(element);
            } else {
                rootNode = createFile(element);
            }

        }

        return new TreeImpl(element.getName(), rootNode);

    }

    @Override
    public Tree readArtifact(File element) {
        String fileName = Paths.get(element.getAbsolutePath()).getFileName().toString();
        return readArtifact(element, fileName);
    }


    private Node createDirectory(File element) {
        return createDirectory(element, element.getName());
    }

    private Node createDirectory(File element, String directoryName) {
        Node directory = new NodeImpl(NodeType.DIRECTORY);
        directory.addAttribute(new AttributeImpl(AttributeDictionary.DIRECTORY_NAME_ATTRIBUTE_KEY,
                new StringValueImpl(directoryName)));

        return directory;
    }

    private Node createFile(File element) {
        Node file = new NodeImpl(NodeType.FILE);
        file.addAttribute(new AttributeImpl(AttributeDictionary.FILE_NAME_ATTRIBUTE_KEY,
                new StringValueImpl(element.getName())));

        file.addAttribute(new AttributeImpl(AttributeDictionary.FILE_EXTENSION_ATTRIBUTE_KEY,
                new StringValueImpl(Files.getFileExtension(element.getName()))));

        return file;
    }

    private void createHierarchy(File element, Node currentNode) {

        if (element.isDirectory()) {
            Node directory = createDirectory(element);

            currentNode.addChildWithParent(directory);
            for (File childElement : element.listFiles()) {
                createHierarchy(childElement, directory);
            }
        } else {
            if (isFileSupported(Files.getFileExtension(element.getName()))) {
                Node artifactRoot = readArtifactRoot(element);
                currentNode.addChildWithParent(artifactRoot);
            } else {
                Node file = createFile(element);
                currentNode.addChildWithParent(file);
            }

        }

    }

    private Node readArtifactRoot(File element) {

        Node rootNode = null;
        try {

            String xmlOutputPath = File.createTempFile("Temp_", Long.toString(System.nanoTime())).getAbsolutePath();
            InputStream inputStream = getInputStream(Paths.get(element.getAbsolutePath()), xmlOutputPath);
            if (inputStream != null) {

                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                saxParserFactory.setNamespaceAware(true);
                SAXParser saxParser = saxParserFactory.newSAXParser();
                XMLReader xmlReader = saxParser.getXMLReader();
                xmlReader.setContentHandler(saxHandler);
                xmlReader.setErrorHandler(saxHandler);
                saxHandler.setExtension(Files.getFileExtension(element.getName()));

                InputSource inputSource = new InputSource(new InputStreamReader(inputStream, "UTF-8"));
                xmlReader.parse(inputSource);


                AdjustAll adjuster = new AdjustAll(saxHandler.getRootNode());
                rootNode = adjuster.adjustAllNodes(); //Adjust Tree Nodes and return new Root
                String fileName = element.getName().split(".cpp")[0];
                rootNode.getChildren().get(0).addAttribute(new AttributeImpl("Name", new StringValueImpl(fileName)));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootNode;

    }

    private InputStream getInputStream(Path fileArgument, String xmlResultPath) throws IOException {

        InputStream inputStream = null;

        ProcessBuilder processBuilder = new ProcessBuilder();

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {

            processBuilder.command("cmd.exe", "/C", "srcml", fileArgument.toString());

        } else if (os.contains("nix") || os.contains("mac")) {
            processBuilder.command("bash", "-c", "srcml", fileArgument.toString());
        }

        processBuilder.redirectErrorStream(true);

        if (xmlResultPath != null && (!xmlResultPath.equals(""))) {
            File log = new File(xmlResultPath);
            processBuilder.redirectOutput(log);
        }

        processBuilder.redirectOutput(Redirect.PIPE);
        Process process = processBuilder.start();
        inputStream = process.getInputStream();

        return inputStream;
    }


}

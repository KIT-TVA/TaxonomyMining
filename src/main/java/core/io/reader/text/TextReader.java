package main.java.core.io.reader.text;

import main.java.core.model.impl.StringValueImpl;
import main.java.core.model.interfaces.Node;
import main.java.core.io.reader.AbstractArtifactReader;
import main.java.core.model.enums.NodeType;
import main.java.core.model.impl.NodeImpl;
import main.java.core.model.impl.TreeImpl;
import main.java.core.model.interfaces.Tree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/***
 *
 *
 *
 */
public class TextReader extends AbstractArtifactReader {
    public final static String[] SUPPORTED_FILE_ENDINGS = {"txt"};

    public TextReader() {
        super(SUPPORTED_FILE_ENDINGS);
    }

    @Override
    public Tree readArtifact(File element) {
        Tree tree = null;
        if (isFileSupported(element)) {
            String s = null;
            try {
                s = Files.readString(Paths.get(element.getAbsolutePath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Spiting the input by lines
            String[] lines = s.split("\n");
            // The name is only the file name
            String name = element.getAbsolutePath().substring(element.getAbsolutePath().lastIndexOf("\\") + 1);

            tree = new TreeImpl(name, new NodeImpl(NodeType.UNDEFINED, TextFileTags.TEXT.toString()));
            tree.setFileExtension("txt");
            for (String line : lines) {
                Node lineNode = new NodeImpl(NodeType.UNDEFINED, TextFileTags.LINE.toString(), tree.getRoot());
                String[] words = line.split(" ");
                for (String word : words) {
                    Node wordNode = new NodeImpl(NodeType.UNDEFINED, TextFileTags.WORD.toString(), lineNode);
                    wordNode.addAttribute(TextFileTags.TEXT.toString(), new StringValueImpl(word));
                }
            }
        }

        return tree;
    }
}

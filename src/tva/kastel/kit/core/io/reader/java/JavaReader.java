package tva.kastel.kit.core.io.reader.java;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import tva.kastel.kit.core.io.reader.AbstractArtifactReader;
import tva.kastel.kit.core.io.reader.java.factory.NodeFactory;
import tva.kastel.kit.core.io.reader.java.factory.StatementNodeFactory;
import tva.kastel.kit.core.model.enums.NodeType;
import tva.kastel.kit.core.model.impl.AttributeImpl;
import tva.kastel.kit.core.model.impl.NodeImpl;
import tva.kastel.kit.core.model.impl.StringValueImpl;
import tva.kastel.kit.core.model.impl.TreeImpl;
import tva.kastel.kit.core.model.interfaces.Attribute;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.interfaces.Tree;
import tva.kastel.kit.core.model.interfaces.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/***
 * This reader converts java files into the generic data structure.
 *
 * @author Serkan Acar
 * @author Hassan Smaoui
 * @author Pascal Blum
 * @author Paulo Haas
 *
 */
public class JavaReader extends AbstractArtifactReader {
    public final static String[] SUPPORTED_FILE_ENDINGS = {"java"};

    public JavaReader() {
        super(SUPPORTED_FILE_ENDINGS);
    }

    /**
     * Converts java files into a tree.
     *
     * @param element Java file
     * @return Tree
     */
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
            String fileName = Paths.get(element.getAbsolutePath()).getFileName().toString();
            CompilationUnit cu = StaticJavaParser.parse(s);
            Node rootNode = new NodeImpl(NodeType.FILE, "JAVA");
            JavaVisitor visitor = new JavaVisitor(new NodeFactory(new StatementNodeFactory()));
            visitor.visit(cu, rootNode);

            for (Node child : rootNode.getChildren()) {
                if (child.getNodeType().equals("CompilationUnit")) {
                    addNameIfEmptyCompilationUnit(child, fileName);
                }
            }


            tree = new TreeImpl(fileName, rootNode);

        }
        return tree;
    }

    private void addNameIfEmptyCompilationUnit(Node compilationUnit, String fileName) {

        Attribute name = compilationUnit.getAttributeForKey("Name");

        if (name == null) {
            compilationUnit.addAttribute(new AttributeImpl("Name", new StringValueImpl(fileName.replace(".java", ""))));
        }

        Attribute comment = compilationUnit.getAttributeForKey("Comment");

        if (comment == null) {
            Attribute newComment = new AttributeImpl("Comment");
            List<Node> childrenToRemove = new ArrayList<>();
            for (Node child : compilationUnit.getChildren()) {
                if (child.getNodeType().equals("BlockComment")) {

                    List<Value> values = child.getAttributeForKey("Comment").getAttributeValues();

                    for (Value value : values) {
                        newComment.addAttributeValue(value);
                    }
                    childrenToRemove.add(child);
                }

            }

            compilationUnit.getChildren().removeAll(childrenToRemove);
            compilationUnit.addAttribute(newComment);
        }

    }
}
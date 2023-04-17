package main.java.core.io.reader.java;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import main.java.core.io.reader.cpp.adjust.Const;
import main.java.core.io.reader.java.factory.NodeFactory;
import main.java.core.io.reader.AbstractArtifactReader;
import main.java.core.io.reader.java.factory.StatementNodeFactory;
import main.java.core.model.enums.NodeType;
import main.java.core.model.impl.AttributeImpl;
import main.java.core.model.impl.NodeImpl;
import main.java.core.model.impl.StringValueImpl;
import main.java.core.model.impl.TreeImpl;
import main.java.core.model.interfaces.Attribute;
import main.java.core.model.interfaces.Node;
import main.java.core.model.interfaces.Tree;
import main.java.core.model.interfaces.Value;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/***
 * This reader converts java files into the generic data structure.
 *
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
                s = Files.readString(Paths.get(element.getAbsolutePath()), StandardCharsets.ISO_8859_1);
            } catch (IOException e) {
                //TODO: why catch exception, just to throw an exception ???
                throw new RuntimeException(e);
            }
            String fileName = Paths.get(element.getAbsolutePath()).getFileName().toString();
            CompilationUnit cu = StaticJavaParser.parse(s);
            Node rootNode = new NodeImpl(NodeType.FILE, "JAVA");
            JavaVisitor visitor = new JavaVisitor(new NodeFactory(new StatementNodeFactory()));
            visitor.visit(cu, rootNode);

            /* TODO: this looks VERY UNNECESSARY and causes an EMPTY COMMENT in the rootNode
            for (Node child : rootNode.getChildren()) {
                if (child.getNodeType().equals(Const.C_UNIT)) {
                    addNameIfEmptyCompilationUnit(child, fileName);
                }
            } */


            tree = new TreeImpl(fileName, rootNode);

        }
        return tree;
    }

    private void addNameIfEmptyCompilationUnit(Node compilationUnit, String fileName) {

        Attribute name = compilationUnit.getAttributeForKey(Const.NAME_BIG);

        if (name == null) {
            compilationUnit.addAttribute(new AttributeImpl(Const.NAME_BIG, new StringValueImpl(fileName.replace(".java", ""))));
        }

        Attribute comment = compilationUnit.getAttributeForKey(Const.COMMENT_BIG);

        if (comment == null) {
            Attribute newComment = new AttributeImpl(Const.COMMENT_BIG);
            List<Node> childrenToRemove = new ArrayList<>();
            for (Node child : compilationUnit.getChildren()) {
                if (child.getNodeType().equals("BlockComment")) {

                    List<Value> values = child.getAttributeForKey(Const.COMMENT_BIG).getAttributeValues();

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
package io.reader.java;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import model.enums.NodeType;
import model.impl.NodeImpl;
import model.impl.TreeImpl;
import io.reader.AbstractArtifactReader;
import model.interfaces.Node;
import model.interfaces.Tree;
import io.reader.java.factory.NodeFactory;
import io.reader.java.factory.StatementNodeFactory;
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
	public final static String[] SUPPORTED_FILE_ENDINGS = { "java" };

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
			tree = new TreeImpl(fileName, rootNode);

		}
		return tree;
	}
}
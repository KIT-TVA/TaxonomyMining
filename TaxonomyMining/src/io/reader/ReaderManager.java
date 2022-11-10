package io.reader;


import com.google.common.io.Files;
import io.reader.gson.GsonImportService;
import io.reader.java.JavaReader;
import model.enums.NodeType;
import model.impl.NodeImpl;
import model.impl.StringValueImpl;
import model.impl.TreeImpl;
import model.interfaces.Node;
import model.interfaces.Tree;



import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * This class manages all artifact reader and creates a tree if file can be
 * readed.
 * 
 * @author Kamil Rosiak
 *
 */

public class ReaderManager {

	private GsonImportService importService;

	public ReaderManager() {
		importService = new GsonImportService();
	}


	public Tree readFile(File fte) {
		Tree tree = new TreeImpl(fte.getName(), readFileRecursivly(null, fte));
		if (fte.isDirectory()) {
			tree.setFileExtension("DIRECTORY");
		} else {
			tree.setFileExtension(Files.getFileExtension(fte.getName()));
		}

		return tree;
	}

	private Node readFileRecursivly(Node parentNode, File fte) {
		if (fte.isDirectory()) {
			Node nextNode = new NodeImpl(NodeType.DIRECTORY);
			nextNode.addAttribute("DIRECTORY_NAME", new StringValueImpl(fte.getName()));

			Arrays.stream(fte.listFiles()).forEach(childFte -> nextNode.addChildWithParent(readFileRecursivly(nextNode, childFte)));
			return nextNode;
		} else {

			ArtifactReader reader = null;
			if(Files.getFileExtension(fte.getName()).equals("java")) {
				reader = new JavaReader();
			}
			else if(Files.getFileExtension(fte.getName()).equals("tree")) {
				try {
					return importService.importTree(java.nio.file.Files.readString(fte.toPath())).getRoot();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			if (reader != null) {
				return reader.readArtifact(fte).getRoot();
			} else {
				// store files that can't be processed as files
				Node fileNode = new NodeImpl(NodeType.FILE);
				fileNode.addAttribute("FILE_NAME", new StringValueImpl(fte.getName()));
				fileNode.addAttribute("FILE_EXTENSION", new StringValueImpl(Files.getFileExtension(fte.getName())));
				return fileNode;
			}
		}
	}

}

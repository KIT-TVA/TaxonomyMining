package main.java.core.io.reader;


import com.google.common.io.Files;
import main.java.core.io.reader.python.python_reader.PythonFileReader;
import main.java.core.io.reader.cpp.SrcMLReader;
import main.java.core.io.reader.gson.GsonImportService;
import main.java.core.io.reader.java.JavaReader;
import main.java.core.model.enums.NodeType;
import main.java.core.model.impl.NodeImpl;
import main.java.core.model.impl.StringValueImpl;
import main.java.core.model.impl.TreeImpl;
import main.java.core.model.interfaces.Node;
import main.java.core.model.interfaces.Tree;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class manages all artifact reader and creates a tree if file can be
 * readed.
 */

public class ReaderManager {

    private GsonImportService importService;

    private ReaderConfiguration readerConfiguration;

    public ReaderManager() {
        this(new ReaderConfiguration());
    }

    public ReaderManager(ReaderConfiguration readerConfiguration) {
        this.readerConfiguration = readerConfiguration;
        importService = new GsonImportService();
    }


    public List<Tree> readFiles(File inputDirectory) {
        List<Tree> trees = new ArrayList<>();

        File[] files = inputDirectory.listFiles();
        Arrays.sort(files);

        for (File file : files) {
            Tree tree = readFile(file);
            trees.add(tree);
        }

        return trees;
    }


    public Tree readFile(File fte) {
        Tree tree = new TreeImpl(fte.getName(), readFileRecursively(null, fte));
        if (fte.isDirectory()) {
            tree.setFileExtension("DIRECTORY");
        } else {
            tree.setFileExtension(Files.getFileExtension(fte.getName()));
        }

        return tree;
    }

    private Node readFileRecursively(Node parentNode, File fte) {

        if (!checkConfiguration(fte)) {
            return null;
        }

        if (fte.isDirectory()) {
            Node nextNode = new NodeImpl(NodeType.DIRECTORY);
            nextNode.addAttribute("DIRECTORY_NAME", new StringValueImpl(fte.getName()));

            for (File childFte : fte.listFiles()) {
                Node childNode = readFileRecursively(nextNode, childFte);
                if (childNode != null) {
                    nextNode.addChildWithParent(childNode);
                }
            }
            return nextNode;
        } else {
            ArtifactReader reader = null;
            if (Files.getFileExtension(fte.getName()).equals(ReaderTypes.JAVA)) {
                reader = new JavaReader();
            } else if (Files.getFileExtension(fte.getName()).equals(ReaderTypes.CPP)) {
                reader = new SrcMLReader();
            } else if (Files.getFileExtension(fte.getName()).equals(ReaderTypes.PY)) {
                reader = new PythonFileReader();
            } else if (Files.getFileExtension(fte.getName()).equals(ReaderTypes.TREE)) {
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


    private boolean checkConfiguration(File file) {
        if (file.isFile()) {
            return readerConfiguration.getFileTypes().size() == 0 || readerConfiguration.getFileTypes().contains(Files.getFileExtension(file.getName()));
        }
        return true;
    }
}

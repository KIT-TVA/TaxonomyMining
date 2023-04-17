package main.java.core.io.reader.python.python_reader;


import java.io.File;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import main.java.core.io.reader.ReaderTypes;
import main.java.core.io.reader.cpp.adjust.Const;
import main.java.core.io.reader.python.python_adjust.RenamerPython;
import main.java.core.io.reader.AbstractArtifactReader;
import main.java.core.io.reader.python.python_adjust.PAdjustAll;
import main.java.core.model.impl.NodeImpl;
import main.java.core.model.impl.TreeImpl;
import main.java.core.model.interfaces.Node;
import main.java.core.model.interfaces.Tree;

/**
 * This class reads a given Python file, converts it into a AST and adjust it to be similar to the Java AST.
 */
public class PythonFileReader extends AbstractArtifactReader {

    public static String[] SUPPORTED_FILE_ENDINGS = {ReaderTypes.PY};

    private final FileToTreeReader fileToTree;

    /**
     * Constructor
     */
    public PythonFileReader() {
        super(SUPPORTED_FILE_ENDINGS);
        fileToTree = new FileToTreeReader();
    }

    @Override
    public Tree readArtifact(File element) {
        Node rootNode = new NodeImpl(Const.PYTHON);
        String path = element.getAbsolutePath();
        path = Const.QUOTATION + path.replace(Const.BACKSLASH, Const.DIVIDE_OP) + Const.QUOTATION;

        Gson gson = new Gson();
        JsonObject obj = gson.fromJson(fileToTree.getTreeFromFileAsJSON(path), JsonObject.class);

        generateNodes(obj, rootNode, gson);

        //adjust the tree so it can be compared to other programming languages
        PAdjustAll PAdjustAll = new PAdjustAll();
        PAdjustAll.adjustAll(rootNode);

        //get file name without extension
        String name = element.getName().substring(0, element.getName().length() - 3);
        rootNode.getChildren().get(0).addAttribute(Const.NAME_BIG, name);

        return new TreeImpl(element.getName(), rootNode);

    }

    private void generateNodes(JsonArray jsonArr, Node rootNode, Gson gson) {
        for (int i = 0; i < jsonArr.size(); i++) {
            JsonObject obj = gson.fromJson(jsonArr.get(i).toString(), JsonObject.class);
            generateNodes(obj, rootNode, gson);
        }
    }

    private void generateNodes(JsonObject obj, Node rootNode, Gson gson) {
        JsonObject jsonObj = gson.toJsonTree(obj).getAsJsonObject();
        Node node = null;
        for (Map.Entry<String, JsonElement> entry : jsonObj.entrySet()) {
            if (entry.getKey().equals(Const._TYPE)) {
                node = new NodeImpl(getName(entry.getValue()), rootNode);
            } else if (node != null) {
                try {
                    createNodefromJsonObject(gson, node, entry.getValue().toString(), entry.getKey());
                } catch (JsonSyntaxException e) {
                    try {
                        createNodefromJsonArray(gson, node, entry.getValue().toString(), entry.getKey());
                    } catch (JsonSyntaxException b) {
                        addAttribute(node, entry.getKey(), entry.getValue());
                    }
                }

            }
        }
    }

    private void createNodefromJsonObject(Gson gson, Node node, String value, String entry) {
        JsonArray jsonArr = gson.fromJson(value, JsonArray.class);
        Node nextNode = new NodeImpl(entry, node);
        generateNodes(jsonArr, nextNode, gson);
    }

    private void createNodefromJsonArray(Gson gson, Node node, String value, String entry) {
        JsonObject nextObj = gson.fromJson(value, JsonObject.class);
        Node nextNode = new NodeImpl(entry, node);
        generateNodes(nextObj, nextNode, gson);
    }

    private void addAttribute(Node node, String entry, JsonElement value) {
        if (!Const.BANED_ATTRIBUTES.contains(entry)) {
            String entryName = RenamerPython.getInstance().renameAttribute(entry);
            node.addAttribute(entryName, getName(value));
        }
    }

    private String getName(JsonElement element) {
        if (element.toString().startsWith(Const.QUOTATION)) {
            //remove quotations
            return element.toString().substring(1, element.toString().length() - 1);
        }
        return element.toString();
    }

}
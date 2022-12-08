package tva.kastel.kit.core.io.reader.gson;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tva.kastel.kit.core.model.impl.TreeImpl;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.interfaces.Tree;

/**
 * Implementation of the import service with GSON.
 *
 * @author Team 6
 */
public class GsonImportService implements ImportService<String> {
    private Gson gson;

    /**
     * Default Constructor
     * <p>
     * Creates a Gson builder, with custom tree instance creator.
     */
    public GsonImportService() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Tree.class, new TreeInstanceCreator());
        gsonBuilder.registerTypeAdapter(Node.class, new NodeInstanceCreator());

        // Short term solution for deserialization
        gsonBuilder.registerTypeAdapter(Node.class, new NodeDeserializer());

        this.gson = gsonBuilder.create();
    }

    /**
     * Imports a tree from JSON.
     *
     * @param jsonString The JSON string to deserialize.
     * @return Returns a deserialized instance of TreeImpl.
     */
    @Override
    public Tree importTree(String jsonString) {
        TreeImpl tree = (TreeImpl) this.gson.fromJson(jsonString, TreeImpl.class);
        this.reconstructTree(tree.getRoot());
        return tree;
    }

    /**
     * The tree is double linked, this can't be displayed in a JSON-Format.
     * This function recreates this links at import time.
     *
     * @param node The root node of the tree.
     */
    private void reconstructTree(Node node) {
        if (node == null || node.getChildren() == null) {
            return;
        }

        for (Node children : node.getChildren()) {
            children.setParent(node);
            reconstructTree(children);
        }
    }
}

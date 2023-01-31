package tva.kastel.kit.core.io.reader.python.python_adjust;


import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.model.interfaces.Node;

import java.util.HashMap;
import java.util.Map;


/**
 * This class is made for renaming NodeTypes for the parsed Tree of python source code.
 * The NodeTypes are renamed, so they are more similar to the NodeTypes of Java source code
 * and can be easier compared to Java source code
 *
 * @author david bumm
 */
public final class RenamerPython {
    private static RenamerPython instance = null;
    private final Map<String, String> nodeMap;
    private final Map<String, String> attrMap;

    private RenamerPython() {
        nodeMap = new HashMap<>();
        attrMap = new HashMap<>();
        fillNodeMap();
        fillAttrMap();
    }

    /**
     * Singleton Constructor, so the hash map only has to be build once.
     *
     * @return RenamerCpp as a Singleton
     */
    public static RenamerPython getInstance() {
        if (instance == null) {
            instance = new RenamerPython();
        }
        return instance;
    }

    private void fillAttrMap() {
        attrMap.put("id", Const.NAME_BIG);
        attrMap.put(Const.VALUE.toLowerCase(), Const.VALUE);
        attrMap.put(Const.NAME_SMALL, Const.NAME_BIG);
    }

    private void fillNodeMap() {
        nodeMap.put("Module", Const.C_UNIT);
        nodeMap.put("FunctionDef", Const.M_DECL);
        nodeMap.put("If", Const.IF_STMT_BIG);
        nodeMap.put("Return", Const.RETURN_STMT);
        nodeMap.put("Assign", Const.ASSIGNMENT);
        nodeMap.put("Test", Const.CONDITION_BIG);
        nodeMap.put("Arguments", Const.ARGUMENT_BIG);
        nodeMap.put("Arg", Const.ARGUMENT_BIG);
        nodeMap.put(Const.OR_ELSE, Const.ELSE_BIG);
        nodeMap.put("BinOp", Const.BINARY_EXPR);
        nodeMap.put("Iter", Const.CONDITION_BIG);
        nodeMap.put("For", Const.FOR_STMT);
        nodeMap.put(Const.NAME_BIG, Const.NAME_EXPR);

    }


    /**
     * renames the Attribute of a Node to their equivalent of a Java Attribute.
     * If the Attribute has no equivalent, it won't be renamed.
     *
     * @param attribute is the Attribute that might be renamed
     */
    public String renameAttribute(String attribute) {
        if (!attrMap.containsKey(attribute)) {
            return attribute;
        }
        return attrMap.get(attribute);
    }


    /**
     * renames the NodeType of a Node to their equivalent of a Java NodeType.
     * If the NodeType has no equivalent, it won't be renamed.
     *
     * @param node is the Node that might be renamed
     */
    public void renameNode(Node node) {
        if (!nodeMap.containsKey(node.getNodeType())) {

            return;
        }
        String newName = nodeMap.get(node.getNodeType());
        node.setNodeType(newName);
    }
}


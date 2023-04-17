package main.java.core.io.reader.python.python_adjust;


import main.java.core.io.reader.cpp.adjust.Const;
import main.java.core.model.interfaces.Node;

import java.util.HashMap;
import java.util.Map;


/**
 * This class is made for renaming NodeTypes and Attributes for the parsed Tree of python source code.
 * The NodeTypes and Attributes are renamed, so they are more similar to the NodeTypes and Attributes
 * of Java source code. This makes it easier to compare java and python source code.
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
        attrMap.put(Const.ID, Const.NAME_BIG);
        attrMap.put(Const.VALUE.toLowerCase(), Const.VALUE);
        attrMap.put(Const.NAME_SMALL, Const.NAME_BIG);
    }

    private void fillNodeMap() {
        nodeMap.put(Const.MODULE, Const.C_UNIT);
        nodeMap.put(Const.FUNCTION_DEF, Const.M_DECL);
        nodeMap.put(Const.IF_BIG, Const.IF_STMT_BIG);
        nodeMap.put(Const.RETURN_BIG, Const.RETURN_STMT);
        nodeMap.put(Const.ASSIGN_BIG, Const.ASSIGNMENT);
        nodeMap.put(Const.TEST, Const.CONDITION_BIG);
        nodeMap.put(Const.ARGUMENTS, Const.ARGUMENT_BIG);
        nodeMap.put(Const.ARG, Const.ARGUMENT_BIG);
        nodeMap.put(Const.OR_ELSE, Const.ELSE_BIG);
        nodeMap.put(Const.BIN_OP, Const.BINARY_EXPR);
        nodeMap.put(Const.ITER, Const.CONDITION_BIG);
        nodeMap.put(Const.FOR_BIG, Const.FOR_STMT);
        nodeMap.put(Const.NAME_BIG, Const.NAME_EXPR);
        nodeMap.put(Const.WHILE_BIG, Const.WHILE_STMT);
        nodeMap.put(Const.UNARY_OP, Const.UNARY_EXPR);
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


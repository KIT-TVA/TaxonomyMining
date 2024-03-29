package main.java.core.io.reader.cpp.adjust;


import main.java.core.model.impl.AttributeImpl;
import main.java.core.model.impl.StringValueImpl;
import main.java.core.model.interfaces.Attribute;
import main.java.core.model.interfaces.Node;
import main.java.core.model.impl.NodeImpl;

import java.util.ArrayList;

/**
 * This class is the entry point for adjusting a tree.
 * It adjusts the root node and calls all existing TreeAdjuster to make the tree as similar as possible to
 * a tree generated from Java source code.
 */
public final class AdjustAll extends TreeAdjuster {
    private Node rootNode;

    public AdjustAll(Node rootNode) {
        this.rootNode = rootNode;
    }

    public Node adjustAllNodes() {
        if (rootNode == null) {
            return null;
        }

        Node node = new NodeImpl(Const.C_PLUS_PLUS);
        node.addChildWithParent(rootNode);
        rootNode.addAttribute(new AttributeImpl(Const.IS_INTERFACE, new StringValueImpl(Const.FALSE)));

        TreeAdjuster arrAdjuster = new AdjustArray();
        arrAdjuster.recursiveAdjust(rootNode);

        TreeAdjuster assignAdjuster = new AdjustAssignment();
        assignAdjuster.recursiveAdjust(rootNode);

        TreeAdjuster opAdjuster = new AdjustOperator();
        opAdjuster.recursiveAdjust(rootNode);

        TreeAdjuster literalAdjuster = new AdjustLiterals();
        literalAdjuster.recursiveAdjust(rootNode);

        TreeAdjuster methodAdjuster = new AdjustMethodCall();
        methodAdjuster.recursiveAdjust(rootNode);

        TreeAdjuster nameAdjuster = new AdjustName();
        nameAdjuster.recursiveAdjust(rootNode);

        recursiveAdjust(rootNode);

        TreeAdjuster conAdjuster = new AdjustConditon();
        conAdjuster.recursiveAdjust(rootNode);

        TreeAdjuster renameAdjuster = new AdjustRename();
        renameAdjuster.recursiveAdjust(rootNode);

        TreeAdjuster superAdjuster = new AdjustSuperClass();
        superAdjuster.recursiveAdjust(rootNode); //should happen before AdjustSpecifier, or AccessModifier gets lost

        TreeAdjuster specAdjuster = new AdjustSpecifier();
        specAdjuster.recursiveAdjust(rootNode);


        return node;
    }

    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.CONTROL) || nodeType.equals(Const.BODY) && (parent.getNodeType().equals(Const.ENUM_DECLARATION)
                || (parent.getNodeType().equals(Const.BODY)))) {
            node.cutWithoutChildren();
        }
        if (nodeType.equals(Const.FIELD_DECL) // enum edge case
                && parent.getNodeType().equals(Const.ENUM_DECLARATION)) {
            node.setNodeType(Const.ENUM_CONST_DECL);
        }
        if (nodeType.equals(Const.FIELD_DECL) && parent.getNodeType().equals(Const.ARGUMENT_BIG)) {
            node.setNodeType(Const.ARGUMENT_BIG);
            parent.cutWithoutChildren();
        }

        if ((nodeType.equals(Const.PRIVATE) || nodeType.equals(Const.PUBLIC)) && node.getAttributes().isEmpty()) {
            node.cut();
        }


        TreeAdjuster forAdjuster = new AdjustForLoop();
        forAdjuster.adjust(node, parent, nodeType);


        if (nodeType.equals(Const.RETURN_STMT) && node.getChildren().size() > 0) {
            node.setAttributes(new ArrayList<Attribute>());
            if (node.getChildren().get(0).getNodeType().equals(Const.EXPR)) {
                node.getChildren().get(0).cutWithoutChildren();
            }
        }

        TreeAdjuster ifAdjuster = new AdjustIfCase();
        ifAdjuster.adjust(node, parent, nodeType);

        TreeAdjuster switchAdjuster = new AdjustSwitchCase();
        switchAdjuster.adjust(node, parent, nodeType);


        TreeAdjuster commentAdjuster = new AdjustComment();
        commentAdjuster.adjust(node, parent, nodeType);


    }

}

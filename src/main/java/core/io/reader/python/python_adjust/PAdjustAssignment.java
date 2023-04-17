package main.java.core.io.reader.python.python_adjust;


import main.java.core.io.reader.cpp.adjust.Const;
import main.java.core.io.reader.cpp.adjust.TreeAdjuster;
import main.java.core.model.impl.NodeImpl;
import main.java.core.model.interfaces.Node;

/**
 * This class is a subclass of TreeAdjuster.
 * It adjusts all nodes that have something to do with value assignments.
 * It is initially called by AdjustAll.
 */
public class PAdjustAssignment extends TreeAdjuster {

    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.ASSIGNMENT)) {
            Node variableExpr = new NodeImpl(Const.VARIABLE_DECL_EXPR);
            Node variableDecl = new NodeImpl(Const.VARIABLE_DECL, variableExpr);
            String name = "";

            for (Node child : node.getChildren()) {
                if (child.getNodeType().equals("Targets") && !child.getChildren().isEmpty()) {
                    name = child.getChildren().get(0).getValueAt(0);
                } else if (child.getNodeType().equals("Value") && !child.getChildren().isEmpty()) {
                    variableDecl.addChildWithParent(child.getChildren().get(0));
                }
            }
            variableDecl.addAttribute(Const.NAME_BIG, name);
            parent.addChild(variableExpr, node.cut());
        } else if (nodeType.equals(Const.AUG_ASSIGN)) {
            Node value = null;
            String target = Const.EMPTY;
            String operator = Const.EMPTY;

            for (Node child : node.getChildren()) {
                if (child.getChildren().isEmpty()) {
                    continue;
                }
                if (child.getNodeType().equals(Const.OP)) {
                    String opNodeType = child.getChildren().get(0).getNodeType();
                    operator = getOperatorFromNodeType(opNodeType);
                } else if (child.getNodeType().equals(Const.TARGET)) {
                    target = child.getChildren().get(0).getValueAt(0);
                } else if (child.getNodeType().equals(Const.VALUE)) {
                    value = child.getChildren().get(0);
                }
            }


            node.getChildren().clear();
            node.addChildWithParent(value);
            node.setNodeType(Const.ASSIGNMENT);
            node.addAttribute(Const.TARGET, target);
            node.addAttribute(Const.OPERATOR_BIG, operator);
        }

    }

}

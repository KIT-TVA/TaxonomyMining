package main.java.core.io.reader.cpp.adjust;

import main.java.core.model.impl.AttributeImpl;
import main.java.core.model.impl.NodeImpl;
import main.java.core.model.impl.StringValueImpl;
import main.java.core.model.interfaces.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a sub class of TreeAdjuster.
 * This class adjust the given tree according to value assignments. E.g int value = 3;
 * It works for array assignments just as for variables.
 * It is initially called by AdjustAll.
 */
public class AdjustAssignment extends TreeAdjuster {

    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        //Assignment
        if (nodeType.equals(Const.EXPR_STMT) && !node.getChildren().isEmpty()) {
            Node exprNode = node.getChildren().get(0);
            if (!exprNode.getChildren().isEmpty()) {
                boolean found = false;
                boolean beforeOP = true;
                List<Node> targetList = new ArrayList<>();
                List<Node> valueList = new ArrayList<>();
                String op = Const.EMPTY;

                for (Node child : exprNode.getChildren()) {
                    if (child.getNodeType().equals(Const.OPERATOR_SMALL) && child.getValueAt(0).contains(Const.EQ)) {
                        found = true;
                        beforeOP = false;
                        op = child.getValueAt(0);
                    } else if (beforeOP) {
                        targetList.add(child);
                    } else {
                        valueList.add(child);
                    }
                }
                if (found) {
                    Node assignment = new NodeImpl(Const.ASSIGNMENT);
                    assignment.setParent(node.getParent());
                    node.getParent().addChild(assignment, node.cut());
                    assignment.addAttribute(new AttributeImpl(Const.TARGET, new StringValueImpl(combineToString(targetList))));
                    assignment.addAttribute(new AttributeImpl(Const.OPERATOR_BIG, new StringValueImpl(getOperation(op))));

                    if (valueList.size() == 1 && !valueList.get(0).getNodeType().equals(Const.NAME_BIG)) {
                        valueList.get(0).updateParent(assignment);
                    } else {
                        Node valueNode = new NodeImpl(Const.NAME_EXPR, assignment);
                        valueNode.addAttribute(new AttributeImpl(Const.VALUE, new StringValueImpl(combineToString(valueList))));
                    }
                }
            }
        }

    }


    private String combineToString(List<Node> nodes) {
        StringBuilder value = new StringBuilder(Const.EMPTY);
        for (Node node : nodes) {
            if (!node.getAttributes().isEmpty()) {
                if (node.getNodeType().equals(Const.ARR_ACCESS_EXPR)) {
                    String text = node.getChildren().get(0).getValueAt(0);
                    text += "[" + node.getValueAt(0) + "]";
                    value.append(text);
                } else if (node.getNodeType().equals(Const.METHOD_CALL)) {
                    String name = node.getValueAt(0);
                    StringBuilder attributes = new StringBuilder(Const.EMPTY);
                    List<Node> attrNodes = node.getChildren().get(1).getChildren();
                    for (int i = 0; i < attrNodes.size(); i++) {
                        Node attr = attrNodes.get(i).getChildren().get(0);
                        attributes.append(attr.getValueAt(0));
                        if (i + 1 != attrNodes.size()) {
                            attributes.append(" ,");
                        }
                    }
                    value.append(name).append("(").append(attributes).append(")");
                } else {
                    value.append(node.getValueAt(0));
                }
            }
        }
        return value.toString();
    }

    private String getOperation(String op) {
        return switch (op) {
            case Const.EQ -> Const.ASSIGN;
            case Const.PLUS_OP + Const.EQ -> Const.PLUS;
            case Const.DIVIDE_OP + Const.EQ -> Const.DIVIDE;
            case Const.MULTIPLY_OP + Const.EQ -> Const.MULTIPLY;
            case Const.MINUS_OP + Const.EQ -> Const.MINUS;
            default -> Const.EMPTY;
        };
    }
}

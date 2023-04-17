package main.java.core.io.reader.python.python_adjust;

import main.java.core.io.reader.cpp.adjust.Const;
import main.java.core.io.reader.cpp.adjust.TreeAdjuster;
import main.java.core.model.interfaces.Node;

import java.util.ArrayList;
import java.util.List;

public class PAdjustBinaryExpr extends TreeAdjuster {

    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.BINARY_EXPR)) {
            List<Node> removables = new ArrayList<>();
            Node operator = null;
            for (Node child : node.getChildren()) {
                if (child.getNodeType().equals("Left") || child.getNodeType().equals("Right")) {
                    removables.add(child);
                }
                if (child.getNodeType().equals(Const.OP) && !child.getChildren().isEmpty()) {
                    String op = getOperatorFromNodeType(child.getChildren().get(0).getNodeType());
                    node.addAttribute(Const.OPERATOR_BIG, op);
                    operator = child;
                }
            }
            for (Node n : removables) {
                n.cutWithoutChildren();
            }
            if (operator != null) {
                operator.cut();
            }
        }
        if (nodeType.equals(Const.BOOL_OP)) {
           node.setNodeType(Const.BINARY_EXPR);
           List<Node> children = new ArrayList<>();

            String operator = Const.EMPTY;
            for (Node child: node.getChildren()) {
                if (child.getNodeType().equals(Const.OP) && !child.getChildren().isEmpty()) {
                    operator = getOperatorFromNodeType(child.getChildren().get(0).getNodeType());
                } else if (child.getNodeType().equals(Const.VALUES) && !child.getChildren().isEmpty()) {
                    children.addAll(child.getChildren());
                }
            }
            node.getChildren().clear();
            for (Node child: children) {
                node.addChildWithParent(child);
            }

            node.addAttribute(Const.OPERATOR_BIG, operator);

        }
    }
}

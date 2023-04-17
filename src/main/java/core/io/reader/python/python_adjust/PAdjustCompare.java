package main.java.core.io.reader.python.python_adjust;

import main.java.core.io.reader.cpp.adjust.Const;
import main.java.core.io.reader.cpp.adjust.TreeAdjuster;
import main.java.core.model.interfaces.Node;
import main.java.core.model.impl.NodeImpl;

public class PAdjustCompare extends TreeAdjuster {
    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.COMPARE)) {
            Node binaryExpr = new NodeImpl(Const.BINARY_EXPR, parent);
            String operator = "";
            for (Node child : node.getChildren()) {
                if (child.getNodeType().equals(Const.OPS) && !child.getChildren().isEmpty()) {
                    operator = child.getChildren().get(0).getNodeType();
                    operator = getOperatorFromNodeType(operator);
                } else if (!child.getChildren().isEmpty()) {
                    for (Node childChild : child.getChildren()) {

                        binaryExpr.addChildWithParent(childChild.cloneNode());
                    }
                }
            }
            binaryExpr.addAttribute(Const.OPERATOR_BIG, operator);
            node.cut();
        }
    }
}

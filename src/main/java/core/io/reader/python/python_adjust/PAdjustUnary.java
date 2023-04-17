package main.java.core.io.reader.python.python_adjust;

import main.java.core.io.reader.cpp.adjust.Const;
import main.java.core.io.reader.cpp.adjust.TreeAdjuster;
import main.java.core.model.interfaces.Node;


/**
 * This class is a subclass of TreeAdjuster.
 * It adjusts all nodes that have something to do with unary expressions (e.g -x)
 * It is initially called by AdjustAll.
 */
public class PAdjustUnary extends TreeAdjuster {
    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.UNARY_EXPR)) {
            String operator = Const.EMPTY;
            Node operand = null;
            for (Node child : node.getChildren()) {
                if (child.getNodeType().equals(Const.OP) && !child.getChildren().isEmpty()) {
                    operator = getOperatorFromNodeType(child.getChildren().get(0).getNodeType());
                } else if (child.getNodeType().equals(Const.OPERAND) && !child.getChildren().isEmpty()) {
                    operand = child.getChildren().get(0).cloneNode();
                }
            }
            node.getChildren().clear();
            node.addChildWithParent(operand);
            node.addAttribute(Const.OPERATOR_BIG, operator);
        }
    }
}

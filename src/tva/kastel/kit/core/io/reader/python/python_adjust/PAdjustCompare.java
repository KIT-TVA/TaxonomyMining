package tva.kastel.kit.core.io.reader.python.python_adjust;

import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.impl.NodeImpl;
import tva.kastel.kit.core.model.interfaces.Node;

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

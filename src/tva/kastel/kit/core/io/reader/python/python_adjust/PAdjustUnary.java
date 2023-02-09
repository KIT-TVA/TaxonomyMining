package tva.kastel.kit.core.io.reader.python.python_adjust;

import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.interfaces.Node;

public class PAdjustUnary extends TreeAdjuster {
    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.UNARY_EXPR)) {
            String operator = Const.EMPTY;
            Node operand = null;
            for (Node child: node.getChildren()) {
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


    private String getOperatorFromNodeType(String nodeType) {
        return switch (nodeType) {
            case "Not" -> Const.LOGICAL_COMP;
            case "USub" -> Const.MINUS;
            case "UAdd" -> Const.PLUS;
            case "Invert" -> Const.BITWISE_COMP;

            default -> nodeType;
        };
    }
}

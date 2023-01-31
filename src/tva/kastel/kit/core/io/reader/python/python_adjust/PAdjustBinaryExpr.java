package tva.kastel.kit.core.io.reader.python.python_adjust;

import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.impl.NodeImpl;
import tva.kastel.kit.core.model.interfaces.Node;

import java.util.ArrayList;
import java.util.List;

public class PAdjustBinaryExpr extends TreeAdjuster {

    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.BINARY_EXPR)) {
            List<Node> removables = new ArrayList<>();
            Node operator = null;
            for (Node child : node.getChildren()) {
                if (child.getNodeType().equals("Left") || child.getNodeType().equals("Right"))  {
                    removables.add(child);
                }
                if (child.getNodeType().equals("Op") && !child.getChildren().isEmpty()) {
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
        } else if (nodeType.equals(Const.COMPARE)) {
            Node binaryExpr = new NodeImpl(Const.BINARY_EXPR, parent);
            String operator = "";
            for (Node child : node.getChildren()) {
                if (child.getNodeType().equals(Const.OPS) && !child.getChildren().isEmpty()) {
                    operator = child.getChildren().get(0).getNodeType();
                    operator = getOperatorFromNodeType(operator);
                } else if (!child.getChildren().isEmpty()) {
                    for (Node childChild: child.getChildren()) {

                        binaryExpr.addChildWithParent(childChild.cloneNode());
                    }
                }
            }
            binaryExpr.addAttribute(Const.OPERATOR_BIG, operator);
            node.cut();
        }
    }
    
    

    private String getOperatorFromNodeType(String nodeType) {
        return switch (nodeType) {
            case "Add" -> Const.PLUS;
            case "Div" -> Const.DIVIDE;
            case "Mult" -> Const.MULTIPLY;
            case "Sub" -> Const.MINUS;
            case "Eq" -> Const.EQUALS;
            case "NotEq" -> Const.NOT_EQUALS;
            case "Gt" -> Const.GREATER;
            case "GtE" -> Const.GREATER_EQUALS;
            case "Lt" -> Const.LESS;
            case "LtE" -> Const.LESS_EQUALS;
            default -> nodeType;
        };
    }
}

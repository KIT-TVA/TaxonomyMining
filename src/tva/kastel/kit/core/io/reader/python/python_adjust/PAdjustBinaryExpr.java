package tva.kastel.kit.core.io.reader.python.python_adjust;

import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
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
        }
    }

    private String getOperatorFromNodeType(String nodeType) {
        switch (nodeType) {
            case "Add":
                return Const.PLUS;
            case "Div":
                return Const.DIVIDE;
            case "Mult":
                return Const.MULTIPLY;
            case "Sub":
                return Const.MINUS;
            default:
                return nodeType;
        }
    }
}

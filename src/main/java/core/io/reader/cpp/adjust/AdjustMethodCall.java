package main.java.core.io.reader.cpp.adjust;

import main.java.core.model.interfaces.Node;
import main.java.core.model.impl.NodeImpl;

/**
 * This class is a sub class of TreeAdjuster.
 * It adjust everything that has to do with method call expressions.
 * It is initially called by AdjustAll.
 */
public class AdjustMethodCall extends TreeAdjuster {

    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.EXPR_STMT)) {
            node.setNodeType(Const.METHOD_CALL);
            if (node.getChildren().size() > 0) {
                Node exprNode = node.getChildren().get(0);
                if (exprNode.getChildren().size() > 0) {
                    Node callNode = exprNode.getChildren().get(0);
                    if (callNode.getAttributes().size() > 0) {
                        node.addAttribute(callNode.getAttributes().get(0));
                        callNode.cutWithoutChildren();
                    }
                }
                if (!exprNode.getAttributes().isEmpty()) {
                    Node nameExpr = new NodeImpl(Const.NAME_EXPR, node);
                    nameExpr.addAttribute(exprNode.getAttributes().get(0));
                }
                exprNode.cutWithoutChildren();
            }
        }

        if (nodeType.equals(Const.ARGUMENT_SMALL)) {
            if (node.getChildren().isEmpty()) {
                return;
            }
            Node exprNode = node.getChildren().get(0);
            exprNode.cutWithoutChildren();

        }

    }

}

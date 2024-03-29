package main.java.core.io.reader.cpp.adjust;

import main.java.core.model.impl.NodeImpl;
import main.java.core.model.interfaces.Attribute;
import main.java.core.model.interfaces.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a sub class of TreeAdjuster. It adjust everything that has to
 * do with If-Else-Cases. It is initially called by AdjustAll.
 */
public class AdjustIfCase extends TreeAdjuster {

    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.IF_STMT_SMALL)) {
            List<Node> children = node.getChildren();
            for (int i = 0; i < children.size(); i++) {
                Node child = children.get(i);
                if (child.getNodeType().equals(Const.IF_STMT_BIG)) {
                    Node thenNode = new NodeImpl(Const.THEN_BIG);
                    child.addChildWithParent(thenNode);
                    Node bodyNode = getChild(child, Const.BODY);
                    if (bodyNode == null) {
                        break;
                    }
                    bodyNode.updateParent(thenNode);

                    if ((i + 1) == children.size()) {
                        break;
                    }
                    Node nextChild = children.get(i + 1);
                    if (nextChild.getNodeType().equals(Const.ELSE_BIG)) {
                        nextChild.setAttributes(new ArrayList<Attribute>());
                        child.addChildWithParent(nextChild);
                    } else {
                        Node elseNode = new NodeImpl(Const.ELSE_BIG);
                        child.addChildWithParent(elseNode);
                        elseNode.addChildWithParent(nextChild);
                    }
                }
            }
            Node ifStmt = node.getChildren().get(0);
            ifStmt.setParent(parent);
            parent.addChild(ifStmt, node.cut());
        }

    }

}

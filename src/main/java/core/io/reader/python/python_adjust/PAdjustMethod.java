package main.java.core.io.reader.python.python_adjust;

import main.java.core.io.reader.cpp.adjust.Const;
import main.java.core.io.reader.cpp.adjust.TreeAdjuster;
import main.java.core.model.interfaces.Node;

import java.util.Objects;


/**
 * This class is a subclass of TreeAdjuster.
 * It adjusts all nodes that have something to do with method declarations.
 * It is initially called by AdjustAll.
 */


public class PAdjustMethod extends TreeAdjuster {
    @Override
    protected void adjust(Node node, Node parent, String nodeType) {

        addReturnType(node, parent, nodeType);
    }


    private void addReturnType(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.M_DECL)) {
            for (Node child : node.getChildren()) {
                if (child.getNodeType().equals(Const.RETURNS)) {
                    String type = child.getChildren().get(0).getValueAt(0);
                    node.addAttribute(Const.RETURN_TYPE, Objects.requireNonNullElse(type, Const.VOID));
                    child.cut();
                    return;
                }
            }
            node.addAttribute(Const.RETURN_TYPE, Const.VOID);
        }
    }
}

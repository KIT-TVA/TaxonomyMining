package tva.kastel.kit.core.io.reader.python.python_adjust;


import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.interfaces.Node;

/**
 * This class is a subclass of TreeAdjuster.
 * It adjusts all nodes that have something to do with if-statements.
 * It is initially called by AdjustAll.
 *
 * @author David Bumm
 */
public class PAdjustIf extends TreeAdjuster {

    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.IF_STMT_BIG)) {
            for (Node child : node.getChildren()) {
                if (child.getNodeType().equals(Const.BODY)) {
                    child.setNodeType(Const.THEN_BIG);
                }
            }
        }
        if (nodeType.equals(Const.ELSE_BIG) && node.getChildren().isEmpty()) {
            node.cut();
        }

    }

}

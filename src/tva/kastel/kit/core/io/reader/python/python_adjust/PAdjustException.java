package tva.kastel.kit.core.io.reader.python.python_adjust;

import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.interfaces.Node;

/**
 * This class is a subclass of TreeAdjuster.
 * It adjusts all nodes that have something to do with exceptions.
 * It is initially called by AdjustAll.
 *
 * @author David Bumm
 */
public class PAdjustException extends TreeAdjuster {
    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.METHOD_CALL) && parent.getNodeType().equals(Const.EXC)) {
            node.setNodeType(Const.EXCEPTION);
            parent.cutWithoutChildren();
        }
    }
}

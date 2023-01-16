package tva.kastel.kit.core.io.reader.cpp.adjust;

import tva.kastel.kit.core.model.interfaces.Node;

/**
 * This class is a sub class of TreeAdjuster.
 * It adjust everything that has to do with Conditions.
 * Conditions occur for example in if cases.
 * It is initially called by AdjustAll.
 *
 * @author David Bumm
 */
public class AdjustConditon extends TreeAdjuster {

    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.EXPR) && parent.getNodeType().equals(Const.CONDITION_BIG)) {
            node.cutWithoutChildren();
        }
    }
}

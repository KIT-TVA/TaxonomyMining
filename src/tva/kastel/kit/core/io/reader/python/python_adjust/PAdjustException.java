package tva.kastel.kit.core.io.reader.python.python_adjust;

import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.interfaces.Node;

public class PAdjustException extends TreeAdjuster {
    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.METHOD_CALL) && parent.getNodeType().equals(Const.EXC)) {
            node.setNodeType(Const.EXCEPTION);
            parent.cutWithoutChildren();
        }
    }
}

package tva.kastel.kit.core.io.reader.python.python_adjust;

import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.impl.NodeImpl;
import tva.kastel.kit.core.model.interfaces.Node;

public class PAdjustArray extends TreeAdjuster {
    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.SUBSCRIPT)) {
            Node arrayAccess = new NodeImpl(Const.ARR_ACCESS_EXPR, parent);
            String array = Const.EMPTY;
            Node access = null;
            for (Node child : node.getChildren()) {
                if (child.getNodeType().equals(Const.SLICE) && !child.getChildren().isEmpty()) {
                    Node accessNode = child.getChildren().get(0);
                    access = accessNode.cloneNode();
                } else if (child.getNodeType().equals(Const.VALUE) && !child.getChildren().isEmpty()) {
                    array = child.getChildren().get(0).getValueAt(0);
                }
            }
            arrayAccess.addAttribute(Const.VALUE, array);
            arrayAccess.addChildWithParent(access);
            node.cut();
        }
    }

}

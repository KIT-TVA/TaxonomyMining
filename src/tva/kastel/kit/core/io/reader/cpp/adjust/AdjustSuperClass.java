package tva.kastel.kit.core.io.reader.cpp.adjust;

import tva.kastel.kit.core.model.impl.AttributeImpl;
import tva.kastel.kit.core.model.impl.StringValueImpl;
import tva.kastel.kit.core.model.interfaces.Node;


/**
 * This class is a sub class of TreeAdjuster.
 * It adjust everything that has to do with super classes.
 * It is initially called by AdjustAll.
 *
 * @author David Bumm
 */
public class AdjustSuperClass extends TreeAdjuster {

    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.SUPER)) {
            if (parent.getParent().getNodeType().equals(Const.CLASS)) {
                parent.getParent().addAttribute(new AttributeImpl(Const.SUPER_CLASS, new StringValueImpl(node.getValueAt(0))));
            }
            if (!node.getChildren().isEmpty() && !node.getChildren().get(0).getAttributes().isEmpty()) {
                parent.getParent().addAttribute(new AttributeImpl(Const.ACCESS_MODIFIER, new StringValueImpl(node.getChildren().get(0).getValueAt(0))));
            }
            parent.cut();
        }

    }

}

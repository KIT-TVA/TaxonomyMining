package main.java.core.io.reader.cpp.adjust;

import main.java.core.model.impl.AttributeImpl;
import main.java.core.model.impl.StringValueImpl;
import main.java.core.model.interfaces.Node;


/**
 * This class is a sub class of TreeAdjuster.
 * It adjust everything that has to do with super classes.
 * It is initially called by AdjustAll.
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

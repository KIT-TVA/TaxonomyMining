package tva.kastel.kit.core.io.reader.cpp.adjust;

import tva.kastel.kit.core.model.impl.AttributeImpl;
import tva.kastel.kit.core.model.impl.NodeImpl;
import tva.kastel.kit.core.model.impl.StringValueImpl;
import tva.kastel.kit.core.model.interfaces.Node;

/**
 * This class is a sub class of TreeAdjuster.
 * It adjust everything that has to do with comments.
 * It is initially called by AdjustAll.
 *
 * @author David Bumm
 */
public class AdjustComment extends TreeAdjuster {

    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.LINE_COMMENT)) {
            String value = node.getValueAt(0);
            // remove "//" and potential space at the beginning of the Comment
            if (value == null) {
                node.cut();
                return;
            }
            String[] arr = value.split(Const.SLASH_TWICE);
            for (String s : arr) {
                value = s;
            }
            node.getAttributes().clear();
            node.addAttribute(new AttributeImpl(Const.COMMENT_BIG, new StringValueImpl(value)));

        }
    }
}

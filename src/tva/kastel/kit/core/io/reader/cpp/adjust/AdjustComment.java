package tva.kastel.kit.core.io.reader.cpp.adjust;

import tva.kastel.kit.core.model.impl.AttributeImpl;
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
            try {
                node.cut();
            } catch (ArrayIndexOutOfBoundsException e) {
                return; // node already has been cut
            }
            String[] arr = value.split(Const.SLASH_TWICE);
            for (int i = 0; i < arr.length; i++) {
                value = arr[i];
            }
            parent.addAttribute(new AttributeImpl(Const.COMMENT_BIG, new StringValueImpl(value)));

        }
    }
}

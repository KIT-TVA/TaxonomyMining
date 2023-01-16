package tva.kastel.kit.core.io.reader.cpp.adjust;

import tva.kastel.kit.core.model.impl.AttributeImpl;
import tva.kastel.kit.core.model.impl.StringValueImpl;
import tva.kastel.kit.core.model.interfaces.Attribute;
import tva.kastel.kit.core.model.interfaces.Node;


/**
 * This class is a sub class of TreeAdjuster.
 * It adjust everything that has to do with Specifier Node. It removes the Node and gives the parent the information.
 * It is initially called by AdjustAll.
 *
 * @author David Bumm
 */
public class AdjustSpecifier extends TreeAdjuster {

    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.SPECIFIER) && !parent.getAttributes().isEmpty()) {
            Attribute type = null;
            for (Attribute a : parent.getAttributes()) {
                if (a.getAttributeKey().equals(Const.TYPE_BIG) || a.getAttributeKey().equals(Const.RETURN_TYPE)) {
                    type = a;
                }
            }
            if (type != null) {
                type.getAttributeValues().get(0).setValue(node.getValueAt(0) + " " +
                        type.getAttributeValues().get(0).getValue().toString());
            } else {
                parent.addAttribute(new AttributeImpl(Const.TYPE_BIG, new StringValueImpl(node.getValueAt(0))));
            }
            node.cutWithoutChildren();
        }

    }

}

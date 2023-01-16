package tva.kastel.kit.core.io.reader.cpp.adjust;

import tva.kastel.kit.core.model.interfaces.Attribute;
import tva.kastel.kit.core.model.interfaces.Node;

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
				node.cutWithoutChildren();
			}
		}

	}

}

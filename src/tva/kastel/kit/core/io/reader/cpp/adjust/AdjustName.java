package tva.kastel.kit.core.io.reader.cpp.adjust;

import tva.kastel.kit.core.model.interfaces.Node;

public class AdjustName extends TreeAdjuster {

	@Override
	protected void adjust(Node node, Node parent, String nodeType) {
		if (nodeType.equals(Const.NAME_BIG)) {
			if (keep(node)) {
				node.setNodeType(Const.NAME_EXPR);
			} else {
				node.cutWithoutChildren();
			}
		}

		if (nodeType.equals(Const.TYPE_SMALL)) {
			node.cutWithoutChildren();
		}

		if (nodeType.equals(Const.MODIFIER) && node.getAttributes().isEmpty()) {
			node.cutWithoutChildren();
		}

	}

	private boolean keep(Node node) {
		String parentType = node.getParent().getNodeType();
		return parentType.equals(Const.EXPR) || parentType.equals(Const.ARGUMENT_BIG);
	}

}

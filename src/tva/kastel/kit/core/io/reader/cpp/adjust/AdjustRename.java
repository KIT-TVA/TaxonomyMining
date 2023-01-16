package tva.kastel.kit.core.io.reader.cpp.adjust;

import tva.kastel.kit.core.model.interfaces.Node;

/**
 * This class renames the rest of the NodeTypes to their camel-case equivalent
 *
 * @author David Bumm
 */
public class AdjustRename extends TreeAdjuster {

	@Override
	protected void adjust(Node node, Node parent, String nodeType) {
		String newName = nodeType.substring(0, 1).toUpperCase() + nodeType.substring(1);
		node.setNodeType(newName);

	}

}

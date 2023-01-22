package tva.kastel.kit.core.io.reader.python.python_adjust;


import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.interfaces.Node;

public class PAdjustNodes extends TreeAdjuster {

	@Override
	protected void adjust(Node node, Node parent, String nodeType) {
		if (Const.BANED_NODES.contains(nodeType)) {
			node.cutWithoutChildren();
		}

	}

}

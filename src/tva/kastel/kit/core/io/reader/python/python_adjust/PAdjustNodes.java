package tva.kastel.kit.core.io.reader.python.python_adjust;


import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.interfaces.Node;

/**
 * This class is a subclass of TreeAdjuster.
 * It removes all nodes that we don't want to show.
 * It is initially called by AdjustAll.
 *
 * @author David Bumm
 */
public class PAdjustNodes extends TreeAdjuster {

	@Override
	protected void adjust(Node node, Node parent, String nodeType) {
		if (Const.BANED_NODES.contains(nodeType)) {
			node.cutWithoutChildren();
		}

	}

}

package tva.kastel.kit.core.io.reader.python.python_adjust;


import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.interfaces.Node;

public class PAdjustArgument extends TreeAdjuster {

	@Override
	protected void adjust(Node node, Node parent, String nodeType) {
		if (nodeType.equals("Args") && !node.getChildren().isEmpty()) {
			if (node.getChildren().get(0).getNodeType().equals("Arguments")) {
				node.cutWithoutChildren();
			}
		} else if (nodeType.equals("Args") || nodeType.equals("Arguments")) {
			node.setNodeType(Const.ARGUMENT_BIG);
		}

	}

}

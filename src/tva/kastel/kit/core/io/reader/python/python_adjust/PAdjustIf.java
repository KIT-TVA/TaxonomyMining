package tva.kastel.kit.core.io.reader.python.python_adjust;


import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.interfaces.Node;

public class PAdjustIf extends TreeAdjuster {

	@Override
	protected void adjust(Node node, Node parent, String nodeType) {
		if (nodeType.equals(Const.IF_STMT_BIG)) {
			for (Node child: node.getChildren()) {
				if (child.getNodeType().equals(Const.BODY)) {
					child.setNodeType(Const.THEN_BIG);
				} else if (child.getNodeType().equals("Test")) {
					child.setNodeType(Const.CONDITION_BIG);
				}
			}
		}
		
	}

}

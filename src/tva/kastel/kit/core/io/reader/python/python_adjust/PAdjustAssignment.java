package tva.kastel.kit.core.io.reader.python.python_adjust;


import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.impl.NodeImpl;
import tva.kastel.kit.core.model.interfaces.Node;

/**
 * This class is a subclass of TreeAdjuster.
 * It adjusts all nodes that have something to do with value assignments.
 * It is initially called by AdjustAll.
 *
 * @author David Bumm
 */
public class PAdjustAssignment extends TreeAdjuster {

	@Override
	protected void adjust(Node node, Node parent, String nodeType) {
		if (nodeType.equals(Const.ASSIGNMENT)) {
			Node variableExpr = new NodeImpl(Const.VARIABLE_DECL_EXPR);
			Node variableDecl = new NodeImpl(Const.VARIABLE_DECL, variableExpr);
			String name = "";
			
			for (Node child: node.getChildren()) {
				if (child.getNodeType().equals("Targets") && !child.getChildren().isEmpty()) {
					name = child.getChildren().get(0).getValueAt(0);
				} else if (child.getNodeType().equals("Value") && !child.getChildren().isEmpty()) {
					variableDecl.addChildWithParent(child.getChildren().get(0));
				}
			}
			variableDecl.addAttribute(Const.NAME_BIG, name);
			parent.addChild(variableExpr, node.cut());
		}

	}

}

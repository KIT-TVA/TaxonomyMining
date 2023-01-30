package tva.kastel.kit.core.io.reader.python.python_adjust;


import tva.kastel.kit.core.io.reader.cpp.adjust.AdjustRename;
import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.interfaces.Node;

public class PAdjustAll extends TreeAdjuster {

	
	public void adjustAll(Node rootNode) {
		
		removeAndRenameNodes(rootNode);
		
		TreeAdjuster importAdjuster = new PAdjustImports();
		importAdjuster.recursiveAdjust(rootNode);

		TreeAdjuster commentAdjuster = new PAdjustComment();
		commentAdjuster.recursiveAdjust(rootNode);
		
		TreeAdjuster ifAdjuster = new PAdjustIf();
		ifAdjuster.recursiveAdjust(rootNode);
		
		TreeAdjuster argumentAdjuster = new PAdjustArgument();
		argumentAdjuster.recursiveAdjust(rootNode);
		
		TreeAdjuster assignmentAdjuster = new PAdjustAssignment();
		assignmentAdjuster.recursiveAdjust(rootNode);

		TreeAdjuster methodAdjuster = new PAdjustMethod();
		methodAdjuster.recursiveAdjust(rootNode);

		TreeAdjuster methodCallAdjuster = new PAdjustMethodCall();
		methodCallAdjuster.recursiveAdjust(rootNode);
		

	}
	
	@Override
	protected void adjust(Node node, Node parent, String nodeType) {
		RenamerPython.getInstance().renameNode(node);
		if (nodeType.equals(Const.BODY) && parent.getNodeType().equals(Const.C_UNIT)) {
			node.cutWithoutChildren();
		}
	}

	
	private void removeAndRenameNodes(Node rootNode) {
		TreeAdjuster renameAdjuster = new AdjustRename();
		renameAdjuster.recursiveAdjust(rootNode);
		
		recursiveAdjust(rootNode);

		TreeAdjuster nodeAdjuster = new PAdjustNodes();
		nodeAdjuster.recursiveAdjust(rootNode);
	}
}

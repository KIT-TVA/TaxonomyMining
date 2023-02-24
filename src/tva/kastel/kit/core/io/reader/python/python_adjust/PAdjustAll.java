package tva.kastel.kit.core.io.reader.python.python_adjust;


import tva.kastel.kit.core.io.reader.cpp.adjust.AdjustRename;
import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.interfaces.Node;


/**
 * This class is the entry point for adjusting a tree.
 * It adjusts the root node and calls all existing TreeAdjuster to make the tree as similar as possible to
 * a tree generated from Java source code. This class is made for a python generated AST.
 *
 * @author David Bumm
 */
public class PAdjustAll extends TreeAdjuster {


	/**
	 * This method creates all TreeAdjuster for Python files and calls recursiveAdjust().
	 * So the entire tree provided will be adjusted in various ways to be more similar to other programmaning languages.
	 * This method is not thread safe since the TreeAdjuster depend on each other.
	 * Some adjust() calls can be reordered without causing problems but this is not recommended.
	 *
	 * @param rootNode the rootNode that will be adjusted
	 */
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

		TreeAdjuster binaryAdjuster = new PAdjustBinaryExpr();
		binaryAdjuster.recursiveAdjust(rootNode);

		TreeAdjuster unaryAdjuster = new PAdjustUnary();
		unaryAdjuster.recursiveAdjust(rootNode);

		TreeAdjuster compareAdjuster = new PAdjustCompare();
		compareAdjuster.recursiveAdjust(rootNode);

		TreeAdjuster returnAdjuster = new PAdjustReturn();
		returnAdjuster.recursiveAdjust(rootNode);

		TreeAdjuster constantAdjuster = new PAdjustConstants();
		constantAdjuster.recursiveAdjust(rootNode);

		TreeAdjuster exceptionAdjuster = new PAdjustException();
		exceptionAdjuster.recursiveAdjust(rootNode);

		TreeAdjuster arrayAdjuster = new PAdjustArray();
		arrayAdjuster.recursiveAdjust(rootNode);

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

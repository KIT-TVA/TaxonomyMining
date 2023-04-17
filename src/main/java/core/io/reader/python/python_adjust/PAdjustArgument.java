package main.java.core.io.reader.python.python_adjust;


import main.java.core.io.reader.cpp.adjust.Const;
import main.java.core.io.reader.cpp.adjust.TreeAdjuster;
import main.java.core.model.interfaces.Node;

public class PAdjustArgument extends TreeAdjuster {

	@Override
	protected void adjust(Node node, Node parent, String nodeType) {
		if (nodeType.equals(Const.ARGUMENT_BIG) && !node.getAttributes().isEmpty()) {
			node.getAttributes().get(0).setAttributeKey(Const.NAME_BIG);
		} else if (nodeType.equals(Const.NAME_EXPR) && parent.getNodeType().equals(Const.ARGUMENT_BIG) && !node.getAttributes().isEmpty()) {
			String type = node.getValueAt(0);
			parent.addAttribute(Const.TYPE_BIG, type);
			node.cutWithoutChildren();
		} else if (nodeType.equals(Const.ARGS) && (parent.getNodeType().equals(Const.M_DECL) || parent.getNodeType().equals(Const.ARGUMENT_BIG))) {
			node.cutWithoutChildren();
		}

	}

}

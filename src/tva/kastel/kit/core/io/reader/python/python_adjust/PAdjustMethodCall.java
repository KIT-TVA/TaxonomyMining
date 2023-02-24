package tva.kastel.kit.core.io.reader.python.python_adjust;

import java.util.ArrayList;
import java.util.List;

import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.impl.NodeImpl;
import tva.kastel.kit.core.model.interfaces.Node;


/**
 * This class is a subclass of TreeAdjuster.
 * It adjusts all nodes that have something to do with method call expressions.
 * It is initially called by AdjustAll.
 *
 * @author David Bumm
 */
public class PAdjustMethodCall extends TreeAdjuster {
    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.ARGS)) {
            node.setNodeType(Const.ARGUMENT_BIG);
            List<Node> allArguments = new ArrayList<>();

            for (Node child : node.getChildren()) {
                Node argument = new NodeImpl(Const.ARGUMENT_BIG);
                argument.setParent(node);
                Node n = child.cloneNode();
                n.updateParent(argument);
                allArguments.add(argument);
            }
            node.getChildren().removeAll(node.getChildren());
            node.getChildren().addAll(allArguments);

        }
        if (nodeType.equals(Const.NAME_EXPR) && !node.getAttributes().isEmpty() && parent.getNodeType().equals(Const.FUNC) && parent.getParent().getNodeType().equals(Const.CALL_BIG)) {
            parent.getParent().setNodeType(Const.METHOD_CALL);
            parent.getParent().addAttribute(Const.NAME_BIG, node.getValueAt(0));
            parent.cut();
        }
        if (nodeType.equals(Const.EXPR_BIG) && node.getChildren().size() == 1 && node.getChildren().get(0).getChildren().size() == 1) {
            node.getChildren().get(0).cutWithoutChildren();
            node.cutWithoutChildren();
        }
    }
}

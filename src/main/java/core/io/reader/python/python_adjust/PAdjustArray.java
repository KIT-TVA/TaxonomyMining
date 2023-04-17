package main.java.core.io.reader.python.python_adjust;

import main.java.core.io.reader.cpp.adjust.Const;
import main.java.core.io.reader.cpp.adjust.TreeAdjuster;
import main.java.core.model.interfaces.Node;
import main.java.core.model.impl.NodeImpl;


/**
 * This class is a subclass of TreeAdjuster. It adjusts everything that has to
 * do with arrays. This class covers normal array accesses (e.g arr[i]) just
 * like expressions inside array accesses (e.g arr(i * j + 5)). It is initially called by
 * PAdjustAll.
 */
public class PAdjustArray extends TreeAdjuster {
    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.SUBSCRIPT)) {
            Node arrayAccess = new NodeImpl(Const.ARR_ACCESS_EXPR, parent);
            String array = Const.EMPTY;
            Node access = null;
            for (Node child : node.getChildren()) {
                if (child.getNodeType().equals(Const.SLICE) && !child.getChildren().isEmpty()) {
                    Node accessNode = child.getChildren().get(0);
                    access = accessNode.cloneNode();
                } else if (child.getNodeType().equals(Const.VALUE) && !child.getChildren().isEmpty()) {
                    array = child.getChildren().get(0).getValueAt(0);
                }
            }
            arrayAccess.addAttribute(Const.VALUE, array);
            arrayAccess.addChildWithParent(access);
            node.cut();
        }
    }

}

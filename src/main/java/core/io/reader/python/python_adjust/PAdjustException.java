package main.java.core.io.reader.python.python_adjust;

import main.java.core.io.reader.cpp.adjust.Const;
import main.java.core.io.reader.cpp.adjust.TreeAdjuster;
import main.java.core.model.interfaces.Node;

/**
 * This class is a subclass of TreeAdjuster.
 * It adjusts all nodes that have something to do with exceptions.
 * It is initially called by AdjustAll.
 */
public class PAdjustException extends TreeAdjuster {
    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.METHOD_CALL) && parent.getNodeType().equals(Const.EXC)) {
            node.setNodeType(Const.EXCEPTION);
            parent.cutWithoutChildren();
        }
    }
}

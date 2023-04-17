package main.java.core.io.reader.python.python_adjust;


import main.java.core.io.reader.cpp.adjust.Const;
import main.java.core.io.reader.cpp.adjust.TreeAdjuster;
import main.java.core.model.interfaces.Node;

/**
 * This class is a subclass of TreeAdjuster.
 * It removes all nodes that we don't want to show.
 * It is initially called by AdjustAll.
 */
public class PAdjustNodes extends TreeAdjuster {

    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (Const.BANED_NODES.contains(nodeType)) {
            node.cutWithoutChildren();
        }

    }

}

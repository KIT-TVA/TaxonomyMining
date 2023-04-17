package main.java.core.io.reader.python.python_adjust;

import main.java.core.io.reader.cpp.adjust.Const;
import main.java.core.io.reader.cpp.adjust.TreeAdjuster;
import main.java.core.model.interfaces.Node;


/**
 * This class is a subclass of TreeAdjuster.
 * It adjusts all nodes that have something to do with return-statements.
 * It is initially called by AdjustAll.
 */
public class PAdjustReturn extends TreeAdjuster {
    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.VALUE) && parent.getNodeType().equals(Const.RETURN_STMT)) {
            node.cutWithoutChildren();
        }
    }
}

package main.java.core.io.reader.python.python_adjust;

import main.java.core.io.reader.cpp.adjust.Const;
import main.java.core.io.reader.cpp.adjust.TreeAdjuster;
import main.java.core.model.interfaces.Node;

public class PAdjustConstants extends TreeAdjuster {
    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.CONSTANT) && !node.getAttributes().isEmpty()) {
            String value = node.getValueAt(0);
            adjustLiteralNode(node, value);
        }
    }
}

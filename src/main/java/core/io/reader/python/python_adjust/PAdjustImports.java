package main.java.core.io.reader.python.python_adjust;

import main.java.core.io.reader.cpp.adjust.Const;
import main.java.core.io.reader.cpp.adjust.TreeAdjuster;
import main.java.core.model.interfaces.Node;
import main.java.core.model.impl.NodeImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a subclass of TreeAdjuster.
 * It adjusts all nodes that have something to do with import statements.
 * It is initially called by AdjustAll.
 */
public class PAdjustImports extends TreeAdjuster {

    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.C_UNIT)) {
            boolean hasImports = false;
            List<Node> oldImportNodes = new ArrayList<>();
            for (Node child : node.getChildren()) {
                if (child.getNodeType().equals(Const.IMPORT) || child.getNodeType().equals(Const.IMPORT_FROM)) {
                    hasImports = true;
                    oldImportNodes.add(child);

                }
            }
            if (hasImports) {
                Node imports = new NodeImpl(Const.IMPORTS);
                imports.setParent(node);
                node.addChild(imports, 0);
                for (Node oldImport : oldImportNodes) {
                    oldImport.setNodeType(Const.IMPORT);
                    String value = oldImport.getChildren().get(0).getChildren().get(0).getValueAt(0);
                    oldImport.addAttribute(Const.NAME_BIG, value);
                    oldImport.getChildren().clear();
                    oldImport.updateParent(imports);
                }
            }
        }

    }

}

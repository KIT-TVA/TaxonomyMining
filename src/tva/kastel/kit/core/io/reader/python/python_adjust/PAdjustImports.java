package tva.kastel.kit.core.io.reader.python.python_adjust;

import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.impl.NodeImpl;
import tva.kastel.kit.core.model.interfaces.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a subclass of TreeAdjuster.
 * It adjusts all nodes that have something to do with import statements.
 * It is initially called by AdjustAll.
 *
 * @author David Bumm
 */
public class PAdjustImports extends TreeAdjuster {

	@Override
	protected void adjust(Node node, Node parent, String nodeType) {
		if (nodeType.equals(Const.C_UNIT)) {
			boolean hasImports = false;
			List<Node> oldImportNodes = new ArrayList<>();
			for (Node child: node.getChildren()) {
				if (child.getNodeType().equals("Import") || child.getNodeType().equals("ImportFrom")) {
					hasImports = true;
					oldImportNodes.add(child);
					
				}
			}
			if (hasImports) {
				Node imports = new NodeImpl("Imports");
				imports.setParent(node);
				node.addChild(imports, 0);
				for (Node oldImport: oldImportNodes) {
					oldImport.setNodeType("Import");
					String value = oldImport.getChildren().get(0).getChildren().get(0).getValueAt(0);
					oldImport.addAttribute(Const.NAME_BIG, value);
					oldImport.getChildren().clear();
					oldImport.updateParent(imports);
				}
			}
		}

	}

}

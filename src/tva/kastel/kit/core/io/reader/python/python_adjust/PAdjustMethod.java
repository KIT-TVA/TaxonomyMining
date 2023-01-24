package tva.kastel.kit.core.io.reader.python.python_adjust;

import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.interfaces.Node;

public class PAdjustMethod extends TreeAdjuster {
    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        addReturnType(node, parent, nodeType);



    }



    private void addReturnType(Node node, Node parent, String nodeType) {
        if (nodeType.equals(Const.M_DECL)) {
            for (Node child: node.getChildren()) {
                if (child.getNodeType().equals(Const.RETURNS)) {
                    String type = child.getChildren().get(0).getValueAt(0);
                    node.addAttribute(Const.RETURN_TYPE, type);
                    child.cut();
                    return;
                }
            }
            node.addAttribute(Const.RETURN_TYPE, Const.VOID);
        }
    }
}

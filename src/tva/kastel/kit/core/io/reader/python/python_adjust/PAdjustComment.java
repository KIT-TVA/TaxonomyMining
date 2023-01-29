package tva.kastel.kit.core.io.reader.python.python_adjust;

import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.cpp.adjust.TreeAdjuster;
import tva.kastel.kit.core.model.interfaces.Node;

public class PAdjustComment extends TreeAdjuster {
    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals("Expr") && node.getChildren().size() == 1) {
            Node valueNode = node.getChildren().get(0);
            if (valueNode.getNodeType().equals("Value") && valueNode.getChildren().size() == 1) {
                Node comment = valueNode.getChildren().get(0);
                if (!comment.getAttributes().isEmpty()) {

                    if (parent != null) {
                        String value = comment.getValueAt(0);
                        value = value.replace("\\n", "ö");
                        String[] allComments = value.split("ö");

                        for (int i = 0; i < allComments.length; i++) {
                            if (!allComments[i].trim().equals(Const.EMPTY)) {
                                parent.addAttribute(Const.COMMENT_BIG, allComments[i]);
                            }
                        }

                        comment.cutWithoutChildren();
                    } else {
                        comment.setNodeType(Const.LINE_COMMENT);
                    }


                    node.cutWithoutChildren();
                    valueNode.cutWithoutChildren();
                }
            }
        }
    }
}

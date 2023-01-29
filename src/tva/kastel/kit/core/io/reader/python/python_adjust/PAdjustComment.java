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
                        StringBuilder value = new StringBuilder(comment.getValueAt(0));
                        value = new StringBuilder(value.toString().replace("\\n", "ö"));
                        String[] allComments = value.toString().split("ö");
                        value = new StringBuilder();
                        for (String allComment : allComments) {
                            if (!allComment.trim().equals(Const.EMPTY)) {
                                value.append(allComment).append(Const.LINE_BREAK);
                            }
                        }
                        parent.addAttribute(Const.COMMENT_BIG, value.substring(0, value.length() - 1));
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

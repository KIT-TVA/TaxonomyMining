package main.java.core.io.reader.python.python_adjust;

import main.java.core.io.reader.cpp.adjust.Const;
import main.java.core.io.reader.cpp.adjust.TreeAdjuster;
import main.java.core.model.interfaces.Node;

public class PAdjustComment extends TreeAdjuster {
    @Override
    protected void adjust(Node node, Node parent, String nodeType) {
        if (nodeType.equals("Expr") && node.getChildren().size() == 1) {
            Node valueNode = node.getChildren().get(0);
            if (valueNode.getNodeType().equals(Const.VALUE) && valueNode.getChildren().size() == 1) {
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
                        comment.getAttributes().clear();
                        comment.setNodeType(Const.LINE_COMMENT);
                        comment.addAttribute(Const.COMMENT_BIG, value.substring(0, value.length() - 1));
                        parent.addChildWithParent(comment,  node.cut());
                    }
                }
            }
        }
    }
}

package main.java.core.io.reader.java.templates;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.PatternExpr;
import com.github.javaparser.ast.modules.*;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.visitor.VoidVisitor;
import main.java.core.io.reader.cpp.adjust.Const;
import main.java.core.io.reader.java.JavaAttributesTypes;
import main.java.core.io.reader.java.JavaNodeTypes;
import main.java.core.model.enums.NodeType;
import main.java.core.model.impl.NodeImpl;
import main.java.core.model.impl.StringValueImpl;
import main.java.core.model.interfaces.Node;

import java.util.*;

public abstract class AbstractJavaVisitor implements VoidVisitor<Node> {

    /**
     * Visits all children but specified exceptions of the node n.
     *
     * @param n          JavaParser Node
     * @param arg        e4cf Parent Node
     * @param exceptions Nodes that should not be visited
     */
    protected synchronized void visitor(com.github.javaparser.ast.Node n, Node arg, com.github.javaparser.ast.Node... exceptions) {
        //setting the start and endline of the artifact
        arg.setStartLine(n.getRange().get().begin.line);
        arg.setEndLine(n.getRange().get().end.line);

        // add non-orphan nodes as a LineComment
        n.getComment().ifPresent(comment -> addComment(arg, comment.getContent()));

        List<com.github.javaparser.ast.Node> children = new ArrayList<>();
        if (!n.getOrphanComments().isEmpty() && !n.toString().equals("Stack")) {
            children = getReorderedNodes(n);
        } else {
            children = n.getChildNodes();
        }


        NodeList<com.github.javaparser.ast.Node> exceptionList = NodeList.nodeList(exceptions);
        for (com.github.javaparser.ast.Node childNode : children) {
            if (!exceptionList.contains(childNode)) {
                childNode.accept(this, arg);
            }
        }
    }

    private List<com.github.javaparser.ast.Node> getReorderedNodes(com.github.javaparser.ast.Node parent) {

        List<com.github.javaparser.ast.Node> list = new ArrayList<>();
        list.addAll(parent.getChildNodes());
        Collections.sort(list, new Comparator<>() {
            @Override
            public int compare(com.github.javaparser.ast.Node o1, com.github.javaparser.ast.Node o2) {
                if (o1.getRange().get().begin.line > o2.getRange().get().begin.line) {
                    return 1;
                } else if (o1.getRange().get().begin.line < o2.getRange().get().begin.line) {
                    return -1;
                } else {
                    return Integer.compare(o1.getRange().get().begin.column, o2.getRange().get().begin.column);
                }


            }
        });
        return list;
    }


    private void addComment(Node node, String comment) {
        Node lineComment = new NodeImpl(Const.LINE_COMMENT);
        lineComment.addAttribute(Const.COMMENT_BIG, comment);

        int position = 0;
        Node parent = node;
        while (!parent.getNodeType().equals(Const.BODY) && !parent.getNodeType().equals(Const.C_UNIT)) {
            if (parent.getParent() == null) {
                parent = node;
                break;
            }
            position = parent.getPosition();
            parent = parent.getParent();
        }

        if (parent.equals(node)) {
            parent.addChildWithParent(lineComment);
        } else {
            parent.addChild(lineComment, position);
        }


    }


    @Override
    public void visit(NodeList n, Node arg) {
        // TODO Auto-generated method stub
    }

    @Override
    public void visit(ExpressionStmt n, Node arg) {
        visitor(n, arg);
    }

    @Override
    public void visit(LabeledStmt n, Node arg) {
        Node labeledStmt = new NodeImpl(NodeType.LABELED_STATEMENT, JavaNodeTypes.LabeledStmt.name(), arg);
        labeledStmt.addAttribute(JavaAttributesTypes.Name.name(), new StringValueImpl(n.getLabel().asString()));
        visitor(n, labeledStmt);
    }

    @Override
    public void visit(ModuleDeclaration n, Node arg) {
        visitor(n, arg);
    }

    @Override
    public void visit(ModuleRequiresDirective n, Node arg) {
        visitor(n, arg);
    }

    @Override
    public void visit(ModuleExportsDirective n, Node arg) {
        visitor(n, arg);
    }

    @Override
    public void visit(ModuleProvidesDirective n, Node arg) {
        visitor(n, arg);
    }

    @Override
    public void visit(ModuleUsesDirective n, Node arg) {
        visitor(n, arg);
    }

    @Override
    public void visit(ModuleOpensDirective n, Node arg) {
        visitor(n, arg);
    }

    @Override
    public void visit(PatternExpr n, Node arg) {
        visitor(n, arg);
    }
}

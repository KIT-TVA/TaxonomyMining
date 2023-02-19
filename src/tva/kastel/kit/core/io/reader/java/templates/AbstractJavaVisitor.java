package tva.kastel.kit.core.io.reader.java.templates;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.PatternExpr;
import com.github.javaparser.ast.modules.*;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.visitor.VoidVisitor;
import tva.kastel.kit.core.io.reader.cpp.adjust.Const;
import tva.kastel.kit.core.io.reader.java.JavaAttributesTypes;
import tva.kastel.kit.core.io.reader.java.JavaNodeTypes;
import tva.kastel.kit.core.model.enums.NodeType;
import tva.kastel.kit.core.model.impl.NodeImpl;
import tva.kastel.kit.core.model.impl.StringValueImpl;
import tva.kastel.kit.core.model.interfaces.Node;

import java.util.ArrayList;

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
   //     n.getComment().ifPresent(comment -> addComment(arg, comment.getContent()));


        NodeList<com.github.javaparser.ast.Node> exceptionList = NodeList.nodeList(exceptions);
        for (com.github.javaparser.ast.Node childNode : n.getChildNodes()) {
            if (n.getOrphanComments().contains(childNode)) {
                continue; //don't handle comments twice
            }
            if (!exceptionList.contains(childNode)) {
                if (childNode.toString().startsWith(Const.SLASH_TWICE)) { //Line Comments are handled here
                    String[] arr = childNode.toString().split("\r\n");
                    String comment = arr[0].substring(2); //comment without leading '//'
                    addComment(arg, comment);
                }
                childNode.accept(this, arg);
            }
        }
    }

    private void addComment(Node parent, String comment) {
        Node lineComment = new NodeImpl(Const.LINE_COMMENT, parent);
        lineComment.addAttribute(Const.COMMENT_BIG, comment);
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

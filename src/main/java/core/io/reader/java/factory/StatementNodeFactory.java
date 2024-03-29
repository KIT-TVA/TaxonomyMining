package main.java.core.io.reader.java.factory;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import main.java.core.io.reader.java.JavaAttributesTypes;
import main.java.core.io.reader.java.JavaNodeTypes;
import main.java.core.io.reader.java.JavaVisitor;
import main.java.core.model.interfaces.Node;
import main.java.core.model.enums.NodeType;
import main.java.core.model.impl.NodeImpl;
import main.java.core.model.impl.StringValueImpl;

public class StatementNodeFactory implements IStatementNodeFactory {

    @Override
    public Node createStatementNode(Statement stmt, Node parent, JavaVisitor visitor) {
        if (stmt instanceof ForStmt) {
            return createForStmt((ForStmt) stmt, parent, visitor);
        }
        if (stmt instanceof IfStmt) {
            return createIfStmtNode((IfStmt) stmt, parent, visitor);
        }
        if (stmt instanceof ForEachStmt) {
            return createForEachStmtNode((ForEachStmt) stmt, parent, visitor);
        }
        return null;
    }

    /**
     * This method creates a Node for a ForEachStmt statement.
     */
    private Node createForEachStmtNode(ForEachStmt stmt, Node parent, JavaVisitor visitor) {
        Node p = new NodeImpl(NodeType.LOOP_COLLECTION_CONTROLLED, JavaNodeTypes.ForEachStmt.name(), parent);
        // Add Iterator as attribute
        p.addAttribute(JavaAttributesTypes.Iterator.name(), new StringValueImpl(stmt.getIterable().toString()));

        // Initializations
        Node initializationContainer = new NodeImpl(NodeType.INITIALIZATION, JavaNodeTypes.Initialization.name(), p);
        stmt.getVariable().accept(visitor, initializationContainer); // visited node excluded in JavaVisitor::visit(ForStmt,...)

        return p;
    }

    /**
     * This method creates a Node for a ForStmt statement.
     */
    private Node createForStmt(ForStmt forStmt, Node parent, JavaVisitor visitor) {
        Node forStmtNode = new NodeImpl(NodeType.LOOP_COUNT_CONTROLLED, JavaNodeTypes.ForStmt.name(), parent);

        // Initializations
        Node initializationContainer = new NodeImpl(NodeType.INITIALIZATION, JavaNodeTypes.Initialization.name(), forStmtNode);
        for (Expression initializtaionExpr : forStmt.getInitialization()) {
            initializtaionExpr.accept(visitor, initializationContainer);
        }
        forStmt.getInitialization().clear();


        // Comparison
        if (forStmt.getCompare().isPresent()) {
            Expression compareExpr = forStmt.getCompare().get();
            Node conditionNode = new NodeImpl(NodeType.CONDITION, JavaNodeTypes.Condition.name(), forStmtNode);
            compareExpr.accept(visitor, conditionNode);
            forStmt.removeCompare();
        }

        // Updates
        Node updateContainer = new NodeImpl(NodeType.UPDATE, JavaNodeTypes.Update.name(), forStmtNode);
        for (Expression updateExpr : forStmt.getUpdate()) {
            updateExpr.accept(visitor, updateContainer);
        }
        forStmt.getUpdate().clear();


        return forStmtNode;
    }

    /**
     * This method creates a Node for a IfStmt statement.
     */
    private Node createIfStmtNode(IfStmt ifStmt, Node arg, JavaVisitor visitor) {
        Node ifStmtNode = new NodeImpl(NodeType.IF, JavaNodeTypes.IfStmt.name(), arg);
        Node conditionNode = new NodeImpl(NodeType.CONDITION, JavaNodeTypes.Condition.name(), ifStmtNode);
        ifStmt.getCondition().accept(visitor, conditionNode);

        // Fall through
        Statement thenStmt = ifStmt.getThenStmt();
        Node thenNode = new NodeImpl(NodeType.THEN, JavaNodeTypes.Then.name(), ifStmtNode);
        thenStmt.accept(visitor, thenNode);

        // Else
        if (ifStmt.getElseStmt().isPresent()) {
            Statement elseStmt = ifStmt.getElseStmt().get();
            Node elseNode = new NodeImpl(NodeType.ELSE, JavaNodeTypes.Else.name(), ifStmtNode);
            if (elseStmt instanceof IfStmt) {
                createIfStmtNode((IfStmt) elseStmt, elseNode, visitor);
            } else {
                elseStmt.accept(visitor, elseNode);
            }
        }
        return ifStmtNode;
    }

}

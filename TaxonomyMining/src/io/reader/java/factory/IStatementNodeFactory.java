package io.reader.java.factory;

import com.github.javaparser.ast.stmt.Statement;

import model.interfaces.Node;
import io.reader.java.JavaVisitor;

public interface IStatementNodeFactory {
    
    public Node createStatementNode(Statement stmt, Node parent, JavaVisitor visitor);
}

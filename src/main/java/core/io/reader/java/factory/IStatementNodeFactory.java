package main.java.core.io.reader.java.factory;

import com.github.javaparser.ast.stmt.Statement;
import main.java.core.io.reader.java.JavaVisitor;
import main.java.core.model.interfaces.Node;

public interface IStatementNodeFactory {

    public Node createStatementNode(Statement stmt, Node parent, JavaVisitor visitor);
}

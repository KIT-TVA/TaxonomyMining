package tva.kastel.kit.core.io.reader.java.factory;

import com.github.javaparser.ast.stmt.Statement;
import tva.kastel.kit.core.io.reader.java.JavaVisitor;
import tva.kastel.kit.core.model.interfaces.Node;

public interface IStatementNodeFactory {

    public Node createStatementNode(Statement stmt, Node parent, JavaVisitor visitor);
}

package main.java.core.compare.comparison.impl;


import main.java.core.compare.comparison.interfaces.Comparison;
import main.java.core.model.interfaces.Node;

public abstract class AbstractComparisonFactory {

    public abstract Comparison<Node> create(Node leftArtifact, Node rightArtifact);

    public abstract Comparison<Node> create(Node leftArtifact, Node rightArtifact, double similarity);

    public abstract Comparison<Node> create(Node leftArtifact, Node rightArtifact, Comparison<Node> parent);

    public abstract Comparison<Node> create(Comparison<Node> source);
}

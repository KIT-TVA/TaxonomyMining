package main.java.core.compare.comparison.impl;


import main.java.core.compare.comparison.interfaces.Comparison;
import main.java.core.model.interfaces.Node;

public class VariationComparisonFactory extends AbstractComparisonFactory {

    public Comparison<Node> create(Node leftArtifact, Node rightArtifact) {
        return new VariationComparison(leftArtifact, rightArtifact);
    }

    public Comparison<Node> create(Node leftArtifact, Node rightArtifact, double similarity) {
        return new VariationComparison(leftArtifact, rightArtifact, similarity);
    }

    public Comparison<Node> create(Node leftArtifact, Node rightArtifact, Comparison<Node> parent) {
        return new VariationComparison(leftArtifact, rightArtifact, parent);
    }

    public Comparison<Node> create(Comparison<Node> source) {
        return new VariationComparison(source);
    }

}

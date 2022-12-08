package tva.kastel.kit.core.compare.comparison.impl;


import tva.kastel.kit.core.compare.comparison.interfaces.Comparison;
import tva.kastel.kit.core.model.interfaces.Node;

public class NodeComparisonFactory extends AbstractComparisonFactory {

    public Comparison<Node> create(Node leftArtifact, Node rightArtifact) {
        return new NodeComparison(leftArtifact, rightArtifact);
    }

    public Comparison<Node> create(Node leftArtifact, Node rightArtifact, double similarity) {
        return new NodeComparison(leftArtifact, rightArtifact, similarity);
    }

    public Comparison<Node> create(Node leftArtifact, Node rightArtifact, Comparison<Node> parent) {
        return new NodeComparison(leftArtifact, rightArtifact, parent);
    }

    public Comparison<Node> create(Comparison<Node> source) {
        return new NodeComparison(source);
    }

}

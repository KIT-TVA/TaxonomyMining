package compare.comparison.impl;


import compare.comparison.interfaces.Comparison;
import model.interfaces.Node;

public class VariationComparisonFactory extends AbstractComparisonFactory {

    public Comparison<Node> create(Node leftArtifact, Node rightArtifact) {
        return new VariationComparison(leftArtifact, rightArtifact);
    }

    public Comparison<Node> create(Node leftArtifact, Node rightArtifact, float similarity) {
        return new VariationComparison(leftArtifact, rightArtifact, similarity);
    }

    public Comparison<Node> create(Node leftArtifact, Node rightArtifact, Comparison<Node> parent) {
        return new VariationComparison(leftArtifact, rightArtifact, parent);
    }

    public Comparison<Node> create(Comparison<Node> source) {
        return new VariationComparison(source);
    }

}

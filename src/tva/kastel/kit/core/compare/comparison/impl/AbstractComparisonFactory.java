package tva.kastel.kit.core.compare.comparison.impl;


import tva.kastel.kit.core.compare.comparison.interfaces.Comparison;
import tva.kastel.kit.core.model.interfaces.Node;

public abstract class AbstractComparisonFactory {

    public abstract Comparison<Node> create(Node leftArtifact, Node rightArtifact);

    public abstract Comparison<Node> create(Node leftArtifact, Node rightArtifact, double similarity);

    public abstract Comparison<Node> create(Node leftArtifact, Node rightArtifact, Comparison<Node> parent);

    public abstract Comparison<Node> create(Comparison<Node> source);
}

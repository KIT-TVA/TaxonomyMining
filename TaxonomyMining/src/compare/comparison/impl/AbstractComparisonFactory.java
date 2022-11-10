package compare.comparison.impl;


import compare.comparison.interfaces.Comparison;
import model.interfaces.Node;

public abstract class AbstractComparisonFactory {

	public abstract Comparison<Node> create(Node leftArtifact, Node rightArtifact);

	public abstract Comparison<Node> create(Node leftArtifact, Node rightArtifact, float similarity);

	public abstract Comparison<Node> create(Node leftArtifact, Node rightArtifact, Comparison<Node> parent);

	public abstract Comparison<Node> create(Comparison<Node> source);
}

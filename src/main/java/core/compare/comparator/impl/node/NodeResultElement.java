package main.java.core.compare.comparator.impl.node;

import main.java.core.compare.comparator.interfaces.Comparator;
import main.java.core.compare.comparator.templates.AbstractResultElement;
import main.java.core.model.interfaces.Node;

import java.util.Map;


public class NodeResultElement extends AbstractResultElement<Node> {

    private Map<String, Double> attributeSimilarities;

    public NodeResultElement(Comparator<Node> usedComparator, double similarity) {
        super(usedComparator, similarity);
    }

    public NodeResultElement(Comparator<Node> usedComparator, double similarity, Map<String, Double> attributeSimilarities) {
        super(usedComparator, similarity);
        this.attributeSimilarities = attributeSimilarities;
    }

    public Map<String, Double> getAttributeSimilarities() {
        return attributeSimilarities;
    }
}

package tva.kastel.kit.core.compare.comparator.impl.node;

import java.util.Map;

import tva.kastel.kit.core.compare.comparator.interfaces.Comparator;
import tva.kastel.kit.core.compare.comparator.templates.AbstractResultElement;
import tva.kastel.kit.core.model.interfaces.Node;

/**
 * A concrete class that stores the comparison information of a comparator.
 *
 * @author Kamil Rosiak
 */
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

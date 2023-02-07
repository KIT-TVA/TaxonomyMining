package tva.kastel.kit.core.compare.comparison.interfaces;

import tva.kastel.kit.core.model.interfaces.Node;

public interface Similarity {

    public double calculateSimilarity(Comparison<Node> comparison);
}

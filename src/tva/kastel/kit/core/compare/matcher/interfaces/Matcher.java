package tva.kastel.kit.core.compare.matcher.interfaces;

import java.util.List;

import tva.kastel.kit.core.compare.comparison.interfaces.Comparison;
import tva.kastel.kit.core.model.interfaces.Node;

public interface Matcher {

    /**
     * Matches all elements within the given NodeComparison recursively
     */
    public <K> Comparison<Node> calculateMatching(Comparison<Node> root);

    /**
     * This method returns a list of comparisons. This list contains the best pairs of nodes between the compared nodes.
     *
     * @param comparisons
     * @return
     */
    public <K> void calculateMatching(List<Comparison<K>> comparisons);

    /**
     * This method sorts a list of comparisons by their similarity values descending
     */
    public <K> void sortBySimilarityDesc(List<Comparison<K>> comparisons);

    /**
     * Returns the name of the matching approach
     */
    public String getMatcherName();

    /**
     * Returns the description of the corresponding matching approach.
     */
    public String getMatcherDescription();
}

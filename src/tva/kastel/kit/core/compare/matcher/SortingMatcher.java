package tva.kastel.kit.core.compare.matcher;

import tva.kastel.kit.core.compare.comparison.interfaces.Comparison;
import tva.kastel.kit.core.compare.matcher.interfaces.AbstractMatcher;
import tva.kastel.kit.core.compare.matcher.util.AbstractArtifactFactory;
import tva.kastel.kit.core.compare.matcher.util.ArtifactFactory;
import tva.kastel.kit.core.model.interfaces.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SortingMatcher extends AbstractMatcher {
    private final double THRESHOLD = 0.4f;

    private AbstractArtifactFactory factory;

    public SortingMatcher() {
        super("sortingMatcher", "description");
        factory = new ArtifactFactory();

    }

    public SortingMatcher(AbstractArtifactFactory factory) {
        super("sortingMatcher", "description");
        this.factory = factory;
    }

    @Override
    public <K> Comparison<Node> calculateMatching(Comparison<Node> root) {
        calculateMatching(root.getChildComparisons());
        return root;
    }

    @Override
    public <K> void calculateMatching(List<Comparison<K>> comparisons) {
        // first match all child comparison
        comparisons.stream().forEach(e -> {
            calculateMatching(e.getChildComparisons());
            e.updateSimilarity();
        });
        // sort by similarity
        sortBySimilarityDesc(comparisons);

        List<K> matchedArtifacts = new ArrayList<K>();
        List<Comparison<K>> removedComparisons = new ArrayList<Comparison<K>>();
        Iterator<Comparison<K>> comparisonIterator = comparisons.iterator();

        while (comparisonIterator.hasNext()) {
            Comparison<K> nextComparison = comparisonIterator.next();
            // first match mandatory and alternative container
            if (!matchedArtifacts.contains(nextComparison.getLeftArtifact())
                    && !matchedArtifacts.contains(nextComparison.getRightArtifact())
                    && nextComparison.getSimilarity() >= THRESHOLD) {
                matchedArtifacts.add(nextComparison.getLeftArtifact());
                matchedArtifacts.add(nextComparison.getRightArtifact());
            } else {
                removedComparisons.add(nextComparison);
                comparisonIterator.remove();
            }
        }

        removedComparisons.stream().forEach(e -> {
            if (e.getLeftArtifact() != null & e.getRightArtifact() != null) {
                // If both model contain only one element we have to copy a container
                // Note the original elements
                K first = e.getLeftArtifact();
                K second = e.getRightArtifact();
                if (!matchedArtifacts.contains(first) && !matchedArtifacts.contains(second)) {
                    Comparison<K> containerClone = factory.copyComparison(e);
                    containerClone.setLeftArtifact(null);
                    e.setRightArtifact(null);
                    comparisons.add(containerClone);
                    comparisons.add(e);
                    matchedArtifacts.add(first);
                    matchedArtifacts.add(second);
                } else if (!matchedArtifacts.contains(first)) {
                    matchedArtifacts.add(first);
                    e.setRightArtifact(null);
                    comparisons.add(e);
                } else if (!matchedArtifacts.contains(second)) {
                    matchedArtifacts.add(second);
                    e.setLeftArtifact(null);
                    comparisons.add(e);
                }

            } else if (e.getLeftArtifact() != null || e.getRightArtifact() != null) {
                // that are all containers that was added as optional by the compare-engine.
                comparisons.add(e);
            }
        });
    }

    /**
     * This method sorts a list of comparisons by their similarity values descending
     */
    @Override
    public <K> void sortBySimilarityDesc(List<Comparison<K>> comparisons) {
        comparisons.sort((first, second) -> {
            //Multiply with -1 to sort descending
            return -1 * Double.compare(first.getSimilarity(), second.getSimilarity());
        });
    }

}

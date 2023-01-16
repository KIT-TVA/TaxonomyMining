package tva.kastel.kit.core.compare.comparison.interfaces;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import tva.kastel.kit.core.compare.comparator.interfaces.ResultElement;
import tva.kastel.kit.core.compare.comparison.util.ComparisonUtil;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.impl.Pair;

/**
 * A data structure for the storage of the comparison results between two
 * artifacts of type Type.
 */
public interface Comparison<Type> extends Serializable {
    /**
     * returns true if nodes are of same type.
     */
    public boolean areArtifactsOfSameType();

    /**
     * Merges both nodes
     */
    public Type mergeArtifacts();

    /**
     * Returns the left artifact of this comparison.
     */
    public Type getLeftArtifact();

    /**
     * Returns the right artifact of this comparison.
     */
    public Type getRightArtifact();

    /**
     * Sets the right artifact of this comparison
     */
    public void setRightArtifact(Type artifact);

    /**
     * Sets the left artifact of this comparison
     */
    public void setLeftArtifact(Type artifact);

    /**
     * Returns all child comparisons
     */
    public List<Comparison<Type>> getChildComparisons();

    /**
     * Adds a given comparison as child element
     */
    public void addChildComparison(Comparison<Type> comparison);

    /**
     * This method adds the given result element to the comparison's results.
     */
    public void addResultElement(ResultElement<Type> result);

    /**
     * This method returns all comparison result elements from this comparison
     */
    public List<ResultElement<Type>> getResultElements();

    /**
     * This method returns the similarity value of this comparison.
     */
    public double getSimilarity();

    /**
     * This method sets the similarity value of this comparison
     */
    public void setSimilarity(double similarity);

    /**
     * This method returns the average similarity value of all comparators which are
     * stored in result elements.
     */
    public default double getResultSimilarity() {
        double resultSimilarity = 0f;
        for (ResultElement<Type> result : getResultElements()) {
            resultSimilarity += result.getSimilarity();
        }
        if (!getResultElements().isEmpty()) {
            resultSimilarity = resultSimilarity / getResultElements().size();
        }
        // if one of both artifacts is null the similarity is 0 because its an optional
        return (getLeftArtifact() == null || getRightArtifact() == null) ? 0f : resultSimilarity;
    }

    /**
     * This method calls updateSimilarity recursively on all child elements to
     * update them first and then returns the average similarity value of all child
     * comparisons. stored in result elements.
     */
    public default double getChildSimilarity() {
        double childSimilarity = 0f;
        // call update similarity recursively on all child comparison
        for (Comparison<Type> childComparison : getChildComparisons()) {
            childComparison.updateSimilarity();
            childSimilarity += childComparison.getSimilarity();
        }
        // calculate the average value
        if (!getChildComparisons().isEmpty()) {
            return childSimilarity / getChildComparisons().size();
        } else {
            // if empty they are similar
            return (getLeftArtifact() == null || getRightArtifact() == null) ? 0f : 1f;
        }
    }

    /**
     * This method updates the similarity of this node and all child nodes recursive
     */
    public default void updateSimilarity() {
        double similarity = 0f;
        double childSimilarity = getChildSimilarity();    // Children based similarity
        double resultSimilarity = getResultSimilarity(); // Comparator based similarity

        // no children node attributes so they are equal on their type
        if (getChildComparisons().isEmpty() && getResultElements().isEmpty()) {
            if (areArtifactsOfSameType()) {
                similarity = 1f;
            } else {
                similarity = 0f;
            }
        }
        // only results available so the similarity is based on them
        if (getChildComparisons().isEmpty() && !getResultElements().isEmpty()) {
            similarity = resultSimilarity;
        }

        // no results available so we use the similarity of our children
        if (!getChildComparisons().isEmpty() && getResultElements().isEmpty()) {
            similarity = childSimilarity;
        }

        // no results available so we use the similarity of our children
        if (!getChildComparisons().isEmpty() && !getResultElements().isEmpty()) {
            // TODO: weight which allows to scale this value
            similarity = (childSimilarity * ComparisonUtil.CHILD_SIMILARITY_WEIGHT + resultSimilarity * ComparisonUtil.RESULT_SIMILARITY_WEIGHT) / 2;
        }
        setSimilarity(similarity);
    }

    public Pair<Map<String, List<Comparison<Node>>>, Map<String, List<Comparison<Node>>>> findOptionalMatchings();
}

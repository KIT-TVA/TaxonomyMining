package main.java.core.compare.comparison.interfaces;

import main.java.core.model.impl.Pair;
import main.java.core.model.interfaces.Node;
import main.java.core.compare.comparator.interfaces.ResultElement;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
    public double getResultSimilarity();

    /**
     * This method calls updateSimilarity recursively on all child elements to
     * update them first and then returns the average similarity value of all child
     * comparisons. stored in result elements.
     */
    public double getChildSimilarity();

    /**
     * This method updates the similarity of this node and all child nodes recursive
     */
    public void updateSimilarity();

    public Pair<Map<String, List<Comparison<Node>>>, Map<String, List<Comparison<Node>>>> findOptionalMatchings();
}

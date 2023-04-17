package main.java.core.compare.comparison.impl;

import main.java.core.compare.comparison.interfaces.Comparison;
import main.java.core.compare.comparator.interfaces.ResultElement;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractComparison<Type> implements Comparison<Type> {
    private static final long serialVersionUID = -2653313078560352977L;
    private Type leftArtifact;
    private Type rightArtifact;
    private double similarity;
    private List<Comparison<Type>> childComparisons;
    private List<ResultElement<Type>> results;

    public AbstractComparison(Type leftArtifact, Type rightArtifact) {
        setLeftArtifact(leftArtifact);
        setRightArtifact(rightArtifact);
        this.childComparisons = new ArrayList<Comparison<Type>>();
        this.results = new ArrayList<ResultElement<Type>>();
    }

    /**
     * This method returns the average similarity value of all comparators which are
     * stored in result elements.
     */
    @Override
    public double getResultSimilarity() {
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
    @Override
    public double getChildSimilarity() {
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
    @Override
    public void updateSimilarity() {
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
            //similarity = (childSimilarity * ComparisonUtil.CHILD_SIMILARITY_WEIGHT + resultSimilarity * ComparisonUtil.RESULT_SIMILARITY_WEIGHT) / 2;
            similarity = (childSimilarity + resultSimilarity) / 2;
        }
        setSimilarity(similarity);
    }

    @Override
    public Type getLeftArtifact() {
        return this.leftArtifact;
    }

    @Override
    public Type getRightArtifact() {
        return this.rightArtifact;
    }

    @Override
    public void setRightArtifact(Type artifact) {
        this.rightArtifact = artifact;
    }

    @Override
    public void setLeftArtifact(Type artifact) {
        this.leftArtifact = artifact;
    }

    @Override
    public double getSimilarity() {
        return this.similarity;
    }

    @Override
    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    @Override
    public List<Comparison<Type>> getChildComparisons() {
        return this.childComparisons;
    }

    @Override
    public void addChildComparison(Comparison<Type> comparison) {
        this.childComparisons.add(comparison);
    }

    @Override
    public void addResultElement(ResultElement<Type> result) {
        this.results.add(result);
    }

    @Override
    public List<ResultElement<Type>> getResultElements() {
        return this.results;
    }

}

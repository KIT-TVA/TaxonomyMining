package tva.kastel.kit.core.compare.comparison.impl;

import tva.kastel.kit.core.compare.comparison.interfaces.Comparison;
import tva.kastel.kit.core.compare.comparison.util.ComparisonUtil;
import tva.kastel.kit.core.model.enums.VariabilityClass;
import tva.kastel.kit.core.model.impl.AttributeImpl;
import tva.kastel.kit.core.model.impl.StringValueImpl;
import tva.kastel.kit.core.model.interfaces.Attribute;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.interfaces.Value;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class VariationComparison extends NodeComparison {

    private static final long serialVersionUID = 2146314128422038513L;


    public VariationComparison(Node leftArtifact, Node rightArtifact) {
        super(leftArtifact, rightArtifact);
    }

    public VariationComparison(Node leftArtifact, Node rightArtifact, double similarity) {
        this(leftArtifact, rightArtifact);
        setSimilarity(similarity);

    }

    public VariationComparison(Node leftArtifact, Node rightArtifact, Comparison<Node> parent) {
        this(leftArtifact, rightArtifact);
        parent.addChildComparison(this);
    }

    public VariationComparison(Comparison<Node> source) {
        this(source.getLeftArtifact(), source.getRightArtifact());
        getChildComparisons().addAll(source.getChildComparisons());
        getResultElements().addAll(source.getResultElements());
        setSimilarity(source.getSimilarity());
    }

    @Override
    public void setNodeOptionalWithChildren(Node node) {

    }

    @Override
    public Node mergeArtifacts(boolean omitOptionalChildren) {
        return mergeArtifacts();
    }

    @Override
    public Node mergeArtifacts() {
        if (getSimilarity() == ComparisonUtil.MANDATORY_VALUE) {
            getLeftArtifact().setVariabilityClass(VariabilityClass.MANDATORY);
            return getLeftArtifact();
        }

        if (getResultSimilarity() == ComparisonUtil.MANDATORY_VALUE) {
            getLeftArtifact().setVariabilityClass(VariabilityClass.MANDATORY);
            getLeftArtifact().getChildren().clear();
            for (Comparison<Node> childComparison : getChildComparisons()) {
                Node node = ((Comparison<Node>) childComparison).mergeArtifacts();
                if (node != null) {
                    getLeftArtifact().addChildWithParent(node);
                }
            }
            getLeftArtifact()
                    .setStartLine(Math.min(getLeftArtifact().getStartLine(), getRightArtifact().getStartLine()));
            getLeftArtifact().setEndLine(Math.min(getLeftArtifact().getEndLine(), getRightArtifact().getEndLine()));

            getLeftArtifact().sortChildNodes();
            return getLeftArtifact();
        }


        if (getLeftArtifact() != null && getRightArtifact() != null) {

            if (hasAtLeastOneExactAttribute() || (getLeftArtifact().isRoot() && getRightArtifact().isRoot())) {
                getLeftArtifact().setVariabilityClass(VariabilityClass.MANDATORY);

                Set<Attribute> containedAttrs = new HashSet<Attribute>();
                for (Attribute leftAttr : getLeftArtifact().getAttributes()) {
                    for (Attribute rightAttr : getRightArtifact().getAttributes()) {
                        // same attr type
                        if (leftAttr.keyEquals(rightAttr)) {
                            List<Value> commonValues = getCommonValues(leftAttr, rightAttr);
                            if (commonValues.size() > 0) {
                                containedAttrs.add(new AttributeImpl(leftAttr.getAttributeKey(), commonValues));
                            }
                        }
                    }
                }

                getLeftArtifact().getAttributes().clear();
                getLeftArtifact().getAttributes().addAll(containedAttrs);
                // process child comparisons recursively
                getLeftArtifact().getChildren().clear();
                for (Comparison<Node> childComparison : getChildComparisons()) {

                    Node merge = ((Comparison<Node>) childComparison).mergeArtifacts();

                    if (merge != null) {
                        getLeftArtifact().addChildWithParent(merge);
                    }

                }
                // add artifacts min line number
                getLeftArtifact()
                        .setStartLine(Math.min(getLeftArtifact().getStartLine(), getRightArtifact().getStartLine()));
                getLeftArtifact().setEndLine(Math.min(getLeftArtifact().getEndLine(), getRightArtifact().getEndLine()));

                getLeftArtifact().sortChildNodes();
                return getLeftArtifact();
            }

            return null;


        }

        return null;
    }

    private List<Value> getCommonValues(Attribute attribute1, Attribute attribute2) {
        List<Value> commonValues = new ArrayList<>();

        for (Value firstValue : attribute1.getAttributeValues()) {
            for (Value secondValue : attribute2.getAttributeValues()) {
                if (firstValue.equals(secondValue)) {
                    commonValues.add(new StringValueImpl((String) firstValue.getValue()));
                }
            }
        }
        return commonValues;
    }

    private boolean hasAtLeastOneExactAttribute() {
        for (Attribute leftAttr : getLeftArtifact().getAttributes()) {
            for (Attribute rightAttr : getRightArtifact().getAttributes()) {
                // same attr type
                if (leftAttr.keyEquals(rightAttr)) {
                    List<Value> commonValues = getCommonValues(leftAttr, rightAttr);
                    if (commonValues.size() == leftAttr.getAttributeValues().size() && commonValues.size() == rightAttr.getAttributeValues().size()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public double getVariationSimilarity() {
        int size = countNodes();

        int leftSize = getLeftArtifact() != null ? getLeftArtifact().getSize() : 0;
        int rightSize = getRightArtifact() != null ? getRightArtifact().getSize() : 0;

        if (leftSize + rightSize == 0) {
            return 0;
        }

        return size / ((double) (leftSize + rightSize) / 2);

    }


    public int countNodes() {

        int nodes = 0;


        if (getSimilarity() == ComparisonUtil.MANDATORY_VALUE) {
            nodes += getLeftArtifact().getSize();
        } else if (getResultSimilarity() == ComparisonUtil.MANDATORY_VALUE) {

            nodes += 1;
            for (Comparison<Node> childComparison : getChildComparisons()) {
                nodes += ((VariationComparison) childComparison).countNodes();
            }
        } else if (getLeftArtifact() != null && getRightArtifact() != null) {

            if (hasAtLeastOneExactAttribute() || (getLeftArtifact().isRoot() && getRightArtifact().isRoot())) {
                nodes += 1;
                for (Comparison<Node> childComparison : getChildComparisons()) {
                    nodes += ((VariationComparison) childComparison).countNodes();
                }

            }

        }

        return nodes;
    }


    public int countNodes2() {

        int nodes = 0;


        if (getSimilarity() == ComparisonUtil.MANDATORY_VALUE) {
            nodes += getLeftArtifact().getSize();
        } else if (getResultSimilarity() == ComparisonUtil.MANDATORY_VALUE) {

            nodes += 1;
            for (Comparison<Node> childComparison : getChildComparisons()) {
                nodes += ((VariationComparison) childComparison).countNodes();
            }
        } else if (getLeftArtifact() != null && getRightArtifact() != null) {

            if (hasAtLeastOneExactAttribute() || (getLeftArtifact().isRoot() && getRightArtifact().isRoot())) {
                nodes += 1;
                for (Comparison<Node> childComparison : getChildComparisons()) {
                    nodes += ((VariationComparison) childComparison).countNodes();
                }

            }

        }

        return nodes;
    }


}

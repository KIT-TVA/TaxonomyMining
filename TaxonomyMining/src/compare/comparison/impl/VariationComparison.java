package compare.comparison.impl;

import compare.comparison.interfaces.Comparison;
import compare.comparison.util.ComparisonUtil;
import model.enums.VariabilityClass;
import model.impl.AttributeImpl;
import model.impl.StringValueImpl;
import model.interfaces.Attribute;
import model.interfaces.Node;
import model.interfaces.Value;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class VariationComparison extends NodeComparison {

    private static final long serialVersionUID = 2146314128422038513L;

    public static ComparisonLevel comparisonLevel = ComparisonLevel.FILE;


    public VariationComparison(Node leftArtifact, Node rightArtifact) {
        super(leftArtifact, rightArtifact);
    }

    public VariationComparison(Node leftArtifact, Node rightArtifact, float similarity) {
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
            for (Comparison<Node> childComparision : getChildComparisons()) {
                Node node = ((Comparison<Node>) childComparision).mergeArtifacts();
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
            for (Comparison<Node> childComparision : getChildComparisons()) {

                Node merge = ((Comparison<Node>) childComparision).mergeArtifacts();

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

}

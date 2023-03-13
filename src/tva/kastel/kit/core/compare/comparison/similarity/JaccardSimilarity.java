package tva.kastel.kit.core.compare.comparison.similarity;

import tva.kastel.kit.core.compare.comparison.interfaces.Comparison;
import tva.kastel.kit.core.compare.comparison.util.ComparisonUtil;
import tva.kastel.kit.core.model.impl.StringValueImpl;
import tva.kastel.kit.core.model.interfaces.Attribute;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.interfaces.Value;

import java.util.ArrayList;
import java.util.List;

public class JaccardSimilarity {

    public static double calculateSimilarity(Comparison<Node> comparison) {
        int leftSize = comparison.getLeftArtifact() != null ? comparison.getLeftArtifact().getSize() : 0;
        int rightSize = comparison.getRightArtifact() != null ? comparison.getRightArtifact().getSize() : 0;

        if (leftSize + rightSize == 0) {
            return 0;
        }

        int intersection = countIntersectingNodes(comparison);

        return ((double) (intersection)) / (leftSize + rightSize - intersection);
    }

    public static int countIntersectingNodes(Comparison<Node> comparison) {

        int nodes = 0;


        if (comparison.getSimilarity() == ComparisonUtil.MANDATORY_VALUE) {
            nodes += comparison.getLeftArtifact().getSize();
        } else if (comparison.getResultSimilarity() == ComparisonUtil.MANDATORY_VALUE) {
            nodes += 1;
            for (Comparison<Node> childComparison : comparison.getChildComparisons()) {
                nodes += countIntersectingNodes(childComparison);
            }
        } else if (comparison.getLeftArtifact() != null && comparison.getRightArtifact() != null) {

            if (hasAtLeastOneExactAttribute(comparison) || (comparison.getLeftArtifact().isRoot() && comparison.getRightArtifact().isRoot())) {
                for (Comparison<Node> childComparison : comparison.getChildComparisons()) {
                    nodes += countIntersectingNodes(childComparison);
                }

            }


        }

        return nodes;
    }

    private static boolean hasAtLeastOneExactAttribute(Comparison<Node> comparison) {
        for (Attribute leftAttr : comparison.getLeftArtifact().getAttributes()) {
            for (Attribute rightAttr : comparison.getRightArtifact().getAttributes()) {
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

    private static List<Value> getCommonValues(Attribute attribute1, Attribute attribute2) {
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

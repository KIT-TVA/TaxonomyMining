package tva.kastel.kit.core.compare.comparator.impl.node;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import tva.kastel.kit.core.compare.comparator.interfaces.Comparator;
import tva.kastel.kit.core.compare.comparator.templates.AbstractNodeComparator;
import tva.kastel.kit.core.model.interfaces.Attribute;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.interfaces.Value;

public class StringComparator extends AbstractNodeComparator {
    double keyValueRatio = 0.4f;

    public StringComparator() {
        super(Comparator.WILDCARD);
    }

    @Override
    public NodeResultElement compare(Node firstNode, Node secondNode) {
        Map<String, Double> similarities = new HashMap<>();

        // compares for every attribute key, which is unique the corresponding values
        for (Attribute firstAttr : firstNode.getAttributes()) {
            for (Attribute secondAttr : secondNode.getAttributes()) {
                // check if attributes are the same
                if (firstAttr.keyEquals(secondAttr)) {
                    similarities.put(firstAttr.getAttributeKey(), compareValues(firstAttr, secondAttr));
                }
            }
        }

        // calculate the avarage similarity
        int maxAttributes = Math.max(firstNode.getAttributes().size(), secondNode.getAttributes().size());
        double similarity = maxAttributes > 0 ? sum(similarities) / maxAttributes : 1f;
        // add keyValueRatio as base similarity because this node are of the same type
        similarity = similarity * (1.0f - keyValueRatio) + keyValueRatio;
        return new NodeResultElement(this, similarity, similarities);
    }

    /**
     * calculates the sum of a map of doubles ignoring keys
     */
    private double sum(Map<String, Double> values) {
        return sum(values.values());
    }

    private double sum(Collection<Double> values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum;
    }

    /**
     * compares the values of a corresponding key returns 1 if a match is found else
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private double compareValues(Attribute firstAttr, Attribute secondAttr) {
        Map<Object, Double> similarities = new HashMap<>();

        for (Value firstValue : firstAttr.getAttributeValues()) {
            for (Value secondValue : secondAttr.getAttributeValues()) {
                if (firstValue.equals(secondValue)) {
                    similarities.put(firstValue.getValue(), 1.0);

                }
            }
        }
        int maxAttributes = Math.max(firstAttr.getAttributeValues().size(), firstAttr.getAttributeValues().size());
        double similarity = maxAttributes > 0 ? sum(similarities.values()) / maxAttributes : 1f;
        // add keyValueRatio as base similarity because this node are of the same type
        similarity = similarity * (1.0f - keyValueRatio) + keyValueRatio;

        return similarity;
    }
}

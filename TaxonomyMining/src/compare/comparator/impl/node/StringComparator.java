package compare.comparator.impl.node;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import compare.comparator.templates.AbstractNodeComparator;
import model.interfaces.Attribute;
import model.interfaces.Node;
import model.interfaces.Value;

public class StringComparator extends AbstractNodeComparator {
	float keyValueRatio = 0.4f;

	public StringComparator() {
		super(WILDCARD);
	}

	@Override
	public NodeResultElement compare(Node firstNode, Node secondNode) {
		Map<String, Float> similarities = new HashMap<>();

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
		float similarity = maxAttributes > 0 ? sum(similarities) / maxAttributes : 1f;
		// add keyValueRatio as base similarity because this node are of the same type
		similarity = similarity * (1.0f - keyValueRatio) + keyValueRatio;
		return new NodeResultElement(this, similarity, similarities);
	}

	/**
	 * calculates the sum of a map of floats ignoring keys
	 */
	private float sum(Map<String, Float> values) {
		return sum(values.values());
	}

	private float sum(Collection<Float> values) {
		float sum = 0;
		for (float value : values) {
			sum += value;
		}
		return sum;
	}

	/**
	 * compares the values of a corresponding key returns 1 if a match is found else
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Float compareValues(Attribute firstAttr, Attribute secondAttr) {
		Map<Object, Float> similarities = new HashMap<Object, Float>();

		for (Value firstValue : firstAttr.getAttributeValues()) {
			for (Value secondValue : secondAttr.getAttributeValues()) {
				if (firstValue.equals(secondValue)) {
					similarities.put(firstValue.getValue(), 1.0f);

				}
			}
		}
		int maxAttributes = Math.max(firstAttr.getAttributeValues().size(), firstAttr.getAttributeValues().size());
		float similarity = maxAttributes > 0 ? sum(similarities.values()) / maxAttributes : 1f;
		// add keyValueRatio as base similarity because this node are of the same type
		similarity = similarity * (1.0f - keyValueRatio) + keyValueRatio;

		return similarity;
	}
}

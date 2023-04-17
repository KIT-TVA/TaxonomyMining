package main.java.core.compare.comparator.util;

import main.java.core.model.interfaces.Attribute;
import main.java.core.model.interfaces.Value;

public class ComparatorUtil {

    /**
     * Compare two sets of values and returns 1f if a match is found else 0f.
     */
    public static double compareValues(Attribute first, Attribute second) {
        for (Value firstValue : first.getAttributeValues()) {
            for (Value secondValue : second.getAttributeValues()) {
                if (firstValue.equals(secondValue)) {
                    return 1f;
                }
            }
        }
        return 0f;
    }
}

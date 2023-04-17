package main.java.core.compare.comparison.util;

import main.java.core.model.enums.VariabilityClass;

public class ComparisonUtil {
    public static final double MANDATORY_VALUE = 1.0f;
    public static final double OPTIONAL_VALUE = 0.3f;


    /**
     * Returns the variability class for a given double similarity value
     */
    public static VariabilityClass getClassForSimilarity(double similarity) {
        if (similarity == MANDATORY_VALUE) {
            return VariabilityClass.MANDATORY;
        } else if (similarity >= OPTIONAL_VALUE) {
            return VariabilityClass.ALTERNATIVE;
        } else {
            return VariabilityClass.OPTIONAL;
        }
    }
}

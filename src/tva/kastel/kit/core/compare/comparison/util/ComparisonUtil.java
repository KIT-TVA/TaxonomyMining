package tva.kastel.kit.core.compare.comparison.util;

import tva.kastel.kit.core.model.enums.VariabilityClass;

public class ComparisonUtil {
    public static final double MANDATORY_VALUE = 1.0f;
    public static final double OPTIONAL_VALUE = 0.3f;

    public static final double CHILD_SIMILARITY_WEIGHT = 0.6;
    public static final double RESULT_SIMILARITY_WEIGHT = 1.4;

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

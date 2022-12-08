package tva.kastel.kit.core.compare.matcher.util;


import tva.kastel.kit.core.compare.comparison.interfaces.Comparison;

public abstract class AbstractArtifactFactory {

    public abstract <K> Comparison<K> copyComparison(Comparison<K> source);

}

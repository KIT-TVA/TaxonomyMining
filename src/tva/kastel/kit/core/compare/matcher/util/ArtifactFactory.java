package tva.kastel.kit.core.compare.matcher.util;

import tva.kastel.kit.core.compare.comparison.impl.NodeComparison;
import tva.kastel.kit.core.compare.comparison.interfaces.Comparison;

public class ArtifactFactory extends AbstractArtifactFactory {


    @Override
    public <K> Comparison<K> copyComparison(Comparison<K> source) {
        if (source instanceof NodeComparison) {
            NodeComparison nodeComp = (NodeComparison) source;
            return (Comparison<K>) new NodeComparison(nodeComp);
        }
        return source;


    }
}

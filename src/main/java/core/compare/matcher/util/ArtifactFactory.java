package main.java.core.compare.matcher.util;

import main.java.core.compare.comparison.interfaces.Comparison;
import main.java.core.compare.comparison.impl.NodeComparison;
import main.java.core.compare.comparison.impl.VariationComparison;

public class ArtifactFactory extends AbstractArtifactFactory {


    @Override
    public <K> Comparison<K> copyComparison(Comparison<K> source) {

        if (source instanceof VariationComparison) {
            VariationComparison varComp = (VariationComparison) source;
            return (Comparison<K>) new VariationComparison(varComp);
        }

        if (source instanceof NodeComparison) {
            NodeComparison nodeComp = (NodeComparison) source;
            return (Comparison<K>) new NodeComparison(nodeComp);
        }
        return source;


    }
}

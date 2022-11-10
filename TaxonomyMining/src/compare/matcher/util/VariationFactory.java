package compare.matcher.util;

import compare.comparison.impl.NodeComparison;
import compare.comparison.impl.VariationComparison;
import compare.comparison.interfaces.Comparison;
import compare.matcher.util.AbstractArtifactFactory;


public class VariationFactory extends AbstractArtifactFactory {

	@Override
	public <K> Comparison<K> copyComparison(Comparison<K> source) {

		if (source instanceof VariationComparison) {
			VariationComparison varComp = (VariationComparison) source;
			return (Comparison<K>) new VariationComparison(varComp);
		} else if (source instanceof NodeComparison) {
			NodeComparison nodeComp = (NodeComparison) source;
			return (Comparison<K>) new NodeComparison(nodeComp);
		}

		return null;
	}

}

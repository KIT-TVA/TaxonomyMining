package compare.matcher.util;


import compare.comparison.interfaces.Comparison;

public abstract class AbstractArtifactFactory {

	public abstract <K> Comparison<K> copyComparison(Comparison<K> source);
	
}

package main.java.core.compare.matcher.util;


import main.java.core.compare.comparison.interfaces.Comparison;

public abstract class AbstractArtifactFactory {

    public abstract <K> Comparison<K> copyComparison(Comparison<K> source);

}

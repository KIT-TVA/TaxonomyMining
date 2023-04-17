package main.java.core.compare.interfaces;

import main.java.core.compare.comparison.interfaces.Comparison;
import main.java.core.model.interfaces.Node;

import java.util.List;

public interface IntraCompare<Type> {

    public ICompareEngine<Type> getCompareEngine();

    public List<List<Comparison<Type>>> getCloneCluster(Node first, Node second, String nodeType);
}

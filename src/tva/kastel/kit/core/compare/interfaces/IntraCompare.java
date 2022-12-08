package tva.kastel.kit.core.compare.interfaces;

import java.util.List;

import tva.kastel.kit.core.compare.comparison.interfaces.Comparison;
import tva.kastel.kit.core.model.interfaces.Node;

public interface IntraCompare<Type> {

    public ICompareEngine<Type> getCompareEngine();

    public List<List<Comparison<Type>>> getCloneCluster(Node first, Node second, String nodeType);
}

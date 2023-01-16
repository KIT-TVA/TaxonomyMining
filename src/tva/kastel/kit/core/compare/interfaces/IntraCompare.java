package tva.kastel.kit.core.compare.interfaces;

import tva.kastel.kit.core.compare.comparison.interfaces.Comparison;
import tva.kastel.kit.core.model.interfaces.Node;

import java.util.List;

public interface IntraCompare<Type> {

    public ICompareEngine<Type> getCompareEngine();

    public List<List<Comparison<Type>>> getCloneCluster(Node first, Node second, String nodeType);
}

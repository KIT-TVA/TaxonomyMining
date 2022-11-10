package compare.interfaces;

import java.util.List;

import compare.comparison.interfaces.Comparison;
import model.interfaces.Node;

public interface IntraCompare<Type> {
	
	public ICompareEngine<Type> getCompareEngine();
	
	public List<List<Comparison<Type>>> getCloneCluster(Node first, Node second, String nodeType);
}

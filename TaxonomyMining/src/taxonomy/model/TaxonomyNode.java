package taxonomy.model;


import model.interfaces.Tree;

public class TaxonomyNode {

	private Tree tree;

	public Tree getTree() {
		return tree;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public TaxonomyNode(Tree tree) {
		super();
		this.tree = tree;
	}

	@Override
	public String toString() {
		return tree.getTreeName();
	}

}

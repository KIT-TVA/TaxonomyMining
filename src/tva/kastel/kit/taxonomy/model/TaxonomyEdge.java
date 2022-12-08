package tva.kastel.kit.taxonomy.model;


import tva.kastel.kit.core.model.interfaces.Tree;

public class TaxonomyEdge {

    private TaxonomyNode start;

    private TaxonomyNode end;

    private Tree refinementTree;

    private TaxonomyFeature feature;


    public TaxonomyFeature getFeature() {
        return feature;
    }

    public void setFeature(TaxonomyFeature feature) {
        this.feature = feature;
    }

    public TaxonomyNode getStart() {
        return start;
    }

    public void setStart(TaxonomyNode start) {
        this.start = start;
    }

    public TaxonomyNode getEnd() {
        return end;
    }

    public void setEnd(TaxonomyNode end) {
        this.end = end;
    }

    public TaxonomyEdge(TaxonomyNode start, TaxonomyNode end, Tree refinementTree) {
        super();
        this.start = start;
        this.end = end;
        this.refinementTree = refinementTree;
    }


    public Tree getRefinementTree() {
        return refinementTree;
    }

    public void setRefinementTree(Tree refinementTree) {
        this.refinementTree = refinementTree;
    }

    @Override
    public String toString() {
        return start.getTree().getTreeName() + "-->" + end.getTree().getTreeName();
    }

}

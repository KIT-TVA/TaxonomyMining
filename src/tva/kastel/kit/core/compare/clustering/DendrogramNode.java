package tva.kastel.kit.core.compare.clustering;

import tva.kastel.kit.core.model.interfaces.Tree;

import java.util.*;

public class DendrogramNode {

    private List<DendrogramNode> children;

    private Tree tree;

    private double height;

    private Map<DendrogramNode, Refinement> refinementMap;

    public DendrogramNode(Tree tree, double height, DendrogramNode... children) {
        this.tree = tree;
        this.children = new ArrayList<>();
        this.children.addAll(Arrays.asList(children));
        this.height = height;
        this.refinementMap = new HashMap<>();
        this.isAbstract = children.length != 0;

    }

    public DendrogramNode(double height, DendrogramNode... children) {
        this(null, height, children);
    }


    public Tree getTree() {
        return tree;
    }

    public List<DendrogramNode> getChildren() {
        return children;
    }

    public void setChildren(List<DendrogramNode> children) {
        this.children = children;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    private boolean isAbstract;

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    @Override
    public String toString() {
        if (tree != null) {
            return tree.getTreeName();
        }
        return super.toString();
    }

    public String printNode() {
        String str = "";

        str += this.getTree().getTreeName() + "--";

        for (DendrogramNode child : this.children) {
            str += refinementMap.get(child).getName() + "--> ";
            str += child.getTree().toString() + "\n";
            str += child.printNode();
        }

        return str;
    }

    public List<DendrogramNode> getAllChildren() {
        List<DendrogramNode> nodes = new ArrayList<>();
        nodes.add(this);

        for (DendrogramNode child : this.children) {
            nodes.addAll(child.getAllChildren());
        }
        return nodes;
    }

    public int getSize() {
        return getAllChildren().size() - 1;
    }

    public void computeRefinements() {
        for (DendrogramNode child : this.children) {
            refinementMap.put(child, new Refinement(this, child));
            child.computeRefinements();
        }

    }

    public void addChild(DendrogramNode child) {
        this.children.add(child);
        refinementMap.put(child, new Refinement(this, child));
    }


    public List<Refinement> getAllRefinements() {
        List<Refinement> refinements = new ArrayList<>();
        refinements.addAll(refinementMap.values());

        for (DendrogramNode child : this.children) {
            refinements.addAll(child.getAllRefinements());
        }
        return refinements;
    }

    public void removeRefinement(DendrogramNode child) {
        this.refinementMap.remove(child);
    }

    public void addRefinement(DendrogramNode child, Refinement refinement) {
        this.refinementMap.put(child, refinement);
    }

    public Refinement getRefinement(DendrogramNode child) {
        return this.refinementMap.get(child);
    }
}

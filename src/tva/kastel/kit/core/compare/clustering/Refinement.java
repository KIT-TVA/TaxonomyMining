package tva.kastel.kit.core.compare.clustering;

import tva.kastel.kit.core.compare.CompareEngineHierarchical;
import tva.kastel.kit.core.compare.matcher.SortingMatcher;
import tva.kastel.kit.core.compare.metric.MetricImpl;
import tva.kastel.kit.core.model.enums.VariabilityClass;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.interfaces.Tree;

import java.util.ArrayList;
import java.util.List;

public class Refinement {

    public static int refinementCounter = 0;

    private Tree tree;

    private String name;

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Refinement(DendrogramNode start, DendrogramNode end) {
        this.tree = getRefinementTree(start, end);
        this.start = start;
        this.end = end;
        this.name = "Refinement_" + refinementCounter;
        refinementCounter++;
    }

    private DendrogramNode start;

    private DendrogramNode end;

    public DendrogramNode getStart() {
        return start;
    }

    public void setStart(DendrogramNode start) {
        this.start = start;
    }

    public DendrogramNode getEnd() {
        return end;
    }

    public void setEnd(DendrogramNode end) {
        this.end = end;
    }

    public Tree getRefinementTree(DendrogramNode node, DendrogramNode child) {

        if (!node.getChildren().contains(child)) {
            return null;
        }

        CompareEngineHierarchical compareEngine = new CompareEngineHierarchical(new SortingMatcher(),
                new MetricImpl(""));

        Tree clone1 = node.getTree().cloneTree();
        Tree clone2 = child.getTree().cloneTree();

        if (clone1.getRoot().getNodeType().equals("DIRECTORY") && clone2.getRoot().getNodeType().equals("DIRECTORY")) {
            clone1.getRoot().getAttributes().clear();
            clone2.getRoot().getAttributes().clear();
        }

        Tree refinementTree = compareEngine.compareMerge(clone1, clone2);

        removeNodesWithVariabilityClass(refinementTree.getRoot(), VariabilityClass.MANDATORY);

        if (refinementTree.getRoot().getVariabilityClass().equals(VariabilityClass.MANDATORY)) {
            refinementTree.setRoot(null);
        } else {
            setNodesMandatory(refinementTree.getRoot());
        }
        return refinementTree;
    }

    private void setNodesMandatory(Node node) {
        node.setVariabilityClass(VariabilityClass.MANDATORY);
        for (Node child : node.getChildren()) {
            setNodesMandatory(child);
        }
    }

    private void removeNodesWithVariabilityClass(Node node, VariabilityClass variabilityClass) {
        List<Node> childrenToRemove = new ArrayList<Node>();
        for (Node child : node.getChildren()) {
            if (child.getVariabilityClass().equals(variabilityClass)) {
                childrenToRemove.add(child);
            }
        }
        node.getChildren().removeAll(childrenToRemove);
        for (Node child : node.getChildren()) {
            removeNodesWithVariabilityClass(child, variabilityClass);
        }

    }


}

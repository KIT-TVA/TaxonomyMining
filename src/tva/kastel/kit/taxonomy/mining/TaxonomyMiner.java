package tva.kastel.kit.taxonomy.mining;

import tva.kastel.kit.core.compare.clustering.ClusterEngine;
import tva.kastel.kit.core.compare.clustering.DendrogramNode;
import tva.kastel.kit.core.compare.clustering.Refinement;
import tva.kastel.kit.core.model.interfaces.Tree;
import tva.kastel.kit.taxonomy.model.Taxonomy;


import java.util.*;


public class TaxonomyMiner {

    private final ClusterEngine clusterEngine;


    public TaxonomyMiner() {
        this.clusterEngine = new ClusterEngine();
    }

    public Taxonomy mine(List<Tree> variants) {
        DendrogramNode rootNode = clusterEngine.calculateDendrogram(variants);
        rootNode.computeRefinements();
        alignRefinements(rootNode);
        uplift(rootNode);
        return new Taxonomy(rootNode);
    }

    public void alignRefinements(DendrogramNode rootNode) {

        List<Refinement> refinements = rootNode.getAllRefinements();
        Map<Tree, Refinement> refinementTreeMap = new HashMap<>();

        Map<Refinement, Refinement> refinementMap = new HashMap<>();

        for (Refinement refinement : refinements) {
            refinementTreeMap.put(refinement.getTree(), refinement);
        }

        List<Set<Tree>> clusters = clusterEngine.cluster(refinementTreeMap.keySet().stream().toList(), 0.0);

        for (Set<Tree> cluster : clusters) {
            Iterator<Tree> iterator = cluster.iterator();
            Tree baseTree = iterator.next();
            Refinement baseRefinement = refinementTreeMap.get(baseTree);
            refinementMap.put(baseRefinement, baseRefinement);
            while (iterator.hasNext()) {
                Tree tree = iterator.next();
                refinementTreeMap.get(tree).setTree(baseTree);
                refinementTreeMap.get(tree).setName(baseRefinement.getName());
            }
        }
    }

    private void uplift(DendrogramNode rootNode) {
        List<Refinement> refinements = rootNode.getAllRefinements();

        for (Refinement refinement : refinements) {

            if (refinement.getTree().getRoot() == null) {
                DendrogramNode parent = refinement.getStart();
                DendrogramNode child = refinement.getEnd();

                parent.getChildren().remove(child);
                parent.removeRefinement(child);
                parent.getChildren().addAll(child.getChildren());
                for (DendrogramNode subChild : child.getChildren()) {
                    parent.addRefinement(subChild, child.getRefinement(subChild));
                }
                parent.getTree().setTreeName(child.getTree().getTreeName());

            }
        }

    }


}

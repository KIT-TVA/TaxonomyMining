package tva.kastel.kit.taxonomy.mining;

import tva.kastel.kit.core.compare.CompareEngineHierarchical;
import tva.kastel.kit.core.compare.clustering.ClusterEngine;
import tva.kastel.kit.core.compare.clustering.DendrogramNode;
import tva.kastel.kit.core.compare.clustering.Refinement;
import tva.kastel.kit.core.compare.comparison.interfaces.Comparison;
import tva.kastel.kit.core.compare.comparison.similarity.JaccardSimilarity;
import tva.kastel.kit.core.compare.matcher.SortingMatcher;
import tva.kastel.kit.core.compare.metric.MetricImpl;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.interfaces.Tree;
import tva.kastel.kit.taxonomy.model.Taxonomy;

import java.util.*;


public class TaxonomyMiner {

    private final ClusterEngine clusterEngine;

    private final CompareEngineHierarchical compareEngine;


    public TaxonomyMiner() {
        this.clusterEngine = new ClusterEngine();
        this.compareEngine = new CompareEngineHierarchical(new SortingMatcher(), new MetricImpl(""));

    }

    public Taxonomy mine(List<Tree> variants) {
        Refinement.refinementCounter = 0;
        DendrogramNode rootNode = clusterEngine.calculateDendrogram(variants);
        rootNode.computeRefinements();
        alignRefinements(rootNode);
        uplift(rootNode);
        updateRefinementNames(rootNode);
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
                Refinement refinement = refinementTreeMap.get(tree);
                refinement.setTree(baseTree);
                refinement.setName(baseRefinement.getName());
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
                    Refinement subRefinement = child.getRefinement(subChild);
                    subRefinement.setStart(parent);
                    parent.addRefinement(subChild, subRefinement);
                }


                if (!parent.isAbstract() && !child.isAbstract()) {
                    parent.getTree().setTreeName(parent.getTree().getTreeName() + " ~ " + child.getTree().getTreeName());
                } else {
                    parent.getTree().setTreeName(child.getTree().getTreeName());
                }
                parent.setAbstract(child.isAbstract());

            }
        }
    }

    private void updateRefinementNames(DendrogramNode rootNode) {
        List<Refinement> refinements = rootNode.getAllRefinements();

        Map<String, List<Refinement>> refinementMap = new HashMap<>();

        for (Refinement refinement : refinements) {

            if (!refinementMap.containsKey(refinement.getName())) {
                refinementMap.put(refinement.getName(), new ArrayList<>());
            }
            refinementMap.get(refinement.getName()).add(refinement);
        }


        int count = 0;

        for (Map.Entry<String, List<Refinement>> entry : refinementMap.entrySet()) {

            String refinementName = "Refinement_" + count;
            for (Refinement refinement : entry.getValue()) {
                refinement.setName(refinementName);
            }
            count++;
        }
    }

    public void identify(Taxonomy taxonomy, Tree tree) {
        DendrogramNode target = identify(taxonomy.getRootNode(), tree);
        System.out.println("Tree " + tree.getTreeName() + " belongs to class " + target.getTree().getTreeName());
    }


    private DendrogramNode identify(DendrogramNode currentNode, Tree tree) {


        Comparison<Node> comparison = compareEngine.compare(currentNode.getTree(), tree);
        double currentNodeSimilarity = JaccardSimilarity.calculateSimilarity(comparison);

        double maxSimilarity = Double.MIN_VALUE;
        DendrogramNode target = null;
        for (DendrogramNode child : currentNode.getChildren()) {
            comparison = compareEngine.compare(child.getTree(), tree);
            double similarity = JaccardSimilarity.calculateSimilarity(comparison);

            if (similarity > maxSimilarity) {
                maxSimilarity = similarity;
                target = child;
            }
        }

        if (currentNodeSimilarity > maxSimilarity) {
            return currentNode;
        }
        return identify(target, tree);
    }

}

package main.java.taxonomy.mining;

import main.java.core.compare.clustering.ClusterEngine;
import main.java.core.compare.clustering.DendrogramNode;
import main.java.core.compare.comparison.interfaces.Comparison;
import main.java.core.compare.matcher.SortingMatcher;
import main.java.core.compare.metric.MetricImpl;
import main.java.core.model.interfaces.Node;
import main.java.core.model.interfaces.Tree;
import main.java.taxonomy.model.Taxonomy;
import main.java.core.compare.CompareEngineHierarchical;
import main.java.core.compare.clustering.Refinement;
import main.java.core.compare.comparison.similarity.JaccardSimilarity;

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
        updateAbstractionNames(rootNode);
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

    public void updateAbstractionNames(DendrogramNode rootNode) {

        int counter = 0;
        for (DendrogramNode node : rootNode.getAllChildren()) {
            if (node.isAbstract()) {
                node.getTree().setTreeName("Abstraction_" + counter);
                counter++;
            }
        }


    }

    public void identify(Taxonomy taxonomy, Tree tree) {

        List<DendrogramNode> nodes = taxonomy.getAllNodes();
        double maxSim = Double.MIN_VALUE;

        DendrogramNode target = null;
        for (DendrogramNode node : nodes) {
            Comparison<Node> comparison = compareEngine.compare(node.getTree(), tree);
            double similarity = JaccardSimilarity.calculateSimilarity(comparison);

            if (similarity > maxSim) {
                maxSim = similarity;
                target = node;
            }

            System.out.println("Tree " + tree.getTreeName() + " comparing " + node.getTree().getTreeName() + " (Similarity: " + similarity + ")");

        }

        System.out.println("Tree " + tree.getTreeName() + " belongs to class " + target.getTree().getTreeName() + " (Similarity: " + maxSim + ")");
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

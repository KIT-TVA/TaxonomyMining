package tva.kastel.kit.taxonomy.mining;

import java.util.*;
import java.util.Map.Entry;

import tva.kastel.kit.core.compare.CompareEngineHierarchical;
import tva.kastel.kit.core.compare.clustering.ClusterEngine;
import tva.kastel.kit.core.compare.comparison.impl.VariationComparisonFactory;
import tva.kastel.kit.core.compare.comparison.interfaces.Comparison;
import tva.kastel.kit.core.compare.matcher.util.VariationFactory;
import tva.kastel.kit.core.compare.matcher.SortingMatcher;
import tva.kastel.kit.core.compare.metric.MetricImpl;
import tva.kastel.kit.core.model.enums.VariabilityClass;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.interfaces.Tree;
import tva.kastel.kit.taxonomy.model.Taxonomy;
import tva.kastel.kit.taxonomy.model.TaxonomyEdge;
import tva.kastel.kit.taxonomy.model.TaxonomyFeature;
import tva.kastel.kit.taxonomy.model.TaxonomyNode;

public class TaxonomyMiner {

    private ClusterEngine clusterEngine;
    private CompareEngineHierarchical compareEngine;

    private Map<Tree, Tree> cloneMap;

    private int treeCount = 0;

    public TaxonomyMiner() {

        this.cloneMap = new HashMap<Tree, Tree>();
        this.compareEngine = new CompareEngineHierarchical(new SortingMatcher(), new MetricImpl(""));
        this.clusterEngine = new ClusterEngine(compareEngine);

    }

    public Taxonomy mine(List<Tree> variants) {

        CompareEngineHierarchical compareEngine = new CompareEngineHierarchical(new SortingMatcher(new VariationFactory()),
                new MetricImpl(""), new VariationComparisonFactory());

        List<Set<Tree>> clusters = null;

        Map<Tree, TaxonomyNode> treeToTaxonomyNode = new HashMap<Tree, TaxonomyNode>();
        List<TaxonomyEdge> taxonomyEdges = new ArrayList<TaxonomyEdge>();

        // Start with base variants
        List<Tree> targets = new ArrayList<Tree>();
        targets.addAll(variants);

        for (Tree variant : variants) {
            cloneMap.put(variant, variant.cloneTree());
        }
        do {

            targets.sort(Comparator.comparing(Tree::getTreeName));

            clusterEngine.setThreshold(findSlidingThreshold(targets));

            clusters = clusterEngine.detectClusters(targets);

            targets.clear();
            for (Set<Tree> cluster : clusters) {
                // process cluster: merge tree, derive refinements and create taxonomy nodes
                // with edges

                System.out.println("Overall clusters: " + clusters.size());

                if (cluster.size() > 1) {

                    System.out.println("Found cluster: ");

                    for (Tree t : cluster) {
                        System.out.print(t.getTreeName() + ", ");
                    }
                    System.out.println();

                    List<Tree> treeList = new ArrayList<Tree>();
                    treeList.addAll(cluster);

                    treeList.sort(Comparator.comparing(Tree::getTreeName));

                    Tree mergedTree = compareEngine.compareMerge(treeList);
                    mergedTree.setTreeName("Abstraction_" + treeCount);
                    System.out.println("Abstraction: " + mergedTree.getTreeName());
                    treeCount++;
                    targets.add(mergedTree);
                    cloneMap.put(mergedTree, mergedTree.cloneTree());
                    taxonomyEdges.addAll(connectNodes(mergedTree, cluster, treeToTaxonomyNode));

                } else {
                    targets.add(cluster.iterator().next());
                }

            }

        } while (targets.size() > 1);

        List<TaxonomyNode> taxonomyNodes = new ArrayList<TaxonomyNode>();
        taxonomyNodes.addAll(treeToTaxonomyNode.values());
        Taxonomy taxonomy = new Taxonomy(taxonomyNodes, taxonomyEdges, treeToTaxonomyNode.get(targets.get(0)),
                targets.get(0));

        uplift(taxonomy);
        mapEdgesToFeatures(taxonomy);

        return taxonomy;

    }

    private double findSlidingThreshold(List<Tree> items) {
        double[][] matrix = new double[items.size()][items.size()];
        for (int i = 0; i < items.size(); i++) {
            for (int j = i; j < items.size(); j++) {

                Node item1 = items.get(i).getRoot();
                Node item2 = items.get(j).getRoot();

                if (i == j) {
                    matrix[i][j] = 0.0f;
                } else {

                    Comparison<Node> comparison = compareEngine.compare(item1, item2);
                    double similarity = comparison.getSimilarity();

                    double distance = 1 - similarity;
                    matrix[i][j] = distance;
                    matrix[j][i] = distance;
                }
            }
        }

        double minimumDistance = 100.0f;
        for (int i = 0; i < items.size(); i++) {
            for (int j = i; j < items.size(); j++) {

                if (i != j) {

                    if (matrix[i][j] < minimumDistance) {
                        minimumDistance = matrix[i][j];
                    }

                }

            }
        }

        return minimumDistance;
    }

    private void mapEdgesToFeatures(Taxonomy taxonomy) {

        clusterEngine.setThreshold(0.0f);
        int count = 0;

        List<Tree> trees = new ArrayList<Tree>();
        for (TaxonomyEdge edge : taxonomy.getEdges()) {
            trees.add(edge.getRefinementTree());
        }

        trees.sort(Comparator.comparing(Tree::getTreeName));

        List<Set<Tree>> clusters = clusterEngine.detectClusters(trees);

        Map<Set<Tree>, TaxonomyFeature> clusterToFeatureMap = new HashMap<Set<Tree>, TaxonomyFeature>();

        for (Set<Tree> cluster : clusters) {

            clusterToFeatureMap.put(cluster, new TaxonomyFeature("REFINEMENT_" + count));
            count++;
        }

        for (Entry<Set<Tree>, TaxonomyFeature> entry : clusterToFeatureMap.entrySet()) {

            for (TaxonomyEdge edge : taxonomy.getEdges()) {
                if (entry.getKey().contains(edge.getRefinementTree())) {
                    edge.setFeature(entry.getValue());
                }
            }

        }

    }

    private void uplift(Taxonomy taxonomy) {

        List<TaxonomyEdge> edgesToRemove = new ArrayList<TaxonomyEdge>();
        List<TaxonomyNode> nodesToRemove = new ArrayList<TaxonomyNode>();
        for (TaxonomyEdge edge : taxonomy.getEdges()) {

            TaxonomyNode start = edge.getStart();
            TaxonomyNode end = edge.getEnd();

            CompareEngineHierarchical compareEngineHierarchical = new CompareEngineHierarchical(new SortingMatcher(),
                    new MetricImpl(""));


            Comparison<Node> comparison = compareEngineHierarchical.compare(start.getTree().getRoot(), end.getTree().getRoot());

            boolean isEqual = false;

            if (edge.getRefinementTree().getRoot() == null) {
                isEqual = true;
            } else {
                double similarity = comparison.getSimilarity();
                isEqual = similarity == 1.0f;
            }

            if (isEqual) {

                edgesToRemove.add(edge);
                nodesToRemove.add(end);
                start.getTree().setTreeName(end.getTree().getTreeName());

                for (TaxonomyEdge edge2 : taxonomy.getEdges()) {

                    if (edge2.getStart().equals(end)) {
                        edge2.setStart(start);
                    }
                    if (edge2.getStart().equals(start)) {
                        edge2.getRefinementTree().setTreeName(edge2.getStart().getTree().getTreeName() + "_"
                                + edge2.getEnd().getTree().getTreeName());
                    }
                }

            }


        }

        taxonomy.getEdges().removeAll(edgesToRemove);
        taxonomy.getNodes().removeAll(nodesToRemove);

        for (TaxonomyEdge edge : taxonomy.getEdges()) {
            edge.getRefinementTree()
                    .setTreeName(edge.getStart().getTree().getTreeName() + "_" + edge.getEnd().getTree().getTreeName());
        }

    }

    private List<TaxonomyEdge> connectNodes(Tree mergedTree, Set<Tree> originalTrees,
                                            Map<Tree, TaxonomyNode> treeToTaxonomyNode) {

        TaxonomyNode mergedNode = null;

        if (treeToTaxonomyNode.containsKey(mergedTree)) {
            mergedNode = treeToTaxonomyNode.get(mergedTree);
        } else {
            mergedNode = new TaxonomyNode(cloneMap.get(mergedTree));
            treeToTaxonomyNode.put(mergedTree, mergedNode);
        }

        List<TaxonomyEdge> taxonomyEdges = new ArrayList<TaxonomyEdge>();

        for (Tree tree : originalTrees) {
            TaxonomyNode taxonomyNode = null;
            if (treeToTaxonomyNode.containsKey(tree)) {
                taxonomyNode = treeToTaxonomyNode.get(tree);
            } else {
                taxonomyNode = new TaxonomyNode(cloneMap.get(tree));
                treeToTaxonomyNode.put(tree, taxonomyNode);
            }

            Tree refinementTree = getRefinementTree(mergedNode.getTree().cloneTree(),
                    taxonomyNode.getTree().cloneTree());

            TaxonomyEdge edge = new TaxonomyEdge(mergedNode, taxonomyNode, refinementTree);
            taxonomyEdges.add(edge);
        }
        return taxonomyEdges;

    }

    private Tree getRefinementTree(Tree tree1, Tree tree2) {
        CompareEngineHierarchical compareEngine = new CompareEngineHierarchical(new SortingMatcher(),
                new MetricImpl(""));


        Tree clone1 = tree1.cloneTree();
        Tree clone2 = tree2.cloneTree();

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

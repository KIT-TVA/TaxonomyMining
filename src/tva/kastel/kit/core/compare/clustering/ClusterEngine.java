package tva.kastel.kit.core.compare.clustering;


import org.apache.commons.lang3.time.StopWatch;
import tva.kastel.kit.core.compare.CompareEngineHierarchical;
import tva.kastel.kit.core.compare.comparison.impl.NodeComparisonFactory;
import tva.kastel.kit.core.compare.comparison.impl.VariationComparisonFactory;
import tva.kastel.kit.core.compare.comparison.interfaces.Comparison;
import tva.kastel.kit.core.compare.comparison.similarity.JaccardSimilarity;
import tva.kastel.kit.core.compare.matcher.SortingMatcher;
import tva.kastel.kit.core.compare.metric.MetricImpl;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.interfaces.Tree;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ClusterEngine {


    public DendrogramNode calculateDendrogram(List<Tree> trees) {
        return calculateDendrogram(trees, true);
    }

    private DendrogramNode calculateDendrogram(List<Tree> trees, boolean createAbstractions) {

        int abstractionCounter = 0;

        CompareEngineHierarchical compareEngine = new CompareEngineHierarchical(new SortingMatcher(), new MetricImpl(""), new VariationComparisonFactory());

        Map<Integer, DendrogramNode> map = new HashMap<>();

        for (int i = 0; i < trees.size(); i++) {
            map.put(i, new DendrogramNode(trees.get(i), 0));
        }

        double[][] distanceMatrix = calculateDistanceMatrix(trees);


        do {
            double lowestDistance = Double.MAX_VALUE;
            int lowestIndexI = 0;
            int lowestIndexJ = 0;

            for (int x = 0; x < distanceMatrix.length; x++) {
                for (int y = x; y < distanceMatrix[0].length; y++) {

                    if (distanceMatrix[x][y] < lowestDistance && x != y) {
                        lowestDistance = distanceMatrix[x][y];
                        lowestIndexI = x;
                        lowestIndexJ = y;
                    }
                }
            }


            double[][] updatedDistanceMatrix = new double[distanceMatrix.length][distanceMatrix[0].length];


            for (int i = 0; i < distanceMatrix.length; i++) {
                for (int j = i; j < distanceMatrix[0].length; j++) {

                    if ((i == j)) {
                        updatedDistanceMatrix[i][j] = 0;
                        updatedDistanceMatrix[j][i] = 0;
                    } else if (i == lowestIndexJ) {
                        updatedDistanceMatrix[i][j] = (distanceMatrix[i][j] + distanceMatrix[j][lowestIndexI]) / 2;
                        updatedDistanceMatrix[j][i] = updatedDistanceMatrix[i][j];
                    } else if (i == lowestIndexI) {
                        updatedDistanceMatrix[i][j] = (distanceMatrix[i][j] + distanceMatrix[j][lowestIndexJ]) / 2;
                        updatedDistanceMatrix[j][i] = updatedDistanceMatrix[i][j];
                    } else {
                        updatedDistanceMatrix[i][j] = distanceMatrix[i][j];
                        updatedDistanceMatrix[j][i] = updatedDistanceMatrix[i][j];

                    }
                }
            }


            DendrogramNode node1 = map.get(lowestIndexI);
            DendrogramNode node2 = map.get(lowestIndexJ);
            DendrogramNode parent = null;
            if (createAbstractions) {
                Tree mergedTree = compareEngine.compareMerge(node1.getTree().cloneTree(), node2.getTree().cloneTree());
                mergedTree.setTreeName("Abstraction_" + abstractionCounter);
                abstractionCounter++;
                parent = new DendrogramNode(mergedTree, updatedDistanceMatrix[lowestIndexI][lowestIndexJ], node1, node2);
            } else {
                parent = new DendrogramNode(updatedDistanceMatrix[lowestIndexI][lowestIndexJ], node1, node2);
            }

            map.replace(lowestIndexJ, parent);
            List<DendrogramNode> nodes = map.values().stream().toList();
            map.clear();
            List<DendrogramNode> newNodes = new ArrayList<>();
            newNodes.addAll(nodes.subList(0, lowestIndexI));
            newNodes.addAll(nodes.subList(lowestIndexI + 1, nodes.size()));
            for (int i = 0; i < newNodes.size(); i++) {
                map.put(i, newNodes.get(i));
            }


            double[][] reducedDistanceMatrix = new double[updatedDistanceMatrix.length - 1][updatedDistanceMatrix[0].length - 1];
            int k = 0;
            for (int i = 0; i < updatedDistanceMatrix.length - 1; i++, k++) {

                if (i == lowestIndexI) {
                    k++;
                }
                System.arraycopy(updatedDistanceMatrix[k], 0, reducedDistanceMatrix[i], 0, lowestIndexI);
                System.arraycopy(updatedDistanceMatrix[k], lowestIndexI + 1, reducedDistanceMatrix[i], lowestIndexI, updatedDistanceMatrix[k].length - 1 - lowestIndexI);
            }

            distanceMatrix = reducedDistanceMatrix;

        }
        while (distanceMatrix.length != 1);
        return map.get(0);

    }

    public List<Set<Tree>> cluster(List<Tree> trees, double threshold) {
        DendrogramNode root = calculateDendrogram(trees, false);
        List<DendrogramNode> nodesBelowThreshold = getNodesBelowThreshold(root, threshold);
        List<Set<Tree>> clusters = new ArrayList<>();

        Set<Tree> treesInCluster = new HashSet<>();

        for (DendrogramNode node : nodesBelowThreshold) {
            Set<Tree> cluster = new HashSet<>();
            List<DendrogramNode> allChildren = node.getAllChildren();
            for (DendrogramNode child : allChildren) {
                if (child.getTree() != null) {
                    cluster.add(child.getTree());
                    treesInCluster.add(child.getTree());
                }
            }
            clusters.add(cluster);

        }

        for (Tree tree : trees) {
            if (!treesInCluster.contains(tree)) {
                Set<Tree> cluster = new HashSet<>();
                cluster.add(tree);
                clusters.add(cluster);
            }
        }

        return clusters;
    }

    private List<DendrogramNode> getNodesBelowThreshold(DendrogramNode currentNode, double threshold) {

        List<DendrogramNode> nodes = new ArrayList<>();

        if (currentNode.getHeight() <= threshold && currentNode.getTree() == null) {
            nodes.add(currentNode);
        } else {
            for (DendrogramNode child : currentNode.getChildren()) {
                nodes.addAll(getNodesBelowThreshold(child, threshold));
            }
        }

        return nodes;
    }


    public double[][] calculateDistanceMatrix(List<Tree> trees) {
        CompareEngineHierarchical compareEngine = new CompareEngineHierarchical(new SortingMatcher(), new MetricImpl(""), new NodeComparisonFactory());

        double[][] matrix = new double[trees.size()][trees.size()];
        for (int i = 0; i < trees.size(); i++) {
            for (int j = i; j < trees.size(); j++) {

                Tree tree1 = trees.get(i);
                Tree tree2 = trees.get(j);

                System.out.println("Comparing " + tree1.getTreeName() + " " + tree2.getTreeName());


                if (i == j) {
                    matrix[i][j] = 0.0f;
                } else {
                    double similarity = 0;
                    if (tree1.getRoot() == null && tree2.getRoot() == null) {
                        similarity = 1;
                    } else if (tree1.getRoot() == null || tree2.getRoot() == null) {
                        similarity = 0;
                    } else {
                        Comparison<Node> comparison = compareEngine.compare(tree1, tree2);
                        similarity = JaccardSimilarity.calculateSimilarity(comparison);


                    }

                    double distance = 1 - similarity;
                    matrix[i][j] = distance;
                    matrix[j][i] = distance;
                }
            }
        }
        return matrix;
    }


}

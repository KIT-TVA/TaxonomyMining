package tva.kastel.kit.core.compare.clustering;


import tva.kastel.kit.core.compare.CompareEngineHierarchical;
import tva.kastel.kit.core.compare.matcher.SortingMatcher;
import jep.Interpreter;
import jep.NDArray;
import jep.SharedInterpreter;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.interfaces.Tree;

import java.util.*;

public class ClusterEngine {

    private CompareEngineHierarchical compareEngine;

    public ClusterEngine(CompareEngineHierarchical compareEngine) {
        this.compareEngine = compareEngine;
    }


    private double threshold = 0.15f;

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }


    public List<Set<Tree>> detectClusters(List<Tree> trees) {

        String distanceString = buildDistanceString(trees);
        List<Set<Tree>> clusters = new ArrayList<Set<Tree>>();

        if (trees.size() == 0) {
            return clusters;
        }

        if (trees.size() == 1) {
            Set<Tree> treeSet = new HashSet<Tree>();
            treeSet.add(trees.get(0));
            clusters.add(treeSet);
            return clusters;
        }


        try (Interpreter interp = new SharedInterpreter()) {
            interp.exec("from sklearn.cluster import AgglomerativeClustering");
            interp.exec("import numpy as np");
            interp.exec("import sys");

            interp.exec("distances = " + distanceString);
            interp.exec("threshold = " + threshold + " + 0.00000001");

            interp.exec("clustering = AgglomerativeClustering(n_clusters=None, affinity='precomputed', linkage='complete', distance_threshold=threshold)");
            interp.exec("clustering.fit(distances)");
            interp.exec("labels = clustering.labels_");
            NDArray labels = (NDArray) interp.getValue("labels");
            long[] results = (long[]) labels.getData();

            Iterator<Tree> iterator = trees.iterator();
            Map<Integer, Set<Tree>> map = new HashMap<Integer, Set<Tree>>();

            for (int i = 0; i < results.length; i++) {
                int cluster = (int) results[i];
                if (!map.containsKey(cluster)) {
                    map.put(cluster, new HashSet<Tree>());
                }
                map.get(cluster).add(iterator.next());
            }


            for (Set<Tree> set : map.values()) {

                clusters.add(set);
            }


            return clusters;


        }


    }


    private String getDistanceString(double[][] matrix, int size) {
        String distanceString = "(";
        for (int i = 0; i < size; i++) {
            distanceString += "[";
            for (int j = 0; j < size; j++) {
                distanceString += matrix[i][j];
                if (j != size - 1) {
                    distanceString += ",";
                }

            }
            distanceString += "]";
            if (i != size - 1) {
                distanceString += ",";
            }

        }
        distanceString += ")";

        return distanceString;
    }

    private String buildDistanceString(List<Tree> trees) {
        double[][] matrix = new double[trees.size()][trees.size()];
        for (int i = 0; i < trees.size(); i++) {
            for (int j = i; j < trees.size(); j++) {

                Tree tree1 = trees.get(i);
                Tree tree2 = trees.get(j);

                if (i == j) {
                    matrix[i][j] = 0.0f;
                } else {
                    double distance = 1 - compareEngine.compare(tree1, tree2).getSimilarity();
                    matrix[i][j] = distance;
                    matrix[j][i] = distance;
                }
            }
        }
        return getDistanceString(matrix, trees.size());
    }


}

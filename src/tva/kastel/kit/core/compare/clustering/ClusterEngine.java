package tva.kastel.kit.core.compare.clustering;


import jep.Interpreter;
import jep.JepConfig;
import jep.NDArray;
import jep.SharedInterpreter;
import tva.kastel.kit.core.compare.CompareEngineHierarchical;
import tva.kastel.kit.core.compare.comparison.impl.VariationComparison;
import tva.kastel.kit.core.compare.comparison.interfaces.Comparison;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.interfaces.Tree;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.*;

public class ClusterEngine {

    private CompareEngineHierarchical compareEngine;

    private ByteArrayOutputStream outputStream;

    public ClusterEngine(CompareEngineHierarchical compareEngine) {
        JepConfig config = new JepConfig();
        outputStream = new ByteArrayOutputStream();
        config.redirectStdout(outputStream);
        config.redirectStdErr(System.err);
        SharedInterpreter.setConfig(config);
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

            String os = System.getProperty("os.name").toLowerCase();
            long[] results = null;
            if (os.contains("win")) {
                NDArray labels = (NDArray) interp.getValue("labels");
                results = (long[]) labels.getData();
            } else {
                interp.exec("print(np.array2string(labels, separator=',', max_line_width=np.inf))");
                String resultString = new String(outputStream.toByteArray());
                outputStream.reset();
                resultString = resultString.replace("[", "").replace("]", "").replace("\n", "").trim();
                results = new long[trees.size()];
                String[] parts = resultString.split(",");
                for (int i = 0; i < results.length; i++) {
                    parts[i] = parts[i].trim();
                    results[i] = Long.parseLong(parts[i]);
                }


            }


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


                    Comparison<Node> comparison = compareEngine.compare(tree1, tree2);
                    double similarity = 0;

                    if (comparison instanceof VariationComparison) {
                        similarity = ((VariationComparison) comparison).getVariationSimilarity();

                        // System.out.println("Comparing " + tree1.getTreeName() + " " + tree2.getTreeName());
                        //System.out.println("Similarity : " + comparison.getSimilarity());
                        // System.out.println("Similarity (new) : " + similarity);
                        //System.out.println();


                    } else {
                        similarity = comparison.getSimilarity();
                    }


                    double distance = 1 - similarity;
                    matrix[i][j] = distance;
                    matrix[j][i] = distance;
                }
            }
        }
        return getDistanceString(matrix, trees.size());
    }


}

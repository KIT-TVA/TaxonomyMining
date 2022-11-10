package taxonomy.mining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import compare.CompareEngineHierarchical;
import compare.clustering.ClusterEngine;
import compare.comparison.impl.VariationComparisonFactory;
import compare.matcher.util.VariationFactory;
import compare.matcher.SortingMatcher;
import compare.metric.MetricImpl;
import model.enums.VariabilityClass;
import model.interfaces.Node;
import model.interfaces.Tree;
import taxonomy.model.Taxonomy;
import taxonomy.model.TaxonomyEdge;
import taxonomy.model.TaxonomyFeature;
import taxonomy.model.TaxonomyNode;

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
			clusterEngine.setThreshold(findSlidingTreshold(targets));

			clusters = clusterEngine.detectClusters(targets);

			targets.clear();
			for (Set<Tree> cluster : clusters) {
				// process cluster: merge tree, derive refinements and create taxonomy nodes
				// with edges

				if (cluster.size() > 1) {
					List<Tree> treeList = new ArrayList<Tree>();
					treeList.addAll(cluster);
					Tree mergedTree = compareEngine.compareMerge(treeList);
					mergedTree.setTreeName("Abstraction_" + treeCount);
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

	private float findSlidingTreshold(List<Tree> items) {
		float[][] matrix = new float[items.size()][items.size()];
		for (int i = 0; i < items.size(); i++) {
			for (int j = i; j < items.size(); j++) {

				Node item1 = items.get(i).getRoot();
				Node item2 = items.get(j).getRoot();

				if (i == j) {
					matrix[i][j] = 0.0f;
				} else {
					float distance = 1 - compareEngine.compare(item1, item2).getSimilarity();
					matrix[i][j] = distance;
					matrix[j][i] = distance;
				}
			}
		}

		float minimumDistance = 100.0f;
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

		List<Set<Tree>> clusters = clusterEngine.detectClusters(trees);

		Map<Set<Tree>, TaxonomyFeature> clusterToFeatureMap = new HashMap<Set<Tree>, TaxonomyFeature>();

		for (Set<Tree> cluster : clusters) {

			clusterToFeatureMap.put(cluster, new TaxonomyFeature("FEATURE_" + count));
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

			float similarity = compareEngineHierarchical.compare(start.getTree().getRoot(), end.getTree().getRoot())
					.getSimilarity();

			if (similarity == 1.0f) {

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

		Tree refinementTree = compareEngine.compareMerge(tree1.cloneTree(), tree2.cloneTree());
		removeNodesWithVariabilityClass(refinementTree.getRoot(), VariabilityClass.MANDATORY);
		setNodesMandatory(refinementTree.getRoot());
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

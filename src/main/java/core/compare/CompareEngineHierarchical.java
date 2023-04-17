package main.java.core.compare;

import main.java.core.compare.comparator.impl.node.StringComparator;
import main.java.core.compare.comparator.interfaces.Comparator;
import main.java.core.compare.comparator.interfaces.ResultElement;
import main.java.core.compare.comparison.impl.AbstractComparisonFactory;
import main.java.core.compare.comparison.impl.NodeComparisonFactory;
import main.java.core.compare.comparison.interfaces.Comparison;
import main.java.core.compare.interfaces.ICompareEngine;
import main.java.core.compare.matcher.interfaces.Matcher;
import main.java.core.compare.metric.interfaces.Metric;
import main.java.core.model.impl.Pair;
import main.java.core.model.interfaces.Node;
import main.java.core.model.interfaces.Tree;
import main.java.core.model.impl.TreeImpl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CompareEngineHierarchical implements ICompareEngine<Node> {
    private Comparator<Node> defaultComparator;
    private Metric metric;
    private Matcher matcher;

    private AbstractComparisonFactory comparisonFactory;

    public CompareEngineHierarchical(Matcher selectedMatcher, Metric selectedMetric) {
        this(selectedMatcher, selectedMetric, new NodeComparisonFactory());
    }


    public CompareEngineHierarchical(Matcher selectedMatcher, Metric selectedMetric,
                                     AbstractComparisonFactory factory) {
        this.metric = selectedMetric;
        this.matcher = selectedMatcher;
        this.comparisonFactory = factory;
        this.defaultComparator = new StringComparator();
    }

    @Override
    public Node compareMerge(Node first, Node second) {
        Comparison<Node> root = compare(first, second);
        // match
        root.updateSimilarity();
        getMatcher().calculateMatching(root);
        root.updateSimilarity();

        Pair<Map<String, List<Comparison<Node>>>, Map<String, List<Comparison<Node>>>> optionalMatchings = root
                .findOptionalMatchings();

        Node rootNode = root.mergeArtifacts();

        return rootNode;
    }


    @Override
    public Comparison<Node> compare(Node first, Node second) {
        Comparison<Node> root = compareNode(first, second);
        // match
        root.updateSimilarity();
        getMatcher().calculateMatching(root);
        root.updateSimilarity();
        return root;
    }


    private Comparison<Node> compareNode(Node first, Node second) {
        Comparison<Node> comparison = comparisonFactory.create(first, second);
        // if nodes are of the same type
        if (first.getNodeType().equals(second.getNodeType())) {

            List<Comparator> comparators = metric.getComparatorForNodeType(first.getNodeType());
            // check if the metric contains attribute for the comparison of this node type
            if (!comparators.isEmpty()) {
                for (Comparator comparator : comparators) {
                    comparison.addResultElement(comparator.compare(first, second));
                }
            } else {
                ResultElement<Node> c = defaultComparator.compare(first, second);
                comparison.addResultElement(c);

            }
            // if no children available the recursion ends here
            if (first.getChildren().isEmpty() && second.getChildren().isEmpty()) {
                return comparison;
            } else {
                // if one of both has no children the other elements are optional
                if (first.getChildren().isEmpty() || second.getChildren().isEmpty()) {
                    first.getChildren().stream()
                            .forEach(e -> comparison.addChildComparison(comparisonFactory.create(e, null, 0f)));
                    second.getChildren().stream()
                            .forEach(e -> comparison.addChildComparison(comparisonFactory.create(null, e, 0f)));
                } else {

                    // compare children recursively
                    first.getChildren().stream().forEach(e -> {
                        second.getChildren().stream().forEach(f -> {
                            Comparison<Node> innerComp = compare(e, f);
                            if (innerComp != null) {
                                comparison.addChildComparison(innerComp);
                            }
                        });
                    });
                }
            }
            return comparison;
        } else {
            return comparison;
        }
    }


    @Override
    public Tree compareMerge(Tree first, Tree second) {
        try {
            return new TreeImpl(first, second, compareMerge(first.getRoot(), second.getRoot()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Tree compareMerge(List<Tree> variants) {
        Iterator<Tree> variantIterator = variants.iterator();
        Tree mergedTree = null;
        for (Tree variant : variants) {
            // first variant
            if (mergedTree == null) {
                mergedTree = variantIterator.next();
            } else {
                mergedTree = compareMerge(mergedTree, variant);
            }
        }
        return mergedTree;
    }

    @Override
    public Comparison<Node> compare(Tree first, Tree second) {
        return compare(first.getRoot(), second.getRoot());
    }


    @Override
    public Comparator getDefaultComparator() {
        return defaultComparator;
    }

    @Override
    public Metric getMetric() {
        return this.metric;
    }

    @Override
    public Matcher getMatcher() {
        return this.matcher;
    }

}
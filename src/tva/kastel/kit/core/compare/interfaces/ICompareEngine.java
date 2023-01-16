package tva.kastel.kit.core.compare.interfaces;

import tva.kastel.kit.core.compare.comparator.interfaces.Comparator;
import tva.kastel.kit.core.compare.comparison.interfaces.Comparison;
import tva.kastel.kit.core.compare.matcher.interfaces.Matcher;
import tva.kastel.kit.core.compare.metric.interfaces.Metric;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.interfaces.Tree;

import java.util.List;

public interface ICompareEngine<Type> {

    /**
     * This method compares two trees and all underlying child nodes and returns the merged tree which contains all information of both trees.
     * Uses IMatcher , IMerger,
     */
    public Tree compareMerge(Tree first, Tree second);

    /**
     * This method compares a list of trees and all underlying child nodes and returns the merged tree which contains all information of both trees.
     * Uses IMatcher , IMerger,
     */
    public Tree compareMerge(List<Tree> variants);

    /**
     * This method compares two trees and all underlying child nodes
     * Uses IMatcher , IMerger,
     */
    public Comparison<Type> compare(Tree first, Tree second);


    /**
     * This method compares two nodes and all underlying child nodes recursively
     *
     * @param first
     * @param second
     * @return comparison between two nodes
     */
    public Comparison<Type> compare(Node first, Node second);


    /**
     * This method compares two nodes and all underlying child nodes recursively and merges them
     *
     * @param first
     * @param second
     * @return comparison between two nodes
     */
    public Node compareMerge(Node first, Node second);

    /**
     * Returns the default comparator for the comparison of two nodes
     */
    public Comparator<Type> getDefaultComparator();

    /**
     * This method returns the comparison metric that is used for the comparison in a compare engine instance
     */
    public Metric getMetric();

    /**
     * This method returns the matcher that is used to match nodes from the comparison.
     */
    public Matcher getMatcher();

}

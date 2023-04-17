package main.java.core.compare.comparator.interfaces;

public interface Comparator<Type> {
    public static final String WILDCARD = "WILDCARD";

    /**
     * This method returns the node type that is supported by this comparator.
     */
    public String getSupportedNodeType();

    /**
     * This method checks if both nodes are comparable with this comparator.
     */
    public boolean isComparable(Type firstNode, Type secondNode);

    /*
     * This method returns the weight of this comparator
     */
    public double getWeight();


    /*
     * This method is used to set the weight of this comparator
     */
    public void setWeight(double weight);

    /**
     * This method compares two nodes of the same type and returns the similarity.
     */
    public ResultElement<Type> compare(Type firstNode, Type secondNode);

}

package main.java.core.compare.comparator.interfaces;

public interface ResultElement<Type> {

    public Comparator<Type> getUsedComparator();

    public double getSimilarity();

    public void setSimilarity(double similarity);
}

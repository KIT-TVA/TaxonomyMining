package tva.kastel.kit.core.compare.comparator.templates;

import tva.kastel.kit.core.compare.comparator.interfaces.Comparator;
import tva.kastel.kit.core.compare.comparator.interfaces.ResultElement;

public class AbstractResultElement<Type> implements ResultElement<Type> {
    private Comparator<Type> usedComparator;
    private double similiarty = 0f;

    public AbstractResultElement(Comparator<Type> usedComparator, double similarity) {
        setUsedComparator(usedComparator);
        setSimilarity(similarity);
    }

    @Override
    public Comparator<Type> getUsedComparator() {
        return this.usedComparator;
    }

    @Override
    public double getSimilarity() {
        return this.similiarty;
    }

    @Override
    public void setSimilarity(double similarity) {
        this.similiarty = similarity;
    }

    public void setUsedComparator(Comparator<Type> usedComparator) {
        this.usedComparator = usedComparator;
    }

    public double getSimiliarty() {
        return similiarty;
    }

    public void setSimiliarty(double similiarty) {
        this.similiarty = similiarty;
    }
}

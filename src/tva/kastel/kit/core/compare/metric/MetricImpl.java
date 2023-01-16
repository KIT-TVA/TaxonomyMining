package tva.kastel.kit.core.compare.metric;

import tva.kastel.kit.core.compare.comparator.interfaces.Comparator;
import tva.kastel.kit.core.compare.metric.interfaces.AbstractMetric;

public class MetricImpl extends AbstractMetric {
    private static final long serialVersionUID = -3144206252367501818L;

    public MetricImpl(String metricName) {
        super();
        setMetricName(metricName);
    }

    public MetricImpl(String metricName, Comparator... comparators) {
        this(metricName);
        addComparators(comparators);
    }

    /**
     * This method adds a array of node comparators to this metric.
     */
    protected void addComparators(Comparator[] comparators) {
        for (Comparator comparator : comparators) {
            addComparator(comparator.getSupportedNodeType(), comparator);
        }
    }

    public void removeComparators(Comparator[] comparators) {
        for (Comparator comparator : comparators) {
            removeComparator(comparator.getSupportedNodeType(), comparator);
        }
    }

}
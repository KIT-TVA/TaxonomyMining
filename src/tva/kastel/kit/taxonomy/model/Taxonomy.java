package tva.kastel.kit.taxonomy.model;

import com.google.common.collect.Lists;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Variable;
import org.logicng.transformations.cnf.BDDCNFTransformation;
import tva.kastel.kit.core.model.interfaces.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Taxonomy {

    private List<TaxonomyNode> nodes;

    private List<TaxonomyEdge> edges;

    private TaxonomyNode root;

    private Tree tree;

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public List<TaxonomyNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<TaxonomyNode> nodes) {
        this.nodes = nodes;
    }

    public List<TaxonomyEdge> getEdges() {
        return edges;
    }

    public void setEdges(List<TaxonomyEdge> edges) {
        this.edges = edges;
    }

    public Taxonomy(List<TaxonomyNode> nodes, List<TaxonomyEdge> edges, TaxonomyNode root, Tree tree) {
        this.root = root;
        this.nodes = nodes;
        this.edges = edges;
        this.tree = tree;
    }

    public TaxonomyNode getRoot() {
        return root;
    }

    public void setRoot(TaxonomyNode root) {
        this.root = root;
    }

    public List<TaxonomyEdge> getPath(TaxonomyNode target) {

        List<TaxonomyEdge> edgesToFollow = new ArrayList<TaxonomyEdge>();

        while (target != root) {
            for (TaxonomyEdge taxonomyEdge : this.edges) {

                if (taxonomyEdge.getEnd().equals(target)) {
                    edgesToFollow.add(taxonomyEdge);
                    target = taxonomyEdge.getStart();
                    break;

                }
            }
        }
        return Lists.reverse(edgesToFollow);

    }


    public Formula getPropositionalFormula() {
        FormulaFactory factory = new FormulaFactory();
        Map<TaxonomyFeature, Variable> variableMapping = new HashMap<TaxonomyFeature, Variable>();

        List<TaxonomyFeature> taxonomyFeatures = new ArrayList<TaxonomyFeature>();
        for (TaxonomyEdge edge : edges) {
            taxonomyFeatures.add(edge.getFeature());
            if (!variableMapping.containsKey(edge.getFeature())) {
                variableMapping.put(edge.getFeature(), factory.variable(edge.getFeature().getName()));
            }
        }

        Variable baseVariable = factory.variable(root.toString());
        List<Formula> singleFormulas = new ArrayList<Formula>();
        for (TaxonomyNode taxonomyNode : nodes) {

            List<TaxonomyEdge> path = getPath(taxonomyNode);

            List<TaxonomyFeature> visitedFeatures = new ArrayList<TaxonomyFeature>();
            List<TaxonomyFeature> unvisitedFeatures = new ArrayList<TaxonomyFeature>();
            for (TaxonomyEdge edge : path) {
                TaxonomyFeature feature = edge.getFeature();
                visitedFeatures.add(feature);
            }
            unvisitedFeatures.addAll(taxonomyFeatures);
            unvisitedFeatures.removeAll(visitedFeatures);

            List<Formula> formulas = new ArrayList<Formula>();
            for (TaxonomyFeature visitedFeature : visitedFeatures) {
                formulas.add(variableMapping.get(visitedFeature));
            }

            for (TaxonomyFeature unvisitedFeature : unvisitedFeatures) {
                formulas.add(factory.not(variableMapping.get(unvisitedFeature)));
            }

            formulas.add(baseVariable);
            Formula singleFormula = factory.and(formulas);
            singleFormulas.add(singleFormula);

        }

        Formula complete = factory.or(singleFormulas);
        complete = complete.transform(new BDDCNFTransformation());
        return complete;

    }

    @Override
    public String toString() {

        String tax = "";
        for (TaxonomyEdge edge : edges) {

            tax += (edge.getStart().toString() + " --" + edge.getFeature().getName() + "--> " + edge.getEnd().toString() + "\n");

        }
        return tax;
    }


}

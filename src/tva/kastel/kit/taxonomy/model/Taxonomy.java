package tva.kastel.kit.taxonomy.model;

import com.google.common.collect.Lists;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Variable;
import org.logicng.transformations.cnf.BDDCNFTransformation;
import tva.kastel.kit.core.compare.clustering.DendrogramNode;
import tva.kastel.kit.core.compare.clustering.Refinement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Taxonomy {


    private DendrogramNode rootNode;


    public DendrogramNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(DendrogramNode rootNode) {
        this.rootNode = rootNode;
    }

    public Taxonomy(DendrogramNode rootNode) {
        this.rootNode = rootNode;
    }


    @Override
    public String toString() {
        return rootNode.printNode();
    }

    public List<DendrogramNode> getAllNodes() {
        return rootNode.getAllChildren();
    }

    public List<Refinement> getAllRefinements() {
        return rootNode.getAllRefinements();
    }


    private List<Refinement> getPath(DendrogramNode target) {

        List<Refinement> refinementsToFollow = new ArrayList<>();

        List<Refinement> allRefinements = getAllRefinements();
        while (target != this.rootNode) {
            for (Refinement refinement : allRefinements) {
                if (refinement.getEnd().equals(target)) {
                    refinementsToFollow.add(refinement);
                    target = refinement.getStart();
                    break;
                }

            }
        }

        return Lists.reverse(refinementsToFollow);

    }


    public Formula getPropositionalFormula() {
        FormulaFactory factory = new FormulaFactory();
        Map<String, Variable> variableMapping = new HashMap<String, Variable>();

        List<Refinement> allRefinements = getAllRefinements();
        for (Refinement refinement : allRefinements) {
            if (!variableMapping.containsKey(refinement.getName())) {
                variableMapping.put(refinement.getName(), factory.variable(refinement.getName()));
            }
        }

        Variable baseVariable = factory.variable("Base");
        List<Formula> singleFormulas = new ArrayList<Formula>();


        for (DendrogramNode node : getAllNodes()) {

            List<Refinement> path = getPath(node);

            List<Refinement> visitedFeatures = new ArrayList<Refinement>();
            List<Refinement> unvisitedFeatures = new ArrayList<Refinement>();
            visitedFeatures.addAll(path);
            unvisitedFeatures.addAll(allRefinements);
            unvisitedFeatures.removeAll(visitedFeatures);

            List<Formula> formulas = new ArrayList<Formula>();
            for (Refinement visitedFeature : visitedFeatures) {
                formulas.add(variableMapping.get(visitedFeature.getName()));
            }

            for (Refinement unvisitedFeature : unvisitedFeatures) {
                formulas.add(factory.not(variableMapping.get(unvisitedFeature.getName())));
            }

            formulas.add(baseVariable);
            Formula singleFormula = factory.and(formulas);
            singleFormulas.add(singleFormula);

        }

        Formula complete = factory.or(singleFormulas);
        complete = complete.transform(new BDDCNFTransformation());
        return complete;

    }


}

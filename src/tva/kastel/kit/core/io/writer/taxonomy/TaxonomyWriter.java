package tva.kastel.kit.core.io.writer.taxonomy;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import tva.kastel.kit.core.compare.clustering.DendrogramNode;
import tva.kastel.kit.core.compare.clustering.Refinement;
import tva.kastel.kit.core.io.writer.dimacs.DimacsWriter;
import tva.kastel.kit.core.io.writer.gson.GsonExportService;
import tva.kastel.kit.taxonomy.model.Taxonomy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;

public class TaxonomyWriter {

    private GsonExportService exportService;
    private DimacsWriter dimacsWriter;

    public TaxonomyWriter(GsonExportService exportService, DimacsWriter dimacsWriter) {
        this.exportService = exportService;
        this.dimacsWriter = dimacsWriter;
    }


    public void writeToFile(Taxonomy taxonomy, String directoryPath) {

        try {

            File directory = new File(directoryPath);

            for (File file : directory.listFiles()) {
                file.delete();
            }

            File taxonomyResult = Paths.get(directoryPath, "taxonomy.txt").toFile();
            taxonomyResult.createNewFile();

            FileWriter fileWriter = new FileWriter(taxonomyResult);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.print(taxonomy.toString());
            printWriter.close();

            //  dimacsWriter.writeToFile(taxonomy.getPropositionalFormula(), directoryPath);


            for (DendrogramNode node : taxonomy.getAllNodes()) {
                File nodeFile = Paths.get(directoryPath, node.getTree().toString() + ".tree").toFile();
                fileWriter = new FileWriter(nodeFile);
                printWriter = new PrintWriter(fileWriter);

                String json = exportService.exportTree(node.getTree());

                printWriter.write(json);
                printWriter.close();
            }

            List<String> processedRefinements = new ArrayList<>();
            for (Refinement refinement : taxonomy.getAllRefinements()) {
                if (!processedRefinements.contains(refinement.getName())) {
                    File edgeFile = Paths.get(directoryPath, refinement.getName() + ".tree").toFile();
                    fileWriter = new FileWriter(edgeFile);
                    printWriter = new PrintWriter(fileWriter);

                    String json = exportService.exportTree(refinement.getTree());

                    printWriter.write(json);
                    printWriter.close();
                }
                processedRefinements.add(refinement.getName());

            }

            Graph graph = plot(taxonomy);
            Graphviz.fromGraph(graph).render(Format.SVG).toFile(Paths.get(directoryPath, "taxonomy.svg").toFile());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public Graph plot(Taxonomy taxonomy) throws IOException {

        Graph graph = Factory.graph().directed();

        Map<String, Node> nodeMap = new HashMap<>();

        taxonomy.getAllRefinements().sort(Comparator.comparing(Refinement::getName));

        List<Refinement> refinementList = taxonomy.getAllRefinements();

        for (Refinement refinement : refinementList) {

            Node start = null;
            if (nodeMap.containsKey(refinement.getStart().toString())) {
                start = nodeMap.get(refinement.getStart().toString());

            } else {
                start = Factory.node(refinement.getStart().toString() + "\n" + refinement.getStart().getTree().getSize());
                nodeMap.put(refinement.getStart().toString(), start);
            }

            Node end = null;
            if (nodeMap.containsKey(refinement.getEnd().toString())) {
                end = nodeMap.get(refinement.getEnd().toString());

            } else {
                end = Factory.node(refinement.getEnd().toString() + "\n" + refinement.getEnd().getTree().getSize());
                nodeMap.put(refinement.getEnd().toString(), end);
            }


            graph = graph.with(start.link(Factory.to(end).with(Label.of(refinement.getName() + "\n" + refinement.getTree().getSize()))));

        }


        return graph;


    }

}

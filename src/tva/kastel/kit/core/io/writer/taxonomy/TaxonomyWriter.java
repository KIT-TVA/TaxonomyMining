package tva.kastel.kit.core.io.writer.taxonomy;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import tva.kastel.kit.core.io.writer.dimacs.DimacsWriter;
import tva.kastel.kit.core.io.writer.gson.GsonExportService;
import tva.kastel.kit.core.model.interfaces.Tree;
import tva.kastel.kit.taxonomy.model.Taxonomy;
import tva.kastel.kit.taxonomy.model.TaxonomyEdge;
import tva.kastel.kit.taxonomy.model.TaxonomyNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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

            dimacsWriter.writeToFile(taxonomy.getPropositionalFormula(), directoryPath);

            for (TaxonomyNode node : taxonomy.getNodes()) {
                File nodeFile = Paths.get(directoryPath, node.toString() + ".tree").toFile();
                fileWriter = new FileWriter(nodeFile);
                printWriter = new PrintWriter(fileWriter);

                String json = exportService.exportTree(node.getTree());

                printWriter.write(json);
                printWriter.close();
            }

            for (TaxonomyEdge edge : taxonomy.getEdges()) {
                File edgeFile = Paths.get(directoryPath, edge.getFeature().getName() + ".tree").toFile();
                fileWriter = new FileWriter(edgeFile);
                printWriter = new PrintWriter(fileWriter);

                String json = exportService.exportTree(edge.getRefinementTree());

                printWriter.write(json);
                printWriter.close();
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

        taxonomy.getEdges().sort(Comparator.comparing(TaxonomyEdge::toString));

        for (TaxonomyEdge edge : taxonomy.getEdges()) {

            Node start = null;
            if (nodeMap.containsKey(edge.getStart().toString())) {
                start = nodeMap.get(edge.getStart().toString());

            } else {
                start = Factory.node(edge.getStart().toString());
                nodeMap.put(edge.getStart().toString(), start);
            }

            Node end = null;
            if (nodeMap.containsKey(edge.getEnd().toString())) {
                end = nodeMap.get(edge.getEnd().toString());

            } else {
                end = Factory.node(edge.getEnd().toString());
                nodeMap.put(edge.getEnd().toString(), end);
            }


            graph = graph.with(start.link(Factory.to(end).with(Label.of(edge.getFeature().getName()))));

        }

        return graph;


    }

}

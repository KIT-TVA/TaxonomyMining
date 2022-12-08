package tva.kastel.kit.taxonomy;


import tva.kastel.kit.core.compare.comparison.impl.ComparisonLevel;
import tva.kastel.kit.core.io.reader.ReaderManager;
import tva.kastel.kit.core.io.reader.java.JavaReader;
import tva.kastel.kit.core.io.reader.java.JavaVisitor;
import tva.kastel.kit.core.io.writer.dimacs.DimacsWriter;
import tva.kastel.kit.core.io.writer.gson.GsonExportService;
import tva.kastel.kit.core.io.writer.taxonomy.TaxonomyWriter;
import tva.kastel.kit.core.model.interfaces.Node;
import tva.kastel.kit.core.model.interfaces.Tree;
import tva.kastel.kit.taxonomy.mining.TaxonomyMiner;
import tva.kastel.kit.taxonomy.model.Taxonomy;

import java.io.File;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {


        if (args.length != 2) {
            System.out.println("Please provide an input and output directory.");
        } else {
            String inputDirectory = args[0];
            String outputDirectory = args[1];

            if (!Files.exists(Paths.get(inputDirectory)) || !Files.exists(Paths.get(outputDirectory))) {
                System.out.println("Invalid input or output directory.");
            } else {
                List<Tree> trees = readFiles(inputDirectory);

                TaxonomyMiner miner = new TaxonomyMiner();
                Taxonomy taxonomy = miner.mine(trees);

                TaxonomyWriter taxonomyWriter = new TaxonomyWriter(new GsonExportService(), new DimacsWriter());
                taxonomyWriter.writeToFile(taxonomy, outputDirectory);

                System.out.println(taxonomy.toString());
            }

        }


    }

    private static List<Tree> readFiles(String inputDirectoryPath) {

        File inputDirectory = new File(inputDirectoryPath);
        ReaderManager readerManager = new ReaderManager();
        List<Tree> trees = new ArrayList<>();

        for (File file : inputDirectory.listFiles()) {
            Tree tree = readerManager.readFile(file);
            tree.getRoot().getAttributes().clear();
            trees.add(tree);
        }


        return trees;
    }

}

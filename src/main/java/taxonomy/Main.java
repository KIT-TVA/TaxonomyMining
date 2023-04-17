package main.java.taxonomy;


import main.java.core.io.reader.ReaderManager;
import main.java.core.io.writer.dimacs.DimacsWriter;
import main.java.core.io.writer.gson.GsonExportService;
import main.java.core.io.writer.taxonomy.TaxonomyWriter;
import main.java.core.model.interfaces.Tree;
import main.java.taxonomy.mining.TaxonomyMiner;
import main.java.taxonomy.model.Taxonomy;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
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
                ReaderManager readerManager = new ReaderManager();
                List<Tree> trees = readerManager.readFiles(new File(inputDirectory));

                TaxonomyMiner miner = new TaxonomyMiner();
                Taxonomy taxonomy = miner.mine(trees);

                TaxonomyWriter taxonomyWriter = new TaxonomyWriter(new GsonExportService(), new DimacsWriter());
                taxonomyWriter.writeToFile(taxonomy, outputDirectory);

                System.out.println(taxonomy.toString());
            }

        }


    }


}

package tva.kastel.kit.taxonomy;


import javafx.scene.control.TreeItem;
import tva.kastel.kit.core.io.reader.ReaderManager;
import tva.kastel.kit.core.io.writer.dimacs.DimacsWriter;
import tva.kastel.kit.core.io.writer.gson.GsonExportService;
import tva.kastel.kit.core.io.writer.taxonomy.TaxonomyWriter;
import tva.kastel.kit.core.model.interfaces.Tree;
import tva.kastel.kit.taxonomy.mining.TaxonomyMiner;
import tva.kastel.kit.taxonomy.model.Taxonomy;
import tva.kastel.kit.ui.util.FileWrapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

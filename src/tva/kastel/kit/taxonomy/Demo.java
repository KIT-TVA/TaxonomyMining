package tva.kastel.kit.taxonomy;

import org.apache.commons.io.FileUtils;
import tva.kastel.kit.core.io.reader.ReaderManager;
import tva.kastel.kit.core.io.writer.dimacs.DimacsWriter;
import tva.kastel.kit.core.io.writer.gson.GsonExportService;
import tva.kastel.kit.core.io.writer.taxonomy.TaxonomyWriter;
import tva.kastel.kit.core.model.interfaces.Tree;
import tva.kastel.kit.taxonomy.mining.TaxonomyMiner;
import tva.kastel.kit.taxonomy.model.Taxonomy;
import tva.kastel.kit.taxonomy.util.DirectoryTable;
import tva.kastel.kit.ui.util.FileTable;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demo {


    public static void main(String[] args) {

        DirectoryTable.createDirectoryStructure();

        try {

            FileUtils.deleteDirectory(DirectoryTable.outputDirectory);
            DirectoryTable.outputDirectory.mkdir();

            if (!DirectoryTable.inputDirectory.exists()) {
                DirectoryTable.inputDirectory.mkdir();
            }

            if (DirectoryTable.inputDirectory.listFiles() != null) {
                for (File exampleDirectory : DirectoryTable.inputDirectory.listFiles()) {
                    runExample(exampleDirectory);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public static void runExample(File exampleDirectory) {

        System.out.println("Processing example: " + exampleDirectory.getName());
        ReaderManager readerManager = new ReaderManager();
        List<Tree> trees = readerManager.readFiles(exampleDirectory);

        TaxonomyMiner miner = new TaxonomyMiner();
        Taxonomy taxonomy = miner.mine(trees);
        TaxonomyWriter taxonomyWriter = new TaxonomyWriter(new GsonExportService(), new DimacsWriter());

        File outputDirectory = new File(DirectoryTable.outputDirectory, exampleDirectory.getName());
        outputDirectory.mkdir();

        taxonomyWriter.writeToFile(taxonomy, outputDirectory.getAbsolutePath());

        System.out.println("DONE");

    }


}

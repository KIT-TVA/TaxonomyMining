package taxonomy;


import compare.comparison.impl.ComparisonLevel;
import io.reader.java.JavaReader;
import io.writer.dimacs.DimacsWriter;
import io.writer.gson.GsonExportService;
import io.writer.taxonomy.TaxonomyWriter;
import model.interfaces.Tree;
import taxonomy.mining.TaxonomyMiner;
import taxonomy.model.Taxonomy;

import java.io.File;
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
        JavaReader reader = new JavaReader();
        List<Tree> trees = new ArrayList<>();

        ComparisonLevel comparisonLevel = ComparisonLevel.FILE;

        for (File file : inputDirectory.listFiles()) {
            Tree tree = reader.readArtifact(file);
            trees.add(tree);
            if (file.isDirectory()) {
                comparisonLevel = ComparisonLevel.DIRECTORY;
            }
        }
        //VariationComparison.comparisonLevel = comparisonLevel;


        return trees;
    }

}

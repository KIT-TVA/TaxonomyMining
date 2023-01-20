package tva.kastel.kit.ui.util;

import javafx.scene.image.Image;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class FileTable {

    public static String iconDirectory = createIconDirectory();

    public static final Image rootImage = new Image(Paths.get(iconDirectory, "root_16.png").toString());

    public static final Image FV_ALTERNATIVE_16 = new Image(Paths.get(iconDirectory, "alternative_16.png").toString());
    public static final Image FV_MANDATORY_16 = new Image(Paths.get(iconDirectory, "mandatory_16.png").toString());
    public static final Image FV_OPTIONAL_16 = new Image(Paths.get(iconDirectory, "optional_16.png").toString());

    public static final Image FV_DIRECTORY_16 = new Image(Paths.get(iconDirectory, "directory_16.png").toString());

    public static final Image FV_FILE_16 = new Image(Paths.get(iconDirectory, "file_16.png").toString());

    public static final Image FV_TREE_16 = new Image(Paths.get(iconDirectory, "tree_16.png").toString());

    public static final File CONFIGURATION_FILE = Paths.get(iconDirectory, "../configuration.properties").toFile();


    public static String createIconDirectory() {

        iconDirectory = "";

        try {
            iconDirectory = Paths.get(FileTable.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
            iconDirectory = Paths.get(iconDirectory, "//..//..//resources//icons//").toAbsolutePath().toString();


        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return iconDirectory;
    }

}

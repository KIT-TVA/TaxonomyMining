package tva.kastel.kit.ui;

import javafx.scene.image.Image;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileTable {

    public static final String iconDirectory = createString();

    public static final Image rootImage = new Image(Paths.get(iconDirectory, "root_16.png").toString());

    public static final Image FV_ALTERNATIVE_16 = new Image(Paths.get(iconDirectory, "alternative_16.png").toString());
    public static final Image FV_MANDATORY_16 = new Image(Paths.get(iconDirectory, "mandatory_16.png").toString());
    public static final Image FV_OPTIONAL_16 = new Image(Paths.get(iconDirectory, "optional_16.png").toString());


    public static String createString() {

        String location = "";

        try {
            location = Paths.get(FileTable.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
            location = Paths.get(location, "//..//..//resources//icons//").toAbsolutePath().toString();


        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return location;
    }

}

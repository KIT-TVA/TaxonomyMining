package tva.kastel.kit.edu.taxonomyui;

import javafx.scene.image.Image;

import java.io.File;
import java.nio.file.Paths;

public class FileTable {

    private static final String iconDirectory = (new File((FileTable.class.getProtectionDomain().getCodeSource().getLocation().getPath())).toString());

    public static final Image nodeImage = new Image(Paths.get(iconDirectory, "icons/node_16.png").toString());
    public static final Image rootImage = new Image(Paths.get(iconDirectory, "icons/root_16.png").toString());

    public static final Image FV_ALTERNATIVE_16 = new Image(Paths.get(iconDirectory, "icons/alternative_16.png").toString());
    public static final Image FV_MANDATORY_16 = new Image(Paths.get(iconDirectory, "icons/mandatory_16.png").toString());
    public static final Image FV_OPTIONAL_16 = new Image(Paths.get(iconDirectory, "/icons/optional_16.png").toString());


}

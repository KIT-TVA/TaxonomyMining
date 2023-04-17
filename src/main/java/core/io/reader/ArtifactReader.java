package main.java.core.io.reader;

import com.google.common.io.Files;
import main.java.core.model.interfaces.Tree;

import java.io.File;

/**
 * Interface for the text to model transformation
 */
public interface ArtifactReader {
    public Tree readArtifact(File element);

    public String[] getSupportedFiles();

    /**
     * This method checks if the current fileEnding is supported
     */
    public default boolean isFileSupported(File element) {
        return element.isDirectory() ? false : isFileSupported(Files.getFileExtension(element.getName()));
    }

    public default boolean isFileSupported(String fileExtension) {
        for (String fileEnding : getSupportedFiles()) {
            if (fileEnding.equals(fileExtension)) {
                return true;
            }
        }
        return false;
    }

}

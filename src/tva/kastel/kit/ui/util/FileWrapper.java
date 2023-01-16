package tva.kastel.kit.ui.util;

import java.io.File;

public class FileWrapper {


    public FileWrapper(File file) {
        this.file = file;
    }

    private File file;

    @Override
    public String toString() {
        return file.getName();
    }


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}

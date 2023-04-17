package main.java.core.io.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReaderConfiguration {


    private List<String> fileTypes;


    public ReaderConfiguration() {
        this.fileTypes = new ArrayList<>();
    }

    public ReaderConfiguration(String... fileTypes) {
        this.fileTypes = Arrays.asList(fileTypes);
    }


    public List<String> getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(List<String> fileTypes) {
        this.fileTypes = fileTypes;
    }


    public boolean hasSpecifiedFileTypes() {
        return fileTypes.size() > 0;
    }

}

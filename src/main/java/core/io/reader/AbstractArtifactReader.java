package main.java.core.io.reader;

import main.java.core.model.interfaces.Tree;

import java.io.File;

public abstract class AbstractArtifactReader implements ArtifactReader {
    private String[] supportedFiles;

    public AbstractArtifactReader(String[] supportedFiles) {
        setSupportedFiles(supportedFiles);
    }

    @Override
    public String[] getSupportedFiles() {
        return supportedFiles;
    }

    public void setSupportedFiles(String[] supportedFiles) {
        this.supportedFiles = supportedFiles;
    }

    public Tree readArtifact(String path) {
        File file = new File(path);
        return readArtifact(file);
    }

}

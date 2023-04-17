package main.java.core.io.writer;


/**
 * Abstract class for the creation of an artifact writer. The constructor of the implementation have to be parameterless or injectable.
 */
public abstract class AbstractArtifactWriter implements ArtifactWriter {
    private static final String DIALOG_MSG = "Select Path";
    private String fileEnding;

    public AbstractArtifactWriter(String fileEnding) {
        setFileEnding(fileEnding);
    }

    @Override
    public String getFileEnding() {
        return this.fileEnding;
    }

    public void setFileEnding(String fileEnding) {
        this.fileEnding = fileEnding;
    }


}

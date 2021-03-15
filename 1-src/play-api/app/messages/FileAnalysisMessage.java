package messages;

public class FileAnalysisMessage {

    private final String fileName;

    public FileAnalysisMessage(String file) {
        this.fileName = file;
    }

    public String getFileName() {
        return fileName;
    }
}

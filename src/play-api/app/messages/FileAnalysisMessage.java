package messages;


public class FileAnalysisMessage {

    private final String fileName;

    /**
     *
     * @param file
     */
    public FileAnalysisMessage(String file) {
        this.fileName = file;
    }

    public String getFileName() {
        return fileName;
    }
}

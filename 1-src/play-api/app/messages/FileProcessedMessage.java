package messages;

import models.LineProcessingResult;

import java.util.List;

public class FileProcessedMessage {

    private List<LineProcessingResult> hMap;

    public FileProcessedMessage(List<LineProcessingResult> hMap) {
        super();
        this.hMap = hMap;
    }

    public List<LineProcessingResult> getHMap() {
        return hMap;
    }

    public void setHMap(List<LineProcessingResult> hMap) {
        this.hMap = hMap;
    }


}

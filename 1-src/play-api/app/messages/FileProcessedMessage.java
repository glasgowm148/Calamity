package messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import models.LineProcessingResult;

public class FileProcessedMessage {

    private List<LineProcessingResult> hMap ;

	public List<LineProcessingResult> getHMap() {
		return hMap;
	}

	public FileProcessedMessage(List<LineProcessingResult> hMap) {
		super();
		this.hMap = hMap;
	}

	public void setHMap(List<LineProcessingResult> hMap) {
		this.hMap = hMap;
	}

    

}

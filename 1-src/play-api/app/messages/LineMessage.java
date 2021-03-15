package messages;

import java.util.List;

public class LineMessage {

    private final List<String> data;

    public LineMessage(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }
}

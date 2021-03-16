package messages;

import java.util.List;

public class LineMessage {

    private final List<String> data;

    /**
     * Set the data
     *
     * @param data - a List of Strings
     */
    public LineMessage(List<String> data) {
        this.data = data;
    }

    /**
     * Getter
     *
     * @return data
     */
    public List<String> getData() {
        return data;
    }
}

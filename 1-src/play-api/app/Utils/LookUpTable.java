package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class LookUpTable {
    private final Set<String> dictionary;

    public LookUpTable(File file) {
        dictionary = new LinkedHashSet<>();
        BufferedReader reader = FileUtils.getFileReader(file);
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                dictionary.add(line.toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean contains(String term) {
        return dictionary.contains(term);
    }
}







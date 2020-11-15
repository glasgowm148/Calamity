package actors;

import java.io.*;

/**
 * Duplicated logic, to delete.
 * StopWords relies on currently.
 */

public class FileUtils {

    public static void findAndReplace(File file, String regex, String replacement) throws Exception {
        String fileAsStr = readFile(file.getAbsolutePath());
        writeToFile(findAndReplace(fileAsStr, regex, replacement), file);
    }

    public static String findAndReplace(String fileAsStr, String regex, String replacement) {
        return fileAsStr.replaceAll(regex, replacement);
    }

    public static String readFromStream(InputStream in) throws IOException {
        StringBuilder wsdlStr = new StringBuilder();

        int read;

        byte[] buf = new byte[1024];
        while ((read = in.read(buf)) > 0) {
            wsdlStr.append(new String(buf, 0, read));
        }
        in.close();
        return wsdlStr.toString();
    }

    public static String readFile(String file) throws Exception {
        FileInputStream in = new FileInputStream(file);
        byte[] content = new byte[in.available()];
        in.read(content);
        in.close();
        return new String(content);
    }

    public static void writeToFile(String data, File file) throws IOException {
        FileWriter fw = new FileWriter(file);

        // get the standard out of the application and write to file
        fw.write(data);
        fw.close();
    }


    public interface LineProcessor {
        boolean process(String line);
    }

    public static void doForEachLine(String file, LineProcessor processor) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;

        while ((line = reader.readLine()) != null) {
            boolean isContinue = processor.process(line);
            if (!isContinue) {
                break;
            }
        }
        reader.close();
    }

}
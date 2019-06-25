package helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileHelper {

    public static String readFile(String filePath) {
        BufferedReader bufferedReader = null;
        String line;
        String text = "";
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            text = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return text;
    }
}

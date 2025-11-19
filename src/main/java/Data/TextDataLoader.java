package Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextDataLoader {

    private static File getDataSubDirectory(String directory){
        String base = System.getProperty("user.dir");   // project root
        File folder = new File(base + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "Data" + File.separator + directory);
        return folder;
    }

    public static List<String> getAllSourceFileNames(String directory){
        File dataSubDir = getDataSubDirectory(directory);
        File[] files = dataSubDir.listFiles();

        ArrayList<String> fileNames = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                fileNames.add(file.getName());
            }
        }
        return fileNames;
    }

    public static <T extends LoadableFromText>
    List<T> load(String directory, String fileName, Class<T> type) throws Exception {

        List<T> list = new ArrayList<>();
        String dataSubDirPath = getDataSubDirectory(directory).getPath() + "/" + fileName;

        try (BufferedReader br = new BufferedReader(new FileReader(dataSubDirPath))) {

            // Read schema
            String headerLine = br.readLine();
            String[] headers = headerLine.trim().split("\\s*/\\s*");

            String line;
            while ((line = br.readLine()) != null) {
                if (line == null || line.trim().isEmpty()) continue;

                String[] parts = line.trim().split("\\s+");

                Map<String, String> values = new HashMap<>();

                // save the name of the file for discerning later
                String cleanedFileName = fileName.toLowerCase().split("\\.")[0];
                values.put("file name", cleanedFileName);

                for (int i = 0; i < headers.length && i < parts.length; i++) {
                    values.put(headers[i], parts[i]);
                }

                // Create and populate new object
                T obj = type.getDeclaredConstructor().newInstance();
                obj.loadFromMap(values);
                list.add(obj);
            }
        }

        return list;
    }
}


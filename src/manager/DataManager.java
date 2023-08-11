package manager;

import utility.Utility;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DataManager extends Manager{

    private String idPATH(String id){
        return "data/" + id + "_data.txt";
    }

    public boolean isExistingData(String id){
        String PATH = idPATH(id);
        if(fileExists(PATH)){
            return true;
        }
        else{
            return false;
        }
    }

    public void saveData(String id, String searchKey){
        String PATH = idPATH(id);
        Utility.createIfNotExist(PATH);
        Utility.createIfNotExist(ALL_PATH);
        searchKey = searchKey.replaceAll("%20", " ");
        Utility.writeAppend(PATH, searchKey);
        Utility.writeAppend(ALL_PATH, searchKey);
    }

    public String loadData(String id){
        String PATH = idPATH(id);
        String content = "";
        try{
            content = new String(Files.readAllBytes(Paths.get(PATH)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public String loadHotData(){
        try{
            Map<String, Integer> SearchCount = new HashMap<>();

            BufferedReader reader = new BufferedReader(new FileReader(ALL_PATH));
            String line;

            while ((line = reader.readLine()) != null) {
                if (SearchCount.containsKey(line)) {
                    // Increment count if search key already exists
                    int count = SearchCount.get(line);
                    SearchCount.put(line, count + 1);
                } else {
                    // Add line with count of 1 if it doesn't exist
                    SearchCount.put(line, 1);
                }
            }

            List<Map.Entry<String, Integer>> list = new ArrayList<>(SearchCount.entrySet());

            // Sort the list based on values in descending order
            list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            // Create a string to store top 10 (or less) search keys
            StringBuilder content = new StringBuilder();
            int count = 0;
            for (Map.Entry<String, Integer> entry : list) {
                content.append(entry.getKey()).append("\n");
                count++;
                if (count == 10) {
                    break;  // Stop after extracting the top 10 values
                }
            }
            return content.toString();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return ""; // No Hot data
    }
}

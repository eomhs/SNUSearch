package manager;

import utility.Utility;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AdminManager extends Manager {

    public void putLog(String log){
        Utility.createIfNotExist(LOG_PATH);
        Utility.writeAppend(LOG_PATH, log);
    }

    public String getAllUserId(){
        if (fileExists(USER_PATH)) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(USER_PATH));
                String line;
                StringBuilder content = new StringBuilder();

                // Read id and append it to content
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" ");
                    String id = parts[0];
                    content.append(id + "\n");
                }
                return content.toString(); // return content
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ""; // If no users found
    }

    public String getAllLog(){
        if (fileExists(LOG_PATH)) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(LOG_PATH));
                String line;
                StringBuilder content = new StringBuilder();

                // Read log and append it to content
                while ((line = reader.readLine()) != null) {
                    content.append(line + "\n");
                }
                return content.toString(); // return content
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ""; // If no log found
    }
}

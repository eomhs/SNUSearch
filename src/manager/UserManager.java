package manager;

import utility.Utility;
import java.io.*;

public class UserManager extends Manager{

    public boolean isExistingUser(String id, String pw, String PATH){
        // If file not exists, then the id not exists
        if (!fileExists(PATH)) {
            return false;
        }
        try{
            BufferedReader reader = new BufferedReader(new FileReader(PATH));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String existingId = parts[0];
                String existingPw = parts[1];
                // Only compare id case
                if(pw == null){
                    if(existingId.equalsIgnoreCase(id)) {
                        reader.close();
                        return true; // Found a matching ID
                    }
                }
                else{ // Both compare id and pw
                    if(existingId.equalsIgnoreCase((id)) && existingPw.equals(pw)){
                        reader.close();
                        return true; // Found a matching ID and PW
                    }
                }
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false; // No matching
    }

    public void addUser(String id, String pw, String PATH){
        Utility.createIfNotExist(PATH);
        Utility.writeAppend(PATH, id + " " + pw);
    }

    public void leaveUser(String id, String pw){
        removeUser(id, pw, USER_PATH);
        addUser(id, pw, LEAVE_PATH);
    }

    public void recoverUser(String id, String pw){
        removeUser(id, pw, LEAVE_PATH);
        addUser(id, pw, USER_PATH);
    }

    public void removeUser(String id, String pw, String PATH){
        String lineToRemove = id + " " + pw;
        try {
            // Create a temporary file
            File tempFile = new File("data/tmp.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            // Read the original file and copy its contents to the temporary file, excluding the line to remove
            BufferedReader reader = new BufferedReader(new FileReader(PATH));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.equals(lineToRemove)) {
                    writer.write(currentLine);
                    writer.newLine();
                }
            }
            reader.close();
            writer.close();

            // Replace the original file with the temporary file
            File originalFile = new File(PATH);
            originalFile.delete();
            tempFile.renameTo(originalFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package utility;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class Utility {

    private Utility(){}

    // Function that creates file if it not exists
    public static void createIfNotExist(String PATH){
        if (!Files.exists(Paths.get(PATH))) {
            try{
                File file = new File(PATH);
                file.createNewFile();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    // Function that write str to the end of the file
    public static void writeAppend(String PATH, String str){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(PATH, true));
            writer.write(str);
            writer.newLine();
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}

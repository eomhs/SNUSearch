package manager;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Manager {
    public final String USER_PATH = "data/UserInfo.txt";
    public final String LEAVE_PATH = "data/LeaveInfo.txt";
    public final String LOG_PATH = "data/LogInfo.txt";
    public final String ALL_PATH = "data/all_data.txt";

    public boolean fileExists(String PATH){
        if(Files.exists(Paths.get(PATH)))
            return true;
        else
            return false;
    }
}

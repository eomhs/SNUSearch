package program;

import manager.*;

public class AdminProgram extends LoginProgram {
    private AdminManager am;

    public AdminProgram(){
        super();
        am = new AdminManager();
    }

    public boolean isAdmin(){
        if(isLogin() && getId().equals("admin"))
            return true;
        else
            return false;
    }

    public void storeLog(String path){
        // Store only when log in
        if(isLogin()){
            String log = "[" + getId() + "]" + " http://localhost:8080" + path;
            am.putLog(log);
        }
    }

    public String loadAcc(){
        if(isAdmin())
            return am.getAllUserId();
        else
            return "";
    }

    public String loadLog(){
        if(isAdmin())
            return am.getAllLog();
        else
            return "";
    }

}

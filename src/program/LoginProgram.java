package program;

import manager.*;

public class LoginProgram {
    private String id;
    private String pw;
    private UserManager um;

    public LoginProgram(){
        id = null;
        pw = null;
        um = new UserManager();
    }

    public String getId(){
        return id;
    }

    public boolean join(String id, String pw){
        if (checkCondition(id, pw)){
            um.addUser(id, pw, um.USER_PATH);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean login(String id, String pw){
        if(um.isExistingUser(id, pw, um.USER_PATH)){
            this.id = id;
            this.pw = pw;
            return true;
        }
        return false;
    }

    public boolean isLogin(){
        if(id != null)
            return true;
        else
            return false;
    }

    public boolean logout(String id){
        if (this.id == null){
            return false;
        }
        else if(this.id.equalsIgnoreCase(id)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean leave(String id, String pw){
        if(um.isExistingUser(id, pw, um.USER_PATH)){
            um.leaveUser(id, pw);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean recover(String id, String pw){
        if(um.isExistingUser(id, pw, um.LEAVE_PATH)){
            um.recoverUser(id, pw);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean checkCondition(String id, String pw){
        if (um.isExistingUser(id, null, um.USER_PATH))
            return false;
        if (pw.length() < 4 || !pw.matches("^[a-zA-Z][a-zA-Z0-9@#%]*$"))
            return false;
        return true;
    }
}

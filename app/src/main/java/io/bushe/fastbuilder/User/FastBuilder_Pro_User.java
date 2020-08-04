package io.bushe.fastbuilder.User;

public class FastBuilder_Pro_User {

    static private FastBuilder_Pro_User fastBuilder_pro_user = null;
    private String done;
    private String username;
    private String cn_username;
    private String bd_username;
    private String message;

    public static FastBuilder_Pro_User getInstance() {
        if (fastBuilder_pro_user == null) {
            fastBuilder_pro_user = new FastBuilder_Pro_User();
        }
        return fastBuilder_pro_user;
    }

    public String getBd_username() {
        return bd_username;
    }

    public String getCn_username() {
        return cn_username;
    }

    public String getDone() {
        return done;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public void logOut() {
        done = null;
        username = null;
        bd_username = null;
        cn_username = null;
        message = null;
    }
}

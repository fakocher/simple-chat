package simplechat.server;

/**
 * Created by kimtaing on 11.05.18.
 */
public class Login {
    private String userEncryptedPassword;
    private String userSalt;
    private String userName;

    public String getUserEncryptedPassword() {
        return userEncryptedPassword;
    }

    public String getUserSalt() {
        return userSalt;
    }

    public String getUserName() {
        return userName;
    }

    public Login(String userName,String userSalt,String userEncryptedPassword) {
        this.userName=userName;
        this.userSalt=userSalt;
        this.userEncryptedPassword=userEncryptedPassword;
    }
}

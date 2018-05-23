package simplechat.server;

import simplechat.GlobalConstants;

/**
 * Created by kimtaing on 11.05.18.
 */
public class Login {

    private String userEncryptedPassword;
    private String userSalt;
    private String userName;
    private int nbFailedConnexion=0;
    private Boolean islocked=false;

    public int getNbFailedConnexion() {
        return nbFailedConnexion;
    }

    public void NbFailedConnexion_increment() {

        this.nbFailedConnexion = this.nbFailedConnexion+1;
        if (this.nbFailedConnexion>=GlobalConstants.MAX_NBFAILEDCONNEXION){
            this.islocked=true;
        }
    }

    public void NbFailedConnexion_tozero() {
        this.nbFailedConnexion = 0;
    }

    public Boolean getIslocked() {
        return islocked;
    }


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
        this.islocked=false;
    }
}

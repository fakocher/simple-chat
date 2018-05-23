package simplechat.server;

import simplechat.GlobalConstants;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Date : 11.05.18.
 * Description : module to manage authentication
 */
public class Authentication  {
    private Map<String, Login> userDatabase = new HashMap<String,Login>();

    // login with username and password
    public UUID login(String userName, String password) throws RemoteException {
        UUID uuid = UUID.randomUUID();
        Login user = userDatabase.get(userName);
        if (user.getIslocked()){
            return GlobalConstants.UIDLOCKED;
        }
        else {
            if (user == null) {
                return null;
            } else {
                try {
                    String salt = user.getUserSalt();
                    String calculatedHash = getEncryptedPassword(password, salt);
                    if (calculatedHash.equals(user.getUserEncryptedPassword())) {
                        user.NbFailedConnexion_tozero();
                        return uuid;
                    } else {
                        user.NbFailedConnexion_increment();
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    // signup to users base
    public boolean singUp(String userName, String password) throws RemoteException {
        try {
            String salt = getNewSalt();
            String encryptedPassword = getEncryptedPassword(password, salt);
            Login user = new Login(userName,salt,encryptedPassword);
            userDatabase.put(user.getUserName(),user);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // encrypt password
    public String getEncryptedPassword(String password, String salt) throws Exception {
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160; // for SHA1
        int iterations = 20000; // NIST specifies 10000
        String encryptedPassword=null;
        try {
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, derivedKeyLength);
            SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
            byte[] encBytes = f.generateSecret(spec).getEncoded();
            encryptedPassword=Base64.getEncoder().encodeToString(encBytes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return encryptedPassword;
    }

    // generate salt (seed for encrypt password
    public String getNewSalt() throws Exception {
        // generate
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}

package simplechat;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by kimtaing on 23.05.18.
 */
public class GlobalConstants {
    public static final UUID UIDLOCKED = new UUID(99999999, 99999999);
    public static final int MAX_NBFAILEDCONNEXION = 2;

    public static final String SSL_PATH = "C:\\ssl\\";

    //Maximal timestamp difference (in milliseconds)
    private static final int MAX_TIMESTAMP_DELTA = 30000;

    //Charset for hash encoding
    public static final String HASH_CHARSET = "UTF-8";

    //Hash algorithm to use
    public static final String HASH_FUNCTION = "SHA-256";

    //Length of the generated Hash
    private static final int HASH_LENGTH = 64;

    //Timestamp length
    private static final int TIMESTAMP_LENGTH = 13;

    public static boolean isVerified(String message)
    {
        Boolean res = false;

        try {
            //Comparing hashes
            String _rHash = message.substring(message.length() - HASH_LENGTH);

            byte[] receivedHash = message.substring(message.length() - HASH_LENGTH).getBytes(HASH_CHARSET);

            String msgTs = message.substring(0, message.length() - HASH_LENGTH);

            // checksum verification
            MessageDigest digest = MessageDigest.getInstance(HASH_FUNCTION);

            byte[] calculatedHash = digest.digest(msgTs.getBytes(HASH_CHARSET));

            String _cHash= convertByteArrayToHexString(calculatedHash);

            if (_cHash.equals(_rHash)) {

                long receivedTimestamp = Long.parseLong(message.substring(message.length() - (HASH_LENGTH + TIMESTAMP_LENGTH), message.length() - HASH_LENGTH));

                //Checking timestamp
                Date dt = new Date();
                long currTimestamp = dt.getTime();

                if (currTimestamp - receivedTimestamp < MAX_TIMESTAMP_DELTA) {
                    res = true;
                }
            }

            return res;

        }catch (UnsupportedEncodingException e){
            System.out.println("Fatal error: " + HASH_CHARSET + " character set not supported!");
            return false;
        }catch (NoSuchAlgorithmException e){
            System.out.println("Fatal error: " + HASH_FUNCTION + " not supported!");
            return false;
        }
    }

    public static String convertByteArrayToHexString(byte[] arrayBytes)
    {
        StringBuilder sb = new StringBuilder();

        for (byte b : arrayBytes)
        {
            sb.append(Integer.toString((b& 0xff) + 0x100, 16)
                    .substring(1));
        }

        return sb.toString();
    }

    public static String getVerifiableMessage(String message) throws UnsupportedEncodingException, NoSuchAlgorithmException
    {
        //Getting UTC date
        Date dt = new Date();
        long timestamp = dt.getTime();

        //Instantiating crypto helper
        MessageDigest messageDigest = MessageDigest.getInstance(HASH_FUNCTION);
        String msgWithTimestamp = message + String.valueOf(timestamp);

        //Computing sha-256 hash
        byte[] fullMessage = msgWithTimestamp.getBytes(HASH_CHARSET);
        byte[] digest = messageDigest.digest(fullMessage);

        String strDigest = GlobalConstants.convertByteArrayToHexString(digest);

        return msgWithTimestamp + strDigest;
    }

    public static String getOriginalMessage(String message)
    {
        int endIndex = message.length() - 77;

        return message.substring(0, endIndex);
    }
}

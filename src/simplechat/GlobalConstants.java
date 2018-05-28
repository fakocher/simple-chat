package simplechat;

import java.util.UUID;

/**
 * Created by kimtaing on 23.05.18.
 */
public class GlobalConstants {
    public static final UUID UIDLOCKED = new UUID(99999999, 99999999);
    public static final int MAX_NBFAILEDCONNEXION = 2;

    public static final String SSL_PATH = "C:\\ssl\\";

    //Maximal timestamp difference (in milliseconds)
    public static final String MAX_TIMESTAMP_DELTA = 3000;

    //Charset for hash encoding
    public static final String HASH_CHARSET = "UTF-8";

    //Hash algorithm to use
    public static final String HASH_FUNCTION = "SHA-256";

    //Length of the generated Hash
    public static final int HASH_LENGTH = 64;

    //Timestamp length
    public static final int TIMESTAMP_LENGTH = 13;

    public static boolean isVerified(String message) {
        Boolean res = false;

        try {
            //Comparing hashes
            String _rHash = message.substring(message.length() - HASH_LENGTH);

            byte[] receivedHash = message.substring(message.length() - HASH_LENGTH).getBytes(HASH_CHARSET);

            String msgTs = message.substring(0, message.length() - HASH_LENGTH);

            // checksum verification
            MessageDigest digest = MessageDigest.getInstance(HASH_FUNCTION);

            byte[] calculatedHash = digest.digest(msgTs.getBytes(HASH_CHARSET);

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
            System.out.println("Fatal error: %s character set not supported!", HASH_CHARSET);
            return false;
        }catch (NoSuchAlgorithmException e){
            System.out.println("Fatal error: %s not supported!", HASH_FUNCTION);
            return false;
        }
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
}


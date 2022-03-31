package rose.android.jlib.kit.data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by RoseHongXin on 2017/9/15 0015.
 */

public final class Encrypter {

    public static String MD5(String text) {
        if(text == null) text = "";
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(text.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        }
        return byts2hex(hash);
    }

    public static String SHA256(String text){
        if(text == null) text = "";
        byte[] hash;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(text.getBytes(StandardCharsets.UTF_8));
            hash = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, SHA-256 should be supported?", e);
        }
        return byts2hex(hash);
    }

    private static String byts2hex(byte[] byts){
        StringBuilder hex = new StringBuilder(byts.length * 2);
        for (byte b : byts) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}

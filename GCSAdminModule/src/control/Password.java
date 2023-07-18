/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Objects;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JPasswordField;

/**
 *
 * @author JC
 */
public class Password {

    public static SecretKeySpec createKey(String key) {
        try {
            byte[] text = key.getBytes("ISO-8859-1");
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            text = md.digest(text);
            text = Arrays.copyOf(text, 16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(text, "AES");
            return secretKeySpec;
        } catch (Exception e) {
            return null;
        }
    }

    public static String encryptPassword(String password) {
        try {
            SecretKeySpec secretKeySpec = createKey(String.valueOf(Objects.hashCode(password)));
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] text = password.getBytes("ISO-8859-1");
            byte[] encrypt = cipher.doFinal(text);
            return Base64.encode(encrypt);
        } catch (Exception e) {
            return null;
        }
    }
}

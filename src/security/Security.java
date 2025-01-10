/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 2dam
 */
public class Security {
    private static final Logger log = Logger.getLogger(Security.class.getName());
    public static String hashText(String text) {
        try {
            //Se instancia la clase que hashea la contrasena
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //Se hashea a un array de bytes
            byte[] hashBytes = md5.digest(text.getBytes());
            
            //Se convierte de una array de bytes a un valor String
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = String.format("%02X", b);
                hexStringBuilder.append(hex);
            }

            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException ex) {
            log.log(Level.SEVERE, "Problem while hashing the password",
                    ex.getMessage());
            return null;
        }
    }
}

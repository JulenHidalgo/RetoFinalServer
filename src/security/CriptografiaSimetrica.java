/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.ResourceBundle;
import javax.crypto.BadPaddingException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CriptografiaSimetrica {
    private static byte[] salt = "palabras aleatorias".getBytes(); 
    public static String cifrarTexto(String clave, String mensaje) {
        String ret = null;
        KeySpec derivedKey = null;
        SecretKeyFactory secretKeyFactory = null;
        StringBuilder hexStringBuilder = null;
        try {
            derivedKey = new PBEKeySpec(clave.toCharArray(), salt, 65536, 128); 
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
             
            byte[] derivedKeyPBK = secretKeyFactory.generateSecret(derivedKey).getEncoded();
                     
            SecretKey derivedKeyPBK_AES = new SecretKeySpec(derivedKeyPBK, 0, derivedKeyPBK.length, "AES");
          
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");	    
            cipher.init(Cipher.ENCRYPT_MODE, derivedKeyPBK_AES);
            byte[] encodedMessage = cipher.doFinal(mensaje.getBytes()); // Mensaje cifrado !!!
            byte[] iv = cipher.getIV(); // vector de inicializaci�n  
             
            // Añadimos el vector de inicialización
            byte[] combined = concatArrays(iv, encodedMessage);

            hexStringBuilder = new StringBuilder();
            for (byte b : combined) {
                String hex = String.format("%02X", b);
                hexStringBuilder.append(hex);
            }

        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            
        }
        return hexStringBuilder.toString();
    }
    
    public static String descifrarTexto(String clave, String fileProp) {
        String ret = null;
        ResourceBundle fichConf = ResourceBundle.getBundle("smtp.smtpCredentials");
        String email = fichConf.getString(fileProp);
         System.out.println(email);
        byte[] fileContent = new byte[email.length() / 2];
        for (int i = 0; i < fileContent.length; i++) {
           int index = i * 2;
           int j = Integer.parseInt(email.substring(index, index + 2), 16);
           fileContent[i] = (byte) j;
        }
        // Fichero leído
        KeySpec keySpec = null;
        SecretKeyFactory secretKeyFactory = null;
        try {
            // Creamos un SecretKey usando la clave + salt
            keySpec = new PBEKeySpec(clave.toCharArray(), salt, 65536, 128); // AES-128
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
            SecretKey privateKey = new SecretKeySpec(key, 0, key.length, "AES");

            // Creamos un Cipher con el algoritmos que vamos a usar
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParam = new IvParameterSpec(Arrays.copyOfRange(fileContent, 0, 16)); // La IV est� aqu�
            cipher.init(Cipher.DECRYPT_MODE, privateKey, ivParam);
            byte[] decodedMessage = cipher.doFinal(Arrays.copyOfRange(fileContent, 16, fileContent.length));
            ret = new String(decodedMessage);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            
        }
        return ret;
    }
    
    private static byte[] concatArrays(byte[] array1, byte[] array2) {
        byte[] ret = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, ret, 0, array1.length);
        System.arraycopy(array2, 0, ret, array1.length, array2.length);
        return ret;
    }
}
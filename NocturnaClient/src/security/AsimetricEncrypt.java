/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

/**
 * Clase que proporciona métodos para encriptar datos utilizando criptografía asimétrica (RSA).
 * 
 * @author Adrian Rocha
 */
public class AsimetricEncrypt {
    /**
     * Encripta los datos proporcionados utilizando una clave pública RSA.
     * 
     * @param data Los datos a encriptar.
     * @return Los datos encriptados en formato Base64, o null si ocurre un error.
     * @throws FileNotFoundException Si no se encuentra el archivo de la clave pública.
     */
    public static String encrypt (String data) {
        try {
            // Cargar la clave pública desde un recurso del classpath
            byte[] publicKeyBytes;
            try (InputStream keyInputStream = AsimetricEncrypt.class.getResourceAsStream("RSA_Public.key");
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                if (keyInputStream == null) {
                    throw new FileNotFoundException("No se encontró el archivo de clave pública.");
                }
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = keyInputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                publicKeyBytes = baos.toByteArray();
            }

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            
            return javax.xml.bind.DatatypeConverter.printBase64Binary(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }
}

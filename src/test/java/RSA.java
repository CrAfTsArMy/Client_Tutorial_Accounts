import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class RSA {

    public static void main(String[] args) {
        RSA rsa = new RSA();
        try {
            String encrypted = rsa.encrypt("Hallo Mein Name ist Günther");
            String decrypted = rsa.decrypt(encrypted);
            System.out.println(encrypted);
            System.out.println(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RSA() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(4096);
            KeyPair pair = generator.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (Exception ignored) {
        }
    }

    public String encrypt(String message) throws Exception {
        byte[] bytes = message.getBytes();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(bytes);
        return encode(encrypted);
    }

    private String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public String decrypt(String message) throws Exception {
        byte[] encrypted = decode(message.getBytes());
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(encrypted);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private byte[] decode(byte[] bytes) {
        return Base64.getDecoder().decode(bytes);
    }

}

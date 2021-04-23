package simpleblockchain;

import lombok.experimental.UtilityClass;

import java.math.BigInteger;
import java.security.*;
import java.util.Base64;

@UtilityClass
public class SigningHelper {

    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return generator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String hash(String aString) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(aString.getBytes());
            return String.format("%064x", new BigInteger(1, messageDigest.digest()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] sign(String aString, PrivateKey privateKey) {
        try {
            Signature rsa = Signature.getInstance("SHA256withRSA");
            rsa.initSign(privateKey);
            rsa.update(aString.getBytes());
            return rsa.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifySignature(String aString, PublicKey publicKey, byte[] signature) {
        try {
            Signature rsa = Signature.getInstance("SHA256withRSA");
            rsa.initVerify(publicKey);
            rsa.update(aString.getBytes());
            return rsa.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String encodeToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

}

package modele;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPoint;

/**
 * Classe pour un participant de l'échange
 */
public class Participant {
    private String name;
    private KeyPair keyPair;
    private byte[] sharedSecret;

    /**
     * Constructeur d'un participant
     * @param name Son nom
     */
    public Participant(String name) {
        this.name = name;
    }

    /**
     * Setter de la paire de clé
     * @param keyPair KeyPair
     */
    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    /**
     * Getter de la pair de clé
     * @return KeyPair
     */
    public KeyPair getKeyPair() {
        return keyPair;
    }

    /**
     * Getter de la clé publique
     * @return PublicKey
     */
    public PublicKey getPublicKey() {
        return keyPair == null ? null : keyPair.getPublic();
    }

    /**
     * Getter de la clé publique x
     * @return BigInteger
     */
    public BigInteger getPubX() {
        if (keyPair == null) return null;
        ECPublicKey pubKey = (ECPublicKey) keyPair.getPublic();
        ECPoint point = pubKey.getW();
        return point.getAffineX();
    }

    /**
     * Getter de la clé publique Y
     * @return BigInteger
     */
    public BigInteger getPubY() {
        if (keyPair == null) return null;
        ECPublicKey pubKey = (ECPublicKey) keyPair.getPublic();
        ECPoint point = pubKey.getW();
        return point.getAffineY();
    }

    /**
     * Setter du secret partagé
     * @param secret byte[]
     */
    public void setSharedSecret(byte[] secret) {
        this.sharedSecret = secret;
    }

    /**
     * Getter du secret partagé
     * @return sharedSecret
     */
    public byte[] getSharedSecret() {
        return sharedSecret;
    }

    /**
     * Getter de la clé privé
     * @return boolean
     */
    public java.security.PrivateKey getPrivateKey() {
        return keyPair == null ? null : keyPair.getPrivate();
    }
    
    /**
     * Getter du nom du participant
     * @return nom String
     */
    public String getName() {
        return name;
    }

}

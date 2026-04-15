package controleur;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Classe encapsulant tous les paramètres du protocole Diffie-Hellman
 */
public class DHParameters {
    private BigInteger p;  // Nombre premier
    private BigInteger g;  // Générateur
    private BigInteger a;  // Secret Alice
    private BigInteger b;  // Secret Bob
    private BigInteger publicA;  // Clé publique Alice (A = g^a mod p)
    private BigInteger publicB;  // Clé publique Bob (B = g^b mod p)
    private BigInteger sharedSecretAlice;  // Secret calculé par Alice
    private BigInteger sharedSecretBob;    // Secret calculé par Bob
    
    private final boolean realisticMode;
    
    /**
     * Constructeur des parametres de diffie-hellman
     * @param p Nombre publique
     * @param g Nombre du générateur
     * @param a Nombre de Alice
     * @param b Nombre de Bob
     * @param realisticMode 
     */
    private DHParameters(BigInteger p, BigInteger g, BigInteger a, BigInteger b, boolean realisticMode) {
        this.p = p;
        this.g = g;
        this.a = a;
        this.b = b;
        this.realisticMode = realisticMode;
        calculatePublicKeys();
        calculateSharedSecrets();
    }
    
    /**
     * Calcule des clés publiques
     */
    private void calculatePublicKeys() {
        this.publicA = g.modPow(a, p);
        this.publicB = g.modPow(b, p);
    }
    
    /**
     * Calcule du secret partagé
     */
    private void calculateSharedSecrets() {
        this.sharedSecretAlice = publicB.modPow(a, p);
        this.sharedSecretBob = publicA.modPow(b, p);
    }
    
    /**
     * Formate un nombre pour l'affichage (tronque si mode réaliste)
     * @param num Le nombre à transformé
     * @return La taille du nombre
     */
    public String formatNumber(BigInteger num) {
        if (!realisticMode || num.bitLength() < 64) {
            return num.toString();
        }
        
        // Pour les grands nombres, afficher début...fin
        String full = num.toString();
        if (full.length() > 30) {
            return full.substring(0, 15) + "..." + full.substring(full.length() - 15) +
                   " (" + full.length() + " chiffres)";
        }
        return full;
    }
    
    // ========== Getters ==========
    /**
     * Getter du nombre P
     * @return p
     */
    public BigInteger getP() { 
        return p; 
    }
    
    /**
     * Getter du nombre G
     * @return g
     */
    public BigInteger getG() { 
        return g; 
    }
    
    /**
     * Getter du nombre A de Alice
     * @return a
     */
    public BigInteger getA() { 
        return a; 
    }
    
    /**
     * Getter du nombre B de Bob
     * @return b
     */
    public BigInteger getB() { 
        return b; 
    }
    
    /**
     * Getter de la public key A
     * @return publicA
     */
    public BigInteger getPublicA() { 
        return publicA; 
    }
    
    /**
     * Getter de la public key B
     * @return publicB
     */
    public BigInteger getPublicB() { 
        return publicB; 
    }
    
    /**
     * Getter du secret partagé de Alice
     * @return sharedSecretAlice
     */
    public BigInteger getSharedSecretAlice() { 
        return sharedSecretAlice; 
    }
    
    /**
     * Getter du secret partagé de Bob
     * @return BigInteger
     */
    public BigInteger getSharedSecretBob() { 
        return sharedSecretBob; 
    }
    
    /**
     * Getter pour savoir si l'on est en mode réaliste
     * @return boolean
     */
    public boolean isRealisticMode() { 
        return realisticMode; 
    }
    
    // ========== Factory Methods ==========
    
    /**
     * Créateur des paramètres de démo
     * @return DHParameters parametre de Diffie-Hellman
     */
    public static DHParameters createDemoParameters() {
        BigInteger p = BigInteger.valueOf(23);
        BigInteger g = BigInteger.valueOf(5);
        BigInteger a = BigInteger.valueOf(6);
        BigInteger b = BigInteger.valueOf(15);
        return new DHParameters(p, g, a, b, false);
    }
    
    /**
     * Génère des paramètres réalistes (grands nombres cryptographiques)
     * @param random Nomnre random sécurisé
     * @return DHParameters parametre de Diffie-Hellman
     */
    public static DHParameters createRealisticParameters(SecureRandom random) {
        // Générer un nombre premier sûr de 512 bits
        BigInteger p = generateSafePrime(512, random);
        BigInteger g = BigInteger.valueOf(2);  // Générateur standard
        
        // Secrets privés de 256 bits
        BigInteger a = new BigInteger(256, random);
        BigInteger b = new BigInteger(256, random);
        
        return new DHParameters(p, g, a, b, true);
    }
    
    /**
     * Génère un nombre premier sûr (p = 2q + 1 où q est aussi premier)
     * @param bitLength Taille du bit
     * @param random Random secure
     * @return Nombre premier
     */
    private static BigInteger generateSafePrime(int bitLength, SecureRandom random) {
        BigInteger p, q;
        do {
            // Générer q premier de (bitLength-1) bits
            q = BigInteger.probablePrime(bitLength - 1, random);
            // Calculer p = 2q + 1
            p = q.multiply(BigInteger.TWO).add(BigInteger.ONE);
        } while (!p.isProbablePrime(50)); // Vérifier que p est aussi premier
        
        return p;
    }
}
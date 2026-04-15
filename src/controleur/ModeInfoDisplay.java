package controleur;

import vue.MainVue;

/**
 * Classe responsable de l'affichage des informations sur le mode actuel
 */
public class ModeInfoDisplay {
    private final MainVue view;
    private final DHParameters params;
    private final boolean realisticMode;

    /**
     * Constructeur du mode avec les infos
     * @param view Vue
     * @param params Paramètres de Diffie-Hellman
     * @param realisticMode boolean
     */
    public ModeInfoDisplay(MainVue view, DHParameters params, boolean realisticMode) {
        this.view = view;
        this.params = params;
        this.realisticMode = realisticMode;
    }

    /**
     * Affiche les informations sur le mode actuel
     */
    public void displayModeInfo() {
        String bitSize = realisticMode ? String.valueOf(params.getP().bitLength()) : "petit";
        String securityLevel = realisticMode ?
                "Niveau de sécurité : ~" + (params.getP().bitLength() / 2) + " bits" :
                "Niveau de sécurité : Pédagogique uniquement !";

        view.dhStepsArea.setText(String.format("""
              CONFIGURATION ACTUELLE
            ═══════════════════════════════════════
            
            Mode : %s
            
              Taille des nombres :
            • p : %s bits (%s chiffres décimaux)
            • g : %s
            • a : %s bits (secret Alice)
            • b : %s bits (secret Bob)
            
            %s
            
               Temps de calcul estimé : %s
            
              En mode réaliste, les calculs prennent plus de temps
               mais correspondent aux standards cryptographiques actuels.
            
              Standards industriels :
               • TLS 1.2 : 2048 bits minimum
               • TLS 1.3 : Préfère ECDH (courbes elliptiques)
               • Banques : 2048-4096 bits
            
            Cliquez sur "Étape suivante" pour commencer la démonstration.
            """,
                realisticMode ? "  Réaliste" : "  Pédagogique",
                bitSize,
                realisticMode ? params.getP().toString().length() : params.getP(),
                params.getG(),
                realisticMode ? params.getA().bitLength() : params.getA(),
                realisticMode ? params.getB().bitLength() : params.getB(),
                securityLevel,
                realisticMode ? "~1-3 secondes par calcul" : "Instantané"
        ));

        view.exchangeArea.setText(realisticMode ? """
               MODE RÉALISTE ACTIVÉ
            
            Les nombres utilisés sont de taille cryptographique
            réelle.
            
              Exemple de valeurs (tronquées pour affichage) :
            
            p ≈ 1340...7891 (nombre premier sûr)
            g = 2 (générateur standard)
            
            Ces valeurs sont générées aléatoirement et changent
            à chaque génération.
            
              Conseil : Utilisez le mode automatique pour une
               démonstration fluide avec ces grands nombres.
            """ : """
              MODE PÉDAGOGIQUE ACTIF
            
            Les petits nombres (p=23, g=5) permettent de :
            • Suivre les calculs à la main
            • Comprendre le principe
            • Vérifier chaque étape
            
            Pour une expérience plus réaliste, passez en
            mode réaliste avec le bouton ci-dessus.
            """);
    }

    /**
     * Affiche le message d'accueil initial
     */
    public void displayInitialInfo() {
        view.dhStepsArea.setText("""
              BIENVENUE DANS LA DÉMO DIFFIE-HELLMAN
            
              Mode pédagogique : petits nombres faciles à suivre
              Mode réaliste : nombres cryptographiques réels
            
            Cliquez sur "Étape suivante" pour suivre le protocole pas à pas,
            ou "Démo DH complète" pour voir toutes les étapes.
            
              Astuce : Observez les panneaux colorés d'Alice et Bob !
            """);
    }
}
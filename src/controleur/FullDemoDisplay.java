package controleur;

import vue.MainVue;

/**
 * Classe responsable de l'affichage de la démonstration complète
 */
public class FullDemoDisplay {
    private final MainVue view;
    private final DHParameters params;
    private final boolean realisticMode;

    /**
     * Constructeur du mode full demo
     * @param view Vue
     * @param params Parametres de Diffie-Hellman
     * @param realisticMode boolean
     */
    public FullDemoDisplay(MainVue view, DHParameters params, boolean realisticMode) {
        this.view = view;
        this.params = params;
        this.realisticMode = realisticMode;
    }

    /**
     * Affiche la démonstration complète du protocole DH
     */
    public void displayFullDemo() {
        // Mise à jour des panneaux visuels
        view.updateAliceSecret(params.formatNumber(params.getA()));
        view.updateAlicePublic(params.formatNumber(params.getPublicA()));
        view.updateBobSecret(params.formatNumber(params.getB()));
        view.updateBobPublic(params.formatNumber(params.getPublicB()));
        view.updateSharedSecret(params.formatNumber(params.getSharedSecretAlice()));

        String modeWarning = realisticMode ?
                "\nMODE RÉALISTE : Les nombres sont tronqués pour l'affichage\n" +
                        "    Seuls les premiers et derniers chiffres sont montrés.\n" : "";

        // Affichage des étapes
        view.dhStepsArea.setText(String.format("""
            PROTOCOLE DIFFIE-HELLMAN COMPLET
            %s
            ═══════════════════════════════════════
               PHASE 1 : INITIALISATION
            ═══════════════════════════════════════
            
               Paramètres publics convenus :
               • p = %s (%d bits)
               • g = %s
            
            ═══════════════════════════════════════
              PHASE 2 : SECRETS PRIVÉS
            ═══════════════════════════════════════
            
              Alice choisit son secret privé :
               • a = %s (%d bits)
            
              Bob choisit son secret privé :
               • b = %s (%d bits)
            
            ═══════════════════════════════════════
              PHASE 3 : CALCULS PUBLICS
            ═══════════════════════════════════════
            
              Alice calcule sa clé publique :
               A = g^a mod p
               A = %s
            
              Bob calcule sa clé publique :
               B = g^b mod p
               B = %s
            
            """,
                modeWarning,
                params.formatNumber(params.getP()), params.getP().bitLength(),
                params.formatNumber(params.getG()),
                params.formatNumber(params.getA()), params.getA().bitLength(),
                params.formatNumber(params.getB()), params.getB().bitLength(),
                params.formatNumber(params.getPublicA()),
                params.formatNumber(params.getPublicB())
        ));

        // Affichage de l'échange
        view.exchangeArea.setText(String.format("""
              ÉCHANGE DES CLÉS PUBLIQUES
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            
              Alice envoie publiquement : A = %s
               └─→  [Internet] →─┐
                                    ↓
              Bob reçoit : A = %s
            
              Bob envoie publiquement : B = %s
               └─→  [Internet] →─┐
                                    ↓
              Alice reçoit : B = %s
            
               Ces valeurs sont PUBLIQUES !
            
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
              CALCUL DU SECRET PARTAGÉ
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            
              Alice calcule : s = B^a mod p = %s 
              Bob calcule : s = A^b mod p = %s 
            
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
              VÉRIFICATION FINALE
            ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
            
            %s LES SECRETS SONT IDENTIQUES !
            
            Secret partagé : %s
            
              Communication sécurisée établie !
            
            %s
            """,
                params.formatNumber(params.getPublicA()),
                params.formatNumber(params.getPublicA()),
                params.formatNumber(params.getPublicB()),
                params.formatNumber(params.getPublicB()),
                params.formatNumber(params.getSharedSecretAlice()),
                params.formatNumber(params.getSharedSecretBob()),
                params.getSharedSecretAlice().equals(params.getSharedSecretBob()) ? "✅" : "❌",
                params.formatNumber(params.getSharedSecretAlice()),
                realisticMode ?
                        "  Ces nombres de " + params.getP().bitLength() + " bits sont impossibles\n" +
                                "   à casser avec la technologie actuelle !" :
                        " "
        ));

        view.statusLabel.setText("Démo complète affichée");
    }
}
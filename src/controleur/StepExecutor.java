package controleur;

import vue.MainVue;

/**
 * Classe responsable de l'exécution des étapes du protocole Diffie-Hellman
 */
public class StepExecutor {
    private final MainVue view;
    private final DHParameters params;
    private final boolean realisticMode;

    /**
     * Constructeur du step executor
     * @param view Vue
     * @param params Paramètres de Diffie-Hellman
     * @param realisticMode Savoir si l'on est dans le mode réaliste
     */
    public StepExecutor(MainVue view, DHParameters params, boolean realisticMode) {
        this.view = view;
        this.params = params;
        this.realisticMode = realisticMode;
    }


    /**
     * Exécute une étape spécifique du protocole
     * @param step Etape du protocole
     */
    public void executeStep(int step) {
        view.resetHighlights();

        switch (step) {
            case 0 -> executeStep1_Parameters();
            case 1 -> executeStep2_AliceSecret();
            case 2 -> executeStep3_BobSecret();
            case 3 -> executeStep4_AlicePublic();
            case 4 -> executeStep5_BobPublic();
            case 5 -> executeStep6_Exchange();
            case 6 -> executeStep7_AliceShared();
            case 7 -> executeStep8_BobShared();
            case 8 -> executeStep9_Verification();
        }
    }

    private void executeStep1_Parameters() {
        view.dhStepsArea.setText(String.format("""
              ÉTAPE 1 : PARAMÈTRES PUBLICS
            ═══════════════════════════════════
            
            Alice et Bob se mettent d'accord sur :
            
              p = %s
               Nombre premier %s de %d bits
               → Définit l'espace de calcul
            
              g = %s (générateur)
               → Base pour les calculs
            
               Ces valeurs sont PUBLIQUES.
            
            %s
            """,
                params.formatNumber(params.getP()),
                realisticMode ? "sûr" : "",
                params.getP().bitLength(),
                params.formatNumber(params.getG()),
                realisticMode ?
                        "  En mode réaliste, p est un nombre premier sûr\n" +
                                "   (p = 2q + 1 où q est aussi premier)\n" +
                                "   Cela garantit un groupe cyclique d'ordre q."
                        :""
        ));
        view.exchangeArea.setText("  Paramètres publiés...");
        view.statusLabel.setText("Étape 1/9");
    }

    private void executeStep2_AliceSecret() {
        view.highlightAlice();
        view.updateAliceSecret(params.formatNumber(params.getA()));
        view.dhStepsArea.append(String.format("""
            
              ÉTAPE 2 : SECRET D'ALICE
            ═══════════════════════════════════
            
            Alice choisit SECRÈTEMENT :
            a = %s
            Taille : %d bits
            
              Ce nombre est PRIVÉ !
            
            %s
            """,
                params.formatNumber(params.getA()),
                params.getA().bitLength(),
                realisticMode ?
                        "  En mode réaliste, 'a' est un nombre aléatoire\n" +
                                "   de 256 bits minimum, généré par un générateur\n" +
                                "   cryptographiquement sûr (SecureRandom)."
                        : " "
        ));
        view.exchangeArea.setText("  Alice garde son secret...");
        view.statusLabel.setText("Étape 2/9");
    }

    private void executeStep3_BobSecret() {
        view.highlightBob();
        view.updateBobSecret(params.formatNumber(params.getB()));
        view.dhStepsArea.append(String.format("""
            
              ÉTAPE 3 : SECRET DE BOB
            ═══════════════════════════════════
            
            Bob choisit SECRÈTEMENT :
            b = %s
            Taille : %d bits
            
              Ce nombre est PRIVÉ !
            """,
                params.formatNumber(params.getB()),
                params.getB().bitLength()
        ));
        view.exchangeArea.setText("  Bob garde son secret...");
        view.statusLabel.setText("Étape 3/9");
    }

    private void executeStep4_AlicePublic() {
        view.highlightAlice();
        view.updateAlicePublic(params.formatNumber(params.getPublicA()));
        view.dhStepsArea.append(String.format("""
            
              ÉTAPE 4 : CLÉ PUBLIQUE D'ALICE
            ═══════════════════════════════════
            
            Alice calcule :
            A = g^a mod p
            A = %s
            
            %s
            
              Alice partagera A publiquement.
            """,
                params.formatNumber(params.getPublicA()),
                realisticMode ?
                        "   Calcul effectué en quelques millisecondes\n" +
                                "   grâce à l'exponentiation modulaire rapide."
                        : "  Calcul : 5^6 mod 23 = 15625 mod 23 = 15"
        ));
        view.exchangeArea.setText(String.format("  Alice calcule A = %s",
                params.formatNumber(params.getPublicA())));
        view.statusLabel.setText("Étape 4/9");
    }

    private void executeStep5_BobPublic() {
        view.highlightBob();
        view.updateBobPublic(params.formatNumber(params.getPublicB()));
        view.dhStepsArea.append(String.format("""
            
              ÉTAPE 5 : CLÉ PUBLIQUE DE BOB
            ═══════════════════════════════════
            
            Bob calcule :
            B = g^b mod p
            B = %s
            
              Bob partagera B publiquement.
            """,
                params.formatNumber(params.getPublicB())
        ));
        view.exchangeArea.setText(String.format("  Bob calcule B = %s",
                params.formatNumber(params.getPublicB())));
        view.statusLabel.setText("Étape 5/9");
    }

    private void executeStep6_Exchange() {
        view.dhStepsArea.append("""
            
            🔄 ÉTAPE 6 : ÉCHANGE PUBLIC
            ═══════════════════════════════════
            
            Alice et Bob échangent leurs clés
            publiques via Internet.
            
               Un attaquant voit tout !
            """);
        view.exchangeArea.setText(String.format("""
              ÉCHANGE EN COURS...
            
              Alice → Public → Bob : A = %s
              Bob → Public → Alice : B = %s
            
            👁️  L'espion voit : p, g, A, B
                Mais ne peut pas retrouver a ou b !
            
            %s
            """,
                params.formatNumber(params.getPublicA()),
                params.formatNumber(params.getPublicB()),
                realisticMode ?
                        "  Avec des nombres de " + params.getP().bitLength() + " bits,\n" +
                                "   casser ce protocole prendrait des millions\n" +
                                "   d'années avec les ordinateurs actuels !"
                        : " La sécurité repose sur la difficulté du\n" +
                        "   problème du logarithme discret."
        ));
        view.statusLabel.setText("Étape 6/9");
    }

    private void executeStep7_AliceShared() {
        view.highlightAlice();
        view.dhStepsArea.append(String.format("""
            
             ÉTAPE 7 : ALICE CALCULE LE SECRET
            ═══════════════════════════════════
            
            Alice calcule : s = B^a mod p
            s = %s
            """,
                params.formatNumber(params.getSharedSecretAlice())
        ));
        view.exchangeArea.append(String.format("\n\n Alice obtient : s = %s",
                params.formatNumber(params.getSharedSecretAlice())));
        view.statusLabel.setText("Étape 7/9");
    }

    private void executeStep8_BobShared() {
        view.highlightBob();
        view.dhStepsArea.append(String.format("""
            
             ÉTAPE 8 : BOB CALCULE LE SECRET
            ═══════════════════════════════════
            
            Bob calcule : s = A^b mod p
            s = %s
            """,
                params.formatNumber(params.getSharedSecretBob())
        ));
        view.exchangeArea.append(String.format("\n Bob obtient : s = %s",
                params.formatNumber(params.getSharedSecretBob())));
        view.statusLabel.setText("Étape 8/9");
    }

    private void executeStep9_Verification() {
        view.updateSharedSecret(params.formatNumber(params.getSharedSecretAlice()));
        boolean valid = params.getSharedSecretAlice().equals(params.getSharedSecretBob());

        view.dhStepsArea.append(String.format("""
            
            %s ÉTAPE 9 : VÉRIFICATION
            ═══════════════════════════════════
            
            Secret d'Alice : %s
            Secret de Bob  : %s
            
            %s LES SECRETS SONT IDENTIQUES !
            
             Secret partagé : %s
            
            %s
            """,
                valid ? "✅" : "❌",
                params.formatNumber(params.getSharedSecretAlice()),
                params.formatNumber(params.getSharedSecretBob()),
                valid ? "✅" : "❌",
                params.formatNumber(params.getSharedSecretAlice()),
                realisticMode ?
                        " Ce secret de " + params.getSharedSecretAlice().bitLength() + " bits peut être utilisé\n" +
                                "   pour générer des clés AES-256 pour chiffrer\n" +
                                "   les communications. C'est exactement ce que\n" +
                                "   fait HTTPS lors de l'établissement d'une\n" +
                                "   connexion TLS !"
                        : ""
        ));

        view.exchangeArea.append(String.format("""
            
            
            ═══════════════════════════════════
             PROTOCOLE TERMINÉ !
            ═══════════════════════════════════
            
            %s
            """,
                realisticMode ?
                        " MODE RÉALISTE VALIDÉ !\n\n" +
                                "Vous venez de simuler un échange de clés\n" +
                                "Diffie-Hellman avec des paramètres de\n" +
                                "sécurité cryptographique réels !\n\n" +
                                "Taille de p : " + params.getP().bitLength() + " bits\n" +
                                "Niveau de sécurité : ~" + (params.getP().bitLength()/2) + " bits\n\n" +
                                " Infaisable à casser avec la technologie actuelle !"
                        : " MODE PÉDAGOGIQUE\n\n" +
                        "Vous avez compris le principe !\n" +
                        "Essayez maintenant le mode réaliste pour voir\n" +
                        "le protocole avec des vraies valeurs cryptographiques."
        ));

        view.statusLabel.setText("✅ Étape 9/9 : Terminé !");
    }
}
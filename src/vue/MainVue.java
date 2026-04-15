package vue;

import java.awt.*;
import javax.swing.*;

public class MainVue extends JPanel {
    // Boutons de contrôle
    public final JButton btnStepByStep = new JButton("Étape suivante");
    public final JButton btnDHFull = new JButton("Démo DH complète");
    public final JButton btnAutoDemo = new JButton("Mode automatique");
    public final JButton btnClear = new JButton("Réinitialiser");
    
    // Boutons pour le mode réaliste
    public final JButton btnRealisticMode = new JButton("Passer en mode réaliste");
    public final JButton btnGenerateNew = new JButton("Nouvelles valeurs");
    public final JLabel modeLabel = new JLabel("Mode : Pédagogique");
    
    // Onglets
    public final JTabbedPane tabbedPane = new JTabbedPane();
    
    // Labels et zones de texte
    public final JLabel statusLabel = new JLabel("Prêt.");
    public final JTextArea dhStepsArea = new JTextArea(20, 30);
    public final JTextArea exchangeArea = new JTextArea(20, 30);
    
    // Panneaux visuels
    public final JPanel alicePanel = new JPanel();
    public final JPanel bobPanel = new JPanel();
    public final JPanel publicSpacePanel = new JPanel();
    
    // Labels pour affichage dynamique
    public final JLabel aliceSecretLabel = new JLabel("Secret: ?");
    public final JLabel alicePublicLabel = new JLabel("Public: ?");
    public final JLabel bobSecretLabel = new JLabel("Secret: ?");
    public final JLabel bobPublicLabel = new JLabel("Public: ?");
    public final JLabel sharedSecretLabel = new JLabel("Secret partagé: ?");
    
    // Panels pour les courbes elliptiques
    public final CourbeElliptiquePanel ecPanel = new CourbeElliptiquePanel();
    public final ECOperationsPanel ecOperationsPanel = new ECOperationsPanel();
    
    public MainVue() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel du haut avec contrôles
        JPanel topPanel = createTopPanel();
        
        // Panel central avec onglets
        JPanel dhClassicPanel = createDHClassicPanel();
        JPanel ecdhPanel = createECDHPanel();
        
        tabbedPane.addTab("Diffie-Hellman Classique", dhClassicPanel);
        tabbedPane.addTab("Diffie-Hellman sur Courbe Elliptique", ecdhPanel);
        
        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Contrôles"));
        
        // Panel des boutons principaux
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        btnStepByStep.setToolTipText("Avancer d'une étape dans la démonstration");
        btnDHFull.setToolTipText("Afficher toutes les étapes d'un coup");
        btnAutoDemo.setToolTipText("Démonstration automatique avec pauses");
        btnClear.setToolTipText("Recommencer depuis le début");
        
        buttonsPanel.add(btnStepByStep);
        buttonsPanel.add(btnDHFull);
        buttonsPanel.add(btnAutoDemo);
        buttonsPanel.add(btnClear);
        
        // Séparateur visuel
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(2, 30));
        buttonsPanel.add(separator);
        
        // Boutons du mode réaliste
        btnRealisticMode.setToolTipText("Basculer entre mode pédagogique et mode réaliste");
        btnRealisticMode.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnRealisticMode.setBackground(new Color(255, 230, 150));
        
        btnGenerateNew.setToolTipText("Générer de nouvelles valeurs aléatoires réalistes");
        btnGenerateNew.setEnabled(false);
        
        buttonsPanel.add(btnRealisticMode);
        buttonsPanel.add(btnGenerateNew);
        
        // Panel du status
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        modeLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        modeLabel.setForeground(new Color(0, 100, 200));
        statusPanel.add(modeLabel);
        statusPanel.add(Box.createHorizontalStrut(20));
        
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        statusLabel.setForeground(new Color(0, 100, 0));
        statusPanel.add(statusLabel);
        
        panel.add(buttonsPanel, BorderLayout.NORTH);
        panel.add(statusPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createDHClassicPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        
        // Panel supérieur : visualisation des participants
        JPanel participantsPanel = createParticipantsPanel();
        
        // Panel du milieu : zone d'échange public
        JPanel exchangeVisualPanel = createExchangeVisualPanel();
        
        // Panel inférieur : détails textuels
        JPanel detailsPanel = createDetailsPanel();
        
        mainPanel.add(participantsPanel, BorderLayout.NORTH);
        mainPanel.add(exchangeVisualPanel, BorderLayout.CENTER);
        mainPanel.add(detailsPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createParticipantsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Alice
        alicePanel.setLayout(new BoxLayout(alicePanel, BoxLayout.Y_AXIS));
        alicePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.PINK, 2), 
            "Alice"));
        alicePanel.setBackground(new Color(255, 240, 245));
        alicePanel.add(Box.createVerticalStrut(10));
        alicePanel.add(createCenteredLabel("Données privées:"));
        alicePanel.add(aliceSecretLabel);
        alicePanel.add(Box.createVerticalStrut(10));
        alicePanel.add(createCenteredLabel("Données publiques:"));
        alicePanel.add(alicePublicLabel);
        alicePanel.add(Box.createVerticalGlue());
        
        // Espace public
        publicSpacePanel.setLayout(new BoxLayout(publicSpacePanel, BoxLayout.Y_AXIS));
        publicSpacePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.ORANGE, 2), 
            "Espace Public (visible par tous)"));
        publicSpacePanel.setBackground(new Color(255, 250, 230));
        publicSpacePanel.add(Box.createVerticalStrut(20));
        publicSpacePanel.add(createCenteredLabel("Paramètres communs:"));
        publicSpacePanel.add(createCenteredLabel("p (nombre premier)"));
        publicSpacePanel.add(createCenteredLabel("g (générateur)"));
        publicSpacePanel.add(Box.createVerticalStrut(20));
        publicSpacePanel.add(new JSeparator());
        publicSpacePanel.add(Box.createVerticalStrut(10));
        publicSpacePanel.add(createCenteredLabel("Secret partagé:"));
        publicSpacePanel.add(sharedSecretLabel);
        publicSpacePanel.add(Box.createVerticalGlue());
        
        // Bob
        bobPanel.setLayout(new BoxLayout(bobPanel, BoxLayout.Y_AXIS));
        bobPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.CYAN, 2), 
            "Bob"));
        bobPanel.setBackground(new Color(240, 248, 255));
        bobPanel.add(Box.createVerticalStrut(10));
        bobPanel.add(createCenteredLabel("Données privées:"));
        bobPanel.add(bobSecretLabel);
        bobPanel.add(Box.createVerticalStrut(10));
        bobPanel.add(createCenteredLabel("Données publiques:"));
        bobPanel.add(bobPublicLabel);
        bobPanel.add(Box.createVerticalGlue());
        
        panel.add(alicePanel);
        panel.add(publicSpacePanel);
        panel.add(bobPanel);
        
        return panel;
    }
    
    private JPanel createExchangeVisualPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Visualisation de l'échange"));
        
        exchangeArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        exchangeArea.setEditable(false);
        exchangeArea.setLineWrap(true);
        exchangeArea.setWrapStyleWord(true);
        exchangeArea.setMargin(new Insets(10, 10, 10, 10));
        
        panel.add(new JScrollPane(exchangeArea));
        panel.setPreferredSize(new Dimension(0, 150));
        
        return panel;
    }
    
    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Détails des calculs"));
        
        dhStepsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        dhStepsArea.setEditable(false);
        dhStepsArea.setLineWrap(true);
        dhStepsArea.setWrapStyleWord(true);
        dhStepsArea.setMargin(new Insets(10, 10, 10, 10));
        
        panel.add(new JScrollPane(dhStepsArea));
        panel.setPreferredSize(new Dimension(0, 200));
        
        return panel;
    }
    
    private JPanel createECDHPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Panel principal avec les deux courbes côte à côte
        JPanel curvesPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Panel gauche : courbe elliptique complète avec légende
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Vue Complète - Protocole ECDH"));
        leftPanel.add(ecPanel, BorderLayout.CENTER);
        
        // Panel droit : opérations sur courbe (2A)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Opérations - Doublement de Point"));
        rightPanel.add(ecOperationsPanel, BorderLayout.CENTER);
        
        // Boutons de contrôle des opérations
        JPanel operationsControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnResetOp = new JButton("Recommencer");
        JButton btnNextStep = new JButton("⏭Étape suivante");
        JButton btnShowAll = new JButton("Tout afficher");
        
        btnResetOp.addActionListener(e -> ecOperationsPanel.reset());
        btnNextStep.addActionListener(e -> ecOperationsPanel.nextStep());
        btnShowAll.addActionListener(e -> ecOperationsPanel.setStep(3));
        
        operationsControlPanel.add(btnResetOp);
        operationsControlPanel.add(btnNextStep);
        operationsControlPanel.add(btnShowAll);
        
        rightPanel.add(operationsControlPanel, BorderLayout.SOUTH);
        
        curvesPanel.add(leftPanel);
        curvesPanel.add(rightPanel);
        
        // Panel inférieur : explications ECDH
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("ECDH - Explication du Protocole"));
        
        JTextArea ecdhExplanation = new JTextArea();
        ecdhExplanation.setFont(new Font("SansSerif", Font.PLAIN, 13));
        ecdhExplanation.setEditable(false);
        ecdhExplanation.setLineWrap(true);
        ecdhExplanation.setWrapStyleWord(true);
        ecdhExplanation.setMargin(new Insets(10, 10, 10, 10));
        ecdhExplanation.setText("""
            PRINCIPE DE L'ECDH
            
            Au lieu d'utiliser l'exponentiation modulaire (g^x mod p), ECDH utilise la multiplication 
            de points sur une courbe elliptique.
            
            Courbe: y² = x³ + ax + b
            
            ÉTAPES DU PROTOCOLE:
            1. Alice et Bob partent du point G (générateur) sur la courbe
            2. Alice choisit un nombre secret nA
            3. Bob choisit un nombre secret nB
            4. Alice calcule PA = nA × G (en additionnant G avec lui-même nA fois grace a la multiplication scalaire)
            5. Bob calcule PB = nB × G (en additionnant G avec lui-même nB fois grace a  la multiplication scalaire)
            6. Ils échangent PA et PB publiquement
            7. Alice calcule S = nA × PB = nA × (nB × G)
            8. Bob calcule S = nB × PA = nB × (nA × G)
            9. Les deux obtiennent le même point S car nA × PB = nA × (nB × G) = nB × (nA × G) = nB × PA!
            
            OPÉRATIONS SUR COURBES:
            • Addition P + Q : Tracer la droite passant par P et Q, trouver le 3ème point d'intersection,
              puis prendre sa symétrie par rapport à l'axe x
            • Doublement 2P = P + P : Tracer la TANGENTE en P, trouver l'intersection avec la courbe,
              puis prendre sa symétrie par rapport à l'axe x
            
            AVANTAGES par rapport à DH classique:
            • Clés beaucoup plus courtes (256 bits vs 2048 bits) pour la même sécurité
            • Calculs plus rapides
            • Moins de bande passante nécessaire
            • Utilisé dans TLS 1.3, SSH moderne, Bitcoin, applications mobiles...
            
            SÉCURITÉ:
            Basé sur le problème du logarithme discret sur courbe elliptique (ECDLP).
            C'est encore plus difficile à résoudre que le logarithme discret classique !
            """);
        
        bottomPanel.add(new JScrollPane(ecdhExplanation), BorderLayout.CENTER);
        bottomPanel.setPreferredSize(new Dimension(0, 200));
        
        panel.add(curvesPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
    
    public void updateAliceSecret(String text) {
        aliceSecretLabel.setText("a = " + text);
        aliceSecretLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
    }
    
    public void updateAlicePublic(String text) {
        alicePublicLabel.setText("A = " + text);
        alicePublicLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        alicePublicLabel.setForeground(new Color(200, 0, 100));
    }
    
    public void updateBobSecret(String text) {
        bobSecretLabel.setText("b = " + text);
        bobSecretLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
    }
    
    public void updateBobPublic(String text) {
        bobPublicLabel.setText("B = " + text);
        bobPublicLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        bobPublicLabel.setForeground(new Color(0, 100, 200));
    }
    
    public void updateSharedSecret(String text) {
        sharedSecretLabel.setText("s = " + text);
        sharedSecretLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        sharedSecretLabel.setForeground(new Color(0, 150, 0));
    }
    
    public void highlightAlice() {
        alicePanel.setBackground(new Color(255, 200, 220));
    }
    
    public void highlightBob() {
        bobPanel.setBackground(new Color(200, 230, 255));
    }
    
    public void resetHighlights() {
        alicePanel.setBackground(new Color(255, 240, 245));
        bobPanel.setBackground(new Color(240, 248, 255));
    }
}
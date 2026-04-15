package controleur;

import vue.MainVue;
import javax.swing.*;
import java.awt.*;
import java.security.SecureRandom;

/**
 * Contrôleur principal - Version refactorisée
 * Délègue les responsabilités aux classes spécialisées
 */
public class Controleur {
    private final MainVue view;
    private final SecureRandom random = new SecureRandom();

    private DHParameters currentParams;
    private boolean realisticMode = false;
    private int currentStep = 0;
    private Timer autoTimer;

    /**
     * Controleur du démonstrateur
     * @param view Vue du démonstrateur
     */
    public Controleur(MainVue view) {
        this.view = view;
        initEvents();
        setDemoMode();
        displayInitialInfo();
    }

    /**
     * Initialise les événements des boutons
     */
    private void initEvents() {
        view.btnStepByStep.addActionListener(e -> runStep());
        view.btnDHFull.addActionListener(e -> runFullDemo());
        view.btnClear.addActionListener(e -> clearAll());
        view.btnAutoDemo.addActionListener(e -> startAutoDemo());
        view.btnRealisticMode.addActionListener(e -> toggleRealisticMode());
        view.btnGenerateNew.addActionListener(e -> generateNewRealisticValues());
    }

    // ========== Gestion des modes ==========
    
    /**
     * Active le mode pédagogique (petits nombres)
     */
    private void setDemoMode() {
        realisticMode = false;
        currentParams = DHParameters.createDemoParameters();
        
        view.btnRealisticMode.setText(" Passer en mode réaliste");
        view.btnGenerateNew.setEnabled(false);
        view.modeLabel.setText(" Mode : Pédagogique (petits nombres)");
        view.modeLabel.setForeground(new Color(0, 100, 200));
    }

    /**
     * Active le mode réaliste (grands nombres cryptographiques)
     */
    private void setRealisticMode() {
        realisticMode = true;
        generateNewRealisticValues();
        
        view.btnRealisticMode.setText(" Retour mode pédagogique");
        view.btnGenerateNew.setEnabled(true);
        view.modeLabel.setText(" Mode : Réaliste (cryptographie standard)");
        view.modeLabel.setForeground(new Color(200, 0, 100));
    }

    /**
     * Bascule entre mode pédagogique et mode réaliste
     */
    private void toggleRealisticMode() {
        if (realisticMode) {
            setDemoMode();
        } else {
            setRealisticMode();
        }
        clearAll();
        displayModeInfo();
    }

    /**
     * Génère de nouvelles valeurs aléatoires pour le mode réaliste
     */
    private void generateNewRealisticValues() {
        if (!realisticMode) return;
        
        view.statusLabel.setText(" Génération de nombres premiers sûrs...");
        
        // Utiliser SwingWorker pour ne pas bloquer l'interface
        SwingWorker<DHParameters, Void> worker = new SwingWorker<>() {
            @Override
            protected DHParameters doInBackground() {
                return DHParameters.createRealisticParameters(random);
            }
            
            @Override
            protected void done() {
                try {
                    currentParams = get();
                    view.statusLabel.setText("✅ Nouvelles valeurs générées !");
                    displayModeInfo();
                } catch (Exception e) {
                    view.statusLabel.setText("❌ Erreur lors de la génération");
                    e.printStackTrace();
                }
            }
        };
        
        worker.execute();
    }

    // ========== Affichage ==========
    
    /**
     * Affiche le message d'accueil initial
     */
    private void displayInitialInfo() {
        ModeInfoDisplay infoDisplay = new ModeInfoDisplay(view, currentParams, realisticMode);
        infoDisplay.displayInitialInfo();
    }

    /**
     * Affiche les informations sur le mode actuel
     */
    private void displayModeInfo() {
        ModeInfoDisplay infoDisplay = new ModeInfoDisplay(view, currentParams, realisticMode);
        infoDisplay.displayModeInfo();
    }

    // ========== Exécution du protocole ==========
    
    /**
     * Exécute une étape du protocole Diffie-Hellman
     */
    private void runStep() {
        if (currentStep < 0 || currentStep > 8) {
            view.statusLabel.setText("Toutes les étapes sont terminées.");
            return;
        }
        
        StepExecutor executor = new StepExecutor(view, currentParams, realisticMode);
        executor.executeStep(currentStep);
        
        if (currentStep == 8) {
            currentStep = -1; // Marquer comme terminé
        } else {
            currentStep++;
        }
    }

    /**
     * Affiche la démonstration complète du protocole
     */
    private void runFullDemo() {
        clearAll();
        currentStep = 0;
        
        FullDemoDisplay fullDemo = new FullDemoDisplay(view, currentParams, realisticMode);
        fullDemo.displayFullDemo();
        
        currentStep = 9;
    }

    // ========== Contrôles ==========
    
    /**
     * Réinitialise l'affichage et l'état
     */
    private void clearAll() {
        currentStep = 0;
        view.dhStepsArea.setText("");
        view.exchangeArea.setText("");
        view.updateAliceSecret("?");
        view.updateAlicePublic("?");
        view.updateBobSecret("?");
        view.updateBobPublic("?");
        view.updateSharedSecret("?");
        view.resetHighlights();
        view.statusLabel.setText(" Réinitialisé");
        
        if (autoTimer != null) {
            autoTimer.stop();
        }
        
        displayModeInfo();
    }

    /**
     * Démarre le mode automatique
     */
    private void startAutoDemo() {
        clearAll();
        view.statusLabel.setText("  Mode automatique démarré...");

        Timer startTimer = new Timer(1000, e -> {
            runStep(); // Première étape immédiate
            
            int delay = realisticMode ? 3500 : 2500;
            autoTimer = new Timer(delay, evt -> {
                if (currentStep == -1 || currentStep > 8) {
                    autoTimer.stop();
                    view.statusLabel.setText(" Démo automatique terminée !");
                    flashSuccess();
                } else {
                    runStep();
                }
            });
            autoTimer.start();
        });
        startTimer.setRepeats(false);
        startTimer.start();
    }
    
    /**
     * Animation de succès à la fin du protocole
     */
    private void flashSuccess() {
        Timer flashTimer = new Timer(300, null);
        final int[] count = {0};
        
        flashTimer.addActionListener(e -> {
            if (count[0] < 6) {
                if (count[0] % 2 == 0) {
                    view.alicePanel.setBackground(new Color(144, 238, 144));
                    view.bobPanel.setBackground(new Color(144, 238, 144));
                    view.publicSpacePanel.setBackground(new Color(255, 215, 0));
                } else {
                    view.resetHighlights();
                    view.publicSpacePanel.setBackground(new Color(255, 250, 230));
                }
                count[0]++;
            } else {
                flashTimer.stop();
                view.resetHighlights();
                view.publicSpacePanel.setBackground(new Color(255, 250, 230));
            }
        });
        flashTimer.start();
    }
}
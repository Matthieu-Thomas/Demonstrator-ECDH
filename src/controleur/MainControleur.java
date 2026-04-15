package controleur;

import vue.MainVue;

import javax.swing.*;

/**
 * Main pour le controleur (Il lance le démonstrateur)
 * @author tellier212
 */
public class MainControleur {
    /**
     * Main pour le controleur (Il lance le démonstrateur)
     * @param args Argument
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainVue view = new MainVue();
            new Controleur(view);

            JFrame frame = new JFrame("Démo Diffie-Hellman pédagogique");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import control.Controller;
import dao.UtilisateurDAO;

public class InterfaceConnexion {
    private Controller control;

    public InterfaceConnexion(Controller control) {
        this.control = control;
    }

    public void init() {
        JFrame frame = new JFrame("Connexion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel labelNomUtilisateur = new JLabel("Nom d'utilisateur:");
        JTextField champNomUtilisateur = new JTextField(20);
        JLabel labelMotDePasse = new JLabel("Mot de passe:");
        JPasswordField champMotDePasse = new JPasswordField(20);
        JButton boutonConnexion = new JButton("Se connecter");
        JLabel labelEspace = new JLabel("");

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        labelNomUtilisateur.setFont(labelFont);
        labelMotDePasse.setFont(labelFont);
        champNomUtilisateur.setFont(labelFont);
        champMotDePasse.setFont(labelFont);
        boutonConnexion.setFont(labelFont);

        boutonConnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomUtilisateur = champNomUtilisateur.getText();
                String motDePasse = new String(champMotDePasse.getPassword());

                if (UtilisateurDAO.getInstance().authentifierUtilisateur(nomUtilisateur, motDePasse)) {
                    JOptionPane.showMessageDialog(null, "Connexion réussie. Chemin associé : " + UtilisateurDAO.getInstance().getCheminUtilisateur(nomUtilisateur));

                    control.connecteAvecSucces();
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Nom d'utilisateur ou mot de passe incorrect.");
                }
            }
        });

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 10));
        formPanel.add(labelNomUtilisateur);
        formPanel.add(champNomUtilisateur);
        formPanel.add(labelMotDePasse);
        formPanel.add(champMotDePasse);
        formPanel.add(new JLabel());
        formPanel.add(boutonConnexion);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(labelEspace, BorderLayout.SOUTH);

        frame.add(panel); 
        frame.setVisible(true);
        
        JButton boutonModifierMotDePasse = new JButton("Modifier Mot de passe");
        boutonModifierMotDePasse.setFont(labelFont);
        boutonModifierMotDePasse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomUtilisateur = champNomUtilisateur.getText();
                String motDePasse = new String(champMotDePasse.getPassword());

                if (UtilisateurDAO.getInstance().authentifierUtilisateur(nomUtilisateur, motDePasse)) {
                    // Authentification réussie, ouvrez une nouvelle fenêtre de modification de mot de passe
                    ouvrirFenetreModificationMotDePasse(nomUtilisateur);
                } else {
                    JOptionPane.showMessageDialog(null, "Nom d'utilisateur ou mot de passe incorrect.");
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(boutonConnexion);
        buttonPanel.add(boutonModifierMotDePasse);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    }
    private void ouvrirFenetreModificationMotDePasse(String nomUtilisateur) {
        // Créez une nouvelle fenêtre pour la modification du mot de passe et passez le nom d'utilisateur
    	ModifMotDePasse fenetreModification = new ModifMotDePasse(nomUtilisateur);
        fenetreModification.init();
    }
}


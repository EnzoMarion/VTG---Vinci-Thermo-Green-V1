package View;

import org.passay.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Tools.BCrypt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Tools.BCrypt;

public class ModifMotDePasse {
	
	private static final String JDBC_URL = "jdbc:mysql://localhost/thg_db";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "coucu";

    private String nomUtilisateur;

    private JLabel lowerCaseCheckLabel;
    private JLabel upperCaseCheckLabel;
    private JLabel specialCharCheckLabel;
    private JLabel digitCheckLabel;
    private JLabel lengthCheckLabel;
    private JLabel spaceCheckLabel;
    
    private boolean isLowerCaseValid = false;
    private boolean isUpperCaseValid = false;
    private boolean isSpecialCharValid = false;
    private boolean isDigitValid = false;
    private boolean isLengthValid = false;
    private boolean isSpaceValid = false;

    public ModifMotDePasse(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public void init() {
        JFrame frame = new JFrame("Modification du Mot de Passe");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2, 10, 10));

        frame.add(panel);

        JLabel labelNouveauMotDePasse = new JLabel("Nouveau Mot de Passe:");
        JPasswordField champNouveauMotDePasse = new JPasswordField(20);
        JLabel labelConfirmerMotDePasse = new JLabel("Confirmer le Mot de Passe:");
        JPasswordField champConfirmerMotDePasse = new JPasswordField(20);
        JButton boutonModifierMotDePasse = new JButton("Modifier le Mot de Passe");

        lowerCaseCheckLabel = new JLabel("❌");
        upperCaseCheckLabel = new JLabel("❌");
        specialCharCheckLabel = new JLabel("❌");
        digitCheckLabel = new JLabel("❌");
        lengthCheckLabel = new JLabel("❌");
        spaceCheckLabel = new JLabel("❌");

        lowerCaseCheckLabel.setForeground(Color.RED);
        upperCaseCheckLabel.setForeground(Color.RED);
        specialCharCheckLabel.setForeground(Color.RED);
        digitCheckLabel.setForeground(Color.RED);
        lengthCheckLabel.setForeground(Color.RED);
        spaceCheckLabel.setForeground(Color.RED);

        lowerCaseCheckLabel.setVisible(false);
        upperCaseCheckLabel.setVisible(false);
        specialCharCheckLabel.setVisible(false);
        digitCheckLabel.setVisible(false);
        lengthCheckLabel.setVisible(false);
        spaceCheckLabel.setVisible(false);

        Font labelFont = new Font("Arial", Font.PLAIN, 16);
        labelNouveauMotDePasse.setFont(labelFont);
        labelConfirmerMotDePasse.setFont(labelFont);
        champNouveauMotDePasse.setFont(labelFont);
        champConfirmerMotDePasse.setFont(labelFont);
        boutonModifierMotDePasse.setFont(labelFont);

        champNouveauMotDePasse.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkPassword(champNouveauMotDePasse.getPassword());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkPassword(champNouveauMotDePasse.getPassword());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkPassword(champNouveauMotDePasse.getPassword());
            }
        });

        boutonModifierMotDePasse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nouveauMotDePasse = new String(champNouveauMotDePasse.getPassword());
                String confirmerMotDePasse = new String(champConfirmerMotDePasse.getPassword());

                if (!nouveauMotDePasse.equals(confirmerMotDePasse)) {
                    JOptionPane.showMessageDialog(null, "Les mots de passe ne correspondent pas.");
                } else {
                    if (isLowerCaseValid && isUpperCaseValid && isSpecialCharValid && isDigitValid && isLengthValid && isSpaceValid) {
                        updatePasswordInDatabase(nouveauMotDePasse);
                        JOptionPane.showMessageDialog(null, "Le mot de passe a été mis à jour avec succès.");
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Le nouveau mot de passe ne respecte pas les conditions requises.");
                    }
                }
            }
        });

        // Add components to the panel
        panel.add(labelNouveauMotDePasse);
        panel.add(champNouveauMotDePasse);
        panel.add(lowerCaseCheckLabel);
        panel.add(upperCaseCheckLabel);
        panel.add(specialCharCheckLabel);
        panel.add(digitCheckLabel);
        panel.add(lengthCheckLabel);
        panel.add(spaceCheckLabel);
        panel.add(labelConfirmerMotDePasse);
        panel.add(champConfirmerMotDePasse);
        panel.add(boutonModifierMotDePasse);

        frame.setVisible(true);
    }

    private void checkPassword(char[] password) {
        String passwordText = new String(password);

        isLowerCaseValid = passwordText.matches(".*[a-z].*");
        isUpperCaseValid = passwordText.matches(".*[A-Z].*");
        isSpecialCharValid = passwordText.matches(".*[!@#-$%^&*].*");
        isDigitValid = passwordText.matches(".*\\d.*");
        isLengthValid = passwordText.length() >= 8 && passwordText.length() <= 32;
        isSpaceValid = !passwordText.contains(" ");

        updateCheckLabel(lowerCaseCheckLabel, isLowerCaseValid, "Minuscule");
        updateCheckLabel(upperCaseCheckLabel, isUpperCaseValid, "Majuscule");
        updateCheckLabel(specialCharCheckLabel, isSpecialCharValid, "Caractère spécial");
        updateCheckLabel(digitCheckLabel, isDigitValid, "Chiffre");
        updateCheckLabel(lengthCheckLabel, isLengthValid, "Longueur");
        updateCheckLabel(spaceCheckLabel, isSpaceValid, "Pas d'espace");
    }

    private void updateCheckLabel(JLabel label, boolean isValid, String ruleName) {
        String checkText = isValid ? "✅" : "❌";
        String labelText = isValid ? ruleName + ": Valide" : ruleName + ": Non Valide";
        
        label.setText(checkText + " " + labelText);
        label.setForeground(isValid ? Color.GREEN : Color.RED);
        label.setVisible(true);
    }


    private void updatePasswordInDatabase(String newPassword) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            String salt = BCrypt.gensalt();

            String hashedPassword = BCrypt.hashpw(newPassword, salt);

            String sql = "UPDATE utilisateurs SET mot_de_passe = ? WHERE nom_utilisateur = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, nomUtilisateur);
            
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Mot de passe mis à jour avec succès !");
            } else {
                System.out.println("Échec de la mise à jour du mot de passe.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

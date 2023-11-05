package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Tools.BCrypt;

public class UtilisateurDAO {
    private static final String JDBC_URL = "jdbc:mysql://localhost/thg_db";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "coucu";

    private static UtilisateurDAO instance = null;

    private UtilisateurDAO() {
    }

    public static UtilisateurDAO getInstance() {
        if (instance == null) {
            instance = new UtilisateurDAO();
        }
        return instance;
    }

    public boolean authentifierUtilisateur(String nomUtilisateur, String motDePasse) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT mot_de_passe FROM utilisateurs WHERE nom_utilisateur = ?")) {
            preparedStatement.setString(1, nomUtilisateur);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String motDePasseHash = resultSet.getString("mot_de_passe");
                    return BCrypt.checkpw(motDePasse, motDePasseHash);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getCheminUtilisateur(String nomUtilisateur) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "SELECT chemin FROM utilisateurs WHERE nom_utilisateur = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, nomUtilisateur);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("chemin");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.xdevapi.Statement;

import Model.Mesure;

public class MesureDAO {

    private final String JDBC_URL = "jdbc:mysql://localhost/thg_db";
    private final String JDBC_USER = "root";
    private final String JDBC_PASSWORD = "coucu";
    private Connection connection;

    public MesureDAO() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Impossible de se connecter à la base de données.");
        }
    }

    public List<Mesure> getAllMesures() {
        List<Mesure> mesures = new ArrayList<>();
        try {
            java.sql.Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM mesure");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String numZone = resultSet.getString("zone");
                String horoDate = resultSet.getString("date_heure");
                float fahrenheit = resultSet.getFloat("temperature");
                mesures.add(new Mesure(id, numZone, horoDate, fahrenheit));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mesures;
    }

    public List<Mesure> getFilteredMesures(boolean isCelsius, int celsiusValue) {
        List<Mesure> mesures = new ArrayList<>();
        String query = "SELECT * FROM mesure WHERE celsius = ? AND celsius_value >= ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setBoolean(1, isCelsius);
            ps.setInt(2, celsiusValue);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ResultSet resultSet = null;
				int id = resultSet.getInt("id");
                String numZone = rs.getString("numZone");
                String horoDate = rs.getString("horoDate");
                float fahrenheit = rs.getFloat("fahrenheit");

                Mesure mesure = new Mesure(id, numZone, horoDate, fahrenheit);
                mesures.add(mesure);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mesures;
    }

	private Connection getConnection() {
		return null;
	}

}
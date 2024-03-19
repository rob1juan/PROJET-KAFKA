package cs2pr3.postgres;

import java.sql.*;
import java.util.logging.Logger;

public class PostgresUtils {
    // Parametres
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "admin";

    public static final String GET_ALL_PATIENTS = "SELECT * FROM Patient;";
    public static final String GET_PATIENT_BY_PID = "SELECT * FROM Patient WHERE id_patient = ?;";
    public static final String GET_PATIENT_BY_NAME = "SELECT p.* FROM Patient p JOIN Name n ON p.id_patient = n.id_patient WHERE n.last_name LIKE ? OR n.first_name LIKE ?;";
    public static final String GET_PATIENT_STAY_BY_PID = "SELECT s.* FROM Stay s WHERE s.id_patient = ?;";
    public static final String GET_PATIENT_MOVEMENTS_BY_SID = "SELECT m.* FROM Movements m WHERE m.visit_number = ?;";
    private static final Logger LOGGER = Logger.getLogger(PostgresUtils.class.getName());

    public static Connection createConnection(String username, String password) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://postgresdb:5432/HOPSIIA", username, password);
            if (connection != null) {
                return connection;
            } else {
                LOGGER.severe("Connection to PostgreSQL failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static String executeQuery(Connection connection, String query) {
        String data = "NO_RESULT";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) data = rs.getString(1);
            rs.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
}

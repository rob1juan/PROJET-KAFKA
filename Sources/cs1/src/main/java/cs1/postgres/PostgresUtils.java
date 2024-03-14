package cs1.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PostgresUtils {
	
	private static final Logger LOGGER = Logger.getLogger(PostgresUtils.class.getName());
	private static final String QUERY_UPDATE = "UPDATE public.covid SET data='$JSON$', date_update='$UPDATE_DATE$' WHERE id=1;";
	
	public static Connection createPostgresConnection(String username, String password) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/HOPSIIA", username, password);
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
	
	public static void sendJsonToPostgres(Connection connection, String json) {
        try {
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            JsonObject patientInfo = jsonObject.getAsJsonObject("patient_information");
            JsonObject addressInfo = jsonObject.getAsJsonObject("address");
            JsonObject movementInfo = jsonObject.getAsJsonObject("movements");
            JsonObject stayInfo = jsonObject.getAsJsonObject("stay");

            // Insertion dans la table Patient
            String queryPatient = "INSERT INTO Patient (id_patient, birthday, sex, nationality) VALUES (?, ?, ?, ?);";
            PreparedStatement pstmtPatient = connection.prepareStatement(queryPatient);
            pstmtPatient.setString(1, patientInfo.get("id_patient").getAsString());
            pstmtPatient.setDate(2, java.sql.Date.valueOf(formatBirthday(patientInfo.get("birthday").getAsString())));
            pstmtPatient.setString(3, patientInfo.get("sex").getAsString());
            pstmtPatient.setString(4, patientInfo.get("nationnatilty").getAsString());
            pstmtPatient.executeUpdate();

            // Insérer les noms - Supposons qu'il y a plusieurs noms, boucler sur l'objet names
            JsonObject names = patientInfo.getAsJsonObject("names");
            String queryName = "INSERT INTO Name (id_patient, last_name, first_name) VALUES (?, ?, ?);";
            PreparedStatement pstmtName = connection.prepareStatement(queryName);
            names.entrySet().forEach(entry -> {
                try {
                    JsonObject nameObj = entry.getValue().getAsJsonObject();
                    pstmtName.setString(1, patientInfo.get("id_patient").getAsString());
                    pstmtName.setString(2, nameObj.get("last_name").getAsString());
                    pstmtName.setString(3, nameObj.get("first_name").getAsString());
                    pstmtName.executeUpdate();
                } catch (SQLException e) {
                    LOGGER.severe("Error inserting name: " + e.getMessage());
                }
            });

            // Insertion dans la table Address
            String queryAddress = "INSERT INTO Address (id_patient, street, city, zip) VALUES (?, ?, ?, ?);";
            PreparedStatement pstmtAddress = connection.prepareStatement(queryAddress);
            pstmtAddress.setString(1, patientInfo.get("id_patient").getAsString());
            pstmtAddress.setString(2, addressInfo.get("street").getAsString());
            pstmtAddress.setString(3, addressInfo.get("city").getAsString());
            pstmtAddress.setString(4, addressInfo.get("zip").getAsString());
            pstmtAddress.executeUpdate();

            // Insertion dans la table Movements
            String queryMovements = "INSERT INTO Movements (id_patient, visit_number, service, bed, room, admit_time) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement pstmtMovements = connection.prepareStatement(queryMovements);
            pstmtMovements.setString(1, patientInfo.get("id_patient").getAsString());
            pstmtMovements.setString(2, movementInfo.get("visit_number").getAsString());
            pstmtMovements.setString(3, movementInfo.get("service").getAsString());
            pstmtMovements.setString(4, movementInfo.get("bed").getAsString());
            pstmtMovements.setString(5, movementInfo.get("room").getAsString());
            pstmtMovements.setTimestamp(6, java.sql.Timestamp.valueOf(movementInfo.get("admit_time").getAsString()));
            pstmtMovements.executeUpdate();

            // Insertion dans la table Stay
            String queryStay = "INSERT INTO Stay (visit_number, start_time, end_time) VALUES (?, ?, ?);";
            PreparedStatement pstmtStay = connection.prepareStatement(queryStay);
            pstmtStay.setString(1, stayInfo.get("visit_number").getAsString());
            pstmtStay.setTimestamp(2, java.sql.Timestamp.valueOf(stayInfo.get("start_time").getAsString()));
            pstmtStay.setTimestamp(3, java.sql.Timestamp.valueOf(stayInfo.get("end_time").getAsString()));
            pstmtStay.executeUpdate();

            connection.close();
            LOGGER.info("La base de données a été mise à jour avec succès.");

        } catch (SQLException e) {
            LOGGER.severe("Erreur lors de l'insertion dans la base de données : " + e.getMessage());
        }
    }

    private static String formatBirthday(String birthday) {
        // Assume birthday is in the format "DD-MM-YYYY" and needs to be converted to "YYYY-MM-DD"
        String[] parts = birthday.split("-");
        return parts[2] + "-" + parts[1] + "-" + parts[0];
    }
}

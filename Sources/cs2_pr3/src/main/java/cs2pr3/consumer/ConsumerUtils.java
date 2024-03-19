package cs2pr3.consumer;

import cs2pr3.postgres.PostgresUtils;
import cs2pr3.producer.ProducerUtils;
import cs2pr3.constant.Commandes;
import cs2pr3.constant.JsonErrors;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.json.XML;
import com.google.gson.Gson;

public class ConsumerUtils {
    private static final Logger LOGGER = Logger.getLogger(ConsumerUtils.class.getName());

    public static void runConsumer() {
        // Creation du consumer
        Consumer<String, String> consumer = ConsumerCreator.createConsumer();

        while (true) {
            @SuppressWarnings("deprecation") // consumer.poll(long) est d�pr�ci�
            final ConsumerRecords<String, String> consumerRecords = consumer.poll(1000);
            consumerRecords.forEach(record -> {

                LOGGER.info("Nouvelle commande utilisateur reçue : " + record.value());

                String retour = treatCommand(record.value());

                try {
                    LOGGER.info("Envoi de la r�ponse dans le topic Kafka 3.");
                    ProducerUtils.sendToTopic(retour);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            consumer.commitAsync();
        }
    }

    public static String treatCommand(String input) {

        if(null != input && !input.isEmpty()) {


            Connection connection = PostgresUtils.createConnection(PostgresUtils.USERNAME, PostgresUtils.PASSWORD);

            input = input.toLowerCase();
            String commande = input.split(" ")[0];
            String result = "{}";

            switch (commande) {
                case "get_all_patients":
                    result = PostgresUtils.executeQuery(connection, Commandes.GET_ALL_PATIENTS);
                    break;
                case "get_patient_by_pid":
                    // Vous devrez passer l'ID du patient de manière dynamique, probablement en le séparant de la commande
                    String patientId = input.split(" ")[1];
                    String query = Commandes.GET_PATIENT_BY_ID.replace("?", patientId);
                    result = PostgresUtils.executeQuery(connection, query);
                    break;
                case "get_patient_by_name":
                    String namePart = input.substring("get_patient_by_name".length()).trim();
                    query = Commandes.GET_PATIENT_BY_NAME.replace("?", "'" + namePart + "%'");
                    result = PostgresUtils.executeQuery(connection, query);
                    break;
                case "get_patient_stay_by_pid":
                    patientId = input.split(" ")[1];
                    query = Commandes.GET_PATIENT_STAY_BY_ID.replace("?", patientId);
                    result = PostgresUtils.executeQuery(connection, query);
                    break;
                case "get_patient_movements_by_sid":
                    String visitNumber = input.split(" ")[1];
                    query = Commandes.GET_PATIENT_MOVEMENTS_BY_SID.replace("?", "'" + visitNumber + "'");
                    result = PostgresUtils.executeQuery(connection, query);
                    break;
                default:
                    return "Commande non reconnue ou non supportée.";
            }

            LOGGER.info("Réponse de la base de données : " + result);

            if (!"NO_RESULT".equals(result)) {
                return result;
            } else {
                return JsonErrors.ERREUR_EXECUTION_PG_JSON;
            }

        } else {
            return JsonErrors.DEFAULT_RETURN_JSON;
        }
    }
}

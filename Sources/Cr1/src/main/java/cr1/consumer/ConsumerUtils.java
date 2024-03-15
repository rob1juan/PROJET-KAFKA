package cr1.consumer;

import java.sql.Connection;
import java.util.logging.Logger;

import cr1.postgres.PostgresConstants;
import cr1.postgres.PostgresUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public class ConsumerUtils {

    private static final Logger LOGGER = Logger.getLogger(ConsumerUtils.class.getName());

    public static void runConsumer() {

        // Creation du consumer
        Consumer<String, String> consumer = ConsumerCreator.createConsumer();

        while (true) {
            @SuppressWarnings("deprecation") // consumer.poll(long) est déprécié
            final ConsumerRecords<String, String> consumerRecords = consumer.poll(1000);

            consumerRecords.forEach(record -> {

                LOGGER.info("Nouvelle demande de mise à jour reçue.");
                // Connection a la base de donnees
                // On ouvre la connection a chaque mise a jour car elle est fermee automatiquement
                // Apres une precedente update.
                Connection connection = PostgresUtils.createPostgresConnection(PostgresConstants.USERNAME.getValue(), PostgresConstants.PASSWORD.getValue());

                // Recuperation des messages dans le topic
                String topicMessage = record.value();

                // Envoyer dans la BDD
                PostgresUtils.sendJsonToPostgres(connection, topicMessage);
            });

            consumer.commitAsync();
        }
    }

}

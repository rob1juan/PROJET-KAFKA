package cs2pr3.producer;

import cs2pr3.constant.KafkaConstants;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

public class ProducerUtils {
    private static final Logger LOGGER = Logger.getLogger(ProducerUtils.class.getName());

    public static void sendToTopic(String data) throws IOException {
        Producer<String, String> producer = ProducerCreator.createProducer();

        final ProducerRecord<String, String> record = new ProducerRecord<String, String>(KafkaConstants.TOPIC_3,
                data);

        ProducerUtils.sendRecord(producer, record);
    }

    private static RecordMetadata sendRecord(Producer<String, String> producer, ProducerRecord<String, String> record) {
        RecordMetadata metadata = null;
        try {
            metadata = producer.send(record).get();
            LOGGER.info(new Date() + " - Enregistrement envoy� vers la partition " + metadata.partition() + " Et l'offset " + metadata.offset());
        } catch (Exception e) {
            LOGGER.severe("Erreur dans l'envoi de l'enregistrement");
            e.printStackTrace();
        }
        return metadata;
    }
}

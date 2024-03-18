package pr1.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSender {

    private static final Logger logger = LoggerFactory.getLogger(KafkaSender.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String message) {
        logger.debug("Sending message='{}' to topic='{}'", message, topic);
        kafkaTemplate.send(topic, message);
        logger.info("Message sent to topic successfully.");
    }
}

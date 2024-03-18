package pr1.controller;

import pr1.kafka.KafkaSender;
import pr1.json.JsonTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/import")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private KafkaSender kafkaSender;

    @PostMapping("/send")
    public void sendJsonToKafka(@RequestBody String json) {
        logger.info("Receiving JSON to send to Kafka: {}", json);
        kafkaSender.send("topic1", JsonTransformer.transformJson(json));
        logger.info("JSON sent to Kafka topic successfully.");
    }
}

package pr1.controler;

import pr1.kafka.KafkaSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/import")
public class ApiController {

    @Autowired
    private KafkaSender kafkaSender;

    @PostMapping("/import")
    public void sendJsonToKafka(@RequestBody String json) {
        kafkaSender.send("topic1", json);
    }
}
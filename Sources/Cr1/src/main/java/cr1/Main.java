package cr1;

import cr1.consumer.ConsumerUtils;

import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Démarrage du Consumer n°1\n");
        ConsumerUtils.runConsumer();
    }
}
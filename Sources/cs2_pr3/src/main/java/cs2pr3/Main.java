package cs2pr3;

import cs2pr3.consumer.ConsumerUtils;

import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Démarrage du Consumer 2 & Producer 3\n");
        ConsumerUtils.runConsumer();
    }
}
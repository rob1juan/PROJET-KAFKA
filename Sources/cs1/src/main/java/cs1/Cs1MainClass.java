package cs1;

import java.util.logging.Logger;

import cs1.consumer.ConsumerUtils;

public class Cs1MainClass {
	
	private static final Logger LOGGER = Logger.getLogger(Cs1MainClass.class.getName());
	
	public static void main(String[] args) {
		LOGGER.info("Démarrage du Consumer n°1\n");
		ConsumerUtils.runConsumer();
	}
}

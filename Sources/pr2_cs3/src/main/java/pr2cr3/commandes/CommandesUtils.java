package pr2cr3.commandes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.consumer.Consumer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.xml.sax.SAXException;
import pr2cr3.producer.ProducerCreator;
import pr2cr3.consumer.ConsumerCreator;
import pr2cr3.producer.ProducerUtils;

public class CommandesUtils {
    private Map<String, String> commandesDef;
    private Producer<String, String> producer;
    private Consumer<String, String> consumer;
    private static final Logger LOGGER = Logger.getLogger(CommandesUtils.class.getName());

    public CommandesUtils() {
        this.setProducer(ProducerCreator.createProducer());
        this.setConsumer(ConsumerCreator.createConsumer());

        this.getCommandesDef().put(Commandes.HELP.getValue(), DescriptionCommandes.HELP.getValue());
        this.getCommandesDef().put(Commandes.GET_ALL_PATIENTS.getValue(), DescriptionCommandes.GET_ALL_PATIENTS.getValue());
        this.getCommandesDef().put(Commandes.GET_PATIENT_BY_ID.getValue() + " ID", DescriptionCommandes.GET_PATIENT_BY_ID.getValue());
        this.getCommandesDef().put(Commandes.GET_PATIENT_BY_NAME.getValue() + " Name", DescriptionCommandes.GET_PATIENT_BY_NAME.getValue());
        this.getCommandesDef().put(Commandes.GET_PATIENT_STAY_BY_ID.getValue(), DescriptionCommandes.GET_PATIENT_STAY_BY_ID.getValue());
        this.getCommandesDef().put(Commandes.GET_PATIENT_MOVEMENTS_BY_SID.getValue(), DescriptionCommandes.GET_PATIENT_MOVEMENTS_BY_SID.getValue());
        this.getCommandesDef().put(Commandes.EXPORT_PATIENTS.getValue(), DescriptionCommandes.EXPORT_PATIENTS.getValue());
        this.getCommandesDef().put(Commandes.QUIT.getValue(), DescriptionCommandes.QUIT.getValue());
    }

    public void execute(String commande) throws SAXException, IOException {
        if (Commandes.HELP.getValue().equals(commande)) {
            this.afficherCommandes();

        } else if (Commandes.QUIT.getValue().equals(commande)) {

            this.getConsumer().close();
            this.getProducer().close();

            System.out.println("\n---------------------------\nMerci de votre utilisation.\nA bientot !\n---------------------------");
            System.exit(0);

        } else {
            LOGGER.info("Envoi de la commande dans le topic 2...");

            try {

                ProducerUtils.sendToTopic(commande);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                this.readResponse(Commandes.EXPORT_PATIENTS.getValue().equals(commande));
            } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
                LOGGER.severe("Erreur lors de la lecture de la reponse");
                e.printStackTrace();
            }
        }
    }

    private void readResponse(boolean export) throws SAXException, IOException, ParserConfigurationException, TransformerException {

        boolean messageLu = false;

        while (!messageLu) {
            @SuppressWarnings("deprecation") // consumer.poll(long) est d�pr�ci�
            final ConsumerRecords<String, String> consumerRecords = this.getConsumer().poll(1000);

            for (ConsumerRecord<String, String> record : consumerRecords) {

                System.out.println("\nRéponse reçue : ");
                if (export) {
                    System.out.println(record.value().replaceAll("--><", "-->\n<"));
                } else {

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    JsonParser jp = new JsonParser();
                    JsonElement je = jp.parse(record.value());
                    String prettyJsonString = gson.toJson(je);

                    System.out.println(prettyJsonString);
                }

                messageLu = true;
            }
            consumer.commitAsync();
        }
    }

    public void afficherCommandes() {
        System.out.println("\nCommandes disponibles :");
        this.getCommandesDef().forEach((commande, description) -> {
            System.out.println("  [*] " + commande + " : " + description);
        });
    }

    private boolean isExist(String commande) {
        return Arrays.asList(Commandes.values()).stream()
                .map(c -> c.getValue().split(" ")[0])
                .collect(Collectors.toList())
                .contains(commande.split(" ")[0]);
    }

    public boolean isValid(String cmd) {

        if (null != cmd && !cmd.isEmpty() && this.isExist(cmd)) {

            if (Commandes.GET_PATIENT_MOVEMENTS_BY_SID.getValue().equals(cmd.split(" ")[0]) && cmd.split(" ").length != 2) {
                LOGGER.severe("La commande " + cmd + " n'est pas valide. Veuillez renseigner un parametre.");
                return false;

            } else if (Commandes.GET_PATIENT_BY_NAME.getValue().equals(cmd.split(" ")[0]) && cmd.split(" ").length != 2) {
                LOGGER.severe("La commande " + cmd + " n'est pas valide. Veuillez renseigner un parametre.");
                return false;
            } else if (Commandes.GET_PATIENT_STAY_BY_ID.getValue().equals(cmd.split(" ")[0]) && cmd.split(" ").length != 2) {
                LOGGER.severe("La commande " + cmd + " n'est pas valide. Veuillez renseigner un parametre.");
                return false;
            } else if (Commandes.GET_PATIENT_STAY_BY_ID.getValue().equals(cmd.split(" ")[0]) && cmd.split(" ").length != 2) {
                LOGGER.severe("La commande " + cmd + " n'est pas valide. Veuillez renseigner un parametre.");
                return false;
            } else if (Commandes.GET_PATIENT_MOVEMENTS_BY_SID.getValue().equals(cmd.split(" ")[0]) && cmd.split(" ").length != 2) {
                LOGGER.severe("La commande " + cmd + " n'est pas valide. Veuillez renseigner un parametre.");
                return false;
            }

            if (cmd.split(" ").length != 1) {
                return false;
            }
            return true;
        }
        return false;
    }

    public Map<String, String> getCommandesDef() {
        if (null == this.commandesDef) this.commandesDef = new HashMap<>();
        return this.commandesDef;
    }

    public void setCommandesDef(Map<String, String> commandesDef) {
        this.commandesDef = commandesDef;
    }

    public Producer<String, String> getProducer() {
        return producer;
    }

    public void setProducer(Producer<String, String> producer) {
        this.producer = producer;
    }

    public Consumer<String, String> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<String, String> consumer) {
        this.consumer = consumer;
    }
}

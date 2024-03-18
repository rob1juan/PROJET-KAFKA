package pr2cr3;

import org.xml.sax.SAXException;
import pr2cr3.commandes.CommandesUtils;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in); // Pour la lecture des user inputs
    private static final CommandesUtils CMD_UTILS = new CommandesUtils(); // Affichage des commandes

    public static void main(String[] args) {

        System.out.println("[INFO] Démarrage du Producer 2 & Consumer 3");
        System.out.println("[INFO] Veuillez patienter, le CLI démarre ...");


        while (true) {
            // Lecture de la commande user
            String commande = readUserInput();

            // Execution de la commande (send to topic : producer)
            try {
                CMD_UTILS.execute(commande);
            } catch (SAXException | IOException e) {
                System.out.println("[ERROR] Erreur lors de l'execution de la commande.");
                e.printStackTrace();
            }
        }
    }

    private static String readUserInput() {
        String uInput = "";
        do {
            System.out.print("\nEntrez votre commande (ex: help) : ");
            uInput = SCANNER.nextLine().toLowerCase();

            if (!CMD_UTILS.isValid(uInput)) {
                System.out.println("\n/!\\ Commande non reconnue, veuillez taper \"help\" pour plus d'informations.");
            }

        } while (!CMD_UTILS.isValid(uInput));

        return uInput;
    }
}
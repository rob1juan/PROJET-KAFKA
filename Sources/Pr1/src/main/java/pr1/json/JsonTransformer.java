package pr1.json;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonTransformer {

    public static String transformJson(String inputJson) {
        JsonObject inputObj = JsonParser.parseString(inputJson).getAsJsonObject();

        JsonObject outputObj = new JsonObject();

        // Patient Information
        JsonObject patientInfo = new JsonObject();
        patientInfo.addProperty("id_patient", inputObj.get("id_patient").getAsString());
        patientInfo.addProperty("birthday", inputObj.get("birthday").getAsString());
        patientInfo.addProperty("sex", inputObj.get("sex").getAsString());
        patientInfo.addProperty("nationnatilty", inputObj.get("nationnatilty").getAsString());

        JsonObject names = new JsonObject();
        // Suppose name_1 and name_2 are formatted as "firstName lastName"
        String[] name1Parts = inputObj.get("name_1").getAsString().split(" ");
        JsonObject name1 = new JsonObject();
        name1.addProperty("first_name", name1Parts[0]);
        name1.addProperty("last_name", name1Parts[1]);
        names.add("name_1", name1);

        String[] name2Parts = inputObj.get("name_2").getAsString().split(" ");
        JsonObject name2 = new JsonObject();
        name2.addProperty("first_name", name2Parts[0]);
        name2.addProperty("last_name", name2Parts[1]);
        names.add("name_2", name2);

        patientInfo.add("names", names);
        outputObj.add("patient_information", patientInfo);

        // Address
        JsonObject addressObj = new JsonObject();
        String[] addressParts = inputObj.get("address").getAsString().split(" - ");
        addressObj.addProperty("street", addressParts[0]);
        addressObj.addProperty("city", addressParts[1]);
        addressObj.addProperty("zip", addressParts[2]);
        outputObj.add("address", addressObj);// Movements
        JsonObject movementsObj = new JsonObject();
        movementsObj.addProperty("visit_number", inputObj.get("visit_number").getAsString());
        movementsObj.addProperty("service", inputObj.get("service").getAsString());
        movementsObj.addProperty("bed", inputObj.get("bed").getAsString());
        movementsObj.addProperty("room", inputObj.get("room").getAsString());
        movementsObj.addProperty("admit_time", inputObj.get("admit_time").getAsString());
        outputObj.add("movements", movementsObj);

        // Stay
        JsonObject stayObj = new JsonObject();
        stayObj.addProperty("visit_number", inputObj.get("visit_number").getAsString());
        stayObj.addProperty("start_time", inputObj.get("start_time").getAsString());
        stayObj.addProperty("end_time", inputObj.get("end_time").getAsString());
        outputObj.add("stay", stayObj);

        // Convertir l'objet de sortie en chaîne JSON
        return outputObj.toString();
    }
}
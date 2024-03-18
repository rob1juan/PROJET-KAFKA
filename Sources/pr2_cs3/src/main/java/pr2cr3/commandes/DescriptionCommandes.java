package pr2cr3.commandes;

public enum DescriptionCommandes {
    HELP("Affiche les commandes disponibles"),
    QUIT("Termine le priorgramme"),
    GET_ALL_PATIENTS("retourn tous les patients !"),
    GET_PATIENT_BY_ID("retourne l’identité complète d’un patient par son identifiant PID-3"),
    GET_PATIENT_BY_NAME("retourne l’identité d’un patient par son l’un de ses noms tout ou partie du nom !"),
    GET_PATIENT_STAY_BY_ID("retour les sejour d’un patient par son identifiant PID-3"),
    GET_PATIENT_MOVEMENTS_BY_SID("retourne tous les mouvements d’un patients par le numero de sejour"),
    EXPORT_PATIENTS("export les données de la base de données");


    private String value;

    private DescriptionCommandes (String _value) {
        this.value = _value;
    }

    public String getValue() {
        return this.value;
    }
}

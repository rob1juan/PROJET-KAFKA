package pr2cr3.commandes;

public enum Commandes {
    HELP("help"),
    QUIT("exit"),
    GET_ALL_PATIENTS("get_all_patients"),
    GET_PATIENT_BY_ID("get_patient_by_id"),
    GET_PATIENT_BY_NAME("get_patient_by_name"),
    GET_PATIENT_STAY_BY_ID("get_patient_stay_by_id"),
    GET_PATIENT_MOVEMENTS_BY_SID("get_patient_movements_by_sid"),
    EXPORT_PATIENTS("export_patients");


    private String value;

    private Commandes (String _value) {
        this.value = _value;
    }

    public String getValue() {
        return this.value;
    }
}

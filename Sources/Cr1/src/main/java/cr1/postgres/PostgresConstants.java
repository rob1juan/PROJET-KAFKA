package cr1.postgres;

public enum PostgresConstants {

    USERNAME("postgres"),
    PASSWORD("admin");

    String value;

    private PostgresConstants(String _value) {
        this.value = _value;
    }

    public String getValue() {
        return this.value;
    }
}

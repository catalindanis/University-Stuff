package models;

public enum DuckType {
    FLYING("Zburatoare"),
    SWIMMING("Inotatoare"),
    FLYING_AND_SWIMMING("Zburatoare si inotatoare");

    private final String name;

    DuckType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static DuckType fromString(String text) {
        if (text == null) return null;

        for (DuckType dt : DuckType.values()) {
            if (dt.toString().equalsIgnoreCase(text)) {
                return dt;
            }
        }

        return null;
    }
}

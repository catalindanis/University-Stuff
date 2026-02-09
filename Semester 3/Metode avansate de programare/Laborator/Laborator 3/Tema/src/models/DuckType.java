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
}

package models;

public enum UserType {
    admin("admin"),
    user("user");

    private final String name;

    UserType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static UserType fromString(String text) {
        if (text == null) return null;

        for (UserType ut : UserType.values()) {
            if (ut.toString().equalsIgnoreCase(text)) {
                return ut;
            }
        }

        return null;
    }
}

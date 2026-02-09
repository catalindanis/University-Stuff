package org.example.template.domain;

public enum UserRole {
    ADMIN("admin"),
    DEALER("dealer");

    private final String name;

    UserRole(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static UserRole fromString(String text) {
        if (text == null) return null;

        for (UserRole dt : UserRole.values()) {
            if (dt.toString().equalsIgnoreCase(text)) {
                return dt;
            }
        }

        return null;
    }
}

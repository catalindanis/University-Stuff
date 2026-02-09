package org.example.template.domain;

public enum CarStatus {
    NEW("new"),
    NEEDS_APPROVAL("needs_approval"),
    REJECTED("rejected"),
    APPROVED("approved");

    private final String name;

    CarStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static CarStatus fromString(String text) {
        if (text == null) return null;

        for (CarStatus dt : CarStatus.values()) {
            if (dt.toString().equalsIgnoreCase(text)) {
                return dt;
            }
        }

        return null;
    }
}

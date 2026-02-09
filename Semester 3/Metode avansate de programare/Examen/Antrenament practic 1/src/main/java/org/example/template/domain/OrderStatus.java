package org.example.template.domain;

public enum OrderStatus {
    PENDING("pending"),
    IN_PROGRESS("in_progress"),
    FINISHED("finished");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static OrderStatus fromString(String text) {
        if (text == null) return null;

        for (OrderStatus dt : OrderStatus.values()) {
            if (dt.toString().equalsIgnoreCase(text)) {
                return dt;
            }
        }

        return null;
    }
}

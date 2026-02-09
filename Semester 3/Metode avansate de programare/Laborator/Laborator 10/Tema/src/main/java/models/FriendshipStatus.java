package models;

public enum FriendshipStatus {
    WAITING("Asteptare"),
    APPROVED("Aprobata"),
    REFUSED("Refuzata");

    private final String name;

    FriendshipStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static FriendshipStatus fromString(String text) {
        if (text == null) return null;

        for (FriendshipStatus dt : FriendshipStatus.values()) {
            if (dt.toString().equalsIgnoreCase(text)) {
                return dt;
            }
        }

        return null;
    }
}

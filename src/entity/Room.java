package src.entity;

public class Room {
    private final int id;
    private final User host;

    public Room(int id, User host) {
        this.id = id;
        this.host = host;
    }

    public int getId() {
        return id;
    }

    public User getHost() {
        return host;
    }
}

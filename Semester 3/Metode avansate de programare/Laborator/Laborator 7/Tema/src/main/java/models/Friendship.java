package models;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Friendship extends Entity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long[] users;

    public Friendship(long id, Long user1, Long user2) {
        super(id);
        this.users = new Long[]{user1, user2};
    }

    public Long[] getUsers() {
        return users;
    }

    public void setUsers(Long[] users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if(super.equals(o)) return true;

        Friendship that = (Friendship) o;
        Set<Long> set1 = new HashSet<>(Arrays.asList(users));
        Set<Long> set2 = new HashSet<>(Arrays.asList(that.users));
        return set1.equals(set2);
    }

    @Override
    public String toString() {
        return super.toString() + ", " +
                "utilizatori=" + Arrays.toString(users);
    }
}

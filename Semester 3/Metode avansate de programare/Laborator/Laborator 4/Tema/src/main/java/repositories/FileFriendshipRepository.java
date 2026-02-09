package repositories;

import exceptions.FriendshipException;
import exceptions.RepositoryException;
import models.Friendship;
import utils.Files;

import java.io.*;
import java.util.List;

public class FileFriendshipRepository extends FriendshipsRepository implements FileRepository {
    private final String filename;

    public FileFriendshipRepository(String fileName) {
        this.filename = fileName;
        if(Files.createIfNotExists(fileName))
            write();
        read();
    }

    @Override
    public Friendship add(Friendship friendship) {
        friendships.add(friendship);
        write();
        return friendship;
    }

    @Override
    public Friendship remove(Friendship friendship) {
        if(!friendships.remove(friendship))
            throw new FriendshipException("Friendship not found");

        write();
        return friendship;
    }

    @Override
    public void read() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
            friendships = (List<Friendship>) in.readObject();
            in.close();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public void write() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
            out.writeObject(friendships);
            out.close();
        } catch (IOException e) {
            throw new RepositoryException(e.getMessage());
        }
    }
}

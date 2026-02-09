package repositories;

import exceptions.RepositoryException;
import exceptions.UserException;
import models.User;

import java.io.*;
import java.util.List;

public class FileUsersRepository extends UsersRepository implements FileRepository {
    String fileName;

    public FileUsersRepository(String fileName) {
        this.fileName = fileName;
        File file = new File(fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
                write();
            }
        } catch (IOException e) {
            throw new RepositoryException(e.getMessage());
        }
        read();
    }

    @Override
    public User add(User user) {
        users.add(user);
        write();
        return user;
    }

    @Override
    public User remove(User user) {
        if(!users.remove(user))
            throw new UserException("User not found");

        write();
        return user;
    }

    @Override
    public void read() {
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName));
            users = (List<User>) input.readObject();
            input.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public void write() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(users);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}

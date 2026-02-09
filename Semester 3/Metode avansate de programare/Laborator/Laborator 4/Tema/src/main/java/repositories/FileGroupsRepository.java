package repositories;

import exceptions.GroupException;
import exceptions.RepositoryException;
import models.Group;
import utils.Files;

import java.io.*;
import java.util.List;

public class FileGroupsRepository extends GroupsRepository implements FileRepository {
    String fileName;

    public FileGroupsRepository(String fileName) {
        this.fileName = fileName;
        if(Files.createIfNotExists(fileName))
            write();
        read();
    }

    @Override
    public Group add(Group group) {
        groups.add(group);
        write();
        return group;
    }

    @Override
    public Group remove(Group group) {
        if(!groups.remove(group))
            throw new GroupException("Group not found");

        write();
        return group;
    }

    @Override
    public void read() {
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName));
            groups = (List<Group>) input.readObject();
            input.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public void write() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(groups);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}

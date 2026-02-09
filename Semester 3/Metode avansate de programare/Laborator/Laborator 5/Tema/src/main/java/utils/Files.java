package utils;

import exceptions.RepositoryException;

import java.io.File;
import java.io.IOException;

public class Files {
    public static boolean createIfNotExists(String fileName) {
        File file = new File(fileName);
        try {
               return file.createNewFile();
        } catch (IOException e) {
            throw new RepositoryException(e.getMessage());
        }
    }
}

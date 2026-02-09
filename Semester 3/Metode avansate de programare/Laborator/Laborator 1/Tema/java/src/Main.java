import container.Container;
import container.ContainerFactory;
import container.TaskContainerFactory;
import models.Duck;
import models.DuckTask;
import models.Lane;
import models.Race;
import runner.DuckTaskRunner;
import runner.TaskRunner;
import utils.ContainerStrategy;
import utils.DuckTaskStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("~App starting~");

        System.out.println("Loading data from file...");
        Race[] races = readRacesFromFile("natatie.in");
        System.out.println("Data loaded successfully");

        System.out.println("Creating and initializing task runner...");
        TaskRunner duckTaskRunner = createAndInitializeTaskRunner(ContainerStrategy.FIFO);
        System.out.println("Task runner created and initialized successfully");

        System.out.println("~App started~");
        System.out.println();

        for(int i=0; i < races.length; i++) {
            duckTaskRunner.addTask(new DuckTask(Integer.toString(i + 1) , "", races[i], DuckTaskStrategy.BACKTRACKING));
        }

        duckTaskRunner.executeAll();
    }

    public static TaskRunner createAndInitializeTaskRunner(ContainerStrategy strategy) {
        ContainerFactory factory = TaskContainerFactory.getInstance();
        Container container = factory.createContainer(strategy);

        return new DuckTaskRunner(container);
    }

    public static Race[] readRacesFromFile(String path) {
        File file = new File(path);
        Scanner scanner;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            return null;
        }

        String line = scanner.nextLine();
        Race[] races = new Race[Integer.parseInt(line)];

        for(int raceIndex=0; raceIndex < races.length; raceIndex++) {

            line = scanner.nextLine();
            String[] values = line.split(" ");

            int noDucks = Integer.parseInt(values[0]);
            int noLanes = Integer.parseInt(values[1]);

            Duck[] ducks = new Duck[noDucks];

            for (int i = 0; i < noDucks; i++) {
                line = scanner.nextLine();
                values = line.split(" ");
                ducks[i] = new Duck(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
            }

            line = scanner.nextLine();
            values = line.split(" ");

            Lane[] lanes = new Lane[noLanes];

            for (int i = 0; i < noLanes; i++)
                lanes[i] = new Lane(Integer.parseInt(values[i]));

            races[raceIndex] = new Race(noDucks, ducks, noLanes, lanes);
        }

        return races;
    }
}
package org.example;

import org.example.model.MessageTask;
import org.example.model.Task;
import org.example.runner.AbstractTaskRunner;
import org.example.runner.PrinterTaskRunner;
import org.example.runner.StrategyTaskRunner;
import org.example.runner.TaskRunner;
import org.example.utils.Strategy;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Task task1 = new MessageTask("id1",
                "descriere1",
                "mesaj1",
                "eu1",
                "tu1",
                LocalDateTime.now());

//        task1.execute();

        Task task2 = new MessageTask("id2",
                "descriere2",
                "mesaj2",
                "eu2",
                "tu2",
                LocalDateTime.now());

        Task task3 = new MessageTask("id3",
                "descriere3",
                "mesaj3",
                "eu3",
                "tu3",
                LocalDateTime.now());

        Task task4 = new MessageTask("id4",
                "descriere4",
                "mesaj4",
                "eu4",
                "tu4",
                LocalDateTime.now());

        Task task5 = new MessageTask("id5",
                "descriere5",
                "mesaj5",
                "eu5",
                "tu5",
                LocalDateTime.now());

        Task[] tasks = new Task[5];
        tasks[0] = task1;
        tasks[1] = task2;
        tasks[2] = task3;
        tasks[3] = task4;
        tasks[4] = task5;

//        System.out.println(tasks.length);

//        for(int i = 0; i < 5; i++)
//            tasks[i].execute();

//        StackContainer stack = new StackContainer();
//        stack.add(task1);
//        stack.add(task2);
//        stack.add(task3);
//
//        for(int i = 0; i < 3; i++)
//            System.out.println(stack.remove());
//
//        System.out.println("Tests passed!");

        TaskRunner taskRunner = new StrategyTaskRunner(Strategy.LIFO);
        for(Task t : tasks) {
            taskRunner.addTask(t);
        }

//        taskRunner.executeAll();

        AbstractTaskRunner decorator = new PrinterTaskRunner(taskRunner);
        decorator.executeAll();
    }
}
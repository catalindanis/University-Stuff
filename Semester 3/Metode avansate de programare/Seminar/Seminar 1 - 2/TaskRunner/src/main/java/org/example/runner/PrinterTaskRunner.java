package org.example.runner;

import org.example.utils.Constants;

import java.time.LocalDateTime;

public class PrinterTaskRunner extends AbstractTaskRunner {
    public PrinterTaskRunner(TaskRunner taskRunner) {
        super(taskRunner);
    }

    @Override
    public void executeOneTask() {
        super.executeOneTask();
        System.out.println("Task executed at: " + LocalDateTime.now().format(Constants.FORMATTER));
    }
}

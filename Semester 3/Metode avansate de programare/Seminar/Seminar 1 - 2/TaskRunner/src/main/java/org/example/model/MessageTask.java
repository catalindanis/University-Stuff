package org.example.model;

import java.time.LocalDateTime;

public class MessageTask extends Task {
    private String message;
    private String from;
    private String to;
    private LocalDateTime date;

    public MessageTask(String taskId, String description,
                       String message, String from, String to, LocalDateTime date) {
        super(taskId, description);
        this.message = message;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    @Override
    public void execute() {
//        System.out.println("Message: " + message + "\n" + date.format(FORMATTER));
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "MessageTask{" +
                "message='" + message + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", date=" + date +
                '}';
    }
}

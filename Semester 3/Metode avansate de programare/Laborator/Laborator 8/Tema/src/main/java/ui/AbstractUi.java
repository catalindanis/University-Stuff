package ui;

import java.util.Scanner;

public abstract class AbstractUi implements Ui {
    boolean exitRequested;

    protected AbstractUi() {
        exitRequested = false;
    }

    public void show() {
        exitRequested = false;
        while(!exitRequested) {
            print();
            handle(input());
        }
    }

    @Override
    public String input() {
        return new Scanner(System.in).nextLine();
    }
}

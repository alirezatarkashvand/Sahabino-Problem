package ir.sahab.monitoringsystem.rulesevaluator;

import java.util.LinkedList;
import java.util.Scanner;

public class RulesEvaluator {

    public static void run(Director director) {
        Thread thread = new Thread(director);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    private static void shutdownByUserCommand(Director director) {
        String input = "";
        Scanner scanner = new Scanner(System.in);
        while(!input.equals("shutdown")) {
            input = scanner.next();
        }
        director.stop();
    }

    public static void main(String[] args) {
        Director director = new Director();
        run(director);
        shutdownByUserCommand(director);
    }
}

package benchmarks.programs.threads_management;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ThreadManagement {

    static class WorkerThread extends Thread {
        @Override
        public void run() {
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: <program> <numOfThreads> <numOfTests>");
            return;
        }

        int numOfThreads;
        int numOfTests;

        try {
            numOfThreads = Integer.parseInt(args[0]);
            numOfTests = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: Expected two integers.");
            return;
        }

        double totalElapsedTime = 0.0;
        String filePath = "..\\..\\result_files\\threads_management\\java_thread_management_result.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            System.out.println("File opened.");

            for (int i = 0; i < numOfTests; i++) {
                long startTime = System.nanoTime();

                for (int j = 0; j < numOfThreads; j++) {
                    WorkerThread thread = new WorkerThread();
                    thread.start();

                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        System.out.println("Thread interrupted: " + e.getMessage());
                    }
                }

                long endTime = System.nanoTime();
                double elapsedTime = (endTime - startTime) / 1_000.0;
                totalElapsedTime += elapsedTime;

                writer.write((i + 1) + " " + String.format("%.3f", elapsedTime));
                writer.newLine();
            }

            double averageTime = totalElapsedTime / numOfTests;
            writer.write(String.format("%.3f", averageTime));
            writer.newLine();

        } catch (IOException e) {
            System.out.println("Failed to write to the file: " + e.getMessage());
        }
    }
}

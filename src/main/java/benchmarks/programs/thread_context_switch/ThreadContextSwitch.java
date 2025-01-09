package benchmarks.programs.thread_context_switch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Semaphore;

public class ThreadContextSwitch {

    static class ThreadFunction implements Runnable {
        private final Semaphore ownSemaphore;
        private final Semaphore nextSemaphore;

        public ThreadFunction(Semaphore ownSemaphore, Semaphore nextSemaphore) {
            this.ownSemaphore = ownSemaphore;
            this.nextSemaphore = nextSemaphore;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    ownSemaphore.acquire();
                    nextSemaphore.release();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static int getNumberOfTests(String[] args) {
        if (args.length < 2) {
            System.out.println("Not enough arguments. Usage: <program> <number_of_tests>");
            System.exit(1);
        }

        int numOfTests = 0;
        try {
            numOfTests = Integer.parseInt(args[1]);
            if (numOfTests <= 0) {
                throw new IllegalArgumentException("Invalid input: Expected a positive integer.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
            System.exit(1);
        }

        return numOfTests;
    }

    public static void main(String[] args) {
        int numOfTests = getNumberOfTests(args);

        Semaphore semaphore1 = new Semaphore(0);
        Semaphore semaphore2 = new Semaphore(0);

        Thread thread1 = new Thread(new ThreadFunction(semaphore1, semaphore2));
        Thread thread2 = new Thread(new ThreadFunction(semaphore2, semaphore1));

        thread1.start();
        thread2.start();

        double totalSwitchTime = 0.0;
        long start, end;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("..\\..\\result_files\\thread_context_switch\\java_thread_context_switch_result.txt"))) {
            for (int i = 0; i < numOfTests + 1; i++) {
                semaphore1.release();

                start = System.nanoTime();
                semaphore2.acquire();
                end = System.nanoTime();

                double switchTime = (end - start) / 1_000.0;
                if (i != 0) {
                    totalSwitchTime += switchTime;
                    writer.write(i + " " + String.format("%.3f", switchTime));
                    writer.newLine();
                }
            }

            double averageSwitchTime = totalSwitchTime / numOfTests;
            writer.write(String.format("%.3f", averageSwitchTime));
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        thread1.interrupt();
        thread2.interrupt();
    }
}

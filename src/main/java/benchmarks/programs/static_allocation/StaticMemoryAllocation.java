package benchmarks.programs.static_allocation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class StaticMemoryAllocation {
    private static final int ARRAY_SIZE = 10000;
    static void useArray(int[] array) {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = i + 1;
        }
    }

    static int getNumberOfTests(String[] args) {
        if (args.length < 2) {
            System.out.println("Not enough arguments");
            return -1;
        }

        int numberOfTests;
        numberOfTests = Integer.parseInt(args[1]);
        return numberOfTests;
    }

    public static void main(String[] args) {
        int numberOfTests = getNumberOfTests(args);
        double totalElapsedTime = 0.0;


        try (PrintWriter file = new PrintWriter(new FileWriter("..\\..\\result_files\\static_allocation\\java_static_allocation_result.txt"))) {
            System.out.println("File opened.");

            for (int i = 0; i < numberOfTests; i++) {
                long start = System.nanoTime();
                int[] stackArray = new int[ARRAY_SIZE];
                long end = System.nanoTime();

                useArray(stackArray);
                double elapsedTime = (end - start) / 1000.0;

                totalElapsedTime += elapsedTime;

                file.printf("%d %.3f\n", i + 1, elapsedTime);
            }

            double averageTime = totalElapsedTime / numberOfTests;
            file.printf("%.3f\n", averageTime);

        } catch (IOException e) {
            System.err.println("Error opening file for writing: " + e.getMessage());
        }

    }
}

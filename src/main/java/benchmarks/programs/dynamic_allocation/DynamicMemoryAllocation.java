package benchmarks.programs.dynamic_allocation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DynamicMemoryAllocation {

    static void useArray(String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = "number" + i;
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java DynamicMemoryAllocation <arraySize> <numRuns>");
            return;
        }

        int arraySize;
        try {
            arraySize = Integer.parseInt(args[0]);
            if (arraySize <= 0) {
                System.out.println("Array size must be a positive integer.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid array size. Please provide a positive integer.");
            return;
        }

        int numRuns;
        try {
            numRuns = Integer.parseInt(args[1]);
            if (numRuns <= 0) {
                System.out.println("Number of runs must be a positive integer.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number of runs. Please provide a positive integer.");
            return;
        }

        double totalElapsedTime = 0.0;

        try (PrintWriter file = new PrintWriter(new FileWriter("..\\..\\result_files\\dynamic_allocation\\java_dynamic_allocation_result.txt"))) {
            System.out.println("File opened.");

            for (int i = 0; i < numRuns; i++) {
                long start = System.nanoTime();
                String[] dynamicArray = new String[arraySize];
                useArray(dynamicArray);
                long end = System.nanoTime();
                double elapsedTime = (end - start) / 1_000_000.0;

                totalElapsedTime += elapsedTime;

                file.printf("%d %.3f\n", i + 1, elapsedTime);
            }

            double averageTime = totalElapsedTime / numRuns;
            file.printf("%.3f\n", averageTime);

        } catch (IOException e) {
            System.err.println("Error opening file for writing: " + e.getMessage());
        }
    }
}

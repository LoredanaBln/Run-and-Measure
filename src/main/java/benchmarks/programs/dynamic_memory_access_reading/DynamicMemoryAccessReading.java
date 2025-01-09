package benchmarks.programs.dynamic_memory_access_reading;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class DynamicMemoryAccessReading {

    static void initializeArray(Integer[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
    }

    static void readFromArray(Integer[] arr) {
        for (Integer s : arr) {
            if (s == null) continue;
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

        try (PrintWriter file = new PrintWriter(new FileWriter("E:\\AN3\\ssc\\PROIECT\\run_and_measure\\src\\main\\java\\benchmarks\\result_files\\dynamic_memory_access_reading\\java_dynamic_memory_access_reading_result.txt"))) {
            System.out.println("File opened.");

            for (int i = 0; i < numRuns; i++) {
                Integer[] dynamicArray = new Integer[arraySize];
                initializeArray(dynamicArray);

                long start = System.nanoTime();
                readFromArray(dynamicArray);
                long end = System.nanoTime();

                double elapsedTime = (end - start) / 1000.0;
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
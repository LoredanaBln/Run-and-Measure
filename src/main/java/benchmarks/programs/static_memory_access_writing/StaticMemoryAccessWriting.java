package benchmarks.programs.static_memory_access_writing;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class StaticMemoryAccessWriting {
    private static final int ARRAY_SIZE = 1000;
    static void writeInArray(int[] array) {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = i;
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


        try (PrintWriter file = new PrintWriter(new FileWriter("..\\..\\result_files\\static_memory_access_writing\\java_static_memory_access_writing_result.txt"))) {
            System.out.println("File opened.");

            for (int i = 0; i < numberOfTests; i++) {
                int[] stackArray = new int[ARRAY_SIZE];

                long start = System.nanoTime();
                writeInArray(stackArray);
                long end = System.nanoTime();

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
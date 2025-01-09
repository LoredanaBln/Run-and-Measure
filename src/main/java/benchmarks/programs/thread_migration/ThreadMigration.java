package benchmarks.programs.thread_migration;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

import java.io.*;

public class ThreadMigration {

    public interface Kernel32 extends Library {
        Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);

        Pointer GetCurrentThread();

        void SetThreadAffinityMask(Pointer hThread, Pointer dwAffinityMask);
    }

    public static class DWORD_PTR extends PointerType {
        public DWORD_PTR(long value) {
            super(Pointer.createConstant(value));
        }
    }

    public static void bindCurrentThreadToCPU(int cpuId) {
        Kernel32 kernel32 = Kernel32.INSTANCE;
        DWORD_PTR affinityMask = new DWORD_PTR(1L << cpuId);
        Pointer hThread = kernel32.GetCurrentThread();

        kernel32.SetThreadAffinityMask(hThread, affinityMask.getPointer());
    }

    public static int getNumberOfTests(String[] args) {
        if (args.length < 2) {
            System.out.println("Not enough arguments");
            System.exit(1);
        }

        int numOfTests;
        try {
            numOfTests = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: Expected an integer");
            System.exit(1);
            return 0;
        }

        return numOfTests;
    }

    public static void main(String[] args) {
        int numProcessors = Runtime.getRuntime().availableProcessors();

        if (numProcessors < 2) {
            System.out.println("This test requires at least 2 processors.");
            return;
        }

        bindCurrentThreadToCPU(0);

        int numOfTests = getNumberOfTests(args);
        double totalMigrationTime = 0.0;

        String filePath = "..\\..\\result_files\\thread_migration\\java_thread_migration_result.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (int i = 0; i < numOfTests; i++) {
                long startTime = System.nanoTime();
                bindCurrentThreadToCPU(i % numProcessors);
                long endTime = System.nanoTime();

                double migrationTime = (endTime - startTime) / 1000.0;
                totalMigrationTime += migrationTime;

                writer.printf("%d %.3f\n", i + 1, migrationTime);
            }

            double averageMigrationTime = totalMigrationTime / numOfTests;
            writer.printf("%.3f\n", averageMigrationTime);
        } catch (IOException e) {
            System.out.println("Failed to write to the file: " + e.getMessage());
        }
        bindCurrentThreadToCPU(0);
    }
}
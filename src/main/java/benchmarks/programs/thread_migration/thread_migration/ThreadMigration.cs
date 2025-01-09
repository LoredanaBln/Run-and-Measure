using System;
using System.IO;
using System.Runtime.InteropServices;

namespace ThreadMigration
{
    class Program
    {
        [DllImport("kernel32.dll")]
        private static extern IntPtr GetCurrentThread();

        [DllImport("kernel32.dll")]
        private static extern IntPtr SetThreadAffinityMask(IntPtr hThread, IntPtr dwThreadAffinityMask);

        [DllImport("kernel32.dll")]
        private static extern bool QueryPerformanceCounter(out long lpPerformanceCount);

        [DllImport("kernel32.dll")]
        private static extern bool QueryPerformanceFrequency(out long lpFrequency);

        static void BindCurrentThreadToCPU(int cpuId)
        {
            IntPtr currentThread = GetCurrentThread();
            IntPtr affinityMask = new IntPtr(1L << cpuId);

            IntPtr previousMask = SetThreadAffinityMask(currentThread, affinityMask);
            if (previousMask == IntPtr.Zero)
            {
                Console.WriteLine($"Failed to set thread affinity to CPU {cpuId}");
            }
        }

        static int GetNumberOfTests(string[] args)
        {
            if (args.Length < 2)
            {
                Console.WriteLine("Not enough arguments");
                Environment.Exit(1);
            }

            if (!int.TryParse(args[1], out int numOfTests))
            {
                Console.WriteLine("Invalid input: Expected an integer");
                Environment.Exit(1);
            }

            return numOfTests;
        }

        static double MeasureTime(Action action)
        {
            QueryPerformanceFrequency(out long frequency);
            QueryPerformanceCounter(out long start);

            action();

            QueryPerformanceCounter(out long end);
            return (end - start) * 1_000_000.0 / frequency;
        }

        static void Main(string[] args)
        {
            int numProcessors = Environment.ProcessorCount;

            if (numProcessors < 2)
            {
                Console.WriteLine("This test requires at least 2 processors.");
                return;
            }

            BindCurrentThreadToCPU(0);

            int numOfTests = GetNumberOfTests(args);
            double totalMigrationTime = 0.0;

            string filePath = "../../../../../../result_files/thread_migration/csharp_thread_migration_result.txt";

            try
            {
                using (StreamWriter writer = new StreamWriter(filePath))
                {
                    for (int i = 0; i < numOfTests; i++)
                    {
                        int targetCPU = i % numProcessors;

                        double migrationTime = MeasureTime(() =>
                        {
                            BindCurrentThreadToCPU(targetCPU);
                        });

                        totalMigrationTime += migrationTime;
                        writer.WriteLine($"{i + 1} {migrationTime:F3}");
                    }

                    double averageMigrationTime = totalMigrationTime / numOfTests;
                    writer.WriteLine($"{averageMigrationTime:F3}");
                }
            }
            catch (IOException e)
            {
                Console.WriteLine($"Failed to write to the file: {e.Message}");
            }

            BindCurrentThreadToCPU(0);
        }
    }
}

using System;
using System.IO;
using System.Runtime.InteropServices;
using System.Threading;

class CsThreadManagement
{
    [DllImport("kernel32.dll")]
    public static extern bool QueryPerformanceFrequency(out long frequency);

    [DllImport("kernel32.dll")]
    public static extern bool QueryPerformanceCounter(out long count);

    static void WorkerThread()
    {
    }

    static void Main(string[] args)
    {
        if (args.Length < 2)
        {
            Console.WriteLine("Usage: <program> <numOfThreads> <numOfTests>");
            return;
        }

        if (!int.TryParse(args[0], out int numOfThreads) || numOfThreads <= 0 ||
            !int.TryParse(args[1], out int numOfTests) || numOfTests <= 0)
        {
            Console.WriteLine("Invalid input: Expected two positive integers.");
            return;
        }

        if (!QueryPerformanceFrequency(out long frequency))
        {
            Console.WriteLine("High-resolution timer not supported.");
            return;
        }

        double totalElapsedTime = 0.0;
        string filePath = "..\\..\\result_files\\threads_management\\cs_thread_management_result.txt";

        using (StreamWriter file = new StreamWriter(filePath))
        {
            Console.WriteLine("File opened.");

            for (int i = 0; i < numOfTests; i++)
            {
                QueryPerformanceCounter(out long start);

                for (int j = 0; j < numOfThreads; j++)
                {
                    Thread thread = new Thread(WorkerThread);
                    thread.Start();

                    thread.Join();
                }

                QueryPerformanceCounter(out long end);

                double elapsedTime = ((double)(end - start) * 1000000) / frequency;
                totalElapsedTime += elapsedTime;

                file.WriteLine($"{i + 1} {elapsedTime:F3}");
            }

            double averageTime = totalElapsedTime / numOfTests;
            file.WriteLine($"{averageTime:F3}");

            Console.WriteLine("Results written to file.");
        }
    }
}

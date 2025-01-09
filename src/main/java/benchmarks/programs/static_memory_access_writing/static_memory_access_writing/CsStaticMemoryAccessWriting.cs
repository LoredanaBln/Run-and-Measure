using System;
using System.IO;
using System.Runtime.InteropServices;

class CsStaticMemoryAccessWriting
{
    const int ARRAY_SIZE = 1000;

    [DllImport("kernel32.dll")]
    public static extern bool QueryPerformanceFrequency(out long frequency);

    [DllImport("kernel32.dll")]
    public static extern bool QueryPerformanceCounter(out long count);

    static void WriteInArray(Span<int> arr, int size)
    {
        for (int i = 0; i < size; i++)
        {
            arr[i] = i;
        }
    }

    static void UseArray(Span<int> arr, int size)
    {
        for (int i = 0; i < size; i++)
        {
            arr[i] = i;
        }
    }

    static void Main(string[] args)
    {
        if (args.Length < 1)
        {
            Console.WriteLine("Usage: StaticAllocation <number_of_runs>");
            return;
        }

        if (!int.TryParse(args[1], out int numRuns) || numRuns < 1)
        {
            Console.WriteLine("Invalid input: <number_of_runs> must be a positive integer.");
            return;
        }

        double totalElapsedTime = 0.0;

        QueryPerformanceFrequency(out long frequency);

        string filePath = "..\\..\\result_files\\static_memory_access_writing\\cs_static_memory_access_writing_result.txt";

        using (StreamWriter file = new StreamWriter(filePath))
        {
            Console.WriteLine("File opened.");

            for (int i = 0; i < numRuns + 1; i++)
            {
                Span<int> stackArray = stackalloc int[ARRAY_SIZE];

                QueryPerformanceCounter(out long start);
                WriteInArray(stackArray, ARRAY_SIZE);
                QueryPerformanceCounter(out long end);

                double elapsedTime = ((double)(end - start) * 1_000_000.0) / frequency;

                if (i != 0)
                {
                    file.WriteLine($"{i} {elapsedTime:F3} ");
                    totalElapsedTime += elapsedTime;
                }
            }

            double averageTime = totalElapsedTime / numRuns;
            file.WriteLine($"{averageTime:F3}");
        }
    }
}

    using System;
    using System.IO;
    using System.Runtime.InteropServices;

    namespace benchmarks
    {
        class StaticAllocation
        {
                const int ARRAY_SIZE = 10000;

                [DllImport("kernel32.dll")]
                public static extern bool QueryPerformanceFrequency(out long frequency);
                [DllImport("kernel32.dll")]
                public static extern bool QueryPerformanceCounter(out long count);

                static void UseArray(Span<int> arr, int size)
                {
                    for (int i = 0; i < size; i++)
                    {
                        arr[i] = i;
                    }
                }

                static void Main(string[] args)
                {
                    if (args.Length < 2)
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

                    string filePath = "..\\..\\..\\..\\..\\..\\result_files\\static_allocation\\cs_static_allocation_result.txt";
                    using (StreamWriter file = new StreamWriter(filePath))
                    {
                        Console.WriteLine("File opened.");

                        for (int i = 0; i < numRuns + 1; i++)
                        {
                            QueryPerformanceCounter(out long start);
                            Span<int> stackArray = stackalloc int[ARRAY_SIZE];
                            QueryPerformanceCounter(out long end);

                            double elapsedTime = ((double)(end - start) * 1_000_000.0) / frequency;

                            UseArray(stackArray, ARRAY_SIZE);

                            if (i != 0)
                            {
                                file.WriteLine($"{i} {elapsedTime:F3} ");
                                totalElapsedTime += elapsedTime;
                            }
                        }

                        double averageTime = totalElapsedTime / (numRuns - 1);
                        file.WriteLine($"{averageTime:F3}");
                    }
                }
            }
    }

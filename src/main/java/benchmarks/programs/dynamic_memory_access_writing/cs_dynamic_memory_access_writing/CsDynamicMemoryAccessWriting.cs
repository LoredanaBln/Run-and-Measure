using System;
using System.IO;
using System.Runtime.InteropServices;

namespace Benchmarks
{
    class Point
    {
        public int X;
        public int Y;
    }

    class CsDynamicMemoryAccessWriting
    {
        [DllImport("kernel32.dll")]
        public static extern bool QueryPerformanceFrequency(out long frequency);

        [DllImport("kernel32.dll")]
        public static extern bool QueryPerformanceCounter(out long count);

        static void WriteInArray(Point[] arr, int size)
        {
            for (int i = 0; i < size; i++)
            {
                arr[i] = new Point { X = i, Y = i + 1 };
            }
        }

        static double MeasureAllocationTime(int arraySize, long frequency)
        {
            Point[] dynamicArray = new Point[arraySize];
            QueryPerformanceCounter(out long start);
            WriteInArray(dynamicArray, arraySize);
            QueryPerformanceCounter(out long end);

            double elapsedTime = ((double)(end - start) * 1000000) / frequency;

            return elapsedTime;
        }

        static void Main(string[] args)
        {
            if (args.Length < 2 ||
                !int.TryParse(args[0], out int arraySize) || arraySize <= 0 ||
                !int.TryParse(args[1], out int numRuns) || numRuns <= 0)
            {
                Console.WriteLine("Usage: <program> <arraySize> <numRuns>");
                return;
            }

            double totalElapsedTime = 0.0;

            if (!QueryPerformanceFrequency(out long frequency))
            {
                Console.WriteLine("High-resolution timer not supported.");
                return;
            }

            string filePath = "..\\..\\..\\..\\..\\..\\result_files\\dynamic_memory_access_writing\\cs_dynamic_memory_access_writing_result.txt";

            using (StreamWriter file = new StreamWriter(filePath))
            {
                Console.WriteLine("File opened.");

                for (int i = 0; i < numRuns + 1; i++)
                {
                    double elapsedTime = MeasureAllocationTime(arraySize, frequency);
                    totalElapsedTime += elapsedTime;

                    if(i != 0){
                        file.WriteLine($"{i} {elapsedTime:F3}");
                    }
                }

                double averageTime = totalElapsedTime / numRuns;
                file.WriteLine($"{averageTime:F3}");

            }
        }
    }
}

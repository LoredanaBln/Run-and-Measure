using System;
using System.IO;
using System.Runtime.InteropServices;
using System.Threading;

class ThreadContextSwitch
{
    [DllImport("kernel32.dll")]
    public static extern bool QueryPerformanceCounter(out long lpPerformanceCount);

    [DllImport("kernel32.dll")]
    public static extern bool QueryPerformanceFrequency(out long lpFrequency);

    class ThreadFunction
    {
        private readonly ManualResetEvent ownEvent;
        private readonly ManualResetEvent nextEvent;

        public ThreadFunction(ManualResetEvent ownEvent, ManualResetEvent nextEvent)
        {
            this.ownEvent = ownEvent;
            this.nextEvent = nextEvent;
        }

        public void Run()
        {
            while (true)
            {
                ownEvent.WaitOne();
                nextEvent.Set();
            }
        }
    }

    public static int GetNumberOfTests(string[] args)
    {
        if (args.Length < 2)
        {
            Console.WriteLine("Not enough arguments. Usage: <program> <number_of_tests>");
            Environment.Exit(1);
        }

        int numOfTests;
        if (!int.TryParse(args[1], out numOfTests) || numOfTests <= 0)
        {
            Console.WriteLine("Invalid input: Expected a positive integer.");
            Environment.Exit(1);
        }

        return numOfTests;
    }

    public static void Main(string[] args)
    {
        int numOfTests = GetNumberOfTests(args);

        ManualResetEvent event1 = new ManualResetEvent(false);
        ManualResetEvent event2 = new ManualResetEvent(false);

        ThreadFunction thread1Function = new ThreadFunction(event1, event2);
        ThreadFunction thread2Function = new ThreadFunction(event2, event1);

        Thread thread1 = new Thread(new ThreadStart(thread1Function.Run));
        Thread thread2 = new Thread(new ThreadStart(thread2Function.Run));

        thread1.Start();
        thread2.Start();

        QueryPerformanceFrequency(out long frequency);

        double totalSwitchTime = 0.0;
        string filePath = "../../../../../../result_files/thread_context_switch/csharp_thread_context_switch_result.txt";

        using (StreamWriter writer = new StreamWriter(filePath))
        {
            for (int i = 0; i < numOfTests + 1; i++)
            {
                event1.Set();

                QueryPerformanceCounter(out long startTime);

                event2.WaitOne();

                QueryPerformanceCounter(out long endTime);

                event1.Reset();
                event2.Reset();

                double switchTime = (endTime - startTime) * 1e6 / frequency;

                if (i != 0)
                {
                    totalSwitchTime += switchTime;
                    writer.WriteLine($"{i} {switchTime:F3}");
                }
            }

            double averageSwitchTime = totalSwitchTime / numOfTests;
            writer.WriteLine($"{averageSwitchTime:F3}");
        }

        thread1.Interrupt();
        thread2.Interrupt();
    }
}

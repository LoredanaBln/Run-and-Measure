#include <windows.h>
#include <stdio.h>
#include <stdlib.h>

DWORD WINAPI ThreadFunction(LPVOID lpParam) {
    HANDLE* events = (HANDLE*)lpParam;
    HANDLE ownEvent = events[0];
    HANDLE nextEvent = events[1];

    while (1) {
        WaitForSingleObject(ownEvent, INFINITE);
        SetEvent(nextEvent);
    }

    return 0;
}

int getNumberOfTests(int argc, char* argv[]) {
    if (argc < 3) {
        printf("Not enough arguments. Usage: <program> <number_of_tests>\n");
        exit(1);
    }

    int numOfTests;
    if (sscanf(argv[2], "%d", &numOfTests) != 1 || numOfTests <= 0) {
        printf("Invalid input: Expected a positive integer.\n");
        exit(1);
    }

    return numOfTests;
}

int main(int argc, char* argv[]) {
    SYSTEM_INFO systemInfo;
    LARGE_INTEGER start, end, frequency;
    HANDLE hThread1, hThread2;
    HANDLE event1, event2;
    DWORD_PTR originalAffinityMask;

    GetSystemInfo(&systemInfo);
    int numProcessors = systemInfo.dwNumberOfProcessors;

    if (numProcessors < 2) {
        printf("This test requires at least 2 processors.\n");
        return 1;
    }

    int numOfTests = getNumberOfTests(argc, argv);

    event1 = CreateEvent(NULL, TRUE, FALSE, NULL);
    event2 = CreateEvent(NULL, TRUE, FALSE, NULL);

    HANDLE thread1Params[2] = { event1, event2 };
    HANDLE thread2Params[2] = { event2, event1 };

    hThread1 = CreateThread(NULL, 0, ThreadFunction, thread1Params, 0, NULL);
    hThread2 = CreateThread(NULL, 0, ThreadFunction, thread2Params, 0, NULL);

    originalAffinityMask = SetThreadAffinityMask(hThread1, 1);
    SetThreadAffinityMask(hThread2, 1);

    QueryPerformanceFrequency(&frequency);
    double totalSwitchTime = 0.0;

    FILE* file = fopen("..\\..\\result_files\\thread_context_switch\\c_thread_context_switch_result.txt", "w");
    if (file == NULL) {
        printf("Failed to open the file for writing.\n");
        CloseHandle(hThread1);
        CloseHandle(hThread2);
        CloseHandle(event1);
        CloseHandle(event2);
        return 1;
    }


    for (int i = 0; i < numOfTests + 1; i++) {
        SetEvent(event1);

        QueryPerformanceCounter(&start);
        WaitForSingleObject(event2, INFINITE);
        QueryPerformanceCounter(&end);

        ResetEvent(event1);
        ResetEvent(event2);

        double switchTime = ((double)(end.QuadPart - start.QuadPart) * 1e6) / frequency.QuadPart;
        if(i != 0 ){
            totalSwitchTime += switchTime;
            fprintf(file, "%d %.3f\n", i, switchTime);
        }
    }

    double averageSwitchTime = totalSwitchTime / numOfTests;
    fprintf(file, "%.3f\n", averageSwitchTime);

    fclose(file);

    SetThreadAffinityMask(hThread1, originalAffinityMask);
    TerminateThread(hThread1, 0);
    TerminateThread(hThread2, 0);
    CloseHandle(hThread1);
    CloseHandle(hThread2);
    CloseHandle(event1);
    CloseHandle(event2);

    return 0;
}

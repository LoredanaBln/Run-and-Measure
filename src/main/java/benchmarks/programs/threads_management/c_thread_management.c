#include <windows.h>
#include <stdio.h>
#include <stdlib.h>

DWORD WINAPI threadFunction(LPVOID lpParam) {
    return 0;
}

int returnNumberOfTests(int argc, char* argv[]) {
    if (argc < 2) {
        printf("Not enough arguments\n");
        exit(1);
    }

    int numOfTests;
    if (sscanf(argv[2], "%d", &numOfTests) != 1) {
        printf("Invalid input: Expected an integer\n");
        exit(1);
    }
    return numOfTests;
}

int returnNumberOfThreads(int argc, char* argv[]) {
    if (argc < 3) {
        printf("Not enough arguments\n");
        exit(1);
    }

    int numOfThreads;
    if (sscanf(argv[1], "%d", &numOfThreads) != 1) {
        printf("Invalid input: Expected an integer\n");
        exit(1);
    }
    return numOfThreads;
}

int main(int argc, char* argv[]) {
    double totalElapsedTime = 0.0;
    int numOfTests = returnNumberOfTests(argc, argv);
    int numOfThreads = returnNumberOfThreads(argc, argv);

    LARGE_INTEGER frequency;
    QueryPerformanceFrequency(&frequency);

    FILE* file = fopen("..\\..\\result_files\\threads_management\\c_thread_management_result.txt", "w");
    if (file == NULL) {
        printf("Failed to open the file for writing.\n");
        return 1;
    } else {
        printf("File opened.\n");
    }

    for (int i = 0; i < numOfTests; i++) {
        LARGE_INTEGER start, end;

        QueryPerformanceCounter(&start);

        for (int j = 0; j < numOfThreads; j++) {
            HANDLE threadHandle = CreateThread(
                NULL,
                0,
                threadFunction,
                NULL,               //parameters for the thread
                0,
                NULL
            );

            if (threadHandle == NULL) {
                printf("Thread creation failed at index %d.\n", j);
                fclose(file);
                return 1;
            }

            WaitForSingleObject(threadHandle, INFINITE);
            CloseHandle(threadHandle);
        }

        QueryPerformanceCounter(&end);

        double elapsedTime = ((double)(end.QuadPart - start.QuadPart) * 1000000) / frequency.QuadPart;

        totalElapsedTime += elapsedTime;
        fprintf(file, "%d %.3f\n", i + 1, elapsedTime);
    }

    double averageTime = totalElapsedTime / numOfTests;
    fprintf(file, "%.3f", averageTime);

    fclose(file);

    return 0;
}

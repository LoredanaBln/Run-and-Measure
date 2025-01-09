    #include <windows.h>
    #include <stdio.h>
    #include <stdlib.h>

    DWORD WINAPI ThreadFunction(LPVOID lpParam) {
        while (1) {}
        return 0;
    }

    int getNumberOfTests(int argc, char* argv[]) {
        if (argc < 3) {
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

    int main(int argc, char* argv[]) {
        HANDLE hThread;
        DWORD_PTR originalAffinityMask, newAffinityMask;
        DWORD_PTR threadAffinityMask;
        SYSTEM_INFO systemInfo;
        LARGE_INTEGER start, end, frequency;

        GetSystemInfo(&systemInfo);
        int numProcessors = systemInfo.dwNumberOfProcessors;

        if (numProcessors < 2) {
            printf("This test requires at least 2 processors.\n");
            return 0;
        }

        hThread = CreateThread(NULL, 0, ThreadFunction, NULL, 0, NULL);
        if (hThread == NULL) {
            printf("Failed to create thread.\n");
            return 1;
        }

        originalAffinityMask = SetThreadAffinityMask(hThread, 1);
        if (originalAffinityMask == 0) {
            printf("Failed to set thread affinity.\n");
            CloseHandle(hThread);
            return 1;
        }

        QueryPerformanceFrequency(&frequency);

        int numOfTests = getNumberOfTests(argc, argv);
        double totalMigrationTime = 0.0;

        FILE* file = fopen("..\\..\\result_files\\thread_migration\\c_thread_migration_result.txt", "w");
        if (file == NULL) {
            printf("Failed to open the file for writing.\n");
            CloseHandle(hThread);
            return 1;
        }

        for (int i = 0; i < numOfTests; i++) {
            newAffinityMask = (1 << ((i + 1) % numProcessors));

            QueryPerformanceCounter(&start);
            threadAffinityMask = SetThreadAffinityMask(hThread, newAffinityMask);
            QueryPerformanceCounter(&end);

            if (threadAffinityMask == 0) {
                printf("Failed to migrate thread.\n");
                fclose(file);
                CloseHandle(hThread);
                return 1;
            }

            double migrationTime = ((double)(end.QuadPart - start.QuadPart) * 1e6) / frequency.QuadPart;
            totalMigrationTime += migrationTime;
            fprintf(file, "%d %.3f\n", i + 1, migrationTime);
        }

        double averageMigrationTime = totalMigrationTime / numOfTests;
        fprintf(file, "%.3f\n", averageMigrationTime);

        fclose(file);

        SetThreadAffinityMask(hThread, originalAffinityMask);
        TerminateThread(hThread, 0);
        CloseHandle(hThread);

        return 0;
    }



#include <windows.h>
#include <stdio.h>
#include <stdlib.h>

#define ARRAY_SIZE 1000

void initializeArray(int* arr, size_t size){
    for (size_t i = 0; i < size; i++) {
        arr[i] = i;
    }
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

int main(int argc, char* argv[]) {
    double totalElapsedTime = 0.0;

    LARGE_INTEGER frequency;
    QueryPerformanceFrequency(&frequency);

    FILE* file = fopen("..\\..\\result_files\\static_memory_access_writing\\c_static_memory_access_writing_result.txt", "w");
    if (file == NULL) {
        printf("Failed to open the file for writing.\n");
        return 1;
    }
    else {
        printf("File opened.\n");
    }

    int numOfTests = returnNumberOfTests(argc, argv);

    for (int i = 0; i < numOfTests; i++) {
        LARGE_INTEGER start, end;
        int stackArray[ARRAY_SIZE];
        QueryPerformanceCounter(&start);
        initializeArray(stackArray, ARRAY_SIZE);
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

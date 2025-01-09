#include <windows.h>
#include <stdio.h>
#include <stdlib.h>

void useArray(int* arr, size_t size) {
    for (size_t i = 0; i < size; i++) {
        arr[i] = i;
    }
}

int returnArraySize(int argc, char* argv[]) {
    if (argc < 3) {
        printf("Not enough arguments\n");
        exit(1);
    }
    int arraySize = sscanf(argv[1], "%d", &arraySize);
    return arraySize;
}

int returnNumberOfTests(int argc, char* argv[]) {
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

double measureAllocationTime(int arraySize, LARGE_INTEGER frequency) {
    LARGE_INTEGER start, end;
    QueryPerformanceCounter(&start);
    int* array = (int*)malloc(arraySize * sizeof(int));
    QueryPerformanceCounter(&end);

    double elapsedTime = ((double)(end.QuadPart  - start.QuadPart) * 1000000) / frequency.QuadPart;

    useArray(array, arraySize);
    free(array);

    return elapsedTime;
}

void writeResults(FILE* file, int run, double elapsedTime) {
    fprintf(file, "%d %.3f\n", run, elapsedTime);
}

FILE* openResultFile(const char* filename) {
    FILE* file = fopen(filename, "w");
    if (file == NULL) {
        printf("Failed to open the file for writing.\n");
        exit(1);
    }
    printf("File opened\n");
    return file;
}

void closeResultFile(FILE* file) {
    fclose(file);
}

int main(int argc, char* argv[]) {
    int arraySize = returnArraySize(argc, argv);
    double totalElapsedTime = 0.0;

    LARGE_INTEGER frequency;
    QueryPerformanceFrequency(&frequency);

    FILE* file = openResultFile("..\\..\\result_files\\dynamic_allocation\\c_dynamic_allocation_result.txt");
    int numOfTests = returnNumberOfTests(argc, argv);


    for (int i = 0; i < numOfTests; i++) {
        double elapsedTime = measureAllocationTime(arraySize, frequency);
        totalElapsedTime += elapsedTime;
        writeResults(file, i + 1, elapsedTime);
    }

    double averageTime = totalElapsedTime / numOfTests;
    fprintf(file, "%.3f\n", averageTime);
    closeResultFile(file);

    return 0;
}

package controller;

import java.io.File;
import java.util.*;

public enum ExecutablePaths {
    C_STATIC_ALLOCATION("c_static_allocation.exe", "static_allocation"),
    C_DYNAMIC_ALLOCATION("c_dynamic_allocation.exe", "dynamic_allocation"),
    C_STATIC_MEMORY_ACCESS_READING("c_static_memory_access_reading.exe", "static_memory_access_reading"),
    C_STATIC_MEMORY_ACCESS_WRITING("c_static_memory_access_writing.exe", "static_memory_access_writing"),
    C_DYNAMIC_MEMORY_ACCESS_READING("c_dynamic_memory_access_reading.exe", "dynamic_memory_access_reading"),
    C_DYNAMIC_MEMORY_ACCESS_WRITING("c_dynamic_memory_access_writing.exe", "dynamic_memory_access_writing"),
    C_THREADS_MANAGEMENT("c_thread_management.exe", "threads_management"),
    C_THREADS_MIGRATION("c_thread_migration.exe", "thread_migration"),
    C_CONTEXT_SWITCH("thread_context_switch.exe", "thread_context_switch"),

    JAVA_STATIC_ALLOCATION("StaticMemoryAllocation.java", "static_allocation"),
    JAVA_DYNAMIC_ALLOCATION("DynamicMemoryAllocation.java", "dynamic_allocation"),
    JAVA_STATIC_MEMORY_ACCESS_READING("StaticMemoryAccessReading.java", "static_memory_access_reading"),
    JAVA_STATIC_MEMORY_ACCESS_WRITING("StaticMemoryAccessWriting.java", "static_memory_access_writing"),
    JAVA_DYNAMIC_MEMORY_ACCESS_READING("DynamicMemoryAccessReading.java", "dynamic_memory_access_reading"),
    JAVA_DYNAMIC_MEMORY_ACCESS_WRITING("DynamicMemoryAccessWriting.java", "dynamic_memory_access_writing"),
    JAVA_THREADS_MANAGEMENT("ThreadManagement.java", "threads_management"),
    JAVA_THREADS_MIGRATION("ThreadMigration.java", "thread_migration"),
    JAVA_CONTEXT_SWITCH("ThreadContextSwitch.java", "thread_context_switch"),


    CS_STATIC_ALLOCATION("static_allocation.exe", "static_allocation\\static_allocation\\bin\\Debug\\net6.0"),
    CS_DYNAMIC_ALLOCATION("cs_dynamic_allocation.exe", "dynamic_allocation\\cs_dynamic_allocation\\cs_dynamic_allocation\\bin\\Debug"),
    CS_STATIC_MEMORY_ACCESS_READING("cs_static_memory_access_reading.exe", "static_memory_access_reading\\cs_static_memory_access_reading\\bin\\Debug\\net6.0"),
    CS_STATIC_MEMORY_ACCESS_WRITING("static_memory_access_writing.exe", "static_memory_access_writing\\static_memory_access_writing\\bin\\Debug\\net6.0"),
    CS_DYNAMIC_MEMORY_ACCESS_READING("cs_dynamic_memory_access_reading.exe", "dynamic_memory_access_reading\\cs_dynamic_memory_access_reading\\bin\\Debug\\net6.0"),
    CS_DYNAMIC_MEMORY_ACCESS_WRITING("cs_dynamic_memory_access_writing.exe", "dynamic_memory_access_writing\\cs_dynamic_memory_access_writing\\bin\\Debug\\net6.0"),
    CS_THREADS_MANAGEMENT("cs_thread_management.exe", "threads_management\\cs_thread_management\\bin\\Debug\\net6.0"),
    CS_THREADS_MIGRATION("thread_migration.exe", "thread_migration\\thread_migration\\bin\\Debug\\net6.0"),
    CS_CONTEXT_SWITCH("thread_context_switch.exe", "thread_context_switch\\thread_context_switch\\bin\\Debug\\net6.0"),

    NON_EXISTING_EXECUTABLE;

    private static final String BASE_PATH = "E:\\AN3\\ssc\\PROIECT\\run_and_measure\\src\\main\\java\\benchmarks\\programs";

    private final File workingDirectory;
    private final String executableCommand;

    ExecutablePaths(String command, String path) {
        this.executableCommand = generateExecutablePath(command, path);
        this.workingDirectory = new File(generateWorkingDirectory(path));
    }

    ExecutablePaths() {
        this.executableCommand = null;
        this.workingDirectory = null;
    }

    private String generateExecutablePath(String command, String path) {
        return BASE_PATH + "\\" + path + "\\" + command;
    }

    private String generateWorkingDirectory(String path) {
        return BASE_PATH + "\\" + path;
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    public String getExecutableCommand() {
        return executableCommand;
    }
    public static Map<Integer, List<ExecutablePaths>> getCorrespondentPath(String testName, Integer dataSize) {
        return switch (testName) {
            case "Static memory allocation" -> Map.of(dataSize, List.of(C_STATIC_ALLOCATION, CS_STATIC_ALLOCATION, JAVA_STATIC_ALLOCATION));
            case "Dynamic memory allocation" -> Map.of(dataSize, List.of(C_DYNAMIC_ALLOCATION, CS_DYNAMIC_ALLOCATION, JAVA_DYNAMIC_ALLOCATION));
            case "Static memory access reading" -> Map.of(dataSize, List.of(C_STATIC_MEMORY_ACCESS_READING, CS_STATIC_MEMORY_ACCESS_READING, JAVA_STATIC_MEMORY_ACCESS_READING));
            case "Static memory access writing" -> Map.of(dataSize, List.of(C_STATIC_MEMORY_ACCESS_WRITING, CS_STATIC_MEMORY_ACCESS_WRITING, JAVA_STATIC_MEMORY_ACCESS_WRITING));
            case "Dynamic memory access reading" -> Map.of(dataSize, List.of(C_DYNAMIC_MEMORY_ACCESS_READING, CS_DYNAMIC_MEMORY_ACCESS_READING, JAVA_DYNAMIC_MEMORY_ACCESS_READING));
            case "Dynamic memory access writing" -> Map.of(dataSize, List.of(C_DYNAMIC_MEMORY_ACCESS_WRITING, CS_DYNAMIC_MEMORY_ACCESS_WRITING, JAVA_DYNAMIC_MEMORY_ACCESS_WRITING));
            case "Threads management" -> Map.of(dataSize, List.of(C_THREADS_MANAGEMENT, CS_THREADS_MANAGEMENT, JAVA_THREADS_MANAGEMENT));
            case "Threads migration" -> Map.of(dataSize, List.of(C_THREADS_MIGRATION, CS_THREADS_MIGRATION, JAVA_THREADS_MIGRATION));
            case "Threads context-switch" -> Map.of(dataSize, List.of(C_CONTEXT_SWITCH, CS_CONTEXT_SWITCH, JAVA_CONTEXT_SWITCH));
            default -> Map.of(0, List.of(NON_EXISTING_EXECUTABLE));
        };
    }
}

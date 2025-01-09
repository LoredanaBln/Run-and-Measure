package controller;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public enum ResultFilesPath {

    STATIC_ALLOCATION_GROUP(new File("static_allocation\\c_static_allocation_result.txt"),
            new File("static_allocation\\cs_static_allocation_result.txt"),
            new File("static_allocation\\java_static_allocation_result.txt")
    ),
    DYNAMIC_ALLOCATION_GROUP(
            new File("dynamic_allocation\\c_dynamic_allocation_result.txt"),
            new File("dynamic_allocation\\cs_dynamic_allocation_result.txt"),
            new File("dynamic_allocation\\java_dynamic_allocation_result.txt")
    ),
    STATIC_MEMORY_ACCESS_READING_GROUP(new File("static_memory_access_reading\\c_static_memory_access_reading_result.txt"),
            new File("static_memory_access_reading\\cs_static_memory_access_reading_result.txt"),
            new File("static_memory_access_reading\\java_static_memory_access_result.txt")),

    STATIC_MEMORY_ACCESS_WRITING_GROUP(new File("static_memory_access_writing\\c_static_memory_access_writing_result.txt"),
            new File("static_memory_access_writing\\cs_static_memory_access_writing_result.txt"),
            new File("static_memory_access_writing\\java_static_memory_access_writing_result.txt")),

    DYNAMIC_MEMORY_ACCESS_READING_GROUP(new File("dynamic_memory_access_reading\\c_dynamic_memory_access_reading_result.txt"),
            new File("dynamic_memory_access_reading\\cs_dynamic_memory_access_reading_result.txt"),
            new File("dynamic_memory_access_reading\\java_dynamic_memory_access_reading_result.txt")),
    DYNAMIC_MEMORY_ACCESS_WRITING_GROUP(new File("dynamic_memory_access_writing\\c_dynamic_memory_access_writing_result.txt"),
            new File("dynamic_memory_access_writing\\cs_dynamic_memory_access_writing_result.txt"),
            new File("dynamic_memory_access_writing\\java_dynamic_memory_access_writing_result.txt")),
    THREAD_MANAGEMENT_GROUP(new File("threads_management\\c_thread_management_result.txt"),
            new File("threads_management\\cs_thread_management_result.txt"),
            new File("threads_management\\java_thread_management_result.txt")),
    THREAD_MIGRATION_GROUP(new File("thread_migration\\c_thread_migration_result.txt"),
            new File("thread_migration\\csharp_thread_migration_result.txt"),
            new File("thread_migration\\java_thread_migration_result.txt")),
    THREAD_CONTEXT_GROUP(new File("thread_context_switch\\c_thread_context_switch_result.txt"),
            new File("thread_context_switch\\csharp_thread_context_switch_result.txt"),
            new File("thread_context_switch\\java_thread_context_switch_result.txt"));
    private final File cFile, csFile, javaFile;
    private static final String BASE_PATH = "E:\\AN3\\ssc\\PROIECT\\run_and_measure\\src\\main\\java\\benchmarks\\result_files\\";

    ResultFilesPath(File c, File cs, File java) {
        this.cFile = new File(BASE_PATH + c);
        this.csFile = new File(BASE_PATH + cs);
        this.javaFile = new File(BASE_PATH + java);
    }

    private List<File> getFiles() {
        return Arrays.asList(cFile, csFile, javaFile);
    }

    public static List<File> getFilesByTestName(String name) {
        return switch (name) {
            case "Static memory allocation" -> STATIC_ALLOCATION_GROUP.getFiles();
            case "Dynamic memory allocation" -> DYNAMIC_ALLOCATION_GROUP.getFiles();
            case "Static memory access reading" -> STATIC_MEMORY_ACCESS_READING_GROUP.getFiles();
            case "Static memory access writing" -> STATIC_MEMORY_ACCESS_WRITING_GROUP.getFiles();
            case "Dynamic memory access writing" -> DYNAMIC_MEMORY_ACCESS_WRITING_GROUP.getFiles();
            case "Dynamic memory access reading" -> DYNAMIC_MEMORY_ACCESS_READING_GROUP.getFiles();
            case "Threads management" -> THREAD_MANAGEMENT_GROUP.getFiles();
            case "Threads migration" -> THREAD_MIGRATION_GROUP.getFiles();
            case "Threads context-switch" -> THREAD_CONTEXT_GROUP.getFiles();
            default -> null;
        };
    }
}

package controller;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import view.AppController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static controller.ExecutablePaths.JAVA_CONTEXT_SWITCH;
import static controller.ExecutablePaths.JAVA_THREADS_MIGRATION;

public class ProcessManager {
    private static TextArea logText;

    public static boolean runExecutables(List<Map<Integer, List<ExecutablePaths>>> executables) {
        for (Map<Integer, List<ExecutablePaths>> map : executables) {
            map.forEach((size, programs) -> {
                for (int i = 0; i < programs.size(); i++) {
                    ProcessBuilder processBuilder = getProcessBuilder(size, programs, i);
                    try {
                        Process process = processBuilder.start();
                        process.waitFor();
                        appendToLabel(programs.get(i) + " started successfully.\n");
                    } catch (IOException e) {
                        System.err.println("Failed to start process: " + e.getMessage());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Process was interrupted", e);
                    }
                }
            });
        }
        return true;
    }

    private static void appendToLabel(String message) {
        if (logText != null) {
            Platform.runLater(() -> logText.setText(logText.getText() + message));
        }
    }

    public static void setOutputLabel(TextArea label) {
        logText = label;
    }

    private static ProcessBuilder getProcessBuilder(Integer size, List<ExecutablePaths> programs, int i) {
        ExecutablePaths executablePath = programs.get(i);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(executablePath.getWorkingDirectory());
        System.out.println(executablePath.getWorkingDirectory() + " " + size);
        if (i == 2) {
            if (executablePath.getWorkingDirectory().equals(JAVA_THREADS_MIGRATION.getWorkingDirectory()) ||
                    executablePath.getWorkingDirectory().equals(JAVA_CONTEXT_SWITCH.getWorkingDirectory())) {
                String classpath = ".;" + executablePath.getWorkingDirectory() + "\\jna-3.0.9.jar";

                processBuilder.command(
                        "java",
                        "-cp", classpath,
                        executablePath.getExecutableCommand(),
                        String.valueOf(size),
                        AppController.getNumberOfTests()
                );
            }
            else {
                processBuilder.command("java", "-cp", executablePath.getWorkingDirectory().getPath(),
                        executablePath.getExecutableCommand(), String.valueOf(size), AppController.getNumberOfTests());
            }
        } else {
            processBuilder.command(executablePath.getExecutableCommand(), String.valueOf(size), AppController.getNumberOfTests());
        }
        return processBuilder;
    }
}

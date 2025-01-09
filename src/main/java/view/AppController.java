package view;

import controller.ChartCreator;
import controller.ExecutablePaths;
import controller.ProcessManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static controller.ExecutablePaths.getCorrespondentPath;
import static controller.ResultFilesPath.getFilesByTestName;
import static javafx.application.Platform.runLater;

public class AppController {

    @FXML
    private ScrollPane chartPanel;

    @FXML
    private CheckBox dynamicAccessReadingCheckbox;

    @FXML
    private CheckBox dynamicAccessWritingCheckbox;

    @FXML
    private CheckBox dynamicAllocationCheckbox;


    @FXML
    private CheckBox staticAccessReadingCheckbox;

    @FXML
    private CheckBox staticAccessWritingCheckbox;

    @FXML
    private CheckBox staticAllocationCheckbox;

    @FXML
    private CheckBox threadContextSwitchCheckbox;

    @FXML
    private CheckBox threadManagementCheckbox;

    @FXML
    private CheckBox threadMigrationCheckbox;

    @FXML
    private ComboBox<Integer> numberOfTestsComboBox = new ComboBox<>();

    @FXML
    private ComboBox<Integer> dynamicAccessReadArraySizeComboBox = new ComboBox<>();

    @FXML
    private ComboBox<Integer> dynamicAccessWriteArraySizeComboBox = new ComboBox<>();

    @FXML
    private ComboBox<Integer> dynamicAllocationArraySizeComboBox = new ComboBox<>();

    @FXML
    private ComboBox<Integer> threadsNumberManagementComboBox;

    @FXML
    private GridPane chartGridPane;
    boolean canGenerateCharts = false;

    @FXML
    private TextArea logTextArea;

    static List<CheckBox> checkBoxList;
    private static Integer numberOfTests;
    private final Popup invalidNumberOfTestsPopup = new Popup();

    @FXML
    public void initialize() {

        ObservableList<Integer> options = FXCollections.observableArrayList(100, 1000, 10000);
        ObservableList<Integer> testChoices = FXCollections.observableArrayList(10, 20, 50);
        ObservableList<Integer> threadOptions = FXCollections.observableArrayList(10, 20, 30);

        dynamicAllocationArraySizeComboBox.setItems(options);
        dynamicAccessWriteArraySizeComboBox.setItems(options);
        dynamicAccessReadArraySizeComboBox.setItems(options);
        threadsNumberManagementComboBox.setItems(threadOptions);

        numberOfTestsComboBox.setItems(testChoices);
        invalidNumberOfTestsPopup.hide();

        ProcessManager.setOutputLabel(logTextArea);
        logTextArea.setWrapText(true);
        chartPanel.setFitToWidth(true);
        checkBoxList = Arrays.asList(
                dynamicAccessReadingCheckbox,
                dynamicAccessWritingCheckbox,
                dynamicAllocationCheckbox,
                staticAccessReadingCheckbox,
                staticAccessWritingCheckbox,
                staticAllocationCheckbox,
                threadContextSwitchCheckbox,
                threadManagementCheckbox,
                threadMigrationCheckbox
        );
    }


    @FXML
    private void runPrograms() {
        runLater(() -> {
            numberOfTests = numberOfTestsComboBox.getValue();
            if (numberOfTests == null) {
                showPopupError("Invalid number of tests");
                return;
            }

            canGenerateCharts = ProcessManager.runExecutables(getSelectedTests());
            if (canGenerateCharts) {
                try {
                    showCharts();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public List<Map<Integer, List<ExecutablePaths>>> getSelectedTests() {
        List<Map<Integer, List<ExecutablePaths>>> selectedTests = new ArrayList<>();
        for (CheckBox checkBox : checkBoxList) {
            if (checkBox.isSelected()) {
                int dataSize = getDataSizeOfSelectedTest(checkBox.getText());
                selectedTests.add(getCorrespondentPath(checkBox.getText(), dataSize));
            }
        }
        return selectedTests;
    }

    public void showCharts() throws IOException {
        if (chartPanel != null) {
            chartGridPane = (GridPane) chartPanel.getContent();
            if (chartGridPane != null) {
                addCharts();
            } else {
                System.err.println("chartGridPane is null. Check the FXML file.");
            }
        } else {
            System.err.println("chartPanel is null. Check the FXML file.");
        }
        canGenerateCharts = false;
    }

    private List<Map<List<Double>, LineChart<Number, Number>>> getLineCharts() {
        List<Map<List<Double>, LineChart<Number, Number>>> scoresChartMap = new ArrayList<>();
        try {
            for (CheckBox checkBox : checkBoxList) {
                if (checkBox.isSelected()) {
                    List<File> files = getFilesByTestName(checkBox.getText());
                    ChartCreator chartCreator = new ChartCreator();
                    Map<List<Double>, LineChart<Number, Number>> chart = chartCreator.createLineChart(Objects.requireNonNull(files), checkBox.getText());
                    scoresChartMap.add(chart);
                }
            }
        } catch (IOException e) {
            System.err.println("Error creating line charts: " + e.getMessage());
        }

        return scoresChartMap;
    }

    public void addCharts() {
        List<Map<List<Double>, LineChart<Number, Number>>> scoresChartMap = new ArrayList<>(getLineCharts());
        int column = 0;
        int row = 1;
        try {
            for (Map<List<Double>, LineChart<Number, Number>> chartMap : scoresChartMap) {
                for (Map.Entry<List<Double>, LineChart<Number, Number>> chartEntry : chartMap.entrySet()) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/view/chartCard.fxml"));
                    AnchorPane cardBox = fxmlLoader.load();
                    ChartCard chartCard = fxmlLoader.getController();
                    chartCard.setChartData(chartEntry.getValue(), chartEntry.getKey());

                    if (column == 1) {
                        column = 0;
                        ++row;
                    }
                    chartGridPane.add(cardBox, column++, row);
                    GridPane.setMargin(cardBox, new Insets(25, 25, 25, 25));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int getDataSizeOfSelectedTest(String checkBoxName) {
        ComboBox<Integer> comboBox = switch (checkBoxName) {
            case "Dynamic memory access writing" -> dynamicAccessWriteArraySizeComboBox;
            case "Dynamic memory access reading" -> dynamicAccessReadArraySizeComboBox;
            case "Dynamic memory allocation" -> dynamicAllocationArraySizeComboBox;
            case "Threads management" -> threadsNumberManagementComboBox;
            default -> null;
        };

        return getSelectedItemOrDefault(comboBox, checkBoxName);
    }

    private int getSelectedItemOrDefault(ComboBox<Integer> comboBox, String checkBoxName) {
        if (comboBox != null && comboBox.getSelectionModel().getSelectedItem() != null) {
            return comboBox.getSelectionModel().getSelectedItem();
        } else {
            return 0;
        }
    }


    public static String getNumberOfTests() {
        return String.valueOf(numberOfTests);
    }

    private void showPopupError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

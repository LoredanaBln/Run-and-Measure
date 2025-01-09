package controller;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChartCreator {
    private static final NumberAxis xAxis = new NumberAxis();
    private static final NumberAxis yAxis = new NumberAxis();

    private final List<Double> scores = new ArrayList<>(3);

    public Map<List<Double>, LineChart<Number, Number>> createLineChart(List<File> files, String text) throws IOException {
        scores.clear();

        LineChart<Number, Number> lineChart = initializeChart(text);
        List<XYChart.Series<Number, Number>> seriesList = initializeSeries();

        loadDataForSeries(files.get(0), seriesList.get(0));  // C
        loadDataForSeries(files.get(1), seriesList.get(1)); // C#
        loadDataForSeries(files.get(2), seriesList.get(2)); // Java

        lineChart.getData().addAll(seriesList);

        return Map.of(scores, lineChart);
    }

    private static LineChart<Number, Number> initializeChart(String text) {
        xAxis.setLabel("Test Number");
        yAxis.setLabel("Time in " + "\u00B5" + "seconds");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(text);
        return lineChart;
    }

    private static List<XYChart.Series<Number, Number>> initializeSeries() {
        XYChart.Series<Number, Number> cSeries = new XYChart.Series<>();
        cSeries.setName("C");

        XYChart.Series<Number, Number> csSeries = new XYChart.Series<>();
        csSeries.setName("C#");

        XYChart.Series<Number, Number> javaSeries = new XYChart.Series<>();
        javaSeries.setName("Java");

        return List.of(cSeries, csSeries, javaSeries);
    }

    private void loadDataForSeries(File file, XYChart.Series<Number, Number> series) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String currentLine;
            String lastLine = null;
            while ((currentLine = reader.readLine()) != null) {
                XYChart.Data<Number, Number> dataPoint = parseLine(currentLine);
                if (dataPoint != null) {
                    dataPoint.setNode(null);
                    series.getData().add(dataPoint);
                }
                lastLine = currentLine;
            }

            if (lastLine != null) {
                double time = Double.parseDouble(lastLine);
                scores.add(time);
            }
        }
    }

    private static XYChart.Data<Number, Number> parseLine(String line) {
        String[] parts = line.split(" ");

        try {
            int testNumber = Integer.parseInt(parts[0].trim());
            double time = Double.parseDouble(parts[1].trim());
            return new XYChart.Data<>(testNumber, time);
        } catch (NumberFormatException e) {
            System.out.println("Skipping line with invalid numbers: " + line);
            return null;
        }
    }

}

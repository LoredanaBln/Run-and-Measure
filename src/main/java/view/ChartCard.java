package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ChartCard {

    @FXML
    private LineChart<Number, Number> lineChart;


    @FXML
    private Label cScore;

    @FXML
    private Label csScore;

    @FXML
    private Label javaScore;

    private final List<Timeline> animationTimelines = new ArrayList<>();

    public void setChartData(LineChart<Number, Number> chart, List<Double> scores) {
        if (chart != null) {
            this.lineChart.getData().clear();
            animationTimelines.forEach(Timeline::stop);
            animationTimelines.clear();

            lineChart.setTitle(chart.getTitle());
            lineChart.getXAxis().setLabel(chart.getXAxis().getLabel());
            lineChart.getYAxis().setLabel(chart.getYAxis().getLabel());

            for (XYChart.Series<Number, Number> series : chart.getData()) {
                Platform.runLater(() -> animateChartData(series));
            }
        } else {
            System.err.println("Chart is null. Unable to set data.");
        }

        updateScoreLabel(scores);
    }

    public void updateScoreLabel(List<Double> scores) {
        System.out.println(scores);
        cScore.setText(String.valueOf(scores.get(0)));
        csScore.setText(String.valueOf(scores.get(1)));
        javaScore.setText(String.valueOf(scores.get(2)));
    }

    private void animateChartData(XYChart.Series<Number, Number> series) {
        XYChart.Series<Number, Number> animatedSeries = new XYChart.Series<>();
        animatedSeries.setName(series.getName());
        lineChart.getData().add(animatedSeries);

        Timeline timeline = new Timeline();
        animationTimelines.add(timeline);

        for (int i = 0; i < series.getData().size(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(i * 0.2), event -> {
                if (index < series.getData().size()) {
                    animatedSeries.getData().add(series.getData().get(index));
                }
            });
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.play();
    }
}

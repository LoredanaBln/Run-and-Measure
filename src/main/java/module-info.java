module com.example.run_and_measure {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires jna;
    opens controller to javafx.fxml;
    exports controller;
    exports view;
    opens view to javafx.fxml;
    exports benchmarks.programs.static_allocation;
    opens benchmarks.programs.static_allocation to javafx.fxml;
    exports benchmarks.programs.dynamic_allocation;
    opens benchmarks.programs.dynamic_allocation to javafx.fxml;
}
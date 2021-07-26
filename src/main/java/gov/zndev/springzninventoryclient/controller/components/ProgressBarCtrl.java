package gov.zndev.springzninventoryclient.controller.components;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("progress_dialog_layout.fxml")
public class ProgressBarCtrl implements Initializable {

    @FXML
    private ProgressBar progress_bar;

    @FXML
    private Label progress_label;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public ProgressBar getProgress_bar() {
        return progress_bar;
    }

    public void setProgress_bar(ProgressBar progress_bar) {
        this.progress_bar = progress_bar;
    }

    public Label getProgress_label() {
        return progress_label;
    }

    public void setProgress_label(Label progress_label) {
        this.progress_label = progress_label;
    }
}

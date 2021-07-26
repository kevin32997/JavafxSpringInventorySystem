package gov.zndev.springzninventoryclient.controller.dashboard;

import javafx.fxml.Initializable;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("dashboard_charts_layout.fxml")
public class DashboardCtrl implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

package gov.zndev.springzninventoryclient.controller.components;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("table_action_buttons_layout.fxml")
public class TableActionButtonCtrl {
    @FXML
    private HBox hbox;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnSub;

    @FXML
    private Button btnRemove;


}

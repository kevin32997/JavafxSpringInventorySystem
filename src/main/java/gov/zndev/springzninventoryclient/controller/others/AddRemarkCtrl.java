package gov.zndev.springzninventoryclient.controller.others;

import gov.zndev.springzninventoryclient.controller.inventory.ViewInventoryCtrl;
import gov.zndev.springzninventoryclient.controller.locations.ViewLocationCtrl;
import gov.zndev.springzninventoryclient.helpers.AlertDialogHelper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Remark;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import gov.zndev.springzninventoryclient.repository.remarks.RemarksRepository;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("add_remark_layout.fxml")

public class AddRemarkCtrl {

    @FXML
    private TextArea description;

    @Autowired
    private RemarksRepository remarksRepo;

    private Stage stage;
    private Object ctrl;
    private String type;
    private int id;


    @FXML
    public void initialize() {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setTypeAndId(String type, int id) {
        this.type = type;
        this.id = id;
    }

    public void setCtrl(Object ctrl) {
        this.ctrl = ctrl;
    }

    private void saveRemark() {
        if (!description.getText().equals("")) {

            Remark remark = new Remark();
            remark.setDescription(description.getText());
            remark.setItemId(this.id);
            remark.setType(this.type);
            remark.setUserId(ResourceHelper.MAIN_USER.getId());

            remarksRepo.createRemark(remark, new RepoInterface() {
                @Override
                public void activityDone(Boolean success, String message, Object object) {
                    if(success){
                        Platform.runLater(()->{
                                AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.INFORMATION,"System Info","Remarks Added!.","Click ok to close.");
                                stage.close();

                                if(ctrl.getClass() == ViewInventoryCtrl.class){
                                    ((ViewInventoryCtrl)ctrl).refreshTables();
                                }
                        });
                    }
                }
            });

        } else {
            AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.ERROR, "System Info", "Description is empty!", "Add Description to proceed.");

        }
    }

    @FXML
    void onSave(ActionEvent event) {
        saveRemark();
    }

}

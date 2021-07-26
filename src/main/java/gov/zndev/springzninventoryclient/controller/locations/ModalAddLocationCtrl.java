package gov.zndev.springzninventoryclient.controller.locations;

import com.jfoenix.controls.JFXButton;
import gov.zndev.springzninventoryclient.controller.activity.ImportInventoryCtrl;
import gov.zndev.springzninventoryclient.controller.activity.SelectLocationCtrl;
import gov.zndev.springzninventoryclient.helpers.AlertDialogHelper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Location;
import gov.zndev.springzninventoryclient.repository.locations.LocationsRepository;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;


@Component
@FxmlView("location_modal_add_location_layout.fxml")
public class ModalAddLocationCtrl implements Initializable {

    @FXML
    private TextField location_name;

    @FXML
    private TextArea location_description;

    @FXML
    private JFXButton btn_save;

    private Stage stage;

    @Autowired
    private LocationsRepository locationRepo;

    private Object parentCtrl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupValidations();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setParentCtrl(Object parentCtrl){
        this.parentCtrl=parentCtrl;
    }

    private ValidationSupport validationSupport;

    private void setupValidations() {
        validationSupport = new ValidationSupport();

        validationSupport.registerValidator(location_name, true, Validator.createEmptyValidator("This is required"));
        validationSupport.registerValidator(location_description, true, Validator.createEmptyValidator("This is required"));
    }


    private void save() {
        System.out.println(validationSupport.isInvalid());

        if (!validationSupport.isInvalid()) {
            locationRepo.createLocation(new Location(location_name.getText(), location_description.getText(), ResourceHelper.MAIN_USER.getId()), (success, message, ob) -> {

                if (success) {
                    System.out.println("Location successfully saved");
                    List<Location> list=(List<Location>)ob;

                    Platform.runLater(() -> {
                        AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.INFORMATION, "System Message", "Location Created", null);
                        stage.close();

                        /*========================================================================
                            CODES / EVENTS for the parent Controller
                        =========================================================================*/

                        if(parentCtrl.getClass().equals(ImportInventoryCtrl.class)){
                            if(list.size()>0){
                                ((ImportInventoryCtrl)parentCtrl).setSelectedLocation(list.get(0));
                            }

                        }else if(parentCtrl.getClass().equals(SelectLocationCtrl.class)){
                            ((SelectLocationCtrl)parentCtrl).selectLocation(list.get(0));
                        }

                        //s=========================================================================
                    });

                } else {
                    System.out.println("Error while saving \nError: " + message);
                    Platform.runLater(() -> {
                        AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.ERROR, "System Message", "Error while saving", message);
                    });
                }
            });
        } else {
            System.out.println("There are missing fields");
            AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.ERROR, "System Message", "Missing fields", "Please fill up missing fields.");
        }
    }

    @FXML
    void onSave(ActionEvent event) {
        save();
    }


}

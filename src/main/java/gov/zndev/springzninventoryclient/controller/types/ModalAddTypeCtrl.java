package gov.zndev.springzninventoryclient.controller.types;

import com.jfoenix.controls.JFXButton;
import gov.zndev.springzninventoryclient.controller.activity.AddNewItemCtrl;
import gov.zndev.springzninventoryclient.controller.inventory.UpdateInventoryCtrl;
import gov.zndev.springzninventoryclient.controller.items.AddItemCtrl;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Brand;
import gov.zndev.springzninventoryclient.models.Type;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import gov.zndev.springzninventoryclient.repository.types.TypesRepository;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
@FxmlView("type_modal_add_type_layout.fxml")
public class ModalAddTypeCtrl implements Initializable {

    @FXML
    private TextField type_name;

    @FXML
    private TextArea type_description;

    @FXML
    private JFXButton btn_save;

    @Autowired
    private TypesRepository typesRepository;

    private Object parentCtrl;

    private Stage stage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupBtnEvents();
        setupValidations();
    }

    public void setName(String name) {
        this.type_name.setText(name);
    }

    public void setParentCtrl(Object ctrl) {
        this.parentCtrl = ctrl;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setupBtnEvents() {
        btn_save.setOnAction(e -> {
            save();
        });
    }

    private ValidationSupport validationSupport;

    private void setupValidations() {
        validationSupport = new ValidationSupport();
        validationSupport.registerValidator(type_name, true, Validator.createEmptyValidator("This is required"));
        validationSupport.registerValidator(type_description, true, Validator.createEmptyValidator("This is required"));
    }

    private void save() {
        System.out.println(validationSupport.isInvalid());

        if (!validationSupport.isInvalid()) {
            typesRepository.createType(new Type(type_name.getText(), type_description.getText()), new RepoInterface() {
                @Override
                public void activityDone(Boolean success, String message, Object ob) {
                    if (success) {
                        System.out.println("Type successfully saved");
                        if (ob != null) {
                            List<Type> typeList = (List<Type>) ob;
                            if (parentCtrl != null) {
                                if (parentCtrl.getClass().equals(AddItemCtrl.class)) {
                                    ((AddItemCtrl) parentCtrl).setSelectedType(typeList.get(0));
                                } else if (parentCtrl.getClass().equals(AddNewItemCtrl.class)) {
                                    ((AddNewItemCtrl) parentCtrl).setSelectedType(typeList.get(0));
                                }else if(parentCtrl.getClass().equals(UpdateInventoryCtrl.class)){
                                    ((UpdateInventoryCtrl) parentCtrl).setSelectedType(typeList.get(0));
                                }
                                Platform.runLater(() -> {
                                    stage.close();
                                });
                            }
                        }

                    } else {
                        System.out.println("Error while saving \nError: " + message);
                    }
                }
            });
        } else {
            System.out.println("There are missing fields");
        }


    }


}

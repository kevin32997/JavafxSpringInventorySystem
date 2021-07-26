package gov.zndev.springzninventoryclient.controller.brands;

import com.jfoenix.controls.JFXButton;
import gov.zndev.springzninventoryclient.controller.activity.AddNewItemCtrl;
import gov.zndev.springzninventoryclient.controller.inventory.UpdateInventoryCtrl;
import gov.zndev.springzninventoryclient.controller.items.AddItemCtrl;
import gov.zndev.springzninventoryclient.models.Brand;
import gov.zndev.springzninventoryclient.repository.brands.BrandsRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@FxmlView("brand_modal_add_brand_layout.fxml")
public class ModalAddBrandCtrl {

    @FXML
    private TextField brand_name;

    @FXML
    private TextArea brand_description;

    @FXML
    private JFXButton btn_save;

    private Object parentCtrl;

    private Stage stage;


    @Autowired
    private BrandsRepository brandsRepository;


    @FXML
    public void initialize() {
        setupBtnEvents();
        setupValidations();
    }

    public void setName(String name) {
        this.brand_name.setText(name);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setParentCtrl(Object ctrl) {
        this.parentCtrl = ctrl;
    }


    private void setupBtnEvents() {
        btn_save.setOnAction(e -> {
            save();
        });
    }

    private ValidationSupport validationSupport;

    private void setupValidations() {
        validationSupport = new ValidationSupport();
        validationSupport.registerValidator(brand_name, true, Validator.createEmptyValidator("This is required"));
        validationSupport.registerValidator(brand_description, true, Validator.createEmptyValidator("This is required"));
    }

    private void save() {
        System.out.println(validationSupport.isInvalid());

        if (!validationSupport.isInvalid()) {
            brandsRepository.createBrand(new Brand(brand_name.getText(), brand_description.getText()), new RepoInterface() {
                @Override
                public void activityDone(Boolean success, String message, Object ob) {
                    if (success) {
                        System.out.println("Brand successfully saved");
                        if (ob != null) {

                            List<Brand> brandList = (List<Brand>) ob;
                            if (parentCtrl != null) {
                                if (parentCtrl.getClass().equals(AddItemCtrl.class)) {
                                    ((AddItemCtrl) parentCtrl).setSelectedBrand(brandList.get(0));
                                }else if(parentCtrl.getClass().equals(AddNewItemCtrl.class)){
                                    ((AddNewItemCtrl) parentCtrl).setSelectedBrand(brandList.get(0));
                                }else if(parentCtrl.getClass().equals(UpdateInventoryCtrl.class)){
                                    ((UpdateInventoryCtrl) parentCtrl).setSelectedBrand(brandList.get(0));
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

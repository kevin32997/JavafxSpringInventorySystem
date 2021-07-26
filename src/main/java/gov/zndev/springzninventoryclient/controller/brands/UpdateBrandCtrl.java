package gov.zndev.springzninventoryclient.controller.brands;

import gov.zndev.springzninventoryclient.helpers.AlertDialogHelper;
import gov.zndev.springzninventoryclient.models.Brand;
import gov.zndev.springzninventoryclient.repository.brands.BrandsRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@FxmlView("brand_update_brand_layout.fxml")
public class UpdateBrandCtrl {

    @FXML
    private TextField brand_name;

    @FXML
    private TextArea brand_description;


    @Autowired
    private FxWeaver fxWeaver;

    @Autowired
    private BrandsRepository brandsRepo;

    private Brand brand;

    private Stage stage;

    private ViewBrandCtrl parentCtrl;

    @FXML
    public void initialize() {
        setupValidations();
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
        populateFields();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setParentCtrl(ViewBrandCtrl parentCtrl) {
        this.parentCtrl = parentCtrl;
    }

    private ValidationSupport validationSupport;

    private void setupValidations() {
        validationSupport = new ValidationSupport();
        validationSupport.registerValidator(brand_name, true, Validator.createEmptyValidator("This is required"));
    }

    private void populateFields() {
        this.brand_name.setText(brand.getName());
        this.brand_description.setText(brand.getDescription());
    }

    private void update() {
        if (!validationSupport.isInvalid()) {
            brand.setName(brand_name.getText());
            brand.setDescription(brand_description.getText());
            brandsRepo.updateBrand(brand.getId(), brand, new RepoInterface() {
                @Override
                public void activityDone(Boolean success, String message, Object object) {
                    if (success) {
                        List<Brand> list = (List<Brand>) object;
                        if (!list.isEmpty()) {
                            Platform.runLater(() -> {
                                AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.INFORMATION, "System Message", "Brand Updated!", null);
                                stage.close();
                                parentCtrl.setBrand(list.get(0));
                            });
                        }
                    }
                }
            });
        } else {
            AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.ERROR, "System Message", "Name is required!.", "Please add name to proceed.");
        }
    }

    @FXML
    void onUpdate(ActionEvent event) {
        update();
    }


}

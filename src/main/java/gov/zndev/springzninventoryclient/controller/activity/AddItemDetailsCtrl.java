package gov.zndev.springzninventoryclient.controller.activity;

import gov.zndev.springzninventoryclient.models.Item;
import gov.zndev.springzninventoryclient.models.others.ItemPick;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("activity_add_item_details_layout.fxml")
public class AddItemDetailsCtrl {
    @FXML
    private TextField name;

    @FXML
    private TextField brand;

    @FXML
    private TextField type;

    @FXML
    private TextField code;

    @FXML
    private TextField serial_number;

    @FXML
    private TextField property_number;

    ///////////////////////////////////////////////////

    private Item item;

    private Stage stage;

    private ImportInventoryCtrl parentCtrl;

    @FXML
    private void initialize() {
        setupView();
    }

    private void setupView() {
        code.setOnAction(e -> {
            serial_number.requestFocus();
        });
        serial_number.setOnAction(e -> {
            property_number.requestFocus();
        });
        property_number.setOnAction(e -> {
            addToTable();
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setItem(Item item) {
        this.item = item;
        this.name.setText(item.getName());
        this.brand.setText(item.getBrandName());
        this.type.setText(item.getTypeName());
    }

    public void setParentCtrl(ImportInventoryCtrl ctrl) {
        this.parentCtrl = ctrl;
    }


    private void addToTable() {
        if (validate()) {
            ItemPick itemToAdd = new ItemPick();
            itemToAdd.setItemId(item.getId());
            itemToAdd.setName(item.getName());
            itemToAdd.setBrand(item.getBrandName());
            itemToAdd.setType(item.getTypeName());
            itemToAdd.setCode(code.getText());
            itemToAdd.setSerial(serial_number.getText());
            itemToAdd.setPropertyNumber(property_number.getText());
            parentCtrl.addItemToTable(itemToAdd);
            stage.close();
        }


    }

    private boolean validate() {
        return true;
    }

    @FXML
    void onAddToTable(ActionEvent event) {
        addToTable();
    }


}

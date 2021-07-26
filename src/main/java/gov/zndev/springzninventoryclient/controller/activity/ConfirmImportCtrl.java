package gov.zndev.springzninventoryclient.controller.activity;

import gov.zndev.springzninventoryclient.helpers.AlertDialogHelper;
import gov.zndev.springzninventoryclient.helpers.Logger;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Inventory;
import gov.zndev.springzninventoryclient.models.InventoryActivity;
import gov.zndev.springzninventoryclient.models.InventoryActivityReference;
import gov.zndev.springzninventoryclient.models.others.ItemPick;
import gov.zndev.springzninventoryclient.repository.inv_activity_ref.InvActivityRefRepository;
import gov.zndev.springzninventoryclient.repository.inventory.InventoryRepository;
import gov.zndev.springzninventoryclient.repository.inventory_activity.InventoryActivityRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("activity_confirm_import_layout.fxml")
public class ConfirmImportCtrl {
    private static final String TAG = "ConfirmImportCtrl";

    @FXML
    private TextField total_items;

    @FXML
    private TextField reference;

    @FXML
    private TextField user;

    @FXML
    private TextField date_time;


    @FXML
    private TextField location_name;


    /////////////////////////////////

    @Autowired
    private InvActivityRefRepository referenceRepo;

    @Autowired
    private InventoryActivityRepository activityRepo;

    @Autowired
    private InventoryRepository inventoryRepo;

    private InventoryActivityReference invReference;

    private List<ItemPick> itemPickList;
    private Stage stage;
    private ImportInventoryCtrl parentCtrl;

    @FXML
    public void initialize() {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setParentCtrl(ImportInventoryCtrl ctrl) {
        this.parentCtrl = ctrl;
    }

    public void setData(List<ItemPick> list, InventoryActivityReference reference) {
        this.itemPickList = list;
        this.invReference = reference;
        setFields();
    }

    private void setFields() {
        // Total Items

        total_items.setText("" + itemPickList.size());

        // Location
        location_name.setText(invReference.getLocation().getName());

        // Reference
        reference.setText(invReference.getReference());

        // User
        user.setText(ResourceHelper.MAIN_USER.getFullname());

        // Date Time

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM dd, yyyy | hh:mm:ss aa");
        date_time.setText(dateFormat.format(new Date()));

    }

    private void proceedImport() {
        // Save Inventory Activity Reference
        referenceRepo.createInvActivityRef(invReference, (success, message, object) -> {
            if (success) {
                InventoryActivityReference savedReference = ((List<InventoryActivityReference>) object).get(0);

                // Add Inventory and Inventory activity
                for (ItemPick item : itemPickList) {

                    ////////////////////////////////////////////////////////////////////////////////////////////////////
                    // Inventory Activity
                    /*
                    InventoryActivity activity = new InventoryActivity();
                    activity.setItemId(item.getItemId());
                    activity.setLocationId(invReference.getLocationId());
                    activity.setOtherLocationId(0);
                    activity.setAction(InventoryActivity.ACTION_IMPORT);
                    activity.setTotal(item.getQuantity());
                    activity.setReferenceId(savedReference.getId());
                    // Save Activity to database
                    activityRepo.createInventoryActivity(activity, (success12, message12, object12) -> {
                        if (success12) {

                            // Create new Item Inventory
                            Inventory inventory = new Inventory();
                            inventory.setItemId(item.getItemId());
                            inventory.setCode(item.getCode());
                            inventory.setLocationId(invReference.getLocationId());
                            inventory.setPropertyNumber(item.getPropertyNumber());
                            inventory.setSerialNumber(item.getSerial());
                            inventoryRepo.createItemInventory(inventory, (success1, message1, object1) -> {
                                if (success1) {
                                    // Inventory Created
                                    // Show Inventory Import Successful
                                    Platform.runLater(() -> {
                                        showSuccessDialog();
                                    });

                                }
                            });
                        } else {
                            Logger.print(TAG, "proceedImport()", "Error Occurred while Creating Inventory Activity \n" + message12, null);
                            // Show Retry Dialog

                        }
                    });

                     */

                    ////////////////////////////////////////////////////////////////////////////////////////////////////


                    Inventory inventory = new Inventory();
                    inventory.setItemId(item.getItemId());
                    inventory.setCode(item.getCode());
                    inventory.setLocationId(invReference.getLocationId());
                    inventory.setPropertyNumber(item.getPropertyNumber());
                    inventory.setSerialNumber(item.getSerial());
                    inventoryRepo.createItemInventory(inventory, (success1, message1, object1) -> {
                        if (success1) {
                            List<Inventory> inventories= (List<Inventory>) object1;
                            Inventory inv=inventories.get(0);

                            // Inventory Created
                            InventoryActivity activity = new InventoryActivity();
                            activity.setItemId(item.getItemId());
                            activity.setInventoryId(inv.getId());
                            activity.setLocationId(invReference.getLocationId());
                            activity.setOtherLocationId(0);
                            activity.setAction(InventoryActivity.ACTION_IMPORT);
                            activity.setReferenceId(savedReference.getId());
                            // Save Activity to database
                            activityRepo.createInventoryActivity(activity, (success12, message12, object12) -> {

                                // Inventory Activity Created


                            });

                        }
                    });
                }

                Platform.runLater(()->{
                    showSuccessDialog();
                });
            } else {
                Logger.print(TAG, "proceedImport()", "Error Occurred while Creating Inventory Activity Reference\n" + message, null);
                // Show Retry Dialog

            }
        });

    }

    private void checkSuccess(){

    }


    private void showSuccessDialog() {
        AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.INFORMATION, "System Message", "Import Successful", "Inventory was successfully updated.");
        // Close Form
        this.stage.close();

        // Reset Import Table
        this.parentCtrl.reset();
    }


    @FXML
    void onConfirm(ActionEvent event) {
        proceedImport();
    }

    @FXML
    void onCancel(ActionEvent event) {
        stage.close();
    }

}

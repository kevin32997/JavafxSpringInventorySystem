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


import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ConfirmExportCtrl implements Initializable {

    private static final String TAG = "ConfirmExportCtrl";

    @FXML
    private TextField total_items;

    @FXML
    private TextField location;

    @FXML
    private TextField reference;

    @FXML
    private TextField user;

    @FXML
    private TextField date_time;

    /////////////////////////////////

    private InvActivityRefRepository referenceRepo;
    private InventoryActivityRepository activityRepo;
    private InventoryRepository inventoryRepo;

    private InventoryActivityReference invReference;
    private List<ItemPick> itemPickList;

    private Stage stage;
    private ExportInventoryCtrl parentCtrl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        referenceRepo = new InvActivityRefRepository();
        activityRepo = new InventoryActivityRepository();
        inventoryRepo = new InventoryRepository();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setParentCtrl(ExportInventoryCtrl ctrl) {
        this.parentCtrl = ctrl;
    }

    public void setData(List<ItemPick> list, InventoryActivityReference reference) {
        this.itemPickList = list;
        this.invReference = reference;
        setFields();
    }


    private void setFields() {
        // Total Items
        int totalItems = 0;
        for (ItemPick item : itemPickList) {
            totalItems += item.getQuantity();
        }
        total_items.setText("" + totalItems);

        // Location
        location.setText(invReference.getLocation().getName());

        // Reference
        reference.setText(invReference.getReference());

        // User
        user.setText(ResourceHelper.MAIN_USER.getFullname());

        // Date Time

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM dd, yyyy | hh:mm:ss aa");
        date_time.setText(dateFormat.format(new Date()));

    }

    boolean originInventoryUpdated = false;
    boolean exportInventoryUpdated = false;

    private void proceedExport() {
        // Save Inventory Activity Reference
        referenceRepo.createInvActivityRef(invReference, (success, message, object) -> {
            if (success) {
                InventoryActivityReference savedReference = ((List<InventoryActivityReference>) object).get(0);

                // Add Inventory and Inventory activity
                for (ItemPick item : itemPickList) {
                    // Inventory Activity
                    InventoryActivity activity = new InventoryActivity();
                    activity.setItemId(item.getItemId());
                    activity.setLocationId(invReference.getLocationId());
                    activity.setOtherLocationId(invReference.getLocationFromId());
                    activity.setAction(InventoryActivity.ACTION_EXPORT);
                    activity.setTotal(item.getQuantity());
                    activity.setReferenceId(savedReference.getId());
                    // Save Activity to database


                    activityRepo.createInventoryActivity(activity, new RepoInterface() {
                        @Override
                        public void activityDone(Boolean success, String message, Object object) {
                            if (success) {

                                // Update Item Inventory of From location
                                inventoryRepo.getInventoryByItemAndLocation(item.getItemId(), invReference.getLocationId(), new RepoInterface() {
                                    @Override
                                    public void activityDone(Boolean success, String message, Object object) {
                                        if (success) {
                                            List<Inventory> inventories = (List<Inventory>) object;
                                            if (inventories.size() != 0) {
                                                // Update Item Inventory
                                                /*
                                                for (Inventory inventory : inventories) {
                                                    inventory.setQuantity(inventory.getQuantity() - item.getQuantity());

                                                    if (inventory.getQuantity() != 0) {
                                                        inventoryRepo.updateItemInventory(inventory, new RepoInterface() {
                                                            @Override
                                                            public void activityDone(Boolean success, String message, Object object) {
                                                                if (success) {
                                                                    // Inventory Updated
                                                                    // Show Inventory Import Successful
                                                                    originInventoryUpdated = true;
                                                                    update();

                                                                } else {
                                                                    Logger.print(TAG, "proceedExport()", "Error Occurred while updating Inventory\n" + message, null);
                                                                    // Show Retry Dialog
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        // Delete Item Inventory

                                                        inventoryRepo.deleteInventory(inventory.getId(), new RepoInterface() {
                                                            @Override
                                                            public void activityDone(Boolean success, String message, Object object) {
                                                                if (success) {
                                                                    originInventoryUpdated = true;
                                                                    update();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }

                                                 */
                                            }
                                        }
                                    }
                                });

                                // Export Inventory to new Location

                                // Check first if inventory already Exist
                                inventoryRepo.getInventoryByItemAndLocation(item.getItemId(), invReference.getLocationFromId(), new RepoInterface() {
                                    @Override
                                    public void activityDone(Boolean success, String message, Object object) {
                                        if (success) {
                                            List<Inventory> inventories = (List<Inventory>) object;
                                            if (inventories.size() != 0) {
                                                // Update the inventory
                                                Inventory inventory = inventories.get(0);
                                                /*
                                                inventory.setQuantity(inventory.getQuantity() + item.getQuantity());
                                                inventoryRepo.updateItemInventory(inventory, new RepoInterface() {
                                                    @Override
                                                    public void activityDone(Boolean success, String message, Object object) {
                                                        if (success) {
                                                            exportInventoryUpdated = true;
                                                            update();
                                                        }
                                                    }
                                                });

                                                 */

                                            } else {
                                                // Create new Inventory
                                                // Create new Item Inventory
                                                Inventory inventory = new Inventory();
                                                inventory.setItemId(item.getItemId());
                                               // inventory.setQuantity(item.getQuantity());
                                                inventory.setLocationId(invReference.getLocationFromId());
                                                inventoryRepo.createItemInventory(inventory, (success1, message1, object1) -> {
                                                    if (success1) {
                                                        // Inventory Created
                                                        // Show Inventory Import Successful
                                                        exportInventoryUpdated = true;
                                                        update();

                                                        System.out.println();
                                                    } else {
                                                        Logger.print(TAG, "proceedExport()", "Error Occurred while Creating Inventory \n" + message1, null);
                                                        // Show Retry Dialog
                                                    }
                                                });

                                            }
                                        }
                                    }
                                });
                            } else {

                                Logger.print(TAG, "proceedExport()", "Error Occurred while Creating Inventory Activity \n" + message, null);
                                // Show Retry Dialog
                            }
                        }
                    });
                }
            } else {
                Logger.print(TAG, "proceedExport()", "Error Occurred while Creating Inventory Activity Reference\n" + message, null);
                // Show Retry Dialog
            }
        });
    }

    private void update() {
        if (originInventoryUpdated && exportInventoryUpdated) {
            Platform.runLater(() -> {
                showSuccessDialog();
            });
        }
    }


    private void showSuccessDialog() {
        AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.INFORMATION, "System Message", "Export Successful", "Inventory was successfully updated.");
        // Close Form
        this.stage.close();

        // Reset Import Table
        this.parentCtrl.reset();
    }


    @FXML
    void onCancel(ActionEvent event) {
        stage.close();
    }

    @FXML
    void onConfirm(ActionEvent event) {
        proceedExport();
    }


}

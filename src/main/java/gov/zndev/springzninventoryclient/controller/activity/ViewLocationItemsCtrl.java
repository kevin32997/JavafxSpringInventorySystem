package gov.zndev.springzninventoryclient.controller.activity;

import gov.zndev.springzninventoryclient.MainController;
import gov.zndev.springzninventoryclient.controller.items.ViewItemCtrl;
import gov.zndev.springzninventoryclient.helpers.AlertDialogHelper;
import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Inventory;
import gov.zndev.springzninventoryclient.models.Item;
import gov.zndev.springzninventoryclient.models.Location;
import gov.zndev.springzninventoryclient.repository.inventory.InventoryRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.List;


public class ViewLocationItemsCtrl implements Initializable {

    @FXML
    private TextField search_item;

    @FXML
    private ListView<AnchorPane> item_list;

    @FXML
    private Label location_name;

    @FXML
    private Label total_quantity;

    ///////////////////////////////////////////////////////

    private Location location;
    private ExportInventoryCtrl mainCtrl;

    private InventoryRepository inventoryRepo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inventoryRepo = new InventoryRepository();
    }

    public void setLocation(Location location) {
        this.location = location;

        location_name.setText(location.getName() + " ITEMS");

        loadLocationItems();
    }

    public void setCtrl(ExportInventoryCtrl ctrl) {
        this.mainCtrl = ctrl;
    }

    private void loadLocationItems() {

        System.out.println("Loading Location Items");
        inventoryRepo.getInventoryByTag(ResourceHelper.TAG_LOCATION, location.getId(), new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {

                    List<Inventory> list = (List<Inventory>) object;

                    System.out.println("View Location Inventory size is " + list.size());
                    if (list.size() > 0) {
                        try {

                            for (Inventory inventory : list) {
                                FXMLLoader loader = Helper.CreateLoader(ResourceHelper.LAYOUT_INVENTORY_VIEW_LOCATION_ITEMS_ROW, this.getClass());
                                AnchorPane pane = loader.load();
                                Label item_name = (Label) pane.lookup("#item_name");
                                Label quantity = (Label) pane.lookup("#quantity");

                                Button btnViewItem = (Button) pane.lookup("#btnViewItem");
                                Button btnExportItem = (Button) pane.lookup("#btnExport");

                                item_name.setText(inventory.getItem().getName());

                                //quantity.setText("" + inventory.getQuantity());

                                btnViewItem.setOnAction(e -> {
                                    viewItem(inventory.getItem());
                                });

                                btnExportItem.setOnAction(e -> {
                                    TextInputDialog dialog = new TextInputDialog("0");
                                    dialog.setTitle("Export Item");
                                    dialog.setHeaderText(inventory.getItem().getName());
                                    dialog.setContentText("Quantity:");

                                    Optional<String> result = dialog.showAndWait();
                                    if (result.isPresent()) {

                                        try {
                                            int exportQty = Integer.parseInt(result.get());
                                            mainCtrl.addItemToTable(inventory.getItem(), exportQty);
                                        } catch (NumberFormatException ex) {
                                            dialog.close();
                                            AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.ERROR, "Input Error", "Cannot use '" + result.get() + "' as multiplier", "Please input a number.");
                                        }
                                    }
                                });

                                item_list.getItems().add(pane);
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    System.out.println("Error Occurred While Loading Location Items \n" + message);

                }
            }
        });
    }

    private void viewItem(Item item) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(ResourceHelper.LAYOUT_ITEM_MODAL_VIEWITEM));
        try {
            Parent parent = loader.load();
            Scene scene = new Scene(parent, 1140, 700);

            Stage stage = Helper.CreateStage("VIEW ITEM - " + item.getName());
            stage.setResizable(false);

            stage.setScene(scene);

            ViewItemCtrl ctrl = loader.getController();
            ctrl.setItem(item.getId()
            );
            ctrl.setStage(stage);

            stage.initOwner(MainController.getStage());
            stage.initModality(Modality.NONE);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

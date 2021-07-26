package gov.zndev.springzninventoryclient.controller.activity;

import gov.zndev.springzninventoryclient.MainController;
import gov.zndev.springzninventoryclient.helpers.AlertDialogHelper;
import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.helpers.SearchResultListView;
import gov.zndev.springzninventoryclient.models.Inventory;
import gov.zndev.springzninventoryclient.models.InventoryActivityReference;
import gov.zndev.springzninventoryclient.models.Item;
import gov.zndev.springzninventoryclient.models.Location;
import gov.zndev.springzninventoryclient.models.others.ItemPick;
import gov.zndev.springzninventoryclient.repository.inventory.InventoryRepository;
import gov.zndev.springzninventoryclient.repository.items.ItemsRepository;
import gov.zndev.springzninventoryclient.repository.locations.LocationsRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
@FxmlView("activity_export_layout.fxml")
public class ExportInventoryCtrl implements Initializable {


    @FXML
    private AnchorPane main_pane;

    @FXML
    private TableView<ItemPick> main_table;

    @FXML
    private TableColumn<ItemPick, String> name_column;

    @FXML
    private TableColumn<ItemPick, String> serial_column;

    @FXML
    private TableColumn<ItemPick, String> model_column;

    @FXML
    private TableColumn<ItemPick, String> brand_column;

    @FXML
    private TableColumn<ItemPick, TextField> quantity_column;

    @FXML
    private TableColumn<ItemPick, AnchorPane> action_column;

    @FXML
    private TextField searchfield;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSubmit;

    @FXML
    private ComboBox<Location> select_export_location;

    @FXML
    private TextArea description;

    @FXML
    private TextField reference;

    @FXML
    private ComboBox<Location> select_from_location;

    @FXML
    private Button btnViewItems;

    ////////////////////////////////////////////////////////

    private Stage stage;
    private ItemsRepository itemsRepo;
    private LocationsRepository locationsRepo;
    private InventoryRepository inventoryRepo;


    /////////////////

    private Location selectedFromLocation;
    private Location selectedExportLocation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        itemsRepo = new ItemsRepository();
        locationsRepo = new LocationsRepository();
        inventoryRepo = new InventoryRepository();

        setup();

        setupTable();
        setupItemSearchEngine();
    }

    private void setup() {

        // Locations From
        locationsRepo.getAllLocation(new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {

                    select_from_location.getItems().addAll((List<Location>) object);
                } else {
                    // Error message
                }
            }
        });

        select_from_location.setConverter(new StringConverter<Location>() {
            @Override
            public String toString(Location location) {
                if (location != null)
                    return location.getName();
                return null;

            }

            @Override
            public Location fromString(String s) {
                return null;
            }
        });



        select_from_location.setOnAction(e -> {
            if(select_from_location.getSelectionModel().getSelectedItem()!=selectedFromLocation) {
                if (main_table.getItems().isEmpty()) {

                    selectedFromLocation = select_from_location.getSelectionModel().getSelectedItem();
                    setupExportLocationAndFields();
                } else {
                    Alert alert = AlertDialogHelper.ShowConfirmationAlertDialog("System Warning", "Table is not empty!", "Selecting new location will reset the table, continue?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        selectedFromLocation = select_from_location.getSelectionModel().getSelectedItem();
                        setupExportLocationAndFields();
                    } else {
                        select_from_location.getSelectionModel().select(selectedFromLocation);
                    }
                }
            }
        });


    }

    private void setupExportLocationAndFields() {
        // Clear Table
        main_table.getItems().clear();

        // Clear Export location
        select_export_location.getItems().clear();

        locationsRepo.getAllLocation(new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {
                    for (Location location : (List<Location>) object) {
                        if (location.getId() != selectedFromLocation.getId()) {
                            select_export_location.getItems().add(location);
                        }
                    }
                } else {
                    // Error message
                }
            }
        });

        select_export_location.setConverter(new StringConverter<Location>() {
            @Override
            public String toString(Location location) {
                if (location != null)
                    return location.getName();
                return null;
            }

            @Override
            public Location fromString(String s) {
                return null;
            }
        });

        select_export_location.setOnAction(e -> {
            selectedExportLocation = select_export_location.getSelectionModel().getSelectedItem();
        });


        // Search Field Prompt text
        searchfield.setPromptText("Search item to export . . .");
        fieldDisable(false);
    }

    private void fieldDisable(boolean disable) {
        select_export_location.setDisable(disable);
        searchfield.setDisable(disable);
        reference.setDisable(disable);
        description.setDisable(disable);
        btnViewItems.setDisable(disable);
        btnSubmit.setDisable(disable);
    }


    private void setupTable() {
        this.name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.serial_column.setCellValueFactory(new PropertyValueFactory<>("serial"));
        this.model_column.setCellValueFactory(new PropertyValueFactory<>("model"));
        this.brand_column.setCellValueFactory(new PropertyValueFactory<>("brand"));
        this.quantity_column.setCellValueFactory(param -> {
                    ItemPick itemPick = param.getValue();
                    TextField textField = new TextField();
                    textField.setText("" + itemPick.getQuantity());
                    itemPick.setQtyTextfield(textField);

                    textField.setOnKeyReleased(e -> {
                        if (!textField.getText().equals("")) {
                            try {
                                int input_qty = Integer.parseInt(textField.getText());
                                itemPick.setQuantity(input_qty);
                            } catch (NumberFormatException ex) {
                                // Error here
                                textField.setText("" + itemPick.getQuantity());
                            }
                        }
                    });

                    textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                            if (!t1) {
                                if (textField.getText().equals("")) {
                                    textField.setText("" + itemPick.getQuantity());
                                }
                            }
                        }
                    });

                    return new SimpleObjectProperty<>(textField);
                }
        );

        this.action_column.setCellValueFactory(param -> {
            AnchorPane pane = null;
            try {
                ItemPick item = param.getValue();

                FXMLLoader loader = Helper.LoadLayoutResource(ResourceHelper.LAYOUT_COMPONENT_TABLEACTIONBUTTONS);
                pane = loader.load();

                HBox hbox = (HBox) pane.lookup("#hbox");
                Button btnAdd = (Button) hbox.lookup("#btnAdd");

                Button btnSub = (Button) hbox.lookup("#btnSub");
                Button btnRemove = (Button) hbox.lookup("#btnRemove");

                btnAdd.setOnAction(e -> {
                    inventoryRepo.getInventoryTotalItemCountByItemAndLocation(item.getItemId(), selectedFromLocation.getId(), new RepoInterface() {
                        @Override
                        public void activityDone(Boolean success, String message, Object object) {
                            if (success) {
                                int count = (int) object;
                                if (item.getQuantity() + 1 > count) {
                                    Platform.runLater(() -> {
                                        AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.WARNING, "System Error", "Export Quantity Exceeded!", "Item max quantity is '" + count + "'");
                                    });
                                } else {
                                    item.setQuantity(item.getQuantity() + 1);
                                    item.getQtyTextfield().setText("" + item.getQuantity());
                                }
                            }
                        }
                    });


                });

                btnSub.setOnAction(e -> {
                    if (item.getQuantity() > 0) {
                        item.setQuantity(item.getQuantity() - 1);
                        item.getQtyTextfield().setText("" + item.getQuantity());
                    } else {
                        // remove item
                        main_table.getItems().remove(item);
                    }
                });

                btnRemove.setOnAction(e -> {
                    main_table.getItems().remove(item);
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
            return new SimpleObjectProperty<>(pane);
        });
    }

    private void setupItemSearchEngine() {
        SearchResultListView item_searchResult = new SearchResultListView(searchfield, searchfield.getWidth(), 27);
        searchfield.widthProperty().addListener((obs, oldVal, newVal) -> {
            item_searchResult.setPrefWidth(newVal.doubleValue());
        });


        searchfield.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() != KeyCode.ENTER) {


                if (keyEvent.getCode() == KeyCode.DOWN) {
                    item_searchResult.requestFocus();
                    item_searchResult.getSelectionModel().select(0);
                    return;
                }

                if (!searchfield.getText().equals("")) {

                    inventoryRepo.searchInventoryItemByLocation(searchfield.getText(), selectedFromLocation.getId(), 10, new RepoInterface() {
                        @Override
                        public void activityDone(Boolean success, String message, Object object) {
                            if (success) {
                                List<Inventory> inventories = (List<Inventory>) object;
                                item_searchResult.setList(inventories);
                                if (inventories.size() > 0) {
                                    List<String> stringList = new ArrayList<>();

                                    for (Inventory inventory : inventories) {
                                       // stringList.add(inventory.getItem().getName() + " (" + inventory.getQuantity() + ")");
                                    }

                                    item_searchResult.displayResult(stringList);
                                    item_searchResult.setVisible(true);
                                } else {

                                    item_searchResult.getList().clear();
                                    item_searchResult.setVisible(false);
                                }

                            } else {
                                // Error code here
                                System.out.println("Error Occurred\n" + message);
                                item_searchResult.getList().clear();
                                item_searchResult.setVisible(false);
                            }
                        }
                    });


                } else {
                    item_searchResult.setVisible(false);
                    item_searchResult.setList(new ArrayList<>());
                }
            }
        });

        searchfield.setOnAction(e -> {
            if (!searchfield.getText().equals("")) {
                if (item_searchResult.isVisible()) {

                    if (item_searchResult.getItems().size() > 0) {
                        Inventory inv = (Inventory) item_searchResult.getList().get(0);
                        searchfield.setText(inv.getItem().getName());
                        searchfield.end();
                        item_searchResult.setVisible(false);
                        item_searchResult.getItems().clear();

                    }
                } else {


                    int qty = 1;
                    String searchItem = "";

                    String[] splitter = searchfield.getText().split("\\*");
                    if (splitter.length > 1) {
                        searchItem = splitter[0];

                        try {
                            qty = Integer.parseInt(splitter[1]);
                        } catch (NumberFormatException ex) {
                            // Show Alert
                            AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.ERROR, "System Error", "Number Exception", "Cannot use '" + splitter[1] + "' as multiplier.");
                            return;
                        }
                    } else {
                        searchItem = searchfield.getText();
                    }


                    int finalQty = qty;
                    inventoryRepo.searchLocationItemByNameOrSerial(selectedFromLocation.getId(), searchItem, new RepoInterface() {
                        @Override
                        public void activityDone(Boolean success, String message, Object object) {
                            if (success) {
                                if (((List<Inventory>) object).size() > 0) {
                                    Inventory inventory = ((List<Inventory>) object).get(0);
                                    Item item = inventory.getItem();
                                    addItemToTable(item, finalQty);
                                    searchfield.clear();
                                } else {
                                    //Show No Item Available
                                }
                            }
                        }
                    });
                }
            }
        });

        item_searchResult.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                searchfield.setText(((Inventory) item_searchResult.getList().get(item_searchResult.getSelectionModel().getSelectedIndex())).getItem().getName());
                item_searchResult.setVisible(false);
                item_searchResult.getItems().clear();
                searchfield.requestFocus();
                searchfield.end();
            }
        });

        // Add search result to main anchor pane
        main_pane.getChildren().add(item_searchResult);
    }

    public void addItemToTable(Item item, int qty) {
        ItemPick pick = new ItemPick();
        pick.copyItem(item);
        pick.setQuantity(qty);

        boolean itemExist = false;
        for (ItemPick tableItem : main_table.getItems()) {
            if (tableItem.getItemId() == pick.getItemId()) {
                tableItem.setQuantity(tableItem.getQuantity() + qty);

                inventoryRepo.getInventoryTotalItemCountByItemAndLocation(tableItem.getItemId(), selectedFromLocation.getId(), new RepoInterface() {
                    @Override
                    public void activityDone(Boolean success, String message, Object object) {
                        if (success) {
                            int count = (int) object;

                            System.out.println("Table Item qty " + tableItem.getQuantity());
                            System.out.println("Actual count " + count);
                            if (tableItem.getQuantity() > count) {
                                tableItem.setQuantity(count);
                                Platform.runLater(() -> {
                                    main_table.refresh();
                                });
                            }
                        }
                    }
                });

                main_table.refresh();
                itemExist = true;
            }
        }

        if (!itemExist) {
            main_table.getItems().add(pick);
        }

    }

    public List<ItemPick> getTableItems() {
        return main_table.getItems();
    }

    public void reset() {
        this.main_table.getItems().clear();
        this.reference.clear();
        this.description.clear();

        selectedFromLocation=null;

        selectedExportLocation=null;
        select_export_location.getSelectionModel().clearSelection();

    }


    private void viewLocationItems() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(ResourceHelper.LAYOUT_INVENTORY_VIEW_LOCATION_ITEMS));
        try {
            Parent parent = loader.load();
            Scene scene = new Scene(parent, 710, 480);

            Stage stage = Helper.CreateStage("VIEW ITEMS - " + selectedFromLocation.getName());
            stage.setResizable(false);

            stage.setScene(scene);

            ViewLocationItemsCtrl ctrl = loader.getController();
            ctrl.setLocation(selectedFromLocation);
            ctrl.setCtrl(this);

            stage.initOwner(MainController.getStage());
            stage.initModality(Modality.NONE);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void validateFields() {
        if (main_table.getItems().isEmpty()) {
            AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.WARNING, "System Error", "Table is empty!", "Add item first to export.");
        } else if (select_export_location.getSelectionModel().isEmpty()) {
            AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.WARNING, "System Error", "Can't Proceed Activity, Export Location is Required!", "Please select Export Location first.");
            select_export_location.requestFocus();
        } else {
            submitExport();
        }
    }

    private void submitExport() {
        InventoryActivityReference reference = new InventoryActivityReference();
        reference.setUserId(ResourceHelper.MAIN_USER.getId());
        reference.setReference(this.reference.getText());
        reference.setConsignee("");
        reference.setLocationId(selectedFromLocation.getId());
        reference.setLocation(selectedFromLocation);
        reference.setLocationFromId(selectedExportLocation.getId());
        reference.setAction(InventoryActivityReference.ACTION_EXPORT);
        reference.setRemarks(description.getText());

        try {
            Stage stage = Helper.CreateStage("Confirm Export");
            FXMLLoader loader = Helper.CreateLoader(ResourceHelper.LAYOUT_INVENTORY_CONFIRM_EXPORT, this.getClass());
            AnchorPane pane = loader.load();
            ConfirmExportCtrl ctrl = loader.getController();
            ctrl.setData(main_table.getItems(), reference);
            ctrl.setStage(stage);
            ctrl.setParentCtrl(this);
            Scene scene = new Scene(pane, 360, 255);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(MainController.getStage());
            stage.setResizable(false);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onViewLocationItems(ActionEvent event) {
        viewLocationItems();
    }


    @FXML
    void onReset(ActionEvent event) {
        reset();
    }

    @FXML
    void onSubmit(ActionEvent event) {
        validateFields();
    }

}

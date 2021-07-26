package gov.zndev.springzninventoryclient.controller.activity;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import gov.zndev.springzninventoryclient.MainController;
import gov.zndev.springzninventoryclient.controller.components.TableActionButtonCtrl;
import gov.zndev.springzninventoryclient.controller.locations.ModalAddLocationCtrl;
import gov.zndev.springzninventoryclient.helpers.AlertDialogHelper;
import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.helpers.SearchResultListView;
import gov.zndev.springzninventoryclient.models.InventoryActivityReference;
import gov.zndev.springzninventoryclient.models.Item;
import gov.zndev.springzninventoryclient.models.Location;
import gov.zndev.springzninventoryclient.models.others.ItemPick;
import gov.zndev.springzninventoryclient.repository.items.ItemsRepository;
import gov.zndev.springzninventoryclient.repository.locations.LocationsRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
@FxmlView("activity_import_layout.fxml")
public class ImportInventoryCtrl implements Initializable {


    @FXML
    private AnchorPane main_pane;

    @FXML
    private TableView<ItemPick> main_table;

    @FXML
    private TableColumn<ItemPick, String> name_column;

    @FXML
    private TableColumn<ItemPick, String> brand_column;

    @FXML
    private TableColumn<ItemPick, String> type_column;

    @FXML
    private TableColumn<ItemPick, String> code_column;

    @FXML
    private TableColumn<ItemPick, String> serial_column;

    @FXML
    private TableColumn<ItemPick, String> property_column;

    @FXML
    private TableColumn<ItemPick, AnchorPane> action_column;

    @FXML
    private TextField searchfield;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSubmit;

    @FXML
    private TextField supplier;

    @FXML
    private ComboBox<Location> select_location;

    @FXML
    private TextArea description;

    @FXML
    private TextField reference;

    @Autowired
    private ItemsRepository itemsRepo;

    @Autowired
    private LocationsRepository locationsRepo;

    @Autowired
    private FxWeaver fxWeaver;


    private Location selectedLocation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        openLocationSelectionDialog();
        setupSelectLocation(null);
        setupTable();
        setupItemSearchEngine();
    }


    private void openLocationSelectionDialog(){
        FxControllerAndView<SelectLocationCtrl, AnchorPane> selectLocationDialog=fxWeaver.load(SelectLocationCtrl.class);

        /*
        Platform.runLater(()->{
            Stage stage=Helper.CreateStage("");
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene=new Scene(selectLocationDialog.getView().get(),705,505);
            stage.setScene(scene);

            selectLocationDialog.getController().setStage(stage);
            selectLocationDialog.getController().setParentCtrl(this);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        });
         */


        Platform.runLater(()->{
            SelectLocationCtrl ctrl=selectLocationDialog.getController();

            JFXDialog dialog=AlertDialogHelper.CreateJFXDialog(main_pane,new Text("Select Location"),selectLocationDialog.getView().get());
            dialog.setOverlayClose(false);


            ctrl.setParentCtrl(this);
            ctrl.setDialog(dialog);

            dialog.show();
        });


    }

    private void setupSelectLocation(Location location) {
        select_location.getItems().clear();

        // Locations
        locationsRepo.getAllLocation(new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {
                    Platform.runLater(()->{
                        select_location.getItems().addAll((List<Location>) object);

                        select_location.setConverter(new StringConverter<Location>() {
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

                        // Insert "Add new location" to selection
                        Location addLocation = new Location();
                        addLocation.setName("+ Add Location");
                        addLocation.setId(-1);

                        select_location.getItems().add(addLocation);
                        select_location.setOnAction(e -> {
                            if (select_location.getSelectionModel().getSelectedItem() != null) {
                                if (select_location.getSelectionModel().getSelectedItem().getId() == -1) {
                                    // Add new Location
                                    addNewLocation();
                                    select_location.getSelectionModel().clearSelection();
                                    selectedLocation=null;
                                } else {
                                    selectedLocation = select_location.getSelectionModel().getSelectedItem();
                                }
                            }
                        });

                        if(location!=null){
                            for(Location item:select_location.getItems()){
                                    if(item.getId()==location.getId()){
                                        select_location.getSelectionModel().select(item);
                                    }
                            }
                        }
                    });

                } else {
                    // Error message
                }
            }
        });



    }

    private void setupTable() {
        // Table
        main_table.setEditable(true);


        this.name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.brand_column.setCellValueFactory(new PropertyValueFactory<>("brand"));
        this.type_column.setCellValueFactory(new PropertyValueFactory<>("type"));


        this.code_column.setCellValueFactory(new PropertyValueFactory<>("code"));
        this.code_column.setCellFactory(TextFieldTableCell.forTableColumn());
        this.code_column.setOnEditCommit(event -> {
            String newValue = event.getNewValue();
            if (!newValue.equals("")) {
                ItemPick item = event.getTableView().getItems().get(event.getTablePosition().getRow());
                item.setCode(newValue);
            }
        });

        this.serial_column.setCellValueFactory(new PropertyValueFactory<>("serial"));
        this.serial_column.setCellFactory(TextFieldTableCell.forTableColumn());
        this.serial_column.setOnEditCommit(event ->

        {
            String newValue = event.getNewValue();
            if (!newValue.equals("")) {
                ItemPick item = event.getTableView().getItems().get(event.getTablePosition().getRow());
                item.setSerial(newValue);
            }
        });

        this.property_column.setCellValueFactory(new PropertyValueFactory<>("propertyNumber"));
        this.property_column.setCellFactory(TextFieldTableCell.forTableColumn());
        this.property_column.setOnEditCommit(event ->

        {
            String newValue = event.getNewValue();
            if (!newValue.equals("")) {
                ItemPick item = event.getTableView().getItems().get(event.getTablePosition().getRow());
                item.setPropertyNumber(newValue);
            }
        });

        /*
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
         */
        this.action_column.setCellValueFactory(param ->

        {

            ItemPick item = param.getValue();
            AnchorPane pane = fxWeaver.loadView(TableActionButtonCtrl.class);

            HBox hbox = (HBox) pane.lookup("#hbox");
            Button btnAdd = (Button) hbox.lookup("#btnAdd");
            Button btnSub = (Button) hbox.lookup("#btnSub");
            Button btnRemove = (Button) hbox.lookup("#btnRemove");

            btnAdd.setOnAction(e -> {
                item.setQuantity(item.getQuantity() + 1);
                item.getQtyTextfield().setText("" + item.getQuantity());
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
            return new SimpleObjectProperty<>(pane);
        });

    }

    private void setupItemSearchEngine() {
        SearchResultListView item_searchResult = new SearchResultListView(searchfield, searchfield.getWidth(), 27);
        searchfield.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.DOWN) {
                item_searchResult.requestFocus();
                item_searchResult.getSelectionModel().select(0);
                return;
            }

            if (!searchfield.getText().equals("")) {
                itemsRepo.searchItem(searchfield.getText(), 10, (success, message, object) -> {
                    if (success) {
                        List<Item> items = (List<Item>) object;
                        item_searchResult.setList(items);
                        if (items.size() > 0) {
                            List<String> stringList = new ArrayList<>();
                            for (Item item : items) {
                                stringList.add(item.getName());
                            }
                            item_searchResult.displayResult(stringList);
                        } else {
                            item_searchResult.displayNoDataFound("No data found . . .");
                            item_searchResult.getList().clear();
                        }

                        item_searchResult.setVisible(true);

                    } else {
                        // Error code here
                        System.out.println("Error Occurred\n" + message);
                        item_searchResult.getList().clear();
                        item_searchResult.setVisible(false);
                    }
                });
            } else {
                item_searchResult.setVisible(false);
                item_searchResult.setList(new ArrayList<>());
            }
        });

        searchfield.setOnAction(e -> {
            if (item_searchResult.getItems().size() > 0) {
                if (!item_searchResult.getItems().get(0).equals("No data found . . .")) {
                    addItemToTable((Item) item_searchResult.getList().get(item_searchResult.getSelectionModel().getSelectedIndex()));
                    item_searchResult.getList().clear();
                    item_searchResult.getItems().clear();
                    item_searchResult.setVisible(false);
                } else {
                    // Error message here no data found // Create new Brand
                    System.out.println("No data found, Cant use enter");
                    addNewItem(searchfield.getText());
                }
            } else {
                System.out.println("List is empty");
                //
            }
        });

        item_searchResult.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                addItemToTable((Item) item_searchResult.getList().get(item_searchResult.getSelectionModel().getSelectedIndex()));
                item_searchResult.getList().clear();
                item_searchResult.getItems().clear();
                item_searchResult.setVisible(false);
            }
        });

        searchfield.widthProperty().addListener((obs, oldVal, newVal) -> {
            item_searchResult.setPrefWidth(newVal.doubleValue());

        });

        // Add search result to main anchor pane
        main_pane.getChildren().add(item_searchResult);
    }

    private void validateFields() {
        if (selectedLocation == null) {

            AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.WARNING, "System Error", "No Location selected", "Please select location to proceed.");
            select_location.requestFocus();

        }else if(main_table.getItems().size()==0){
            AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.WARNING, "System Error", "No Items to Submit", "Please add items to proceed.");
        } else {
            submitImport();
        }
    }

    private void submitImport() {

        // Initialize Inventory Activity Reference
        InventoryActivityReference reference = new InventoryActivityReference();
        reference.setUserId(ResourceHelper.MAIN_USER.getId());
        reference.setReference(this.reference.getText());
        reference.setConsignee(supplier.getText());
        reference.setLocationId(selectedLocation.getId());
        reference.setLocation(selectedLocation);
        reference.setLocationFromId(0);
        reference.setAction(InventoryActivityReference.ACTION_IMPORT);
        reference.setRemarks(description.getText());

        // Get Confirm Import Modal

        FxControllerAndView<ConfirmImportCtrl, AnchorPane> confirmImportModal = fxWeaver.load(ConfirmImportCtrl.class);


        // Create and Initialize Stage
        Stage stage = Helper.CreateStage("Confirm Import");


        AnchorPane pane = confirmImportModal.getView().get();
        ConfirmImportCtrl ctrl = confirmImportModal.getController();
        ctrl.setData(main_table.getItems(), reference);
        ctrl.setStage(stage);
        ctrl.setParentCtrl(this);
        Scene scene = new Scene(pane, 360, 255);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(MainController.getStage());
        stage.setResizable(false);

        stage.show();


    }

    private void addItemToTable(Item item) {
        FxControllerAndView<AddItemDetailsCtrl, AnchorPane> addItemDetailsModal = fxWeaver.load(AddItemDetailsCtrl.class);
        addItemDetailsModal.getController().setParentCtrl(this);
        addItemDetailsModal.getController().setItem(item);


        Stage stage = Helper.CreateStage("Add Item Details");
        addItemDetailsModal.getController().setStage(stage);

        Scene scene = new Scene(addItemDetailsModal.getView().get(), 400, 360);
        stage.setScene(scene);

        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(MainController.getStage());

        stage.show();
    }

    public void addItemToTable(ItemPick item) {

        main_table.getItems().add(item);
        searchfield.clear();
        searchfield.requestFocus();
    }

    private void addNewItem(String name) {
        FxControllerAndView<AddNewItemCtrl, AnchorPane> addNewItemModal = fxWeaver.load(AddNewItemCtrl.class);
        addNewItemModal.getController().setParentCtrl(this);
        addNewItemModal.getController().setName(name);

        // Init Stage

        Stage stage = Helper.CreateStage("Create Item");

        addNewItemModal.getController().setStage(stage);

        Scene scene = new Scene(addNewItemModal.getView().get(), 725, 380);
        stage.setScene(scene);

        stage.setResizable(false);
        stage.initOwner(MainController.getStage());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    private void addNewLocation() {
        FxControllerAndView<ModalAddLocationCtrl, AnchorPane> addLocationModal = fxWeaver.load(ModalAddLocationCtrl.class);
        addLocationModal.getController().setParentCtrl(this);
        // Init Stage
        Stage stage = Helper.CreateStage("Create Location");
        addLocationModal.getController().setStage(stage);
        Scene scene = new Scene(addLocationModal.getView().get(), 445, 345);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initOwner(MainController.getStage());
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();
    }

    public void setSelectedLocation(Location location) {
        this.setupSelectLocation(location);
    }

    public void reset() {
        this.main_table.getItems().clear();
        this.supplier.clear();
        this.select_location.getSelectionModel().clearSelection();
        this.selectedLocation = null;
        this.reference.clear();
        this.description.clear();
    }


    public void notifyNewImportActivity(int invActRefId) {
    }

    @FXML
    void onReset(ActionEvent event) {
        Alert alert = AlertDialogHelper.ShowConfirmationAlertDialog("System Message", "Reset Table?", "This cannot be undone.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            reset();
        }
    }

    @FXML
    void onSubmit(ActionEvent event) {
        validateFields();
    }

    @FXML
    void onNewItem(ActionEvent event) {
        addNewItem("");
    }

    @FXML
    void showNotification(ActionEvent event) {
        notifyNewImportActivity(1);
    }

}

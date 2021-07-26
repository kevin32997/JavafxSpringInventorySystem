package gov.zndev.springzninventoryclient.controller.inventory;

import gov.zndev.springzninventoryclient.MainController;
import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Inventory;
import gov.zndev.springzninventoryclient.models.Item;
import gov.zndev.springzninventoryclient.models.Location;
import gov.zndev.springzninventoryclient.models.others.ItemPick;
import gov.zndev.springzninventoryclient.repository.brands.BrandsRepository;
import gov.zndev.springzninventoryclient.repository.inventory.InventoryRepository;
import gov.zndev.springzninventoryclient.repository.items.ItemsRepository;
import gov.zndev.springzninventoryclient.repository.locations.LocationsRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import gov.zndev.springzninventoryclient.repository.types.TypesRepository;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@FxmlView("inventory_item_list_layout.fxml")
public class InventoryTableListCtrl {

    @FXML
    private TableView<ItemPick> main_table;

    @FXML
    private TableColumn<ItemPick, String> name_column;

    @FXML
    private TableColumn<ItemPick, String> type_column;

    @FXML
    private TableColumn<ItemPick, String> brand_column;

    @FXML
    private TableColumn<ItemPick, String> model_column;

    @FXML
    private TableColumn<ItemPick, String> serial_column;

    @FXML
    private TableColumn<ItemPick, String> code_column;

    @FXML
    private TableColumn<ItemPick, String> location_column;

    @FXML
    private TextField searchfield;

    @FXML
    private Pagination pagination;

    @FXML
    private Label total_inventory;

    @FXML
    private ComboBox<String> selectSort;

    /////////////////////////////////////////////////////


    @Autowired
    private ItemsRepository itemsRepo;

    @Autowired
    private InventoryRepository inventoryRepo;

    @Autowired
    private LocationsRepository locationsRepo;

    @Autowired
    private FxWeaver fxWeaver;

    @FXML
    public void initialize() {

        setup();
        setupTable();
        setupPagination();

        // Load Initial Data
        loadInventoryToTable(1, table_row_size);
    }

    private void setup() {
        // Search Textfield

        searchfield.setOnKeyReleased(e -> {
            if (searchfield.getText().equals("")) {
                loadInventoryToTable(selected_page + 1, table_row_size);
            }
        });

        searchfield.setOnAction(e -> {
            if (!searchfield.getText().equals("")) {
                itemsRepo.searchItem(searchfield.getText(), table_row_size, new RepoInterface() {
                    @Override
                    public void activityDone(Boolean success, String message, Object object) {
                        Platform.runLater(() -> {

                            List<Item> resultList = (List<Item>) object;

                            ObservableList<ItemPick> listToStage = FXCollections.observableArrayList();
                            for (Item item : resultList) {
                                ItemPick itemToStage = new ItemPick();
                                itemToStage.copyItem(item);
                                listToStage.add(itemToStage);

                                inventoryRepo.getInventoryTotalItemCountByTag(ResourceHelper.TAG_ITEM, itemToStage.getItemId(), new RepoInterface() {
                                    @Override
                                    public void activityDone(Boolean success, String message, Object object) {
                                        itemToStage.setQuantity((int) object);
                                    }
                                });
                            }
                            setTableItems(listToStage);
                        });
                    }
                });
            }
        });

        // Total inventory
        itemsRepo.getItemsCount(new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if(success) {
                    Platform.runLater(()->{
                        total_inventory.setText("Total Items: " + object.toString());
                    });

                }
            }
        });

        // Search by

        // Sort
        selectSort.getItems().addAll("Date Created", "Name");
        selectSort.getSelectionModel().select(0);
        selectSort.setOnAction(e -> {
            switch (selectSort.getSelectionModel().getSelectedItem()) {
                case "Date Created":

                    break;
                case "Name":

                    break;
            }
        });
    }

    private void setupTable() {
        this.main_table.setRowFactory(row -> new TableRow<ItemPick>() {
            @Override
            public void updateItem(ItemPick item, boolean empty) {
                super.updateItem(item, empty);

                setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2 && (!isEmpty())) {
                        //openViewPersonInfo(getItem());

                    }
                });

                /*
                if (getItem() != null) {
                    System.out.println("Is exported is " + getItem().getIsExported());
                    if (getItem().getIsExported() == 1) {
                        getStyleClass().add("statusPrinted");
                    }
                }
                 */

            }
        });

        this.name_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("name"));
        this.type_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("type"));
        this.brand_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("brand"));
        this.model_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("model"));
        this.serial_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("serial"));
        this.code_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("code"));
        this.location_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("location"));

        main_table.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

            }
        });

        main_table.setRowFactory(row -> new TableRow<ItemPick>() {
            @Override
            protected void updateItem(ItemPick item, boolean b) {
                super.updateItem(item, b);

                setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2 && !isEmpty()) {
                        openInventoryView(getItem().getItemId());
                    }
                });
            }
        });
    }


    private int selected_page = 0;
    private int table_row_size = 30;
    private int sort_type = Helper.SORTTYPE_ASCENDING;

    private void setupPagination() {

        inventoryRepo.getInventoryCount((success, message, object) -> {
            if (success) {
                Platform.runLater(()->{
                    int item_count = (Integer) object;
                    int page_count = item_count / table_row_size;
                    if (item_count % table_row_size > 0) {
                        page_count++;
                    }

                    pagination.setCurrentPageIndex(selected_page);

                    if (selected_page < 50) {
                        pagination.setMaxPageIndicatorCount(page_count);
                    } else {
                        pagination.setMaxPageIndicatorCount(50);
                    }

                    pagination.setPageCount(page_count);
                    pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                        pagination.setCurrentPageIndex(newIndex.intValue());
                        selected_page = newIndex.intValue();

                        if (sort_type == Helper.SORTTYPE_ASCENDING) {
                            // Ascending
                            loadInventoryToTable(selected_page + 1, table_row_size);
                        } else {
                            // Descending
                            loadInventoryToTable(selected_page + 1, table_row_size);
                        }
                    });
                });

            } else {
                System.out.println("ITEMS: setupPagination error: " + message);
            }
        });
    }


    private void setTableItems(ObservableList<ItemPick> items) {
        main_table.getItems().clear();
        main_table.getItems().addAll(items);
    }


    private void loadInventoryToTable(int page, int size) {


        inventoryRepo.getInventoryByPage(page, size, new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {
                    List<Inventory> resultList = (List<Inventory>) object;

                    ObservableList<ItemPick> listToStage = FXCollections.observableArrayList();
                    for (Inventory result : resultList) {
                        ItemPick item = new ItemPick();
                        item.setItemId(result.getId());
                        item.setName(result.getItem().getName());
                        item.setType(result.getItem().getTypeName());
                        item.setBrand(result.getItem().getBrandName());
                        item.setModel(result.getItem().getModel());
                        item.setSerial(result.getSerialNumber());
                        item.setCode(result.getCode());

                        // Get location
                        System.out.println("Location id to fetch: "+result.getLocationId());
                        locationsRepo.getLocationById(result.getLocationId(), new RepoInterface() {
                            @Override
                            public void activityDone(Boolean success, String message, Object object) {
                                if(success){
                                    List<Location> list=(List<Location>)object;
                                    if(list.size()>0){
                                        System.out.println("Location name is "+list.get(0).getName());
                                        item.setLocation(list.get(0).getName());
                                    }else{
                                        item.setLocation("No Data Found");
                                    }
                                }else{
                                    System.out.println("Error fetching location: "+message);
                                }
                            }
                        });

                        listToStage.add(item);
                    }
                    Platform.runLater(() -> {
                        setTableItems(listToStage);
                    });

                } else {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setHeaderText("Cannot load data!");
                        alert.setContentText("Error: " + message);
                        alert.showAndWait();
                    });
                }
            }
        });
    }

    private void openInventoryView(int id){
        FxControllerAndView<ViewInventoryCtrl, AnchorPane> viewInventoryModal=fxWeaver.load(ViewInventoryCtrl.class);

        // Init Stage
        Stage stage=Helper.CreateStage("View Inventory");
        Scene scene=new Scene(viewInventoryModal.getView().get(),1080,650);

        stage.setScene(scene);

        stage.setResizable(false);
        stage.initModality(Modality.NONE);
        stage.initOwner(MainController.getStage());

        viewInventoryModal.getController().setStage(stage);
        viewInventoryModal.getController().setInventory(id);

        stage.show();


    }


}

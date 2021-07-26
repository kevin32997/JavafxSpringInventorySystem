package gov.zndev.springzninventoryclient.controller.items;

import gov.zndev.springzninventoryclient.MainController;
import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Item;
import gov.zndev.springzninventoryclient.models.others.ItemPick;
import gov.zndev.springzninventoryclient.models.others.Sort;
import gov.zndev.springzninventoryclient.repository.inventory.InventoryRepository;
import gov.zndev.springzninventoryclient.repository.items.ItemsRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


@Component
@FxmlView("item_table_list_layout.fxml")
public class ItemTableListCtrl implements Initializable {

    @FXML
    private TableView<Item> main_table;


    @FXML
    private TableColumn<Item, String> name_column;

    @FXML
    private TableColumn<Item, String> type_column;

    @FXML
    private TableColumn<Item, String> brand_column;

    @FXML
    private TableColumn<Item, String> date_created_column;

    @FXML
    private TableColumn<Item, String> created_by_column;

    @FXML
    private TableColumn<Item, String> inventory_column;

    @FXML
    private TextField searchfield;


    @FXML
    private Pagination pagination;

    @FXML
    private Label total_inventory;

    @FXML
    private ComboBox<String> selectSort;


    @Autowired
    private ItemsRepository itemsRepo;

    @Autowired
    private InventoryRepository inventoryRepo;

    @Autowired
    private FxWeaver fxWeaver;

    private Sort sort;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setup();
        setupTable();
        setupPagination();

        // Load Initial Data
        loadItemsToTable(selected_page, table_row_size);
    }

    private void setup() {

        // Initialize sort
        sort = new Sort(Sort.ASCENDING, "name");

        selectSort.getItems().addAll("Name", "Date Created");
        selectSort.getSelectionModel().select(0);
        selectSort.setOnAction(e -> {
            switch (selectSort.getSelectionModel().getSelectedItem()) {
                case "Date Created":
                    sort.setSortBy("dateCreated");
                    loadItemsToTable(selected_page, table_row_size);
                    break;
                case "Name":
                    sort.setSortBy("name");
                    loadItemsToTable(selected_page, table_row_size);
                    break;
            }
        });

        // Search Textfield
        searchfield.setOnKeyReleased(e -> {
            if (searchfield.getText().equals("")) {
                loadItemsToTable(selected_page + 1, table_row_size);
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


                                inventoryRepo.getInventoryCountByTag(ResourceHelper.TAG_ITEM, item.getId(), new RepoInterface() {
                                    @Override
                                    public void activityDone(Boolean success, String message, Object object) {
                                        if(success) {
                                            item.setInventoryCount((int) object);
                                        }
                                    }
                                });
                            }
                            setTableItems(FXCollections.observableArrayList(resultList));
                        });
                    }
                });
            }
        });

        // Total inventory
        itemsRepo.getItemsCount(new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                Platform.runLater(() -> {
                    total_inventory.setText("Total Items: " + object.toString());
                });

            }
        });


    }

    private void setupTable() {
        this.main_table.setRowFactory(row -> new TableRow<Item>() {
            @Override
            public void updateItem(Item item, boolean empty) {
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

        this.name_column.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        this.type_column.setCellValueFactory(new PropertyValueFactory<Item, String>("typeName"));
        this.brand_column.setCellValueFactory(new PropertyValueFactory<Item, String>("brandName"));
        this.date_created_column.setCellValueFactory(new PropertyValueFactory<Item, String>("dateCreated"));
        this.created_by_column.setCellValueFactory(new PropertyValueFactory<Item, String>("createdBy"));
        this.inventory_column.setCellValueFactory(new PropertyValueFactory<Item, String>("inventoryCount"));

        main_table.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

            }
        });

        main_table.setRowFactory(row -> new TableRow<Item>() {
            @Override
            protected void updateItem(Item item, boolean b) {
                super.updateItem(item, b);

                setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2 && !isEmpty()) {
                        openItemView(getItem());
                    }
                });
            }
        });

    }


    private int selected_page = 1;
    private int table_row_size = 30;

    private void setupPagination() {
        itemsRepo.getItemsCount((success, message, object) -> {
            if (success) {
                int item_count = (Integer) object;
                int page_count = item_count / table_row_size;
                if (item_count % table_row_size > 0) {
                    page_count++;
                }

                int finalPage_count = page_count;
                Platform.runLater(() -> {
                    pagination.setCurrentPageIndex(selected_page - 1);

                    if (selected_page < 50) {
                        pagination.setMaxPageIndicatorCount(finalPage_count);
                    } else {
                        pagination.setMaxPageIndicatorCount(50);
                    }

                    pagination.setPageCount(finalPage_count);
                    pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                        pagination.setCurrentPageIndex(newIndex.intValue());
                        selected_page = newIndex.intValue() + 1;

                        loadItemsToTable(selected_page, table_row_size);
                    });
                });

            } else {
                System.out.println("ITEMS: setupPagination error: " + message);
            }
        });
    }


    private void setTableItems(ObservableList<Item> items) {
        main_table.getItems().clear();
        main_table.getItems().addAll(items);
    }


    private void loadItemsToTable(int page, int size) {


        itemsRepo.getItemsByPage(page, size, sort, (success, message, object) -> {
            if (success) {
                List<Item> resultList = (List<Item>) object;

                for (Item result : resultList) {
                    inventoryRepo.getInventoryCountByTag(ResourceHelper.TAG_ITEM, result.getId(), new RepoInterface() {
                        @Override
                        public void activityDone(Boolean success, String message, Object object) {
                            if(success) {
                                System.out.println("Result Item Inventory Count: "+object);
                                result.setInventoryCount((int) object);
                            }
                        }
                    });
                }
                Platform.runLater(() -> {
                    setTableItems(FXCollections.observableArrayList(resultList));
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
        });
    }

    private void openItemView(Item item) {
        FxControllerAndView<ViewItemCtrl, AnchorPane> viewItemModal = fxWeaver.load(ViewItemCtrl.class);
        Parent parent = viewItemModal.getView().get();
        Scene scene = new Scene(parent, 1045, 555);

        Stage stage = Helper.CreateStage("View Item - " + item.getName());
        stage.setResizable(false);

        stage.setScene(scene);

        ViewItemCtrl ctrl = viewItemModal.getController();
        ctrl.setItem(item.getId());

        ctrl.setStage(stage);

        stage.initOwner(MainController.getStage());
        stage.initModality(Modality.NONE);
        stage.show();
    }


    @FXML
    void onCreateINewItem(ActionEvent event) {

    }

    @FXML
    void onBtnAscending(ActionEvent event) {
        sort.setSortType(Sort.ASCENDING);
        loadItemsToTable(selected_page, table_row_size);
    }

    @FXML
    void onBtnDescending(ActionEvent event) {
        sort.setSortType(Sort.DESCENDING);
        loadItemsToTable(selected_page, table_row_size);
    }


}

package gov.zndev.springzninventoryclient.controller.brands;

import gov.zndev.springzninventoryclient.controller.inventory.ViewInventoryCtrl;
import gov.zndev.springzninventoryclient.controller.items.ViewItemCtrl;
import gov.zndev.springzninventoryclient.helpers.AlertDialogHelper;
import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Brand;
import gov.zndev.springzninventoryclient.models.Inventory;
import gov.zndev.springzninventoryclient.models.Item;
import gov.zndev.springzninventoryclient.models.Location;
import gov.zndev.springzninventoryclient.models.others.ItemPick;
import gov.zndev.springzninventoryclient.models.others.Sort;
import gov.zndev.springzninventoryclient.repository.brands.BrandsRepository;
import gov.zndev.springzninventoryclient.repository.inventory.InventoryRepository;
import gov.zndev.springzninventoryclient.repository.items.ItemsRepository;
import gov.zndev.springzninventoryclient.repository.locations.LocationsRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
@FxmlView("brand_modal_view_brand_layout.fxml")
public class ViewBrandCtrl implements Initializable {

    private static final String TAG = "ViewBrandCtrl";

    @FXML
    private TextField brand_name;

    @FXML
    private TextField date_created;

    @FXML
    private TextField created_by;


    @FXML
    private TextArea description;

    @FXML
    private TableView<Item> main_table;

    @FXML
    private TableColumn<Item, String> name_column;

    @FXML
    private TableColumn<Item, String> model_column;

    @FXML
    private TableColumn<Item, String> type_column;

    @FXML
    private Pagination pagination;

    @FXML
    private TableView<Inventory> inv_table;

    @FXML
    private TableColumn<Inventory, String> inv_name_column;

    @FXML
    private TableColumn<Inventory, String> inv_code_column;

    @FXML
    private TableColumn<Inventory, String> inv_serial_column;

    @FXML
    private TableColumn<Inventory, String> inv_property_column;

    @FXML
    private TableColumn<Inventory, String> inv_location_column;

    @FXML
    private Pagination inv_pagination;

    @FXML
    private Label total_inventory;

    @FXML
    private Label total_items;

    //////////////////////////////////

    private InventoryRepository inventoryRepo;
    private LocationsRepository locationsRepo;
    private BrandsRepository brandsRepo;
    private ItemsRepository itemsRepo;

    @Autowired
    private FxWeaver fxWeaver;


    private Brand brand;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        inventoryRepo = new InventoryRepository();
        locationsRepo = new LocationsRepository();
        brandsRepo = new BrandsRepository();
        itemsRepo = new ItemsRepository();

        setupTable();
    }

    public void setBrand(Brand brand) {
        this.brand = brand;

        populateFields();
        setupPagination();
        loadItemToTable(1, table_row_size);

        setupInvPagination();
        loadInventoryToTable(inv_selected_page, inv_table_row_size);

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setupTable() {
        this.name_column.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        this.model_column.setCellValueFactory(new PropertyValueFactory<Item, String>("model"));
        this.type_column.setCellValueFactory(new PropertyValueFactory<Item, String>("typeName"));

        this.main_table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                viewItems(main_table.getSelectionModel().getSelectedItem());
            }
        });

        ////////////////////////
        inv_name_column.setCellValueFactory(c -> {
            if (c.getValue() != null) {
                return new SimpleStringProperty("" + c.getValue().getItem().getName());
            }
            return new SimpleStringProperty("<no data>");
        });

        inv_code_column.setCellValueFactory(new PropertyValueFactory<>("code"));
        inv_serial_column.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        inv_property_column.setCellValueFactory(new PropertyValueFactory<>("propertyNumber"));
        inv_location_column.setCellValueFactory(new PropertyValueFactory<>("locationName"));

        this.inv_table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                viewInventory(inv_table.getSelectionModel().getSelectedItem());
            }
        });

    }


    private void populateFields() {
        // Location name
        brand_name.setText(brand.getName());

        // Date Created
        date_created.setText(brand.getDateCreated().toString());

        // Created By
        created_by.setText("" + brand.getCreatedBy());


        // Description
        description.setText(brand.getDescription());
    }


    private int selected_page = 0;
    private int table_row_size = 17;
    private int sort_type = Helper.SORTTYPE_ASCENDING;

    private void setupPagination() {
        itemsRepo.getItemsCountByTag(ResourceHelper.TAG_BRAND, brand.getId(), new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {
                    int item_count = (Integer) object;

                    //Set total items
                    Platform.runLater(() -> {
                        total_items.setText(item_count + " Items");
                    });

                    if (item_count > 0) {
                        int page_count = item_count / table_row_size;
                        if (item_count % table_row_size > 0) {
                            page_count++;
                        }

                        pagination.setCurrentPageIndex(selected_page);

                        if (selected_page < 50) {
                            int finalPage_count = page_count;
                            Platform.runLater(() -> {
                                pagination.setMaxPageIndicatorCount(finalPage_count);
                            });

                        } else {
                            Platform.runLater(() -> {
                                pagination.setMaxPageIndicatorCount(50);
                            });
                        }

                        pagination.setPageCount(page_count);
                        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                            pagination.setCurrentPageIndex(newIndex.intValue());
                            selected_page = newIndex.intValue();

                            if (sort_type == Helper.SORTTYPE_ASCENDING) {
                                // Ascending
                                loadItemToTable(selected_page + 1, table_row_size);
                            } else {
                                // Descending
                                loadItemToTable(selected_page + 1, table_row_size);
                            }
                        });
                    } else {
                        pagination.setVisible(false);
                    }
                }
            }
        });

    }

    private void loadItemToTable(int page, int size) {
        itemsRepo.getItemsPageByTag(ResourceHelper.TAG_BRAND, brand.getId(), page, size, new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {

                if (success) {
                    List<Item> resultList = (List<Item>) object;
                    Platform.runLater(() -> {
                        main_table.getItems().clear();
                        main_table.getItems().addAll(resultList);
                    });
                } else {
                    Platform.runLater(() -> {
                        AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.ERROR, "System Message", "Can't load Items", "An Error has occurred!\n" + message);
                    });
                }
            }
        });
    }

    private int inv_selected_page = 1;
    private int inv_table_row_size = 17;
    private Sort inv_sort = new Sort(Sort.ASCENDING, "dateCreated");

    private void setupInvPagination() {
        inventoryRepo.getInventoryCountByTag(ResourceHelper.TAG_BRAND, brand.getId(), (success, message, object) -> {
            if (success) {
                Platform.runLater(() -> {
                    int item_count = (Integer) object;
                    total_inventory.setText(item_count + " Inventories");

                    if (item_count > 0) {
                        int page_count = item_count / inv_table_row_size;
                        if (item_count % inv_table_row_size > 0) {
                            page_count++;
                        }

                        inv_pagination.setCurrentPageIndex(inv_selected_page);

                        if (inv_selected_page < 50) {
                            int finalPage_count = page_count;
                            Platform.runLater(() -> {
                                inv_pagination.setMaxPageIndicatorCount(finalPage_count);
                            });

                        } else {
                            Platform.runLater(() -> {
                                inv_pagination.setMaxPageIndicatorCount(50);
                            });
                        }

                        inv_pagination.setPageCount(page_count);
                        inv_pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                            inv_pagination.setCurrentPageIndex(newIndex.intValue());
                            inv_selected_page = newIndex.intValue();
                            loadInventoryToTable(inv_selected_page, inv_table_row_size);
                        });
                    } else {
                        inv_pagination.setVisible(false);
                    }
                });
            }
        });

    }

    private void loadInventoryToTable(int page, int size) {
        inv_table.getItems().clear();
        inventoryRepo.getInventoryByTagPageSorted(ResourceHelper.TAG_BRAND, brand.getId(), page, size, inv_sort, new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {
                    List<Inventory> inventories = (List<Inventory>) object;
                    for (Inventory inventory : inventories) {
                        locationsRepo.getLocationById(inventory.getLocationId(), new RepoInterface() {
                            @Override
                            public void activityDone(Boolean success, String message, Object object) {
                                if (success) {
                                    List<Location> locations = (List<Location>) object;
                                    if (!locations.isEmpty()) {
                                        inventory.setLocationName(locations.get(0).getName());
                                    }
                                }
                            }
                        });
                    }

                    Platform.runLater(() -> {
                        inv_table.getItems().addAll(inventories);
                    });
                }
            }
        });
    }

    private void viewItems(Item item) {
        FxControllerAndView<ViewItemCtrl, AnchorPane> viewItem = fxWeaver.load(ViewItemCtrl.class);
        viewItem.getController().setItem(item);

        Stage stage = Helper.CreateStage("View Item - " + item.getName());
        stage.setScene(new Scene(viewItem.getView().get()));

        stage.setResizable(false);
        stage.initOwner(this.stage);

        viewItem.getController().setStage(stage);
        stage.show();
    }

    private void viewInventory(Inventory inventory) {
        FxControllerAndView<ViewInventoryCtrl, AnchorPane> viewInventory = fxWeaver.load(ViewInventoryCtrl.class);
        viewInventory.getController().setInventory(inventory);

        Stage stage = Helper.CreateStage("View Inventory - " + inventory.getItem().getName());
        stage.setScene(new Scene(viewInventory.getView().get()));

        stage.setResizable(false);
        stage.initOwner(this.stage);

        viewInventory.getController().setStage(stage);
        stage.show();

    }

    @FXML
    void onUpdateBrand(ActionEvent event) {
        FxControllerAndView<UpdateBrandCtrl,AnchorPane> updateBrand=fxWeaver.load(UpdateBrandCtrl.class);
        updateBrand.getController().setBrand(this.brand);
        updateBrand.getController().setParentCtrl(this);

        Stage stage=Helper.CreateStage("Update Brand");
        stage.setScene(new Scene(updateBrand.getView().get()));
        stage.setResizable(false);
        stage.initOwner(this.stage);

        updateBrand.getController().setStage(stage);
        stage.show();
    }


}

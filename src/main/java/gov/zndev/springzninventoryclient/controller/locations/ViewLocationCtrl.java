package gov.zndev.springzninventoryclient.controller.locations;

import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.Logger;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Inventory;
import gov.zndev.springzninventoryclient.models.InventoryActivityReference;
import gov.zndev.springzninventoryclient.models.Item;
import gov.zndev.springzninventoryclient.models.Location;
import gov.zndev.springzninventoryclient.models.others.ItemActivity;
import gov.zndev.springzninventoryclient.models.others.ItemPick;
import gov.zndev.springzninventoryclient.repository.inv_activity_ref.InvActivityRefRepository;
import gov.zndev.springzninventoryclient.repository.inventory.InventoryRepository;
import gov.zndev.springzninventoryclient.repository.inventory_activity.InventoryActivityRepository;
import gov.zndev.springzninventoryclient.repository.items.ItemsRepository;
import gov.zndev.springzninventoryclient.repository.locations.LocationsRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;


@Component
@FxmlView("location_modal_view_location_layout.fxml")
public class ViewLocationCtrl implements Initializable {

    private static final String TAG = "ViewLocationCtrl";

    @FXML
    private TextField location_name;

    @FXML
    private TextField date_created;

    @FXML
    private TextField created_by;

    @FXML
    private TextField total_inventory;

    @FXML
    private TextArea description;

    @FXML
    private TableView<ItemPick> inventory_table;

    @FXML
    private TableColumn<ItemPick, String> name_column;

    @FXML
    private TableColumn<ItemPick, String> code_column;

    @FXML
    private TableColumn<ItemPick, String> serial_column;

    @FXML
    private TableColumn<ItemPick, String> property_column;

    @FXML
    private Pagination inventory_pagination;

    @FXML
    private TableView<ItemActivity> activity_table;

    @FXML
    private TableColumn<ItemActivity, String> activity_id_column;

    @FXML
    private TableColumn<ItemActivity, String> activity_date_time_column;

    @FXML
    private TableColumn<ItemActivity, String> activity_action_column;

    @FXML
    private TableColumn<ItemActivity, String> activity_description_column;

    @FXML
    private TableColumn<ItemActivity, String> activity_total_column;

    @FXML
    private Pagination activity_pagination;


    //////////////////////////////////////////////////////////

    @Autowired
    private LocationsRepository locationsRepo;

    @Autowired
    private ItemsRepository itemsRepo;

    @Autowired
    private InventoryRepository inventoryRepo;

    @Autowired
    private InvActivityRefRepository invActivityRefRepo;

    @Autowired
    private InventoryActivityRepository invActivityRepo;

    private Location location;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        setupLocationItemsTable();
        setupLocationActivityTable();



    }

    public void setLocation(Location location) {
        this.location = location;
        populateFields();
        setupLocationItemsPagination();
        loadInventoryToTable(1, table_row_size);

        setupLocationActivityPagination();
        loadActivityToTable(1,table_row_size);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setupLocationItemsTable() {
        this.name_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("name"));
        this.code_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("code"));
        this.serial_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("serial"));
        this.property_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("propertyNumber"));
    }

    private void setupLocationActivityTable() {
        DecimalFormat decimalFormat=new DecimalFormat("0000000");

        this.activity_id_column.setCellValueFactory(c -> {
            if (c.getValue() != null) {
                return new SimpleStringProperty( decimalFormat.format(c.getValue().getId()));
            }
            return new SimpleStringProperty("<no data>");
        });

        this.activity_date_time_column.setCellValueFactory(new PropertyValueFactory<ItemActivity, String>("dateTime"));

        this.activity_action_column.setCellValueFactory(new PropertyValueFactory<ItemActivity, String>("type"));

        this.activity_description_column.setCellValueFactory(new PropertyValueFactory<ItemActivity, String>("description"));

        this.activity_total_column.setCellValueFactory(new PropertyValueFactory<ItemActivity, String>("total"));
    }

    private void populateFields() {
        // Location name
        location_name.setText(location.getName());

        // Date Created
        date_created.setText(location.getDateCreated().toString());

        // Created By
        created_by.setText("" + location.getCreatedBy());

        // Total Inventory
        inventoryRepo.getInventoryCountByTag(ResourceHelper.TAG_LOCATION, location.getId(), (success, message, object) -> {
            if (success) {
                total_inventory.setText("" + (int) object);
            }
        });

        // Description
        description.setText(location.getDescription());
    }

    private int table_row_size = 17;
    private int sort_type = Helper.SORTTYPE_ASCENDING;

    private int items_selected_page = 0;
    private int activity_selected_page = 0;


    private void setupLocationItemsPagination() {
        inventoryRepo.getInventoryCountByTag(ResourceHelper.TAG_LOCATION, location.getId(), new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {
                    int item_count = (Integer) object;

                    if (item_count > 0) {
                        int page_count = item_count / table_row_size;
                        if (item_count % table_row_size > 0) {
                            page_count++;
                        }

                        inventory_pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                            inventory_pagination.setCurrentPageIndex(newIndex.intValue());
                            items_selected_page = newIndex.intValue();

                            if (sort_type == Helper.SORTTYPE_ASCENDING) {
                                // Ascending
                                loadInventoryToTable(items_selected_page + 1, table_row_size);
                            } else {
                                // Descending
                                loadInventoryToTable(items_selected_page + 1, table_row_size);
                            }
                        });

                        int finalPage_count = page_count;
                        Platform.runLater(() -> {
                            inventory_pagination.setCurrentPageIndex(items_selected_page);

                            if (items_selected_page < 50) {
                                inventory_pagination.setMaxPageIndicatorCount(finalPage_count);
                            } else {
                                inventory_pagination.setMaxPageIndicatorCount(50);
                            }
                            inventory_pagination.setPageCount(finalPage_count);
                        });
                    } else {
                        Platform.runLater(() -> {
                            inventory_pagination.setVisible(false);
                        });

                    }
                } else {
                    Logger.print(TAG, "loadInventoryToTable():getInventoryCountByLocation():", "Error occurred: \n" + message, null);
                }
            }
        });
    }

    private void setupLocationActivityPagination() {
        invActivityRefRepo.getInvActivityRefCountByLocation(location.getId(), new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {
                    int item_count = (Integer) object;

                    if (item_count > 0) {
                        int page_count = item_count / table_row_size;
                        if (item_count % table_row_size > 0) {
                            page_count++;
                        }

                        activity_pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                            activity_pagination.setCurrentPageIndex(newIndex.intValue());
                            activity_selected_page = newIndex.intValue();

                            if (sort_type == Helper.SORTTYPE_ASCENDING) {
                                // Ascending
                                loadActivityToTable(activity_selected_page + 1, table_row_size);
                            } else {
                                // Descending
                                loadActivityToTable(activity_selected_page + 1, table_row_size);
                            }
                        });

                        int finalPage_count = page_count;
                        Platform.runLater(() -> {
                            activity_pagination.setCurrentPageIndex(activity_selected_page);


                            if (activity_selected_page < 50) {
                                activity_pagination.setMaxPageIndicatorCount(finalPage_count);
                            } else {
                                activity_pagination.setMaxPageIndicatorCount(50);
                            }
                            activity_pagination.setPageCount(finalPage_count);
                        });
                    } else {
                        Platform.runLater(() -> {
                            activity_pagination.setVisible(false);
                        });

                    }
                } else {
                    Logger.print(TAG, "loadInventoryToTable():getInventoryCountByLocation():", "Error occurred: \n" + message, null);
                }
            }
        });
    }

    private void loadInventoryToTable(int page, int size) {
        inventoryRepo.getInventoryPageByLocation(location.getId(), page, size, new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {
                    List<Inventory> inventories = (List<Inventory>) object;
                    ObservableList<ItemPick> items = FXCollections.observableArrayList();
                    for (Inventory inventory : inventories) {
                        ItemPick item = new ItemPick();
                        item.setItemId(inventory.getItemId());
                        item.setName(inventory.getItem().getName());
                        item.setCode(inventory.getCode());
                        item.setSerial(inventory.getSerialNumber());
                        item.setPropertyNumber(inventory.getPropertyNumber());
                        items.add(item);
                    }
                    inventory_table.getItems().clear();
                    inventory_table.getItems().addAll(items);
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

    private void loadActivityToTable(int page, int size) {
        invActivityRefRepo.getInvActivityRefByLocation(location.getId(), page, size, new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {
                    List<InventoryActivityReference> inventoryActivityReferences = (List<InventoryActivityReference>) object;
                    ObservableList<ItemActivity> items = FXCollections.observableArrayList();
                    for (InventoryActivityReference reference : inventoryActivityReferences) {
                        ItemActivity activity=new ItemActivity();
                        activity.setId(reference.getId());
                        activity.setDateTime(reference.getActivityDate().toString());
                        activity.setType(reference.getAction().toUpperCase());
                        activity.setDescription(reference.getRemarks());

                        items.add(activity);

                        invActivityRepo.getInvActCountByTag(ResourceHelper.TAG_REFERENCE, reference.getId(), new RepoInterface() {
                            @Override
                            public void activityDone(Boolean success, String message, Object object) {
                                if(success){
                                    activity.setTotal(""+(int)object);
                                }
                            }
                        });
                    }
                    activity_table.getItems().clear();
                    activity_table.getItems().addAll(items);
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


    public void refreshTables() {


    }
}

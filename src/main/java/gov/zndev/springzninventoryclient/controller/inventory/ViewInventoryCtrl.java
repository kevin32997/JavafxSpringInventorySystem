package gov.zndev.springzninventoryclient.controller.inventory;

import gov.zndev.springzninventoryclient.controller.activity.ViewActivityCtrl;
import gov.zndev.springzninventoryclient.controller.locations.ViewLocationCtrl;
import gov.zndev.springzninventoryclient.controller.others.AddRemarkCtrl;
import gov.zndev.springzninventoryclient.controller.others.ViewRemarkCtrl;
import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.*;
import gov.zndev.springzninventoryclient.models.others.ItemActivity;
import gov.zndev.springzninventoryclient.models.others.Sort;
import gov.zndev.springzninventoryclient.repository.brands.BrandsRepository;
import gov.zndev.springzninventoryclient.repository.inventory.InventoryRepository;
import gov.zndev.springzninventoryclient.repository.inventory_activity.InventoryActivityRepository;
import gov.zndev.springzninventoryclient.repository.items.ItemsRepository;
import gov.zndev.springzninventoryclient.repository.locations.LocationsRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import gov.zndev.springzninventoryclient.repository.remarks.RemarksRepository;
import gov.zndev.springzninventoryclient.repository.types.TypesRepository;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@FxmlView("inventory_view_inventory_layout.fxml")
public class ViewInventoryCtrl {

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

    @FXML
    private TextField model;

    @FXML
    private TableView<ItemActivity> activity_table;

    @FXML
    private TableColumn<ItemActivity, String> activity_description_column;

    @FXML
    private TableColumn<ItemActivity, String> activity_user_column;

    @FXML
    private TableColumn<ItemActivity, String> activity_date_column;

    @FXML
    private TableView<Remark> remarks_table;

    @FXML
    private TableColumn<Remark, String> remarks_description_column;

    @FXML
    private TableColumn<Remark, String> remarks_date_column;

    @FXML
    private TableColumn<Remark, String> remarks_user_column;

    @FXML
    private TextArea description;

    @FXML
    private TextField location_name;

    @FXML
    private Pagination activity_pagination;

    @FXML
    private Pagination remarks_pagination;


    //////////////////////////////////////////////////////

    @Autowired
    private LocationsRepository locationsRepo;

    @Autowired
    private InventoryRepository inventoryRepo;

    @Autowired
    private InventoryActivityRepository activityRepo;

    @Autowired
    private ItemsRepository itemsRepo;

    @Autowired
    private RemarksRepository remarksRepo;

    @Autowired
    private BrandsRepository brandsRepo;

    @Autowired
    private TypesRepository typesRepo;

    @Autowired
    private FxWeaver fxWeaver;

    private Stage stage;

    private Inventory inventory;

    private Item inventoryItem;

    private Location inventoryLocation;

    @FXML
    public void initialize() {
        setupRemarksTable();
        setupActivityTable();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        populateFields();
        initTablesData();
    }

    public void setInventory(int id) {
        inventoryRepo.getInventoryById(id, new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {
                    List<Inventory> inventories = (List<Inventory>) object;
                    if (inventories.size() > 0) {
                        inventory = inventories.get(0);
                        Platform.runLater(() -> {
                            populateFields();
                            initTablesData();
                        });
                    }
                }
            }
        });
    }

    private void initTablesData() {
        setupPagination();
        loadInventoryActivityToTable(selected_page, table_row_size);

        setupRemarksPagination();
        loadRemarksActivityToTable(remark_selected_page, remark_table_row_size);
    }

    private void populateFields() {

        // Item Info Fields

        name.setText(inventory.getItem().getName());

        // Brand
        brandsRepo.getBrandById(inventory.getItem().getBrandId(), new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if(success){
                    List<Brand> list= (List<Brand>) object;
                    if(list.size()>0) {

                        Platform.runLater(() -> {
                            brand.setText(list.get(0).getName());
                        });
                    }
                }
            }
        });

        // Type
        typesRepo.getTypeById(inventory.getItem().getTypeId(), new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if(success){
                    List<Type> list= (List<Type>) object;
                    if(list.size()>0){
                        Platform.runLater(()->{
                            type.setText(list.get(0).getName());
                        });
                    }
                }
            }
        });


        model.setText(inventory.getItem().getModel());

        // Inventory Info Fields

        code.setText(inventory.getCode());
        serial_number.setText(inventory.getSerialNumber());
        property_number.setText(inventory.getPropertyNumber());

        // Location Field

        locationsRepo.getLocationById(inventory.getLocationId(), new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {
                    List<Location> list = (List<Location>) object;
                    if (list.size() > 0) {
                        inventoryLocation = list.get(0);
                        location_name.setText(inventoryLocation.getName());
                    }
                }
            }
        });

        // Item Description
        description.setText(inventory.getItem().getDescription());
    }

    private void setupRemarksTable() {
        this.remarks_description_column.setCellValueFactory(new PropertyValueFactory<Remark, String>("description"));
        this.remarks_date_column.setCellValueFactory(c -> {
            if (c.getValue() != null) {
                return new SimpleStringProperty(new SimpleDateFormat("MMMMM dd, yyyy").format(c.getValue().getDateCreated()));
            }
            return new SimpleStringProperty("<no data>");
        });

        this.remarks_user_column.setCellValueFactory(new PropertyValueFactory<Remark, String>("userId"));

        this.remarks_table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                openViewRemark(remarks_table.getSelectionModel().getSelectedItem());
            }
        });

    }

    private void setupActivityTable() {
        this.activity_description_column.setCellValueFactory(new PropertyValueFactory<ItemActivity, String>("description"));
        this.activity_user_column.setCellValueFactory(new PropertyValueFactory<ItemActivity, String>("user"));
        this.activity_date_column.setCellValueFactory(new PropertyValueFactory<ItemActivity, String>("dateTime"));

        this.activity_table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                openViewActivity(this.activity_table.getSelectionModel().getSelectedItem().getReferenceId());
            }
        });
    }

    private int selected_page = 1;
    private int table_row_size = 14;

    private void setupPagination() {
        activityRepo.getInvActCountByTag(ResourceHelper.TAG_INVENTORY, inventory.getId(), (success, message, object) -> {
            if (success) {
                Platform.runLater(() -> {
                    int item_count = (Integer) object;
                    int page_count = item_count / table_row_size;
                    if (item_count % table_row_size > 0) {
                        page_count++;
                    }
                    activity_pagination.setCurrentPageIndex(selected_page - 1);

                    if (selected_page < 50) {
                        activity_pagination.setMaxPageIndicatorCount(page_count);
                    } else {
                        activity_pagination.setMaxPageIndicatorCount(50);
                    }
                    activity_pagination.setPageCount(page_count);
                    activity_pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                        activity_pagination.setCurrentPageIndex(newIndex.intValue());
                        selected_page = newIndex.intValue() + 1;
                        loadInventoryActivityToTable(selected_page, table_row_size);
                    });
                });

            } else {
                System.out.println("Brands: setupPagination error: " + message);
            }
        });
    }


    private void loadInventoryActivityToTable(int page, int table_row_size) {
        activity_table.getItems().clear();
        activityRepo.getInvActivityPageByTag(ResourceHelper.TAG_INVENTORY, inventory.getId(), page, table_row_size, (success, message, object) -> {
            if (success) {
                List<InventoryActivity> list = (List<InventoryActivity>) object;
                List<ItemActivity> listToTable = new ArrayList<>();
                for (InventoryActivity activity : list) {
                    ItemActivity itemActivity = new ItemActivity();
                    itemActivity.setId(activity.getId());
                    itemActivity.setUser("");
                    itemActivity.setDateTime(activity.getActivityDate().toString());
                    itemActivity.setReferenceId(activity.getReferenceId());
                    locationsRepo.getLocationById(activity.getLocationId(), new RepoInterface() {
                        @Override
                        public void activityDone(Boolean success, String message, Object object) {
                            if (success) {
                                List<Location> locations = (List<Location>) object;
                                if (locations.size() > 0) {
                                    Location location = locations.get(0);
                                    if (activity.getAction().equals(InventoryActivity.ACTION_IMPORT)) {
                                        itemActivity.setDescription("Imported to " + location.getName());
                                    } else if (activity.getAction().equals(InventoryActivity.ACTION_EXPORT)) {
                                        locationsRepo.getLocationById(activity.getOtherLocationId(), new RepoInterface() {
                                            @Override
                                            public void activityDone(Boolean success, String message, Object object) {
                                                if (success) {
                                                    List<Location> otherLocations = (List<Location>) object;
                                                    if (otherLocations.size() > 0) {
                                                        Location otherLocation = otherLocations.get(0);
                                                        itemActivity.setDescription("Moved from " + otherLocation.getName() + " to " + location.getName());
                                                    }
                                                }
                                            }
                                        });
                                    }

                                }
                            }
                        }
                    });
                    listToTable.add(itemActivity);
                    Platform.runLater(() -> {
                        activity_table.getItems().addAll(listToTable);
                    });
                }

            }
        });
    }

    private int remark_selected_page = 1;
    private int remark_table_row_size = 5;
    private Sort remarks_sort = new Sort(Sort.ASCENDING, "dateCreated");

    private void setupRemarksPagination() {
        remarksRepo.getRemarksCountByTagId(ResourceHelper.TAG_INVENTORY, inventory.getId(), (success, message, object) -> {
            if (success) {
                Platform.runLater(() -> {
                    int item_count = (Integer) object;
                    System.out.println("View Inventory remarks item count is " + item_count);
                    if (item_count > 0) {
                        int page_count = item_count / remark_table_row_size;
                        if (item_count % remark_table_row_size > 0) {
                            page_count++;
                        }
                        remarks_pagination.setCurrentPageIndex(remark_selected_page - 1);

                        if (remark_selected_page < 50) {
                            remarks_pagination.setMaxPageIndicatorCount(page_count);
                        } else {
                            remarks_pagination.setMaxPageIndicatorCount(50);
                        }
                        remarks_pagination.setPageCount(page_count);
                        remarks_pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                            remarks_pagination.setCurrentPageIndex(newIndex.intValue());
                            remark_selected_page = newIndex.intValue() + 1;
                            loadRemarksActivityToTable(remark_selected_page, remark_table_row_size);
                        });
                    } else {
                        remarks_pagination.setVisible(false);
                    }
                });

            } else {
                System.out.println("Remarks: setupPagination error: " + message);
            }
        });
    }


    private void loadRemarksActivityToTable(int page, int table_row_size) {
        remarks_table.getItems().clear();
        remarksRepo.getRemarksByPageTagSorted(ResourceHelper.TAG_INVENTORY, inventory.getId(), page, table_row_size, remarks_sort, (success, message, object) -> {
            if (success) {
                List<Remark> list = (List<Remark>) object;
                remarks_table.getItems().addAll(list);
            }
        });
    }

    private void viewLocation() {
        FxControllerAndView<ViewLocationCtrl, AnchorPane> viewLocation = fxWeaver.load(ViewLocationCtrl.class);
        viewLocation.getController().setLocation(inventoryLocation);

        Stage stage = Helper.CreateStage("View Location - " + inventoryLocation.getName());
        stage.setScene(new Scene(viewLocation.getView().get()));
        stage.setResizable(false);
        stage.initOwner(this.stage);

        viewLocation.getController().setStage(stage);
        stage.show();
    }

    private void openAddRemark() {
        FxControllerAndView<AddRemarkCtrl, AnchorPane> addRemark = fxWeaver.load(AddRemarkCtrl.class);
        addRemark.getController().setCtrl(this);
        addRemark.getController().setTypeAndId(ResourceHelper.TAG_INVENTORY, inventory.getId());
        Stage stage = Helper.CreateStage("Add Remarks - Inventory");
        stage.setScene(new Scene(addRemark.getView().get()));
        stage.setResizable(false);
        stage.initOwner(this.stage);
        addRemark.getController().setStage(stage);
        stage.show();
    }

    private void openViewRemark(Remark remark) {
        FxControllerAndView<ViewRemarkCtrl, AnchorPane> viewRemark = fxWeaver.load(ViewRemarkCtrl.class);
        viewRemark.getController().setRemark(remark);

        Stage stage = Helper.CreateStage("View Remark");
        stage.setScene(new Scene(viewRemark.getView().get()));

        stage.setResizable(false);
        stage.initOwner(this.stage);

        stage.show();
    }

    private void openViewActivity(int id) {
        FxControllerAndView<ViewActivityCtrl, AnchorPane> viewActivity = fxWeaver.load(ViewActivityCtrl.class);
        viewActivity.getController().setInvActReference(id);

        Stage stage = Helper.CreateStage("View Activity");
        stage.setScene(new Scene(viewActivity.getView().get()));
        stage.setResizable(false);
        stage.initOwner(this.stage);

        stage.show();

    }

    private void openUpdateInventory(){
        FxControllerAndView<UpdateInventoryCtrl,AnchorPane> updateInventory=fxWeaver.load(UpdateInventoryCtrl.class);
        updateInventory.getController().setInventory(inventory);
        updateInventory.getController().setParentController(this);

        Stage stage=Helper.CreateStage("Update Inventory");
        stage.setScene(new Scene(updateInventory.getView().get(),790,400));
        stage.setResizable(false);
        stage.initOwner(this.stage);

        updateInventory.getController().setStage(stage);

        stage.show();
    }


    public void refreshTables() {
        initTablesData();
    }

    @FXML
    void onAddRemarks(ActionEvent event) {
        openAddRemark();
    }

    @FXML
    void onLocationDetails(ActionEvent event) {
        viewLocation();
    }

    @FXML
    void onUpdateDetails(ActionEvent event) {
        openUpdateInventory();
    }


}

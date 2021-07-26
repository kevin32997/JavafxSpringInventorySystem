package gov.zndev.springzninventoryclient.controller.activity;

import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Inventory;
import gov.zndev.springzninventoryclient.models.InventoryActivity;
import gov.zndev.springzninventoryclient.models.InventoryActivityReference;
import gov.zndev.springzninventoryclient.models.Location;
import gov.zndev.springzninventoryclient.models.others.ItemPick;
import gov.zndev.springzninventoryclient.repository.inv_activity_ref.InvActivityRefRepository;
import gov.zndev.springzninventoryclient.repository.inventory.InventoryRepository;
import gov.zndev.springzninventoryclient.repository.inventory_activity.InventoryActivityRepository;
import gov.zndev.springzninventoryclient.repository.locations.LocationsRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@FxmlView("activity_view_activity_layout.fxml")
public class ViewActivityCtrl {


    @FXML
    private TextField location_from;

    @FXML
    private TextField location_to;

    @FXML
    private TextField activity_date;

    @FXML
    private TextField incharge;

    @FXML
    private TextField reference;

    @FXML
    private TextField encoder;

    @FXML
    private TextField id;

    @FXML
    private TextField type;

    @FXML
    private TableView<ItemPick> items_table;

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
    private TextArea description;

    ///////////////////////////////////////////////////////////////////////////////


    @Autowired
    private InvActivityRefRepository referenceRepo;

    @Autowired
    private InventoryRepository invRepo;

    @Autowired
    private InventoryActivityRepository invActivityRepo;

    @Autowired
    private LocationsRepository locationRepo;

    private InventoryActivityReference invReference;

    private Stage stage;

    @FXML
    public void initialize() {
        setupItemsTable();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setInvActReference(InventoryActivityReference invReference) {
        this.invReference = invReference;
        populateFields();
        loadItems();
    }

    public void setInvActReference(int id) {
        referenceRepo.getInvActivityRefById(id, new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {
                    List<InventoryActivityReference> list = (List<InventoryActivityReference>) object;
                    if (list.size() > 0) {
                        invReference = list.get(0);
                        Platform.runLater(() -> {
                            populateFields();

                        });
                        loadItems();
                    }
                }
            }
        });
    }

    public void populateFields() {
        this.id.setText(new DecimalFormat("000000").format(invReference.getId()));
        this.type.setText(invReference.getAction().toUpperCase());

        // Location from
        if (invReference.getLocationFromId() == 0) {
            location_from.setText("OUT");
        } else {
            locationRepo.getLocationById(invReference.getLocationFromId(), new RepoInterface() {
                @Override
                public void activityDone(Boolean success, String message, Object object) {
                    if (success) {
                        List<Location> list = (List<Location>) object;
                        if (list.size() > 0) {
                            invReference.setLocationFrom(list.get(0));
                            Platform.runLater(() -> {
                                location_from.setText(list.get(0).getName());
                            });
                        }
                    }
                }
            });
        }

        // Location to
        System.out.println("View Activity Location id is " + invReference.getLocationId());
        locationRepo.getLocationById(invReference.getLocationId(), new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {
                    List<Location> list = (List<Location>) object;
                    if (list.size() > 0) {
                        invReference.setLocation(list.get(0));
                        Platform.runLater(() -> {
                            location_to.setText(list.get(0).getName());
                        });
                    }
                }
            }
        });

        incharge.setText(invReference.getConsignee());
        encoder.setText("" + invReference.getUserId());
        activity_date.setText(new SimpleDateFormat("MMMMM dd, yyyy hh:mm:ss aaa").format(invReference.getActivityDate()));
        reference.setText(invReference.getReference());
        description.setText(invReference.getRemarks());

    }

    private void setupItemsTable() {
        this.name_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("name"));
        this.brand_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("brand"));
        this.type_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("type"));
        this.code_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("code"));
        this.serial_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("serial"));
        this.property_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("propertyNumber"));
    }

    private void loadItems() {
        items_table.getItems().clear();
        invActivityRepo.getAllInvActivityByTagId(ResourceHelper.TAG_REFERENCE, this.invReference.getId(), (success, message, object) -> {
            if (success) {
                List<InventoryActivity> activities = (List<InventoryActivity>) object;
                int item_size = activities.size();

                ObservableList<ItemPick> items = FXCollections.observableArrayList();

                for (InventoryActivity activity : activities) {

                    invRepo.getInventoryById(activity.getInventoryId(), new RepoInterface() {
                        @Override
                        public void activityDone(Boolean success, String message, Object object) {
                            if (success) {

                                List<Inventory> inventories = (List<Inventory>) object;

                                if (inventories.size() > 0) {
                                    Inventory inventory = inventories.get(0);
                                    ItemPick temp = new ItemPick();
                                    temp.setName(inventory.getItem().getName());
                                    temp.setBrand(inventory.getItem().getBrandName());
                                    temp.setType(inventory.getItem().getTypeName());
                                    temp.setCode(inventory.getCode());
                                    temp.setSerial(inventory.getSerialNumber());
                                    temp.setPropertyNumber(inventory.getPropertyNumber());
                                    items.add(temp);
                                    Platform.runLater(() -> {
                                        checkItems(items, item_size);
                                    });
                                }
                            }
                        }
                    });
                }
            } else {
                System.out.println("Error Occured: " + message);
            }
        });
    }

    private void checkItems(List<ItemPick> list, int items_count) {
        if (list.size() == items_count) {
            items_table.getItems().clear();
            items_table.getItems().addAll(list);
        }
    }


}

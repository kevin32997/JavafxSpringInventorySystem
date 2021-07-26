package gov.zndev.springzninventoryclient.controller.items;

import gov.zndev.springzninventoryclient.controller.inventory.ViewInventoryCtrl;
import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Inventory;
import gov.zndev.springzninventoryclient.models.Item;
import gov.zndev.springzninventoryclient.models.Location;
import gov.zndev.springzninventoryclient.models.others.ItemLocation;
import gov.zndev.springzninventoryclient.models.others.ItemPick;
import gov.zndev.springzninventoryclient.models.others.Sort;
import gov.zndev.springzninventoryclient.repository.inventory.InventoryRepository;
import gov.zndev.springzninventoryclient.repository.items.ItemsRepository;
import gov.zndev.springzninventoryclient.repository.locations.LocationsRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;


@Component
@FxmlView("item_modal_view_item_layout.fxml")
public class ViewItemCtrl {

    @FXML
    private TableView<Inventory> inventory_table;

    @FXML
    private TableColumn<Inventory, String> code_column;

    @FXML
    private TableColumn<Inventory, String> serial_column;

    @FXML
    private TableColumn<Inventory, String> property_column;

    @FXML
    private TableColumn<Inventory, String> location_column;

    @FXML
    private Pagination pagination;

    @FXML
    private TextField name;

    @FXML
    private TextField brand;

    @FXML
    private TextField type;

    @FXML
    private TextField model;

    @FXML
    private TextField dateCreated;

    @FXML
    private TextField createdBy;

    @FXML
    private TextArea description;

    /////////////////////////////////
    private Stage stage;
    private Item item;

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
        setupTable();
    }

    private void setupTable() {
        this.code_column.setCellValueFactory(new PropertyValueFactory<>("code"));
        this.serial_column.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        this.property_column.setCellValueFactory(new PropertyValueFactory<>("propertyNumber"));
        this.location_column.setCellValueFactory(new PropertyValueFactory<>("locationName"));

        this.inventory_table.setOnMouseClicked(e->{
            if(e.getClickCount()==2){
                openViewInventory(inventory_table.getSelectionModel().getSelectedItem());
            }
        });
    }

    public void setItem(Item item) {
        this.item = item;
        load();

    }

    public void setItem(int itemId) {
        itemsRepo.getItemById(itemId, (success, message, object) -> {
            if (success) {
                List<Item> resultList = (List<Item>) object;
                if (resultList.size() > 0) {
                    item = resultList.get(0);
                    load();
                }
            }
        });
    }

    private void load() {
        loadItem();
        setupInventoryPagination();
        loadInventoryToTable(selected_page, table_row_size);
    }

    private void loadItem() {
        name.setText(item.getName());
        brand.setText(item.getBrandName());
        type.setText(item.getTypeName());
        model.setText(item.getModel());
        description.setText(item.getDescription());
        dateCreated.setText(item.getDateCreated().toString());
        createdBy.setText("" + item.getCreatedBy());
    }

    private int selected_page = 1;
    private int table_row_size = 30;
    private Sort sort = new Sort(Sort.DESCENDING, "dateCreated");

    private void setupInventoryPagination() {
        inventoryRepo.getInventoryCountByTag(ResourceHelper.TAG_ITEM, item.getId(), (success, message, object) -> {
            if (success) {
                Platform.runLater(() -> {
                    int item_count = (Integer) object;
                    int page_count = item_count / table_row_size;
                    if (item_count % table_row_size > 0) {
                        page_count++;
                    }
                    pagination.setCurrentPageIndex(selected_page - 1);

                    if (selected_page < 50) {
                        pagination.setMaxPageIndicatorCount(page_count);
                    } else {
                        pagination.setMaxPageIndicatorCount(50);
                    }
                    pagination.setPageCount(page_count);
                    pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                        pagination.setCurrentPageIndex(newIndex.intValue());
                        selected_page = newIndex.intValue() + 1;
                        loadInventoryToTable(selected_page, table_row_size);
                    });
                });

            } else {
                System.out.println("Item's Inventory: setupPagination error: " + message);
            }
        });
    }


    private void loadInventoryToTable(int selected_page, int table_row_size) {
        inventory_table.getItems().clear();
        inventoryRepo.getInventoryByTagPageSorted(ResourceHelper.TAG_ITEM, item.getId(), selected_page, table_row_size, sort, new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {
                    List<Inventory> list = (List<Inventory>) object;

                    for (Inventory inventory : list) {
                        locationsRepo.getLocationById(inventory.getLocationId(), (success1, message1, object1) -> {
                            if (success1) {
                                List<Location> locations = (List<Location>) object1;
                                if (locations.size() > 0) {
                                    inventory.setLocationName(locations.get(0).getName());
                                }
                            }
                        });
                    }

                    Platform.runLater(() -> {
                        inventory_table.getItems().addAll(list);
                    });

                }
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void onUpdateDetails(ActionEvent event) {
        openUpdateItemView(this.item);
    }


    private void openUpdateItemView(Item item) {
        FxControllerAndView<UpdateItemCtrl, AnchorPane> updateItem=fxWeaver.load(UpdateItemCtrl.class);
        updateItem.getController().setItem(item);
        Stage stage= Helper.CreateStage("Update Item");
        stage.setScene(new Scene(updateItem.getView().get()));
        stage.setResizable(false);
        stage.initOwner(this.stage);
        updateItem.getController().setStage(stage);
        stage.show();
    }

    private void openViewInventory(Inventory inventory){
        FxControllerAndView<ViewInventoryCtrl,AnchorPane> viewInventory=fxWeaver.load(ViewInventoryCtrl.class);
        viewInventory.getController().setInventory(inventory);

        Stage stage=Helper.CreateStage("View Inventory");
        stage.setScene(new Scene(viewInventory.getView().get()));
        stage.setResizable(false);
        stage.initOwner(this.stage);

        viewInventory.getController().setStage(stage);
        stage.show();
    }


}

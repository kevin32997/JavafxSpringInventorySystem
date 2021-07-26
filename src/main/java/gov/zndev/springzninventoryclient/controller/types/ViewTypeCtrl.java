package gov.zndev.springzninventoryclient.controller.types;

import gov.zndev.springzninventoryclient.helpers.AlertDialogHelper;
import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Item;
import gov.zndev.springzninventoryclient.models.Type;
import gov.zndev.springzninventoryclient.models.others.ItemPick;
import gov.zndev.springzninventoryclient.repository.brands.BrandsRepository;
import gov.zndev.springzninventoryclient.repository.inventory.InventoryRepository;
import gov.zndev.springzninventoryclient.repository.items.ItemsRepository;
import gov.zndev.springzninventoryclient.repository.locations.LocationsRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewTypeCtrl implements Initializable {

    @FXML
    private TextField type_name;

    @FXML
    private TextField date_created;

    @FXML
    private TextField created_by;

    @FXML
    private TextField total_inventory;

    @FXML
    private TextArea description;

    @FXML
    private TableView<ItemPick> main_table;

    @FXML
    private TableColumn<ItemPick, String> name_column;

    @FXML
    private TableColumn<ItemPick, String> model_column;

    @FXML
    private TableColumn<ItemPick, String> brand_column;

    @FXML
    private TableColumn<ItemPick, Integer> quantity_column;

    @FXML
    private Pagination pagination;

    /////////////////////////////////////////////////

    private InventoryRepository inventoryRepo;
    private LocationsRepository locationsRepo;
    private BrandsRepository brandsRepo;
    private ItemsRepository itemsRepo;

    private Type type;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        inventoryRepo = new InventoryRepository();
        locationsRepo = new LocationsRepository();
        brandsRepo = new BrandsRepository();
        itemsRepo = new ItemsRepository();

        setupTable();
    }

    public void setType(Type type) {
        this.type = type;
        populateFields();
        setupPagination();
        loadInventoryToTable(1, table_row_size);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setupTable() {
        this.name_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("name"));
        this.model_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("model"));
        this.brand_column.setCellValueFactory(new PropertyValueFactory<ItemPick, String>("brand"));
        this.quantity_column.setCellValueFactory(new PropertyValueFactory<ItemPick, Integer>("quantity"));
    }


    private void populateFields() {
        // Location name
        type_name.setText(type.getName());

        // Date Created
        date_created.setText(type.getDateCreated().toString());

        // Created By
        created_by.setText("" + type.getCreatedBy());

        // Total Inventory
        itemsRepo.getItemsCountByTag(ResourceHelper.TAG_TYPE,type.getId(), (success, message, object) -> {
            if (success) {
                total_inventory.setText("" + (int) object);
            }
        });

        // Description
        description.setText(type.getDescription());
    }


    private int selected_page = 0;
    private int table_row_size = 17;
    private int sort_type = Helper.SORTTYPE_ASCENDING;

    private void setupPagination() {
        itemsRepo.getItemsCountByTag(ResourceHelper.TAG_TYPE,type.getId(), new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {
                    int item_count = (Integer) object;

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
                                loadInventoryToTable(selected_page + 1, table_row_size);
                            } else {
                                // Descending
                                loadInventoryToTable(selected_page + 1, table_row_size);
                            }
                        });
                    } else {
                        pagination.setVisible(false);
                    }
                }
            }
        });

    }

    private void loadInventoryToTable(int page, int size) {
        itemsRepo.getItemsPageByTag(ResourceHelper.TAG_TYPE,type.getId(), page, size, new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {

                if (success) {
                    List<Item> resultList = (List<Item>) object;
                    ObservableList<ItemPick> items = FXCollections.observableArrayList();
                    for (Item item : resultList) {
                        ItemPick resultItem = new ItemPick();
                        resultItem.setName(item.getName());
                        resultItem.setBrand(item.getBrandName());
                        resultItem.setModel(item.getModel());
                        resultItem.setType(item.getTypeName());

                        // get item total quantity
                        inventoryRepo.getInventoryTotalItemCountByTag(ResourceHelper.TAG_ITEM, item.getId(), new RepoInterface() {
                            @Override
                            public void activityDone(Boolean success, String message, Object object) {
                                if (success) {
                                    if ((int) object > 0) {
                                        resultItem.setQuantity((int) object);
                                    }
                                }
                            }
                        });
                        items.add(resultItem);
                    }
                    main_table.getItems().clear();
                    main_table.getItems().addAll(items);
                } else {
                    Platform.runLater(() -> {
                        AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.ERROR, "System Message", "Can't load Items", "An Error has occurred!\n" + message);
                    });
                }
            }
        });
    }


}

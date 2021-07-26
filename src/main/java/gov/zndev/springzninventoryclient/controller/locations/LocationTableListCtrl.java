package gov.zndev.springzninventoryclient.controller.locations;

import gov.zndev.springzninventoryclient.MainController;
import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.Logger;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Location;
import gov.zndev.springzninventoryclient.repository.inventory.InventoryRepository;
import gov.zndev.springzninventoryclient.repository.locations.LocationsRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
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
@FxmlView("location_table_list_layout.fxml")
public class LocationTableListCtrl implements Initializable {
    private static final String TAG = "LocationTableListCtrl";


    @FXML
    private TableView<Location> main_table;

    @FXML
    private TableColumn<Location, String> name_column;

    @FXML
    private TableColumn<Location, String> model_column;

    @FXML
    private TableColumn<Location, String> inventory_column;
    @FXML
    private TableColumn<Location, String> date_column;

    @FXML
    private TextField searchfield;

    @FXML
    private Pagination pagination;

    @FXML
    private Label total_inventory;

    @FXML
    private ComboBox<?> selectSort;

    @FXML
    private Button btn_createLocation;


    //===============================================================================

    @Autowired
    private LocationsRepository locationsRepo;

    @Autowired
    private InventoryRepository inventoryRepo;

    @Autowired
    private FxWeaver fxWeaver;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setup();
        setupTable();
        setupPagination();
        loadLocationsToTable(1, table_row_size);
    }

    private void setup() {
        // Total inventory
        locationsRepo.getLocationCount((success, message, object) -> {
            if (success)
                Platform.runLater(() -> {
                    total_inventory.setText("Locations: " + ((int) object));
                });

        });
    }

    private void setupTable() {
        this.main_table.setRowFactory(row -> new TableRow<Location>() {
            @Override
            public void updateItem(Location location, boolean empty) {
                super.updateItem(location, empty);

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


        main_table.setRowFactory(row -> new TableRow<Location>() {
            @Override
            protected void updateItem(Location location, boolean b) {
                super.updateItem(location, b);

                setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2 && !isEmpty()) {
                        openLocationView(getItem());
                    }
                });
            }
        });
        this.name_column.setCellValueFactory(new PropertyValueFactory<Location, String>("name"));

        this.date_column.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Location, String> c) {
                if (c.getValue() != null) {
                    return new SimpleStringProperty("" + c.getValue().getDateCreated());
                }
                return new SimpleStringProperty("<no data>");
            }
        });

        this.inventory_column.setCellValueFactory(new PropertyValueFactory<Location, String>("inventoryCount"));

    }

    private void loadLocationsToTable(int page, int size) {
        locationsRepo.getLocationsByPage(page, size, (success, message, object) -> {
            if (success) {
                Platform.runLater(() -> {
                    ObservableList<Location> list = FXCollections.observableList((List<Location>) object);
                    main_table.getItems().clear();

                    for (Location location : list) {
                        inventoryRepo.getInventoryCountByTag(ResourceHelper.TAG_LOCATION, location.getId(), new RepoInterface() {
                            @Override
                            public void activityDone(Boolean success, String message, Object object) {
                                if (success) {
                                   location.setInventoryCount((int) object);
                                } else {
                                    Logger.print(TAG, "loadLocationsToTable():getInventoryCountByLocation()", "Error while counting invetory on location\n" + message, null);
                                }
                            }
                        });
                    }
                    main_table.getItems().addAll(list);
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


    private int selected_page = 0;
    private int table_row_size = 30;
    private int sort_type = Helper.SORTTYPE_ASCENDING;

    private void setupPagination() {
        locationsRepo.getLocationCount((success, message, object) -> {
            if (success) {
                Platform.runLater(() -> {
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
                            loadLocationsToTable(selected_page + 1, table_row_size);
                        } else {
                            // Descending
                            loadLocationsToTable(selected_page + 1, table_row_size);
                        }
                    });
                });

            } else {
                System.out.println("Locations: setupPagination error: " + message);
            }
        });
    }


    private void openLocationView(Location location) {
        FxControllerAndView<ViewLocationCtrl, AnchorPane> viewLocationModal = fxWeaver.load(ViewLocationCtrl.class);


        Parent parent = viewLocationModal.getView().get();
        Stage stage = Helper.CreateStage("View Location - " + location.getName());
        stage.initOwner(MainController.getStage());

        Scene scene = new Scene(parent, 975, 545);
        stage.setScene(scene);

        stage.setResizable(false);

        ViewLocationCtrl ctrl = viewLocationModal.getController();

        ctrl.setStage(stage);
        ctrl.setLocation(location);

        stage.show();

    }

    @FXML
    void onCreateLocation(ActionEvent event) {
        openCreateLocationModal();
    }

    private void openCreateLocationModal() {
        try {
            FXMLLoader loader = Helper.CreateLoader(ResourceHelper.LAYOUT_LOCATION_MODAL_ADDLOCATION, getClass());
            Parent parent = loader.load();
            Stage stage = Helper.CreateStage("Create Location");
            stage.initOwner(MainController.getStage());

            Scene scene = new Scene(parent, 445, 348);
            stage.setScene(scene);

            stage.setResizable(false);

            ModalAddLocationCtrl ctrl = loader.getController();
            ctrl.setStage(stage);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

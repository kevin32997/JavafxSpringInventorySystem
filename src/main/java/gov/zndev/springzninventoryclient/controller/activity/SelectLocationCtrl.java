package gov.zndev.springzninventoryclient.controller.activity;

import com.jfoenix.controls.JFXDialog;
import gov.zndev.springzninventoryclient.MainController;
import gov.zndev.springzninventoryclient.controller.locations.ModalAddLocationCtrl;
import gov.zndev.springzninventoryclient.helpers.AlertDialogHelper;
import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.models.Location;
import gov.zndev.springzninventoryclient.repository.locations.LocationsRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
@FxmlView("activity_select_location_layout.fxml")
public class SelectLocationCtrl {

    @FXML
    private ListView<AnchorPane> location_list;

    @FXML
    private TextField search_location;
    @FXML
    private Pagination pagination;

    @Autowired
    private LocationsRepository locationsRepo;

    @Autowired
    private FxWeaver fxWeaver;


    private Stage stage;
    private Object parentCtrl;
    private JFXDialog dialog;

    @FXML
    public void initialize() {
        setupLocationList();
        setupSearchField();
        setupPagination();
        loadLocationsToTable(selected_page, table_row_size);
    }

    private void setupLocationList() {
        location_list.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                selectLocation(listLocations.get(location_list.getSelectionModel().getSelectedIndex()));
            }
        });
    }

    private void setupSearchField() {

    }


    private int selected_page = 1;
    private int table_row_size = 10;

    private void setupPagination() {
        locationsRepo.getLocationCount((success, message, object) -> {
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
                        loadLocationsToTable(selected_page, table_row_size);
                    });
                });

            } else {
                System.out.println("Brands: setupPagination error: " + message);
            }
        });
    }

    private List<Location> listLocations;

    private void loadLocationsToTable(int page, int size) {

        locationsRepo.getLocationsByPage(page, size, (success, message, object) -> {
            if (success) {
                Platform.runLater(() -> {
                    listLocations = (List<Location>) object;

                    location_list.getItems().clear();

                    for (Location location : listLocations) {
                        AnchorPane pane = fxWeaver.loadView(SelectLocationItemLayoutCtrl.class);
                        Label label = (Label) pane.lookup("#label");
                        Button btnSelect = (Button) pane.lookup("#btnSelect");

                        label.setText(location.getName());
                        btnSelect.setOnAction(e -> {
                            // Action
                            selectLocation(location);
                        });
                        location_list.getItems().add(pane);
                    }
                });
            } else {
                Platform.runLater(() -> {
                    AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.ERROR, "System Message", "Can't load Items", "An Error has occurred!\n" + message);
                });
            }
        });
    }

    public void selectLocation(Location location) {
        if (parentCtrl.getClass().equals(ImportInventoryCtrl.class)) {
            ImportInventoryCtrl ctrl = (ImportInventoryCtrl) parentCtrl;
            ctrl.setSelectedLocation(location);
        }
        if (dialog != null) {
            dialog.close();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setParentCtrl(Object parentCtrl) {
        this.parentCtrl = parentCtrl;
    }

    public void setDialog(JFXDialog dialog) {
        this.dialog = dialog;
    }

    @FXML
    void onAddLocation(ActionEvent event) {
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

}

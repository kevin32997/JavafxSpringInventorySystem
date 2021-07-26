package gov.zndev.springzninventoryclient.controller.types;

import com.jfoenix.controls.JFXButton;
import gov.zndev.springzninventoryclient.MainController;
import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Type;
import gov.zndev.springzninventoryclient.models.others.Sort;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import gov.zndev.springzninventoryclient.repository.types.TypesRepository;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
@FxmlView("type_table_list_layout.fxml")
public class TypesTableListCtrl implements Initializable {

    @FXML
    private TableView<Type> main_table;

    @FXML
    private TableColumn<Type, String> name_column;

    @FXML
    private TableColumn<Type, String> description_column;

    @FXML
    private TableColumn<Type, String> date_created_column;

    @FXML
    private TextField searchfield;


    @FXML
    private Pagination pagination;

    @FXML
    private JFXButton btn_createType;


    @FXML
    private ComboBox<String> selectSort;


    //====================================================================

    @Autowired
    private TypesRepository typesRepo;

    private Sort sort;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        setupPagination();
        setupBtnEvents();
        // Load Initial Data
        loadTypesToTable(1, table_row_size);
    }

    private void setupView() {
        // Initialize Sort
        sort = new Sort(Sort.ASCENDING, "name");

        selectSort.getItems().addAll("Name", "Date Created");
        selectSort.getSelectionModel().select(0);
        selectSort.setOnAction(e -> {
            switch (selectSort.getSelectionModel().getSelectedItem()) {
                case "Date Created":
                    sort.setSortBy("dateCreated");
                    loadTypesToTable(selected_page, table_row_size);
                    break;
                case "Name":
                    sort.setSortBy("name");
                    loadTypesToTable(selected_page, table_row_size);
                    break;
            }
        });
    }

    private void setupTable() {
        this.main_table.setRowFactory(row -> new TableRow<Type>() {
            @Override
            public void updateItem(Type type, boolean empty) {
                super.updateItem(type, empty);

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

        this.name_column.setCellValueFactory(new PropertyValueFactory<Type, String>("name"));

        this.description_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Type, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Type, String> c) {
                if (c.getValue() != null) {
                    return new SimpleStringProperty("" + c.getValue().getDescription());
                }
                return new SimpleStringProperty("<no data>");
            }
        });

        this.date_created_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Type, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Type, String> c) {
                if (c.getValue() != null) {
                    return new SimpleStringProperty("" + c.getValue().getDateCreated());
                }
                return new SimpleStringProperty("<no data>");
            }
        });


        main_table.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

            }
        });

        main_table.setRowFactory(row -> new TableRow<Type>() {
            @Override
            protected void updateItem(Type type, boolean b) {
                super.updateItem(type, b);

                setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2 && !isEmpty()) {
                        openTypeView(getItem());
                    }
                });
            }
        });
    }


    private void loadTypesToTable(int page, int size) {

        typesRepo.getTypesByPageSorted(page, size, sort, new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {
                    Platform.runLater(() -> {
                        main_table.getItems().clear();
                        main_table.getItems().addAll((List<Type>) object);
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

    private int selected_page = 1;
    private int table_row_size = 30;

    private void setupPagination() {
        typesRepo.getTypesCount((success, message, object) -> {
            if (success) {
                int item_count = (Integer) object;
                int page_count = item_count / table_row_size;
                if (item_count % table_row_size > 0) {
                    page_count++;
                }
                pagination.setCurrentPageIndex(selected_page-1);
                if (selected_page < 50) {
                    pagination.setMaxPageIndicatorCount(page_count);
                } else {
                    pagination.setMaxPageIndicatorCount(50);
                }
                pagination.setPageCount(page_count);
                pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                    pagination.setCurrentPageIndex(newIndex.intValue());
                    selected_page = newIndex.intValue()+1;
                    loadTypesToTable(selected_page, table_row_size);
                });
            } else {
                System.out.println("Brands: setupPagination error: " + message);
            }
        });
    }

    private void setupBtnEvents() {
        btn_createType.setOnAction(e -> {
            openCreateTypeModal();
        });

    }

    private void openTypeView(Type type) {
        try {
            FXMLLoader loader = Helper.CreateLoader(ResourceHelper.LAYOUT_TYPES_MODAL_VIEW_TYPE, getClass());
            Parent parent = loader.load();
            Stage stage = Helper.CreateStage("View TYPE - " + type.getName());
            stage.initOwner(MainController.getStage());
            Scene scene = new Scene(parent, 975, 545);
            stage.setScene(scene);
            stage.setResizable(false);

            ViewTypeCtrl ctrl = loader.getController();
            ctrl.setType(type);
            ctrl.setStage(stage);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void openCreateTypeModal() {
        try {
            FXMLLoader loader = Helper.CreateLoader(ResourceHelper.LAYOUT_TYPES_MODAL_ADD_TYPE, getClass());
            Parent parent = loader.load();
            Stage stage = new Stage();
            stage.initOwner(MainController.getStage());

            Scene scene = new Scene(parent, 445, 348);
            stage.setScene(scene);
            stage.setResizable(false);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void onBtnAscending(ActionEvent event) {
        sort.setSortType(Sort.ASCENDING);
        loadTypesToTable(selected_page,table_row_size);
    }

    @FXML
    void onBtnDescending(ActionEvent event) {
        sort.setSortType(Sort.DESCENDING);
        loadTypesToTable(selected_page,table_row_size);
    }

    /*
    private void openItemView(Item item) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(ResourceHelper.LAYOUT_ITEM_MODAL_VIEWITEM));
        try {
            Parent parent = loader.load();
            Scene scene = new Scene(parent, 1150, 690);

            Stage stage = Helper.CreateStage();
            stage.setResizable(false);

            stage.setTitle("VIEW ITEM - "+item.getName());

            stage.setScene(scene);

            ViewItemCtrl ctrl = loader.getController();
            ctrl.setItem(item);
            ctrl.setStage(stage);

            stage.initOwner(MainController.getStage());
            stage.initModality(Modality.NONE);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     */
}

package gov.zndev.springzninventoryclient.controller.brands;

import com.jfoenix.controls.JFXButton;

import gov.zndev.springzninventoryclient.MainController;
import gov.zndev.springzninventoryclient.controller.activity.SelectLocationCtrl;
import gov.zndev.springzninventoryclient.helpers.AlertDialogHelper;
import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Brand;
import gov.zndev.springzninventoryclient.models.others.Sort;
import gov.zndev.springzninventoryclient.repository.brands.BrandsRepository;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
@FxmlView("brand_table_list_layout.fxml")
public class BrandTableListCtrl implements Initializable {
    @FXML
    private TableView<Brand> main_table;

    @FXML
    private TableColumn<Brand, String> name_column;

    @FXML
    private TableColumn<Brand, String> description_column;

    @FXML
    private TableColumn<Brand, String> date_created_column;

    @FXML
    private TextField searchfield;

    @FXML
    private Pagination pagination;


    @FXML
    private JFXButton btn_createBrand;

    @FXML
    private ComboBox<String> selectSort;

    //===============================================================================

    @Autowired
    private BrandsRepository brandRepo;

    @Autowired
    private FxWeaver fxWeaver;

    private Sort sort;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupView();
        setupTable();
        setupPagination();
        loadBrandsToTable(selected_page, table_row_size);
        setupBtnEvents();
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
                    loadBrandsToTable(selected_page, table_row_size);
                    break;
                case "Name":
                    sort.setSortBy("name");
                    loadBrandsToTable(selected_page, table_row_size);
                    break;
            }
        });


    }

    private void setupTable() {
        this.main_table.setRowFactory(row -> new TableRow<Brand>() {
            @Override
            public void updateItem(Brand brand, boolean empty) {
                super.updateItem(brand, empty);

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
        main_table.setRowFactory(row -> new TableRow<Brand>() {
            @Override
            protected void updateItem(Brand brand, boolean b) {
                super.updateItem(brand, b);

                setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2 && !isEmpty()) {
                        openBrandView(getItem());
                    }
                });
            }
        });
        this.name_column.setCellValueFactory(new PropertyValueFactory<Brand, String>("name"));
        this.description_column.setCellValueFactory(new PropertyValueFactory<Brand, String>("description"));
        this.date_created_column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Brand, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Brand, String> c) {
                if (c.getValue() != null) {
                    return new SimpleStringProperty("" + c.getValue().getDateCreated());
                }
                return new SimpleStringProperty("<no data>");
            }
        });
    }

    private void loadBrandsToTable(int page, int size) {

        brandRepo.getBrandsByPageSorted(page, size, sort, (success, message, object) -> {
            if (success) {
                Platform.runLater(() -> {
                    main_table.getItems().clear();
                    main_table.getItems().addAll((List<Brand>) object);
                });
            } else {
                Platform.runLater(() -> {
                    AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.ERROR, "System Message", "Can't load Items", "An Error has occurred!\n" + message);
                });
            }
        });
    }

    private int selected_page = 1;
    private int table_row_size = 30;

    private void setupPagination() {
        brandRepo.getBrandsCount((success, message, object) -> {
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
                        loadBrandsToTable(selected_page, table_row_size);
                    });
                });

            } else {
                System.out.println("Brands: setupPagination error: " + message);
            }
        });
    }


    private void setupBtnEvents() {
        btn_createBrand.setOnAction(e -> {
            openCreateBrandModal();
        });

    }

    private void openBrandView(Brand brand) {

        FxControllerAndView<ViewBrandCtrl, AnchorPane> viewBrand = fxWeaver.load(ViewBrandCtrl.class);
        viewBrand.getController().setBrand(brand);

        Stage stage = Helper.CreateStage("View Brand - " + brand.getName());
        stage.setScene(new Scene(viewBrand.getView().get()));

        stage.initOwner(MainController.getStage());
        stage.setResizable(false);

        viewBrand.getController().setStage(stage);

        stage.show();


    }


    private void openCreateBrandModal() {
        try {
            FXMLLoader loader = Helper.CreateLoader(ResourceHelper.LAYOUT_BRAND_MODAL_ADDBRAND, getClass());
            Parent parent = loader.load();
            Stage stage = Helper.CreateStage("Create Brand");
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
        loadBrandsToTable(selected_page, table_row_size);
    }

    @FXML
    void onBtnDescending(ActionEvent event) {
        sort.setSortType(Sort.DESCENDING);
        loadBrandsToTable(selected_page, table_row_size);
    }
}

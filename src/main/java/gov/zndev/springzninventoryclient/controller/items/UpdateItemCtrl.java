package gov.zndev.springzninventoryclient.controller.items;

import com.jfoenix.controls.JFXButton;
import gov.zndev.springzninventoryclient.MainController;
import gov.zndev.springzninventoryclient.controller.brands.ModalAddBrandCtrl;
import gov.zndev.springzninventoryclient.controller.components.ProgressBarCtrl;
import gov.zndev.springzninventoryclient.controller.inventory.ViewInventoryCtrl;
import gov.zndev.springzninventoryclient.controller.types.ModalAddTypeCtrl;
import gov.zndev.springzninventoryclient.helpers.AlertDialogHelper;
import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.helpers.SearchResultListView;
import gov.zndev.springzninventoryclient.models.Brand;
import gov.zndev.springzninventoryclient.models.Inventory;
import gov.zndev.springzninventoryclient.models.Item;
import gov.zndev.springzninventoryclient.models.Type;
import gov.zndev.springzninventoryclient.repository.brands.BrandsRepository;
import gov.zndev.springzninventoryclient.repository.inventory.InventoryRepository;
import gov.zndev.springzninventoryclient.repository.items.ItemsRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import gov.zndev.springzninventoryclient.repository.types.TypesRepository;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
@FxmlView("item_modal_update_item_layout.fxml")
public class UpdateItemCtrl {

    @FXML
    private AnchorPane main_pane;

    @FXML
    private TextField name;

    @FXML
    private TextField brand;

    @FXML
    private TextField type;

    @FXML
    private TextArea description;

    @FXML
    private TextField model;


    //===================================================
// Main
    @Autowired
    private BrandsRepository brandsRepo;

    @Autowired
    private TypesRepository typesRepo;

    @Autowired
    private ItemsRepository itemsRepo;

    @Autowired
    private InventoryRepository inventoryRepo;

    @Autowired
    private FxWeaver fxWeaver;

    // Others
    private Stage stage;
    private Brand selected_brand;
    private Type selected_type;
    private Object parentCtrl;

    // ProgressDialog
    private Stage progressStage;
    private ProgressBarCtrl progressBarCtrl;

    private Item item;


    @FXML
    public void initialize() {
        setupBrandSearchEngine();
        setupTypeSearchEngine();
        setupProgressDialog();
    }

    public void setItem(Item item) {
        this.item = item;
        populateFields();
    }

    public void setParentController(Object parentCtrl) {
        this.parentCtrl = parentCtrl;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void populateFields() {
        this.name.setText(item.getName());

        brandsRepo.getBrandById(item.getBrandId(), (success, message, object) -> {
            if (success) {
                List<Brand> list = (List<Brand>) object;
                this.selected_brand = list.get(0);
                Platform.runLater(() -> {
                    this.brand.setText(selected_brand.getName());
                });
            }
        });

        typesRepo.getTypeById(item.getTypeId(), (success, message, object) -> {
            if (success) {
                List<Type> list = (List<Type>) object;
                this.selected_type = list.get(0);
                Platform.runLater(() -> {
                    this.type.setText(selected_type.getName());
                });
            }
        });

        this.model.setText(item.getModel());
        this.description.setText(item.getDescription());


    }

    private void setupProgressDialog() {
        Platform.runLater(() -> {
            progressStage = new Stage();
            progressStage.initStyle(StageStyle.UNDECORATED);
            FxControllerAndView<ProgressBarCtrl, AnchorPane> progressBarModal = fxWeaver.load(ProgressBarCtrl.class);
            AnchorPane progressDialogPane = progressBarModal.getView().get();
            Scene scene = new Scene(progressDialogPane, 350, 100);
            progressStage.setScene(scene);
            progressBarCtrl = progressBarModal.getController();
        });
    }

    private void setupBrandSearchEngine() {
        SearchResultListView brand_searchResult = new SearchResultListView(brand, 375, 27);
        brand.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.DOWN) {
                brand_searchResult.requestFocus();
                brand_searchResult.getSelectionModel().select(0);
                return;
            }
            if (!brand.getText().equals("")) {
                brandsRepo.searchBrand(brand.getText(), 10, new RepoInterface() {
                    @Override
                    public void activityDone(Boolean success, String message, Object object) {
                        if (success) {
                            List<Brand> brands = (List<Brand>) object;
                            brand_searchResult.setList(brands);
                            if (brands.size() > 0) {
                                List<String> stringList = new ArrayList<>();
                                for (Brand brand : brands) {
                                    stringList.add(brand.getName());
                                }
                                brand_searchResult.displayResult(stringList);
                            } else {
                                brand_searchResult.displayNoDataFound("No data found . . .");
                                brand_searchResult.getList().clear();
                            }
                            brand_searchResult.setVisible(true);
                        } else {
                            // Error code here
                            System.out.println("Error Occurred\n" + message);
                            brand_searchResult.getList().clear();
                        }
                    }
                });
            } else {
                brand_searchResult.setVisible(false);
                brand_searchResult.setList(new ArrayList<>());
            }
        });

        brand.setOnAction(e -> {
            if (brand_searchResult.getItems().size() > 0) {
                if (!brand_searchResult.getItems().get(0).equals("No data found . . .")) {
                    setSelectedBrand((Brand) brand_searchResult.getList().get(brand_searchResult.getSelectionModel().getSelectedIndex() + 1));
                    brand_searchResult.getList().clear();
                    brand_searchResult.getItems().clear();
                    brand_searchResult.setVisible(false);
                    type.requestFocus();
                } else {
                    // Error message here no data found // Create new Brand
                    System.out.println("No data found, Cant use enter");
                    createBrand(brand.getText());
                    brand_searchResult.getList().clear();
                    brand_searchResult.getItems().clear();
                    brand_searchResult.setVisible(false);
                }
            } else {
                System.out.println("List is empty");
                //
                createBrand(brand.getText());
            }
        });

        brand_searchResult.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                setSelectedBrand((Brand) brand_searchResult.getList().get(brand_searchResult.getSelectionModel().getSelectedIndex()));
                brand_searchResult.getList().clear();
                brand_searchResult.getItems().clear();
                brand_searchResult.setVisible(false);
                type.requestFocus();
            }
        });
        // Add search result to main anchor pane
        main_pane.getChildren().add(brand_searchResult);


        brand.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (!t1) {
                    if (!brand_searchResult.isFocused()) {
                        if (brand.getText().equals("") || brand_searchResult.getItems().get(0).equals("No data found . . .")) {
                            brand.setText(selected_brand.getName());
                        } else if (!brand_searchResult.getItems().get(0).equals("No data found . . .") && brand_searchResult.isVisible()) {
                            setSelectedBrand((Brand) brand_searchResult.getList().get(0));
                        }
                        if (brand_searchResult.isVisible()) {
                            brand_searchResult.setVisible(false);
                        }
                    }
                }
            }
        });

    }


    private void setupTypeSearchEngine() {

        SearchResultListView type_searchResult = new SearchResultListView(type, 375, 27);

        type.setOnKeyReleased(keyEvent -> {

            if (keyEvent.getCode() == KeyCode.DOWN) {
                type_searchResult.requestFocus();
                type_searchResult.getSelectionModel().select(0);
                return;
            }

            if (!type.getText().equals("")) {
                typesRepo.searchType(type.getText(), 10, new RepoInterface() {
                    @Override
                    public void activityDone(Boolean success, String message, Object object) {
                        if (success) {
                            List<Type> types = (List<Type>) object;
                            type_searchResult.setList(types);
                            if (types.size() > 0) {
                                List<String> stringList = new ArrayList<>();
                                for (Type type : types) {
                                    stringList.add(type.getName());
                                }
                                type_searchResult.displayResult(stringList);
                            } else {
                                type_searchResult.displayNoDataFound("No data found . . .");
                                type_searchResult.getList().clear();
                            }
                            type_searchResult.setVisible(true);
                        } else {
                            // Error code here
                            System.out.println("Error Occurred\n" + message);
                            type_searchResult.getList().clear();
                        }
                    }
                });
            } else {
                type_searchResult.setVisible(false);
                type_searchResult.setList(new ArrayList<>());
            }
        });

        type.setOnAction(e -> {
            if (type_searchResult.getItems().size() > 0) {
                if (!type_searchResult.getItems().get(0).equals("No data found . . .")) {
                    setSelectedType((Type) type_searchResult.getList().get(type_searchResult.getSelectionModel().getSelectedIndex() + 1));
                    type_searchResult.getList().clear();
                    type_searchResult.getItems().clear();
                    type_searchResult.setVisible(false);
                    model.requestFocus();
                } else {
                    // Error message here no data found // Create new Brand
                    System.out.println("No data found, Cant use enter");
                    createType(type.getText());
                    type_searchResult.getList().clear();
                    type_searchResult.getItems().clear();
                    type_searchResult.setVisible(false);
                }
            } else {
                System.out.println("List is empty");
                //

                createType(type.getText());
            }
        });

        type_searchResult.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                setSelectedType((Type) type_searchResult.getList().get(type_searchResult.getSelectionModel().getSelectedIndex()));
                type_searchResult.getList().clear();
                type_searchResult.getItems().clear();
                type_searchResult.setVisible(false);

                model.requestFocus();
            }
        });
        // Add search result to main anchor pane
        main_pane.getChildren().add(type_searchResult);

        type.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (!t1) {
                    if (!type_searchResult.isFocused()) {
                        if (type.getText().equals("") || type_searchResult.getItems().get(0).equals("No data found . . .") && type_searchResult.isVisible()) {
                            type.setText(selected_type.getName());
                        } else if (!type_searchResult.getItems().get(0).equals("No data found . . .") && type_searchResult.isVisible()) {
                            setSelectedType((Type) type_searchResult.getList().get(0));
                        }
                        if (type_searchResult.isVisible()) {
                            type_searchResult.setVisible(false);
                        }
                    }
                }
            }
        });
    }

    private void validateItemDetails() {
        //TODO

    }


    public void setSelectedBrand(Brand brand) {
        this.selected_brand = brand;
        this.brand.setText(brand.getName());
        System.out.println("Selected Brand ID is " + selected_brand.getId());
    }

    public void setSelectedType(Type type) {
        this.selected_type = type;
        this.type.setText(type.getName());
        System.out.println("Selected Type ID is " + selected_type.getId());
    }

    private void createBrand(String name) {
        FxControllerAndView<ModalAddBrandCtrl, AnchorPane> createBrandModal = fxWeaver.load(ModalAddBrandCtrl.class);
        createBrandModal.getController().setName(name);
        createBrandModal.getController().setParentCtrl(this);
        Stage stage = Helper.CreateStage("Create new Brand");
        createBrandModal.getController().setStage(stage);
        Scene scene = new Scene(createBrandModal.getView().get(), 445, 345);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void createType(String type) {
        FxControllerAndView<ModalAddTypeCtrl, AnchorPane> createTypeModal = fxWeaver.load(ModalAddTypeCtrl.class);
        createTypeModal.getController().setName(type);
        createTypeModal.getController().setParentCtrl(this);
        Stage stage = Helper.CreateStage("Create new Type");
        createTypeModal.getController().setStage(stage);
        Scene scene = new Scene(createTypeModal.getView().get(), 445, 345);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void updateItem() {
        if (validateFields()) {

            item.setName(name.getText());
            item.setBrandId(selected_brand.getId());
            item.setTypeId(selected_type.getId());
            item.setModel(model.getText());
            item.setDescription(description.getText());

            itemsRepo.updateItem(item, new RepoInterface() {
                @Override
                public void activityDone(Boolean success, String message, Object object) {
                    if (success) {
                        showItemUpdatedDialog();
                    }
                }
            });



        }
    }


    private void showItemUpdatedDialog() {
            Platform.runLater(() -> {
                AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.INFORMATION, "System Message", "Item Updated", null);
            });
    }

    private boolean validateFields() {
        if (name.getText().equals("")) {
            AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.ERROR, "System Message", "Please fill up missing fields!", null);
            return false;
        }
        return true;
    }


    @FXML
    void onCreateBrand(ActionEvent event) {
        createBrand("");
    }

    @FXML
    void onCreateType(ActionEvent event) {
        createType("");
    }

    @FXML
    void onUpdate(ActionEvent event) {
        updateItem();
    }

}


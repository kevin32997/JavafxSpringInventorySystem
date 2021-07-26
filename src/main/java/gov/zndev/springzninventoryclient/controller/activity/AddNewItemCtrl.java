package gov.zndev.springzninventoryclient.controller.activity;

import gov.zndev.springzninventoryclient.controller.brands.ModalAddBrandCtrl;
import gov.zndev.springzninventoryclient.controller.types.ModalAddTypeCtrl;
import gov.zndev.springzninventoryclient.helpers.AlertDialogHelper;
import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.helpers.SearchResultListView;
import gov.zndev.springzninventoryclient.models.Brand;
import gov.zndev.springzninventoryclient.models.Item;
import gov.zndev.springzninventoryclient.models.Type;
import gov.zndev.springzninventoryclient.models.others.ItemPick;
import gov.zndev.springzninventoryclient.repository.brands.BrandsRepository;
import gov.zndev.springzninventoryclient.repository.items.ItemsRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import gov.zndev.springzninventoryclient.repository.types.TypesRepository;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@FxmlView("activity_add_new_item_layout.fxml")
public class AddNewItemCtrl {

    @FXML
    private AnchorPane main_pane;

    @FXML
    private TextField name;

    @FXML
    private TextField brand;

    @FXML
    private TextField type;

    @FXML
    private TextField model;

    @FXML
    private TextArea description;

    @FXML
    private TextField code;

    @FXML
    private TextField serial_number;

    @FXML
    private TextField property_number;

    /////////////////////////////////////////////////////

    @Autowired
    private ItemsRepository itemsRepo;

    @Autowired
    private BrandsRepository brandsRepo;

    @Autowired
    private TypesRepository typesRepo;

    @Autowired
    private FxWeaver fxWeaver;

    private ImportInventoryCtrl parentCtrl;

    private Brand selected_brand;
    private Type selected_type;

    private Stage stage;

    @FXML
    public void initialize() {
        setupView();
        setupBrandSearchEngine();
        setupTypeSearchEngine();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setName(String name){
        this.name.setText(name);
    }

    public void setParentCtrl(ImportInventoryCtrl parentCtrl) {
        this.parentCtrl = parentCtrl;
    }

    private void setupView() {
        name.requestFocus();

        name.setOnAction(e -> {

            brand.requestFocus();

        });

        model.setOnAction(e -> {
            description.requestFocus();
        });

        code.setOnAction(e -> {
            serial_number.requestFocus();
        });

        serial_number.setOnAction(e -> {
            property_number.requestFocus();
        });

        property_number.setOnAction(e -> {
            saveAndAdd();
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
                this.selected_brand = (Brand) brand_searchResult.getList().get(brand_searchResult.getSelectionModel().getSelectedIndex());
                brand.setText(selected_brand.getName());
                brand_searchResult.getList().clear();
                brand_searchResult.getItems().clear();
                brand_searchResult.setVisible(false);
                type.requestFocus();
            }
        });


        brand.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (!t1) {
                    if (!brand_searchResult.isFocused()) {
                        brand_searchResult.getList().clear();
                        brand_searchResult.getItems().clear();
                        brand_searchResult.setVisible(false);
                    }
                }
            }
        });

        // Add search result to main anchor pane
        main_pane.getChildren().add(brand_searchResult);
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


        type.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (!t1) {
                    if (!type_searchResult.isFocused()) {
                        type_searchResult.getList().clear();
                        type_searchResult.getItems().clear();
                        type_searchResult.setVisible(false);

                    }
                }
            }
        });

        // Add search result to main anchor pane
        main_pane.getChildren().add(type_searchResult);
    }

    private void saveAndAdd() {
        if (validate()) {
            Item item = new Item();
            item.setName(name.getText());
            item.setBrandId(selected_brand.getId());
            item.setBrandName(selected_brand.getName());
            item.setTypeId(selected_type.getId());
            item.setTypeName(selected_type.getName());
            item.setModel(model.getText());
            item.setCreatedBy(ResourceHelper.MAIN_USER.getId());
            item.setDescription(description.getText());

            itemsRepo.createItem(item, (success, message, object) -> {
                if (success) {

                    // Add Item To Table
                    List<Item> items = (List<Item>) object;
                    Item newItem = items.get(0);
                    ItemPick itemToAdd = new ItemPick();
                    itemToAdd.setItemId(newItem.getId());
                    itemToAdd.setName(newItem.getName());
                    itemToAdd.setBrand(newItem.getBrandName());
                    itemToAdd.setType(newItem.getTypeName());
                    itemToAdd.setCode(code.getText());
                    itemToAdd.setSerial(serial_number.getText());
                    itemToAdd.setPropertyNumber(property_number.getText());
                    Platform.runLater(() -> {
                        AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.INFORMATION, "System Info", "Item Saved", "New item successfully saved to database.");
                        stage.close();

                        parentCtrl.addItemToTable(itemToAdd);
                    });
                } else {

                    // Show Error While Saving
                }
            });

        }
    }


    private boolean validate() {

        return true;
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


    @FXML
    void onSaveAndAdd(ActionEvent event) {
        saveAndAdd();
    }

}

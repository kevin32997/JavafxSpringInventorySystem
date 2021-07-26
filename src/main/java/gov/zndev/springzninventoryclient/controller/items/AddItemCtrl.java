package gov.zndev.springzninventoryclient.controller.items;

import com.jfoenix.controls.JFXButton;
import gov.zndev.springzninventoryclient.controller.brands.ModalAddBrandCtrl;
import gov.zndev.springzninventoryclient.controller.components.ProgressBarCtrl;
import gov.zndev.springzninventoryclient.controller.types.ModalAddTypeCtrl;
import gov.zndev.springzninventoryclient.helpers.AlertDialogHelper;
import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.helpers.SearchResultListView;
import gov.zndev.springzninventoryclient.models.Brand;
import gov.zndev.springzninventoryclient.models.Item;
import gov.zndev.springzninventoryclient.models.Type;
import gov.zndev.springzninventoryclient.repository.brands.BrandsRepository;
import gov.zndev.springzninventoryclient.repository.items.ItemsRepository;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import gov.zndev.springzninventoryclient.repository.types.TypesRepository;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


@Component
@FxmlView("item_add_item_layout.fxml")
public class AddItemCtrl implements Initializable {


    @FXML
    private ScrollPane scroll_pane;

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
    private JFXButton btn_save;

    @FXML
    private TextField model;


    ///////////////////////////////////////

    // Main
    @Autowired
    private BrandsRepository brandsRepo;

    @Autowired
    private TypesRepository typesRepo;

    @Autowired
    private ItemsRepository itemsRepo;

    @Autowired
    private FxWeaver fxWeaver;

    // Others
    private Brand selected_brand;
    private Type selected_type;
    private File image_file;

    // ProgressDialog
    private Stage progressStage;
    private ProgressBarCtrl progressBarCtrl;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupView();
        setupBrandSearchEngine();
        setupTypeSearchEngine();
        setupProgressDialog();
    }

    private void setupView() {

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
                this.selected_brand = (Brand) brand_searchResult.getList().get(brand_searchResult.getSelectionModel().getSelectedIndex());
                brand.setText(selected_brand.getName());
                brand_searchResult.getList().clear();
                brand_searchResult.getItems().clear();
                brand_searchResult.setVisible(false);
                type.requestFocus();
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
        // Add search result to main anchor pane
        main_pane.getChildren().add(type_searchResult);
    }

    private void validateItemDetails() {
        //TODO
        saveItem();
    }


    private void saveItem() {
        // Create Progress bar
        progressBarCtrl.getProgress_label().setText("Saving item . . .");
        progressStage.show();
        // Save Item details
        Item item = new Item();

        item.setName(name.getText());
        item.setBrandId(selected_brand.getId());
        item.setTypeId(selected_type.getId());
        item.setModel(model.getText());
        item.setDescription(description.getText());
        item.setCreatedBy(ResourceHelper.MAIN_USER.getId());

        itemsRepo.createItem(item, (success, message, object) -> {
            if (success) {
                // update item Image
                List<Item> items = (List<Item>) object;
                Item savedItem = items.get(0);
                if (image_file != null) {
                    updateItemImage(savedItem.getId());
                } else {
                    Platform.runLater(() -> {
                        progressStage.hide();
                        AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.INFORMATION, "System Message", "Item Saved!", "Item was successfully added to list.");
                        this.clear();
                    });
                }
            } else {
                progressStage.hide();
                AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.ERROR, "System Message", "Can't save item", "Error occurred while saving\nError: " + message);
            }
        });
    }

    private void updateItemImage(int itemId) {
        Platform.runLater(() -> {
            progressBarCtrl.getProgress_label().setText("Uploading image . . .");
        });
        itemsRepo.updateItemImage(itemId, this.image_file, new RepoInterface() {
            @Override
            public void activityDone(Boolean success, String message, Object object) {
                if (success) {
                    Platform.runLater(() -> {
                        progressStage.hide();
                        AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.INFORMATION, "System Message", "Item Saved!", "Item was successfully added to list.");
                    });
                } else {
                    Platform.runLater(() -> {
                        progressStage.hide();
                        Optional<ButtonType> result = AlertDialogHelper
                                .ShowConfirmationAlertDialog("System Message", "Item Saved. Image not uploaded", "Click ok to re-upload image file.\nError: " + message)
                                .showAndWait();
                        if (result.get() == ButtonType.OK) {
                            saveItem();
                        }
                    });
                }
            }
        });
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

    private void clear() {
        this.name.clear();
        this.brand.clear();
        this.selected_brand = null;
        this.selected_type = null;
        this.model.clear();
        this.description.clear();

    }

    @FXML
    void onSave(ActionEvent event) {
        Optional<ButtonType> result = AlertDialogHelper.ShowConfirmationAlertDialog("System Message", "Save Item?", null).showAndWait();
        if (result.get() == ButtonType.OK) {
            saveItem();
        }
    }

    @FXML
    void onCreateBrand(ActionEvent event) {
        createBrand("");
    }

    @FXML
    void onCreateType(ActionEvent event) {
        createType("");
    }

        /*
    @FXML
    void onBrowse(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(MainController.getStage());

        try {
            String mimetype = Files.probeContentType(selectedFile.toPath());
            String type = mimetype.split("/")[0];
            if (type.equals("image")) {
                Image img = new Image(selectedFile.toURI().toString());
                image.setImage(img);
                image_url.setText(selectedFile.getAbsolutePath());
                image_file = selectedFile;
            } else {
                // Alert Here
                AlertDialogHelper.ShowSimpleAlertDialog(Alert.AlertType.ERROR, "System Message", "Selected file is not an Image", "Please choose .jpg/.png files only");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     */
}

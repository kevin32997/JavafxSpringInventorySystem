package gov.zndev.springzninventoryclient.controller.components;

import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("side_bar_layout.fxml")
public class SideBarCtrl implements Initializable {
    @FXML
    private JFXButton btnDashboard;


    @FXML
    private JFXButton btnItems;

    @FXML
    private VBox items_child_vbox;

    @FXML
    private JFXButton btnViewItems;

    @FXML
    private JFXButton btnCreateItem;

    @FXML
    private JFXButton btnBrands;

    @FXML
    private JFXButton btnType;

    @FXML
    private JFXButton btnLocations;



    @FXML
    private JFXButton btnActivity;

    @FXML
    private VBox inventory_child_vbox;

    @FXML
    private JFXButton btnImport;

    @FXML
    private JFXButton btnExport;

    @FXML
    private JFXButton btnActivities;

    @FXML
    private JFXButton btnUsers;

    @FXML
    private VBox user_child_vbox;

    @FXML
    private JFXButton btnUserList;

    @FXML
    private JFXButton btnCreateUser;

    @FXML
    private JFXButton btnReport;

    @FXML
    private JFXButton btnInventory;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupSideBar();
    }

    private void setupSideBar() {
        // Items
        btnItems.setOnAction(e -> {
            if (isItemsChildrenOpen) {
                closeItemsPane();
            } else {
                openItemsPane();
            }
        });

        // Inventory
        btnActivity.setOnAction(e -> {
            if (isInventoryChildrenOpen) {
                closeInventoryPane();
            } else {
                openInventoryPane();
            }
        });

        // Users
        btnUsers.setOnAction(e -> {
            if (isUserChildrenOpen) {
                closeUserPane();
            } else {
                openUserPane();
            }
        });
    }

    /*========================================================================================
                                              ITEMS
    ========================================================================================*/
    private boolean isItemsChildrenOpen = false;

    private void openItemsPane() {
        KeyValue maxH_vbox = new KeyValue(items_child_vbox.prefHeightProperty(), 85);
        KeyFrame frame_vbox = new KeyFrame(Duration.millis(100), maxH_vbox);

        Timeline tl_vbox = new Timeline();
        tl_vbox.setCycleCount(1);
        tl_vbox.setAutoReverse(false);
        tl_vbox.getKeyFrames().addAll(frame_vbox);


        /////////////////////
        KeyValue opBtn1 = new KeyValue(btnViewItems.opacityProperty(), 1);
        KeyValue opBtn2 = new KeyValue(btnCreateItem.opacityProperty(), 1);
        KeyFrame frame_opBtns = new KeyFrame(Duration.millis(200), opBtn1, opBtn2);

        Timeline tl_btnOp = new Timeline();
        tl_btnOp.setCycleCount(1);
        tl_btnOp.setAutoReverse(false);
        tl_btnOp.getKeyFrames().addAll(frame_opBtns);


        tl_vbox.play();
        tl_vbox.setOnFinished(e -> {
            tl_btnOp.play();
        });
        isItemsChildrenOpen = true;
    }

    private void closeItemsPane() {
        KeyValue maxH_vbox = new KeyValue(items_child_vbox.prefHeightProperty(), 0);
        KeyFrame frame_vbox = new KeyFrame(Duration.millis(100), maxH_vbox);

        Timeline tl_vbox = new Timeline();
        tl_vbox.setCycleCount(1);
        tl_vbox.setAutoReverse(false);
        tl_vbox.getKeyFrames().addAll(frame_vbox);


        /////////////////////
        KeyValue opBtn1 = new KeyValue(btnViewItems.opacityProperty(), 0);
        KeyValue opBtn2 = new KeyValue(btnCreateItem.opacityProperty(), 0);
        KeyFrame frame_opBtns = new KeyFrame(Duration.millis(200), opBtn1, opBtn2);

        Timeline tl_btnOp = new Timeline();
        tl_btnOp.setCycleCount(1);
        tl_btnOp.setAutoReverse(false);
        tl_btnOp.getKeyFrames().addAll(frame_opBtns);
        tl_btnOp.setOnFinished(e -> {
            tl_vbox.play();
        });

        tl_btnOp.play();
        isItemsChildrenOpen = false;
    }

    /*========================================================================================
                                          INVENTORY
    ========================================================================================*/
    private boolean isInventoryChildrenOpen = false;

    private void openInventoryPane() {
        KeyValue maxH_vbox = new KeyValue(inventory_child_vbox.prefHeightProperty(), 130);
        KeyFrame frame_vbox = new KeyFrame(Duration.millis(100), maxH_vbox);

        Timeline tl_vbox = new Timeline();
        tl_vbox.setCycleCount(1);
        tl_vbox.setAutoReverse(false);
        tl_vbox.getKeyFrames().addAll(frame_vbox);


        /////////////////////
        KeyValue opBtn1 = new KeyValue(btnImport.opacityProperty(), 1);
        KeyValue opBtn2 = new KeyValue(btnExport.opacityProperty(), 1);
        KeyValue opBtn3 = new KeyValue(btnActivities.opacityProperty(), 1);
        KeyFrame frame_opBtns = new KeyFrame(Duration.millis(200), opBtn1, opBtn2,opBtn3);

        Timeline tl_btnOp = new Timeline();
        tl_btnOp.setCycleCount(1);
        tl_btnOp.setAutoReverse(false);
        tl_btnOp.getKeyFrames().addAll(frame_opBtns);


        tl_vbox.play();
        tl_vbox.setOnFinished(e -> {
            tl_btnOp.play();
        });
        isInventoryChildrenOpen = true;
    }

    private void closeInventoryPane() {
        KeyValue maxH_vbox = new KeyValue(inventory_child_vbox.prefHeightProperty(), 0);
        KeyFrame frame_vbox = new KeyFrame(Duration.millis(100), maxH_vbox);

        Timeline tl_vbox = new Timeline();
        tl_vbox.setCycleCount(1);
        tl_vbox.setAutoReverse(false);
        tl_vbox.getKeyFrames().addAll(frame_vbox);


        /////////////////////
        KeyValue opBtn1 = new KeyValue(btnImport.opacityProperty(), 0);
        KeyValue opBtn2 = new KeyValue(btnExport.opacityProperty(), 0);
        KeyValue opBtn3 = new KeyValue(btnActivities.opacityProperty(), 0);
        KeyFrame frame_opBtns = new KeyFrame(Duration.millis(200), opBtn1, opBtn2,opBtn3);

        Timeline tl_btnOp = new Timeline();
        tl_btnOp.setCycleCount(1);
        tl_btnOp.setAutoReverse(false);
        tl_btnOp.getKeyFrames().addAll(frame_opBtns);
        tl_btnOp.setOnFinished(e -> {
            tl_vbox.play();
        });

        tl_btnOp.play();
        isInventoryChildrenOpen = false;
    }

    /*========================================================================================
                                            USERS
    ========================================================================================*/
    private boolean isUserChildrenOpen = false;

    private void openUserPane() {
        KeyValue maxH_vbox = new KeyValue(user_child_vbox.prefHeightProperty(), 85);
        KeyFrame frame_vbox = new KeyFrame(Duration.millis(100), maxH_vbox);

        Timeline tl_vbox = new Timeline();
        tl_vbox.setCycleCount(1);
        tl_vbox.setAutoReverse(false);
        tl_vbox.getKeyFrames().addAll(frame_vbox);


        /////////////////////
        KeyValue opBtn1 = new KeyValue(btnCreateUser.opacityProperty(), 1);
        KeyValue opBtn2 = new KeyValue(btnUserList.opacityProperty(), 1);
        KeyFrame frame_opBtns = new KeyFrame(Duration.millis(200), opBtn1, opBtn2);

        Timeline tl_btnOp = new Timeline();
        tl_btnOp.setCycleCount(1);
        tl_btnOp.setAutoReverse(false);
        tl_btnOp.getKeyFrames().addAll(frame_opBtns);


        tl_vbox.play();
        tl_vbox.setOnFinished(e -> {
            tl_btnOp.play();
        });
        isUserChildrenOpen = true;
    }

    private void closeUserPane() {
        KeyValue maxH_vbox = new KeyValue(user_child_vbox.prefHeightProperty(), 0);
        KeyFrame frame_vbox = new KeyFrame(Duration.millis(100), maxH_vbox);

        Timeline tl_vbox = new Timeline();
        tl_vbox.setCycleCount(1);
        tl_vbox.setAutoReverse(false);
        tl_vbox.getKeyFrames().addAll(frame_vbox);


        /////////////////////
        KeyValue opBtn1 = new KeyValue(btnCreateUser.opacityProperty(), 0);
        KeyValue opBtn2 = new KeyValue(btnUserList.opacityProperty(), 0);
        KeyFrame frame_opBtns = new KeyFrame(Duration.millis(200), opBtn1, opBtn2);

        Timeline tl_btnOp = new Timeline();
        tl_btnOp.setCycleCount(1);
        tl_btnOp.setAutoReverse(false);
        tl_btnOp.getKeyFrames().addAll(frame_opBtns);
        tl_btnOp.setOnFinished(e -> {
            tl_vbox.play();
        });

        tl_btnOp.play();
        isUserChildrenOpen = false;
    }

    //=========================== Helpers ==================================


    public JFXButton getBtnDashboard() {
        return btnDashboard;
    }

    public void setBtnDashboard(JFXButton btnDashboard) {
        this.btnDashboard = btnDashboard;
    }

    public JFXButton getBtnItems() {
        return btnItems;
    }

    public void setBtnItems(JFXButton btnItems) {
        this.btnItems = btnItems;
    }

    public VBox getItems_child_vbox() {
        return items_child_vbox;
    }

    public void setItems_child_vbox(VBox items_child_vbox) {
        this.items_child_vbox = items_child_vbox;
    }

    public JFXButton getBtnViewItems() {
        return btnViewItems;
    }

    public void setBtnViewItems(JFXButton btnViewItems) {
        this.btnViewItems = btnViewItems;
    }

    public JFXButton getBtnCreateItem() {
        return btnCreateItem;
    }

    public void setBtnCreateItem(JFXButton btnCreateItem) {
        this.btnCreateItem = btnCreateItem;
    }

    public JFXButton getBtnBrands() {
        return btnBrands;
    }

    public void setBtnBrands(JFXButton btnBrands) {
        this.btnBrands = btnBrands;
    }

    public JFXButton getBtnType() {
        return btnType;
    }

    public void setBtnType(JFXButton btnType) {
        this.btnType = btnType;
    }

    public JFXButton getBtnLocations() {
        return btnLocations;
    }

    public void setBtnLocations(JFXButton btnLocations) {
        this.btnLocations = btnLocations;
    }

    public JFXButton getBtnActivity() {
        return btnActivity;
    }

    public void setBtnActivity(JFXButton btnActivity) {
        this.btnActivity = btnActivity;
    }

    public VBox getInventory_child_vbox() {
        return inventory_child_vbox;
    }

    public void setInventory_child_vbox(VBox inventory_child_vbox) {
        this.inventory_child_vbox = inventory_child_vbox;
    }

    public JFXButton getBtnImport() {
        return btnImport;
    }

    public void setBtnImport(JFXButton btnImport) {
        this.btnImport = btnImport;
    }

    public JFXButton getBtnExport() {
        return btnExport;
    }

    public void setBtnExport(JFXButton btnExport) {
        this.btnExport = btnExport;
    }

    public JFXButton getBtnActivities() {
        return btnActivities;
    }

    public void setBtnActivities(JFXButton btnActivities) {
        this.btnActivities = btnActivities;
    }

    public JFXButton getBtnUsers() {
        return btnUsers;
    }

    public void setBtnUsers(JFXButton btnUsers) {
        this.btnUsers = btnUsers;
    }

    public VBox getUser_child_vbox() {
        return user_child_vbox;
    }

    public void setUser_child_vbox(VBox user_child_vbox) {
        this.user_child_vbox = user_child_vbox;
    }

    public JFXButton getBtnUserList() {
        return btnUserList;
    }

    public void setBtnUserList(JFXButton btnUserList) {
        this.btnUserList = btnUserList;
    }

    public JFXButton getBtnCreateUser() {
        return btnCreateUser;
    }

    public void setBtnCreateUser(JFXButton btnCreateUser) {
        this.btnCreateUser = btnCreateUser;
    }

    public JFXButton getBtnReport() {
        return btnReport;
    }

    public void setBtnReport(JFXButton btnReport) {
        this.btnReport = btnReport;
    }

    public boolean isItemsChildrenOpen() {
        return isItemsChildrenOpen;
    }

    public void setItemsChildrenOpen(boolean itemsChildrenOpen) {
        isItemsChildrenOpen = itemsChildrenOpen;
    }

    public boolean isInventoryChildrenOpen() {
        return isInventoryChildrenOpen;
    }

    public void setInventoryChildrenOpen(boolean inventoryChildrenOpen) {
        isInventoryChildrenOpen = inventoryChildrenOpen;
    }

    public boolean isUserChildrenOpen() {
        return isUserChildrenOpen;
    }

    public void setUserChildrenOpen(boolean userChildrenOpen) {
        isUserChildrenOpen = userChildrenOpen;
    }

    public JFXButton getBtnInventory() {
        return btnInventory;
    }

    public void setBtnInventory(JFXButton btnInventory) {
        this.btnInventory = btnInventory;
    }
}

package gov.zndev.springzninventoryclient;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import gov.zndev.springzninventoryclient.controller.brands.BrandTableListCtrl;
import gov.zndev.springzninventoryclient.controller.components.LoadingPaneCtrl;
import gov.zndev.springzninventoryclient.controller.components.SideBarCtrl;
import gov.zndev.springzninventoryclient.controller.dashboard.DashboardCtrl;
import gov.zndev.springzninventoryclient.controller.activity.ExportInventoryCtrl;
import gov.zndev.springzninventoryclient.controller.activity.ImportInventoryCtrl;
import gov.zndev.springzninventoryclient.controller.inventory.InventoryTableListCtrl;
import gov.zndev.springzninventoryclient.controller.items.AddItemCtrl;
import gov.zndev.springzninventoryclient.controller.items.ItemTableListCtrl;
import gov.zndev.springzninventoryclient.controller.locations.LocationTableListCtrl;
import gov.zndev.springzninventoryclient.controller.types.TypesTableListCtrl;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.User;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@FxmlView("controller/components/main_layout.fxml")
public class MainController {

    @FXML
    private AnchorPane main_anchorpane;

    @FXML
    private BorderPane border_pane;

    @FXML
    private AnchorPane display_pane;

    private static Stage stage;
    private JFXHamburger burger;

    @Autowired
    private FxWeaver fxWeaver;

    @FXML
    public void initialize() {
        setupSidebar();
        setupBurger();
        setMainDisplay(DashboardCtrl.class);
    }

    public void setUser(User user) {
        ResourceHelper.MAIN_USER=user;
        //main_user_label.setText(user.getFullname());
        //checkUserAuth(user);
    }

    private boolean isSideBarOpen = true;

    private void setupBurger() {
        burger = new JFXHamburger();

        HamburgerNextArrowBasicTransition task = new HamburgerNextArrowBasicTransition(burger);
        task.setRate(-1);
        burger.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            task.setRate(task.getRate() * -1);
            task.play();
            if (isSideBarOpen) {
                closeSideBar();
            } else {
                openSideBar();
            }
        });
    }

    private AnchorPane sidebar;

    private void setupSidebar() {

        FxControllerAndView<SideBarCtrl, AnchorPane> sidebarClass = fxWeaver.load(SideBarCtrl.class);

        this.sidebar = sidebarClass.getView().get();

        // Setup Sidebar Button Events
        SideBarCtrl ctrl = sidebarClass.getController();

        // Dashboard
        ctrl.getBtnDashboard().setOnAction(e -> {
            setMainDisplay(DashboardCtrl.class);
            System.out.println("I was clicked");
        });

        // INVENTORY
        ctrl.getBtnInventory().setOnAction(e -> {
            setMainDisplay(InventoryTableListCtrl.class);

        });

        // View Items
        ctrl.getBtnViewItems().setOnAction(e -> {
            setMainDisplay(ItemTableListCtrl.class);
        });

        // Create Item
        ctrl.getBtnCreateItem().setOnAction(e -> {
            setMainDisplay(AddItemCtrl.class);
        });

        // View Brands
        ctrl.getBtnBrands().setOnAction(e -> {
            setMainDisplay(BrandTableListCtrl.class);
        });


        // View Types
        ctrl.getBtnType().setOnAction(e -> {
            setMainDisplay(TypesTableListCtrl.class);
        });

        // View Location
        ctrl.getBtnLocations().setOnAction(e -> {
            setMainDisplay(LocationTableListCtrl.class);
        });

        // Import Item
        ctrl.getBtnImport().setOnAction(e -> {
            setMainDisplay(ImportInventoryCtrl.class);
        });

        // Export Item
        ctrl.getBtnExport().setOnAction(e -> {
            setMainDisplay(ExportInventoryCtrl.class);
        });

        // Reporting
        ctrl.getBtnReport().setOnAction(e -> {
        });


        border_pane.setLeft(sidebar);
    }

    private void openSideBar() {
        sidebar.setVisible(true);
        KeyValue value_op_sidebar = new KeyValue(sidebar.opacityProperty(), 1);
        KeyFrame frame_op_sidebar = new KeyFrame(Duration.millis(180), value_op_sidebar);

        KeyValue value_w_sidebar = new KeyValue(sidebar.prefWidthProperty(), 215);
        KeyFrame frame_w_sidebar = new KeyFrame(Duration.millis(200), value_w_sidebar);

        Timeline tl_sidebar = new Timeline();
        tl_sidebar.setCycleCount(1);
        tl_sidebar.setAutoReverse(false);
        tl_sidebar.getKeyFrames().addAll(frame_w_sidebar, frame_op_sidebar);
        tl_sidebar.play();
        tl_sidebar.setOnFinished(e -> {
            isSideBarOpen = true;
        });
    }

    private void closeSideBar() {


        /*
        KeyFrame frame_nodes=new KeyFrame(Duration.millis(50));
        for(Node node:sidebar.getChildren()){

            if(!(node instanceof VBox)) {
                KeyValue value_op_node = new KeyValue(node.opacityProperty(), 0);

                try {
                    frame_nodes.getValues().add(value_op_node);
                } catch (UnsupportedOperationException ex) {
                    System.out.println("Exception: Node: " + node.toString());
                }
            }else{
                for(Node subNode:((VBox) node).getChildren()){
                    KeyValue value_op_node = new KeyValue(subNode.opacityProperty(), 0);
                    try {
                        frame_nodes.getValues().add(value_op_node);
                    } catch (UnsupportedOperationException ex) {
                        System.out.println("Exception: Node: " + node.toString());
                    }
                }
            }
        }

        Timeline tl_nodes=new Timeline();
        tl_nodes.setCycleCount(1);
        tl_nodes.setAutoReverse(false);
        tl_nodes.getKeyFrames().addAll(frame_nodes);
        tl_nodes.play();

         */


        KeyValue value_op_sidebar = new KeyValue(sidebar.opacityProperty(), 0);
        KeyFrame frame_op_sidebar = new KeyFrame(Duration.millis(50), value_op_sidebar);

        KeyValue value_w_sidebar = new KeyValue(sidebar.prefWidthProperty(), 0);
        KeyFrame frame_w_sidebar = new KeyFrame(Duration.millis(200), value_w_sidebar);

        Timeline tl_sidebar = new Timeline();
        tl_sidebar.setCycleCount(1);
        tl_sidebar.setAutoReverse(false);
        tl_sidebar.getKeyFrames().addAll(frame_w_sidebar, frame_op_sidebar);
        tl_sidebar.play();
        tl_sidebar.setOnFinished(e -> {
            isSideBarOpen = false;
            sidebar.setVisible(false);
        });
    }

    /*


    private void setMainDisplay(String src) {


        new Thread(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(ResourceHelper.LAYOUT_COMPONENT_LOADINGPANE));
                AnchorPane loading_pane = loader.load();
                Platform.runLater(() -> {
                    loading_pane.getChildren().add(burger);
                    loading_pane.setTopAnchor(burger, 10.0);
                    loading_pane.setLeftAnchor(burger, 10.0);
                    border_pane.setCenter(loading_pane);
                });

                loader = new FXMLLoader(getClass().getClassLoader().getResource(src));
                AnchorPane parent = loader.load();
                Thread.sleep(500);
                Platform.runLater(() -> {
                    parent.getChildren().add(burger);
                    parent.setTopAnchor(burger, 10.0);
                    parent.setLeftAnchor(burger, 10.0);
                    border_pane.setCenter(parent);
                });
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Cause: " + src, ex);

            }
        }).start();

            /*
            System.out.println("Children Count "+display_pane.getChildren().size());


            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(src));
            AnchorPane parent = loader.load();

            Platform.runLater(()->{
                display_pane.getChildren().removeAll();

                this.display_pane.getChildren().add(parent);
                this.display_pane.setTopAnchor(parent,0.0);
                this.display_pane.setBottomAnchor(parent,0.0);
                this.display_pane.setLeftAnchor(parent,0.0);
                this.display_pane.setRightAnchor(parent,0.0);
            });

            System.out.println("Children Count "+display_pane.getChildren().size());
            for(Node node:display_pane.getChildren()){
                System.out.println("Node name "+node.toString());
            }

    }
      */

    private void setMainDisplay(Class<?> c) {

        new Thread(() -> {
            try {
                AnchorPane loading_pane = fxWeaver.loadView(LoadingPaneCtrl.class);
                Platform.runLater(() -> {
                    loading_pane.getChildren().add(burger);
                    loading_pane.setTopAnchor(burger, 10.0);
                    loading_pane.setLeftAnchor(burger, 10.0);
                    border_pane.setCenter(loading_pane);
                });

                Thread.sleep(300);

                AnchorPane main_pane = fxWeaver.loadView(c);
                Platform.runLater(() -> {
                    main_pane.getChildren().add(burger);
                    main_pane.setTopAnchor(burger, 10.0);
                    main_pane.setLeftAnchor(burger, 10.0);
                    border_pane.setCenter(main_pane);
                });

            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }).start();

    }


    // Helpers //////////////////////////////////////////////////////
    public static final Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Bean
    public BorderPane getBorderPane() {
        return border_pane;
    }


}

package gov.zndev.springzninventoryclient;

import gov.zndev.springzninventoryclient.controller.others.LoginCtrl;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PrimaryStageInitializer implements ApplicationListener<StageReadyEvent> {

    private final FxWeaver fxWeaver;

    @Autowired
    public PrimaryStageInitializer(FxWeaver fxWeaver) { //(1)
        this.fxWeaver = fxWeaver;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) { //(2)
        FxControllerAndView<LoginCtrl, AnchorPane> login = fxWeaver.load(LoginCtrl.class);
        Stage stage = event.stage;
        stage.setTitle("ZN Inventory - Login");
        Scene scene = new Scene(login.getView().get()); //(3)
        stage.setScene(scene);
        stage.setResizable(false);
        login.getController().setStage(stage);
        stage.show();
    }
}
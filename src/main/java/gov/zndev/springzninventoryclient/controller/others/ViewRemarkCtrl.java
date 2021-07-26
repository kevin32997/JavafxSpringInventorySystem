package gov.zndev.springzninventoryclient.controller.others;

import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Remark;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
@FxmlView("view_remark_layout.fxml")
public class ViewRemarkCtrl {

    @FXML
    private TextArea description;

    @FXML
    private Label remarks_by;

    @FXML
    private Label date_created;

    private Remark remark;

    @FXML
    public void initialize(){}

    public void setRemark(Remark remark){
        this.remark=remark;
        populateFields();
    }

    private void populateFields(){
        this.remarks_by.setText(ResourceHelper.MAIN_USER.getFullname());
        this.date_created.setText(new SimpleDateFormat("MMMMM dd, yyyy").format(remark.getDateCreated()));
        this.description.setText(remark.getDescription());
    }



}

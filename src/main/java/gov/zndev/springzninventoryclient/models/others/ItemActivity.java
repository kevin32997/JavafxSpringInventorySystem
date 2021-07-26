package gov.zndev.springzninventoryclient.models.others;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ItemActivity {

    private IntegerProperty id=new SimpleIntegerProperty();
    private StringProperty dateTime=new SimpleStringProperty();
    private StringProperty type=new SimpleStringProperty();
    private StringProperty description=new SimpleStringProperty();
    private StringProperty total=new SimpleStringProperty();
    private StringProperty user=new SimpleStringProperty();
    private IntegerProperty referenceId=new SimpleIntegerProperty();

    public ItemActivity() {
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getDateTime() {
        return dateTime.get();
    }

    public StringProperty dateTimeProperty() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime.set(dateTime);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getTotal() {
        return total.get();
    }

    public StringProperty totalProperty() {
        return total;
    }

    public void setTotal(String total) {
        this.total.set(total);
    }

    public String getUser() {
        return user.get();
    }

    public StringProperty userProperty() {
        return user;
    }

    public void setUser(String user) {
        this.user.set(user);
    }

    public int getReferenceId() {
        return referenceId.get();
    }

    public IntegerProperty referenceIdProperty() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId.set(referenceId);
    }
}

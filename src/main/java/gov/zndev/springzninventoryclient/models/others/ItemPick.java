package gov.zndev.springzninventoryclient.models.others;

import gov.zndev.springzninventoryclient.models.Item;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;

public class ItemPick {

    private IntegerProperty itemId = new SimpleIntegerProperty();
    private IntegerProperty inventoryId = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty serial = new SimpleStringProperty();
    private StringProperty propertyNumber=new SimpleStringProperty();
    private StringProperty model = new SimpleStringProperty();
    private StringProperty brand = new SimpleStringProperty();
    private StringProperty type = new SimpleStringProperty();
    private StringProperty code = new SimpleStringProperty();
    private StringProperty location = new SimpleStringProperty();
    private IntegerProperty quantity = new SimpleIntegerProperty();
    private StringProperty dateCreated= new SimpleStringProperty();
    private StringProperty dateupdated= new SimpleStringProperty();
    private IntegerProperty createdBy= new SimpleIntegerProperty();


    private TextField qtyTextfield;

    public ItemPick() {
    }

    public ItemPick(Item item, int qty) {
        this.itemId.set(item.getId());
        this.name.set(item.getName());
        this.model.set(item.getModel());
        this.brand.set(item.getBrandName());
        this.quantity.set(qty);
    }

    public void copyItem(Item item) {
        this.setItemId(item.getId());
        this.setName(item.getName());
        this.setModel(item.getModel());
        this.setBrand(item.getBrandName());
        this.setType(item.getTypeName());
    }


    public int getItemId() {
        return itemId.get();
    }

    public IntegerProperty itemIdProperty() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId.set(itemId);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSerial() {
        return serial.get();
    }

    public StringProperty serialProperty() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial.set(serial);
    }

    public String getModel() {
        return model.get();
    }

    public StringProperty modelProperty() {
        return model;
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public String getBrand() {
        return brand.get();
    }

    public StringProperty brandProperty() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand.set(brand);
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

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public TextField getQtyTextfield() {
        return qtyTextfield;
    }

    public void setQtyTextfield(TextField qtyTextfield) {
        this.qtyTextfield = qtyTextfield;
    }

    public String getCode() {
        return code.get();
    }

    public StringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public String getDateCreated() {
        return dateCreated.get();
    }

    public StringProperty dateCreatedProperty() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public String getDateupdated() {
        return dateupdated.get();
    }

    public StringProperty dateupdatedProperty() {
        return dateupdated;
    }

    public void setDateupdated(String dateupdated) {
        this.dateupdated.set(dateupdated);
    }

    public int getCreatedBy() {
        return createdBy.get();
    }

    public IntegerProperty createdByProperty() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy.set(createdBy);
    }

    public String getPropertyNumber() {
        return propertyNumber.get();
    }

    public StringProperty propertyNumberProperty() {
        return propertyNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber.set(propertyNumber);
    }

    public int getInventoryId() {
        return inventoryId.get();
    }

    public IntegerProperty inventoryIdProperty() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId.set(inventoryId);
    }
}

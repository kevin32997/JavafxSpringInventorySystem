package gov.zndev.springzninventoryclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Date;

public class Item {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("brandName")
    @Expose
    private String brandName;

    @SerializedName("brandId")
    @Expose
    private int brandId;

    @SerializedName("model")
    @Expose
    private String model;

    @SerializedName("typeName")
    @Expose
    private String typeName;

    @SerializedName("typeId")
    @Expose
    private int typeId;

    private String description;

    private int createdBy;


    @SerializedName("dateCreated")
    @Expose
    private Date dateCreated;

    @SerializedName("dateUpdated")
    @Expose
    private Date dateUpdated;


    private IntegerProperty inventoryCount=new SimpleIntegerProperty();

    public Item(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInventoryCount() {
        return inventoryCount.get();
    }

    public IntegerProperty inventoryCountProperty() {
        return inventoryCount;
    }

    public void setInventoryCount(int inventoryCount) {
        this.inventoryCount.set(inventoryCount);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brandName='" + brandName + '\'' +
                ", brandId=" + brandId +
                ", model='" + model + '\'' +
                ", typeName='" + typeName + '\'' +
                ", typeId=" + typeId +
                ", description='" + description + '\'' +
                ", createdBy=" + createdBy +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                ", inventoryCount=" + inventoryCount +
                '}';
    }
}

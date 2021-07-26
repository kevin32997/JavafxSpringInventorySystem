package gov.zndev.springzninventoryclient.models;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.Date;

public class Location {

    private int id;
    private String name;
    private String description;
    private int createdBy;

    private SimpleIntegerProperty inventoryCount=new SimpleIntegerProperty();

    private Date dateCreated;
    private Date dateUpdated;

    public Location() {
    }

    public Location(String name, String description, int createdBy) {
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }


    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getInventoryCount() {
        return inventoryCount.get();
    }

    public SimpleIntegerProperty inventoryCountProperty() {
        return inventoryCount;
    }

    public void setInventoryCount(int inventoryCount) {
        this.inventoryCount.set(inventoryCount);
    }
}

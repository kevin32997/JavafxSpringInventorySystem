package gov.zndev.springzninventoryclient.models;

import java.util.Date;

public class InventoryActivity {

    public static final String ACTION_IMPORT="import";
    public static final String ACTION_EXPORT="export";

    private int id;
    private int referenceId;
    private int total;
    private int itemId;
    private int inventoryId;
    private int locationId;
    private int otherLocationId;
    private String action;
    private Date activityDate;

    public InventoryActivity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public int getOtherLocationId() {
        return otherLocationId;
    }

    public void setOtherLocationId(int otherLocationId) {
        this.otherLocationId = otherLocationId;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }
}

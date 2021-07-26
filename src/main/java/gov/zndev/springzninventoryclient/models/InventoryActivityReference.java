package gov.zndev.springzninventoryclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class InventoryActivityReference {

    public static final String ACTION_IMPORT="import";
    public static final String ACTION_EXPORT="export";

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("action")
    @Expose
    private String action;

    @SerializedName("consignee")
    @Expose
    private String consignee;

    @SerializedName("locationId")
    @Expose
    private int locationId;

    private Location location;

    @SerializedName("locationFromId")
    @Expose
    private int locationFromId;

    private Location locationFrom;

    @SerializedName("reference")
    @Expose
    private String reference;

    @SerializedName("remarks")
    @Expose
    private String remarks;

    @SerializedName("userId")
    @Expose
    private int userId;

    @SerializedName("activityDate")
    @Expose
    private Date activityDate;

    public InventoryActivityReference() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getLocationFromId() {
        return locationFromId;
    }

    public void setLocationFromId(int locationFromId) {
        this.locationFromId = locationFromId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocationFrom() {
        return locationFrom;
    }

    public void setLocationFrom(Location locationFrom) {
        this.locationFrom = locationFrom;
    }

    @Override
    public String toString() {
        return "InventoryActivityReference{" +
                "id=" + id +
                ", action='" + action + '\'' +
                ", consignee='" + consignee + '\'' +
                ", locationId=" + locationId +
                ", location=" + location +
                ", locationFromId=" + locationFromId +
                ", locationFrom=" + locationFrom +
                ", reference='" + reference + '\'' +
                ", remarks='" + remarks + '\'' +
                ", userId=" + userId +
                ", activityDate=" + activityDate +
                '}';
    }
}

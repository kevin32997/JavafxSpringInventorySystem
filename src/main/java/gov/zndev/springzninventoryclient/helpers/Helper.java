package gov.zndev.springzninventoryclient.helpers;

import com.google.gson.internal.LinkedTreeMap;
import gov.zndev.springzninventoryclient.models.*;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationSupport;
import org.springframework.context.annotation.Bean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Helper {


    public static final int SORTTYPE_ASCENDING = 0;
    public static final int SORTTYPE_DESCENDING = 1;

    public static final FXMLLoader CreateLoader(String src, Class<?> object) {
        return new FXMLLoader(object.getClassLoader().getResource(src));
    }

    public static final ValidationSupport CreateValidationSupport() {
        return new ValidationSupport();
    }


    public static final List<Item> CastToItemList(List<?> list) {
        List<Item> items = new ArrayList<>();
        for (Object object : list) {
            LinkedTreeMap map = (LinkedTreeMap) object;
            Item item = new Item();
            item.setId((int) Double.parseDouble(map.get("id").toString()));
            item.setName(map.get("name").toString());
            item.setBrandId((int) Double.parseDouble(map.get("brandId").toString()));
            item.setBrandName((map.get("brandName") != null) ? map.get("brandName").toString() : "");
            item.setTypeId((int) Double.parseDouble(map.get("typeId").toString()));
            item.setTypeName((map.get("typeName") != null) ? map.get("typeName").toString() : "");
            item.setModel((map.get("model") != null) ? map.get("model").toString() : "");
            item.setDescription((map.get("description") != null) ? map.get("description").toString() : "");
            item.setCreatedBy((map.get("createdBy") != null) ? (int) Double.parseDouble(map.get("createdBy").toString()) : 0);
            try {
                item.setDateCreated(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(map.get("dateCreated").toString()));
                item.setDateUpdated(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(map.get("dateUpdated").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            try {
                item.setDateCreated(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(map.get("dateCreated").toString()));
                item.setDateUpdated(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(map.get("dateUpdated").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            items.add(item);
        }

        return items;
    }

    public static final List<Brand> CastToBrandList(List<?> list) {
        List<Brand> brands = new ArrayList<>();
        for (Object object : list) {
            LinkedTreeMap map = (LinkedTreeMap) object;

            Brand brand = new Brand();
            brand.setId((int) Double.parseDouble(map.get("id").toString()));
            brand.setName(map.get("name").toString());
            brand.setDescription(map.get("description").toString());
            try {
                brand.setDateCreated(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(map.get("dateCreated").toString()));
                brand.setDateUpdated(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(map.get("dateUpdated").toString()));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            brands.add(brand);
        }

        return brands;
    }

    public static final List<Type> CastToTypeList(List<?> list) {
        List<Type> types = new ArrayList<>();
        for (Object object : list) {
            LinkedTreeMap map = (LinkedTreeMap) object;
            Type type = new Type();
            type.setId((int) Double.parseDouble(map.get("id").toString()));
            type.setName(map.get("name").toString());
            type.setDescription(map.get("description").toString());
            try {
                type.setDateCreated(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(map.get("dateCreated").toString()));
                type.setDateUpdated(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(map.get("dateUpdated").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            types.add(type);
        }
        return types;
    }


    public static final List<Location> CastToLocationList(List<?> list) {
        List<Location> locations = new ArrayList<>();
        for (Object object : list) {
            LinkedTreeMap map = (LinkedTreeMap) object;
            Location location = new Location();
            location.setId((int) Double.parseDouble(map.get("id").toString()));
            location.setName(map.get("name").toString());
            location.setDescription(map.get("description").toString());
            location.setCreatedBy((int) Double.parseDouble(map.get("id").toString()));
            try {
                location.setDateCreated(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(map.get("dateCreated").toString()));
                location.setDateUpdated(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(map.get("dateUpdated").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            locations.add(location);
        }
        return locations;
    }

    public static final List<InventoryActivityReference> CastToInvActivityReference(List<?> list) {
        List<InventoryActivityReference> references = new ArrayList<>();
        for (Object object : list) {
            LinkedTreeMap map = (LinkedTreeMap) object;
            InventoryActivityReference reference = new InventoryActivityReference();
            reference.setId((int) Double.parseDouble(map.get("id").toString()));
            reference.setUserId((map.get("userId") != null) ? (int) Double.parseDouble(map.get("userId").toString()) : 0);
            reference.setAction((map.get("action") != null) ? map.get("action").toString() : "");
            reference.setConsignee((map.get("consignee") != null) ? map.get("consignee").toString() : "");
            reference.setLocationId((map.get("location") != null) ? (int) Double.parseDouble(map.get("location").toString()) : 0);
            reference.setLocationFromId((map.get("locationFrom") != null) ? (int) Double.parseDouble(map.get("locationFrom").toString()) : 0);

            reference.setReference((map.get("reference") != null) ? map.get("reference").toString() : "");

            reference.setRemarks((map.get("remarks") != null) ? map.get("remarks").toString() : "");
            try {
                reference.setActivityDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(map.get("activityDate").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            references.add(reference);
        }
        return references;
    }

    public static final List<InventoryActivity> CastToInventoryActivity(List<?> list) {
        List<InventoryActivity> activities = new ArrayList<>();
        for (Object object : list) {
            LinkedTreeMap map = (LinkedTreeMap) object;
            InventoryActivity activity = new InventoryActivity();
            activity.setId((int) Double.parseDouble(map.get("id").toString()));
            activity.setReferenceId((map.get("referenceId") != null) ? (int) Double.parseDouble(map.get("referenceId").toString()) : 0);
            activity.setTotal((map.get("total") != null) ? (int) Double.parseDouble(map.get("total").toString()) : 0);
            activity.setItemId((map.get("itemId") != null) ? (int) Double.parseDouble(map.get("itemId").toString()) : 0);

            activity.setInventoryId((map.get("inventoryId") != null) ? (int) Double.parseDouble(map.get("inventoryId").toString()) : 0);


            activity.setLocationId((map.get("locationId") != null) ? (int) Double.parseDouble(map.get("locationId").toString()) : 0);
            activity.setOtherLocationId((map.get("otherLocationId") != null) ? (int) Double.parseDouble(map.get("otherLocationId").toString()) : 0);

            activity.setAction((map.get("action") != null) ? map.get("action").toString() : "");
            try {
                activity.setActivityDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(map.get("activityDate").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            activities.add(activity);
        }
        return activities;
    }

    public static final List<Inventory> CastToInventoryList(List<?> list) {
        List<Inventory> inventories = new ArrayList<>();
        for (Object object : list) {
            LinkedTreeMap map = (LinkedTreeMap) object;
            Inventory inventory = new Inventory();
            inventory.setId((int) Double.parseDouble(map.get("id").toString()));
            inventory.setItemId((map.get("itemId") != null) ? (int) Double.parseDouble(map.get("itemId").toString()) : 0);
            inventory.setLocationId((map.get("locationId") != null) ? (int) Double.parseDouble(map.get("locationId").toString()) : 0);
            inventory.setPropertyNumber((map.get("propertyNumber") != null) ? map.get("propertyNumber").toString() : "");
            inventory.setSerialNumber((map.get("serialNumber") != null) ? map.get("serialNumber").toString() : "");
            inventory.setCode((map.get("code") != null) ? map.get("code").toString() : "");
            inventory.setCreatedBy((map.get("createdBy") != null) ? (int) Double.parseDouble(map.get("createdBy").toString()) : 0);
            try {
                inventory.setDateCreated(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(map.get("dateCreated").toString()));
                inventory.setDateUpdated(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(map.get("dateUpdated").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            LinkedTreeMap item_map = (LinkedTreeMap) map.get("item");
            if(item_map!=null) {
                Item item = new Item();
                item.setId((item_map.get("id") != null) ? (int) Double.parseDouble(item_map.get("id").toString()) : 0);
                item.setName((item_map.get("name") != null) ? item_map.get("name").toString() : "");
                item.setBrandId((item_map.get("brandId") != null) ? (int) Double.parseDouble(item_map.get("brandId").toString()) : 0);
                item.setBrandName((item_map.get("brandName") != null) ? item_map.get("brandName").toString() : "");
                item.setTypeId((item_map.get("typeId") != null) ? (int) Double.parseDouble(item_map.get("typeId").toString()) : 0);
                item.setTypeName((item_map.get("typeName") != null) ? item_map.get("typeName").toString() : "");
                item.setModel((item_map.get("model") != null) ? item_map.get("model").toString() : "");
                item.setDescription((item_map.get("description") != null) ? item_map.get("description").toString() : "");
                item.setCreatedBy((item_map.get("createdBy") != null) ? (int) Double.parseDouble(map.get("createdBy").toString()) : 0);


                try {
                    item.setDateCreated((map.get("dateCreated") != null) ? new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(item_map.get("dateCreated").toString()) : new Date());
                    item.setDateUpdated((map.get("dateUpdated") != null) ? new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(item_map.get("dateUpdated").toString()) : new Date());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                inventory.setItem(item);
            }
            inventories.add(inventory);
        }
        return inventories;
    }

    public static final List<Remark> CastToRemarksList(List<?> list) {
        List<Remark> remarks = new ArrayList<>();
        for (Object object : list) {
            LinkedTreeMap map = (LinkedTreeMap) object;
            Remark remark = new Remark();
            remark.setId((int) Double.parseDouble(map.get("id").toString()));
            remark.setUserId((map.get("userId") != null) ? (int) Double.parseDouble(map.get("userId").toString()) : 0);
            remark.setType((map.get("type") != null) ? map.get("type").toString() : "");
            remark.setItemId((map.get("itemId") != null) ? (int) Double.parseDouble(map.get("itemId").toString()) : 0);
            remark.setDescription((map.get("description") != null) ? map.get("description").toString() : "");
            try {
                remark.setDateCreated(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(map.get("dateCreated").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            remarks.add(remark);

        }
        return remarks;
    }


    public static FXMLLoader LoadLayoutResource(String resource) {
        return new FXMLLoader(Helper.class.getClassLoader().getResource(resource));
    }

    @Bean
    public static final Stage CreateStage(String title) {
        Stage stage = new Stage();
        stage.setTitle(title);
        return stage;
    }

    public static final List<User> CastToUserList(List<?> list) {
        List<User> convertedList = new ArrayList<>();
        for (Object object : list) {
            LinkedTreeMap map = (LinkedTreeMap) object;
            User user = new User();
            user.setId((map.get("id") != null) ? (int) Double.parseDouble(map.get("id").toString()) : 0);
            user.setFullname((map.get("fullname") != null) ? map.get("fullname").toString() : "");
            user.setUsername((map.get("username") != null) ? map.get("username").toString() : "");
            user.setPassword((map.get("password") != null) ? map.get("password").toString() : "");
            user.setUserRole((map.get("userRole") != null) ? map.get("userRole").toString() : "");
            try {
                DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                user.setDateCreated(utcFormat.parse(map.get("dateCreated").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            convertedList.add(user);
        }

        return convertedList;
    }

}

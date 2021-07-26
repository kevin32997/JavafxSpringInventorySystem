package gov.zndev.springzninventoryclient.helpers;


import gov.zndev.springzninventoryclient.models.User;

public class ResourceHelper {

    // App Title
    public static String APP_NAME = "ZN Inventory Client";

    // Main User ID
    public static User MAIN_USER = new User();

    // URL (Rest Api Connections)
    public static String BASE_URL = "http://localhost:8081/";


    // MAIN COMPONENTS
    public static final String LAYOUT_COMPONENT_MAINLAYOUT = "main/resources/fxml/components/main_layout.fxml";
    public static final String LAYOUT_COMPONENT_SIDEBAR = "main/resources/fxml/components/side_bar_layout.fxml";
    public static final String LAYOUT_COMPONENT_LOADINGPANE = "gov/zndev/springinventoryclient/controller/components/loading_pane_layout.fxml";
    public static final String LAYOUT_COMPONENT_SEARCHRESULT = "main/resources/fxml/components/search_result_layout.fxml";
    public static final String LAYOUT_COMPONENT_PROGRESSDIALOG = "main/resources/fxml/components/progress_dialog_layout.fxml";
    public static final String LAYOUT_COMPONENT_TABLEACTIONBUTTONS = "main/resources/fxml/components/table_action_buttons_layout.fxml";

    // DASHBOARD
    public static final String LAYOUT_DASHBOARD_CHARTS = "main/resources/fxml/dashboard/dashboard_charts_layout.fxml";

    // ITEMS
    public static final String LAYOUT_ITEM_ITEMSTABLE = "main/resources/fxml/items/item_table_list_layout.fxml";
    public static final String LAYOUT_ITEM_ADDITEM = "main/resources/fxml/items/item_add_item_layout.fxml";
    public static final String LAYOUT_ITEM_MODAL_VIEWITEM = "main/resources/fxml/items/item_modal_view_item_layout.fxml";
    public static final String LAYOUT_ITEM_MODAL_UPDATEITEM = "main/resources/fxml/items/item_modal_update_item_layout.fxml";

    // BRANDS
    public static final String LAYOUT_BRAND_BRANDSTABLE = "main/resources/fxml/brands/brand_table_list_layout.fxml";
    public static final String LAYOUT_BRAND_MODAL_ADDBRAND = "main/resources/fxml/brands/brand_modal_add_brand_layout.fxml";
    public static final String LAYOUT_BRAND_MODAL_VIEW_BRAND = "main/resources/fxml/brands/brand_modal_view_brand_layout.fxml";

    // TYPES
    public static final String LAYOUT_TYPES_TYPES_TABLE = "main/resources/fxml/types/type_table_list_layout.fxml";
    public static final String LAYOUT_TYPES_MODAL_ADD_TYPE = "main/resources/fxml/types/type_modal_add_type_layout.fxml";
    public static final String LAYOUT_TYPES_MODAL_VIEW_TYPE = "main/resources/fxml/types/type_modal_view_type_layout.fxml";

    // LOCATION
    public static final String LAYOUT_LOCATIONS_LOCATIONSTABLE = "main/resources/fxml/locations/location_table_list_layout.fxml";
    public static final String LAYOUT_LOCATION_MODAL_ADDLOCATION = "main/resources/fxml/locations/location_modal_add_location_layout.fxml";
    public static final String LAYOUT_LOCATION_MODAL_VIEWLOCATION = "main/resources/fxml/locations/location_modal_view_location_layout.fxml";

    // INVENTORY
    public static final String LAYOUT_INVENTORY_IMPORT = "main/resources/fxml/inventory/activity_import_layout.fxml";
    public static final String LAYOUT_INVENTORY_EXPORT = "main/resources/fxml/inventory/activity_export_layout.fxml";
    public static final String LAYOUT_INVENTORY_VIEW_LOCATION_ITEMS = "main/resources/fxml/inventory/activity_location_view_items_layout.fxml";
    public static final String LAYOUT_INVENTORY_VIEW_LOCATION_ITEMS_ROW = "main/resources/fxml/inventory/activity_location_view_item_row_layout.fxml";
    public static final String LAYOUT_INVENTORY_CONFIRM_IMPORT = "main/resources/fxml/inventory/activity_confirm_import_layout.fxml";
    public static final String LAYOUT_INVENTORY_CONFIRM_EXPORT = "main/resources/fxml/inventory/activity_confirm_export_layout.fxml";


    // TAGS
    public static final String TAG_LOCATION = "location";
    public static final String TAG_ITEM = "item";
    public static final String TAG_BRAND = "brand";
    public static final String TAG_TYPE = "type";
    public static final String TAG_INVENTORY= "inventory";
    public static final String TAG_REFERENCE= "reference";

    // SORT TYPE
    public static final String SORT_ASCENDING = "ascending";
    public static final String SORT_DESCENDING = "descending";
}

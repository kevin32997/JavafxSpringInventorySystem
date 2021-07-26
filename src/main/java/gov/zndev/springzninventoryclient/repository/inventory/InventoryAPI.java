package gov.zndev.springzninventoryclient.repository.inventory;

import gov.zndev.springzninventoryclient.repository.other.RequestResponse;
import okhttp3.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface InventoryAPI {

    @POST("api/inventory/add")
    @Multipart
    Call<RequestResponse> createInventory(
            @Part("itemId") RequestBody itemId,
            @Part("locationId") RequestBody locationId,
            @Part("propertyNumber") RequestBody propertyNumber,
            @Part("serialNumber") RequestBody serialNumber,
            @Part("code") RequestBody code,
            @Part("createdBy") RequestBody createdBy
    );

    @GET("api/inventory/view/{inventory_id}")
    Call<RequestResponse> getInventoryById(@Path("inventory_id") int id);

    @GET("api/inventory/view/page/{page}/{size}")
    Call<RequestResponse> getInventoryByPage(@Path("page") int page, @Path("size") int size);

    @GET("api/inventory/view/tag/{tag}/{id}")
    Call<RequestResponse> getInventoryByTag(@Path("tag") String tag, @Path("id") int id);

    @GET("api/inventory/view/byItemAndLocation/{item_id}/{location_id}")
    Call<RequestResponse> getInventoryByItemAndLocation(@Path("item_id") int itemId, @Path("location_id") int locationId);

    @GET("api/inventory/pageByLocation/{location_id}/{page}/{size}")
    Call<RequestResponse> getInventoryPageByLocation(@Path("location_id") int locationId, @Path("page") int page, @Path("size") int size);

    @GET("api/inventory/search/{search}/{location}/{size}")
    Call<RequestResponse> searchItemInventoryByLocation(@Path("search") String search, @Path("location") int location, @Path("size") int size);

    @GET("api/inventory/search_location/byNameOrSerial/{location_id}/{searched}")
    Call<RequestResponse> searchLocationItemByNameOrSerial(@Path("location_id") int locationId, @Path("searched") String searched);

    @GET("api/inventory/view/tag_page/{tag}/{tag_id}/page/{page}/{size}/sort/{sort_type}/{sort_by}")
    Call<RequestResponse> getInventoryByTagPageSorted(
            @Path("tag") String tag,
            @Path("tag_id") int id,
            @Path("page") int page,
            @Path("size") int size,
            @Path("sort_type") String sortType,
            @Path("sort_by") String sortBy
    );


    /*=====================================================================
                                    COUNTS
     =====================================================================*/

    @GET("api/inventory/count")
    Call<Map<String, Integer>> getInventoryCount();

    @GET("api/inventory/count/{tag}/{id}")
    Call<Map<String, Integer>> getInventoryCountByTag(@Path("tag") String tag, @Path("id") int locationId);

    @GET("api/inventory/count_total/{tag}/{id}")
    Call<Map<String, Integer>> getInventoryTotalItemCountByTag(@Path("tag") String tag, @Path("id") int id);

    @GET("api/inventory/count_total/byItemAndLocation/{item_id}/{location_id}")
    Call<Map<String, Integer>> getInventoryTotalItemCountByItemAndLocation(@Path("item_id") int itemId, @Path("location_id") int locationId);




    /*=====================================================================
                                   DELETE
    =====================================================================*/

    @DELETE("api/inventory/delete/{inventory_id}")
    Call<RequestResponse> deleteInventory(@Path("inventory_id") int id);


    /*=====================================================================
                                  PUT REQUEST
    =====================================================================*/
    @PUT("api/inventory/update/{inventory_id}")
    @Multipart
    Call<RequestResponse> updateInventoryDetails(
            @Path("inventory_id") int id,
            @Part("serialNumber") RequestBody serialNumber,
            @Part("code") RequestBody code,
            @Part("propertyNumber") RequestBody propertyNumber
    );

    @PUT("api/inventory/update_location/{inventory_id}/{location_id}")
    @Multipart
    Call<RequestResponse> updateInventoryLocation(
            @Path("inventory_id") int id,
            @Part("location_id") RequestBody locationId
    );

}

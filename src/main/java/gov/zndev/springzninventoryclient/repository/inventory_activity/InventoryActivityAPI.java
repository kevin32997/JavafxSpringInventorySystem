package gov.zndev.springzninventoryclient.repository.inventory_activity;

import gov.zndev.springzninventoryclient.repository.other.RequestResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface InventoryActivityAPI {

    @GET("api/inv_activity/view/{inv_act_id}")
    Call<RequestResponse> getInvActivityById(@Path("inv_act_id") int id);

    @GET("api/inv_activity/count")
    Call<Map<String, String>> getInvActivityCount();

    @GET("api/inv_activity/page/{page}/{size}")
    Call<RequestResponse> getInvActivityByPage(@Path("page") int page, @Path("size") int size);

    @GET("api/inv_activity/pageByTag/{tag}/{id}/{page}/{size}")
    Call<RequestResponse> getInvActivityPageByTag(
            @Path("tag") String tag,
            @Path("id") int id,
            @Path("page") int page,
            @Path("size") int size

    );

    @GET("api/inv_activity/view_tag/{tag}/{id}")
    Call<RequestResponse> getAllInvActivityByTagId(
            @Path("tag") String tag,
            @Path("id") int id
    );


    @GET("api/inv_activity/last_id")
    Call<Map<String, String>> getInvActivityLastId();

    @POST("api/inv_activity/add")
    @Multipart
    Call<RequestResponse> createInvActivity(
            @Part("itemId") RequestBody itemId,
            @Part("inventoryId") RequestBody inventoryId,
            @Part("referenceId") RequestBody referenceId,
            @Part("locationId") RequestBody locationId,
            @Part("otherLocationId") RequestBody otherLocationId,
            @Part("total") RequestBody total,
            @Part("action") RequestBody action
    );

    // Counts //////////////////////////////////////////

    @GET("api/inv_activity/count/{tag}/{id}")
    Call<Map<String, Integer>> getInvActCountByTag(@Path("tag") String tag, @Path("id") int id);
}

package gov.zndev.springzninventoryclient.repository.inv_activity_ref;

import gov.zndev.springzninventoryclient.repository.other.RequestResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface InvActivityReferenceAPI {
    @GET("api/inv_activity_ref/view/{reference_id}")
    Call<RequestResponse> getInvActivityRefById(@Path("reference_id") int id);

    @GET("api/inv_activity_ref/count")
    Call<Map<String, String>> getInvActivityRefCount();

    @GET("api/inv_activity_ref/page/{page}/{size}")
    Call<RequestResponse> getInvActivityRefByPage(@Path("page") int page, @Path("size") int size);

    @GET("api/inv_activity_ref/search/{search_text}/{limit}")
    Call<RequestResponse> searchInvActivityRef(@Path("search_text") String search, @Path("limit") int size);

    @GET("api/inv_activity_ref/last_id")
    Call<Map<String, String>> getInvActReferenceLastId();

    @GET("api/inv_activity_ref/viewByLocation/{location_id}/{page}/{size}")
    Call<RequestResponse> getInvActReferenceByLocation(@Path("location_id") int locationId, @Path("page") int page, @Path("size") int size);

    @POST("api/inv_activity_ref/add")
    @Multipart
    Call<RequestResponse> createInvActivityRef(
            @Part("userId") RequestBody userId,
            @Part("reference") RequestBody reference,
            @Part("consignee") RequestBody consignee,
            @Part("remarks") RequestBody remarks,
            @Part("location") RequestBody location,
            @Part("locationFrom") RequestBody locationFrom,
            @Part("action") RequestBody action
    );

    // Counts
    @GET("api/inv_activity_ref/countByLocation/{location_id}")
    Call<Map<String, Integer>> getInvActReferenceCountByLocation(@Path("location_id") int locationId);

}

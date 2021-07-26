package gov.zndev.springzninventoryclient.repository.locations;

import gov.zndev.springzninventoryclient.repository.other.RequestResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface LocationApi {

    @GET("api/locations/view/{location_id}")
    Call<RequestResponse> getLocationById(@Path("location_id") int id);

    @GET("api/locations/view")
    Call<RequestResponse> getAllLocation();

    @GET("api/locations/count")
    Call<Map<String,String>> getLocationsCount();

    @GET("api/locations/page/{page}/{size}")
    Call<RequestResponse> getLocationByPage(@Path("page") int page, @Path("size") int size);

    @GET("api/locations/search/{search_text}/{limit}")
    Call<RequestResponse> searchLocation(@Path("search_text") String search, @Path("limit") int size);

    @POST("api/locations/add")
    @Multipart
    Call<RequestResponse> createLocation(@Part("name") RequestBody name, @Part("description") RequestBody description);
}

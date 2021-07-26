package gov.zndev.springzninventoryclient.repository.types;

import gov.zndev.springzninventoryclient.repository.other.RequestResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface TypeAPI {


    @GET("api/types/view/{type_id}")
    Call<RequestResponse> getTypeById(@Path("type_id") int page);

    @GET("api/types/page/{page}/{size}")
    Call<RequestResponse> getTypesByPage(@Path("page") int page, @Path("size") int size);

    @GET("api/types/page/{page}/{size}/sort/{sortType}/{sortBy}")
    Call<RequestResponse> getTypesByPageSorted(
            @Path("page") int page,
            @Path("size") int size,
            @Path("sortType") String sortType,
            @Path("sortBy") String sortBy
    );

    @GET("api/types/search/{search_text}/{limit}")
    Call<RequestResponse> searchType(@Path("search_text") String search, @Path("limit") int size);

    @GET("api/types/count")
    Call<Map<String, String>> getTypesCount();

    @POST("api/types/add")
    @Multipart
    Call<RequestResponse> createType(@Part("name") RequestBody name, @Part("description") RequestBody description);


}

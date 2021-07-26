package gov.zndev.springzninventoryclient.repository.brands;

import gov.zndev.springzninventoryclient.repository.other.RequestResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface BrandAPI {

    @GET("api/brands/view/{brand_id}")
    Call<RequestResponse> getBrandById(@Path("brand_id") int page);

    @GET("api/brands/count")
    Call<Map<String, String>> getBrandsCount();

    @GET("api/brands/page/{page}/{size}")
    Call<RequestResponse> getBrandsByPage(@Path("page") int page, @Path("size") int size);

    @GET("api/brands/page/{page}/{size}/sort/{sortType}/{sortBy}")
    Call<RequestResponse> getBrandsByPageSorted(
            @Path("page") int page,
            @Path("size") int size,
            @Path("sortType") String sortype,
            @Path("sortBy") String sortBy
    );

    @GET("api/brands/search/{search_text}/{limit}")
    Call<RequestResponse> searchBrand(@Path("search_text") String search, @Path("limit") int size);

    @POST("api/brands/add")
    @Multipart
    Call<RequestResponse> createBrand(@Part("name") RequestBody name, @Part("description") RequestBody description);

    @PUT("api/brands/update/{brand_id}")
    @Multipart
    Call<RequestResponse> updateBrand(
            @Path("brand_id") int id,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description
    );

}

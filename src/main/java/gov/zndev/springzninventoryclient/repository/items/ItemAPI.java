package gov.zndev.springzninventoryclient.repository.items;

import gov.zndev.springzninventoryclient.repository.other.RequestResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface ItemAPI {

    @GET("api/items/view/{item_id}")
    Call<RequestResponse> getItemById(@Path("item_id") int itemId);

    @GET("api/items/page/{page}/{size}/sort/{sortBy}/{sortType}")
    Call<RequestResponse> getItemsByPage(
            @Path("page") int page,
            @Path("size") int size,
            @Path("sortBy") String sortBy,
            @Path("sortType") String sortType);

    @GET("api/items/pageByTag/{tag}/{id}/{page}/{size}")
    Call<RequestResponse> getItemsPageByTag(@Path("tag") String tag, @Path("id") int brandId, @Path("page") int page, @Path("size") int size);

    @GET("api/items/count")
    Call<Map<String, String>> getItemsCount();

    @GET("api/items/count/{tag}/{id}")
    Call<Map<String, String>> getItemsCountByTag(@Path("tag") String tag, @Path("id") int id);

    @GET("api/items/search/{searched_text}/{limit}")
    Call<RequestResponse> searchItem(@Path("searched_text") String searched, @Path("limit") int limit);

    @POST("api/items/add")
    @Multipart
    Call<RequestResponse> createItem(
            @Part("name") RequestBody name,
            @Part("brandId") RequestBody brandId,
            @Part("typeId") RequestBody typeId,
            @Part("model") RequestBody model,
            @Part("createdBy") RequestBody createdBy,
            @Part("description") RequestBody description
    );

    @POST("api/items/image/update/{item_id}")
    @Multipart
    Call<RequestResponse> updateItemImage(@Part MultipartBody.Part image, @Path("item_id") int itemId);

    @PUT("api/items/update/{item_id}")
    @Multipart
    Call<RequestResponse> updateItem(
            @Path("item_id") int item_id,
            @Part("name") RequestBody name,
            @Part("brandId") RequestBody brandId,
            @Part("typeId") RequestBody typeId,
            @Part("model") RequestBody model
    );

}

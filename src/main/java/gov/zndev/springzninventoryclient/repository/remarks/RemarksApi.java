package gov.zndev.springzninventoryclient.repository.remarks;

import gov.zndev.springzninventoryclient.repository.other.RequestResponse;
import okhttp3.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface RemarksApi {

    @POST("api/remarks/add")
    @Multipart
    Call<RequestResponse> createRemark(
            @Part("itemId") RequestBody name,
            @Part("description") RequestBody description,
            @Part("userId") RequestBody user,
            @Part("type") RequestBody type
    );


    @GET("api/remarks/view/{remark_id}")
    Call<RequestResponse> getRemarkById(@Path("remark_id") int id);

    @GET("api/remarks_tag/{tag}/{id}/page/{page}/{size}/sort/{sortType}/{sortBy}")
    Call<RequestResponse> getRemarksByPageTagSorted(
            @Path("tag") String tag,
            @Path("id") int id,
            @Path("page") int page,
            @Path("size") int size,
            @Path("sortType") String sort_type,
            @Path("sortBy") String sort_by
    );

    @GET("api/remarks/count")
    Call<Map<String, String>> getRemarksCount();

    @GET("api/remarks/count_tag/{tag}/{id}")
    Call<Map<String, String>> getRemarksCountByTagAndId(
            @Path("tag") String tag,
            @Path("id") int id
    );

}

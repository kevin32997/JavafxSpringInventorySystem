package gov.zndev.springzninventoryclient.repository.brands;

import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Brand;
import gov.zndev.springzninventoryclient.models.others.Sort;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import gov.zndev.springzninventoryclient.repository.other.RequestResponse;
import gov.zndev.springzninventoryclient.helpers.Helper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;

@Service
public class BrandsRepository {
    private Retrofit retrofit;
    private BrandAPI brandAPI;

    public BrandsRepository() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ResourceHelper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        brandAPI = retrofit.create(BrandAPI.class);
    }

    /*============================================================================
                                   GET REQUEST
    ============================================================================= */

    public void getBrandById(int brand_id, RepoInterface delegate) {
        new Thread(() -> {
            try {
                Call<RequestResponse> call = brandAPI.getBrandById(brand_id);
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            delegate.activityDone(true, "Request Successful.",Helper.CastToBrandList( response.body().getList()));
                        } else {
                            delegate.activityDone(false, "Server not responding.\nError Code: " + response.code(), null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RequestResponse> call, Throwable t) {
                        if (t instanceof ConnectException) {
                            delegate.activityDone(false, "Cannot connect to server, Please check your connection.", null);
                        } else if (t instanceof SocketTimeoutException) {
                            delegate.activityDone(false, "Cannot connect to server. Please try again later. ", null);
                        } else {
                            delegate.activityDone(false, "An Error has occurred! \nError: " + t.getMessage(), null);
                            t.printStackTrace();
                        }
                    }

                });
            } catch (Exception ex) {
                delegate.activityDone(false, "An Error has occurred! \nError: " + ex.getMessage(), null);
            }
        }).start();
    }

    public void getBrandsCount(RepoInterface delegate) {
        new Thread(() -> {
            Call<Map<String, String>> call = brandAPI.getBrandsCount();
            call.enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    System.out.println("response Code: " + response.code());
                    if (response.code() == 200) {
                        System.out.println("Body: " + response.body().toString());
                        Map<String, String> responseBody = response.body();
                        int count = Integer.parseInt(responseBody.get("count").toString());
                        delegate.activityDone(true, "Items Counted.", count);
                    } else {
                        delegate.activityDone(false, "Server Error.\nError Code: " + response.code(), null);
                    }
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    if (t instanceof ConnectException) {
                        delegate.activityDone(false, "Cannot connect to server, Please check your connection.", null);
                    } else if (t instanceof SocketTimeoutException) {
                        delegate.activityDone(false, "Cannot connect to server. Please try again later. ", null);
                    } else {
                        delegate.activityDone(false, "An Error has occurred! \nError: " + t.getMessage(), null);
                        t.printStackTrace();
                    }
                }
            });

        }).start();

    }


    public void getBrandsByPage(int page, int size, RepoInterface delegate) {
        new Thread(() -> {
            try {


                Call<RequestResponse> call = brandAPI.getBrandsByPage(page, size);
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            delegate.activityDone(true, "Request Successful.", Helper.CastToBrandList(response.body().getList()));
                        } else {
                            delegate.activityDone(false, "Server not responding.\nError Code: " + response.code(), null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RequestResponse> call, Throwable t) {
                        if (t instanceof ConnectException) {
                            delegate.activityDone(false, "Cannot connect to server, Please check your connection.", null);
                        } else if (t instanceof SocketTimeoutException) {
                            delegate.activityDone(false, "Cannot connect to server. Please try again later. ", null);
                        } else {
                            delegate.activityDone(false, "An Error has occurred! \nError: " + t.getMessage(), null);
                            t.printStackTrace();
                        }
                    }

                });
            } catch (Exception ex) {
                delegate.activityDone(false, "An Error has occurred! \nError: " + ex.getMessage(), null);
            }
        }).start();
    }

    public void getBrandsByPageSorted(int page, int size, Sort sort, RepoInterface delegate) {
        new Thread(() -> {
            try {


                Call<RequestResponse> call = brandAPI.getBrandsByPageSorted(page, size, sort.getSortType(), sort.getSortBy());
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            delegate.activityDone(true, "Request Successful.", Helper.CastToBrandList(response.body().getList()));
                        } else {
                            delegate.activityDone(false, "Server not responding.\nError Code: " + response.code(), null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RequestResponse> call, Throwable t) {
                        if (t instanceof ConnectException) {
                            delegate.activityDone(false, "Cannot connect to server, Please check your connection.", null);
                        } else if (t instanceof SocketTimeoutException) {
                            delegate.activityDone(false, "Cannot connect to server. Please try again later. ", null);
                        } else {
                            delegate.activityDone(false, "An Error has occurred! \nError: " + t.getMessage(), null);
                            t.printStackTrace();
                        }
                    }

                });
            } catch (Exception ex) {
                delegate.activityDone(false, "An Error has occurred! \nError: " + ex.getMessage(), null);
            }
        }).start();
    }


    public void searchBrand(String search, int size, RepoInterface delegate) {
        new Thread(() -> {
            try {
                Call<RequestResponse> call = brandAPI.searchBrand(search, size);
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {

                            delegate.activityDone(true, "Request Successful.", Helper.CastToBrandList(response.body().getList()));
                        } else {
                            delegate.activityDone(false, "Server not responding.\nError Code: " + response.code(), null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RequestResponse> call, Throwable t) {
                        if (t instanceof ConnectException) {
                            delegate.activityDone(false, "Cannot connect to server, Please check your connection.", null);
                        } else if (t instanceof SocketTimeoutException) {
                            delegate.activityDone(false, "Cannot connect to server. Please try again later. ", null);
                        } else {
                            delegate.activityDone(false, "An Error has occurred! \nError: " + t.getMessage(), null);
                            t.printStackTrace();
                        }
                    }
                });
            } catch (Exception ex) {
                delegate.activityDone(false, "An Error has occurred! \nError: " + ex.getMessage(), null);
            }
        }).start();
    }


    /*============================================================================
                                    POST REQUEST
    ============================================================================= */

    public void createBrand(Brand brand, RepoInterface delegate) {
        new Thread(() -> {
            RequestBody brandName = RequestBody.create(brand.getName(), MediaType.parse("text/plain"));
            RequestBody description = RequestBody.create(brand.getDescription(), MediaType.parse("text/plain"));

            Call<RequestResponse> call = brandAPI.createBrand(brandName, description);
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {

                        System.out.println("Response Body: " + response.body().getList());

                        delegate.activityDone(response.body().getSuccess(), response.body().getMessage(), Helper.CastToBrandList(response.body().getList()));
                    } else {
                        delegate.activityDone(false, "Server not responding.\nError Code: " + response.code(), null);
                    }
                }

                @Override
                public void onFailure(Call<RequestResponse> call, Throwable t) {
                    if (t instanceof ConnectException) {
                        delegate.activityDone(false, "Cannot connect to server, Please check your connection.", null);
                    } else if (t instanceof SocketTimeoutException) {
                        delegate.activityDone(false, "Cannot connect to server. Please try again later. ", null);
                    } else {
                        delegate.activityDone(false, "An Error has occurred! \nError: " + t.getMessage(), null);
                        t.printStackTrace();
                    }
                }
            });
        }).start();

    }

    /*============================================================================
                                    POST REQUEST
    ============================================================================= */
    public void updateBrand(int brand_id,Brand brand, RepoInterface delegate) {
        new Thread(() -> {
            RequestBody brandName = RequestBody.create(brand.getName(), MediaType.parse("text/plain"));
            RequestBody description = RequestBody.create(brand.getDescription(), MediaType.parse("text/plain"));

            Call<RequestResponse> call = brandAPI.updateBrand(brand_id,brandName, description);
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {

                        delegate.activityDone(response.body().getSuccess(), response.body().getMessage(), Helper.CastToBrandList(response.body().getList()));
                    } else {
                        delegate.activityDone(false, "Server not responding.\nError Code: " + response.code(), null);
                    }
                }

                @Override
                public void onFailure(Call<RequestResponse> call, Throwable t) {
                    if (t instanceof ConnectException) {
                        delegate.activityDone(false, "Cannot connect to server, Please check your connection.", null);
                    } else if (t instanceof SocketTimeoutException) {
                        delegate.activityDone(false, "Cannot connect to server. Please try again later. ", null);
                    } else {
                        delegate.activityDone(false, "An Error has occurred! \nError: " + t.getMessage(), null);
                        t.printStackTrace();
                    }
                }
            });
        }).start();

    }


}

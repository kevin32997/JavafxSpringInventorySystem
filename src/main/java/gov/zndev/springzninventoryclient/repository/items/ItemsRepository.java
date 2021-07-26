package gov.zndev.springzninventoryclient.repository.items;

import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Item;
import gov.zndev.springzninventoryclient.models.others.Sort;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import gov.zndev.springzninventoryclient.repository.other.RequestResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.util.List;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;

@Service
public class ItemsRepository {

    private Retrofit retrofit;
    private ItemAPI itemsApiInterface;

    public ItemsRepository() {

        retrofit = new Retrofit.Builder()
                .baseUrl(ResourceHelper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        itemsApiInterface = retrofit.create(ItemAPI.class);
    }


    /*============================================================================
                                    GET REQUEST
    ============================================================================= */

    public void getItemById(int itemId, RepoInterface delegate) {
        new Thread(() -> {
            try {

                Call<RequestResponse> call = itemsApiInterface.getItemById(itemId);
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            List itemList = Helper.CastToItemList(response.body().getList());
                            delegate.activityDone(true, "Request Successful.", itemList);
                        } else {
                            delegate.activityDone(false, "Server Error.\nError Code: " + response.code(), null);
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


    public void getItemsByPage(int page, int size, Sort sort, RepoInterface delegate) {
        new Thread(() -> {
            try {
                Call<RequestResponse> call = itemsApiInterface.getItemsByPage(page, size, sort.getSortBy(), sort.getSortType());
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            List itemList = Helper.CastToItemList(response.body().getList());
                            delegate.activityDone(true, "Request Successful.", itemList);
                        } else {
                            delegate.activityDone(false, "Server Error.\nError Code: " + response.code(), null);
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


    public void getItemsCount(RepoInterface delegate) {
        new Thread(() -> {
            Call<Map<String, String>> call = itemsApiInterface.getItemsCount();
            call.enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.code() == 200) {
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

    public void getItemsCountByTag(String tag, int brandId, RepoInterface delegate) {
        new Thread(() -> {
            Call<Map<String, String>> call = itemsApiInterface.getItemsCountByTag(tag, brandId);
            call.enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {

                    if (response.code() == 200) {
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


    public void searchItem(String searched_text, int limit, RepoInterface delegate) {
        new Thread(() -> {
            try {

                Call<RequestResponse> call = itemsApiInterface.searchItem(searched_text, limit);
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            List itemList = Helper.CastToItemList(response.body().getList());
                            delegate.activityDone(true, "Request Successful.", itemList);
                        } else {
                            delegate.activityDone(false, "Server Error.\nError Code: " + response.code(), null);
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

    public void getItemImage(int item_id, RepoInterface delegate) {
        new Thread(() -> {
            Call<Map<String, String>> call = itemsApiInterface.getItemsCount();
            call.enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.code() == 200) {
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


    public void getItemsPageByTag(String tag, int brandId, int page, int size, RepoInterface delegate) {
        new Thread(() -> {
            try {

                Call<RequestResponse> call = itemsApiInterface.getItemsPageByTag(tag, brandId, page, size);
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            List itemList = Helper.CastToItemList(response.body().getList());
                            delegate.activityDone(true, "Request Successful.", itemList);
                        } else {
                            delegate.activityDone(false, "Server Error.\nError Code: " + response.code(), null);
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

    public void createItem(Item item, RepoInterface delegate) {
        new Thread(() -> {

            RequestBody name = RequestBody.create(item.getName(), MediaType.parse("text/plain"));
            RequestBody brandId = RequestBody.create("" + item.getBrandId(), MediaType.parse("text/plain"));
            RequestBody typeId = RequestBody.create("" + item.getTypeId(), MediaType.parse("text/plain"));
            RequestBody model = RequestBody.create("" + item.getModel(), MediaType.parse("text/plain"));
            RequestBody createdBy = RequestBody.create("" + item.getCreatedBy(), MediaType.parse("text/plain"));
            RequestBody description = RequestBody.create(item.getDescription(), MediaType.parse("text/plain"));

            Call<RequestResponse> call = itemsApiInterface.createItem(name, brandId, typeId, model, createdBy, description);
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {

                        System.out.println("ItemRepository:createItem(): " + response.body().getList().toString());
                        delegate.activityDone(response.body().getSuccess(), response.body().getMessage(), Helper.CastToItemList(response.body().getList()));
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


    public void updateItemImage(int itemId, File image_file, RepoInterface delegate) {
        new Thread(() -> {

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), image_file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", image_file.getName(), requestFile);

            Call<RequestResponse> call = itemsApiInterface.updateItemImage(body, itemId);
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {

                        delegate.activityDone(response.body().getSuccess(), response.body().getMessage(), response.body().getList());
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
                                    PUT REQUEST
    ============================================================================= */

    public void updateItem(Item item, RepoInterface delegate) {
        new Thread(() -> {
            RequestBody name = RequestBody.create(item.getName(), MediaType.parse("text/plain"));
            RequestBody brandId = RequestBody.create("" + item.getBrandId(), MediaType.parse("text/plain"));
            RequestBody typeId = RequestBody.create("" + item.getTypeId(), MediaType.parse("text/plain"));
            RequestBody model = RequestBody.create("" + item.getModel(), MediaType.parse("text/plain"));

            Call<RequestResponse> call = itemsApiInterface.updateItem(item.getId(), name, brandId, typeId, model);
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {
                        System.out.println("ItemRepository:updateItem(): " + response.body().getList().toString());
                        delegate.activityDone(response.body().getSuccess(), response.body().getMessage(), Helper.CastToItemList(response.body().getList()));
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

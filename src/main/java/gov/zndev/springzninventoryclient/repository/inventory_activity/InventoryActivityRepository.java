package gov.zndev.springzninventoryclient.repository.inventory_activity;

import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.InventoryActivity;
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
public class InventoryActivityRepository {

    private Retrofit retrofit;
    private InventoryActivityAPI inventoryActivityAPI;

    public InventoryActivityRepository() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ResourceHelper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        inventoryActivityAPI = retrofit.create(InventoryActivityAPI.class);
    }

    /*============================================================================
                                   GET REQUEST
    ============================================================================= */
    public void getInvActivityById(int activity_id, RepoInterface delegate) {
        new Thread(() -> {
            try {
                Call<RequestResponse> call = inventoryActivityAPI.getInvActivityById(activity_id);
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            delegate.activityDone(true, "Request Successful.", Helper.CastToInvActivityReference(response.body().getList()));
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

    public void getInvActivityCount(RepoInterface delegate) {
        new Thread(() -> {
            Call<Map<String, String>> call = inventoryActivityAPI.getInvActivityCount();
            call.enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    System.out.println("response Code: " + response.code());
                    if (response.code() == 200) {
                        System.out.println("Body: " + response.body().toString());
                        Map<String, String> responseBody = response.body();
                        int count = Integer.parseInt(responseBody.get("count").toString());
                        delegate.activityDone(true, "Reference Counted.", count);
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

    public void getInvActivityLastId(RepoInterface delegate) {
        new Thread(() -> {
            Call<Map<String, String>> call = inventoryActivityAPI.getInvActivityLastId();
            call.enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.code() == 200) {
                        Map<String, String> responseBody = response.body();
                        int count = Integer.parseInt(responseBody.get("last_id").toString());
                        delegate.activityDone(true, response.message(), count);
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

    public void getInvActivityByPage(int page, int size, RepoInterface delegate) {
        new Thread(() -> {
            try {


                Call<RequestResponse> call = inventoryActivityAPI.getInvActivityByPage(page, size);
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            delegate.activityDone(true, "Request Successful.", Helper.CastToInventoryActivity(response.body().getList()));
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

    public void getInvActivityPageByTag(String tag, int id, int page, int size, RepoInterface delegate) {
        new Thread(() -> {
            try {

                Call<RequestResponse> call = inventoryActivityAPI.getInvActivityPageByTag(tag, id, page, size);
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            delegate.activityDone(true, "Request Successful.", Helper.CastToInventoryActivity((response.body().getList())));
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

    public void getAllInvActivityByTagId(String tag, int id, RepoInterface delegate) {
        new Thread(() -> {
            try {
                Call<RequestResponse> call = inventoryActivityAPI.getAllInvActivityByTagId(tag, id);
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            delegate.activityDone(true, "Request Successful.", Helper.CastToInventoryActivity(response.body().getList()));
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



    public void getInvActCountByTag(String tag, int id, RepoInterface delegate) {
        new Thread(() -> {
            Call<Map<String, Integer>> call = inventoryActivityAPI.getInvActCountByTag(tag, id);
            call.enqueue(new Callback<Map<String, Integer>>() {
                @Override
                public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                    if (response.code() == 200) {
                        Map<String, Integer> responseBody = response.body();
                        int count = response.body().get("count");
                        delegate.activityDone(true, "Location Inventory Counted.", count);
                    } else {
                        delegate.activityDone(false, "Server Error.\nError Code: " + response.code(), null);
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
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


    public void createInventoryActivity(InventoryActivity activity, RepoInterface delegate) {
        new Thread(() -> {
            RequestBody itemId = RequestBody.create("" + activity.getItemId(), MediaType.parse("text/plain"));
            RequestBody inventoryId = RequestBody.create("" + activity.getInventoryId(), MediaType.parse("text/plain"));
            RequestBody referenceId = RequestBody.create("" + activity.getReferenceId(), MediaType.parse("text/plain"));
            RequestBody locationId = RequestBody.create("" + activity.getLocationId(), MediaType.parse("text/plain"));
            RequestBody otherLocationId = RequestBody.create("" + activity.getOtherLocationId(), MediaType.parse("text/plain"));
            RequestBody total = RequestBody.create("" + activity.getTotal(), MediaType.parse("text/plain"));
            RequestBody action = RequestBody.create(activity.getAction(), MediaType.parse("text/plain"));

            Call<RequestResponse> call = inventoryActivityAPI.createInvActivity(itemId, inventoryId, referenceId, locationId, otherLocationId, total, action);
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {
                        delegate.activityDone(response.body().getSuccess(), response.body().getMessage(),Helper.CastToInventoryActivity( response.body().getList()));
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

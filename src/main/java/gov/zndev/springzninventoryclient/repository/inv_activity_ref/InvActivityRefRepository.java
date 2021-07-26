package gov.zndev.springzninventoryclient.repository.inv_activity_ref;

import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.InventoryActivityReference;
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
public class InvActivityRefRepository {
    private Retrofit retrofit;
    private InvActivityReferenceAPI referenceAPI;

    public InvActivityRefRepository() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ResourceHelper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        referenceAPI = retrofit.create(InvActivityReferenceAPI.class);
    }

    /*============================================================================
                                   GET REQUEST
    ============================================================================= */

    public void getInvActivityRefById(int reference_id, RepoInterface delegate) {
        new Thread(() -> {
            try {
                Call<RequestResponse> call = referenceAPI.getInvActivityRefById(reference_id);
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


    public void getInvActivityRefLastId(RepoInterface delegate) {
        new Thread(() -> {
            Call<Map<String, String>> call = referenceAPI.getInvActReferenceLastId();
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


    public void getInvActivityRefByPage(int page, int size, RepoInterface delegate) {
        new Thread(() -> {
            try {


                Call<RequestResponse> call = referenceAPI.getInvActivityRefByPage(page, size);
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


    public void searchInvActivityRef(String search, int size, RepoInterface delegate) {
        new Thread(() -> {
            try {
                Call<RequestResponse> call = referenceAPI.searchInvActivityRef(search, size);
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

    public void getInvActivityRefByLocation(int locationId, int page, int size, RepoInterface delegate) {
        new Thread(() -> {
            try {


                Call<RequestResponse> call = referenceAPI.getInvActReferenceByLocation(locationId, page, size);
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

    // Counts ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void getInvActivityRefCount(RepoInterface delegate) {
        new Thread(() -> {
            Call<Map<String, String>> call = referenceAPI.getInvActivityRefCount();
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

    public void getInvActivityRefCountByLocation(int locationId, RepoInterface delegate) {
        new Thread(() -> {
            Call<Map<String, Integer>> call = referenceAPI.getInvActReferenceCountByLocation(locationId);
            call.enqueue(new Callback<Map<String, Integer>>() {
                @Override
                public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                    System.out.println("response Code: " + response.code());
                    if (response.code() == 200) {
                        System.out.println("Body: " + response.body().toString());
                        Map<String, Integer> responseBody = response.body();
                        int count = responseBody.get("count");
                        delegate.activityDone(true, "Reference Counted.", count);
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
    =============================================================================*/

    public void createInvActivityRef(InventoryActivityReference ref, RepoInterface delegate) {
        new Thread(() -> {
            RequestBody userId = RequestBody.create("" + ref.getUserId(),MediaType.parse("text/plain") );
            RequestBody reference = RequestBody.create( ref.getReference(),MediaType.parse("text/plain"));
            RequestBody consignee = RequestBody.create( ref.getConsignee(),MediaType.parse("text/plain"));
            RequestBody remarks = RequestBody.create( ref.getRemarks(), MediaType.parse("text/plain"));
            RequestBody location = RequestBody.create( "" + ref.getLocationId(),MediaType.parse("text/plain"));
            RequestBody locationFrom = RequestBody.create( "" + ref.getLocationFromId(),MediaType.parse("text/plain"));
            RequestBody action = RequestBody.create( ref.getAction(),MediaType.parse("text/plain"));

            Call<RequestResponse> call = referenceAPI.createInvActivityRef(userId, reference, consignee, remarks, location, locationFrom, action);
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {
                        System.out.println("Body is " + response.body().getList().toString());
                        delegate.activityDone(response.body().getSuccess(), response.body().getMessage(), Helper.CastToInvActivityReference(response.body().getList()));
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
    =============================================================================*/

}

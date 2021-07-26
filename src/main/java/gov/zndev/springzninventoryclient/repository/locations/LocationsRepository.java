package gov.zndev.springzninventoryclient.repository.locations;

import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Location;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import gov.zndev.springzninventoryclient.repository.other.RequestResponse;
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
public class LocationsRepository {

    private Retrofit retrofit;
    private LocationApi locationApi;

    public LocationsRepository() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ResourceHelper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        locationApi = retrofit.create(LocationApi.class);
    }

    /*============================================================================
                                   GET REQUEST
    ============================================================================= */

    public void getLocationById(int location_id, RepoInterface delegate) {
        new Thread(() -> {
            try {
                Call<RequestResponse> call = locationApi.getLocationById(location_id);
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            delegate.activityDone(true, "Request Successful.", Helper.CastToLocationList(response.body().getList()));
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

    public void getAllLocation(RepoInterface delegate) {
        new Thread(() -> {
            try {
                Call<RequestResponse> call = locationApi.getAllLocation();
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            System.out.println("LocationsRepository: getAllLocation(): response list: " + response.body().getList());
                            delegate.activityDone(true, "Request Successful.", Helper.CastToLocationList(response.body().getList()));
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


    public void getLocationCount(RepoInterface delegate) {
        new Thread(() -> {
            Call<Map<String, String>> call = locationApi.getLocationsCount();
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


    public void getLocationsByPage(int page, int size, RepoInterface delegate) {
        new Thread(() -> {
            try {
                Call<RequestResponse> call = locationApi.getLocationByPage(page, size);
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            delegate.activityDone(true, "Request Successful.", Helper.CastToLocationList(response.body().getList()));
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


    public void searchLocation(String search, int size, RepoInterface delegate) {
        new Thread(() -> {
            try {
                Call<RequestResponse> call = locationApi.searchLocation(search, size);
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {

                            delegate.activityDone(true, "Request Successful.", Helper.CastToLocationList(response.body().getList()));
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

    public void createLocation(Location location, RepoInterface delegate) {
        new Thread(() -> {
            RequestBody locationName = RequestBody.create(location.getName(), MediaType.parse("text/plain"));
            RequestBody description = RequestBody.create(location.getDescription(), MediaType.parse("text/plain"));
            Call<RequestResponse> call = locationApi.createLocation(locationName, description);
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {
                        delegate.activityDone(response.body().getSuccess(), response.body().getMessage(), Helper.CastToLocationList(response.body().getList()));
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

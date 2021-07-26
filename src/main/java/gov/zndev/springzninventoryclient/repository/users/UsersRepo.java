package gov.zndev.springzninventoryclient.repository.users;


import gov.zndev.springzninventoryclient.helpers.Helper;
import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.User;
import gov.zndev.springzninventoryclient.models.others.Sort;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import gov.zndev.springzninventoryclient.repository.other.RequestResponse;
import okhttp3.MediaType;
import okhttp3.RequestBody;
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
public class UsersRepo {

    private Retrofit retrofit;
    private UsersApi usersApi;

    public UsersRepo() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ResourceHelper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        usersApi = retrofit.create(UsersApi.class);
    }

    private void closeRepository() {

    }


    /*==================================================================================================================
                                                      POST REQUEST
    ==================================================================================================================*/

    public void createUser(User user, RepoInterface delegate) {
        new Thread(() -> {

            RequestBody fullname = RequestBody.create(user.getFullname(), MediaType.parse("text/plain"));
            RequestBody username = RequestBody.create(user.getUsername(), MediaType.parse("text/plain"));
            RequestBody password = RequestBody.create(user.getPassword(), MediaType.parse("text/plain"));
            RequestBody userRole = RequestBody.create(user.getUserRole(), MediaType.parse("text/plain"));

            Call<RequestResponse> call = usersApi.createUser(fullname, username, password, userRole);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {

                        System.out.println("Response Body: " + response.body().getList());

                        delegate.activityDone(response.body().getSuccess(), response.body().getMessage(), Helper.CastToUserList(response.body().getList()));
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

    /*==================================================================================================================
                                                       GET REQUEST
    ==================================================================================================================*/


    // Counts //////////////////////////

    public void getUserCount(RepoInterface delegate) {
        try {
            Call<Map<String, Long>> call = usersApi.getUsersCount();
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Map<String, Long>> call, Response<Map<String, Long>> response) {
                    System.out.println("response Code: " + response.code());
                    if (response.code() == 200) {
                        System.out.println("Get User Count Body: " + response.body().toString());
                        Map<String, Long> responseBody = response.body();
                        int count = responseBody.get("count").intValue();
                        delegate.activityDone(true, "Users Counted.", count);
                    } else {
                        delegate.activityDone(false, "Server Error.\nError Code: " + response.code(), null);
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Long>> call, Throwable t) {
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
    }

    // By Page ////////////////////////////////////

    public void getUserByPageSorted(int page, int size, Sort sort, RepoInterface delegate) {
        try {
            Call<RequestResponse> call = usersApi.getUserByPageSorted(page, size, sort.getSortType(), sort.getSortBy());
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {
                        delegate.activityDone(true, "Request Successful.", Helper.CastToUserList(response.body().getList()));
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
    }

    public void checkUser(String username, String password, RepoInterface delegate) {
        try {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ResourceHelper.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UsersApi usersApi = retrofit.create(UsersApi.class);
            Call<RequestResponse> call = usersApi.checkUser(username, password);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {
                        delegate.activityDone(response.body().getSuccess(), response.body().getMessage(), Helper.CastToUserList(response.body().getList()));
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
    }

    public void getUserById(int id, RepoInterface delegate) {
        try {
            Call<RequestResponse> call = usersApi.getUserById(id);
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {

                        delegate.activityDone(true, "Request Successful.", Helper.CastToUserList(response.body().getList()));
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
    }

    /*==================================================================================================================
                                                       PUT REQUEST
    ==================================================================================================================*/

    public void updateUser(int userId,User user, RepoInterface delegate) {
        new Thread(() -> {

            RequestBody fullname = RequestBody.create(user.getFullname(), MediaType.parse("text/plain"));
            RequestBody username = RequestBody.create(user.getUsername(), MediaType.parse("text/plain"));
            RequestBody password = RequestBody.create(user.getPassword(), MediaType.parse("text/plain"));
            RequestBody userRole = RequestBody.create(user.getUserRole(), MediaType.parse("text/plain"));

            Call<RequestResponse> call = usersApi.updateUser(userId,fullname, username, password, userRole);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {

                        System.out.println("Response Body: " + response.body().getList());

                        delegate.activityDone(response.body().getSuccess(), response.body().getMessage(), Helper.CastToUserList(response.body().getList()));
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

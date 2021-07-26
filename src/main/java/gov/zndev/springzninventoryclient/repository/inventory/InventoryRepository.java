package gov.zndev.springzninventoryclient.repository.inventory;

import gov.zndev.springzninventoryclient.helpers.ResourceHelper;
import gov.zndev.springzninventoryclient.models.Inventory;
import gov.zndev.springzninventoryclient.models.others.Sort;
import gov.zndev.springzninventoryclient.repository.other.RepoInterface;
import gov.zndev.springzninventoryclient.repository.other.RequestResponse;
import gov.zndev.springzninventoryclient.helpers.Helper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

@Component
public class InventoryRepository {

    private Retrofit retrofit;
    private InventoryAPI inventoryAPI;

    public InventoryRepository() {

        retrofit = new Retrofit.Builder()
                .baseUrl(ResourceHelper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        inventoryAPI = retrofit.create(InventoryAPI.class);
    }


    /*============================================================================
                                    GET REQUEST
    ============================================================================= */

    public void getInventoryById(int id, RepoInterface delegate) {
        new Thread(() -> {
            try {
                Call<RequestResponse> call = inventoryAPI.getInventoryById(id);
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            List itemList = Helper.CastToInventoryList(response.body().getList());
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


    public void getInventoryByTag(String tag, int id, RepoInterface delegate) {
        new Thread(() -> {
            try {
                Call<RequestResponse> call = inventoryAPI.getInventoryByTag(tag, id);
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            List itemList = Helper.CastToInventoryList(response.body().getList());
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


    public void getInventoryByItemAndLocation(int itemId, int locationId, RepoInterface delegate) {
        new Thread(() -> {
            try {
                Call<RequestResponse> call = inventoryAPI.getInventoryByItemAndLocation(itemId, locationId);
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            List itemList = Helper.CastToInventoryList(response.body().getList());
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

    public void getInventoryByTagPageSorted(String tag, int tagId, int page, int size, Sort sort, RepoInterface delegate) {
        new Thread(() -> {
            try {
                Call<RequestResponse> call = inventoryAPI.getInventoryByTagPageSorted(tag,tagId,page,size,sort.getSortType(),sort.getSortBy());
                call.enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        if (response.code() == 200) {
                            List itemList = Helper.CastToInventoryList(response.body().getList());
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



    public void getInventoryCount(RepoInterface delegate) {
        new Thread(() -> {
            Call<Map<String, Integer>> call = inventoryAPI.getInventoryCount();
            call.enqueue(new Callback<Map<String, Integer>>() {
                @Override
                public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                    if (response.code() == 200) {
                        Map<String, Integer> responseBody = response.body();
                        int count = response.body().get("count");
                        delegate.activityDone(true, "Inventory Counted.", count);
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


    public void getInventoryCountByTag(String tag, int id, RepoInterface delegate) {
        new Thread(() -> {
            Call<Map<String, Integer>> call = inventoryAPI.getInventoryCountByTag(tag, id);
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


    /**********************************
     Returns total Item inventory count

     Ex. code:

     int count=0;
     for(Inventory inv:list){
     count+=inv.getQuantity;
     }

     return count;
     **********************************/
    public void getInventoryTotalItemCountByTag(String tag, int id, RepoInterface delegate) {
        new Thread(() -> {
            Call<Map<String, Integer>> call = inventoryAPI.getInventoryTotalItemCountByTag(tag, id);
            call.enqueue(new Callback<>() {
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


    public void getInventoryPageByLocation(int locationId, int page, int size, RepoInterface delegate) {
        new Thread(() -> {
            Call<RequestResponse> call = inventoryAPI.getInventoryPageByLocation(locationId, page, size);
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {

                        delegate.activityDone(true, "Location Inventory Counted.", Helper.CastToInventoryList(response.body().getList()));
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

        }).start();
    }

    public void searchInventoryItemByLocation(String search, int locationId, int limit, RepoInterface delegate) {
        new Thread(() -> {
            Call<RequestResponse> call = inventoryAPI.searchItemInventoryByLocation(search, locationId, limit);
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {

                    if (response.code() == 200) {
                        delegate.activityDone(true, response.message(), Helper.CastToInventoryList(response.body().getList()));
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

        }).start();
    }

    public void searchLocationItemByNameOrSerial(int locationId, String searched, RepoInterface delegate) {
        new Thread(() -> {
            Call<RequestResponse> call = inventoryAPI.searchLocationItemByNameOrSerial(locationId, searched);
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {

                    if (response.code() == 200) {
                        delegate.activityDone(true, response.message(), Helper.CastToInventoryList(response.body().getList()));
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

        }).start();
    }


    public void getInventoryTotalItemCountByItemAndLocation(int itemId, int locationId, RepoInterface delegate) {
        new Thread(() -> {
            Call<Map<String, Integer>> call = inventoryAPI.getInventoryTotalItemCountByItemAndLocation(itemId, locationId);
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


    public void getInventoryByPage(int page, int size, RepoInterface delegate) {
        new Thread(() -> {
            Call<RequestResponse> call = inventoryAPI.getInventoryByPage(page, size);
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {
                        delegate.activityDone(true, "Location Inventory Counted.", Helper.CastToInventoryList(response.body().getList()));
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

        }).start();
    }

    /*============================================================================
                                    POST REQUEST
    ============================================================================= */

    public void createItemInventory(Inventory inventory, RepoInterface delegate) {
        new Thread(() -> {

            RequestBody itemId = RequestBody.create("" + inventory.getItemId(), MediaType.parse("text/plain"));
            RequestBody locationId = RequestBody.create("" + inventory.getLocationId(), MediaType.parse("text/plain"));
            RequestBody propertyNumber = RequestBody.create("" + inventory.getPropertyNumber(), MediaType.parse("text/plain"));
            RequestBody serialNumber = RequestBody.create("" + inventory.getSerialNumber(), MediaType.parse("text/plain"));
            RequestBody code = RequestBody.create("" + inventory.getCode(), MediaType.parse("text/plain"));
            RequestBody createdBy = RequestBody.create("" + inventory.getCreatedBy(), MediaType.parse("text/plain"));

            Call<RequestResponse> call = inventoryAPI.createInventory(itemId, locationId, propertyNumber, serialNumber, code, createdBy);
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {
                        delegate.activityDone(response.body().getSuccess(), response.body().getMessage(), Helper.CastToInventoryList(response.body().getList()));
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


    public void updateInventoryDetails(int inventory_id, Inventory details, RepoInterface delegate) {
        new Thread(() -> {

            RequestBody serialNumber = RequestBody.create("" + details.getSerialNumber(), MediaType.parse("text/plain"));
            RequestBody propertyNumber = RequestBody.create("" + details.getPropertyNumber(), MediaType.parse("text/plain"));
            RequestBody code = RequestBody.create("" + details.getCode(), MediaType.parse("text/plain"));

            Call<RequestResponse> call = inventoryAPI.updateInventoryDetails(inventory_id, serialNumber, code, propertyNumber);
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {
                        delegate.activityDone(response.body().getSuccess(), response.body().getMessage(), Helper.CastToInventoryList(response.body().getList()));
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

    public void updateInventoryLocation(int inventory_id, Inventory details, RepoInterface delegate) {
        new Thread(() -> {

            RequestBody locationId = RequestBody.create("" + details.getLocationId(), MediaType.parse("text/plain"));
            Call<RequestResponse> call = inventoryAPI.updateInventoryLocation(inventory_id, locationId);
            call.enqueue(new Callback<RequestResponse>() {
                @Override
                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                    if (response.code() == 200) {
                        delegate.activityDone(response.body().getSuccess(), response.body().getMessage(), Helper.CastToInventoryList(response.body().getList()));
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
                                    DELETE REQUEST
    ============================================================================= */


    public void deleteInventory(int inventoryId, RepoInterface delegate) {
        new Thread(() -> {


            Call<RequestResponse> call = inventoryAPI.deleteInventory(inventoryId);
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
}

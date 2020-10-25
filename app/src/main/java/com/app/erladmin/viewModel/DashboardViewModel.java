package com.app.erladmin.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.app.erladmin.ERLApp;
import com.app.erladmin.model.entity.info.ClientInfo;
import com.app.erladmin.model.entity.request.SaveOrderRequest;
import com.app.erladmin.model.entity.response.AddressListResponse;
import com.app.erladmin.model.entity.response.BaseResponse;
import com.app.erladmin.model.entity.response.ChatListResponse;
import com.app.erladmin.model.entity.response.ClientsResponse;
import com.app.erladmin.model.entity.response.GetMessagesResponse;
import com.app.erladmin.model.entity.response.ModuleResponse;
import com.app.erladmin.model.entity.response.OrderResourcesResponse;
import com.app.erladmin.model.entity.response.OrdersResponse;
import com.app.erladmin.model.entity.response.SendMessageResponse;
import com.app.erladmin.model.entity.response.ServiceItemsResponse;
import com.app.erladmin.model.state.DashboardServiceInterface;
import com.app.erladmin.network.RXRetroManager;
import com.app.erladmin.network.RetrofitException;
import com.app.erladmin.util.ResourceProvider;
import com.app.utilities.utils.StringHelper;

import java.io.File;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DashboardViewModel extends BaseViewModel {
    @Inject
    DashboardServiceInterface dashboardServiceInterface;

    private MutableLiveData<BaseResponse> mBaseResponse;
    private MutableLiveData<ClientsResponse> mClientsResponse;
    private MutableLiveData<OrdersResponse> mOrdersResponse;
    private MutableLiveData<ServiceItemsResponse> serviceItemsResponse;
    private MutableLiveData<OrderResourcesResponse> orderResourcesResponse;
    private MutableLiveData<ModuleResponse> moduleResponse;
    private MutableLiveData<AddressListResponse> addressListResponse;
    private MutableLiveData<ChatListResponse> chatListResponse;
    private MutableLiveData<SendMessageResponse> mSendMessageResponse;
    private MutableLiveData<GetMessagesResponse> getMessagesResponse;

    private ClientInfo addClientRequest;
    private SaveOrderRequest saveOrderRequest;

    public DashboardViewModel(ResourceProvider resourceProvider) {
        ERLApp.getServiceComponent().inject(this);
        addClientRequest = new ClientInfo();
        saveOrderRequest = new SaveOrderRequest();
    }

    public void getClientsRequest(boolean isProgress, int limit, int offset, String search) {
        RequestBody limitBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(limit));
        RequestBody offsetBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(offset));
        RequestBody searchBody = RequestBody.create(MediaType.parse("text/plain"), search);

        if (view != null && isProgress) {
            view.showProgress();
        }
        new RXRetroManager<ClientsResponse>() {
            @Override
            protected void onSuccess(ClientsResponse response) {
                if (view != null) {
                    mClientsResponse.postValue(response);
                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
                    view.hideProgress();
                }
            }
        }.rxSingleCall(dashboardServiceInterface.getClients(limitBody, offsetBody, searchBody));
    }

    public void getOrdersRequest(boolean isProgress, int limit, int offset, String search) {
        RequestBody limitBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(limit));
        RequestBody offsetBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(offset));
        RequestBody searchBody = RequestBody.create(MediaType.parse("text/plain"), search);

        if (view != null && isProgress) {
            view.showProgress();
        }
        new RXRetroManager<OrdersResponse>() {
            @Override
            protected void onSuccess(OrdersResponse response) {
                if (view != null) {
                    mOrdersResponse.postValue(response);
                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
                    view.hideProgress();
                }
            }
        }.rxSingleCall(dashboardServiceInterface.getOrders(limitBody, offsetBody, searchBody));
    }

    public void getServiceItemsRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<ServiceItemsResponse>() {
            @Override
            protected void onSuccess(ServiceItemsResponse response) {
                if (view != null) {
                    serviceItemsResponse.postValue(response);
                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
                    view.hideProgress();
                }
            }
        }.rxSingleCall(dashboardServiceInterface.getServiceItems());
    }

    public void getOrderResourcesRequest(int addressId) {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<OrderResourcesResponse>() {
            @Override
            protected void onSuccess(OrderResourcesResponse response) {
                if (view != null) {
                    orderResourcesResponse.postValue(response);
                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
                    view.hideProgress();
                }
            }
        }.rxSingleCall(dashboardServiceInterface.getOrderResources(addressId));
    }

    public void getAllClientsRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<ModuleResponse>() {
            @Override
            protected void onSuccess(ModuleResponse response) {
                if (view != null) {
                    moduleResponse.postValue(response);
                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
                    view.hideProgress();
                }
            }
        }.rxSingleCall(dashboardServiceInterface.getAllClients());
    }

    public void getAddressesRequest(int clientId) {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<AddressListResponse>() {
            @Override
            protected void onSuccess(AddressListResponse response) {
                if (view != null) {
                    addressListResponse.postValue(response);
                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
                    view.hideProgress();
                }
            }
        }.rxSingleCall(dashboardServiceInterface.getAddresses(clientId));
    }

    public void saveAddressRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<BaseResponse>() {
            @Override
            protected void onSuccess(BaseResponse response) {
                if (view != null) {
                    mBaseResponse.postValue(response);
                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
                    view.hideProgress();
                }
            }
        }.rxSingleCall(dashboardServiceInterface.placeOrder(saveOrderRequest));
    }

    public void storeClientRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<BaseResponse>() {
            @Override
            protected void onSuccess(BaseResponse response) {
                if (view != null) {
                    mBaseResponse.postValue(response);
                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
                    view.hideProgress();
                }
            }
        }.rxSingleCall(dashboardServiceInterface.storeClient(addClientRequest));
    }

    public void getChatListRequest(boolean isProgress) {
        if (view != null && isProgress) {
            view.showProgress();
        }
        new RXRetroManager<ChatListResponse>() {
            @Override
            protected void onSuccess(ChatListResponse response) {
                if (view != null) {
                    chatListResponse.postValue(response);
                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
                    view.hideProgress();
                }
            }
        }.rxSingleCall(dashboardServiceInterface.getChatList());
    }

    public void sendMessage(int toUserId,String message, String imagePath, boolean isProgress) {
        RequestBody messageBody = RequestBody.create(MediaType.parse("text/plain"), message);
        RequestBody toUserIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(toUserId));

        MultipartBody.Part imageFileBody = null;
        if (!StringHelper.isEmpty(imagePath)) {
            File file = new File(imagePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            imageFileBody = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        }

        if (view != null && isProgress) {
            view.showProgress();
        }
        new RXRetroManager<SendMessageResponse>() {
            @Override
            protected void onSuccess(SendMessageResponse response) {
                if (view != null) {
                    mSendMessageResponse.postValue(response);
                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
                    view.hideProgress();
                }
            }
        }.rxSingleCall(dashboardServiceInterface.sendMessage(toUserIdBody,messageBody, imageFileBody));
    }

    public void getMessages(int toUserId,int lastMessageId, boolean isProgress) {
        if (view != null && isProgress) {
            view.showProgress();
        }
        new RXRetroManager<GetMessagesResponse>() {
            @Override
            protected void onSuccess(GetMessagesResponse response) {
                if (view != null) {
                    getMessagesResponse.postValue(response);
                    view.hideProgress();
                }
            }

            @Override
            protected void onFailure(RetrofitException retrofitException, String errorCode) {
                super.onFailure(retrofitException, errorCode);
                if (view != null) {
                    view.showApiError(retrofitException, errorCode);
                    view.hideProgress();
                }
            }
        }.rxSingleCall(dashboardServiceInterface.getMessages(toUserId,lastMessageId));
    }

    public MutableLiveData<BaseResponse> mBaseResponse() {
        if (mBaseResponse == null) {
            mBaseResponse = new MutableLiveData<>();
        }
        return mBaseResponse;
    }

    public MutableLiveData<ClientsResponse> mClientsResponse() {
        if (mClientsResponse == null) {
            mClientsResponse = new MutableLiveData<>();
        }
        return mClientsResponse;
    }

    public MutableLiveData<OrdersResponse> mOrderResponse() {
        if (mOrdersResponse == null) {
            mOrdersResponse = new MutableLiveData<>();
        }
        return mOrdersResponse;
    }

    public MutableLiveData<ServiceItemsResponse> serviceItemsResponse() {
        if (serviceItemsResponse == null) {
            serviceItemsResponse = new MutableLiveData<>();
        }
        return serviceItemsResponse;
    }

    public MutableLiveData<OrderResourcesResponse> orderResourcesResponse() {
        if (orderResourcesResponse == null) {
            orderResourcesResponse = new MutableLiveData<>();
        }
        return orderResourcesResponse;
    }

    public MutableLiveData<ModuleResponse> moduleResponse() {
        if (moduleResponse == null) {
            moduleResponse = new MutableLiveData<>();
        }
        return moduleResponse;
    }

    public MutableLiveData<AddressListResponse> addressListResponse() {
        if (addressListResponse == null) {
            addressListResponse = new MutableLiveData<>();
        }
        return addressListResponse;
    }

    public MutableLiveData<ChatListResponse> chatListResponse() {
        if (chatListResponse == null) {
            chatListResponse = new MutableLiveData<>();
        }
        return chatListResponse;
    }

    public MutableLiveData<SendMessageResponse> mSendMessageResponse() {
        if (mSendMessageResponse == null) {
            mSendMessageResponse = new MutableLiveData<>();
        }
        return mSendMessageResponse;
    }

    public MutableLiveData<GetMessagesResponse> getMessagesResponse() {
        if (getMessagesResponse == null) {
            getMessagesResponse = new MutableLiveData<>();
        }
        return getMessagesResponse;
    }

    public ClientInfo getAddClientRequest() {
        return addClientRequest;
    }

    public void setAddClientRequest(ClientInfo addClientRequest) {
        this.addClientRequest = addClientRequest;
    }

    public SaveOrderRequest getSaveOrderRequest() {
        return saveOrderRequest;
    }

    public void setSaveOrderRequest(SaveOrderRequest saveOrderRequest) {
        this.saveOrderRequest = saveOrderRequest;
    }
}

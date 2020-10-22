package com.app.erladmin.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.app.erladmin.ERLApp;
import com.app.erladmin.model.entity.request.AddClientRequest;
import com.app.erladmin.model.entity.request.SaveOrderRequest;
import com.app.erladmin.model.entity.response.BaseResponse;
import com.app.erladmin.model.entity.response.ClientsResponse;
import com.app.erladmin.model.entity.response.OrderResourcesResponse;
import com.app.erladmin.model.entity.response.OrdersResponse;
import com.app.erladmin.model.entity.response.ServiceItemsResponse;
import com.app.erladmin.model.entity.response.UserResponse;
import com.app.erladmin.model.state.DashboardServiceInterface;
import com.app.erladmin.network.RXRetroManager;
import com.app.erladmin.network.RetrofitException;
import com.app.erladmin.util.ResourceProvider;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class DashboardViewModel extends BaseViewModel {
    @Inject
    DashboardServiceInterface dashboardServiceInterface;

    private MutableLiveData<BaseResponse> mBaseResponse;
    private MutableLiveData<ClientsResponse> mClientsResponse;
    private MutableLiveData<OrdersResponse> mOrdersResponse;
    private MutableLiveData<ServiceItemsResponse> serviceItemsResponse;
    private MutableLiveData<OrderResourcesResponse> orderResourcesResponse;

    private AddClientRequest addClientRequest;
    private SaveOrderRequest saveOrderRequest;

    public DashboardViewModel(ResourceProvider resourceProvider) {
        ERLApp.getServiceComponent().inject(this);
        addClientRequest = new AddClientRequest();
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

    public void getOrderResourcesRequest() {
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
        }.rxSingleCall(dashboardServiceInterface.getOrderResources());
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

    public AddClientRequest getAddClientRequest() {
        return addClientRequest;
    }

    public void setAddClientRequest(AddClientRequest addClientRequest) {
        this.addClientRequest = addClientRequest;
    }

    public SaveOrderRequest getSaveOrderRequest() {
        return saveOrderRequest;
    }

    public void setSaveOrderRequest(SaveOrderRequest saveOrderRequest) {
        this.saveOrderRequest = saveOrderRequest;
    }
}

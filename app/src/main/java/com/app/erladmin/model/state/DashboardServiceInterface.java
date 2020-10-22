package com.app.erladmin.model.state;


import com.app.erladmin.model.entity.request.AddClientRequest;
import com.app.erladmin.model.entity.response.BaseResponse;
import com.app.erladmin.model.entity.response.ClientsResponse;
import com.app.erladmin.model.entity.response.OrderResourcesResponse;
import com.app.erladmin.model.entity.response.ServiceItemsResponse;
import com.app.erladmin.model.entity.response.UserResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DashboardServiceInterface {
    @Multipart
    @POST("clients")
    Observable<ClientsResponse> getClients(@Part("limit") RequestBody limit
            , @Part("offset") RequestBody offset, @Part("search") RequestBody search);

    @POST("store-client")
    Observable<BaseResponse> storeClient(@Body AddClientRequest addClientRequest);

    @Multipart
    @POST("orders")
    Observable<ClientsResponse> getOrders(@Part("limit") RequestBody limit
            , @Part("offset") RequestBody offset, @Part("search") RequestBody search);

    @GET("get-service-items")
    Observable<ServiceItemsResponse> getServiceItems();

    @GET("order-resources")
    Observable<OrderResourcesResponse> getOrderResources();
}

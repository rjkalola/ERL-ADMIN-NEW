package com.app.erladmin.model.state;


import com.app.erladmin.model.entity.request.AddClientRequest;
import com.app.erladmin.model.entity.request.SaveOrderRequest;
import com.app.erladmin.model.entity.response.AddressListResponse;
import com.app.erladmin.model.entity.response.BaseResponse;
import com.app.erladmin.model.entity.response.ClientsResponse;
import com.app.erladmin.model.entity.response.ModuleResponse;
import com.app.erladmin.model.entity.response.OrderResourcesResponse;
import com.app.erladmin.model.entity.response.ServiceItemsResponse;

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

    @Multipart
    @POST("working-hours")
    Observable<OrderResourcesResponse> getOrderResources(@Part("address_id") int address_id);

    @GET("all-clients")
    Observable<ModuleResponse> getAllClients();

    @Multipart
    @POST("client-addresses")
    Observable<AddressListResponse> getAddresses(@Part("client_id") int clientId);

    @POST("place-order")
    Observable<BaseResponse> placeOrder(@Body SaveOrderRequest saveOrderRequest);


}

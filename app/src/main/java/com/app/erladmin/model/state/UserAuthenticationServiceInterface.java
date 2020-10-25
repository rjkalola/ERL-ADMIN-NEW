package com.app.erladmin.model.state;


import com.app.erladmin.model.entity.request.LoginRequest;
import com.app.erladmin.model.entity.response.BaseResponse;
import com.app.erladmin.model.entity.response.ModuleResponse;
import com.app.erladmin.model.entity.response.UserResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserAuthenticationServiceInterface {
    @POST("login")
    Observable<UserResponse> login(@Body LoginRequest loginRequest);

    @GET("logout")
    Observable<BaseResponse> logout();

    @Multipart
    @POST("kkm-token")
    Observable<BaseResponse> registerToken(@Part("token") RequestBody token, @Part("device_type") RequestBody device_type);
}

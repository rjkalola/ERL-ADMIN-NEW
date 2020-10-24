package com.app.erladmin.model.state;


import com.app.erladmin.model.entity.request.LoginRequest;
import com.app.erladmin.model.entity.response.BaseResponse;
import com.app.erladmin.model.entity.response.ModuleResponse;
import com.app.erladmin.model.entity.response.UserResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserAuthenticationServiceInterface {
    @POST("login")
    Observable<UserResponse> login(@Body LoginRequest loginRequest);

    @GET("logout")
    Observable<BaseResponse> logout();

}

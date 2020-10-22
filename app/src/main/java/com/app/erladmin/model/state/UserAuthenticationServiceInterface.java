package com.app.erladmin.model.state;


import com.app.erladmin.model.entity.request.AddClientRequest;
import com.app.erladmin.model.entity.request.LoginRequest;
import com.app.erladmin.model.entity.response.UserResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAuthenticationServiceInterface {
    @POST("login")
    Observable<UserResponse> login(@Body LoginRequest loginRequest);
}

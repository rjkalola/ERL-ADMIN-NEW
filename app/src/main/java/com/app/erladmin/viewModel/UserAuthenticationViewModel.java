package com.app.erladmin.viewModel;

import androidx.lifecycle.MutableLiveData;

import com.app.erladmin.ERLApp;
import com.app.erladmin.model.entity.request.AddClientRequest;
import com.app.erladmin.model.entity.request.LoginRequest;
import com.app.erladmin.model.entity.response.BaseResponse;
import com.app.erladmin.model.entity.response.UserResponse;
import com.app.erladmin.model.state.UserAuthenticationServiceInterface;
import com.app.erladmin.network.RXRetroManager;
import com.app.erladmin.network.RetrofitException;
import com.app.erladmin.util.ResourceProvider;

import javax.inject.Inject;

public class UserAuthenticationViewModel extends BaseViewModel {
    @Inject
    UserAuthenticationServiceInterface userAuthenticationServiceInterface;

    private MutableLiveData<UserResponse> mUserResponse;
    private MutableLiveData<BaseResponse> mBaseResponse;
    private LoginRequest loginRequest;


    public UserAuthenticationViewModel(ResourceProvider resourceProvider) {
        ERLApp.getServiceComponent().inject(this);
        loginRequest = new LoginRequest();

    }

    public void sendLoginRequest() {
        if (view != null) {
            view.showProgress();
        }
        new RXRetroManager<UserResponse>() {
            @Override
            protected void onSuccess(UserResponse response) {
                if (view != null) {
                    mUserResponse.postValue(response);
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
        }.rxSingleCall(userAuthenticationServiceInterface.login(loginRequest));
    }

    public MutableLiveData<BaseResponse> mBaseResponse() {
        if (mBaseResponse == null) {
            mBaseResponse = new MutableLiveData<>();
        }
        return mBaseResponse;
    }

    public MutableLiveData<UserResponse> mUserResponse() {
        if (mUserResponse == null) {
            mUserResponse = new MutableLiveData<>();
        }
        return mUserResponse;
    }

    public LoginRequest getLoginRequest() {
        return loginRequest;
    }

    public void setLoginRequest(LoginRequest loginRequest) {
        this.loginRequest = loginRequest;
    }

}

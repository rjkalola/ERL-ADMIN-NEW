package com.app.erladmin.callback;

import com.app.erladmin.network.RetrofitException;

public interface ViewListener {

    void showProgress();

    void hideProgress();

    void showApiError(RetrofitException retrofitException, String errorCode);
}

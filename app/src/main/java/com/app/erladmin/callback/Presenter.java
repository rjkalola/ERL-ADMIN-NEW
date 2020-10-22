package com.app.erladmin.callback;

public interface Presenter<T> {

    void createView(T view);

    void destroyView();
}

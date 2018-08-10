package com.github.neone35.singspots.asynctasks;


public interface OnAsyncEventListener<T> {
    void onSuccess(T object);

    void onFailure(Exception e);
}

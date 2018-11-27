package com.innowave.mobiletest.callbacks;

public interface ResponseHandler {
    <T>void onSuccess(Object response, int reqCode);
    void onFailure(Throwable t, int reqCode);
}
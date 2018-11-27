package com.innowave.mobiletest.apis;

import android.content.Context;
import android.widget.Toast;

 import com.innowave.mobiletest.callbacks.ResponseHandler;
import com.innowave.mobiletest.utils.Utility;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RestCaller {
    private int REQUEST_CODE = 0;
    ResponseHandler handler;

    public <T> RestCaller(Context context, ResponseHandler handler, Observable<T> call, final int REQUEST_CODE) throws NumberFormatException {
        if (Utility.isNetworkAvailable(context)) {
            if (REQUEST_CODE <= 0) {
                NumberFormatException ex = new NumberFormatException();
                throw ex;
            }
            this.REQUEST_CODE = REQUEST_CODE;
            this.handler = handler;
            callApi(call);
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }


    private <T> void callApi(Observable<T> call) {

        call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(T bookingResults) {
                        handler.onSuccess(bookingResults, REQUEST_CODE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.onFailure(e, REQUEST_CODE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}

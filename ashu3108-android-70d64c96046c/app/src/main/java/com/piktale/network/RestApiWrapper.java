package com.piktale.network;

import com.piktale.models.BasicResponse;

import retrofit2.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RestApiWrapper {

    public static Subscription call(Observable observable, CallbackWrapper callbackWrapper){
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return observable.subscribe(callbackWrapper);
    }

}

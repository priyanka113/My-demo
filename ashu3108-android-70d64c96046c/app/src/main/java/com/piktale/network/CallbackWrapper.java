package com.piktale.network;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.piktale.models.BasicResponse;
import com.piktale.models.RestError;
import com.piktale.views.activities.BaseActivity;
import com.piktale.views.fragments.BaseFragment;
import com.piktale.views.widget.SubmitButton;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class CallbackWrapper<T> implements Observer<T> {
    protected abstract void onSuccess(T t);

    protected abstract void onFailure(String message);

    private View clickedView;

    public CallbackWrapper(Activity activity, View clickedView) {
        this.clickedView = clickedView;
        if(clickedView instanceof SubmitButton)
            ((SubmitButton) clickedView).showProgress();
    }

    public CallbackWrapper(BaseFragment fragment, View clickedView) {
        this.clickedView = clickedView;
        if(clickedView instanceof SubmitButton)
            ((SubmitButton) clickedView).showProgress();
    }

    public CallbackWrapper() {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if(clickedView instanceof SubmitButton)
            ((SubmitButton) clickedView).hideProgress();

        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            String error = getErrorMessage(responseBody);
            onFailure(error);
            Log.d("HttpException", error);
        } else if (e instanceof SocketTimeoutException) {
            onFailure("Socket Timeout Exception");
        } else if (e instanceof IOException) {
            onFailure("Network Error (IOException)");
        } else {
            if (e.getCause() != null)
                Log.d("GenericFailure", e.getCause().getMessage());
            onFailure(e.getMessage());
        }

    }

    @Override
    public void onNext(T t) {
        Response<BasicResponse> br = (Response<BasicResponse>) t;
        if(clickedView instanceof SubmitButton)
            ((SubmitButton) clickedView).hideProgress();

        if (br.isSuccessful()) {
            onSuccess(t);
        } else {
            RestError restError = handleError(br.errorBody());
            onFailure(restError.getErrorMsg());
        }
    }

    public static RestError handleError(ResponseBody response) {
        RestError restError = new RestError();
        restError.setErrorMsg("Something Went Wrong!");
        if (response instanceof ResponseBody) {
            ResponseBody responseBody = response;
            try {
                restError = new Gson().fromJson(new String(responseBody.bytes()), RestError.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return restError;
    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
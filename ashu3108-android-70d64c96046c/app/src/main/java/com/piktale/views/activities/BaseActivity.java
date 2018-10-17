package com.piktale.views.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.piktale.R;
import com.piktale.RootApplication;
import com.piktale.listeners.ConnectivityReceiver;
import com.piktale.network.APIService;
import com.piktale.utils.UiUtils;
import com.piktale.utils.Util;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

public class BaseActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    @Nullable
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Nullable
    @BindView(R.id.progress)
    public ProgressBar progress;

    protected Context context;
    private ProgressDialog mDialog;
    private OptionMenuListener optionMenuListener;

    ConnectivityReceiver connectivityReceiver;
    private IntentFilter filter = new IntentFilter();

    public CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        context = this;
        mDialog = new ProgressDialog(context);
        connectivityReceiver = new ConnectivityReceiver();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        compositeSubscription = new CompositeSubscription();
        hideKeyboardOnTouchOutside();
        if (progress != null) {
            progress.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        }
//        Util.makeStatusbarTint(this, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(connectivityReceiver, filter);
        getApp().setConnectivityListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getApp().setConnectivityListener(null);
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    public void showDialog(String title, String body) {
        mDialog.setIndeterminate(true);
        mDialog.setCancelable(false);
        mDialog.setTitle(title);
        mDialog.setMessage(body);
        mDialog.show();
    }

    public void setOptionMenu(int menu, OptionMenuListener listener) {
        if (toolbar != null) {
            this.optionMenuListener = listener;
            toolbar.getMenu().clear();
            toolbar.inflateMenu(menu);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    optionMenuListener.onMenuClicked(item);
                    return false;
                }
            });
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    public interface OptionMenuListener {
        public void onMenuClicked(MenuItem item);
    }

    public RootApplication getApp() {
        return ((RootApplication) getApplication());
    }

    public boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected(this);
        return isConnected;
    }

    public void networkNotAvailable() {
        UiUtils.showSnackbar(findViewById(android.R.id.content), getString(R.string.network_not_available), Snackbar.LENGTH_LONG);
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setBackNavigation(boolean dark) {
        toolbar.setNavigationIcon(dark ? R.drawable.ic_action_chevron_left_dark : R.drawable.ic_action_chevron_left_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void setToolbarTitle(String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    public void dismissDialog() {
        mDialog.dismiss();
    }

    public void setRecyclerViewLayoutManager(int orientation, RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(orientation);
        recyclerView.setLayoutManager(manager);
    }

    public void setRecyclerViewGridLayoutManager(RecyclerView recyclerView) {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
    }

    public APIService getAPIService() {
        return getApp().getAPIService();
    }

    public CompositeSubscription getCompositeSubscription() {
        return compositeSubscription;
    }

    private void hideKeyboardOnTouchOutside() {
        View view = findViewById(android.R.id.content);
        if (view != null) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    hideKeyboard(view);
                    return false;
                }
            });
        }
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
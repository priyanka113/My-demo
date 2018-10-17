package com.piktale.views.assist;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.piktale.R;
import com.piktale.RootApplication;
import com.piktale.assist.AlertHelper;
import com.piktale.models.BasicResponse;
import com.piktale.models.User;
import com.piktale.network.CallbackWrapper;
import com.piktale.utils.UiUtils;
import com.piktale.utils.Util;
import com.piktale.views.fragments.DatePickerFragment;

import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class Dialogs {

    public interface DialogListener {
        public void onSelected();
    }

    public interface OkayDialogListener {
        public void okay();

        public void cancel();
    }

    public interface UsernameDialogListener {
        public void okay(String username);

        public void cancel();
    }

    public interface PopupMenuListener {
        public void onSelected(MenuItem item);
    }

    public static void showList(Context context, View view, String title, CharSequence[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                if (view instanceof TextView) {
                    ((TextView) view).setText(items[pos]);
                } else if (view instanceof EditText) {
                    ((EditText) view).setText(items[pos]);
                }
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();
    }

    public static void showList(Context context, View view, String title, CharSequence[] items, final DialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                if (view instanceof TextView) {
                    ((TextView) view).setText(items[pos]);
                } else if (view instanceof EditText) {
                    ((EditText) view).setText(items[pos]);
                }
                dialog.dismiss();
                listener.onSelected();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();
    }

    public static void showOnlyOkayDialog(Context context, int layout, final OkayDialogListener listener) {
        AlertHelper helper = new AlertHelper(context);
        android.app.AlertDialog.Builder builder = helper.createDialog(layout);
        View dialodView = helper.getView();
        Button btnOk = dialodView
                .findViewById(R.id.okay);
        Button btnCan = dialodView
                .findViewById(R.id.cancle);
        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                listener.okay();
            }
        });
        btnCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                listener.cancel();
            }
        });
    }

    public static void showUsernameDialog(Context context, final UsernameDialogListener listener, RootApplication app) {
        String currentUsername = app.getPreferences().getProfile().getUser().getUsername();
        CompositeSubscription compositeSubscription = new CompositeSubscription();
        AlertHelper helper = new AlertHelper(context);
        android.app.AlertDialog.Builder builder = helper.createDialog(R.layout.dialog_username);
        View dialodView = helper.getView();
        Button btnOk = dialodView
                .findViewById(R.id.okay);
        EditText username = dialodView
                .findViewById(R.id.username);
        TextView error = dialodView
                .findViewById(R.id.error);
        ProgressBar progressBar = dialodView
                .findViewById(R.id.progress);
        ImageView applied = dialodView
                .findViewById(R.id.applied);
        ImageView cancel = dialodView
                .findViewById(R.id.cancel);

        username.setText(currentUsername);

        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnOk.setSelected(true);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressBar.getVisibility() == View.VISIBLE && cancel.getVisibility() == View.VISIBLE)
                    return;

                if (username.getText().toString().equalsIgnoreCase(currentUsername)) {
                    alertDialog.dismiss();
                    compositeSubscription.clear();
                    listener.okay(null);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    User l = new User();
                    l.setUsername(username.getText().toString());
                    compositeSubscription.add(app.getAPIService().updateUsername(l).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse<String>>>() {
                        @Override
                        protected void onSuccess(Response<BasicResponse<String>> response) {
                            alertDialog.dismiss();
                            progressBar.setVisibility(View.GONE);
                            compositeSubscription.clear();
                            listener.okay(username.getText().toString());
                            UiUtils.showToast(context, response.body().getData());
                        }

                        @Override
                        protected void onFailure(String message) {
                            progressBar.setVisibility(View.GONE);
                            error.setText(message);
                        }
                    }));
                }
            }
        });
        Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                User l = new User();
                l.setUsername(username.getText().toString());
                compositeSubscription.add(app.getAPIService().usernameAvailability(l).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse<String>>>() {
                    @Override
                    protected void onSuccess(Response<BasicResponse<String>> response) {
                        progressBar.setVisibility(View.GONE);
                        applied.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected void onFailure(String message) {
                        progressBar.setVisibility(View.GONE);
                        error.setText(message);
                    }
                }));
            }
        };

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                handler.removeCallbacks(runnable);

                applied.setVisibility(View.GONE);
                error.setText("");
                String d = charSequence.toString();

                if (d.equalsIgnoreCase(currentUsername))
                    return;

                if (!Util.textIsEmpty(d) && d.length() > 2) {
                    progressBar.setVisibility(View.VISIBLE);
                    handler.postDelayed(runnable, 500);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public static void showPopupMenu(Context context, int menu, View targetView, PopupMenuListener listener) {
        PopupMenu popup = new PopupMenu(context, targetView);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (menu == R.menu.popup_privacy && targetView instanceof LinearLayout) {
                    LinearLayout ll = (LinearLayout) targetView;
                    for (int i = 0; i < ll.getChildCount(); i++) {
                        if (ll.getChildAt(i) instanceof TextView) {
                            ((TextView) ll.getChildAt(i)).setText(item.getTitle());
                            break;
                        }
                    }
                }
                listener.onSelected(item);
                return true;
            }
        });
        popup.inflate(menu);
        popup.show();
    }

}

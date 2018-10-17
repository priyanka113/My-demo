package com.piktale.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.angads25.toggle.LabeledSwitch;
import com.google.gson.Gson;
import com.piktale.BuildConfig;
import com.piktale.R;
import com.piktale.enums.Gender;
import com.piktale.listeners.OnPhotoAlbumLoadedListener;
import com.piktale.models.BasicResponse;
import com.piktale.models.GenericRequestBody;
import com.piktale.models.Photo;
import com.piktale.models.PhotoAlbum;
import com.piktale.models.User;
import com.piktale.models.requestBody.Register;
import com.piktale.models.requestBody.VerifyOTP;
import com.piktale.network.CallbackWrapper;
import com.piktale.receiver.SMSReceiver;
import com.piktale.utils.Constants;
import com.piktale.utils.DevicePhotoManager;
import com.piktale.utils.UiUtils;
import com.piktale.utils.Util;
import com.piktale.views.adapter.PhotoAlbumRecyclerViewAdapter;
import com.piktale.views.adapter.PhotoRecyclerViewAdapter;
import com.piktale.views.adapter.decorations.GridItemOffsetDecoration;
import com.piktale.views.adapter.decorations.VerticalSpaceItemDecoration;
import com.piktale.views.fragments.DatePickerFragment;
import com.appolica.flubber.Flubber;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.piktale.views.widget.SubmitButton;
import com.squareup.picasso.Picasso;
import com.suke.widget.SwitchButton;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.internal.Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A login screen that offers login via email/password.
 */
public class SignUpActivity1 extends BaseActivity implements DatePickerFragment.DatePickerFragmentListener {

    private static final int CAMERA_REQUEST = 1002;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.identity)
    EditText identity;

    @BindView(R.id.otp)
    EditText otp;

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.username)
    EditText username;

    @BindView(R.id.final_user_name)
    EditText finalUsername;

    @BindView(R.id.final_name)
    EditText finalName;

    @BindView(R.id.final_email)
    EditText finalEmail;

    @BindView(R.id.dob)
    EditText dob;

    @BindView(R.id.isd_cont)
    TextInputLayout isdCont;

    @BindView(R.id.helper)
    TextView helper;

    @BindView(R.id.otp_sent)
    TextView otpSent;

    @BindView(R.id.switch_button)
    SwitchButton switchButton;

    @BindView(R.id.gallery)
    LinearLayout gallery;

    @BindView(R.id.login)
    LinearLayout login;

    @BindView(R.id.gender)
    LabeledSwitch gender;

    // form1 = input form for email or mobile
    // form2 = input form for OTP
    // form3 = input form for Name and password
    // form4 = input form for selecting username
    @BindViews({R.id.form1, R.id.form2, R.id.form3, R.id.form4, R.id.form5})
    LinearLayout[] forms;

    @BindView(R.id.pic)
    ImageView pic;

    @BindView(R.id.profile_pic)
    CircleImageView profilePic;

    @BindView(R.id.recyler_view)
    RecyclerView recyclerView;

    @BindView(R.id.content)
    LinearLayout content;

    @BindView(R.id.submit_button)
    SubmitButton submitButton;


    private int currentForm = -1;
    private PhotoAlbumRecyclerViewAdapter photoAlbumRecyclerViewAdapter;
    private PhotoRecyclerViewAdapter photoRecyclerViewAdapter;
    private DevicePhotoManager devicePhotoManager;
    private List<PhotoAlbum> photoAlbums = null;
    private VerticalSpaceItemDecoration verticalSpaceItemDecoration;
    private GridItemOffsetDecoration gridSpaceItemDecoration;
    private String galleryView = "";
    private Uri cameraFile;

    Register register;
    private Timer timer;
    private SMSReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);
        ButterKnife.bind(this);
        setBackNavigation(true);
        UiUtils.setAuthStatusBarColor(this);
        toolbar.setTitle("Sign Up");
        UiUtils.activeDeactive(submitButton, false);
        register = new Register();
//        email.setBaseColor(Color.parseColor("#000000"));

        Display display = getWindowManager().getDefaultDisplay();
        gallery.setX(display.getWidth());
//        Drawable drawable = new IconDrawable(this, FontAwesomeIcons.fa_calendar)
//                .colorRes(R.color.text).sizeDp(12);
//        dob.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        devicePhotoManager = new DevicePhotoManager();

        for (int i = 0; i < forms.length; i++) {
            forms[i].setVisibility(View.GONE);
        }

        gotoNextForm();

        smsReceiver = new SMSReceiver();
        smsReceiver.setListener(new SMSReceiver.SMSListener() {
            @Override
            public void onReceived(String val) {
                otp.setText(val);
            }
        });
        registerReceiver(smsReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));

        if (!BuildConfig.BUILD_TYPE.contains("release")) {
//            identity.setText("9619560549");
//            name.setText("Saya Godshala");
//            password.setText("123456789");
        }

        submitButton.mProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit(view);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    void onSubmit(View view) {

        if (Util.textIsEmpty(submitButton.mProceed.getText().toString()))
            return;
        if (forms[0].getVisibility() == View.VISIBLE) {
            if (TextUtils.isDigitsOnly(identity.getText().toString())) {
                checkPermissions();
            } else {
                register();
            }
        } else if (forms[1].getVisibility() == View.VISIBLE) {
            verifyOtp(view);
        }
//        else if (forms[2].getVisibility() == View.VISIBLE) {
//            gotoNextForm();
//        } else if (forms[3].getVisibility() == View.VISIBLE) {
//            gotoNextForm();
//        }
    }

    private boolean isSignUpValid() {
        String error = "";
        if (Util.textIsEmpty(name.getText().toString())) {
            error = "Name required";
        } else if (Util.textIsEmpty(password.getText().toString())) {
            error = "Password required";
        } else if (Util.textIsEmpty(identity.getText().toString())) {
            error = "Identity required";
        }

        if (!Util.textIsEmpty(identity.getText().toString())) {
            if (TextUtils.isDigitsOnly(identity.getText().toString())) {
                if (identity.getText().toString().length() < 10) {
                    error = "Invalid mobile number";
                }
            } else if (!Util.isValidEmail(identity.getText().toString())) {
                error = "Invalid email";
            }
        }

        return error.length() == 0;
    }

    @OnTextChanged(R.id.identity)
    void onTextChanged(CharSequence input) {
        if (isSignUpValid()) {
            UiUtils.activeDeactive(submitButton, true);
        } else {
            UiUtils.activeDeactive(submitButton, false);
        }
    }

    @OnTextChanged(R.id.otp)
    void onOtp(CharSequence input) {
        String s = input.toString();
        UiUtils.activeDeactive(submitButton, s.length() == 4 ? true : false);
    }

    @OnTextChanged(R.id.name)
    void onName(CharSequence input) {
        if (isSignUpValid()) {
            UiUtils.activeDeactive(submitButton, true);
        } else {
            UiUtils.activeDeactive(submitButton, false);
        }
        register.setName(input.toString());
    }

    @OnTextChanged(R.id.username)
    void onUsername(CharSequence input) {
        String s = input.toString();
        if (!Util.textIsEmpty(s)) {
            UiUtils.activeDeactive(submitButton, true);
        } else {
            UiUtils.activeDeactive(submitButton, false);
        }

        new CountDownTimer(400, 400) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                checkUsernameAvailability();
            }
        }.start();
        register.setUsername(username.getText().toString());
    }

//    @OnTextChanged(R.id.final_user_name)
//    void onFinalUsername(CharSequence input) {
//        String s = input.toString();
//        if (!Util.textIsEmpty(s)) {
//            UiUtils.activeDeactive(proceed, true);
//        } else {
//            UiUtils.activeDeactive(proceed, false);
//        }
//        register.setUsername(finalUsername.getText().toString());
//    }
//
//    @OnTextChanged(R.id.final_name)
//    void onFinalname(CharSequence input) {
//        String s = input.toString();
//        if (!Util.textIsEmpty(s)) {
//            UiUtils.activeDeactive(proceed, true);
//        } else {
//            UiUtils.activeDeactive(proceed, false);
//        }
//        register.setName(finalName.getText().toString());
//    }

    @OnTextChanged(R.id.password)
    void onPassword(CharSequence input) {
        if (isSignUpValid()) {
            UiUtils.activeDeactive(submitButton, true);
        } else {
            UiUtils.activeDeactive(submitButton, false);
        }
        register.setPassword(input.toString());
    }


    private void gotoNextForm() {
        if (currentForm == -1) {
            forms[0].setVisibility(View.GONE);
        } else {
            forms[currentForm].setVisibility(View.GONE);
        }
        currentForm += 1;
        Flubber.with()
                .animation(Flubber.AnimationPreset.SLIDE_LEFT) // Slide up animation
                .repeatCount(0)                              // Repeat once
                .duration(1000)                              // Last for 1000 milliseconds(1 second)
                .createFor(forms[currentForm])                             // Apply it to the view
                .start();
        forms[currentForm].setVisibility(View.VISIBLE);
        UiUtils.activeDeactive(submitButton, false);

        if (currentForm == 1) {
            register.setPhone(identity.getText().toString());
            register.setBusiness(String.valueOf(switchButton.isActivated()));
            if (TextUtils.isDigitsOnly(identity.getText().toString())) {
                otpSent.setText("OTP has been sent to your mobile number\n+91 " + identity.getText().toString());
            } else {
                otpSent.setText("OTP has been sent to your email id\n" + identity.getText().toString());
            }
        }
//        else if (currentForm == 2) {
//            register.setName(name.getText().toString());
//            register.setPassword(password.getText().toString());
//            helper.setVisibility(View.VISIBLE);
//            helper.setText(Html.fromHtml(getString(R.string.signup_terms)));
//        } else if (currentForm == 3) {
//            proceed.setText("Proceed");
//            helper.setText(Html.fromHtml(getString(R.string.pick_username_help)));
//        } else if (currentForm == 4) {
//            proceed.setText("Submit");
//            finalName.setText(register.getName());
//            finalUsername.setText(register.getUsername());
//            finalEmail.setText(register.getEmail());
//            helper.setTextColor(getResources().getColor(R.color.colorPrimary));
//            helper.setText(Html.fromHtml(getString(R.string.skip)));
//            helper.setVisibility(View.VISIBLE);
//            toolbar.setTitle("Profile Detail");
//        }
    }

    @OnClick(R.id.login)
    void onSignup() {
        onBackPressed();
    }

    @OnClick(R.id.dob)
    void onDobCont(View view) {
        DatePickerFragment.newInstance(this).show(getSupportFragmentManager(), "Date of Birth");
    }

    @Override
    public void onDateSet(Calendar date) {
//        dob.setText(date);
//        UiUtils.activeDeactive(proceed, true);
    }

    protected int getScreenWidth() {
        return findViewById(android.R.id.content).getWidth();
    }

    public void moveGalleryIn() {
        int width = gallery.getWidth();
        UiUtils.activeDeactive(submitButton, false);
        gallery.animate().x(getScreenWidth() - width);
        content.animate().x(-width);
        login.animate().x(-width);
        content.animate().alpha(0.2f).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
        login.animate().alpha(0.2f).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
    }

    public void moveGalleryOutfewYards() {
//        galleryName.setVisibility(View.GONE);
        UiUtils.activeDeactive(submitButton, true);
        gallery.animate().x(getScreenWidth() + 200);
        content.animate().x(0f);
        login.animate().x(0f);
        content.animate().alpha(1f).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
        login.animate().alpha(1f).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
    }

    public void moveGalleryInForPhotos() {
//        galleryName.setVisibility(View.VISIBLE);
        gallery.animate().x(getScreenWidth() - gallery.getWidth());
        content.animate().x(-gallery.getWidth());
        login.animate().x(-gallery.getWidth());
        content.animate().alpha(0.2f).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
        login.animate().alpha(0.2f).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
    }

    public void moveGalleryOut() {
        galleryView = "";
        UiUtils.activeDeactive(submitButton, isSignUpValid());
        gallery.animate().x(getScreenWidth());
        content.animate().x(0f);
        login.animate().x(0f);
        content.animate().alpha(1f).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
        login.animate().alpha(1f).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
    }

    @Override
    public void onBackPressed() {
        if (galleryView.equalsIgnoreCase("albums")) {
            moveGalleryOut();
        } else if (galleryView.equalsIgnoreCase("photos")) {
            moveGalleryOutfewYards();
            new CountDownTimer(500, 500) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    recyclerView.removeAllViews();
                    setPhotoAlbum();
                    new CountDownTimer(500, 500) {
                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            moveGalleryIn();
                        }
                    }.start();
                }
            }.start();
        } else {
            startActivity(new Intent(SignUpActivity1.this, AuthActivity.class));
            super.onBackPressed();
        }
    }

    @OnClick({R.id.pic, R.id.profile_pic})
    void onProfileClick() {
        askPermission();
    }

    private void setPhotoAlbum() {
        galleryView = "albums";
        if (recyclerView.getLayoutManager() != null && recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            recyclerView.removeItemDecoration(gridSpaceItemDecoration);
        }

        setRecyclerViewLayoutManager(LinearLayoutManager.VERTICAL, recyclerView);
        photoAlbumRecyclerViewAdapter = new PhotoAlbumRecyclerViewAdapter(photoAlbums, this);
        photoAlbumRecyclerViewAdapter.setOnItemClickListener(new PhotoAlbumRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                PhotoAlbum photoAlbum = photoAlbums.get(position);
                if (photoAlbum.getId() != -100) {
                    cameraFile = null;
                    setPhotos(photoAlbum);
                } else {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraFile = FileProvider.getUriForFile(SignUpActivity1.this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            getOutputMediaFile());
                    cameraFile = Uri.fromFile(getOutputMediaFile());
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFile);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
        recyclerView.setAdapter(photoAlbumRecyclerViewAdapter);
        verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(8, false);
        recyclerView.addItemDecoration(verticalSpaceItemDecoration);
    }


    private void setPhotos(PhotoAlbum photoAlbum) {
        moveGalleryOutfewYards();

        new CountDownTimer(500, 500) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                galleryView = "photos";
                if (recyclerView.getLayoutManager() != null && recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    recyclerView.removeItemDecoration(verticalSpaceItemDecoration);
                }
                setRecyclerViewGridLayoutManager(recyclerView);
                photoRecyclerViewAdapter = new PhotoRecyclerViewAdapter(photoAlbum.getPhotos(), SignUpActivity1.this);
                photoRecyclerViewAdapter.setOnItemClickListener(new PhotoRecyclerViewAdapter.MyClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Photo photo = (Photo) v.getTag();
                        openFilterActivity(photo.getUri());
                    }
                });
                recyclerView.setAdapter(photoRecyclerViewAdapter);
                gridSpaceItemDecoration = new GridItemOffsetDecoration(SignUpActivity1.this, R.dimen.grid);
                recyclerView.addItemDecoration(gridSpaceItemDecoration);

                new CountDownTimer(500, 500) {
                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        moveGalleryInForPhotos();
                    }
                }.start();


            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
    }

    private void askPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            if (photoAlbums == null) {
                                showDialog(getString(R.string.app_name), "fetching your album");
                                devicePhotoManager.getPhotoAlbums(SignUpActivity1.this, new OnPhotoAlbumLoadedListener() {
                                    @Override
                                    public void onPhotosLoaded(List<PhotoAlbum> albums) {
                                        dismissDialog();
                                        photoAlbums = new ArrayList<>();
                                        photoAlbums.add(new PhotoAlbum("Take Photo", -100));
                                        photoAlbums.addAll(albums);
                                        setPhotoAlbum();
                                        new CountDownTimer(500, 500) {
                                            public void onTick(long millisUntilFinished) {

                                            }

                                            public void onFinish() {
                                                moveGalleryIn();
                                            }
                                        }.start();
                                    }

                                    @Override
                                    public void onPhotosError(String error) {
                                        dismissDialog();
                                        UiUtils.showSnackbar(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
                                    }
                                });
                            } else {
                                galleryView = "albums";
                                moveGalleryIn();
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();


//        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
//            @Override
//            public void onPermissionGranted(PermissionGrantedResponse response) {
//
//            }
//
//            @Override
//            public void onPermissionDenied(PermissionDeniedResponse response) {
//
//            }
//
//            @Override
//            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//                token.continuePermissionRequest();
//            }
//        }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            int filterPosition = data.getIntExtra(SelectedPhotoFilterActivity.FILTER_POSITION, 0);
            String imagePath = data.getStringExtra(SelectedPhotoFilterActivity.IMAGE_PATH);

            moveGalleryOutfewYards();
            new CountDownTimer(500, 500) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    recyclerView.removeAllViews();
                    setPhotoAlbum();
                    if (imagePath.contains("content://")) {
                        profilePic.setImageURI(Uri.parse(imagePath));
                    } else {
                        profilePic.setImageURI(Uri.parse("file://" + imagePath));
                    }
                    Filter filter = Constants.filtersMap[filterPosition];
                    Bitmap temp = ((BitmapDrawable) profilePic.getDrawable()).getBitmap();
                    Bitmap filteredBitmap = filter.processFilter(temp.copy(Bitmap.Config.ARGB_8888, true));
                    filteredBitmap = filter.processFilter(filteredBitmap);
                    profilePic.setImageBitmap(filteredBitmap);
                    pic.setVisibility(View.GONE);
                    profilePic.setVisibility(View.VISIBLE);
                }
            }.start();
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//            Picasso.get().load(cameraFile).into(profilePic);
//            profilePic.setVisibility(View.VISIBLE);
//            pic.setVisibility(View.GONE);
            openFilterActivity(cameraFile.toString());
            moveGalleryOut();
        }
    }

    private File getOutputMediaFile() {

        String folderName = getString(R.string.app_name);

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), folderName);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    private void openFilterActivity(String path) {

        moveGalleryOutfewYards();
        new CountDownTimer(500, 500) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                recyclerView.removeAllViews();
                setPhotoAlbum();
                if (path.contains("content://")) {
                    profilePic.setImageURI(Uri.parse(path));
                    profilePic.setTag(Uri.parse(path));
                } else {
                    profilePic.setImageURI(Uri.parse("file://" + path));
                    profilePic.setTag(Uri.parse("file://" + path));
                }


//                Filter filter = Constants.filtersMap[filterPosition];
//                Bitmap temp = ((BitmapDrawable) profilePic.getDrawable()).getBitmap();
//                Bitmap filteredBitmap = filter.processFilter(temp.copy(Bitmap.Config.ARGB_8888, true));
//                filteredBitmap = filter.processFilter(filteredBitmap);
//                Picasso.get().load(Uri.parse(path)).into(profilePic);
//                profilePic.setImageBitmap(filteredBitmap);
                pic.setVisibility(View.GONE);
                profilePic.setVisibility(View.VISIBLE);
            }
        }.start();


//        Intent intent = new Intent(SignUpActivity1.this, SelectedPhotoFilterActivity.class);
//        intent.putExtra(SelectedPhotoFilterActivity.IMAGE_PATH, path);
//        startActivityForResult(intent, 1001);
    }

    private void temporaryUser(View clickedView) {
        User l = new User();
        l.setPhone(identity.getText().toString());
        getCompositeSubscription().add(getAPIService().temporaryUser(l).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse<String>>>(this, clickedView) {
            @Override
            protected void onSuccess(Response<BasicResponse<String>> response) {
                gotoNextForm();
            }

            @Override
            protected void onFailure(String message) {
                UiUtils.showSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            }
        }));
    }

    private void verifyOtp(View clickedView) {
        VerifyOTP l = new VerifyOTP();
        l.setUsername(identity.getText().toString());
        l.setOtp(Integer.parseInt(otp.getText().toString()));
        l.setApiType("signup");
        getCompositeSubscription().add(getAPIService().verifyOTP(l).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse<User>>>(this, clickedView) {
            @Override
            protected void onSuccess(Response<BasicResponse<User>> response) {
                gotoNextForm();
                User user = response.body().getData();
                getApp().getPreferences().setPreference(Constants.PreferenceKeys.USER_DATA, new Gson().toJson(user));
                getApp().getPreferences().setPreference(Constants.PreferenceKeys.USER_TOKEN, user.getUserToken());
                getApp().setApiService();
                getApp().setUser();
                startActivity(new Intent(SignUpActivity1.this, MainActivity.class));
                finish();
            }

            @Override
            protected void onFailure(String message) {
                UiUtils.showSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            }
        }));
    }

    private void checkUsernameAvailability() {
        User l = new User();
        l.setUsername(identity.getText().toString());
        getCompositeSubscription().add(getAPIService().usernameAvailability(l).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse<String>>>(this, submitButton) {
            @Override
            protected void onSuccess(Response<BasicResponse<String>> response) {
                UiUtils.showToast(context, response.body().getData());
            }

            @Override
            protected void onFailure(String message) {
                UiUtils.showSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            }
        }));
    }

    private void register() {

        RequestBody rbName =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, name.getText().toString());

        RequestBody rbPassword =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, password.getText().toString());

        RequestBody rbPhone =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, "");

        RequestBody rbEmail =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, "");

        if (TextUtils.isDigitsOnly(identity.getText().toString())) {
            rbPhone =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, identity.getText().toString());
        } else {
            rbEmail =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, identity.getText().toString());
        }

        RequestBody rbIsBusiness =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, String.valueOf(switchButton.isActivated()));

        RequestBody rbDeviceType =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, "Android");

        int isPictureThere = profilePic.getTag() != null ? 1 : 0;

        RequestBody rbIsPicture =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, String.valueOf(isPictureThere));

        Observable<Response<BasicResponse<String>>> obs = null;

        if (isPictureThere == 1) {
            Uri uri = (Uri) profilePic.getTag();
            File file = new File(uri.getPath());
            // create RequestBody instance from file
            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse(getMimeType(uri.getPath())), file);
            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

            obs = getAPIService().register(rbName, rbPassword, rbPhone, rbEmail, rbIsBusiness, rbDeviceType, rbIsPicture, body);
        } else {
            obs = getAPIService().register(rbName, rbPassword, rbPhone, rbEmail, rbIsBusiness, rbDeviceType, rbIsPicture);
        }

        getCompositeSubscription().add(obs.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse<String>>>(this, submitButton) {
            @Override
            protected void onSuccess(Response<BasicResponse<String>> response) {
                gotoNextForm();
            }

            @Override
            protected void onFailure(String message) {
                UiUtils.showSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            }
        }));

    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private void checkPermissions() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            register();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

}


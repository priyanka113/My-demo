package com.piktale.views.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.piktale.R;
import com.piktale.listeners.OnPhotoAlbumLoadedListener;
import com.piktale.models.BasicResponse;
import com.piktale.models.Photo;
import com.piktale.models.PhotoAlbum;
import com.piktale.models.User;
import com.piktale.network.CallbackWrapper;
import com.piktale.utils.AnimationUtil;
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
import com.chaos.view.PinView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A login screen that offers login via email/password.
 */
public class SignUpActivity extends BaseActivity implements DatePickerFragment.DatePickerFragmentListener {

    private static final int CAMERA_REQUEST = 1002;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.otp_sent_cont)
    LinearLayout otpSentCont;

    @BindView(R.id.name_password_cont)
    LinearLayout namePasswordCont;

    @BindView(R.id.select_username_cont)
    LinearLayout selectUsernameCont;

    @BindView(R.id.details_cont)
    LinearLayout detailsCont;

    @BindView(R.id.terms)
    LinearLayout terms;

    @BindView(R.id.fullname)
    EditText fullname;

    @BindView(R.id.username)
    EditText username;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.skip)
    TextView skip;

    @BindView(R.id.dob)
    TextView dob;

    @BindView(R.id.gallery_name)
    TextView galleryName;

    @BindView(R.id.otp)
    PinView otp;

    @BindView(R.id.proceed)
    Button proceed;

    @BindView(R.id.gallery)
    RelativeLayout gallery;

    @BindView(R.id.recyler_view)
    RecyclerView recyclerView;

    @BindView(R.id.content)
    RelativeLayout content;

    @BindView(R.id.profile_pic)
    CircleImageView profilePic;

    private PhotoAlbumRecyclerViewAdapter photoAlbumRecyclerViewAdapter;
    private PhotoRecyclerViewAdapter photoRecyclerViewAdapter;
    private DevicePhotoManager devicePhotoManager;
    private List<PhotoAlbum> photoAlbums = null;
    private VerticalSpaceItemDecoration verticalSpaceItemDecoration;
    private GridItemOffsetDecoration gridSpaceItemDecoration;
    private String galleryView = "";
    private Uri cameraFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setBackNavigation(true);
        UiUtils.setStatusBarGradiant(this);
        toolbar.setTitle("Sign Up");
        devicePhotoManager = new DevicePhotoManager();
        Flubber.with()
                .animation(Flubber.AnimationPreset.SLIDE_LEFT) // Slide up animation
                .repeatCount(0)                              // Repeat once
                .duration(1000)                              // Last for 1000 milliseconds(1 second)
                .createFor(otpSentCont)                             // Apply it to the view
                .start();
        otpSentCont.setVisibility(View.VISIBLE);
        Display display = getWindowManager().getDefaultDisplay();
        gallery.setX(display.getWidth());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.proceed)
    void onProceed() {
        if (otpSentCont.getVisibility() == View.VISIBLE) {
            String pin = otp.getText().toString();
            if (Util.textIsEmpty(pin)) {
                AnimationUtil.shake(this, otp);
//                UiUtils.showSnackbar(findViewById(android.R.id.content), "Please enter OTP", Snackbar.LENGTH_LONG);
            } else if (pin.length() < 4) {
                AnimationUtil.shake(this, otp);
//                UiUtils.showSnackbar(findViewById(android.R.id.content), "OTP must be 4 digits long", Snackbar.LENGTH_LONG);
            } else {

                Flubber.with()
                        .animation(Flubber.AnimationPreset.SLIDE_LEFT) // Slide up animation
                        .repeatCount(0)                              // Repeat once
                        .duration(1000)                              // Last for 1000 milliseconds(1 second)
                        .createFor(namePasswordCont)                             // Apply it to the view
                        .start();

//                AnimationUtil.slideOutToLeft(context, otpSentCont, false);
//                AnimationUtil.slideInFromRight(context, namePasswordCont, true);

//                namePasswordCont.startAnimation(AnimationUtils.loadAnimation(
//                        context, R.anim.slide_to_right
//                ));
                namePasswordCont.setVisibility(View.VISIBLE);
                otpSentCont.setVisibility(View.GONE);
                terms.setVisibility(View.VISIBLE);
            }
        } else if (namePasswordCont.getVisibility() == View.VISIBLE) {
            if (isFormValid()) {
                Flubber.with()
                        .animation(Flubber.AnimationPreset.SLIDE_LEFT) // Slide up animation
                        .repeatCount(0)                              // Repeat once
                        .duration(1000)                              // Last for 1000 milliseconds(1 second)
                        .createFor(selectUsernameCont)                             // Apply it to the view
                        .start();
                namePasswordCont.setVisibility(View.GONE);
                selectUsernameCont.setVisibility(View.VISIBLE);
            }
        } else if (selectUsernameCont.getVisibility() == View.VISIBLE) {
            String usernameStr = username.getText().toString();
            if (Util.textIsEmpty(usernameStr)) {
                AnimationUtil.shake(this, username);
//                UiUtils.showSnackbar(findViewById(android.R.id.content), "Please enter username", Snackbar.LENGTH_LONG);
            } else {
                Flubber.with()
                        .animation(Flubber.AnimationPreset.SLIDE_LEFT) // Slide up animation
                        .repeatCount(0)                              // Repeat once
                        .duration(1000)                              // Last for 1000 milliseconds(1 second)
                        .createFor(detailsCont)                             // Apply it to the view
                        .start();
                detailsCont.setVisibility(View.VISIBLE);
                skip.setVisibility(View.VISIBLE);
                selectUsernameCont.setVisibility(View.GONE);
                terms.setVisibility(View.GONE);
                toolbar.setTitle("Welcome");
            }
        }
    }

    private boolean isFormValid() {
        String error = "";
        String name = fullname.getText().toString();
        String pass = password.getText().toString();
        if (Util.textIsEmpty(name)) {
            error = "Please enter name";
            AnimationUtil.shake(fullname, 1);
        } else if (Util.textIsEmpty(pass)) {
            error = "please enter password";
            AnimationUtil.shake(password, 1);
        }
//        if (!error.equalsIgnoreCase("")) {
//            UiUtils.showSnackbar(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
//        }

        return error.equalsIgnoreCase("");
    }

    @OnClick(R.id.dob_cont)
    void onDobCont(View view) {
        DatePickerFragment.newInstance(this).show(getSupportFragmentManager(), "Date of Birth");
    }


    @Override
    public void onDateSet(Calendar date) {
//        dob.setText(date);
    }

    protected int getScreenWidth() {
        return findViewById(android.R.id.content).getWidth();
    }

    public void moveGalleryIn() {
        int width = gallery.getWidth();
        otp.setEnabled(false);
        proceed.setEnabled(false);
        otp.setFocusable(false);
        gallery.animate().x(getScreenWidth() - width);
        content.animate().x(-width);
    }

    public void moveGalleryOutfewYards() {
//        galleryName.setVisibility(View.GONE);
        proceed.setEnabled(true);
        otp.setEnabled(true);
        otp.setFocusable(true);
        gallery.animate().x(getScreenWidth() + 200);
        content.animate().x(0f);
    }

    public void moveGalleryInForPhotos() {
//        galleryName.setVisibility(View.VISIBLE);
        gallery.animate().x(getScreenWidth() - gallery.getWidth());
        content.animate().x(-gallery.getWidth());
    }

    public void moveGalleryOut() {
        galleryView = "";
        proceed.setEnabled(true);
        otp.setEnabled(true);
        otp.setFocusable(true);
        gallery.animate().x(getScreenWidth());
        content.animate().x(0f);
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
            super.onBackPressed();
        }
    }

    @OnClick(R.id.profile_pic)
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
                    setPhotos(photoAlbum);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                photoRecyclerViewAdapter = new PhotoRecyclerViewAdapter(photoAlbum.getPhotos(), SignUpActivity.this);
                photoRecyclerViewAdapter.setOnItemClickListener(new PhotoRecyclerViewAdapter.MyClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Photo photo = (Photo) v.getTag();
                        Intent intent = new Intent(SignUpActivity.this, SelectedPhotoFilterActivity.class);
                        intent.putExtra(SelectedPhotoFilterActivity.IMAGE_PATH, photo.getUri());
                        startActivityForResult(intent, 1001);
                    }
                });
                recyclerView.setAdapter(photoRecyclerViewAdapter);
                gridSpaceItemDecoration = new GridItemOffsetDecoration(SignUpActivity.this, R.dimen.grid);
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

    private void askPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            if (photoAlbums == null) {
                                showDialog(getString(R.string.app_name), "fetching your album");
                                devicePhotoManager.getPhotoAlbums(SignUpActivity.this, new OnPhotoAlbumLoadedListener() {
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
                    profilePic.setImageURI(Uri.parse("file://" + imagePath));
                    Filter filter = Constants.filtersMap[filterPosition];
                    Bitmap temp = ((BitmapDrawable) profilePic.getDrawable()).getBitmap();
                    Bitmap filteredBitmap = filter.processFilter(temp.copy(Bitmap.Config.ARGB_8888, true));
                    filteredBitmap = filter.processFilter(filteredBitmap);
                    profilePic.setImageBitmap(filteredBitmap);
                }
            }.start();
        } else if (requestCode == CAMERA_REQUEST && requestCode == RESULT_OK) {
            profilePic.setImageURI(cameraFile);
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




}


package com.rkvitsolutions.dhruviconic.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.rkvitsolutions.dhruviconic.Model.AtndModel;
import com.rkvitsolutions.dhruviconic.Model.AttendModel;
import com.rkvitsolutions.dhruviconic.Model.DailyAtndReportModel;
import com.rkvitsolutions.dhruviconic.R;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.APIClient;
import com.rkvitsolutions.dhruviconic.RetrofitUtil.ApiInterface;
import com.rkvitsolutions.dhruviconic.Utils.ImagePickerActivity;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class MarkAttendance extends AppCompatActivity implements View.OnClickListener, LocationListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int REQUEST_IMAGE = 100;
    private static final int GALLERY = 100;
    List<Address> addresses;
    private CardView markAtnd, checkOut, othersAttend;
    private Bitmap bitmap;
    private Bitmap compressedBitmap;
    private String tempUri;
    private File finalFile;
    private ApiInterface apiInterface;
    private List<String> data;
    private String EmpId, EmpName, Email, MobileNo, InTime, OutTime, AttndStatus;
    private TextView pickupLocation;
    private TextView dropLocation;
    private ImageView gps;
    private GoogleApiClient googleApiClient;
    private LocationManager locationManager;
    private boolean GpsStatus;
    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private double Lat, Longi;
    private TextView inTime, outTime, status, name, designation;
    private String currentDateTimeString;
    //    private CircleImageView img;
    private android.app.AlertDialog alert;
    private EditText mobileNo;
    private Button othersCheckin, othersCheckout;
    private String OthersMobileNo;
    private MultipartBody.Part body;
    private String address;
    private String type;
    private String URL;
    private ProgressDialog locationProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

//        checkLocationPermission();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiInterface = APIClient.getClient().create(ApiInterface.class);

        markAtnd = findViewById(R.id.mark_atnd);
        markAtnd.setOnClickListener(this);

        checkOut = findViewById(R.id.mark_check_out);
        checkOut.setOnClickListener(this);

        othersAttend = findViewById(R.id.others);
        othersAttend.setOnClickListener(this);

        loadLocation();
        getUserData();

        pickupLocation = findViewById(R.id.pickup_location);
        dropLocation = findViewById(R.id.drop_location);
        dropLocation.setOnClickListener(this);

    }

    private void getUserData() {

        SharedPreferences pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        AttndStatus = pref.getString("Status", null);
        InTime = pref.getString("InTime", null);
        OutTime = pref.getString("OutTime", null);
        EmpId = pref.getString("userCode", null);
//        pref.getString("DOB", null);
//        pref.getString("Gender", null);
        EmpName = pref.getString("username", null);
//        pref.getString("MatitalStatus", null);
//        MobileNo = pref.getString("MobileNo", null);
//        Email = pref.getString("EmailId", null);

        inTime = findViewById(R.id.in_time);
        if (InTime != null)
            inTime.setText(InTime);

        outTime = findViewById(R.id.out_time);
        if (OutTime != null)
            outTime.setText(OutTime);

        status = findViewById(R.id.status);
        if (AttndStatus != null)
            status.setText(AttndStatus);


//        img = findViewById(R.id.profileImg);

        name = findViewById(R.id.name);
        name.setText(EmpName);

        designation = findViewById(R.id.designation);
        designation.setText(MobileNo);

        getAttendance();

    }

    private void getAttendance() {

        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setCancelable(false);
        progress.show();


        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Call<List<DailyAtndReportModel>> call = apiInterface.getDailyAtndReport(EmpId, date);
        call.enqueue(new Callback<List<DailyAtndReportModel>>() {
            @Override
            public void onResponse(Call<List<DailyAtndReportModel>> call, retrofit2.Response<List<DailyAtndReportModel>> response) {

                if (response.isSuccessful()) {

//                    attndSts = response.body();

                    if (!response.body().isEmpty()) {

                        inTime.setText(convertTime(response.body().get(0).getINTime().split("\\.", 2)[0]));
                        outTime.setText(convertTime(response.body().get(0).getOUTTime().split("\\.", 2)[0]));
                        status.setText(response.body().get(0).getStatus());

                    } else {
                        //                        dataSts = "no data";
                    }
                    progress.cancel();

                } else {
                    progress.cancel();
                    FancyToast.makeText(MarkAttendance.this, "Something went wrong! Try again later", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }

            }

            @Override
            public void onFailure(Call<List<DailyAtndReportModel>> call, Throwable t) {
                progress.cancel();
//                avi.hide();
                FancyToast.makeText(MarkAttendance.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                Log.v("ImageUploadError", t.toString());
                call.cancel();
            }
        });

    }

    @SuppressLint("SimpleDateFormat")
    private String convertTime(String time) {

        String newTime = null;

        try {
            @SuppressLint("SimpleDateFormat") final SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
            final Date dateObj = sdf.parse(time);
//            System.out.println(dateObj);
//            System.out.println(new SimpleDateFormat("K:mm").format(dateObj));
            newTime = new SimpleDateFormat("hh:mm a").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }


        return newTime;
    }

    @SuppressLint("SimpleDateFormat")
    public void getCurrentTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

        currentDateTimeString = sdf.format(d);
        status.setText("P");

        Log.d("CurrentDate", currentDateTimeString);
    }

    @SuppressLint("CheckResult")
    private void loadLocation() {
        new RxPermissions(this)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION) // ask single or multiple permission once
                .subscribe(granted -> {
                    if (granted) {
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.my_map);
                        mapFragment.getMapAsync((OnMapReadyCallback) this);

                    } else {
//                        onGps();

                    }
                });
    }

    public void onGps() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AppTheme));

        alertDialogBuilder.setTitle("Enable GPS");
        alertDialogBuilder.setMessage("To mark attendance please enable GPS");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                dialog.cancel();
            }
        });
//        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                alertDialogBuilder.setCancelable(true);
//            }
//        });
        alertDialogBuilder.show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationProgress = new ProgressDialog(this);
        locationProgress.setMessage("Getting your location...");
        locationProgress.setCancelable(false);
        locationProgress.show();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //get Address from lat long
        getAddress(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("You are here");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @SuppressLint("SetTextI18n")
    private void getAddress(double latitude, double longitude) {

        Lat = latitude;
        Longi = longitude;

        System.out.print("LatLong>>>>" + Lat + ">>>>" + Longi);

        Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        String locality = addresses.get(0).getLocality();
        String subLocality = addresses.get(0).getSubLocality();
        String subsubLocality = String.valueOf(addresses.get(0).getSubAdminArea());
//        Log.v("Area", locality);
//        Log.v("Area", subLocality);
//        Log.v("Area", subsubLocality);
//
//
//        Log.v("Address", address);

        pickupLocation.setText(address);
//        dropLocation.setText("Latitude: " + String.valueOf(latitude) + "\n\nLongitude: " + String.valueOf(longitude));

        locationProgress.cancel();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.mark_atnd:

                type = "checkin";
                if (inTime.getText().toString().isEmpty()) {
                    addPic();
                } else
                    FancyToast.makeText(MarkAttendance.this, "You already marked check in!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                break;

            case R.id.mark_check_out:

                type = "checkout";
                if (outTime.getText().toString().isEmpty()) {
                    addPic();
                } else
                    FancyToast.makeText(MarkAttendance.this, "You already marked check out!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

                break;

            case R.id.logout:
                logout();
                break;

            default:

        }
    }

    @SuppressLint("CheckResult")
    private void addPic() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        } else {
                            // TODO - handle permission denied case
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    private void showImagePickerOptions() {

        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(this);
        pictureDialog.setTitle("Add Image");
        String[] pictureDialogItems = {
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                takePhotoFromCamera();

                    }
                });
        pictureDialog.show();

//        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
//            @Override
//            public void onTakeCameraSelected() {
//                launchCameraIntent();
//            }
//
//            @Override
//            public void onChooseGallerySelected() {
//                launchGalleryIntent();
//            }
//        });
    }

    private void launchCameraIntent() {
        ImagePickerActivity.clearCache(this);
        Intent intent = new Intent(MarkAttendance.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        ImagePickerActivity.clearCache(this);
        Intent intent = new Intent(MarkAttendance.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);

//        int preference = ScanConstants.OPEN_CAMERA;
//        Intent intent = new Intent(this, ScanActivity.class);
//        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
//        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == this.RESULT_CANCELED) {
            return;
        }

        else if (requestCode == CAMERA) {

            bitmap = (Bitmap) data.getExtras().get("data");

            //seting resolution 400x400
            compressedBitmap = getResizedBitmap(bitmap, 400, 400);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            tempUri = getImagePath(getApplicationContext(), compressedBitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            finalFile = new File(tempUri);
            Log.v("finalfile", String.valueOf(finalFile));
            int length = (int) finalFile.length();
            System.out.println("length>>>>" + length);

            markMyAtend();

        }

//        if (requestCode == REQUEST_IMAGE) {
//            if (resultCode == Activity.RESULT_OK) {
//                Uri uri = data.getParcelableExtra("path");
//                try {
//                    // You can update this bitmap to your server
//                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//
//                    //seting resolution 400x400
//                    compressedBitmap = getResizedBitmap(bitmap, 400, 400);
//
//                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
//                    tempUri = getImagePath(getApplicationContext(), compressedBitmap);
//
//                    // CALL THIS METHOD TO GET THE ACTUAL PATH
//                    finalFile = new File(tempUri);
//                    Log.v("finalfile", String.valueOf(finalFile));
//                    int length = (int) finalFile.length();
//                    System.out.println("length>>>>" + length);
//
////                    addAttendance();
//
//                    markMyAtend();
//
//                    // loading profile image from local cache
////                    loadProfile(uri.toString());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public String getImagePath(Context inContext, Bitmap inImage) {

        getCurrentTime();

        String fiePath = "";
        try {
//            String filePath = Environment.getExternalStorageDirectory() + "/" + "dhruv_iconic";
//            File dir = new File(filePath);

            String filePath = this.getApplicationContext().getExternalFilesDir(null).getAbsolutePath();
            File dir = new File(filePath + this.getApplicationContext().getResources().getString(R.string.app_name));
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(dir, EmpId + "_" + currentDateTimeString.trim() + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            inImage.compress(Bitmap.CompressFormat.PNG, 50, fOut);

            fiePath = file.getAbsolutePath();
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fiePath;
    }

    private void addAttendance() {

        RequestBody requestBody = RequestBody.create(MediaType.parse("//multipart/form-data"), finalFile);
        body = MultipartBody.Part.createFormData("Picks", finalFile.getName(), requestBody);

        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Marking Your Attendance...");
        progress.setCancelable(false);
        progress.show();
        String Latitude = String.valueOf(Lat);
        String Longitude = String.valueOf(Longi);

        Log.v("Latitude", Latitude);
        Log.v("Longitude", Longitude);
        Log.v("EmpId", EmpId);

        Call<AttendModel> call = apiInterface.markAttend(EmpId, address, body);
        call.enqueue(new Callback<AttendModel>() {
            @Override
            public void onResponse(Call<AttendModel> call, retrofit2.Response<AttendModel> response) {

                progress.cancel();

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus().contains("Record Updated Successfully")) {

                        getCurrentTime();
                        inTime.setText(currentDateTimeString);

                        //adding in Time
                        SharedPreferences pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        pref.edit().putString("InTime", currentDateTimeString)
                                .putString("Status", "P").apply();

//                            img.setImageBitmap(compressedBitmap);

                        progress.cancel();
//                            data = response.body().getDATA();

//                        profileImg.setImageBitmap(compressedBitmap);

                        FancyToast.makeText(MarkAttendance.this, "Updated Successfully!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
//                        getUserDetails();

//                    saveUserData();
//                    startActivity(new Intent(Login.this, AddDetails.class));
                    } else
                        FancyToast.makeText(MarkAttendance.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                } else
                    FancyToast.makeText(MarkAttendance.this, "Something went wrong!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

            }

            @Override
            public void onFailure(Call<AttendModel> call, Throwable t) {
                progress.cancel();
//                avi.hide();
                FancyToast.makeText(MarkAttendance.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                Log.v("ImageUploadError", t.toString());
                call.cancel();
            }
        });

//        else {
//            Call<MarkAttendModel> call = apiInterface.markOthersAttend(OthersMobileNo, Latitude, Longitude, body);
//            call.enqueue(new Callback<MarkAttendModel>() {
//                @Override
//                public void onResponse(Call<MarkAttendModel> call, retrofit2.Response<MarkAttendModel> response) {
//
//                    progress.cancel();
//
//                    if (response.isSuccessful()) {
//                        if (response.body().getRESPONSECODE().contains("2XX")) {
//
//                            OthersMobileNo = "";
//
//                            alert.cancel();
//
////                            getCurrentTime();
////                            inTime.setText(currentDateTimeString);
//
//                            //adding in Time
////                            SharedPreferences pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);
////                            pref.edit().putString("InTime", currentDateTimeString).apply();
//
//                            img.setImageBitmap(compressedBitmap);
//
//                            progress.cancel();
//                            data = response.body().getDATA();
//
////                        profileImg.setImageBitmap(compressedBitmap);
//
//                            FancyToast.makeText(MarkAttendance.this, "Updated Successfully!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
////                        getUserDetails();
//
////                    saveUserData();
////                    startActivity(new Intent(Login.this, AddDetails.class));
//                        } else
//                            FancyToast.makeText(MarkAttendance.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
//                    } else
//                        FancyToast.makeText(MarkAttendance.this, "Something went wrong!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
//
//                }
//
//                @Override
//                public void onFailure(Call<MarkAttendModel> call, Throwable t) {
//                    progress.cancel();
////                avi.hide();
//                    FancyToast.makeText(MarkAttendance.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
//                    Log.v("ImageUploadError", t.toString());
//                    call.cancel();
//                }
//            });
//        }

    }

    private void markMyAtend() {

        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Marking Your Attendance...");
        progress.setCancelable(false);
        progress.show();

        RequestBody requestBody = RequestBody.create(MediaType.parse("//multipart/form-data"), finalFile);
        body = MultipartBody.Part.createFormData("file", finalFile.getName(), requestBody);


        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);

        Call<AtndModel> call = apiInterface.markAttnd(EmpId, address, body);
        call.enqueue(new Callback<AtndModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<AtndModel> call, retrofit2.Response<AtndModel> response) {
                progress.cancel();

                if (response.isSuccessful()) {
                    getCurrentTime();

//                    if (response.body().getStatus().equals("Record Updated Successfully")) {
                    getCurrentTime();

                    if (type.equals("checkin")) {
                        inTime.setText(currentDateTimeString);
                        //adding in Time
                        SharedPreferences pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        pref.edit().putString("InTime", currentDateTimeString)
                                .putString("Status", "P").apply();
                    } else {
                        outTime.setText(currentDateTimeString);
//                            data = response.body().getDATA();

                        //adding in Time
                        SharedPreferences pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        pref.edit().putString("OutTime", currentDateTimeString).apply();

                        logout();
                    }

                    progress.cancel();

                    FancyToast.makeText(MarkAttendance.this, "Updated Successfully!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                } else {
                    progress.cancel();
                    FancyToast.makeText(MarkAttendance.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            }

            @Override
            public void onFailure(Call<AtndModel> call, Throwable t) {
                progress.cancel();
                FancyToast.makeText(MarkAttendance.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                Log.v("ImageUploadError", t.toString());
                call.cancel();
            }
        });
    }

    private void checkOut() {

        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Checking Out...");
        progress.setCancelable(false);
        progress.show();
        String Latitude = String.valueOf(Lat);
        String Longitude = String.valueOf(Longi);

//        Log.v("Latitude", Latitude);
//        Log.v("Longitude", Longitude);
//        Log.v("EmpId", EmpId);

        if (OthersMobileNo == null) {

            Call<AttendModel> call = apiInterface.markAttend(EmpId, " ", body);
            call.enqueue(new Callback<AttendModel>() {
                @Override
                public void onResponse(Call<AttendModel> call, retrofit2.Response<AttendModel> response) {

                    progress.cancel();

                    if (response.isSuccessful()) {
                        if (response.body().getStatus().contains("Record Updated Successfully")) {

                            getCurrentTime();

                            outTime.setText(currentDateTimeString);

                            progress.cancel();
//                            data = response.body().getDATA();

                            //adding in Time
                            SharedPreferences pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                            pref.edit().putString("OutTime", currentDateTimeString).apply();

                            logout();

                            FancyToast.makeText(MarkAttendance.this, "Updated Successfully!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                        } else
                            FancyToast.makeText(MarkAttendance.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    } else
                        FancyToast.makeText(MarkAttendance.this, "Something went wrong!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

                }

                @Override
                public void onFailure(Call<AttendModel> call, Throwable t) {
                    progress.cancel();
//                avi.hide();
                    FancyToast.makeText(MarkAttendance.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    Log.v("ImageUploadError", t.toString());
                    call.cancel();
                }
            });

        }

//        else {
//
//            Call<MarkAttendModel> call = apiInterface.checkOutOthers(OthersMobileNo);
//            call.enqueue(new Callback<MarkAttendModel>() {
//                @Override
//                public void onResponse(Call<MarkAttendModel> call, retrofit2.Response<MarkAttendModel> response) {
//
//                    progress.cancel();
//
//                    if (response.isSuccessful()) {
//                        if (response.body().getRESPONSECODE().contains("2XX")) {
//
////                            getCurrentTime();
////
////                            outTime.setText(currentDateTimeString);
//
//                            OthersMobileNo = "";
//
//                            alert.cancel();
//
//                            progress.cancel();
//                            data = response.body().getDATA();
//
//                            FancyToast.makeText(MarkAttendance.this, "Updated Successfully!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
//
//                        } else
//                            FancyToast.makeText(MarkAttendance.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
//                    } else
//                        FancyToast.makeText(MarkAttendance.this, "Something went wrong!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
//
//                }
//
//                @Override
//                public void onFailure(Call<MarkAttendModel> call, Throwable t) {
//                    progress.cancel();
////                avi.hide();
//                    FancyToast.makeText(MarkAttendance.this, "Try Again!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
//                    Log.v("ImageUploadError", t.toString());
//                    call.cancel();
//                }
//            });
//        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

    private void logout() {

        SharedPreferences pref = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear().apply();

        SharedPreferences loginPref = getSharedPreferences("LoginStatus", Context.MODE_PRIVATE);
        loginPref.edit().clear().apply();

        FancyToast.makeText(this, "Checkout done. Logging you out!\n See you again", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

        finishAndRemoveTask();

    }

}
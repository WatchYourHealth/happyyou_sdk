package com.wyh.happyyousdk.dashboard;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.crypto.RSAEncryption;
import com.wyh.happyyousdk.dashboard.adapter.ScanListAdapter;
import com.wyh.happyyousdk.databinding.ActivityScanQractivityBinding;
import com.wyh.happyyousdk.model.ScanQRModel;
import com.wyh.happyyousdk.model.request.Qrrequestmodel;
import com.wyh.happyyousdk.model.response.QrResponse;
import com.wyh.happyyousdk.model.response.SaveQrResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.io.IOException;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanQRActivity extends AppCompatActivity {


    ActivityScanQractivityBinding binding;
    Context context;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    String latitude, longitude;
    String city = "";

    ApiInterfaceWyh apiInterfaceWyh;
    ProgressDialog progressDialog;

    RecyclerView scan_qr_list;

    ScanListAdapter scanListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan_qractivity);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        setToolBar();
        fetchToken();
        setWelcomeText();


        binding.scanqrbtn.setOnClickListener(v -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(ScanQRActivity.this);
            intentIntegrator.setPrompt("Place a barcode/QR Code inside the viewfinder rectangle to scan it.");
            intentIntegrator.setCameraId(0);
            intentIntegrator.setCaptureActivity(ScannerIntegrate.class);
            intentIntegrator.setOrientationLocked(false);
            intentIntegrator.initiateScan(IntentIntegrator.ALL_CODE_TYPES);
        });

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void setToolBar() {
        binding.includeBack.tvBack.setText("Scan QR");
        binding.includeBack.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeBack.ivBack.setColorFilter(getResources().getColor(R.color.white));
        binding.includeBack.llBack.setOnClickListener(v -> {
            onBackPressed();
        });


        //binding.scanqrlayout.setVisibility(View.GONE);
        //binding.congralayout.setVisibility(View.VISIBLE);
    }

    private void setWelcomeText() {
        binding.welcometext.setText("Indulge in our fun activities and get a chance to win exciting rewards.\n" +
                "All you need to do is scan the QR at any of the activities");
    }

    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location == null) {
                        requestNewLocationData();
                    } else {
                        latitude = location.getLatitude() + "";
                        longitude = location.getLongitude() + "";
                        getCurrentCityName(latitude, longitude);
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude() + "";
            longitude = mLastLocation.getLongitude() + "";
            getCurrentCityName(latitude, longitude);
        }
    };

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private void getCurrentCityName(String latitude, String longitude) {


        Geocoder gcd = new Geocoder(context,
                Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
            if (addresses.size() > 0 && addresses.get(0).getAddressLine(0) != null) {
                city = addresses.get(0).getAddressLine(0);
            } else {
                city = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(ScanQRActivity.this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }


    private void saveQRToken(String result) {
        progressDialog.show();
        Qrrequestmodel request = new Qrrequestmodel(city, result.trim());
        Call<SaveQrResponse> call = apiInterfaceWyh.saveQRToken(SharedPref.getAuthToken(), request);
        Log.d("AuthToken", new Gson().toJson(request));
        call.enqueue(new Callback<SaveQrResponse>() {
            @Override
            public void onResponse(Call<SaveQrResponse> call, Response<SaveQrResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200 && response.body().getSuccess()) {
                    fetchToken();
                }
                if(response.body().getSuccess() != null && !response.body().getSuccess()){
                    Toast.makeText(ScanQRActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SaveQrResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Type type1 = new TypeToken<ScanQRModel>() {
                    }.getType();
                    Log.d("Intent",intentResult.getContents());
                    ScanQRModel familyList = new Gson().fromJson(intentResult.getContents(), type1);
                    String keyDec = RSAEncryption.callDecryptionMethod(familyList.getKey());
//                    String keyDec = CryptoHelper.decrypt(this,familyList.getKey());
                    Log.d("success dec", keyDec);
                    if (familyList.getKey() != null && keyDec.equalsIgnoreCase("HappyU by kotak")) {
                        Log.d("success", "success");
                        String activityDec = RSAEncryption.callDecryptionMethod(familyList.getActivityName());
//                        String activityDec = CryptoHelper.decrypt(this,familyList.getActivityName());
                        Log.d("success dec", activityDec);
                        Log.d("AuthToken","QR Key "+keyDec);
                        saveQRToken(activityDec);
                    } else {
                        Log.d("AuthToken","QR Key "+keyDec);
                        Toast.makeText(context, "Invalid QR code..", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.d("AuthToken","QR Exception "+e);
                    Toast.makeText(context, "Invalid QR code", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void fetchToken() {
        progressDialog.show();
        Call<QrResponse> call = apiInterfaceWyh.fetchQRToken(SharedPref.getAuthToken());
        call.enqueue(new Callback<QrResponse>() {
            @Override
            public void onResponse(Call<QrResponse> call, Response<QrResponse> response) {
                try{
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (response.code() == 200 && response.body() != null) {

                        ArrayList<QrResponse.Datum> data = response.body().getData();

                        if (!data.isEmpty()) {
                            setData(data);
                        } else {
                            binding.congralayout.setVisibility(View.GONE);
                            binding.scanqrlayout.setVisibility(View.VISIBLE);
                        }

                    } else {
                        Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.fetch_pending_diary_events_failed));
                        Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                   // e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<QrResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setData(ArrayList<QrResponse.Datum> data) {
        try {
            binding.congralayout.setVisibility(View.VISIBLE);
            scan_qr_list = findViewById(R.id.scan_qr_list);
            scan_qr_list.setLayoutManager(new LinearLayoutManager(this));
            scan_qr_list.hasFixedSize();
            scanListAdapter = new ScanListAdapter(this,data);
            scan_qr_list.setAdapter(scanListAdapter);

        } catch (Exception e) {
            binding.scanqrlayout.setVisibility(View.VISIBLE);
            Toast.makeText(context, "Sorry.Some thing went wrong.\n Please try again ! ", Toast.LENGTH_LONG).show();
            binding.congralayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                menualPermission();
            }
        }
    }

    private void menualPermission() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        View view = LayoutInflater.from(context).inflate(R.layout.delete_popup, null);
        alertBuilder.setView(view);
        Button btn_yes = view.findViewById(R.id.btnOK);
        Button btn_no = view.findViewById(R.id.btnCancel);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        ImageView imageView = view.findViewById(R.id.iv_delete);

        imageView.setBackgroundResource(R.drawable.baseline_location_on_24);
        tvMsg.setText("Please allow a location permission for scanning a QR.");
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
        btn_yes.setOnClickListener(view1 -> {
            alertDialog.dismiss();
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        });
        btn_no.setOnClickListener(view1 -> {
            alertDialog.dismiss();
            finish();
        });

        Rect displayRectangle = new Rect();
        Window window;

        window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), (int) (displayRectangle.height() * 0.45f));
    }


}
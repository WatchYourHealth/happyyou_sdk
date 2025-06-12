package com.wyh.happyyousdk.dashboard;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivityScannerIntegrateBinding;

import java.util.Random;

public class ScannerIntegrate extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private ViewfinderView viewfinderView;
    private ImageView ivscannerback, ivTorch;
    private Boolean torchState = false;

    private ActivityScannerIntegrateBinding binding;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_integrate);


        initView();
        barcodeScannerView.setTorchListener(this);
        viewfinderView = (ViewfinderView) findViewById(R.id.zxing_viewfinder_view);
        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            //switchFlashlightButton.setVisibility(View.GONE);
            ivTorch.setVisibility(View.GONE);
        }


        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        changeMaskColor(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    public void initView() {
        barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
        /*switchFlashlightButton = (Button)findViewById(R.id.switch_flashlight);*/
        ivscannerback = (ImageView) findViewById(R.id.ivscannerback);
        ivTorch = (ImageView) findViewById(R.id.ivTorch);
    }

    public void traverseBack(View view) {
        onBackPressed();
    }

    public void changeMaskColor(View view) {
        /*Random rnd = new Random();
        int color = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    public void onTorchOn() {
        //switchFlashlightButton.setText(R.string.turn_off_flashlight);
        //Toast.makeText(this, "Torch turned on", Toast.LENGTH_SHORT).show();
        ivTorch.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.torchon));
    }

    @Override
    public void onTorchOff() {
        //switchFlashlightButton.setText(R.string.turn_on_flashlight);
        //Toast.makeText(this, "Torch turned off", Toast.LENGTH_SHORT).show();
        ivTorch.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.torch));
    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight(View view) {
        /*if (getString(R.string.turn_on_flashlight).equals(switchFlashlightButton.getText())) {
            barcodeScannerView.setTorchOn();
        } else {
            barcodeScannerView.setTorchOff();
        }*/

        if (!torchState) {
            barcodeScannerView.setTorchOn();
            torchState = true;
        } else {
            barcodeScannerView.setTorchOff();
            torchState = false;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
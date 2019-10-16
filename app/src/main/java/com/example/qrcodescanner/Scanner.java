package com.example.qrcodescanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    ActionManager actionManager;
    ZXingScannerView collaboratorView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_layout);
        collaboratorView = (ZXingScannerView)findViewById(R.id.zxscan);
        scannerView = new ZXingScannerView(this);
        collaboratorView.addView(scannerView);

        Toolbar toolbar = (Toolbar)findViewById(R.id.scn_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Scanner");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                Toast.makeText(getApplicationContext(),"Camera permission granted",Toast.LENGTH_SHORT).show();
            }
            else{
                requestPermission();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if(checkPermission()){
            if(scannerView==null){
                scannerView = new ZXingScannerView(this);
                collaboratorView.addView(scannerView);
            }
            scannerView.setResultHandler(this);
            scannerView.setBorderColor(ContextCompat.getColor(this,R.color.light_blue));
            scannerView.setMaskColor(ContextCompat.getColor(this,R.color.maskcolor));
            scannerView.startCamera();
        }
        else{
            requestPermission();
        }
    }

    @Override
    public void handleResult(Result result) {
        final String scanResult = result.getText();
        actionManager = new ActionManager(Scanner.this,scanResult);
        actionManager.action();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.flash_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.switch_control);
        menuItem.setActionView(R.layout.swtich_layout);

        Switch sw = (Switch)menu.findItem(R.id.switch_control).getActionView().findViewById(R.id.switchLight);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                if(on){
                    if(scannerView!=null){
                        scannerView.setFlash(true);
                    }
                }else {
                    if(scannerView!=null){
                        scannerView.setFlash(false);
                    }
                }
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        scannerView.stopCamera();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPermission(){
        if(this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        else{
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermission(){
        if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
            Toast.makeText(getApplicationContext(),"Permission is necessary",Toast.LENGTH_SHORT).show();
        }
        else {
            requestPermissions(new String[]{Manifest.permission.CAMERA},1);
        }
    }
}

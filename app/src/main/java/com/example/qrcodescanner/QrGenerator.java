package com.example.qrcodescanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QrGenerator extends BaseActivity {

    ImageView imageView;
    EditText text;
    Button encodeButton;
    FileStorage fileStorage = null;
    Bitmap bitmap;
    AlertManager alertManager = null;
    View customView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generator);
        getSupportActionBar().setTitle("QR generator");

        customView = getLayoutInflater().inflate(R.layout.custom_dialogue,null);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                Toast.makeText(this,"Storage permission granted",Toast.LENGTH_SHORT).show();
            }else {
                requestPermission();
            }
        }

        imageView = (ImageView)findViewById(R.id.qr_field);
        text = (EditText)findViewById(R.id.qr_info);
        encodeButton = (Button)findViewById(R.id.generate_button);

        encodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = text.getText().toString().trim();
                generate(msg);
            }
        });
    }

    public void generate(String msg){
        if(msg!=null){
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try{
                BitMatrix bitMatrix = multiFormatWriter.encode(msg, BarcodeFormat.QR_CODE,400,400);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                bitmap = barcodeEncoder.createBitmap(bitMatrix);
                imageView.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.save_button:
                if(checkPermission()){
                    if(bitmap!=null){
                        if(alertManager==null){
                            alertManager = new AlertManager(this,bitmap,customView);
                        }
                        alertManager.exec();
                    }else {
                        Toast.makeText(this,"No image to save",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    requestPermission();
                }
                break;
            case R.id.share_button:
                if(bitmap!=null){
                    if(fileStorage==null){
                        fileStorage = new FileStorage(this,bitmap);
                    }
                    fileStorage.share();
                }
                else {
                    Toast.makeText(this,"No image available to share",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPermission(){
        if(this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=(PackageManager.PERMISSION_GRANTED)){
            return false;
        }else {
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermission(){
        if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(this,"Storage permission required",Toast.LENGTH_SHORT).show();
        }else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }
}

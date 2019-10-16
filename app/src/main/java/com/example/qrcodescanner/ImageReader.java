package com.example.qrcodescanner;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

public class ImageReader extends BaseActivity {

    ImageView imageView;
    TextView resulText,resultInfo;
    Button analyzer,test;
    ActionManager actionManager;
    Bitmap bitmap;
    String info,path;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        getSupportActionBar().setTitle("QR reader");

        imageView = (ImageView)findViewById(R.id.image_field);
        resulText = (TextView)findViewById(R.id.read_result_field);
        analyzer = (Button)findViewById(R.id.analyze_result);
        test = (Button)findViewById(R.id.test_another);
        resultInfo = (TextView)findViewById(R.id.result_info);

        path = getIntent().getStringExtra("qrPath");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                Toast.makeText(getApplicationContext(),"Storage permission granted",Toast.LENGTH_SHORT).show();
            }
            else {
                requestPermission();
            }
        }


        analyzer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionManager = new ActionManager(ImageReader.this,resulText.getText().toString());
                actionManager.action();
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFromGallery();
            }
        });
    }

    public void setResult(String result){
        if(info!=null){
            imageView.setImageDrawable(null);
            imageView.setImageBitmap(bitmap);
            resulText.setText(info);
            resultInfo.setTextColor(ContextCompat.getColor(this,R.color.light_blue));
            resultInfo.setText("Result");
            analyzer.setEnabled(true);
        }else {
            imageView.setImageDrawable(null);
            resultInfo.setTextColor(ContextCompat.getColor(this,R.color.red));
            resultInfo.setText("No QR code detected");
            resulText.setText("");
            analyzer.setEnabled(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if(checkPermission()){
            bitmap = BitmapFactory.decodeFile(path);
            info = read(bitmap);
            setResult(info);
        }else {
            requestPermission();
        }

    }

    public String read(Bitmap bitmap){
        String decodedData = null;
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());

        RGBLuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(),bitmap.getHeight(),pixels);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

        MultiFormatReader multiFormatReader = new MultiFormatReader();
        try {
           Result decodedResult =  multiFormatReader.decode(binaryBitmap);
           BarcodeFormat barcodeFormat= decodedResult.getBarcodeFormat();
           if(barcodeFormat==null){
               return null;
           }
           decodedData = decodedResult.getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return decodedData;
    }

    public void readFromGallery(){
        Intent readIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(readIntent,2);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2){
            if(data!=null){
                Uri uriData = data.getData();

                String[] imageColumn = {MediaStore.Images.Media.DATA};
                Cursor uriToPath = getContentResolver().query(uriData,imageColumn,null,null,null);
                uriToPath.moveToFirst();

                int index = uriToPath.getColumnIndex(imageColumn[0]);
                path = uriToPath.getString(index);
            }
        }
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

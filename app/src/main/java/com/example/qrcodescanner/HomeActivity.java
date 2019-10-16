package com.example.qrcodescanner;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends BaseActivity {

    Button qr_button,generator_button,read_button;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        qr_button = (Button) findViewById(R.id.qr_scan_button);
        generator_button = (Button) findViewById(R.id.qr_generate_button);
        read_button = (Button)findViewById(R.id.read_qr_button);

        qr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scannerIntent = new Intent(HomeActivity.this,Scanner.class);
                startActivity(scannerIntent);
            }
        });

        read_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        });

        generator_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent generatorIntent = new Intent(HomeActivity.this,QrGenerator.class);
                startActivity(generatorIntent);
            }
        });

    }
    public void sendResult(){

    }
}

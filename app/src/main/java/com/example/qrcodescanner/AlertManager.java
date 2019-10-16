package com.example.qrcodescanner;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlertManager extends FileStorage{
    Context context;
    View view;
    EditText image_name;
    Button no_button,yes_btn;
    String name;
    AlertDialog.Builder alert;
    AlertDialog alertDialog;

    public AlertManager(Context context,Bitmap bitmap,View view){
        super(context,bitmap);
        this.context = context;
        this.view = view;
        initiate();
        alert = new AlertDialog.Builder(context);
        alert.setView(view);
        alertDialog = alert.create();
    }

    public void showDialogue(){
        alertDialog.show();
    }

    public void exec(){
        showDialogue();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = simpleDateFormat.format(new Date());
        String qr_name = "Qcode"+date;

        image_name.setText(qr_name);

        no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.hide();
            }
        });

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String temp_name = image_name.getText().toString().trim();
               name = temp_name + ".jpg";
               save(name,"Qr_image");
               Toast.makeText(context,"Image saved successfuly",Toast.LENGTH_SHORT).show();
               alertDialog.hide();
            }
        });
    }

    public void initiate(){
        image_name = (EditText) view.findViewById(R.id.qname_field);
        no_button = (Button) view.findViewById(R.id.cancel_button);
        yes_btn = (Button) view.findViewById(R.id.yes_button);
    }
}

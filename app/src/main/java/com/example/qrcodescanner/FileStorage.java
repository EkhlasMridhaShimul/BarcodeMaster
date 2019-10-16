package com.example.qrcodescanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class FileStorage {

    FileOutputStream fileOutputStream = null;
    Context context;
    Bitmap bitmap;

    public FileStorage(Context context, Bitmap bitmap){
        this.context = context;
        this.bitmap = bitmap;
    }

    public void save(String name,String dir){
        File imageDir = getDir(dir);
        if(!imageDir.exists() && !imageDir.mkdirs()){
            Toast.makeText(context,"Can't create directory",Toast.LENGTH_SHORT).show();
            return;
        }
        String img_path = imageDir.getAbsolutePath()+"/"+name;
        File file = new File(img_path);
        try{
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        refreshGallery(file);
    }

    public void refreshGallery(File file){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }

  /*  public File getDir(){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file,"Qr_image");
    }*/

    public File getDir(String dir){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file,dir);
    }

    public void share(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        //File imageDir = getDir("tmp");
        save("temp.jpg","Qr_image/tmp");
        /*if(!imageDir.exists() && !imageDir.mkdirs()){
            Toast.makeText(context,"Error sharing",Toast.LENGTH_SHORT).show();
            return;
        }
        String img_path = imageDir.getAbsolutePath()+"/"+"temp.jpg";
        File file = new File(img_path);
        try{
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }*/
        intent.putExtra(Intent.EXTRA_STREAM,Uri.parse("file://"+"/sdcard/DCIM/Qr_image/tmp"+"/temp.jpg"));
        context.startActivity(Intent.createChooser(intent,"Share Qr code"));
    }

}

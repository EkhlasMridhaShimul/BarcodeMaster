package com.example.qrcodescanner;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.support.v7.widget.Toolbar;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout fullview;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    View aboutView=null;
    AlertManager aboutDialogue=null;
    Button facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setContentView(int LayoutResId) {
        fullview = (DrawerLayout)getLayoutInflater().inflate(R.layout.activity_base,null);
        if(aboutView==null){
            aboutView = getLayoutInflater().inflate(R.layout.about_layout,null);
            facebook = (Button)aboutView.findViewById(R.id.fb_button);
        }

        FrameLayout activityContainer = (FrameLayout) fullview.findViewById(R.id.contents);
        getLayoutInflater().inflate(LayoutResId,activityContainer,true);
        Toolbar toolbar = (Toolbar)fullview.findViewById(R.id.toolbar_panel);
        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,fullview,toolbar,R.string.menu_open,R.string.menu_close);
        fullview.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView)fullview.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        super.setContentView(fullview);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id){
            case R.id.home_id:
                Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(homeIntent);
                break;
            case R.id.scanner_id:
                Intent scannerIntent = new Intent(getApplicationContext(),Scanner.class);
                startActivity(scannerIntent);
                break;
            case R.id.gallery_button:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
                break;
            case R.id.generator_id:
                Intent generatorIntent = new Intent(getApplicationContext(),QrGenerator.class);
                startActivity(generatorIntent);
                break;
            case R.id.about_id:
                if(aboutDialogue==null){
                    aboutDialogue = new AlertManager(this,null,aboutView);
                }
                aboutDialogue.showDialogue();
                facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent fb = new Intent(Intent.ACTION_VIEW,Uri.parse("https://web.facebook.com/profile.php?id=100012709064551"));
                        startActivity(fb);
                    }
                });
                break;
        }
        fullview.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        actionBarDrawerToggle.syncState();
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if(fullview.isDrawerOpen(GravityCompat.START)){
            fullview.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(data!=null){
                Uri uriData = data.getData();
                String[] imageColumn = {MediaStore.Images.Media.DATA};

                Cursor uriToPath = getContentResolver().query(uriData,imageColumn,null,null,null);
                uriToPath.moveToFirst();

                int index = uriToPath.getColumnIndex(imageColumn[0]);

                String imagePath = uriToPath.getString(index);

                Intent readerIntent = new Intent(BaseActivity.this,ImageReader.class);
                readerIntent.putExtra("qrPath",imagePath);
                startActivity(readerIntent);
            }
        }
    }
}

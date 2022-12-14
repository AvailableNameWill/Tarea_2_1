package com.example.tarea_2_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    static final int peticion_acceso_cam = 1;
    static final int video_capture = 101;

    private VideoView VV1;
    private Button btnRec;
    private Button btnSave;
    private MediaController mc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getObj();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });
    }

    private void getObj(){
        VV1 = (VideoView) findViewById(R.id.VV1);
        btnRec = (Button) findViewById(R.id.btnRec);
        btnSave = (Button) findViewById(R.id.btnSave);
        mc =  new MediaController(this);
        mc.setAnchorView(VV1);
        mc.setMediaPlayer(VV1);
        VV1.setMediaController(mc);
    }

    private void permisos(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, peticion_acceso_cam);
        }else{
            takeVideo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == peticion_acceso_cam){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takeVideo();
            }
        }
    }


    private void takeVideo(){
        Intent vIntent =  new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(vIntent.resolveActivity(getPackageManager()) != null){
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "VIDEO_" + timeStamp + "_.mp4";
            File vid = new File(getExternalFilesDir(null), imageFileName);
            Uri fotoUri= FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".fileprovider", vid);
            vIntent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
            startActivityForResult(vIntent, video_capture);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == video_capture && resultCode == RESULT_OK){
            Uri uri = data.getData();
            VV1.setVideoURI(uri);
            //VV1.start();
        }
    }

}
package com.dam2.trivial_it;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void intentPrincipal(View view){
        Intent IPrincipal = new Intent(this, Principal.class);
        startActivity(IPrincipal);
    }



    public void onStartCommand(){
        mediaPlayer = MediaPlayer.create(this, R.raw.fondo);
        mediaPlayer.start();
    }

}
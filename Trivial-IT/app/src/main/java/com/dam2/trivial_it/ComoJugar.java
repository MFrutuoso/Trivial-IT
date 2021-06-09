package com.dam2.trivial_it;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class ComoJugar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_como_jugar);

        WebView wv = (WebView) findViewById(R.id.como_jugar);
        wv.loadUrl("file:///android_asset/modo_juego.html");
    }
}
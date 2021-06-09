package com.dam2.trivial_it;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class Reglas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglas);

        WebView wv = (WebView) findViewById(R.id.reglas);
        wv.loadUrl("file:///android_asset/reglas.html");
    }
}
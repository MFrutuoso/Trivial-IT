package com.dam2.trivial_it;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void intentPrincipal(View view){
        Intent IPrincipal = new Intent(this, Principal.class);
        startActivity(IPrincipal);
    }

}

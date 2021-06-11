package com.dam2.trivial_it;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityBase extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;

    @Override
    public void onBackPressed() {
        if (backPressedTime+2000 > System.currentTimeMillis()){
            backToast.cancel();
            finish();
            return;
        }
        else{
            backToast = Toast.makeText(getBaseContext(),"Pulsa de nuevo para salir", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

}

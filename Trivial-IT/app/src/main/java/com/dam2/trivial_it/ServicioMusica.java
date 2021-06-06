package com.dam2.trivial_it;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.View;

public class ServicioMusica extends Service {

    private MediaPlayer miReproductor;
    
    @Override
    public void onCreate() {
        super.onCreate();
        miReproductor = null;
        miReproductor = MediaPlayer.create(this, R.raw.fondo);
        miReproductor.setLooping(true);
        miReproductor.setVolume(100f,100f);
    }
    ///Iniciamos el servicio
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        if(miReproductor.isPlaying() == false){
            if(miReproductor != null)
                miReproductor.start();
        }
        return START_STICKY;
    }

    //Paramos el servicio
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(miReproductor.isPlaying() == true) miReproductor.stop();
        miReproductor.release();
        miReproductor = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}
package com.dam2.trivial_it;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Random;

class Partida implements Serializable {
    Jugador j1, j2;
    int turno;
    boolean continuaTurno=false;

    public Partida(Jugador j1, Jugador j2) {
        this.j1 = j1;
        this.j2 = j2;
        this.turno = (int) Math.floor(Math.random()*(2-1+1)+1);  // num aleatorio entre 1 y 2
        this.continuaTurno = false;
    }
}

public class Ruleta extends AppCompatActivity implements Animation.AnimationListener {
    boolean blnButtonRotation = true;
    int intNumber = 7;
    long IngDegrees = 0;
    MediaPlayer mp;
    ImageView selected, imageRoulette;
    String [] tablaCatArray = {"programacionweb", "Comodín", "hardware", "entornosdedesarrollo", "basededatos", "programacion", "sistemasinformaticos" };
    String [] sectors ={"Programación Web", "Comodín", "Hardware", "Entornos de desarrollo","Bases de Datos","Programación", "Sistemas Informáticos"};
    TextView textView;
    Button b_start;
    String resultadoCategoria = null;
    String tablaCat="";
    Partida partida;
    TextView tvJ1, tvJ2;
    ImageView[] imgQuesitosJ1 = new ImageView[6];
    ImageView[] imgQuesitosJ2 = new ImageView[6];
    String ganador="";
    public void encenderQuesito(ImageView[] imgQuesitos, int categoria){
        switch (categoria){
            case 0: imgQuesitos[0].setImageResource(R.drawable.categorias_on_si);
                break;
            case 1: imgQuesitos[1].setImageResource(R.drawable.categorias_on_prog);
                break;
            case 2: imgQuesitos[2].setImageResource(R.drawable.categorias_on_db);
                break;
            case 3: imgQuesitos[3].setImageResource(R.drawable.categorias_on_entornos);
                break;
            case 4: imgQuesitos[4].setImageResource(R.drawable.categorias_on_hw);
                break;
            case 5: imgQuesitos[5].setImageResource(R.drawable.categorias_on_web);
                break;
            default:Log.e("encenderQuesito()","Ocurrió un error en este método");
        }
    }

    public void apagarQuesito(ImageView[] imgQuesitos, int categoria){
        switch (categoria){
            case 0: imgQuesitos[0].setImageResource(R.drawable.categorias_off_si);
                break;
            case 1: imgQuesitos[1].setImageResource(R.drawable.categorias_off_prog);
                break;
            case 2: imgQuesitos[2].setImageResource(R.drawable.categorias_off_db);
                break;
            case 3: imgQuesitos[3].setImageResource(R.drawable.categorias_off_entornos);
                break;
            case 4: imgQuesitos[4].setImageResource(R.drawable.categorias_off_hw);
                break;
            case 5: imgQuesitos[5].setImageResource(R.drawable.categorias_off_web);
                break;
            default:Log.e("apagarQuesito()","Ocurrió un error en este método");
        }
    }

    public void mostrarTurno(){
        String jugador="";
        String tituloAlert ="TURNO";
        if (partida.turno==1) jugador=partida.j1.getNick();
        else jugador=partida.j2.getNick();
        String mensajeAlert="\nTurno de "+jugador+" (J"+partida.turno+")";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensajeAlert)
                .setTitle(tituloAlert);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void mostrarComodin(){
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elige una categoría");

// add a list
        String[] categorias = {"Base de datos", "Programación", "Programación Web", "Sistemas informáticos",
                "Entornos de desarrollo", "Hardware"};
        builder.setItems(categorias, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: resultadoCategoria=categorias[0]; tablaCat="basededatos"; break;
                    case 1: resultadoCategoria=categorias[1]; tablaCat="programacion"; break;
                    case 2: resultadoCategoria=categorias[2]; tablaCat="programacionweb"; break;
                    case 3: resultadoCategoria=categorias[3]; tablaCat="sistemasinformaticos"; break;
                    case 4: resultadoCategoria=categorias[4]; tablaCat="entornosdedesarrollo"; break;
                    case 5: resultadoCategoria=categorias[5]; tablaCat="hardware"; break;
                }
                iniciarPregunta();
            }
        });

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruleta);

        //Establecemos los nombres de los jugadores en los paneles correspondientes
        TextView tvJ1 = findViewById(R.id.tv_J1);
        TextView tvJ2 = findViewById(R.id.tv_J2);

        Ruleta quesitosJ1 = new Ruleta();
        Ruleta quesitosJ2 = new Ruleta();

        b_start = (Button)findViewById(R.id.buttonStart);
        textView = findViewById(R.id.txtv);
        selected = (ImageView)findViewById(R.id.imageSelected);
        imageRoulette = (ImageView)findViewById(R.id.rouletteImage);

        //Asociamos las ImagesView con el objeto Jugador que corresponde
        imgQuesitosJ1[0] = (ImageView)findViewById(R.id.img_SistemasJ1);
        imgQuesitosJ1[1] = (ImageView)findViewById(R.id.img_ProgramacionJ1);
        imgQuesitosJ1[2] = (ImageView)findViewById(R.id.img_BaseDeDatosJ1);
        imgQuesitosJ1[3] = (ImageView)findViewById(R.id.img_EntornosJ1);
        imgQuesitosJ1[4] = (ImageView)findViewById(R.id.img_HardwareJ1);
        imgQuesitosJ1[5] = (ImageView)findViewById(R.id.img_ProgramacionWebJ1);

        //Asociamos las ImagesView con el objeto Jugador que corresponde
        imgQuesitosJ2[0] = (ImageView)findViewById(R.id.img_SistemasJ2);
        imgQuesitosJ2[1] = (ImageView)findViewById(R.id.img_ProgramacionJ2);
        imgQuesitosJ2[2] = (ImageView)findViewById(R.id.img_BaseDeDatosJ2);
        imgQuesitosJ2[3] = (ImageView)findViewById(R.id.img_EntornosJ2);
        imgQuesitosJ2[4] = (ImageView)findViewById(R.id.img_HardwareJ2);
        imgQuesitosJ2[5] = (ImageView)findViewById(R.id.img_ProgramacionWebJ2);


        //Parametros traídos de la actividad anterior a través del intent
        Bundle extras = getIntent().getExtras();

        if (extras != null) { //Si venimos de responder una pregunta
            partida = (Partida) getIntent().getSerializableExtra("partida");
            tvJ1.setText(partida.j1.getNick());
            tvJ2.setText(partida.j2.getNick());

            for (int i=0; i<partida.j1.getQuesitos().length; i++){
                if (partida.j1.getQuesitos()[i] == true){
                   encenderQuesito(imgQuesitosJ1,i);
                }
                if (partida.j2.getQuesitos()[i] == true){
                    encenderQuesito(imgQuesitosJ2,i);
                }
            }
            if (partida.j1.contarQuesitos()==6){
                ganador=partida.j1.getNick();
                mostrarGanador();
            }

            if (partida.j2.contarQuesitos()==6){
                ganador=partida.j2.getNick();
                mostrarGanador();
            }
            System.out.println(partida.j1.resultadosJugador());
            System.out.println(partida.j2.resultadosJugador());

        }else { //Si no viene de quiz

            Jugador j1 = new Jugador(Login.nick, imgQuesitosJ1);
            tvJ1.setText(j1.getNick());

            Jugador j2 = new Jugador("Pepe", imgQuesitosJ2);
            tvJ2.setText(j2.getNick());

            partida = new Partida(j1,j2); //Se instancia un objeto partida.
        }
        if(!ganador.equalsIgnoreCase("")) mostrarTurno();
        textView.setText("El turno es de J"+partida.turno);
    }

    private void mostrarGanador() {


        String tituloAlert ="GANADOR: "+ganador+"!";
        String mensajeAlert=partida.j1.resultadosJugador()+"\n\n"+partida.j2.resultadosJugador();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensajeAlert)
                .setTitle(tituloAlert);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Ruleta.this, ModoDeJuego.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onAnimationStart(Animation animation){
        this.blnButtonRotation=false;
        b_start.setVisibility(View.VISIBLE);
        mp = MediaPlayer.create(this, R.raw.sonido);
        mp.start();
    }

    @Override
    public void onAnimationEnd(Animation animation){
        @SuppressLint("WrongConstant") Toast toast = Toast.makeText(this, " " + String.valueOf((int)(((double)this.intNumber)
                - Math.floor(((double)this.IngDegrees) / (360.0d / ((double)this.intNumber))))) + "  ",0);
        toast.setGravity(49,0,0);
        //toast.show();
        this.blnButtonRotation = true;
        b_start.setGravity(View.VISIBLE);
        CalculatePoint(IngDegrees);
    }
    @Override
    public void onAnimationRepeat(Animation animation){
    }

    public void onClickButtonRotation (View v){
        if(this.blnButtonRotation){
            int ran = new Random().nextInt(360)+3600;
            RotateAnimation rotateAnimation = new RotateAnimation((float)this.IngDegrees, (float)
                    (this.IngDegrees + ((long)ran)),1,0.5f,1,0.515f);
            this.IngDegrees = (this.IngDegrees + ((long)ran)) % 360;
            rotateAnimation.setDuration((long)ran);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setInterpolator(new DecelerateInterpolator());
            rotateAnimation.setAnimationListener(this);
            imageRoulette.setAnimation(rotateAnimation);
            imageRoulette.startAnimation(rotateAnimation);
        }

    }

    private void iniciarPregunta() {
        Intent i = new Intent(this, Quiz.class);
        i.putExtra("categoria",resultadoCategoria); //Titulo de a categoría
        i.putExtra("tablaCat" ,tablaCat); //Nombre de la tabla que consultaremos en la DB
        i.putExtra("intentPractica",false);
        i.putExtra("partida",partida);
        startActivity(i);
    }

    public void CalculatePoint(long degree){
        double initialPoint = 0;
        double endPoint=51.42857142857143;
        int i=0;

        do{
            if(degree > initialPoint && degree < endPoint){
                resultadoCategoria=sectors[i]; //resultadoCategoria = Categoría resultante de la Ruleta
                tablaCat = tablaCatArray[i]; //Guardamos el nombre de la tabla de la base de datos

                if(sectors[i] != "Comodín")
                    iniciarPregunta(); //Intents a ventana con pregunta sector[i];
                else {mostrarComodin();} //"Comodín" -> Iniciamos emergente preguntando categoría a elegir
            }
            initialPoint+=51.42857142857143; endPoint+=51.42857142857143;
            i++;
        }while(resultadoCategoria == null);
        textView.setText(resultadoCategoria); //TextView prescindible, muestra categoría elegida.
    }

    public void btnAtras(View view) {

        String mensajeAlert="Seguro que quieres salir?";
        String tituloAlert ="Cuidado!";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensajeAlert)
                .setTitle(tituloAlert);
        builder.setPositiveButton("Si, salir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Ruleta.this, ModoDeJuego.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
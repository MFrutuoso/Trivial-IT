package com.dam2.trivial_it;

import android.app.ApplicationExitInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.rtt.ResponderLocation;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    static Boolean encendida = true;
    EditText edtUsuario, edtPassword;
    Button btnLogin;
    String nick, pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtUsuario=findViewById(R.id.etUsuario);
        edtPassword=findViewById(R.id.etpass);
        btnLogin=findViewById(R.id.btnLogin);

        recuperarPreferencias();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nick = edtUsuario.getText().toString();
                pass = edtPassword.getText().toString();
                //DIRECCION DONDE SE ENCUENTRA EL VALIDAR USUARIO PHP
                if(!nick.isEmpty() && !pass.isEmpty()){
                    validarUsuario("https://trivialit2021.000webhostapp.com/validar_usuario.php");
                }else{
                    Toast.makeText(Login.this, "Campos vacíos", Toast.LENGTH_SHORT).show();
                }

            }
        });
        if(encendida==null){ //Enciende la musica por primera vez para toda la app
            encenderMusica();
        }
    }
    public void btnRegistro(View view){
        Intent IRegistro = new Intent(this, Registro.class);
        startActivity(IRegistro);
    }
    //METODO PARA VALIDAR EL USUARIO Y LA CONTRASEÑA CON LA DB
    private void validarUsuario(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //SI EL USUARIO Y LA CONTRASEÑA ESTÁN CORRECTAS, INICIA LA ACTIVIDAD SI COINCIDE CON LA BASE DE DATOS, AL SER ERRÓNEOS NO PERMITIRÁ EL ACCESO AL INTENT
                if(!response.isEmpty()){
                    guardarPreferencias();
                    Intent intent=new Intent(getApplicationContext(), Principal.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(Login.this, "Usuario o contraseña erróneas", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //COMPARAMOS EL NICK Y LA PASS DE LA BASE DE DATOS CON LOS INTRODUCIDOS EN LA APLICACION, (edtUsuario, edtPassword = id de los editText) (nick, pass = campos de la BD)
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("nick", nick);
                parametros.put("pass", pass);
                return parametros;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //
    //!!!!!!!!!!!!! INTENT ANTES DE la VERIFICACION DE USUARIO !!!!!!!!!!!!!!!!!!!!!!
    //Inicia sesión y nos lleva al menú principal
    /*public void intentPrincipal(View view){
        Intent IPrincipal = new Intent(this, Principal.class);
        startActivity(IPrincipal);
    }*/

    //!!!!!!    METODO PARA GUARDAR EL USUARIO Y EVITAR TENER QUE VOLVER A LOGUEARSE AL CERRAR LA APP   !!!!!!!
    private void guardarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("nick", nick);
        editor.putString("pass", pass);
        editor.putBoolean("sesion", true);
        editor.commit();
    }

    //!!!!!!!   MÉTODO PARA RECUPERAR LOS DATOS !!!!!!
    private void recuperarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        edtUsuario.setText(preferences.getString("nick", ""));
        edtPassword.setText(preferences.getString("pass", ""));
    }

    public void encenderMusica(){
        if(encendida==null){
            Intent miReproductor = new Intent(this, ServicioMusica.class);
            this.startService(miReproductor);
            encendida = true;

        }

    }

}
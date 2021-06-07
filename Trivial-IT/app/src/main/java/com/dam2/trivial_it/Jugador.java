package com.dam2.trivial_it;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Switch;

import java.io.Serializable;
import java.util.Arrays;

public class Jugador implements Serializable {
    private String nick;
    private boolean [] quesitos = new boolean[6];
    private int preguntasAcertadas;
    private int preguntasFalladas;
    private int puntuacionTotal;

    public Jugador(String nick, ImageView[] imgQuesitos) {
        this.nick = nick;
        this.quesitos[0] = false;
        this.quesitos[1] = false;
        this.quesitos[2] = false;
        this.quesitos[3] = false;
        this.quesitos[4] = false;
        this.quesitos[5] = false;
        this.preguntasAcertadas=0;
        this.preguntasFalladas=0;
        this.puntuacionTotal=0;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public boolean[] getQuesitos() {
        return quesitos;
    }

    public void setQuesitos(int categoria, boolean quesito) {
        this.quesitos[categoria]=quesito;
    }

    public int getPreguntasAcertadas() {
        return preguntasAcertadas;
    }

    public void setPreguntasAcertadas() {
        this.preguntasAcertadas++;
    }

    public int getPreguntasFalladas() {
        return preguntasFalladas;
    }

    public void setPreguntasFalladas() {
        this.preguntasFalladas++;
    }

    public int getPuntuacionTotal() {
        return puntuacionTotal;
    }

    public void setPuntuacionTotal(int puntuacionTotal) {
        this.puntuacionTotal = puntuacionTotal;
    }

    public int contarQuesitos(){
        int quesitosObtenidos=0;

        for (int i=0; i<quesitos.length; i++){
            if(quesitos[i] == true) quesitosObtenidos++;
        }

        return quesitosObtenidos;
    }

    public int calcularPuntuacionTotal(){
        puntuacionTotal=0;
        puntuacionTotal+=preguntasAcertadas*100;
        puntuacionTotal-=preguntasFalladas*50;
        puntuacionTotal+=contarQuesitos()*500;

        return puntuacionTotal;
    }

    public String resultadosJugador() {
        return "\nJugador: " + nick +
                "\n\tCategorías completadas: " + contarQuesitos() +
                "\n\t\tPreguntas Acertadas: " + preguntasAcertadas +
                "\n\t\tPreguntas Falladas: " + preguntasFalladas +
                "\n\t\tPuntuacion Total: " + calcularPuntuacionTotal();
    }


}

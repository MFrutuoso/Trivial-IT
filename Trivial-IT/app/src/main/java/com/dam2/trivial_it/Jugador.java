package com.dam2.trivial_it;

import java.util.Arrays;

public class Jugador {
    private String nick;
    private boolean [] quesitos = new boolean[6];
    private int preguntasAcertadas;
    private int preguntasFalladas;
    private int puntuacionTotal;

    public Jugador(String nick) {
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

    public void setQuesitos(boolean[] quesitos) {
        this.quesitos = quesitos;
    }

    public int getPreguntasAcertadas() {
        return preguntasAcertadas;
    }

    public void setPreguntasAcertadas(int preguntasAcertadas) {
        this.preguntasAcertadas = preguntasAcertadas;
    }

    public int getPreguntasFalladas() {
        return preguntasFalladas;
    }

    public void setPreguntasFalladas(int preguntasFalladas) {
        this.preguntasFalladas = preguntasFalladas;
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

        puntuacionTotal+=preguntasAcertadas*100;
        puntuacionTotal-=preguntasFalladas*50;
        puntuacionTotal+=contarQuesitos()*500;

        return puntuacionTotal;
    }

    public String resultadosJugador() {
        return "\nJugador: " + nick +
                "\n\tCategorías completadas: " + contarQuesitos() +
                "\n\tPreguntas Acertadas: " + preguntasAcertadas +
                "\n\tPreguntas Falladas: " + preguntasFalladas +
                "\n\tPuntuacion Total: " + calcularPuntuacionTotal();
    }
}

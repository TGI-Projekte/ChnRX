/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.nailuj.chnrx;

import java.awt.Color;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author Julian
 */
public class Spiel {

    private Feld[][] felder;
    private Spieler aktuellerSpieler;
    private ArrayList<Spieler> spieler;
    private String statusUUID;

    public static class Feld {
        // Anfang Attribute1

        private Color farbe;
        private int anzahl;
        private String uuidbesitzer;
        // Ende Attribute1

        public Feld() {
            farbe = Color.BLACK;
            anzahl = 0;
        }
        // Anfang Methoden1

        public void setzeFarbe(Color col) {
            farbe = col;
        }

        public Color holeFarbe() {
            return farbe;
        }

        public void setzeBesitzerUuid(String uuid) {
            uuidbesitzer = uuid;
        }

        public String holeBesitzerUuid() {
            return uuidbesitzer;
        }

        public void setzeAnzahl(int setzendeanzahl) {
            anzahl = setzendeanzahl;
        }

        public int holeAnzahl() {
            return anzahl;
        }
        // Ende Methoden1
    }

    /**
     * @return the felder
     */
    public Feld[][] getFelder() {
        return felder;
    }

    /**
     * @param felder the felder to set
     */
    public void setFelder(Feld[][] felder) {
        this.felder = felder;
    }

    /**
     * @return the aktuellerSpieler
     */
    public Spieler getAktuellerSpieler() {
        return aktuellerSpieler;
    }

    /**
     * @param aktuellerSpieler the aktuellerSpieler to set
     */
    public void setAktuellerSpieler(Spieler aktuellerSpieler) {
        this.aktuellerSpieler = aktuellerSpieler;
    }

    /**
     * @return the spieler
     */
    public ArrayList<Spieler> getSpieler() {
        return spieler;
    }

    /**
     * @param spieler the spieler to set
     */
    public void setSpieler(ArrayList<Spieler> spieler) {
        this.spieler = spieler;
    }
    
    public String getUUID(){
        return statusUUID;
    }
    
    public void newUUID(){
        statusUUID = UUID.randomUUID().toString();
    }
}

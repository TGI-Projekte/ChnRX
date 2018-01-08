/*
 * Copyright (C) 2018 Zisis Relas, Florian Rost, Julian Blazek
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

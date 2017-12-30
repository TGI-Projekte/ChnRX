/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chnrx;

import java.awt.Color;
import java.util.UUID;

/**
 *
 * @author Julian
 */
class Spieler {

    // Anfang Attribute
    private Color farbe;
    private String name;
    private boolean istAI;
    private final String uuidstring;
    // Ende Attribute

    public Spieler(Color clfarbe, String name, boolean istAI) {
        this.farbe = clfarbe;
        this.name = name;
        this.istAI = istAI;
        UUID uuid = UUID.randomUUID();
        uuidstring = uuid.toString();
    }
    // Anfang Methoden

    public String getName() {
        return name;
    }

    public Color getFarbe() {
        return farbe;
    }

    public boolean istAI() {
        return istAI;
    }

    public void setName(String nameNeu) {
        name = nameNeu;
    }

    public void setIstAI(boolean istAINeu) {
        istAI = istAINeu;
    }

    public void setFarbe(Color farbeNeu) {
        farbe = farbeNeu;
    }

    public String getUuidstring() {
        return uuidstring;
    }

    // Ende Methoden
}

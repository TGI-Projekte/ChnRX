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

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

import javax.swing.UIManager;

/**
 *
 * @author Julian
 */
public class ChnRX {

    private static Steuerung steu;
    public static final String version = "v0.8.0";

    public ChnRX() {
        // Setzt das LaF auf das System LaF..
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            e.printStackTrace();
        }
        steu = new Steuerung();
    }

    public static void restart() {
        System.out.println("Spiel wird neugestartet..");
        steu = null;
        System.gc();
        steu = new Steuerung();
    }

    public static void main(String[] args) {
        new ChnRX();
    }

}

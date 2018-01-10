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
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

/**
 * Steuerung.java Zweck: Verbindet SpielSteuerung und SpielGUI und ist zuständig
 * für das Einrichten der Parameter für das Spiel durch die Abfrage mit der
 * EinstellungenGUI
 */
class Steuerung {

    private SpielGUI gui;
    private EinstellungenGUI einstgui;
    private ArrayList<Spieler> spieler;
    private SpielSteuerung spiel;

    private final boolean useImages = true;

    private BufferedImage[] imgArr = new BufferedImage[4];

    public Steuerung() {
        try {
            imgArr[0] = ImageIO.read(this.getClass().getResource("/Atom1.png"));
            imgArr[1] = ImageIO.read(this.getClass().getResource("/Atom2.png"));
            imgArr[2] = ImageIO.read(this.getClass().getResource("/Atom3.png"));
            imgArr[3] = ImageIO.read(this.getClass().getResource("/Atom4.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        einstgui = new EinstellungenGUI(this);
        spieler = new ArrayList<>();
    }

    public void addSpieler(Spieler spielerobj) {
        for (Spieler spielerlistenobj : spieler) {
            if (spielerlistenobj.getUuidstring() == spielerobj.getUuidstring()) {
                int index = spieler.indexOf(spielerlistenobj);
                spieler.set(index, spielerobj);
                return;
            }
        } // end of for  
        spieler.add(spielerobj);
    } // end of if

    public void delSpieler(Spieler spielerobj) {
        for (Spieler spielerlistenobj : spieler) {
            if (spielerlistenobj.getUuidstring() == spielerobj.getUuidstring()) {
                int index = spieler.indexOf(spielerlistenobj);
                spieler.remove(index);
                return;
            }
        } // end of for  
    }

    public void aktualisiereSpielerListe() {
        DefaultListModel listmodel = einstgui.getListSpielerModel();
        listmodel.clear();
        for (Spieler spielerobj : spieler) {
            listmodel.addElement(spielerobj.getName());
        } // end of for
        einstgui.setListSpielerModel(listmodel);
    }

    public Spieler holeSpieler(int index) {
        if (spieler.isEmpty()) {
            System.out.println("Keine Spieler vorhanden..");
            return null;
        } else {
            try {
                return spieler.get(index);
            } catch (IndexOutOfBoundsException ex) {
                return null;
            }
        } // end of if
    }

    public void startClicked() {
        Dimension groesse = einstgui.getSelectedGroesse();
        gui = new SpielGUI(this, groesse);
        spiel = new SpielSteuerung(this, spieler, groesse, einstgui.isOnline(), einstgui.isHost());
    }

    public void aktualisiereSpielfeld(Spiel.Feld[][] felder) {
        //    System.out.println(""+felder.length+", "+felder[0].length);
        for (int i = 0; i < felder.length; i++) {
            for (int j = 0; j < felder[0].length; j++) {
                //        System.out.println(""+i+", "+j);
                if (!useImages) {
                    gui.setzeFeldTextFarbe(felder[i][j].holeFarbe(), i, j);
                    gui.setzeFeldText("" + felder[i][j].holeAnzahl(), i, j);
                    continue;
                }
                BufferedImage image = null;

                switch (felder[i][j].holeAnzahl()) {
                    case 1:
                        image = imgArr[0];
                        break;
                    case 2:
                        image = imgArr[1];
                        break;
                    case 3:
                        image = imgArr[2];
                        break;
                    case 4:
                        image = imgArr[3];
                        break;
                    default:
                        gui.setzeFeldText("", i, j);
                        gui.setzeFeldBild(null, i, j);
                        continue;
                }
                Color col = felder[i][j].holeFarbe();
                ImageFilter filter = new HueFilter(col);
                FilteredImageSource filteredSource = new FilteredImageSource(image.getSource(), filter);
                Image filtered = Toolkit.getDefaultToolkit().createImage(filteredSource);
                filtered = filtered.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                gui.setzeFeldBild(new ImageIcon(filtered), i, j);
            } // end of for
        } // end of for
    }

    public void feldGedrueckt(int x, int y) {
        spiel.feldGedrueckt(x, y);
    }

    public void setzeRandFarbe(Color farbe) {
        gui.changeBorderColor(farbe);
    }

    public SpielGUI getGUI() {
        return gui;
    }
}

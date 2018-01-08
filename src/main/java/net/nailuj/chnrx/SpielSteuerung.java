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
import java.awt.Point;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Julian
 */
class SpielSteuerung {

    // Anfang Attribute
    private Spiel spiel;
//    private ArrayList<Spieler> spieler;
    private Steuerung steu;
//    private Feld[][] felder;
//    private Spieler aktuellerSpieler;
    private int aktuellerIndex = 0;
    private boolean akzeptiereKlicks = false;
    private String serverurl = "https://whispering-waste.glitch.me/";
    private Networking web;
    private Spieler eigenerSpieler; // Nur bei Netzwerk benutzt
    private boolean spielWirdGeaendert = false;
    Thread refreshWeb;
    // Ende Attribute

    public SpielSteuerung(Steuerung steuobj, ArrayList<Spieler> spielerliste, Dimension groesse, boolean isOnline, boolean isHost) {
        spielWirdGeaendert = true;
        steu = steuobj;
        if (isOnline) {
            web = new Networking(serverurl);
            if (isHost) {
                eigenerSpieler = spielerliste.get(0);
            } else {
                spiel = web.getGameWeb();
                for (Spieler spieler : spiel.getSpieler()) {
                    if (spielerliste.get(0).getName().equals(spieler.getName())) {
                        eigenerSpieler = spieler;
                    }
                }
            }
            refreshWeb = new Thread(() -> {
                while (true) {
                    if (!spielWirdGeaendert) {
                        akzeptiereKlicks = false;
                        Spiel tempSpiel = web.getGameWeb();
                        System.out.println("invoked!");
                        if (!spiel.getUUID().equals(tempSpiel.getUUID())) {
                            spiel = tempSpiel;
                            berechneSpiel();
                        }
                        steu.aktualisiereSpielfeld(spiel.getFelder());
                        steu.setzeRandFarbe(spiel.getAktuellerSpieler().getFarbe());
                        akzeptiereKlicks = true;
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SpielSteuerung.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            refreshWeb.start();
        }
        if (isOnline && isHost || !isOnline) {
            spiel = new Spiel();
            spiel.setSpieler(spielerliste);
            spiel.setFelder(new Spiel.Feld[(int) groesse.getWidth()][(int) groesse.getHeight()]);
            for (int i = 0; i < spiel.getFelder().length; i++) {
                for (int j = 0; j < spiel.getFelder()[0].length; j++) {
                    spiel.getFelder()[i][j] = new Spiel.Feld();
                } // end of for
            } // end of for

            spiel.setAktuellerSpieler(spiel.getSpieler().get(0));
        }
        steu.setzeRandFarbe(spiel.getAktuellerSpieler().getFarbe());
        steu.aktualisiereSpielfeld(spiel.getFelder());
        if (isHost) {
            spiel.newUUID();
            web.postGameWeb(spiel);
        }
        akzeptiereKlicks = true;
        spielWirdGeaendert = false;
    }
    // Anfang Methoden

    public void feldGedrueckt(int x, int y) {
        System.out.println("");
        if (!akzeptiereKlicks) {
            return;
        } // end of if
        if (spiel.getFelder()[x][y].holeBesitzerUuid() != null) {
            if (!spiel.getFelder()[x][y].holeBesitzerUuid().equals(spiel.getAktuellerSpieler().getUuidstring())) {
                return;
            } // end of if
        }
        if (web != null) {
            if (!eigenerSpieler.getUuidstring().equals(spiel.getAktuellerSpieler().getUuidstring())) {
                System.out.println("Not your turn!");
                return;
            }
        }
        spielWirdGeaendert = true;
        Spieler aktuellerSpieler;
        if (web == null) {
            aktuellerSpieler = spiel.getAktuellerSpieler();
        } else {
            aktuellerSpieler = eigenerSpieler;
        }
        spiel.getFelder()[x][y].setzeFarbe(aktuellerSpieler.getFarbe());
        spiel.getFelder()[x][y].setzeBesitzerUuid(aktuellerSpieler.getUuidstring());
        spiel.getFelder()[x][y].setzeAnzahl(spiel.getFelder()[x][y].holeAnzahl() + 1);
        steu.aktualisiereSpielfeld(spiel.getFelder());

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                akzeptiereKlicks = false;

                if (web != null) {
                    spiel.newUUID();
                    web.postGameWeb(spiel);
                }

                berechneSpiel();
                
                steu.aktualisiereSpielfeld(spiel.getFelder());
                steu.setzeRandFarbe(spiel.getAktuellerSpieler().getFarbe());
                akzeptiereKlicks = true;
                spielWirdGeaendert = false;
            }
        });
        t.start();

    }

    private void berechneSpiel() {
        ArrayList<Point> punkte = new ArrayList<>();
        for (int i = 0; i < spiel.getFelder().length; i++) {
            for (int j = 0; j < spiel.getFelder()[0].length; j++) {
                if (spiel.getFelder()[i][j].holeAnzahl() >= 4) {
                    punkte.add(new Point(i, j));
                } // end of if
            } // end of for
        } // end of for
        if (!punkte.isEmpty()) {
            berechneExplosionen(punkte);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            steu.aktualisiereSpielfeld(spiel.getFelder());
            berechneSpiel();
        } // end of if

        gewonnenCheck:
        for (Spieler spieler : spiel.getSpieler()) {
            for (int i = 0; i < spiel.getFelder().length; i++) {
                for (int j = 0; j < spiel.getFelder()[0].length; j++) {
                    if (spiel.getFelder()[i][j].holeBesitzerUuid() != spieler.getUuidstring()) {
                        continue gewonnenCheck;
                    }
                }
            }
            refreshWeb.stop();
            JOptionPane.showMessageDialog(null, "" + spieler.getName() + " hat gewonnen!");
        }
        naechsterSpieler();
    }

    private void berechneExplosionen(ArrayList<Point> punkte) {
        for (Point punkt : punkte) {
            int x = punkt.x;
            int y = punkt.y;
            spiel.getFelder()[x][y].setzeAnzahl(0);
            spiel.getFelder()[x][y].setzeFarbe(Color.BLACK);
            spiel.getFelder()[x][y].setzeBesitzerUuid(null);
            if (x != 0) {
                spiel.getFelder()[x - 1][y].setzeAnzahl(spiel.getFelder()[x - 1][y].holeAnzahl() + 1);
                spiel.getFelder()[x - 1][y].setzeFarbe(spiel.getAktuellerSpieler().getFarbe());
                spiel.getFelder()[x - 1][y].setzeBesitzerUuid(spiel.getAktuellerSpieler().getUuidstring());
            } // end of if
            if (x != spiel.getFelder().length - 1) {
                spiel.getFelder()[x + 1][y].setzeAnzahl(spiel.getFelder()[x + 1][y].holeAnzahl() + 1);
                spiel.getFelder()[x + 1][y].setzeFarbe(spiel.getAktuellerSpieler().getFarbe());
                spiel.getFelder()[x + 1][y].setzeBesitzerUuid(spiel.getAktuellerSpieler().getUuidstring());
            } // end of if
            if (y != 0) {
                spiel.getFelder()[x][y - 1].setzeAnzahl(spiel.getFelder()[x][y - 1].holeAnzahl() + 1);
                spiel.getFelder()[x][y - 1].setzeFarbe(spiel.getAktuellerSpieler().getFarbe());
                spiel.getFelder()[x][y - 1].setzeBesitzerUuid(spiel.getAktuellerSpieler().getUuidstring());
            } // end of if
            if (y != spiel.getFelder()[0].length - 1) {
                spiel.getFelder()[x][y + 1].setzeAnzahl(spiel.getFelder()[x][y + 1].holeAnzahl() + 1);
                spiel.getFelder()[x][y + 1].setzeFarbe(spiel.getAktuellerSpieler().getFarbe());
                spiel.getFelder()[x][y + 1].setzeBesitzerUuid(spiel.getAktuellerSpieler().getUuidstring());
            } // end of if
        } // end of for
    }
    // Ende Methoden

    private void naechsterSpieler() {
        if (aktuellerIndex == (spiel.getSpieler().size() - 1)) {
            aktuellerIndex = 0;
        } else {
            aktuellerIndex++;
        } // end of if-else
        spiel.setAktuellerSpieler(spiel.getSpieler().get(aktuellerIndex));
    }
}

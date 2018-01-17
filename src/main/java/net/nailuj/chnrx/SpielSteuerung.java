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
 * SpielSteuerung.java Zweck: Die Hauptklasse für das Steuern des Spiels.
 */
class SpielSteuerung {

    private Spiel spiel;
    private Steuerung steu;
    private int aktuellerIndex = 0;
    private boolean akzeptiereKlicks = false;
    private String serverurl = "https://chnrx-server.glitch.me/"; // Server 01
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

            // Setzt den Korrekten Spieler
            if (isHost) {
                eigenerSpieler = spielerliste.get(0);
            } else { // isClient
                spiel = web.getGameWeb();
                for (Spieler spieler : spiel.getSpieler()) {
                    if (spielerliste.get(0).getName().equals(spieler.getName())) {
                        eigenerSpieler = spieler;
                    }
                }
                if (eigenerSpieler == null) {
                    System.err.println("Kein Spieler mit den Namen " + spielerliste.get(0).getName() + " wurde in dem Spiel auf " + serverurl + " gefunden!");
                }
            }

            refreshWeb = new Thread(() -> {
                while (true) {
                    // Das Spiel wird nur vom Server abgerufen wenn es im Moment auch wirklich nicht bearbeitet wird.
                    if (!spielWirdGeaendert) {
                        akzeptiereKlicks = false;
                        Spiel tempSpiel = web.getGameWeb();

                        /*
                        Falls das Spiel keine änderung vorweist,
                        so ersetze das aktuelle nicht und berechne es auch nicht neu.
                         */
                        if (!spiel.getUUID().equals(tempSpiel.getUUID())) {
                            spiel = tempSpiel;
                            berechneSpiel();
                            naechsterSpieler(); // Setzt den Spieler auf den nächsten Spieler
                        }

                        steu.aktualisiereSpielfeld(spiel.getFelder());
                        steu.setzeRandFarbe(spiel.getAktuellerSpieler().getFarbe());
                        akzeptiereKlicks = true;
                    }
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SpielSteuerung.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
            refreshWeb.start();
        }

        // Initialisiert das Spiel auf Datenebene
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

        if (isHost) { // Postet das neu initialisierte Spiel
            spiel.newUUID(); // Setzt eine neue UUID da änderungen am Objekt gemacht wurden
            web.postGameWeb(spiel);
        }

        akzeptiereKlicks = true;
        spielWirdGeaendert = false;
    }
    // Anfang Methoden
    private static final Logger LOG = Logger.getLogger(SpielSteuerung.class.getName());

    public void feldGedrueckt(int x, int y) {
        System.out.println("");
        if (!akzeptiereKlicks) {
            return;
        } // end of if
        if (spiel.getFelder()[x][y].holeBesitzerUuid() != null) {
            if (!spiel.getFelder()[x][y].holeBesitzerUuid().equals(spiel.getAktuellerSpieler().getUuidstring())) {
                return; // Falls das Feld bereits besetzt wurde aber dem aktuellen Spieler nicht gehört so mache nichts.
            } // end of if
        }
        if (web != null) {
            if (!eigenerSpieler.getUuidstring().equals(spiel.getAktuellerSpieler().getUuidstring())) {
                System.out.println("Du bist nicht dran!");
                return;
                /*
                Falls der eigene Spieler nicht dem durch das Spiel definierte
                aktuellen Spieler entspricht so mache nichts.
                 */
            }
        }
        spielWirdGeaendert = true;
        /*
        Verhindert, dass das Spiel während der Berechnung vom Spiel vom Server überschrieben wird.
         */

        Spieler aktuellerSpieler;
        if (web == null) {
            aktuellerSpieler = spiel.getAktuellerSpieler();
        } else {
            aktuellerSpieler = eigenerSpieler;
        }
        spiel.getFelder()[x][y].setzeFarbe(aktuellerSpieler.getFarbe());
        spiel.getFelder()[x][y].setzeBesitzerUuid(aktuellerSpieler.getUuidstring());
        spiel.getFelder()[x][y].setzeAnzahl(spiel.getFelder()[x][y].holeAnzahl() + 1);
        aktuellerSpieler.setActive();
        steu.aktualisiereSpielfeld(spiel.getFelder());

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                akzeptiereKlicks = false;

                /*
                Postet die geänderte Version des Spiels mit einer neuen UUID
                was bei den anderen Clients bewirkt, dass diese das Spiel
                updaten und bei sich lokal neu berechnen.
                 */
                if (web != null) {
                    spiel.newUUID();
                    web.postGameWeb(spiel);
                }

                berechneSpiel();
                naechsterSpieler(); // Setzt den Spieler auf den nächsten Spieler

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
                    // Fügt alle Koordinaten von Feldern einer Liste hinzu deren Werte 4 oder höher sind.
                    punkte.add(new Point(i, j));
                } // end of if
            } // end of for
        } // end of for
        if (!punkte.isEmpty()) {
            berechneExplosionen(punkte);
            try {
                Thread.sleep(500);
                /*
                Das warten erzeugt einen Effekt wodurch das Spiel in
                Stufen berechnet wird.
                 */
            } catch (InterruptedException e) {
            }
            steu.aktualisiereSpielfeld(spiel.getFelder());
            berechneSpiel();
        } // end of if

        /*
        Überprüft ob ein Spieler gewonnen hat.
         */
        if (punkte.isEmpty()) {
            ArrayList<Spieler> zuLoeschen = new ArrayList<>();
            gewonnenCheck1:
            for (Spieler spieler : spiel.getSpieler()) {
                if (spieler.istAktiv()) {
                    for (int i = 0; i < spiel.getFelder().length; i++) {
                        for (int j = 0; j < spiel.getFelder()[0].length; j++) {
                            System.out.println(spiel.getFelder()[i][j].holeBesitzerUuid());
                            if (spiel.getFelder()[i][j].holeBesitzerUuid() != null && spiel.getFelder()[i][j].holeBesitzerUuid().equals(spieler.getUuidstring())) {
                                continue gewonnenCheck1;
                            }
                        }
                    }
                } else {
                    continue;
                }
                zuLoeschen.add(spieler);
            }
            spiel.getSpieler().removeAll(zuLoeschen);
            if (spiel.getSpieler().size() == 1) {
                if (web != null) {
                    refreshWeb.stop();
                }
                JOptionPane.showMessageDialog(null, "" + spiel.getSpieler().get(0).getName() + " hat gewonnen!");
                ChnRX.restart();
            }
        }
    }

    /**
     * Berechnet die Explosionen für bestimmte Felder.
     *
     * @param punkte Eine ArrayListe mit Koordinaten für zu berechnende Felder
     */
    private void berechneExplosionen(ArrayList<Point> punkte) {
        for (Point punkt : punkte) {
            int x = punkt.x;
            int y = punkt.y;
            spiel.getFelder()[x][y].setzeAnzahl(0); // Setzt die Anzahl zurück
            spiel.getFelder()[x][y].setzeFarbe(Color.BLACK); // Setzt die Farbe zurück
            spiel.getFelder()[x][y].setzeBesitzerUuid(null); // Setzt den Besitzer des Feldes zurück

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

    // TODO HIER IST EIN BUG
    private void naechsterSpieler() {
        if (aktuellerIndex >= (spiel.getSpieler().size() - 1)) {
            aktuellerIndex = 0; // Falls am Ende der Liste angekommen setze am Anfang fort
        } else {
            aktuellerIndex++;
        } // end of if-else
        spiel.setAktuellerSpieler(spiel.getSpieler().get(aktuellerIndex));
    }
}

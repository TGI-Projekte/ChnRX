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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

/**
 * SpielGUI.java Zweck: Das JFrame in dem das eigentliche Spiel abläuft.
 */
public class SpielGUI extends JFrame {

    private JLabel l3 = new JLabel();
    private JLabel[][] labelarray;
    private Dimension feldgroesse;
    private Steuerung steu;

    // TODO: Initialisierung außerhalb des Konstruktors
    public SpielGUI(Steuerung steuobjekt, Dimension groesse) {
        // Frame-Initialisierung
        super();
        steu = steuobjekt;
        feldgroesse = groesse;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        int frameWidth = 80 * (int) feldgroesse.getWidth();
        int frameHeight = 80 * (int) feldgroesse.getHeight();
        setSize(frameWidth, frameHeight);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - getSize().width) / 2;
        int y = (d.height - getSize().height) / 2;
        setLocation(x, y);
        setTitle("ChnRX " + ChnRX.version);
        setResizable(false);

        Container cp = getContentPane();
        cp.setLayout(null);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                frame_KeyTyped(e);
            }
        });

        labelarray = new JLabel[(int) feldgroesse.getWidth()][(int) feldgroesse.getHeight()];

        Border border = BorderFactory.createLineBorder(Color.black);

        // Initialisierung der visuellen Repräsentierung des Spielfelds
        for (int i = 0; i < feldgroesse.getHeight(); i++) {
            for (int j = 0; j < feldgroesse.getWidth(); j++) {
                ClickListener listener = new ClickListener(j, i);
                labelarray[j][i] = new JLabel();
                labelarray[j][i].setSize(80, 80);
                labelarray[j][i].setLocation(j * 80, i * 80);
                labelarray[j][i].setBorder(border);
                labelarray[j][i].setHorizontalAlignment(SwingConstants.CENTER);
                labelarray[j][i].setVerticalAlignment(SwingConstants.CENTER);
                labelarray[j][i].setFont(labelarray[j][i].getFont().deriveFont(18.0f));
                labelarray[j][i].addMouseListener(listener);
                cp.add(labelarray[j][i]);
            }
        } // end of for

        // Setze das Fenster mittig
        setLocationRelativeTo(null);

        setVisible(true);
        cp.setPreferredSize(new Dimension(frameWidth, frameHeight));

        pack();
    }

    /**
     * Ändert alle Kanten der Labels zu einer Farbe.
     *
     * @param c Die Farbe zu der diese geändert werden sollen.
     */
    public void changeBorderColor(Color c) {
        Border border = BorderFactory.createLineBorder(c);

        for (int i = 0; i < feldgroesse.getHeight(); i++) {
            for (int j = 0; j < feldgroesse.getWidth(); j++) {
                labelarray[j][i].setBorder(border);
            }
        } // end of for
    }

    private void labelClicked(MouseEvent e, int x, int y) {
        // System.out.println("" + x + ", " + y);
        steu.feldGedrueckt(x, y);
    }

    /**
     * Ein Mouselistener für die einzelnen JLabels
     */
    private class ClickListener extends MouseAdapter {

        public final int x;
        public final int y;

        public ClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            labelClicked(e, x, y);
        }
    }

    public void setzeFeldTextFarbe(Color farbe, int x, int y) {
        labelarray[x][y].setForeground(farbe);
    }

    public void setzeFeldText(String text, int x, int y) {
        labelarray[x][y].setText(text);
    }

    public void setzeFeldBild(ImageIcon bild, int x, int y) {
        labelarray[x][y].setText("");
        labelarray[x][y].setIcon(bild);
    }

    private void frame_KeyTyped(KeyEvent evt) {

    }
} // end of class SpielGUI

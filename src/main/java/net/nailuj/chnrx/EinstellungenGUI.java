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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * 
 */
class EinstellungenGUI extends JFrame {
// Anfang Attribute

    private JRadioButton rbGross = new JRadioButton();
    private JRadioButton rbMittel = new JRadioButton();
    private JRadioButton rbKlein = new JRadioButton();
    private JLabel lblGroesse = new JLabel();
    private ButtonGroup bgGroesse = new ButtonGroup();
    private JLabel lSpieler = new JLabel();
    private JList listSpieler = new JList();
    private DefaultListModel listSpielerModel = new DefaultListModel();
    private JScrollPane listSpielerScrollPane = new JScrollPane(listSpieler);
    private JButton btnNeu = new JButton();
    private JButton btnAndern = new JButton();
    private JButton btnStart = new JButton();
    private JCheckBox cbOnline = new JCheckBox();
    private JRadioButton rbHost = new JRadioButton();
    private JRadioButton rbClient = new JRadioButton();
    private ButtonGroup bgHostClient = new ButtonGroup();
    // Ende Attribute
    private Steuerung steu;

    public EinstellungenGUI(Steuerung steuerung) {
        // Frame-Initialisierung
        super();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        int frameWidth = 230;
        int frameHeight = 358;
        setSize(frameWidth, frameHeight);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - getSize().width) / 2;
        int y = (d.height - getSize().height) / 2;
        setLocation(x, y);
        setTitle("Einstellungen");

        setResizable(false);
        Container cp = getContentPane();
        cp.setLayout(null);
        // Anfang Komponenten

        rbGross.setBounds(104, 8, 100, 20);
        rbGross.setOpaque(false);
        rbGross.setText("Groß");
        bgGroesse.add(rbGross);
        cp.add(rbGross);
        rbMittel.setBounds(104, 32, 100, 20);
        rbMittel.setOpaque(false);
        rbMittel.setText("Mittel");
        bgGroesse.add(rbMittel);
        rbMittel.setSelected(true);
        cp.add(rbMittel);
        rbKlein.setBounds(104, 56, 100, 20);
        rbKlein.setOpaque(false);
        rbKlein.setText("Klein");
        bgGroesse.add(rbKlein);
        cp.add(rbKlein);
        lblGroesse.setBounds(8, 8, 86, 20);
        lblGroesse.setText("Spielfeldgröße:");
        lblGroesse.setHorizontalAlignment(SwingConstants.RIGHT);
        cp.add(lblGroesse);
        lSpieler.setBounds(8, 88, 83, 25);
        lSpieler.setText("Spieler:");
        lSpieler.setHorizontalTextPosition(SwingConstants.CENTER);
        lSpieler.setHorizontalAlignment(SwingConstants.RIGHT);
        cp.add(lSpieler);
        listSpieler.setModel(listSpielerModel);
        listSpielerScrollPane.setBounds(96, 88, 121, 97);
        listSpieler.addListSelectionListener((ListSelectionEvent evt) -> {
            listSpieler_ValueChanged(evt);
        });
        listSpieler.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cp.add(listSpielerScrollPane);
        btnNeu.setBounds(96, 192, 57, 25);
        btnNeu.setText("Neu");
        btnNeu.setMargin(new Insets(2, 2, 2, 2));
        btnNeu.addActionListener((ActionEvent evt) -> {
            bNeu_ActionPerformed(evt);
        });
        cp.add(btnNeu);
        btnAndern.setBounds(160, 192, 57, 25);
        btnAndern.setText("Ändern");
        btnAndern.setMargin(new Insets(2, 2, 2, 2));
        btnAndern.addActionListener((ActionEvent evt) -> {
            btnAndern_ActionPerformed(evt);
        });
        cp.add(btnAndern);
        btnStart.setBounds(8, 272, 209, 41);
        btnStart.setText("Start");
        btnStart.setMargin(new Insets(2, 2, 2, 2));
        btnStart.addActionListener((ActionEvent evt) -> {
            btnStart_ActionPerformed(evt);
        });
        cp.add(btnStart);
        cbOnline.setBounds(8, 224, 60, 20);
        cbOnline.setOpaque(false);
        cbOnline.setText("Online");
        cbOnline.addActionListener((ActionEvent e) -> {
            cbOnline_ActionPerformed(e);
        });
        cp.add(cbOnline);
        rbHost.setBounds(96, 224, 100, 20);
        rbHost.setOpaque(false);
        rbHost.setText("Host");
        rbHost.setEnabled(false);
        bgHostClient.add(rbHost);
        cp.add(rbHost);
        rbClient.setBounds(96, 248, 100, 20);
        rbClient.setOpaque(false);
        rbClient.setText("Client");
        rbClient.setEnabled(false);
        bgHostClient.add(rbClient);
        cp.add(rbClient);
        // Ende Komponenten
        steu = steuerung;
        setVisible(true);
        btnNeu.requestFocus();
    } // end of public EinstellungenGUI

    // Anfang Methoden
    public String bgGroesse_getSelectedRadioButtonLabel() {
        for (java.util.Enumeration<AbstractButton> e = bgGroesse.getElements(); e.hasMoreElements();) {
            AbstractButton b = e.nextElement();
            if (b.isSelected()) {
                return b.getText();
            }
        }
        return "";
    }

    public void bNeu_ActionPerformed(ActionEvent evt) {
        new SpielerEditorGUI(steu, false);
    } // end of bNeu_ActionPerformed

    public void btnAndern_ActionPerformed(ActionEvent evt) {
        Spieler ausgewspieler = steu.holeSpieler(listSpieler.getSelectedIndex());
        if (ausgewspieler == null) {
            return;
        } // end of if
        new SpielerEditorGUI(steu, true, ausgewspieler);
    } // end of btnAndern_ActionPerformed

    public void btnStart_ActionPerformed(ActionEvent evt) {
        if (steu.holeSpieler(0) != null) {
            if (rbClient.isSelected()) {
                if (steu.holeSpieler(1) != null) {
                    System.out.println("Bitte lege als Client maximal einen Spieler an!");
                    return;
                }
            }
            if (rbHost.isSelected()) {
                if (steu.holeSpieler(1) == null) {
                    System.out.println("Bitte lege als Server mehr als einen Spieler an!");
                    return;
                }
            }
            this.setVisible(false);
            this.dispose();
            steu.startClicked();
        } else {
            System.out.println("Lege bitte erst Spieler an..");
        } // end of if

    } // end of btnStart_ActionPerformed

    public void listSpieler_ValueChanged(ListSelectionEvent evt) {
    } // end of listSpieler_ValueChanged

    public DefaultListModel getListSpielerModel() {
        return listSpielerModel;
    }

    public void setListSpielerModel(DefaultListModel listSpielerModelNeu) {
        listSpielerModel = listSpielerModelNeu;
    }

    public Dimension getSelectedGroesse() {
        Dimension groesse = new Dimension();
        if (rbKlein.isSelected()) {
            groesse.setSize(7, 5);
        } else if (rbMittel.isSelected()) {
            groesse.setSize(9, 6);
        } else {
            groesse.setSize(15, 10);
        } // end of if
        return groesse;
    }

    private void cbOnline_ActionPerformed(ActionEvent e) {
        if (cbOnline.isSelected()) {
            rbHost.setEnabled(true);
            rbClient.setEnabled(true);
            rbHost.setSelected(true);
        } else {
            rbHost.setEnabled(false);
            rbClient.setEnabled(false);
            bgHostClient.clearSelection();
        }
    }

    public boolean isOnline() {
        return cbOnline.isSelected();
    }

    public boolean isHost() {
        return rbHost.isSelected();
    }
}

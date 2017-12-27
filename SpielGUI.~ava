import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;


/**
  *
  * Beschreibung
  *
  * @version 1.0 vom 05.10.2017
  * @author
  */
public class SpielGUI extends JFrame {
  // Anfang Attribute
  private JLabel l3 = new JLabel();
  private JLabel[][] labelarray;
  private Dimension feldgroesse;
  private Steuerung steu;

  // Ende Attribute
  public SpielGUI(Steuerung steuobjekt, Dimension groesse) {
    // Frame-Initialisierung
    super();
    steu = steuobjekt;
    feldgroesse = groesse;
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    
    int frameWidth = 80 * (int) feldgroesse.getWidth();
    int frameHeight = 80 * (int) feldgroesse.getHeight();
    setSize(frameWidth, frameHeight);
    
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (d.width - getSize().width) / 2;
    int y = (d.height - getSize().height) / 2;
    setLocation(x, y);
    setTitle("ChnRX "+ChnRX.version);
    setResizable(false);
    
    Container cp = getContentPane();
    cp.setLayout(null);
    // Anfang Komponenten
    // Ende Komponenten
    addKeyListener(new KeyAdapter() {
    @Override
    public void keyTyped(KeyEvent e) {
    frame_KeyTyped(e);
    }
    });
    
    labelarray = new JLabel[(int) feldgroesse.getWidth()][(int) feldgroesse.getHeight()];
    
    Border border = BorderFactory.createLineBorder(Color.black);
    
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
    setLocationRelativeTo(null);
    setVisible(true);
    cp.setPreferredSize(new Dimension(frameWidth, frameHeight));
    pack();
  }

  // Anfang Methoden
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

  // Ende Methoden
  private class ClickListener extends MouseAdapter {
    public final int x;
    public final int y;

    public ClickListener(int x, int y) {
      this.x = x;
      this.y = y;
    }

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

  private void frame_KeyTyped(KeyEvent evt) {
    //TODO
  }
} // end of class SpielGUI

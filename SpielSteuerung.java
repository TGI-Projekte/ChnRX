import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.lang.Thread;
import java.lang.Runnable;

public class SpielSteuerung{
  // Anfang Attribute
  private ArrayList<Spieler> spieler;
  private Steuerung steu;
  private Feld[][] felder;
  private Spieler aktuellerSpieler;
  private int aktuellerIndex = 0;
  private boolean akzeptiereKlicks = true;
  // Ende Attribute

  public SpielSteuerung(Steuerung steuobj, ArrayList<Spieler> spielerliste,
  Dimension groesse) {
    steu = steuobj;
    spieler = spielerliste;
    felder = new Feld[(int) groesse.getWidth()][(int) groesse.getHeight()];
    for (int i = 0; i < felder.length; i++) {
      for (int j = 0; j < felder[0].length; j++) {
        felder[i][j] = new Feld();
      } // end of for
    } // end of for
    
    aktuellerSpieler = spieler.get(0);
    steu.setzeRandFarbe(aktuellerSpieler.getFarbe());
    steu.aktualisiereSpielfeld(felder);
  }
  // Anfang Methoden

  public void feldGedrueckt(int x, int y) {
    if (!akzeptiereKlicks) {
      return;
    } // end of if
    if (felder[x][y].holeBesitzerUuid() != null) {
      if (felder[x][y].holeBesitzerUuid() != aktuellerSpieler.getUuidstring()) {
        return;
      } // end of if
    }
    
    felder[x][y].setzeFarbe(aktuellerSpieler.getFarbe());
    felder[x][y].setzeBesitzerUuid(aktuellerSpieler.getUuidstring());
    felder[x][y].setzeAnzahl(felder[x][y].holeAnzahl() + 1);
    steu.aktualisiereSpielfeld(felder);
    Thread t = new Thread(new Runnable() {
    @Override
    public void run() {
    akzeptiereKlicks = false;
    try {
    berechneSpiel();
    } catch(Exception e) {
    } // end of try
    akzeptiereKlicks = true;
    }
    });
    t.start();
    if (aktuellerIndex == (spieler.size() - 1)) {
      aktuellerIndex = 0;
    } else {
      aktuellerIndex++;
    } // end of if-else
    
    aktuellerSpieler = spieler.get(aktuellerIndex);
    steu.setzeRandFarbe(aktuellerSpieler.getFarbe());
  }
  
  private void berechneSpiel(){
    ArrayList<Point> punkte = new ArrayList<>();
    for (int i = 0; i < felder.length; i++) {
      for (int j = 0; j < felder[0].length; j++) {
        if (felder[i][j].holeAnzahl() >= 4) {
          punkte.add(new Point(i,j));
        } // end of if
      } // end of for
    } // end of for
    if (!punkte.isEmpty()) {
      berechneExplosionen(punkte);
      try{
        Thread.sleep(500);
      } catch (Exception e) {
        
      }
      steu.aktualisiereSpielfeld(felder);
      berechneSpiel();    
    } // end of if
  }
  
  private void berechneExplosionen(ArrayList<Point> punkte){
    for (Point punkt : punkte) {
      int x = punkt.x;
      int y = punkt.y;
      felder[x][y].setzeAnzahl(0);
      felder[x][y].setzeFarbe(Color.BLACK);
      felder[x][y].setzeBesitzerUuid(null);
      if (x!=0) {
        felder[x-1][y].setzeAnzahl(felder[x-1][y].holeAnzahl()+1);
        felder[x-1][y].setzeFarbe(aktuellerSpieler.getFarbe());
        felder[x-1][y].setzeBesitzerUuid(aktuellerSpieler.getUuidstring());
      } // end of if
      if (x!=felder.length-1) {
        felder[x+1][y].setzeAnzahl(felder[x+1][y].holeAnzahl()+1);
        felder[x+1][y].setzeFarbe(aktuellerSpieler.getFarbe());
        felder[x+1][y].setzeBesitzerUuid(aktuellerSpieler.getUuidstring());
      } // end of if
      if (y!=0) {
        felder[x][y-1].setzeAnzahl(felder[x][y-1].holeAnzahl()+1);
        felder[x][y-1].setzeFarbe(aktuellerSpieler.getFarbe());
        felder[x][y-1].setzeBesitzerUuid(aktuellerSpieler.getUuidstring());
      } // end of if
      if (y!=felder[0].length-1) {
        felder[x][y+1].setzeAnzahl(felder[x][y+1].holeAnzahl()+1);
        felder[x][y+1].setzeFarbe(aktuellerSpieler.getFarbe());
        felder[x][y+1].setzeBesitzerUuid(aktuellerSpieler.getUuidstring());
      } // end of if
    } // end of for
  }
  // Ende Methoden
    
    
  public class Feld {
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
}

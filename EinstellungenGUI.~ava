import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
  *
  * Beschreibung
  *
  * @version 1.0 vom 05.10.2017
  * @author 
  */

public class EinstellungenGUI extends JFrame {
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
  private JButton bNeu = new JButton();
  private JButton btnAndern = new JButton();
  private JButton btnStart = new JButton();
  // Ende Attribute
  private Steuerung steu;
  
  public EinstellungenGUI(Steuerung steuerung) { 
    // Frame-Initialisierung
    super();
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    int frameWidth = 230; 
    int frameHeight = 307;
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
    listSpieler.addListSelectionListener(new ListSelectionListener() { 
    public void valueChanged(ListSelectionEvent evt) { 
    listSpieler_ValueChanged(evt);
    }
    });
    listSpieler.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    cp.add(listSpielerScrollPane);
    bNeu.setBounds(96, 192, 57, 25);
    bNeu.setText("Neu");
    bNeu.setMargin(new Insets(2, 2, 2, 2));
    bNeu.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent evt) { 
    bNeu_ActionPerformed(evt);
    }
    });
    cp.add(bNeu);
    btnAndern.setBounds(160, 192, 57, 25);
    btnAndern.setText("Ändern");
    btnAndern.setMargin(new Insets(2, 2, 2, 2));
    btnAndern.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent evt) { 
    btnAndern_ActionPerformed(evt);
    }
    });
    cp.add(btnAndern);
    btnStart.setBounds(8, 224, 209, 41);
    btnStart.setText("Start");
    btnStart.setMargin(new Insets(2, 2, 2, 2));
    btnStart.addActionListener(new ActionListener() { 
    public void actionPerformed(ActionEvent evt) { 
    btnStart_ActionPerformed(evt);
    }
    });
    cp.add(btnStart);
    // Ende Komponenten
    steu = steuerung;
    setVisible(true);
    bNeu.requestFocus();
  } // end of public EinstellungenGUI
  
  // Anfang Methoden
  
  public String bgGroesse_getSelectedRadioButtonLabel() {
    for (java.util.Enumeration<AbstractButton> e = bgGroesse.getElements(); e.hasMoreElements();) {
      AbstractButton b = e.nextElement();
      if (b.isSelected()) return b.getText();
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
    new SpielerEditorGUI(steu, true).setzeSpieler(ausgewspieler);
  } // end of btnAndern_ActionPerformed

  public void btnStart_ActionPerformed(ActionEvent evt) {
    if (steu.holeSpieler(0)!=null) {
      this.setVisible(false);
      this.dispose();
      steu.startClicked();
    } else {
      System.out.println("Lege bitte erst Spieler an..");
    } // end of if
    
  } // end of btnStart_ActionPerformed

  public void listSpieler_ValueChanged(ListSelectionEvent evt) {
    // TODO hier Quelltext einfügen
    
  } // end of listSpieler_ValueChanged

  public DefaultListModel getListSpielerModel() {
    return listSpielerModel;
  }

  public void setListSpielerModel(DefaultListModel listSpielerModelNeu) {
    listSpielerModel = listSpielerModelNeu;
  }
  
  public Dimension getSelectedGroesse(){
    Dimension groesse = new Dimension();
    if (rbKlein.isSelected()) {
      groesse.setSize(7,5);
    } else if (rbMittel.isSelected()){
      groesse.setSize(9,6);
    } else {
      groesse.setSize(15,10);
    } // end of if
    return groesse;
  }


  // Ende Methoden
} // end of class EinstellungenGUI


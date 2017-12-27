import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.event.*;

/**
  *
  * Beschreibung
  *
  * @version 1.0 vom 12.10.2017
  * @author 
  */

public class EinstellungenGUIFX extends Application {
  // Anfang Attribute
  private Button button1 = new Button();
  // Ende Attribute
  
  public void start(Stage primaryStage) { 
    Pane root = new Pane();
    Scene scene = new Scene(root, 149, 1);
    // Anfang Komponenten
    
    button1.setLayoutX(0);
    button1.setLayoutY(0);
    button1.setPrefHeight(225);
    button1.setPrefWidth(267);
    root.getChildren().add(button1);
    button1.setOnAction(
      (event) -> {button1_Action(event);} 
    );
    // Ende Komponenten
    
    primaryStage.setOnCloseRequest(e -> System.exit(0));
    primaryStage.setTitle("EinstellungenGUIFX");
    primaryStage.setScene(scene);
    primaryStage.show();
  } // end of public EinstellungenGUIFX
  
  // Anfang Methoden
  
  public static void main(String[] args) {
    launch(args);
  } // end of main
  
  public void button1_Action(Event evt) {
    // TODO hier Quelltext einfügen

  } // end of button1_Action

  // Ende Methoden
} // end of class EinstellungenGUIFX


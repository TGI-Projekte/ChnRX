public class ChnRX{
  private static Steuerung steu;
  public static final String version = "v0.5.0";
  public ChnRX(){
    steu = new Steuerung();
  }
  
  public static void restart(){
    System.out.println("Spiel wird neugestartet..");
    steu = null;
    System.gc();
    steu = new Steuerung();
  }


  public static void main(String[] args){
    new ChnRX();
  } 
}

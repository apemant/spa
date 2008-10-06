package hr.restart.robno;

public class presDOSIzd extends jpPreselectDoc {
  static presDOSIzd presdosizd;

  public void defaultMatDocAllowed(){
    isMatDocAllowed = true;
  }

  public void defaultMatDocAllowedifObrac(){
    isMatDocifObracAllowed = true;
  }
  public presDOSIzd() {
    super('D', 'N');
    presdosizd=this;
  }
  public static jpPreselectDoc getPres() {
    if (presdosizd==null) {
      presdosizd=new presDOSIzd();
    }
    return presdosizd;
  }
}

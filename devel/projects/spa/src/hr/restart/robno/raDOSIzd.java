package hr.restart.robno;

import hr.restart.robno.raDOS.DosColorModifier;

public class raDOSIzd extends raDOS {
  public void initialiser(){
    what_kind_of_dokument = "DOS";
  }
  public boolean isDosIzd() {
    return true;
  }
  public void RestPanelSetup() {
    DP.addRestOnlyKol();
  }
  public boolean LocalValidacijaMaster(){
    if (hr.restart.util.Valid.getValid().isEmpty(MP.panelBasic.jrfCORG)) return false;
    if (hr.restart.sisfun.frmParam.getParam("robno","useVRtros",
            "D","Provjera vrste troška obavezna").equalsIgnoreCase("D")){
System.out.println("asdad");      
       if (hr.restart.util.Valid.getValid().isEmpty(MP.panelBasic.jrfCVRTR)) return false;
    }
//    if (!MP.panelBasic.jpRN.Validacija()) {
//      return false;
//    }
//    
//    if ("D".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno","useRadniNalozi",
//                 "D","D ako se koriste radni nalozi inaèe slobodan upis"))){
//      return validRealRN();
//    }
    
    return true;  
  }
  public raDOSIzd() {
    setPreSel((jpPreselectDoc) presDOSIzd.getPres());
    master_titel = "Izdatnice (DOS)";
    detail_titel_mno = "Stavke izdatnice";
    detail_titel_jed = "Stavka izdatnice";
    zamraciMaster(dm.getZagDos());
    zamraciDetail(dm.getStOtp());
    setMasterSet(dm.getZagDos());
    setDetailSet(dm.getStOtp());
    MP.BindComp();
    DP.BindComp();
    DP.rpcart.setKolSkladScreen(true);
    this.raMaster.getJpTableView().addTableModifier(new DosColorModifier());
    ConfigViewOnTable();
  }
}

package hr.restart.util.reports;

import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;


public class raPonIzlazDetail extends raIzlazDetail {
  
  raReportElement pic;

  public raPonIzlazDetail(raReportTemplate owner) {
    super(owner);
    
    String dim = frmParam.getParam("robno", "dimSlike", "", "Dimenzije slika na ponudama u pikselima (H:W)");
    if (dim == null || dim.length() == 0) return;
    
    String[] parts = new VarStr(dim).splitTrimmed(':');
    if (parts.length != 2) return;
    if (!Aus.isNumber(parts[0]) || !Aus.isNumber(parts[1])) return;
    
    System.out.println("Definining pic: " + dim);
    
    pic = addModel(ep.IMAGE, null).defaultAlterer();  

    pic.setLeft(1300);
    pic.setTop(240);
    pic.setWidth(Aus.getNumber(parts[0]) * 20);
    pic.setHeight(Aus.getNumber(parts[1]) * 20);
    pic.setPicture("$art");
    pic.setAlignment(ev.CENTER);
    pic.setSizeMode(ev.CLIP);
    
    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }
  
  private void modifyThis() {
    if (pic != null) pic.setLeft(TextNAZART.getLeft());
  }
}

package hr.restart.robno;

import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;


public class repMeiLOTTemplate extends repMeiOrigTemplate {

  public raReportSection createSectionHeader1() {
    raReportSection sh = super.createSectionHeader1();
    
    LabelNaziv.defaultAlterer().setWidth(6120 - 720);
    LabelJmj.defaultAlterer().setLeft(LabelNaziv.defaultAlterer().getLeft() + LabelNaziv.defaultAlterer().getWidth() + 20);
    raReportElement lot = sh.copyToModify(LabelNaziv);
    lot.setCaption("Šarža / Lot");
    lot.setWidth(1060);
    lot.setLeft(LabelJmj.defaultAlterer().getLeft() + LabelJmj.defaultAlterer().getWidth() + 20);
    LabelKolicina.defaultAlterer().setWidth(1860 - 360);
    LabelKolicina.defaultAlterer().setLeft(lot.getLeft() + lot.getWidth() + 20);
    
    return sh;
  }
  
  public raReportSection createDetail() {
    raReportSection sh = super.createDetail();
    
    TextNAZART.defaultAlterer().setWidth(6120 - 720);
    TextJM.defaultAlterer().setLeft(TextNAZART.defaultAlterer().getLeft() + TextNAZART.defaultAlterer().getWidth() + 20);
    raReportElement lot = sh.copyToModify(TextNAZART);
    lot.setControlSource("LOT");
    lot.setWidth(1060);
    lot.setLeft(TextJM.defaultAlterer().getLeft() + TextJM.defaultAlterer().getWidth() + 20);
    TextKOL.defaultAlterer().setWidth(1860 - 360);
    TextKOL.defaultAlterer().setLeft(lot.getLeft() + lot.getWidth() + 20);
    
    return sh;
  }
  
  public repMeiLOTTemplate() {
    // 
  }
}

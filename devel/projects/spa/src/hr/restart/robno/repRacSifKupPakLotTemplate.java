package hr.restart.robno;

import hr.restart.util.reports.raDetailRacSif;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raSHRacSif;


public class repRacSifKupPakLotTemplate extends repRacTemplate {

  public raReportSection createDetail() {
    raDetailRacSif sh = new raDetailRacSif(this);
    raReportElement lot = sh.copyToModify(sh.TextSSIF);
    lot.setLeft(sh.TextNAZART.defaultAlterer().getLeft());
    lot.setControlSource("LOT");
    
    sh.TextRBR.defaultAlterer().alterWidth(-40);
    sh.TextCART.defaultAlterer().alterWidth(-40);
    sh.TextCART.defaultAlterer().moveHor(-40);
    sh.TextSSIF.defaultAlterer().moveHor(-80);
    sh.TextSSIF.defaultAlterer().alterWidth(40);
    lot.moveHor(-40);
    
    sh.TextINETO.defaultAlterer().alterWidth(-60);
    sh.TextINETO.defaultAlterer().moveHor(60);
    sh.TextPor1p2p3Naz.defaultAlterer().alterWidth(-20);
    sh.TextPor1p2p3Naz.defaultAlterer().moveHor(80);
    sh.TextUPRAB1.defaultAlterer().alterWidth(-20);
    sh.TextUPRAB1.defaultAlterer().moveHor(100);
    sh.TextFC.defaultAlterer().alterWidth(-40);
    sh.TextFC.defaultAlterer().moveHor(140);
    sh.TextJM.defaultAlterer().moveHor(140);
    sh.TextKOL.defaultAlterer().alterWidth(-40);
    sh.TextKOL.defaultAlterer().moveHor(180);
    sh.TextNAZART.defaultAlterer().alterWidth(-680);
    sh.TextNAZART.defaultAlterer().moveHor(860);
    
    return sh;
  }
  
  public raReportSection createSectionHeader1() {
    raSHRacSif sh = new raSHRacSif(this);
    raReportElement lot = sh.copyToModify(sh.LabelSifra_kupca);
    lot.setLeft(sh.LabelNaziv.defaultAlterer().getLeft());
    lot.setCaption("Šarža / Lot");
    sh.LabelRbr.defaultAlterer().alterWidth(-40);
    sh.LabelSifra.defaultAlterer().moveHor(-40);
    sh.LabelSifra.defaultAlterer().alterWidth(-40);
    sh.LabelSifra_kupca.defaultAlterer().moveHor(-80);
    sh.LabelSifra_kupca.defaultAlterer().alterWidth(40);
    lot.moveHor(-40);
    
    sh.LabelIznos.defaultAlterer().alterWidth(-60);
    sh.LabelIznos.defaultAlterer().moveHor(60);
    sh.Label1.defaultAlterer().alterWidth(-40);
    sh.Label1.defaultAlterer().moveHor(100);
    sh.LabelPop.defaultAlterer().alterWidth(-20);
    sh.LabelPop.defaultAlterer().moveHor(100);
    sh.LabelPor.defaultAlterer().alterWidth(-20);
    sh.LabelPor.defaultAlterer().moveHor(80);
    sh.LabelCijena.defaultAlterer().alterWidth(-40);
    sh.LabelCijena.defaultAlterer().moveHor(140);
    sh.LabelJmj.defaultAlterer().moveHor(140);
    sh.LabelKolicina.defaultAlterer().alterWidth(-40);
    sh.LabelKolicina.defaultAlterer().moveHor(180);
    sh.LabelNaziv.defaultAlterer().alterWidth(-680);
    sh.LabelNaziv.defaultAlterer().moveHor(860);
    
    return sh;
  }
  
  
  public repRacSifKupPakLotTemplate() {
    // TODO Auto-generated constructor stub
  }
}
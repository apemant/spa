package hr.restart.robno;

import hr.restart.util.reports.raIzlazDetail;
import hr.restart.util.reports.raIzlazSectionHeaderLines;
import hr.restart.util.reports.raReportSection;


public class repPonudaPopTemplate extends repPonudaTemplate {

  public raReportSection createSectionHeader1() {
    raIzlazSectionHeaderLines sh = (raIzlazSectionHeaderLines) super.createSectionHeader1();
    sh.Label1.defaultAlterer().setCaption("Popust");
    sh.LabelPop.defaultAlterer().setCaption("%");
    sh.LabelPor.defaultAlterer().setCaption("Iznos");
    return sh;
  }
  
  public raReportSection createDetail() {
    raIzlazDetail sh = (raIzlazDetail) super.createDetail();
    sh.TextPor1p2p3Naz.defaultAlterer().setControlSource("UIRAB");
    return sh;
  }

  public repPonudaPopTemplate() {
    // TODO Auto-generated constructor stub
  }

}

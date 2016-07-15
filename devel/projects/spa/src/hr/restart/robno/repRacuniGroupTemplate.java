package hr.restart.robno;

import hr.restart.util.reports.raIzlazDetail;
import hr.restart.util.reports.raIzlazGroupSectionHeader;
import hr.restart.util.reports.raIzlazSectionFooterLines;
import hr.restart.util.reports.raIzlazSectionHeaderLines;
import hr.restart.util.reports.raNewDefaultPageHeader;
import hr.restart.util.reports.raRPSectionHeaderROT;
import hr.restart.util.reports.raReportSection;


public class repRacuniGroupTemplate extends repIzlazGroupOrigTemplate {

  public raReportSection createPageHeader() {
    return new raNewDefaultPageHeader(this); // return new raIzlazPageHeader(this);
//    return createCustomSection("ROT", "H", super.createPageHeader());
  }

  public raReportSection createSectionHeader0() {
    raRPSectionHeaderROT sh = new raRPSectionHeaderROT(this);
    return sh;
  }

  public raReportSection createSectionHeader1() {
    return new raIzlazSectionHeaderLines(this); // return new raIzlazSectionHeader(this);
  }

  public raReportSection createDetail() {
    return new raIzlazDetail(this);
  }

  public raReportSection createSectionFooter1() {
    return new raIzlazSectionFooterLines(this); // return new raIzlazSectionFooter(this);
  }

  public raReportSection createSectionHeader2() {
    return new raIzlazGroupSectionHeader(this); // return new raIzlazSectionHeader(this);
  }
  
  public repRacuniGroupTemplate() {
    // TODO Auto-generated constructor stub
  }

}

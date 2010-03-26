package hr.restart.os;

import hr.restart.util.reports.raOSDetail01;
import hr.restart.util.reports.raOSPageHeader001;
import hr.restart.util.reports.raOSPageHeader002;
import hr.restart.util.reports.raOSSectionFooter001;
import hr.restart.util.reports.raOSSectionFooter101;
import hr.restart.util.reports.raOSSectionHeader101;
import hr.restart.util.reports.raReportSection;

public class repIspOS_06Template extends repInvIspOSTemplate {

  public repIspOS_06Template() {
    System.out.println("\nrepIspOS_06Template()\n");
  }
  public raReportSection createPageHeader() {
    raReportSection s = new raOSPageHeader002(this);
    return s;
  }
  public raReportSection createSectionHeader1() {
    raReportSection s = super.createSectionHeader1();
    s.defaultAlterer().setVisible(false);
    return s;
  }
  public raReportSection createSectionFooter1() {
    raReportSection s = super.createSectionFooter1();
    s.defaultAlterer().setVisible(false);
    return s;
  }


}

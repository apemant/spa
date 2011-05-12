package hr.restart.robno;

import hr.restart.util.reports.raIzlazSectionFooterForCustom;
import hr.restart.util.reports.raReportSection;


public class repMesklaSpecTemplate extends repMesklaSpecOrigTemplate {

  public raReportSection createPageHeader() {
    return new raReportSection(template.getModel(ep.PAGE_HEADER), new String[] {"", "", "0"});
  }

  public raReportSection createSectionFooter0() {
    return new raIzlazSectionFooterForCustom(this);
  }

}

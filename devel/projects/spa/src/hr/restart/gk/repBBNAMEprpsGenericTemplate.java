package hr.restart.gk;

import hr.restart.sisfun.frmParam;
import hr.restart.util.reports.ReportModifier;
import hr.restart.util.reports.raElixirPropertyValues;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raStandardReportFooter;
import hr.restart.util.reports.raStandardReportHeader;

public class repBBNAMEprpsGenericTemplate extends repBBNAMEprpsWideExtendedOrigTemplate {

  public repBBNAMEprpsGenericTemplate() {
//    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepBBNAMEprpsGeneric");
    this.addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  public raReportSection createReportHeader() {
    return  new raStandardReportHeader(this);
  }

  public raReportSection createReportFooter() {
   return  new raStandardReportFooter(this);
  }

  public raReportSection createPageHeader() {
    return new raReportSection(template.getModel(ep.PAGE_HEADER), new String[] {"", "", "0"});
  }

  
  public void modifyThis() {
    boolean prije = frmParam.getFrmParam().getParam("gk","brBilRslj","P","Redosljed klasa P - prije E - poslje").equals("P");
    
    this.Section2.setProperty(ep.GROUP_HEADER, prije ? raElixirPropertyValues.YES : raElixirPropertyValues.NO);
    this.Section3.setProperty(ep.GROUP_HEADER, prije ? raElixirPropertyValues.YES : raElixirPropertyValues.NO);
    this.Section4.setProperty(ep.GROUP_HEADER, prije ? raElixirPropertyValues.YES : raElixirPropertyValues.NO);
    
    this.Section2.setProperty(ep.GROUP_FOOTER, prije ? raElixirPropertyValues.NO : raElixirPropertyValues.YES);
    this.Section3.setProperty(ep.GROUP_FOOTER, prije ? raElixirPropertyValues.NO : raElixirPropertyValues.YES);
    this.Section4.setProperty(ep.GROUP_FOOTER, prije ? raElixirPropertyValues.NO : raElixirPropertyValues.YES);

//    this.Section2.setProperty(ep.GROUP_HEADER, prije ? raElixirPropertyValues.YES : raElixirPropertyValues.NO);
//    this.Section3.setProperty(ep.GROUP_HEADER, /*prije ? raElixirPropertyValues.YES : */raElixirPropertyValues.NO);
//    this.Section4.setProperty(ep.GROUP_HEADER, /*prije ? raElixirPropertyValues.YES : */raElixirPropertyValues.NO);
//    
//    this.Section2.setProperty(ep.GROUP_FOOTER, prije ? raElixirPropertyValues.NO : raElixirPropertyValues.YES);
//    this.Section3.setProperty(ep.GROUP_FOOTER, /*prije ?*/ raElixirPropertyValues.NO /*: raElixirPropertyValues.YES*/);
//    this.Section4.setProperty(ep.GROUP_FOOTER, /*prije ? */raElixirPropertyValues.NO /*: raElixirPropertyValues.YES*/);  
  
  }

}

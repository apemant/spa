package hr.restart.robno;

import hr.restart.util.reports.raElixirProperties;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raReportTemplate;


public class repOdjavaTemplate extends repOTPvriTemplate {
  

  public raReportSection createSectionHeader0() {
    return new MySectionHeader(this);
  }
  
  public repOdjavaTemplate() {
    
  }

  private static class MySectionHeader extends raReportSection {
    private String[] thisProps = new String[] {"BRDOK", "Before", "", "", "Yes", "", "Yes", "", "1100"};
    
    public raReportElement TextTitle;
    private String[] TextTitleProps = new String[] {"Title", "", "", "", "", "", "Yes", "", "", "",
       "10720", "760", "", "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center", ""};
    public raReportElement TextSubTitle;
    private String[] TextSubTitleProps = new String[] {"SubTitle", "", "", "", "", "", "", "", "",
       "440", "10720", "600", "", "", "", "", "", "", "Lucida Bright", "11", "Bold", "", "", "Center",
       ""};
    
    public raReportElement LabelPartner;
    private String[] LabelPartnerProps = new String[] {"Partner", "", "", "1200", 
       "1200", "240", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
    public raReportElement TextNAZPARALL;
    private String[] TextNAZPARALLProps = new String[] {"NAZPARALL", "", "", "", "", "", "", "", "1300", 
       "1200", "6200", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
    
    public MySectionHeader(raReportTemplate owner) {
      super(owner.template.getModel(raElixirProperties.SECTION_HEADER + 0));
      this.setDefaults(thisProps);
      
      TextTitle = addModel(ep.TEXT, TextTitleProps);
      TextSubTitle = addModel(ep.TEXT, TextSubTitleProps);
      
      LabelPartner = addModel(ep.LABEL, LabelPartnerProps);
      TextNAZPARALL = addModel(ep.TEXT, TextNAZPARALLProps);
    }
  }
}

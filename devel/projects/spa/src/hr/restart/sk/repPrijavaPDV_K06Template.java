package hr.restart.sk;

import hr.restart.util.reports.raReportSection;

public class repPrijavaPDV_K06Template extends repPrijavaPDV_K06OrigTemplate {

  public raReportSection createReportHeader() {
    return new raReportSection(template.getModel(ep.REPORT_HEADER), 
        new String[] {"", "", "", "", "", "", "", "0"});
  }

  public raReportSection createPageHeader() {
    return new raReportSection(template.getModel(ep.PAGE_HEADER), new String[] {"", "", "0"});  
  }

  public raReportSection createDetail() {
    return new raReportSection(template.getModel(ep.DETAIL), 
        new String[] {"", "", "", "", "", "", "", "0"});
  }

  public raReportSection createReportFooter() {
    return new raReportSection(template.getModel(ep.REPORT_FOOTER),
        new String[] {"", "", "", "", "", "", "", "0"});
  }

  public raReportSection createPageFooter() {
    return new raReportSection(template.getModel(ep.PAGE_FOOTER),
        new String[] {"", "", "", "", "", "", "", "0"});
  }

}

//public class repPrijavaPDV_K06Template extends repPrijavaPDV_K06OrigTemplate {
//
//  @Override
//  public raReportSection createDetail() {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public raReportSection createPageFooter() {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public raReportSection createPageHeader() {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public raReportSection createReportFooter() {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//  @Override
//  public raReportSection createReportHeader() {
//    // TODO Auto-generated method stub
//    return null;
//  }
//
//}

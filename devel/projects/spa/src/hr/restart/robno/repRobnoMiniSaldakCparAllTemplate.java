/****license*****************************************************************
**   file: repRobnoMiniSaldakCparTemplate.java
**   Copyright 2006 Rest Art
**
**   Licensed under the Apache License, Version 2.0 (the "License");
**   you may not use this file except in compliance with the License.
**   You may obtain a copy of the License at
**
**       http://www.apache.org/licenses/LICENSE-2.0
**
**   Unless required by applicable law or agreed to in writing, software
**   distributed under the License is distributed on an "AS IS" BASIS,
**   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
**   See the License for the specific language governing permissions and
**   limitations under the License.
**
****************************************************************************/
package hr.restart.robno;

import sg.com.elixir.reportwriter.xml.ModelFactory;
import hr.restart.util.reports.*;

/*
 * Created on 2004.11.08
 */

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class repRobnoMiniSaldakCparAllTemplate extends raReportTemplate {
  
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "", 
     "JDOrepRobnoMiniSaldak", "", "", "10540", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "567.0", "560", "567.0", "", "", "11880", 
     "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"FirstLine", "", "Yes", "", "", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"Partner", "", "Yes", "Yes", "", "", ""};
  public raReportSection ReportHeader;
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  public raReportSection SectionHeader1;
  public raReportSection Detail;
  public raReportSection SectionFooter1;
  public raReportSection PageFooter;
  public raReportSection ReportFooter;
  
  
  public raReportSection createSections() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTIONS));

    Section0 = sect.getModel(ep.SECTION + 0, Section0Props);
    Section1 = sect.getModel(ep.SECTION + 1, Section1Props);
    return sect;
  }
  
  public void createReportStructure() {
    template = ModelFactory.getModel(ep.REPORT_TEMPLATE);
    ModelFactory.setCurrentReport(template);

    ReportTemplate = addSection(new raReportSection(template, ReportTemplateProps));

    PageSetup = addSection(new raReportSection(template.getModel(ep.PAGE_SETUP), PageSetupProps));
    Sections = addSection(createSections());

    ReportHeader = addSection(createReportHeader());
    PageHeader = addSection(createPageHeader());
    SectionHeader0 = addSection(createSectionHeader0());
    SectionHeader1 = addSection(createSectionHeader1());
    Detail = addSection(createDetail());
    SectionFooter1 = addSection(createSectionFooter1());
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
  

  public raReportSection createSectionHeader0() {
    raReportSection sh = new raReportSection(template.getModel(raElixirProperties.SECTION_HEADER + 0));
    sh.setDefaults(new String[] {"FirstLine", "", "", "", "Yes", "", "", "", "0"});
    
    sh.addModel(ep.TEXT, new String[] {"FirstLine", "", "", "", "", "", "", "", "60", 
        "", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""});
    sh.addModel(ep.TEXT, new String[] {"SecondLine", "", "", "", "", "", "", "", 
        "60", "240", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", 
        ""});
    sh.addModel(ep.TEXT, new String[] {"ThirdLine", "", "", "", "", "", "", "", "60", 
        "480", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""});
    sh.addModel(ep.TEXT, new String[] {"StatusDospjece", "", "", "", "", "", 
        "Yes", "", "", "900", "10740", "340", "", "", "", "", "", "", "Lucida Bright", "11", "", "", 
        "", "Center", ""});
    return sh;
  }
  public raReportSection createDetail() {
    return new RaRobnoMiniSaldakDetail(this);
  }
  public raReportSection createSectionFooter1() {
    
    raReportSection sh = new raReportSection(template.getModel(raElixirProperties.SECTION_FOOTER + 1));
    sh.setDefaults(new String[] {"Partner", "", "", "", "Yes", "", "", "540"});
    
    RaRobnoMiniSaldakSF0 orig = new RaRobnoMiniSaldakSF0(this);
    sh.addModel(ep.LINE, orig.Line1.getDefaults());
    sh.addModel(ep.LABEL, orig.Label1.getDefaults());
    sh.addModel(ep.LABEL, orig.Label2.getDefaults());
    sh.addModel(ep.LABEL, orig.Label3.getDefaults());
    sh.addModel(ep.LABEL, orig.Label4.getDefaults());
    sh.addModel(ep.LABEL, orig.LabelUKUPNO.getDefaults());
    sh.addModel(ep.TEXT, orig.Text1.getDefaults());
    sh.addModel(ep.TEXT, orig.Text2.getDefaults());
    sh.addModel(ep.TEXT, orig.Text3.getDefaults());
    sh.addModel(ep.LINE, orig.Line2.getDefaults());
 
    return sh;
  }
  public raReportSection createSectionHeader1() {
    RaRobnoMiniSaldakSH1 sh = new RaRobnoMiniSaldakSH1(this);
    sh.setDefault(raElixirProperties.FORCE_NEW, raElixirPropertyValues.NO);
    sh.removeModels(new raReportElement[] {sh.TextFirstLine, sh.TextSecondLine,
        sh.TextThirdLine, sh.TextNASLOV, sh.TextStatusDospjece, sh.LabelPRILOG_1});
    
    sh.defaultAltererSect().getView(sh.LabelPartner, sh.Line2).moveUp(2000);
    sh.setDefault(raElixirProperties.HEIGHT, "900");
    
    return sh;
  }
  /**
   * 
   */
  public repRobnoMiniSaldakCparAllTemplate() {
    System.out.println("TEMPLATE KLASA _ repRobnoMiniSaldakCparTemplate");
    
    createReportStructure();
    setReportProperties();
    
    this.addReportModifier(new ReportModifier() {
      public void modify() {
        System.out.println("ja bi sad nesto modificira....");
        modifyThis();
      }
    });
  }
  /* (non-Javadoc)
   * @see hr.restart.robno.repRobnoMiniSaldakCparOrigTemplate#createReportHeader()
   */
  public raReportSection createReportHeader() {    
    // {CAPTION, FORCE_NEW, NEW_ROWCOL, KEEP_TOGETHER, VISIBLE, GROW, SHRINK, HEIGHT}
    return  new raReportSection(template.getModel(ep.REPORT_HEADER), new String[] {"Report Header", "", "", 
        "","","","","0"});
  }

  /* (non-Javadoc)
   * @see hr.restart.robno.repRobnoMiniSaldakCparOrigTemplate#createPageHeader()
   */
  public raReportSection createPageHeader() {
    // TODO Auto-generated method stub
    return new raReportSection(template.getModel(ep.PAGE_HEADER), new String[] {"", "", "0"});
  }

  /* (non-Javadoc)
   * @see hr.restart.robno.repRobnoMiniSaldakCparOrigTemplate#createPageFooter()
   */
  public raReportSection createPageFooter() {
    // TODO Auto-generated method stub
    return new raReportSection(template.getModel(ep.PAGE_FOOTER), new String[] {"", "", "0"});
  }

  /* (non-Javadoc)
   * @see hr.restart.robno.repRobnoMiniSaldakCparOrigTemplate#createReportFooter()
   */
  public raReportSection createReportFooter() {
    // TODO Auto-generated method stub
    return  new raReportSection(template.getModel(ep.REPORT_FOOTER), new String[] {"Report Header", "", "", 
        "","","","","0"});
  }

  private void modifyThis() {
    /*
      if ("L".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno", "ispProzor"))) 
        sectHead0.restoreDefaults();
      else {
        raReportSection left = sectHead0.getView(LabelDuznik, Rectangle2);
        raReportSection rigt = sectHead0.getView(LabelVjerovnik, TextThirdLine);
        left.moveRightCm(9.5);
        rigt.moveLeftCm(9.5);
      }
    */
  }

}

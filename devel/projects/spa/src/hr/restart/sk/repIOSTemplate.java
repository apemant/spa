/****license*****************************************************************
**   file: repIOSTemplate.java
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
package hr.restart.sk;

import hr.restart.robno.repRobnoMiniSaldakCparOrigTemplate;
import hr.restart.util.reports.RaRobnoMiniSaldakSH0;
import hr.restart.util.reports.raIOS_OpomenaDetail;
import hr.restart.util.reports.raIOS_OpomenaSF0;
import hr.restart.util.reports.raIOS_OpomenaSH1;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;

/*
 * Created on 2004.11.11
 */

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class repIOSTemplate extends repRobnoMiniSaldakCparOrigTemplate {


  public repIOSTemplate() {
    System.out.println("TEMPLATE KLASA _ repIOSTemplate");
  }

  public raReportSection createSectionHeader0() {
    RaRobnoMiniSaldakSH0 sh0 = new RaRobnoMiniSaldakSH0(this);
    
    try {
      raReportElement TextMjestoDana;
      String[] TextMjestoDanaProperties = new String[] {"UMJESTUDANA", "", "", "", "", "", "", "", 
         "220", "8940", "20200", "1500", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", "", ""};
      TextMjestoDana = sh0.addModel(ep.TEXT, TextMjestoDanaProperties);
      
      raReportElement TextOsoba;
      /*{"Pošiljalac izvatka", "", "6880", "8940", "3480", "340", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", "Center"}*/
      String[] TextOsobaProperties = new String[] {"KONTAKTOSOBA", "", "", "", "", "", "", "", 
          "6880", "9620", "3480", "340", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", "Left", ""};
      TextOsoba = sh0.addModel(ep.TEXT, TextOsobaProperties);
      
      raReportElement TextBrojTela;
      /*{"Pošiljalac izvatka", "", "6880", "8940", "3480", "340", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", "Center"}*/
      String[] TextBrojTelaProperties = new String[] {"KONTAKTTEL", "", "", "", "", "", "", "", 
          "6880", "10220", "3480", "340", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", "Left", ""};
      TextBrojTela = sh0.addModel(ep.TEXT, TextBrojTelaProperties);
      
      raReportElement TextZiro;
      String[] TextZiroProperties = new String[] {"ZiroLine", "", "", "", "", "", "", "", 
          "5900", "2860", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", 
          ""};
      TextZiro = sh0.addModel(ep.TEXT, TextZiroProperties);
      
      raReportElement TextTel;
      String[] TextTelProperties = new String[] {"TelLine", "", "", "", "", "", "", "", 
          "5900", "3120", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", 
          ""};
      TextTel = sh0.addModel(ep.TEXT, TextTelProperties);
      
      raReportElement TextFax;
      String[] TextFaxProperties = new String[] {"FaxLine", "", "", "", "", "", "", "", 
          "5900", "3380", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", 
          ""};
      TextFax = sh0.addModel(ep.TEXT, TextFaxProperties);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return sh0;
  }

  public raReportSection createDetail() {
    return new raIOS_OpomenaDetail(this);
  }
  public raReportSection createSectionFooter0() {
    return new raIOS_OpomenaSF0(this);
  }
  public raReportSection createSectionHeader1() {
    return new raIOS_OpomenaSH1(this);
  }
  
  public raReportSection createReportHeader() {    
    // {CAPTION, FORCE_NEW, NEW_ROWCOL, KEEP_TOGETHER, VISIBLE, GROW, SHRINK, HEIGHT}
    return  new raReportSection(template.getModel(ep.REPORT_HEADER), new String[] {"Report Header", "", "", 
        "","","","","0"});
  }

  public raReportSection createPageHeader() {
    // TODO Auto-generated method stub
    return new raReportSection(template.getModel(ep.PAGE_HEADER), new String[] {"", "", "0"});
  }

  public raReportSection createPageFooter() {
    // TODO Auto-generated method stub
    return new raReportSection(template.getModel(ep.PAGE_FOOTER), new String[] {"", "", "0"});
  }
  
  public raReportSection createReportFooter() {
    // TODO Auto-generated method stub
    return  new raReportSection(template.getModel(ep.REPORT_FOOTER), new String[] {"Report Header", "", "", 
        "","","","","0"});
  }
}

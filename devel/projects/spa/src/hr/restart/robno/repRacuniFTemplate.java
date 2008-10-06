/****license*****************************************************************
**   file: repRacuniFTemplate.java
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

import hr.restart.util.reports.raIzlazDetail;
import hr.restart.util.reports.raIzlazSectionFooterLines;
import hr.restart.util.reports.raIzlazSectionHeaderLines;
import hr.restart.util.reports.raNewDefaultPageHeader;
import hr.restart.util.reports.raRPSectionHeaderROT;
import hr.restart.util.reports.raReportSection;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repRacuniFTemplate extends repIzlazOrigTemplate {

  public raReportSection createPageHeader() {
    return new raNewDefaultPageHeader(this); // return new raIzlazPageHeader(this);
//    return createCustomSection("ROT", "H", super.createPageHeader());
  }

  public raReportSection createSectionHeader0() {
    raRPSectionHeaderROT sh = new raRPSectionHeaderROT(this);
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nRAÈUN");
    sh.Text1.defaultAlterer().setControlSource("NAZPARL");
    sh.TextADR.defaultAlterer().setControlSource("ADRPAR");
    sh.TextMJ.defaultAlterer().setControlSource("MJPAR");
    sh.TextISPORUKA.setVisible(false);
//    if (!(hr.restart.sisfun.frmParam.getParam("robno","ispisPJ","D","Ispis poslovne jedinice na ROT-u (D-u adresi, I-kao isporuka, O-na oba mjesta, N-bez P.J.)").equalsIgnoreCase("I") ||
//        hr.restart.sisfun.frmParam.getParam("robno","ispisPJ").equalsIgnoreCase("O"))) {
      sh.LabelIsporuka.defaultAlterer().setCaption("");  //LabelIsporuka.setCaption("");
//    }
    return sh;

//    raRPSectionHeader sh = new raRPSectionHeader(this);
//    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nRAÈUN");
//    sh.TextNAZPAR.setDefault(ep.CONTROL_SOURCE, "=(string-append \"\n\" [NAZPARPJ])");
//    sh.TextADR.setDefault(ep.CONTROL_SOURCE,"ADRPJ");
//    sh.TextMJ.setDefault(ep.CONTROL_SOURCE, "MJPJ");
//    return sh;
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

  public repRacuniFTemplate() {
//    System.out.println("ysdfkjioansdfboandfnaoiscjao!!!!!!!!!");
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepRacuniF");
//    hr.restart.util.Aus.dumpModel(this.template.getModel("Page Header"), 0);
  }
}

//
//package hr.restart.robno;
//
//public class  {
//
//  public repRacuniFTemplate() {
//  }
//}

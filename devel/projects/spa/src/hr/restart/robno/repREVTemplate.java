/****license*****************************************************************
**   file: repREVTemplate.java
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

import hr.restart.util.reports.raElixirPropertyValues;
import hr.restart.util.reports.raKCIDetail;
import hr.restart.util.reports.raKCISectionFooter;
import hr.restart.util.reports.raKCISectionHeader;
import hr.restart.util.reports.raRPSectionHeaderROT;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;

/*
 * Created on 2004.11.17
 */

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class repREVTemplate extends repIzlazOrigTemplate {



  public raReportSection createSectionFooter1() {
    raKCISectionFooter sf0 = new raKCISectionFooter(this); // new
                                                           // raIzlazSectionFooter(this);
    //  sh.LabelZAPLATITI.setDefault(ep.CAPTION, "ODOBRENJE");

    raReportElement TextSLOVIMA = sf0.addModel(ep.TEXT, new String[]{"SLOVIMA", "", "", "", "", "", "Yes", "", "900",
    /* "1800" */"600", "8500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""});
    raReportElement LabelSlovima_ = sf0.addModel(ep.LABEL, new String[]{"Slovima :", "", "", /* "1800" */"600", "840", 
        "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""});
//    raReportElement TextNAPOMENAOPIS = sf0.addModel(ep.TEXT, new String[]{"NAPOMENAOPIS", "", "", "", "", "", "Yes", "Yes", "", /* "2220" */"820", "10580", "680", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""});

    return sf0;
  }
  public repREVTemplate() {
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepREV");
  }
  
  public raReportSection createSectionHeader0() {
//    raRPSectionHeader sh = new raRPSectionHeader(this);
    raRPSectionHeaderROT sh = new raRPSectionHeaderROT(this);
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nREVERS");
    sh.defaultAltererSect().getView(sh.LabelMjestoDatum, sh.TextNAZFRA).
        deleteElementsPushDown(sh, new raReportElement[] {sh.LabelDospijeceDatum, sh.LabelDospijeceDatum,
        sh.LabelNacin_otpreme, sh.LabelNacin_placanja, sh.LabelNarudzba,
        sh.LabelParitet, sh.TextSgetDATDOSP, sh.TextSgetDDOSP, sh.TextBRNARIZ,
        sh.TextSgetDATNARIZ, sh.TextSgetDVO, sh.TextNAZNAC, sh.TextNAZNACPL, sh.TextNAZFRA});
    sh.LabelR1.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.LabelObrazac.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    return sh;
  }

  public raReportSection createSectionHeader1() {
    return new raKCISectionHeader(this); // return new raIzlazSectionHeader(this);
  }

  public raReportSection createDetail() {
    return new raKCIDetail(this);
  }

//  public raReportSection createSectionFooter0() {
//    raKCISectionFooter sf0 = new raKCISectionFooter(this); // new raIzlazSectionFooter(this);
////    sh.LabelZAPLATITI.setDefault(ep.CAPTION, "ODOBRENJE");
//    
//    raReportElement TextSLOVIMA = sf0.addModel(ep.TEXT, new String[] {"SLOVIMA", "", "", "", "", "", "Yes", "", "900",
//        /*"1800"*/"400", "8500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""});
//    raReportElement LabelSlovima_ = sf0.addModel(ep.LABEL, new String[] {"Slovima :", "", "", /*"1800"*/"400", "840", "220", "",
//        "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""});
//    raReportElement TextNAPOMENAOPIS = sf0.addModel(ep.TEXT, new String[] {"NAPOMENAOPIS", "", "", "", "", "", "Yes",
//        "Yes", "", /*"2220"*/"820", "10580", "680", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
//        "", ""});
//    
//    return sf0;
//  }

}

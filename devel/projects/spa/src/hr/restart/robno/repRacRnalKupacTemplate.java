/****license*****************************************************************
**   file: repRacRnalKupacTemplate.java
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

import hr.restart.util.reports.raGRSectionFooterLines;
import hr.restart.util.reports.raGRSectionFooterMCLines;
import hr.restart.util.reports.raIzlazPageFooter;
import hr.restart.util.reports.raNewDefaultPageHeader;
import hr.restart.util.reports.raRacRnalSection_Header0;
import hr.restart.util.reports.raReportSection;

public class repRacRnalKupacTemplate extends repRacZaRnalOrigTemplate {

  public repRacRnalKupacTemplate() {
    System.out.println("repRacRnalKupacTemplate()");
  }
  public raReportSection createSectionHeader0() {
    raRacRnalSection_Header0 rsrsh0 = new raRacRnalSection_Header0(this);
    
    rsrsh0.TextMB.defaultAlterer().setControlSource("JMBG");
    rsrsh0.TextCPAR.defaultAlterer().setControlSource("=(if (> [CKUPAC] 0) [CKUPAC] \"\")");
    
    return rsrsh0;
  }
//  public raReportSection createSectionFooter0() {
//    return new raIzlazSectionFooter(this);
//  }
  public raReportSection createPageHeader() {
    return new raNewDefaultPageHeader(this); // return new raIzlazPageHeader(this);
  }
//  public raReportSection createDetail() {
//    return new raIzlazDetail(this);
//  }
//  public raReportSection createSectionHeader1() {
//    return new raIzlazSectionHeaderLines(this);
//  }
//  public raReportSection createSectionFooter2() {
//    return new raRacRnalSumSection_Footer2(this);
//  }
  public raReportSection createSectionFooter1() {
    if (hr.restart.sisfun.frmParam.getParam("robno", "GOTcijena", "VC").equalsIgnoreCase("MC")){
      raGRSectionFooterMCLines rgsfml = new raGRSectionFooterMCLines(this); // return new raGRSectionFooterMC(this);
      rgsfml.Text1.defaultAlterer().setControlSource("=(dsum \"IZNFMCPRP\")");

//      rgsfml.Text1.defaultAlterer().setControlSource("=(dsum \"ISP\")"); // bug tomo zakomentao
//=(dsum \"IPRODSP\")(+ (dsum \"IPRODBP\") (+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\")))
      rgsfml.Text2.defaultAlterer().setControlSource
//      ("=(-(+ (dsum \"IPRODBP\") (+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\")) (- (dsum \"ISP\"))))");
      ("=(- (dsum \"IZNFMCPRP\") (+ (dsum \"IPRODBP\") (+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\"))))");
      return rgsfml;
    }
    return new raGRSectionFooterLines(this);
  }
  
  public raReportSection createPageFooter() {
    return new raIzlazPageFooter(this);
  }
}
/****license*****************************************************************
**   file: repPonudaKupTemplate.java
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
import hr.restart.util.reports.raGRSectionFooterMCLines;
import hr.restart.util.reports.raIzlazDetail;
import hr.restart.util.reports.raIzlazDetailMC;
import hr.restart.util.reports.raIzlazSectionFooterLines;
import hr.restart.util.reports.raIzlazSectionHeaderLines;
import hr.restart.util.reports.raRPSectionHeader;
import hr.restart.util.reports.raReportSection;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repPonudaKupTemplate extends repIzlazOrigTemplate {

  public raReportSection createSectionHeader0() {
    System.out.println("repPonudaKupTemplate");
    raRPSectionHeader sh = new raRPSectionHeader(this);
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nP O N U D A");
    sh.LabelDospijece.setDefault(ep.CAPTION, "Opcija dana");
    sh.TextSgetDDOSP.setDefault(ep.CONTROL_SOURCE, "DDOSP");
    sh.TextNAZPAR.setDefault(ep.CONTROL_SOURCE, "NAZKUPCAL");
    sh.TextADR.setDefault(ep.CONTROL_SOURCE,"ADRKUPCA");
    sh.TextMJ.setDefault(ep.CONTROL_SOURCE,"PbrMjestoKupca");
    sh.TextMB.defaultAlterer().setControlSource("JMBG");
    sh.TextCPAR.defaultAlterer().setControlSource("CKUPAC");
//    sh.TextSgetDATDOSP.setDefault(ep.LEFT, sh.TextBRNARIZ.getDefault(ep.LEFT));
//    sh.TextSgetDATDOSP.setDefault(ep.ALIGN, "");
    sh.LabelR1.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    return sh;
//    return  new raIPSectionHeader(this);
  }

  public raReportSection createSectionHeader1() {
    return new raIzlazSectionHeaderLines(this); // return new raIzlazSectionHeader(this);
  }

//  public raReportSection createDetail() {
//    return new raIzlazDetail(this);
//  }
//
//  public raReportSection createSectionFooter0() {
//    return new raIzlazSectionFooterLines(this); // return new raIzlazSectionFooter(this);
//  }

  public raReportSection createDetail() {
    if (hr.restart.sisfun.frmParam.getParam("robno", "GOTcijena", "VC").equalsIgnoreCase("MC")){
      raIzlazDetailMC ridmc = new raIzlazDetailMC(this);
      ridmc.TextFMCPRP.defaultAlterer().setControlSource("FMCPRP");
      ridmc.TextIZNOSSTAVKESP.defaultAlterer().setControlSource("PonIznos");
      return ridmc;
    }
    else return new raIzlazDetail(this);
  }

  public raReportSection createSectionFooter1() {
    if (hr.restart.sisfun.frmParam.getParam("robno", "GOTcijena", "VC").equalsIgnoreCase("MC")){
      raGRSectionFooterMCLines rgsfml = new raGRSectionFooterMCLines(this);
      rgsfml.Text1.defaultAlterer().setControlSource("=(dsum \"PonIznos\")");
      /*rgsfml.Text2.defaultAlterer().setControlSource
      ("=(-(+ (dsum \"IPRODBP\") (+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\")) (- (dsum \"PonIznos\"))))");*/
      rgsfml.Text2.defaultAlterer().setControlSource
      ("=(- (dsum \"PonIznos\") (+ (dsum \"IPRODBP\") (+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\"))))");
      return rgsfml;
    }
    else return new raIzlazSectionFooterLines(this);
  }

  public repPonudaKupTemplate() {
  }
}
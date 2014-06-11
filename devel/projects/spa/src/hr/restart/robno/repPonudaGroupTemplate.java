/****license*****************************************************************
**   file: repPonudaGroupTemplate.java
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
import hr.restart.util.reports.raIzlazDetail;
import hr.restart.util.reports.raIzlazGroupSectionHeader;
import hr.restart.util.reports.raIzlazSectionFooterLines;
import hr.restart.util.reports.raIzlazSectionHeaderLines;
import hr.restart.util.reports.raPONSectionHeader0TF;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repPonudaGroupTemplate extends repIzlazGroupOrigTemplate {

  public raReportSection createSectionHeader0() {
    /*raRPSectionHeader sh = new raRPSectionHeader(this);
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nP O N U D A");
    sh.LabelDospijece.setDefault(ep.CAPTION, "Opcija dana");
    sh.TextSgetDDOSP.setDefault(ep.CONTROL_SOURCE, "DDOSP");
//    sh.TextSgetDATDOSP.setDefault(ep.LEFT, sh.TextBRNARIZ.getDefault(ep.LEFT));
//    sh.TextSgetDATDOSP.setDefault(ep.ALIGN, "");
    sh.LabelR1.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.LabelObrazac.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    return sh;
//    return  new raIPSectionHeader(this);*/
    raPONSectionHeader0TF sh = new raPONSectionHeader0TF(this);
    sh.TextCPAR.setDefault(ep.CONTROL_SOURCE, "=(if (> [CPAR] 0) [CPAR] \"\")");
    sh.defaultAltererSect().getView(sh.LabelNacin_placanja, sh.TextNAZNACPL).moveUp(
        sh.LabelNacin_placanja.defaultAlterer().getTop() - sh.LabelUgovor.defaultAlterer().getTop());
    sh.defaultAltererSect().removeModels(new raReportElement[] {
        sh.LabelNarudzba, sh.TextBRNARIZ, sh.TextSgetDATNARIZ,
        sh.LabelUgovor, sh.TextCUG, sh.TextSgetDATUG,
        sh.LabelNacin_otpreme, sh.TextNAZNAC, 
        sh.LabelParitet, sh.TextNAZFRA, sh.LabelIsporuka, 
        sh.TextISPORUKA, sh.LabelObrazac, sh.LabelR1});
    
    raReportElement[] adrmj = {sh.TextADR, sh.TextMJ};
    sh.defaultAltererSect().getView(adrmj).moveUp(40);
    raReportElement[] rest = {sh.TextMB, sh.TextCPAR, sh.TextTEL};
    sh.defaultAltererSect().getView(rest).moveUp(360);
    sh.Rectangle1.defaultAlterer().setHeight(sh.Rectangle1.defaultAlterer().getHeight() - 360);
    sh.Rectangle2.defaultAlterer().setHeight(sh.Rectangle2.defaultAlterer().getHeight() - 360);
    sh.Rectangle4.defaultAlterer().setHeight(sh.Rectangle4.defaultAlterer().getHeight() - 360);
    
    sh.setDefault(ep.SHRINK, raElixirPropertyValues.YES);
    //sh.defaultAltererSect().setHeight(sh.defaultAltererSect().getHeight() - 200);
    return sh;

//    return new raPONSectionHeader0TF(this);
  }

  public raReportSection createSectionHeader1() {
    raIzlazSectionHeaderLines sh = new raIzlazSectionHeaderLines(this); // return new raIzlazSectionHeader(this);
    long rbrGain = sh.LabelSifra.defaultAlterer().getLeft() - sh.LabelRbr.defaultAlterer().getLeft();
    long kolGain = sh.LabelKolicina.defaultAlterer().getWidth() * 4 / 10;
        
    sh.defaultAltererSect().removeModel(sh.LabelRbr);
    sh.LabelSifra.defaultAlterer().setLeft(sh.LabelSifra.defaultAlterer().getLeft() - rbrGain);
    sh.LabelKolicina.defaultAlterer().setWidth(sh.LabelKolicina.defaultAlterer().getWidth() - kolGain);
    sh.LabelKolicina.defaultAlterer().setLeft(sh.LabelKolicina.defaultAlterer().getLeft() + kolGain);
    sh.LabelKolicina.defaultAlterer().setCaption("Kol");
    sh.LabelNaziv.defaultAlterer().setLeft(sh.LabelNaziv.defaultAlterer().getLeft() - rbrGain);
    sh.LabelNaziv.defaultAlterer().setWidth(sh.LabelNaziv.defaultAlterer().getWidth() + rbrGain + kolGain);
    return sh;
  }

  public raReportSection createSectionHeader2() {
    return new raIzlazGroupSectionHeader(this); // return new raIzlazSectionHeader(this);
  }

  public raReportSection createDetail() {
    raIzlazDetail sh = new raIzlazDetail(this);
    sh.TextRBR.defaultAlterer().setControlSource("RBRDUMMY");
    long rbrGain = sh.TextCART.defaultAlterer().getLeft() - sh.TextRBR.defaultAlterer().getLeft();
    long kolGain = sh.TextKOL.defaultAlterer().getWidth() * 4 / 10;
    
    sh.defaultAltererSect().removeModel(sh.TextRBR);
    sh.TextCART.defaultAlterer().setLeft(sh.TextCART.defaultAlterer().getLeft() - rbrGain);
    sh.TextKOL.defaultAlterer().setWidth(sh.TextKOL.defaultAlterer().getWidth() - kolGain);
    sh.TextKOL.defaultAlterer().setLeft(sh.TextKOL.defaultAlterer().getLeft() + kolGain);

    sh.TextNAZART.defaultAlterer().setLeft(sh.TextNAZART.defaultAlterer().getLeft() - rbrGain);
    sh.TextNAZART.defaultAlterer().setWidth(sh.TextNAZART.defaultAlterer().getWidth() + rbrGain + kolGain);
    return sh;
  }
  
  public raReportSection createSectionFooter1() {
    return new raIzlazSectionFooterLines(this); // return new raIzlazSectionFooter(this);
  }

  public repPonudaGroupTemplate() {
  }
}

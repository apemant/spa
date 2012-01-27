/****license*****************************************************************
**   file: repPovratTemplate.java
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
import hr.restart.util.reports.raIzlazSectionFooterForCustom;
import hr.restart.util.reports.raIzlazSectionFooterLines;
import hr.restart.util.reports.raIzlazSectionHeaderLines;
import hr.restart.util.reports.raOTPDetail;
import hr.restart.util.reports.raOTPSectionHeader;
import hr.restart.util.reports.raRPSectionHeaderROT;
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

public class repPovratTemplate extends repIzlazOrigTemplate {

  public raReportSection createSectionHeader0() {
//    raRPSectionHeader sh = new raRPSectionHeader(this);
    raRPSectionHeaderROT sh = new raRPSectionHeaderROT(this);

    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nPOVRATNICA");
    sh.defaultAltererSect().getView(sh.LabelMjestoDatum, sh.TextNAZFRA).
    deleteElementsPushDown(sh, new raReportElement[] {sh.LabelDospijeceDatum, sh.LabelDospijeceDatum,
    sh.LabelNacin_otpreme, sh.LabelNacin_placanja, /*sh.LabelNarudzba,*/
    sh.LabelParitet, sh.TextSgetDATDOSP, sh.TextSgetDDOSP, /*sh.TextBRNARIZ,*/
    /*sh.TextSgetDATNARIZ,*/ sh.TextNAZNAC, sh.TextNAZNACPL, sh.TextNAZFRA});
//    sh.LabelR1.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.LabelNarudzba.setDefault(ep.CAPTION, "Izlazni dokument");
    sh.TextBRNARIZ.setDefault(ep.CONTROL_SOURCE, "BRDOKIZ");
    sh.TextSgetDATNARIZ.setDefault(ep.CONTROL_SOURCE, "SgetDATDOKIZ");
    sh.TextLogoMjestoZarez.defaultAlterer().setTop(
        sh.LabelMjestoDatum.defaultAlterer().getTop());
    return sh;
  }

  public raReportSection createSectionHeader1() {
    return new raOTPSectionHeader(this); // return new raIzlazSectionHeader(this);
  }

  public raReportSection createDetail() {
    return new raOTPDetail(this);
  }

  public raReportSection createSectionFooter1() {
    return new raIzlazSectionFooterForCustom(this); 
  }

  public repPovratTemplate() {
  }
}

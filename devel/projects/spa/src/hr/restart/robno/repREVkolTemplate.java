/****license*****************************************************************
**   file: repREVkolTemplate.java
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

public class repREVkolTemplate  extends repIzlazOrigTemplate {



  public raReportSection createSectionFooter1() {
    raKCISectionFooter sh = new raKCISectionFooter(this);
    sh.removeModels(new raReportElement[] {sh.Line2, sh.Label1, sh.LabelU_K_U_P_N_O, sh.Text1});
    sh.Line1.defaultAlterer().setTop(100);
//    sh.Text1.defaultAlterer().setControlSource("=(dsum \"INABreal\")");
    return sh;
  }
  public repREVkolTemplate() {
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
    raKCISectionHeader sh = new raKCISectionHeader(this);
    sh.removeModels(new raReportElement[] {sh.LabelCijena, sh.LabelIznos});
    sh.LabelKolicina.defaultAlterer().setLeft(9440);
    sh.LabelJm.defaultAlterer().setLeft(8940);
    sh.LabelNaziv_artikla.defaultAlterer().setWidth(7120);
    return sh;
  }

  public raReportSection createDetail() {
    raKCIDetail sh = new raKCIDetail(this);
    sh.removeModels(new raReportElement[]{sh.TextZC, sh.TextIRAZ});
    sh.TextKOL.defaultAlterer().setLeft(9440);
    sh.TextJM.defaultAlterer().setLeft(8940);
    sh.TextNAZART.defaultAlterer().setWidth(7120);
    return sh;
  }

}

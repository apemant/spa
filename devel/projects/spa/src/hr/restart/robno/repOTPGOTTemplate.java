/****license*****************************************************************
**   file: repOTPGOTTemplate.java
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
import hr.restart.util.reports.raRPSectionHeader;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;

/**
 * @author S.G.
 *
 * Started 2005.04.13
 * 
 */

public class repOTPGOTTemplate extends repOTPTemplate {

  public raReportSection createSectionHeader0() {
    raRPSectionHeader sh = new raRPSectionHeader(this);
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nO T P R E M N I C A");
    sh.defaultAltererSect().getView(sh.LabelMjesto, sh.TextNAZFRA).
        deleteElementsPushDown(sh, new raReportElement[] {sh.LabelNacinplacanja,
        sh.LabelDospijece, sh.TextSgetDATDOSP, sh.TextSgetDDOSP, sh.TextNAZNACPL});
    sh.LabelR1.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.LabelObrazac.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);

    sh.TextNAZPAR.setDefault(ep.CONTROL_SOURCE, "NAZKUPCAL");
    sh.TextADR.setDefault(ep.CONTROL_SOURCE, "ADRKUPCA");
    sh.TextMJ.setDefault(ep.CONTROL_SOURCE, "PbrMjestoKupca");
    sh.TextMB.setDefault(ep.CONTROL_SOURCE, "JMBG");
    sh.TextCPAR.setDefault(ep.CONTROL_SOURCE, "CKUPAC");
    
    return sh;
  }

}

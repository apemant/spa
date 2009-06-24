/****license*****************************************************************
**   file: repNarPopTemplate.java
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

import hr.restart.util.reports.raDetailNarPop;
import hr.restart.util.reports.raIzlazSectionFooterForCustom;
import hr.restart.util.reports.raIzlazSectionFooterForCustomSlovima;
import hr.restart.util.reports.raRPSectionHeader;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raSection_Header1NarPop;

public class repNarPopTemplate extends repIzlazOrigTemplate {
  public raReportSection createSectionHeader0() {
    raRPSectionHeader sh = new raRPSectionHeader(this);
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nNARUDŽBENICA");

    sh.defaultAltererSect().getView(sh.LabelMjesto, sh.TextNAZFRA).
        deleteElementsPushDown(sh, new raReportElement[]
        {sh.LabelNarudzba, sh.TextBRNARIZ, sh.TextSgetDATNARIZ,
        /*sh.LabelParitet, sh.TextNAZFRA*/});

    sh.LabelR1.defaultAlterer().setVisible(false);
    sh.LabelObrazac.defaultAlterer().setVisible(false);
    sh.LabelDospijece.defaultAlterer().setCaption("Rok izvršenja");
    
    sh.LabelParitet.defaultAlterer().setCaption("Mjesto troška");
    sh.TextNAZFRA.defaultAlterer().setControlSource("CSNS");

    sh.addModel(ep.TEXT, new String[] {
      "NATEM", "", "", "", "", "", "", "", "20",
     "2600", "10500", "220", "", "", "", "", "", "", "Lucida Bright", "8",
      "", "", "", "", ""
    });
    return sh;
  }
  public raReportSection createSectionFooter1() {
    raIzlazSectionFooterForCustomSlovima sh = new raIzlazSectionFooterForCustomSlovima(this);
    sh.Text1.defaultAlterer().setControlSource("=(dsum \"IBPNAR\")");
    sh.TextSLOVIMA.defaultAlterer().setControlSource("SLOVIMApop");
    return sh;
  }
  public raReportSection createDetail() {
    raDetailNarPop sh = new raDetailNarPop(this);
    return sh;
  }
  public raReportSection createSectionHeader1() {
    raSection_Header1NarPop sh = new raSection_Header1NarPop(this);
    return sh;
  }
  
  public raReportSection createSectionFooter0() {
    /*raIzlazSectionFooterForCustom isf0 = new raIzlazSectionFooterForCustom(this);
    isf0.TextNAPOMENAOPIS.defaultAlterer().setControlSource("NAZNAP");
    return isf0;*/    
    return new raIzlazSectionFooterForCustom(this);
  }

}

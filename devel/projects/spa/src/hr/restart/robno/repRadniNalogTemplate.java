/****license*****************************************************************
**   file: repRadniNalogTemplate.java
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

import hr.restart.util.reports.raIzlazPageFooter;
import hr.restart.util.reports.raNewDefaultPageHeader;
import hr.restart.util.reports.raPrazniDetail;
import hr.restart.util.reports.raRadniNalogSection_Footer0;
import hr.restart.util.reports.raRadniNalogSection_Header0;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;

public class repRadniNalogTemplate extends repRadniNalogOrigTemplate {

  public repRadniNalogTemplate() {
  }
  public raReportSection createPageHeader() {
    return new raNewDefaultPageHeader(this);
  }
  public raReportSection createSectionHeader0() {
    
    raRadniNalogSection_Header0 rnlsh0 = new raRadniNalogSection_Header0(this);
    
    rnlsh0.LabelStatus.defaultAlterer().setVisible(false);
    rnlsh0.TextSTATUS.defaultAlterer().setVisible(false);
    
    rnlsh0.defaultAltererSect().getView(rnlsh0.LabelDatum_prijave, rnlsh0.TextDATUMZATVARANJA).
    deleteElementsPushDown(rnlsh0,new raReportElement[]{rnlsh0.LabelDatum_obrade,rnlsh0.TextDATUMOBRADE,
        rnlsh0.LabelDatum_zatvaranja, rnlsh0.TextDATUMZATVARANJA});
    
    return rnlsh0;
  }
  public raReportSection createSectionFooter0() {
    return new raRadniNalogSection_Footer0(this);
  }
  public raReportSection createDetail() {
    return new raPrazniDetail(this);
  }

  public raReportSection createPageFooter() {
    return new raIzlazPageFooter(this);
  }

  private void modifyThis() {
  }
}
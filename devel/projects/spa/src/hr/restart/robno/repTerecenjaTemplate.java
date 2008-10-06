/****license*****************************************************************
**   file: repTerecenjaTemplate.java
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
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raSectionHeader0TF;

public class repTerecenjaTemplate extends repIzlazOrigTemplate {

  public raReportSection createSectionHeader0() {

    raSectionHeader0TF sh = new raSectionHeader0TF(this);
    sh.LabelR_A_C_U_N.defaultAlterer().setCaption("\nTEREÆENJE");
    sh.defaultAltererSect().getView(sh.LabelMjestoDatum,sh.TextSgetDATDOKIZ).
    deleteElementsPushDown(sh, new raReportElement[]{sh.LabelNarudzba, sh.TextBRNARIZ, sh.TextSgetDATNARIZ});
//  zakaii::    sh.TextTEL.defaultAlterer().setVisible(false);

    return sh;
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

  public repTerecenjaTemplate() {
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepRac");
  }
}

/*
public class repTerecenjaTemplate {

}
*/
/****license*****************************************************************
**   file: repNarudzba2Template.java
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
import hr.restart.util.reports.raIzlazDetail2;
import hr.restart.util.reports.raIzlazSectionFooter2Lines;
import hr.restart.util.reports.raIzlazSectionHeader2Lines;
import hr.restart.util.reports.raRPSectionHeader;
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

public class repNarudzba2Template extends repIzlazOrigTemplate {

  public raReportSection createSectionHeader0() {
    raRPSectionHeader sh = new raRPSectionHeader(this);
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nNARUDŽBENICA");

    sh.defaultAltererSect().getView(sh.LabelMjesto, sh.TextNAZFRA).
        deleteElementsPushDown(sh, new raReportElement[]
        {sh.LabelNarudzba, sh.TextBRNARIZ, sh.TextSgetDATNARIZ});
    sh.LabelR1.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.LabelObrazac.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    return sh;
  }

  public raReportSection createSectionHeader1() {
    return new raIzlazSectionHeader2Lines(this); // return new raIzlazSectionHeader2(this);
  }

  public raReportSection createDetail() {
    return new raIzlazDetail2(this);
  }

  public raReportSection createSectionFooter1() {
    return new raIzlazSectionFooter2Lines(this); // return new raIzlazSectionFooter2(this);
  }

  public repNarudzba2Template() {
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepNarudzba2");
  }
}

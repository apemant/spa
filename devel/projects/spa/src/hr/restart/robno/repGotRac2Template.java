/****license*****************************************************************
**   file: repGotRac2Template.java
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

import hr.restart.util.reports.ReportModifier;
import hr.restart.util.reports.raGRSectionFooter2Lines;
import hr.restart.util.reports.raGRSectionHeaderWin;
import hr.restart.util.reports.raIzlazDetail2;
import hr.restart.util.reports.raIzlazSectionHeader2Lines;
import hr.restart.util.reports.raReportSection;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repGotRac2Template extends repIzlazOrigTemplate {

  raGRSectionHeaderWin sh;

  public raReportSection createSectionHeader0() {
    sh = new raGRSectionHeaderWin(this);
//    sh.defaultAltererSect().getView(sh.LabelMjesto, sh.TextNAZFRA).
//        deleteElementsPushDown(sh, new raReportElement[] {
//        sh.LabelNacinplacanja, sh.TextNAZNACPL});
//        sh.LabelRACUNOTPREMNICA.defaultAlterer().setCaption("\nR A È U N");
//
//        sh.TextMJ.defaultAlterer().setControlSource("PbrMjestoKupca");
//
//    sh.TextCPAR.defaultAlterer().setControlSource("CKUPAC");
    return sh;
  }
//  public raReportSection createSectionHeader0() {
//    return new raGRSectionHeader(this);
//  }

  public raReportSection createSectionHeader1() {
    return new raIzlazSectionHeader2Lines(this); // return new raIzlazSectionHeader2(this);
  }

  public raReportSection createDetail() {
    return new raIzlazDetail2(this);
  }

  public raReportSection createSectionFooter1() {
    return new raGRSectionFooter2Lines(this); // return new raGRSectionFooter2(this);
  }

  public repGotRac2Template() {
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepGotRac2");
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepGotRac");
    this.addReportModifier(new ReportModifier(){
      public void modify(){
        if (reportsQuerysCollector.getRQCModule().getQueryDataSet().getInt("CKUPAC") == 0){
//          System.out.println("dva reda ukidam R-1");
          sh.LabelObrazac.setCaption("");
          sh.LabelR1.setCaption("");
        } else {
          sh.LabelR1.setCaption("R-1");
        }
      }
    });

  }
}

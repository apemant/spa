/****license*****************************************************************
**   file: repGrnRac2Template.java
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

import hr.restart.util.reports.raGRNSectionHeader0;
import hr.restart.util.reports.raGRSectionFooter2Lines;
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

public class repGrnRac2Template extends repIzlazOrigTemplate {

  /*raGRSectionHeaderWin*/ //raGRNSectionHeader0 sh;

  public raReportSection createSectionHeader0() {
//    sh = new raGRNSectionHeader0(this);
    raGRNSectionHeader0 sh = new raGRNSectionHeader0(this);
//    sh.TextTEL.setDefault(ep.CONTROL_SOURCE, "TELKUPCA");
//    sh.TextMJKUPCA.setDefault(ep.CONTROL_SOURCE, "PbrMjestoKupca");
//    sh.TextCPAR.setDefault(ep.CONTROL_SOURCE,"CKUPAC");
//    sh.TextCPAR.setDefault(ep.CONTROL_SOURCE, "=(if (> [CKUPAC] 0) [CKUPAC] \"\")");
//    sh.TextTEL.defaultAlterer().setVisible(false);
    /*if (hr.restart.sisfun.frmParam.getParam("robno","ispisTELFAX_Par","N","Ispis Telefona i faxa partnera na ROT-u u polju za adresu").
        equalsIgnoreCase("N")) {
      sh.TextTEL.defaultAlterer().setVisible(false);
//      sh.TextFAX.defaultAlterer().setVisible(false);
    }*/
    
    return sh;
//    return sh;
//  public raReportSection createSectionHeader0() {
//    return new raRacSectionHeader(this);
  }

  public raReportSection createSectionHeader1() {
    return new raIzlazSectionHeader2Lines(this); // return new raIzlazSectionHeader2(this);
  }

  public raReportSection createDetail() {
    return new raIzlazDetail2(this);
  }

  public raReportSection createSectionFooter1() {
    return new raGRSectionFooter2Lines(this); // return new raGRSectionFooter2(this);
  }

  public repGrnRac2Template() {
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepGrnRac2");
//    this.addReportModifier(new ReportModifier(){
//      public void modify(){
//        System.out.println("reportsQuerysCollector.getRQCModule().getQueryDataSet().getInt(\"CKUPAC\") = "+reportsQuerysCollector.getRQCModule().getQueryDataSet().getInt("CKUPAC"));
//        if (reportsQuerysCollector.getRQCModule().getQueryDataSet().getInt("CKUPAC") == 0){
//          System.out.println("dvored ukidam R-1");
//          sh.LabelObrazac.setCaption("");
//          sh.LabelR1.setCaption("");
//        } else {
//          sh.LabelR1.setCaption("R-1");
//        }
//      }
//    });
  }
}

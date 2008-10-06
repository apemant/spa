/****license*****************************************************************
**   file: repPonuda2Template.java
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

import hr.restart.util.reports.raIzlazDetail2;
import hr.restart.util.reports.raIzlazSectionFooter2Lines;
import hr.restart.util.reports.raIzlazSectionHeader2Lines;
import hr.restart.util.reports.raPONSectionHeader0TF;
import hr.restart.util.reports.raReportSection;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repPonuda2Template extends repIzlazOrigTemplate {

  public raReportSection createSectionHeader0() {
    /*raRPSectionHeader sh = new raRPSectionHeader(this);
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nP O N U D A");
    sh.LabelDospijece.setDefault(ep.CAPTION, "Opcija dana");
    sh.TextSgetDDOSP.setDefault(ep.CONTROL_SOURCE, "DDOSP");
//    sh.TextSgetDATDOSP.setDefault(ep.LEFT, sh.TextBRNARIZ.getDefault(ep.LEFT));
//    sh.TextSgetDATDOSP.setDefault(ep.ALIGN, "");
    sh.LabelR1.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    return sh;*/
    raPONSectionHeader0TF sh = new raPONSectionHeader0TF(this);
    sh.TextCPAR.setDefault(ep.CONTROL_SOURCE, "=(if (> [CPAR] 0) [CPAR] \"\")");
//////Prebaceno u raPONSectionHeader0TF
//    if (hr.restart.sisfun.frmParam.getParam("robno","ispisTELFAX_Par","N","Ispis Telefona i faxa partnera na ROT-u u polju za adresu").
//        equalsIgnoreCase("N")) {
//      sh.TextTEL.defaultAlterer().setVisible(false);
////      sh.TextFAX.defaultAlterer().setVisible(false);
//    }
    
    return sh;

//    return new raPONSectionHeader0TF(this);
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

  public repPonuda2Template() {
  }
}

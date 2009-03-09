/****license*****************************************************************
**   file: repPonudaNopTemplate.java
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
import hr.restart.util.reports.raOTPDetail;
import hr.restart.util.reports.raOTPSectionHeader;
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

public class repPonudaNopTemplate extends repIzlazOrigTemplate {

  public raReportSection createSectionHeader0() {
    /*raRPSectionHeader sh = new raRPSectionHeader(this);
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nP O N U D A");
    sh.LabelDospijece.setDefault(ep.CAPTION, "Opcija dana");
    sh.TextSgetDDOSP.setDefault(ep.CONTROL_SOURCE, "DDOSP");
//    sh.TextSgetDATDOSP.setDefault(ep.LEFT, sh.TextBRNARIZ.getDefault(ep.LEFT));
//    sh.TextSgetDATDOSP.setDefault(ep.ALIGN, "");
    sh.LabelR1.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.LabelObrazac.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    return sh;
//    return  new raIPSectionHeader(this);*/
    raPONSectionHeader0TF sh = new raPONSectionHeader0TF(this);
    sh.TextCPAR.setDefault(ep.CONTROL_SOURCE, "=(if (> [CPAR] 0) [CPAR] \"\")");

    
    return sh;
    
//    return new raPONSectionHeader0TF(this);
  }

  public raReportSection createSectionHeader1() {
    return new raOTPSectionHeader(this); // return new raIzlazSectionHeader(this);
  }

  public raReportSection createDetail() {
    return new raOTPDetail(this);
  }

  public raReportSection createSectionFooter1() {
    return new raIzlazSectionFooterLines(this); // return new raIzlazSectionFooter(this);
  }

  public repPonudaNopTemplate() {
  }
}

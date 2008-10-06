/****license*****************************************************************
**   file: repPonuda2VKupTemplate.java
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
import hr.restart.util.reports.raReportSection;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repPonuda2VKupTemplate extends repIzlazOrigTemplate {

  public raReportSection createSectionHeader0() {
    System.out.println("repPonuda2VKupTemplate");
    raRPSectionHeader sh = new raRPSectionHeader(this);
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nP O N U D A");
    sh.LabelDospijece.setDefault(ep.CAPTION, "Opcija dana");
    sh.TextSgetDATDOSP.setDefault(ep.CONTROL_SOURCE, "DDOSP");
    sh.TextNAZPAR.setDefault(ep.CONTROL_SOURCE, "NAZKUPCAL");
    sh.TextADR.setDefault(ep.CONTROL_SOURCE,"ADRKUPCA");
    sh.TextMJ.setDefault(ep.CONTROL_SOURCE,"PbrMjestoKupca");
    sh.TextSgetDATDOSP.setDefault(ep.LEFT, sh.TextBRNARIZ.getDefault(ep.LEFT));
    sh.TextSgetDATDOSP.setDefault(ep.ALIGN, "");
    sh.LabelR1.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
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

  public repPonuda2VKupTemplate() {
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepPonuda2V");
  }
}
/*

package hr.restart.robno;

public class  {

  public repPonuda2VKupTemplate() {
  }
}*/
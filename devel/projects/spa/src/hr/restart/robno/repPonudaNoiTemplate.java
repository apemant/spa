/****license*****************************************************************
**   file: repPonudaNoiTemplate.java
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

import hr.restart.util.reports.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repPonudaNoiTemplate extends repIzlazOrigTemplate {

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
    raPonSectionHeader1 sh = new raPonSectionHeader1(this);
    long gain = sh.LabelIznos.defaultAlterer().getLeft() + sh.LabelIznos.defaultAlterer().getWidth() -
                sh.LabelCijena.defaultAlterer().getLeft() - sh.LabelCijena.defaultAlterer().getWidth();
    sh.removeModel(sh.LabelIznos);
    sh.LabelCijena.defaultAlterer().moveHor(gain);
    sh.LabelKolicina.defaultAlterer().moveHor(gain);
    sh.LabelJm.defaultAlterer().moveHor(gain);
    sh.LabelNaziv_artikla.defaultAlterer().alterWidth(gain);
    return sh;
  }

  public raReportSection createDetail() {
    raPonDetail det = new raPonDetail(this);
    long gain = det.TextIRAZ.defaultAlterer().getLeft() + det.TextIRAZ.defaultAlterer().getWidth() -
        det.TextZC.defaultAlterer().getLeft() - det.TextZC.defaultAlterer().getWidth();
    det.removeModel(det.TextIRAZ);    
    det.TextZC.defaultAlterer().setControlSource("FC");
    det.TextZC.defaultAlterer().moveHor(gain);
    det.TextKOL.defaultAlterer().moveHor(gain);
    det.TextJM.defaultAlterer().moveHor(gain);
    det.TextNAZART.defaultAlterer().alterWidth(gain);
    det.TextNAZARText.defaultAlterer().alterWidth(gain);
    return det;
  }

  public raReportSection createSectionFooter1() {
     raReportSection foot = new raReportSection(template.getModel(raElixirProperties.SECTION_FOOTER + 1), 
         new String[] {"FormatBroj", "", "", "", "Yes", "", "", "120"});
     foot.addModel(ep.LINE, new String[] {"", "", "", "100", "10820", "0", "", "", ""});     
     return foot;
  }

  public repPonudaNoiTemplate() {
  }
}

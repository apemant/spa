/****license*****************************************************************
**   file: repDOS4Template.java
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

import hr.restart.util.reports.raDosDetailjmNI;
import hr.restart.util.reports.raDosSH1jmNI;
import hr.restart.util.reports.raReportSection;

public class repDOS4Template extends repDOSTemplate {

  public raReportSection createSectionHeader1() {
    return new raDosSH1jmNI(this);
/*    rsh.LabelSifra.setCaption(Aut.getAut().getIzlazCARTdep("Šifra", "Oznaka", "Barcode"));
    rsh.resizeElement(rsh.LabelSifra, Aut.getAut().getIzlazCARTdep(900, 1780, 1780), rsh.LabelNaziv_artikla);
    String komText = frmParam.getParam("robno", "komText", "Kom",
      "Tekst za ispisati na mjestu dodatne kolièine na dostavnici");
    rsh.LabelKom.setCaption(komText);
    return rsh;*/
  }
  
  public raReportSection createDetail() {
    return new raDosDetailjmNI(this);
/*    rdd.resizeElement(rdd.TextCART, Aut.getAut().getIzlazCARTdep(900, 1780, 1780), rdd.TextNAZART);
    String id = frmParam.getParam("robno", "ispDOS", "D",
      "Ispisati isporuèenu kolièinu na dostavnici (D,N)?");
    if (id.equalsIgnoreCase("N"))
      rdd.TextKOL.setControlSource("");
    return rdd;*/
  }
  
  public void modifyThis() {}

}
/****license*****************************************************************
**   file: repKomIzdTemplate.java
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
import hr.restart.util.reports.raIzlazSectionHeaderLines;
import hr.restart.util.reports.raKCISectionFooter;
import hr.restart.util.reports.raPONSectionHeader0TF;
import hr.restart.util.reports.raPonIzlazDetail;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;


public class repKomIzdTemplate extends repPonudaTemplate {

  public raReportSection createSectionHeader0() {
    raPONSectionHeader0TF sh = new raPONSectionHeader0TF(this);
    sh.TextCPAR.setDefault(ep.CONTROL_SOURCE, "=(if (> [CPAR] 0) [CPAR] \"\")");
    sh.LabelP_O_N_U_D_A.defaultAlterer().setCaption("\nKomisijska izdadnica");
    sh.removeModels(new raReportElement[] {sh.LabelNacin_placanja, sh.LabelNacin_otpreme, 
        sh.LabelParitet, sh.TextNAZNACPL, sh.TextNAZNAC, sh.TextNAZFRA, sh.LabelObrazac, sh.LabelR1});
    
    return sh;
  }
  
  public raReportSection createSectionHeader1() {
    raIzlazSectionHeaderLines sh = new raIzlazSectionHeaderLines(this);
    
    sh.LabelRbr.defaultAlterer().alterWidth(-60L);
    sh.LabelSifra.defaultAlterer().moveHor(-60L);
    sh.LabelNaziv.defaultAlterer().moveHor(-60L);
    sh.LabelNaziv.defaultAlterer().alterWidth(-500L);
    sh.LabelKolicina.defaultAlterer().moveHor(-560L);
    sh.LabelKolicina.defaultAlterer().alterWidth(-80L);
    sh.LabelJmj.defaultAlterer().moveHor(-640L);
    sh.LabelJmj.defaultAlterer().alterWidth(-60);
    sh.LabelCijena.defaultAlterer().moveHor(-700L);
    sh.LabelCijena.defaultAlterer().alterWidth(-220L);
    sh.Label1.defaultAlterer().moveHor(-920L);
    sh.Label1.defaultAlterer().alterWidth(-80L);
    sh.LabelPop.defaultAlterer().moveHor(-920L);
    sh.LabelPop.defaultAlterer().alterWidth(-40L);
    sh.LabelPor.defaultAlterer().moveHor(-960L);
    sh.LabelPor.defaultAlterer().alterWidth(-40L);
    sh.LabelIznos.defaultAlterer().moveHor(-1000L);
    sh.LabelIznos.defaultAlterer().alterWidth(-200L);
    raReportElement uk = sh.copyToModify(sh.LabelIznos);
    uk.alterWidth(-20);
    uk.moveHor(1220L);
    uk.setCaption("Ukupno");
    
    return sh;
  }

  private raReportElement neto, uk;
  public raReportSection createDetail() {
    raPonIzlazDetail sh = new raPonIzlazDetail(this);
    
    sh.TextRBR.defaultAlterer().alterWidth(-60L);
    sh.TextCART.defaultAlterer().moveHor(-60L);
    sh.TextNAZART.defaultAlterer().moveHor(-60L);
    sh.TextNAZART.defaultAlterer().alterWidth(-500L);
    sh.TextNAZARText.defaultAlterer().moveHor(-60L);
    sh.TextKOL.defaultAlterer().moveHor(-560L);
    sh.TextKOL.defaultAlterer().alterWidth(-80L);
    sh.TextJM.defaultAlterer().moveHor(-640L);
    sh.TextJM.defaultAlterer().alterWidth(-60);
    sh.TextFC.defaultAlterer().moveHor(-700L);
    sh.TextFC.defaultAlterer().alterWidth(-220L);
    sh.TextUPRAB1.defaultAlterer().moveHor(-920L);
    sh.TextUPRAB1.defaultAlterer().alterWidth(-40L);
    sh.TextPor1p2p3Naz.defaultAlterer().moveHor(-960L);
    sh.TextPor1p2p3Naz.defaultAlterer().alterWidth(-40L);
    sh.TextINETO.defaultAlterer().moveHor(-1000L);
    sh.TextINETO.defaultAlterer().alterWidth(-200L);
    uk = sh.copyToModify(sh.TextINETO);
    uk.alterWidth(-20);
    uk.moveHor(1220L);
    uk.setControlSource("KomBP");
    sh.TextINETO.defaultAlterer().setControlSource("KomNeto");
    neto = sh.TextINETO;
    
    return sh;
  }
  
  public raReportSection createSectionFooter1() {
    raKCISectionFooter sh = new raKCISectionFooter(this);
    
    long left = sh.Label1.defaultAlterer().getLeft();
    sh.removeModel(sh.Label1);
    sh.removeModel(sh.LabelU_K_U_P_N_O);
    raReportElement ineto = sh.copyToModify(sh.Text1);
    sh.Text1.defaultAlterer().setLeft(uk.defaultAlterer().getLeft());
    sh.Text1.defaultAlterer().setWidth(uk.defaultAlterer().getWidth());
    sh.Text1.defaultAlterer().setControlSource("=(dsum \"KomBP\")");
    ineto.setLeft(neto.defaultAlterer().getLeft());
    ineto.setWidth(neto.defaultAlterer().getWidth());
    ineto.setControlSource("=(dsum \"KomNeto\")");
    
    raReportElement ukupno = sh.copyToModify(sh.Text1);
    ukupno.setLeft(left);
    ukupno.setWidth(ineto.getLeft() - left - 20);
    ukupno.setControlSource("=(\" U K U P N O\")");
    ukupno.setTextAlign(raElixirPropertyValues.LEFT);
    sh.Line1.defaultAlterer().moveVert(20);

    return sh;
  }
  
}

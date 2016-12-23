/****license*****************************************************************
**   file: repOTPLOTTemplate.java
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

import hr.restart.util.reports.raDosDetail;
import hr.restart.util.reports.raDosSH1;
import hr.restart.util.reports.raRPSectionHeaderROT;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;

/**
 * @author S.G.
 *
 * Started 2005.04.28
 * 
 */

public class repOTPLOT2Template extends repDOSTemplate {

  public raReportSection createSectionHeader0() {
    raRPSectionHeaderROT sh = new raRPSectionHeaderROT(this);
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nO T P R E M N I C A");
    sh.LabelR1.defaultAlterer().setVisible(false);
    sh.LabelObrazac.defaultAlterer().setVisible(false);
    return sh;
    /*raRPSectionHeader sh = new raRPSectionHeader(this);
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nO T P R E M N I C A");
    sh.defaultAltererSect().getView(sh.LabelMjesto, sh.TextNAZFRA).
        deleteElementsPushDown(sh, new raReportElement[] {sh.LabelNacinplacanja,
        sh.LabelDospijece, sh.TextSgetDATDOSP, sh.TextSgetDDOSP, sh.TextNAZNACPL});
    sh.LabelR1.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.LabelObrazac.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    return sh;*/
  }

  public raReportSection createSectionHeader1() {
    raDosSH1 sh = new raDosSH1(this);
    
    sh.LabelNaziv_artikla.defaultAlterer().alterWidth(-1600);
    sh.LabelJmj.defaultAlterer().moveHor(-1600);
    raReportElement lot = sh.copyToModify(sh.LabelNaziv_artikla);
    lot.setCaption("�ar�a / Lot");
    lot.setWidth(1040);
    lot.setLeft(sh.LabelJmj.defaultAlterer().getLeft() + sh.LabelJmj.defaultAlterer().getWidth() + 20);
    raReportElement rok = sh.copyToModify(sh.LabelNaziv_artikla);
    rok.setCaption("Upotrebivo do");
    rok.setWidth(1220);
    rok.setLeft(sh.LabelJmj.defaultAlterer().getLeft() + sh.LabelJmj.defaultAlterer().getWidth() + 1080);

    sh.LabelKol.defaultAlterer().alterWidth(-360);
    sh.LabelKol.defaultAlterer().moveHor(360);
    sh.LabelKom.defaultAlterer().alterWidth(-340);
    sh.LabelKom.defaultAlterer().moveHor(700);
    
    return sh;
/*    rsh.LabelSifra.setCaption(Aut.getAut().getIzlazCARTdep("�ifra", "Oznaka", "Barcode"));
    rsh.resizeElement(rsh.LabelSifra, Aut.getAut().getIzlazCARTdep(900, 1780, 1780), rsh.LabelNaziv_artikla);
    return rsh;*/
  }
  
  public raReportSection createDetail() {
    raDosDetail sh = new raDosDetail(this);
    
    sh.TextNAZART.defaultAlterer().alterWidth(-1600);
    sh.TextJM.defaultAlterer().moveHor(-1600);
    raReportElement lot = sh.copyToModify(sh.TextNAZART);
    lot.setControlSource("LOT");
    lot.setWidth(1040);
    lot.setLeft(sh.TextJM.defaultAlterer().getLeft() + sh.TextJM.defaultAlterer().getWidth() + 20);
    raReportElement rok = sh.copyToModify(sh.TextJM);
    rok.setControlSource("ROKTRAJ");
    rok.setWidth(1220);
    rok.setLeft(sh.TextJM.defaultAlterer().getLeft() + sh.TextJM.defaultAlterer().getWidth() + 1080);
    sh.TextKOL.defaultAlterer().alterWidth(-360);
    sh.TextKOL.defaultAlterer().moveHor(360);
    sh.TextKOL1.defaultAlterer().alterWidth(-340);
    sh.TextKOL1.defaultAlterer().moveHor(700);
    
    return sh;
/*    rdd.resizeElement(rdd.TextCART, Aut.getAut().getIzlazCARTdep(900, 1780, 1780), rdd.TextNAZART);
    return rdd;*/
  }
  
  public void modifyThis() {
  }
}

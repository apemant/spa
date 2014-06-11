/****license*****************************************************************
**   file: repRacGroupTemplate.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.util.reports.raElixirPropertyValues;
import hr.restart.util.reports.raIzlazDetail;
import hr.restart.util.reports.raIzlazGroupSectionHeader;
import hr.restart.util.reports.raIzlazSectionFooterLines;
import hr.restart.util.reports.raIzlazSectionHeaderLines;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raSectionHeader0TF;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repRacGroupTemplate extends repIzlazGroupOrigTemplate {

  public raReportSection createSectionHeader0() {
//    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nR A \u010C U N");
//    sh.defaultAltererSect().getView(sh.LabelMjesto, sh.TextNAZFRA).
//        deleteElementsPushDown(sh, new raReportElement[] {sh.LabelParitet, sh.TextNAZFRA});
//    long top = sh.LabelNacin_placanja.defaultAlterer().getTop();
//    raReportElement lab = sh.copyToModify(sh.LabelNarudzba);
//    lab.setCaption("Izlazni dokument");
//    lab.setTop(top);
//    lab.setLeft(lab.getLeft() - 300);
//    lab.setWidth(lab.getWidth() + 300);
//    raReportElement tx1 = sh.copyToModify(sh.TextBRNARIZ);
//    tx1.setControlSource("BRDOKIZ");
//    tx1.setTop(top);
//    raReportElement tx2 = sh.copyToModify(sh.TextSgetDATNARIZ);
//    tx2.setControlSource("SgetDATDOKIZ");
//    tx2.setTop(top);
    

    raSectionHeader0TF sh = new raSectionHeader0TF(this);
//  zakaii::     sh.TextTEL.defaultAlterer().setVisible(false);
    
    
    sh.defaultAltererSect().getView(sh.LabelNacin_placanja, sh.TextNAZNACPL).moveUp(
        sh.LabelNacin_placanja.defaultAlterer().getTop() - sh.LabelNarudzba.defaultAlterer().getTop());
    
    sh.defaultAltererSect().removeModels(new raReportElement[] {
        sh.LabelNarudzba, sh.TextBRNARIZ, sh.TextSgetDATNARIZ,
        sh.LabelIzlazni_dokument, sh.TextBRDOKIZ, sh.TextLABDOD, 
        sh.TextTEXTDOD, sh.LabelIsporuka, 
        sh.TextISPORUKA, sh.LabelObrazac, sh.LabelR1});
    
    raReportElement[] adrmj = {sh.TextADR, sh.TextMJ};
    sh.defaultAltererSect().getView(adrmj).moveUp(40);
    raReportElement[] rest = {sh.TextMB, sh.TextCPAR, sh.TextTEL};
    sh.defaultAltererSect().getView(rest).moveUp(360);
    sh.Rectangle1.defaultAlterer().setHeight(sh.Rectangle1.defaultAlterer().getHeight() - 360);
    sh.Rectangle2.defaultAlterer().setHeight(sh.Rectangle2.defaultAlterer().getHeight() - 360);
    sh.Rectangle3.defaultAlterer().setHeight(sh.Rectangle3.defaultAlterer().getHeight() - 360);
    
    /*long topNar = sh.LabelNarudzba.defaultAlterer().getTop();
    long topIzDok = sh.LabelIzlazni_dokument.defaultAlterer().getTop();
    
    sh.defaultAltererSect().getView(sh.LabelNarudzba, sh.LabelR1).moveDown(topIzDok - topNar);
    
    raReportElement labelUgovor = sh.copyToModify(sh.LabelNarudzba);
    labelUgovor.setCaption("Ugovor");
    labelUgovor.setTop(topNar);
    
    raReportElement textCUG = sh.copyToModify(sh.TextBRNARIZ);
    textCUG.setControlSource("CUG");
    textCUG.setTop(topNar);
    
    raReportElement textSgetDatug = sh.copyToModify(sh.TextSgetDATNARIZ);
    textSgetDatug.setControlSource("SgetDATUG");
    textSgetDatug.setTop(topNar);*/
    
    
    /*if (hr.restart.sisfun.frmParam.getParam("robno","ispisTELFAX_Par","N","Ispis Telefona i faxa partnera na ROT-u u polju za adresu").
.        equalsIgnoreCase("N")) {
      sh.TextTEL.defaultAlterer().setVisible(false);
//      sh.TextFAX.defaultAlterer().setVisible(false);
    }*/
    
    if (frmParam.getParam("robno", "vertZag", "N", 
        "Vertikalno pozicionirano zaglavlje raèuna").equals("D")) {
      raReportElement isp = sh.LabelIsporuka.defaultAlterer();
      raReportElement rac = sh.LabelR_A_C_U_N.defaultAlterer();
      raReportSection win = sh.defaultAltererSect().getView(
          isp.getLeft(), rac.getTop(), rac.getRight(), isp.getBottom());
      sh.setHeight(sh.getHeight() + isp.getBottom() - rac.getTop());
      
      win.moveDown(isp.getTop() - rac.getTop() - 120);
      win.moveLeft(5760);
      raReportElement r1 = sh.LabelR1.defaultAlterer();
      r1.setTop(r1.getTop() + isp.getTop() - rac.getTop() - 120);
      raReportElement ob = sh.LabelObrazac.defaultAlterer();
      ob.setTop(ob.getTop() + isp.getTop() - rac.getTop() - 120);
    }
    
    sh.setDefault(ep.SHRINK, raElixirPropertyValues.YES);
    //sh.defaultAltererSect().setHeight(sh.defaultAltererSect().getHeight() - 200);
    
    return sh;
//    sh.LabelNacinplacanja.defaultAlterer().setTop(sh.LabelNacinotpreme.defaultAlterer().getTop());
//    sh.TextNAZNACPL.defaultAlterer().setTop(sh.TextNAZNAC.defaultAlterer().getTop());
//    sh.removeModels(new raReportElement[] {sh.LabelNacinotpreme, sh.TextNAZNAC});

  }

  public raReportSection createSectionHeader1() {
    raIzlazSectionHeaderLines sh = new raIzlazSectionHeaderLines(this); // return new raIzlazSectionHeader(this);
    long rbrGain = sh.LabelSifra.defaultAlterer().getLeft() - sh.LabelRbr.defaultAlterer().getLeft();
    long kolGain = sh.LabelKolicina.defaultAlterer().getWidth() * 4 / 10;
        
    sh.defaultAltererSect().removeModel(sh.LabelRbr);
    sh.LabelSifra.defaultAlterer().setLeft(sh.LabelSifra.defaultAlterer().getLeft() - rbrGain);
    sh.LabelKolicina.defaultAlterer().setWidth(sh.LabelKolicina.defaultAlterer().getWidth() - kolGain);
    sh.LabelKolicina.defaultAlterer().setLeft(sh.LabelKolicina.defaultAlterer().getLeft() + kolGain);
    sh.LabelKolicina.defaultAlterer().setCaption("Kol");
    sh.LabelNaziv.defaultAlterer().setLeft(sh.LabelNaziv.defaultAlterer().getLeft() - rbrGain);
    sh.LabelNaziv.defaultAlterer().setWidth(sh.LabelNaziv.defaultAlterer().getWidth() + rbrGain + kolGain);
    
    long need = sh.LabelIznos.defaultAlterer().getLeft() - sh.LabelPor.defaultAlterer().getLeft();
    sh.LabelIznos.defaultAlterer().setWidth(sh.LabelIznos.defaultAlterer().getWidth() - need / 2);
    sh.LabelIznos.defaultAlterer().setLeft(sh.LabelIznos.defaultAlterer().getLeft() + need / 2);
    sh.LabelCijena.defaultAlterer().setWidth(sh.LabelCijena.defaultAlterer().getWidth() - need + need / 2);
    
    sh.Label1.defaultAlterer().setLeft(sh.Label1.defaultAlterer().getLeft() - need + need / 2);
    sh.Label1.defaultAlterer().setWidth(sh.Label1.defaultAlterer().getWidth() + need);
    sh.LabelPop.defaultAlterer().setLeft(sh.LabelPop.defaultAlterer().getLeft() - need + need / 2);
    
    raReportElement pnp = sh.copyToModify(sh.LabelPor);
    pnp.defaultAlterer().setCaption("Pnp");
    pnp.defaultAlterer().setLeft(pnp.defaultAlterer().getLeft() + need / 2);
    sh.LabelPor.defaultAlterer().setLeft(sh.LabelPor.defaultAlterer().getLeft() - need + need / 2);
    sh.LabelPor.defaultAlterer().setCaption("Pdv");
    
    return sh;
  }
  
  public raReportSection createSectionHeader2() {
    return new raIzlazGroupSectionHeader(this); // return new raIzlazSectionHeader(this);
  }

  public raReportSection createDetail() {
    raIzlazDetail sh = new raIzlazDetail(this);
    sh.TextRBR.defaultAlterer().setControlSource("RBRDUMMY");
    long rbrGain = sh.TextCART.defaultAlterer().getLeft() - sh.TextRBR.defaultAlterer().getLeft();
    long kolGain = sh.TextKOL.defaultAlterer().getWidth() * 4 / 10;
    
    sh.defaultAltererSect().removeModel(sh.TextRBR);
    sh.TextCART.defaultAlterer().setLeft(sh.TextCART.defaultAlterer().getLeft() - rbrGain);
    sh.TextKOL.defaultAlterer().setWidth(sh.TextKOL.defaultAlterer().getWidth() - kolGain);
    sh.TextKOL.defaultAlterer().setLeft(sh.TextKOL.defaultAlterer().getLeft() + kolGain);

    sh.TextNAZART.defaultAlterer().setLeft(sh.TextNAZART.defaultAlterer().getLeft() - rbrGain);
    sh.TextNAZART.defaultAlterer().setWidth(sh.TextNAZART.defaultAlterer().getWidth() + rbrGain + kolGain);
    
    long need = sh.TextINETO.defaultAlterer().getLeft() - sh.TextPor1p2p3Naz.defaultAlterer().getLeft();
    System.out.println("need:" + need);
    sh.TextINETO.defaultAlterer().setWidth(sh.TextINETO.defaultAlterer().getWidth() - need / 2);
    sh.TextINETO.defaultAlterer().setLeft(sh.TextINETO.defaultAlterer().getLeft() + need / 2);
    sh.TextFC.defaultAlterer().setWidth(sh.TextFC.defaultAlterer().getWidth() - need + need / 2);
    
    sh.TextUPRAB1.defaultAlterer().setLeft(sh.TextUPRAB1.defaultAlterer().getLeft() - need + need / 2);
    
    raReportElement pnp = sh.copyToModify(sh.TextPor1p2p3Naz);
    pnp.defaultAlterer().setControlSource("POSPor2");
    pnp.defaultAlterer().setLeft(pnp.defaultAlterer().getLeft() + need / 2);
    sh.TextPor1p2p3Naz.defaultAlterer().setLeft(sh.TextPor1p2p3Naz.defaultAlterer().getLeft() - need + need / 2);
    sh.TextPor1p2p3Naz.defaultAlterer().setControlSource("POSPor1");
    
    return sh;
  }

  public raReportSection createSectionFooter1() {
    return new raIzlazSectionFooterLines(this); // return new raIzlazSectionFooter(this);
  }

  public repRacGroupTemplate() {
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepRacGroup");
  }
}

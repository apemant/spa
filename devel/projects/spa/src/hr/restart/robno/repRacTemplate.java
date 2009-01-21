/****license*****************************************************************
**   file: repRacTemplate.java
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
import hr.restart.util.reports.raIzlazDetail;
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

public class repRacTemplate extends repIzlazOrigTemplate {

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
    
    
    long topNar = sh.LabelNarudzba.defaultAlterer().getTop();
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
    textSgetDatug.setTop(topNar);
    
    
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
    
    return sh;
//    sh.LabelNacinplacanja.defaultAlterer().setTop(sh.LabelNacinotpreme.defaultAlterer().getTop());
//    sh.TextNAZNACPL.defaultAlterer().setTop(sh.TextNAZNAC.defaultAlterer().getTop());
//    sh.removeModels(new raReportElement[] {sh.LabelNacinotpreme, sh.TextNAZNAC});

  }

  public raReportSection createSectionHeader1() {
    return new raIzlazSectionHeaderLines(this); // return new raIzlazSectionHeader(this);
  }

  public raReportSection createDetail() {
    return new raIzlazDetail(this);
  }

  public raReportSection createSectionFooter1() {
    return new raIzlazSectionFooterLines(this); // return new raIzlazSectionFooter(this);
  }

  public repRacTemplate() {
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepRac");
  }
}

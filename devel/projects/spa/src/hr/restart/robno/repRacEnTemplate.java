/****license*****************************************************************
**   file: repRacEnTemplate.java
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


public class repRacEnTemplate extends repIzlazOrigTemplate {

  public raReportSection createSectionHeader0() {

    raSectionHeader0TF sh = new raSectionHeader0TF(this);
    
    long topNar = sh.LabelNarudzba.defaultAlterer().getTop();
    long topIzDok = sh.LabelIzlazni_dokument.defaultAlterer().getTop();
    
    sh.defaultAltererSect().getView(sh.LabelNarudzba, sh.LabelR1).moveDown(topIzDok - topNar);
    
    raReportElement labelUgovor = sh.copyToModify(sh.LabelNarudzba);
    labelUgovor.setCaption("Contract no.");
    labelUgovor.setTop(topNar);
    
    raReportElement textCUG = sh.copyToModify(sh.TextBRNARIZ);
    textCUG.setControlSource("CUG");
    textCUG.setTop(topNar);
    
    raReportElement textSgetDatug = sh.copyToModify(sh.TextSgetDATNARIZ);
    textSgetDatug.setControlSource("SgetDATUG");
    textSgetDatug.setTop(topNar);
    

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
    
    sh.LabelR_A_C_U_N.defaultAlterer().setCaption("\nI N V O I C E");
    sh.LabelBroj.defaultAlterer().setCaption("No.");
    sh.LabelMjestoDatum.defaultAlterer().setCaption("Invoice date");
    sh.LabelDatum_isporuke.defaultAlterer().setCaption("Delivery date");
    sh.LabelDospijeceDatum.defaultAlterer().setCaption("Due date");
    sh.LabelNarudzba.defaultAlterer().setCaption("Order no.");
    sh.LabelIzlazni_dokument.defaultAlterer().setCaption("Quote no.");
    sh.LabelNacin_placanja.defaultAlterer().setCaption("Payment type");
    sh.LabelIsporuka.defaultAlterer().setCaption("Currency");
    sh.TextISPORUKA.defaultAlterer().setControlSource("CURRENCY");
    
    sh.TextMB.defaultAlterer().setControlSource("=(string-append \"VAT ID: \" [MBM])");
        
    return sh;
//    sh.LabelNacinplacanja.defaultAlterer().setTop(sh.LabelNacinotpreme.defaultAlterer().getTop());
//    sh.TextNAZNACPL.defaultAlterer().setTop(sh.TextNAZNAC.defaultAlterer().getTop());
//    sh.removeModels(new raReportElement[] {sh.LabelNacinotpreme, sh.TextNAZNAC});

  }

  public raReportSection createSectionHeader1() {
    raIzlazSectionHeaderLines sh = new raIzlazSectionHeaderLines(this); // return new raIzlazSectionHeader(this);
    
    sh.LabelRbr.defaultAlterer().setCaption("No.");
    sh.LabelSifra.defaultAlterer().setCaption("Item #");
    sh.LabelNaziv.defaultAlterer().setCaption("Description");
    sh.LabelKolicina.defaultAlterer().setCaption("Quantity");
    sh.LabelJmj.defaultAlterer().setCaption("UoM");
    sh.LabelCijena.defaultAlterer().setCaption("Unit price");
    sh.LabelPop.defaultAlterer().setCaption("Disc.");
    sh.LabelPor.defaultAlterer().setCaption("VAT");
    sh.LabelIznos.defaultAlterer().setCaption("Total");
    
    return sh;
  }

  public raReportSection createDetail() {
    return new raIzlazDetail(this);
  }

  public raReportSection createSectionFooter1() {
    raIzlazSectionFooterLines sh = new raIzlazSectionFooterLines(this); // return new raIzlazSectionFooter(this);
    
    long shrink = sh.LabelUkupno_porez.defaultAlterer().getTop() - sh.LabelUkupno_bez_poreza.defaultAlterer().getTop();
    
    sh.removeModels(new raReportElement[] {sh.LabelREKAPITULACIJA_POREZA, sh.LabelGrupa, sh.Label2, sh.LabelOsnovica, sh.LabelPorez, 
        sh.TextPorezDepartmentCPOR, sh.TextPorezDepartmentCrtica, sh.TextPorezDepartmentIPRODBP, 
        sh.TextPorezDepartmentUKUPOR, sh.TextPorezDepartmentPOR1,
        sh.LabelSlovima_, sh.TextSLOVIMA, sh.LabelUkupno_bez_poreza, sh.Text3});
    
    sh.LabelUkupno_porez.defaultAlterer().moveVert(-shrink);
    sh.LabelUkupno_s_porezom.defaultAlterer().moveVert(-shrink);
    sh.Text4.defaultAlterer().moveVert(-shrink);
    sh.Text5.defaultAlterer().moveVert(-shrink);
    sh.Line2.defaultAlterer().moveVert(-shrink);
    sh.Label1.defaultAlterer().alterHeight(-shrink);
    sh.defaultAlterer().setHeight(sh.defaultAlterer().getHeight() - shrink);
    
    sh.LabelUkupno.defaultAlterer().setCaption("Subtotal");
    sh.LabelPopust.defaultAlterer().setCaption("Discount");
    sh.LabelUkupno_porez.defaultAlterer().setCaption("VAT Amount");
    sh.LabelUkupno_s_porezom.defaultAlterer().setCaption("Invoice Total");
    
    return sh;
  }

  public repRacEnTemplate() {
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepRac");
  }
}

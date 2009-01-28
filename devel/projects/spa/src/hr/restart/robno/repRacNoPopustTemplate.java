/****license*****************************************************************
**   file: repRacNoPopustTemplate.java
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
import hr.restart.util.reports.raDetailNoPopust;
import hr.restart.util.reports.raIzlazSectionHeaderNoPopust;
import hr.restart.util.reports.raIzlazSection_Footer1NoPopust;
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

public class repRacNoPopustTemplate extends repIzlazOrigTemplate {

  public raReportSection createSectionHeader0() {

    raSectionHeader0TF sh = new raSectionHeader0TF(this);
//  zakaii::    sh.TextTEL.defaultAlterer().setVisible(false);
    
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
    
    sh.LabelR_A_C_U_N.defaultAlterer().setCaption(
        frmParam.getParam("robno", "racPNPTekst", "R A È U N", 
            "Naslov raèuna bez popusta"));
   
    if ("D".equalsIgnoreCase(frmParam.getParam("robno", "rnpTri", "N",
        "Raèun bez popusta u Tri-formatu"))) {
      sh.defaultAltererSect().removeModels(new raReportElement[]
         {sh.LabelDatum_isporuke, sh.TextSgetDVO, sh.LabelNarudzba,
          sh.TextBRNARIZ, sh.LabelIzlazni_dokument, sh.TextBRDOKIZ});
      sh.defaultAltererSect().getView(sh.LabelMjestoDatum, 
          sh.TextSgetDATDOK).moveDown(220);
      sh.defaultAltererSect().getView(sh.LabelNacin_placanja, 
          sh.TextNAZNACPL).moveUp(440);
      sh.LabelBroj.defaultAlterer().setCaption("RN");
      sh.LabelR_A_C_U_N.defaultAlterer().setVisible(false);
      sh.TextFormatBroj.defaultAlterer().setControlSource("FormatBrojTri");
    }
    return sh;
  }

//  public raReportSection createSectionHeader1() {
//    return new raIzlazSectionHeaderLines(this); // return new raIzlazSectionHeader(this);
//  }
//
//  public raReportSection createDetail() {
//    return new raIzlazDetail(this);
//  }
//
//  public raReportSection createSectionFooter1() {
//    return new raIzlazSectionFooterLines(this); // return new raIzlazSectionFooter(this);
//  }

  public raReportSection createSectionHeader1() {
    return new raIzlazSectionHeaderNoPopust(this);
  }

  public raReportSection createDetail() {
    return new raDetailNoPopust(this);
  }

  public raReportSection createSectionFooter1() {
    raIzlazSection_Footer1NoPopust sh = new raIzlazSection_Footer1NoPopust(this);
    
    if ("D".equalsIgnoreCase(frmParam.getParam("robno", "rnpTri", "N",
    "Raèun bez popusta u Tri-formatu"))) {
      
      sh.defaultAltererSect().removeModels(new raReportElement[] {
          sh.LabelREKAPITULACIJA_POREZA, sh.LabelGrupa, sh.Label2,
          sh.LabelOsnovica, sh.LabelPorez, sh.TextPorezDepartmentCPOR,
          sh.TextPorezDepartmentCrtica, sh.TextPorezDepartmentIPRODBP,
          sh.TextPorezDepartmentPOR1, sh.TextPorezDepartmentUKUPOR
      });
      
    }
    
    return sh;
  }

  public repRacNoPopustTemplate() {
    System.out.println("TEMPLATE CLASS!!!"); //XDEBUG delete when no more needed
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepRacNoPopust");
  }
}

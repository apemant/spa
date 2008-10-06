/****license*****************************************************************
**   file: repRacNoPopust2RedTemplate.java
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

import hr.restart.util.reports.raDetail2RedNoPopust;
import hr.restart.util.reports.raIzlazSectionHeader2RedNoPopust;
import hr.restart.util.reports.raIzlazSection_Footer12RedNoPopust;
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

public class repRacNoPopust2RedTemplate extends repIzlazOrigTemplate {

  public raReportSection createSectionHeader0() {

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
    
    return sh;
  }

  public raReportSection createSectionHeader1() {
    return new raIzlazSectionHeader2RedNoPopust(this);
  }

  public raReportSection createDetail() {
    return new raDetail2RedNoPopust(this);
  }

  public raReportSection createSectionFooter1() {
    return new raIzlazSection_Footer12RedNoPopust(this);
  }

  public repRacNoPopust2RedTemplate() {
    System.out.println("TEMPLATE CLASS!!!"); //XDEBUG delete when no more needed
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepRacNoPopust2Red");
  }
}

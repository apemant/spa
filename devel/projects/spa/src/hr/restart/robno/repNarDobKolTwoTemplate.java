/****license*****************************************************************
**   file: repNarDobKolTwoTemplate.java
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
import hr.restart.util.reports.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repNarDobKolTwoTemplate extends repIzlazOrigTemplate {
  public raReportSection createSectionHeader0() {
    raRPSectionHeaderROT sh = new raRPSectionHeaderROT(this);
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nNARUDŽBENICA");
    
    long old = sh.LabelMjestoDatum.defaultAlterer().getTop();

    sh.defaultAltererSect().getView(sh.LabelMjestoDatum, sh.TextNAZFRA).
        deleteElementsPushDown(sh, new raReportElement[]
        {sh.LabelDatum_isporuke, sh.LabelNacin_otpreme, sh.TextNAZNAC,
        sh.LabelNacin_placanja, sh.TextNAZNACPL});

    sh.LabelR1.defaultAlterer().setVisible(false);
    sh.LabelObrazac.defaultAlterer().setVisible(false);
    sh.LabelDospijeceDatum.defaultAlterer().setCaption("Rok izvršenja");
    
    sh.LabelParitet.defaultAlterer().setCaption("Mjesto troška");
    sh.TextNAZFRA.defaultAlterer().setControlSource("CSNS");
    
    sh.TextSgetDATDOK.defaultAlterer().setTop(sh.LabelMjestoDatum.defaultAlterer().getTop());
    
    sh.defaultAltererSect().getView(sh.LabelMjestoDatum, sh.TextISPORUKA).moveUp(sh.LabelMjestoDatum.defaultAlterer().getTop() - old);

    if (frmParam.getParam("robno", "narNatemelju", "D", 
        "Ispis linije 'na temelju' na narudžbi za dobavljaèa (D,N)?").equalsIgnoreCase("D"))
      sh.addModel(ep.TEXT, new String[] {
          "NATEM", "", "", "", "", "", "", "", "20",
          "2600", "10500", "220", "", "", "", "", "", "", "Lucida Bright", "8",
          "", "", "", "", ""
      });
    return sh;
  }
  public raReportSection createSectionFooter1() {
    raKCISectionFooter sh = new raKCISectionFooter(this);
    sh.removeModels(new raReportElement[] {sh.Line2, sh.Label1, sh.LabelU_K_U_P_N_O, sh.Text1});
    sh.Line1.defaultAlterer().setTop(100);
//    sh.Text1.defaultAlterer().setControlSource("=(dsum \"INABreal\")");
    return sh;
  }
  public raReportSection createDetail() {
    raDosDetail sh = new raDosDetail(this);
//    sh.TextZC.defaultAlterer().setControlSource("NC");
    //    sh.TextIRAZ.defaultAlterer().setControlSource("INAB");
    /*sh.removeModels(new raReportElement[] {sh.TextZC, sh.TextIRAZ});
    sh.TextKOL.defaultAlterer().setLeft(9440);
    sh.TextJM.defaultAlterer().setLeft(8940);
    sh.TextNAZART.defaultAlterer().setWidth(7120);*/
    return sh;
  }
  public raReportSection createSectionHeader1() {
    raDosSH1 sh = new raDosSH1(this);
    /*sh.removeModels(new raReportElement[] {sh.LabelCijena, sh.LabelIznos});
    sh.LabelKolicina.defaultAlterer().setLeft(9440);
    sh.LabelJm.defaultAlterer().setLeft(8940);
    sh.LabelNaziv_artikla.defaultAlterer().setWidth(7120);*/
//    sh.LabelSifra.setCaption("Šifra");
//    sh.resizeElement(sh.LabelSifra, 1400, sh.LabelNaziv_artikla);
    return sh;
  }
  
  public raReportSection createSectionFooter0() {
    raIzlazSectionFooterForCustom isf0 = new raIzlazSectionFooterForCustom(this);
    isf0.TextNAPOMENAOPIS.defaultAlterer().setControlSource("NAZNAP");
    return isf0;    
  }
}


/****license*****************************************************************
**   file: repMiniRacSloTemplate.java
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
import hr.restart.util.reports.raRPSectionHeaderROT;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raStandardReportFooter;
import hr.restart.util.reports.raStandardReportHeader;


public class repMiniRacSloTemplate extends repOTPvriOrigTemplate {

  public raReportSection createSectionHeader0() {
    raRPSectionHeaderROT sh = new raRPSectionHeaderROT(this);
    
    sh.defaultAlterer().setHeight(2800);
    sh.defaultAlterer().setProperty(ep.SHRINK, raElixirPropertyValues.NO);
    
    sh.removeModel(sh.TextSgetDDOSP);
    sh.LabelMjestoDatum.defaultAlterer().setTop(sh.LabelDatum_isporuke.defaultAlterer().getTop());
    sh.TextSgetDATDOK.defaultAlterer().setTop(sh.TextSgetDVO.defaultAlterer().getTop());
    sh.removeModels(new raReportElement[] {sh.LabelNarudzba, sh.TextBRNARIZ, sh.TextLogoMjestoZarez,
        sh.LabelDatum_isporuke, sh.TextSgetDVO, sh.LabelNacin_otpreme, sh.TextNAZNAC, sh.LabelIsporuka, sh.TextISPORUKA,
        sh.LabelNacin_placanja, sh.TextNAZNACPL, sh.LabelParitet, sh.TextNAZFRA, sh.LabelR1, sh.LabelObrazac});

    long shrink = 1460;
    sh.Label1.defaultAlterer().moveHor(shrink);
    sh.Label1.defaultAlterer().alterWidth(-shrink);
    sh.LabelRACUNOTPREMNICA.defaultAlterer().moveHor(shrink);
    sh.LabelRACUNOTPREMNICA.defaultAlterer().alterWidth(-shrink);
    sh.TextFormatBroj.defaultAlterer().moveHor(shrink);
    sh.TextFormatBroj.defaultAlterer().alterWidth(-shrink);
    sh.Line2.defaultAlterer().moveHor(shrink);
    sh.Line3.defaultAlterer().moveHor(shrink);
    sh.Line3.defaultAlterer().alterWidth(-shrink);
    sh.Line4.defaultAlterer().moveHor(shrink);
    sh.Line4.defaultAlterer().alterWidth(-shrink);
    sh.LabelBroj.defaultAlterer().moveHor(shrink);
    sh.LabelMjestoDatum.defaultAlterer().moveHor(shrink);
    sh.LabelDospijeceDatum.defaultAlterer().moveHor(shrink);
    
    sh.TextMJ.defaultAlterer().setControlSource("MJZ");
    sh.TextMJ.defaultAlterer().setHeight(480);
    
    sh.TextMB.defaultAlterer().setControlSource("=(string-append \"ID za DDV kupca  \" [MBM])");
    
    sh.LabelRACUNOTPREMNICA.defaultAlterer().setCaption("\nR A È U N");
    sh.LabelBroj.defaultAlterer().setCaption("Št.");
    sh.LabelMjestoDatum.defaultAlterer().setCaption("Datum izdaje");
    sh.LabelDospijeceDatum.defaultAlterer().setCaption("Rok plaèila");
    
    return sh;
  }
  
  public raReportSection createSectionHeader1() {
    raReportSection sh = super.createSectionHeader1();
    
    LabelNaziv_artikla.defaultAlterer().setLeft(LabelRbr.defaultAlterer().getLeft());    
    LabelNaziv_artikla.defaultAlterer().setWidth(LabelKolicina.defaultAlterer().getLeft() -
        LabelNaziv_artikla.defaultAlterer().getLeft() - 20);
    
    sh.removeModel(LabelRbr);
    sh.removeModel(LabelSifra);
    sh.removeModel(LabelJmj);
    
    LabelNaziv_artikla.defaultAlterer().setCaption("Opis");
    //LabelNaziv_artikla.defaultAlterer().setTextAlign(raElixirPropertyValues.LEFT);
    LabelKolicina.defaultAlterer().setCaption("Kolièina");
    //LabelKolicina.defaultAlterer().setTextAlign(raElixirPropertyValues.RIGHT);
    LabelCijena.defaultAlterer().setCaption("Cena (€)");
    //LabelCijena.defaultAlterer().setTextAlign(raElixirPropertyValues.RIGHT);
    LabelVrijednost.defaultAlterer().setCaption("Znesek (€)");
    //LabelVrijednost.defaultAlterer().setTextAlign(raElixirPropertyValues.RIGHT);
    
    return sh;
  }
  
  public raReportSection createDetail() {
    raReportSection sh = super.createDetail();

    TextNAZART.defaultAlterer().setLeft(TextRBR.defaultAlterer().getLeft());
    TextNAZART.defaultAlterer().setWidth(TextKOL.defaultAlterer().getLeft() -
        TextNAZART.defaultAlterer().getLeft());
    
    sh.removeModel(TextRBR);
    sh.removeModel(TextCART);
    sh.removeModel(TextJM);
    
    TextZC.defaultAlterer().setControlSource("FMC");
    TextIRAZ.defaultAlterer().setControlSource("IPRODSP");
        
    return sh;
  }
  
  public raReportSection createSectionFooter0() {
    raReportSection sh = super.createSectionFooter0();
    
    sh.defaultAlterer().setHeight(740);
    
    LabelU_K_U_P_N_O.defaultAlterer().setCaption("Skupaj");
    Text2.defaultAlterer().setControlSource("=(dsum \"IPRODSP\")");
    TextNAPOMENAOPIS.defaultAlterer().setHeight(0);
    
    return sh;
  }
  
  public raReportSection createPageHeader() {
    return new raStandardReportHeader(this);
  }

  public raReportSection createPageFooter() {
    return new raStandardReportFooter(this);
  }


}

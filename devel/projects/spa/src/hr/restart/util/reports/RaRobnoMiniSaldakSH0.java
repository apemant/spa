/****license*****************************************************************
**   file: RaRobnoMiniSaldakSH0.java
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
package hr.restart.util.reports;

import hr.restart.sk.raIspisKartica;
import hr.restart.sk.repIOSTemplate;

public class RaRobnoMiniSaldakSH0 extends raReportSection {

  private String[] thisProps = new String[] {"FirstLine", "Before", "", "", "Yes", "No", "", "", 
     "15320"};
  public raReportElement LabelVjerovnik;
  private String[] LabelVjerovnikProps = new String[] {"Vjerovnik", "", "5900", "700", "1400", 
     "340", "", "", "", "", "", "", "Lucida Bright", "12", "", "", "", ""};
  public raReportElement LabelDuznik;
  private String[] LabelDuznikProps = new String[] {"Dužnik", "", "400", "700", "1400", "340", "", 
     "", "", "", "", "", "Lucida Bright", "12", "", "", "", ""};
  public raReportElement Rectangle1;
  private String[] Rectangle1Props = new String[] {"", "540", "1220", "4160", "2200", "", "", "", 
     "White", ""};
  public raReportElement Rectangle2;
  private String[] Rectangle2Props = new String[] {"", "200", "1240", "4840", "2160", "Transparent", 
     "", "", "", ""};
  public raReportElement TextNAZPARL;
  private String[] TextNAZPARLProps = new String[] {"NAZPARL", "", "", "", "", "", "Yes", "Yes", 
     "420", "1420", "4360", "840", "", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", "", 
     ""};
  public raReportElement TextEnterFirstLine;
  private String[] TextEnterFirstLineProps = new String[] {"EnterFirstLine", "", "", "", "", "", 
     "Yes", "Yes", "5900", "1420", "4360", "840", "", "", "", "", "", "", "Lucida Bright", "", 
     "Bold", "", "", "", ""};
  public raReportElement Rectangle3;
  private String[] Rectangle3Props = new String[] {"", "180", "1580", "4880", "1500", "", "", "", 
     "White", ""};
  public raReportElement TextAdresaPARTNERA;
  private String[] TextAdresaPARTNERAProps = new String[] {"AdresaPARTNERA", "", "", "", "", "", "", 
     "", "420", "2340", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", 
     ""};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "", 
     "5900", "2340", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", 
     ""};
  public raReportElement TextMjestoIpbrPARTNERA;
  private String[] TextMjestoIpbrPARTNERAProps = new String[] {"MjestoIpbrPARTNERA", "", "", "", "", 
     "", "", "", "420", "2600", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", 
     "", "", ""};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", 
     "5900", "2600", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", 
     ""};
  public raReportElement TextMBPAR;
  private String[] TextMBPARProps = new String[] {"MBPAR", "", "", "", "", "", "", "", "440", 
     "3120", "2240", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextCPAR;
  private String[] TextCPARProps = new String[] {"CPAR", "", "", "", "", "", "", "", "4160", "3120", 
     "660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "4100", "10740", "0", "Dashes", 
     "Light Gray", ""};
  public raReportElement LabelPREDMET;
  private String[] LabelPREDMETProps = new String[] {"PREDMET", "", "480", "4340", "1360", "260", 
     "", "", "", "", "", "", "Lucida Bright", "", "", "", "", ""};
  public raReportElement LabelIZVADAK;
  private String[] LabelIZVADAKProps = new String[] {"\nIZVADAK", "", "", "4360", "10740", "600", 
     "", "", "", "", "", "", "Lucida Bright", "12", "Bold", "", "", "Center"};
  public raReportElement TextPremaEvidenciji;
  private String[] TextPremaEvidencijiProps = new String[] {"PremaEvidenciji", "", "", "", "", "", 
     "Yes", "", "220", "5460", "10200", "600", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", 
     "", ""};
  public raReportElement TextPokazniSaldo;
  private String[] TextPokazniSaldoProps = new String[] {"PokazniSaldo", "", "", "", "", "", "", "", 
     "220", "5980", "10200", "500", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", "", ""};
  public raReportElement LabelMOLIMO_VAS_DA_NAM;
  private String[] LabelMOLIMO_VAS_DA_NAMProps = new String[] {
     "\nMOLIMO VAS DA NAM POTVRDITE ISPRAVNOST SALDA NA PRILOŽENOJ POTVRDI. AKO ISKAZANI SALDO NIJE SUGLASAN SA STANJEM PREMA NAŠOJ EVIDENCIJI, IZVOLITE NAM POSLATI STANJE NAŠEG RAÈUNA", 
     "", "220", "6480", "10200", "1300", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", 
     ""};
  public raReportElement LabelPosiljalac_izvatka;
  private String[] LabelPosiljalac_izvatkaProps = new String[] {"Pošiljalac izvatka", "", "6880", 
     "7940", "3480", "340", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", "Center"};
  public raReportElement LabelMP;
  private String[] LabelMPProps = new String[] {"\nM.P.", "", "", "8520", "10740", "600", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "6880", "8820", "3480", "0", "", "Light Gray", 
     ""};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "6880", "9420", "3480", "0", "", 
     "Light Gray", ""};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "", "9720", "10740", "0", "Dashes", 
     "Light Gray", ""};
  public raReportElement LabelPOTVRDA;
  private String[] LabelPOTVRDAProps = new String[] {"\nPOTVRDA", "", "", "9820", "10740", "600", 
     "", "", "", "", "", "", "Lucida Bright", "12", "Bold", "", "", "Center"};
  
  public raReportElement Label12;
  private String[] Label12Props = new String[] {
     "Dužnik priznaje svoj dug prema vjerovniku po osnovama i u visini kako je to naznaèeno u ovoj ispravi i otvorenim stavkama, koje se nalaze u prilogu ove isprave i èine njezin sastavni dio.", 
     "", "220", "10600", "10200", "560", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", 
     ""};
  
  public raReportElement LabelA;
  private String[] LabelAProps = new String[] {"(A)", "", "500", "11400", "360", "260", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", ""};
  /*public raReportElement LabelPOTVRDjUJEMO_SUGLASNOST_SALDA;
  private String[] LabelPOTVRDjUJEMO_SUGLASNOST_SALDAProps = new String[] {
     "Potvrðujemo suglasnost duga", "", "860", "11000", "4020", "560", "", "", "", "", "", 
     "", "Lucida Bright", "", "", "", "", ""};*/
  public raReportElement TextSuglasniSaldo;
  private String[] TextSuglasniSaldoProps = new String[] {"SuglasniSaldo", "", "", "", "", "", 
     "Yes", "Yes", "860", "11400", "9280", "260", "", "", "", "", "", "", "Lucida Bright", "", "", 
     "", "", "", ""};
  //public raReportElement Line5;
  //private String[] Line5Props = new String[] {"", "No", "5840", "10920", "4780", "0", "", "", ""};
  public raReportElement LabelB;
  private String[] LabelBProps = new String[] {"(B)", "", "500", "11700", "360", "260", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", ""};
  public raReportElement LabelSTANJE_VASEG_RACUNA_KOD;
  private String[] LabelSTANJE_VASEG_RACUNA_KODProps = new String[] {
     "Stanje vašeg raèuna kod nas", "", "860", "11700", "4020", "260", "", "", "", "", "", 
     "", "Lucida Bright", "", "", "", "", ""};
  public raReportElement Rectangle4;
  private String[] Rectangle4Props = new String[] {"No", "6120", "12480", "4580", "400", "", "", "", 
     "", ""};
  public raReportElement Rectangle5;
  private String[] Rectangle5Props = new String[] {"", "540", "12740", "4160", "2200", "", "", "", 
     "White", ""};
  public raReportElement Rectangle6;
  private String[] Rectangle6Props = new String[] {"", "200", "12760", "4840", "2160", 
     "Transparent", "", "", "", ""};
  public raReportElement Line6;
  private String[] Line6Props = new String[] {"", "", "3560", "11960", "2000", "0", "", 
     "Light Gray", ""};
  public raReportElement TextEnterFirstLine1;
  private String[] TextEnterFirstLine1Props = new String[] {"EnterFirstLine", "", "", "", "", "", 
     "Yes", "Yes", "420", "12960", "4360", "840", "", "", "", "", "", "", "Lucida Bright", "", 
     "Bold", "", "", "", ""};
  public raReportElement Rectangle7;
  private String[] Rectangle7Props = new String[] {"", "180", "13100", "4880", "1500", "", "", "", 
     "White", ""};
  public raReportElement Line7;
  private String[] Line7Props = new String[] {"", "No", "5840", "13300", "4780", "0", "", "", ""};
  public raReportElement LabelDatum;
  private String[] LabelDatumProps = new String[] {"Datum", "", "7100", "13380", "940", "260", "", 
     "", "", "", "", "", "Lucida Bright", "", "", "", "", ""};
  public raReportElement Line8;
  private String[] Line8Props = new String[] {"", "", "8140", "13600", "1580", "0", "", 
     "Light Gray", ""};
  public raReportElement LabelMP1;
  private String[] LabelMP1Props = new String[] {"\nM.P.", "", "8120", "13820", "580", "600", "", 
     "", "", "", "", "", "Lucida Bright", "", "", "", "", ""};
  public raReportElement TextSecondLine1;
  private String[] TextSecondLine1Props = new String[] {"SecondLine", "", "", "", "", "", "", "", 
     "420", "13860", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", 
     ""};
  public raReportElement TextThirdLine1;
  private String[] TextThirdLine1Props = new String[] {"ThirdLine", "", "", "", "", "", "", "", 
     "420", "14120", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", 
     ""};
  public raReportElement Line9;
  private String[] Line9Props = new String[] {"", "", "7100", "14860", "3480", "0", "", 
     "Light Gray", ""};
  public raReportElement LabelPotpis;
  private String[] LabelPotpisProps = new String[] {"(Potpis)", "", "7100", "14900", "3480", "320", 
     "", "", "", "", "", "", "Lucida Bright", "", "", "", "", "Center"};

  public RaRobnoMiniSaldakSH0(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_HEADER + 0));
    this.setDefaults(thisProps);

    addElements();

    if (owner instanceof repIOSTemplate)
      addReportModifier(new ReportModifier() {
        public void modify() {
          modifyThis();
        }
      });
  }

  private void addElements() {
    Rectangle2 = addModel(ep.RECTANGLE, Rectangle2Props);
    Rectangle1 = addModel(ep.RECTANGLE, Rectangle1Props);
    Rectangle3 = addModel(ep.RECTANGLE, Rectangle3Props);
    
    Rectangle6 = addModel(ep.RECTANGLE, Rectangle6Props);
    Rectangle5 = addModel(ep.RECTANGLE, Rectangle5Props);
    Rectangle7 = addModel(ep.RECTANGLE, Rectangle7Props);
    
    LabelVjerovnik = addModel(ep.LABEL, LabelVjerovnikProps);
    LabelDuznik = addModel(ep.LABEL, LabelDuznikProps);
    TextNAZPARL = addModel(ep.TEXT, TextNAZPARLProps);
    TextEnterFirstLine = addModel(ep.TEXT, TextEnterFirstLineProps);
    TextAdresaPARTNERA = addModel(ep.TEXT, TextAdresaPARTNERAProps);
    TextSecondLine = addModel(ep.TEXT, TextSecondLineProps);
    TextMjestoIpbrPARTNERA = addModel(ep.TEXT, TextMjestoIpbrPARTNERAProps);
    TextThirdLine = addModel(ep.TEXT, TextThirdLineProps);
    TextMBPAR = addModel(ep.TEXT, TextMBPARProps);
    TextCPAR = addModel(ep.TEXT, TextCPARProps);
    Line1 = addModel(ep.LINE, Line1Props);
    LabelPREDMET = addModel(ep.LABEL, LabelPREDMETProps);
    LabelIZVADAK = addModel(ep.LABEL, LabelIZVADAKProps);
    TextPremaEvidenciji = addModel(ep.TEXT, TextPremaEvidencijiProps);
    TextPokazniSaldo = addModel(ep.TEXT, TextPokazniSaldoProps);
    LabelMOLIMO_VAS_DA_NAM = addModel(ep.LABEL, LabelMOLIMO_VAS_DA_NAMProps);
    LabelPosiljalac_izvatka = addModel(ep.LABEL, LabelPosiljalac_izvatkaProps);
    LabelMP = addModel(ep.LABEL, LabelMPProps);
    Line2 = addModel(ep.LINE, Line2Props);
    Line3 = addModel(ep.LINE, Line3Props);
    Line4 = addModel(ep.LINE, Line4Props);
    LabelPOTVRDA = addModel(ep.LABEL, LabelPOTVRDAProps);
    Label12 = addModel(ep.LABEL, Label12Props);
    LabelA = addModel(ep.LABEL, LabelAProps);
    //LabelPOTVRDjUJEMO_SUGLASNOST_SALDA = addModel(ep.LABEL, LabelPOTVRDjUJEMO_SUGLASNOST_SALDAProps);
    TextSuglasniSaldo = addModel(ep.TEXT, TextSuglasniSaldoProps);
    //Line5 = addModel(ep.LINE, Line5Props);
    LabelB = addModel(ep.LABEL, LabelBProps);
    LabelSTANJE_VASEG_RACUNA_KOD = addModel(ep.LABEL, LabelSTANJE_VASEG_RACUNA_KODProps);
    Rectangle4 = addModel(ep.RECTANGLE, Rectangle4Props);
    Line6 = addModel(ep.LINE, Line6Props);
    TextEnterFirstLine1 = addModel(ep.TEXT, TextEnterFirstLine1Props);
    Line7 = addModel(ep.LINE, Line7Props);
    LabelDatum = addModel(ep.LABEL, LabelDatumProps);
    Line8 = addModel(ep.LINE, Line8Props);
    LabelMP1 = addModel(ep.LABEL, LabelMP1Props);
    TextSecondLine1 = addModel(ep.TEXT, TextSecondLine1Props);
    TextThirdLine1 = addModel(ep.TEXT, TextThirdLine1Props);
    Line9 = addModel(ep.LINE, Line9Props);
    LabelPotpis = addModel(ep.LABEL, LabelPotpisProps);
  }

  private void modifyThis() {
    if (raIspisKartica.getInstance(raIspisKartica.IOS).isKupac()) {
      LabelVjerovnik.setCaption("Vjerovnik");
      LabelDuznik.setCaption("Dužnik");
    } else {
      LabelVjerovnik.setCaption("Dužnik");
      LabelDuznik.setCaption("Vjerovnik");
    }
  }
}




/*package hr.restart.util.reports;

public class RaRobnoMiniSaldakSH0 extends raReportSection {

  private String[] thisProps = new String[] {"FirstLine", "Before", "", "", "Yes", "No", "", "", 
     "15320"};
  public raReportElement LabelDuznik;
  private String[] LabelDuznikProps = new String[] {"Dužnik", "", "400", "1700", "1400", "340", "", 
     "", "", "", "", "", "Lucida Bright", "12", "", "", "", ""};
  public raReportElement LabelVjerovnik;
  private String[] LabelVjerovnikProps = new String[] {"Vjerovnik", "", "5900", "1700", "1400", 
     "340", "", "", "", "", "", "", "Lucida Bright", "12", "", "", "", ""};
  public raReportElement Rectangle1;
  private String[] Rectangle1Props = new String[] {"", "540", "2220", "4160", "2180", "", "", "", 
     "White", ""};
  public raReportElement Rectangle2;
  private String[] Rectangle2Props = new String[] {"", "200", "2240", "4840", "2160", "Transparent", 
     "", "", "", ""};
  public raReportElement TextEnterFirstLine;
  private String[] TextEnterFirstLineProps = new String[] {"EnterFirstLine", "", "", "", "", "", 
     "Yes", "Yes", "5900", "2420", "4360", "840", "", "", "", "", "", "", "Lucida Bright", "", 
     "Bold", "", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"NAZPARL", "", "", "", "", 
     "", "Yes", "Yes", "420", "2420", "4360", "840", "", "", "", "", "", "", "Lucida Bright", "", 
     "Bold", "", "", "", ""};
  public raReportElement Rectangle3;
  private String[] Rectangle3Props = new String[] {"", "180", "2580", "4860", "1500", "", "", "", 
     "White", ""};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "", 
     "5900", "3340", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", 
     ""};
  public raReportElement TextAdresaPARTNERA;
  private String[] TextAdresaPARTNERAProps = new String[] {"AdresaPARTNERA", "", "", "", "", "", "", 
     "", "420", "3340", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", 
     ""};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", 
     "5900", "3600", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", 
     ""};
  public raReportElement TextMjestoIpbrPARTNERA;
  private String[] TextMjestoIpbrPARTNERAProps = new String[] {"MjestoIpbrPARTNERA", "", "", "", "", 
     "", "", "", "420", "3600", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", 
     "", "", ""};
  public raReportElement TextCPAR;
  private String[] TextCPARProps = new String[] {"CPAR", "", "", "", "", "", "", "", "4160", "4120", 
     "660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextMBPAR;
  private String[] TextMBPARProps = new String[] {"MBPAR", "", "", "", "", "", "", "", "440", 
     "4120", "2240", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "5100", "10740", "0", "Dashes", 
     "Light Gray", ""};
  public raReportElement LabelPREDMET;
  private String[] LabelPREDMETProps = new String[] {"PREDMET", "", "480", "5340", "1360", "260", 
     "", "", "", "", "", "", "Lucida Bright", "", "", "", "", ""};
  public raReportElement LabelIZVADAK;
  private String[] LabelIZVADAKProps = new String[] {"\nIZVADAK", "", "", "5360", "10740", "600", 
     "", "", "", "", "", "", "Lucida Bright", "12", "Bold", "", "", "Center"};
  public raReportElement TextPremaEvidenciji;
  private String[] TextPremaEvidencijiProps = new String[] {"PremaEvidenciji", "", "", "", "", "", 
     "", "", "220", "6460", "10200", "600", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", 
     "", ""};
  public raReportElement TextPokazniSaldo;
  private String[] TextPokazniSaldoProps = new String[] {"PokazniSaldo", "", "", "", "", "", "", "", 
     "220", "6980", "10200", "500", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", "", ""};
  public raReportElement LabelMOLIMO_VAS_DA_NAM;
  private String[] LabelMOLIMO_VAS_DA_NAMProps = new String[] {
     "\nMOLIMO VAS DA NAM POTVRDITE ISPRAVNOST SALDA NA PRILOŽENOJ POTVRDI. AKO ISKAZANI SALDO NIJE SUGLASAN SA STANJEM PREMA VAŠOJ EVIDENCIJI IZVOLITE NAM POSLATI STANJE NAŠEG RA\u010CUNA", 
     "", "220", "7480", "10200", "1200", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", 
     ""};
  public raReportElement LabelPosiljalac_izvatka;
  private String[] LabelPosiljalac_izvatkaProps = new String[] {"Pošiljalac izvatka", "", "6880", 
     "8940", "3480", "340", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", "Center"};
  public raReportElement LabelMP;
  private String[] LabelMPProps = new String[] {"\nM.P.", "", "", "9520", "10740", "600", "", "", 
     "", "", "", "", "Lucida Bright", "", "", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "6880", "9820", "3480", "0", "", "Light Gray", 
     ""};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "6880", "10420", "3480", "0", "", 
     "Light Gray", ""};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "", "10720", "10740", "0", "Dashes", 
     "Light Gray", ""};
  public raReportElement LabelPOTVRDA;
  private String[] LabelPOTVRDAProps = new String[] {"\nPOTVRDA", "", "", "10820", "10740", "600", 
     "", "", "", "", "", "", "Lucida Bright", "12", "Bold", "", "", "Center"};
  public raReportElement LabelA_POTVRDjUJEMO_SUGLASNOST_SALDA;
  private String[] LabelA_POTVRDjUJEMO_SUGLASNOST_SALDAProps = new String[] {
     "\n(A) POTVR\u0110UJEMO SUGLASNOST SALDA\n\n\n(B) STANJE VAŠEG RA\u010CUNA KOD NAS", "", 
     "6160", "11400", "4580", "980", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", ""};
  public raReportElement Line5;
  private String[] Line5Props = new String[] {"", "", "6360", "12700", "4360", "0", "", 
     "Light Gray", ""};
  public raReportElement Rectangle4;
  private String[] Rectangle4Props = new String[] {"", "540", "12740", "4160", "2180", "", "", "", 
     "White", ""};
  public raReportElement Rectangle5;
  private String[] Rectangle5Props = new String[] {"", "200", "12760", "4840", "2160", 
     "Transparent", "", "", "", ""};
  public raReportElement LabelDatum;
  private String[] LabelDatumProps = new String[] {"Datum", "", "7200", "12880", "940", "260", "", 
     "", "", "", "", "", "Lucida Bright", "", "", "", "", ""};
  public raReportElement TextEnterFirstLine1;
  private String[] TextEnterFirstLine1Props = new String[] {"EnterFirstLine", "", "", "", "", "", 
     "Yes", "Yes", "420", "12960", "4360", "840", "", "", "", "", "", "", "Lucida Bright", "", 
     "Bold", "", "", "", ""};
  public raReportElement Line6;
  private String[] Line6Props = new String[] {"", "", "8240", "13100", "1580", "0", "", 
     "Light Gray", ""};
  public raReportElement Rectangle6;
  private String[] Rectangle6Props = new String[] {"", "180", "13100", "4860", "1500", "", "", "", 
     "White", ""};
  public raReportElement LabelMP1;
  private String[] LabelMP1Props = new String[] {"\nM.P.", "", "8220", "13320", "580", "600", "", 
     "", "", "", "", "", "Lucida Bright", "", "", "", "", ""};
  public raReportElement TextSecondLine1;
  private String[] TextSecondLine1Props = new String[] {"SecondLine", "", "", "", "", "", "", "", 
     "420", "13860", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", 
     ""};
  public raReportElement TextThirdLine1;
  private String[] TextThirdLine1Props = new String[] {"ThirdLine", "", "", "", "", "", "", "", 
     "420", "14120", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", 
     ""};
  public raReportElement Line7;
  private String[] Line7Props = new String[] {"", "", "7200", "14360", "3480", "0", "", 
     "Light Gray", ""};
  public raReportElement LabelPotpis;
  private String[] LabelPotpisProps = new String[] {"(Potpis)", "", "7200", "14400", "3480", "320", 
     "", "", "", "", "", "", "Lucida Bright", "", "", "", "", "Center"};

  public RaRobnoMiniSaldakSH0(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_HEADER + 0));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  private void addElements() {

    Rectangle2 = addModel(ep.RECTANGLE, Rectangle2Props);
    Rectangle1 = addModel(ep.RECTANGLE, Rectangle1Props);
    Rectangle3 = addModel(ep.RECTANGLE, Rectangle3Props);

    Rectangle5 = addModel(ep.RECTANGLE, Rectangle5Props);
    Rectangle4 = addModel(ep.RECTANGLE, Rectangle4Props);
    Rectangle6 = addModel(ep.RECTANGLE, Rectangle6Props);
    
    LabelDuznik = addModel(ep.LABEL, LabelDuznikProps);
    LabelVjerovnik = addModel(ep.LABEL, LabelVjerovnikProps);
//    Rectangle1 = addModel(ep.RECTANGLE, Rectangle1Props);
//    Rectangle2 = addModel(ep.RECTANGLE, Rectangle2Props);
    TextEnterFirstLine = addModel(ep.TEXT, TextEnterFirstLineProps);
    Text1 = addModel(ep.TEXT, Text1Props);
//    Rectangle3 = addModel(ep.RECTANGLE, Rectangle3Props);
    TextSecondLine = addModel(ep.TEXT, TextSecondLineProps);
    TextAdresaPARTNERA = addModel(ep.TEXT, TextAdresaPARTNERAProps);
    TextThirdLine = addModel(ep.TEXT, TextThirdLineProps);
    TextMjestoIpbrPARTNERA = addModel(ep.TEXT, TextMjestoIpbrPARTNERAProps);
    TextCPAR = addModel(ep.TEXT, TextCPARProps);
    TextMBPAR = addModel(ep.TEXT, TextMBPARProps);
    Line1 = addModel(ep.LINE, Line1Props);
    LabelPREDMET = addModel(ep.LABEL, LabelPREDMETProps);
    LabelIZVADAK = addModel(ep.LABEL, LabelIZVADAKProps);
    TextPremaEvidenciji = addModel(ep.TEXT, TextPremaEvidencijiProps);
    TextPokazniSaldo = addModel(ep.TEXT, TextPokazniSaldoProps);
    LabelMOLIMO_VAS_DA_NAM = addModel(ep.LABEL, LabelMOLIMO_VAS_DA_NAMProps);
    LabelPosiljalac_izvatka = addModel(ep.LABEL, LabelPosiljalac_izvatkaProps);
    LabelMP = addModel(ep.LABEL, LabelMPProps);
    Line2 = addModel(ep.LINE, Line2Props);
    Line3 = addModel(ep.LINE, Line3Props);
    Line4 = addModel(ep.LINE, Line4Props);
    LabelPOTVRDA = addModel(ep.LABEL, LabelPOTVRDAProps);
    LabelA_POTVRDjUJEMO_SUGLASNOST_SALDA = addModel(ep.LABEL, LabelA_POTVRDjUJEMO_SUGLASNOST_SALDAProps);
    Line5 = addModel(ep.LINE, Line5Props);
//    Rectangle4 = addModel(ep.RECTANGLE, Rectangle4Props);
//    Rectangle5 = addModel(ep.RECTANGLE, Rectangle5Props);
    LabelDatum = addModel(ep.LABEL, LabelDatumProps);
    TextEnterFirstLine1 = addModel(ep.TEXT, TextEnterFirstLine1Props);
    Line6 = addModel(ep.LINE, Line6Props);
//    Rectangle6 = addModel(ep.RECTANGLE, Rectangle6Props);
    LabelMP1 = addModel(ep.LABEL, LabelMP1Props);
    TextSecondLine1 = addModel(ep.TEXT, TextSecondLine1Props);
    TextThirdLine1 = addModel(ep.TEXT, TextThirdLine1Props);
    Line7 = addModel(ep.LINE, Line7Props);
    LabelPotpis = addModel(ep.LABEL, LabelPotpisProps);
  }

  private void modifyThis() {
    if ("L".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno", "ispProzor"))) 
      restoreDefaults();
    else {
      raReportSection left = getView(LabelDuznik, Rectangle2);
      raReportSection rigt = getView(LabelVjerovnik, TextThirdLine);
      left.moveRightCm(9.5);
      rigt.moveLeftCm(9.5);
    }
  }
}
*/
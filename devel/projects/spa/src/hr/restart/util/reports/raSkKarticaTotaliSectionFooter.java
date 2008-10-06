/****license*****************************************************************
**   file: raSkKarticaTotaliSectionFooter.java
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

public class raSkKarticaTotaliSectionFooter extends raReportSection {

  private String[] thisProps = new String[] {"Partner", "", "", "", "Yes", "No", "", "2340"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "3280", "120", "9120", "0", "", "-11184811", // "-11184811" was "Gray"
     ""};
  public raReportElement LabelSaldo;
  private String[] LabelSaldoProps = new String[] {"Saldo", "", "10660", "180", "1760", "240", "",
     "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement LabelTOTAL;
  private String[] LabelTOTALProps = new String[] {"TOTAL", "", "3280", "180", "960", "240", "", "",
     "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement LabelUPLATE;
  private String[] LabelUPLATEProps = new String[] {"UPLATE", "", "8820", "180", "1740", "240", "",
     "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement LabelRACUNI;
  private String[] LabelRACUNIProps = new String[] {"RA\u010CUNI", "", "4960", "180", "1740", "240",
     "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"-->", "", "10880", "180", "480", "240", "", "", "",
     "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"-->", "", "7040", "180", "480", "240", "", "", "",
     "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement LabelSaldo1;
  private String[] LabelSaldo1Props = new String[] {"Saldo", "", "6800", "180", "1760", "240", "",
     "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "3280", "440", "9120", "0", "", "-11184811", // "-11184811" was "Gray",
     ""};
  public raReportElement TextTotRac;
  private String[] TextTotRacProps = new String[] {"TotRac", "", "", "Number|true|#.##0,00", "", "",
     "", "", "4960", "500", "1760", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "",
     "Right", ""};
  public raReportElement LabelIZNOS;
  private String[] LabelIZNOSProps = new String[] {"IZNOS", "", "3280", "500", "960", "240", "", "",
     "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement TextSaldoUpl;
  private String[] TextSaldoUplProps = new String[] {"SaldoUpl", "", "", "Number|true|#.##0,00", "",
     "", "", "", "10660", "500", "1760", "240", "", "", "", "", "", "", "Lucida Bright", "9", "",
     "", "", "Right", ""};
  public raReportElement TextSaldoRac;
  private String[] TextSaldoRacProps = new String[] {"SaldoRac", "", "", "Number|true|#.##0,00", "",
     "", "", "", "6800", "500", "1760", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "",
     "", "Right", ""};
  public raReportElement TextTotUpl;
  private String[] TextTotUplProps = new String[] {"TotUpl", "", "", "Number|true|#.##0,00", "", "",
     "", "", "8820", "500", "1760", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "",
     "Right", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {
     "=(if (or (> [TotKO] 0.) (< [TotKO] 0.)) [SaldoKO] \"\")", "", "", "Number|true|#.##0,00",
     "", "", "", "", "6800", "780", "1760", "240", "", "", "", "", "", "", "Lucida Bright", "9", "",
     "", "", "Right", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {
     "=(if (or (> [TotKO] 0.) (< [TotKO] 0.)) [TotKO] \"\")", "", "", "Number|true|#.##0,00", "",
     "", "", "", "4960", "780", "1760", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "",
     "", "Right", ""};
  public raReportElement Text7;
  private String[] Text7Props = new String[] {
     "=(if (or (> [TotKOU] 0.) (< [TotKOU] 0.)) [SaldoKOU] \"\")", "", "", "Number|true|#.##0,00",
     "", "", "", "", "10660", "780", "1760", "240", "", "", "", "", "", "", "Lucida Bright", "9", "",
     "", "", "Right", ""};
  public raReportElement Text8;
  private String[] Text8Props = new String[] {
     "=(if (or (> [TotKOU] 0.) (< [TotKOU] 0.)) [TotKOU] \"\")", "", "", "Number|true|#.##0,00", "",
     "", "", "", "8820", "780", "1760", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "",
     "", "Right", ""};
  public raReportElement LabelKO;
  private String[] LabelKOProps = new String[] {"K.O.", "", "3280", "780", "960", "240", "", "", "",
     "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "3280", "1060", "9120", "0", "", "-11184811", // "-11184811" was "Gray",
     ""};
  public raReportElement LabelUKUPNO;
  private String[] LabelUKUPNOProps = new String[] {"UKUPNO", "", "3280", "1120", "960", "240", "",
     "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(+ [TotRac] [TotKO])", "", "",
     "Number|true|#.##0,00", "", "", "", "", "4960", "1120", "1760", "240", "", "", "", "", "", "",
     "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement TextTotUpl1;
  private String[] TextTotUpl1Props = new String[] {"=(+ [TotUpl] [TotKOU])", "", "", "Number|true|#.##0,00", "",
     "", "", "", "8820", "1120", "1760", "240", "", "", "", "", "", "", "Lucida Bright", "9", "",
     "", "", "Right", ""};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(+ [SaldoRac] [SaldoKO])", "", "",
     "Number|true|#.##0,00", "", "", "", "", "6800", "1120", "1760", "240", "", "", "", "", "", "",
     "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement TextSaldoUpl1;
  private String[] TextSaldoUpl1Props = new String[] {"=(+ [SaldoUpl] [SaldoKOU])", "", "", "Number|true|#.##0,00",
     "", "", "", "", "10660", "1120", "1760", "240", "", "", "", "", "", "", "Lucida Bright", "9",
     "", "", "", "Right", ""};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "3280", "1380", "9120", "0", "", "-11184811", // "-11184811" was "Gray",
     ""};
  public raReportElement LabelRacuni__Uplate;
  private String[] LabelRacuni__UplateProps = new String[] {"Ra\u010Duni  -  Uplate", "", "4040",
     "1460", "4000", "240", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {"=(- (+ [TotRac] [TotKO]) (+ [TotUpl] [TotKOU]))", "", "",
     "Number|true|#.##0,00", "", "", "", "", "8080", "1460", "1760", "240", "", "", "", "", "", "",
     "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement LabelSaldo_Racuna__Saldo;
  private String[] LabelSaldo_Racuna__SaldoProps = new String[] {
     "Saldo Ra\u010Duna  -  Saldo Uplata", "", "4040", "1760", "4000", "240", "", "", "", "", "",
     "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement Text6;
  private String[] Text6Props = new String[] {"=(- (+ [SaldoRac] [SaldoKO]) (+ [SaldoUpl] [SaldoKOU]))", "", "",
     "Number|true|#.##0,00", "", "", "", "", "8080", "1760", "1760", "240", "", "", "", "", "", "",
     "Lucida Bright", "9", "", "", "", "Right", ""};

  public raSkKarticaTotaliSectionFooter(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_FOOTER + 0));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  private void addElements() {
    Line1 = addModel(ep.LINE, Line1Props);
    LabelSaldo = addModel(ep.LABEL, LabelSaldoProps);
    LabelTOTAL = addModel(ep.LABEL, LabelTOTALProps);
    LabelUPLATE = addModel(ep.LABEL, LabelUPLATEProps);
    LabelRACUNI = addModel(ep.LABEL, LabelRACUNIProps);
    Label1 = addModel(ep.LABEL, Label1Props);
    Label2 = addModel(ep.LABEL, Label2Props);
    LabelSaldo1 = addModel(ep.LABEL, LabelSaldo1Props);
    Line2 = addModel(ep.LINE, Line2Props);
    TextTotRac = addModel(ep.TEXT, TextTotRacProps);
    LabelIZNOS = addModel(ep.LABEL, LabelIZNOSProps);
    TextSaldoUpl = addModel(ep.TEXT, TextSaldoUplProps);
    TextSaldoRac = addModel(ep.TEXT, TextSaldoRacProps);
    TextTotUpl = addModel(ep.TEXT, TextTotUplProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    Text2 = addModel(ep.TEXT, Text2Props);
    LabelKO = addModel(ep.LABEL, LabelKOProps);
    Text7 = addModel(ep.TEXT, Text7Props);
    Text8 = addModel(ep.TEXT, Text8Props);
    Line3 = addModel(ep.LINE, Line3Props);
    LabelUKUPNO = addModel(ep.LABEL, LabelUKUPNOProps);
    Text3 = addModel(ep.TEXT, Text3Props);
    TextTotUpl1 = addModel(ep.TEXT, TextTotUpl1Props);
    Text4 = addModel(ep.TEXT, Text4Props);
    TextSaldoUpl1 = addModel(ep.TEXT, TextSaldoUpl1Props);
    Line4 = addModel(ep.LINE, Line4Props);
    LabelRacuni__Uplate = addModel(ep.LABEL, LabelRacuni__UplateProps);
    Text5 = addModel(ep.TEXT, Text5Props);
    LabelSaldo_Racuna__Saldo = addModel(ep.LABEL, LabelSaldo_Racuna__SaldoProps);
    Text6 = addModel(ep.TEXT, Text6Props);
  }

  private void modifyThis() {
  }
}

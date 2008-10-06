/****license*****************************************************************
**   file: raSkKarticaSectionHeader.java
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

public class raSkKarticaSectionHeader extends raReportSection {

  private String[] thisProps = new String[] {"Partner", "", "", "", "Yes", "", "Yes", "Yes", "2880"};
  public raReportElement TextOneLine;
  private String[] TextOneLineProps = new String[] {"OneLine", "", "", "", "", "", "", "",
     "60", "60", "5920", "240", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "",
     ""};
  public raReportElement LabelDatum_ispisa_;
  private String[] LabelDatum_ispisa_Props = new String[] {"Datum ispisa", "", "12380", "60",
     "1820", "240", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "",
     "14260", "60", "1240", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "",
     ""};
  public raReportElement TextNaslov;
  private String[] TextNaslovProps = new String[] {"Naslov", "", "", "", "", "", "", "", "", "120", // "350" was "600"
     "15580", "680", "", "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center", ""};
  public raReportElement TextPeriod;
  private String[] TextPeriodProps = new String[] {"Period", "", "", "", "", "", "", "", "", "580", // "1050" was "1300"
     "15580", "600", "", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", "Center", ""};
  public raReportElement TextPartnerAdresa;
  private String[] TextPartnerAdresaProps = new String[] {"PartnerAdresa", "", "", "", "", "", "Yes",
     "", "1420", "1240", "14760", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", // "1720" was "1800"
     "", ""};
  public raReportElement TextPartnerLab;
  private String[] TextPartnerLabProps = new String[] {"PartnerLab", "", "", "", "", "", "", "", "",
     "1240", "1360", "260", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""}; // "1720" was "1800"

  public raReportElement TextKontoLab;
  private String[] TextKontoLabProps = new String[] {"KontoLab", "", "", "", "", "", "", "", "13680",
     "1240", "900", "260", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""}; // "1720" was "1800"

  public raReportElement TextKonto;
  private String[] TextKontoProps = new String[] {"Konto", "", "", "", "", "", "Yes",
     "", "14640", "1240", "1000", "260", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", // "1720" was "1800"
     "Right", "No"};
  
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "1200", "15620", "0", "", "", ""};

  public raReportElement Line_1;
  private String[] Line_1Props = new String[] {"", "", "", "20", "15620", "0", "", "", ""};

  public raSkKarticaSectionHeader(raReportTemplate owner) {
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
    TextOneLine = addModel(ep.TEXT, TextOneLineProps);
    LabelDatum_ispisa_ = addModel(ep.LABEL, LabelDatum_ispisa_Props);
    TextDatumIsp = addModel(ep.TEXT, TextDatumIspProps);
    TextNaslov = addModel(ep.TEXT, TextNaslovProps);
    TextPeriod = addModel(ep.TEXT, TextPeriodProps);
    TextPartnerAdresa = addModel(ep.TEXT, TextPartnerAdresaProps);
    TextPartnerLab = addModel(ep.TEXT, TextPartnerLabProps);
    TextKontoLab = addModel(ep.TEXT, TextKontoLabProps);
    TextKonto = addModel(ep.TEXT, TextKontoProps);
    Line_1 = addModel(ep.LINE, Line_1Props);
    Line1 = addModel(ep.LINE, Line1Props);
  }

  private void modifyThis() {
  }
}

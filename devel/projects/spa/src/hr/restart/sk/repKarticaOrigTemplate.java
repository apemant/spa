/****license*****************************************************************
**   file: repKarticaOrigTemplate.java
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
package hr.restart.sk;

import hr.restart.util.reports.raElixirProperties;
import hr.restart.util.reports.raElixirPropertiesInstance;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raReportTemplate;
import sg.com.elixir.reportwriter.xml.ModelFactory;

public abstract class repKarticaOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"", "", "JDOrepKartica", "", "", "15700", "",
     "", "", "", ""};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "567.0", "560", "567.0", "Landscape", "",
     "11900", "16840", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"Partner", "", "Yes", "Yes", "", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"Partner", "", "Yes", "Yes", "", "", ""};
  public raReportSection ReportHeader;
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"Partner", "", "", "", "Yes", "", "", "Yes",
     "560"};  // "560" was "500"
  public raReportElement LabelSaldo;
  private String[] LabelSaldoProps = new String[] {"\nSaldo", "", "13660", "60", "1980", "440", // "60" was ""
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPotrazuje;
  private String[] LabelPotrazujeProps = new String[] {"\nPotražuje", "", "11660", "60", "1980",  // "60" was ""
     "440", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelDuguje;
  private String[] LabelDugujeProps = new String[] {"\nDuguje", "", "9660", "60", "1980", "440",  // "60" was ""
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelDatum_dospjeca;
  private String[] LabelDatum_dospjecaProps = new String[] {"Datum\ndospje\u0107a", "", "8240", "60", // "60" was ""
     "1400", "440", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement LabelDatum_dokumenta;
  private String[] LabelDatum_dokumentaProps = new String[] {"Datum\ndokumenta", "", "6820", "60", // "60" was ""
     "1400", "440", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement LabelBroj_dokumenta;
  private String[] LabelBroj_dokumentaProps = new String[] {"\nDokument", "", "4400", "60", // "60" was ""
     "2400", "440", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement LabelDatum_knjizenja;
  private String[] LabelDatum_knjizenjaProps = new String[] {"Datum\nknjiženja", "", "2980", "60", // "60" was ""
     "1400", "440", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement LabelBroj_naloga;
  private String[] LabelBroj_nalogaProps = new String[] {"Broj naloga\n(knjig-god-vrsta-broj)", 
      "", "800", "60", "2160", "440", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", 
      "Bold", "", "", "Center"};

  public raReportElement LineSH1_L1;
  private String[] LineSH1_L1Props = new String[] {"", "", "", "0", "15620", "0", "", "", ""};

  public raReportElement LineSH1_L2;
  private String[] LineSH1_L2Props = new String[] {"", "", "", "540", "15620", "0", "", "", ""};

  public raReportElement LabelOrg_jedinica;
  private String[] LabelOrg_jedinicaProps = new String[] {"\nOJ", "", "", "60", "780", // "60" was ""
     "440", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "No", "260"};
  public raReportElement TextSaldo;
  private String[] TextSaldoProps = new String[] {"Saldo", "", "", "Number|true|#.##0,00", "", "",
     "", "", "13660", "", "1980", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "",
     "Right", ""};
  public raReportElement TextIP;
  private String[] TextIPProps = new String[] {"IP", "", "", "Number|true|#.##0,00", "", "", "", "",
     "11660", "", "1980", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right",
     ""};
  public raReportElement TextID;
  private String[] TextIDProps = new String[] {"ID", "", "", "Number|true|#.##0,00", "", "", "", "",
     "9660", "", "1980", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right",
     ""};
  public raReportElement TextBrojDok;
  private String[] TextBrojDokProps = new String[] {"BrojDok", "", "", "", "", "", "", "", "4400",
     "", "2400", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement TextDatDosp;
  private String[] TextDatDospProps = new String[] {"DatDosp", "", "", "", "", "", "", "", "8240",
     "", "1400", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Center", ""};
  public raReportElement TextDatDok;
  private String[] TextDatDokProps = new String[] {"DatDok", "", "", "", "", "", "", "", "6820", "",
     "1400", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Center", ""};
  public raReportElement TextCNaloga;
  private String[] TextCNalogaProps = new String[] {"CNaloga", "", "", "", "", "", "", "", "800",
     "", "2160", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement TextDatKnj;
  private String[] TextDatKnjProps = new String[] {"DatKnj", "", "", "", "", "", "", "", "2980", "",
     "1400", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Center", ""};
  public raReportElement TextCOrg;
  private String[] TextCOrgProps = new String[] {"COrg", "", "", "", "", "", "", "", "", "", "780",
     "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportSection SectionFooter1;
  private String[] SectionFooter1Props = new String[] {"Partner", "", "", "", "Yes", "", "No",
     "400"};


  public raReportElement LineSF1_L1;
  private String[] LineSF1_L1Props = new String[] {"", "", "", "0", "15620", "0", "", "", ""};

  public raReportElement LineSF1_L2;
  private String[] LineSF1_L2Props = new String[] {"", "", "", "340", "15620", "0", "", "", ""};


  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "13660", "60", "1980", "240", "Normal", // "60" was ""
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelU_K_U_P;
  private String[] LabelU_K_U_PProps = new String[] {"U K U P N O", "", "", "60", "9640", "240", // "60" was ""
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum\"ID\")", "", "", "Number|true|#.##0,00", "",
     "", "", "", "9660", "60", "2000", "260", "Normal", "Gray", "", "", "", "", "Lucida Bright", // "60" was ""
     "9", "Bold", "", "", "Right", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum\"IP\")", "", "", "Number|true|#.##0,00", "",
     "", "", "", "11660", "60", "2000", "260", "Normal", "Gray", "", "", "", "", "Lucida Bright", // "60" was ""
     "9", "Bold", "", "", "Right", ""};
  public raReportSection SectionFooter0;
  public raReportSection PageFooter;
  public raReportSection ReportFooter;

  public repKarticaOrigTemplate() {
    createReportStructure();
    setReportProperties();
  }

  public raReportSection createSections() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTIONS));

    Section0 = sect.getModel(ep.SECTION + 0, Section0Props);
    Section1 = sect.getModel(ep.SECTION + 1, Section1Props);
    return sect;
  }

  public abstract raReportSection createReportHeader();

  public abstract raReportSection createPageHeader();

  public abstract raReportSection createSectionHeader0();

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    LineSH1_L1 = sect.addModel(ep.LINE, LineSH1_L1Props);
    LabelSaldo = sect.addModel(ep.LABEL, LabelSaldoProps);
    LabelPotrazuje = sect.addModel(ep.LABEL, LabelPotrazujeProps);
    LabelDuguje = sect.addModel(ep.LABEL, LabelDugujeProps);
    LabelDatum_dospjeca = sect.addModel(ep.LABEL, LabelDatum_dospjecaProps);
    LabelDatum_dokumenta = sect.addModel(ep.LABEL, LabelDatum_dokumentaProps);
    LabelBroj_dokumenta = sect.addModel(ep.LABEL, LabelBroj_dokumentaProps);
    LabelDatum_knjizenja = sect.addModel(ep.LABEL, LabelDatum_knjizenjaProps);
    LabelBroj_naloga = sect.addModel(ep.LABEL, LabelBroj_nalogaProps);
    LabelOrg_jedinica = sect.addModel(ep.LABEL, LabelOrg_jedinicaProps);
    LineSH1_L2 = sect.addModel(ep.LINE, LineSH1_L2Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextSaldo = sect.addModel(ep.TEXT, TextSaldoProps);
    TextIP = sect.addModel(ep.TEXT, TextIPProps);
    TextID = sect.addModel(ep.TEXT, TextIDProps);
    TextBrojDok = sect.addModel(ep.TEXT, TextBrojDokProps);
    TextDatDosp = sect.addModel(ep.TEXT, TextDatDospProps);
    TextDatDok = sect.addModel(ep.TEXT, TextDatDokProps);
    TextCNaloga = sect.addModel(ep.TEXT, TextCNalogaProps);
    TextDatKnj = sect.addModel(ep.TEXT, TextDatKnjProps);
    TextCOrg = sect.addModel(ep.TEXT, TextCOrgProps);
    return sect;
  }

  public raReportSection createSectionFooter1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 1), SectionFooter1Props);

    LineSF1_L1 = sect.addModel(ep.LINE, LineSF1_L1Props);
    Label1 = sect.addModel(ep.LABEL, Label1Props);
    LabelU_K_U_P = sect.addModel(ep.LABEL, LabelU_K_U_PProps);
    Text1 = sect.addModel(ep.TEXT, Text1Props);
    Text2 = sect.addModel(ep.TEXT, Text2Props);
    LineSF1_L2 = sect.addModel(ep.LINE, LineSF1_L2Props);
    return sect;
  }

  public abstract raReportSection createSectionFooter0();

  public abstract raReportSection createPageFooter();

  public abstract raReportSection createReportFooter();

  public void createReportStructure() {
    template = ModelFactory.getModel(ep.REPORT_TEMPLATE);
    ModelFactory.setCurrentReport(template);

    ReportTemplate = addSection(new raReportSection(template, ReportTemplateProps));

    PageSetup = addSection(new raReportSection(template.getModel(ep.PAGE_SETUP), PageSetupProps));
    Sections = addSection(createSections());

    ReportHeader = addSection(createReportHeader());
    PageHeader = addSection(createPageHeader());
    SectionHeader0 = addSection(createSectionHeader0());
    SectionHeader1 = addSection(createSectionHeader1());
    Detail = addSection(createDetail());
    SectionFooter1 = addSection(createSectionFooter1());
    SectionFooter0 = addSection(createSectionFooter0());
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
}

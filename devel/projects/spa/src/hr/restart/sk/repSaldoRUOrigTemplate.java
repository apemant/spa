/****license*****************************************************************
**   file: repSaldoRUOrigTemplate.java
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

public abstract class repSaldoRUOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"", "", "JDOrepSaldoRU", "", "", "15700", "",
     "", "", "", ""};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "567.0", "560", "567.0", "Landscape", "",
     "11900", "16840", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"Partner", "", "Yes", "Yes", "Each Value", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"Partner", "", "Yes", "Yes", "", "", ""};
  public raReportElement Section2;
  private String[] Section2Props = new String[] {"Partner", "", "", "Yes", "", "", ""};
  public raReportSection ReportHeader;
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"Partner", "", "", "", "Yes", "", "", "Yes",
     "560"}; // "560" was "500"
  public raReportElement LabelSaldo;
  private String[] LabelSaldoProps = new String[] {"\nSaldo", "", "13700", "60", "1940", "440", // "60" was ""
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};


  public raReportElement LineSH1_L1;
  private String[] LineSH1_L1Props = new String[] {"", "", "", "0", "15620", "0", "", "", ""};

  public raReportElement LineSH1_L2;
  private String[] LineSH1_L2Props = new String[] {"", "", "", "540", "15620", "0", "", "", ""};


  public raReportElement LabelIznos;
  private String[] LabelIznosProps = new String[] {"\nIznos", "", "11740", "60", "1940", "440", // "60" was ""
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelDatum_dospjeca;
  private String[] LabelDatum_dospjecaProps = new String[] {"Datum\ndospje\u0107a", "", "8660", "60", // "60" was ""
     "1260", "440", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement LabelDatum_dokumenta;
  private String[] LabelDatum_dokumentaProps = new String[] {"Datum\ndokumenta", "", "7380", "60",
     "1260", "440", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement LabelBroj_dokumenta;
  private String[] LabelBroj_dokumentaProps = new String[] {"\nBroj dokumenta", "", "5000", "60", // "60" was ""
     "2360", "440", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement LabelDatum_knjizenja;
  private String[] LabelDatum_knjizenjaProps = new String[] {"Datum\nknjiženja", "", "2900", "60", // "60" was ""
     "1260", "440", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement LabelBroj_naloga;
  private String[] LabelBroj_nalogaProps = new String[] {"Broj naloga\n(knjig-god-vrsta-broj)", "", 
     "740", "60", "2140", "440", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelOrg_jedinica;
  private String[] LabelOrg_jedinicaProps = new String[] {"\nOJ", "", "", "60", "720", // "60" was ""
     "440", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelVrsta;
  private String[] LabelVrstaProps = new String[] {"\nVrsta", "", "4180", "60", "800", "440", // "60" was ""
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelBroj_izvoda;
  private String[] LabelBroj_izvodaProps = new String[] {"Broj\nizvoda", "", "9940", "60", "840", // "60" was ""
     "440", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNacin_placanja;
  private String[] LabelNacin_placanjaProps = new String[] {"Na\u010Din\npla\u0107anja", "",
     "10800", "60", "920", "440", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", // "60" was ""
     "", "", "Center"};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "260"};
  public raReportElement TextSaldo;
  private String[] TextSaldoProps = new String[] {"Saldo", "", "", "Number|true|#.##0,00", "", "",
     "", "", "13700", "", "1940", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "",
     "Right", ""};
  public raReportElement TextIznos;
  private String[] TextIznosProps = new String[] {"Iznos", "", "", "Number|true|#.##0,00", "", "",
     "", "", "11740", "", "1940", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "",
     "Right", ""};
  public raReportElement TextBrojDok;
  private String[] TextBrojDokProps = new String[] {"BrojDok", "", "", "", "", "", "", "", "5000",
     "", "2360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement TextVrsta;
  private String[] TextVrstaProps = new String[] {"Vrsta", "", "", "", "", "", "", "", "4180", "",
     "800", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement TextDatDosp;
  private String[] TextDatDospProps = new String[] {"DatDosp", "", "", "", "", "", "", "", "8660",
     "", "1260", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Center", ""};
  public raReportElement TextIzvod;
  private String[] TextIzvodProps = new String[] {"Izvod", "", "", "", "", "", "", "", "9940", "",
     "840", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement TextDatDok;
  private String[] TextDatDokProps = new String[] {"DatDok", "", "", "", "", "", "", "", "7380", "",
     "1260", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Center", ""};
  public raReportElement TextNacpl;
  private String[] TextNacplProps = new String[] {"Nacpl", "", "", "", "", "", "", "", "10800", "",
     "920", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Center", ""};
  public raReportElement TextCNaloga;
  private String[] TextCNalogaProps = new String[] {"CNaloga", "", "", "", "", "", "", "", "740",
     "", "2140", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement TextDatKnj;
  private String[] TextDatKnjProps = new String[] {"DatKnj", "", "", "", "", "", "", "", "2900", "",
     "1260", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Center", ""};
  public raReportElement TextCOrg;
  private String[] TextCOrgProps = new String[] {"COrg", "", "", "", "", "", "", "", "", "", "720",
     "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportSection SectionFooter2;
  private String[] SectionFooter2Props = new String[] {"Partner", "", "", "", "Yes", "No", "No",
     "400"};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "", "60", "15640", "220", "Normal", "Gray", // "60" was ""
     "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportSection SectionFooter1;
  private String[] SectionFooter1Props = new String[] {"Partner", "", "", "", "Yes", "No", "No",
     "440"};
  public raReportElement LabelU_K_U_P;
  private String[] LabelU_K_U_PProps = new String[] {"U K U P N O", "", "", "", "11660", "240",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"Iznos\")", "", "", "Number|true|#.##0,00",
     "", "", "", "", "11680", "", "2000", "260", "Normal", "Gray", "", "", "", "",
     "Lucida Bright", "9", "Bold", "", "", "Right", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"Saldo\")", "", "", "Number|true|#.##0,00",
     "", "", "", "", "13640", "", "2000", "260", "Normal", "Gray", "", "", "", "",
     "Lucida Bright", "9", "Bold", "", "", "Right", ""};

  public raReportElement LineSF1_L1;
  private String[] LineSF1_L1Props = new String[] {"", "", "", "0", "15620", "0", "", "", ""};

  public raReportElement LineSF1_L2;
  private String[] LineSF1_L2Props = new String[] {"", "", "", "320", "15620", "0", "", "", ""};

  public raReportSection SectionFooter0;
  public raReportSection PageFooter;
  public raReportSection ReportFooter;

  public repSaldoRUOrigTemplate() {
    createReportStructure();
    setReportProperties();
  }

  public raReportSection createSections() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTIONS));

    Section0 = sect.getModel(ep.SECTION + 0, Section0Props);
    Section1 = sect.getModel(ep.SECTION + 1, Section1Props);
    Section2 = sect.getModel(ep.SECTION + 2, Section2Props);
    return sect;
  }

  public abstract raReportSection createReportHeader();

  public abstract raReportSection createPageHeader();

  public abstract raReportSection createSectionHeader0();

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    LineSH1_L1 = sect.addModel(ep.LINE, LineSH1_L1Props);
    LabelSaldo = sect.addModel(ep.LABEL, LabelSaldoProps);
    LabelIznos = sect.addModel(ep.LABEL, LabelIznosProps);
    LabelDatum_dospjeca = sect.addModel(ep.LABEL, LabelDatum_dospjecaProps);
    LabelDatum_dokumenta = sect.addModel(ep.LABEL, LabelDatum_dokumentaProps);
    LabelBroj_dokumenta = sect.addModel(ep.LABEL, LabelBroj_dokumentaProps);
    LabelDatum_knjizenja = sect.addModel(ep.LABEL, LabelDatum_knjizenjaProps);
    LabelBroj_naloga = sect.addModel(ep.LABEL, LabelBroj_nalogaProps);
    LabelOrg_jedinica = sect.addModel(ep.LABEL, LabelOrg_jedinicaProps);
    LabelVrsta = sect.addModel(ep.LABEL, LabelVrstaProps);
    LabelBroj_izvoda = sect.addModel(ep.LABEL, LabelBroj_izvodaProps);
    LabelNacin_placanja = sect.addModel(ep.LABEL, LabelNacin_placanjaProps);
    LineSH1_L2 = sect.addModel(ep.LINE, LineSH1_L2Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextSaldo = sect.addModel(ep.TEXT, TextSaldoProps);
    TextIznos = sect.addModel(ep.TEXT, TextIznosProps);
    TextBrojDok = sect.addModel(ep.TEXT, TextBrojDokProps);
    TextVrsta = sect.addModel(ep.TEXT, TextVrstaProps);
    TextDatDosp = sect.addModel(ep.TEXT, TextDatDospProps);
    TextIzvod = sect.addModel(ep.TEXT, TextIzvodProps);
    TextDatDok = sect.addModel(ep.TEXT, TextDatDokProps);
    TextNacpl = sect.addModel(ep.TEXT, TextNacplProps);
    TextCNaloga = sect.addModel(ep.TEXT, TextCNalogaProps);
    TextDatKnj = sect.addModel(ep.TEXT, TextDatKnjProps);
    TextCOrg = sect.addModel(ep.TEXT, TextCOrgProps);
    return sect;
  }

  public raReportSection createSectionFooter2() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 2), SectionFooter2Props);

    LineSF1_L1 = sect.addModel(ep.LINE, LineSF1_L1Props);
    Label1 = sect.addModel(ep.LABEL, Label1Props);
    LineSF1_L2 = sect.addModel(ep.LINE, LineSF1_L2Props);
    return sect;
  }

  public raReportSection createSectionFooter1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 1), SectionFooter1Props);

    LabelU_K_U_P = sect.addModel(ep.LABEL, LabelU_K_U_PProps);
    Text1 = sect.addModel(ep.TEXT, Text1Props);
    Text2 = sect.addModel(ep.TEXT, Text2Props);
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
    SectionFooter2 = addSection(createSectionFooter2());
    SectionFooter1 = addSection(createSectionFooter1());
    SectionFooter0 = addSection(createSectionFooter0());
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
}

/****license*****************************************************************
**   file: repGrupArtOrigTemplate.java
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

import hr.restart.util.reports.raElixirProperties;
import hr.restart.util.reports.raElixirPropertiesInstance;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raReportTemplate;
import sg.com.elixir.reportwriter.xml.ModelFactory;

public abstract class repGrupArtOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"", "", "JDOrepGrupArt", "", "", "10680", "",
     "", "", "", ""};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"300", "380.2", "538.65", "538.65", "", "",
     "11895.66", "16834.23", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"FirstLine", "", "", "", "", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"FirstLine", "", "Yes", "", "", "", ""};
  public raReportElement Section2;
  private String[] Section2Props = new String[] {"FirstLine", "", "Yes", "", "", "", ""};
  public raReportElement Section3;
  private String[] Section3Props = new String[] {"EQUAL", "", "", "", "", "", ""};
  public raReportSection ReportHeader;
  public raReportSection PageHeader;
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"FirstLine", "", "", "", "Yes", "", "Yes",
     "", "1580"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "60",
     "", "5670.0", "226.8", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextFirstLine1;
  private String[] TextFirstLine1Props = new String[] {"FirstLine", "", "", "", "", "", "", "",
     "60", "", "5670.0", "226.8", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "",
     ""};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "", "9620",
     "180", "1120", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelDatumispisa;
  private String[] LabelDatumispisaProps = new String[] {"Datum ispisa :", "", "8160", "180",
     "1460", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "",
     "60", "240", "5670.0", "226.8", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "", ""};
  public raReportElement TextSecondLine1;
  private String[] TextSecondLine1Props = new String[] {"SecondLine", "", "", "", "", "", "", "",
     "60", "240", "5670.0", "226.8", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "", ""};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", "60",
     "480", "5670.0", "226.8", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextThirdLine1;
  private String[] TextThirdLine1Props = new String[] {"ThirdLine", "", "", "", "", "", "", "",
     "60", "480", "5670.0", "226.8", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "", ""};
  public raReportElement LabelGRUPEARTIKALA;
  private String[] LabelGRUPEARTIKALAProps = new String[] {"\nGRUPE ARTIKALA", "", "", "640",
     "10740", "640", "", "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center"};
  public raReportSection SectionHeader2;
  private String[] SectionHeader2Props = new String[] {"FirstLine", "", "", "", "Yes", "", "Yes",
     "Yes", "400"};
  public raReportElement LabelPripadnost;
  private String[] LabelPripadnostProps = new String[] {"Pripadnost", "", "6520", "100", "4220",
     "240.0111", "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "",
     "Center"};
  public raReportElement LabelNazivgrupeartikla;
  private String[] LabelNazivgrupeartiklaProps = new String[] {"Naziv grupe artikla", "", "2300",
     "100", "4200", "240.0111", "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold",
     "", "", "Center"};
  public raReportElement LabelSifragrupeartikla;
  private String[] LabelSifragrupeartiklaProps = new String[] {"Šifra grupe artikla", "", "", "100",
     "2280", "240.0111", "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "",
     "Center"};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "300"};
  public raReportElement TextEQUAL;
  private String[] TextEQUALProps = new String[] {"EQUAL", "", "en",
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "6520", "", "4220", "220", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextCGRART;
  private String[] TextCGRARTProps = new String[] {"CGRART", "", "", "", "", "", "", "", "", "",
     "2280", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNAZGRART;
  private String[] TextNAZGRARTProps = new String[] {"NAZGRART", "", "", "", "", "", "", "", "2320",
     "", "4160", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportSection PageFooter;
  public raReportSection ReportFooter;

  public repGrupArtOrigTemplate() {
    createReportStructure();
    setReportProperties();
  }

  public raReportSection createSections() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTIONS));

    Section0 = sect.getModel(ep.SECTION + 0, Section0Props);
    Section1 = sect.getModel(ep.SECTION + 1, Section1Props);
    Section2 = sect.getModel(ep.SECTION + 2, Section2Props);
    Section3 = sect.getModel(ep.SECTION + 3, Section3Props);
    return sect;
  }

  public abstract raReportSection createReportHeader();

  public abstract raReportSection createPageHeader();

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    TextFirstLine = sect.addModel(ep.TEXT, TextFirstLineProps);
    TextFirstLine1 = sect.addModel(ep.TEXT, TextFirstLine1Props);
    TextDatumIsp = sect.addModel(ep.TEXT, TextDatumIspProps);
    LabelDatumispisa = sect.addModel(ep.LABEL, LabelDatumispisaProps);
    TextSecondLine = sect.addModel(ep.TEXT, TextSecondLineProps);
    TextSecondLine1 = sect.addModel(ep.TEXT, TextSecondLine1Props);
    TextThirdLine = sect.addModel(ep.TEXT, TextThirdLineProps);
    TextThirdLine1 = sect.addModel(ep.TEXT, TextThirdLine1Props);
    LabelGRUPEARTIKALA = sect.addModel(ep.LABEL, LabelGRUPEARTIKALAProps);
    return sect;
  }

  public raReportSection createSectionHeader2() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 2), SectionHeader2Props);

    LabelPripadnost = sect.addModel(ep.LABEL, LabelPripadnostProps);
    LabelNazivgrupeartikla = sect.addModel(ep.LABEL, LabelNazivgrupeartiklaProps);
    LabelSifragrupeartikla = sect.addModel(ep.LABEL, LabelSifragrupeartiklaProps);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextEQUAL = sect.addModel(ep.TEXT, TextEQUALProps);
    TextCGRART = sect.addModel(ep.TEXT, TextCGRARTProps);
    TextNAZGRART = sect.addModel(ep.TEXT, TextNAZGRARTProps);
    return sect;
  }

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
    SectionHeader1 = addSection(createSectionHeader1());
    SectionHeader2 = addSection(createSectionHeader2());
    Detail = addSection(createDetail());
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
}

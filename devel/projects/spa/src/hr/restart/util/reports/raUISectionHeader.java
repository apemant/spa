/****license*****************************************************************
**   file: raUISectionHeader.java
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

public class raUISectionHeader extends raReportSection {

  private String[] thisProps = new String[] {"CSKL", "", "", "", "Yes", "", "Yes", "", "2380"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "60",
     "", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "",
     "60", "240", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "",
     ""};
  public raReportElement LabelDatum_ispisa_;
  private String[] LabelDatum_ispisa_Props = new String[] {"Datum ispisa :", "", "12720", "480",
     "1820", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", "60",
     "480", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "",
     "14560", "480", "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement LabelPREGLED_IZLAZNIH_DOKUMENATA;
  private String[] LabelPREGLED_IZLAZNIH_DOKUMENATAProps = new String[] {
     "\nPREGLED IZLAZNIH DOKUMENATA", "", "20", "840", "15640", "700", "", "", "", "", "", "",
     "Lucida Bright", "14", "Bold", "", "", "Center"};
  public raReportElement TextRANGE;
  private String[] TextRANGEProps = new String[] {"RANGE", "", "", "", "", "", "", "", "", "1520",
     "15640", "260", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Center", ""};
  public raReportElement TextNAZSKL;
  private String[] TextNAZSKLProps = new String[] {"NAZSKL", "", "", "", "", "", "Yes", "", "3440",
     "1880", "7360", "280", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement TextCSKL;
  private String[] TextCSKLProps = new String[] {"CSKL", "", "", "", "", "", "Yes", "", "2040",
     "1880", "1360", "280", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement TextLABELCSKL;
  private String[] TextLABELCSKLProps = new String[] {"LABELCSKL", "", "", "", "", "", "Yes", "",
     "", "1880", "2020", "280", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "",
     "Right", ""};

  public raUISectionHeader(raReportTemplate owner) {
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
    TextFirstLine = addModel(ep.TEXT, TextFirstLineProps);
    TextSecondLine = addModel(ep.TEXT, TextSecondLineProps);
    LabelDatum_ispisa_ = addModel(ep.LABEL, LabelDatum_ispisa_Props);
    TextThirdLine = addModel(ep.TEXT, TextThirdLineProps);
    TextDatumIsp = addModel(ep.TEXT, TextDatumIspProps);
    LabelPREGLED_IZLAZNIH_DOKUMENATA = addModel(ep.LABEL, LabelPREGLED_IZLAZNIH_DOKUMENATAProps);
    TextRANGE = addModel(ep.TEXT, TextRANGEProps);
    TextNAZSKL = addModel(ep.TEXT, TextNAZSKLProps);
    TextCSKL = addModel(ep.TEXT, TextCSKLProps);
    TextLABELCSKL = addModel(ep.TEXT, TextLABELCSKLProps);
  }

  private void modifyThis() {
  }
}

/****license*****************************************************************
**   file: raOSSectionFooter101.java
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

public class raOSSectionFooter101 extends raReportSection {

  private String[] thisProps = new String[] {"COrg", "", "", "", "Yes", "", "Yes", "440"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "0", "15660", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "No", "13280", "40", "2400", "240", "Normal", 
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"", "No", "8400", "40", "2420", "240", "Normal", 
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label3;
  private String[] Label3Props = new String[] {"", "No", "", "40", "8380", "240", "Normal", "Gray", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label4;
  private String[] Label4Props = new String[] {"", "No", "10840", "40", "2420", "240", "Normal", 
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum\"OsnVr\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "8360", "60", "2420", "240", "", "", "", "", "", "", "Lucida Bright", "8", 
     "Bold", "", "", "Right", "No"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum\"IspVr\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "10820", "60", "2420", "240", "", "", "", "", "", "", "Lucida Bright", "8", 
     "Bold", "", "", "Right", "No"};
  public raReportElement LabelUkupno_org_jedinica;
  private String[] LabelUkupno_org_jedinicaProps = new String[] {"Ukupno org. jedinica", "", "", 
     "60", "2120", "240", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(dsum\"SadVr\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "13240", "60", "2420", "240", "", "", "", "", "", "", "Lucida Bright", "8", 
     "Bold", "", "", "Right", "No"};
  public raReportElement TextCOrg;
  private String[] TextCOrgProps = new String[] {"COrg", "", "", "", "", "", "", "", "2300", "60", 
     "1400", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNazOrg;
  private String[] TextNazOrgProps = new String[] {"NazOrg", "", "", "", "", "", "", "", "3800", 
     "60", "4380", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "300", "15660", "0", "", "", ""};

  public raOSSectionFooter101(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_FOOTER + 1));
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
    Label1 = addModel(ep.LABEL, Label1Props);
    Label2 = addModel(ep.LABEL, Label2Props);
    Label3 = addModel(ep.LABEL, Label3Props);
    Label4 = addModel(ep.LABEL, Label4Props);
    Text1 = addModel(ep.TEXT, Text1Props);
    Text2 = addModel(ep.TEXT, Text2Props);
    LabelUkupno_org_jedinica = addModel(ep.LABEL, LabelUkupno_org_jedinicaProps);
    Text3 = addModel(ep.TEXT, Text3Props);
    TextCOrg = addModel(ep.TEXT, TextCOrgProps);
    TextNazOrg = addModel(ep.TEXT, TextNazOrgProps);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
  }
}

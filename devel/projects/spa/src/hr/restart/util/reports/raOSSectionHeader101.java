/****license*****************************************************************
**   file: raOSSectionHeader101.java
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

public class raOSSectionHeader101 extends raReportSection {

  private String[] thisProps = new String[] {"COrg", "", "", "", "Yes", "", "Yes", "", "400"};
  public raReportElement TextCOrg;
  private String[] TextCOrgProps = new String[] {"COrg", "", "", "", "", "", "", "", "2280", "", 
     "1400", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNazOrg;
  private String[] TextNazOrgProps = new String[] {"NazOrg", "", "", "", "", "", "", "", "3800", "", 
     "9620", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelOrg_jedinica;
  private String[] LabelOrg_jedinicaProps = new String[] {"Org. jedinica", "", "200", "", "1920", 
     "240", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "260", "15660", "0", "", "", ""};

  public raOSSectionHeader101(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_HEADER + 1));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  private void addElements() {
    TextCOrg = addModel(ep.TEXT, TextCOrgProps);
    TextNazOrg = addModel(ep.TEXT, TextNazOrgProps);
    LabelOrg_jedinica = addModel(ep.LABEL, LabelOrg_jedinicaProps);
    Line1 = addModel(ep.LINE, Line1Props);
  }

  private void modifyThis() {
  }
}

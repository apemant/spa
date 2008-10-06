/****license*****************************************************************
**   file: raRadniNalogObradaSection_Header3.java
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

public class raRadniNalogObradaSection_Header3 extends raReportSection {

  private String[] thisProps = new String[] {"JEDANBROJ", "", "", "", "Yes", "Yes", "Yes", "Yes", 
     "280"};
  public raReportElement TextJEDANBROJ;
  private String[] TextJEDANBROJProps = new String[] {"JEDANBROJ", "", "", "", "", "", "Yes", "Yes", 
     "140", "", "3700", "240", "", "-1973791", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(put-var \"H1\" 1)", "", "", "", "No", "", "", "", 
     "0", "0", "0", "0", "", "", "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "160", "240", "10660", "0", "", "", ""};

  public raRadniNalogObradaSection_Header3(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_HEADER + 3));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  private void addElements() {
    TextJEDANBROJ = addModel(ep.TEXT, TextJEDANBROJProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    Line1 = addModel(ep.LINE, Line1Props);
  }

  private void modifyThis() {
  }
}

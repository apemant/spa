/****license*****************************************************************
**   file: raRadniNalogObradaSection_Footer3.java
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

public class raRadniNalogObradaSection_Footer3 extends raReportSection {

  private String[] thisProps = new String[] {"JEDANBROJ", "", "", "", "Yes", "", "Yes", "320"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "160", "0", "10660", "0", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"IZNOS\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9640", "40", "1200", "200", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};

  public raRadniNalogObradaSection_Footer3(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_FOOTER + 3));
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
    Text1 = addModel(ep.TEXT, Text1Props);
  }

  private void modifyThis() {
  }
}

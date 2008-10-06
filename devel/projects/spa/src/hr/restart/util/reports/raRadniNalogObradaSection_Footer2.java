/****license*****************************************************************
**   file: raRadniNalogObradaSection_Footer2.java
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

public class raRadniNalogObradaSection_Footer2 extends raReportSection {

  private String[] thisProps = new String[] {"VRDOK", "", "", "", "Yes", "", "Yes", "120"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "160", "0", "10660", "0", "", "", ""};

  public raRadniNalogObradaSection_Footer2(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_FOOTER + 2));
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
  }

  private void modifyThis() {
  }
}

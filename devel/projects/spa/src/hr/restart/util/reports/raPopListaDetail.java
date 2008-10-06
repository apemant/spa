/****license*****************************************************************
**   file: raPopListaDetail.java
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

public class raPopListaDetail extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "", "380"};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "7160", "60",
     "860", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", ""};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "", "", "2560",
     "60", "4560", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "", "60",
     "2520", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "280", "10760", "0", "", "", ""};

  public raPopListaDetail(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.DETAIL));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  private void addElements() {
    TextJM = addModel(ep.TEXT, TextJMProps);
    TextNAZART = addModel(ep.TEXT, TextNAZARTProps);
    TextCART = addModel(ep.TEXT, TextCARTProps);
    Line1 = addModel(ep.LINE, Line1Props);
  }

  private void modifyThis() {//900, 1780, 1780
    resizeElement(this.TextCART, hr.restart.robno.Aut.getAut().getIzlazCARTdep(750, 1600, 1600), this.TextNAZART);
  }
}

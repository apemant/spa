/****license*****************************************************************
**   file: raIzlazPageFooter.java
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

public class raIzlazPageFooter extends raReportSection {

  private String[] thisProps = new String[] {"", "", "380"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "20", "40", "10810", "0", "", "Light Gray", ""}; // 10800 -> 10810
  public raReportElement Text1;
  private String[] Text1Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "8120",
     "120", "2700", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};

  public raIzlazPageFooter(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.PAGE_FOOTER));
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

  private boolean vis = true;
  private void modifyThis() {
    boolean visn = dlgRunReport.getCurrentDlgRunReport().getCurrentDescriptor().isPrintLogo();
    if (vis != visn) {
      vis = visn;
      setTransparent(!vis);
    }
    String sl = hr.restart.sisfun.frmParam.getParam("robno", "sirFooter", "0",
        "Širina predštampanog zaglavlja u cm");
    try {
      this.setHeightCm(Double.parseDouble(sl));
    } catch (Exception ex) {
      try {
        this.setHeightCm(java.text.NumberFormat.getInstance().parse(sl).doubleValue());
      } catch (Exception ex2) {
        ex2.printStackTrace();
      }
    }
  }
}

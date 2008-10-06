/****license*****************************************************************
**   file: raUIPageFooter.java
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

public class raUIPageFooter extends raReportSection {

  private String[] thisProps = new String[] {"", "", "240"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "12940",
     "", "2700", "226.8", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};

  public raUIPageFooter(raReportTemplate owner) {
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
    Text1 = addModel(ep.TEXT, Text1Props);
  }

  private String vis = "";
  private void modifyThis() {
    String pv = hr.restart.sisfun.frmParam.getParam("robno","ispLogo","N","Ispis loga na dokumentima (D/N)");
    if (!pv.equals(vis)) {
      vis = pv;
      setTransparent(vis.equals("N"));
    }
  }
}

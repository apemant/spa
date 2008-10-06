/****license*****************************************************************
**   file: raNewDefaultPageHeader.java
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

public class raNewDefaultPageHeader extends raReportSection {

  private String[] thisProps = new String[] {"", "", "2060"};
  public raReportElement TextLogoNazivlog;
  private String[] TextLogoNazivlogProps = new String[] {"LogoNazivlog", "", "", "", "", "", "", "",
     "", "", "10860", "420", "", "", "", "", "", "", "Lucida Bright", "18", "Bold", "", "", "", ""};
  public raReportElement TextLogoFullAdr;
  private String[] TextLogoFullAdrProps = new String[] {"LogoFullAdr", "", "", "", "", "", "", "",
     "", "400", "10860", "280", "", "", "", "", "", "", "Lucida Bright", "11", "", "", "", "", ""};
  public raReportElement TextTelFax;
  private String[] TextTelFaxProps = new String[] {"TelFax", "", "", "", "", "", "", "", "", "700",
     "10860", "280", "", "", "", "", "", "", "Lucida Bright", "11", "", "", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {
     "=(if (> [LogoPotvrda] 0) \"\nŽiro ra\u010Dun\" \"\")", "", "", "", "", "", "Yes", "", "",
     "760", "7400", "520", "", "", "", "", "", "", "Lucida Bright", "11", "", "", "", "", ""};
  public raReportElement TextLogoZiro;
  private String[] TextLogoZiroProps = new String[] {"LogoZiro", "", "", "", "", "", "", "", "1280",
     "1000", "3080", "280", "", "", "", "", "", "", "Lucida Bright", "11", "Bold", "", "", "", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(if (> [LogoPotvrda] 0) \", MB\" \"\")", "", "", "",
     "", "", "", "", "4360", "1000", "620", "280", "", "", "", "", "", "", "Lucida Bright", "11",
     "", "", "", "", ""};
  public raReportElement TextLogoMatbroj;
  private String[] TextLogoMatbrojProps = new String[] {"LogoMatbroj", "", "", "", "", "", "", "",
     "4920", "1000", "2820", "280", "", "", "", "", "", "", "Lucida Bright", "11", "", "", "", "",
     ""};

  public raNewDefaultPageHeader(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.PAGE_HEADER));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  private void addElements() {
    TextLogoNazivlog = addModel(ep.TEXT, TextLogoNazivlogProps);
    TextLogoFullAdr = addModel(ep.TEXT, TextLogoFullAdrProps);
    TextTelFax = addModel(ep.TEXT, TextTelFaxProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    TextLogoZiro = addModel(ep.TEXT, TextLogoZiroProps);
    Text2 = addModel(ep.TEXT, Text2Props);
    TextLogoMatbroj = addModel(ep.TEXT, TextLogoMatbrojProps);
  }

  private void modifyThis() {
    boolean vis = dlgRunReport.getCurrentDlgRunReport().getCurrentDescriptor().isPrintLogo();
    if (!vis) setTransparent(true);
//    if (vis.equals("N"))
    String sl = hr.restart.sisfun.frmParam.getParam("robno", "sirLogo", "0",
        "Širina predštampanog zaglavlja u cm");
    try {
      this.setHeightCm(Double.parseDouble(sl));
    } catch (Exception ex) {
      ex.printStackTrace();
      try {
        this.setHeightCm(java.text.NumberFormat.getInstance().parse(sl).doubleValue());
      } catch (Exception ex2) {
        ex2.printStackTrace();
      }
    }
//    else
//      this.restoreDefault(ep.HEIGHT);
  }
}

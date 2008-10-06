/****license*****************************************************************
**   file: raIzlazPageHeader.java
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

public class raIzlazPageHeader extends raReportSection {

  private String[] thisProps = new String[] {"", "", "1860"};
  public raReportElement TextLogoNazivlog;
  private String[] TextLogoNazivlogProps = new String[] {"LogoNazivlog", "", "", "", "", "", "", "",
     "", "", "10800", "420", "", "", "", "", "", "", "Lucida Bright", "18", "Bold", "", "", "", ""};
  public raReportElement TextLogoFullAdr;
  private String[] TextLogoFullAdrProps = new String[] {"LogoFullAdr", "", "", "", "", "", "", "",
     "60", "400", "10780", "280", "", "", "", "", "", "", "Lucida Bright", "11", "", "", "", "", ""};
  public raReportElement LabelFax;
  private String[] LabelFaxProps = new String[] {"Fax :", "", "6440", "680", "620", "280", "", "",
     "", "", "", "", "Lucida Bright", "", "", "", "", ""};
  public raReportElement LabelTel;
  private String[] LabelTelProps = new String[] {"Tel :", "", "40", "680", "560", "280", "", "", "",
     "", "", "", "Lucida Bright", "", "", "", "", ""};
  public raReportElement TextLogoTel1;
  private String[] TextLogoTel1Props = new String[] {"LogoTel1", "", "", "", "", "", "", "", "980",
     "680", "2400", "280", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", "", ""};
  public raReportElement TextLogoTel2;
  private String[] TextLogoTel2Props = new String[] {"LogoTel2", "", "", "", "", "", "", "", "3400",
     "680", "2400", "280", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", "", ""};
  public raReportElement TextLogoFax;
  private String[] TextLogoFaxProps = new String[] {"LogoFax", "", "", "", "", "", "", "", "7120",
     "680", "1980", "280", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", "", ""};
  public raReportElement LabelZiroracun;
  private String[] LabelZiroracunProps = new String[] {"\nŽiro ra\u010Dun :", "", "40", "720", "1420",
     "480", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", ""};
  public raReportElement LabelMB;
  private String[] LabelMBProps = new String[] {"MB :", "", "6440", "940", "620", "280", "", "", "",
     "", "", "", "Lucida Bright", "", "", "", "", ""};
  public raReportElement TextLogoZiro;
  private String[] TextLogoZiroProps = new String[] {"LogoZiro", "", "", "", "", "", "", "", "1500",
     "940", "4400", "280", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", "", ""};
  public raReportElement TextLogoMatbroj;
  private String[] TextLogoMatbrojProps = new String[] {"LogoMatbroj", "", "", "", "", "", "", "",
     "7120", "940", "2820", "280", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", "", ""};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "1220", "10810", "0", "", "Light Gray", //10640 -> 10810
     ""};

  public raIzlazPageHeader(raReportTemplate owner) {
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
    LabelFax = addModel(ep.LABEL, LabelFaxProps);
    LabelTel = addModel(ep.LABEL, LabelTelProps);
    TextLogoTel1 = addModel(ep.TEXT, TextLogoTel1Props);
    TextLogoTel2 = addModel(ep.TEXT, TextLogoTel2Props);
    TextLogoFax = addModel(ep.TEXT, TextLogoFaxProps);
    LabelZiroracun = addModel(ep.LABEL, LabelZiroracunProps);
    LabelMB = addModel(ep.LABEL, LabelMBProps);
    TextLogoZiro = addModel(ep.TEXT, TextLogoZiroProps);
    TextLogoMatbroj = addModel(ep.TEXT, TextLogoMatbrojProps);
    Line1 = addModel(ep.LINE, Line1Props);
  }

  private String vis = "";
  private void modifyThis() {
    String pv = hr.restart.sisfun.frmParam.getParam("robno","ispLogo","N","Ispis loga na dokumentima (D/N)");
    if (!pv.equals(vis)) {
      vis = pv;
      setTransparent("N".equalsIgnoreCase(vis));
    }
//    if (vis.equals("N"))
      this.setHeightCm(Double.parseDouble(hr.restart.sisfun.frmParam.getParam("robno", "sirLogo")));
//    else
//      this.restoreDefault(ep.HEIGHT);
  }
}

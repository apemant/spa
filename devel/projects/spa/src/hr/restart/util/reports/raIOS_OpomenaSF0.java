/****license*****************************************************************
**   file: raIOS_OpomenaSF0.java
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

public class raIOS_OpomenaSF0 extends raReportSection {

  private String[] thisProps = new String[] {"CPAR", "", "", "", "Yes", "", "", ""};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "40", "10680", "0", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"IP\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "8240", "80", "2480", "240", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"ID\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5760", "80", "2480", "240", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement LabelUKUPNO;
  private String[] LabelUKUPNOProps = new String[] {"UKUPNO", "", "", "80", "5740", "220", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "320", "10680", "0", "", "", ""};

  public raIOS_OpomenaSF0(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_FOOTER + 0));
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
    Text2 = addModel(ep.TEXT, Text2Props);
    LabelUKUPNO = addModel(ep.LABEL, LabelUKUPNOProps);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
  }
}

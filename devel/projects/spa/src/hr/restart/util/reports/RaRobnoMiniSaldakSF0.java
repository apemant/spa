/****license*****************************************************************
**   file: RaRobnoMiniSaldakSF0.java
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

public class RaRobnoMiniSaldakSF0 extends raReportSection {

  private String[] thisProps = new String[] {"FirstLine", "", "", "", "Yes", "", "Yes", "540"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "60", "10700", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "7440", "100", "1640", "260", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"", "", "5780", "100", "1640", "260", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement Label3;
  private String[] Label3Props = new String[] {"", "", "9100", "100", "1640", "260", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement Label4;
  private String[] Label4Props = new String[] {"", "", "20", "100", "5740", "260", "Normal", "Gray", 
     "Solid", "Gray", "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement LabelUKUPNO;
  private String[] LabelUKUPNOProps = new String[] {"UKUPNO", "", "40", "120", "4260", "220", "", 
     "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"SALDO\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9100", "120", "1640", "220", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", "No"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"UIRAC\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5780", "120", "1640", "220", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", "No"};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(dsum \"PLATITI\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7440", "120", "1640", "220", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", "No"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "380", "10700", "0", "", "", ""};

  public RaRobnoMiniSaldakSF0(raReportTemplate owner) {
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
    Label1 = addModel(ep.LABEL, Label1Props);
    Label2 = addModel(ep.LABEL, Label2Props);
    Label3 = addModel(ep.LABEL, Label3Props);
    Label4 = addModel(ep.LABEL, Label4Props);
    LabelUKUPNO = addModel(ep.LABEL, LabelUKUPNOProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    Text2 = addModel(ep.TEXT, Text2Props);
    Text3 = addModel(ep.TEXT, Text3Props);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
  }
}

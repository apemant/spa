/****license*****************************************************************
**   file: raIOS_OpomenaDetail.java
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

public class raIOS_OpomenaDetail extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "", "740"};
  public raReportElement TextBROJDOK;
  private String[] TextBROJDOKProps = new String[] {"BROJDOK", "", "", "", "", "", "", "", "", "", 
     "2100", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextDATDOK;
  private String[] TextDATDOKProps = new String[] {"DATDOK", "", "", "", "", "", "", "", "2120", "", 
     "1800", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", ""};
  public raReportElement TextDATDOSP;
  private String[] TextDATDOSPProps = new String[] {"DATDOSP", "", "", "", "", "", "", "", "3960", 
     "", "1800", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", ""};
  public raReportElement TextID;
  private String[] TextIDProps = new String[] {"ID", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "5760", "", "2460", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", ""};
  public raReportElement TextIP;
  private String[] TextIPProps = new String[] {"IP", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "8240", "", "2480", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", ""};

  public raIOS_OpomenaDetail(raReportTemplate owner) {
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
    TextBROJDOK = addModel(ep.TEXT, TextBROJDOKProps);
    TextDATDOK = addModel(ep.TEXT, TextDATDOKProps);
    TextDATDOSP = addModel(ep.TEXT, TextDATDOSPProps);
    TextID = addModel(ep.TEXT, TextIDProps);
    TextIP = addModel(ep.TEXT, TextIPProps);
  }

  private void modifyThis() {
  }
}

/****license*****************************************************************
**   file: RaRobnoMiniSaldakDetail.java
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

public class RaRobnoMiniSaldakDetail extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "", "380"};
  public raReportElement TextDATDOSP;
  private String[] TextDATDOSPProps = new String[] {"DATDOSP", "", "", "", "", "", "", "Yes", 
     "3980", "", "1780", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", 
     "No"};
  public raReportElement TextPNBZ2;
  private String[] TextPNBZ2Props = new String[] {"PNBZ2", "", "", "", "", "", "", "Yes", "40", "", 
     "2120", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextDVO;
  private String[] TextDVOProps = new String[] {"DVO", "", "", "", "", "", "", "Yes", "2180", "", 
     "1780", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", "No"};
  public raReportElement TextPLATITI;
  private String[] TextPLATITIProps = new String[] {"PLATITI", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "Yes", "7440", "", "1640", "220", "", "", 
     "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextUIRAC;
  private String[] TextUIRACProps = new String[] {"UIRAC", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "Yes", "5780", "", "1640", "220", "", "", 
     "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextSALDO;
  private String[] TextSALDOProps = new String[] {"SALDO", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "Yes", "9100", "", "1640", "220", "", "", 
     "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};

  public RaRobnoMiniSaldakDetail(raReportTemplate owner) {
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
    TextDATDOSP = addModel(ep.TEXT, TextDATDOSPProps);
    TextPNBZ2 = addModel(ep.TEXT, TextPNBZ2Props);
    TextDVO = addModel(ep.TEXT, TextDVOProps);
    TextPLATITI = addModel(ep.TEXT, TextPLATITIProps);
    TextUIRAC = addModel(ep.TEXT, TextUIRACProps);
    TextSALDO = addModel(ep.TEXT, TextSALDOProps);
  }

  private void modifyThis() {
  }
}

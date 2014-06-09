/****license*****************************************************************
**   file: raIzlazSectionFooterForCustom.java
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
/*
 * Created on 2004.11.16
 */
package hr.restart.util.reports;

import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;


/**
 * @author abf
 */
public class raIzlazSectionFooterForCustom extends raReportSection {
  private String[] thisProps = new String[] {"BRDOK", "After", "", "", "Yes", "Yes", "Yes", "680"};
  public raReportElement TextNAPOMENAOPIS;
  private String[] TextNAPOMENAOPISProps = new String[] {"NAPOMENAOPIS", "", "", "", "", "", "Yes",
     "Yes", "", "0", "10580", "0", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "", ""};
  
  public raIzlazSectionFooterForCustom(raReportTemplate owner) {
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
    TextNAPOMENAOPIS = addModel(ep.TEXT, TextNAPOMENAOPISProps);
  }

  private void modifyThis() {
    String font = frmParam.getParam("robno", "napomenaFont", "", "Font za napomene na izlazima (Name;size;N/B/I/BI)");
    if (font != null && font.length() > 0) {
      String[] p = new VarStr(font).split(';');
      TextNAPOMENAOPIS.setFont(p[0]);
      if (p.length > 1 && p[1].length() > 0) TextNAPOMENAOPIS.setFontSize(Aus.getAnyNumber(p[1]));
      if (p.length > 2 && p[2].toUpperCase().indexOf("B") > 0) TextNAPOMENAOPIS.setFontBold(true);
      if (p.length > 2 && p[2].toUpperCase().indexOf("I") > 0) TextNAPOMENAOPIS.setFontItalic(true);
    }
  }
}

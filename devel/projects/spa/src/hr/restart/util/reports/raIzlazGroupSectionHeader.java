/****license*****************************************************************
**   file: raIzlazGroupSectionHeader.java
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


/**
 * @author abf
 */
public class raIzlazGroupSectionHeader extends raReportSection {
  private String[] thisProps = new String[] {"Group", "", "", "", "Yes", "Yes", "No", "No", "320"};
  public raReportElement TextGROUPTEXT;
  private String[] TextGROUPTEXTProps = new String[] {"GROUPTEXT", "", "", "", "", "", "Yes",
     "Yes", "", "200", "10000", "0", "", "", "", "", "", "", "Lucida Bright", "12", "", "", "",
     "", ""};
  
  public raIzlazGroupSectionHeader(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_HEADER + 2));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  private void addElements() {
    TextGROUPTEXT = addModel(ep.TEXT, TextGROUPTEXTProps);
  }

  private void modifyThis() {
    int fs = Aus.getAnyNumber(frmParam.getParam("robno", "grupaFont", "12", "Velièina fonta za naziv grupe na raèunima/ponudama"));
    if (fs < 8) fs = 8;
    if (fs > 20) fs = 20;
    TextGROUPTEXT.setFontSize(fs);
  }
}

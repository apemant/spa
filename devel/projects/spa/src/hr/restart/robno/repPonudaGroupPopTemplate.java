/****license*****************************************************************
**   file: repPonudaGroupPopTemplate.java
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
package hr.restart.robno;

import hr.restart.util.reports.raIzlazDetail;
import hr.restart.util.reports.raIzlazSectionHeaderLines;
import hr.restart.util.reports.raReportSection;


public class repPonudaGroupPopTemplate extends repPonudaGroupTemplate {

  public raReportSection createSectionHeader1() {
    raIzlazSectionHeaderLines sh = (raIzlazSectionHeaderLines) super.createSectionHeader1();
    sh.Label1.defaultAlterer().setCaption("Popust");
    sh.LabelPop.defaultAlterer().setCaption("%");
    sh.LabelPor.defaultAlterer().setCaption("Iznos");
    return sh;
  }
  
  public raReportSection createDetail() {
    raIzlazDetail sh = (raIzlazDetail) super.createDetail();
    sh.TextPor1p2p3Naz.defaultAlterer().setControlSource("UIRAB");
    return sh;
  }
  
  public repPonudaGroupPopTemplate() {
    // TODO Auto-generated constructor stub
  }

}

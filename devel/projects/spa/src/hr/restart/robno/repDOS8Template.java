/****license*****************************************************************
**   file: repDOS8Template.java
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

import hr.restart.util.reports.raDOSvvDetail;
import hr.restart.util.reports.raDOSvvSH;
import hr.restart.util.reports.raElixirPropertyValues;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;

public class repDOS8Template extends repDOSTemplate {

  public raReportElement TextKOLs;
  private String[] TextKOLsProps = new String[] {"=(dsum \"KOL\")", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "8660", "80", "1080", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextKOL1s;
  private String[] TextKOL1sProps = new String[] {"=(dsum \"KOL1\")", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "7760", "80", "880", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  
  public raReportSection createSectionHeader1() {
    return new raDOSvvSH(this);
  }
  
  public raReportSection createDetail() {
    return new raDOSvvDetail(this);

  }
  
  public raReportSection createSectionFooter0() {
    raReportSection sect = super.createSectionFooter0();

    TextKOLs = sect.addModel(ep.TEXT, TextKOLsProps);
    TextKOL1s = sect.addModel(ep.TEXT, TextKOL1sProps);
    TextNAPOMENAOPIS.defaultAlterer().setTop(500);

    return sect;
  }
    
  public void modifyThis() {}
}

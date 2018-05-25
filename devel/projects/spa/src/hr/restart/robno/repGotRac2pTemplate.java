/****license*****************************************************************
**   file: repGotRac2pTemplate.java
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

import hr.restart.util.reports.raGRSectionFooterMCLines;
import hr.restart.util.reports.raIzlazDetailMC2p;
import hr.restart.util.reports.raIzlazSectionHeaderLines2p;
import hr.restart.util.reports.raReportSection;


public class repGotRac2pTemplate extends repGotRacTemplate {

  public repGotRac2pTemplate() {
    // 
  }
  
  public raReportSection createSectionHeader1() {
    return new raIzlazSectionHeaderLines2p(this); // return new raIzlazSectionHeader(this);
  }

  public raReportSection createDetail() {
    ridmc = new raIzlazDetailMC2p(this);
    ridmc.TextIZNOSSTAVKESP.defaultAlterer().setControlSource("IZNFMCPRP");
    return ridmc;
  }

  public raReportSection createSectionFooter1() {
      rgsfml = new raGRSectionFooterMCLines(this); // return new raGRSectionFooterMC(this);
      rgsfml.Text1.defaultAlterer().setControlSource("=(dsum \"IZNFMCPRP\")");

      rgsfml.Text2.defaultAlterer().setControlSource
      ("=(- (dsum \"IZNFMCPRP\") (+ (dsum \"IPRODBP\") (+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\"))))");
      return rgsfml;
  }
}

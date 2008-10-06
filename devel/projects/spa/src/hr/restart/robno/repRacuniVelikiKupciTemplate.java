/****license*****************************************************************
**   file: repRacuniVelikiKupciTemplate.java
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

import hr.restart.util.reports.raIzlazSKPDetail;
import hr.restart.util.reports.raIzlazSectionSKPHeader1;
import hr.restart.util.reports.raRPSectionHeaderROT;
import hr.restart.util.reports.raReportSection;

public class repRacuniVelikiKupciTemplate extends repRacuniTemplate {

  public raReportSection createSectionHeader0() {
    raRPSectionHeaderROT sh = new raRPSectionHeaderROT(this);
    sh.LabelRACUNOTPREMNICA.defaultAlterer().setCaption("\nRAÈUN");
    
    return sh;
  }

  public raReportSection createDetail() {
    raIzlazSKPDetail det = new raIzlazSKPDetail(this);
    
    det.TextCART.defaultAlterer().setControlSource("BC");
    det.TextJMPAK.defaultAlterer().setVisible(false);
    det.TextKOLPAK.defaultAlterer().setVisible(false);
    
    return det;
  }
  
  public raReportSection createSectionHeader1() {
    raIzlazSectionSKPHeader1 sh = new raIzlazSectionSKPHeader1(this);
    
    sh.LabelSifra.defaultAlterer().setCaption("EAN code");
    sh.LabelPakiranje.defaultAlterer().setVisible(false);
    sh.LabelKolicina.defaultAlterer().setHeight(sh.LabelJmj.defaultAlterer().getHeight());
    
    return sh;
  }
}

/****license*****************************************************************
**   file: repIspOS_01Template.java
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
package hr.restart.os;

import hr.restart.util.reports.raElixirPropertyValues;
import hr.restart.util.reports.raOSDetail01;
import hr.restart.util.reports.raOSPageHeader002;
import hr.restart.util.reports.raOSSectionFooter002;
import hr.restart.util.reports.raOSSectionFooter101;
import hr.restart.util.reports.raOSSectionHeader101;
import hr.restart.util.reports.raReportSection;

public class repIspOS_01Template extends repIspOSOrigTemplate {

  public repIspOS_01Template() {
    System.out.println("\nrepIspOS_01Template()\n");
    this.Section2.setDefault(ep.FIELD,"");
  }

  public raReportSection createPageHeader() {
    return new raOSPageHeader002(this);
  }
  public raReportSection createSectionHeader1() {
    raOSSectionHeader101 sh1 = new raOSSectionHeader101(this);

    sh1.TextCOrg.defaultAlterer().setVisible(false);
    sh1.TextNazOrg.defaultAlterer().setVisible(false);
    sh1.LabelOrg_jedinica.defaultAlterer().setVisible(false);
    sh1.Line1.defaultAlterer().setVisible(false);
    sh1.defaultAlterer().setHeight(0);
    sh1.setDefault(ep.GROW, raElixirPropertyValues.NO);
//    sh1.defaultAlterer().setVisible(false);
    return sh1;//new raPrazniDetail(this);//sh1;
  }
  public raReportSection createDetail() {
    raOSDetail01 det = new raOSDetail01(this);
    det.TextNazOblikListe.defaultAlterer().setControlSource("NazOrg");
    det.TextOblikListe.defaultAlterer().setControlSource("COrg");
    return det;
  }
  public raReportSection createSectionFooter1() {
    raOSSectionFooter101 sf1 = new raOSSectionFooter101(this);

    sf1.Line1.defaultAlterer().setVisible(false);
    sf1.Label1.defaultAlterer().setVisible(false);
    sf1.Label2.defaultAlterer().setVisible(false);
    sf1.Label3.defaultAlterer().setVisible(false);
    sf1.Label4.defaultAlterer().setVisible(false);
    sf1.Text1.defaultAlterer().setVisible(false);
    sf1.Text2.defaultAlterer().setVisible(false);
    sf1.LabelUkupno_org_jedinica.defaultAlterer().setVisible(false);
    sf1.Text3.defaultAlterer().setVisible(false);
    sf1.TextCOrg.defaultAlterer().setVisible(false);
    sf1.TextNazOrg.defaultAlterer().setVisible(false);
    sf1.Line2.defaultAlterer().setVisible(false);
    sf1.defaultAlterer().setHeight(0);
    sf1.setDefault(ep.GROW, raElixirPropertyValues.NO);
//    sf1.defaultAlterer().setVisible(false);
    return sf1;//new raPrazniDetail(this);//sf1;
  }
  public raReportSection createSectionFooter0() {
    raOSSectionFooter002 sf0 = new raOSSectionFooter002(this);
//    sf0.LabelS_V_E_U_K_U_P_N_O.defaultAlterer().setCaption("U K U P N O ");
    return sf0;
  }
}
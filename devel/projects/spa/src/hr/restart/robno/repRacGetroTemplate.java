/****license*****************************************************************
**   file: repRacGetroTemplate.java
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
import hr.restart.util.reports.raIzlazSectionFooterLines;
import hr.restart.util.reports.raIzlazSectionHeaderLines;
import hr.restart.util.reports.raNewDefaultPageHeader;
import hr.restart.util.reports.raRPSectionHeaderROT;
import hr.restart.util.reports.raRacGetroDetail;
import hr.restart.util.reports.raRacGetroSF1;
import hr.restart.util.reports.raReportSection;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repRacGetroTemplate extends repRacuniTemplate {

  public raReportSection createSectionHeader1() {
    return new raRacGetroSF1(this); // return new raIzlazSectionHeader(this);
  }

  public raReportSection createDetail() {
    return new raRacGetroDetail(this);
  }

  public raReportSection createSectionFooter1() {
    raIzlazSectionFooterLines foo = new raIzlazSectionFooterLines(this); // return new raIzlazSectionFooter(this);
    foo.removeModel(foo.Line1);
    return foo;
  }
}

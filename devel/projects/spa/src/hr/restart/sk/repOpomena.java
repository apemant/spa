/****license*****************************************************************
**   file: repOpomena.java
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
package hr.restart.sk;

import hr.restart.util.Util;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class repOpomena extends repIOS {
  List index;
  
  public repOpomena() {
    index = new ArrayList();

    for (int i = 0; i < ds.getRowCount(); i++) {
      ds.goToRow(i);
      if (rik.plus == 0) {
        if (!ds.getTimestamp("DATDOSP").after(rik.getLastDay()))
          index.add(new Integer(i));
      } else {
        if (!ds.getTimestamp("DATDOK").before(rik.poc) &&
            !Util.getUtil().addDays(ds.getTimestamp("DATDOK"), 30-rik.plus).after(rik.getLastDay()))
          index.add(new Integer(i));
      }
    }
  }
  
  public raReportData getRow(int i) {
    ds.goToRow(((Integer) index.get(i)).intValue());
    totals = rik.getTotals(ds.getInt("CPAR"));
    return this;
  }

  public int getRowCount() {
    return index.size();
  }
  
  public void close() {
    index.clear();
    index = null;
  }
  
  public BigDecimal getPOKAZNISALDO(){
    return rik.getSaldoDosp(getCPAR());
  }
  
  public BigDecimal getTOTALSALDO() {
    return rik.getSaldo(getCPAR());
  }
  
  public BigDecimal getDOSPSALDO() {
    return rik.getSaldoDosp(getCPAR());
  }
  
  public String getPREVID() {
    return "Prema našoj poslovnoj evidenciji na dan " + getNADAN() + 
      ", navedenim raèunima istekao je rok za uplatu.";
  }
  
  public String getNAZPARL(){
    if (lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",ds.getInt("CPAR")+""))
      return dm.getPartneri().getString("NAZPAR");
    return "";
  }
}

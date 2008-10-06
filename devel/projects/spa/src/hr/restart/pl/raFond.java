/****license*****************************************************************
**   file: raFond.java
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
package hr.restart.pl;

import hr.restart.baza.Orgpl;
import hr.restart.baza.dM;
import hr.restart.util.Util;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;

import com.borland.dx.sql.dataset.QueryDataSet;

public class raFond {
  public short godina;
  public short mjesec;
  public BigDecimal satiRad;
  public BigDecimal satiPraz;
  public BigDecimal satiUk;
  private BigDecimal nula = new BigDecimal(0.00);
  public raFond(short _godina, short _mjesec, boolean isplata) {
    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    String _knjig = hr.restart.zapod.OrgStr.getKNJCORG(false);
    lookupData ld = lookupData.getlookupData();
    if (!isplata) {
      godina = _godina;
      mjesec = _mjesec;
    } else {
      String s = raPlObrRange.getInQueryIsp(_godina, _mjesec).substring(0);
System.out.println(s);
      String[] v= new VarStr( s.substring(s.indexOf( "(" )+1,s.length()-1 )).splitTrimmed(",");
      TreeSet set = new TreeSet(Arrays.asList(v));
for (Iterator iterator = set.iterator(); iterator.hasNext();) {
  Object object = (Object) iterator.next();
  System.out.println(object);
}
      String godmj = set.last().toString();
System.out.println(godmj);
      if (godmj.trim().equals("99999999")) {
        raIniciranje.getInstance().posOrgsPl(_knjig);
        QueryDataSet orgpl = Orgpl.getDataModule().getQueryDataSet();
System.out.println("orgpl :: "+orgpl);
System.out.println(Short.parseShort(Util.getUtil().getMonth(orgpl.getTimestamp("DATUMISPL")))+"=="+_mjesec);
System.out.println(Short.parseShort(Util.getUtil().getYear(orgpl.getTimestamp("DATUMISPL")))+"=="+_godina);
        if (Short.parseShort(Util.getUtil().getMonth(orgpl.getTimestamp("DATUMISPL")))==_mjesec 
            && Short.parseShort(Util.getUtil().getYear(orgpl.getTimestamp("DATUMISPL")))==_godina) {
          godina = orgpl.getShort("GODOBR");
          mjesec = orgpl.getShort("MJOBR");
        }
      } else {
        godina = Short.parseShort(godmj.substring(0,4));
        mjesec = Short.parseShort(godmj.substring(4,6));
      }
    }
System.out.println("raFond :: g: "+godina+" m: "+mjesec+" knj: "+_knjig);    
    dm.getFondSati().open();
    if (ld.raLocate(dm.getFondSati(),new String[] {"KNJIG","GODINA","MJESEC"},
    new String[] {_knjig,godina+"",mjesec+""})) {
      satiRad = dm.getFondSati().getBigDecimal("SATIRAD");
      satiPraz = dm.getFondSati().getBigDecimal("SATIPRAZ");
      satiUk = dm.getFondSati().getBigDecimal("SATIUK");
    } else {
      satiRad = nula;
      satiPraz = nula;
      satiUk = nula;
    }
  }
  public raFond(short _godina, short _mjesec) {
    this(_godina, _mjesec, false);
  }
}


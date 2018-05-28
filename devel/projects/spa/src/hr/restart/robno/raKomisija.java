/****license*****************************************************************
**   file: raKomisija.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Stdoku;
import hr.restart.baza.dM;
import hr.restart.util.lookupData;
import hr.restart.util.raMatPodaci;
import hr.restart.zapod.OrgStr;

import java.math.BigDecimal;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;


public class raKomisija {
  
  dM dm = dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
    
  public static final raKomisija inst = new raKomisija();

  public raKomisija() {
    // TODO Auto-generated constructor stub
  }
  
  public boolean isKomisija() {
    return OrgStr.getOrgStr().isKomisija();
  }
  
  public boolean checkIzlaz(raMatPodaci mp, BigDecimal old) {
    if (!OrgStr.getOrgStr().isKomisija()) return true;
    
    BigDecimal needk = mp.getRaQueryDataSet().getBigDecimal("KOL");
    if (needk.signum() < 0) return true;
    if (old != null && old.signum() != 0 && needk.compareTo(old) < 0) return true;
    //if (needk.signum() < 0) return true;
    
    DataSet ds = Stdoku.getDataModule().getTempSet("KOMISIJA ID_STAVKA CPARKOM KOLKOM", 
        Condition.whereAllEqual("CSKL GOD", mp.getRaQueryDataSet()));
    if (ds.rowCount() == 0) {
      JOptionPane.showMessageDialog(mp.getWindow(),
          "Nema ulaznih stavaka za razduženje komisije !",
          "Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    //DataSet vez = 
    
    /*BigDecimal ownTotal = Aus.zero2;
    BigDecimal komTotal = Aus.zero2;
    BigDecimal ownMax = Aus.zero2;
    BigDecimal komMax = Aus.zero2;
    
    for (ds.first(); ds.inBounds(); ds.next()) {
      BigDecimal kolkom = ds.getBigDecimal("KOLKOM");
      if (old != null && old.signum() != 0 && ds.getString("ID_STAVKA").equals(ds.getBigDecimal("STAVKOM")))
        kolkom = kolkom.add(rKD.stavkaold.kol);
      if (ds.isNull("CPARKOM") || ds.getInt("CPARKOM") == 0) {
        ownTotal = ownTotal.add(kolkom);
        if (kolkom.compareTo(ownMax) > 0) ownMax = kolkom;
      } else {
        komTotal = komTotal.add(kolkom);
        if (kolkom.compareTo(komMax) > 0) komMax = kolkom;
      }
    }
    
    if (ownTotal.add(komTotal).compareTo(needk) < 0) {
      JOptionPane.showMessageDialog(raDetail.getWindow(),
          "Nema dovoljno ulaznih stavaka za razduženje !",
          "Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }*/
    return true;  
  }

}

/****license*****************************************************************
**   file: raObrKumGK.java
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
package hr.restart.gk;

import hr.restart.baza.dM;
import hr.restart.util.lookupData;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKonta;

import java.math.BigDecimal;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class raObrKumGK {
  dM dm = dM.getDataModule();
  lookupData ld = lookupData.getlookupData();

  private static raObrKumGK inst;
  private StorageDataSet konta = new StorageDataSet();

  public static raObrKumGK getInstance() {
    if (inst == null) new raObrKumGK();
    return inst;
  }

  private raObrKumGK() {
    try {
      inst = this;
      init();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void init() throws Exception {
    konta.setColumns(new Column[] {
      dM.createStringColumn("BROJKONTA", 8),
      dM.createStringColumn("CORG", 12),
      dM.createBigDecimalColumn("ID", 2),
      dM.createBigDecimalColumn("IP", 2),
    });
    konta.open();
  }
  
  public DataSet createKontaSet(final QueryDataSet source, boolean ps) {    
    String knjig = OrgStr.getKNJCORG(false);
    konta.empty();
    String lastKonto = "", lastCorg = "";
    boolean kumCorg = false;
    String[] cols = new String[] {"BROJKONTA", "ID", "IP"};
    for (source.first(); source.inBounds(); source.next()) {
      if (!source.getString("BROJKONTA").equals(lastKonto)) {
        lastKonto = source.getString("BROJKONTA");
        lastCorg = source.getString("CORG");
        //ld.raLocate(dm.getKonta(), "BROJKONTA", lastKonto);
        kumCorg = ps ? !raKonta.isOrgStrPS(lastKonto) : !raKonta.isOrgStr(lastKonto); 
        //  dm.getKonta().getString("ORGSTR").equals("N");
        konta.insertRow(false);
        konta.setString("CORG", kumCorg ? knjig : lastCorg);
        dM.copyColumns(source, konta, cols);
      } else if (!kumCorg && !source.getString("CORG").equals(lastCorg)) {
        lastCorg = source.getString("CORG");
        konta.insertRow(false);
        konta.setString("CORG", lastCorg);
        dM.copyColumns(source, konta, cols);
      } else {
        konta.setBigDecimal("ID", konta.getBigDecimal("ID").add(source.getBigDecimal("ID")));
        konta.setBigDecimal("IP", konta.getBigDecimal("IP").add(source.getBigDecimal("IP")));
      }
    }
    return konta;
  }
  
  public void clearKontaSet() {
    konta.empty();
  }

  public boolean createTem(frmKnjizenje frm, final QueryDataSet kum, String opis, 
      boolean noSave, boolean invert) {
    frm.setProcessMessage("Dohvat kumulativa ...");
    kum.open();
    frm.getKnjizenje().setSKRacKnj(noSave);
    if (!frm.getKnjizenje().startKnjizenje(frm)) return false;
    frm.setProcessMessage("Izra\u010Dun salda ...");
    createKontaSet(kum, !invert);
//    sys.prn(konta);
    frm.setProcessMessage("Formiranje temeljnice...");
    BigDecimal n0 = new BigDecimal(0.);
    for (konta.first(); konta.inBounds(); konta.next())
      if (!noSave || !raKonta.isSaldak(konta.getString("BROJKONTA"))) {
        BigDecimal saldo = konta.getBigDecimal("ID").subtract(konta.getBigDecimal("IP"));
        int diff = saldo.signum();
        BigDecimal id = n0, ip = n0;
        if (diff < 0) ip = saldo.negate();
        if (diff > 0) id = saldo;
        if (diff != 0) {
          StorageDataSet stavka = frm.getKnjizenje().
                                  getNewStavka(konta.getString("BROJKONTA"), konta.getString("CORG"));
          frm.getKnjizenje().setID(invert ? ip : id);
          frm.getKnjizenje().setIP(invert ? id : ip);
          stavka.setString("OPIS",opis);
          if (!frm.getKnjizenje().saveStavka()) {
            konta.empty();
            return false;
          }
        }
      }
    konta.empty();
    return noSave || frm.getKnjizenje().saveAll();
  }
}

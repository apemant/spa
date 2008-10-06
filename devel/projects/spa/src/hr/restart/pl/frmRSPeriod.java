/****license*****************************************************************
**   file: frmRSPeriod.java
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

import hr.restart.baza.dM;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.startFrame;

import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

public class frmRSPeriod extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  private StorageDataSet primanjaobr;
  private static frmRSPeriod _this;
  private short zadnjidan;
  private short prvidan;
  jpRSPeriod jpDetail;
  private String cradnik;
  private String corg;

  public frmRSPeriod() {
    super(2);
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static frmRSPeriod go(StorageDataSet _primanjaobr) {
    if (_this == null)
      _this = new frmRSPeriod();
    startFrame.getStartFrame().centerFrame(_this, 0, "Period za RS");
    _this.setPrimanja(_primanjaobr);
    startFrame.getStartFrame().showFrame(_this);
    return _this;
  }

  public void setPrimanja(StorageDataSet _primanjaobr) {
    primanjaobr = _primanjaobr;
    cradnik = primanjaobr.getString("CRADNIK");
    ld.raLocate(dm.getRadnici(), new String[] { "CRADNIK" },
        new String[] { cradnik });
    corg = dm.getRadnici().getString("CORG");
    dm.getRSPeriod().close();
    dm.getRSPeriod().setQuery(
        new QueryDescriptor(dm.getDatabase1(),
            "SELECT * FROM rsperiod where cradnik = '" + cradnik + "'"));
    dm.getRSPeriod().setSort(
        new SortDescriptor(new String[] { "CRADNIK", "RBR" }));
    dm.getRSPeriod().open();
    getJpTableView().fireTableDataChanged();
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      setDefaults();
      jpDetail.jlrRsoo.forceFocLost();
      jpDetail.jlrRsoo.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraOddana.requestFocus();
    }
  }

  TreeSet existOO = new TreeSet();

  void parseOOs() {
    existOO.add(""); //zbog getNextOO() kada nema ni jednog perioda;
    Variant v = new Variant();
    for (int i = 0; i < primanjaobr.getRowCount(); i++) {
      primanjaobr.getVariant("CVRP", i, v);
      ld.raLocate(dm.getVrsteprim(), new String[] { "CVRP" }, new String[] { v
          .toString() });
      existOO.add(dm.getVrsteprim().getString("RSOO"));
    }
  }

  void setDefaults() {
    parseOOs();
    QueryDataSet laststavka = Util
        .getNewQueryDataSet("SELECT max(DODANA),max(RSOO) from rsperiod where cradnik = '"
            + cradnik + "'");
    zadnjidan = getZadnjiDan();
    prvidan = (short) (laststavka.getShort(0) + 1);
    if (prvidan > zadnjidan)
      prvidan = zadnjidan;
    String nextOO = getNextOO(laststavka.getString(1));
    getRaQueryDataSet().setShort("ODDANA", prvidan);
    getRaQueryDataSet().setShort("DODANA", zadnjidan);
    getRaQueryDataSet().setString("RSOO", nextOO);
  }

  private short getZadnjiDan() {
    raIniciranje.getInstance().posOrgsPl(corg);
    short godina = dm.getOrgpl().getShort("GODOBR");
    short mjesec = dm.getOrgpl().getShort("MJOBR");
    //    Util.getUtil().getLastDayOfMonth(new
    // java.sql.Timestamp(System.currentTimeMillis()));
    return getZadnjiDan(godina, mjesec);
  }

  static short getZadnjiDan(short _godina, short _mjesec) {
    java.util.Calendar kalendaros = java.util.Calendar.getInstance();
    kalendaros.set(_godina, _mjesec - 1, 1);
    java.sql.Date d = new java.sql.Date(kalendaros.getTime().getTime());
    java.sql.Timestamp tsd = Util.getUtil().getLastDayOfMonth(
        new java.sql.Timestamp(d.getTime()));
    return Short.parseShort(tsd.toString().substring(8, 10));
    /*
     * if (_mjesec == 2) { if (_godina%4 == 0) { return (short)29; } else return
     * (short)28; } if (_mjesec == 8) return (short)31; if (_mjesec < 8 &&
     * _mjesec%2 == 0) return (short)30; else if (_mjesec > 8 && _mjesec%2 == 0)
     * return (short)31; else if (_mjesec < 8 && _mjesec%2 != 0) return
     * (short)31; else return (short)30; // else return (short)30;
     */
  }

  private String getNextOO(String zadnji) {
    for (Iterator i = existOO.iterator(); i.hasNext();) {
      String item = i.next().toString();
      if (item.equals(zadnji) && i.hasNext()) {
        return i.next().toString();
      }
    }
    return zadnji;
  }

  public boolean Validacija(char mode) {
    getRaQueryDataSet().setString("CRADNIK", cradnik);
    getRaQueryDataSet().setInt(
        "RBR",
        Util.getNewQueryDataSet(
            "SELECT max(RBR) from rsperiod where cradnik = '" + cradnik + "'")
            .getInt(0) + 1);
    if (vl.isEmpty(jpDetail.jlrRsoo) || vl.isEmpty(jpDetail.jraOddana)
        || vl.isEmpty(jpDetail.jraDodana))
      return false;
    if (!chkDanMsg())
      return false;
    //    if (mode == 'N' && vl.notUnique()) /**@todo: Provjeriti jedinstvenost
    // kljuca */
    //      return false;
    return true;
  }

  boolean chkDanMsg() {
    if (chkDan())
      return true;
    JOptionPane.showMessageDialog(jpDetail, "Neispravan datumski period!",
        "Greška", JOptionPane.ERROR_MESSAGE);
    return false;
  }

  boolean chkDan() {
    short oddana = getRaQueryDataSet().getShort("ODDANA");
    short dodana = getRaQueryDataSet().getShort("DODANA");
    return chkDan(oddana, dodana, zadnjidan);
    /** @todo kontrola preklapanja perioda */
  }

  public static boolean chkDan(short oddana, short dodana, short zadnji) {
    if (dodana > zadnji)
      return false;
    if (oddana < 1)
      return false;
    if (oddana > dodana)
      return false;
    return true;
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getRSPeriod());
    this.setVisibleCols(new int[] { 2, 3, 4 });
    jpDetail = new jpRSPeriod(this);
    this.setRaDetailPanel(jpDetail);
  }
}
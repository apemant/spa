/****license*****************************************************************
**   file: frmKamate.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.sisfun.Asql;
import hr.restart.swing.raTableValueModifier;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;

import java.awt.event.KeyEvent;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;


public class frmKamate extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpKamateMaster jpMaster;
  jpKamateDetail jpDetail;

  QueryDataSet mast = new QueryDataSet() {
    public boolean saveChangesSupported() {
      return false;
    }
  };


  public frmKamate() {
    super(1,2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void beforeShowMaster() {
    mast.refresh();
    this.getDetailSet().open();
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jpMaster.jraCkam, false);
    }
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      jpMaster.jraCkam.requestFocus();
    } else if (mode == 'I') {
      jpMaster.jraOpis.requestFocus();
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jraCkam))
      return false;
    if (mode == 'N' && MasterNotUnique()) {
      jpMaster.jraCkam.requestFocus();
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
           "Tablica kamata s istom šifrom ve\u0107 postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
    }
    if (mode == 'I') {
      Asql.propagateMasterChanges("kamate", mast, new String[] {"CKAM"}, new String[] {"OPIS"});
    }
    mast.post();
    return true;
  }

  private boolean MasterNotUnique() {
    vl.execSQL("SELECT * FROM kamate WHERE ckam="+getMasterSet().getInt("CKAM"));
    vl.RezSet.open();
    return vl.RezSet.rowCount() > 0;
  }

  public void ZatvoriOstaloDetail() {
    int row = this.getMasterSet().getRow();
      this.getMasterSet().refresh();
      raMaster.getJpTableView().fireTableDataChanged();
      this.getMasterSet().goToClosestRow(row);
  }

  public void refilterDetailSet() {
    super.refilterDetailSet();
    this.setNaslovDetail("Stope tablice kamata " + getMasterSet().getInt("CKAM") +
                         " - " + getMasterSet().getString("OPIS"));
  }

  public void EntryPointDetail(char mode) {
    // Disabla tekst komponentu kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraDatum, false);
    }
    rcc.setLabelLaF(jpDetail.jraDani, false);
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      jpDetail.jraDatum.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jraStopa.requestFocus();
    }
  }

  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jpDetail.jraDatum) || vl.isEmpty(jpDetail.jraStopa))
      return false;
    if (mode == 'N' && notUnique()) /**@todo: Provjeriti jedinstvenost kljuca detaila */
      return false;
    getDetailSet().setString("OPIS", getMasterSet().getString("OPIS"));
    setDani();
    return true;
  }

  public void setDani() {
    int[] days = new int[] {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    Timestamp datum = getDetailSet().getTimestamp("DATUM");
    if (getDetailSet().getString("VRSTA").equalsIgnoreCase("G")) {
      getDetailSet().setInt("DANI", Aus.isYearExt(datum) ? 366 : 365);
    } else {
      int month = Aus.getNumber(Util.getUtil().getMonth(datum));
      if (month != 2) getDetailSet().setInt("DANI", days[month - 1]);
      else getDetailSet().setInt("DANI", Aus.isYearExt(datum) ? 29 : 28);
    }
  }

  public boolean notUnique() {
    vl.execSQL("SELECT * FROM kamate WHERE ckam="+getMasterSet().getInt("CKAM")+
               " AND "+Condition.where("datum", Condition.EQUAL,
               getDetailSet().getTimestamp("DATUM")));
    vl.RezSet.open();
    return vl.RezSet.rowCount() > 0;
  }

  private void jbInit() throws Exception {
    Asql.createMasterKamate(mast);

    this.setMasterSet(mast);
    this.setNaslovMaster("Tablice kamata");
    this.setVisibleColsMaster(new int[] {0, 1});
    this.setMasterKey(new String[] {"CKAM"});
    jpMaster = new jpKamateMaster(this);
    this.setJPanelMaster(jpMaster);

    this.setDetailSet(dm.getKamate());
    this.setNaslovDetail("Stope tablice kamata");
    this.setVisibleColsDetail(new int[] {2, 3, 4, 5});
    this.setDetailKey(new String[] {"CKAM", "DATUM"});
    jpDetail = new jpKamateDetail(this);
    this.setJPanelDetail(jpDetail);

    jpDetail.setBorder(null);

    raDetail.getJpTableView().addTableModifier(new raTableValueModifier("VRSTA",
        new String[] {"G", "M"}, new String[] {" Godišnja", " Mjese\u010Dna"}));

    Aus.removeSwingKeyRecursive(raDetail.getContentPane(), KeyEvent.VK_F8);
  }
}

/****license*****************************************************************
**   file: frmVrsteOdb.java
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
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.startFrame;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

public class frmVrsteOdb extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();

  dM dm = dM.getDataModule();

  Valid vl = Valid.getValid();

  Hashtable comboDS = new Hashtable();

  Hashtable DScombo = new Hashtable();

  raNavAction rnvStOdb = new raNavAction("Standardni odbici",
      raImages.IMGPREFERENCES, KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      stOdb_action();
    }
  };

  raNavAction rnvOdbCorg = new raNavAction("Povjerioc za druge OJ",
      raImages.IMGALIGNJUSTIFY, KeyEvent.VK_F8) {
    public void actionPerformed(ActionEvent e) {
      odbCorg_action();
    }
  };

  jpVrsteOdb jpDetail;

  frmOdbCorg fodbcorg;

  public frmVrsteOdb() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jraCvrodb, false);
    }
  }

  public void SetFokus(char mode) {
    rcc.setLabelLaF(jpDetail.jraIznos, true);
    rcc.setLabelLaF(jpDetail.jraStopa, false);
    if (mode == 'N') {
      jpDetail.jlrCpov.forceFocLost();
      jpDetail.jraCvrodb.requestFocus();
      jpDetail.jcbNivo1Main.setSelectedIndex(0);
      jpDetail.jcbTip.setSelectedItem("Stvarni");
      jpDetail.jcbVrOs.setSelectedItem("Neto");
      jpDetail.jcbOsOb.setSelectedItem("Fiksni iznos");
    } else if (mode == 'I') {
      setComboItems();
      rcc.setLabelLaF(jpDetail.jcbNivo1, false);
      rcc.setLabelLaF(jpDetail.jcbNivo1Main, false);
      rcc.setLabelLaF(jpDetail.jcbNivo2, false);
      jpDetail.jraOpisvrodb.requestFocus();
    } else {
      rcc.setLabelLaF(jpDetail.jraIznos, false);
    }
  }

  public void detailViewShown(char mod) {
    char[] test = new char[] { mod };
    if (mod == 'B')
      setComboItems();
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jraCvrodb) || vl.isEmpty(jpDetail.jraOpisvrodb)
        || vl.isEmpty(jpDetail.jlrCpov))
      return false;
    if (jpDetail.jraIznos.isEnabled()) {
      if (vl.isEmpty(jpDetail.jraIznos))
        return false;
    }

    if (jpDetail.jraStopa.isEnabled()) {
      if (vl.isEmpty(jpDetail.jraStopa))
        return false;
    }

    if (mode == 'N' && vl.notUnique(jpDetail.jraCvrodb))
      return false;
    String strNivoOdb = "";
    if (jpDetail.jcbNivo1.isVisible()) {
      String temp1 = jpDetail.sDS.getString("NIVO1");
      String temp2 = jpDetail.sDS.getString("NIVO2");
      strNivoOdb = temp1 + temp2;
    } else {
      strNivoOdb = jpDetail.sDS.getString("NIVO");
    }
    if (strNivoOdb.equals(""))
      strNivoOdb = "ZA";
    this.getRaQueryDataSet().setString("NIVOODB", strNivoOdb);
    return true;
  }

  private void jbInit() throws Exception {
    this.addOption(rnvStOdb, 3);
    this.addOption(rnvOdbCorg, 3);
    this.setRaQueryDataSet(dm.getVrsteodb());
    this.setVisibleCols(new int[] { 0, 1 });
    jpDetail = new jpVrsteOdb(this);
    fodbcorg = new frmOdbCorg();
    this.setRaDetailPanel(jpDetail);
    setComboHash();
    raDataIntegrity.installFor(this);
  }

  public void setComboHash() {
    comboDS.put("", "");
    comboDS.put("Primanja", "ZA");
    comboDS.put("Radnici", "RA");
    comboDS.put("Op\u0107ine", "OP");
    comboDS.put("Org. jedinice", "OJ");
    comboDS.put("Poduze\u0107e", "PO");
    comboDS.put("Vrsta radnog odnosa", "VR");

    DScombo.put("", "");
    DScombo.put("ZA", "Primanja");
    DScombo.put("RA", "Radnici");
    DScombo.put("OP", "Op\u0107ine");
    DScombo.put("OJ", "Org. jedinice");
    DScombo.put("PO", "Poduze\u0107e");
    DScombo.put("VR", "Vrsta radnog odnosa");
  }

  public void setComboItems() {
    String combos = getRaQueryDataSet().getString("NIVOODB");
    String strCombo1 = "";
    String strCombo2 = "";
    if (combos.length() == 2) {
      strCombo1 = DScombo.get(combos).toString();
      jpDetail.jcbNivo1Main.setSelectedItem(strCombo1);
    }
    if (combos.length() == 4) {
      strCombo1 = DScombo.get(combos.substring(0, 2)).toString();
      strCombo2 = DScombo.get(combos.substring(2, 4)).toString();

      jpDetail.jcbNivo1.setVisible(true);
      jpDetail.jcbNivo2.setVisible(true);
      jpDetail.jcbNivo1Main.setSelectedItem("2 nivoa odbitka");
      jpDetail.jcbNivo1.setSelectedItem(strCombo1);
      jpDetail.jcbNivo2.setSelectedItem(strCombo2);
    }
  }
/*
  public boolean BeforeDelete() {
    if (plUtil.getPlUtil().checkVrOdbStavke(
        this.getRaQueryDataSet().getShort("CVRODB"))) {
      JOptionPane.showConfirmDialog(this, "Nisu pobrisane stavke !", "Greška!",
          JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }
*/
  private void stOdb_action() {
    if (getRaQueryDataSet().getRowCount() == 0)
      return;
    frmStandOdb frmSO = new frmStandOdb(this.getRaQueryDataSet().getShort(
        "CVRODB"));
    frmSO.show();
  }

  private void odbCorg_action() {
    if (getRaQueryDataSet().getRowCount() == 0)
      return;
    fodbcorg.setCvrodb(getRaQueryDataSet().getShort("CVRODB"));
    startFrame.getStartFrame().centerFrame(fodbcorg, 0, fodbcorg.getTitle());
    startFrame.getStartFrame().showFrame(fodbcorg);
  }
}


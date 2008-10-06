/****license*****************************************************************
**   file: frmKljucevi.java
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
package hr.restart.sisfun;

import hr.restart.baza.Kljucevi;
import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavBar;

import java.util.StringTokenizer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmKljucevi extends raMatPodaci {

  raCommonClass rcc = raCommonClass.getraCommonClass();

  dM dm = dM.getDataModule();

  Valid vl = Valid.getValid();

  JPanel jpDetail = new JPanel();

  XYLayout layDetail = new XYLayout();

  JLabel jlCuser = new JLabel();

  JLabel jlDatum = new JLabel();

  JLabel jlImetab = new JLabel();

  JLabel jlKeys = new JLabel();

  JLabel jlValues = new JLabel();

  JraTextField jraCuser = new JraTextField();

  JraTextField jraDatum = new JraTextField();

  JraTextField jraImetab = new JraTextField();

  JraTextField jraKeys = new JraTextField();

  JraTextField jraValues = new JraTextField();

  private String[] keys = null, vals = null;

  private String table;

  public frmKljucevi() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void beforeShow() {
    getRaQueryDataSet().refresh();
  }

  public void EntryPoint(char mode) {
    rcc.EnabDisabAll(jpDetail, false);
    rcc.setLabelLaF(getOKpanel().jBOK, false);
  }

  public void SetFokus(char mode) {
    //     this.getOKpanel().jPrekid_actionPerformed();
  }

  public boolean Validacija(char mode) {
    return true;
  }

  public boolean DeleteCheck() {
    table = this.getRaQueryDataSet().getString("IMETAB").toUpperCase();
    StringTokenizer sk = new StringTokenizer(this.getRaQueryDataSet()
        .getString("KL_KEYS"), ";");
    StringTokenizer sv = new StringTokenizer(this.getRaQueryDataSet()
        .getString("VALS"), ";");
    int num = Math.min(sk.countTokens(), sv.countTokens());
    if (num == 0) {
      keys = vals = null;
      return true;
    }
    keys = new String[num];
    vals = new String[num];
    for (int i = 0; i < num; i++) {
      keys[i] = sk.nextToken();
      vals[i] = sv.nextToken();
    }
    if (table.equals("USERI") && keys[0].equals("CUSER")
        && vals[0].equals(raUser.getInstance().getUser())) {
      JOptionPane.showMessageDialog(getJpTableView().getRootPane(),
          "Nije mogu\u0107e otklju\u010Dati aktivnog korisnika!", "Greška",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public void AfterDelete() {
    if (keys != null)
      raUser.getInstance().unlockRows(table, keys, vals);
    else
      raUser.getInstance().unlockTable(table);
  }

  private void jbInit() throws Exception {
    this.setRaDetailPanel(this.jpDetail);
    this.setRaQueryDataSet(Kljucevi.getDataModule().copyDataSet());
    this.setVisibleCols(new int[] {
        0, 1, 2, 3, 4
    });

    jpDetail.setLayout(layDetail);
    layDetail.setWidth(505);
    layDetail.setHeight(130);

    jlCuser.setText("Korisnik");
    jlDatum.setText("Datum");
    jlImetab.setText("Tablica");
    jlKeys.setText("Kolone");
    jlValues.setText("Vrijednosti");
    jraCuser.setColumnName("CUSER");
    jraCuser.setDataSet(this.getRaQueryDataSet());
    jraDatum.setColumnName("DATUM");
    jraDatum.setDataSet(this.getRaQueryDataSet());
    jraImetab.setColumnName("IMETAB");
    jraImetab.setDataSet(this.getRaQueryDataSet());
    jraKeys.setColumnName("KL_KEYS");
    jraKeys.setDataSet(this.getRaQueryDataSet());
    jraValues.setColumnName("VALS");
    jraValues.setDataSet(this.getRaQueryDataSet());

    jpDetail.add(jlCuser, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlDatum, new XYConstraints(300, 20, -1, -1));
    jpDetail.add(jlImetab, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlKeys, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlValues, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jraCuser, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jraDatum, new XYConstraints(390, 20, 100, -1));
    jpDetail.add(jraImetab, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jraKeys, new XYConstraints(150, 70, 340, -1));
    jpDetail.add(jraValues, new XYConstraints(150, 95, 340, -1));

    removeRnvCopyCurr();
    this.getNavBar().removeStandardOptions(
        new int[] {
            raNavBar.ACTION_ADD, raNavBar.ACTION_UPDATE,
            raNavBar.ACTION_TOGGLE_TABLE
        });
    //    this.disableAdd();
    //    this.setEditEnabled(false);
  }
}
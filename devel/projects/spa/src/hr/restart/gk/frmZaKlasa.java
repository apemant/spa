/****license*****************************************************************
**   file: frmZaKlasa.java
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
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTextMask;
import hr.restart.util.Aus;
import hr.restart.zapod.OrgStr;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class frmZaKlasa extends frmKnjizenje {

  JLabel jlGod = new JLabel();
  JLabel jlOdg = new JLabel();
  JraTextField jraDog = new JraTextField();
  JraTextField jraGod = new JraTextField();
  JraTextField jraOdg = new JraTextField();

  StorageDataSet zds = new StorageDataSet();

  public frmZaKlasa() {
    try {
      init();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void init() throws Exception {
    jpCommon.remove(jlDATUMKNJ);
    jpCommon.remove(jtDATUMKNJ);
    jpCommon.remove(jlDATUMDO);
    jtDATUMDO.setVisible(false);
    jtDATUMDO.setEnabled(false);
    jpCommon.remove(jtDATUMDO);

    zds.setColumns(new Column[] {
      dM.createStringColumn("ODG", 4),
      dM.createStringColumn("DOG", 4),
      dM.createIntColumn("GOD")
    });
    zds.open();

    jlGod.setText("Godina");
    jlOdg.setText("Grupa (od - do)");
    jraDog.setColumnName("DOG");
    jraDog.setDataSet(zds);
    new raTextMask(jraDog, 4, false, raTextMask.DIGITS);
    jraDog.setHorizontalAlignment(SwingConstants.CENTER);
    jraGod.setColumnName("GOD");
    jraGod.setDataSet(zds);
    new raTextMask(jraGod, 4, false, raTextMask.DIGITS);
    jraGod.setHorizontalAlignment(SwingConstants.CENTER);
    jraOdg.setColumnName("ODG");
    jraOdg.setDataSet(zds);
    new raTextMask(jraOdg, 4, false, raTextMask.DIGITS);
    jraOdg.setHorizontalAlignment(SwingConstants.CENTER);

    jpCommon.add(jlOdg, new XYConstraints(15, 45, -1, -1));
    jpCommon.add(jraDog, new XYConstraints(203, 45, 47, -1));
    jpCommon.add(jraOdg, new XYConstraints(150, 45, 47, -1));
    jpCommon.add(jraGod, new XYConstraints(470, 45, 50, -1));
    jpCommon.add(jlGod, new XYConstraints(395, 45, -1, -1));
    jpCommon.add(jlDATUMKNJ, new XYConstraints(15, 70, -1, -1));
    jpCommon.add(jtDATUMKNJ, new XYConstraints(150, 70, 100, -1));
    jpCommon.add(jtDATUMDO, new XYConstraints(255, 70, 100, -1));
  }

  public void initInputValues() {
    super.initInputValues();
    zds.setString("ODG", "");
    zds.setString("DOG", "");
    zds.setInt("GOD", Aus.getNumber(ut.getYear(vl.getToday())) - 1);
    dataSet.setTimestamp("DATUMKNJ", ut.getLastDayOfYear(ut.addYears(vl.getToday(), -1)));

  }

  public boolean Validacija() {
    if (vl.isEmpty(jraOdg) || vl.isEmpty(jraDog) || vl.isEmpty(jraGod)) return false;
    if (jraOdg.getText().compareTo(jraDog.getText()) > 0) {
      jraOdg.requestFocus();
      JOptionPane.showMessageDialog(this, "Po\u010Detna granica je ve\u0107a od završne!",
                                    "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (zds.getInt("GOD") > Aus.getNumber(ut.getYear(vl.getToday())) ||
        zds.getInt("GOD") < 1970) {
      jraGod.requestFocus();
      JOptionPane.showMessageDialog(this, "Pogrešna godina!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public boolean okPress() {
    String odg = zds.getString("ODG");
    String dog = (zds.getString("DOG") + Aus.string(8, '9')).substring(0, 8);
    String odgod = String.valueOf(zds.getInt("GOD")) + "01";
    String dogod = String.valueOf(zds.getInt("GOD")) + "12";
    String knjig = OrgStr.getKNJCORG(false);
    String opis = "Zaklju\u010Dno knjiženje grupa "+zds.getString("ODG")+" - "+
                  zds.getString("DOG")+" za "+zds.getInt("GOD")+". godinu";
    QueryDataSet kum = ut.getNewQueryDataSet("SELECT corg,brojkonta,godmj,id,ip "+
        "FROM gkkumulativi WHERE knjig = '"+knjig+
        "' AND (brojkonta BETWEEN '"+odg+"' AND '"+dog+"') AND (godmj BETWEEN '"+
        odgod+"' AND '"+dogod+"') ORDER BY brojkonta,corg", false);
    return raObrKumGK.getInstance().createTem(this, kum, opis, false, true);
  }
}

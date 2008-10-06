/****license*****************************************************************
**   file: frmSalKonOK.java
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
import hr.restart.baza.Skstavke;
import hr.restart.baza.Skstavkerad;
import hr.restart.baza.UIstavke;
import hr.restart.baza.dM;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;

import java.awt.event.KeyEvent;
import java.math.BigDecimal;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class frmSalKonOK extends frmSalKon {

  public frmSalKonOK() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  protected void setTitleMaster() {
    this.setNaslovMaster("Obavijesti knjiženja " + (ira ? "kupcima" : "dobavlja\u010Da"));
  }

  protected void setTitleDetail() {
    setNaslovDetail("Stavke knjižne obavijesti "+this.getMasterSet().getString("BROJDOK")+" od "+
       hr.restart.robno.raDateUtil.getraDateUtil().dataFormatter(this.getMasterSet().getTimestamp("DATDOK")));
  }

  protected boolean checkIznosMaster() {
    return true;
  }

  protected boolean checkIznosDetail() {
    return true;
  }

  protected void initPreselect() {
    pres = new presSalKon(getMasterSet(), "VRDOK", "OKD", "OKK");
  }

  private void getStorno() {
    String[] pkey = {"CPAR", "BROJDOK"};
    Skstavke.getDataModule().setFilter("knjig = '"+pres.getSelRow().getString("KNJIG")+
      "' AND vrdok = '"+vrdokSheme+(knj.equals("") ? "" : ("' AND cknjige='"+knj))+
      "' AND brojizv = 0 AND "+Condition.between("DATPRI", pres.getSelRow().getTimestamp("DATPRI-from"),
      pres.getSelRow().getTimestamp("DATPRI-to")));
    System.out.println(dm.getSkstavke().getQuery().getQueryString());
    dm.getSkstavke().open();
    String[] res = lookupData.getlookupData().lookUp(raMaster.getWindow(), dm.getSkstavke(),
        pkey, new String[] {"", ""}, new int[] {1,2,4,8,19});
    System.out.println(res);
    if (res != null) System.out.println(VarStr.join(res, ", "));
    if (res != null && res.length == 2 && res[1] != null && res[1].length() > 0) {
      if (lookupData.getlookupData().raLocate(dm.getSkstavke(), pkey, res)) {
        if (dm.getSkstavke().getBigDecimal("ID").add(dm.getSkstavke().getBigDecimal("IP")).
            compareTo(dm.getSkstavke().getBigDecimal("SALDO")) != 0) {
          if (JOptionPane.showConfirmDialog(jpMaster, "Ra\u010Dun je " +
                "(potpuno ili djelomi\u010Dno) pokriven! Stornirati ipak?", "Upozorenje", 
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        }
        if (!raSaldaKonti.isDomVal(dm.getSkstavke()) && !lookupData.getlookupData().
            raLocate(dm.getValute(), "OZNVAL", dm.getSkstavke().getString("OZNVAL"))) {
          JOptionPane.showMessageDialog(jpMaster, "Ra\u010Dun je u nepostojeæoj valuti!",
                                        "Greška", JOptionPane.ERROR_MESSAGE);
          return;
        }
        createStorno();
        getMasterSet().refresh();
        raMaster.getJpTableView().fireTableDataChanged();
        raMaster.jeprazno();
      }
    }
  }

  private void createStorno() {
    UIstavke.getDataModule().setFilter(Condition.whereAllEqual(
        new String[] {"KNJIG", "CPAR", "VRDOK", "BROJDOK"}, dm.getSkstavke()));
    dm.getUIstavke().open();
    QueryDataSet skr = Skstavkerad.getDataModule().getTempSet("1=0");
    skr.open();
    String[] skscols = {"KNJIG", "CPAR", "BROJDOK", "DATPRI", "DATDOK", "EXTBRDOK",
      "DATDOSP", "DATUNOS", "CNACPL", "OZNVAL", "TECAJ"};
    String[] uiscols = {"RBS", "CSKL", "STAVKA", "CORG", "CKOLONE",
      "CKNJIGE", "DUGPOT", "URAIRA", "BROJKONTA"};

    BigDecimal jedval = raSaldaKonti.getJedVal(dm.getSkstavke().getString("OZNVAL"));
    for (dm.getUIstavke().first(); dm.getUIstavke().inBounds(); dm.getUIstavke().next()) {
      skr.insertRow(false);
      dM.copyColumns(dm.getSkstavke(), skr, skscols);
      dM.copyColumns(dm.getUIstavke(), skr, uiscols);
      skr.setString("VRDOK", vrdok);
      skr.setInt("BROJIZV", 0);
      skr.setBigDecimal("PVID", dm.getUIstavke().getBigDecimal("ID").negate());
      skr.setBigDecimal("PVIP", dm.getUIstavke().getBigDecimal("IP").negate());
      if (raSaldaKonti.isDomVal(dm.getSkstavke())) {
        skr.setBigDecimal("ID", dm.getUIstavke().getBigDecimal("ID").negate());
        skr.setBigDecimal("IP", dm.getUIstavke().getBigDecimal("IP").negate());
      } else {
        skr.setBigDecimal("ID", dm.getUIstavke().getBigDecimal("ID").
          divide(raSaldaKonti.calcTecaj(dm.getSkstavke()), 2, BigDecimal.ROUND_HALF_UP).negate());
        skr.setBigDecimal("IP", dm.getUIstavke().getBigDecimal("IP").
          divide(raSaldaKonti.calcTecaj(dm.getSkstavke()), 2, BigDecimal.ROUND_HALF_UP).negate());
      }
      if (skr.getInt("RBS") != 1)
        skr.setBigDecimal("SALDO", skr.getBigDecimal("ID").subtract(skr.getBigDecimal("IP")));
      else skr.setBigDecimal("SALDO", skr.getBigDecimal("ID").add(skr.getBigDecimal("IP")));
      skr.setString("OPIS", "Storno ra\u010Duna "+skr.getString("BROJDOK"));
    }
    skr.saveChanges();
  }

  private void jbInit() throws Exception {
    okajac = true;
    this.raMaster.addOption(new hr.restart.util.raNavAction("Storno ra\u010Duna", raImages.IMGSENDMAIL, KeyEvent.VK_F8) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        getStorno();
      }
    },6,false);
  }
}

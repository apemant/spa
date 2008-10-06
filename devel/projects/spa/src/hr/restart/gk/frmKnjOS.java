/****license*****************************************************************
**   file: frmKnjOS.java
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

import hr.restart.swing.JrCheckBox;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raTransaction;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmKnjOS extends frmKnjizenje {
  hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
  String cnaloga;
  String qStr;
  private JPanel jpan = new JPanel();
  private XYLayout xYLayout1 = new XYLayout();
  private JrCheckBox jcbAMOR = new JrCheckBox();
  private JrCheckBox jcbLIK = new JrCheckBox();
  private JrCheckBox jcbPRIP = new JrCheckBox();
  private JrCheckBox jcbCORG = new JrCheckBox();

  public frmKnjOS() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public boolean okPress() throws Exception {
    qStr="";
    try {
      if (!getKnjizenje().startKnjizenje(this)) {
        return false;
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    dm.getOS_Metaobrada().open();
    if (!dm.getOS_Metaobrada().getString("CORG2").equals("")) {
    }
    if (jcbAMOR.isSelected()) {
      qStr=sjQuerys.getOSKnjizenjeAmor();
    }
    if (jcbPRIP.isSelected()) {
      if (qStr!="") {
        qStr=qStr+" UNION ";
      }
      qStr=qStr+sjQuerys.getOSKnjizenjaPriprema();
    }
    if (jcbLIK.isSelected()) {
      if (qStr!="") {
        qStr=qStr+" UNION ";
      }
      qStr=qStr+sjQuerys.getOSKnjizenjeLikvidacija();
    }
    if (jcbCORG.isSelected()) {
      if (qStr!="") {
        qStr=qStr+" UNION ";
      }
      qStr=qStr+sjQuerys.getOSKnjizenjeChangeCORG();
    }
    System.out.println("SQL : "+qStr);
    QueryDataSet qdsKnjizenjeAmor = Util.getNewQueryDataSet(qStr);
    if (qdsKnjizenjeAmor.getRowCount()==0) {
      getKnjizenje().setErrorMessage("Nema podataka za prijenos");
      return false;
    }
    st.prn(qdsKnjizenjeAmor);
    cnaloga = getKnjizenje().cGK;

    try {
      do {
        StorageDataSet stavka = getKnjizenje().getNewStavka(qdsKnjizenjeAmor.getString("konto"),qdsKnjizenjeAmor.getString("corg"));
        stavka.setString("OPIS",qdsKnjizenjeAmor.getString("tekst"));
        stavka.setTimestamp("DATDOK",Valid.getValid().getToday());
        getKnjizenje().setID(qdsKnjizenjeAmor.getBigDecimal("idug"));
        getKnjizenje().setIP(qdsKnjizenjeAmor.getBigDecimal("ipot"));
        getKnjizenje().saveStavka();
      } while (qdsKnjizenjeAmor.next());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    return getKnjizenje().saveAll();
  }
  public boolean commitTransfer() {
    try {
      System.out.println("commitTrans");
      raTransaction.runSQL(sjQuerys.updateOS_Log(cnaloga));
      raTransaction.runSQL(sjQuerys.updateOS_Meta());
      // update kontrolni slog set cnaloga = cnmaloga
      return true;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return false;
    }
  }
  public void SetFokus() {
    super.SetFokus();
  }
  private void jbInit() throws Exception {
    jpan.setLayout(xYLayout1);
    jcbAMOR.setText("Amortizacija i revalorizacija");
    jcbLIK.setText("Likvidacija osnovnog sredstva");
    jcbPRIP.setText("Prijenos iz pripremnu u upotrebu");
    jcbCORG.setText("Promjena organizacijske jedinice");
    xYLayout1.setWidth(450);
    xYLayout1.setHeight(70);
    jpan.add(jcbAMOR, new XYConstraints(15, 5, -1, -1));
    jpan.add(jcbLIK, new XYConstraints(15, 30, -1, -1));
    jpan.add(jcbPRIP, new XYConstraints(255, 5, -1, -1));
    jpan.add(jcbCORG, new XYConstraints(255, 30, -1, -1));
    super.jp.add(jpan, BorderLayout.CENTER);
  }
}
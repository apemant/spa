/****license*****************************************************************
**   file: frmUnRealDoc.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raUpit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.TableDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class frmUnRealDoc extends raUpit {

  boolean inDatum = false;

  JPanel jPanel3 = new JPanel();

  JPanel jp = new JPanel();

  hr.restart.robno._Main main;

  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();

  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();

  TableDataSet tds = new TableDataSet();

  java.sql.Date dateP = null;

  static frmUnRealDoc upk;

  XYLayout xYLayout1 = new XYLayout();

  JraComboBox jcbDokumenti = new JraComboBox();

  JLabel jLabel1 = new JLabel();

  Valid vl;

  dM dm;

  String sSklSelected = "";

  JLabel jLabel2 = new JLabel();

  JraTextField jtfZavDatum = new JraTextField();

  JRadioButton jrbNDok = new JRadioButton();

  JRadioButton jrbDDok = new JRadioButton();

  BorderLayout borderLayout1 = new BorderLayout();

  hr.restart.robno.rapancskl rpcskl = new hr.restart.robno.rapancskl() {
    public void findFocusAfter() {
    }
  };

  // hr.restart.robno._Main main;

  //*** konstruktor
  public frmUnRealDoc() {
    try {
      jbInit();
      upk = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("sad u vlasnistvu SG-a");
  }

  public static frmUnRealDoc getfrmUnRealDoc() {
    if (upk == null) {
      upk = new frmUnRealDoc();
    }
    return upk;
  }

  public void okPress() {
    String qStr = "";
    String sSkl = "";

    if (!rpcskl.jrfCSKL.getText().equals("")) {
      sSkl = " AND DOKI.CSKL ='" + rpcskl.jrfCSKL.getText() + "'";
      this.sSklSelected = "D";
    }

    rcc.EnabDisabAll(jp, false);
    rcc.EnabDisabAll(rpcskl, false);

    if (jrbNDok.isSelected()) {
      qStr = rdUtil.getUtil().getUnReal(getVrDok(jcbDokumenti.getSelectedIndex()), sSkl, true, util.getTimestampValue(tds.getTimestamp("zavDatum"), 1));
    } else {
      qStr = rdUtil.getUtil().getUnReal(getVrDok(1), sSkl, false, util.getTimestampValue(tds.getTimestamp("zavDatum"), 1));
    }
    
    System.out.println("\n"+qStr); //XDEBUG delete when no more needed

    vl.execSQL(qStr);
    vl.RezSet.setMetaDataUpdate(MetaDataUpdate.TABLENAME + MetaDataUpdate.PRECISION + MetaDataUpdate.SCALE + MetaDataUpdate.SEARCHABLE);
    vl.RezSet.setColumns(new Column[]{(Column) dm.getDoki().getColumn("CSKL").clone(), (Column) dm.getDoki().getColumn("VRDOK").clone(), (Column) dm.getDoki().getColumn("BRDOK").clone(), (Column) dm.getDoki().getColumn("CPAR").clone(), (Column) dm.getPartneri().getColumn("NAZPAR").clone(), (Column) dm.getDoki().getColumn("DATDOK").clone(), (Column) dm.getDoki().getColumn("UIRAC").clone()});
    vl.RezSet.open();
    vl.RezSet.first();
    vl.RezSet.getColumn("VRDOK").setVisible(0);
    if (vl.RezSet.getRowCount() > 0) {
      getJPTV().setDataSet(vl.RezSet);
      getJPTV().getMpTable().requestFocus();
    } else
      setNoDataAndReturnImmediately();
  }

  //*** handlanje pritiska na tipku F10 ili klika na OK button
  public boolean Validacija() {
    return true;
  }

  // handlanje pritiska na tipku ESC
  public void firstESC() {
    if (getJPTV().getDataSet() == null) {
      showDefaultValues();
    } else {
      getJPTV().setDataSet(null);
      rcc.EnabDisabAll(this.rpcskl, true);
      rpcskl.setCSKL("");
    }
  }

  public boolean runFirstESC() {

    if (inDatum == true) {
      return false;
    } else {
      return true;
    }
  }

  //*** init metoda
  private void jbInit() throws Exception {
    jPanel3.setLayout(xYLayout1);
    addReport("hr.restart.robno.repUnRealDoc", "Ispis nerealiziranih dokumenata", 5);
    dm = hr.restart.baza.dM.getDataModule();
    vl = hr.restart.util.Valid.getValid();
    rpcskl.setRaMode('S');
    rpcskl.setCSKL("");

    setJPan(jp);

    jcbDokumenti.addItem(new String("Narudžbe"));
    jcbDokumenti.addItem(new String("Ponude"));
    jcbDokumenti.addItem(new String("Raèuni za predujam"));

    jp.setLayout(borderLayout1);
    jp.setMinimumSize(new Dimension(555, 135));
    jp.setPreferredSize(new Dimension(650, 135));
    jtfZavDatum.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtfZavDatum_focusGained(e);
      }

      public void focusLost(FocusEvent e) {
        jtfZavDatum_focusLost(e);
      }
    });
    jp.add(rpcskl, BorderLayout.CENTER);
    jp.add(jPanel3, BorderLayout.NORTH);

    jPanel3.setMinimumSize(new Dimension(555, 70));
    jPanel3.setPreferredSize(new Dimension(650, 70));
    jLabel1.setText("Vrsta dokumenta");
    jcbDokumenti.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbDokumenti_actionPerformed(e);
      }
    });

    jLabel2.setText("Do datuma");
    jrbNDok.setHorizontalTextPosition(SwingConstants.RIGHT);
    jrbNDok.setText("Svi dokumenti");
    jrbNDok.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jrbNDok_actionPerformed(e);
      }
    });
    jrbDDok.setHorizontalTextPosition(SwingConstants.RIGHT);
    jrbDDok.setText("Dospjeli dokumenti");
    jrbDDok.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jrbDDok_actionPerformed(e);
      }
    });
    jPanel3.add(jcbDokumenti, new XYConstraints(150, 20, 480, -1));
    jPanel3.add(jLabel1, new XYConstraints(15, 20, -1, -1));
    jPanel3.add(jLabel2, new XYConstraints(15, 45, -1, -1));
    jPanel3.add(jtfZavDatum, new XYConstraints(150, 45, 100, -1));
    jPanel3.add(jrbDDok, new XYConstraints(505, 45, -1, -1));
    jPanel3.add(jrbNDok, new XYConstraints(332, 45, -1, -1));

    tds.setColumns(new Column[]{dm.createTimestampColumn("zavDatum")});
    jtfZavDatum.setDataSet(tds);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setText("");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);

    showDefaultValues();
  }

  //*** Defaultne vrijednosti dialoga
  void showDefaultValues() {
    rcc.EnabDisabAll(jPanel3, true);
    tds.open();
    rpcskl.setCSKL("");
    tds.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false, 0));
    getJPTV().setDataSet(null);
    setCombo(0);
    jrbNDok.setSelected(true);
    jrbDDok.setSelected(false);
    jtfZavDatum.requestFocus();
  }


  //***************************************************************************
  public void findFocusAfter() {

  }

  public String getVrDok(int cbIdx) {
    String s = "";
    switch (cbIdx) {
      case 0:
        s = " AND DOKI.VRDOK = 'NKU'";
        break;
      case 1:
        s = " AND DOKI.VRDOK = 'PON'";
        break;
      case 2:
        s = " AND DOKI.VRDOK = 'PRD'";
        break;
    }
    ;
    return s;
  }

  public com.borland.dx.dataset.DataSet getQds() {
    return getJPTV().getMpTable().getDataSet();
  }

  public void componentShow() {
    //    rpcskl.setCSKL(raUser.getInstance().getDefSklad());
    showDefaultValues();
  }

  void jcbDokumenti_actionPerformed(ActionEvent e) {}

  void jrbNDok_actionPerformed(ActionEvent e) {
    jrbDDok.setSelected(false);
    jrbNDok.setSelected(true);
    setCombo(0);
  }

  void jrbDDok_actionPerformed(ActionEvent e) {
    jrbNDok.setSelected(false);
    jrbDDok.setSelected(true);
    setCombo(1);
  }

  public void setCombo(int rb) {
    int cItem = jcbDokumenti.getItemCount();
    switch (rb) {
      case 0:
        if (cItem == 1) {
          jcbDokumenti.removeAllItems();
          jcbDokumenti.addItem(new String("Narudžbe"));
          jcbDokumenti.addItem(new String("Ponude"));
          jcbDokumenti.addItem(new String("Raèuni za predujam"));
        }
        break;
      case 1:
        if (cItem > 1) {
          jcbDokumenti.removeAllItems();
          jcbDokumenti.addItem(new String("Ponude"));
        }
        break;
    }
  }

  void jtfZavDatum_focusGained(FocusEvent e) {
    inDatum = true;
  }

  void jtfZavDatum_focusLost(FocusEvent e) {
    inDatum = false;
  }
}
/****license*****************************************************************
**   file: frmUseri.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raImages;
import hr.restart.util.raLoader;
import hr.restart.util.raMatPodaci;

import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class frmUseri extends raMatPodaci {
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  JPanel jp = new JPanel();
  XYLayout xYLay = new XYLayout();
  dM dm;
  JLabel jlCUSER = new JLabel();
  JraTextField jtCUSER = new JraTextField();
  JLabel jlNAZIV = new JLabel();
  JraTextField jtNAZIV = new JraTextField();
  JLabel jlCGRUPEUSERA = new JLabel();
  JlrNavField jlrCGRUPEUSERA = new JlrNavField();
  JlrNavField jlrNAZIVGRUSERA = new JlrNavField();
  JraButton jBgetGRUS = new JraButton();
  JraCheckBox jcbAktivan = new JraCheckBox();
  JraCheckBox jcbSuper = new JraCheckBox();

  JraRadioButton jrbOg = new JraRadioButton();
  JraRadioButton jrbNog = new JraRadioButton();
  JraRadioButton jrbGog = new JraRadioButton();
  raButtonGroup bg = new raButtonGroup();

  JLabel jlOg = new JLabel();

  public frmUseri() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    this.setRaQueryDataSet(dm.getUseri());
//    dm.getUseri().refresh();
    this.setVisibleCols(new int[] {0,1,2});
    jlCUSER.setToolTipText("");
    jlCUSER.setText("Oznaka");
    jp.setLayout(xYLay);
    jtCUSER.setColumnName("CUSER");
    jtCUSER.setDataSet(dm.getUseri());
    jlNAZIV.setText("Naziv");
    jlNAZIV.setToolTipText("");
    jtNAZIV.setDataSet(dm.getUseri());
    jtNAZIV.setColumnName("NAZIV");
    jtNAZIV.setText("jraTextField1");
    jlCGRUPEUSERA.setToolTipText("");
    jlCGRUPEUSERA.setText("Grupa korisnika");
    jcbAktivan.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktivan.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktivan.setText("Aktivan");
    jcbAktivan.setColumnName("AKTIV");
    jcbAktivan.setDataSet(dm.getUseri());
    jcbAktivan.setSelectedDataValue("D");
    jcbAktivan.setUnselectedDataValue("N");
/*    jcbAktivan.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbAktivan_actionPerformed(e);
      }
    }); */

    xYLay.setWidth(467);
    xYLay.setHeight(130);

    jlOg.setText("Predpostavljena prava");
    bg.setDataSet(dm.getUseri());
    bg.setColumnName("OGRANICEN");
    bg.add(jrbNog, " Sva ", "N");
    bg.add(jrbOg, " Nijedno ", "D");
    bg.add(jrbGog, " S grupe ", "G");
    bg.setHorizontalAlignment(SwingConstants.LEADING);
    bg.setHorizontalTextPosition(SwingConstants.TRAILING);
//    bg.setSelected(jrbGog);

    jlrCGRUPEUSERA.setColumnName("CGRUPEUSERA");
    jlrCGRUPEUSERA.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZIVGRUSERA});
    jlrCGRUPEUSERA.setColNames(new String[] {"NAZIV"});
    jlrCGRUPEUSERA.setSearchMode(0);
    jlrCGRUPEUSERA.setDataSet(dm.getUseri());
    jlrCGRUPEUSERA.setRaDataSet(dm.getGrupeusera());
    jlrCGRUPEUSERA.setVisCols(new int[] {0,1});
    jlrCGRUPEUSERA.setNavButton(jBgetGRUS);

    jlrNAZIVGRUSERA.setColumnName("NAZIV");
    jlrNAZIVGRUSERA.setNavProperties(jlrCGRUPEUSERA);
    jlrNAZIVGRUSERA.setSearchMode(1);

    jBgetGRUS.setText("jraButton1");
/*    jcbSuper.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbSuper_actionPerformed(e);
      }
    }); */
    jcbSuper.setUnselectedDataValue("N");
    jcbSuper.setSelectedDataValue("D");
    jcbSuper.setDataSet(dm.getUseri());
    jcbSuper.setColumnName("SUPER");
    jcbSuper.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbSuper.setText("Super user");
    jcbSuper.setHorizontalAlignment(SwingConstants.RIGHT);
    jp.add(jlCUSER, new XYConstraints(15, 20, -1, -1));
    jp.add(jtCUSER, new XYConstraints(150, 20, 120, -1));
    jp.add(jlNAZIV, new XYConstraints(15, 45, -1, -1));
    jp.add(jtNAZIV, new XYConstraints(150, 45, 300, -1));
    jp.add(jlCGRUPEUSERA, new XYConstraints(15, 70, -1, -1));
    jp.add(jlrCGRUPEUSERA, new XYConstraints(150, 70, 75, -1));
    jp.add(jlrNAZIVGRUSERA, new XYConstraints(230, 70, 194, -1));
    jp.add(jBgetGRUS, new XYConstraints(429, 70, 21, 21));
    jp.add(jcbAktivan, new XYConstraints(375, 20, 75, -1));
    jp.add(jcbSuper, new XYConstraints(275, 20, 100, -1));
    jp.add(jlOg, new XYConstraints(15, 95, -1, -1));
    jp.add(jrbNog, new XYConstraints(150, 92, 70, -1));
    jp.add(jrbOg, new XYConstraints(230, 92, 80, -1));
    jp.add(jrbGog, new XYConstraints(335, 92, 90, -1));

    this.setRaDetailPanel(jp);
    this.addOption(new hr.restart.util.raNavAction("Prava", raImages.IMGHISTORY, KeyEvent.VK_F6) {
        public void actionPerformed(java.awt.event.ActionEvent ev) {
          pravaClick();
        }
      },3);
  }
  void pravaClick() {
    frmPravaUser puser = (frmPravaUser) raLoader.load("hr.restart.sisfun.frmPravaUser");
    hr.restart.util.startFrame.getStartFrame().centerFrame(puser,0,"Prava korisnika "+getRaQueryDataSet().getString("NAZIV"));
//    puser.initDataSet();
    puser.setUser(getRaQueryDataSet().getString("CUSER"));
    puser.show();
//ST.prn(pgrus.getRaQueryDataSet());
//    startFrame.getStartFrame().showFrame("hr.restart.sisfun.frmPravaGrus","Prava Grupe "+dm.getGrupeusera().getString("NAZIV"));
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jtCUSER.requestFocus();
    } else if (mode == 'I') {
      rcc.setLabelLaF(jtCUSER, false);
      jtNAZIV.requestFocus();
    }
  }
  public boolean Validacija(char mode) {
    if (mode == 'N') {
      if (vl.notUnique(jtCUSER)) return false;
    }
    if (vl.isEmpty(jlrCGRUPEUSERA)) return false;
    return true;
  }

/*  void jcbAktivan_actionPerformed(ActionEvent e) {

  }
  void jcbSuper_actionPerformed(ActionEvent e) {

  }
*/
}
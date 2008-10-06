/****license*****************************************************************
**   file: frmGrupeUsera.java
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
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raLoader;
import hr.restart.util.raMatPodaci;
import hr.restart.util.sysoutTEST;

import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.NavigationEvent;
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

public class frmGrupeUsera extends raMatPodaci {
hr.restart.util.sysoutTEST ST = new sysoutTEST(false);
  JPanel jp = new JPanel();
  XYLayout xYLay = new XYLayout();
  JLabel jlCGRUPEUSERA = new JLabel();
  dM dm;
  JraTextField jtCGRUPEUSERA = new JraTextField();
  JLabel jlNAZIV = new JLabel();
  JraTextField jtNAZIV = new JraTextField();
  JraCheckBox jcbAktivan = new JraCheckBox();
  JraRadioButton jrbOg = new JraRadioButton();
  JraRadioButton jrbNog = new JraRadioButton();
  raButtonGroup bg = new raButtonGroup();

  JLabel jlOg = new JLabel();

  public frmGrupeUsera() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    this.setRaQueryDataSet(dm.getGrupeusera());
    this.setVisibleCols(new int[] {0,1});
    jlCGRUPEUSERA.setText("Oznaka");
    jp.setLayout(xYLay);
    jtCGRUPEUSERA.setColumnName("CGRUPEUSERA");
    jtCGRUPEUSERA.setDataSet(dm.getGrupeusera());
    jlNAZIV.setText("Naziv");
    jtNAZIV.setDataSet(dm.getGrupeusera());
    jtNAZIV.setColumnName("NAZIV");
    xYLay.setWidth(498);
    xYLay.setHeight(110);
    jcbAktivan.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktivan.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktivan.setText("Aktivan");
    jcbAktivan.setColumnName("AKTIV");
    jcbAktivan.setDataSet(dm.getGrupeusera());
    jcbAktivan.setSelectedDataValue("D");
    jcbAktivan.setUnselectedDataValue("N");
    jlOg.setText("Predpostavljena prava");
    bg.setDataSet(dm.getGrupeusera());
    bg.setColumnName("OGRANICEN");
    bg.add(jrbNog, " Sva ", "N");
    bg.add(jrbOg, " Nijedno ", "D");
    bg.setHorizontalAlignment(SwingConstants.LEADING);
    bg.setHorizontalTextPosition(SwingConstants.TRAILING);
    bg.setSelected(jrbNog);

    jp.add(jlCGRUPEUSERA, new XYConstraints(15, 20, -1, -1));
    jp.add(jtCGRUPEUSERA, new XYConstraints(150, 20, 75, -1));
    jp.add(jlNAZIV, new XYConstraints(15, 45, -1, -1));
    jp.add(jtNAZIV, new XYConstraints(150, 45, 300, -1));
    jp.add(jcbAktivan, new XYConstraints(230, 20, 220, -1));
    jp.add(jlOg, new XYConstraints(15, 70, -1, -1));
    jp.add(jrbNog, new XYConstraints(150, 67, 70, -1));
    jp.add(jrbOg, new XYConstraints(230, 67, 100, -1));

    this.setRaDetailPanel(jp);
    this.addOption(new hr.restart.util.raNavAction("Prava", raImages.IMGHISTORY, KeyEvent.VK_F6) {
        public void actionPerformed(java.awt.event.ActionEvent ev) {
          pravaClick();
        }
      },3);
  }

  void pravaClick() {
    frmPravaGrus pgrus = (frmPravaGrus)raLoader.load("hr.restart.sisfun.frmPravaGrus");
    hr.restart.util.startFrame.getStartFrame().centerFrame(pgrus,0,"Prava Grupe "+dm.getGrupeusera().getString("NAZIV"));
//    pgrus.initDataSet();
    pgrus.setGrupa(getRaQueryDataSet().getString("CGRUPEUSERA"));
    pgrus.show();
//ST.prn(pgrus.getRaQueryDataSet());
//    startFrame.getStartFrame().showFrame("hr.restart.sisfun.frmPravaGrus","Prava Grupe "+dm.getGrupeusera().getString("NAZIV"));
  }
  public void SetFokus(char mode) {
    if (mode == 'N') {
      raCommonClass.getraCommonClass().setLabelLaF(jtCGRUPEUSERA, true);
      jtCGRUPEUSERA.requestFocus();
    } else if (mode == 'I') {
      raCommonClass.getraCommonClass().setLabelLaF(jtCGRUPEUSERA, false);
      jtNAZIV.requestFocus();
    }
  }
  public boolean Validacija(char mode) {
    Valid vl = Valid.getValid();
    if (mode == 'N') {
      if (vl.notUnique(jtCGRUPEUSERA)) return false;
    }
    return true;
  }

  public void raQueryDataSet_navigated(NavigationEvent e) {

  }

}
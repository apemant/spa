/****license*****************************************************************
**   file: frmAgenti.java
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
package hr.restart.zapod;



import hr.restart.baza.dM;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;

import java.awt.event.ActionEvent;
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



public class frmAgenti extends raMatPodaci {

  private static frmAgenti fagenti;

  dM dm;

  raCommonClass rcc = new raCommonClass();

  Valid vl = Valid.getValid();

  JPanel jp = new JPanel();

  XYLayout xYLayout1 = new XYLayout();

  JLabel jlCagent = new JLabel();

  JraTextField jtCAGENT = new JraTextField();

  JLabel jlNazAgent = new JLabel();

  JraTextField jtNAZAGENT = new JraTextField();

  JraCheckBox jcbAktivan = new JraCheckBox();

  JLabel jlAgent = new JLabel();

  JLabel jlProv = new JLabel();
  JraTextField jtProv = new JraTextField();

  JLabel jlVr = new JLabel();
  raComboBox rcbVr = new raComboBox();

  JraTextField jrEMADR = new JraTextField();
  dlgTotalAgent dtp = new dlgTotalAgent();

  public frmAgenti() {

    super(2);

    try {

      jbInit();

      fagenti = this;

    }

    catch(Exception e) {

      e.printStackTrace();

    }

  }

  private void jbInit() throws Exception {

    dm = dM.getDataModule();

    setRaDetailPanel(jp);

    setRaQueryDataSet(dm.getAllAgenti());

    setVisibleCols(new int[] {0,1});

    jp.setLayout(xYLayout1);

    jlCagent.setText("Oznaka");

    jtCAGENT.setColumnName("CAGENT");

    jtCAGENT.setDataSet(getRaQueryDataSet());

    jlNazAgent.setText("Ime i prezime");

    jtNAZAGENT.setDataSet(getRaQueryDataSet());

    jtNAZAGENT.setColumnName("NAZAGENT");

//    xYLayout1.setWidth(480);

//    xYLayout1.setHeight(85);

    jlProv.setText("Postotak provizije");
    jtProv.setColumnName("POSTOPROV");
    jtProv.setDataSet(getRaQueryDataSet());
    rcbVr.setRaDataSet(getRaQueryDataSet());
    rcbVr.setRaColumn("VRSTAPROV");
    rcbVr.setRaItems(new String[][] {
      {" Prema izdanim raèunima", "F"},
      {" Prema naplaæenim raèunima", "N"}
    });

    jrEMADR.setColumnName("EMADR");
    jrEMADR.setDataSet(getRaQueryDataSet());

    jcbAktivan.setHorizontalAlignment(SwingConstants.RIGHT);

    jcbAktivan.setHorizontalTextPosition(SwingConstants.LEADING);

    jcbAktivan.setSelected(true);

    jcbAktivan.setText("Aktivan");

    jcbAktivan.setColumnName("AKTIV");

    jcbAktivan.setDataSet(getRaQueryDataSet());

    jcbAktivan.setSelectedDataValue("D");

    jcbAktivan.setUnselectedDataValue("N");



    xYLayout1.setWidth(570);

    xYLayout1.setHeight(126);

    jlAgent.setText("Agent");

    jp.add(jtCAGENT,  new XYConstraints(150, 30, 100, -1));

    jp.add(jtNAZAGENT,    new XYConstraints(255, 30, 300, -1));

    jp.add(jlNazAgent,  new XYConstraints(255, 12, -1, -1));

    jp.add(jlCagent,  new XYConstraints(150, 12, -1, -1));

    jp.add(jlAgent,   new XYConstraints(15, 30, -1, -1));

    jp.add(jcbAktivan,        new XYConstraints(492, 8, -1, -1));
    jp.add(jlProv, new XYConstraints(15, 55, -1, -1));
    jp.add(jtProv, new XYConstraints(150, 55, 100, -1));
/*    jp.add(jlVr, new XYConstraints(275, 55, -1, -1)); */
    jp.add(rcbVr, new XYConstraints(255, 55, 300, -1));

    jp.add(new JLabel("E-Mail"), new XYConstraints(15, 80, -1, -1));
    jp.add(jrEMADR, new XYConstraints(150, 80, 405, -1));
    
    
    this.addOption(new raNavAction("Promet", raImages.IMGMOVIE, KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
        showPromet();
      }
    }, 6);


  }

  private void showPromet() {
    int cag = getRaQueryDataSet().getInt("CAGENT");
    if (cag == 0) return;
    dtp.show(frmAgenti.this.getWindow(), cag,
             "Promet agenta "+getRaQueryDataSet().getInt("CAGENT")+" "+
             getRaQueryDataSet().getString("NAZAGENT"));
  }

  public static frmAgenti getFrmAgenti() {

    if (fagenti==null) fagenti = new frmAgenti();



    return fagenti;

  }

  public boolean Validacija(char mode) {

    if (mode=='N') {

      if (vl.notUnique(jtCAGENT)) return false;

    }

    if (vl.isEmpty(jtNAZAGENT)) return false;

    return true;

  }



  public void SetFokus(char mode) {

    if (mode=='N') {

      rcc.setLabelLaF(jtCAGENT, true);

//      jtCAGENT.setEnabled(true);

      jtCAGENT.requestFocus();

    }

    else {

      rcc.setLabelLaF(jtCAGENT, false);

//      jtCAGENT.setEnabled(false);

      jtNAZAGENT.requestFocus();

    }

  }

}


/****license*****************************************************************
**   file: jpMiniPres.java
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
package hr.restart.blpn;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraDialog;
import hr.restart.util.JlrNavField;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpMiniPres extends JraDialog {

  private JPanel panelJedan = new JPanel();
  private BorderLayout borderLayout1 = new BorderLayout();
  private XYLayout xYlay = new XYLayout();
  JLabel jlCpn = new JLabel();
  JLabel jlaCradnik = new JLabel();
  JLabel jlaIme = new JLabel();
  JLabel jlaPrezime = new JLabel();
  JLabel jlRadnik = new JLabel();

  JraButton jbSelCradnik = new JraButton();
  JlrNavField jlrCpn = new JlrNavField();
  JlrNavField jlrCradnik = new JlrNavField();
  JlrNavField jlrIme = new JlrNavField();
  JlrNavField jlrPrezime = new JlrNavField();

  dM dm = dM.getDataModule();
  sgStuff ss = sgStuff.getStugg();
  frmRazlikaPN_V2 frpn;

  hr.restart.util.OKpanel okp = new hr.restart.util.OKpanel() {
    public void jBOK_actionPerformed() {
      setStore();
      sakrij();
      frpn = new frmRazlikaPN_V2(forForward);
      frpn.setVisible(true);
    }
    public void jPrekid_actionPerformed() {
      sakrij();
    }
  };

  public jpMiniPres() {
  }

  StorageDataSet fromBehind;
  StorageDataSet forForward = new StorageDataSet();

  public jpMiniPres(StorageDataSet sSet) {
    try {
      jbInit();
      fromBehind = sSet;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {

    this.addKeyListener(
        new java.awt.event.KeyAdapter() {
          public void keyPressed(java.awt.event.KeyEvent e) {
            if (e.getKeyCode() == e.VK_F10) {
              setStore();
              sakrij();
              frpn = new frmRazlikaPN_V2(forForward);
              frpn.setVisible(true);
            } else if (e.getKeyCode() == e.VK_ESCAPE) {
              sakrij();
            }
          }
    });

    forForward.setColumns(new Column[]{
      dm.createIntColumn("CBLAG"),
      dm.createStringColumn("OZNVAL", 3),
      dm.createStringColumn("CRADNIK", 6),
      dm.createStringColumn("CPN", 15),
    });

    forForward.open();

    this.getContentPane().setLayout(borderLayout1);
    panelJedan.setLayout(xYlay);
    xYlay.setWidth(591);
    xYlay.setHeight(95);

    jlCpn.setText("Broj putnog naloga");
    jlRadnik.setText("Djelatnik");
    jlaIme.setText("Ime");
    jlaPrezime.setText("Prezime");
    jlaCradnik.setText("M.B.");
    jlaCradnik.setHorizontalAlignment(SwingConstants.CENTER);
    jlaIme.setHorizontalAlignment(SwingConstants.CENTER);
    jlaPrezime.setHorizontalAlignment(SwingConstants.CENTER);

    jlrCradnik.setColumnName("CRADNIK");
    jlrCradnik.setDataSet(forForward);
    jlrCradnik.setColNames(new String[] {"IME", "PREZIME", "CPN"});
    jlrCradnik.setTextFields(new JTextComponent[] {jlrIme, jlrPrezime, jlrCpn});
    jlrCradnik.setVisCols(new int[] {0, 1, 2, 3});
    jlrCradnik.setSearchMode(0);
    jlrCradnik.setRaDataSet(ss.getPutniNaloziPrAk('R'));
    jlrCradnik.setNavButton(jbSelCradnik);

    jlrIme.setColumnName("IME");
    jlrIme.setNavProperties(jlrCradnik);
    jlrIme.setSearchMode(1);

    jlrPrezime.setColumnName("PREZIME");
    jlrPrezime.setNavProperties(jlrCradnik);
    jlrPrezime.setSearchMode(1);

    jlrCpn.setHorizontalAlignment(SwingConstants.CENTER);
    jlrCpn.setColumnName("CPN");
    jlrCpn.setNavProperties(jlrCradnik);
    jlrCpn.setSearchMode(1);

    panelJedan.setBorder(BorderFactory.createEtchedBorder());
    panelJedan.add(jlaCradnik, new XYConstraints(151,13,48,-1));
    panelJedan.add(jlrCradnik, new XYConstraints(150,30,50,-1));
    panelJedan.add(jlaIme, new XYConstraints(206,13,168,-1));
    panelJedan.add(jlrIme, new XYConstraints(205,30,170,-1));
    panelJedan.add(jlaPrezime, new XYConstraints(381,13,168,-1));
    panelJedan.add(jlrPrezime, new XYConstraints(380,30,170,-1));
    panelJedan.add(jlRadnik, new XYConstraints(15, 30, -1, -1));

    panelJedan.add(jbSelCradnik, new XYConstraints(555, 30, 21, 21));

    panelJedan.add(jlCpn, new XYConstraints(15, 55, -1, -1));
    panelJedan.add(jlrCpn, new XYConstraints(150, 55, 130, -1));

    this.getContentPane().add(panelJedan, BorderLayout.NORTH);
    this.getContentPane().add(okp, BorderLayout.SOUTH);
    startFrame.getStartFrame().centerFrame(this, 0 ,"");
  }

  void setStore(){
    forForward.setInt("CBLAG", fromBehind.getInt("CBLAG"));
    forForward.setString("OZNVAL", fromBehind.getString("OZNVAL"));
    forForward.setString("CPN", jlrCpn.getText().trim());
//    ss.setNeisplaceneStavkePN(forForward.getString("CPN"), true);
  }

  void sakrij(){
    this.hide();
  }

  public void show(){
    super.show();
  }
}
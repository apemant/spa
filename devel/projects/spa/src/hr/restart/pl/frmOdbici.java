/****license*****************************************************************
**   file: frmOdbici.java
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
import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.lookupFrame;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmOdbici extends raMatPodaci {
  hr.restart.util.PreSelect pres = new hr.restart.util.PreSelect() {
    public boolean Validacija() {
      if (!jrfCOdb.getText().equals("")) {
        vrOdb = (short) (new Integer(jrfCOdb.getText()).intValue());
        return true;
      } else {
        return false;
      }
    }
  };

  raCommonClass rcc = raCommonClass.getraCommonClass();

  dM dm = dM.getDataModule();

  Valid vl = Valid.getValid();

  jpOdbici jpDetail;

  JPanel jpSel = new JPanel();

  XYLayout xYLayout1 = new XYLayout();

  JlrNavField jrfCOdb = new JlrNavField();

  JraButton jbCOdb = new JraButton();

  JlrNavField jrfNazOdb = new JlrNavField();

  JLabel jVrOdb = new JLabel();

  JLabel jlNazOdb = new JLabel();

  JLabel jlCOdb = new JLabel();

  jpChooseOdb jpDetailOdb;

  jpChooseOdb2 jpDetailOdb2;

  QueryDataSet defaultQDS = new QueryDataSet();

  QueryDataSet vrOdbQDS = new QueryDataSet();

  String nivoOdb = "";

  String osnovica = "";

  short vrOdb = -1;

  int RowPointer = 0;

  boolean param4 = false;

  boolean param0 = false;

  char escMod;

  public static char mod;

  public frmOdbici() {
    param0 = true;
    pres.setSelDataSet(dm.getOdbici());
    this.setRaQueryDataSet((QueryDataSet) pres.getSelDataSet());
    this.setVisibleCols(new int[] { 0, 1, 2, 4, 5, 6 });
    try {
      jbInit();
    } catch (Exception ex) {
    }
    pres.setSelPanel(jpSel);
  }

  public frmOdbici(short vo) {
    try {
      vrOdb = vo;
      defaultQDS = plUtil.getPlUtil().getDefDS(vrOdb);
      this.setRaQueryDataSet(defaultQDS);
      this.setVisibleCols(new int[] { 0, 1, 2, 5, 6 });
      jbInit();
      nivoOdb = plUtil.getPlUtil().getStrNivo(vrOdb);
      osnovica = plUtil.getPlUtil().getOsnovica(vrOdb);
      if (nivoOdb.length() > 2) {
        jpDetailOdb2 = new jpChooseOdb2(this, nivoOdb, jpDetail, false);
        jpDetail.add(jpDetailOdb2, BorderLayout.CENTER);
      } else {
        jpDetailOdb = new jpChooseOdb(this, nivoOdb, jpDetail);
        jpDetail.add(jpDetailOdb, BorderLayout.CENTER);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public frmOdbici(String no, String to, String vos, String os) {
    try {
      param4 = true;
      vrOdbQDS = plUtil.getPlUtil().getvrOdbQDS(no, to, vos, os);
      defaultQDS = plUtil.getPlUtil().getDefDS(getInStr());
      this.setRaQueryDataSet(defaultQDS);
      this.setVisibleCols(new int[] { 0, 1, 2, 5, 6 });
      jbInit();
      nivoOdb = no;
      osnovica = os;
      if (nivoOdb.length() > 2) {
        jpDetailOdb2 = new jpChooseOdb2(this, nivoOdb, jpDetail, false);
        jpDetail.add(jpDetailOdb2, BorderLayout.CENTER);
      } else {
        jpDetailOdb = new jpChooseOdb(this, nivoOdb, jpDetail);
        jpDetail.add(jpDetailOdb, BorderLayout.CENTER);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokus(char mode) {
    this.getRaQueryDataSet().setBigDecimal("IZNOS",
        plUtil.getPlUtil().getIznos(this.vrOdb));
    this.getRaQueryDataSet().setBigDecimal("STOPA",
        plUtil.getPlUtil().getStopa(this.vrOdb));
    escMod = mode;
    if (osnovica.equals("0")) {
      this.jpDetail.jp2.jlIznos.setVisible(true);
      this.jpDetail.jp2.jraIznos.setVisible(true);
      this.jpDetail.jp2.jlStopa.setVisible(false);
      this.jpDetail.jp2.jraStopa.setVisible(false);
      this.jpDetail.jp2.jraDatpoc.setVisible(true);
      this.jpDetail.jp2.jraDatzav.setVisible(true);
      this.jpDetail.jp2.jraGlavnica.setVisible(true);
      this.jpDetail.jp2.jraRata.setVisible(true);
      this.jpDetail.jp2.jraSaldo.setVisible(true);
      this.jpDetail.jp2.jlDatpoc.setVisible(true);
      this.jpDetail.jp2.jlDatzav.setVisible(true);
      this.jpDetail.jp2.jlGlavnica.setVisible(true);
      this.jpDetail.jp2.jlRata.setVisible(true);
      this.jpDetail.jp2.jlSaldo.setVisible(true);
      this.jpDetail.jp2.setSize(605, 150);
    } else {
      this.jpDetail.jp2.jlIznos.setVisible(false);
      this.jpDetail.jp2.jraIznos.setVisible(false);
      this.jpDetail.jp2.jlStopa.setVisible(true);
      this.jpDetail.jp2.jraStopa.setVisible(true);
      this.jpDetail.jp2.jraDatpoc.setVisible(false);
      this.jpDetail.jp2.jraDatzav.setVisible(false);
      this.jpDetail.jp2.jraGlavnica.setVisible(false);
      this.jpDetail.jp2.jraRata.setVisible(false);
      this.jpDetail.jp2.jraSaldo.setVisible(false);
      this.jpDetail.jp2.jlDatpoc.setVisible(false);
      this.jpDetail.jp2.jlDatzav.setVisible(false);
      this.jpDetail.jp2.jlGlavnica.setVisible(false);
      this.jpDetail.jp2.jlRata.setVisible(false);
      this.jpDetail.jp2.jlSaldo.setVisible(false);
      this.jpDetail.jp2.setSize(605, 70);
    }
    mod = mode;
    if (param0 == false)
      this.getRaQueryDataSet()
          .setShort("CVRODB", defaultQDS.getShort("CVRODB"));
    if (mode == 'N') {
      enableSifra();
      if (param4 && vrOdb == -1) {
        this.newKeyF9Pressed();
        if (vrOdb != -1)
          this.getRaQueryDataSet().setShort("CVRODB", vrOdb);
        else
          this.getOKpanel().jPrekid_actionPerformed();
      }
      this.getRaQueryDataSet().setShort("CVRODB", vrOdb);
      if (nivoOdb.length() > 2) {
        this.jpDetailOdb2.jrfSifra.setText("");
        this.jpDetailOdb2.jrfSifra2.setText("");
        this.jpDetailOdb2.jrfSifra.forceFocLost();
        this.jpDetailOdb2.jrfSifra2.forceFocLost();
        rcc.setLabelLaF(jpDetailOdb2.jtfRBR, false);
        this.jpDetailOdb2.jrfSifra.requestFocus();
      } else {
        this.jpDetailOdb.jrfSifra.setText("");
        this.jpDetailOdb.jrfSifra.forceFocLost();
        rcc.setLabelLaF(jpDetailOdb.jtfRBR, false);
        this.jpDetailOdb.jrfSifra.requestFocus();
      }
      //      short rbr = plUtil.getPlUtil().getMaxOdbiciRBR(vrOdb);
      this.getRaQueryDataSet().setShort("RBRODB", (short) 1);
    } else if (mode == 'I' || mode == 'B') {
      String cKey = this.getRaQueryDataSet().getString("CKEY");
      String cKey2 = this.getRaQueryDataSet().getString("CKEY2");
      if (nivoOdb.length() > 2) {
        jpDetailOdb2.jrfSifra.setText(cKey);
        jpDetailOdb2.jrfSifra.forceFocLost();
        jpDetailOdb2.jrfSifra2.setText(cKey2);
        jpDetailOdb2.jrfSifra2.forceFocLost();
        rcc.setLabelLaF(jpDetailOdb2.jrfNaziv, false);
        rcc.setLabelLaF(jpDetailOdb2.jrfNaziv2, false);
        rcc.setLabelLaF(jpDetailOdb2.jrfSifra, false);
        rcc.setLabelLaF(jpDetailOdb2.jrfSifra2, false);
        rcc.setLabelLaF(jpDetailOdb2.jtfRBR, false);
        this.jpDetail.jp2.jraPnb1.requestFocus();
      } else {
        jpDetailOdb.jrfSifra.setText(cKey);
        jpDetailOdb.jrfSifra.forceFocLost();
        rcc.setLabelLaF(jpDetailOdb.jrfSifra, false);
        rcc.setLabelLaF(jpDetailOdb.jrfNaziv, false);
        rcc.setLabelLaF(jpDetailOdb.jtfRBR, false);
        this.jpDetail.jp2.jraPnb1.requestFocus();
      }
      rcc.setLabelLaF(jpDetail.jp2.jraRBROdb, false);
    }
  }

  public boolean Validacija(char mode) {
    if (nivoOdb.length() > 2) {
      if (vl.isEmpty(jpDetailOdb2.jrfSifra)
          || vl.isEmpty(jpDetailOdb2.jrfSifra2))
        return false;
    }
    if (nivoOdb.length() > 2) {
      this.getRaQueryDataSet().setString("CKEY",
          this.jpDetailOdb2.jrfSifra.getText());
      this.getRaQueryDataSet().setString("CKEY2",
          this.jpDetailOdb2.jrfSifra2.getText());
    } else {
      if (vl.isEmpty(jpDetailOdb.jrfSifra))
        return false;
      this.getRaQueryDataSet().setString("CKEY",
          this.jpDetailOdb.jrfSifra.getText());
      this.getRaQueryDataSet().setString("CKEY2", "");
    }
    if (mode == 'N' && notUnique())
      return false;
    return true;
  }

  private void jbInit() throws Exception {
    int x = (Toolkit.getDefaultToolkit().getScreenSize().width) - 580;
    int y = (Toolkit.getDefaultToolkit().getScreenSize().height) - 310;
    this.setLocation((int) x / 2, (int) y / 2);
    this.setTitle("Odbici");
    jrfNazOdb.setNavProperties(jrfCOdb);
    jrfNazOdb.setColumnName("OPISVRODB");
    jbCOdb.setText("...");
    jrfCOdb.setSearchMode(0);
    jrfCOdb.setColumnName("CVRODB");
    jrfCOdb.setTextFields(new javax.swing.text.JTextComponent[] { jrfNazOdb });
    jrfCOdb.setColNames(new String[] { "OPISVRODB" });
    jrfCOdb.setVisCols(new int[] { 0, 1 });
    jrfCOdb.setRaDataSet(dm.getVrsteodb());
    jrfCOdb.setNavButton(jbCOdb);
    jpDetail = new jpOdbici(this);
    addKeyAction(new hr.restart.util.raKeyAction(
        java.awt.event.KeyEvent.VK_ESCAPE) {
      public void keyAction() {
        ESCPressed();
      }
    });
    this.setRaDetailPanel(jpDetail);
    jpSel.setLayout(xYLayout1);
    xYLayout1.setWidth(522);
    xYLayout1.setHeight(70);
    jbCOdb.setToolTipText("");
    jVrOdb.setText("Vrsta odbitka");
    jlNazOdb.setText("Naziv");
    jlCOdb.setText("Šifra");
    jpSel.add(jrfCOdb, new XYConstraints(150, 25, 80, -1));
    jpSel.add(jrfNazOdb, new XYConstraints(235, 25, 250, -1));
    jpSel.add(jbCOdb, new XYConstraints(488, 25, 21, 21));
    jpSel.add(jVrOdb, new XYConstraints(26, 26, -1, -1));
    jpSel.add(jlNazOdb, new XYConstraints(235, 5, -1, -1));
    jpSel.add(jlCOdb, new XYConstraints(150, 5, -1, -1));
  }

  void ESCPressed() {
    if (nivoOdb.length() > 2) {
      this.jpDetailOdb2.ESCPressed(escMod);
    } else {
      this.jpDetailOdb.ESCPressed(escMod);
    }
  }

  private String getInStr() {
    String temp = "";
    vrOdbQDS.open();
    vrOdbQDS.first();
    do {
      if (vrOdbQDS.getRow() < vrOdbQDS.getRowCount() - 1)
        temp += vrOdbQDS.getShort("CVRODB") + ", ";
      else
        temp += vrOdbQDS.getShort("CVRODB");
      vrOdbQDS.next();
    } while (vrOdbQDS.inBounds());
    return temp;
  }

  void newKeyF9Pressed() {
    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    Frame frame = null;
    Dialog dialog = null;
    lookupFrame lookUp = null;
    String[] result;
    vrOdbQDS.open();
    vrOdbQDS.setTableName("DOHVATVRODBSAF9");
    lookupData LD = lookupData.getlookupData();
    result = LD.lookUp((java.awt.Frame) this.getFrameOwner(), vrOdbQDS,
        new int[] { 0, 1, 2 });
    try {
      vrOdb = (short) (new Integer(result[2]).intValue());
    } catch (Exception ex) {
    }
  }

  public void AfterCancel() {
    if (param4)
      vrOdb = -1;
  }

  public boolean BeforeDelete() {
    RowPointer = getRaQueryDataSet().getRow();
    return true;
  }

  //  public void AfterDelete()
  //  {
  //    short delVrOdb = getRaQueryDataSet().getShort("CVRODB");
  //
  //    if(getRaQueryDataSet().getShort("RBRODB")==1 &&
  // getRaQueryDataSet().getRow()==0)
  //    {
  //      plUtil.getPlUtil().recalcOdbiciRBR(delVrOdb,
  //                                  this.getRaQueryDataSet().getShort("RBRODB"));
  //    }
  //    else
  //    {
  //        plUtil.getPlUtil().recalcOdbiciRBR(delVrOdb,
  //                                  (short)(getRaQueryDataSet().getShort("RBRODB")-1));
  //    }
  //    this.getRaQueryDataSet().refresh();
  //
  //    if (RowPointer == getRaQueryDataSet().getRowCount())
  //    {
  //      getRaQueryDataSet().goToRow(RowPointer-1);
  //    }
  //    else
  //    {
  //        getRaQueryDataSet().goToRow(RowPointer);
  //    }
  //  }
  private void enableSifra() {
    try {
      rcc.setLabelLaF(this.jpDetailOdb.jrfSifra, true);
      rcc.setLabelLaF(this.jpDetailOdb.jrfNaziv, true);
      rcc.setLabelLaF(this.jpDetailOdb.jrfIme, true);
      rcc.setLabelLaF(this.jpDetailOdb.jbChoose, true);
    } catch (Exception ex) {
    }
    try {
      rcc.setLabelLaF(this.jpDetailOdb2.jrfSifra, true);
      rcc.setLabelLaF(this.jpDetailOdb2.jrfNaziv, true);
      rcc.setLabelLaF(this.jpDetailOdb2.jrfIme, true);
      rcc.setLabelLaF(this.jpDetailOdb2.jbChoose, true);
      rcc.setLabelLaF(this.jpDetailOdb2.jrfSifra2, true);
      rcc.setLabelLaF(this.jpDetailOdb2.jrfNaziv2, true);
      rcc.setLabelLaF(this.jpDetailOdb2.jbChoose2, true);
    } catch (Exception ex) {
    }
  }

  public void show() {
    if (vrOdb == -1 && param0) {
      this.pres.showPreselect(this, "Vrsta odbitaka");
      try {
        param0 = true;
        if (jpDetailOdb != null)
          jpDetail.remove(jpDetailOdb);
        if (jpDetailOdb2 != null)
          jpDetail.remove(jpDetailOdb2);
        nivoOdb = plUtil.getPlUtil().getStrNivo(vrOdb);
        osnovica = plUtil.getPlUtil().getOsnovica(vrOdb);
        if (nivoOdb.length() > 2) {
          if (nivoOdb.substring(0, 2).equals("RA")
              || nivoOdb.substring(2, 4).equals("RA"))
            this.setSize(625, 375);
          else
            this.setSize(625, 355);
          jpDetailOdb2 = new jpChooseOdb2(this, nivoOdb, jpDetail, false);
          jpDetail.add(jpDetailOdb2, BorderLayout.NORTH);
        } else if (nivoOdb.length() > 0) {
          this.setSize(622, 327);
          jpDetailOdb = new jpChooseOdb(this, nivoOdb, jpDetail);
          jpDetail.add(jpDetailOdb, BorderLayout.CENTER);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      super.show();
    }
  }

  public void this_hide() {
    if (param0)
      vrOdb = -1;
    super.this_hide();
  }

  public boolean notUnique() {
    if (plUtil.getPlUtil().checkGlobalOdbiciUnique(this.getRaQueryDataSet())) {
      JOptionPane.showConfirmDialog(this.jpDetail, "Zapis postoji !", "Greška",
          JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
      jpDetail.jp2.jraRBROdb.requestFocus();
      return true;
    }
    return false;
  }

  public void raQueryDataSet_navigated(com.borland.dx.dataset.NavigationEvent e) {
    enabOznVal();
  }

  private void enabOznVal() {
    if (plUtil.getPlUtil().getOznValEnable(this.vrOdb)) {
      jpDetail.jp2.jraOznVal.setVisible(true);
      jpDetail.jp2.jlOznVal.setVisible(true);
      jpDetail.jp2.jbOznVal.setVisible(true);
    } else {
      jpDetail.jp2.jraOznVal.setVisible(false);
      jpDetail.jp2.jlOznVal.setVisible(false);
      jpDetail.jp2.jbOznVal.setVisible(false);
    }
  }
}
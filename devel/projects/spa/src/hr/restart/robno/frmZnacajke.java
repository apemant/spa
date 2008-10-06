/****license*****************************************************************
**   file: frmZnacajke.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.RN_vrsub;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Assert;
import hr.restart.util.AssertionException;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.startFrame;

import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmZnacajke extends raMasterDetail {

  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();

  static frmZnacajke znac;

  JPanel jpMaster = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlVrsub = new JLabel();
  JraTextField jraVrsub = new JraTextField();
  JraTextField jraNazvrsub = new JraTextField();
  JLabel jlPrip = new JLabel();
  JlrNavField jlrPrip = new JlrNavField();
  JlrNavField jlrNazPrip = new JlrNavField();
  JLabel jlSB = new JLabel();
  JraTextField jraSB = new JraTextField();
  JraButton jbPrip = new JraButton();
  JPanel jpDetail = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  JLabel jlZnac = new JLabel();
  JLabel jlDohvat = new JLabel();
  JraTextField jraZnac = new JraTextField();
  JraTextField jraNaznac = new JraTextField();
  JraTextField jraDohvat = new JraTextField();
  JLabel jlTip = new JLabel();
  raComboBox jcbTip = new raComboBox() {
    public void this_itemStateChanged() {
      super.this_itemStateChanged();
      if (jcbTip.getSelectedIndex() > 1) {
        jraDohvat.setText("");
        rcc.setLabelLaF(jraDohvat, false);
      } else if (raDetail.getMode() != 'B') 
        rcc.setLabelLaF(jraDohvat, true);
    }
  };
  JraCheckBox jcbObavez = new JraCheckBox();
  JraCheckBox jcbParam = new JraCheckBox();
  //JraButton jb = new JraButton();
  String deleteSQL;

  public frmZnacajke() {
    try {
      znac = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmZnacajke getInstance() {
    return znac;
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jraVrsub, false);
    }
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      jraVrsub.requestFocus();
      getMasterSet().setString("NAZSERBR", "S/B");
    } else if (mode == 'I') {
      jraNazvrsub.requestFocus();
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jraVrsub) || vl.isEmpty(jraNazvrsub))
      return false;
    if (mode == 'N' && MasterNotUnique()) {
      jraVrsub.requestFocus();
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Vrsta subjekta s istom šifrom ve\u0107 postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
      //System.out.println(this.getMasterSet().rowCount());
      return false;
    }
    if (getMasterSet().getShort("CPRIP") == 0)
      getMasterSet().setShort("CPRIP", getMasterSet().getShort("CVRSUBJ"));
    return true;
  }

/*  public boolean DeleteCheckMaster() {
    deleteSQL = "";
    this.refilterDetailSet();
    if (this.getDetailSet().rowCount() > 0) {
      cvrsbj = mast.getShort("CVRSUBJ");
      deleteSQL = "DELETE FROM RN_znacajke WHERE cvrsubj = " + mast.getShort("CVRSUBJ");
    }
    return true;
  } */

//  public void AfterDeleteMaster() {
//    vl.runSQL("DELETE FROM RN_sifznac WHERE cvrsubj = "+cvrsbj);
//  }

  public void refilterDetailSet() {
    super.refilterDetailSet();
    this.setNaslovDetail("Podaci za vrstu subjekta - "+getMasterSet().getString("NAZVRSUBJ"));
  }

  public void EntryPointDetail(char mode) {
    if (mode == 'N') {
      EraseFields();
      //jb.setVisible(false);
    } else if (mode == 'I') {
      //jb.setVisible(true);
      jcbParam.setSelected((this.getDetailSet().getString("ZNACSIF").equals("D")));
      jcbObavez.setSelected((this.getDetailSet().getString("ZNACREQ").equals("D")));
    }
//    rcc.setLabelLaF(jraZnac, false);
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      EraseFields();
      getDetailSet().setShort("CZNAC", getNextZnac());
      jraNaznac.requestFocus();
      jcbTip.setSelectedIndex(0);
    } else if (mode == 'I' ){
      jraNaznac.requestFocus();
    }
    if (mode != 'N') {
      jcbParam.setSelected((this.getDetailSet().getString("ZNACSIF").equals("D")));
      jcbObavez.setSelected((this.getDetailSet().getString("ZNACREQ").equals("D")));
    }
  }
  
  private void checkColumn(DataSet ds, String column) {
    Column col = ds.hasColumn(column);
    Assert.notNull(col, column);
    Assert.is(col.getVisible() != com.borland.jb.util.TriStateProperty.FALSE, column);
  }
  
  private boolean validateDohvat() {
    VarStr gr = new VarStr(getDetailSet().getString("ZNACDOH"));
    if (gr.trim().length() == 0) return true;
    
    VarStr cols = null;
    int colon = gr.indexOf(':');
    if (colon >= 0) {
      cols = gr.copy(colon + 1, gr.length());
      gr.truncate(colon);
    }

    int f = gr.indexOf('+'), l = gr.lastIndexOf('+');
    String getter = gr.from(l + 1);
    DataSet ds;
    try {
      java.lang.reflect.Method m = dM.class.getMethod(getter, null);
      ds = (DataSet) m.invoke(dM.getDataModule(), null);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this.getWindow(), "Pogrešan getter "+getter+
        "() u dohvatu:\n"+gr, "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    String key = gr.left(f);
    String[] descs = gr.truncate(l).leftChop(f + 1).splitTrimmed('+');
    if (descs.length != 1) {
      JOptionPane.showMessageDialog(this.getWindow(), "Dohvat mora navesti toèno 2 kolone " +
          "i getter!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    try {
      checkColumn(ds, key);
      for (int i = 0; i < descs.length; i++)
        checkColumn(ds, descs[i]);
    } catch (AssertionException e) {
      JOptionPane.showMessageDialog(this.getWindow(), "Nepostoje\u0107a kolona '"+e.getMessage()+
         "' u DataSet-u "+getter+"(), u duhvatu:\n"+gr, "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (cols != null) {
      String[] ci = cols.splitTrimmed(',');
      for (int i = 0; i < ci.length; i++) {
        if (!Aus.isNumber(ci[i])) {
          JOptionPane.showMessageDialog(this.getWindow(), "Pogrešna vidljiva kolona '"+ci[i]+
              "' u popisu "+cols, "Greška", JOptionPane.ERROR_MESSAGE);
          return false;
        }
        int num = Aus.getNumber(ci[i]);
        if (num < 0 || num >= ds.getColumnCount()) {
          JOptionPane.showMessageDialog(this.getWindow(), "Pogrešna vidljiva kolona '"+ci[i]+
              "' u popisu "+cols, "Greška", JOptionPane.ERROR_MESSAGE);
          return false;
        }
      }
    }
    return true;
  }

  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jraNaznac)) return false;
    if (!getDetailSet().getString("ZNACTIP").equalsIgnoreCase("S") &&
        !getDetailSet().getString("ZNACTIP").equalsIgnoreCase("I"))
      getDetailSet().setString("ZNACDOH", "");
    else if (!validateDohvat()) return false;
    if (getDetailSet().isNull("CZNAC"))
      getDetailSet().setShort("CZNAC", getNextZnac());
    this.getDetailSet().setString("ZNACSIF", jcbParam.isSelected() ? "D" : "N");
    this.getDetailSet().setString("ZNACREQ", jcbObavez.isSelected() ? "D" : "N");
    return true;
  }

/*  public void AfterSaveDetail(char mode) {
    if (mode == 'N') {
      rpc.EnabDisab(true);
    }
   } */

  private void EraseFields() {
  }

  public boolean DeleteCheckDetail() {
    deleteSQL = "DELETE FROM RN_sifznac WHERE cvrsubj = "+getMasterSet().getShort("CVRSUBJ")+
                " AND cznac = "+getDetailSet().getShort("CZNAC");
    return true;
  }

  public void AfterDeleteDetail() {
    vl.runSQL(deleteSQL);
  }

  protected boolean MasterNotUnique() {
    return (RN_vrsub.getDataModule().getRowCount(
        Condition.equal("CVRSUBJ", getMasterSet().getShort("CVRSUBJ"))) > 0);
  }

  protected short getNextZnac() {
    vl.execSQL("SELECT MAX(cznac) as cznac FROM RN_znacajke WHERE cvrsubj ="+
               getMasterSet().getShort("CVRSUBJ"));
    vl.RezSet.open();
    Variant v = new Variant();
    vl.RezSet.getVariant("CZNAC", v);
    return (short) (v.getAsShort() + 1);
  }

  private void jbInit() throws Exception {

//    Asql.createMasterZnac(mast);

    this.setMasterSet(dm.getRN_vrsubUnos());
    this.setNaslovMaster("Vrste subjekata");
    this.setVisibleColsMaster(new int[] {0,1,2});
    this.setMasterKey(new String[] {"CVRSUBJ"});

    this.setDetailSet(dm.getRN_znacajke());
    this.setNaslovDetail("Podaci za vrstu subjekta");
    this.setVisibleColsDetail(new int[] {0,2});
    this.setDetailKey(new String[] {"CVRSUBJ", "CZNAC"});

    this.setMasterDeleteMode(DELDETAIL);

    xYLayout1.setWidth(535);
    xYLayout1.setHeight(110);
    jpMaster.setLayout(xYLayout1);
    jpDetail.setLayout(xYLayout2);
    jlVrsub.setText("Vrsta subjekta");
    jlSB.setText("Tekst S/B");
    jlDohvat.setText("Dohvat iz baze");

    jraVrsub.setDataSet(getMasterSet());
    jraVrsub.setColumnName("CVRSUBJ");
    jraNazvrsub.setDataSet(getMasterSet());
    jraNazvrsub.setColumnName("NAZVRSUBJ");
    jraSB.setDataSet(getMasterSet());
    jraSB.setColumnName("NAZSERBR");
    jlPrip.setText("Pripadnost");
    jlrPrip.setColumnName("CPRIP");
    jlrPrip.setNavColumnName("CVRSUBJ");
    jlrPrip.setDataSet(getMasterSet());
    jlrPrip.setColNames(new String[] {"NAZVRSUBJ"});
    jlrPrip.setVisCols(new int[]{0,1,2});
    jlrPrip.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazPrip});
    jlrPrip.setRaDataSet(dm.getRN_vrsub());
    jlrPrip.setSearchMode(0);
    jlrPrip.setNavButton(jbPrip);

    jlrNazPrip.setColumnName("NAZVRSUBJ");
    jlrNazPrip.setNavProperties(jlrPrip);
    jlrNazPrip.setSearchMode(1);


    xYLayout2.setWidth(560);
    xYLayout2.setHeight(110);
    jlZnac.setText("Podatak");
    jlTip.setText("Tip podatka");
    jcbObavez.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbObavez.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbObavez.setText("Obavezan unos  ");
    jcbParam.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbParam.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbParam.setText("Unos iz popisa  ");

    jraZnac.setDataSet(this.getDetailSet());
    jraZnac.setColumnName("CZNAC");
    jraNaznac.setDataSet(this.getDetailSet());
    jraNaznac.setColumnName("ZNACOPIS");
    jraDohvat.setDataSet(this.getDetailSet());
    jraDohvat.setColumnName("ZNACDOH");

    jcbTip.setRaDataSet(this.getDetailSet());
    jcbTip.setRaColumn("ZNACTIP");
    jcbTip.setRaItems(new String[][] {{"Tekstualni", "S"}, {"Cjelobrojni", "I"}, {"Datum", "D"},
                                      {"Decimalni, 2 mjesta", "2"}, {"Decimalni, 3 mjesta", "3"}});

    jpMaster.add(jlVrsub, new XYConstraints(15, 20, -1, -1));
    jpMaster.add(jraVrsub, new XYConstraints(150, 20, 75, -1));
    jpMaster.add(jraNazvrsub, new XYConstraints(230, 20, 275, -1));
    jpMaster.add(jlPrip, new XYConstraints(15, 45, -1, -1));
    jpMaster.add(jlrPrip, new XYConstraints(150, 45, 75, -1));
    jpMaster.add(jlrNazPrip, new XYConstraints(230, 45, 275, -1));
    jpMaster.add(jbPrip, new XYConstraints(510, 45, 21, 21));
    jpMaster.add(jlSB, new XYConstraints(15, 70, -1, -1));
    jpMaster.add(jraSB, new XYConstraints(150, 70, 355, -1));

    jpDetail.add(jlZnac, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jraZnac, new XYConstraints(150, 20, 75, -1));
    jpDetail.add(jraNaznac, new XYConstraints(230, 20, 310, -1));
    jpDetail.add(jlTip, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jcbTip, new XYConstraints(150, 45, 140, -1));
    jpDetail.add(jcbObavez, new XYConstraints(420, 45, 120, -1));
    jpDetail.add(jcbParam, new XYConstraints(300, 45, 115, -1));
    jpDetail.add(jlDohvat, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jraDohvat, new XYConstraints(150, 70, 390, -1));

    this.setJPanelMaster(jpMaster);
    this.setJPanelDetail(jpDetail);

    this.setMasterDeleteMode(raMasterDetail.EMPTYDEL);

    hr.restart.sisfun.raDataIntegrity.installFor(raDetail);
    raDetail.dataIntegrity().setProtectedColumns(new String[] {"ZNACSIF"});

    this.raDetail.addOption(new hr.restart.util.raNavAction("Popis vrijednosti", raImages.IMGHISTORY, KeyEvent.VK_F6) {
        public void actionPerformed(java.awt.event.ActionEvent ev) {
          keyF9pressed();
        }
      },3);
    this.raMaster.addOption(new hr.restart.util.raNavAction("Upute za izgled forme", raImages.IMGALIGNJUSTIFY, KeyEvent.VK_F6) {
        public void actionPerformed(java.awt.event.ActionEvent ev) {
          frmRNHints rnh = (frmRNHints)
            startFrame.getStartFrame().showFrame("hr.restart.robno.frmRNHints", 15,
              "Popis uputa za ekran unosa subjekata "+getMasterSet().getShort("CVRSUBJ")+
              " - " + getMasterSet().getString("NAZVRSUBJ"), false);
          rnh.setVrsub(getMasterSet().getShort("CVRSUBJ"));
          rnh.show();
        }
      },3);

  }

  private void keyF9pressed() {
    if (getDetailSet().getString("ZNACDOH").length() > 0) {
      JOptionPane.showMessageDialog(jpDetail,"Podatak se dohvaæa iz postojeæih tablica!",
          "",JOptionPane.WARNING_MESSAGE);
    } else if (this.getDetailSet().getString("ZNACSIF").equalsIgnoreCase("D")) {
      startFrame.getStartFrame().showFrame("hr.restart.robno.frmVrijednosti","");
      frmVrijednosti.getInstance().setTitle("Popis vrijednosti podatka - "+this.getDetailSet().getString("ZNACOPIS"));
      frmVrijednosti.getInstance().getJpTableView().fireTableDataChanged();
      //frmVrijednosti.getFrmVrijednosti().SetQuery(mast.getShort("CVRSUBJ"), this.getDetailSet().getShort("CZNAC"));
    } else {
      JOptionPane.showMessageDialog(jpDetail,"Podatak nije šifriran!","",JOptionPane.WARNING_MESSAGE);
    }
  }
}

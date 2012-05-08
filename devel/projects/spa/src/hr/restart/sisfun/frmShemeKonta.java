/****license*****************************************************************
**   file: frmShemeKonta.java
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

import hr.restart.baza.Vrdokum;
import hr.restart.baza.Vrshemek;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Locate;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmShemeKonta extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();

  JPanel jpMaster = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JlrNavField jlrNazDok = new JlrNavField() {
    public void after_lookUp() {
      checkMes();
    }
  };
  JlrNavField jlrDok = new JlrNavField() {
    public void after_lookUp() {
      checkMes();
    }
  };
  JraButton jbDok = new JraButton();
  JLabel jlDok = new JLabel();
  JLabel jlSkl = new JLabel();
  JLabel jlSkl2 = new JLabel();
  public JlrNavField jlrSkl = new JlrNavField();
  JlrNavField jlrNazSkl = new JlrNavField();
  JlrNavField jlrSkl2 = new JlrNavField();
  JlrNavField jlrNazSkl2 = new JlrNavField();
  JraButton jbSkl = new JraButton();
  JraButton jbSkl2 = new JraButton();

  String[] keym = new String[] {"VRDOK", "CSKL", "CSKLUL"};
  String[] keyd = new String[] {"VRDOK", "CSKL", "CSKLUL", "STAVKA"};
  private String app, nazcol, lastv = "";
  private boolean disablenew, two, foc;

  protected QueryDataSet mast = new QueryDataSet() {
    public void saveChanges() {
      this.post();
    }
    public boolean saveChangesSupported() {
      return false;
    }
  };

  protected QueryDataSet vrdok, vrsk;

//  QueryDataSet kontp = new QueryDataSet(), kontd = new QueryDataSet();

  JraButton jbAdvanced = new JraButton();
  JraButton jbSql = new JraButton();
  JPanel jpDetail = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  JLabel jlStavka = new JLabel();
  JLabel jlOzn = new JLabel();
  JLabel jlNaziv = new JLabel();
  JLabel jlPolje = new JLabel();
  JLabel jlKonto = new JLabel();
  JraTextField jraStavka = new JraTextField();
  JraTextField jraPolje = new JraTextField();
  JlrNavField jlrKonto = new JlrNavField() /*{
    protected void this_keyPressed(KeyEvent e) {
      if (e.getKeyCode() == e.VK_F9) {
        this.setSearchMode(1);
      }
      super.this_keyPressed(e);
    }
    public void focusGained(FocusEvent e) {
      super.focusGained(e);
      this.setSearchMode(0);
    }
  }*/;
  JraTextField jraNazStavka = new JraTextField();
  JlrNavField jlrNazKonto = new JlrNavField();
  JlrNavField jlrKarKonto = new JlrNavField();
  JraTextField jraKar = new JraTextField() {
    public void valueChanged() {
      String kar = getDetailSet().getString("KARAKTERISTIKA").toUpperCase();
      if (!kar.equals("P") && kar.equals("D")) kar = "D";
      getDetailSet().setString("KARAKTERISTIKA", kar);
    }
  };
  JraButton jbKonto = new JraButton();

  private frmShemeKonta() {
    this(null, "Vrsta sheme", "CVRSK", "OPISVRSK", "");
  }

//  private frmShemeKonta(DataSet ds, String name, String col, String nazcol) {
//    this(ds, name, col, nazcol, "");
//  }

  public frmShemeKonta(DataSet ds, String name, String col, String nazcol, String app) {
    try {
      setShema(ds, name, col, nazcol, app);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public QueryDataSet getVrdok() {
    return vrdok;
  }

  public void beforeShowMaster() {
    mast.refresh();
//    vrdok.refresh();
    if (jlrSkl.getRaDataSet().refreshSupported())
      jlrSkl.getRaDataSet().refresh();
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'N') {
      if (two)
        setSecond(false);
    } else if (mode == 'I') {
      rcc.EnabDisabAll(jpMaster, false);
    }
  }

  private void setSecond(boolean onoff) {
    if (jlSkl.getText().indexOf("kladište") > 0)
      jlSkl.setText(onoff ? "Izlazno skladište" : "Skladište");
    jlSkl2.setVisible(onoff);
    jlrSkl2.setVisible(onoff);
    jlrNazSkl2.setVisible(onoff);
    jbSkl2.setVisible(onoff);
    rcc.setLabelLaF(jlSkl2, onoff);
    rcc.setLabelLaF(jlrSkl2, onoff);
    rcc.setLabelLaF(jlrNazSkl2, onoff);
    rcc.setLabelLaF(jbSkl2, onoff);

  }

  private void checkMes() {
    if (two)
      setSecond(this.getMasterSet().getString("VRDOK").equals("MES"));
    else {
      String vc;
      if (getMasterSet().getString("VRDOK").length() == 0) vc = "vrdok = ''";
      else vc = "vrdok IN ('', '"+getMasterSet().getString("VRDOK")+"')";
      Vrshemek.getDataModule().setFilter(vrsk, "app = '"+app+"' AND "+vc).open();
//      System.out.println(vrsk.getQuery().getQueryString());
      if (foc) jlrSkl.forceFocLost();
    }
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      jlrDok.requestFocus();
    }
    if (mode != 'B') foc = true;
  }

  public boolean ValidacijaMaster(char mode) {
    /*if (jlrDok.getText().trim().equals("")) {
      jlrSkl.requestFocus();
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Obavezno izabrati vrstu dokumenta!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }*/
    if (vl.isEmpty(jlrSkl)) return false;
    if (this.getMasterSet().getString("VRDOK").equals("MES") && vl.isEmpty(jlrSkl2)) return false;
    if (!this.getMasterSet().getString("VRDOK").equals("MES"))
      this.getMasterSet().setString("CSKLUL", this.getMasterSet().getString("CSKL"));

    if (mode == 'N' && MasterNotUnique()) {
      jlrSkl.requestFocus();
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Shema ve\u0107 postoji u tablici!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (jlrDok.getText().trim().equals("")) {
      if (JOptionPane.showConfirmDialog(this.getJPanelMaster(),
           "Želite li prepisati sheme kontiranja za sve definirane vrste dokumenata?", null,
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        foc = false;
        copyAllSheme();
//        raMaster.getJpTableView().fireTableDataChanged();
        this.raMaster.getOKpanel().jPrekid_actionPerformed();
      }
      return false;
    } else {

      if (mode == 'N' || JOptionPane.showConfirmDialog(this.getJPanelMaster(),
           "Želite li ažurirati stavke sheme kontiranja "+jlrDok.getText().trim()+" dokumenata?", null,
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        if (!lookupData.getlookupData().raLocate(dm.getDefshkonta(), "VRDOK",
            jlrDok.getText(), Locate.CASE_INSENSITIVE)) {
          jlrSkl.requestFocus();
          JOptionPane.showMessageDialog(this.getJPanelMaster(),
             "Ne postoji standardna shema za dokumente tipa " + jlrDok.getText()
             + "!", "Greška", JOptionPane.ERROR_MESSAGE);
          return false;
        } //else raMaster.getJpTableView().fireTableDataChanged();
        foc = false;
        Asql.copyShemeKonta(mast.getString("VRDOK"), mast.getString("CSKL"), mast.getString("CSKLUL"));
        getDetailSet().refresh();
        String cskl = jlrSkl.getText();
        String vrdok = jlrDok.getText();
        getMasterSet().refresh();
        raMaster.getJpTableView().fireTableDataChanged();
        lookupData.getlookupData().raLocate(getMasterSet(), new String[] {"CSKL", "VRDOK"},
            new String[] {cskl, vrdok}, Locate.CASE_INSENSITIVE);
      }
    }
    foc = false;
    return true;
  }

  public void afterSetModeMaster(char bef, char aft) {
    if (aft == 'B') foc = false;
  }

/*  public void AfterAfterSaveMaster(char mode) {
    if (mode == 'N') {

      super.AfterAfterSaveMaster(mode);
      raDetail.setLockedMode('0');
      raDetail.getOKpanel().jPrekid_actionPerformed();
    }
  }*/

/*  public boolean DeleteCheckMaster() {
    deleteSQL = "";
    this.refilterDetailSet();
    if (this.getDetailSet().rowCount() > 0) {
      deleteSQL = "DELETE FROM shkonta WHERE vrdok = '" + mast.getString("VRDOK") +
                  "' and cskl = '" + mast.getString("CSKL") + "' and csklul = '" + mast.getString("CSKLUL") + "'";
      /*vl.runSQL("DELETE FROM shkonta WHERE vrdok = '" + mast.getString("VRDOK") +
       "' and cskl = '" + mast.getString("CSKL") + "'"); */
 /*   }
    return true;
  }*/

/*  public void AfterDeleteMaster() {
    if (!deleteSQL.equals(""))
      vl.runSQL(deleteSQL);
  } */

  public void refilterDetailSet() {
    super.refilterDetailSet();
    if (two) this.setNaslovDetail("Stavke sheme - " + mast.getString("VRDOK"));
    else this.setNaslovDetail("Stavke sheme " + mast.getString(nazcol));
  }

  /*public void ZatvoriOstaloDetail() {
    if (this.getDetailSet().rowCount() == 0) {
      this.getMasterSet().refresh();
      raMaster.getJpTableView().fireTableDataChanged();
    }
  }*/

  public boolean isNewDetailNeeded() {
    return false;
  }

  public void EntryPointDetail(char mode) {
    if (mode == 'N') {
      //EraseFields();
      //jraStavka.requestFocus();
    } else if (mode == 'I') {
      rcc.setLabelLaF(jraStavka, false);
      if (disablenew) {
        //rcc.setLabelLaF(jraNazStavka, false);
        rcc.setLabelLaF(jraKar, false);
      }
      setKonto(this.getDetailSet().getString("KARAKTERISTIKA"));
    }
//    super.afterSetModeDetail();
  }

  public void afterSetModeDetail(char oldm, char newm) {
    dlgUraIra.setEnabled(newm != 'B');
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      //EraseFields();
      //jraStavka.requestFocus();
      //raDetail.getOKpanel().jPrekid.doClick();
      jlrKonto.forceFocLost();
      int p = getDetailSet().getQuery().getQueryString().toLowerCase().indexOf("from");
      vl.execSQL("SELECT MAX(stavka) as maxs "+getDetailSet().getQuery().getQueryString().substring(p));
      vl.RezSet.open();
      getDetailSet().setShort("STAVKA", (short) (1 + vl.RezSet.getShort("MAXS")));
//      jraStavka.setText(Integer.toString(1 + vl.RezSet.getShort("MAXS")));
      jraNazStavka.requestFocus();
    } else if (mode == 'I' ){
      jlrKonto.requestFocus();
    }
  }

  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jraStavka) || vl.isEmpty(jlrKonto) || vl.isEmpty(jraKar))
      return false;
    String kar = this.getDetailSet().getString("KARAKTERISTIKA");
    if (!kar.equalsIgnoreCase("D") && !kar.equalsIgnoreCase("P")) {
      jraKar.requestFocus();
      JOptionPane.showMessageDialog(raDetail.getWindow(), "Neispravna karakteristika!", "Greška",
                                    JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (!kar.equalsIgnoreCase(jlrKarKonto.getText()) && !"O".equalsIgnoreCase(jlrKarKonto.getText())) {
      jraKar.requestFocus();
      JOptionPane.showMessageDialog(raDetail.getWindow(), "Karakteristika nekompatibilna s kontom!",
                                    "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    this.getDetailSet().setString("KARAKTERISTIKA", kar.toUpperCase());
    this.getDetailSet().setString("APP", app);
    System.out.println(this.getDetailSet());
    

    /*if (mode == 'N' && stavkaNotUnique()) {
      JOptionPane.showMessageDialog(this.jpDetail,"Stavka ve\u0107 u tablici!","Greška",
        JOptionPane.ERROR_MESSAGE);
      jraStavka.requestFocus();
      return false;
    }*/
/*    this.getDetailSet().setString("VRDOK", mast.getString("VRDOK"));
    this.getDetailSet().setString("CSKL", mast.getString("CSKL"));
    this.getDetailSet().setString("SKLUL", mast.getString("SKLUL"));
    this.getDetailSet().setString("KARAKTERISTIKA", (String) jcbKar.getSelectedItem()); */
    return true;
  }

  protected void EraseFields() {
    /*jraStavka.setText("");
    jraNazStavka.setText("");
    jlrKonto.setText("");
    jlrNazKonto.setText("");*/
  }

  protected boolean MasterNotUnique() {
    return (Asql.getStavkeShkonta(mast.getString("VRDOK"), mast.getString("CSKL"), mast.getString("CSKLUL")).rowCount() > 0);
  }

  private void copyAllSheme() {
    this.getMasterSet().cancel();
    Asql.copyAllShemeKonta(mast.getString("CSKL"), mast.getString("CSKLUL"));
    this.getMasterSet().refresh();
    this.getMasterSet().insertRow(false);
  }


//  public boolean doWithSaveMaster(char mode) {
//
//    return true;
//  }

 /* private void updateKar() {
    jcbKar.removeAllItems();
    if (jlrKonto.getText().equals("")) {
      jcbKar.addItem(" ");
      jcbKar.addItem("D");
      jcbKar.addItem("P");
    } else {
      if (jlrKar.getText().equals("O"))
        jcbKar.addItem(" ");
      if (jlrKar.getText().equals("O") || jlrKar.getText().equals("D"))
        jcbKar.addItem("D");
      if (jlrKar.getText().equals("O") || jlrKar.getText().equals("P"))
        jcbKar.addItem("P");
    }
    jcbKar.setSelectedIndex(0);
  }*/

  private void setMasterData(DataSet ds, String table, String col) {
//    String appcond, kcond;
//    if (app == null || app.trim().equals("")) appcond = "";
//    else appcond = " AND vrdokum.app = '"+app+"'";
//    if (disablenew) appcond = "";
//    appcond = " AND "+table+".app = '"+app+"'";
    String appcond = two ? "" : " AND "+table+".app = vrdokum.app";

    /*if (col.equalsIgnoreCase("CSKL"))
      kcond = " AND vrdokum.tipdok IN ('S','SF')";
    else if (col.equalsIgnoreCase("CORG"))
      kcond = " AND vrdokum.tipdok = 'F'";
    else kcond = ""; */

    String sql = "SELECT shkonta.cskl, MAX("+table+"."+nazcol+") as "+nazcol+", shkonta.csklul, "+
       "shkonta.vrdok, MAX(vrdokum.nazdok) as nazdok "+
       "FROM shkonta INNER JOIN vrdokum ON shkonta.vrdok = vrdokum.vrdok LEFT OUTER JOIN "+table+
       " ON shkonta.cskl = "+table+"."+col+" WHERE 1=1 "+
       appcond+/*kcond+*/" AND vrdokum.app = '"+app+"' GROUP BY shkonta.vrdok, shkonta.cskl, shkonta.csklul";
    //vl.execSQL(sql);
    mast.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(
      dM.getDataModule().getDatabase1(),sql
      ));
    //part = vl.RezSet;
//    System.err.println(ds);
//    System.err.println(mast);
    mast.setColumns(new Column[] {
      (Column) dm.getShkonta().getColumn("CSKL").clone(),
      (Column) ds.getColumn(nazcol).clone(),
      (Column) dm.getMeskla().getColumn("CSKLUL").clone(),
      (Column) dm.getShkonta().getColumn("VRDOK").clone(),
      (Column) dm.getVrdokum().getColumn("NAZDOK").clone()
//      (Column) dm.getOrgstruktura().getColumn("NAZIV").clone()
    });
    mast.getColumn("CSKL").setCaption(ds.getColumn(col).getCaption());
    mast.getColumn("NAZDOK").setCaption("Naziv dokumenta");
    mast.getColumn("VRDOK").setWidth(6);
//    mast.getColumn("NAZIV").setVisible(0);

    mast.open();
    mast.setRowId("VRDOK", true);
    mast.setRowId("NAZDOK", false);
    mast.setRowId("CSKL", true);
    mast.setRowId("CSKLUL", true);
    mast.setRowId(nazcol, false);
    mast.setTableName("shkonta_master_"+table);
    //part.setRowId();
    //vl.RezSet = null;

    if (col.equals("CSKL"))
      vrdok = Vrdokum.getDataModule().getFilteredDataSet("app = '"+app+"' AND knjiz='D' AND tipdok IN ('S','SF')");
    else if (col.equals("CORG"))
      vrdok = Vrdokum.getDataModule().getFilteredDataSet("app = '"+app+"' AND knjiz='D' AND tipdok='F'");
    else vrdok = Vrdokum.getDataModule().getFilteredDataSet("app = '"+app+"' AND knjiz='D'");
    vrdok.open();
  }

  private void setShema(DataSet ds, String name, String col, String nazcol, String app) {
//    key[1] = col;
    this.app = app;
    this.nazcol = nazcol;
    if (app.toUpperCase().equals("ROBNO") || app.toUpperCase().equals("MP") || app.toUpperCase().equals("RAC")) {
      disablenew = two = true;
    }
//    if (ds == null) ds = dm.getVrshemek();
    if (!two)
      ds = vrsk = Vrshemek.getDataModule().getFilteredDataSet("app = '"+app+"' AND vrdok=''");
//    System.out.println("HEREEEEE");
    jlrSkl.setNavColumnName(col);
    jlrSkl.setColNames(new String[] {nazcol});
    jlrSkl.setRaDataSet(ds);
    jlrNazSkl.setColumnName(nazcol);
    jlSkl.setText(name);
    ds.open();
    setMasterData(ds, col.equalsIgnoreCase("CORG") ? "Orgstruktura" : ds.getTableName(), col);
  }

  private void jbInit() throws Exception {
//    setMasterData();
//    initKonta();
    this.setMasterSet(mast);
    this.setNaslovMaster("Sheme kontiranja");
    this.setVisibleColsMaster(new int[] {0,1,3,4});
    this.setMasterKey(keym);

    this.setDetailSet(dm.getShkontaUnos());
    this.setNaslovDetail("Stavke sheme");
    this.setVisibleColsDetail(new int[] {2,3,5,6});
    this.setJPanelDetail(jpDetail);
    this.setDetailKey(keyd);

    super.setMasterDeleteMode(DELDETAIL);

    JPanel adv = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
    jbAdvanced.setText("Knjiga URA/IRA");
    jbSql.setText("SQL dohvat");
    adv.add(jbAdvanced);
    adv.add(jbSql);

    raDetail.getOKpanel().add(adv, BorderLayout.WEST);

    xYLayout1.setWidth(548);
    if (two)
      xYLayout1.setHeight(110);
    else
      xYLayout1.setHeight(85);
    jpMaster.setLayout(xYLayout1);


    jlrDok.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrDok.setColumnName("VRDOK");
    jlrDok.setDataSet(mast);
    jlrDok.setColNames(new String[] {"NAZDOK"});
    jlrDok.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazDok});
    jlrDok.setVisCols(new int[] {0,1});
    jlrDok.setSearchMode(0);
    jlrDok.setRaDataSet(vrdok);
    jlrDok.setNavButton(jbDok);

    jlrNazDok.setNavProperties(jlrDok);
    jlrNazDok.setColumnName("NAZDOK");
    jlrNazDok.setDataSet(mast);
    jlrNazDok.setSearchMode(1);

    jlrSkl.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrSkl.setColumnName("CSKL");
    jlrSkl.setDataSet(mast);
//    jlrSkl.setColNames(new String[] {"NAZSKL"});
    jlrSkl.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazSkl});
    jlrSkl.setVisCols(new int[] {0,1});
    jlrSkl.setSearchMode(0);
    jlrSkl.setNavButton(jbSkl);
//    jlrSkl.setRaDataSet(dm.getSklad());

    jlrNazSkl.setNavProperties(jlrSkl);
//    jlrNazSkl.setColumnName("NAZSKL");
    jlrNazSkl.setDataSet(mast);
    jlrNazSkl.setSearchMode(1);

    //jlrSklUl.setDataSet(mast);
    if (two) {
      jlSkl2.setText("Ulazno skladište");
      jlrSkl2.setHorizontalAlignment(SwingConstants.TRAILING);
      jlrSkl2.setColumnName("CSKLUL");
      jlrSkl2.setNavColumnName("CSKL");
      jlrSkl2.setDataSet(mast);
      jlrSkl2.setColNames(new String[] {"NAZSKL"});
      jlrSkl2.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazSkl2});
      jlrSkl2.setVisCols(new int[] {0,1});
      jlrSkl2.setSearchMode(0);
      jlrSkl2.setNavButton(jbSkl2);
      jlrSkl2.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());

      jlrNazSkl2.setNavProperties(jlrSkl2);
      jlrNazSkl2.setColumnName("NAZSKL");
//      jlrNazSkl2.setDataSet(mast);
      jlrNazSkl2.setSearchMode(1);
    }

    jlDok.setText("Vrsta dokumenta");
    jbDok.setText("...");
//    jlSkl.setText("Skladište");
    jbSkl.setText("...");

    jpDetail.setLayout(xYLayout2);
    xYLayout2.setWidth(560);
    xYLayout2.setHeight(85);
    jlStavka.setText("Stavka");
    jlOzn.setText("Broj");
    jlOzn.setHorizontalAlignment(SwingConstants.TRAILING);
    jlNaziv.setText("Naziv");
    jlPolje.setText("Kolona");
    jlKonto.setText("Konto");
    jbKonto.setText("...");

    jraStavka.setDataSet(this.getDetailSet());
    jraStavka.setColumnName("STAVKA");
    jraStavka.setHorizontalAlignment(SwingConstants.TRAILING);

    jraNazStavka.setDataSet(this.getDetailSet());
    jraNazStavka.setColumnName("OPIS");

    jraPolje.setDataSet(this.getDetailSet());
    jraPolje.setColumnName("POLJE");

    jlrKonto.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrKonto.setColumnName("BROJKONTA");
    jlrKonto.setDataSet(this.getDetailSet());
    jlrKonto.setColNames(new String[] {"NAZIVKONTA","KARAKTERISTIKA"});
    jlrKonto.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazKonto,jlrKarKonto});
    jlrKonto.setVisCols(new int[] {0,1});
    jlrKonto.setSearchMode(3);
    jlrKonto.setRaDataSet(dm.getKontaAnalitic());
    jlrKonto.setNavButton(jbKonto);

    jlrNazKonto.setNavProperties(jlrKonto);
    jlrNazKonto.setColumnName("NAZIVKONTA");
    jlrNazKonto.setSearchMode(1);

    jlrKarKonto.setNavProperties(jlrKonto);
    jlrKarKonto.setColumnName("KARAKTERISTIKA");
    jlrKarKonto.setSearchMode(1);

    jraKar.setColumnName("KARAKTERISTIKA");
    jraKar.setDataSet(this.getDetailSet());

    jpMaster.add(jlrNazDok, new XYConstraints(230, 20, 275, -1));
    jpMaster.add(jlrDok, new XYConstraints(150, 20, 75, -1));
    jpMaster.add(jlDok, new XYConstraints(15, 20, -1, -1));
    jpMaster.add(jlSkl, new XYConstraints(15, 45, -1, -1));
    jpMaster.add(jlrSkl, new XYConstraints(150, 45, 75, -1));
    jpMaster.add(jlrNazSkl, new XYConstraints(230, 45, 275, -1));
    jpMaster.add(jbSkl, new XYConstraints(510, 45, 21, 21));
    jpMaster.add(jbDok, new XYConstraints(510, 20, 21, 21));
    if (two) {
      jpMaster.add(jlSkl2, new XYConstraints(15, 70, -1, -1));
      jpMaster.add(jlrSkl2, new XYConstraints(150, 70, 75, -1));
      jpMaster.add(jlrNazSkl2, new XYConstraints(230, 70, 275, -1));
      jpMaster.add(jbSkl2, new XYConstraints(510, 70, 21, 21));
    }

    jpDetail.add(jlStavka, new XYConstraints(15, 30, -1, -1));
    jpDetail.add(jlOzn, new XYConstraints(150, 13, 74, -1));
    jpDetail.add(jlNaziv, new XYConstraints(231, 13, -1, -1));
    jpDetail.add(jlKonto, new XYConstraints(15, 55, -1, -1));
    jpDetail.add(jraStavka, new XYConstraints(150, 30, 75, -1));
    jpDetail.add(jlrKonto, new XYConstraints(150, 55, 75, -1));
    if (disablenew)
      jpDetail.add(jraNazStavka, new XYConstraints(230, 30, 315, -1));
    else {
      jpDetail.add(jraNazStavka, new XYConstraints(230, 30, 250, -1));
      jpDetail.add(jraPolje, new XYConstraints(485, 30, 60, -1));
      jpDetail.add(jlPolje, new XYConstraints(486, 13, -1, -1));
    }
    jpDetail.add(jlrNazKonto, new XYConstraints(230, 55, 250, -1));
    jpDetail.add(jbKonto, new XYConstraints(485, 55, 21, 21));
    jpDetail.add(jraKar, new XYConstraints(520, 55, 25, -1));

    if (disablenew)
        raDetail.disableAdd();

    this.setJPanelMaster(jpMaster);

    /*jraKar.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        super.focusLost(e);
        String kar = getDetailSet().getString("KARAKTERISTIKA").toUpperCase();
        if (!kar.equals("P") && kar.equals("D")) kar = "D";
        getDetailSet().setString("KARAKTERISTIKA", kar);
      }
    });*/

    jbAdvanced.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showAdvanced();
      }
    });
    
    jbSql.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showSql();
      }
    });
  }

  public void masterSet_navigated(NavigationEvent e) {
    if (!lastv.equals(mast.getString("VRDOK"))) {
      lastv = mast.getString("VRDOK");
      checkMes();
    }
  }

  private void setKonto(String kar) {
    if (kar.equals("P"))
      jlrKonto.setRaDataSet(dm.getKontaPot());
    else if (kar.equals("D"))
      jlrKonto.setRaDataSet(dm.getKontaDug());
    else
      jlrKonto.setRaDataSet(dm.getKontaAnalitic());
  }
  
  void showSql() {
    String ret = dlgSqlDohvat.showDlg(this.getWindow(), getDetailSet().getString("sqlcondition"));
    if (ret != null) {
      if (ret.length() > 5000) ret = ret.substring(0, 5000);
      getDetailSet().setString("sqlcondition", ret);
    }
  }

  void showAdvanced() {
    lookupData.getlookupData().raLocate(dm.getVrdokum(), "VRDOK", getMasterSet().getString("VRDOK"));
    if (dm.getVrdokum().getString("VRSDOK").equals("U")) {
      dlgUraIra.setUI(dlgUraIra.URA);
      dlgUraIra.open(raDetail);
    } else if (dm.getVrdokum().getString("VRSDOK").equals("I")) {
      dlgUraIra.setUI(dlgUraIra.IRA);
      dlgUraIra.open(raDetail);
    } else {
      JOptionPane.showMessageDialog(raDetail.getWindow(), "Pogrešna vrsta dokumenta!",
                                    "Greška", JOptionPane.ERROR_MESSAGE);
    }
    System.out.println(raDetail.getRaQueryDataSet());
  }
}

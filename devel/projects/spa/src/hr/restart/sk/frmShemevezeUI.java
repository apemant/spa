/****license*****************************************************************
**   file: frmShemevezeUI.java
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
import hr.restart.baza.ShemevezeUI;
import hr.restart.baza.Shkonta;
import hr.restart.baza.Vrdokum;
import hr.restart.baza.Vrshemek;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmShemevezeUI extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  JPanel jpMaster = new JPanel();
  XYLayout laym = new XYLayout();
  JLabel jlShem = new JLabel();
  JlrNavField jlrShem = new JlrNavField();
  JlrNavField jlrNazShem = new JlrNavField();
  JlrNavField jlrDokShem = new JlrNavField();
  JraButton jbSelShem = new JraButton();

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCskl = new JLabel();
  JLabel jlStavka1 = new JLabel();
  JLabel jlStavka = new JLabel();
  JLabel jlStopa = new JLabel();
//  JLabel jlVrdok = new JLabel();
  JraButton jbSelCskl = new JraButton();
  JraButton jbSelStavka1 = new JraButton();
  JraButton jbSelStavka = new JraButton();
//  JraButton jbSelVrdok = new JraButton();
  JraCheckBox jcbInvertna = new JraCheckBox();
  JraTextField jraStopa = new JraTextField();
//  JlrNavField jlrVrdok = new JlrNavField();
  JlrNavField jlrStavka = new JlrNavField();
  JlrNavField jlrStavka1 = new JlrNavField();
  JlrNavField jlrOpis1 = new JlrNavField();
  JlrNavField jlrOpis = new JlrNavField();

  JraTextField jraSt2 = new JraTextField();
  JraCheckBox jcbSt2 = new JraCheckBox();
//  JlrNavField jlrCskl = new JlrNavField() {
//    public void after_lookUp() {
//      afterShema();
//    }
//  };
//  JlrNavField jlrNazdok = new JlrNavField() {
//    public void after_lookUp() {
//      afterVrdok();
//    }
//  };
//  JlrNavField jlrOpisvrsk = new JlrNavField() {
//    public void after_lookUp() {
//      afterShema();
//    }
//  };
//  JlrNavField jlrVrdok = new JlrNavField() {
//    public void after_lookUp() {
//      afterVrdok();
//    }
//  };
  QueryDataSet shk = Shkonta.getDataModule().copyDataSet();
//  String vrdok = "";
//  String shema = "";

  protected QueryDataSet mast = new QueryDataSet() {
//    public void saveChanges() {
//      this.post();
//    }
    public boolean saveChangesSupported() {
      return false;
    }
  };

  public frmShemevezeUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void beforeShowMaster() {
    mast.refresh();
    jlrShem.getRaDataSet().refresh();
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'I')
      rcc.EnabDisabAll(jpMaster, false);
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      jlrShem.requestFocus();
    }
  }

  private void updateStopa2() {
    jraSt2.setVisible("D".equalsIgnoreCase(getDetailSet().getString("DODATNA")));
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jlrShem)) return false;
    if (mode == 'N' && MasterNotUnique()) {
      jlrShem.requestFocus();
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Shema veze ve\u0107 postoji u tablici!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    mast.post();
    return true;
  }

  private boolean MasterNotUnique() {
    return ShemevezeUI.getDataModule().getRowCount(
        Condition.equal("CSKL", mast).and(Condition.in("VRDOK", Vrdokum.getDataModule().getTempSet(Condition.equal("APP", getApp()))))) > 0;
  }

  public void refilterDetailSet() {
    super.refilterDetailSet();
    setShema(mast.getString("CSKL"), mast.getString("VRDOK"));
    setNaslovDetail("Veze za shemu - " + mast.getString("OPISVRSK"));
  }

  public void EntryPointDetail(char mode) {
    if (mode == 'I') {
      rcc.EnabDisabAll(jpDetail, false);
      rcc.setLabelLaF(jraStopa, true);
      rcc.setLabelLaF(jcbInvertna, true);
      rcc.setLabelLaF(jcbSt2, true);
      rcc.setLabelLaF(jraSt2, true);
    }
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      jraSt2.setVisible(false);
      jcbSt2.setSelected(false);
      getDetailSet().setBigDecimal("STOPA2", new java.math.BigDecimal(100));
      jlrStavka.forceFocLost();
      jlrStavka1.forceFocLost();
//      jlrVrdok.forceFocLost();
      jlrStavka.requestFocus();
    } else if (mode == 'I') {
      jraStopa.requestFocus();
    } else updateStopa2();
  }

  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jlrStavka) || vl.isEmpty(jlrStavka1))
      return false;
    if (mode == 'N' && DetailNotUnique()) {
      jlrStavka.requestFocus();
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Isti zapis ve\u0107 postoji u tablici!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    LinkMap links = getLinks();
    if (!links.isInjection()) {
      jlrStavka1.requestFocus();
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Odredišna stavka se veæ pojavljuje u shemi!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (checkForRecursion(links)) {
      jlrStavka1.requestFocus();
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Beskonaène petlje su nedopuštene!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  
  private class LinkMap {
    Map links = new HashMap();
    Set dests = new HashSet();
    
    boolean duplicates = false;
    
    public void addLink(short from, short to) {
      Short f = new Short(from);
      Short t = new Short(to);
      List dest = (List) links.get(f);
      if (dest == null) links.put(f, dest = new ArrayList());
      dest.add(t);
      
      if (!dests.add(t)) duplicates = true;
    }
    
    public boolean isInjection() {
      return !duplicates;
    }
    
    public List getLinksFrom(Short from) {
      return (List) links.get(from);
    }
    
    public Set getLinks() {
      return links.keySet();
    }
  }
  
  private LinkMap getLinks() {
    DataSet ds = ShemevezeUI.getDataModule().getTempSet("STAVKA1 STAVKA2",
        Condition.equal("CSKL", mast).and(Condition.equal("VRDOK", mast)));
    ds.open();
    LinkMap links = new LinkMap();
    for (ds.first(); ds.inBounds(); ds.next())
      links.addLink(ds.getShort("STAVKA1"), ds.getShort("STAVKA2"));
    links.addLink(getDetailSet().getShort("STAVKA1"), getDetailSet().getShort("STAVKA2"));
    return links;
  }
  
  private boolean checkForRecursion(LinkMap links) {
    for (Iterator i = links.getLinks().iterator(); i.hasNext(); )
      if (checkLoop((Short) i.next(), links, new HashSet())) return true;
    
    return false;
  }
  
  private boolean checkLoop(Short start, LinkMap links, Set visited) {
    visited.add(start);
    
    for (Iterator i = links.getLinksFrom(start).iterator(); i.hasNext(); ) {
      Short dest = (Short) i.next();
      if (visited.contains(dest)) return true;
      if (links.getLinksFrom(dest) != null && checkLoop(dest, links, visited)) return true;
    }
    return false;
  }

  private boolean DetailNotUnique() {
    return ShemevezeUI.getDataModule().getRowCount(
        Condition.equal("CSKL", mast).and(
        Condition.equal("STAVKA1", getDetailSet())).and(
        Condition.equal("STAVKA2", getDetailSet())).and(
        Condition.equal("VRDOK", getDetailSet()))) > 0;
  }

  private void setMasterData() {
    String sql = "SELECT shemevezeui.cskl, MAX(vrshemek.opisvrsk) as opisvrsk, "+
                 "MAX(vrshemek.vrdok) as vrdok FROM shemevezeui,vrshemek,vrdokum "+
                 "WHERE shemevezeui.cskl=vrshemek.cvrsk and shemevezeui.vrdok=vrdokum.vrdok and vrdokum.app='" + getApp() + "'" +
                 " GROUP BY shemevezeui.cskl";
    mast.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(
      dM.getDataModule().getDatabase1(),sql));
    mast.setColumns(new Column[] {
      (Column) dm.getShemevezeUI().getColumn("CSKL").clone(),
      (Column) dm.getVrshemek().getColumn("OPISVRSK").clone(),
      (Column) dm.getVrshemek().getColumn("VRDOK").clone()
    });

    mast.open();
    mast.setRowId("CSKL", true);
    mast.setRowId("OPISVRSK", false);
    mast.setRowId("VRDOK", true);
    mast.setTableName("shemevezaui_master");
  }

  public void detailSet_navigated(NavigationEvent e) {
    updateStopa2();
  }

  private void jbInit() throws Exception {
    setMasterData();
    this.setMasterSet(mast);
    this.setNaslovMaster("Vrste shema veza");
    this.setVisibleColsMaster(new int[] {0,1});
    this.setMasterKey(new String[] {"VRDOK","CSKL"});

    this.setJPanelDetail(this.jpDetail);
    this.setDetailSet(dm.getShemevezeUI());
    this.setVisibleColsDetail(new int[] {2, 3, 4});
    this.setDetailKey(new String[] {"VRDOK","CSKL"});

//    shk.setColumns(dm.getShkonta().cloneColumns());
    setShema("", "");

    jpMaster.setLayout(laym);
    laym.setWidth(546);
    laym.setHeight(60);

    jlShem.setText("Vrsta sheme");
    jlrShem.setColumnName("CSKL");
    jlrShem.setNavColumnName("CVRSK");
    jlrShem.setDataSet(this.getMasterSet());
    jlrShem.setColNames(new String[] {"OPISVRSK", "VRDOK"});
    jlrShem.setTextFields(new JTextComponent[] {jlrNazShem, jlrDokShem});
    jlrShem.setVisCols(new int[] {0, 1, 2});
    jlrShem.setSearchMode(0);
    jlrShem.setRaDataSet(getVrsk());
    jlrShem.setNavButton(jbSelShem);

    jlrNazShem.setDataSet(this.getMasterSet());
    jlrNazShem.setColumnName("OPISVRSK");
    jlrNazShem.setNavProperties(jlrShem);
    jlrNazShem.setSearchMode(1);

    jlrDokShem.setDataSet(this.getMasterSet());
    jlrDokShem.setColumnName("VRDOK");
    jlrDokShem.setNavProperties(jlrShem);
    jlrDokShem.setSearchMode(1);
    jlrDokShem.setVisible(false);

    jpDetail.setLayout(lay);
    lay.setWidth(586);
    lay.setHeight(110);

//    jbSelVrdok.setText("...");
    jlStavka.setText("Osnovica");
    jlStavka1.setText("Rezultat");
    jlStopa.setText("Stopa");
//    jlVrdok.setText("Vrsta dokumenta");

    jcbSt2.setColumnName("DODATNA");
    jcbSt2.setDataSet(this.getDetailSet());
    jcbSt2.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbSt2.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbSt2.setSelectedDataValue("D");
    jcbSt2.setText(" Dodatna izravna stopa ");
    jcbSt2.setUnselectedDataValue("N");

    jcbInvertna.setColumnName("INVERTNA");
    jcbInvertna.setDataSet(this.getDetailSet());
    jcbInvertna.setHorizontalAlignment(SwingConstants.LEFT);
    jcbInvertna.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbInvertna.setSelectedDataValue("D");
    jcbInvertna.setText(" Invertna ");
    jcbInvertna.setUnselectedDataValue("N");
    jraStopa.setColumnName("STOPA");
    jraStopa.setDataSet(this.getDetailSet());
    jraSt2.setColumnName("STOPA2");
    jraSt2.setDataSet(this.getDetailSet());

    jlrStavka.setColumnName("STAVKA1");
    jlrStavka.setNavColumnName("STAVKA");
    jlrStavka.setDataSet(this.getDetailSet());
    jlrStavka.setColNames(new String[] {"OPIS"});
    jlrStavka.setTextFields(new JTextComponent[] {jlrOpis});
    jlrStavka.setVisCols(new int[] {2, 3});
    jlrStavka.setSearchMode(0);
    jlrStavka.setRaDataSet(shk);
    jlrStavka.setNavButton(jbSelStavka);

    jlrOpis.setColumnName("OPIS");
    jlrOpis.setNavProperties(jlrStavka);
    jlrOpis.setSearchMode(1);

    jlrStavka1.setColumnName("STAVKA2");
    jlrStavka1.setNavColumnName("STAVKA");
    jlrStavka1.setDataSet(this.getDetailSet());
    jlrStavka1.setColNames(new String[] {"OPIS"});
    jlrStavka1.setTextFields(new JTextComponent[] {jlrOpis1});
    jlrStavka1.setVisCols(new int[] {2, 3});
    jlrStavka1.setSearchMode(0);
    jlrStavka1.setRaDataSet(shk);
    jlrStavka1.setNavButton(jbSelStavka1);

    jlrOpis1.setColumnName("OPIS");
    jlrOpis1.setNavProperties(jlrStavka1);
    jlrOpis1.setSearchMode(1);

/*    jlrCskl.setColumnName("CSKL");
    jlrCskl.setNavColumnName("CVRSK");
    jlrCskl.setDataSet(this.getDetailSet());
    jlrCskl.setColNames(new String[] {"OPISVRSK", "VRDOK"});
    jlrCskl.setTextFields(new JTextComponent[] {jlrOpisvrsk, jlrVrdok});
    jlrCskl.setVisCols(new int[] {0, 1});
    jlrCskl.setSearchMode(0);
//    jlrCskl.setRaDataSet(frmVrskSk.getInstance().getRaQueryDataSet());
    jlrCskl.setRaDataSet(getVrsk());
    jlrCskl.setNavButton(jbSelCskl);

    jlrOpisvrsk.setColumnName("OPISVRSK");
    jlrOpisvrsk.setNavProperties(jlrCskl);
    jlrOpisvrsk.setSearchMode(1); */

/*    jlrVrdok.setColumnName("VRDOK");
    jlrVrdok.setNavProperties(jlrCskl);
    jlrVrdok.setDataSet(this.getDetailSet());
    jlrVrdok.setSearchMode(1);
    jlrVrdok.setVisible(false); */

//    jlrVrdok.setColumnName("VRDOK");
//    jlrVrdok.setDataSet(this.getRaQueryDataSet());
//    jlrVrdok.setColNames(new String[] {"NAZDOK"});
//    jlrVrdok.setTextFields(new JTextComponent[] {jlrNazdok});
//    jlrVrdok.setVisCols(new int[] {0, 1});
//    jlrVrdok.setSearchMode(0);
//    jlrVrdok.setRaDataSet(frmShemeKontaSk.getInstance().getVrdok());
//    jlrVrdok.setNavButton(jbSelVrdok);
//
//    jlrNazdok.setColumnName("NAZDOK");
//    jlrNazdok.setNavProperties(jlrVrdok);
//    jlrNazdok.setSearchMode(1);

    super.setMasterDeleteMode(DELDETAIL);

//    jpDetail.add(jbSelCskl, new XYConstraints(510, 20, 21, 21));
    jpDetail.add(jbSelStavka, new XYConstraints(550, 20, 21, 21));
    jpDetail.add(jbSelStavka1, new XYConstraints(550, 45, 21, 21));
//    jpDetail.add(jbSelVrdok, new XYConstraints(510, 45, 21, 21));
    jpDetail.add(jcbInvertna, new XYConstraints(230, 70, -1, -1));
//    jpDetail.add(jlCskl, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlStavka, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlStavka1, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlStopa, new XYConstraints(15, 70, -1, -1));
//    jpDetail.add(jlVrdok, new XYConstraints(15, 45, -1, -1));
//    jpDetail.add(jlrCskl, new XYConstraints(150, 20, 75, -1));
//    jpDetail.add(jlrNazdok, new XYConstraints(230, 45, 275, -1));
    jpDetail.add(jlrOpis, new XYConstraints(230, 20, 315, -1));
    jpDetail.add(jlrOpis1, new XYConstraints(230, 45, 315, -1));
//    jpDetail.add(jlrOpisvrsk, new XYConstraints(230, 20, 275, -1));
    jpDetail.add(jlrStavka, new XYConstraints(150, 20, 75, -1));
    jpDetail.add(jlrStavka1, new XYConstraints(150, 45, 75, -1));
//    jpDetail.add(jlrVrdok, new XYConstraints(150, 45, 75, -1));
    jpDetail.add(jraStopa, new XYConstraints(150, 70, 75, -1));
    jpDetail.add(jcbSt2, new XYConstraints(305, 70, -1, -1));
    jpDetail.add(jraSt2, new XYConstraints(470, 70, 75, -1));
//    jpDetail.add(jlrVrdok, new XYConstraints(140, 20, 5, -1));

    jpMaster.add(jlShem, new XYConstraints(15, 20, -1, -1));
    jpMaster.add(jlrShem, new XYConstraints(150, 20, 75, -1));
    jpMaster.add(jlrNazShem, new XYConstraints(230, 20, 275, -1));
    jpMaster.add(jlrDokShem, new XYConstraints(507, 20, 1, -1));
    jpMaster.add(jbSelShem, new XYConstraints(510, 20, 21, 21));

    jcbSt2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        updateStopa2();
      }
    });

    this.setJPanelMaster(jpMaster);

//    this.getJpTableView().addTableModifier(
//        new raTableColumnModifier("CSKL", new String[] {"CVRSK", "OPISVRSK"}, new String[] {"CSKL"},
//            new String[] {"CVRSK"}, frmVrskSk.getInstance().getRaQueryDataSet()));
    this.raDetail.getJpTableView().addTableModifier(
        new raTableColumnModifier("STAVKA1", new String[] {"STAVKA", "OPIS"},
        new String[] {"STAVKA1", "CSKL", "VRDOK"},
        new String[] {"STAVKA", "CSKL", "VRDOK"}, dm.getShkonta()));
    this.raDetail.getJpTableView().addTableModifier(
        new raTableColumnModifier("STAVKA2", new String[] {"STAVKA", "OPIS"},
        new String[] {"STAVKA2", "CSKL", "VRDOK"},
        new String[] {"STAVKA", "CSKL", "VRDOK"}, dm.getShkonta()));
  }

  private void setShema(String sh, String vr) {
    if (!sh.equals("") && !vr.equals(""))
      Shkonta.getDataModule().setFilter(shk, "cskl = '"+sh+"' AND vrdok = '"+vr+"'").open();
    else Shkonta.getDataModule().setFilter(shk, "1=0").open();
  }

/*  private void afterShema() {
    if (!this.getDetailSet().getString("CSKL").equals(shema))
      clearStavke();
//    System.out.println(jlrCskl.getRaDataSet());
//    System.out.println(this.getRaQueryDataSet());
    shema = this.getDetailSet().getString("CSKL");
    vrdok = this.getDetailSet().getString("VRDOK");
  } */

//  private void afterVrdok() {
//    if (!this.getRaQueryDataSet().getString("VRDOK").equals(vrdok))
//      clearStavke();
//    vrdok = this.getRaQueryDataSet().getString("VRDOK");
//  }

  private void clearStavke() {
    jlrStavka1.setText("");
    jlrStavka1.forceFocLost();
    jlrStavka.setText("");
    jlrStavka.forceFocLost();
  }

  private QueryDataSet getVrsk() {
    QueryDataSet vrsk = Vrshemek.getDataModule().getFilteredDataSet(
      "app = '"+getApp()+"' AND vrdok IS NOT NULL AND vrdok != '' AND "+
      "EXISTS (SELECT * FROM shkonta where shkonta.cskl=vrshemek.cvrsk)");
    return vrsk;
  }
  
  protected String getApp() {
    return "sk";
  }
/*  public void raQueryDataSet_navigated(NavigationEvent e) {
    if (!this.getDetailSet().getString("CSKL").equals(shema) ||
        !this.getDetailSet().getString("VRDOK").equals(vrdok)) {
      shema = this.getDetailSet().getString("CSKL");
      vrdok = this.getDetailSet().getString("VRDOK");
      setShema(shema, vrdok);
    }
  } */

}

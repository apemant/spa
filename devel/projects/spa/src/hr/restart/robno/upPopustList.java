/****license*****************************************************************
**   file: upPopustList.java
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
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class upPopustList extends raUpitLite {
  dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  Valid vl = Valid.getValid();
  raCommonClass rcc = raCommonClass.getraCommonClass();

  JPanel glavniPanel = new JPanel();
  private XYLayout xyLay = new XYLayout();

  TableDataSet tds = new TableDataSet();
  QueryDataSet qds = new QueryDataSet();

  JLabel jlCskl = new JLabel();
  JlrNavField jlrCskl = new JlrNavField();
  JlrNavField jlrNazskl = new JlrNavField();
  JraButton jbSelCskl = new JraButton();

  JLabel jlVrdok = new JLabel();
  JlrNavField jlrVrdok = new JlrNavField();
  JlrNavField jlrNazdok = new JlrNavField();
  JraButton jbSelVrdok = new JraButton();

  JLabel jlDatum = new JLabel();
  JraTextField pocDat = new JraTextField();
  JraTextField zavDat = new JraTextField();

  public upPopustList() {
    try {
      jbInit();
      upl = this;
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  static upPopustList upl;

  public static upPopustList getInstance(){
    return upl;
  }

  public void componentShow() {
    System.out.println("this will bi \"lista popusta\"");
    setDefolt();
  }

  public boolean Validacija(){
    boolean povratak = true;
    if (!vl.isValidRange(pocDat,zavDat)) {
      povratak = false;
    }
    if (vl.isEmpty(jlrCskl)) {
      povratak = false;
    }
    if (!povratak) runFirstESC();
    return povratak;
  }

  boolean lupiEsc = false;

  public void okPress() {
    lupiEsc = true;
    setQDS();
  }


  private String getUpit(){
    Condition datumRange = Condition.between("DOKI.DATDOK",tds.getTimestamp("PDAT"),tds.getTimestamp("ZDAT"));
    Condition cskl = Condition.equal("CSKL",tds.getString("CSKL"));
    Condition vrdok = Condition.equal("VRDOK",tds.getString("VRDOK"));
//    System.out.println("datum condition = " + datumRange);
//    System.out.println("cskl condition  = " + cskl);
//    System.out.println("vrdok condition = " + vrdok);

    String qdStr = "SELECT max(doki.datdok) as datdok, max(stdoki.cskl) as cskl, max(stdoki.vrdok) as vrdok, max(stdoki.god) as god, "+
                   "max(stdoki.brdok) as brdok, max(stdoki.uprab) as uprab, sum(stdoki.uirab) as uirab, sum(stdoki.iprodbp) as iprodbp, "+
                   "sum(stdoki.iprodsp) as iprodsp "+
                   "FROM doki, stdoki "+
                   "WHERE stdoki.cskl = doki.cskl AND stdoki.vrdok = doki.vrdok AND stdoki.god = doki.god AND stdoki.brdok = doki.brdok "+
                   "and stdoki.uprab != 0.0 and " + cskl.qualified("stdoki") + " "+
                   "and " + datumRange;
    if (!tds.getString("VRDOK").equals("")){
      qdStr += " and " + vrdok.qualified("stdoki") + " ";
    } else {
      qdStr += " ";
    }
    qdStr += "group by doki.datdok, doki.vrdok, doki.god, doki.brdok, stdoki.uprab";

    System.out.println(qdStr);

    return qdStr;
  }

  private void setQDS(){
    qds = ut.getNewQueryDataSet(getUpit());
    System.out.println("qds.rowcount = " + qds.rowCount());
  }

  public void firstESC() {
    rcc.EnabDisabAll(this.getJPan(),true);
    System.out.println("firstesc");
    jlrCskl.setText("");
    jlrCskl.emptyTextFields();
    lupiEsc = false;
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jlrCskl.requestFocus();
      }
    });
  }

  public boolean runFirstESC() {
    System.out.println("runit - firstesc");
    if (lupiEsc) return true;
    return false;
  }

  private void jbInit() throws Exception {
    this.addReport("hr.restart.robno.repListaPopusta","hr.restart.robno.repListaPopusta","ListaPopusta","Lista popusta");
    tds.setColumns(new Column[] {
      dm.createTimestampColumn("PDAT", "Po\u010Detni datum"),
      dm.createTimestampColumn("ZDAT", "Završni datum"),
      dm.createStringColumn("VRDOK", "Vrsta dokumenta", 3),
      dm.createStringColumn("CSKL", "Skladište", 12)
    });
    tds.open();

    jlCskl.setText("Skladište");

    jlrCskl.setColumnName("CSKL");
    jlrCskl.setDataSet(this.tds);
    jlrCskl.setColNames(new String[] {"NAZSKL"});
    jlrCskl.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazskl});
    jlrCskl.setVisCols(new int[] {0,1});
    jlrCskl.setSearchMode(0);
    jlrCskl.setRaDataSet(rut.getSkladFromCorg());
    jlrCskl.setNavButton(jbSelCskl);

    jlrNazskl.setColumnName("NAZSKL");
    jlrNazskl.setNavProperties(jlrCskl);
    jlrNazskl.setSearchMode(1);

    jlVrdok.setText("Vrsta dokumenta");

    jlrVrdok.setColumnName("VRDOK");
    jlrVrdok.setDataSet(this.tds);
    jlrVrdok.setColNames(new String[] {"NAZDOK"});
    jlrVrdok.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazdok});
    jlrVrdok.setVisCols(new int[] {0,1});
    jlrVrdok.setSearchMode(0);
    jlrVrdok.setRaDataSet(izlazFandSF());
    jlrVrdok.setNavButton(jbSelVrdok);

    jlrNazdok.setColumnName("NAZDOK");
    jlrNazdok.setNavProperties(jlrVrdok);
    jlrNazdok.setSearchMode(1);

    jlDatum.setText("Datum (od - do)");
    pocDat.setDataSet(tds);
    pocDat.setColumnName("PDAT");
    zavDat.setDataSet(tds);
    zavDat.setColumnName("ZDAT");

    glavniPanel.setLayout(xyLay);
    xyLay.setWidth(645);
    xyLay.setHeight(130);
    this.setJPan(glavniPanel);

    glavniPanel.add(jlCskl, new XYConstraints(15, 20, -1, -1));
    glavniPanel.add(jlrCskl, new XYConstraints(150, 20, 100, -1));
    glavniPanel.add(jlrNazskl, new XYConstraints(255, 20, 350, -1));
    glavniPanel.add(jbSelCskl, new XYConstraints(610, 20, 21, 21));

    glavniPanel.add(jlVrdok, new XYConstraints(15, 45, -1, -1));
    glavniPanel.add(jlrVrdok, new XYConstraints(150, 45, 100, -1));
    glavniPanel.add(jlrNazdok, new XYConstraints(255, 45, 350, -1));
    glavniPanel.add(jbSelVrdok, new XYConstraints(610, 45, 21, 21));

    glavniPanel.add(jlDatum, new XYConstraints(15, 70, 100, -1));
    glavniPanel.add(pocDat, new XYConstraints(150, 70, 100, -1));
    glavniPanel.add(zavDat, new XYConstraints(255, 70, 100, -1));
  }

  private void setDefolt(){
    tds.setTimestamp("PDAT", ut.getFirstDayOfMonth());
    tds.setTimestamp("ZDAT", vl.getToday());
    tds.setString("CSKL",hr.restart.sisfun.raUser.getInstance().getDefSklad());
    tds.setString("VRDOK","");
    jlrCskl.forceFocLost();
  }

  public DataSet getRepSet(){
    return qds;
  }

  public java.sql.Timestamp getDatumOd(){
    return tds.getTimestamp("PDAT");
  }

  public java.sql.Timestamp getDatumDo(){
    return tds.getTimestamp("ZDAT");
  }

  public boolean isIspis(){
    return false;
  }

  public boolean ispisNow(){
    if (qds.rowCount() == 0) {
      jlrCskl.requestFocus();
      JOptionPane.showConfirmDialog(this.getJPan(),"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  private QueryDataSet izlazFandSF(){
    QueryDataSet ifasf = ut.getNewQueryDataSet("SELECT vrdok, nazdok FROM Vrdokum WHERE vrsdok='I' and tipdok in ('F','SF') and app='robno'",false);
    ifasf.setColumns(new Column[] {
      (Column)dm.getVrdokum().getColumn("VRDOK").clone(),
      (Column)dm.getVrdokum().getColumn("NAZDOK").clone()
    });
    ifasf.open();
    return ifasf;
  }
}

















/****license*****************************************************************
**   file: jpZnac.java
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

import javax.swing.JPanel;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

/*class Podatak {

//  static sysoutTEST sys = new sysoutTEST(false);
  Valid vl = Valid.getValid();

  static TableDataSet ds;

  String tip;
  Column col;
  JraTextField jra;
  JlrNavField jlr, jlrn;
  JraButton jb;
  JLabel l;

  short cvrsubj, cznac;
  boolean obavez;

  public static void clearColumns() {
    ds = new TableDataSet();
    ds.setTableName("ATRIBUTI");
  }

  public static void createRow() {
    ds.insertRow(false);
  }

  public static void setColumns(Object[] pod) {
    Column[] cols = new Column[pod.length];
    for (int i = 0; i < pod.length; i++)
      cols[i] = ((Podatak) pod[i]).col;
    ds.close();
    ds.setColumns(cols);
    ds.open();
    ds.insertRow(false);
    ds.post();
  }

  public Podatak(DataSet znset, boolean def) {
    tip = znset.getString("ZNACSIF").equals("D") ? "NAV" : znset.getString("ZNACTIP");
    cvrsubj = znset.getShort("CVRSUBJ");
    cznac = znset.getShort("CZNAC");
    String opis = znset.getString("ZNACOPIS");
    obavez = znset.getString("ZNACREQ").equals("D");
    l = new JLabel();
    l.setText(opis);
    col = new Column();
    col.setColumnName(cvrsubj + "POD" + cznac);
    col.setTableName("ATRIBUTI");
    col.setCaption(opis);
    col.setPrecision(50);
    //col.setScale(2);
    if (tip.equals("NAV")) {
      col.setDataType(Variant.STRING);
      jb = new JraButton();
      jb.setText("...");
      jlr = new JlrNavField();
      jlrn = new JlrNavField();
      jlr.setColumnName(col.getColumnName());
      jlr.setDataSet(ds);
      jlr.setSearchMode(0);
      jlr.setRaDataSet(Asql.getVriSifZnac(cvrsubj, cznac));
      jlr.setColNames(new String[] {"OPIS"});
      jlr.setTextFields(new JlrNavField[] {jlrn});
      jlr.setNavColumnName("VRIZNAC");
      jlr.setVisCols(new int[] {4,5});
      jlr.setNavButton(jb);

      jlrn.setColumnName("OPIS");
      jlrn.setNavProperties(jlr);
      jlrn.setSearchMode(1);

    } else {
      jra = new JraTextField();
      if (tip.equals("S"))
        col.setDataType(Variant.STRING);
      else if (tip.equals("I")) {
        col.setDataType(Variant.INT);
        if (def) col.setDefault("0");
      } else if (tip.equals("D")) {
        col.setDataType(Variant.TIMESTAMP);
        col.setDisplayMask("dd-MM-yyyy");
        col.setEditMask("dd-MM-yyyy");
        new hr.restart.swing.raDatePopup(jra);
      } else if (tip.equals("2")) {
        col.setDataType(Variant.BIGDECIMAL);
        if (def) col.setDefault("0");
        col.setScale(2);
        col.setDisplayMask("###,###,##0.00");
      } else if (tip.equals("3")) {
        col.setDataType(Variant.BIGDECIMAL);
        if (def) col.setDefault("0");
        col.setScale(3);
        col.setDisplayMask("###,###,##0.000");
      }
//      jra = new JraTextField();
      jra.setDataSet(ds);
      if (tip.equals("D"))
        jra.setHorizontalAlignment(SwingConstants.CENTER);
      jra.setColumnName(col.getColumnName());
    }
  }

  public boolean isNav() {
    return "NAV".equalsIgnoreCase(tip);
  }

  public JraTextField getField() {
    if (tip.equals("NAV"))
      return jlr;
    else
      return jra;
  }

  public JlrNavField getFieldNaziv() {
    return jlrn;
  }

  public JraButton getButton() {
    if (tip.equals("NAV"))
      return jb;
    else
      return null;
  }

  public JLabel getLabel() {
    return l;
  }

  public void setStringValue(String value) {
    try {
      if (tip.equals("NAV") || tip.equals("S"))
        ds.setString(col.getColumnName(), value);
      else if (tip.equals("2") || tip.equals("3"))
        ds.setBigDecimal(col.getColumnName(), new BigDecimal(value));
      else if (tip.equals("I"))
        ds.setInt(col.getColumnName(), Integer.parseInt(value));
      else ds.setTimestamp(col.getColumnName(), Timestamp.valueOf(value));
      if (isNav()) jlr.forceFocLost();
    } catch (Exception e) {}
  }

  public String getStringValue() {
    if (tip.equals("NAV") || tip.equals("S"))
      return ds.getString(col.getColumnName());
    else if (tip.equals("2") || tip.equals("3"))
      return ds.getBigDecimal(col.getColumnName()).toString();
    else if (tip.equals("I"))
      return "" + ds.getInt(col.getColumnName());
    else return ds.getTimestamp(col.getColumnName()).toString();
  }

  public short getZnac() {
    return cznac;
  }

  public short getVrsubj() {
    return cvrsubj;
  }

  public boolean isObavezan() {
    return obavez;
  }
}


*/
public class jpZnac extends JPanel {
/*  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
  sysoutTEST sys = new sysoutTEST(false);

  XYLayout xy = new XYLayout();

  ArrayList podaci;
  String csubrn, vrsub;
  short cvrsubj;

  public jpZnac() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setFields(short _cvrs) {
    setFields(_cvrs, true);
  }

  public void setFields(short _cvrs, boolean def) {
    DataSet znac;
    Podatak p;
    int i = 0, y = 5;

    cvrsubj = _cvrs;
    this.removeAll();

    csubrn = "";
    boolean prip = ld.raLocate(dm.getRN_vrsub(), "CVRSUBJ", String.valueOf(_cvrs));
    vrsub = dm.getRN_vrsub().getString("NAZVRSUBJ");

    znac = RN_znacajke.getDataModule().getTempSet("1=0");
    znac.open();

    while (prip) {
      DataSet ds = Asql.getZnacVrsub(_cvrs);
      for (ds.first(); ds.inBounds(); ds.next()) {
        znac.insertRow(false);
        dM.copyColumns(ds, znac);
      }
      prip = (_cvrs != dm.getRN_vrsub().getShort("CPRIP")) &&
             ld.raLocate(dm.getRN_vrsub(), "CVRSUBJ", String.valueOf(
             _cvrs = dm.getRN_vrsub().getShort("CPRIP")));
    }

    if (znac.rowCount() > 0) {
      Podatak.clearColumns();
      podaci = new ArrayList();
      for (znac.last(); znac.inBounds(); znac.prior()) {
        podaci.add(p = new Podatak(znac, def));
        this.add(p.getLabel(), new XYConstraints(15, y, 130, -1));
        if (p.tip.equals("NAV")) {
          this.add(p.getField(), new XYConstraints(150, y, 100, -1));
          this.add(p.getFieldNaziv(), new XYConstraints(255, y, 200, -1));
        } else if (znac.getString("ZNACTIP").equals("S"))
          this.add(p.getField(), new XYConstraints(150, y, 305, -1));
        else
          this.add(p.getField(), new XYConstraints(150, y, 100, -1));
        if (p.getButton() != null)
          this.add(p.getButton(), new XYConstraints(460, y, 21, 21));
        ++i;
        y += 25;
      }
      Podatak.setColumns(podaci.toArray());
      xy.setHeight(y + 5);
    } else xy.setHeight(0);
  }

  public void setValues(String newsub) {
    if (newsub.equals(csubrn)) return;
    csubrn = newsub;
    if (csubrn.equals("")) return;

    Podatak.ds.first();
//    sys.prn(Podatak.ds);
    QueryDataSet vri = Asql.getVriznacSubjekta(csubrn);
    //sys.prn(vl.RezSet);
    for (vri.first(); vri.inBounds(); vri.next()) {
      Iterator it = podaci.iterator();
      Podatak p = null;
      while (it.hasNext()) {
        p = (Podatak) it.next();
        if (p.col.getColumnName().equals(vri.getShort("CVRSUBJ") + "POD" + vri.getShort("CZNAC")))
          break;
        p = null;
      }
      if (p != null) {
        p.setStringValue(vri.getString("VRIZNAC"));
      } else {
        System.out.println("nepostoje\u0107a kolona " + vri.getShort("CVRSUBJ") + "POD" + vri.getShort("CZNAC"));
        System.out.println("subjekt "+csubrn);
      }
    }
  }

  public void updateValues() {
    Iterator it = podaci.iterator();
    Podatak p;

//    dm.getRNZnacSub().close();
//    dm.getRNZnacSub().closeStatement();
//    dm.getRNZnacSub().setQuery(new QueryDescriptor(dm.getDatabase1(),
//      dm.getRN_znacsub().getOriginalQueryString() + " WHERE csubrn = '"+csubrn+"'", null, true, Load.ALL));
    RN_znacsub.getDataModule().setFilter(dm.getRNZnacSub(), "csubrn = '"+csubrn+"'");
    dm.getRNZnacSub().open();

    while (it.hasNext()) {
      p = (Podatak) it.next();
      if (!ld.raLocate(dm.getRNZnacSub(), new String[] {"CVRSUBJ","CZNAC"},
          new String[] {String.valueOf(p.getVrsubj()), String.valueOf(p.getZnac())})) {
        dm.getRNZnacSub().insertRow(false);
        dm.getRNZnacSub().setString("CSUBRN", csubrn);
        dm.getRNZnacSub().setShort("CZNAC", p.getZnac());
        dm.getRNZnacSub().setShort("CVRSUBJ", p.getVrsubj());
      }
      dm.getRNZnacSub().setString("VRIZNAC", p.getStringValue());
    }
//    dm.getRNZnacSub().saveChanges();
//    dm.getRN_znacsub().refresh();
  }

  public void cancel() {
    Podatak.ds.cancel();
  }

  public void insert() {
    Podatak.createRow();
  }

  public int getFieldCount() {
    return podaci.size();
  }

  public String getVrsub() {
    return vrsub;
  }

  public void setCsubrn(String cs) {
    csubrn = cs;
  }

  public String getCsubrn() {
    return csubrn;
  }

  public short getCvrsubj() {
    return cvrsubj;
  }

  public short getCznac(int idx) {
    return ((Podatak) podaci.get(idx)).getZnac();
  }

  public String getFieldName(int idx) {
    return ((Podatak) podaci.get(idx)).getLabel().getText();
  }

  public String getFieldValue(int idx) {
    return ((Podatak) podaci.get(idx)).getField().getText();
  }

  public String getFieldValueFormatted(int idx) {
    Podatak p = (Podatak) podaci.get(idx);
    if (p.isNav() && p.getFieldNaziv().getText().length() > 0)
      return p.getFieldNaziv().getText();
    else return p.getField().getText();
  }

  public void SetFokus() {
    ((Podatak) podaci.get(0)).getField().requestFocus();
  }

  public boolean Validacija() {
    Iterator it = podaci.iterator();
    Podatak p;
    while (it.hasNext()) {
      p = (Podatak) it.next();
      if (p.isObavezan() && vl.isEmpty(p.getField()))
        return false;
    }
    return true;
  }

  void jbInit() throws Exception {
    this.setLayout(xy);
    xy.setWidth(500);
    xy.setHeight(0);
  }*/
}

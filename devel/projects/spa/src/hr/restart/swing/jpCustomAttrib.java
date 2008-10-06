/****license*****************************************************************
**   file: jpCustomAttrib.java
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
package hr.restart.swing;

import hr.restart.baza.Condition;
import hr.restart.baza.KreirDrop;
import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class jpCustomAttrib extends JPanel {

  /* kolone i tablice */
  String attrTable;  /* Ime tablice znacajki */
  String vrsubTable;  /* Ime tablice vrsta subjekta (moze biti null) */
  String valueTable;  /* Ime tablice s vrijednostima za konkretne subjekte */
  String sifTable;  /* Ime tablice sifrarnika, moze biti null */
  String hintTable;  /* Ime tablice hintova, moze biti null */

  String attrKey;  /* Kolona sifre znacajke */
  String attrDesc;  /* Kolona opisa znacajke */
  String attrVrsub;   /* Kolona veze s vrstom subjekta (moze biti null) */
  String attrType;   /* Kolona tipa znacajke (S, I, D, 2, 3) */
  String attrReq;    /* Kolona oznake obaveznog unosa (D, N) */
  String attrSif;    /* Kolona oznake sifriranosti (D, N) */
  String attrDoh;    /* Kolona dohvata, moze biti null */

  /* Ako vrste subjekta postoje, moraju biti tipa Short! */
  String vrsubKey;   /* Kolona sifre vrste subjekta */
  String vrsubDesc;  /* Kolona opisa vrste subjekta */
  String vrsubPrip;   /* Kolona pripadnosti opcenitijoj vrsti subjekta */

  String valueSub;    /* Kolona subjekta u tablici vrijednosti */
  String valueVrsub;   /* Kolona vrste subjekta (moze null) */
  String valueAttr;    /* Kolona sifre znacajke u tablici */
  String valueVal;    /* Kolona vrijednosti znacajke za taj subjekt */

  /* Ako je sifTable null, onda je potrebno overridati metodu
  getSifrarnikDataSet(), koja bi po mogucnosti trebala vratiti
  raDataSet dohvata za odgovarajucu znacajku, te getSifrarnikVisibleCols(). */
  String sifVrsub; /* Kolona vrste subjekta */
  String sifAttr;   /* Kolona sifre znacajke */
  String sifValue;   /* Kolona vrijednosti sifre */
  String sifDesc;   /* Kolona opisa vrijednosti */

  String hintVrsub;   /* Kolona vrste subjekta (moze null) */
  String hintText;   /* Kolona teksta hinta */

  int labelWidth = 150, lw, tw;

  KreirDrop attrDm, vrsubDm, valueDm, sifDm, hintDm;

  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();
  lookupData ld = lookupData.getlookupData();

  XYLayout xy = new XYLayout();

  ArrayList podaci;
  String csubrn, vrsub;
  short cvrsubj;

  public jpCustomAttrib() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setTables(String vrsub, String attr, String value, String sif, String hint) {
    hintTable = hint;
    vrsubTable = vrsub;
    attrTable = attr;
    valueTable = value;
    sifTable = sif;
    if (vrsub != null) vrsubDm = KreirDrop.getModuleByName(vrsub);
    attrDm = KreirDrop.getModuleByName(attr);
    valueDm = KreirDrop.getModuleByName(value);
    if (sif != null) sifDm = KreirDrop.getModuleByName(sif);
    if (hint != null) hintDm = KreirDrop.getModuleByName(hint);
  }

  public void setTables(String vrsub, String attr, String value, String sif) {
    setTables(vrsub, attr, value, sif, null);
  }

  public void setTables(String attr, String value, String hint) {
    setTables(null, attr, value, null, hint);
  }

  public void setTables(String attr, String value) {
    setTables(null, attr, value, null, null);
  }

  public void setVrsubCols(String key, String desc, String prip) {
    vrsubKey = key;
    vrsubDesc = desc;
    vrsubPrip = prip;
    if (attrVrsub == null) attrVrsub = key;
    if (valueVrsub == null) valueVrsub = key;
    if (sifVrsub == null) sifVrsub = key;
    if (hintVrsub == null) hintVrsub = key;
  }

  public void setVrsubCols(String key, String desc) {
    setVrsubCols(key, desc, null);
  }

  public void setAttrCols(String key, String desc, String vrsub, String type, String req, String sif) {
    attrKey = key;
    attrDesc = desc;
    attrVrsub = vrsub;
    attrType = type;
    attrReq = req;
    attrSif = sif;
    attrDoh = null;
    if (valueAttr == null) valueAttr = attrKey;
    if (sifAttr == null) sifAttr = attrKey;
  }
  
  public void setAttrDohCol(String doh) {
    attrDoh = doh;
  }

  public void setAttrCols(String key, String desc, String type, String req, String sif) {
    setAttrCols(key, desc, vrsubKey, type, req, sif);
  }

  public void setValueCols(String sub, String vrsub, String attr, String val) {
    valueSub = sub;
    valueVrsub = vrsub;
    valueAttr = attr;
    valueVal = val;
    if (sifValue == null) sifValue = val;
  }

  public void setValueCols(String sub, String val) {
    setValueCols(sub, vrsubKey, attrKey, val);
  }

  public void setHintCols(String vrsub, String text) {
    hintVrsub = vrsub;
    hintText = text;
  }

  public void setHintText(String text) {
    setHintCols(vrsubKey, text);
  }

  public void setSifCols(String vrsub, String attr, String val, String desc) {
    sifVrsub = vrsub;
    sifAttr = attr;
    sifValue = val;
    sifDesc = desc;
  }

  public void setSifDesc(String desc) {
    setSifCols(vrsubKey, attrKey, valueVal, desc);
  }

  public void setLabelWidth(int width) {
    labelWidth = width;
  }

  public void setFields() {
    setFields((short) 0, true);
  }

  public void setFields(short _cvrs) {
    setFields(_cvrs, true);
  }

  private DataSet getAttributeSet(short _cvrs) {
    DataSet znac;
    if (vrsubTable == null) {
      znac = attrDm.getTempSet("1=1");
      znac.open();
    } else {
      DataSet vrsubDs = vrsubDm.getTempSet();
      boolean prip = ld.raLocate(vrsubDs, vrsubKey, String.valueOf(_cvrs));
      vrsubDs.open();
      vrsub = vrsubDs.getString(vrsubDesc);

      znac = attrDm.getTempSet("1=0");
      znac.open();

      while (prip) {
//        "SELECT * FROM RN_znacajke WHERE cvrsubj = "+cvrsubj+" ORDER BY cznac DESCENDING");
        DataSet ds = attrDm.getTempSet(Condition.equal(attrVrsub, String.valueOf(_cvrs)));
        ds.open();
        for (ds.first(); ds.inBounds(); ds.next()) {
          znac.insertRow(false);
          dM.copyColumns(ds, znac);
        }
        prip = vrsubPrip != null && (_cvrs != vrsubDs.getShort(vrsubPrip)) &&
               ld.raLocate(vrsubDs, vrsubKey, String.valueOf(
               _cvrs = vrsubDs.getShort(vrsubPrip)));
      }
      znac.setSort(new SortDescriptor(new String[] {attrKey}));
    }
    return znac;
  }

  private String getHintKey(short vrsub, String col) {
    return (vrsubTable == null) ? col : "V"+vrsub+"A"+col;
  }

  private String getHintKey(ReadRow znac) {
    return (vrsubTable == null) ? String.valueOf(znac.getShort(attrKey)) :
      "V"+znac.getShort(attrVrsub)+"A"+znac.getShort(attrKey);
  }

  private String getHintKey(ReadRow hint, String col) {
    return getHintKey(hint.getShort(hintVrsub), col);
  }

  private class RenderHint {
    int width, x;
    String joinString;
    int[] joinCols;
    boolean above, movedown, nolabel;
    public RenderHint() {
      movedown = true;
    }
    public RenderHint(int width) {
      this.width = width;
    }
    public RenderHint(String joinString, int[] joinCols) {
      this.joinString = joinString;
      this.joinCols = joinCols;
    }
    public void setAbove() {
      above = true;
    }
    public void setNoMove() {
      movedown = false;
    }
    public void setNoLabel() {
      nolabel = true;
    }

    public void setWidth(int width) {
      this.width = width;
    }
    public void setX(int x) {
      this.x = x;
    }
    public void setJoin(String joinString, int[] joinCols) {
      this.joinString = joinString;
      this.joinCols = joinCols;
    }
    public boolean isAbove() {
      return above;
    }
    public boolean isMove() {
      return movedown;
    }
    public boolean isLabel() {
      return !nolabel;
    }
    public int getWidth() {
      return width;
    }
    public int getX() {
      return x;
    }
    public String getJoinString() {
      return joinString;
    }
    public int[] getJoinCols() {
      return joinCols;
    }
    public String toString() {
      return "width="+width+";joinString="+joinString;
    }
  }

  private void addSimpleHint(HashMap map, DataSet ds, String col, String hint) {
    if (col.equalsIgnoreCase("text") || col.equalsIgnoreCase("label") || Aus.isDigit(col)) {
      col = getHintKey(ds, col);
      if (hint.indexOf('=') > 0) {
        String[] hp = new VarStr(hint).splitTrimmed('=');
        if (hp.length == 2) {
          if (hp[0].equalsIgnoreCase("width") && Aus.isDigit(hp[1])) {
            if (!map.containsKey(col)) map.put(col, new RenderHint(Aus.getNumber(hp[1])));
            else ((RenderHint) map.get(col)).setWidth(Aus.getNumber(hp[1]));
            return;
          }
        }
      }
    }
    System.err.println("invalid hint: "+hint);
  }

  private void addJoinHint(HashMap map, DataSet ds, String cols, String text, String wids) {
    if (cols.indexOf(',') >= 0 && wids.indexOf(',') >= 0) {
      String[] cl = new VarStr(cols).splitTrimmed(',');
      String[] wl = new VarStr(wids).splitTrimmed(',');
      if (cl.length == wl.length) {
        int[] cn = new int[cl.length];
        int[] wn = new int[wl.length];
        for (int i = 0; i < cn.length; i++)
          if (!Aus.isDigit(cl[i]) || !Aus.isDigit(wl[i])) {
            System.err.println("invalid hint: "+cols+"|"+text+"|"+wids);
            return;
          } else {
            cn[i] = Aus.getNumber(cl[i]);
            wn[i] = Aus.getNumber(wl[i]);
          }
        boolean nolabel;
        if (nolabel = text.startsWith("*")) text = text.substring(1);

        RenderHint rh;
        if (map.containsKey(getHintKey(ds, cl[0]))) {
          rh = (RenderHint) map.get(getHintKey(ds, cl[0]));
          rh.setJoin(text, cn);
        } else {
          rh = new RenderHint(text, cn);
          map.put(getHintKey(ds, cl[0]), rh);
        }
        rh.setWidth(wn[0]);
        rh.setAbove();
        rh.setNoMove();
        if (nolabel) rh.setNoLabel();
        for (int i = 1; i < cn.length; i++) {
          if (map.containsKey(getHintKey(ds, cl[i])))
            rh = (RenderHint) map.get(getHintKey(ds, cl[1]));
          else {
            rh = new RenderHint();
            map.put(getHintKey(ds, cl[i]), rh);
          }
          rh.setWidth(wn[i]);
          rh.setAbove();
          if (nolabel) rh.setNoLabel();
          if (i < cn.length - 1) rh.setNoMove();
        }
        return;
      }
    }
    System.err.println("invalid hint: "+cols+"|"+text+"|"+wids);
  }

  private void inflateHintMap(HashMap map, DataSet ds) {
    for (ds.open(), ds.first(); ds.inBounds(); ds.next()) {
      String[] hints = new VarStr(ds.getString(hintText)).splitTrimmed(';');
      for (int i = 0; i < hints.length; i++)
        if (hints[i].length() > 0 && hints[i].indexOf(':') >= 0) {
          String[] parts = new VarStr(hints[i]).splitTrimmed(':');
          if (parts.length == 2) addSimpleHint(map, ds, parts[0], parts[1]);
          else if (parts.length == 3) addJoinHint(map, ds, parts[0], parts[1], parts[2]);
          else System.err.println("invalid hint: "+hints[i]);
        }
    }
  }

  private HashMap getHints(DataSet znac) {
    HashMap hints = new HashMap();
    if (hintTable == null) return hints;

    short vrsub = -1;
    if (vrsubTable == null) inflateHintMap(hints, hintDm.getTempSet());
    else for (znac.first(); znac.inBounds(); znac.next())
      if (znac.getShort(attrVrsub) != vrsub) {
        vrsub = znac.getShort(attrVrsub);
        inflateHintMap(hints, hintDm.getTempSet(Condition.equal(hintVrsub, vrsub)));
      }
    return hints;
  }

  public void setFields(short _cvrs, boolean def) {
    DataSet znac;
    Podatak p;
    int y = 5;
    lw = labelWidth;
    tw = 310;

    cvrsubj = _cvrs;
    this.removeAll();

    csubrn = "";
    znac = getAttributeSet(_cvrs);
    if (znac.rowCount() > 0) {
      clearColumns();
      HashMap hints = getHints(znac);
      System.out.println(hints);
      if (hints.containsKey(getHintKey(_cvrs, "label")))
        lw = ((RenderHint) hints.get(getHintKey(_cvrs, "label"))).getWidth();
      if (hints.containsKey(getHintKey(_cvrs, "text")))
        tw = ((RenderHint) hints.get(getHintKey(_cvrs, "text"))).getWidth();
      podaci = new ArrayList();
      for (znac.first(); znac.inBounds(); znac.next()) {
        podaci.add(p = new Podatak(znac, def));
        boolean above = false, down = true, label = true;
        short cvrsz = (vrsubTable == null) ? _cvrs : znac.getShort(attrVrsub);
        int w = 100, wn = tw - w - 5, x = 0;
        RenderHint rh = (RenderHint) hints.get(getHintKey(znac));
        if (rh != null) {
          label = rh.isLabel();
          above = rh.isAbove();
          down = rh.isMove();
          if (rh.getJoinString() != null) {
            if (label) y += 20;
            this.add(new JLabel(rh.getJoinString()), new XYConstraints(15, y, lw-20, -1));
            x = rh.getWidth() + 5;
            for (int i = 1; i < rh.getJoinCols().length; i++) {
              RenderHint rhj = (RenderHint) hints.get(getHintKey(cvrsz,
                String.valueOf(rh.getJoinCols()[i])));
              if (rhj != null) {
                rhj.setX(x);
                x += rhj.getWidth() + 5;
              }
            }
          }
          if (rh.getWidth() > 0) w = rh.getWidth();
          wn = tw - w - 5;
          x = rh.getX();
        }
        if (above) {
          p.getLabel().setHorizontalAlignment(JLabel.CENTER);
          if (label) this.add(p.getLabel(), new XYConstraints(lw + x, y - 18, w, -1));
        } else this.add(p.getLabel(), new XYConstraints(15, y, lw-20, -1));
        if (p.tip.equals("NAV")) {
          this.add(p.getField(), new XYConstraints(lw, y, w, -1));
          this.add(p.getFieldNaziv(), new XYConstraints(lw + w + 5, y, wn, -1));
        } else if (znac.getString("ZNACTIP").equals("S") && rh == null)
          this.add(p.getField(), new XYConstraints(lw, y, w + wn + 5, -1));
        else
          this.add(p.getField(), new XYConstraints(lw + x, y, w, -1));
        if (p.getButton() != null)
          this.add(p.getButton(), new XYConstraints(lw + w + wn + 10, y, 21, 21));
        if (down) y += 25;
      }
      setColumns(podaci.toArray());
/*      Podatak.createRow();
      System.out.println(Podatak.ds); */
      xy.setWidth(lw + tw + 40);
      xy.setHeight(y + 5);
    } else xy.setHeight(0);
  }

  public int getLayWidth() {
    return xy.getWidth();
  }

  public int getLayHeight() {
    return xy.getHeight();
  }

  public int getLabelWidth() {
    return lw;
  }

  public int getTextWidth() {
    return tw;
  }

  public void clearValues() {
    for (Iterator it = podaci.iterator(); it.hasNext();
        ((Podatak) it.next()).setStringValue(""));
  }
  public void setValues(String newsub) {
    setValues(newsub, Condition.none);
  }

  public void setValues(String newsub, Condition additionalKey) {
    if (newsub.equals(csubrn)) return;
    csubrn = newsub;
    if (csubrn.equals("")) return;
    tds.first();
    clearValues();
//    sys.prn(Podatak.ds);
    DataSet vri = valueDm.getTempSet(Condition.equal(valueSub, csubrn).and(additionalKey));
    vri.open();
    System.out.println("jpCustomAttrib, setValues: csubrn="+csubrn+
        " cond="+additionalKey + ": " + vri.rowCount());
    System.out.println("Field count: " + getFieldCount() + "  tds count: " + tds.rowCount());

    for (vri.first(); vri.inBounds(); vri.next()) {
      short vs = vrsubTable != null ? vri.getShort(valueVrsub) : 0;
      Iterator it = podaci.iterator();
      Podatak p = null;
      while (it.hasNext()) {
        p = (Podatak) it.next();
        if (p.col.getColumnName().equals(vs + "POD" + vri.getShort(valueAttr)))
          break;
        p = null;
      }
      if (p != null) {
        p.setStringValue(vri.getString(valueVal));
      } else {
        System.out.println("nepostoje\u0107a kolona " + vs + "POD" + vri.getShort(valueAttr));
        System.out.println("subjekt "+csubrn);
      }
    }
  }
  
  public QueryDataSet updateValues() {
    return updateValues(null, null);
  }

  public QueryDataSet updateValues(String addCol, String addVal) {
    Iterator it = podaci.iterator();
    Podatak p;
    boolean fnd;
    Condition additionalKey = addCol == null ? Condition.none : Condition.equal(addCol, addVal);
//    dm.getRNZnacSub().close();
//    dm.getRNZnacSub().closeStatement();
//    dm.getRNZnacSub().setQuery(new QueryDescriptor(dm.getDatabase1(),
//      dm.getRN_znacsub().getOriginalQueryString() + " WHERE csubrn = '"+csubrn+"'", null, true, Load.ALL));
    QueryDataSet vri = valueDm.getTempSet(Condition.equal(valueSub, csubrn).and(additionalKey));
    vri.open();
    System.out.println("jpCustomAttrib, updateValues: csubrn="+csubrn+" cond="+additionalKey);
    
    while (it.hasNext()) {
      p = (Podatak) it.next();
      if (vrsubTable == null)
        fnd = ld.raLocate(vri, valueAttr, String.valueOf(p.getZnac()));
      else fnd = ld.raLocate(vri, new String[] {valueVrsub, valueAttr},
        new String[] {String.valueOf(p.getVrsubj()), String.valueOf(p.getZnac())});
      if (!fnd) {
        vri.insertRow(false);
        vri.setString(valueSub, csubrn);
        vri.setShort(valueAttr, p.getZnac());
        if (addCol != null) vri.setString(addCol, addVal);
        if (vrsubTable != null) vri.setShort(valueVrsub, p.getVrsubj());
      }
      vri.setString(valueVal, p.getStringValue());
    }
//    dm.getRNZnacSub().saveChanges();
//    dm.getRN_znacsub().refresh();
    return vri;
  }

  public void cancel() {
    try {
      tds.cancel();
    } catch (Exception e) {
      // nothing
    }
  }

  public void insert() {
    createRow();
    Podatak p = null;
    Iterator it = podaci.iterator();
    while (it.hasNext()) {
      p = (Podatak) it.next();
      if (p.isNav()) p.getFieldNaziv().setText("");
    }
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
  
  public void setFieldValue(int idx, String text) {
    ((Podatak) podaci.get(idx)).setStringValue(text);
  }

  public String getFieldValueFormatted(int idx) {
    Podatak p = (Podatak) podaci.get(idx);
    if (p.isNav() && p.getFieldNaziv().getText().length() > 0)
      return p.getFieldNaziv().getText();
    return p.getField().getText();
  }

  public void SetFokus() {
    ((Podatak) podaci.get(0)).getField().requestFocusLater();
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
    xy.setWidth(labelWidth + 350);
    xy.setHeight(0);
  }

  TableDataSet tds = new TableDataSet();

  public void clearColumns() {
    tds = new TableDataSet();
    tds.setTableName("ATRIBUTI");
  }

  public void createRow() {
    tds.insertRow(false);
  }

  public void setColumns(Object[] pod) {
    Column[] cols = new Column[pod.length];
    for (int i = 0; i < pod.length; i++)
      cols[i] = ((Podatak) pod[i]).col;
    tds.close();
    tds.setColumns(cols);
    tds.open();
    tds.insertRow(false);
    tds.post();
  }

  class Podatak {

//  static sysoutTEST sys = new sysoutTEST(false);
    Valid vl = Valid.getValid();

    String tip;
    Column col;
    JraTextField jra;
    JlrNavField jlr, jlrn;
    JraButton jb;
    JLabel l;

    short cvrsubj, cznac;
    boolean obavez;

    public Podatak(DataSet znset, boolean def) {
      boolean doh = attrDoh != null && znset.getString(attrDoh).trim().length() > 0;
      VarStr dohText = doh ? new VarStr(znset.getString(attrDoh)).trim() : null; 
      tip = znset.getString(attrSif).equals("D") || doh ? "NAV" : znset.getString(attrType);
      
      cvrsubj = vrsubTable != null ? znset.getShort(attrVrsub) : 0;
      cznac = znset.getShort(attrKey);
      String opis = znset.getString(attrDesc);
      obavez = znset.getString(attrReq).equals("D");
      l = new JLabel();
      l.setText(opis);
      col = new Column();
      col.setColumnName(cvrsubj + "POD" + cznac);
      col.setTableName("ATRIBUTI");
      col.setCaption(opis);
      col.setPrecision(50);
      //col.setScale(2);
      if (tip.equals("NAV")) {
        if (!doh) col.setDataType(Variant.STRING);
        
        jb = new JraButton();
        jb.setText("...");
        jlr = new JlrNavField();
        jlrn = new JlrNavField();
        jlr.setColumnName(col.getColumnName());
        jlr.setDataSet(tds);
        jlr.setSearchMode(0);
        if (!doh) {
          jlr.setRaDataSet(getSifrarnikDataSet(cvrsubj, cznac));
          jlr.getRaDataSet().open();
          jlr.setColNames(new String[] {getSifrarnikDesc(cvrsubj, cznac)});
          jlr.setTextFields(new JlrNavField[] {jlrn});
          jlr.setNavColumnName(getSifrarnikValue(cvrsubj, cznac));
          jlr.setVisCols(getSifrarnikVisibleCols(cvrsubj, cznac));
          jlrn.setColumnName(getSifrarnikDesc(cvrsubj, cznac));
        }
        jlr.setNavButton(jb);

        jlrn.setNavProperties(jlr);
        jlrn.setSearchMode(1);
        
        if (doh) {
          VarStr cols = null;
          int colon = dohText.indexOf(':');
          if (colon >= 0) {
            cols = dohText.copy(colon + 1, dohText.length());
            dohText.truncate(colon);
          }
          
          String[] parts = dohText.splitTrimmed('+');
          DataSet ds = null;
          try {
            java.lang.reflect.Method m = dM.class.getMethod(parts[2], null);
            ds = (DataSet) m.invoke(dM.getDataModule(), null);
          } catch (Exception e) {}
          
          col.setDataType(ds.getColumn(parts[0]).getDataType());
          
          jlr.setRaDataSet(ds);
          jlr.getRaDataSet().open();
          jlr.setNavColumnName(parts[0]);
          jlr.setColNames(new String[] {parts[1]});
          jlr.setTextFields(new JlrNavField[] {jlrn});
          jlrn.setColumnName(parts[1]);
          
          if (cols == null || cols.trim().length() == 0) 
            jlr.setVisCols(new int[] {0, 1});
          else {
            String[] ci = cols.splitTrimmed(',');
            int[] cint = new int[ci.length];
            for (int i = 0; i < ci.length; i++)
              cint[i] = Aus.getNumber(ci[i]);
            jlr.setVisCols(cint);
          }
        }

      } else {
        jra = new JraTextField();
        if (tip.equals("S"))
          col.setDataType(Variant.STRING);
        else if (tip.equals("I")) {
          col.setDataType(Variant.INT);
          if (def) col.setDefault("0");
          new raTextMask(jra, 10, false, raTextMask.NUMERIC);
        } else if (tip.equals("D")) {
          col.setDataType(Variant.TIMESTAMP);
          col.setDisplayMask("dd-MM-yyyy");
          new hr.restart.swing.raDatePopup(jra);
          new raDateMask(jra);
        } else if (tip.equals("2")) {
          col.setDataType(Variant.BIGDECIMAL);
          if (def) col.setDefault("0");
          col.setScale(2);
          col.setDisplayMask("###,###,##0.00");
          Aus.installNumberMask(jra, 2);          
        } else if (tip.equals("3")) {
          col.setDataType(Variant.BIGDECIMAL);
          if (def) col.setDefault("0");
          col.setScale(3);
          col.setDisplayMask("###,###,##0.000");
          Aus.installNumberMask(jra, 3);          
        }
//      jra = new JraTextField();
        jra.setDataSet(tds);
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
        if (col.getDataType() == Variant.STRING)
        //if (tip.equals("NAV") || tip.equals("S"))
          tds.setString(col.getColumnName(), value);
        else if (value == null || value.length() == 0)
          tds.setAssignedNull(col.getColumnName());
        else if (col.getDataType() == Variant.BIGDECIMAL)
        //else if (tip.equals("2") || tip.equals("3"))
          tds.setBigDecimal(col.getColumnName(), new BigDecimal(value));
        else if (col.getDataType() == Variant.INT)
        //else if (tip.equals("I"))
          tds.setInt(col.getColumnName(), Integer.parseInt(value));
        else tds.setTimestamp(col.getColumnName(), Timestamp.valueOf(value));
        if (isNav()) jlr.forceFocLost();
      } catch (Exception e) {
        System.out.println(e);
      }
    }

    public String getStringValue() {
      if (col.getDataType() == Variant.STRING)
      //if (tip.equals("NAV") || tip.equals("S"))
        return tds.getString(col.getColumnName());
      else if (tds.isNull(col.getColumnName())) return "";
      else if (col.getDataType() == Variant.BIGDECIMAL)
      //else if (tip.equals("2") || tip.equals("3"))
        return tds.getBigDecimal(col.getColumnName()).toString();
      else if (col.getDataType() == Variant.INT)
      //else if (tip.equals("I"))
        return "" + tds.getInt(col.getColumnName());
      else return tds.getTimestamp(col.getColumnName()).toString();
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

  public DataSet getHorizontalSet(String table) {
    return getHorizontalSet(table, null, (String) null, (short) 0);
  }

  public DataSet getHorizontalSet(String table, short _cvrs) {
    return getHorizontalSet(table, null, (String) null, _cvrs);
  }

  public DataSet getHorizontalSet(String table, Condition where) {
    return getHorizontalSet(table, null, where.toString(), (short) 0);
  }

  public DataSet getHorizontalSet(String table, Condition where, short _cvrs) {
    return getHorizontalSet(table, null, where.toString(), _cvrs);
  }

  public DataSet getHorizontalSet(String table, String where) {
    return getHorizontalSet(table, null, where, (short) 0);
  }

  public DataSet getHorizontalSet(String table, String where, short _cvrs) {
    return getHorizontalSet(table, null, where, _cvrs);
  }

  public DataSet getHorizontalSet(String table, String table2, Condition where) {
    return getHorizontalSet(table, table2, where.toString(), (short) 0);
  }

  public DataSet getHorizontalSet(String table, String table2, String where) {
    return getHorizontalSet(table, table2, where.toString(), (short ) 0);
  }

  public DataSet getHorizontalSet(String table, String table2, Condition where, short _cvrs) {
    return getHorizontalSet(table, table2, where.toString(), _cvrs);
  }

  public DataSet getHorizontalSet(String table, String table2, String where, short _cvrs) {
    DataSet znac = getAttributeSet(_cvrs);
    String mainq = "SELECT * FROM "+table+(table2 == null ? "" : ","+table2)+
                   (where == null ? "" : " WHERE "+where);
    vl.execSQL(mainq);
    QueryDataSet main = vl.getDataAndClear();
    main.open();

    KreirDrop t1 = KreirDrop.getModuleByName(table);
    KreirDrop t2 = table2 == null ? null : KreirDrop.getModuleByName(table2);

    Column[] maincols = main.cloneColumns();
    Column[] cols = new Column[maincols.length + znac.getRowCount()];
    for (int i = 0; i < maincols.length; i++) {
      cols[i] = maincols[i];
      if (t1.getColumn(maincols[i].getColumnName()) != null)
        cols[i] = (Column) t1.getColumn(maincols[i].getColumnName()).cloneColumn();
      else if (t2 != null && t2.getColumn(maincols[i].getColumnName()) != null)
        cols[i] = (Column) t2.getColumn(maincols[i].getColumnName()).cloneColumn();
      else cols[i].setVisible(0);
    }
    znac.last();
    for (int i = 0; i < znac.getRowCount(); i++) {
      Column col = new Column();
      String tip = znac.getString(attrType);
      col.setCaption(znac.getString(attrDesc));
      col.setColumnName("ATTR" + znac.getShort("CZNAC"));
      col.setPrecision(50);
      col.setRowId(false);
      if (tip.equals("S")) {
        col.setDataType(Variant.STRING);
        col.setWidth(15);
      } else if (tip.equals("I")) {
        col.setDataType(Variant.INT);
        col.setWidth(6);
      } else if (tip.equals("2")) {
        col.setDataType(Variant.BIGDECIMAL);
        col.setScale(2);
        col.setDisplayMask("###,###,##0.00");
        col.setWidth(9);
      } else if (tip.equals("3")) {
        col.setDataType(Variant.BIGDECIMAL);
        col.setScale(3);
        col.setDisplayMask("###,###,##0.000");
        col.setWidth(9);
      } else {
        col.setDataType(Variant.TIMESTAMP);
        col.setDisplayMask("dd-MM-yyyy");
        col.setWidth(9);
      }
      cols[maincols.length + i] = col;
      znac.prior();
    }

    QueryDataSet all = new QueryDataSet();
    all.setColumns(cols);
    all.open();

    String valueq = "SELECT "+table+"."+valueSub+","+valueTable+"."+valueAttr+","+
                    valueTable+"."+valueVal+" FROM "+valueTable+","+table+
       (table2 == null ? "" : ","+table2)+" WHERE "+(where == null ? "" : where+" AND ")+
       (vrsubTable == null ? "" : valueTable+"."+valueVrsub+"="+_cvrs+" AND ")+
       table+"."+valueSub+"="+valueTable+"."+valueSub+" ORDER BY "+valueSub;

    System.out.println(valueq);
    vl.execSQL(valueq);
    QueryDataSet values = vl.getDataAndClear();
    values.open();

    String lasts = "";
    for (values.first(); values.inBounds(); values.next()) {
      if (!lasts.equals(values.getString(valueSub))) {
        lasts = values.getString(valueSub);
        ld.raLocate(main, valueSub, lasts);
        all.insertRow(false);
        dM.copyColumns(main, all);
      }
      String cname = "ATTR" + values.getShort(valueAttr);
      int ctip = all.getColumn(cname).getDataType();
      String val = values.getString(valueVal);
      if (ctip == Variant.STRING)
        all.setString(cname, val);
      else if (val == null || val.length() == 0)
        all.setAssignedNull(cname);
      else if (ctip == Variant.INT)
        all.setInt(cname, Integer.parseInt(val));
      else if (ctip == Variant.BIGDECIMAL)
        all.setBigDecimal(cname, new BigDecimal(val));
      else if (ctip == Variant.TIMESTAMP)
        all.setTimestamp(cname, Timestamp.valueOf(val));
    }
    return all;
  }

  public DataSet getSifrarnikDataSet(short cvrsubj, short cznac) {
    return KreirDrop.getModuleByName(sifTable).getFilteredDataSet(cvrsubj == 0 ?
        Condition.equal(sifAttr, cznac) : Condition.equal(sifVrsub, cvrsubj).
        and(Condition.equal(sifAttr, cznac)));
  }

  public int[] getSifrarnikVisibleCols(short cvrsubj, short cznac) {
    return new int[] {2, 3};
  }

  public String getSifrarnikValue(short cvrsubj, short cznac) {
    return sifValue;
  }

  public String getSifrarnikDesc(short cvrsubj, short cznac) {
    return sifDesc;
  }
}

/****license*****************************************************************
**   file: frmParam.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Parametri;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraLabel;
import hr.restart.swing.JraTextField;
import hr.restart.util.IntParam;
import hr.restart.util.JlrNavField;
import hr.restart.util.ParamHandler;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;

import java.awt.Font;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
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

public class frmParam extends raMatPodaci {
//sysoutTEST ST = new sysoutTEST(false);
  //static hr.restart.util.lookupData LD = hr.restart.util.lookupData.getlookupData();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  private static frmParam fparam;
  private String APPNAME="";
  private String naslov = "";
  JPanel jp = new JPanel();
  XYLayout xYLay = new XYLayout();
  JLabel jlAPP = new JLabel();
  JLabel jlAPP2 = new JLabel();
  JlrNavField jlrAPP = new JlrNavField();
  Valid Vl = Valid.getValid();
  dM dm;
  JlrNavField jlrOPIS = new JlrNavField();
  JraButton jbgetAPL = new JraButton();
  JLabel jlPARAM = new JLabel();
  JraTextField jrPARAM = new JraTextField();
  JLabel jlOPIS = new JLabel();
  JraTextField jrOPIS = new JraTextField();
  JLabel jlVRIJEDNOST = new JLabel();
  JraTextField jrVRIJEDNOST = new JraTextField();
//  JraButton jbpreSel = new JraButton();
  public PreSelect psFrmParam = new PreSelect() {
    public void SetFokus() {
      jlrAPP.requestFocus();
    }
    public boolean Validacija() {
      return !Vl.isEmpty(jlrAPP);
    }
  };
  JPanel jpsel = new JPanel();
  XYLayout xYLaySel = new XYLayout();
  JraLabel jralAPP = new JraLabel();
  public static final int LOCAL = 1;
  public static final int GLOBAL = 2;
  private int pmode;
//  private static boolean needRefresh = true;

  public frmParam() {
    this(LOCAL);
  }

  public frmParam(int mod) {
    pmode = mod;
//    super(2);
    try {
      if (pmode == LOCAL) fparam = this;
      jbInit();

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public frmParam(String appname) {
    this();
    APPNAME = appname;
  }

  public static frmParam getFrmParam() {
    if (fparam==null) fparam=new frmParam();
    return fparam;
  }

  public void beforeShow() {
    if (naslov.equals("")) naslov = this.getTitle();
    this.setTitle(naslov + " - " + jlrOPIS.getText());
//    needRefresh = true;
  }

  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    this.setRaDetailPanel(jp);
    if (pmode == LOCAL)
      this.setRaQueryDataSet(dm.getLocParametri());
    else this.setRaQueryDataSet(dm.getGlobParametri());
    this.setVisibleCols(new int[] {1,2});

    jlAPP.setText("Aplikacija");
    jlAPP2.setText(jlAPP.getText());
    jp.setLayout(xYLay);
    jlrAPP.setColumnName("APP");
    jlrAPP.setDataSet(this.getRaQueryDataSet());
    jlrAPP.setColNames(new String[] {"OPIS"});
    jlrAPP.setVisCols(new int[] {0,3});
    jlrAPP.setTextFields(new javax.swing.text.JTextComponent[] {jlrOPIS});
    jlrAPP.setRaDataSet(dm.getAplikacija());
    jlrAPP.setNavButton(jbgetAPL);
    jlrOPIS.setColumnName("OPIS");
    jlrOPIS.setSearchMode(1);
    jlrOPIS.setNavProperties(jlrAPP);
    jbgetAPL.setText("...");
    jlPARAM.setText("Naziv parametra");
    jrPARAM.setColumnName("PARAM");
    jrPARAM.setDataSet(this.getRaQueryDataSet());
    jlOPIS.setText("Opis parametra");
    jrOPIS.setColumnName("OPISPAR");
    jrOPIS.setDataSet(this.getRaQueryDataSet());
    jlVRIJEDNOST.setText("Vrijednost parametra");
    jrVRIJEDNOST.setColumnName("VRIJEDNOST");
    jrVRIJEDNOST.setDataSet(this.getRaQueryDataSet());
//    jbpreSel.setText("Aplikacija");
//    jbpreSel.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        jbpreSel_actionPerformed();
//      }
//    });
    jpsel.setLayout(xYLaySel);
    xYLaySel.setWidth(525);
    xYLaySel.setHeight(63);
    jralAPP.setColumnName("APP");
    jralAPP.setDataSet(this.getRaQueryDataSet());
    jralAPP.setFont(jralAPP.getFont().deriveFont(Font.BOLD));
    jpsel.add(jlAPP, new XYConstraints(15, 20, -1, -1));
    jpsel.add(jlrAPP, new XYConstraints(150, 20, 75, -1));
    jpsel.add(jlrOPIS, new XYConstraints(230, 20, 250, -1));
    jpsel.add(jbgetAPL, new XYConstraints(485, 20, 21, 21));
    jp.add(jlAPP2, new XYConstraints(15, 20, -1, -1));
    jp.add(jlPARAM, new XYConstraints(15, 45, -1, -1));
    jp.add(jrPARAM, new XYConstraints(150, 45, 200, -1));
    jp.add(jlOPIS, new XYConstraints(15, 70, -1, -1));
    jp.add(jrOPIS, new XYConstraints(150, 70, 360, -1));
    jp.add(jlVRIJEDNOST, new XYConstraints(15, 95, -1, -1));
    jp.add(jrVRIJEDNOST, new XYConstraints(150, 95, 200, -1));
    jp.add(jralAPP, new XYConstraints(150, 20, -1, -1));

    psFrmParam.setSelDataSet(this.getRaQueryDataSet());
    psFrmParam.setSQLFilter(false);
    psFrmParam.setSelPanel(jpsel);
//    this.AddButton(jbpreSel,3,false);
    this.addOption(new raNavAction("Aplikacija") {
       public void actionPerformed(java.awt.event.ActionEvent ev) {
          jbpreSel_actionPerformed();
       }
    }, 3);
    xYLay.setWidth(561);
    xYLay.setHeight(188);
    this.disableAdd();
  }

  String oldvalue, newvalue, key;

  public void EntryPoint(char mode) {
    if (mode == 'I') {
      rcc.setLabelLaF(jrPARAM, false);
      rcc.setLabelLaF(jrOPIS, false);
      if (pmode == LOCAL) {
        newvalue = oldvalue = this.getRaQueryDataSet().getString("VRIJEDNOST");
        key = this.getRaQueryDataSet().getString("APP")+"."+this.getRaQueryDataSet().getString("PARAM");
        newvalue = IntParam.getTag(key);
        if (newvalue.equals("")) newvalue = oldvalue;
        this.getRaQueryDataSet().setString("VRIJEDNOST", newvalue);
      }
    }
  }

  public void SetFokus(char mode) {
    if (mode=='N') {
/*      psFrmParam.copySelValues();
      jrPARAM.setEnabled(true);
      jrPARAM.requestFocus(); */
      System.out.println("NEMOGU\u0106E!!");
    } else if (mode=='I') {
      jrVRIJEDNOST.requestFocus();
    } else if (mode == 'B' && pmode == LOCAL) {
      key = this.getRaQueryDataSet().getString("APP")+"."+this.getRaQueryDataSet().getString("PARAM");
      newvalue = IntParam.getTag(key);
      if (!newvalue.equals("")) jrVRIJEDNOST.setText(newvalue);
    }
  }
  public boolean DeleteCheck() {
    JOptionPane.showMessageDialog(null, "Nije mogu\u0107e brisati parametre!", "Greška", JOptionPane.ERROR_MESSAGE);
    return false;
  }
  public boolean Validacija(char mode) {
/*    if (mode=='N') {
      if (Valid.getValid().notUnique(new javax.swing.text.JTextComponent[] {jrPARAM,jlrAPP})) return false;
    } */
    if (pmode == LOCAL) {
      IntParam.setTag(key, this.getRaQueryDataSet().getString("VRIJEDNOST"));
      this.getRaQueryDataSet().setString("VRIJEDNOST", oldvalue);
    };
    return true;
  }
  void jbpreSel_actionPerformed() {
    psFrmParam.showPreselect(this,"Aplikacija");
  }
  public static String getParam(String sApl, String sParam) {
    return getParam(sApl, sParam, null, null, false);
  }

  public static String getParam(String sApl, String sParam, String defValue) {
    return getParam(sApl, sParam, defValue, "Oops, nazvati REST-ART za zna\u010Denje", false);
  }

  public static String getParam(String sApl, String sParam, String defValue, String defOpis) {
    return getParam(sApl, sParam, defValue, defOpis, false);
  }
  
  /**
   * Postavlja parametar sParam u aplikaciji sApl na vrijednost defValue ako i samo ako je taj 
   * parametar vec prijavljen. Ako je parametar lokalni azurira se restart.properties, u protivnom
   * tablica Parametri.  
   * @param sApl
   * @param sParam
   * @param defValue
   * @return false ako parametar ne postoji u tablici Parametri
   */
  public static boolean setParam(String sApl, String sParam, String defValue) {
    QueryDataSet parqds = Parametri.getDataModule().getFilteredDataSet(Condition.equal("APP",sApl).and(Condition.equal("PARAM", sParam)));
    parqds.open();
    if (parqds.getRowCount() == 0) return false;
    if (parqds.getString("SISTEMSKI").equals("L")) {
      String _key = sApl+"."+sParam;
      IntParam.setTag(_key, defValue);
    } else {
      parqds.setString("VRIJEDNOST", defValue);
      parqds.saveChanges();
    }
    return true;
  }
/**
 * Vraca u string vrijednost parametra za zadanu aplikaciju i naziv parametra
 */
  public synchronized static String getParam(String sApl, String sParam, String defValue, String defOpis, boolean locpar) {
    if (sApl==null) return null;
    if (sParam==null) return null;
    if (sApl.equals("")) return null;
    if (sParam.equals("")) return null;
    try {
      dM dm = dM.getDataModule();
      if (grabber != null) grab(sApl, sParam, defValue, defOpis, locpar);
      
      DataRow param = lookupData.getlookupData().raLookup(dm.getParametri(),
                          new String[] {"APP","PARAM"},new String[] {sApl,sParam});
      if (param == null) {
        if (defValue != null) {
          try {
            DataSet ds = dm.getParametri();
            ds.insertRow(false);
            ds.setString("APP", sApl);
            ds.setString("PARAM", sParam);
            ds.setString("OPISPAR", defOpis);
            ds.setString("VRIJEDNOST", defValue);
            if (locpar) ds.setString("SISTEMSKI", "L");
            ds.saveChanges();
            return defValue;
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        return null;
      }
      if (param.getString("SISTEMSKI").equals("L")) {
        String local = IntParam.getTag(sApl+"."+sParam);
        if (!local.equals("")) return local;
      }
      return param.getString("VRIJEDNOST");
    } catch (Exception e) {
      e.printStackTrace();
      return defValue;
    }
  }
  
  // trik za punjenje tablice parametara :) - pozvati metodu grab() i onda raditi u programu...
  // ... a on æe zgrabati sve parametre na koje naiðe i trpati ih sortirane u clipboard, koji se onda moze pastat tamo u ParamHandler
  public static void grab() {
    grabber = new TreeSet();
    area = new JTextArea();
    ParamHandler.init();
  }
  
  static void grab(String sApl, String sParam, String defValue, String defOpis, boolean locpar) {
    if (ParamHandler.inst.defined(sApl + "." + sParam)) return;
    
    char s = '\"';
    
    String line = "{" + s + sApl + "." + sParam + s + ", " + 
                s + (defValue == null ? "" : defValue) + s + ", " +
                s + (defOpis == null ? "" : defOpis) + s + ", " +
                (locpar ? "LOCAL" : "GLOBAL") + ", QUICK, SPEC, " + s + s + "},\n";
    
    if (!grabber.add(line)) return;
    
    area.selectAll();
    area.replaceSelection("");
    for (Iterator i = grabber.iterator(); i.hasNext(); )
      area.append((String) i.next());
    area.selectAll();
    area.cut();
  }

  private static JTextArea area = null;
  private static Set grabber = null; 
}
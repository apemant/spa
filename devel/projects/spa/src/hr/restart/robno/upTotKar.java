/****license*****************************************************************
**   file: upTotKar.java
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

import hr.restart.baza.dM;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitFat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title: Robno poslovanje Description: Copyright: Copyright (c) 2000 Company:
 * REST-ART
 * 
 * @author REST-ART development team
 * @version 1.0
 */

public class upTotKar extends raUpitFat {
  _Main main;
  raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");
  String cart;
  lookupData ld = lookupData.getlookupData();
  Valid vl = hr.restart.util.Valid.getValid();
  QueryDataSet vNull, stanje;
  TableDataSet tds = new TableDataSet();
  java.sql.Date dateZ = null;
  java.sql.Date dateP = null;
  Column col1 = new Column();
  Column col2 = new Column();
  Column colKDon = new Column();
  Column colKUl = new Column();
  Column colKIz = new Column();
  Column colZC = new Column();
  Column colZDon = new Column();
  Column colZad = new Column();
  Column colRaz = new Column();
  Column colSKL = new Column();
  Column colNSKL = new Column();
  Column colPD = new Column();
  Column colZD = new Column();
  boolean prvi = true;
  static String[] datum;
  String vrzal;
  XYLayout xYLayout2 = new XYLayout();
  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfZavDatum = new JraTextField();

  rapancskl rpcskl = new rapancskl() {
    public void findFocusAfter() {
      rpcart.setCskl(rpcskl.getCSKL());
      if (!rpcskl.jrfCSKL.getText().equals("")) {
        rpcart.setDefParam();
        jtfPocDatum.requestFocus();
      }
    }

  };

  rapancart rpcart = new rapancart() {
    public void nextTofocus() {
      getOKPanel().jPrekid.requestFocus();
    }
  };

  BorderLayout borderLayout1 = new BorderLayout();
  JLabel jLabel1 = new JLabel();
  JPanel jPanel3 = new JPanel();
  JPanel jp = new JPanel();
  dM dm = dM.getDataModule();
  static upTotKar upk;

  JraTextField jtfPocArtikl = new JraTextField();
  JraTextField jtfZavArtikl = new JraTextField();
  JLabel jLabel2 = new JLabel();
  JraButton jbFilter = new JraButton();
  JRadioButton jrbTotali = new JRadioButton();
  JRadioButton jrbSveKar = new JRadioButton();

  public upTotKar() {
    try {
      jbInit();
      upk = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static upTotKar getupTotKar() {
    if (upk == null) {
      upk = new upTotKar();
    }
    return upk;
  }

  boolean podgrupe = false;

  public boolean Validacija() {
    if (vratiGreske())
      return false;
    if (!jtfPocArtikl.getText().equals("") && !validateTF(jtfPocArtikl.getText())) {
      jtfPocArtikl.requestFocus();
      JOptionPane.showConfirmDialog(this.jp, "Pogrešan unos artikla !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

      return false;
    }
    if (!jtfZavArtikl.getText().equals("") && !validateTF(jtfZavArtikl.getText())) {
      jtfZavArtikl.requestFocus();
      JOptionPane.showConfirmDialog(this.jp, "Pogrešan unos artikla !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (!rpcart.getCGRART().equals("") && (rpcart.getCART().equals("") && rpcart.getCART1().equals(""))) {
      int grupe = JOptionPane.showConfirmDialog(this.jp, "Ukljuèiti i podgrupe?", "Grupe artikala", JOptionPane.YES_NO_OPTION);
      if (grupe == JOptionPane.CANCEL_OPTION)
        return false;
      if (grupe == JOptionPane.NO_OPTION) {
        podgrupe = false;
      } else {
        podgrupe = true;
      }
    }
    return true;
  }

  public void okPress() {
    this.killAllReports();

    cart = rpcart.getCART();
    dateP = new java.sql.Date(this.tds.getTimestamp("pocDatum").getTime());
    dateZ = new java.sql.Date(this.tds.getTimestamp("zavDatum").getTime());

    createDataSets();
    setSifraVisible();
    setTotaliVis();

    vl.RezSet.setSort(new SortDescriptor(new String[]{"CART"}));
    vNull.setSort(new SortDescriptor(new String[]{"CART"}));
    openScratchDataSet(vl.RezSet);
    openScratchDataSet(vNull);

    //*** def kumulativa
    calculate();
//    int sir1 = vNull.getColumn("JM").getWidth();
    vNull.getColumn("NAZART").setWidth(35);
    vNull.getColumn("JM").setWidth(5);
    vNull.getColumn("ZC").setWidth(8);
    
//    System.out.println("NAZART - " + vNull.getColumn("NAZART").getWidth());
//    System.out.println("JM     - " + vNull.getColumn("JM").getWidth());
//    System.out.println("KOLUL  - " + vNull.getColumn("KOLUL").getWidth());
//    System.out.println("KOLIZ  - " + vNull.getColumn("KOLIZ").getWidth());
//    System.out.println("ZC     - " + vNull.getColumn("ZC").getWidth());
//    System.out.println("KOLZAD - " + vNull.getColumn("KOLZAD").getWidth());
//    System.out.println("KOLRAZ - " + vNull.getColumn("KOLRAZ").getWidth());
//    System.out.println("SKOL   - " + vNull.getColumn("SKOL").getWidth());
//    System.out.println("SIZN   - " + vNull.getColumn("SIZN").getWidth());
    
    this.getJPTV().getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    setDataSetAndSums(vNull, new String[]{"KOLZAD", "KOLRAZ", "SIZN"});

  }

  private boolean vratiGreske() {
    if (rpcskl.getCSKL().equals("")) {
      rpcskl.jrfCSKL.requestFocus();
      JOptionPane.showConfirmDialog(this.jp, "Obavezan unos skladišta !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      return true;
    }

    return false;
  }
  
  private int[] visCols;

  private void createDataSets() {
    
    stanje = new QueryDataSet();
    stanje = hr.restart.util.Util.getUtil().getNewQueryDataSet("select stanje.cart from stanje where cskl = '"+rpcskl.getCSKL()+"' and god = '"+ hr.restart.util.Util.getUtil().getYear(tds.getTimestamp("zavDatum")) +"'");
    if (stanje.isEmpty()) setNoDataAndReturnImmediately();
    
    String qStr, s = "";
    if (!rpcart.findCART(podgrupe).equals("")) {
      s = " AND " + rpcart.findCART(podgrupe);
    }

    vNull = new QueryDataSet();
    
    vNull.setMetaDataUpdate(MetaDataUpdate.TABLENAME + MetaDataUpdate.PRECISION + MetaDataUpdate.SCALE + MetaDataUpdate.SEARCHABLE);
    vNull.setColumns(new Column[]{
        (Column) dm.getStdoku().getColumn("CART").clone(), 
        (Column) dm.getArtikli().getColumn("CART1").clone(), 
        (Column) dm.getArtikli().getColumn("BC").clone(), 
        (Column) dm.getStdoku().getColumn("NAZART").clone(), 
        (Column) dm.getArtikli().getColumn("JM").clone(), 
        (Column) colKDon.clone(),
        (Column) colKUl.clone(), 
        (Column) colKIz.clone(), 
        (Column) colZC.clone(), 
        (Column) colZDon.clone(), 
        (Column) colZad.clone(), 
        (Column) colRaz.clone(), 
        (Column) col1.clone(), 
        (Column) col2.clone(), 
        (Column) colSKL.clone(), 
        (Column) colNSKL.clone(), 
        (Column) colPD.clone(), 
        (Column) colZD.clone()});
    vNull.getColumn("KOLPD").setVisible(0);
    vNull.getColumn("KOLZD").setVisible(0);
    
    
    
    handleColsAndRest();

    ld.raLocate(dm.getSklad(), new String[]{"CSKL"}, new String[]{rpcskl.getCSKL()});
    vrzal = dm.getSklad().getString("VRZAL");

    // uvijek dovuæ od poèetka godine...
    qStr = rdUtil.getUtil().getSveKartice(jtfPocArtikl.getText(), jtfZavArtikl.getText(), vrzal, rpcskl.getCSKL(), util.getTimestampValue(hr.restart.util.Util.getUtil().getFirstDayOfYear(tds.getTimestamp("pocDatum")), 0), util.getTimestampValue(tds.getTimestamp("zavDatum"), 1), s);
    
//    System.out.println(qStr);
    
    vl.execSQL(qStr);
    
//    vl.RezSet.open();
//    sysoutTEST sou = new sysoutTEST(false);
//    sou.prn(vl.RezSet);
//    vl.RezSet.close();
  }

  protected void handleColsAndRest() {
    if (ut.getFirstSecondOfDay(tds.getTimestamp("pocDatum")).equals(ut.getFirstDayOfYear(tds.getTimestamp("pocDatum")))) {
      this.addReport("hr.restart.robno.repTotKar","hr.restart.robno.repTotKar","TotKar", "Totali prometa kartica");
      vNull.getColumn("KOLDON").setVisible(0);
      vNull.getColumn("KOLDZAD").setVisible(0);
      visCols = new int[] {0,1,2,3,4,5,6,7,8,9};
    } else {
      this.addReport("hr.restart.robno.repTotKar","hr.restart.robno.repTotKar","TotKarDonos", "Totali prometa kartica");
      visCols = new int[] {0,1,2,3,4,5,6,7,8,9,10,11};
    }
  }

  private void setTotaliVis() {
//    vNull.getColumn("BRDOK").setVisible(0);
//    vNull.getColumn("VRDOK").setVisible(0);
//    vNull.getColumn("ZC").setVisible(0);
//    vNull.getColumn("DATDOK").setVisible(0);
    vNull.getColumn("KOLPD").setVisible(0);
    vNull.getColumn("KOLZD").setVisible(0);
  }

  private void setPojedinacnoVis() {
    vl.RezSet.getColumn("BRDOK").setVisible(0);
    vl.RezSet.getColumn("VRDOK").setVisible(0);
    vl.RezSet.getColumn("ZC").setVisible(0);
    vl.RezSet.getColumn("DATDOK").setVisible(0);
    vl.RezSet.getColumn("KOLSK").setVisible(0);
    vl.RezSet.getColumn("KOLNSK").setVisible(0);
    vl.RezSet.getColumn("SKOL").setVisible(0);
    vl.RezSet.getColumn("SIZN").setVisible(0);
    vNull.getColumn("KOLPD").setVisible(0);
    vNull.getColumn("KOLZD").setVisible(0);
  }

  // Esc hendlanje
  public void firstESC() {
    if (!this.jtfPocArtikl.isEnabled()) {
      upStanjeNaSkladistu.getInstance().nTransData = 0;
      this.jtfPocArtikl.setEnabled(true);
      this.showDefaultValues();
      rpcartReset();
      rcc.EnabDisabAll(this.jPanel3, true);
      this.jtfPocArtikl.requestFocus();
    } else {
      rcc.EnabDisabAll(this.rpcskl, true);
      rpcskl.setCSKL("");
    }
  }

  public boolean runFirstESC() {
    return !rpcskl.getCSKL().equals("");
  }

  public void componentShow() {
    rpcart.setGodina(Valid.getValid().getKnjigYear("robno"));
    rpcart.setDefParam();
    showDefaultValues();
    boolean cskl_corg = rdUtil.getUtil().getCSKL_CORG(raUser.getInstance().getDefSklad(), hr.restart.zapod.OrgStr.getKNJCORG());

    if (cskl_corg) {
      rpcskl.setCSKL(raUser.getInstance().getDefSklad());
      rpcskl.setDisab('S');
      jtfPocArtikl.requestFocusLater();
    } else
      rpcskl.jrfCSKL.requestFocus();
  }

  //Init
  private void jbInit() throws Exception {
    col1 = dM.createBigDecimalColumn("SKOL", "Saldo kol", 3);
    col2 = dM.createBigDecimalColumn("SIZN", "Saldo izn", 2);
    colKDon = dM.createBigDecimalColumn("KOLDON", "Donos kol.", 3);
    colKUl = dM.createBigDecimalColumn("KOLUL", "Ulaz", 3);
    colKIz = dM.createBigDecimalColumn("KOLIZ", "Izlaz", 3);
    colZDon = dM.createBigDecimalColumn("KOLDZAD", "Donos vri.", 2);
    colZad = dM.createBigDecimalColumn("KOLZAD", "Zaduženje", 2);
    colRaz = dM.createBigDecimalColumn("KOLRAZ", "Razduženje", 2);
    colZC = dM.createBigDecimalColumn("ZC", "Cijena", 2);

    this.setJPan(jp);

    jPanel3.setPreferredSize(new Dimension(605, 65));
    jPanel3.setLayout(xYLayout2);
    jLabel1.setText("Datum (od-do)");
    rpcskl.setRaMode('S');

    colSKL = dM.createStringColumn("KOLSK", "ŠS", 12);
    colNSKL = dM.createStringColumn("KOLNSK", "NS", 50);
    colNSKL.setWidth(30);

    colPD = dM.createTimestampColumn("KOLPD");
    colZD = dM.createTimestampColumn("KOLZD");

    tds.setColumns(new Column[]{dM.createTimestampColumn("pocDatum"), dM.createTimestampColumn("zavDatum")});
    jtfPocDatum.setDataSet(tds);
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setText("jraTextField2");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);

    jtfZavDatum.setDataSet(tds);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setText("jraTextField2");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);

    new raDateRange(jtfPocDatum, jtfZavDatum);

    jp.setLayout(borderLayout1);
    jLabel2.setText("Artikl (od-do)");
    rpcart.setMode(new String("DOH"));
    rpcart.setBorder(null);
    jtfPocArtikl.setHorizontalAlignment(SwingConstants.LEFT);
    jtfZavArtikl.setHorizontalAlignment(SwingConstants.LEFT);
    jp.setMinimumSize(new Dimension(599, 68));
    jbFilter.setText("Artikli");
    jbFilter.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbFilter_actionPerformed();
      }
    });

    jrbTotali.setText("Totali");
    jrbTotali.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jrbTotali_actionPerformed();
      }
    });
    jrbSveKar.setText("Pojedinaèno");
    jrbSveKar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jrbSveKar_actionPerformed();
      }
    });
    jPanel3.add(jLabel1, new XYConstraints(15, 5, -1, -1));
    jPanel3.add(jtfPocDatum, new XYConstraints(150, 5, 100, -1));
    jPanel3.add(jtfZavDatum, new XYConstraints(255, 5, 100, -1));
    jPanel3.add(jtfZavArtikl, new XYConstraints(255, 31, 100, -1));
    jPanel3.add(jrbTotali, new XYConstraints(375, 5, -1, -1));
    jPanel3.add(jrbSveKar, new XYConstraints(375, 31, -1, -1));
    jp.add(rpcart, BorderLayout.SOUTH);
    jp.add(rpcskl, BorderLayout.NORTH);
    jp.add(jPanel3, BorderLayout.CENTER);
    jPanel3.add(jLabel2, new XYConstraints(15, 31, -1, -1));
    jPanel3.add(jbFilter, new XYConstraints(504, 31, 100, 22));
    jPanel3.add(jtfPocArtikl, new XYConstraints(150, 31, 100, -1));

    jrbTotali.setVisible(false);
    jrbSveKar.setVisible(false);
  }

  void showDefaultValues() {
    tds.setTimestamp("pocDatum", hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(vl.findYear())));
    tds.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false, 0));
    this.jtfPocDatum.requestFocus();
    upStanjeNaSkladistu.getInstance().nTransData = 0;
    jp.setPreferredSize(jp.getPreferredSize());
    rpcart.setVisible(false);
    this.jtfPocArtikl.setText("");
    this.jtfZavArtikl.setText("");
    this.getJPTV().clearDataSet();
    removeNav();

    this.jrbTotali.setSelected(true);
    this.jrbSveKar.setSelected(false);
    rcc.EnabDisabAll(jPanel3, true);
  }

  public com.borland.dx.dataset.DataSet getQds() {
    return getJPTV().getMpTable().getDataSet();
  }

  public String getCskl() {
    return rpcskl.getCSKL();
  }
  
  public String getNazSkl() {
    return rpcskl.jrfNAZSKL.getText();
  }

  public int getCart() {
    return Integer.parseInt(rpcart.getCART());
  }

  public java.sql.Timestamp getPocDatum() {
    return tds.getTimestamp("pocDatum");
  }

  public java.sql.Timestamp getZavDatum() {
    return tds.getTimestamp("zavDatum");
  }
  
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

  public void calculate() {

    vNull.deleteAllRows();

    if (vl.RezSet.rowCount() == 0)
      setNoDataAndReturnImmediately();
    
    vl.RezSet.first();

    do  {
      checkClosing();
      
      BigDecimal kul, kiz, kzad, kraz;
      
      try {
        kul = vl.RezSet.getBigDecimal("KOLUL");
      } catch (Exception e) {
        kul = new BigDecimal(vl.RezSet.getDouble("KOLUL"));
      }
   
      try {
        kiz = vl.RezSet.getBigDecimal("KOLIZ");
      } catch (Exception e) {
        kiz = new BigDecimal(vl.RezSet.getDouble("KOLIZ"));
      }
   
      try {
        kzad = vl.RezSet.getBigDecimal("KOLZAD");
      } catch (Exception e) {
        kzad = new BigDecimal(vl.RezSet.getDouble("KOLZAD"));
      }
      
      try {
        kraz = vl.RezSet.getBigDecimal("KOLRAZ");
      } catch (Exception e) {
        kraz = new BigDecimal(vl.RezSet.getDouble("KOLRAZ"));
      }
   
      if (ld.raLocate(vNull,"CART",""+vl.RezSet.getInt("CART"))) { //vNull.getInt("CART") == vl.RezSet.getInt("CART")) {
        
        if (vl.RezSet.getTimestamp("DATDOK").before(ut.getFirstSecondOfDay(tds.getTimestamp("pocDatum")))){
          vNull.setBigDecimal("KOLDON",vNull.getBigDecimal("KOLDON").add(kul.subtract(kiz)));
          vNull.setBigDecimal("KOLDZAD",vNull.getBigDecimal("KOLDZAD").add(kzad.subtract(kraz)));
          vNull.setBigDecimal("SKOL", vNull.getBigDecimal("SKOL").add(kul.add(kiz.negate())));
          vNull.setBigDecimal("SIZN", vNull.getBigDecimal("SIZN").add(kzad.add(kraz.negate())));
          try {
            vNull.setBigDecimal("ZC", vNull.getBigDecimal("KOLDZAD").divide(vNull.getBigDecimal("KOLDON"),2,BigDecimal.ROUND_HALF_UP));
          } catch (Exception sex){
            vNull.setBigDecimal("ZC", vl.RezSet.getBigDecimal("ZC"));
          }
          continue;
        }
        
        
        vNull.setBigDecimal("KOLUL", vNull.getBigDecimal("KOLUL").add(kul));
        vNull.setBigDecimal("KOLIZ", vNull.getBigDecimal("KOLIZ").add(kiz));
        vNull.setBigDecimal("KOLZAD", vNull.getBigDecimal("KOLZAD").add(kzad));
        vNull.setBigDecimal("KOLRAZ", vNull.getBigDecimal("KOLRAZ").add(kraz));
        vNull.setBigDecimal("SKOL", vNull.getBigDecimal("SKOL").add(kul.add(kiz.negate())));
        vNull.setBigDecimal("SIZN", vNull.getBigDecimal("SIZN").add(kzad.add(kraz.negate())));
      } else {
        if (!postojimNaStanju(vl.RezSet.getInt("CART"))) continue;
        vNull.insertRow(false);
        
        if (vl.RezSet.getTimestamp("DATDOK").before(ut.getFirstSecondOfDay(tds.getTimestamp("pocDatum")))){
          vNull.setBigDecimal("KOLDON",(kul.subtract(kiz)));
          vNull.setBigDecimal("KOLDZAD",(kzad.subtract(kraz)));

          
          vNull.setInt("CART",vl.RezSet.getInt("CART"));
          vNull.setString("CART1",vl.RezSet.getString("CART1"));
          vNull.setString("BC",vl.RezSet.getString("BC"));
          vNull.setString("NAZART",vl.RezSet.getString("NAZART"));
          vNull.setString("JM", vl.RezSet.getString("JM"));
          vNull.setBigDecimal("SKOL", kul.add(kiz.negate()));
          vNull.setBigDecimal("SIZN", kzad.add(kraz.negate()));
          try {
            vNull.setBigDecimal("ZC", vNull.getBigDecimal("KOLDZAD").divide(vNull.getBigDecimal("KOLDON"),2,BigDecimal.ROUND_HALF_UP));
          } catch (Exception sex){
            vNull.setBigDecimal("ZC", vl.RezSet.getBigDecimal("ZC"));
          }
          continue;
        }
        
        
        vNull.setInt("CART",vl.RezSet.getInt("CART"));
        vNull.setString("CART1",vl.RezSet.getString("CART1"));
        vNull.setString("BC",vl.RezSet.getString("BC"));
        vNull.setString("NAZART",vl.RezSet.getString("NAZART"));
        vNull.setString("JM", vl.RezSet.getString("JM"));
        vNull.setBigDecimal("KOLUL", kul);
        vNull.setBigDecimal("KOLIZ", kiz);
        vNull.setBigDecimal("KOLZAD", kzad);
        vNull.setBigDecimal("KOLRAZ", kraz);
        vNull.setBigDecimal("SKOL", kul.add(kiz.negate()));
        vNull.setBigDecimal("SIZN", kzad.add(kraz.negate()));
        vNull.setString("KOLSK", rpcskl.jrfCSKL.getText());
        vNull.setString("KOLNSK", rpcskl.jrfNAZSKL.getText());
        vNull.setTimestamp("KOLPD", tds.getTimestamp("pocDatum"));
        vNull.setTimestamp("KOLZD", tds.getTimestamp("zavDatum"));
      }
      try {
        vNull.setBigDecimal("ZC", vNull.getBigDecimal("SIZN").divide(vNull.getBigDecimal("SKOL"),2,BigDecimal.ROUND_HALF_UP));
      } catch (Exception sex){
        vNull.setBigDecimal("ZC", vl.RezSet.getBigDecimal("ZC"));
      }
    } while (vl.RezSet.next());
    
    vNull.first();
    do {
      if (vNull.getBigDecimal("KOLUL").compareTo(Aus.zero3) == 0 && 
          vNull.getBigDecimal("KOLIZ").compareTo(Aus.zero3) == 0 &&
          vNull.getBigDecimal("KOLDON").compareTo(Aus.zero3) == 0)
        vNull.deleteRow();
      else 
        vNull.next();
    } while (vNull.inBounds());
    vNull.first();
  }

  void rpcartReset() {
    rpcart.jrfCART.setText("");
    rpcart.jrfCART1.setText("");
    rpcart.jrfCGRART.setText("");
    rpcart.jrfCPOR.setText("");
    rpcart.jrfISB.setText("");
    rpcart.jrfJM.setText("");
    rpcart.jrfNAZART.setText("");
  }

  void setSifraVisible() {
    vNull.getColumn("CART").setVisible(0);
    vNull.getColumn("CART1").setVisible(0);
    vNull.getColumn("BC").setVisible(0);
    vNull.getColumn("KOLSK").setVisible(0);
    vNull.getColumn("KOLNSK").setVisible(0);

    vNull.getColumn(Aut.getAut().getCARTdependable("CART", "CART1", "BC")).setVisible(1);
  }

  void jrbTotali_actionPerformed() {
    this.jrbTotali.setSelected(true);
    this.jrbSveKar.setSelected(false);
  }

  void jrbSveKar_actionPerformed() {
    this.jrbSveKar.setSelected(true);
    this.jrbTotali.setSelected(false);
  }

  void jbFilter_actionPerformed() {
    jtfPocArtikl.setText("");
    jtfZavArtikl.setText("");
    rcc.EnabDisabAll(this.jPanel3, false);
    rpcart.EnabDisab(true);
    rpcart.setVisible(true);
    rpcart.setCARTLater();
  }

//  int nTransData=0;
//  java.sql.Timestamp nTransDateP, nTransDateZ;
//  String nTransCskl="";
//
//  public void jptv_doubleClick() {
//    nTransDateP = tds.getTimestamp("pocDatum");
//    nTransDateZ = tds.getTimestamp("zavDatum");
//
//    nTransCskl = this.getJPTV().getMpTable().getDataSet().getString("CSKL");
//
//    nTransData=this.getJPTV().getMpTable().getDataSet().getInt("CART");
//    
//    _Main.getStartFrame().showFrame("hr.restart.robno.upKartica", res.getString("upFrmKartica_title"));
//  }

  public void jptv_doubleClick() {
    /*UpStanjeRobno ussns = UpStanjeRobno.getInstance();
    
    ussns.nTransDonos = !ut.getFirstSecondOfDay(tds.getTimestamp("pocDatum")).equals(ut.getFirstDayOfYear(tds.getTimestamp("pocDatum")));
    ussns.nTransDate = tds.getTimestamp("zavDatum");
    ussns.nTransDateOd = tds.getTimestamp("pocDatum");
    ussns.nTransData = this.getJPTV().getMpTable().getDataSet().getInt("CART");
    ussns.nTransCskl = rpcskl.getCSKL();*/
    _Main.getStartFrame().showFrame("hr.restart.robno.upKartica", 15, 
        res.getString("upFrmKartica_title"), false);
    upKartica.getupKartica().setOutsideData(
        rpcskl.getCSKL(), getJPTV().getDataSet().getInt("CART"),
        tds.getTimestamp("pocDatum"), tds.getTimestamp("zavDatum"));
    _Main.getStartFrame().showFrame("hr.restart.robno.upKartica", 
        res.getString("upFrmKartica_title"));
    
  }

  public boolean validateTF(String str) {
    try {
      new Integer(str);
    } catch (Exception ex) {
      return false;
    }
    return true;
  }

  private boolean postojimNaStanju(int tempcart) {
    return ld.raLocate(stanje,"CART",tempcart+"");
  }
  
  
  
  
  
  public void cancelPress() {
    upStanjeNaSkladistu.getInstance().nTransData = 0;
    super.cancelPress();
  }
  
  public String navDoubleClickActionName() {
    return "Kartica artikla";
  }
  public int[] navVisibleColumns() {
    return visCols;
  }
}
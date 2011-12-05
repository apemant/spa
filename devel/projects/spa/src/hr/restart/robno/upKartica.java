/****license*****************************************************************
**   file: upKartica.java
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
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.swing.raTableRunningSum;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raUpitFat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:        Robno poslovanje
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      REST-ART
 * @author REST-ART development team
 * @version 1.0
 */

public class upKartica extends raUpitFat {
  boolean lTrans=false;
  hr.restart.robno._Main main;
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  String cart;
  String vrzal;
  Valid vl = Valid.getValid();
  TableDataSet tds = new TableDataSet();
  java.sql.Date dateZ = null;
  java.sql.Date dateP = null;
  Column col1=new Column();
  Column col2=new Column();
  Column colKUl=new Column();
  Column colKIz=new Column();
  Column colZad=new Column();
  Column colRaz=new Column();
  Column colUI = dM.createStringColumn("UI",1);
  Column colSRT = dM.createStringColumn("SRT",1);

  Column colUlSk = dM.createStringColumn("SKLUL", "Skladište ulaza", 12);
  Column colIzSk = dM.createStringColumn("SKLIZ", "Skladište izlaza", 12);


  JraCheckBox jcbDonos = new JraCheckBox();
  XYLayout xYLayout2 = new XYLayout();
  JraTextField jtfZavDatum = new JraTextField();
  rapancskl rpcskl = new rapancskl() {
    public void findFocusAfter() {
      rpcart.setCskl(rpcskl.getCSKL());
      rpcart.setGodina(vl.findYear(getPocDatum()));
      if (rpcart.getCART().length() == 0) {
        rpcart.setDefParam();
        if (!lTrans){
          rpcart.setCART();
        }
      }
//      if(!rpcskl.jrfCSKL.getText().equals(""))
//      {
//        rpcart.setParam(hr.restart.sisfun.frmParam.getParam("robno","indiCart"));
//        rpcart.SetFocus(hr.restart.sisfun.frmParam.getParam("robno","focusCart"));
//      }
    }
  };
  BorderLayout borderLayout1 = new BorderLayout();
  JraTextField jtfPocDatum = new JraTextField();
  rapancart rpcart = new rapancart() {
    public void nextTofocus(){
      if (!lTrans) jtfPocDatum.requestFocus();
    }
  };
  JLabel jLabel1 = new JLabel();
  JPanel jPanel3 = new JPanel();
  JPanel jp = new JPanel();
  dM dm;
  static upKartica upk;
  
  OutsideData out = null;
  
  OutsideLocation loc = null;
  

  public upKartica() {
    try {
      jbInit();
      upk=this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public static upKartica getupKartica(){
    if (upk == null) {
      upk = new upKartica();
    }
    return upk;
  }

  public String navDoubleClickActionName(){
    return "Prikaz dokumenta";
  }

  public int[] navVisibleColumns(){
    return new int[] {0,1,2,3,4,5,6,7,10,11};
  }

  String newDateP, newDateZ;

  public boolean Validacija() {
    cart=rpcart.getCART();
    return !vratiGreske();
//    if(vratiGreske())
//      return false;
//    return true;
  }

  public void okPress() {
    java.math.BigDecimal tempKOL = main.nul;
    java.math.BigDecimal tempVR = main.nul;
    /*java.math.BigDecimal tempKOL2 = main.nul;
    java.math.BigDecimal tempSUM = main.nul;*/
    java.math.BigDecimal tempUl = main.nul;
    java.math.BigDecimal tempIz = main.nul;
//    java.math.BigDecimal tempUk = main.nul;
    java.math.BigDecimal tempZad = main.nul;
    java.math.BigDecimal tempRaz = main.nul;
    boolean insertNewRow=false;
    dateP = new java.sql.Date(this.tds.getTimestamp("pocDatum").getTime());
    dateZ = new java.sql.Date(this.tds.getTimestamp("zavDatum").getTime());
    String qStr;
    newDateP = rut.getTimestampValue(tds.getTimestamp("pocDatum"), 0);
    newDateZ = rut.getTimestampValue(tds.getTimestamp("zavDatum"), 1);

//    lookupData.getlookupData().raLocate(dm.getSklad(), new String[] {"CSKL"}, new String[] {rpcskl.getCSKL()});

    vrzal = lookupData.getlookupData().raLookup(dm.getSklad(), new String[] {"CSKL"}, new String[] {rpcskl.getCSKL()}).getString("VRZAL");
    
    qStr = rdUtil.getUtil().getKarticaArtikla(cart, vrzal, rpcskl.getCSKL(), rut.getDoc("DOKU", "STDOKU"), rut.getDoc("DOKI", "STDOKI"), newDateZ);

//    System.out.println("qdst KARTICA : " + qStr);
    
    QueryDataSet card = new QueryDataSet();
    card.setLocale(Aus.hr);
    
/*
    vl.execSQL(qStr);
    QueryDataSet card = vl.getDataAndClear();
    card.open();
    Aus.dumpColumns(card);
    card.close();*/
//    card.close();
//    card.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
//    card.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
    card.setColumns(new Column[] {
      (Column) colUI.clone(),
      (Column) colSRT.clone(),
      (Column) dM.getDataModule().getDoku().getColumn("VRDOK").clone(),
      (Column) dM.getDataModule().getDoku().getColumn("BRDOK").clone(),
      (Column) dM.getDataModule().getStdoku().getColumn("RBR").clone(),
      (Column) dM.getDataModule().getDoku().getColumn("DATDOK").clone(),
      (Column) colKUl.clone(),
      (Column) colKIz.clone(),
      (Column) hr.restart.baza.dM.getDataModule().getStdoku().getColumn("ZC").clone(),
      (Column) colZad.clone(),
      (Column) colRaz.clone(),
      (Column) col1.clone(),
      (Column) col2.clone(),
      (Column) colUlSk.clone(),
      (Column) colIzSk.clone()
    });

    card.getColumn("VRDOK").setWidth(8);
    card.getColumn("DATDOK").setWidth(16);


    card.getColumn("UI").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    card.getColumn("SRT").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    card.getColumn("RBR").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    //card.getColumn("SKLUL").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    //card.getColumn("SKLIZ").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    
    //additionalDisable();

    //openScratchDataSet(card);
    //card = timeReset(card);
    fillScratchDataSet(card, qStr);
    
    card.setSort(new SortDescriptor(new String[]{"DATDOK","SRT","UI","BRDOK","RBR"}));

    //card.open();

//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(card);

//      OLD: card.setSort(new SortDescriptor(new String[]{"BRDOK", "RBR", "SRT", "UI"}));

//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(card);

    if (card.rowCount() == 0) setNoDataAndReturnImmediately();

    card.first();
      do {
        Aus.sub(card, "SKOL", "KOLUL", "KOLIZ");
        Aus.sub(card, "SIZN", "KOLZAD", "KOLRAZ");
        tempKOL=tempKOL.add(card.getBigDecimal("SKOL"));
        tempVR=tempVR.add(card.getBigDecimal("SIZN"));
        if (card.getTimestamp("DATDOK").before(java.sql.Timestamp.valueOf(newDateP.substring(1,newDateP.length()-2)))) {
          tempUl=tempUl.add(card.getBigDecimal("KOLUL"));
          tempIz=tempIz.add(card.getBigDecimal("KOLIZ"));
          tempZad = tempZad.add(card.getBigDecimal("KOLZAD"));
          tempRaz = tempRaz.add(card.getBigDecimal("KOLRAZ"));
          card.deleteRow();
          if (!insertNewRow) insertNewRow=true;
        }
        else {
          card.next();
        }
      } while (card.inBounds());

      card.first(); // mozda je to mislio (?)

      DataRow temprow = new DataRow(card);
      String[] cols = temprow.getColumnNames(temprow.getColumnCount());

      if(jcbDonos.isSelected())
      {
        if (insertNewRow)
        {
//          System.out.println("row 0 : " + card.getRow());
          DataSet.copyTo(cols, card, cols, temprow);
//          System.out.println("row A : " + card.getRow());
          card.insertRow(false);
//          System.out.println("row B : " + card.getRow());
          DataSet.copyTo(cols, temprow, cols, card);
//          System.out.println("row C : " + card.getRow());

          card.first();
          card.clearValues();
//          System.out.println("row D : " + card.getRow());
//
          card.setString(0, "A");
          card.setString(1, "A");
          card.setString(2, "DON");
          card.setInt(3,0);
          card.setShort(4,(short)0);
          card.setTimestamp(5, tds.getTimestamp("pocDatum"));
          card.setBigDecimal(6, tempUl);
          card.setBigDecimal(7, tempIz);
          
          card.setBigDecimal(9, tempZad);
          card.setBigDecimal(10, tempRaz);
          try {
            card.setBigDecimal("SKOL", tempUl.subtract(tempIz));
            card.setBigDecimal("SIZN", tempZad.subtract(tempRaz));
            card.setBigDecimal(8, rut.divideValue(
                card.getBigDecimal("SIZN"), card.getBigDecimal("SKOL")));
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
//          System.out.println("row E : " + card.getRow());
//          sysoutTEST syst = new sysoutTEST(false);
//          syst.prn(card);
        }
      }


      if (card.rowCount() == 0) setNoDataAndReturnImmediately();
      
      if (loc != null) {
        if (loc.sklul == null)
          lookupData.getlookupData().raLocate(card, 
              new String[] {"VRDOK", "BRDOK"},
              new String[] {loc.vrdok, Integer.toString(loc.brdok)});
      }

//      this.getJPTV().setDataSet(card);
      setTitle("Kartica artikla " + Aut.getAut().getCARTdependable(
          rpcart.getCART(), rpcart.getCART1(), rpcart.getBC()) + " - "
          + rpcart.getNAZART() + "  od " + 
          Aus.formatTimestamp(tds.getTimestamp("pocDatum")) + " do " +
          Aus.formatTimestamp(tds.getTimestamp("zavDatum")));
      setDataSetAndSums(card, new String[] {"KOLUL","KOLIZ","KOLZAD","KOLRAZ"});
      System.out.println("CART poslije tablice " + rpcart.getCART());
  }
  
  protected void additionalDisable(){
  }

  private boolean vratiGreske()
  {
//    String zavGod = hr.restart.util.Util.getUtil().getYear(tds.getTimestamp("zavDatum"));
//    String pocGod = hr.restart.util.Util.getUtil().getYear(tds.getTimestamp("pocDatum"));
//    java.sql.Timestamp beginDate = beginDateField.getDataSet().getTimestamp(beginDateField.getColumnName());
//    Timestamp endDate = endDateField.getDataSet().getTimestamp(endDateField.getColumnName());
    if (rpcskl.getCSKL().equals("")) {
      rpcskl.jrfCSKL.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Obavezan unos skladišta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return true;
    }
    else if (rpcart.getCART().equals("")) {
      rpcart.setCART();
      JOptionPane.showConfirmDialog(this.jp,"Obavezan unos artikla !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return true;
    } else if (tds.getTimestamp("zavDatum").before(tds.getTimestamp("pocDatum"))) {
      jtfPocDatum.requestFocus();
      JOptionPane.showMessageDialog(jtfPocDatum,
        "Po\u010Detni datum ve\u0107i od završnog !","Greška",JOptionPane.ERROR_MESSAGE);
      return true;
    }


    /*else if (!Aus.checkDateRange(jtfPocDatum, jtfZavDatum)) return true;
    else if(!pocGod.equals(zavGod))
    {
      jtfPocDatum.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Godina u poèetnom i završnom datumu nije ista !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return true;
    }*/
    return false;/** @todo zasad ovako, a kasnije cemo vidit kako */
  }

  public void firstESC() {
    if (!lTrans) {
      if (rpcart.getCART().trim().equals("")) {
        rcc.EnabDisabAll(this.jPanel3, true);
        rpcart.EnabDisab(true);
        rpcskl.setDisab('N');
        rpcskl.setCSKL("");
      }
      else {
        getJPTV().clearDataSet();
        removeNav();
        rcc.EnabDisabAll(this.jPanel3, true);
        rpcart.EnabDisab(true);
        rpcart.setCARTLater();
      }
    }
    else {
//      rcc.EnabDisabAll(this.jPanel3, true);
//      showDefaultValues();
      this.cancelPress();
    }
    System.out.println("CART first esc " + rpcart.getCART());
  }
  public boolean runFirstESC() {
    return !rpcskl.getCSKL().equals("");
  }
  
  public void resetDefaults() {
    rpcskl.setDisab('N');
    rpcskl.setCSKL(raUser.getInstance().getDefSklad());
    rpcart.EnabDisab(true);
    rpcart.setDefParam();
    rpcart.clearFields();
    String god = Aut.getAut().getKnjigodRobno();
    rpcart.setGodina(god);
    tds.setTimestamp("pocDatum", ut.getYearBegin(god));
    tds.setTimestamp("zavDatum", 
        ut.getToday(ut.getYearBegin(god), ut.getYearEnd(god)));
    jcbDonos.setSelected(false);
    initialized = true;
  }
  
  boolean initialized = false;
  public void componentShow() {
    if (!initialized) resetDefaults();
    getJPTV().clearDataSet();
    repaint();
    if (lTrans) {
      tds.setTimestamp("zavDatum", out.to);
      tds.setTimestamp("pocDatum", out.from);
      jcbDonos.setSelected(false);
      rpcskl.setCSKL(out.cskl);
      rpcart.setCART(out.art);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          getOKPanel().jBOK_actionPerformed();
        }
      });
    } else if (rpcskl.getCSKL().length() == 0)
      rpcskl.jrfCSKL.requestFocusLater();
    else if (rpcart.getCART().length() == 0)
      rpcart.setCARTLater();
    else jtfPocDatum.requestFocusLater();
  }
  
  private void jbInit() throws Exception {
    handleReports();
    dm = hr.restart.baza.dM.getDataModule();
    vl = hr.restart.util.Valid.getValid();

    col1 = dM.createBigDecimalColumn("SKOL", "Saldo kol", 3);
    col1.setSqlType(0);
    col2 = dM.createBigDecimalColumn("SIZN", "Saldo izn", 2);
    col2.setSqlType(0);

    colKUl = dM.createBigDecimalColumn("KOLUL", "Ulaz", 3);
    colKUl.setSqlType(0);
    colKIz = dM.createBigDecimalColumn("KOLIZ", "Izlaz", 3);
    colKIz.setSqlType(0);
    
    colZad = dM.createBigDecimalColumn("KOLZAD", "Zaduženje", 2);
    colZad.setSqlType(0);
    colRaz = dM.createBigDecimalColumn("KOLRAZ", "Razduženje", 2);
    colRaz.setSqlType(0);

    this.setJPan(jp);
    jPanel3.setPreferredSize(new Dimension(555, 45));
    jPanel3.setLayout(xYLayout2);
    jLabel1.setText("Datum (od-do)");
    rpcart.setMode("DOH");
    rpcart.setBorder(null);

//    tds.setColumns(new Column[] {dM.createTimestampColumn("pocDatum"), dM.createTimestampColumn("zavDatum")});
    tds.setColumns(new Column[] {
                   dM.createTimestampColumn("pocDatum", "Po\u010Detni datum"),
                   dM.createTimestampColumn("zavDatum", "Završni datum"),
                   dM.createStringColumn("cskl", "Skladište", 12)
    });


    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(tds);
    jtfPocDatum.setText("jraTextField1");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    rpcskl.setRaMode('S');
    rpcskl.setDataSet(tds);
    jtfZavDatum.setDataSet(tds);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setText("");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);

    new raDateRange(jtfPocDatum, jtfZavDatum);

    jcbDonos.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbDonos.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbDonos.setText("Prikaz donosa");

    jp.setLayout(borderLayout1);
    jp.setMinimumSize(new Dimension(650, 165));
    jp.setPreferredSize(new Dimension(650, 165));
    jp.add(jPanel3, BorderLayout.SOUTH);
    jPanel3.add(jLabel1,     new XYConstraints(15, 5, -1, -1));
    jPanel3.add(jtfPocDatum, new XYConstraints(150, 5, 100, -1));
    jPanel3.add(jtfZavDatum, new XYConstraints(255, 5, 100, -1));
    jPanel3.add(jcbDonos,    new XYConstraints(465, 5, 140, -1));
    jp.add(rpcart, BorderLayout.CENTER);
    jp.add(rpcskl, BorderLayout.NORTH);
    
    getJPTV().addTableModifier(new raTableRunningSum("SIZN"));
    getJPTV().addTableModifier(new raTableRunningSum("SKOL"));
    
    installResetButton();

//    rpcart.jrfCART.getRaDataSet().open();
//
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(rpcart.jrfCART.getRaDataSet());

  }
  protected void handleReports() {
//  this.addReport("hr.restart.robno.repKartica", "Ispis kartice artikla", 5);
//  this.addReport("hr.restart.robno.repKartica2Reda", "Ispis kartice artikla - 2 reda", 5);
  this.addReport("hr.restart.robno.repKartica","hr.restart.robno.repKartica","Kartica", "Ispis kartice artikla");
  this.addReport("hr.restart.robno.repKartica2Reda","hr.restart.robno.repKartica2Reda","Kartica2Reda", "Ispis kartice artikla - 2 reda");
  }
  /**
   * Prikazivanje defaultnih vrijednosti
   */
  /*void showDefaultValues() {
    rpcart.setGodina(Valid.getValid().getKnjigYear("robno"));
    tds.setTimestamp("pocDatum",
                     hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(vl.findYear())));
    tds.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false,0));
    jcbDonos.setSelected(false);
    this.getJPTV().clearDataSet();
    rpcart.EnabDisab(true);
    System.out.println("UpStanjeRobno.getInstance().nTransData = " + UpStanjeRobno.getInstance().nTransData);
    if (UpStanjeRobno.getInstance().nTransData==0) {
      lTrans=false;
    }
    else {
      lTrans = true;
      tds.setTimestamp("zavDatum", UpStanjeRobno.getInstance().nTransDate);
      tds.setTimestamp("pocDatum", UpStanjeRobno.getInstance().nTransDateOd);
      this.jcbDonos.setSelected(UpStanjeRobno.getInstance().nTransDonos);
      //if (rpcskl.jrfCSKL.isEnabled())
      rpcskl.setCSKL(UpStanjeRobno.getInstance().nTransCskl);
      rpcart.setCART(UpStanjeRobno.getInstance().nTransData);
//      rpcart.jrfCART.forceFocLost();
      this.getOKPanel().jBOK_actionPerformed();
//
//      okPress();

      System.out.println("CART !! " + rpcart.getCART());
//      UpStanjeRobno.getInstance().nTransData=0;
    }
  }*/

  public QueryDataSet timeReset(QueryDataSet qds)
  {
    if(!qds.isOpen())
      qds.open();
    qds.first();
    while(qds.inBounds())
    {
      qds.setTimestamp("DATDOK", hr.restart.util.Util.getUtil().getFirstSecondOfDay(qds.getTimestamp("DATDOK")));
      qds.next();
    }

    return qds;
  }

  public void jptv_doubleClick() {
    DataSet ds = getQds();
    if (TypeDoc.getTypeDoc().isDocMeskla(ds.getString("VRDOK")))
      rut.showDocs(ds.getString("SKLIZ"), ds.getString("SKLUL"),
          ds.getString("VRDOK"), ds.getInt("BRDOK"),
          vl.findYear(ds.getTimestamp("DATDOK")), cart);
    else 
      rut.showDocs(tds.getString("CSKL"), "",
          ds.getString("VRDOK"), ds.getInt("BRDOK"),
          vl.findYear(ds.getTimestamp("DATDOK")), cart);
          

//    System.out.println("Neš' ti dabl klika");/**@todo: Natjerati Tomu da slozi pregled dokumenta */
//    System.out.println("VRDOK: " + this.getJPTV().getMpTable().getDataSet().getString("VRDOK"));
//    System.out.println("BRDOK: " + this.getJPTV().getMpTable().getDataSet().getInt("BRDOK"));
//    System.out.println("CSKL: " + rpcskl.getCSKL());
/*    String cskl = "";
    String csklul = "";
    String cskliz = "";
    String classss = "";

    if (TypeDoc.getTypeDoc().isDocMeskla(this.getJPTV().getMpTable().getDataSet().getString("VRDOK"))){
      if (this.getJPTV().getMpTable().getDataSet().getString("VRDOK").equals("MEI")){
      lookupData.getlookupData().raLocate(dm.getMeskla(),
                                          new String[] {"VRDOK","BRDOK","CSKLIZ"},
                                          new String[] {"MEI", this.getJPTV().getMpTable().getDataSet().getInt("BRDOK")+"",rpcskl.getCSKL()}
                                          );
      csklul = dm.getMeskla().getString("CSKLUL");
      cskliz = rpcskl.getCSKL();
      classss = "hr.restart.robno.raMEI";

      } else if (this.getJPTV().getMpTable().getDataSet().getString("VRDOK").equals("MEU")){
      lookupData.getlookupData().raLocate(dm.getMeskla(),
                                          new String[] {"VRDOK","BRDOK","CSKLUL"},
                                          new String[] {"MEI", this.getJPTV().getMpTable().getDataSet().getInt("BRDOK")+"",rpcskl.getCSKL()}
                                          );
      csklul = rpcskl.getCSKL();
      cskliz = dm.getMeskla().getString("CSKLIZ");
      classss = "hr.restart.robno.raMEU";
      } else {
//        System.out.println("E jebiga....");
        return;
      }

      raMasterDetail.showRecord(classss,
                                new String[] {"VRDOK","BRDOK","CSKLUL","CSKLIZ"},
                                new String[] {this.getJPTV().getMpTable().getDataSet().getString("VRDOK"),
                                this.getJPTV().getMpTable().getDataSet().getInt("BRDOK")+"",
                                csklul,cskliz}
                                );
    } else {
      if (this.getJPTV().getMpTable().getDataSet().getString("VRDOK").equals("RAC")) classss = "hr.restart.robno.raRAC";
      else if (this.getJPTV().getMpTable().getDataSet().getString("VRDOK").equals("GOT")) classss = "hr.restart.robno.raGOT";
      else if (this.getJPTV().getMpTable().getDataSet().getString("VRDOK").equals("ROT")) classss = "hr.restart.robno.raROT";
      else if (this.getJPTV().getMpTable().getDataSet().getString("VRDOK").equals("POS")) classss = "hr.restart.robno.raPOS";
      else if (this.getJPTV().getMpTable().getDataSet().getString("VRDOK").equals("OTR")) classss = "hr.restart.robno.frmOtpisRobe";
      else if (this.getJPTV().getMpTable().getDataSet().getString("VRDOK").equals("OTP")) classss = "hr.restart.robno.raOTP";
      else if (this.getJPTV().getMpTable().getDataSet().getString("VRDOK").equals("PRD")) classss = "hr.restart.robno.raPRD";
      else if (this.getJPTV().getMpTable().getDataSet().getString("VRDOK").equals("PON")) classss = "hr.restart.robno.raPON";
      else if (this.getJPTV().getMpTable().getDataSet().getString("VRDOK").equals("IZD")) classss = "hr.restart.robno.raIZD";
      else if (this.getJPTV().getMpTable().getDataSet().getString("VRDOK").equals("PST")) classss = "hr.restart.robno.frmPST";
      else if (this.getJPTV().getMpTable().getDataSet().getString("VRDOK").equals("PTE")) classss = "hr.restart.robno.frmPTE";
      else if (this.getJPTV().getMpTable().getDataSet().getString("VRDOK").equals("INM")) classss = "hr.restart.robno.frmPregledManjak";
      else if (this.getJPTV().getMpTable().getDataSet().getString("VRDOK").equals("INV")) classss = "hr.restart.robno.frmPregledVisak";
      else if (this.getJPTV().getMpTable().getDataSet().getString("VRDOK").equals("PRK")) classss = "hr.restart.robno.frmPRK";
//      else if (this.getJPTV().getMpTable().getDataSet().getString("VRDOK").equals("POR")) classss = "hr.restart.robno.?????????";
      else return; // @todo pitati za vrste tipa POR, KAL..... >>>>----------------=>
      cskl = rpcskl.getCSKL();

      raMasterDetail.showRecord(classss,
                                new String[] {"VRDOK","BRDOK","CSKL","GOD"},
                                new String[] {this.getJPTV().getMpTable().getDataSet().getString("VRDOK"),
                                this.getJPTV().getMpTable().getDataSet().getInt("BRDOK")+"",
                                cskl,vl.findYear(this.getJPTV().getMpTable().getDataSet().getTimestamp("DATDOK"))
      }                         );
    }*/

  }

  public com.borland.dx.dataset.DataSet getQds() {
    return getJPTV().getMpTable().getDataSet();
  }

  public String getCskl() {
    return rpcskl.getCSKL();
  }

  public int getCart() {
    return Integer.parseInt(rpcart.getCART());
  }

  public String getGodina(){
    return newDateZ.substring(1,5);
  }

  public java.sql.Timestamp getPocDatum() {
    return tds.getTimestamp("pocDatum");
  }

  public java.sql.Timestamp getZavDatum() {
    return tds.getTimestamp("zavDatum");
  }
  
  public void setOutsideData(String cskl, int cart, Timestamp from, Timestamp to) {
    out = new OutsideData();
    out.from = from;
    out.to = to;
    out.cskl = cskl;
    out.art = cart;
    lTrans = true;
  }
  
  
  public void setOutsideLocation(String vrdok, int brdok) {
    setOutsideLocation(null, vrdok, brdok);
  }
  
  public void setOutsideLocation(String sklul, String vrdok, int brdok) {
    loc = new OutsideLocation();
    loc.sklul = sklul;
    loc.vrdok = vrdok;
    loc.brdok = brdok;
  }
  
  public void clearOutsideData() {
    out = null;
    loc = null;
    lTrans = false;
  }
  
  class OutsideLocation {
    String sklul, vrdok;
    int brdok;
  }
  
  class OutsideData {
    public Timestamp from, to;
    public String cskl;
    public int art;
  }
}

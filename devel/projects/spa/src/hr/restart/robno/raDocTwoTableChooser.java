/****license*****************************************************************
**   file: raDocTwoTableChooser.java
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
import hr.restart.baza.KreirDrop;
import hr.restart.baza.Pos;
import hr.restart.baza.Sklad;
import hr.restart.baza.Stanje;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;
import hr.restart.util.raTwoTableFrame;
import hr.restart.util.sysoutTEST;

import java.awt.Font;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public abstract class raDocTwoTableChooser extends raTwoTableFrame {

  BigDecimal ukupno = new BigDecimal(0);
  String errors = null;
  StorageDataSet errorSet = new StorageDataSet();
  int errnum = 0;
  QueryDataSet master;
  QueryDataSet detail;
  QueryDataSet stanje;
  StorageDataSet presel;
  Condition brdoks, doctype; 
  //public static String whString, whStringPrev;
  //public static boolean ok=true;
  hr.restart.util.Valid val= hr.restart.util.Valid.getValid();
  private hr.restart.util.LinkClass lc = hr.restart.util.LinkClass.getLinkClass();
  private raKalkulBDDoc rKD = new raKalkulBDDoc();
  boolean isEverythingOK = true;
  hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  String table,tabledet,vrdok;
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  JPanel jpUpit = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JraTextField jtfVRDOK = new JraTextField();
  JlrNavField jrfNAZSKL = new JlrNavField();
  JraTextField jtfDATUM = new JraTextField();
  JlrNavField jrfCSKL = new JlrNavField();
  JraButton jbCSKL = new JraButton();
  TableDataSet tds = new TableDataSet();
  Column colCSKL = new Column();
  Column colVRDOK = new Column();
  Column colDATUM = new Column();
  String vrsta,masterkey;
  boolean rep, cag, arh;
  public raDocTwoTableChooser(String vrd) {
    vrsta=vrd;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    this.getTTC().getLeftJPTW().setVisibleCols(new int[] {3,4,5});
    this.getTTC().getRightJPTW().setVisibleCols(new int[] {3,4,5});
    colCSKL=(Column) dm.getSklad().getColumn("CSKL").clone();
    colVRDOK=(Column) dm.getDoku().getColumn("VRDOK").clone();
    colDATUM=(Column) dm.getDoku().getColumn("DATDOK").clone();
    tds.setColumns(new Column[] {colCSKL,colVRDOK,colDATUM});
    raPozivNaBroj.getraPozivNaBrojClass(); // nije bez razloga :)
    
    errorSet.setColumns(new Column[] {
        dm.getArtikli().getColumn("CART").cloneColumn(),
        dm.getArtikli().getColumn("CART1").cloneColumn(),
        dm.getArtikli().getColumn("BC").cloneColumn(),
        dm.getArtikli().getColumn("NAZART").cloneColumn(),
        dM.createStringColumn("OPIS", "Opis", 50),
        dM.createBigDecimalColumn("KOL", "Kolièina", 3),
        dM.createBigDecimalColumn("KOLS", "Stanje", 3)
     });
     errorSet.setTableName("errors");
     errorSet.open();
    
    if (vrsta.equals("GRC")) {
      jLabel3.setText("Prodajno mjesto");
    }
    else {
      jLabel3.setText("Skladište");
    }
    jrfCSKL.setNavColumnName("CSKL");
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setVisCols(new int[]{0,1});
    jrfCSKL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
    jrfCSKL.setNavButton(jbCSKL);
    jrfCSKL.setDataSet(tds);
    jrfCSKL.setColumnName("CSKL");
    jrfNAZSKL.setColumnName("NAZSKL");
    jrfNAZSKL.setSearchMode(1);
    jrfNAZSKL.setNavProperties(jrfCSKL);
    jbCSKL.setText("...");
    jtfVRDOK.setDisabledTextColor(jtfVRDOK.getForeground());
    jtfVRDOK.setEnablePopupMenu(false);
    jtfVRDOK.setEnabled(false);
    jtfVRDOK.setOpaque(false);
    jtfVRDOK.setFont(jtfVRDOK.getFont().deriveFont(Font.BOLD));
    jtfVRDOK.setDataSet(tds);
    jtfVRDOK.setColumnName("VRDOK");
    jtfVRDOK.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATUM.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATUM.setDataSet(tds);
    jtfDATUM.setColumnName("DATDOK");

    jpUpit.setLayout(xYLayout1);
    jLabel4.setText("Dokument");
    jLabel5.setText("Datum");
    xYLayout1.setWidth(525);
    xYLayout1.setHeight(105);
    jpUpit.add(jLabel3, new XYConstraints(15, 20, -1, -1));
    jpUpit.add(jLabel4, new XYConstraints(15, 45, -1, -1));
    jpUpit.add(jLabel5, new XYConstraints(15, 70, -1, -1));
    jpUpit.add(jrfCSKL, new XYConstraints(150, 20, -1, -1));
    jpUpit.add(jtfVRDOK, new XYConstraints(150, 45, -1, -1));
    jpUpit.add(jtfDATUM, new XYConstraints(150, 70, -1, -1));
    jpUpit.add(jrfNAZSKL, new XYConstraints(260, 20, 355, -1));
    jpUpit.add(jbCSKL, new XYConstraints(624, 20, 21, 21));
    this.setJPan(jpUpit);
    this.getTTC().setEnabled(false);
  }
  public void componentShow() {
    getTTC().setLeftDataSet(null);
    getTTC().setRightDataSet(null);

    getTTC().initialize();
    tds.setTimestamp("DATDOK", vl.getToday());
//    jrfCSKL.requestFocus();
    jrfCSKL.selectAll();
    tds.setString("VRDOK", vrsta);
    rcc.setLabelLaF(this.getJPan(), false);
    this.adisab(true);
    jrfCSKL.requestFocusLater();
  }
  public void setDataSet(QueryDataSet left, QueryDataSet right) {
    this.getTTC().setLeftDataSet(left);
    this.getTTC().setRightDataSet(right);
    left.setTableName(vrsta);
    right.setTableName(vrsta);
//  this.getTTC().rnvSave.setVisible(false);
    this.getTTC().initialize();
  }
  public boolean Validacija() {
    if (hr.restart.util.Valid.getValid().isEmpty(jrfCSKL))
      return false;
    if (hr.restart.util.Valid.getValid().isEmpty(jtfDATUM))
      return false;
    if (getTTC().getLeftDataSet()!=null &&
        getTTC().getRightDataSet().rowCount() == 0) {
      JOptionPane.showMessageDialog(this, "Nije odabran nijedan dokument!",
          "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  public boolean isIspis() {
/*    if (getTTC().getLeftDataSet()!=null) {
      changeIcon(3);
    }
    else {
      changeIcon(2);
    }*/
    return false;
  }
  QueryDataSet qdsLeft;
  QueryDataSet qdsRight;

  boolean dontClose = false;
  public void okPress() {
  	errors=null;
  	errnum = 0;
  	errorSet.empty();
  	ukupno=new BigDecimal(0);
  	bCancelPress=false;
    isEverythingOK = true;
    arh = master.getString("CSKL").startsWith("#");
    if (vrsta.equals("GRC")) {
      table="POS";
      tabledet="STPOS";
      vrdok="GRC";
    }
    else {
      table="DOKI";
      tabledet="STDOKI";
      if (vrsta.equals("PON")) {
        vrdok="PON";
      }
    }
    if (getTTC().getLeftDataSet()!=null) {
      boolean prvi=true;
      getTTC().getRightJPTW().enableEvents(false);
      doctype = Condition.whereAllEqual(
            new String[] {"CSKL", "GOD", "VRDOK"}, qdsRight);
      brdoks = Condition.in("BRDOK", qdsRight);
      cag = frmParam.getParam("pos", "cagentHack", "N",
          "Naèin plaæanja u cagent (D,N)").equals("D");
      rep = true;
      String cn = "";
      for (qdsRight.first(); qdsRight.inBounds(); qdsRight.next())
        if (!(cn = qdsRight.getString("CNACPL")).equals("R")) rep = false;
      
      for (qdsRight.first(); qdsRight.inBounds(); qdsRight.next())
        if (!qdsRight.getString("CNACPL").equals(cn)) cn = "";

      DataSet arts = getArtikliSet();
      
      /*      
       //qdsRight.first();
       String str=
          "select max("+tabledet+".cart) as cart, max("+tabledet+".cart1) as cart1, "+
          "max("+tabledet+".bc) as bc, max("+tabledet+".jm) as jm, "+
          "max("+tabledet+".nazart) as nazart, sum("+tabledet+".kol) as kol, "+
          "sum("+tabledet+".iznos) as isp, "+
          "sum("+tabledet+".ipopust1)+sum("+tabledet+".ipopust2) as uirab, "+
          "((sum("+tabledet+".ipopust1)+sum("+tabledet+".ipopust2))/sum("+tabledet+".iznos))*100 as uprab, "+
          "(sum("+tabledet+".iznos)-(sum("+tabledet+".por1)+sum("+tabledet+".por2)+sum("+tabledet+".por3)))/sum("+tabledet+".kol) as fc, "+
          "(sum("+tabledet+".iznos)-(sum("+tabledet+".por1)+sum("+tabledet+".por2)+sum("+tabledet+".por3))) as ineto, "+
          "(sum("+tabledet+".neto)-(sum("+tabledet+".por1)+sum("+tabledet+".por2)+sum("+tabledet+".por3)))/sum("+tabledet+".kol) as fvc, "+
          "(sum("+tabledet+".neto)-(sum("+tabledet+".por1)+sum("+tabledet+".por2)+sum("+tabledet+".por3))) as iprodbp, "+
          "sum("+tabledet+".por1) as por1, sum("+tabledet+".por2) as por2, sum("+tabledet+".por3) as por3, "+
          "sum("+tabledet+".neto)/sum("+tabledet+".kol) as fmc, "+
          "max("+tabledet+".mc) as mc, "+
          "sum("+tabledet+".neto) as iprodsp, "+
          "max("+tabledet+".ppor1) as ppor1, "+
          "max("+tabledet+".ppor2) as ppor2, "+
          "max("+tabledet+".ppor3) as ppor3, "+
          "max("+tabledet+".cskl) as cskl, "+
          "max(artikli.vrart) as vrart "+
          "from "+tabledet+", "+table+",artikli where "
		  +tabledet+".cart=artikli.cart and "
          +tabledet+".cskl="+table+".cskl and "
          +tabledet+".vrdok="+table+".vrdok and "
          +tabledet+".brdok="+table+".brdok and "
          +tabledet+".god="+table+".god and "
          +table+".status='N' and ";
      
        

      str = str + doctype.and(brdoks).qualified(table) +
          " group by artikli.cart having sum("+tabledet+".kol) <> 0";
      System.out.println("sql: "+str);
      vl.execSQL(str);
      DataSet arts = vl.getDataAndClear();*/
      
      if (!arh) {
      	lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL",	qdsRight.getString("CSKL"));
      	DataSet samec = Sklad.getDataModule().getTempSet(Condition.equal("CORG", dm.getSklad()));
      	Condition sc = Condition.in("CSKL", samec);
      	
        stanje = Stanje.getDataModule().getTempSet(Condition.equal("GOD", qdsRight).and(sc).and(Condition.in("CART", arts)));
        stanje.open();
        System.out.println(stanje.getOriginalQueryString());
      }
      obrada();
      
      int br = 1;
      for (arts.first(); arts.inBounds(); arts.next())
        if (arts.getBigDecimal("KOL").signum() != 0) {
          ukupno=ukupno.add(arts.getBigDecimal("IPRODSP"));
          obradaSt(br++, arts);
        }
      if (errnum > 5) 
        errors = errors + "\n ( ... još " + 
             Aus.getNum(errnum - 5, "greška", "greške", "grešaka") + " ... )";
      master.setBigDecimal("UIRAC", ukupno);
      master.setString("CNACPL", cn);
      adisab(true);
      getTTC().getRightJPTW().enableEvents(true);
      //this.hide();
      return;
    }
    KreirDrop kdp = vrsta.equals("GRC") ? 
        (KreirDrop) Pos.getDataModule() : 
        (KreirDrop) doki.getDataModule();
        
    Condition cskcond = !arh ? Condition.equal("CSKL", tds) :
      Condition.equal("CSKL", "#" + tds.getString("CSKL"));

    qdsLeft = kdp.getTempSet("cskl god vrdok brdok datdok cnacpl uirac",
        Condition.equal("STATUS", "N").
        and(cskcond).
        and(Condition.equal("VRDOK", vrsta)).
        and(Condition.till("DATDOK", tds)).
        and(Condition.from("DATDOK", 
            ut.getFirstDayOfYear(tds.getTimestamp("DATDOK")))));

    qdsLeft.open();
    if (qdsLeft.hasColumn("CPRODMJ") != null)
      qdsLeft.hasColumn("CPRODMJ").setVisible(0);

    qdsRight = new QueryDataSet();
    qdsRight.setLocale(Aus.hr);
    qdsRight.setColumns(qdsLeft.cloneColumns());
    qdsRight.open();
    
    dontClose = true;
//      JOptionPane.showConfirmDialog(this,"Nema podataka za prijenos !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    if (qdsLeft.getRowCount()>0) {
      //adisab(false);
      qdsLeft.setTableName(vrsta);
      qdsRight.setTableName(vrsta);
      
    }
//    rcc.setLabelLaF(jpUpit,false);
//    getTTC().requestFocus();
  }
  protected void upitCompleted() {
    if (dontClose) {
      if (qdsLeft.getRowCount()>0) {
        getTTC().setLeftDataSet(qdsLeft);
        getTTC().setRightDataSet(qdsRight);
        getTTC().initialize();
        getTTC().getLeftJPTW().getMpTable().requestFocus();
      } else {
        JOptionPane.showMessageDialog(this, "Nema stavaka za prijenos!",
            "Greška", JOptionPane.ERROR_MESSAGE);
        adisab(true);
        jtfDATUM.requestFocusLater();
      }
    }
  }
  
  protected DataSet getArtikliSet() {
    VarStr q = new VarStr(
        "SELECT d.cart, d.cart1, d.bc, d.jm, d.nazart, " +
        "d.kol, d.rezkol, d.ipopust1+d.ipopust2 as uirab, " +
        "(d.ipopust1+d.ipopust2)/d.ukupno*100 as uprab, " +
        "(d.iznos-d.por1-d.por2-d.por3)/d.kol as fc, " +
        "(d.iznos-d.por1-d.por2-d.por3) as ineto, " +
        "(d.neto-d.por1-d.por2-d.por3)/d.kol as fvc, " +
        "(d.neto-d.por1-d.por2-d.por3) as iprodbp, " +
        "d.por1, d.por2, d.por3, d.neto/d.kol as fmc, d.mc, " +
        "d.neto as iprodsp, d.ppor1, d.ppor2, d.ppor3, " +
        "d.cskl from %m m, %d d WHERE " + util.getDoc("m", "d") +
        " AND m.status='N' AND d.iznos!=0 AND d.kol!=0 AND "
    );
    q.replaceAll("%d", tabledet).replaceAll("%m", table);
    
    q.append(doctype.and(brdoks).qualified("m"));
    System.out.println("sql: "+q);
    
    String[] cols = {"CART", "CART1", "BC", "NAZART", "JM", "KOL", 
        "REZKOL", "UIRAB", "UPRAB", "FC", "INETO", "FVC", 
        "IPRODBP", "POR1", "POR2", "POR3", "FMC", "MC", 
        "IPRODSP", "PPOR1", "PPOR2", "PPOR3", "CSKL", "BRDOK"};
    String[] sumc = {"KOL", "UIRAB", "INETO", "IPRODBP", 
          "POR1", "POR2", "POR3", "IPRODSP"};
    StorageDataSet inter = stdoki.getDataModule().getScopedSet(cols);       
    ut.fillReadonlyData(inter, q.toString());
    inter.setSort(new SortDescriptor(
        new String[] {"CART", "REZKOL", "UPRAB", "BRDOK"}));
    
    StorageDataSet group = stdoki.getDataModule().getScopedSet(cols);
    group.open();
    
    int cart = -999;
    String rezkol = "";
    BigDecimal uprab = Aus.zero2;
    for (inter.first(); inter.inBounds(); inter.next()) {
      if (inter.getInt("CART") != cart || 
          !inter.getString("REZKOL").equals(rezkol) ||
          inter.getBigDecimal("UPRAB").compareTo(uprab) != 0) {
        group.insertRow(false);
        dM.copyColumns(inter, group, cols);
        cart = inter.getInt("CART");
        rezkol = inter.getString("REZKOL");
        uprab = inter.getBigDecimal("UPRAB");
      } else {
        for (int i = 0; i < sumc.length; i++)
          Aus.add(group, sumc[i], inter, sumc[i]);
      }
    }
    group.setSort(new SortDescriptor(new String[] {"BRDOK"}));
    return group;
  }
  
  public void firstESC() {
    getTTC().setLeftDataSet(null);
    getTTC().setRightDataSet(null);
    getTTC().initialize();
//    rcc.setLabelLaF(jpUpit,true);
    adisab(true);
    jtfDATUM.requestFocusLater();
  }
  public boolean runFirstESC() {
    if (getTTC().getLeftDataSet()!=null) {
      return true;
    }
    return false;

  }
  private static boolean bCancelPress=false;
  public boolean isCancelPress(){
    return bCancelPress;
  }
  public void cancelPress(){
    bCancelPress=true;
    super.cancelPress();
  }


  private void adisab(boolean istina) {
    rcc.setLabelLaF(this.jtfDATUM, istina);
    rcc.setLabelLaF(this.jrfCSKL, istina);
    rcc.setLabelLaF(this.jrfNAZSKL, istina);
    rcc.setLabelLaF(this.jtfVRDOK, false);
  }
  public void obrada() {
//    master.insertRow(false);
//    master.setString("CSKL", presel.getString("CSKL"));
//    master.setString("VRDOK", presel.getString("VRDOK"));
//    master.setTimestamp("DATDOK",presel.getTimestamp("DATDOK-to"));
//    master.setTimestamp("DVO",presel.getTimestamp("DATDOK-to"));
//    master.setTimestamp("DATDOSP",presel.getTimestamp("DATDOK-to"));
//    master.setString("GOD", val.findYear(master.getTimestamp("DATDOK")));
//    master.setString("CUSER",hr.restart.sisfun.raUser.getInstance().getUser());
    master.setInt("BRDOK", (val.findSeqInteger(util.getSeqString(master),false)).intValue());
    if (cag) master.setInt("CAGENT", rep ? 2 : 1);
    System.out.println("UIRAC: "+ukupno);
    master.setBigDecimal("UIRAC", ukupno);
    if (arh) master.setString("STATKNJ", "K");
    masterkey = raControlDocs.getKey(master);
//    master.post();
//    master.saveChanges();
  }

  public boolean kalkSkladAndStanje() {

    if (lookupData.getlookupData().raLocate(stanje,"CART",
        Integer.toString(detail.getInt("CART")))) {

      rKD.setVrzal(detail.getString("CSKL"));
      rKD.stanje.Init();
      rKD.stavka.Init();
      rKD.stavkaold.Init();
      lc.TransferFromDB2Class(detail,rKD.stavka);
      lc.TransferFromDB2Class(stanje,rKD.stanje);

      rKD.pureKalkSkladPart();
      System.out.println("rKD.testStanje(): "+rKD.TestStanje());
      if (rKD.TestStanje()<0) {
        JOptionPane.showConfirmDialog(null,"Nema podataka na stanju !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        isEverythingOK = false;
      }
      rKD.KalkulacijaStanje(detail.getString("VRDOK"));
      lc.TransferFromClass2DB(detail,rKD.stavka);
      lc.TransferFromClass2DB(stanje,rKD.stanje);

    } else {
      // ako nema stanja inicjalizira skladišnu stranu
      System.out.println("Jebote, nema sranja");
      lc.TransferFromDB2Class(detail,rKD.stavka);
      rKD.kalkResetSkladPart();
      rKD.stavka.Init();
      lc.TransferFromClass2DB(detail,rKD.stavka);
    }
    return true;

  }

  private void addError(String opis, DataSet qds, BigDecimal kols) {
    errorSet.insertRow(false);
    dM.copyColumns(qds, errorSet, 
        new String[] {"CART", "CART1", "BC", "NAZART", "KOL"});
    errorSet.setString("OPIS", opis);
    errorSet.setBigDecimal("KOLS", kols);
    errorSet.post();
  }

  public void obradaSt(int rbr, DataSet qds) {

    String[] cols = {"CART", "CART1", "BC", "NAZART", "JM", "KOL", 
        "REZKOL", "UIRAB", "UPRAB", "FC", "INETO", "FVC", 
        "IPRODBP", "POR1", "POR2", "POR3", "FMC", "MC", 
        "IPRODSP", "PPOR1", "PPOR2", "PPOR3"};
  	detail.insertRow(false);
  	dM.copyColumns(qds, detail, cols);
    detail.setString("CSKL", master.getString("CSKL"));
    detail.setString("VRDOK", master.getString("VRDOK"));
    detail.setString("GOD", master.getString("GOD"));
    detail.setInt("BRDOK", master.getInt("BRDOK"));
    detail.setShort("RBR", (short)rbr);
    /*detail.setInt("CART", qds.getInt("CART"));
    detail.setString("CART1", qds.getString("CART1"));
    detail.setString("BC", qds.getString("BC"));
    detail.setString("NAZART", qds.getString("NAZART"));
    detail.setString("JM", qds.getString("JM"));
    detail.setString("REZKOL", qds.getString("REZKOL"));
    detail.setBigDecimal("KOL", qds.getBigDecimal("KOL"));
    detail.setBigDecimal("UPRAB", new java.math.BigDecimal(qds.getDouble("UPRAB")));
    detail.setBigDecimal("UIRAB", new java.math.BigDecimal(qds.getDouble("UIRAB")));
    detail.setBigDecimal("FC", new java.math.BigDecimal(qds.getDouble("FC")));
    detail.setBigDecimal("INETO", new java.math.BigDecimal(qds.getDouble("INETO")));
    detail.setBigDecimal("FVC", new java.math.BigDecimal(qds.getDouble("FVC")));
    detail.setBigDecimal("IPRODBP", new java.math.BigDecimal(qds.getDouble("IPRODBP")));
    detail.setBigDecimal("POR1", qds.getBigDecimal("POR1"));
    detail.setBigDecimal("POR2", qds.getBigDecimal("POR2"));
    detail.setBigDecimal("POR3", qds.getBigDecimal("POR3"));
    detail.setBigDecimal("PPOR1", qds.getBigDecimal("PPOR1"));
    detail.setBigDecimal("PPOR2", qds.getBigDecimal("PPOR2"));
    detail.setBigDecimal("PPOR3", qds.getBigDecimal("PPOR3"));
    detail.setBigDecimal("FMC", new java.math.BigDecimal(qds.getDouble("FMC")));
    detail.setBigDecimal("MC", qds.getBigDecimal("MC"));
    detail.setBigDecimal("IPRODSP", qds.getBigDecimal("IPRODSP"));*/
    detail.setString("CSKLART", qds.getString("CSKL"));
    detail.setInt("RBSID", rbr);
    detail.setString("ID_STAVKA",
        raControlDocs.getKey(detail, new String[] { "cskl",
                "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
    detail.post();
    if (arh) return;
    if (raVart.isStanje(qds.getInt("CART")) && (!raVart.isNorma(qds.getInt("CART")) 
         || !lookupData.getlookupData().raLocate(dm.getSortedNorme(), 
             "CARTNOR", Integer.toString(qds.getInt("CART"))))) {
        //System.out.println("Vrsta: "+qds.getString("VRART"));
    	if (lookupData.getlookupData().raLocate(stanje, "CART",
    	    Integer.toString(qds.getInt("CART")))) {
    		if (stanje.getBigDecimal("KOL").compareTo(qds.getBigDecimal("KOL")) < 0) {
    		    addError("Nedovoljna kolièina na skladištu " + 
    		        qds.getString("CSKL"), qds, 
    		        stanje.getBigDecimal("KOL"));
    		    
    			if (errors == null) {
    				errors = "Greška kod prijenosa artikla!";
    			}
    			if (++errnum <= 5)
    			errors = errors + "\nArtikl " + stanje.getInt("CART")
    						  + " (" + qds.getString("NAZART")
    						  + ")\n-  kolièina na skladištu "
    						  + qds.getString("CSKL")	+ ": "
    						  + stanje.getBigDecimal("KOL")
    						  + "\n-  kolièina za razdužiti: "
    						  + qds.getBigDecimal("KOL");						
    			//System.out.println("Errors: "+errors);
    		}
    		detail.setString("CSKLART", stanje.getString("CSKL"));
    	}
    	else {
    	     
    	    addError("Nema zapisa na skladištu " + qds.getString("CSKL"),
              qds, Aus.zero3);
    	  
    		if (errors == null) {
    			errors = "Greška kod prijenosa artikla!";
    		}
    		if (++errnum <= 5)
    		errors = errors + "\nArtikl " + qds.getInt("CART")
						  + " (" + qds.getString("NAZART")
						  + ")\n-  nema zapisa na skladištu "
						  + qds.getString("CSKL")
						  + "\n-  kolièina za razdužiti: "
						  + qds.getBigDecimal("KOL");					
    		//System.out.println("Errors: "+errors);
    	
    	}
    }

/*    detail.setBigDecimal("NC", dm.getStanje().getBigDecimal("NC"));
    detail.setBigDecimal("INAB", util.multiValue(dm.getStanje().getBigDecimal("NC"), qds.getBigDecimal("KOL")));
    detail.setBigDecimal("VC", dm.getStanje().getBigDecimal("VC"));
    detail.setBigDecimal("IBP", util.multiValue(dm.getStanje().getBigDecimal("VC"), qds.getBigDecimal("KOL")));
    detail.setBigDecimal("IMAR", util.negateValue(detail.getBigDecimal("IBP"), detail.getBigDecimal("INAB")));
    detail.setBigDecimal("MC", dm.getStanje().getBigDecimal("MC"));
    detail.setBigDecimal("ISP", util.multiValue(dm.getStanje().getBigDecimal("MC"), qds.getBigDecimal("KOL")));
    detail.setBigDecimal("IPOR", util.negateValue(detail.getBigDecimal("ISP"), detail.getBigDecimal("IBP")));
    detail.setBigDecimal("ZC", dm.getStanje().getBigDecimal("ZC"));
    detail.setBigDecimal("IRAZ", util.multiValue(dm.getStanje().getBigDecimal("ZC"), qds.getBigDecimal("KOL")));
    */
    //    kalkSkladAndStanje();
    
//    detail.saveChanges();
//    dm.getStanje().saveChanges();    // ovo Spliniša pukni u transakšn
  }
  public void afterOKPress() {
    if (dontClose) dontClose = false;
    else this.hide();
//    JOptionPane.showConfirmDialog(null,"Nema podataka za prijenos !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
  }

  public void saveAll() {

    new raLocalTransaction() {
      public boolean transaction() throws Exception {
        sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
        
        System.out.println("---------------------------------------");
        st.prn(master);
        System.out.println("---------------------------------------");
        st.prn(detail);
        System.out.println("---------------------------------------");
        master.setString("PNBZ2",
            raPozivNaBroj.getraPozivNaBrojClass().getPozivNaBroj(master));
        
      	raTransaction.saveChanges(master);
      	raTransaction.saveChanges(detail);
      	if (!arh) raTransaction.saveChanges(stanje);
      	raTransaction.saveChanges(dm.getSeq());
      	if (raDocTwoTableChooser.this instanceof frmPos2POS)
      	  raTransaction.runSQL("update "+table+
            " set status='P', rdok='" + masterkey +
            "' where "+ doctype.and(brdoks).qualified(table));
      	else raTransaction.runSQL("update "+table+
      	    " set status='P' where "+ doctype.and(brdoks).qualified(table));
      	return true;
      }
    }.execTransaction();
  }
}

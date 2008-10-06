/****license*****************************************************************
**   file: upKPR.java
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
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Date;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class upKPR extends raUpit {

  static upKPR upk;
//  private sysoutTEST ST = new sysoutTEST(false);
  public static upKPR getInstance() {
    if (upk == null) {
      upk = new upKPR();
    }
    return upk;
  }
  BorderLayout borderLayout1 = new BorderLayout();
  dM dm = dM.getDataModule();
  Timestamp date;
  Date dateP = null;
  Date dateZ = null;

  JLabel jLabel1 = new JLabel();
  JPanel jPanel3 = new JPanel();
  JLabel jlRbr = new JLabel();
  JPanel jp = new JPanel();
  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfRbr = new JraTextField();
  JraTextField jtfZavDatum = new JraTextField();
  hr.restart.robno._Main main;
  QueryDataSet qdsKPR ;
  QueryDataSet qdsDonos;
  raCommonClass rcc = raCommonClass.getraCommonClass();
  double initZAD = 0.0;
  double initRAZ = 0.0;

  hr.restart.robno.rapancskl rpcskl = new hr.restart.robno.rapancskl() {
    public void findFocusAfter() {
      jtfPocDatum.requestFocus();
  }};

  TableDataSet tds = new TableDataSet();
  java.math.BigDecimal tempRAZ = main.nul;
  java.math.BigDecimal tempZAD = main.nul;
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  Valid vl;
  XYLayout xYLayout1 = new XYLayout();

  private void myInit() {
    Column sumRAZ = new Column();
    Column sumZAD = new Column();
    Column colCSklIz = dM.createStringColumn("CSKLIZ", 12);
    Column colCSklUl = dM.createStringColumn("CSKLUL", 12);
    Column colND = new Column();
    Column colNSKL = new Column();
    Column colRBR = dM.createShortColumn("RBR", "Rbr");
    colRBR.setCaption("Rbr");
    Column colSKL = new Column();
    sumZAD.setColumnName("SUMZAD");
    sumZAD.setDataType(Variant.BIGDECIMAL);
    sumZAD.setDisplayMask("###,###,##0.00");
    sumZAD.setDefault("0");
    sumZAD.setResolvable(false);
    sumZAD.setSqlType(0);
    sumZAD.setCaption("Iznos zaduženja");
    sumRAZ.setColumnName("SUMRAZ");
    sumRAZ.setDataType(Variant.BIGDECIMAL);
    sumRAZ.setDisplayMask("###,###,##0.00");
    sumRAZ.setDefault("0");
    sumRAZ.setResolvable(false);
    sumRAZ.setSqlType(0);
    sumRAZ.setCaption("Iznos razduženja");
    colND.setColumnName("KOLND");
    colND.setDataType(Variant.STRING);
    colND.setDefault("");
    colND.setResolvable(false);
    colND.setSqlType(0);
    colND.setCaption("Opis");
    colND.setWidth(30);
    colSKL.setColumnName("KOLSK");
    colSKL.setDataType(Variant.STRING);
    colSKL.setDefault("");
    colSKL.setResolvable(false);
    colSKL.setSqlType(0);
    colSKL.setCaption("ŠS");
    colNSKL.setColumnName("KOLNSK");
    colNSKL.setDataType(Variant.STRING);
    colNSKL.setDefault("");
    colNSKL.setResolvable(false);
    colNSKL.setSqlType(0);
    colNSKL.setCaption("NS");
    qdsKPR = new QueryDataSet();
    qdsKPR.setColumns(new Column[] {
      (Column) colCSklUl.clone(),
      (Column) colCSklIz.clone(),
      (Column) colRBR.clone(),
      (Column) dm.getDoku().getColumn("DATDOK").clone(),
      (Column) dm.getDoku().getColumn("VRDOK").clone(),
      (Column) colND.clone(),
      (Column) dm.getDoku().getColumn("BRDOK").clone(),
      (Column) sumZAD.clone(),
      (Column) sumRAZ.clone(),
      (Column) colSKL.clone(),
      (Column) colNSKL.clone(),
      dm.createIntColumn("MINBRAC"),
      dm.createIntColumn("MAXBRAC")
    });
    qdsKPR.getColumn("MINBRAC").setVisible(0);
    qdsKPR.getColumn("MAXBRAC").setVisible(0);
    qdsDonos = new QueryDataSet();
    qdsDonos.setColumns(qdsKPR.cloneColumns());
  }
//*** konstruktor

  public upKPR() {
    try {
      myInit();
      jbInit();
      upk = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean Validacija(){
    if (rpcskl.getCSKL().equals("")){
      rpcskl.jrfCSKL.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Obavezan unos skladišta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (!Aus.checkDateRange(jtfPocDatum, jtfZavDatum)) return false;
    return true;
  }

  public void okPress() {
  // pridruzivanje datuma iz TableDataSet-a
    dateP = new Date(this.tds.getTimestamp("pocDatum").getTime());
    dateZ = new Date(this.tds.getTimestamp("zavDatum").getTime());
  // Disable komponenti na jp panelu
    rcc.EnabDisabAll(jp,false);
    getJPTV().setKumTak(true);
    getJPTV().setStoZbrojiti(new String[] {"SUMZAD", "SUMRAZ"});
  // rezult set za donos -> dohvat iz baze
    String qStr="";
    if(qdsKPR.isOpen())qdsKPR.close();
    qStr = rdUtil.getUtil()./*getKPRUlSql(rpcskl.getCSKL(),
                                        util.getTimestampValue(hr.restart.util.Util.getUtil().getFirstDayOfYear(tds.getTimestamp("pocDatum")), 0),
                                        util.getTimestampValue(tds.getTimestamp("pocDatum"), 0),
                                        frmParam.getParam("robno","indKPR"));*/

                                         getKPRDonosSql(rpcskl.getCSKL(),
                                                         util.getTimestampValue(tds.getTimestamp("pocDatum"), 0),
                                                         frmParam.getParam("robno","indKPR"));

//    System.out.println("QUERY KPR DONOS : " + qStr);


    qdsDonos.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    qdsDonos.setSort(new SortDescriptor(new String[]{"DATDOK"}));
    qdsDonos.open();
    tempZAD = main.nul;
    tempRAZ = main.nul;
    for (qdsDonos.first();qdsDonos.inBounds();qdsDonos.next()) {
      tempZAD = tempZAD.add( qdsDonos.getBigDecimal("SUMZAD"));
      tempRAZ = tempRAZ.add(qdsDonos.getBigDecimal("SUMRAZ"));
      date = qdsDonos.getTimestamp("DATDOK");
    }
    qdsDonos.close();

    if(qdsKPR.isOpen()) qdsKPR.close();

//    qdsKPR.open();
//    qdsKPR.deleteAllRows();

//    System.out.println("kveriDrugiUP(rpcskl.getCSKL())\n"+kveriDrugiUP(rpcskl.getCSKL()));
//
//    QueryDataSet q3 = hr.restart.util.Util.getUtil().getNewQueryDataSet(kveriPrviU(rpcskl.getCSKL()));
//    makeMainSet(sumGOT(q3));
//    QueryDataSet q4 = hr.restart.util.Util.getUtil().getNewQueryDataSet(kveriDrugiUP(rpcskl.getCSKL()));
//    makeMainSet(sumGOT(q4));
//    QueryDataSet q5 = hr.restart.util.Util.getUtil().getNewQueryDataSet(kveriTreciU(rpcskl.getCSKL()));
//    makeMainSet(sumGOT(q5));
//    QueryDataSet q6 = hr.restart.util.Util.getUtil().getNewQueryDataSet(kveriCetvrtiP(rpcskl.getCSKL()));
//    makeMainSet(sumGOT(q6));
//    QueryDataSet q7 = hr.restart.util.Util.getUtil().getNewQueryDataSet(kveriSedmiI(rpcskl.getCSKL(), frmParam.getParam("robno","indKPR")));
//    makeMainSet(sumGOT(q7));
//    QueryDataSet q8 = hr.restart.util.Util.getUtil().getNewQueryDataSet(kveriOsmiROT(rpcskl.getCSKL(), frmParam.getParam("robno","indKPR")));
//    makeMainSet(sumGOT(q8));

//    QueryDataSet q2 = hr.restart.util.Util.getUtil().getNewQueryDataSet(kveriSestiP(rpcskl.getCSKL(), frmParam.getParam("robno","indKPR")));
//    //----------------------------------------------------------------------
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(q2);
//    //----------------------------------------------------------------------
//    makeMainSet(sumGOT(q2));
//    QueryDataSet q1 = hr.restart.util.Util.getUtil().getNewQueryDataSet(kveriPetiI(rpcskl.getCSKL(), frmParam.getParam("robno","indKPR")));
//    makeMainSet(sumGOT(q1));
//
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(sumGOT(q1));
//    syst.prn(sumGOT(q2));
//
    qStr = rdUtil.getUtil().getKPRUlSql(rpcskl.getCSKL(),
                                        util.getTimestampValue(tds.getTimestamp("pocDatum"), 0),
                                        util.getTimestampValue(tds.getTimestamp("zavDatum"), 1),
                                        frmParam.getParam("robno","indKPR"));
//
    System.out.println("QUERY KPR : " + qStr);
//
    qdsKPR.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));



    qdsKPR.setSort(new SortDescriptor(new String[]{"DATDOK"}));

    qdsKPR.open();
    qdsKPR.first();

    qdsKPR.getColumn("KOLSK").setVisible(0);
    qdsKPR.getColumn("KOLNSK").setVisible(0);
    qdsKPR.getColumn("CSKLUL").setVisible(0);
    qdsKPR.getColumn("CSKLIZ").setVisible(0);
    qdsKPR.getColumn("VRDOK").setVisible(0);
    qdsKPR.getColumn("BRDOK").setVisible(0);


    if (qdsKPR.rowCount()>0) {
//      do {
//        qdsKPR.setTimestamp("DATDOK", getTocnoUPodne(qdsKPR.getTimestamp("DATDOK")));
//      } while (qdsKPR.next());
//      qdsKPR.first();

      /** @todo ovo tu doli malo ispod - mozda*/

//      if (frmParam.getFrmParam().getParam("robno","GotKPRsum","N","Sumiranje GOT raèuna po danima").equals("D")){
//        qdsKPR = sumGOT(q1);
//      }
      nazivDok();
      qdsKPR = rangeRBR(qdsKPR);
      if(qdsKPR.getRowCount()==0) {
        tds.setShort("docRBR", (short)1);
        rcc.EnabDisabAll(this.jPanel3, true);
        this.jtfPocDatum.requestFocus();
        status = REDNIBROJ;
//        JOptionPane.showConfirmDialog(this.jp,"Poèetni redni broj veæi od broja stavaka !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
        return ;
      }

      this.getJPTV().setDataSetAndSums(qdsKPR, new String[] {"SUMZAD","SUMRAZ"});//.setDataSet(qdsKPR);
//      this.getJPTV().getMpTable().requestFocus();
      initZAD = 0.0;
      initRAZ = 0.0;

   //-> prikaz donosa?
      insertDonos();
      tempZAD = main.nul;
      tempRAZ = main.nul;
    } else {
   //-> postoji li dohvat bez obzira na rezultate za zadane datume?
      if (insertDonos()){
        this.getJPTV().setDataSetAndSums(qdsKPR, new String[] {"SUMZAD","SUMRAZ"});
//        this.getJPTV().getMpTable().requestFocus();
      } else {
        status = NEMAPODATAKA;
//        JOptionPane.showConfirmDialog(this.jp,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
   //        firstESC();
        return ;
      }
    }
    if(qdsKPR.getRowCount()==0){
      status = NEMAPODATAKA;
//      JOptionPane.showConfirmDialog(this.jp,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
   //      firstESC();
      return ;
    }

    status = SVEUREDU;
  }

  private int status;

  private final int NEMAPODATAKA = 0;
  private final int REDNIBROJ = 1;
  private final int SVEUREDU = 3;

  public void afterOKPress(){
    if (status != SVEUREDU){
      if (status == NEMAPODATAKA) JOptionPane.showConfirmDialog(this,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      else if (status == REDNIBROJ) JOptionPane.showConfirmDialog(this,"Poèetni redni broj veæi od broja stavaka !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      firstESC();
      return;
    }
    this.requestFocus();
  }

  public void componentShow() {
    showDefaultValues();
    String skl = raUser.getInstance().getDefSklad();
    boolean sklPostoji = false;
    QueryDataSet qdsS = new QueryDataSet();

    qdsS = hr.restart.robno.Util.getUtil().getMPSklDataset();
    qdsS.open();
    qdsS.first();
    while (qdsS.inBounds()) {
      if (skl.equals(qdsS.getString("CSKL"))) {
        sklPostoji = true;
      }
      qdsS.next();
    }
    if (sklPostoji) {
      rpcskl.setCSKL(skl);
      this.jtfPocDatum.requestFocus();
    } else {
      rpcskl.jrfCSKL.requestFocus();
    }
  }

//***************************************************************************
  public void findFocusAfter() {}

  public void firstESC() {
//    System.out.println("first esc...");
    if (this.getJPTV().getDataSet() == null) {
      rcc.EnabDisabAll(this.rpcskl, true);
      rcc.EnabDisabAll(this.jPanel3, true);
      rpcskl.setCSKL("");
    } else {
      this.getJPTV().setDataSet(null);
      tds.setShort("docRBR", (short) 1);
      rcc.EnabDisabAll(this.jPanel3, true);
      this.jtfPocDatum.requestFocus();
    }
  }

  public String getCskl() {
    return rpcskl.getCSKL();
  }

  public DataSet getQds() {
    return qdsKPR; //getJPTV().getMpTable().getDataSet();
  }

//*** Ubacivanje izracunatog donosa u postojeci rezult set / ako postoji vraca true /
  boolean insertDonos() {
    qdsKPR.setRowId("VRDOK", true);
//    System.out.println("tempZAD.toString()='"+tempZAD.toString()+"'");
//    System.out.println("tempRAZ.toString()='"+tempRAZ.toString()+"'");
    if (!(tempZAD.compareTo(main.nul) == 0 && tempRAZ.compareTo(main.nul) == 0)){
//    if (!(tempZAD.toString().equals("0.00") && tempRAZ.toString().equals("0.00"))) {
      qdsKPR.insertRow(false);
      qdsKPR.dittoRow(true);
      qdsKPR.first();
      qdsKPR.clearValues();
      qdsKPR.setBigDecimal("SUMZAD", tempZAD);
      qdsKPR.setBigDecimal("SUMRAZ", tempRAZ);
      qdsKPR.setString("VRDOK", "DON");
      qdsKPR.setString("KOLND", "Donos  ");
      qdsKPR.setTimestamp("DATDOK", this.tds.getTimestamp("pocDatum")); //date);
      qdsKPR.setShort("RBR", (short) 0);
      qdsKPR.setSort(new SortDescriptor(new String[]{"RBR"}));
      qdsKPR.goToRow(0);
      initZAD = qdsKPR.getBigDecimal("SUMZAD").doubleValue();
      initRAZ = qdsKPR.getBigDecimal("SUMRAZ").doubleValue();
//      System.out.println("insert donos - true");
      return true;
    }
//      System.out.println("insert donos - false");
    return false;
  }

  private void jbInit() throws Exception {
//    this.addReport("hr.restart.robno.repKPR", "Knjiga popisa robe", 5);
    this.addReport("hr.restart.robno.repKPR","hr.restart.robno.repKPR", "KPR", "Knjiga popisa robe");
    rpcskl.jrfCSKL.setRaDataSet(hr.restart.robno.Util.getUtil().getMPSklDataset());
    setJPan(jp);
    jPanel3.setMinimumSize(new Dimension(604, 40));
    jPanel3.setPreferredSize(new Dimension(655, 58));
    jPanel3.setLayout(xYLayout1);
    jLabel1.setText("Datum (od-do)");
    jlRbr.setText("Po\u010Detni redni broj");
    vl = Valid.getValid();
    jp.setLayout(borderLayout1);
    jp.setMinimumSize(new Dimension(555, 88));
    jp.setPreferredSize(new Dimension(650, 105));
    rpcskl.setMinimumSize(new Dimension(10, 20));
    rpcskl.setPreferredSize(new Dimension(10, 20));
    xYLayout1.setWidth(655);
    xYLayout1.setHeight(110);
    jp.add(rpcskl, BorderLayout.CENTER);
    rpcskl.setRaMode('S');
    tds.setColumns(new Column[]{dM.createTimestampColumn("pocDatum"), dM.createTimestampColumn("zavDatum"),
        dM.createShortColumn("docRBR")});
    jtfZavDatum.setDataSet(tds);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setText("jraTextField2");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfRbr.setDataSet(tds);
    jtfRbr.setColumnName("docRBR");
    jtfRbr.setHorizontalAlignment(SwingConstants.RIGHT);
    jtfPocDatum.setDataSet(tds);
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setText("jraTextField1");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    new raDateRange(jtfPocDatum, jtfZavDatum);
    jPanel3.add(jLabel1, new XYConstraints(15, 5, -1, -1));
    jPanel3.add(jtfPocDatum, new XYConstraints(150, 4, 100, -1));
    jPanel3.add(jtfZavDatum, new XYConstraints(255, 4, 100, -1));
    jPanel3.add(jtfRbr, new XYConstraints(150, 30, 50, -1));
    jPanel3.add(jlRbr, new XYConstraints(15, 30, -1, -1));
    jp.add(jPanel3, BorderLayout.SOUTH);
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        rpcskl.jrfCSKL.setRaDataSet(hr.restart.robno.Util.getUtil().getMPSklDataset());
      }
    });
  }

//-> dodjela naziva vrsti dokumenta
  void nazivDok() {
    qdsKPR.first();
    qdsKPR.getColumn("VRDOK").setRowId(true);
    qdsKPR.getColumn("BRDOK").setRowId(true);
    do {
      qdsKPR.setShort("RBR", (short) (qdsKPR.getRow() + 1));
      qdsKPR.setString("KOLSK", rpcskl.jrfCSKL.getText());
      qdsKPR.setString("KOLNSK", rpcskl.jrfNAZSKL.getText());
      if (qdsKPR.getString("VRDOK").equals("ROT")) {
        qdsKPR.setString("KOLND", "Ra\u010Dun otpremnica br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("DON")) {
        qdsKPR.setString("KOLND", "Donos  ");
      } else if (qdsKPR.getString("VRDOK").equals("PRK")) {
        qdsKPR.setString("KOLND", "Primka - Kalkulacija br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("POR")) {
        qdsKPR.setString("KOLND", "Poravnanje br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("PST")) {
        qdsKPR.setString("KOLND", "Po\u010Detno stanje br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("GOT")) {
        qdsKPR.setString("KOLND", "Gotovinski ra\u010Dun br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("POS")) {
        qdsKPR.setString("KOLND", "Maloprodaja br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("IZD")) {
        qdsKPR.setString("KOLND", "Izdatnica br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("INV")) {
        qdsKPR.setString("KOLND", "Inventurni višak br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("INM")) {
        qdsKPR.setString("KOLND", "Inventurni manjak br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("OTR")) {
        qdsKPR.setString("KOLND", "Otpis robe br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("MEI")) {
        qdsKPR.setString("KOLND", "Me\u0111uskladišnica - izlaz br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("MEU")) {
        qdsKPR.setString("KOLND", "Me\u0111uskladišnica - ulaz br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("PDO")) {
        qdsKPR.setString("KOLND", "Povratnica dobavlja\u010Du br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("PKU")) {
        qdsKPR.setString("KOLND", "Povratnica kupcu br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("OTP")) {
        qdsKPR.setString("KOLND", "Otpremnica br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("PTE")) {
        qdsKPR.setString("KOLND", "Povratnica - tere\u0107enje br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("NAR")) {
        qdsKPR.setString("KOLND", "Narudžba br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("GRN")) {
        qdsKPR.setString("KOLND", "Gotovinski ra\u010Dun br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("TER")) {
        qdsKPR.setString("KOLND", "Tere\u0107enje br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("ODB")) {
        qdsKPR.setString("KOLND", "Odobrenje br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("MES")) {
        qdsKPR.setString("KOLND", "Me\u0111uskladišnica br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("POD")) {
        qdsKPR.setString("KOLND", "Povratnica - odobrenje br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("REV")) {
        qdsKPR.setString("KOLND", "Revers br. "+getKey());
      } else if (qdsKPR.getString("VRDOK").equals("PRV")) {
        qdsKPR.setString("KOLND", "Povratnica reversa br. "+getKey());

      } else if (qdsKPR.getString("VRDOK").equals("GSZ")) {
        qdsKPR.setString("KOLND", "Popusti gotovinskih raèuna");
      } else if (qdsKPR.getString("VRDOK").equals("GSR")) {
        qdsKPR.setString("KOLND", "Prometi gotovinskih raèuna");
      }

      qdsKPR.next();
    } while (qdsKPR.inBounds());

    qdsKPR.post();
  }

  private String getKey() {
    String forReturn= qdsKPR.getString("VRDOK").trim();
    if (!qdsKPR.getString("CSKLIZ").equalsIgnoreCase("")) {
      forReturn=forReturn+"-"+qdsKPR.getString("CSKLIZ").trim();
    }
    if (!qdsKPR.getString("CSKLUL").equalsIgnoreCase("")) {
      forReturn=forReturn+"-"+qdsKPR.getString("CSKLUL").trim();
    }
    forReturn=forReturn+"/"+
              Valid.getValid().findYear(qdsKPR.getTimestamp("DATDOK"))+"-"+
              qdsKPR.getInt("BRDOK");
    return forReturn;
  }

//  private String getKeyGOT(String tip) {
//    String dat = raDateUtil.getraDateUtil().dataFormatter(qdsKPR.getTimestamp("DATDOK"));
//    return null;
//  }

  private QueryDataSet rangeRBR(QueryDataSet qds) {
    if (!qds.isOpen()) {
      qds.open();
    }
    short brojac = tds.getShort("docRBR");
    for(qds.first();qds.inBounds();qds.next()) {
      if (qds.getString("VRDOK").equals("POR") &&
          qds.getBigDecimal("SUMZAD").compareTo(new java.math.BigDecimal("0.00")) == 0 &&
          qds.getBigDecimal("SUMRAZ").compareTo(new java.math.BigDecimal("0.00")) == 0){
      qds.deleteRow();
      qds.goToRow(qds.getRow()-1);
      continue;
    }
    qds.setShort("RBR",brojac);
    brojac ++;
    }
    qds.first();
    return qds;
  }

  private void makeMainSet(QueryDataSet qs){
    if (qs == null) return;
    qs.first();
    do {
      qdsKPR.insertRow(false);
      qdsKPR.setTimestamp("DATDOK", qs.getTimestamp("DATDOK"));
      qdsKPR.setString("VRDOK", qs.getString("VRDOK"));
      qdsKPR.setInt("BRDOK", qs.getInt("BRDOK"));
      try {
        qdsKPR.setBigDecimal("SUMZAD",qs.getBigDecimal("SUMZAD"));
      }
      catch (Exception ex) {
        qdsKPR.setBigDecimal("SUMZAD",new java.math.BigDecimal(qs.getDouble("SUMZAD")));
      }
      try {
        qdsKPR.setBigDecimal("SUMRAZ",qs.getBigDecimal("SUMRAZ"));
      }
      catch (Exception ex) {
        qdsKPR.setBigDecimal("SUMRAZ",new java.math.BigDecimal(qs.getDouble("SUMRAZ")));
      }
      try {
        qdsKPR.setInt("MINBRAC",qs.getInt("MINBRAC"));
        qdsKPR.setInt("MAXBRAC",qs.getInt("MAXBRAC"));
      }
      catch (Exception ex) {
        qdsKPR.setInt("MINBRAC",0);
        qdsKPR.setInt("MAXBRAC",0);
      }
    } while (qs.next());
  }

  private QueryDataSet sumGOT(QueryDataSet orginal){
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(orginal);
    if (orginal.rowCount() == 0) return null;
    QueryDataSet summed = new QueryDataSet();
    summed.setColumns(orginal.cloneColumns());
    summed.open();
    orginal.first();
    do {
      orginal.setTimestamp("DATDOK",getTocnoUPodne(orginal.getTimestamp("DATDOK")));
      if (!orginal.getString("VRDOK").equals("GOT")){
          if (!lookupData.getlookupData().raLocate(summed,new String[]{"VRDOK","BRDOK"},new String[]{orginal.getString("VRDOK"),orginal.getInt("BRDOK")+""})){
            summed.insertRow(false);
            orginal.copyTo(summed);
          } else {
            summed.setBigDecimal("SUMZAD",summed.getBigDecimal("SUMZAD").add(orginal.getBigDecimal("SUMZAD")));
            summed.setBigDecimal("SUMRAZ",summed.getBigDecimal("SUMRAZ").add(orginal.getBigDecimal("SUMRAZ")));
          }
      } else {
        System.out.println("got + " +orginal.getInt("BRDOK"));
        if (orginal.getBigDecimal("SUMRAZ").compareTo(new java.math.BigDecimal("0.00")) == 0){
          if (!lookupData.getlookupData().raLocate(summed,new String[]{"VRDOK","DATDOK"},new String[]{"GSZ",orginal.getTimestamp("DATDOK")+""})){
            summed.insertRow(false);
            orginal.copyTo(summed);
            summed.setString("VRDOK","GSZ");
          } else {
            summed.setDouble("SUMZAD", summed.getDouble("SUMZAD")+(orginal.getDouble("SUMZAD")));
//            summed.setBigDecimal("SUMZAD",summed.getBigDecimal("SUMZAD").add(orginal.getBigDecimal("SUMZAD")));
          }
        } else {
          if (!lookupData.getlookupData().raLocate(summed,new String[]{"VRDOK","DATDOK"},new String[]{"GSR",orginal.getTimestamp("DATDOK")+""})){
            summed.insertRow(false);
            orginal.copyTo(summed);
            summed.setString("VRDOK","GSR");
          } else {
//            summed.setDouble("SUMRAZ", summed.getDouble("SUMRAZ")+(orginal.getDouble("SUMRAZ")));
            summed.setBigDecimal("SUMRAZ",summed.getBigDecimal("SUMRAZ").add(orginal.getBigDecimal("SUMRAZ")));
          }
        }
      }
    } while (orginal.next());
    return summed;
  }

  public double getDonosZad(){
    return initZAD;
  }

  public double getDonosRaz(){
    return initRAZ;
  }

  public boolean runFirstESC() {
//    System.out.println("rpcskl.getCSKL() = prazno -->> " + rpcskl.getCSKL().equals(""));
    return !rpcskl.getCSKL().equals(""); // o kako li je jednostavno :))) by S.G.
    /*if (rpcskl.getCSKL().equals("")) {
      return false;
    } else {
      return true;
    }*/
  }

  void showDefaultValues() {
    tds.setTimestamp("pocDatum", hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(vl.findYear())));
    tds.setTimestamp("zavDatum", Valid.getValid().findDate(false, 0));
    tds.setShort("docRBR", (short) 1);
    jp.setPreferredSize(jp.getPreferredSize());
    this.getJPTV().setDataSet(null);
  }

  /////-----------------KVERIZ-----------------/////

  private String kveriPrviU(String cSkl){
    return "SELECT DOKU.CSKL as CSKLUL,'            ' as CSKLIZ, 0 as RBR, "+
        "DOKU.DATDOK AS DATDOK, STDOKU.VRDOK AS VRDOK, '' AS KOLND, STDOKU.BRDOK AS BRDOK, "+
        "STDOKU.IZAD AS SUMZAD, 0.00 AS SUMRAZ "+
        "from STDOKU, DOKU "+
        "where STDOKU.CSKL='"+cSkl+"' AND "+util.getDoc("DOKU", "STDOKU")+
        " AND DOKU."+range("DATDOK",tds.getTimestamp("pocDatum"),tds.getTimestamp("zavDatum"));
  }

  private String kveriDrugiUP(String cSkl){
    return "select DOKU.CSKL as CSKLUL,'            ' as CSKLIZ, 0 as RBR, DOKU.DATDOK AS DATDOK, 'POR' AS VRDOK, '' AS KOLND, "+
        "STDOKU.BRDOK AS BRDOK, STDOKU.PORAV AS SUMZAD, 0.00 AS SUMRAZ "+
        "from STDOKU,DOKU "+
        "where STDOKU.CSKL='"+cSkl+"' AND "+util.getDoc("DOKU", "STDOKU")+" AND (STDOKU.PORAV<>0 OR STDOKU.VRDOK IN ('POR')) "+ //AND STDOKU.VRDOK NOT IN ('POR') "+
        "AND DOKU."+range("DATDOK",tds.getTimestamp("pocDatum"),tds.getTimestamp("zavDatum"));
  }

  private String kveriTreciU(String cSkl){
    return "select MESKLA.CSKLUL as CSKLUL, MESKLA.CSKLIZ as CSKLIZ, 0 as RBR, MESKLA.DATDOK AS DATDOK, MESKLA.VRDOK AS VRDOK, "+
        "'' AS KOLND,  "+
        "MESKLA.BRDOK AS BRDOK, STMESKLA.ZADRAZUL AS SUMZAD, 0.00 AS SUMRAZ "+
        "from STMESKLA, MESKLA "+
        "where  STMESKLA.VRDOK = 'MEU' and STMESKLA.CSKLUL='"+cSkl+"' AND STMESKLA.CSKLUL=MESKLA.CSKLUL "+
        "AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND STMESKLA.GOD=MESKLA.GOD " +
        "AND STMESKLA.BRDOK=MESKLA.BRDOK AND STMESKLA.VRDOK = MESKLA.VRDOK " +
        "AND MESKLA."+range("DATDOK",tds.getTimestamp("pocDatum"),tds.getTimestamp("zavDatum"));
  }

  private String kveriCetvrtiP(String cSkl){
    return "select MESKLA.CSKLUL as CSKLUL, MESKLA.CSKLIZ as CSKLIZ, 0 as RBR, MESKLA.DATDOK AS DATDOK, 'POR' AS VRDOK, '' AS KOLND, "+
        "MESKLA.BRDOK AS BRDOK, STMESKLA.PORAV AS SUMZAD, 0.00 AS SUMRAZ "+
        "from STMESKLA,MESKLA "+
        "where STMESKLA.VRDOK = 'MEU' and STMESKLA.CSKLUL='"+cSkl+"' AND MESKLA.CSKLUL=STMESKLA.CSKLUL "+
        "AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ AND MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD "+
        "AND MESKLA.BRDOK=STMESKLA.BRDOK AND STMESKLA.PORAV<>0 "+
        "AND MESKLA."+range("DATDOK",tds.getTimestamp("pocDatum"),tds.getTimestamp("zavDatum"));
  }

  private String kveriPetiI(String cSkl, String parametar){
    String q = "";
    if (parametar.equals("N")) {
      System.out.println("N!!!");
    } else {
      q = "select '            ' as CSKLUL,DOKI.CSKL as CSKLIZ, 0 as RBR, DOKI.DATDOK AS DATDOK, "+
          "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, 0.00 AS SUMZAD, "+
          "STDOKI.IPRODSP AS SUMRAZ 0 as MINBRAC, 0 as MAXBRAC "+
          "from STDOKI, DOKI "+
          "where STDOKI.CSKL='"+cSkl+"' AND "+util.getDoc("DOKI", "STDOKI")+ dokumentiE("STDOKI")+
          " AND DOKI."+range("DATDOK",tds.getTimestamp("pocDatum"),tds.getTimestamp("zavDatum"));
    }
    return q;
  }

  private String kveriSestiP(String cSkl, String parametar){
    String q = "";
    if (parametar.equals("N")) {
      System.out.println("N!!!");
    } else {
      q = "select '            ' as CSKLUL, DOKI.CSKL as CSKLIZ, 0 as RBR, DOKI.DATDOK AS DATDOK, "+
          "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, (STDOKI.IPRODSP-STDOKI.IRAZ) AS SUMZAD, "+
          "0.00 AS SUMRAZ, 0 as MINBRAC, 0 as MAXBRAC "+
          "from STDOKI, DOKI "+
          "where (STDOKI.IPRODSP-STDOKI.IRAZ)<>0 and STDOKI.CSKL='"+cSkl+"' AND "+util.getDoc("DOKI", "STDOKI")+ dokumentiE("STDOKI")+
          " AND DOKI."+range("DATDOK",tds.getTimestamp("pocDatum"),tds.getTimestamp("zavDatum"));
    }
    return q;
  }

  private String kveriSedmiI(String cSkl, String parametar){
    String q = "";
    if (parametar.equals("N")) {
      System.out.println("N!!!");
    } else {
      q = "select MESKLA.CSKLUL as CSKLUL, MESKLA.CSKLIZ as CSKLIZ, 0 as RBR, MESKLA.DATDOK AS DATDOK, MESKLA.VRDOK AS VRDOK,  "+
          "'' AS KOLND, MESKLA.BRDOK AS BRDOK, -(STMESKLA.ZADRAZIZ) AS SUMZAD, 0.00 AS SUMRAZ "+
          "from STMESKLA, MESKLA "+
          "where  STMESKLA.VRDOK = 'MEI' and STMESKLA.CSKLIZ='"+cSkl+"' AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND STMESKLA.GOD=MESKLA.GOD " +
          " AND STMESKLA.BRDOK=MESKLA.BRDOK AND STMESKLA.VRDOK = MESKLA.VRDOK " +//dokumentiE("STMESKLA")+
          " AND MESKLA."+range("DATDOK",tds.getTimestamp("pocDatum"),tds.getTimestamp("zavDatum"));
    }
    return q;
  }

  private String kveriOsmiROT(String cSkl, String parametar){
    String q = "";
    if (parametar.equals("N")) {
      System.out.println("N!!!");
    } else {
      q = "select '            ' as CSKLUL,DOKI.CSKL as CSKLIZ, 0 as RBR, DOKI.DATDOK AS DATDOK, STDOKI.VRDOK AS VRDOK, "+
          "'' AS KOLND, STDOKI.BRDOK AS BRDOK,  -(STDOKI.ISP) AS SUMZAD, 0.00 AS SUMRAZ "+
          "from STDOKI, DOKI "+
          "where STDOKI.CSKL='"+cSkl+"' AND "+util.getDoc("DOKI", "STDOKI")+
          " AND STDOKI.VRDOK = 'ROT'" +
          " AND DOKI."+range("DATDOK",tds.getTimestamp("pocDatum"),tds.getTimestamp("zavDatum"));
    }
    return q;
  }

  private String range(String col,Timestamp pd,Timestamp zd){
    return Condition.between(col,pd,zd).toString();
  }

  /////-----------------KVERIZ-----------------/////

//      public String getKPRUlSql(String cSkl, String dateP, String dateZ, String parametar) {
//    //    System.out.println("datumi: " + dateP+" "+dateZ);
//        String qStr, izlazi;
//        izlazi = this.izlazi(cSkl, dateP, dateZ, parametar);
//
//        qStr="SELECT DOKU.CSKL as CSKLUL,'            ' as CSKLIZ, 0 as RBR, "+
//             "DOKU.DATDOK AS DATDOK, STDOKU.VRDOK AS VRDOK, '' AS KOLND, STDOKU.BRDOK AS BRDOK, "+
//             "STDOKU.IZAD AS SUMZAD, 0.00 AS SUMRAZ "+
//             "from STDOKU, DOKU "+
//             "where STDOKU.CSKL='"+cSkl+"' AND "+util.getDoc("DOKU", "STDOKU")+
//             " AND DOKU.DATDOK >= "+dateP+" AND DOKU.DATDOK <= "+dateZ+" "+
//             "UNION ALL "+
//    // Poravnanje
//        "select DOKU.CSKL as CSKLUL,'            ' as CSKLIZ, 0 as RBR, DOKU.DATDOK AS DATDOK, 'POR' AS VRDOK, '' AS KOLND, "+
//             "STDOKU.BRDOK AS BRDOK, STDOKU.PORAV AS SUMZAD, 0.00 AS SUMRAZ "+
//             "from STDOKU,DOKU "+
//             "where STDOKU.CSKL='"+cSkl+"' AND "+util.getDoc("DOKU", "STDOKU")+" AND (STDOKU.PORAV<>0 OR STDOKU.VRDOK IN ('POR')) "+ //AND STDOKU.VRDOK NOT IN ('POR') "+
//             "AND DOKU.DATDOK >= "+dateP+" AND DOKU.DATDOK <= "+dateZ+" "+
//             "UNION ALL "+
//    // Medjuskladisnice - ulaz
//        "select MESKLA.CSKLUL as CSKLUL, MESKLA.CSKLIZ as CSKLIZ, 0 as RBR, MESKLA.DATDOK AS DATDOK, MESKLA.VRDOK AS VRDOK, '' AS KOLND,  "+
//             "MESKLA.BRDOK AS BRDOK, STMESKLA.ZADRAZUL AS SUMZAD, 0.00 AS SUMRAZ "+
//             "from STMESKLA, MESKLA "+
//             "where  STMESKLA.VRDOK = 'MEU' and STMESKLA.CSKLUL='"+cSkl+"' AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND STMESKLA.GOD=MESKLA.GOD " +
//             " AND STMESKLA.BRDOK=MESKLA.BRDOK AND STMESKLA.VRDOK = MESKLA.VRDOK " +
//             "AND MESKLA.DATDOK >= "+dateP+" AND MESKLA.DATDOK <= "+dateZ+" " +
//             "UNION ALL "+
//    // Medjuskladisnice - Poravnanje
//        "select MESKLA.CSKLUL as CSKLUL, MESKLA.CSKLIZ as CSKLIZ, 0 as RBR, MESKLA.DATDOK AS DATDOK, 'POR' AS VRDOK, '' AS KOLND, "+
//             "MESKLA.BRDOK AS BRDOK, STMESKLA.PORAV AS SUMZAD, 0.00 AS SUMRAZ "+
//             "from STMESKLA,MESKLA "+
//             "where STMESKLA.VRDOK = 'MEU' and STMESKLA.CSKLUL='"+cSkl+"' AND MESKLA.CSKLUL=STMESKLA.CSKLUL AND MESKLA.CSKLIZ=STMESKLA.CSKLIZ AND MESKLA.VRDOK=STMESKLA.VRDOK AND MESKLA.GOD=STMESKLA.GOD AND MESKLA.BRDOK=STMESKLA.BRDOK AND STMESKLA.PORAV<>0 "+
//             "AND MESKLA.DATDOK >= "+dateP+ " AND MESKLA.DATDOK <= "+dateZ+" " + izlazi;
//
//        return qStr;
//      }
//      private String izlazi(String cSkl, String dateP, String dateZ, String parametar) {
//        String izl="";
//        if (parametar.equals("N")) {
//    // Izlazi
//    //      MAX(DOKU.CSKL)
//    //      izl="UNION select '            ' as CSKLUL,'            ' as CSKLIZ, MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, "+
//          izl="UNION ALL "+
//              "SELECT '            ' as CSKLUL, DOKI.CSKL as CSKLIZ, 0 as RBR, DOKI.DATDOK AS DATDOK, "+
//              "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK,  0.00 AS SUMZAD, STDOKI.ISP AS SUMRAZ "+
//              "from STDOKI, DOKI "+
//              "where STDOKI.CSKL='"+cSkl+"' AND "+util.getDoc("DOKI", "STDOKI")+ dokumentiN("STDOKI")+
//              " AND DOKI.DATDOK >= "+dateP+" AND DOKI.DATDOK <="+dateZ+" "+
//              "UNION ALL "+
//
//    // Medjuskladisnice izlaz
//          "select MESKLA.CSKLUL as CSKLUL, MESKLA.CSKLIZ as CSKLIZ, 0 as RBR, MESKLA.DATDOK AS DATDOK, MESKLA.VRDOK AS VRDOK, '' AS KOLND, MESKLA.BRDOK AS BRDOK, 0.00 AS SUMZAD, STMESKLA.ZADRAZIZ AS SUMRAZ "+
//              "from STMESKLA, MESKLA "+
//              "where  STMESKLA.VRDOK = 'MEI' and STMESKLA.VRDOK = 'MEI' and STMESKLA.CSKLIZ='"+cSkl+"' AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND STMESKLA.GOD=MESKLA.GOD " +
//              " AND STMESKLA.BRDOK=MESKLA.BRDOK AND STMESKLA.VRDOK = MESKLA.VRDOK " +
//              dokumentiN("STMESKLA")+
//              " AND MESKLA.DATDOK >= "+dateP+" AND MESKLA.DATDOK <="+dateZ+" ";
//        } else {
//    // Izlazi -> tu je promjena
//    //      izl="UNION select MAX(0) as RBR, MAX(DOKI.DATDOK) AS DATDOK, STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, SUM(-STDOKI.UIRAB) AS SUMZAD, SUM(STDOKI.IPRODSP) AS SUMRAZ "+
//          izl="UNION ALL " +
//              "select '            ' as CSKLUL,DOKI.CSKL as CSKLIZ, 0 as RBR, DOKI.DATDOK AS DATDOK, "+
//              "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, 0.00 AS SUMZAD, "+
//              "STDOKI.IPRODSP AS SUMRAZ "+
//              "from STDOKI, DOKI "+
//              "where STDOKI.CSKL='"+cSkl+"' AND "+util.getDoc("DOKI", "STDOKI")+ dokumentiE("STDOKI")+
//              " AND DOKI.DATDOK >= "+dateP+" AND DOKI.DATDOK <="+dateZ+" "+
//              "UNION ALL "+
//
//              "select '            ' as CSKLUL, DOKI.CSKL as CSKLIZ, 0 as RBR, DOKI.DATDOK AS DATDOK, "+
//              "STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK, (STDOKI.IPRODSP-STDOKI.IRAZ) AS SUMZAD, "+
//              "0.00 AS SUMRAZ "+
//              "from STDOKI, DOKI "+
//              "where (STDOKI.IPRODSP-STDOKI.IRAZ)<>0 and STDOKI.CSKL='"+cSkl+"' AND "+util.getDoc("DOKI", "STDOKI")+ dokumentiE("STDOKI")+
//              " AND DOKI.DATDOK >= "+dateP+" AND DOKI.DATDOK <="+dateZ+" "+
//              "UNION ALL "+
//
//    // Medjuskladisnice izlaz
//          "select MESKLA.CSKLUL as CSKLUL, MESKLA.CSKLIZ as CSKLIZ, 0 as RBR, MESKLA.DATDOK AS DATDOK, MESKLA.VRDOK AS VRDOK,  '' AS KOLND, MESKLA.BRDOK AS BRDOK, -(STMESKLA.ZADRAZIZ) AS SUMZAD, 0.00 AS SUMRAZ "+
//              "from STMESKLA, MESKLA "+
//              "where  STMESKLA.VRDOK = 'MEI' and STMESKLA.CSKLIZ='"+cSkl+"' AND STMESKLA.CSKLUL=MESKLA.CSKLUL AND STMESKLA.CSKLIZ=MESKLA.CSKLIZ AND STMESKLA.GOD=MESKLA.GOD " +
//              " AND STMESKLA.BRDOK=MESKLA.BRDOK AND STMESKLA.VRDOK = MESKLA.VRDOK " +dokumentiE("STMESKLA")+
//              " AND MESKLA.DATDOK >= "+dateP+" AND MESKLA.DATDOK <="+dateZ+" "+
//              "UNION ALL "
//             + dodaciIzlaza(cSkl, dateP, dateZ);
//        }
//        return izl;
//      }
//
//      private String dodaciIzlaza(String cSkl, String dateP, String dateZ) {
//        String izl;
//    // Izlazi
//        izl="select '            ' as CSKLUL,DOKI.CSKL as CSKLIZ, 0 as RBR, DOKI.DATDOK AS DATDOK, STDOKI.VRDOK AS VRDOK, '' AS KOLND, STDOKI.BRDOK AS BRDOK,  -(STDOKI.ISP) AS SUMZAD, 0.00 AS SUMRAZ "+
//            "from STDOKI, DOKI "+
//            "where STDOKI.CSKL='"+cSkl+"' AND "+util.getDoc("DOKI", "STDOKI")+
//            " AND STDOKI.VRDOK = 'ROT'" +
//            " AND DOKI.DATDOK >= "+dateP+" AND DOKI.DATDOK <="+dateZ+" "+
//            "UNION ALL "+
//    // Medjuskladisnice izlaz
//        "select MESKLA.CSKLUL as CSKLUL, MESKLA.CSKLIZ as CSKLIZ, 0 as RBR, MESKLA.DATDOK AS DATDOK, MESKLA.VRDOK AS VRDOK, '' AS KOLND, MESKLA.BRDOK AS BRDOK, -(STMESKLA.ZADRAZIZ) AS SUMZAD, 0.00  AS SUMRAZ "+
//            "from STMESKLA, MESKLA "+
//            "where  STMESKLA.CSKLIZ='"+cSkl+"' " + " AND STMESKLA.VRDOK = 'ROT' " +
//            " AND MESKLA.DATDOK >= "+dateP+" AND MESKLA.DATDOK <="+dateZ+" ";
//
//        return izl;
//      }

    //*** Def dokumenata
      private String dokumentiN(String table) {
        String dok = "";
        if (table.equals("STDOKI"))
          dok = " AND STDOKI.VRDOK NOT IN ('RAC','PRD','PON','NKU','GOT','TRE','ZAH')";
//        else
//          dok = " AND STMESKLA.VRDOK NOT IN ('RAC','PRD','PON','NKU','GOT')";
        return dok;
      }

      private String dokumentiE(String table) {
        String dok = "";
        if (table.equals("STDOKI")) //{
          dok = " AND STDOKI.VRDOK NOT IN ('RAC','PRD','PON','NKU','ROT','TRE','ZAH')";
//        } else {
//          dok = " AND STMESKLA.VRDOK NOT IN ('RAC','PRD','PON','NKU','ROT')";
//        }
        return  dok;
      }

      /** @todo ovo ispod za potrebe agregacije po datumu */

  java.util.Calendar cal =  java.util.Calendar.getInstance();

  private Timestamp getTocnoUPodne(Timestamp source) {
    cal.setTime(new java.util.Date(source.getTime()));
    cal.set(cal.HOUR_OF_DAY, 12);
    cal.set(cal.MINUTE, 00);
    cal.set(cal.SECOND, 00);
    cal.set(cal.MILLISECOND, 0);
    return new Timestamp(cal.getTime().getTime());
  }

}
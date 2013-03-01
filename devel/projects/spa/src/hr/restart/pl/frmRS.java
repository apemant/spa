/****license*****************************************************************
**   file: frmRS.java
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
package hr.restart.pl;

import hr.restart.baza.Orgpl;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.util.Aus;
import hr.restart.util.FileHandler;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raTransaction;
import hr.restart.util.raUpitLite;
import hr.restart.util.startFrame;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;


public class frmRS extends raUpitLite {
  raCommonClass rCC = raCommonClass.getraCommonClass();
  JPanel jpRS = new JPanel(new BorderLayout());
  jpHeaderRS jpHead = new jpHeaderRS(this);
  jpValuesRS jpVals = new jpValuesRS(this);
  JraButton jbDetail = new JraButton();
  StorageDataSet kumulRS;
  QueryDataSet detailRS;
  QueryDataSet tweekSet;
  HashMap hm = new HashMap();
  Valid vl = Valid.getValid();
  hr.restart.zapod.OrgStr orgStr = hr.restart.zapod.OrgStr.getOrgStr();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  public static String[] sumcols = new String[] {"bruto", "brutomj", "mio1", "mio1mj", "mio2", "mio2mj", "zo", "zomj",
    "zapos", "zaposmj", "premos", "osodb", "porez", "porezmj", "prirez", "prirezmj", "netopk"};

  protected String rsmode;         //O - obracunski podaci, A - arhivski

  public frmRS() {
    this("O");
  }

  public frmRS(String _mod) {
    rsmode = _mod;
    init();
    isArh = rsmode.equals("A");
    if (!isArh) {
      frs = this;
      addRep();
    }
  }

  private static frmRS frs;

  static boolean isArh;

  public static frmRS getInstance(){
    if (isArh) {
      return frmRSArh.getInstanceA();
    }
    else return frs;
  }

  private void init() {
    jpRS.add(jpHead,BorderLayout.NORTH);
    jpRS.add(jpVals,BorderLayout.CENTER);
//    rCC.EnabDisabAll(jpVals,false);
    jbDetail.setText("Detaljno");
    jbDetail.setToolTipText(jbDetail.getText().concat(" F6"));
    jbDetail.setIcon(raImages.getImageIcon(raImages.IMGALIGNJUSTIFY));
    jbDetail.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showDetails();
      }
    });
    jbDetail.setEnabled(false);
    getOKPanel().add(jbDetail,BorderLayout.WEST);
    enabHead(true);
    setJPan(jpRS);
  }

  public void keyF6Press() {
    showDetails();
  }

  public QueryDataSet getTweekSet(){
    return tweekSet;
  }

  private String getAzurirabilanString(){
      detailRS.first();
      int rbr = 0;
      String rbrIn = "( ";
      String in = "( ";
      String identifikator = detailRS.getString("IDENTIFIKATOR");
      java.util.Hashtable rbrHt = new java.util.Hashtable();
      java.util.Hashtable crHt = new java.util.Hashtable();
      String azurirabilan = "";

//      System.out.println("detailRS != null " + (detailRS != null));
//      System.out.println("detailRS.rowCount() != 0 " + (detailRS.rowCount() != 0) + " = " + detailRS.rowCount());

      if (detailRS.rowCount() != 0){

        do {
          rbrHt.put(""+detailRS.getInt("RBR"),""+detailRS.getInt("RBR"));
          crHt.put(detailRS.getString("CRADNIK"),detailRS.getString("CRADNIK"));
        } while (detailRS.next());

        Object[] crcl = crHt.values().toArray();

        for (int i = 0; i < crcl.length; i++) {
          if (i == (crcl.length-1)) {
            in += String.valueOf(crcl[i]) + " )";
          } else {
            in += String.valueOf(crcl[i]) + ", ";
          }
        }

        Object[] rbrcl = rbrHt.values().toArray();

        for (int i = 0; i < rbrcl.length; i++) {
          if (i == (rbrcl.length-1)){
            rbrIn += String.valueOf(rbrcl[i]) + " )";
          } else {
            rbrIn += String.valueOf(rbrcl[i]) + ", ";
          }
        }

        azurirabilan = "SELECT CRADNIK,RBR,RSOO,ODDANA,DODANA,JMBG,"+
                       "COPCINE,RSINV,RSB,RSZ,SATI,BRUTO,"+
                       "BRUTOMJ,MIO1,MIO1MJ,MIO2,MIO2MJ,ZO,"+
                       "ZOMJ,ZAPOS,ZAPOSMJ,PREMOS,OSODB,POREZ,"+
                       "POREZMJ,PRIREZ,PRIREZMJ,NETOPK,MJESEC,GODINA,"+
                       "IDENTIFIKATOR,VRSTAUPL  FROM " + getTableName() + " "+
                       "where cradnik in " + in + " AND rbr in " + rbrIn;

        if (isArh) {
          azurirabilan += " and identifikator = '" + identifikator + "'";
        }
      } else {
        azurirabilan = "SELECT CRADNIK,RBR,RSOO,ODDANA,DODANA,JMBG,"+
                       "COPCINE,RSINV,RSB,RSZ,SATI,BRUTO,"+
                       "BRUTOMJ,MIO1,MIO1MJ,MIO2,MIO2MJ,ZO,"+
                       "ZOMJ,ZAPOS,ZAPOSMJ,PREMOS,OSODB,POREZ,"+
                       "POREZMJ,PRIREZ,PRIREZMJ,NETOPK,MJESEC,GODINA,"+
                       "IDENTIFIKATOR,VRSTAUPL  FROM " + getTableName() +
                       " where 1 = 2";
      }
    return azurirabilan;
  }

  public void setKveri(){
    tweekSet.close();
    tweekSet.setQuery(new QueryDescriptor(dm.getDatabase1(), getAzurirabilanString()));
    tweekSet.open();
  }

  private void showDetails() {
    if (detailRS != null) {
//      sysoutTEST syst = new sysoutTEST(false);
//      syst.prn(detailRS);

//      System.out.println("azurirabilan = " + azurirabilan);
      tweekSet = Util.getNewQueryDataSet(getAzurirabilanString(), false);
      tweekSet.setColumns(dm.getRSPeriodobr().cloneColumns());
      tweekSet.open();
//      sysoutTEST syst = new sysoutTEST(false);
//      syst.prn(tweekSet);
      frmRamatDetailRS fdet = new frmRamatDetailRS(this);
      startFrame.getStartFrame().centerFrame(fdet,0,"Detaljni pregled podataka za RS");
      fdet.show();

//      rCC.EnabDisabAll(jpHead, false);


      rCC.setLabelLaF(this, false);

//      frmDetailRS fdetailRS = new frmDetailRS(this);
//      startFrame.getStartFrame().centerFrame(fdetailRS,0,"Detaljni pregled podataka za RS");
//      fdetailRS.show();
    }
  }
  protected void enabHead(boolean enab) {
    rCC.setLabelLaF(jpHead.jbSelCsif,enab&&rsmode.equals("O"));
    rCC.setLabelLaF(jpHead.jlrCsif,enab&&rsmode.equals("O"));
    rCC.setLabelLaF(jpHead.jlrOpis,enab&&rsmode.equals("O"));
//    rCC.setLabelLaF(jpHead.jbSelCvrob,enab&&rsmode.equals("O"));
//    rCC.setLabelLaF(jpHead.jlrCvrob,enab&&rsmode.equals("O"));
//    rCC.setLabelLaF(jpHead.jlrVrstaObv,enab&&rsmode.equals("O"));
    rCC.setLabelLaF(jpHead.jbSelCvrob,enab); //dok ne dobijemu vrstu obveznika u rsperiodarh
    rCC.setLabelLaF(jpHead.jlrCvrob,enab);  //dok ne dobijemu vrstu obveznika u rsperiodarh
    rCC.setLabelLaF(jpHead.jlrVrstaObv,enab); //dok ne dobijemu vrstu obveznika u rsperiodarh
    rCC.setLabelLaF(jpHead.jraDod,enab);
    rCC.setLabelLaF(jpHead.jraOdd,enab);
    rCC.setLabelLaF(jpHead.jcbDisketa,enab);
    rCC.setLabelLaF(jpHead.jcbMjesecIspl,enab);
    rCC.setLabelLaF(jpHead.jraGodobr,false);
    rCC.setLabelLaF(jpHead.jraMjobr,false);
  }
  public void componentShow() {
    kumulRS = null;
    enabHead(true);
    manageFocus(1);
    if (rsmode.equals("O")) {
      isArh = false;
      lookupData.getlookupData().raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
      if (dm.getLogotipovi().getString("MATBROJ").length() == 13){
        jpHead.headerRS.setString("CVROB","02");
      } else {
        jpHead.headerRS.setString("CVROB","01");
      }
      jpHead.jlrCvrob.forceFocLost();
      if (!jpHead.headerRS.getString("CSIF").equals("")){
        jpHead.headerRS.setString("CSIF","");
        jpHead.jlrCsif.forceFocLost();
      }
      raIniciranje.getInstance().posOrgsPl(hr.restart.zapod.OrgStr.getKNJCORG());
      QueryDataSet orgpl = dm.getOrgpl();
      jpHead.headerRS.setShort("MJESEC",orgpl.getShort("MJOBR"));
      jpHead.headerRS.setShort("GODINA",orgpl.getShort("GODOBR"));
      jpHead.headerRS.setString("IDENTIFIKATOR",orgpl.getString("RSIND"));
      jpHead.headerRS.setShort("ODDANA",(short)1);
      jpHead.headerRS.setShort("DODANA",frmRSPeriod.getZadnjiDan(orgpl.getShort("GODOBR"),orgpl.getShort("MJOBR")));
      jpHead.jcbDisketa.setSelected(true);
      jpHead.jcbMjesecIspl.setSelected(true);
//      jbDetail.setEnabled(false);
      rCC.EnabDisabAll(jpVals,false);
    } else isArh = true;
  }
  protected void manageFocus(final int step) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (step == 1 && rsmode.equals("O")) {
          jpHead.jlrCsif.requestFocus();
        } else {
          jpHead.jraRsind.requestFocus();
        }
      }
    });
  }
  public void okPress() {
//    isArh = rsmode.equals("A");
    prepareDetailRS();
    prepareKumulRS();
    jpVals.rebind();
    enabHead(false);
    rCC.setLabelLaF(jpHead.jraRsind,true);
    manageFocus(2);
    setBrojRadnika();
    setBrojBStranica();
    ispis = true;
    setSHA1();
//    addRep();
  }

  public void afterOKPress(){
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        frmRS.this.requestFocus();
        getOKPanel().jBOK.requestFocus();
      }
    });
  }

  public void addRep() {
    this.killAllReports();
    //R-Sm 2010 OIB
    this.addJasper("hr.restart.pl.repRSm_AOIB","hr.restart.pl.repRS_A","repRSm_A.jrxml", "R-Sm obrazac stranica A OIB");
    this.addJasper("hr.restart.pl.repRSm_BOIB","hr.restart.pl.repRS_B","repRSm_B.jrxml", "R-Sm obrazac stranica B OIB");
    this.addReport("hr.restart.pl.repDiskRSm2010","R-Sm obrazac OIB - DISKETA");
    //R-Sm obrazac 2005g i dalje
//    this.addReport("hr.restart.pl.repRSm_A","hr.restart.pl.repRS_A","RSm_A", "R-Sm obrazac stranica A MB");
//    this.addReport("hr.restart.pl.repRSm_B","hr.restart.pl.repRS_B","RSm_B", "R-Sm obrazac stranica B MB");    
//    this.addReport("hr.restart.pl.repDiskRSm","R-Sm obrazac - DISKETA");
    ////R-S obrazac 2000-2004
//    this.addReport("hr.restart.pl.repRS_A", "R-S obrazac stranica A", 2);
    this.addReport("hr.restart.pl.repRS_B", "STARI R-S stranica B", 2);
//    this.addReport("hr.restart.pl.repDiskRS","R-S obrazac - DISKETA");
  }

  String getTableName() {
    if (rsmode.equals("O")) return "rsperiodobr";
    else return "rsperiodarh";
  }
  protected void prepareKumulRS() {
/*    String qry = "SELECT ";
    for (int i = 0; i < sumcols.length; i++) {
      qry = qry.concat("sum(").concat(getTableName()).concat(".")
               .concat(sumcols[i].toUpperCase())
               .concat(") AS ")
               .concat(sumcols[i].toUpperCase())
               .concat(i<sumcols.length - 1?",":"");
    }
*/

    //storagedataset kumulrs
    //clone kolone sa imenima u sumcols iz dm.getRSPeriodobr
    //u do whileu sumirati te kolone i upisati u kumulrs
    //qry = qry.concat(getFromQry());
    //kumulRS = Util.getNewQueryDataSet(qry,false);

    kumulRS = new StorageDataSet();
    Column[] columns = new Column[sumcols.length];
    for (int i = 0; i < sumcols.length; i++) {
      columns[i] = (Column)dm.getRSPeriodobr().getColumn(sumcols[i]).clone();
    }
    kumulRS.setColumns(columns);
    kumulRS.open();
    BigDecimal[] kumValuesRS = new BigDecimal[sumcols.length];
    Arrays.fill(kumValuesRS,Aus.zero2);
    detailRS.first();
    do {
      for (int i = 0; i < sumcols.length; i++) {
        kumValuesRS[i] = kumValuesRS[i].add(detailRS.getBigDecimal(sumcols[i].toUpperCase()));
      }
    } while (detailRS.next());
    kumulRS.insertRow(false);
    for (int i = 0; i < sumcols.length; i++) {
      kumulRS.setBigDecimal(sumcols[i].toUpperCase(),kumValuesRS[i]);
    }
    kumulRS.post();
  }

  protected void prepareDetailRS() {
//    System.out.println("prepareDetailRS() pozvan");
    QueryDataSet rsperiodobr = dm.getRSPeriodobr();
    String[] rsc = rsperiodobr.getColumnNames(rsperiodobr.getColumnCount());
//    System.out.println("CHECKPOINT 1");
    String qry = "SELECT ";
    for (int i = 0; i < rsc.length; i++) {
      qry = qry.concat(getTableName()).concat(".")
               .concat(rsc[i].toUpperCase()).concat(i<rsc.length-1?",":" ");
    }
//    System.out.println("CHECKPOINT 2");
    qry = qry.concat(getFromQry()); //.concat(" order by "+getTableName()+".JMBG, "+getTableName()+".DODANA");
//    System.out.println("qry : " + qry);

//    System.out.println("CHECKPOINT 3");
    detailRS = Util.getNewQueryDataSet(qry,false);
    detailRS.setColumns(dm.getRSPeriodobr().cloneColumns());
    detailRS.open();
//    System.out.println("CHECKPOINT 4");

    if (detailRS.getRowCount() == 0) {
      jbDetail.setEnabled(true);
      return;
    }
//    System.out.println("CHECKPOINT 5");
    //   micanje smeca ala CRADNIK1 koje nastaje bugom u QueryDataSetu
    for (int i = 0; i < detailRS.getColumnCount(); i++) {
      Column clc = detailRS.getColumn(i);
      if (clc.getColumnName().startsWith(clc.getCaption()) &&
          !clc.getColumnName().equals("JMBG")) {
        clc.setVisible(0);
      }
    }
    if (rsmode.equals("O")) {
      //  azuriraj dm.getOrgPL
      QueryDataSet orgpl = Util.getNewQueryDataSet("SELECT * FROM orgpl where corg in "
          .concat(orgStr.getInQuery(orgStr.getOrgstrAndCurrKnjig()))
          );
      orgpl.first();
      do {
        orgpl.setString("RSIND",jpHead.headerRS.getString("IDENTIFIKATOR"));
      } while (orgpl.next());
      //  azuriraj rsperiodobr

//      detailRS.getColumn("CRADNIK").setRowId(true); // ODKOMENTIRATI AKO SE ODKOMENTIRA LINIJA 169! (???)
//      detailRS.getColumn("RBR").setRowId(true);

      parseDetailRS();
//      System.out.println("if (!raTransaction.saveChan...");
      if (!raTransaction.saveChangesInTransaction(new QueryDataSet[] {detailRS,orgpl})) return; /// ??? cemu ovo ???
//      System.out.println("proslo if (!raTransaction.saveChan...");
      dm.getOrgpl().refresh();
    } else {
//ovo vrijedi i za arhivu al bez sejvanja      
      parseDetailRS();
    }
    jbDetail.setEnabled(true);
    System.out.println("jbDetail inejblan");
  }

  private void parseDetailRS() {
    detailRS.first();
    do {
      detailRS.setString("IDENTIFIKATOR",jpHead.headerRS.getString("IDENTIFIKATOR"));
      detailRS.setString("VRSTAUPL",jpHead.headerRS.getString("CSIF"));
      if (getVrstaUplate().equals("03")||getVrstaUplate().equals("05")){
        detailRS.setString("RSINV","0");
        detailRS.setString("RSB","0");
//          detailRS.setString("RSZ","1");
        detailRS.setShort("ODDANA", (short)0);
        detailRS.setShort("DODANA", (short)0);
        detailRS.setBigDecimal("SATI", Aus.zero0);
        detailRS.setBigDecimal("PREMOS", Aus.zero0);
      }
      if (getVrstaUplate().equals("00")) {//nema uplate
      	detailRS.setBigDecimal("NETOPK", Aus.zero0);
      }
    } while (detailRS.next());
  }

  public boolean Validacija() {
    if (vl.isEmpty(jpHead.jlrCsif)) return false;
    if (vl.isEmpty(jpHead.jlrCvrob)) return false;
    if (vl.isEmpty(jpHead.jraGodobr)) return false;
    if (vl.isEmpty(jpHead.jraMjobr)) return false;
    if (vl.isEmpty(jpHead.jraOdd)) return false;
    if (vl.isEmpty(jpHead.jraDod)) return false;
    if (vl.isEmpty(jpHead.jraRsind)) return false;

//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(jpHead.headerRS);
    getOKPanel().jBOK.setSelected(true);

//    return frmRSPeriod.chkDan(jpHead.headerRS.getShort("ODDANA"),
//                jpHead.headerRS.getShort("DODANA"),
//                frmRSPeriod.getZadnjiDan(jpHead.headerRS.getShort("GODINA"),jpHead.headerRS.getShort("MJESEC")));

    if (!frmRSPeriod.chkDan(jpHead.headerRS.getShort("ODDANA"), jpHead.headerRS.getShort("DODANA"),
                            frmRSPeriod.getZadnjiDan(jpHead.headerRS.getShort("GODINA"),jpHead.headerRS.getShort("MJESEC")))){
      jpHead.jraOdd.requestFocus();
      JOptionPane.showMessageDialog(this,"Od - Do period nije ispravan","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;

//    return true;
//    raIzvjestaji.convertCopcineToRS("");
  }
  private String getFromQry() {
    String ret = " FROM "
               .concat(getTableName())
               .concat(",radnici where radnici.cradnik = ")
               .concat(getTableName()).concat(".cradnik AND (radnici.corg in ")
               .concat(orgStr.getInQuery(orgStr.getOrgstrAndCurrKnjig(),"radnici.corg")+") ");
    if (rsmode.equals("A")) {
      ret = ret.concat(" AND ").concat(getTableName()).concat(".IDENTIFIKATOR = '")
          .concat(jpHead.headerRS.getString("IDENTIFIKATOR"))
          .concat("'");
    }
    return ret;
  }
  private String lastIND = "";
  public void focLostRSInd() {
    String IND = jpHead.headerRS.getString("IDENTIFIKATOR");
    if (IND.equals(lastIND)) return;
    lastIND = IND;
    if (rsmode.equals("A") && !IND.equals("")) {
      String qry = "SELECT max("+getTableName()+".GODINA),max("
                 +getTableName()+".MJESEC),max("+getTableName()+".VRSTAUPL) from "
                 +getTableName()+" WHERE "+getTableName()+".IDENTIFIKATOR = '"+IND+"' " +
                 (frmParam.getParam("pl", "corginopt", "D", "Optimizirati in query u frmIzvjestajiPL (D/N").equalsIgnoreCase("D")
                     ?"":
                 		"AND EXISTS (SELECT * FROM radnici WHERE radnici.cradnik="+getTableName()+".cradnik and radnici.corg in "
                 		    +OrgStr.getOrgStr().getInQuery(OrgStr.getOrgStr().getOrgstrAndCurrKnjig(), "radnici.corg")+")"
                 	);
      System.out.println("qry focLost : " + qry);
      QueryDataSet qds = Util.getNewQueryDataSet(qry);
      short godrs = qds.getShort(0);
      short mjrs = qds.getShort(1);
      String vrupl = qds.getString(2);
      if (godrs == 0) {
        jpHead.jraRsind.setErrText("Zadani identifikator ne postoji u arhivi");
        jpHead.jraRsind.this_ExceptionHandling(null);
        jpHead.headerRS.clearValues();
        jpHead.jlrCsif.forceFocLost();
        jpHead.jlrCvrob.forceFocLost();
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Zadani identifikator ne postoji u arhivi",
            "Greška",
            javax.swing.JOptionPane.ERROR_MESSAGE);
//        System.out.println("Hm.... ovde bi bilo dobro izbacit kakvu poruku");
        return;
      }
      jpHead.headerRS.setShort("GODINA",godrs);
      jpHead.headerRS.setShort("MJESEC",mjrs);
      jpHead.headerRS.setShort("ODDANA",(short)1);
      jpHead.headerRS.setShort("DODANA",frmRSPeriod.getZadnjiDan(godrs,mjrs));
      jpHead.headerRS.setString("CSIF",vrupl);

      lookupData.getlookupData().raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
      if (dm.getLogotipovi().getString("MATBROJ").length() == 13){
        jpHead.headerRS.setString("CVROB","02");
      } else {
        jpHead.headerRS.setString("CVROB","01");
      }
      jpHead.jlrCvrob.forceFocLost();
      jpHead.jlrCsif.forceFocLost();
    }
  }
  protected boolean ispis = false;
  public boolean isIspis() {
    return ispis;
  }
  public void firstESC() {
    enabHead(true);
    if (ispis){
      kumulRS.empty();
      kumulRS = null;
      detailRS = null;
      godmjIspl = null;
//      jpVals.rebind();
      manageFocus(1);
      jbDetail.setEnabled(false);
      ispis = false;
    }
  }
  public boolean runFirstESC() {
    return ispis;
  }
  public void cancelPress() {
//  //  //    this.setVisible(false);
//  //  //    raCommonClass.getraCommonClass().EnabDisabAllLater(getJPan(), true);
//  //  //    interrupted = false;
    if (ispis){
      System.out.println("cancelPress() :)");
      kumulRS.empty();
      kumulRS = null;
      detailRS = null;
      godmjIspl = null;
      manageFocus(1);
      ispis = false;
    }
    super.cancelPress();
  }
  StorageDataSet getKumulRS() {
//    return null;
    return kumulRS;
  }

  public DataSet getRepSetRS_A(){
    return kumulRS;
  }

  public DataSet getRepSetRS_B(){
    return detailRS;
  }

  public DataSet getHead(){
    return jpHead.headerRS;
  }

  public void setBrojRadnika(){
    hm.clear();
    for (detailRS.first();detailRS.inBounds();detailRS.next()) {
      hm.put(detailRS.getString("CRADNIK"), detailRS.getString("JMBG"));
    }
    brR = hm.size();
  }

  public String getVrstaUplate(){
//    System.out.println("vrsta isplate iz storageseta   : " + jpHead.headerRS.getString("CSIF"));
    return jpHead.headerRS.getString("CSIF"); //jpHead.jlrCsif.getText().trim();
  }

  public String getVrstaObveznika(){
//    System.out.println("vrsta obveznika iz storageseta : " + jpHead.headerRS.getString("CVROB"));
    return jpHead.headerRS.getString("CVROB"); //jpHead.jlrCvrob.getText().trim();
  }

  public void setBrojBStranica(){
    int rc1 = detailRS.rowCount();
    int rc2 = rc1 / 12;
    int dif = rc2*12 - rc1;
    if (dif == 0) {
      brS = rc2;
      return;
    }
    brS = rc2 + 1;
  }

  int brR;
  int brS;

  public int getBrojRadnika(){
    return brR;
  }

  public int getBrojBStranica(){
    return brS;
  }

  public HashMap getMap(){
    return hm;
  }

  public String getSHA1(){
    return sha1formated;
  }

  public boolean isDisketa(){
    return jpHead.isPodaciZaDisketu();
  }

  String sha1formated;
  int sha1Length;

  protected void setSHA1(){
    repDiskRSm repdisk = new repDiskRSm2010();
    repdisk.makeReport();
    String sha1 = FileHandler.getSHA1(hr.restart.util.reports.mxReport.TMPPRINTFILE);
    sha1formated = "";
    sha1Length = sha1.length();
    for (int i=0 ; i < sha1.length() ; i++) {
      sha1formated = sha1formated.concat(sha1.substring(i,i+1)).concat(" ");
    }
    sha1formated = sha1formated;
  }
  private String godmjIspl = null;
  public String getGODMJISPL() {
    if (godmjIspl != null) return godmjIspl;
    if (getVrstaUplate().equals("00")) return "-";//nema uplate
    Timestamp datumispl = getDatumIspl();
    godmjIspl = Util.getUtil().getYear(datumispl)+" - "+Util.getUtil().getMonth(datumispl);
    System.out.println("godmjIspl = "+godmjIspl);
    return godmjIspl;
  }

  public String getOpcinaRadaFromOrgpl() {
    try {
      dm.getOrgpl().open();
      raIniciranje.getInstance().posOrgsPl(OrgStr.getKNJCORG());
      return raIzvjestaji.convertCopcineToRS(dm.getOrgpl().getString("COPCINE"));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return "SHIT";
    }
  }
  public Timestamp getDatumIspl() {
    Timestamp datumispl;
    if (isArh) {
//      QueryDataSet _bilokojiradnik = Util.
//      	getNewQueryDataSet("SELECT CRADNIK from kumulradarh where "+sjQuerys.getPripOrg(OrgStr.getKNJCORG(), "", "kumulradarh."));
//      _bilokojiradnik.open();
//      QueryDataSet _rsperarh = Util.getNewQueryDataSet("SELECT GODOBR, MJOBR, RBROBR FROM rsperiodarh WHERE CRADNIK='"+
//          _bilokojiradnik.getString("CRADNIK")+"' AND IDENTIFIKATOR = '"+getHead().getString("IDENTIFIKATOR")+"'");
//      _rsperarh.open();
//      QueryDataSet _datispl = Util.getNewQueryDataSet("SELECT DATUMISPL FROM kumulorgarh WHERE CORG='"+OrgStr.getKNJCORG()
//            +"' AND GODOBR="+_rsperarh.getShort("GODOBR")
//      			+" AND MJOBR="+_rsperarh.getShort("MJOBR")+
//      			" AND RBROBR="+_rsperarh.getShort("RBROBR"));
      String _q;
      QueryDataSet _datispl = Util.getNewQueryDataSet(_q = "SELECT datumispl FROM rsperiodarh, kumulradarh, kumulorgarh WHERE rsperiodarh.godobr = kumulradarh.godobr " +
      		"AND rsperiodarh.mjobr = kumulradarh.mjobr " +
      		"AND rsperiodarh.rbrobr = kumulradarh.rbrobr " +
      		"AND rsperiodarh.cradnik = kumulradarh.cradnik " +
      		"AND kumulradarh.godobr = kumulorgarh.godobr " +
      		"AND kumulradarh.mjobr = kumulorgarh.mjobr " +
      		"AND kumulradarh.rbrobr = kumulorgarh.rbrobr " +
      		"AND kumulradarh.cvro = kumulorgarh.cvro " +
      		"AND kumulradarh.corg = kumulorgarh.corg " +
      		"AND kumulorgarh.knjig='" + OrgStr.getKNJCORG() + "' " +
      		"AND rsperiodarh.IDENTIFIKATOR = '"+getHead().getString("IDENTIFIKATOR")+"'"
        /* "SELECT DATUMISPL FROM kumulorgarh, rsperiodarh WHERE " +
      		"kumulorgarh.knjig='" + OrgStr.getKNJCORG() + "' AND " +
       		"kumulorgarh.godobr = rsperiodarh.godobr AND kumulorgarh.mjobr = rsperiodarh.mjobr AND kumulorgarh.rbrobr = rsperiodarh.rbrobr " +
      		"AND rsperiodarh.IDENTIFIKATOR = '"+getHead().getString("IDENTIFIKATOR")+"'"*/
        );
      System.out.println(_q);
      _datispl.open();
      _datispl.first();
      datumispl = _datispl.getTimestamp("DATUMISPL");
    } else {
      QueryDataSet _orps = Orgpl.getDataModule().getQueryDataSet();
      _orps.open();
      lookupData.getlookupData().raLocate(_orps, "CORG", OrgStr.getKNJCORG());
      datumispl = _orps.getTimestamp("DATUMISPL");
    }
    return datumispl;
  }
}

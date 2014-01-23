/****license*****************************************************************
**   file: frmFormKPR.java
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
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.util.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: raFormKPR</p>
 * <p>Description: Formiranje knjige popisa robe</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Rest-Art</p>
 * @author S.J. && S.G.
 * @version 1.0
 */

public class frmFormKPR extends raUpitLite {
  String qStr="";
  Valid vl = hr.restart.util.Valid.getValid();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  TableDataSet tds = new TableDataSet();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

  QueryDataSet dummyKPR;
  QueryDataSet dummyGOT;
  String updateDoki, updateDokiPOS, updateDoku, updateMesklaUl, updateMesklaIz, updateMesklaMes, deleteGOT;

  java.sql.Date dateZ = null;
//  int rbr;
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JraButton jbSelCskl = new JraButton();
  JlrNavField jlrNazskl = new JlrNavField();
  JlrNavField jlrCskl = new JlrNavField();
  JLabel jlDatum = new JLabel();
  JraTextField jtfZavDatum = new JraTextField();
  JLabel jlCskl = new JLabel();

  public frmFormKPR() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public boolean runFirstESC() {
    if (!tds.getString("CSKL").equals("")) return true;
    return false;
  }

  public void firstESC() {
    rcc.EnabDisabAll(jp, true);
    tds.setString("CSKL","");
    jlrCskl.forceFocLost();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jlrCskl.requestFocus();
      }
    });
  }
  
  public DataSet getTDS() {
    return tds;
  }

  public void componentShow() {
    tds.setTimestamp("zavDatum", vl.findDate(false,0));
    tds.setString("CSKL", hr.restart.sisfun.raUser.getInstance().getDefSklad());
    jlrCskl.forceFocLost();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jlrCskl.requestFocus();
      }
    });
  }

  public boolean Validacija(){
    if (vl.isEmpty(jlrCskl)) return false;
    rcc.EnabDisabAll(jp,false);
    if (JOptionPane.showConfirmDialog(this.jp,
                                      "Formiranje knjige popisa robe?",
                                      "Formiranje KPR",
                                      JOptionPane.YES_NO_OPTION,
                                      JOptionPane.QUESTION_MESSAGE)
        != JOptionPane.YES_OPTION) {
      firstESC();
      return false;
    }
    return true;
  }

  boolean flag;

  public void okPress() {
    flag=true;
    dateZ = new java.sql.Date(this.tds.getTimestamp("zavDatum").getTime());
    String knjigodina = tds.getTimestamp("zavDatum").toString().substring(0,4);

    QueryDataSet lastNum = ut.getNewQueryDataSet("select max(rbr) as LAST_NUMBER from kpr where god='"+knjigodina+"' and cskl = '"+tds.getString("CSKL")+"'");
/*
    dm.getKPR().open();
    if (lookupData.getlookupData().raLocate(dm.getKPR(),new String[] {"GOD","CSKL"}, new String[] {knjigodina,tds.getString("CSKL")})){
      rbr = dm.getKPR().getInt("RBR");
    } else {
      rbr = 1;
    }
*/
    
    String otpdod = "";
    if (frmParam.getParam("robno", "kprOtp", "N", "Otpremnice staviti u KPR tek kad se prebace u racune (D,N)?").equalsIgnoreCase("D")) {
      otpdod = " AND (doki.vrdok!='OTP' OR doki.statira!='N') ";
    }

    int rbr = lastNum.getInt("LAST_NUMBER")+1;
    String tipkpr = frmParam.getParam("robno","indKPR","B",
                    "Naèin formiranja KPR (N,B,E,C)");

    qStr = rdUtil.getUtil().getKPRUlSql2(tds.getString("CSKL"),
              tds.getTimestamp("zavDatum"), tipkpr);

//    System.out.println("Last num " + lastNum.getInt("LAST_NUMBER") + " godina "+knjigodina);

    System.out.println("frmFormKPR : " + qStr);

//    flag=false;            // u svrhu testa
//    raProcess.fail();      // u svrhu testa

    QueryDataSet dejtaSet = ut.getNewQueryDataSet(qStr, false);


    dejtaSet.setSort(new SortDescriptor(new String[] {"DATDOK","BRDOK"}));
    dejtaSet.open();
    
    System.out.println("opened");
    
    raProcess.setMessage("Punjenje knjige popisa robe " + tds.getString("CSKL") + " ... ", true);

//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(dejtaSet);


    if (dejtaSet.isEmpty()) {
      flag=false;
      raProcess.fail();
    }

    if (hr.restart.sisfun.frmParam.getParam("robno","sumGotKPR","P","Ispis GOT/GRN racuna u KPR (P - pojedinacno D - sumirano po danu)").equalsIgnoreCase("D")){
      fillDummySumGot(dejtaSet, rbr, knjigodina, tipkpr);
    } else {
      fillDummy(dejtaSet, rbr, knjigodina, tipkpr, false);
    }

    updateDoki = "update doki set stat_kpr='D' where cskl='" + tds.getString("CSKL") + "' "+
                 "and god = '"+knjigodina+"' and datdok <= '"+ut.getLastSecondOfDay(tds.getTimestamp("zavDatum"))+"' and vrdok not in ('PON','TRE','ZAH') "+ otpdod;
    
    /*updateDokiPOS = "update doki set stat_kpr='D' where cskl='" + hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG() + "' "+
    				"and god = '"+knjigodina+"' and datdok <= '"+ut.getLastSecondOfDay(tds.getTimestamp("zavDatum"))+"'";*/
    
    updateDokiPOS = "update doki set stat_kpr='D' where "+Condition.in("CSKL", hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig(), "CORG")+
      " and god = '"+knjigodina+"' and datdok <= '"+ut.getLastSecondOfDay(tds.getTimestamp("zavDatum"))+"'";

    updateDoku = "update doku set stat_kpr='D' where cskl='" + tds.getString("CSKL") + "' "+//"' and vrdok = 'POS' "+
                 "and god = '"+knjigodina+"' and datdok <= '"+ut.getLastSecondOfDay(tds.getTimestamp("zavDatum"))+"'";

    updateMesklaUl = "update meskla set stat_kpr='D' where vrdok = 'MEU' and csklul='" + tds.getString("CSKL") + "' "+
                     "and god = '"+knjigodina+"' and datdok <= '"+ut.getLastSecondOfDay(tds.getTimestamp("zavDatum"))+"'";

    updateMesklaIz = "update meskla set stat_kpr='D' where vrdok = 'MEI' and cskliz='" + tds.getString("CSKL") + "' "+
                     "and god = '"+knjigodina+"' and datdok <= '"+ut.getLastSecondOfDay(tds.getTimestamp("zavDatum"))+"'";

    updateMesklaMes = "update meskla set stat_kpr='D' where vrdok = 'MES' and (csklul='" + tds.getString("CSKL") + "' or  cskliz='" + tds.getString("CSKL") + "') "+
                     "and god = '"+knjigodina+"' and datdok <= '"+ut.getLastSecondOfDay(tds.getTimestamp("zavDatum"))+"'";
    
//    System.out.println(updateDoku);
  }
  
  public void externalSave() {
    if (flag) saveDataSetAndUpdateRest();
  }

  public void afterOKPress(){
    if (flag){
      if (saveDataSetAndUpdateRest()){
        dm.getSynchronizer().markAsDirty("kpr");
        JOptionPane.showMessageDialog(this.jp,
                                      "Knjiga popisa robe je ažurirana",
                                      "Formiranje KPR",
                                      JOptionPane.INFORMATION_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(this.jp,
                                      new raMultiLineMessage(new String[] {"Knjiga popisa robe NIJE ažurirana",
            "Baza podataka nedostupna"}),
            "Formiranje KPR",
            JOptionPane.ERROR_MESSAGE);
      }
    } else {
      JOptionPane.showMessageDialog(this.jp,
                                    "Knjiga popisa robe je ažurna",
                                    "Formiranje KPR",
                                    JOptionPane.INFORMATION_MESSAGE);
    }
    firstESC();
  }


//  private java.util.Calendar cal = java.util.Calendar.getInstance();
//
//  private java.sql.Timestamp endDayTime(java.sql.Timestamp timstamp) {
//
//      cal.setTime(timstamp);
//      cal.set(cal.HOUR_OF_DAY,23);
//      cal.set(cal.MINUTE,59);
//      cal.set(cal.SECOND,59);
//      cal.set(cal.MILLISECOND,999);
//      java.sql.Timestamp retT = new java.sql.Timestamp(cal.getTime().getTime());
//
//      return retT;
//  }

  private QueryDataSet checkKprForGots(java.sql.Timestamp pd, int rb){
    QueryDataSet lastGOT = ut.getNewQueryDataSet("select * from kpr where god='"+
                                                 tds.getTimestamp("zavDatum").toString().substring(0,4)+
                                                 "' and cskl = '"+tds.getString("CSKL")+"' "+
                                                 "and datum >= '"+ut.getFirstSecondOfDay(pd)+"' "+
                                                 "and kljuc = 'GOT' and rbr="+rb);

//    Stln("select zad, raz from kpr where god='"+
//                       tds.getTimestamp("zavDatum").toString().substring(0,4)+
//                       "' and cskl = '"+tds.getString("CSKL")+"' "+
//                       "and datum >= '"+pd+ "' "+
//                       "and kljuc = 'GOT' and rbr="+rb);
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(falstem.out.prine);
//    syst.prn(lastGOT);

    return lastGOT;
  }

  private boolean brisired;


  private void fillDummySumGot(QueryDataSet data, int rb, String god, String tip) {
    brisired = false;
//    System.out.println("Formiranje KPR s racunima po danu");

    int rbr = rb;

    dummyKPR = hr.restart.baza.KPR.getDataModule().getTempSet("1=0");
    dummyKPR.open();
    data.first();
    
    
    /*QueryDataSet tmp = checkKprForGots(data.getTimestamp("DATDOK"),rb-1);

    if (!tmp.isEmpty()){
     System.out.println("uguzivan got");
      rbr -= 1;
      brisired = true;
      deleteGOT = "DELETE FROM KPR WHERE cskl='"+tds.getString("CSKL")+"' and rbr="+rbr+" and god='"+god+"'";      
      tmp.copyTo(dummyKPR);
    }*/
    
    dummyGOT = null;
    
    int brzap = 0;
    if ("C".equals(tip)) {
      QueryDataSet lastNum = Aus.q("select max(brzap) as LAST_NUMBER from doki " +
            "where god='"+god+"' and cskl = '"+tds.getString("CSKL")+
            "' and vrdok='GOT'");
      QueryDataSet lastGrn = Aus.q("SELECT MAX(brzap) as LAST_NUMBER from doki "+
          "where god='" + god + "' and vrdok='GRN'");
      
      
      brzap = lastNum.getInt("LAST_NUMBER")+1;
      if (brzap == 0) brzap = 1;
      if (lastGrn.getInt("LAST_NUMBER") + 1 > brzap)
        brzap = lastGrn.getInt("LAST_NUMBER")+1;
      
      Condition dat = Condition.between("DATDOK", ut.getYearBegin(god), tds.getTimestamp("zavDatum"));
      
      dummyGOT = Aus.q("SELECT * FROM doki WHERE god='"+god+"' and (vrdok='GRN' or (cskl = '"+
                    tds.getString("CSKL")+"' and vrdok='GOT')) " +
                    "and (brzap is null or brzap = 0) and "+dat);

      dummyGOT.setSort(new SortDescriptor(new String[] {"DATDOK"}));
    }
    
    boolean zbd = data.getColumn("SUMZAD").getDataType() == Variant.BIGDECIMAL;
    boolean rbd = data.getColumn("SUMRAZ").getDataType() == Variant.BIGDECIMAL;
    
    Timestamp oldt = new Timestamp(data.getTimestamp("DATDOK").getTime());
    
    BigDecimal sumzad, sumraz;
    BigDecimal pop = Aus.zero2;

    do {
      
      sumzad = zbd ? data.getBigDecimal("SUMZAD") 
          : new BigDecimal(data.getDouble("SUMZAD"));
 
      sumraz = rbd ? data.getBigDecimal("SUMRAZ") 
          : new BigDecimal(data.getDouble("SUMRAZ"));
      
      if (sumraz.compareTo(new java.math.BigDecimal("0.00")) != 0 ||
          sumzad.compareTo(new java.math.BigDecimal("0.00"))!=0){
        
        Timestamp dat = data.getTimestamp("DATDOK");
        String vd = data.getString("VRDOK");
        
        if ("C".equals(tip) && !ut.sameDay(oldt, dat)) {
          if (pop.signum() != 0) {
            fillZap(god, oldt, brzap++, rb++, pop);
            pop = Aus.zero0;
          }
          oldt = new Timestamp(dat.getTime());
        }
        
        
        if(vd.equals("GOT") || vd.equals("GRN") || vd.equals("USL")){
          if(lookupData.getlookupData().raLocate(dummyKPR, "DATUM", ut.getLastSecondOfDay(dat).toString())){

            dummyKPR.setBigDecimal("ZAD", dummyKPR.getBigDecimal("ZAD").add(sumzad)); //new java.math.BigDecimal(data.getDouble("SUMZAD")));
            dummyKPR.setBigDecimal("RAZ", dummyKPR.getBigDecimal("RAZ").add(sumraz));
          } else{
            dummyKPR.insertRow(false);
//            dummyKPR.setInt("RBR", rb++);
            dummyKPR.setString("GOD", god);
            dummyKPR.setString("OPIS", "Gotovinski raèuni");
            dummyKPR.setTimestamp("DATUM", ut.getLastSecondOfDay(dat));
            dummyKPR.setBigDecimal("ZAD", sumzad); //new java.math.BigDecimal(data.getDouble("SUMZAD")));
            dummyKPR.setBigDecimal("RAZ", sumraz);
            dummyKPR.setString("CSKL", tds.getString("CSKL"));
            dummyKPR.setString("KLJUC", "GOT");
          }
          
        } else if ("POP".equals(vd)) {
          pop = pop.add(sumraz);
        } else{
          dummyKPR.insertRow(false);
//          dummyKPR.setInt("RBR", rb++);
          dummyKPR.setString("GOD", god);
          dummyKPR.setString("OPIS", nazivDok(data, vd));
          dummyKPR.setTimestamp("DATUM", dat);
          dummyKPR.setBigDecimal("ZAD", sumzad);
          dummyKPR.setBigDecimal("RAZ", sumraz);
          dummyKPR.setString("CSKL", tds.getString("CSKL"));
          dummyKPR.setString("KLJUC", getKey(data));
        }
      }
    } while (data.next());
    
    if (pop.signum() != 0) {
      fillZap(god, oldt, brzap++, rb++, pop);
    }

    dummyKPR.close();
    dummyKPR.setSort(new SortDescriptor(new String[] {"DATUM", "KLJUC"}));
    dummyKPR.open();
    dummyKPR.first();

    do {
      dummyKPR.setInt("RBR", rbr++);
    } while (dummyKPR.next());

//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(dummyKPR);
  }

  
  
  private void fillDummy(QueryDataSet data, int rb, String god, 
      String tip, boolean force) {
    brisired = false;
//    System.out.println("Formiranje KPR s racunima pojedinacno");
    dummyKPR = hr.restart.baza.KPR.getDataModule().getTempSet("1=0");
    dummyKPR.open();
    
    dummyGOT = null;
    
    int brzap = 0;
    if ("C".equals(tip)) {
      QueryDataSet lastNum = Aus.q("select max(brzap) as LAST_NUMBER from doki " +
      		"where god='"+god+"' and cskl = '"+tds.getString("CSKL")+
      		"' and vrdok='GOT'");
      brzap = lastNum.getInt("LAST_NUMBER")+1;
      if (brzap == 0) brzap = 1;
      
      Condition dat = Condition.between("DATDOK", ut.getYearBegin(god), tds.getTimestamp("zavDatum"));
      
      dummyGOT = Aus.q("SELECT * FROM doki WHERE god='"+god+"' and cskl = '"+
                    tds.getString("CSKL")+"' and vrdok='GOT' " +
            		"and (brzap is null or brzap = 0) and "+dat);

      dummyGOT.setSort(new SortDescriptor(new String[] {"DATDOK"}));
    }
    
    data.first();
    
    BigDecimal sz, sr;
    
    boolean zbd = data.getColumn("SUMZAD").getDataType() == Variant.BIGDECIMAL;
    boolean rbd = data.getColumn("SUMRAZ").getDataType() == Variant.BIGDECIMAL;
    
    Timestamp oldt = new Timestamp(data.getTimestamp("DATDOK").getTime());
    BigDecimal pop = Aus.zero2;
    
    do {
      sz = zbd ? data.getBigDecimal("SUMZAD") 
               : new BigDecimal(data.getDouble("SUMZAD"));
      
      sr = rbd ? data.getBigDecimal("SUMRAZ") 
          : new BigDecimal(data.getDouble("SUMRAZ"));
      
      if (sz.signum() != 0 || sr.signum() != 0) {
        Timestamp dat = data.getTimestamp("DATDOK");
        String vd = data.getString("VRDOK");
        if ("C".equals(tip) && !ut.sameDay(oldt, dat)) {
          if (pop.signum() != 0) {
            fillZap(god, oldt, brzap++, rb++, pop);
            pop = Aus.zero0;
          }
          oldt = new Timestamp(dat.getTime());
        }
        
        if ("POP".equals(vd)) {
          pop = pop.add(sr);
        } else {        
          dummyKPR.insertRow(false);
          dummyKPR.setInt("RBR", rb++);
          dummyKPR.setString("GOD", god);
          dummyKPR.setString("OPIS", nazivDok(data, vd));
          dummyKPR.setTimestamp("DATUM", data.getTimestamp("DATDOK"));
          dummyKPR.setBigDecimal("ZAD", sz); //new java.math.BigDecimal(data.getDouble("SUMZAD")));
          dummyKPR.setBigDecimal("RAZ", sr);
          dummyKPR.setString("CSKL", tds.getString("CSKL"));
          dummyKPR.setString("KLJUC", getKey(data));
        }
      }
    } while (data.next());
    
    if (force && pop.signum() != 0) {
      fillZap(god, oldt, brzap++, rb++, pop);
    }
//    System.out.println("zadnji zapisan rbr je " + (rb-1));
//
//    dummyKPR.close();
//    dummyKPR.setSort(new SortDescriptor(new String[] {"DATUM","BRDOK"}));
//    dummyKPR.open();
//
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(dummyKPR);

  }
  
  private void fillZap(String god, Timestamp dat, int brzap, int rbr, BigDecimal pop) {
    while (dummyGOT.inBounds() && dummyGOT.getTimestamp("DATDOK").before(dat) &&
        !ut.sameDay(dummyGOT.getTimestamp("DATDOK"), dat)) {
      dummyGOT.setInt("BRZAP", -1);
      dummyGOT.next();
    }
    
    while (dummyGOT.inBounds() && ut.sameDay(dummyGOT.getTimestamp("DATDOK"), dat)) {
      dummyGOT.setInt("BRZAP", brzap);
      dummyGOT.next();
    }
    
    dummyKPR.insertRow(false);
    dummyKPR.setInt("RBR", rbr);
    dummyKPR.setString("GOD", god);
    dummyKPR.setString("OPIS", "Zapisnik o poravnanju br. " 
        + brzap + "/" + god);
    dummyKPR.setTimestamp("DATUM", ut.getLastSecondOfDay(dat));
    dummyKPR.setBigDecimal("ZAD", pop.negate());
    dummyKPR.setBigDecimal("RAZ", Aus.zero2);
    dummyKPR.setString("CSKL", tds.getString("CSKL"));
    dummyKPR.setString("KLJUC", "ZOP-" + tds.getString("CSKL") +
        "/" + god + "-" + dummyKPR.getInt("RBR"));
  }

  private String getKey(QueryDataSet data) {
    String forReturn= data.getString("VRDOK").trim();
    if (!data.getString("CSKLIZ").equalsIgnoreCase("")) {
      forReturn=forReturn+
                "-"+
                data.getString("CSKLIZ").trim();
    }
    if (!data.getString("CSKLUL").equalsIgnoreCase("")) {
      forReturn=forReturn+
                "-"+
                data.getString("CSKLUL").trim();
    }
    forReturn=forReturn+
              "/"+
              Valid.getValid().findYear(data.getTimestamp("DATDOK"))+
              "-"+
              data.getInt("BRDOK");
    return forReturn;
  }

//  private String getGotKey(QueryDataSet data){
//    return "GOT" + "-" + data.getString("CSKLIZ").trim() + "/" + Valid.getValid().findYear(data.getTimestamp("DATDOK"));
//  }

  private String nazivDok(QueryDataSet data, String vd) {
    if (vd.equals("ROT")) {
      return "Ra\u010Dun otpremnica br. " + getKey(data);
    } else if (vd.equals("RAC")) {
      return "Ra\u010Dun br. " + getKey(data);
    } else if (vd.equals("DON")) {
      return "Donos";
    } else if (vd.equals("PRK")) {
      return "Primka - Kalkulacija br. "+ getKey(data);
    } else if (vd.equals("POR")) {
      return("Poravnanje br. "+getKey(data));
    } else if (vd.equals("PST")) {
      return("Po\u010Detno stanje br. "+getKey(data));
    } else if (vd.equals("GOT")) {
      return("Gotovinski ra\u010Dun br. "+getKey(data));
    } else if (vd.equals("POS")) {
      return("Maloprodaja br. "+getKey(data));
    } else if (vd.equals("IZD")) {
      return("Izdatnica br. "+getKey(data));
    } else if (vd.equals("INV")) {
      return("Inventurni višak br. "+getKey(data));
    } else if (vd.equals("INM")) {
      return("Inventurni manjak br. "+getKey(data));
    } else if (vd.equals("OTR")) {
      return("Otpis robe br. "+getKey(data));
    } else if (vd.equals("MEI")) {
      return("Me\u0111uskladišnica - izlaz br. "+getKey(data));
    } else if (vd.equals("MEU")) {
      return("Me\u0111uskladišnica - ulaz br. "+getKey(data));
    } else if (vd.equals("PDO")) {
      return("Povratnica dobavlja\u010Du br. "+getKey(data));
    } else if (vd.equals("PKU")) {
      return("Povratnica kupcu br. "+getKey(data));
    } else if (vd.equals("OTP")) {
      return("Otpremnica br. "+getKey(data));
    } else if (vd.equals("PTE")) {
      return("Povratnica - tere\u0107enje br. "+getKey(data));
    } else if (vd.equals("NAR")) {
      return("Narudžba br. "+getKey(data));
    } else if (vd.equals("GRN")) {
      return("Gotovinski ra\u010Dun br. "+getKey(data));
    } else if (vd.equals("TER")) {
      return("Tere\u0107enje br. "+getKey(data));
    } else if (vd.equals("ODB")) {
      return("Odobrenje br. "+getKey(data));
    } else if (vd.equals("MES")) {
      return("Me\u0111uskladišnica br. "+getKey(data));
    } else if (vd.equals("POD")) {
      return("Povratnica - odobrenje br. "+getKey(data));
    } else if (vd.equals("REV")) {
      return("Revers br. "+getKey(data));
    } else if (vd.equals("PRV")) {
      return("Povratnica reversa br. "+getKey(data));
    }
    return "";
  }

  private void jbInit() throws Exception {
    this.setJPan(jp);

    tds.setColumns(new Column[] {
      dM.createTimestampColumn("zavDatum", "Završni datum"),
      dM.createStringColumn("cskl", "Skladište", 12)
    });
    tds.open();

    jlCskl.setText("Skladište");
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfZavDatum.setDataSet(tds);
    jlDatum.setText("Datum do");
    jlrCskl.setColumnName("CSKL");
    jlrCskl.setColNames(new String[] {"NAZSKL"});
    jlrCskl.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazskl});
    jlrCskl.setVisCols(new int[] {0,1});
    jlrCskl.setSearchMode(0);
    jlrCskl.setRaDataSet(rut.getMPSklDataset());
    jlrCskl.setNavButton(jbSelCskl);
    jlrCskl.setDataSet(this.tds);
    jlrNazskl.setColumnName("NAZSKL");
    jlrNazskl.setNavProperties(jlrCskl);
    jlrNazskl.setSearchMode(1);
    xYLayout1.setWidth(656);
    xYLayout1.setHeight(80);
    jp.setLayout(xYLayout1);
    jp.add(jlCskl,  new XYConstraints(20, 15, -1, -1));
    jp.add(jlrCskl, new XYConstraints(155, 15, -1, -1));
    jp.add(jlrNazskl, new XYConstraints(260, 15, 350, -1));
    jp.add(jbSelCskl, new XYConstraints(615, 15, 21, 21));
    jp.add(jlDatum, new XYConstraints(20, 40, -1, -1));
    jp.add(jtfZavDatum, new XYConstraints(155, 40, -1, -1));
  }

  public boolean isIspis() {
    return false;
  }

  protected boolean saveDataSetAndUpdateRest(){
    raLocalTransaction saveMyData = new raLocalTransaction() {
      public boolean transaction() throws Exception {
        try {
          if (brisired) raTransaction.runSQL(deleteGOT);
          raTransaction.saveChanges(dummyKPR);
          raTransaction.runSQL(updateDoki);
          raTransaction.runSQL(updateDokiPOS);
          raTransaction.runSQL(updateDoku);
          raTransaction.runSQL(updateMesklaUl);
          raTransaction.runSQL(updateMesklaIz);
          raTransaction.runSQL(updateMesklaMes);
          if (dummyGOT != null) raTransaction.saveChanges(dummyGOT);
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    };
    return (saveMyData.execTransaction());

//  System.out.println("Pretpostavimo da bi transakcija snimanja u bazu bila OK");
//  return true;

  }
}

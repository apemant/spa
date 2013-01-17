/****license*****************************************************************
**   file: frmPK.java
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
import hr.restart.baza.dM;
import hr.restart.util.Aus;
import hr.restart.util.ProcessInterruptException;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.sysoutTEST;
import hr.restart.zapod.dlgGetKnjig;

import java.math.BigDecimal;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class frmPK extends frmDNR{

  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  private static frmPK fPK;
  public static int brRad;
  StorageDataSet repSetPKList;
  
  public static frmPK getPKInstance()
  {
    return fPK;
  }

  public frmPK() {
    try {
      jbInitA();
      fPK = this;
    }
    catch (Exception ex) {

    }

  }

  private void jbInitA()
  {
    this.killAllReports();
//    this.addReport("hr.restart.pl.repPK", "PK", 5);
  }

  private String getInStr(QueryDataSet qds)
  {
    String temp="";
    qds.first();
    while(qds.inBounds())
    {
      if(qds.getRow() < (qds.getRowCount()-1))
        temp+=qds.getShort("MJOBR") +", ";
      else
        temp+=qds.getShort("MJOBR");
      qds.next();
    }
    return "kumulradarh.mjobr in ("+temp+") ";
  }

  private QueryDataSet createRepSet(QueryDataSet temp)
  {
    QueryDataSet qdsSum = new QueryDataSet();
    qdsSum.setColumns(new Column[]{
    (Column) temp.getColumn("BRUTO").clone(),
    (Column) temp.getColumn("DOPRINOSI").clone(),
    (Column) temp.getColumn("ISKNEOP").clone(),
    (Column) temp.getColumn("POROSN").clone(),
    (Column) temp.getColumn("PORIPRIR").clone(),
    (Column) temp.getColumn("CRADNIK").clone(),
    (Column) dm.getKumulradarh().getColumn("MJOBR").clone()
    });
    if(!temp.isOpen())
      temp.open();
    qdsSum.open();


    temp.setSort(new SortDescriptor(new String[]{"CRADNIK","DATUMISPL"}));
    temp.first();
//    sysoutTEST ST = new sysoutTEST(false);
//    ST.prn(temp);
    String mjObr="";
    String cRad= "";

    while(temp.inBounds())
    {

      if(!cRad.equals(temp.getString("CRADNIK")) || !mjObr.equals(ut.getMonth(temp.getTimestamp("DATUMISPL"))))
      {
        qdsSum.insertRow(false);
        mjObr = ut.getMonth(temp.getTimestamp("DATUMISPL"));
        cRad = temp.getString("CRADNIK");
      }
      qdsSum.setBigDecimal("BRUTO", qdsSum.getBigDecimal("BRUTO").add(temp.getBigDecimal("BRUTO")));
      qdsSum.setBigDecimal("DOPRINOSI", qdsSum.getBigDecimal("DOPRINOSI").add(temp.getBigDecimal("DOPRINOSI")));
      qdsSum.setBigDecimal("ISKNEOP", qdsSum.getBigDecimal("ISKNEOP").add(temp.getBigDecimal("ISKNEOP")));
      qdsSum.setBigDecimal("PORIPRIR", qdsSum.getBigDecimal("PORIPRIR").add(temp.getBigDecimal("PORIPRIR")));
      qdsSum.setBigDecimal("POROSN", qdsSum.getBigDecimal("POROSN").add(temp.getBigDecimal("POROSN")));
      qdsSum.setShort("MJOBR", getShortMj(mjObr));
      qdsSum.setString("CRADNIK", cRad);

      temp.next();

    }
    qdsSum.post();

    StorageDataSet listDS = qdsSum.cloneDataSetStructure();



    return qdsSum;
  }

  private short getShortMj(String mj)
  {
    Integer t = new Integer(mj);
    return (short)t.intValue();
  }

  QueryDataSet repSetPK;


  public void okPress()
  {
    getOKPanel().requestFocus();
    disejblajPanel();
    try {
      this.killAllReports();
      this.addJasper("hr.restart.pl.repIP","hr.restart.pl.repPK","repIP2011.jrxml", "Obrazac IP 2011+");
      this.addJasper("hr.restart.pl.repIP","hr.restart.pl.repPK","repIP.jrxml", "Stari Obrazac IP");
//      this.addReport("hr.restart.pl.repIP","hr.restart.pl.repPK","IP", "Obrazac IP 2009");
      this.addReport("hr.restart.pl.repIPDisk","Obrazac IP datoteka");
      this.addJasper("hr.restart.pl.repID1","hr.restart.pl.repID_1","repID_1.jrxml", "Obrazac ID-1");
      this.addReport("hr.restart.pl.repID_1XLSDisk","Obrazac ID-1 CSV datoteka");
//      this.addReport("hr.restart.pl.repPK", "Obrazac PK-1", 5);
//      this.addReport("hr.restart.pl.repPKDisk","Obrazac PK-1 disketa");
      this.addReport("hr.restart.pl.repPKDisk_PL","Popratni list",5);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
//    super.okPress();
//    getOKPanel().jPrekid.requestFocus();
//    rcc.EnabDisabAll(super.jPanel1, false);
    if (fieldSet.getInt("CRADNIKOD") > fieldSet.getInt("CRADNIKDO")){
      int i= fieldSet.getInt("CRADNIKOD");
      fieldSet.setInt("CRADNIKOD", fieldSet.getInt("CRADNIKDO"));
      jlrCradnikOd.forceFocLost();
      fieldSet.setInt("CRADNIKDO", i);
      jlrCradnikDo.forceFocLost();
    }
    knjigovodstvo = Util.getNewQueryDataSet(getKnjigovodstvoSQL());

//System.out.println("Engaging getRepQdsString()....");
    nextStep("Dohvat sumarnih podataka o djelatnicima..");
    repSet = Util.getNewQueryDataSet(getRepQdsString());
    if (repSet.getRowCount() == 0 && !jlrCradnikOd.getText().equals("")) {
      String qstr = "select CAST ("+Util.getUtil().getYear(fieldSet.getTimestamp("DATISPLOD"))+" as numeric(4)) as godobr, CAST (0 as numeric(4)) as mjobr, CAST (0 as numeric(4)) as rbrobr,"+
      " CAST (null as TIMESTAMP) as datumispl, CAST (0 as numeric(17,2)) as bruto, CAST (0 as numeric(17,2)) as doprinosi,"+
      " CAST (0 as numeric(17,2)) as iskneop, CAST (0 as numeric(17,2)) as porosn,"+
      " CAST (0 as numeric(17,2)) as por1, CAST (0 as numeric(17,2)) as por2, CAST (0 as numeric(17,2)) as por3,"+
      " CAST (0 as numeric(17,2)) as por4, CAST (0 as numeric(17,2)) as por5, CAST (0 as numeric(17,2)) as poruk,"+
      " CAST (0 as numeric(17,2)) as prir, CAST (0 as numeric(17,2)) as poriprir,"+
      " radnici.ime, radnici.prezime, radnici.cradnik, radnicipl.jmbg, radnicipl.oib,"+
      " radnicipl.adresa," +
      " '' as copcine, CAST (0 as numeric(17,2)) as netopk"+//ip
      " from radnici,radnicipl where radnici.cradnik = radnicipl.cradnik and radnici.cradnik between '"+ fieldSet.getInt("CRADNIKOD") + "' and '" + fieldSet.getInt("CRADNIKDO") + "'";
      System.out.println("goli k: "+qstr);
      repSet = Util.getNewQueryDataSet(qstr);
    } else if (repSet.getRowCount() == 0) {
      JOptionPane.showMessageDialog(null, "Nema isplata za zadani period. Prazni obrasci se mogu ispisati pojedinacno.");
      throw new ProcessInterruptException();
    }
//System.out.println("done gRQS!");

    repSetPK = new QueryDataSet();
    repSetPKList = null;
    repSetPK.setColumns(repSet.cloneColumns());
    repSetPK.addColumn(dM.createStringColumn("MJISPL",2));
    repSetPK.addColumn(dM.createBigDecimalColumn("OSIG"));
    repSetPK.addColumn(dM.createBigDecimalColumn("HARACH"));

    repSet.first();

    String mjIspl;

//hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//syst.prn(repSet);
    do{
      mjIspl = vl.maskString(ut.getMonth(repSet.getTimestamp("DATUMISPL")),'0',2);
//System.out.println("Engaging multiple HartAttacks for "+repSet.getShort("GODOBR")+"-"+repSet.getShort("MJOBR")+"-"+repSet.getShort("RBROBR")+" / "+repSet.getString("CRADNIK"));
      nextStep("Priprema PK za radnika "+repSet.getString("CRADNIK")+", obrada "+repSet.getShort("GODOBR")+"-"+repSet.getShort("MJOBR")+"-"+repSet.getShort("RBROBR"));
			java.math.BigDecimal osig_ =  (getGodPK(repSetPK) > 2011?Aus.zero2:getHartAttack(
          repSet.getShort("GODOBR"),
          repSet.getShort("MJOBR"),
          repSet.getShort("RBROBR"),
          repSet.getString("CRADNIK"),odbiciWh(raIzvjestaji.ID_3_1)).add(
          getHartAttack(
          repSet.getShort("GODOBR"),
          repSet.getShort("MJOBR"),
          repSet.getShort("RBROBR"),
          repSet.getString("CRADNIK"),odbiciWh(raIzvjestaji.ID_3_2))).add(
          getHartAttack(
          repSet.getShort("GODOBR"),
          repSet.getShort("MJOBR"),
          repSet.getShort("RBROBR"),
          repSet.getString("CRADNIK"),odbiciWh(raIzvjestaji.ID_3_3)))
          );
//System.out.println("done EmHAtt!");
//      System.out.println("god " +repSet.getShort("GODOBR") + " mj " + repSet.getShort("MJOBR") + " rbr " + repSet.getShort("RBROBR") + " radnik " + repSet.getString("CRADNIK") + " osig " + osig_ + " mj ispl " + mjIspl);

      if (!lookupData.getlookupData().raLocate(repSetPK,new String[] {"MJISPL","CRADNIK"},new String[] {mjIspl, cleanCRADNIK(repSet.getString("CRADNIK"))})){
        repSetPK.insertRow(false);
        repSetPK.setString("MJISPL",mjIspl);
        repSetPK.setString("CRADNIK",cleanCRADNIK(repSet.getString("CRADNIK")));
        repSetPK.setString("IME",repSet.getString("IME"));
        repSetPK.setString("PREZIME",repSet.getString("PREZIME"));
        repSetPK.setString("JMBG",repSet.getString("JMBG"));
        repSetPK.setString("ADRESA",repSet.getString("ADRESA"));
        repSetPK.setShort("GODOBR",getGodPK(repSet));//repSet.getShort("GODOBR"));
        repSetPK.setBigDecimal("BRUTO",repSet.getBigDecimal("BRUTO"));
        repSetPK.setBigDecimal("DOPRINOSI",repSet.getBigDecimal("DOPRINOSI"));
        repSetPK.setBigDecimal("ISKNEOP",repSet.getBigDecimal("ISKNEOP"));
        repSetPK.setBigDecimal("POROSN",repSet.getBigDecimal("POROSN"));
        repSetPK.setBigDecimal("POR1",repSet.getBigDecimal("POR1"));
        repSetPK.setBigDecimal("POR2",repSet.getBigDecimal("POR2"));
        repSetPK.setBigDecimal("POR3",repSet.getBigDecimal("POR3"));
        repSetPK.setBigDecimal("PORUK",repSet.getBigDecimal("PORUK"));
        repSetPK.setBigDecimal("PRIR",repSet.getBigDecimal("PRIR"));
        repSetPK.setBigDecimal("PORIPRIR",repSet.getBigDecimal("PORIPRIR"));
        repSetPK.setBigDecimal("OSIG",osig_);
        //IP
        repSetPK.setString("COPCINE", repSet.getString("COPCINE"));
        repSetPK.setBigDecimal("NETOPK", repSet.getBigDecimal("NETOPK"));
        repSetPK.setBigDecimal("HARACH", (getGodPK(repSetPK) > 2011?Aus.zero2:Harach.getHaracMj(getGodPK(repSet)+mjIspl, repSet.getString("CRADNIK"), null)[1]));
      } else {
        repSetPK.setBigDecimal("BRUTO",repSetPK.getBigDecimal("BRUTO").add(repSet.getBigDecimal("BRUTO")));
        repSetPK.setBigDecimal("DOPRINOSI",repSetPK.getBigDecimal("DOPRINOSI").add(repSet.getBigDecimal("DOPRINOSI")));
        repSetPK.setBigDecimal("ISKNEOP",repSetPK.getBigDecimal("ISKNEOP").add(repSet.getBigDecimal("ISKNEOP")));
        repSetPK.setBigDecimal("POROSN",repSetPK.getBigDecimal("POROSN").add(repSet.getBigDecimal("POROSN")));
        repSetPK.setBigDecimal("POR1",repSetPK.getBigDecimal("POR1").add(repSet.getBigDecimal("POR1")));
        repSetPK.setBigDecimal("POR2",repSetPK.getBigDecimal("POR2").add(repSet.getBigDecimal("POR2")));
        repSetPK.setBigDecimal("POR3",repSetPK.getBigDecimal("POR3").add(repSet.getBigDecimal("POR3")));
        repSetPK.setBigDecimal("PORUK",repSetPK.getBigDecimal("PORUK").add(repSet.getBigDecimal("PORUK")));
        repSetPK.setBigDecimal("PRIR",repSetPK.getBigDecimal("PRIR").add(repSet.getBigDecimal("PRIR")));
        repSetPK.setBigDecimal("PORIPRIR",repSetPK.getBigDecimal("PORIPRIR").add(repSet.getBigDecimal("PORIPRIR")));
        repSetPK.setBigDecimal("OSIG",osig_.add(repSetPK.getBigDecimal("OSIG")));
        //IP
        repSetPK.setBigDecimal("NETOPK", repSetPK.getBigDecimal("NETOPK").add(repSet.getBigDecimal("NETOPK")));
      }
    } while(repSet.next());
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(repSet);
//    syst.prn(repSetPK);
    //*hack-fix iskneop*
    for (repSetPK.first(); repSetPK.inBounds(); repSetPK.next()) {
      BigDecimal dohodak = repSetPK.getBigDecimal("BRUTO").subtract(repSetPK.getBigDecimal("DOPRINOSI")).subtract(repSetPK.getBigDecimal("OSIG")); 
      if (dohodak.subtract(repSetPK.getBigDecimal("ISKNEOP")).signum() < 0) {
        repSetPK.setBigDecimal("ISKNEOP", dohodak);
        repSetPK.setBigDecimal("POROSN", Aus.zero2);
        repSetPK.post();
      }
    }
    
    setBrojRadnikaPK();
    brRad = getBrojRadnika();
  }
  private String cleanCRADNIK(String cradnik) {
    if (frmID.getOjWith().trim().equals("")) return cradnik;
    String ow = "@"+dlgGetKnjig.getKNJCORG().trim();
    String ret = null;
//    System.out.println("ow.length() = "+ow.length());
//    System.out.println(cradnik+".endsWith("+ow+") : "+cradnik.endsWith(ow));
    if (cradnik.endsWith(ow)) {
//      System.err.println("I'm IN!");
      ret = cradnik.substring(0, cradnik.length()-ow.length());
    } else ret = cradnik;
//    System.out.println("cleanCRADNIK in "+cradnik+" out "+ret);
    return ret;
  }

  public void setBrojRadnikaPK() {
    try {
      String a1 = "SELECT count(distinct cradnik) as br FROM Kumulradarh, Kumulorgarh"
      +" WHERE kumulradarh.godobr = kumulorgarh.godobr"
      +" AND kumulradarh.mjobr = kumulorgarh.mjobr"
      +" AND kumulradarh.rbrobr = kumulorgarh.rbrobr"
      +" AND kumulradarh.corg = kumulorgarh.corg"
      +" and EXTRACT(YEAR FROM kumulorgarh.datumispl)="+getGodPK(repSet)
      +" and (kumulorgarh.corg in " + orgs.getInQuery(orgs.getOrgstrAndKnjig(fieldSet.getString("CORG")),"kumulorgarh.corg")+") ";
      String a2 = "AND cradnik between '"+ fieldSet.getInt("CRADNIKOD") + "' and '" + fieldSet.getInt("CRADNIKDO") + "'";
      String a;
      if (!jlrCradnikOd.getText().equals("")) a = a1.concat(a2);
      else a = a1;
System.out.println(a);    
      QueryDataSet alagada = Util.getNewQueryDataSet(a);
      brr = Valid.getValid().getSetCount(alagada, 0);
//    brr = alagada.getInt("BR");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      brr = 0;
    }
    
  }
  /**
   * @param repSet
   * @return
   */
  public short getGodPK(ReadRow repSet) {
//    return Short.parseShort(ut.getYear(repSet.getTimestamp("DATUMISPL")));
    return Short.parseShort(ut.getYear(fieldSet.getTimestamp("DATISPLOD")));
  }

  public DataSet getRepSet(){
    //repSetPK.setSort(new SortDescriptor(new String[] {"PREZIME","CRADNIK","MJISPL"}));
    return repSetPK;
  }
  
  public DataSet getRepSetList(){
    if (repSetPKList == null) return getNewRepSetList();
//    sysoutTEST ST = new sysoutTEST(false);
//    ST.showInFrame(repSetPKList, "TEST!!!");

    return repSetPKList;
  }
  
  public DataSet getNewRepSetList(){
		repSetPKList = repSetPK.cloneDataSetStructure();
		repSetPKList.addColumn(dM.createStringColumn("SORTNIPOJAM", 200));
		repSetPKList.setLocale(Aus.hr);
    repSetPKList.open();
    BigDecimal nula = new BigDecimal(0);
    repSetPK.setSort(new SortDescriptor(new String[] {"CRADNIK","MJISPL"}));
    int mbegin = Integer.parseInt(Util.getUtil().getMonth(fieldSet.getTimestamp("DATISPLOD")));
    int mend = Integer.parseInt(Util.getUtil().getMonth(fieldSet.getTimestamp("DATISPLDO")));
    repSetPK.first();
    String lastcradnik = repSetPK.getString("CRADNIK");
    String sortnipojam = getSortniPojam(lastcradnik);
//System.out.println("getRepSetList() entering loop");
    for (repSetPK.first(); repSetPK.inBounds(); repSetPK.next()) {
      nextStep("Dodavanje mjeseci bez isplate za radnika "+lastcradnik);
      if (!lastcradnik.equals(repSetPK.getString("CRADNIK"))) {
        addMissingMonths(repSetPKList, nula, mbegin, mend, lastcradnik, sortnipojam);
        lastcradnik = repSetPK.getString("CRADNIK");
        sortnipojam = getSortniPojam(lastcradnik);
      }
      repSetPKList.insertRow(false);
      repSetPK.copyTo(repSetPKList);
      repSetPKList.setString("SORTNIPOJAM", sortnipojam);
    }
    addMissingMonths(repSetPKList, nula, mbegin, mend, lastcradnik, sortnipojam);
    repSetPKList.setSort(new SortDescriptor(new String[] {"SORTNIPOJAM","MJISPL"}));
    return repSetPKList;
  }
  private String getSortniPojam(String cRad) {
    dm.getAllRadnici().open();
    lookupData.getlookupData().raLocate(dm.getAllRadnici(), new String[]{"CRADNIK"}, new String[]{cRad+""});
    return dm.getAllRadnici().getString("PREZIME")+" "+ dm.getAllRadnici().getString("IME");
  }
  private void addMissingMonths(StorageDataSet repSetPKList, BigDecimal nula, int mbegin, int mend, String lastcradnik, String srt) {
    for (int i=mbegin; i <= mend; i++) {
      if (!lookupData.getlookupData().raLocate(repSetPKList,
          new String[] {"CRADNIK", "MJISPL"},
          new String[] {lastcradnik,vl.maskString(i+"",'0',2)})) {
        //nema za taj mjesec dodaj nule
        repSetPKList.insertRow(false);
        repSetPKList.setString("CRADNIK",lastcradnik);
        repSetPKList.setString("MJISPL",vl.maskString(i+"",'0',2));
        repSetPKList.setShort("GODOBR",getGodPK(repSet));
        repSetPKList.setBigDecimal("BRUTO",nula);
        repSetPKList.setBigDecimal("DOPRINOSI",nula);
        repSetPKList.setBigDecimal("ISKNEOP",nula);
        repSetPKList.setBigDecimal("POROSN",nula);
        repSetPKList.setBigDecimal("POR1",nula);
        repSetPKList.setBigDecimal("POR2",nula);
        repSetPKList.setBigDecimal("POR3",nula);
        repSetPKList.setBigDecimal("PORUK",nula);
        repSetPKList.setBigDecimal("PRIR",nula);
        repSetPKList.setBigDecimal("PORIPRIR",nula);
        repSetPKList.setBigDecimal("OSIG",nula);
        repSetPKList.setString("SORTNIPOJAM", srt);
      }
    }
//    System.out.println("addMissingMonths for "+lastcradnik+" done");
  }

  public boolean Validacija(){
    if (!super.Validacija()) return false;
    return Valid.getValid().isValidRange(jraDatOd, jraDatDo);
  }
}
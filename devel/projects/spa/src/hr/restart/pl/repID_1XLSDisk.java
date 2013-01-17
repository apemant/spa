/****license*****************************************************************
**   file: repPKDisk.java
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
**  1/31/12 6:07 OM
****************************************************************************/
package hr.restart.pl;

import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.mxPrinter;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.repDisk;

import java.math.BigDecimal;
import java.util.StringTokenizer;

import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class repID_1XLSDisk extends repDisk{
  StorageDataSet qds;
  StorageDataSet kumulDS = new StorageDataSet();
  frmPK frPK = frmPK.getPKInstance();
  hr.restart.baza.dM dm;
  Valid vl;
  int mjeseci[] = new int []{0,0,0,0,0,0,0,0,0,0,0,0};
  lookupData ld = lookupData.getlookupData();

  public repID_1XLSDisk() {
    super(200);
    try {
      try {
        this.setPrinter(mxPrinter.getDefaultMxPrinter());
        this.getPrinter().setNewline(System.getProperty("line.separator"));
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      qds = (StorageDataSet)frPK.getRepSet();
      qds.open();
      qds.first();
      this.setPrint("ID-1-"+qds.getShort("GODOBR")+".csv");
      fill();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  private repID_1XLSDisk(boolean f) {
    qds = (StorageDataSet)frPK.getRepSet();
    qds.open();
    qds.first();
    kumulDS.setColumns(qds.cloneColumns());
    kumulDS.open();
    sumIznosi();
  }

  public static StorageDataSet getKumulDS() {
    repID_1XLSDisk rid = new repID_1XLSDisk(false);
    return rid.kumulDS;
  }
  
  public void fill() throws Exception
  {
    vl = Valid.getValid();
    dm = hr.restart.baza.dM.getDataModule();
    qds.first();


      kumulDS.setColumns(qds.cloneColumns());

    kumulDS.open();
    diskzap.open();
    sumIznosi();
    insertSlogV();
    kumulDS.first();
    while(kumulDS.inBounds())
    {
      insertSlogP();
      kumulDS.next();
    }
    insertSlogS();
  }
  public String getOIB(String cRad){
    ld.raLocate(dm.getAllRadnicipl(), new String[] {"CRADNIK"}, new String[] {""+cRad});
    return dm.getAllRadnicipl().getString("OIB");
  }
  public static String getRacunZaKolonu() {
    String[] konta = new String[] {
        "1422", 
    		"1805", 
    		"1457", 
    		"1465", 
    		"1473", 
    		"1546", 
    		"1570", 
    		"1589", 
    		"1597", 
    		"1600", 
    		"1813",
    		"1821", 
    		"1830",
    		"1848",
    		"1902"};
    int kolona = getKolonaID_1();
    return konta[kolona-8];
  }
  public static int getKolonaID_1() {
    try {
      return Integer.parseInt(frmParam.getParam("pl", "kolid-1", "8", "U koju kolonu se upisuje porez na ID-1", true));
    } catch (Exception e) {
      return 8;
    }
  }



//  public static BigDecimal getIzdatak(BigDecimal iznos) {
//    BigDecimal koef = new BigDecimal(frmParam.getParam("pl", "koefizid-1","1","Koeficijent izdatka za ID-1", true));
//    return iznos.multiply(koef).setScale(2,BigDecimal.ROUND_HALF_UP);
//  }
  private void insertSlogV() {
    System.out.println("insertSlogV");
    short god = qds.getShort("GODOBR");
    String redak = "";
    String ime = "-";
    String prezime = "-";

    StringTokenizer ti = new StringTokenizer(raUser.getInstance().getImeUsera(), " ");
    if (ti.hasMoreTokens()) ime = ti.nextToken();
    if (ti.hasMoreTokens()) prezime = ti.nextToken();
    String qry = "SELECT OIB,tel1, tel2,  email, '0' as STORNO FROM logotipovi WHERE corg='"+OrgStr.getKNJCORG()+"'";
    QueryDataSet headset = Util.getNewQueryDataSet(qry, true);
    headset.first();
    redak += headset.getString("OIB").trim() + ",";
    redak += god + ",";
    redak += ime + ",";
    redak += prezime + ",";
    redak += headset.getString("TEL1").trim() + ",";
    redak += headset.getString("TEL2").trim() + ",";
    redak += headset.getString("EMAIL").trim() + ",0";
    
    diskzap.insertRow(false);
    diskzap.setString("REDAK", redak);
    diskzap.post();
  }
  
  private void insertSlogP() {
    System.out.println("insertSlogP");
    String redak = "";
    redak += kumulDS.getString("PREZIME") + ",";
    redak += kumulDS.getString("IME") + ",";
    redak += getOIB(kumulDS.getString("CRADNIK")) + ",";
    redak += addZero(getCOpc(kumulDS.getString("CRADNIK")),3) + ",";
    redak += kumulDS.getBigDecimal("BRUTO").setScale(2, BigDecimal.ROUND_HALF_UP) + ",";
    sume[0] = sume[0].add(kumulDS.getBigDecimal("BRUTO"));
    
    redak += kumulDS.getBigDecimal("ISKNEOP").setScale(2, BigDecimal.ROUND_HALF_UP) + ",";
    sume[1] = sume[1].add(kumulDS.getBigDecimal("ISKNEOP"));
    
    redak += kumulDS.getBigDecimal("DOPRINOSI").setScale(2, BigDecimal.ROUND_HALF_UP) + ",";
    sume[2] = sume[2].add(kumulDS.getBigDecimal("DOPRINOSI"));
    
    redak += getRacunZaKolonu() + ",";
    redak += kumulDS.getBigDecimal("PORIPRIR").setScale(2, BigDecimal.ROUND_HALF_UP) + ",";
    redak += "*,";//kraj popisa 'konto,iznos...'
    redak += kumulDS.getBigDecimal("PORIPRIR").setScale(2, BigDecimal.ROUND_HALF_UP) + ",";
    sume[3] = sume[3].add(kumulDS.getBigDecimal("PORIPRIR"));
    
    redak += kumulDS.getBigDecimal("BRUTO").subtract(kumulDS.getBigDecimal("DOPRINOSI"))
        .subtract(kumulDS.getBigDecimal("PORIPRIR")).setScale(2, BigDecimal.ROUND_HALF_UP);
    sume[4] = sume[4].add(
        kumulDS.getBigDecimal("BRUTO").subtract(kumulDS.getBigDecimal("DOPRINOSI"))
        .subtract(kumulDS.getBigDecimal("PORIPRIR"))
    );
    diskzap.insertRow(false);
    diskzap.setString("REDAK", redak);
    diskzap.post();
    
  }
  BigDecimal[] sume = new BigDecimal[] {
      Aus.zero2, //0 primitak
      Aus.zero2, //1 izdatak
      Aus.zero2, //2 doprinosi
      Aus.zero2, //3 poriprir
      Aus.zero2, //4 ukupno primitak
  };
  private void insertSlogS() {
    System.out.println("insertSlogS");
    String redak = "DATOTEKA_UKUPNO,";
    redak += sume[0].setScale(2, BigDecimal.ROUND_HALF_UP) + ",";
    redak += sume[1].setScale(2, BigDecimal.ROUND_HALF_UP) + ",";
    redak += sume[2].setScale(2, BigDecimal.ROUND_HALF_UP) + ",";
    redak += getRacunZaKolonu() + ",";
    redak += sume[3].setScale(2, BigDecimal.ROUND_HALF_UP) + ",";
    redak += "*,";//kraj popisa 'konto,iznos...'
    redak += sume[3].setScale(2, BigDecimal.ROUND_HALF_UP) + ",";
    redak += sume[4].setScale(2, BigDecimal.ROUND_HALF_UP);
    
    diskzap.insertRow(false);
    diskzap.setString("REDAK", redak);
    diskzap.post();
  }



  private void sumIznosi() //tu bi trebalo rasporediti po racunima/kontima ali mi se sad ne da
  {
    String cRadnik = "";
    qds.setSort(new SortDescriptor(new String[]{"CRADNIK"}));
    qds.first();
    while(qds.inBounds())
    {
      if(!cRadnik.equals(qds.getString("CRADNIK")))
      {
        cRadnik = qds.getString("CRADNIK");
        kumulDS.insertRow(false);
//        kumulDS.setString("CORG", qds.getString("CORG"));
//        kumulDS.setString("CVRO", qds.getString("CVRO"));
        kumulDS.setShort("GODOBR", qds.getShort("GODOBR"));
        kumulDS.setShort("MJOBR", qds.getShort("MJOBR"));
        kumulDS.setShort("RBROBR", qds.getShort("RBROBR"));
        kumulDS.setString("CRADNIK", qds.getString("CRADNIK"));
        kumulDS.setString("IME", qds.getString("IME"));
        kumulDS.setString("PREZIME", qds.getString("PREZIME"));
        kumulDS.setString("JMBG", qds.getString("JMBG"));
        kumulDS.setString("ADRESA", qds.getString("ADRESA"));
      }
      kumulDS.setBigDecimal("BRUTO", kumulDS.getBigDecimal("BRUTO").add(qds.getBigDecimal("BRUTO")));
      kumulDS.setBigDecimal("DOPRINOSI", kumulDS.getBigDecimal("DOPRINOSI").add(qds.getBigDecimal("DOPRINOSI")));
      kumulDS.setBigDecimal("ISKNEOP", kumulDS.getBigDecimal("ISKNEOP").add(qds.getBigDecimal("ISKNEOP")));
      kumulDS.setBigDecimal("POROSN", kumulDS.getBigDecimal("POROSN").add(qds.getBigDecimal("POROSN")));
      kumulDS.setBigDecimal("PORUK", kumulDS.getBigDecimal("PORUK").add(qds.getBigDecimal("PORUK")));
      kumulDS.setBigDecimal("PRIR", kumulDS.getBigDecimal("PRIR").add(qds.getBigDecimal("PRIR")));
      kumulDS.setBigDecimal("PORIPRIR", kumulDS.getBigDecimal("PORIPRIR").add(qds.getBigDecimal("PORIPRIR")));
      qds.next();
    }
    kumulDS.post();
  }

  private String getCOpc(String crad)
  {
    ld.raLocate(dm.getAllRadnicipl(), new String[]{"CRADNIK"}, new String[]{crad});
    return dm.getAllRadnicipl().getString("COPCINE");
  }

  private String addZero(String s, int len)
  {
    return vl.maskString(s,'0',len);
  }
}


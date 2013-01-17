/****license*****************************************************************
**   file: repIPDisk.java
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
import hr.restart.sisfun.raUser;
import hr.restart.util.FileHandler;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.mxPrinter;
import hr.restart.util.reports.mxReport;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.repDisk;

import java.io.FileOutputStream;
import java.util.StringTokenizer;

import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class repIPDisk extends repDisk {
  StorageDataSet qds;
  StorageDataSet kumulDS = new StorageDataSet();
  frmPK frPK = frmPK.getPKInstance();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  Util ut = Util.getUtil();
  lookupData ld = lookupData.getlookupData();
  String separator = null;
  public repIPDisk() {
      try {
//        separator = Aus.getDumpSeparator();
        if (separator == null) separator = "#";
        separator = " "+separator;
        this.setPrinter(mxPrinter.getDefaultMxPrinter());
        this.getPrinter().setNewline(System.getProperty("line.separator"));
        qds = (StorageDataSet)frPK.getRepSet();
        QueryDataSet mbs = Util.getNewQueryDataSet("SELECT OIB FROM logotipovi WHERE corg='"+OrgStr.getKNJCORG()+"'",true);
        this.setPrint("IP_"+mbs.getString("OIB")+"_01.xml");
        qds.open();
        qds.first();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
  }
  private void dumpHead() {
    short god = qds.getShort("GODOBR");
    String ime = "-";
    String prezime = "-";

    StringTokenizer ti = new StringTokenizer(raUser.getInstance().getImeUsera(), " ");
    if (ti.hasMoreTokens()) ime = ti.nextToken();
    if (ti.hasMoreTokens()) prezime = ti.nextToken();
    String qry = "SELECT OIB,'"+god+"' as GOD,'"+ime+"' as IME,'"+prezime+"' as PREZIME,tel1, tel2,  email, '0' as STORNO FROM logotipovi WHERE corg='"+OrgStr.getKNJCORG()+"'";
    QueryDataSet headset = Util.getNewQueryDataSet(qry, true);
    headset.first();
    String dump = headset.getString("OIB")+separator
    +headset.getString("GOD")+separator
    +headset.getString("IME")+separator
    +headset.getString("PREZIME")+separator
    +headset.getString("TEL1")+separator
    +headset.getString("TEL2")+separator
    +headset.getString("EMAIL")+separator
    +headset.getString("STORNO");
    System.out.println(dump);
    FileHandler.writeConverted(dump, "ip_head.csv",null);
    
  }
  public void makeReport() {
    try {
      dumpHead();
      dumpDetail();
//      hr.porezna_uprava.e_porezna.obrasci.ip.v3_0.Generator.delimiter = separator;
//      new hr.porezna_uprava.e_porezna.obrasci.ip.v3_0.Generator("ip_head.csv", "ip_det.csv", new FileOutputStream(mxReport.TMPPRINTFILE));
      hr.porezna_uprava.e_porezna.obrasci.ip.v4_0.Generator.delimiter = separator;
      new hr.porezna_uprava.e_porezna.obrasci.ip.v4_0.Generator("ip_head.csv", "ip_det.csv", new FileOutputStream(mxReport.TMPPRINTFILE));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private String getCOPCINE(ReadRow ds) {
    String op = ds.getString("COPCINE");
    try {
      return vl.maskZeroInteger(new Integer(op),3);      
    } catch (Exception e) {
      e.printStackTrace();
      return op;
    }
  }
  private void dumpDetail() {
    qds.first();
    String retci = "";
    for (qds.first(); qds.inBounds(); qds.next()) {
      if (qds.getBigDecimal("BRUTO").signum()==0 || qds.getBigDecimal("PORIPRIR").signum()!=0) {
        System.out.println(":: B:0 P:>0 Radnik ::"+qds.getString("cradnik"));
      }
      if (qds.getBigDecimal("BRUTO").signum()!=0 || qds.getBigDecimal("PORIPRIR").signum()!=0) {
        String redak = getJMBG()+separator
          +"1"+separator
          +qds.getString("MJISPL")+separator
          //+qds.getString("COPCINE")+separator
          +getCOPCINE(qds)+separator
          +qds.getBigDecimal("BRUTO")+separator
          +qds.getBigDecimal("DOPRINOSI")+separator
          +qds.getBigDecimal("OSIG")+separator
          +ut.setScale(qds.getBigDecimal("BRUTO")
              .add(qds.getBigDecimal("DOPRINOSI").negate())
              .add(qds.getBigDecimal("OSIG").negate()),2)+separator
          +qds.getBigDecimal("ISKNEOP")+separator
          +qds.getBigDecimal("POROSN")+separator
          +qds.getBigDecimal("PORIPRIR")+separator
          +ut.setScale(qds.getBigDecimal("BRUTO")
              .add(qds.getBigDecimal("DOPRINOSI").negate())
              .add(qds.getBigDecimal("OSIG").negate()) //??
              .add(qds.getBigDecimal("PORIPRIR").negate())
              .add(qds.getBigDecimal("HARACH").negate())
              ,2)+separator
          +qds.getBigDecimal("HARACH")
          ;
        retci = retci + redak + "\n";
      }
    }
    FileHandler.writeConverted(retci, "ip_det.csv",null);
  }
  public String getJMBG(){
    String cRad = qds.getString("cradnik");
    ld.raLocate(dm.getAllRadnicipl(), new String[] {"CRADNIK"}, new String[] {""+cRad});
    return dm.getAllRadnicipl().getString("OIB");
  }
    
}

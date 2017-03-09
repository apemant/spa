/****license*****************************************************************
**   file: ispSI_NextGeneration.java
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
package hr.restart.os;

import hr.restart.util.Aus;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSetException;
import com.borland.dx.sql.dataset.QueryDataSet;

public class ispSI_NextGeneration extends ispOS_NextGeneration {
  
  static ispSI_NextGeneration isig;

  public ispSI_NextGeneration() {
    isig = this;
  }
  
  public static ispSI_NextGeneration getSiInstance(){
    if (isig == null) isig = new ispSI_NextGeneration();
    return isig;
  }

  public void setCheckBoxes(){
    rcbStatus.setRaItems(new String[][] {
      {"Sav SI","S"},
      {"SI u pripremi","P"},
      {"SI u upotrebi","A"}
    });
    rcbPorijeklo.setRaItems(new String[][] {
      {"Sva porijekla",""},
      {"Tuzemstvo","1"},
      {"Inozemstvo","2"},
      {"Vrijednosnice","3"}
    });
    rcbAktivnost.setRaItems(new String[][] {
      {"Sav SI",""},
      {"Aktivni SI","D"},
      {"Neaktivni SI","N"}
    });
  }

  public void okPress(){
    okpressed = true;
//    System.out.println("preparedQueryDataSet().rowCount() " + preparedQueryDataSet().rowCount());
    QueryDataSet temporary = preparedQueryDataSet();
    if (temporary != null && temporary.rowCount() > 0){
      killAllReports();
      if(!jcbOrgJed.isSelected() && !jcbOblikListe.isSelected() && !jcbInvBr.isSelected()) {
        System.out.println("\nOS_0\n");
        this.addReport("hr.restart.os.repIspSI","hr.restart.os.repIspSI","IspisSI_0","Ispis osnovnih sredstava");
      }
      else if(jcbOrgJed.isSelected() && !jcbOblikListe.isSelected() && !jcbInvBr.isSelected()) {
        System.out.println("\nOS_1\n");
        this.addReport("hr.restart.os.repIspSI_01","hr.restart.os.repIspSI","IspisSI_1","Ispis osnovnih sredstava IspOS01 NEW");
//        this.addReport("hr.restart.os.repIspisOS_1","hr.restart.os.repIspOS","IspisOS_1","Ispis osnovnih sredstava repIspisOS_1 OLD");
      }
      else if(!jcbOrgJed.isSelected() && jcbOblikListe.isSelected() && !jcbInvBr.isSelected()) {
        System.out.println("\nOS_2\n");
        this.addReport("hr.restart.os.repIspSI2","hr.restart.os.repIspSI","IspisSI","Ispis osnovnih sredstava NEW");
//        this.addReport("hr.restart.os.repIspisOS_2","hr.restart.os.repIspOS","IspisOS_2","Ispis osnovnih sredstava repIspisOS_2 OLD");
//        this.addReport("hr.restart.os.repIspisOS_2","Ispis osnovnih sredstava", 5);
      }
      else if(jcbOrgJed.isSelected() && jcbOblikListe.isSelected() && !jcbInvBr.isSelected()) {
        System.out.println("\nOS\n");
        this.addReport("hr.restart.os.repIspSI","hr.restart.os.repIspSI","IspisSI","Ispis osnovnih sredstava NEW");
//        this.addReport("hr.restart.os.repIspisOS","hr.restart.os.repIspOS","IspisOS","Ispis osnovnih sredstava repIspisOS OLD");
//        this.addReport("hr.restart.os.repIspisOS","Ispis osnovnih sredstava", 5);
      }
      else if(!jcbOrgJed.isSelected() && !jcbOblikListe.isSelected() && jcbInvBr.isSelected()) {
        System.out.println("\nOS_4\n");
        this.addReport("hr.restart.os.repIspSI4","hr.restart.os.repIspSI","IspisSI_4","Ispis osnovnih sredstava NEW");
//        this.addReport("hr.restart.os.repIspisOS_4","hr.restart.os.repIspOS","IspisOS_4","Ispis osnovnih sredstava repIspisOS_4 OLD");
//      this.addReport("hr.restart.os.repIspisOS_4","Ispis osnovnih sredstava", 5);
      }
      else if(jcbOrgJed.isSelected() && !jcbOblikListe.isSelected() && jcbInvBr.isSelected()) {
        System.out.println("\nOS_5\n");
        this.addReport("hr.restart.os.repIspSI5","hr.restart.os.repIspSI","IspisSI_5","Ispis osnovnih sredstava NEW");
//        this.addReport("hr.restart.os.repIspisOS_5","hr.restart.os.repIspOS","IspisOS_5","Ispis osnovnih sredstava repIspisOS_5 OLD");
//        this.addReport("hr.restart.os.repIspisOS_5","Ispis osnovnih sredstava", 5);
      }
      else if(!jcbOrgJed.isSelected() && jcbOblikListe.isSelected() && jcbInvBr.isSelected()) {
        System.out.println("\nOS_6\n");
        this.addReport("hr.restart.os.repIspSI1","hr.restart.os.repIspSI","IspisSI_6","Ispis osnovnih sredstava NEW");
//        this.addReport("hr.restart.os.repIspisOS_6","hr.restart.os.repIspOS","IspisOS_6","Ispis osnovnih sredstava repIspisOS_6 OLD");
//        this.addReport("hr.restart.os.repIspisOS_6","Ispis osnovnih sredstava", 5);
      }
      else if(jcbOrgJed.isSelected() && jcbOblikListe.isSelected() && jcbInvBr.isSelected()) {
        System.out.println("\nInvIspOS\n");
        this.addReport("hr.restart.os.repIspSI1","hr.restart.os.repIspSI","InvIspSI","Ispis osnovnih sredstava NEW");
//        this.addReport("hr.restart.os.repInvIspOS","hr.restart.os.repIspOS","InvIspOS","Ispis osnovnih sredstava repInvIspOS OLD");
//        this.addReport("hr.restart.os.repInvIspOS","Ispis osnovnih sredstava", 5);
      }
    } else {
      setNoDataAndReturnImmediately();
    }
  }

  public QueryDataSet preparedQueryDataSet() {
    qds = new QueryDataSet();
    String qStr ="";
//    qds = ut.getNewQueryDataSet(qStr);
    if (jrbPocStanje.isSelected()) { System.out.println("pocetno stanje selektirano"); // Pocetno sranje
      qStr = rdOSUtil.getUtil().getPST_SIIspisV2(fake.getString("CORG"),
                                               jcbOrgJed.isSelected(),
                                               jcbOblikListe.isSelected(),
                                               jcbInvBr.isSelected(),
                                               jpC.isRecursive(), //jcbPripOrgJed.isSelected(),
                                               statusDS.getString("STATUS"),
                                               fake.getString("GPP"),
                                               fake.getString("GPZ"),
                                               statusDS.getString("PORIJEKLO"),
                                               statusDS.getString("AKTIV"),
                                               getOblikIspisa());
    } else if (jrbStNaDan.isSelected()) { System.out.println("stanje na dan selektirano"); // Stanje na dan
      qStr = rdOSUtil.getUtil().getSND_SIIspisV2(fake.getString("CORG"),
                                               getPocDatum(),
                                               util.getTimestampValue(fake.getTimestamp("datum"),1),
                                               jcbOrgJed.isSelected(),
                                               jcbOblikListe.isSelected(),
                                               jcbInvBr.isSelected(),
                                               jpC.isRecursive(), //jcbPripOrgJed.isSelected(),
                                               statusDS.getString("STATUS"),
                                               fake.getString("GPP"),
                                               fake.getString("GPZ"),
                                               statusDS.getString("PORIJEKLO"),
                                               statusDS.getString("AKTIV"),
                                               getOblikIspisa());
    } else if (jrbTrStanje.isSelected()) { System.out.println("trenutno stanje selektirano"); //  Trenutno stanje
      qStr = rdOSUtil.getUtil().getTST_SIIspisV2(fake.getString("CORG"),
                                               jcbOrgJed.isSelected(),
                                               jcbOblikListe.isSelected(),
                                               jcbInvBr.isSelected(),
                                               jpC.isRecursive(), //jcbPripOrgJed.isSelected(),
                                               statusDS.getString("STATUS"),
                                               fake.getString("GPP"),
                                               fake.getString("GPZ"),
                                               statusDS.getString("PORIJEKLO"),
                                               statusDS.getString("AKTIV"),
                                               getOblikIspisa());
    }
    try {
    	Aus.refilter(qds, qStr);
          qds.getColumn("CORG").setRowId(true);
          qds.first();
          for(int j=0;j<qds.getRowCount();j++) {
            if (qds.getString("CORG").equals("")) {
               qds.deleteRow();
            }
            qds.next();
          }
          if(qds.getRowCount()==0) return qds;

          if((!jcbOrgJed.isSelected() && jpC.isRecursive())){ //jcbPripOrgJed.isSelected())) {
            qds.first();
            for(int j=0;j<qds.getRowCount();j++) {
              qds.setString("CORG", jpC.getCorg());//this.jrfCOrg.getText());
            }
            qds.next();
          }

          BigDecimal osnSum = new BigDecimal(0);
          BigDecimal ispSum = new BigDecimal(0);
          BigDecimal osn_ispSum = new BigDecimal(0);

          qds.open();
          qds.first();
          do {
            if (jrbPocStanje.isSelected()) {                                        // pocetno st
              osnSum=osnSum.add(qds.getBigDecimal("OSNPOCETAK"));
              ispSum=ispSum.add(qds.getBigDecimal("ISPPOCETAK"));
            } else if (jrbTrStanje.isSelected()){ //jrbStNaDan.isSelected()) {      // tr stanje
              qds.setBigDecimal("OSNDUGUJE", qds.getBigDecimal("OSNDUGUJE").add(qds.getBigDecimal("OSNPOCETAK")));
              qds.setBigDecimal("ISPPOTRAZUJE", qds.getBigDecimal("ISPPOTRAZUJE").add(qds.getBigDecimal("ISPPOCETAK")));
              osnSum=osnSum.add(qds.getBigDecimal("OSNDUGUJE").subtract(qds.getBigDecimal("OSNPOTRAZUJE")));
              ispSum=ispSum.add(qds.getBigDecimal("ISPPOTRAZUJE").subtract(qds.getBigDecimal("ISPDUGUJE")));
            } else if (jrbStNaDan.isSelected()){ //jrbTrStanje.isSelected()) {      // st na dan
       //        System.out.println("trenutno stanje");
              osnSum=osnSum.add(qds.getBigDecimal("OSNDUGUJE").subtract(qds.getBigDecimal("OSNPOTRAZUJE")));
              ispSum=ispSum.add(qds.getBigDecimal("ISPPOTRAZUJE").subtract(qds.getBigDecimal("ISPDUGUJE")));
       //        System.out.println("osnSum - " + osnSum);
       //        System.out.println("ispSum - " + ispSum);
            }
            qds.next();
          } while(qds.inBounds());
          osn_ispSum = osn_ispSum.add(osnSum.add(ispSum.negate()));
          sume = new double[] {osnSum.doubleValue(), ispSum.doubleValue(), osn_ispSum.doubleValue()};

      hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
      syst.prn(qds);

          return qds;
    }
    catch (DataSetException ex) {
      System.err.println("Za sada ako nema querija hvatam exception");
      ex.printStackTrace();
      return null;
    }
  }
}

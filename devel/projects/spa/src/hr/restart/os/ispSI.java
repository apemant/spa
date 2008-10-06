/****license*****************************************************************
**   file: ispSI.java
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

import javax.swing.JOptionPane;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

//public class ispSI extends raIspisDialog{
public class ispSI extends ispOS{
  public static double [] sume;
  public static String statusSI ;
  public static String porijekloSI ;
  public static String aktivnostSI ;

  public ispSI() {
    try {
      jbInitA();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInitA() throws Exception
  {
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

  public boolean okPress()
  {
    if(!vl.findYear(defQDS.getTimestamp("DATUM")).equals(vl.findYear(tds.getTimestamp("datum"))))
    {
      jtfStNaDan.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Pogrešan datum !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }
    int rows = 0;
    dan = jtfStNaDan.getText().trim();
    if(selectedRB2==2)
      oldValue = tds.getTimestamp("datum");
    if (jrfCOrg.getText().trim().equals("0") || jrfCOrg.getText().trim().equals(""))
    {
      rows = prepareIspis(0);
    }
    else
      rows = prepareIspis(1);
    if (rows == 0)
    {
      EnabDisabOrg(0);
      JOptionPane.showConfirmDialog(this.jp,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }

    getRepRunner().clearAllReports();
     switch (OJ+OL+IB) {
      case 0:
        this.addReport("hr.restart.os.repIspisSI_0","Ispis sitnog inventara", 5);
        break;
      case 1:
        this.addReport("hr.restart.os.repIspisSI_1","Ispis sitnog inventara", 5);
        break;
      case 2:
        this.addReport("hr.restart.os.repIspisSI_2","Ispis sitnog inventara", 5);
        break;
      case 3:
        this.addReport("hr.restart.os.repIspisSI","Ispis sitnog inventara", 5);
        break;
      case 4:
        this.addReport("hr.restart.os.repIspisSI_4","Ispis sitnog inventara", 5);
        break;
      case 5:
        this.addReport("hr.restart.os.repIspisSI_5","Ispis sitnog inventara", 5);
        break;
      case 6:
        this.addReport("hr.restart.os.repIspisSI_6","Ispis sitnog inventara", 5);
        break;
      case 7:
        this.addReport("hr.restart.os.repInvIspSI","Ispis sitnog inventara", 5);
        break;
    }
    return true;
  }

  //******* report
  public int prepareIspis(int i)
  {
    statusSI = statusDS.getString("STATUS");
    porijekloSI = statusDS.getString("PORIJEKLO");
    aktivnostSI = statusDS.getString("AKTIV");
    
    String qStr ="";
    if (selectedRB2==0) // Pocetno stanje
    {
      qStr = rdOSUtil.getUtil().getPST_SIIspis(jrfCOrg.getText().trim(), OJ,OL,IB,PO,
          statusDS.getString("STATUS"), fake.getString("GPP"), fake.getString("GPZ"), statusDS.getString("PORIJEKLO"),
          statusDS.getString("AKTIV"));
    }
    else if (this.selectedRB2==2) // Stanje na dan
    {
      qStr = rdOSUtil.getUtil().getSND_SIIspis(jrfCOrg.getText().trim(), getPocDatum(),
          util.getTimestampValue(tds.getTimestamp("datum"),1), OJ, OL, IB, PO, statusDS.getString("STATUS"),
          fake.getString("GPP"), fake.getString("GPZ"), statusDS.getString("PORIJEKLO"),
          statusDS.getString("AKTIV"));
    }
    else if (selectedRB2==1) //  Trenutno stanje
    {
      qStr = rdOSUtil.getUtil().getTST_SIIspis(jrfCOrg.getText().trim(), OJ, OL, IB, PO,
          statusDS.getString("STATUS"), fake.getString("GPP"), fake.getString("GPZ"), statusDS.getString("PORIJEKLO"),
          statusDS.getString("AKTIV"));
    }
    Aus.refilter(qds, qStr);    
    qds.getColumn("CORG").setRowId(true);
    qds.first();
    for(int j=0;j<qds.getRowCount();j++)
    {
      if (qds.getString("CORG").equals(""))
      {
         qds.deleteRow();
      }
      qds.next();
    }
    if(qds.getRowCount()==0) return 0;
    qds.first();
    if((OJ==0 && PO==8) && qds.getRowCount()>0)
    {
      for(int j=0;j<qds.getRowCount();j++)
      {
        qds.setString("CORG", this.jrfCOrg.getText());
      }
      qds.next();
    }


    BigDecimal osnSum = new BigDecimal(0);
    BigDecimal ispSum = new BigDecimal(0);
    BigDecimal osn_ispSum = new BigDecimal(0);

    qds.open();
    qds.first();
    do
    {
      if (this.selectedRB2==0 )
      {
        osnSum=osnSum.add(qds.getBigDecimal("OSNPOCETAK"));
        ispSum=ispSum.add(qds.getBigDecimal("ISPPOCETAK"));
      }
      else if (this.selectedRB2==1)
      {
        qds.setBigDecimal("OSNDUGUJE", qds.getBigDecimal("OSNDUGUJE").add(qds.getBigDecimal("OSNPOCETAK")));
        qds.setBigDecimal("ISPPOTRAZUJE", qds.getBigDecimal("ISPPOTRAZUJE").add(qds.getBigDecimal("ISPPOCETAK")));
        osnSum=osnSum.add(qds.getBigDecimal("OSNDUGUJE").add(qds.getBigDecimal("OSNPOTRAZUJE").negate()));
        ispSum=ispSum.add(qds.getBigDecimal("ISPPOTRAZUJE").add(qds.getBigDecimal("ISPDUGUJE").negate()));
      }
      else
      {
        osnSum=osnSum.add(qds.getBigDecimal("OSNDUGUJE").add(qds.getBigDecimal("OSNPOTRAZUJE").negate()));
        ispSum=ispSum.add(qds.getBigDecimal("ISPPOTRAZUJE").add(qds.getBigDecimal("ISPDUGUJE").negate()));
      }
      qds.next();
    }while(qds.inBounds());
    osn_ispSum = osn_ispSum.add(osnSum.add(ispSum.negate()));
    sume = new double[] {osnSum.doubleValue(), ispSum.doubleValue(), osn_ispSum.doubleValue()};
    return qds.getRowCount();
  }
}
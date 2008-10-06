/****license*****************************************************************
**   file: ispKnjizSI.java
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

public class ispKnjizSI extends ispKnjiz{

  public ispKnjizSI() {
  }

  public boolean Validacija() {
    if (vl.isEmpty(jpC.corg)) return false;
    if (tds.getTimestamp("zavDatum").before(tds.getTimestamp("pocDatum"))) {
      jtfPocDatum.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Datumski period nije ispravan","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  public void okPress(){
    okpr = true;
    int rows = 0;
    rows = prepareIspis();

    if (rows == 0) setNoDataAndReturnImmediately();

    getRepRunner().clearAllReports();
    if(selected)
      addReport("hr.restart.os.repIzKnjizSIKum","Sitni inventar", 5);
    else
      addReport("hr.restart.os.repIzKnjizSI","Sitni inventar", 5);
  }

  protected int prepareIspis() {
    String qStr="";
    
    selected = jpC.isRecursive();//jcbPripOJ.isSelected(); //   selected = jcbPripOJ.isSelected();
    qStr = rdOSUtil.getUtil().getIzKnjizSI(jpC.getCorg(), jrfKonto.getText().trim(), jrfVrPromjene.getText().trim(),
        util.getTimestampValue(tds.getTimestamp("pocDatum"),0), util.getTimestampValue(tds.getTimestamp("zavDatum"),1), selected );
    
    BigDecimal osnDuguje = new BigDecimal(0);
    BigDecimal osnPotrazuje = new BigDecimal(0);
    BigDecimal ispDuguje = new BigDecimal(0);
    BigDecimal ispPotrazuje = new BigDecimal(0);
    BigDecimal saldo = new BigDecimal(0);

    Aus.refilter(qds, qStr);
    qds.first();
    do {
      osnDuguje=osnDuguje.add(qds.getBigDecimal("OSNDUGUJE"));
      osnPotrazuje=osnPotrazuje.add(qds.getBigDecimal("OSNPOTRAZUJE"));
      ispDuguje=ispDuguje.add(qds.getBigDecimal("ISPDUGUJE"));
      ispPotrazuje=ispPotrazuje.add(qds.getBigDecimal("ISPPOTRAZUJE"));
      saldo=saldo.add(qds.getBigDecimal("SALDO"));
      qds.next();
    } while(qds.inBounds());


    sume = new double[] {osnDuguje.doubleValue(), osnPotrazuje.doubleValue(), ispDuguje.doubleValue(),
      ispPotrazuje.doubleValue(), saldo.doubleValue()};

    datumOd= jtfPocDatum.getText();
    datumDo= jtfZavDatum.getText();
    return qds.getRowCount();
  }
}


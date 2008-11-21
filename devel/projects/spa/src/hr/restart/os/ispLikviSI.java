/****license*****************************************************************
**   file: ispLikviSI.java
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

import javax.swing.JOptionPane;

public class ispLikviSI extends ispLikvi{

  public ispLikviSI() {
  }

  public void componentShow() {
    super.componentShow();
    this.setTitle("Ispis likvidiranog sitnog inventara");
  }

  public boolean Validacija() {
    if (vl.isEmpty(jpC.corg)) return false;
//    if(jrfCOrg.getText().equals("")) {
//      JOptionPane.showConfirmDialog(this.jp,
//                                    "Obavezan unos org. jedinice !",
//                                    "Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
//      jrfCOrg.requestFocus();
//      return false;
//    }
    if(tds.getTimestamp("zavDatum").before(tds.getTimestamp("pocDatum"))) {
      jtfPocDatum.requestFocus();
      JOptionPane.showConfirmDialog(this.jp,"Poèetni datum veæi od završnog!","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  public void okPress(){
    okpr = true;
    int rows = 0;
    rows = prepareIspis();
    if (rows == 0) setNoDataAndReturnImmediately();
    killAllReports();
    if(selected)
      addReport("hr.restart.os.repLikSIKum","Likvidacija SI", 5);
    else
      addReport("hr.restart.os.repLikSI","Likvidacija SI", 5);
  }

  public int prepareIspis() {
    String qStr="";    
    selected = jpC.isRecursive();//jcbPripOJ.isSelected();
      qStr = rdOSUtil.getUtil().getLikviSI(jpC.getCorg(),//jrfCOrg.getText().trim(),
                                         util.getTimestampValue(tds.getTimestamp("pocDatum"),0),
                                         util.getTimestampValue(tds.getTimestamp("zavDatum"),1), selected);    

    datumOd= jtfPocDatum.getText();
    datumDo= jtfZavDatum.getText();

    double osn = 0;
    double isp = 0;
    double saldo = 0;

    Aus.refilter(qds, qStr);
    qds.first();
    do {
      osn=osn + qds.getBigDecimal("OSNOVICA").doubleValue();
      isp=isp + qds.getBigDecimal("ISPRAVAK").doubleValue();
      saldo=saldo+qds.getBigDecimal("SALDO").doubleValue();
      qds.next();
    } while(qds.inBounds());

    sume = new double[] {osn, isp, saldo};
    return qds.getRowCount();
  }
}

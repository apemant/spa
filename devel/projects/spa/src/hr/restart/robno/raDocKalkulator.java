/****license*****************************************************************
**   file: raDocKalkulator.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;

import java.awt.Frame;
import java.math.BigDecimal;

import javax.swing.JDialog;
import javax.swing.JLabel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class raDocKalkulator {

  StorageDataSet tmpSet = new StorageDataSet();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  public raDocKalkulator() {
    tmpSet.setColumns(new Column[]{
      dm.getStdoki().getColumn("INAB").cloneColumn(),
      dm.getStdoki().getColumn("UPRAB").cloneColumn(),
      dm.getStdoki().getColumn("UIRAB").cloneColumn(),
      dm.getStdoki().getColumn("IPOR").cloneColumn(),
      dm.getStdoki().getColumn("IPRODBP").cloneColumn(),
      dm.getStdoki().getColumn("IPRODSP").cloneColumn()});
  }


  public void showPananel(QueryDataSet qds,double x,double y,Frame fr){
    if (!kalkulSve(qds)) return ;
    JraTextField inab = new JraTextField();
    inab.setDataSet(tmpSet);
    inab.setColumnName("INAB");
    JraTextField uprab = new JraTextField();
    uprab.setDataSet(tmpSet);
    uprab.setColumnName("UPRAB");
    JraTextField uirab = new JraTextField();
    uirab.setDataSet(tmpSet);
    uirab.setColumnName("UIRAB");
    JraTextField ipor = new JraTextField();
    ipor.setDataSet(tmpSet);
    ipor.setColumnName("IPOR");
    JraTextField iprodbp = new JraTextField();
    iprodbp.setDataSet(tmpSet);
    iprodbp.setColumnName("IPRODBP");
    JraTextField iprodsp = new JraTextField();
    iprodsp.setDataSet(tmpSet);
    iprodsp.setColumnName("IPRODSP");
//    JraFrame mymain = new JraFrame("Elementi");
    JDialog mymain = new JDialog(fr,"Elementi",true);

    mymain.setLocation((int) x+25,(int) y+25);
    mymain.getContentPane().setLayout(new XYLayout());
    ((XYLayout) mymain.getContentPane().getLayout()).setHeight(190);
    ((XYLayout) mymain.getContentPane().getLayout()).setWidth(330);

    mymain.getContentPane().add(new JLabel("Ukupno nabava"),new XYConstraints(15,15,-1,-1));
    mymain.getContentPane().add(inab,new XYConstraints(200,15,100,-1));
    mymain.getContentPane().add(new JLabel("RUC"),new XYConstraints(15,40,-1,-1));
    mymain.getContentPane().add(uirab,new XYConstraints(200,40,100,-1));
    mymain.getContentPane().add(new JLabel("RUC %"),new XYConstraints(15,65,-1,-1));
    mymain.getContentPane().add(uprab,new XYConstraints(200,65,100,-1));
    mymain.getContentPane().add(new JLabel("Ukupno bez poreza"),new XYConstraints(15,90,-1,-1));
    mymain.getContentPane().add(iprodbp,new XYConstraints(200,90,100,-1));
    mymain.getContentPane().add(new JLabel("Ukupno porez"),new XYConstraints(15,115,-1,-1));
    mymain.getContentPane().add(ipor,new XYConstraints(200,115,100,-1));
    mymain.getContentPane().add(new JLabel("Ukupno raèun"),new XYConstraints(15,140,-1,-1));
    mymain.getContentPane().add(iprodsp,new XYConstraints(200,140,100,-1));

    hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(inab,false);
    hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(uirab,false);
    hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(uprab,false);
    hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(iprodbp,false);
    hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(ipor,false);
    hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(iprodsp,false);
    mymain.pack();
    mymain.show();
  }

  public boolean kalkulSve(QueryDataSet qds) {

    if (qds.hasColumn("INAB") != null&&
        qds.hasColumn("IPRODBP") != null&&
        qds.hasColumn("IPRODSP") !=null) {
      tmpSet.open();
      tmpSet.insertRow(true);
      tmpSet.setBigDecimal("INAB",Aus.zero2);
      tmpSet.setBigDecimal("IPRODBP",Aus.zero2);
      tmpSet.setBigDecimal("IPRODSP",Aus.zero2);

     int cuurrow = qds.getRow();

     for (qds.first();qds.inBounds();qds.next()) {
       tmpSet.setBigDecimal("INAB",tmpSet.getBigDecimal("INAB").add(qds.getBigDecimal("INAB")));
       tmpSet.setBigDecimal("IPRODBP",tmpSet.getBigDecimal("IPRODBP").add(qds.getBigDecimal("IPRODBP")));
       tmpSet.setBigDecimal("IPRODSP",tmpSet.getBigDecimal("IPRODSP").add(qds.getBigDecimal("IPRODSP")));
     }
     if (!qds.goToRow(cuurrow)) {
       System.out.println("Tebra nije dobro ne mogu se vratiti na zadani current row");
     }

    try {
      tmpSet.setBigDecimal("UIRAB",tmpSet.getBigDecimal("IPRODBP").subtract(tmpSet.getBigDecimal("INAB")));
       tmpSet.setBigDecimal("UPRAB",tmpSet.getBigDecimal("UIRAB").multiply(
           new BigDecimal(100)).divide(tmpSet.getBigDecimal("INAB"),4,BigDecimal.ROUND_HALF_UP));
       tmpSet.setBigDecimal("IPOR",tmpSet.getBigDecimal("IPRODSP").subtract(tmpSet.getBigDecimal("IPRODBP")));
       return true;
    }
    catch (Exception ex) {
      return false;
    }
    }
    return false;
  }

}
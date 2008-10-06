/****license*****************************************************************
**   file: sjUtil.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;

public class sjUtil {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  public static sjUtil myUtil;

  public sjUtil() {
  }

  public static sjUtil getSjUtil() {
    if (myUtil==null) myUtil=new sjUtil();
    return myUtil;
  }
  private JraTextField comp = null;
  public void findFocusAfter(String vrp, JraTextField bruto, JraTextField koef, JraTextField sati) {
    System.out.println("findFocusAfter: ");
    
    hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
    if (ld.raLocate(dm.getVrsteprim(),
      new java.lang.String[] {"CVRP"},
      new java.lang.String[] {vrp})) {
      System.out.println("Finding CVRP: "+dm.getVrsteprim().getShort("CVRP"));
      if (ld.raLocate(dm.getNacobr(),
        new java.lang.String[] {"COBR"},
        new java.lang.String[] {new Integer(dm.getVrsteprim().getShort("COBR")).toString()})) {
        System.out.println("Finding COBR"+dm.getVrsteprim().getShort("COBR"));

        koef.getDataSet().setBigDecimal(koef.getColumnName(), dm.getVrsteprim().getBigDecimal("KOEF"));
        if (dm.getNacobr().getString("UNIZNOS").equals("D")) {
          comp=bruto;
          rcc.setLabelLaF(bruto, true);
        }
        else {
          rcc.setLabelLaF(bruto, false);
        }
        if (dm.getNacobr().getString("UNKOEF").equals("D")) {
          comp=koef;
          rcc.setLabelLaF(koef, true);
        }
        else {
          rcc.setLabelLaF(koef, false);
        }
        if (dm.getNacobr().getString("UNSATI").equals("D")) {
          comp=sati;
          rcc.setLabelLaF(sati, true);
        }
        else {
          rcc.setLabelLaF(sati, false);
        }
      }
      if (comp!=null) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            comp.requestFocus();
            comp.selectAll();
          }
        });
      }
    }
    System.out.println("end FindfokusAfter");
  }
  public void delIniciranje(final String corg) {
    System.out.println("poništavanje inicijalizacije");
    new raLocalTransaction() {
      public boolean transaction() throws Exception {
/*        vl.execSQL(sjQuerys.selectOrgPl(corg));
        vl.RezSet.open();*/
        com.borland.dx.sql.dataset.QueryDataSet orgspl = Util.getNewQueryDataSet(sjQuerys.selectOrgPl(corg));
        orgspl.first();
        do {
          raParam.setParam(orgspl, raParam.ORGPL_STATUS, "X");
        } while (orgspl.next());
        raTransaction.saveChanges(orgspl);
        raTransaction.runSQL(sjQuerys.delPrimanjaObr(corg));
        raTransaction.runSQL(sjQuerys.delRSPeriod(corg));
        return true;
      }
    }.execTransaction();
  }
}
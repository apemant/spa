/****license*****************************************************************
**   file: upProdajaPoAgentima.java
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

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;

public class upProdajaPoAgentima extends upProdajaPoDucanima {

  private static upProdajaPoAgentima anotherInstanceOfMe;

  public upProdajaPoAgentima() {
    try {
      init2();
      anotherInstanceOfMe = this;
    } catch (Exception e) {}
  }

  public static upProdajaPoAgentima getInstanceAgenti() {
    if (anotherInstanceOfMe == null)
      anotherInstanceOfMe = new upProdajaPoAgentima();
    return anotherInstanceOfMe;
  }

  protected void showDefaultValues() {
    super.showDefaultValues();
    jlrCagent.requestFocus();
  }

  private void init2() throws Exception {
    jlAgent.setText("Agent");
    jlrCagent.setColumnName("CAGENT");
    jlrCagent.setDataSet(fieldSet);
    jlrCagent.setColNames(new String[]{"NAZAGENT"});
    jlrCagent.setTextFields(new javax.swing.text.JTextComponent[]{jlrNazAgent});
    jlrCagent.setVisCols(new int[]{0, 1});
    jlrCagent.setSearchMode(0);
    jlrCagent.setRaDataSet(dm.getAgenti());
    jlrCagent.setNavButton(jbSelCagent);

    jlrNazAgent.setColumnName("NAZAGENT");
    jlrNazAgent.setNavProperties(jlrCagent);
    jlrNazAgent.setSearchMode(1);

    jp.remove(jMCs);

    jp.add(jlAgent, new XYConstraints(15, 45, -1, -1));
    jp.add(jlrCagent, new XYConstraints(150, 45, 100, -1));
    jp.add(jlrNazAgent, new XYConstraints(255, 45, 350, -1));
    jp.add(jbSelCagent, new XYConstraints(610, 45, 21, 21));

  }

  public void okPress() {
    sklfin = fieldSet.getString("VRART").equalsIgnoreCase("SF");
    fin = fieldSet.getString("VRART").equalsIgnoreCase("F");
    
    QueryDataSet jptvSet = new QueryDataSet();

    if (!fieldSet.getString("PRIKAZ").equalsIgnoreCase("MJ")) {

      this.killAllReports();

      if (fin)
        this.addReport("hr.restart.robno.repStatSkladista", "hr.restart.robno.repStatSkladista", "StatSkladsFin", "Prodaja po prodajnim mjestima");
      else
        this.addReport("hr.restart.robno.repStatSkladista", "hr.restart.robno.repStatSkladista", "StatSklads", "Prodaja po prodajnim mjestima");
      this.addReport("hr.restart.robno.RepStatPreglSklad", "hr.restart.robno.RepStatPreglSklad", "Prodaja po prodajnim mjestima - grafikon");
      
//      reportSet = racunica(ut.getNewQueryDataSet(getUpit()),"AG"); //TODO overrajdati
      
//      sysoutTEST syst = new sysoutTEST(false);

      //    getUpit();

      //    reportSet = ut.getNewQueryDataSet(getUpit());
      jptvSet.setColumns(reportSet.cloneColumns());

      if (fin) {
        jptvSet.getColumn("INAB").setVisible(0);
        jptvSet.getColumn("RUC").setVisible(0);
        jptvSet.getColumn("PRUC").setVisible(0);
      }

      jptvSet.open();
      reportSet.first();
      do {
        checkClosing();
        jptvSet.insertRow(false);
        reportSet.copyTo(jptvSet);
      } while (reportSet.next());

      getJPTV().setDataSetAndSums(jptvSet, new String[]{"INAB", "RUC", "POR", "IPRODBP", "IPRODSP"});
    } else { //TODO ovdje za po mjesecima...
      this.killAllReports();
      //TODO izvjestaj!!!!!
      this.addReport("hr.restart.robno.repStatPreglSkladMOnthChart", "hr.restart.robno.repStatPreglSkladMOnthChart", "Prodaja po radnim mjestima mjeseèno - grafikon");
      reportSet = mjesecnaRacunica(ut.getNewQueryDataSet(getUpit())); //TODO i ovo overrajdati
      getJPTV().setDataSetAndSums(reportSet, prikaz);
      
    }
    //    System.out.println(getNewUpit());
    // setNoDataAndReturnImmediately();
  }
  
  public String getCskl(){
//    System.out.println("getCskl u po AGENTIMA knjig = '"+fieldSet.getString("CORG")+"'");
    QueryDataSet skls = hr.restart.baza.Sklad.getDataModule().getFilteredDataSet("knjig = '"+fieldSet.getString("CORG")+"'");
    skls.open();
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(skls);
    if (skls.isEmpty()) return "";
    skls.first();
    String sks=" (";
    for (int i = 0;; i++) {
      sks += "'"+skls.getString("CSKL")+"'";
      if (skls.next()) sks +=",";
      else {
        sks += ") ";
        break;
      }
    }
    return sks;
  }
}
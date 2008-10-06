/****license*****************************************************************
**   file: upStatsMonthsArtikls.java
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

import hr.restart.util.lookupData;

import com.borland.dx.dataset.DataSetException;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;

public class upStatsMonthsArtikls extends upStatsMonths {

  public upStatsMonthsArtikls() {

    rcmbSljed.setRaItems(new String[][] { {Aut.getAut().getCARTdependable(
        "Šifra artikla", "Oznaka artikla", "Barcode"),
                         Aut.getAut().getCARTdependable("CART", "CART1", "BC")}
                         , {"Ukupni iznos", "UKUPNO"}
    });

  }


  protected void showDefaultValues() {
    super.showDefaultValues();
    fieldSet.setString("SLJED", Aut.getAut().getCARTdependable("CART","CART1","BC"));
  }

  public void firstESC(){
//  	jbChart.setVisible(false);

    if (doubleClicked){
      doubleClicked = false;
      this.killAllReports();
      this.addReport("hr.restart.robno.repStatsMonthsArtikli",
                     "hr.restart.robno.repStatsMonths",
                     "StatsMonthsArtikli",
                     "bla bla bla");



      jpKup.setCpar("");
      rpcart.clearFields();
      rpcart.jrfCGRART.setText(grupa);

      changeIcon(1);

      monthSet = monthSetCache;

      if(fieldSet.getString("SLJED").equals("CPAR"))monthSet.setSort(new SortDescriptor(new String[]{fieldSet.getString("SLJED")}));
      else monthSet.setSort(new SortDescriptor(new String[]{fieldSet.getString("SLJED")}, true, true));

      getJPTV().setDataSetAndSums(monthSet, new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "UKUPNO"});
    } else super.firstESC();
  }

private String grupa = "";


  protected void racunica(QueryDataSet tmpSet) throws DataSetException {
    String misec;
    if (!rpcart.getCART().equals("")) {
      this.killAllReports();
      this.addReport("hr.restart.robno.repStatsMonthsArtikliKup",
                     "hr.restart.robno.repStatsMonths",
                     "StatsMonthsArtikliKup",
                     "bla bla bla");
      do{
        misec = tmpSet.getTimestamp("DATDOK").toString().substring(5, 7);
        if(!lookupData.getlookupData().raLocate(monthSet, "CPAR", tmpSet.getInt("CPAR")+"")){
          monthSet.insertRow(false);
          monthSet.setInt("CPAR", tmpSet.getInt("CPAR"));
          monthSet.setBigDecimal(misec, tmpSet.getBigDecimal("IPRODSP"));
          monthSet.setBigDecimal("UKUPNO", tmpSet.getBigDecimal("IPRODSP"));
        } else{
          monthSet.setBigDecimal(misec, monthSet.getBigDecimal(misec).add(tmpSet.getBigDecimal("IPRODSP")));
          monthSet.setBigDecimal("UKUPNO", monthSet.getBigDecimal("UKUPNO").add(tmpSet.getBigDecimal("IPRODSP")));
        }
      } while(tmpSet.next());

      monthSet.getColumn("CART").setVisible(0);
      monthSet.getColumn("CART1").setVisible(0);
      monthSet.getColumn("BC").setVisible(0);
      monthSet.getColumn("NAZART").setVisible(0);

      monthSet.setSort(new SortDescriptor(new String[]{fieldSet.getString("SLJED")}, true, true));

    } else {
      if (!rpcart.getCGRART().equals("")) grupa = rpcart.getCGRART();
      else grupa = "";
      this.killAllReports();
      this.addReport("hr.restart.robno.repStatsMonthsArtikli",
                     "hr.restart.robno.repStatsMonths",
                     "StatsMonthsArtikli",
                     "bla bla bla");

      do {
        misec = tmpSet.getTimestamp("DATDOK").toString().substring(5, 7);
        if(!lookupData.getlookupData().raLocate(monthSet, "CART", tmpSet.getInt("CART")+"")){
          monthSet.insertRow(false);
          monthSet.setInt("CART", tmpSet.getInt("CART"));
          monthSet.setString("CART1",tmpSet.getString("CART1"));
          monthSet.setString("BC",tmpSet.getString("BC"));
          monthSet.setString("NAZART", tmpSet.getString("NAZART"));
          monthSet.setBigDecimal(misec, tmpSet.getBigDecimal("IPRODSP"));
          monthSet.setBigDecimal("UKUPNO", tmpSet.getBigDecimal("IPRODSP"));
        } else{
          monthSet.setBigDecimal(misec, monthSet.getBigDecimal(misec).add(tmpSet.getBigDecimal("IPRODSP")));
          monthSet.setBigDecimal("UKUPNO", monthSet.getBigDecimal("UKUPNO").add(tmpSet.getBigDecimal("IPRODSP")));
        }
      } while (tmpSet.next());
      monthSet.getColumn("CART").setVisible(0);
      monthSet.getColumn("CART1").setVisible(0);
      monthSet.getColumn("BC").setVisible(0);
      monthSet.getColumn("CPAR").setVisible(0);
      monthSet.getColumn(Aut.getAut().getCARTdependable("CART","CART1","BC")).setVisible(1);


      if(fieldSet.getString("SLJED").equals(Aut.getAut().getCARTdependable("CART","CART1","BC")))monthSet.setSort(new SortDescriptor(new String[]{fieldSet.getString("SLJED")}));
      else monthSet.setSort(new SortDescriptor(new String[]{fieldSet.getString("SLJED")}, true, true));

    }
  }


  public void jptv_doubleClick(){
    if (getCkup().equalsIgnoreCase("")){
//      System.out.println(this.getJPTV().getDataSet().getInt("CART"));
      rpcart.setCART(this.getJPTV().getDataSet().getInt("CART"));
      monthSetCache = getJPTV().getDataSet();
      doubleClicked = true;
      isIspis = false;
      ok_action();
      isIspis = true;
    }
  }


}

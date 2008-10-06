/****license*****************************************************************
**   file: raVectorRekap.java
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

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class raVectorRekap {

  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();
  QueryDataSet qds = new QueryDataSet();
  QueryDataSet qds_tmp = new QueryDataSet();
  String key = "";
  boolean gogo = false;
  private com.borland.dx.dataset.Column KEY = new com.borland.dx.dataset.Column("KEY","KEY",com.borland.dx.dataset.Variant.STRING);

  public raVectorRekap() {

    Column bjesansam1 = (Column) dm.getPorezi().getColumn("NAZPOR").clone();
    bjesansam1.setColumnName("CPOR");

    qds.close();
    qds.setColumns(new Column [] {
        (Column) dm.getStdoki().getColumn("CSKL").clone(),
        (Column) dm.getStdoki().getColumn("VRDOK").clone(),
        (Column) dm.getStdoki().getColumn("GOD").clone(),
        (Column) dm.getStdoki().getColumn("BRDOK").clone(),
    //    (Column) dm.getPorezi().getColumn("CPOR").clone(),
        bjesansam1 ,
        (Column) dm.getPorezi().getColumn("NAZPOR").clone(),
        (Column) dm.getPorezi().getColumn("UKUPOR").clone(),
        (Column) dm.getStdoki().getColumn("IPRODBP").clone(),
        (Column) dm.getStdoki().getColumn("POR1").clone(),KEY});
    qds.open();
    qds_tmp.close();
    qds_tmp.setColumns(new Column [] {
        (Column) dm.getStdoki().getColumn("CSKL").clone(),
        (Column) dm.getStdoki().getColumn("VRDOK").clone(),
        (Column) dm.getStdoki().getColumn("GOD").clone(),
        (Column) dm.getStdoki().getColumn("BRDOK").clone(),
//        (Column) dm.getPorezi().getColumn("CPOR").clone(),
        (Column) bjesansam1.clone() ,
        (Column) dm.getPorezi().getColumn("NAZPOR").clone(),
        (Column) dm.getPorezi().getColumn("UKUPOR").clone(),
        (Column) dm.getStdoki().getColumn("IPRODBP").clone(),
        (Column) dm.getStdoki().getColumn("POR1").clone(),
        (Column) KEY.clone()});
    qds_tmp.open();
  }
  public DataSet rakapitulacija(DataSet ds) {

    dm.getArtikli().open();
    dm.getPorezi().open();
    qds.open();
    qds.deleteAllRows();
    ds.open();
    ds.first();
    do {
      lD.raLocate(dm.getArtikli(),
                  new String[]{"CART"},
                  new String[]{String.valueOf(ds.getInt("CART"))});
      lD.raLocate(dm.getPorezi(),
                  new String[]{"CPOR"},
                  new String[]{dm.getArtikli().getString("CPOR")});
      for (int i = 1 ; i<4;i++) {

      if (!dm.getPorezi().getString("NAZPOR"+String.valueOf(i)).equals("")){

        key = ds.getString("CSKL").concat("-").concat(
              ds.getString("GOD")).concat("-").concat(
              ds.getString("VRDOK")).concat("-").concat(
              String.valueOf(ds.getInt("BRDOK"))).concat("-").concat(
              dm.getPorezi().getString("NAZPOR"+String.valueOf(i)));

        gogo = lD.raLocate(qds,
                    new String[]{"KEY"},
                    new String[]{key});

          if (!gogo) {
            qds.insertRow(true);
            qds.setString("CSKL",ds.getString("CSKL"));
            qds.setString("VRDOK",ds.getString("VRDOK"));
            qds.setString("GOD",ds.getString("GOD"));
            qds.setInt("BRDOK",ds.getInt("BRDOK"));
            qds.setString("CPOR",dm.getPorezi().getString("NAZPOR"+String.valueOf(i)));
            qds.setString("NAZPOR",dm.getPorezi().getString("NAZPOR"));
//            qds.setBigDecimal("UKUPOR",dm.getPorezi().getBigDecimal("POR"+String.valueOf(i)));

            ForOveride(ds,i);
            qds.setString("KEY",key);
          }
          else {
             ForOverideCh(ds,i);
          }
        }
      }
    } while (ds.next());

    return qds;
  }
  public void ForOveride(DataSet ds, int i) {
    qds.setBigDecimal("UKUPOR",ds.getBigDecimal("PPOR"+String.valueOf(i)));
    qds.setBigDecimal("IPRODBP",ds.getBigDecimal("IPRODBP"));
    qds.setBigDecimal("POR1",ds.getBigDecimal("POR"+String.valueOf(i)));
  }
  public void ForOverideCh(DataSet ds,int i) {
        qds.setBigDecimal("IPRODBP",qds.getBigDecimal("IPRODBP").
                                        add(ds.getBigDecimal("IPRODBP")));
        qds.setBigDecimal("POR1",qds.getBigDecimal("POR1").add(ds.getBigDecimal("POR"+String.valueOf(i))));
  }
  public DataSet getPoreziSet() {
    return qds;
  }
  public DataSet getPoreziSet(String cskl,String vrdok,String god,int brdok ) {

    qds_tmp.open();
    qds_tmp.deleteAllRows();
    qds.first();
    do {
      if (qds.getString("CSKL").equals(cskl) &&
          qds.getString("VRDOK").equals(vrdok) &&
          qds.getString("GOD").equals(god) &&
          qds.getInt("BRDOK")==brdok){
        qds_tmp.insertRow(true);
        qds_tmp.setString("CSKL",qds.getString("CSKL"));
        qds_tmp.setString("VRDOK",qds.getString("VRDOK"));
        qds_tmp.setString("GOD",qds.getString("GOD"));
        qds_tmp.setInt("BRDOK",qds.getInt("BRDOK"));
        qds_tmp.setString("CPOR",qds.getString("CPOR"));
        qds_tmp.setString("NAZPOR",qds.getString("NAZPOR"));
        qds_tmp.setBigDecimal("UKUPOR",qds.getBigDecimal("UKUPOR"));
        qds_tmp.setBigDecimal("IPRODBP",qds.getBigDecimal("IPRODBP"));
        qds_tmp.setBigDecimal("POR1",qds.getBigDecimal("POR1"));
        qds_tmp.setString("KEY",qds.getString("KEY"));
      }
    }
    while(qds.next());
//ST.prn(qds);
//ST.prn(qds_tmp);
    return qds_tmp;
  }

}
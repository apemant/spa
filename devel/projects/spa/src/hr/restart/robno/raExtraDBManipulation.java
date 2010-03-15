/****license*****************************************************************
**   file: raExtraDBManipulation.java
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
import hr.restart.util.raTransaction;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

public class raExtraDBManipulation {

  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);

  public raExtraDBManipulation() {}

  allSelect aSS = new allSelect();

  final PreparedStatement delVtrabat = raTransaction.getPreparedStatement
                                      (aSS.getQuery4rEDBM4DeleteVTrabat());

  final PreparedStatement delVTzavtr = raTransaction.getPreparedStatement
                                      (aSS.getQuery4rEDBM4DeleteVTzavtr());


  public boolean DeleteVTrabat(String cskl,
                               String vrdok,
                               String god,
                               int brdok,
                               int rbr1){

    boolean returnValue = true;
    try {
      QueryDataSet Qdelvtrabat = hr.restart.util.Util.getNewQueryDataSet(
            aSS.getQuery4rEDBM4DeleteVTrabatQ(cskl,vrdok,god,brdok,rbr1));
      Qdelvtrabat.deleteAllRows();
      raTransaction.saveChanges(Qdelvtrabat);
    }
    catch (Exception ex){
      ex.printStackTrace();
      returnValue = false;
    }
    return returnValue;
  }

  public boolean DeleteVTrabat(DataSet ds,short rbr1){

    return DeleteVTrabat(ds.getString("CSKL"),
                         ds.getString("VRDOK"),
                         ds.getString("GOD"),
                         ds.getInt("BRDOK"),rbr1);

  }

  public boolean  InsertVTrabat(DataSet ds,int rbr1,String cshrab) {

    boolean returnValue = true;

    String SQLString = aSS.getQuery4rEDBM4InsertVTrabat(ds.getString("CSKL"),
                               ds.getString("VRDOK"),
                               ds.getString("GOD"),
                               ds.getInt("BRDOK"),
                               rbr1,cshrab);
/*
    queryString = "insert into vtrabat " +
         "select lokk,aktiv,'"+cskl+"' as cskl,'"+
         vrdok+"' as vrdok,'"+
         god+"' as god,"+
         brdok+" as brdok,"+
         rbr1+" as rbr, rbr as lrbr,crab,prab,0 as irab,rabnarab from vshrab_rab where cshrab = '"+
         cshrab+"'";
*/
    /*dm.getVtrabat().open();
    dm.getVtrabat().insertRow(true);
    QueryDataSet Qvtrabat = hr.restart.util.Util.getNewQueryDataSet(
        "select lokk,aktiv,'"+ds.getString("CSKL")+"' as cskl,'"+
        ds.getString("VRDOK")+"' as vrdok,'"+
        ds.getString("GOD")+"' as god,"+
        ds.getInt("BRDOK")+" as brdok,"+
        rbr1+" as rbr, rbr as lrbr,crab,prab,0 as irab,rabnarab from vshrab_rab where cshrab = '"+
         cshrab+"'",true);
*/
//    dm.getVtrabat().setString("CSKL",ds.getString("CSKL"));
//    dm.getVtrabat().setString("VRDOK",ds.getString("VRDOK"));

    try {
      raTransaction.runSQL(SQLString);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      returnValue = false;
    }

    return returnValue;
  }

  public DataSet getVtrabat(String cskl,String vrdok,String god,
                                           int brdok) {

    QueryDataSet QDSVtrabat = hr.restart.util.Util.getNewQueryDataSet(
        aSS.getQuery4rEDBM4SelectVTrabat(cskl,vrdok,god,brdok),true);
    return (DataSet) QDSVtrabat;

  }

  //-> zavrsni troskovi
  public boolean DeleteVTzavtr(String cskl,
                               String vrdok,
                               String god,
                               int brdok,
                               int rbr1){
    boolean returnValue = true;


    try {
      delVTzavtr.setString(1,cskl);
      delVTzavtr.setString(2,vrdok);
      delVTzavtr.setString(3,god);
      delVTzavtr.setInt(4,brdok);
      delVTzavtr.setInt(5,rbr1);
      delVTzavtr.execute();
    }
    catch (SQLException ex) {
      ST.prn(ex);
      returnValue = false;
    }
    return returnValue;

  }

  public boolean DeleteVTzavtr(DataSet ds,short rbr1){

    return DeleteVTzavtr(ds.getString("CSKL"),
                     ds.getString("VRDOK"),
                     ds.getString("GOD"),
                         ds.getInt("BRDOK"),rbr1);
  }

  public boolean InsertVTzavtr(DataSet ds,int rbr1,String cshzt) {
    boolean returnValue = true;

    String SQLString = aSS.getQuery4rEDBM4InsertVTzavtr(ds.getString("CSKL"),
                                             ds.getString("VRDOK"),
                                             ds.getString("GOD"),
                                             ds.getInt("BRDOK"),
                                             rbr1,cshzt);

    try {
      raTransaction.runSQL(SQLString);
    }
    catch (Exception ex) {
      ST.prn(ex);
      returnValue = false ;
    }
    return returnValue;
  }

  public DataSet getVtzavtr(String cskl,String vrdok,String god,
                                           int brdok) {

    QueryDataSet QDSVzavtr = new QueryDataSet();
    QDSVzavtr.setResolver(dm.getQresolver());
    QDSVzavtr.setQuery(new QueryDescriptor(dm.getDatabase1(),
        aSS.getQuery4rEDBM4SelectVTzavtr(cskl,vrdok,god,brdok),
        null, true, Load.ALL));
    QDSVzavtr.open();
    return (DataSet) QDSVzavtr;

  }

}
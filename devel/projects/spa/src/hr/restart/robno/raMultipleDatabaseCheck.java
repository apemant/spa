/****license*****************************************************************
**   file: raMultipleDatabaseCheck.java
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

import hr.restart.baza.KreirDrop;
import hr.restart.db.raConnectionFactory;
import hr.restart.db.raPreparedStatement;
import hr.restart.util.VarStr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.ConnectionDescriptor;
import com.borland.dx.sql.dataset.Database;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.dx.sql.dataset.QueryResolver;



public class raMultipleDatabaseCheck {

  public raMultipleDatabaseCheck() {
  }
/*
   database.setConnection(new ConnectionDescriptor(
         "jdbc:interbase://161.53.200.99/home/interbase/maximirska.gdb",
         "REST-ART","bingo",false,"interbase.interclient.Driver",
         ConnectionDescriptor.arrayToProperties(new String[][] {{"charSet", "Cp1250"},})));
  */

  public QueryResolver getQueryResolver(Database db) throws Exception {
    com.borland.dx.sql.dataset.QueryResolver qresolver = new com.borland.dx.sql.dataset.QueryResolver();
    qresolver.setDatabase(db);
    qresolver.setResolverQueryTimeout(0);
    qresolver.setUpdateMode(com.borland.dx.dataset.UpdateMode.KEY_COLUMNS);
    return qresolver;
  }

  public Database getDatabase(String url,String username,String pass,String driver) throws Exception {
      Database database = new Database();
      database.setConnection(new ConnectionDescriptor(
          url,username,pass,false,driver,
          ConnectionDescriptor.arrayToProperties(new String[][] {{"charSet", "Cp1250"},})));
      database.setTransactionIsolation(java.sql.Connection.TRANSACTION_REPEATABLE_READ);
      database.openConnection();
      return database;
  }

  public QueryDataSet getMyNewQueryDataSet(String qS,Database dbb,QueryResolver qresolver, boolean toOpen) {
    QueryDataSet retSet = new QueryDataSet();
    retSet.setQuery(new QueryDescriptor(dbb,qS));
    retSet.setResolver(qresolver);
    if (toOpen) retSet.open();
    return retSet;
  }

  /*
       Usporedjuje dva query data seta po kljucu, ako ne nadje trazi redom po field4check i
       onaj koji se nadje taj racuna da je pravi i chendja target bazu s prometima
       vraæa HashMapu punu hr.restart.preparestatmenta koje treba executirati


  */

/*
  public String getSqlQueryTypeString(Column col,boolean isAnd) {
    String adder = isAnd?" AND ":"";
    int type = col.getDataType();
    if ( type== Variant.BIGDECIMAL){
      return col.getColumnName()+"="+
          +adder;
    } else if (type==Variant.STRING){
      return column+"='"+source.getString(column)+"'"+adder;
    } else if (type==Variant.TIMESTAMP){
      return column+"='"+source.getTimestamp(column)+"'"+adder;
    } else if (type==Variant.INT){
      return column+"="+String.valueOf(source.getInt(column))+adder;
    } else if (type==Variant.SHORT){
      return column+"="+String.valueOf(source.getShort(column))+adder;
    }
*/
//    return "NEPODRŽANO "+Variant.typeName(type);
//  }


  public String getSqlQueryTypeString(QueryDataSet source,String column,boolean isAnd) {
    String adder = isAnd?" AND ":"";
    if (source.isNull(column)) return "";
    int type = source.getColumn(column).getDataType();
    if ( type== Variant.BIGDECIMAL){
      return column+"="+source.getBigDecimal(column).toString()+adder;
    } else if (type==Variant.STRING){
      return column+"='"+source.getString(column)+"'"+adder;
    } else if (type==Variant.TIMESTAMP){
      return column+"='"+source.getTimestamp(column)+"'"+adder;
    } else if (type==Variant.INT){
      return column+"="+String.valueOf(source.getInt(column))+adder;
    } else if (type==Variant.SHORT){
      return column+"="+String.valueOf(source.getShort(column))+adder;
    }
    return "NEPODRŽANO "+Variant.typeName(type);
  }

  public QueryDataSet getQDS(String table_name,String[] fields,String[] values,
                             Database target,QueryResolver qtarget) throws Exception{

    VarStr sqlupit=new VarStr("select * from ").append(table_name).append(" WHERE ");
    for (int i=0;i<fields.length;i++){
    }
    return getMyNewQueryDataSet(sqlupit.toString(),
                                     target,qtarget,true);
  }

  public Column[] getKeysColumn(QueryDataSet qds,String table_name) {
    Object[] obj = getKeysNames(qds.getDatabase(),table_name);

    Column[] cols = new Column[obj.length];
    for (int i=0;i<cols.length;i++) {
      cols[i] = qds.getColumn((String) obj[i]);
    }

    return cols;
  }


  public Object[] getKeysNames(Database db,String table_name) {
    java.util.ArrayList al = new ArrayList();
    try {
      ResultSet rs = db.getMetaData().getPrimaryKeys("","",table_name);
      while(rs.next()){
        al.add(rs.getString("COLUMN_NAME"));
      }
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return al.toArray();
  }

  public raPreparedStatement[] chackTableInMultipleDatabases(
      String table_name,Database source,QueryResolver qsource,
      Database target,QueryResolver qtarget,String[] field4check) {

    ArrayList al = new ArrayList();



    QueryDataSet QDSsource = getMyNewQueryDataSet("select * from "+table_name,
        source,qsource,true);

    if (QDSsource.getRowCount()==0) {
      return null;
    }
    Column[] keys = getKeysColumn(QDSsource,table_name);
    String[] key_names = new String[keys.length];
    for (int i=0;i<key_names.length;i++) {
      key_names[i]=keys[i].getColumnName();
//System.out.println(key_names[i]);
    }

    KreirDrop[] kd_araj = KreirDrop.getModulesWithColumns(key_names);
    String[] vezne_tabele = new  String[kd_araj.length];

    for (int i=0;i<key_names.length;i++) {
      vezne_tabele[i]=kd_araj[i].Naziv;
//System.out.println(vezne_tabele[i]);
    }

    for (QDSsource.first();QDSsource.inBounds();QDSsource.next()){
      QueryDataSet QDStarget = null;
      VarStr sqlupit=new VarStr("select * from ").append(table_name).append(" WHERE ");

      for (int i=0;i<keys.length;i++){
        sqlupit = sqlupit.append(getSqlQueryTypeString(QDSsource,((Column) keys[i]).getColumnName(),i!=keys.length-1));
      }

      QDStarget = getMyNewQueryDataSet(sqlupit.toString(),target,qtarget,true);

      if (QDStarget.getRowCount()==0) {
        if(true) {
          raPreparedStatement[] rpstmp = changeOtherTables();
          for (int i=0 ;i< rpstmp.length;i++) {
            al.add(rpstmp[i]);
          }
        } else {
          raPreparedStatement rPS =
              new raPreparedStatement(table_name,raPreparedStatement.INSERT,target.getJdbcConnection());
          String[] columns = raConnectionFactory.getColumns(source.getJdbcConnection(),table_name);
          for (int i=0 ;i< columns.length;i++) {
            rPS.setValue(columns[i],hr.restart.db.raVariant.getDataSetValue(QDSsource,columns[i]),false);
          }
          al.add(rPS);
        }
      }
    }
    raPreparedStatement[] rps = new raPreparedStatement[al.size()];
    for (int i = 0;i<rps.length;i++) {
      rps[i] = (raPreparedStatement) al.get(i);
    }
    return rps;
  }

  public raPreparedStatement[] changeOtherTables(){
    raPreparedStatement[] rps = new raPreparedStatement[]{};
//    raConnectionFactory.

    return rps;
  }
}
/****license*****************************************************************
**   file: raResultSetMetaData.java
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
package hr.restart.db;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class raResultSetMetaData implements ResultSetMetaData,java.io.Serializable {
  private mtdColumn[] mtdCols;
  private java.util.HashSet unsuported = new java.util.HashSet();
  public raResultSetMetaData(ResultSetMetaData mtd) throws Exception {
    mtdCols = new mtdColumn[mtd.getColumnCount()];
    for (int i = 0; i < mtdCols.length; i++) {
      mtdCols[i] = new mtdColumn();
      fillValues(mtdCols[i],mtd,i+1);
    }
  }
  private void fillValues(mtdColumn mc,ResultSetMetaData mtd,int idx) throws Exception {
    Field[] flds = mc.getClass().getFields();
    for (int i = 0; i < flds.length; i++) {
      String mn = flds[i].getName();
      try {
        Method met = mtd.getClass().getMethod(mn,new Class[] {int.class});
        flds[i].set(mc,met.invoke(mtd,new Object[] {new Integer(idx)}));
      }
      catch (Exception ex) {
//        System.out.println(mn+" ex = "+ex+" "+ex.getMessage());
        unsuported.add(mn);
      }
    }
  }
//  public static void test() {
//    try {
//      ResultSetMetaData rsmtd = raConnectionFactory.getDMConnection().createStatement().executeQuery("SELECT * FROM AGENTI").getMetaData();
//      raResultSetMetaData mtd = new raResultSetMetaData(rsmtd);
//      hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
//      ST.prn(mtd.unsuported.toArray());
//      mtdColumn m = mtd.mtdCols[0];
//      System.out.println("isAutoIncrement          "+m.isAutoIncrement     );
//      System.out.println("isCaseSensitive          "+m.isCaseSensitive     );
//      System.out.println("isSearchable             "+m.isSearchable        );
//      System.out.println("isCurrency               "+m.isCurrency          );
//      System.out.println("isNullable               "+m.isNullable          );
//      System.out.println("isSigned                 "+m.isSigned            );
//      System.out.println("getColumnDisplaySize     "+m.getColumnDisplaySize);
//      System.out.println("getColumnLabel           "+m.getColumnLabel      );
//      System.out.println("getColumnName            "+m.getColumnName       );
//      System.out.println("getSchemaName            "+m.getSchemaName       );
//      System.out.println("getPrecision             "+m.getPrecision        );
//      System.out.println("getScale                 "+m.getScale            );
//      System.out.println("getTableName             "+m.getTableName        );
//      System.out.println("getCatalogName           "+m.getCatalogName      );
//      System.out.println("getColumnType            "+m.getColumnType       );
//      System.out.println("getColumnTypeName        "+m.getColumnTypeName   );
//      System.out.println("isReadOnly               "+m.isReadOnly          );
//      System.out.println("isWritable               "+m.isWritable          );
//      System.out.println("isDefinitelyWritable     "+m.isDefinitelyWritable);
//      System.out.println("getColumnClassName       "+m.getColumnClassName  );
//    }
//    catch (Exception ex) {
//      ex.printStackTrace();
//    }
//  }
  private void suportCheck(String methodName) throws SQLException {
    if (unsuported.contains(methodName)) throw new java.lang.UnsupportedOperationException();
  }
  public int getColumnCount() throws SQLException {
    return mtdCols.length;
  }
  public boolean isAutoIncrement(int column) throws SQLException {
    suportCheck("isAutoIncrement");
    return mtdCols[column-1].isAutoIncrement;
  }
  public boolean isCaseSensitive(int column) throws SQLException {
    suportCheck("isCaseSensitive");
    return mtdCols[column-1].isCaseSensitive;
  }
  public boolean isSearchable(int column) throws SQLException {
    suportCheck("isSearchable");
    return mtdCols[column-1].isSearchable;
  }
  public boolean isCurrency(int column) throws SQLException {
    suportCheck("isCurrency");
    return mtdCols[column-1].isCurrency;
  }
  public int isNullable(int column) throws SQLException {
    suportCheck("isNullable");
    return mtdCols[column-1].isNullable;
  }
  public boolean isSigned(int column) throws SQLException {
    suportCheck("isSigned");
    return mtdCols[column-1].isSigned;
  }
  public int getColumnDisplaySize(int column) throws SQLException {
    suportCheck("getColumnDisplaySize");
    return mtdCols[column-1].getColumnDisplaySize;
  }
  public String getColumnLabel(int column) throws SQLException {
    suportCheck("getColumnLabel");
    return mtdCols[column-1].getColumnLabel;
  }
  public String getColumnName(int column) throws SQLException {
    suportCheck("getColumnName");
    return mtdCols[column-1].getColumnName;
  }
  public String getSchemaName(int column) throws SQLException {
    suportCheck("getSchemaName");
    return mtdCols[column-1].getSchemaName;
  }
  public int getPrecision(int column) throws SQLException {
    suportCheck("getPrecision");
    return mtdCols[column-1].getPrecision;
  }
  public int getScale(int column) throws SQLException {
    suportCheck("getScale");
    return mtdCols[column-1].getScale;
  }
  public String getTableName(int column) throws SQLException {
    suportCheck("getTableName");
    return mtdCols[column-1].getTableName;
  }
  public String getCatalogName(int column) throws SQLException {
    suportCheck("getCatalogName");
    return mtdCols[column-1].getCatalogName;
  }
  public int getColumnType(int column) throws SQLException {
    suportCheck("getColumnType");
    return mtdCols[column-1].getColumnType;
  }
  public String getColumnTypeName(int column) throws SQLException {
    suportCheck("getColumnTypeName");
    return mtdCols[column-1].getColumnTypeName;
  }
  public boolean isReadOnly(int column) throws SQLException {
    suportCheck("isReadOnly");
    return mtdCols[column-1].isReadOnly;
  }
  public boolean isWritable(int column) throws SQLException {
    suportCheck("isWritable");
    return mtdCols[column-1].isWritable;
  }
  public boolean isDefinitelyWritable(int column) throws SQLException {
    suportCheck("isDefinitelyWritable");
    return mtdCols[column-1].isDefinitelyWritable;
  }
  public String getColumnClassName(int column) throws SQLException {
    suportCheck("getColumnClassName");
    return mtdCols[column-1].getColumnClassName;
  }

  class mtdColumn {
    public boolean isAutoIncrement;
    public boolean isCaseSensitive;
    public boolean isSearchable;
    public boolean isCurrency;
    public int isNullable;
    public boolean isSigned;
    public int getColumnDisplaySize;
    public String getColumnLabel;
    public String getColumnName;
    public String getSchemaName;
    public int getPrecision;
    public int getScale;
    public String getTableName;
    public String getCatalogName;
    public int getColumnType;
    public String getColumnTypeName;
    public boolean isReadOnly;
    public boolean isWritable;
    public boolean isDefinitelyWritable;
    public String getColumnClassName;
  }
}



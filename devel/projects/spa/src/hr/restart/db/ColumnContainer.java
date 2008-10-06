/****license*****************************************************************
**   file: ColumnContainer.java
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

import java.sql.ResultSet;
import java.util.LinkedList;

public class ColumnContainer extends LinkedList {
  private static ColumnContainer colContainer;
  protected ColumnContainer() {
  }

  public static void registerColumn(ColumnDef col) {
    try {
      getColumnContainer();
      if (colContainer.contains(col)) return;
      colContainer.add(col);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void registerColumn(String colName, ResultSet rs) {
    ColumnDef newCol = getColumn(colName);
    if (newCol==null) {
      newCol = createColumn(colName, rs);
    }
    registerColumn(newCol);
  }

  public static void registerColumns(ResultSet rs) {
    try {
      java.sql.ResultSetMetaData metadata = rs.getMetaData();
      for (int i=0;i<metadata.getColumnCount();i++) {
        String columnName = metadata.getColumnName(i);
        if (getColumn(columnName) == null) {
          registerColumn(createColumn(columnName,metadata.getColumnType(i),metadata.getColumnDisplaySize(i)));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static ColumnDef createColumn(String colName, ResultSet rs) {
    try {
      java.sql.ResultSetMetaData metadata = rs.getMetaData();
      int idx = rs.findColumn(colName);
      return createColumn(colName,metadata.getColumnType(idx),metadata.getColumnDisplaySize(idx));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private static ColumnDef createColumn(String colName, int sqlType, int width) {
    ColumnDef newCol = new ColumnDef();
    try {
      newCol.setColumnName(colName);
      newCol.setCaption(colName);
      newCol.setSqlType(sqlType);
      newCol.setVisible(true);
      newCol.setWidth(width);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return newCol;
  }

  public static ColumnDef getColumn(String columnName) {
    if (!getColumnContainer()) return null;
    for (int i=0;i<colContainer.size();i++) {
      try {
        ColumnDef c = (ColumnDef)colContainer.get(i);
        if (c.getColumnName().equalsIgnoreCase(columnName)) return c;
      } catch (Exception e) {
      }
    }
    return null;
  }

  public static void registerColumns(ColumnDef[] cols) {
    for (int i=0;i<cols.length;i++) {
      registerColumn(cols[i]);
    }
  }

  private static boolean getColumnContainer() {
    if (colContainer == null) {
      colContainer = new ColumnContainer();
      return false;
    }
    return true;
  }

}
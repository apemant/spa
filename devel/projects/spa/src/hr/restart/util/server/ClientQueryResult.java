/****license*****************************************************************
**   file: ClientQueryResult.java
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
package hr.restart.util.server;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public class ClientQueryResult implements Serializable {
  private Hashtable rows = new Hashtable();
  int rowid = 1;

  private String[] columns;

  public ClientQueryResult() {
  }

  public String[] getColumns() {
    return columns;
  }
  public void setColumns(String[] columns) {
    this.columns = columns;
  }

  public void addRow(String[] values) {
    rows.put(Integer.toString(rowid),new qRow(columns,values));
    rowid ++;
  }

  public void delRow(int rID) {
    rows.remove(Integer.toString(rID));
  }

  public String toString() {
    String str = "";
    for (int i=0;i<columns.length;i++) {
      str = str + columns[i] + " | ";
    }
    str = str + "\n";
    Enumeration Erowids = rows.keys();
    while (Erowids.hasMoreElements()) str = str + rows.get(Erowids.nextElement()).toString();
    return str;
  }

  class qRow implements Serializable {
    Hashtable values = new Hashtable();
    private String[] columns;
    public qRow(String[] clmns,String[] vals) {
      add(clmns,vals);
    }

    private void add(String[] cols,String[] vals) {
      columns = cols;
      values.clear();
      for (int i=0;i<columns.length;i++) {
        try {
          values.put(columns[i],vals[i]);
        }
        catch (Exception ex) {
          values.put(columns[i],"");
        }
      }
    }
    public String toString() {
      String ret = "";
      for (int i=0;i<columns.length;i++) {
        ret = ret + values.get(columns[i]).toString()+" | ";
      }
      ret = ret + "\n";
      return ret;
    }
  }
}

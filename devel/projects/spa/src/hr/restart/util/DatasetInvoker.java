/****license*****************************************************************
**   file: DatasetInvoker.java
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
package hr.restart.util;

public class DatasetInvoker {
  public DatasetInvoker() {
  }

  public void getDataSet(DatasetCommunicator dsComm) {
    Valid vl = Valid.getValid();
    vl.execSQL(dsComm.query);
    vl.RezSet.open();
    hr.restart.util.server.ClientQueryResult qResult = new hr.restart.util.server.ClientQueryResult();
    int colCnt = vl.RezSet.getColumnCount();
    qResult.setColumns(vl.RezSet.getColumnNames(colCnt));
    com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
    for (int i = 0; i< vl.RezSet.getRowCount(); i++) {
      String[] values = new String[colCnt];
      for (int j = 0;j<colCnt;j++) {
        vl.RezSet.getVariant(j,i,v);
        values[j] = v.toString();
      }
      qResult.addRow(values);
    }
    dsComm.dataset = qResult;
  }
}

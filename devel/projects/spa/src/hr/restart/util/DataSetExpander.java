/****license*****************************************************************
**   file: DataSetExpander.java
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

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class DataSetExpander {
  private DataSet data;
  private String keyCol, linkCol;
  private Variant v = new Variant();
  private int keyRow;

  public DataSetExpander(DataSet data, String keyCol, String linkCol) {
    this.data = data;
    this.keyCol = keyCol;
    this.linkCol = linkCol;
  }

  public String expandToList(String val) {
    int ctype = data.getColumn(keyCol).getDataType();
    String delim = (ctype == Variant.STRING || ctype == Variant.TIMESTAMP) ? "'" : "";
    DataSet ds = expand(val);
    if (ds.rowCount() == 0) return null;
    if (ds.rowCount() == 1) {
      ds.getVariant(keyCol, v);
      return keyCol+"="+delim+v+delim;
    }
    VarStr list = new VarStr();
    list.append(keyCol).append(" in (");
    for (ds.first(); ds.inBounds(); ds.next()) {
      ds.getVariant(keyCol, v);
      list.append(delim).append(v).append(delim).append(',');
    }
    return list.chop().append(')').toString();
  }

  public DataSet expand(String val) {
    StorageDataSet dest = new StorageDataSet();
    dest.setColumns(((StorageDataSet) data).cloneColumns());
    dest.open();
    data.open();
    if (!lookupData.getlookupData().raLocate(data, keyCol, val)) return dest;
    keyRow = data.getRow();
    dest.insertRow(false);
    data.copyTo(dest);
    Variant vs = new Variant();
    data.getVariant(keyCol, vs);
    expandImpl(dest, vs);
    return dest;
  }

  private void expandImpl(DataSet dest, Variant vs) {
    for (int i = 0; i < data.rowCount(); i++) {
      data.getVariant(linkCol, i, v);
      if (v.equals(vs) && i != keyRow) {
        dest.insertRow(false);
        data.goToRow(i);
        data.copyTo(dest);
        Variant vn = new Variant();
        data.getVariant(keyCol, vn);
        expandImpl(dest, vn);
      }
    }
  }
}

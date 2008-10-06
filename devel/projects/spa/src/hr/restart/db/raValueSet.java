/****license*****************************************************************
**   file: raValueSet.java
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
import java.io.Serializable;

public abstract class raValueSet implements Serializable {

  private java.util.Hashtable rows = new java.util.Hashtable();
  private boolean open = false;
  private raValueRow currRow = null;
  private raValueRow editRow = null;
  private int currIdx;
  private int rowCount = 0;
  private int lastRowIndex = 0;
  private int firstRowIndex = 0;
  private boolean inbounds = true;
  private String[] colNames;

  public raValueSet() {
  }

  public void finalize() throws Throwable {
    rows = null;
    currRow = null;
    editRow = null;
    colNames = null;
    super.finalize();
  }
/*
  manipulacija
*/

  public void setRows(java.util.Hashtable newRows) throws Exception {
    checkRows(newRows);
    setLastRowIndex(getRowCount());
    setFirstRowIndex(0);
    rows = newRows;
  }

  private void checkRows(java.util.Hashtable newRows) throws Exception {
    String exString = "cannot set rows, rows must have integer key in growing sequence and values must be instances of raValueRow!";
    int rowcnt = 0;
    for (int i = 0; i < newRows.size(); i++) {
      raValueRow row;
      try {
        row = (raValueRow)newRows.get(new Integer(i+1));
      }
      catch (Exception ex) {
        throw new IllegalArgumentException(ex.getMessage().concat(" -> ").concat(exString));
      }
      if (row == null) throw new IllegalArgumentException(exString);
      if (row.getState()!='D') rowcnt = rowcnt+1;
    }
    rowCount = rowcnt;
  }

  public java.util.Hashtable getRows() {
    return rows;
  }

  public void insertRow() {
    post();
    if (currRow != null) editRow = currRow.getNew();
    else {
      editRow = new raValueRow();
      editRow.setValues(new Object[colNames.length]);
    }
  }

  public void deleteRow() {
    deleteRow(true);
  }

  public void deleteRow(boolean move) {
    if (getRowCount() == 0) {
      currRow = null;
      editRow = null;
    }
    if (currRow == null) return;
    if (editRow != null) cancel();
    currRow.setState('D');
    rows.put(new Integer(currIdx),currRow);
    rowCount = rowCount - 1;
    if (move)
      if (!next()) last();
  }

  public void deleteAll() {
    first();
    do {
      deleteRow(false);
    } while (next());
  }

  public void clearAllRows() {
    rows.clear();
  }

  public void addRow(raValueRow row) {
    int newIdx = rows.size()+1;
    rows.put(new Integer(newIdx),row);
    if (row.getState()!='D') rowCount = rowCount + 1;
    setLastRowIndex(rowCount);
  }

  public void editRow() {
    post();
    editRow = (raValueRow)currRow.clone();
  }

  public void post() {
    if (editRow == null) return;
    //???? ako je dodao, pa postao, pa editirao pa postao i sve bez saveChanges
    char state = currRow.getState();
    if (editRow.getState()=='I') {
      currIdx = rows.size()+1;
      state = 'A';
    } else if (currRow.getState()==' ') {
      state = 'U';
    }
    editRow.setState(state);
    rows.put(new Integer(currIdx),editRow);
    if (editRow.getState() == 'A') rowCount = rowCount + 1;
    currRow = editRow;
    editRow = null;
  }

  public void cancel() {
    editRow = null;
  }

  public void refresh() {
    close();
    try {
      open();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public abstract void open() throws Exception;

  public abstract void saveChanges() throws Exception;

  public abstract boolean moreData() throws Exception;

  public void close() {
    open = false;
  }
  public void setOpenFlag() {
    open = true;
  }
  public void setValue(int idx, Object value) {
    if (editRow == null) editRow(); // automatski ulazi u edit ako vec nije
    if (editRow != null) {
      editRow.setValue(idx,value);
    }
  }

  public Object getValue(int idx) {
    if (editRow != null) return editRow.getValue(idx);
    if (currRow != null) return currRow.getValue(idx);
    return null;
  }

  public void setValue(String columnName, Object value) {
    int idx = findColumn(columnName);
    if (idx == -1) return;
    setValue(idx,value);
  }

  public Object getValue(String columnName) {
    int idx = findColumn(columnName);
    if (idx == -1) return null;
    return getValue(idx);
  }

  public void first() {
    goToRow(1);
  }

  public void last() {
    goToRow(rows.size(),false);
  }

  public boolean next() {
    return goToRow(currIdx+1);
  }

  public boolean prev() {
    return goToRow(currIdx-1,false);
  }

  public boolean prior() {
    return prev();
  }

  public boolean inBounds() {
    return inbounds;
  }
  public boolean goToRow(int idx) {
    return goToRow(idx,true);
  }
  public boolean goToRow(int idx, boolean searchNext) {
    if (getRowCount() == 0) {
      currRow = null;
      editRow = null;
      return false;
    }
    if (editRow!=null) post();
/** @todo implementirati nekako moreData, prohandlati sa idx+firstRowindex i svu zbrku dovesti u red
 * sto sa izmjenjenim redovima van cache-a ?
 */
    if (!checkRowExists(idx)) return false;
    raValueRow tmpRow = getAvailableRow(idx, searchNext);
    if (tmpRow == null) {
      return false;
    } else {
      currRow = tmpRow;
      return true;
    }
  }

  private raValueRow getAvailableRow(int idx, boolean searchNext) {
    if (!checkRowExists(idx)) return null;
    raValueRow tmpRow = (raValueRow)rows.get(new Integer(idx));
    if (tmpRow.getState() != 'D') {
      currIdx = idx;
      return tmpRow;
    } else {
      int newidx = searchNext?idx+1:idx-1;
      return getAvailableRow(newidx,searchNext);
    }
  }

  private boolean checkRowExists(int idx) {
    inbounds = (idx <= rows.size());
    return inbounds;
  }

/*
  informacija
*/
  public boolean isOpen() {
    return open;
  }

  public String[] getColNames() {
    return colNames;
  }

  public void setColNames(String[] colnames) {
    colNames = colnames;
  }

  public int findColumn(String columnName) {
    try {
      for (int i = 0; i < colNames.length; i++) {
        if (colNames[i].equals(columnName)) return i;
      }
    }
    catch (Exception ex) {
      return -1;
    }
    return -1;
  }

  public int getRowCount() {
    return rowCount;
  }

  public void setRowCount(int rowCnt) {
    rowCount = rowCnt;
  }

  public void setLastRowIndex(int lrIdx) {
    lastRowIndex = lrIdx;
  }

  public int getFirstRowIndex() {
    return firstRowIndex;
  }
  public void setFirstRowIndex(int frIdx) {
    firstRowIndex = frIdx;
  }

  public int getLastRowIndex() {
    return lastRowIndex;
  }


  public int getColumnCount() {
    try {
      return colNames.length;
    }
    catch (Exception ex) {
      return 0;
    }
  }

/*
  tostring
*/

  public String print() {
    String str=toStringHeader().concat("\n");
    first();
    do {
      str = str.concat(printRow(currRow)).concat("\n");
    } while (next());
    return str;
  }

  public String printAll() {
    String str=toStringHeader().concat("\n");
    for (int i = 0; i < rows.size(); i++) {
      String rowstr = printRow((raValueRow)rows.get(new Integer(i+1)));
      if (rowstr == null) System.out.println("row "+i+" is null!");
      else
      str = str.concat(rowstr).concat("\n");
    }
    return str;
  }
  public String printCurrentRow() {
    String str=toStringHeader().concat("\n");
    if (currRow != null) str = str.concat(printRow(currRow));
    return str;
  }

  public String printRow(raValueRow row) {
    if (row == null) return "null";
    String str="(state:"+row.getState()+") ";
    try {
      for (int i = 0; i < colNames.length; i++) {
        Object oVal = row.getValue(i);
        String strVal = "null";
        if (oVal != null) strVal = oVal.toString();
        str = str.concat(strVal).concat(" | ");
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return str;
  }
  public String toStringHeader() {
    String str="";
    try {
      for (int i = 0; i < colNames.length; i++) {
        str = str.concat(colNames[i]).concat(" | ");
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return str;
  }

}

/****license*****************************************************************
**   file: raDynamicTableData.java
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
package hr.restart.util.reports;

import javax.swing.JLabel;

import hr.restart.util.VarStr;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raDynamicTableData {

  private repDynamicProvider dp;
  private int currentRow;

  public raDynamicTableData(int rownum) {
    currentRow = rownum;
    dp = repDynamicProvider.getInstance();
  }

  public String getGroup(int n) {
    try {
      return dp.getGroup(currentRow, n);
    } catch (Exception e) {
      return "";
    }
  }

  public String getGroupCaption(int n) {
    try {
      return dp.getGroupCaption(n);
    } catch (Exception e) {
      return "";
    }
  }

  public String getGroupValue(int n) {
    try {
      return dp.getGroupValue(currentRow, n);
    } catch (Exception e) {
      return "";
    }
  }

  public String getDataValue(int col) {
    if (col >= dp.jt.getColumnCount()) return "";
    try {
      return dp.getValueAt(currentRow, col);
    } catch (Exception e) {
      return "";
    }
  }

  public double getDataNum(int col) {
    if (col >= dp.jt.getColumnCount()) return 0;
    try {
      return dp.getDoubleAt(currentRow, col);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  public String getHeaderValue(int col) {
    if (col >= dp.jt.getColumnCount()) return "";
    try {
      return dp.table.getColumnModel().getColumn(col).getHeaderValue().toString();
    } catch (Exception e) {
      return "";
    }
  }
  
  public String getSummaryHead(int col) {
    if (col >= dp.summ.getColumnCount()) return "";
    try {
      return dp.summ.getColumnModel().getColumn(col).getHeaderValue().toString();
    } catch (Exception e) {
      return "";
    }
  }
  
  public String getSummaryData(int col) {
    if (col >= dp.summ.getColumnCount()) return "";
    try {
      VarStr v = new VarStr();
      for (int i = 0; i < dp.summ.getRowCount(); i++) {
        v.append(((JLabel) dp.summ.getDefaultRenderer(Object.class).
          getTableCellRendererComponent(dp.summ, 
              dp.summ.getValueAt(i, col), false, false, i, col)).
              getText()).append('\n');
      }
      return v.toString();
    } catch (Exception e) {
      return "";
    }
  }

  public String getSumValue(int col) {
    if (col >= dp.jt.getColumnCount()) return "";
    try {
      return dp.getValueAt(dp.table.getRowCount() - 1, col);
    } catch (Exception e) {
      return "";
    }
  }

  public String getTitle() {
    return dp.getTitle();
  }

  public String getSubTitle() {
    return dp.getSubtitle();
  }

  public int getLogo() {
    return (dp.test() ? 1 : 0);
  }

  public String getFirstLine(){
    return dp.test() ? dp.rpm.getFirstLine() : "";
  }
  public String getSecondLine(){
    return dp.test() ? dp.rpm.getSecondLine() : "";
  }
  public String getThirdLine(){
    return dp.test() ? dp.rpm.getThirdLine() : "";
  }
  public String getDatumIsp(){
    return dp.test() ? dp.rdu.dataFormatter(dp.vl.getToday()) : "";
  }
}

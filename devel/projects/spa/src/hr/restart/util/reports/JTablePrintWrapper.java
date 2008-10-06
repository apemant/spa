/****license*****************************************************************
**   file: JTablePrintWrapper.java
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

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

import hr.restart.swing.JraTable2;
import hr.restart.util.lookupData;
import hr.restart.util.raRowSume;
import hr.restart.util.columnsbean.ColumnsBean;
import hr.restart.zapod.OrgStr;

public class JTablePrintWrapper implements JTablePrintWrapperInterface {

  public JTablePrintWrapper(ColumnsBean cB,int row,String Title) {
    setTableTitle(Title);
    setColBean(cB);
    currRow=row;
  }
  private OrgStr orgs = OrgStr.getOrgStr();
  private String tableTitle;
  private String tableHeader;
  private String tableRow;
  private ColumnsBean colBean;
  private javax.swing.table.TableColumnModel cModel;//static
//  private static javax.swing.JTable cTable;
  private hr.restart.swing.JraTable2 cTable; //static
  private int currRow;//static

  private int getHorAligment(int row,int col) {
    try {
      javax.swing.JTable jt = (javax.swing.JTable)cTable;
      return ((javax.swing.table.DefaultTableCellRenderer)jt.getDefaultRenderer(Object.class).getTableCellRendererComponent(jt,jt.getValueAt(row,col),false,false,row,col)).getHorizontalAlignment();
    }
    catch (Exception ex) {
      System.out.println("getHorAligment ex: "+ex);
      return javax.swing.SwingConstants.LEFT;
    }
  }
  private String getTableColValue(int row, int col) {
    javax.swing.JTable jt = (javax.swing.JTable)cTable;
    java.awt.Component renderComp = ((javax.swing.table.DefaultTableCellRenderer)jt.getDefaultRenderer(Object.class)
      .getTableCellRendererComponent(jt,jt.getValueAt(row,col),false,false,row,col));
    if (renderComp instanceof javax.swing.JLabel)
      return ((javax.swing.JLabel)renderComp).getText();
    if (renderComp instanceof javax.swing.text.JTextComponent)
      return ((javax.swing.text.JTextComponent)renderComp).getText();
    if (renderComp instanceof javax.swing.JCheckBox) {
      javax.swing.JCheckBox cb = (javax.swing.JCheckBox)renderComp;
      if (cb.isSelected()) return "O";
      else return "X";
    }
    return (String)cTable.getValueAt(row,col).toString();
  }
  private double getKoef() {
    return 108.00/cModel.getTotalColumnWidth();
  }

  private int getColWidth(int i) {
    double td =
      //java.lang.Math.floor(
      ((double)cModel.getColumn(i).getWidth())*getKoef();
      //);
    return (int)td-1;
  }

  public String getDatumIsp() {
    return hr.restart.robno.raDateUtil.getraDateUtil().dataFormatter(hr.restart.util.Valid.getValid().getToday());
  }

  public String getTableTitle() {
    //format
    tableTitle="\n ".concat(tableTitle.toUpperCase());

    return tableTitle;
  }

  public String getMemo() {
    lookupData.getlookupData().raLocate(orgs.getKnjigovodstva(), "CORG", orgs.getKNJCORG(false));
    return orgs.getKnjigovodstva().getString("NAZIV");
  }

  public String getTableHeader() {
    tableHeader="";
    for (int i=0;i<cModel.getColumnCount();i++) {
      tableHeader=tableHeader.concat(
          hr.restart.util.Util.getUtil().alignString(
              cModel.getColumn(i).getHeaderValue().toString().toUpperCase(),
              getColWidth(i),
              getHorAligment(0,i)
            )
        ).concat(" ");
    }
    return tableHeader;
  }

  public String getTableRow() {
    tableRow="";
    String colValue;
    int wdth = 15;
    int alig = javax.swing.SwingConstants.LEFT;
    for (int i=0;i<cModel.getColumnCount();i++) {
      try {
        String tmpColValue = getTableColValue(currRow,i);
        wdth = getColWidth(i);
        alig = getHorAligment(currRow,i);
        colValue = hr.restart.util.Util.getUtil().alignString(tmpColValue,wdth,alig);
      } catch (Exception e) {
        colValue="";
      }
      tableRow=tableRow.concat(colValue).concat(" ");
    }
    return tableRow;
  }

  public String getColDelimiters() {
    String colDelimiters = "";
    for (int i=0;i<cModel.getColumnCount();i++) {
      colDelimiters=colDelimiters.concat(toAlpha(" ",getColWidth(i))).concat(" | ");
    }
    return colDelimiters;
  }

  public String getTableTotals() {
    String tableTotals = "";
    String sumValue;
    if (colBean.getSumRow()==null) return "";
    raRowSume srow = colBean.getSumRow();
    for (int i=0;i<cModel.getColumnCount();i++) {
      try {
        String tmpSumValue = ((hr.restart.swing.JraLabel)srow.getComponent(i)).getText();
        int wdth = getColWidth(i);
        int alig = getHorAligment(0,i);
        sumValue = hr.restart.util.Util.getUtil().alignString(tmpSumValue,wdth,alig);
      } catch (Exception e) {
        System.out.println("getTableTotals :: "+e);
        sumValue="";
      }
      tableTotals=tableTotals.concat(sumValue).concat(" ");
    }
    return tableTotals;
  }

  public String getDataCol(int colOrdinal) {
    try {
      String colValue = (String)cTable.getValueAt(currRow,colOrdinal).toString();
      //format
      colValue = colValue.trim();
      return colValue;
    } catch (Exception e) {
//      e.printStackTrace(); vecinom IndexOut of bounds
      return "";
    }
  }

  public String getHeadCol(int colOrdinal) {
    try {
      String colHeader = cModel.getColumn(colOrdinal).getHeaderValue().toString();
      //format
      colHeader = colHeader.toUpperCase();
      return colHeader;
    } catch (Exception e) {
      return "";
    }
  }

  public void setColBean(ColumnsBean newColBean) {
    colBean=newColBean;
    cTable = (JraTable2) colBean.getRaJdbTable();
    cModel = cTable.getColumnModel();
  }

  public void setTableTitle(String newTableTitle) {
    tableTitle = newTableTitle;
  }

  private String toAlpha(String str,int len) {
    char[] C = new char[len];
    for (int i=0;i<len;i++) {
      C[i]=' ';
    }
    String S = new String(C);
    S = str.concat(S).substring(0,len);
    return S;
  }

  public java.util.Enumeration getEnumeration () {
    return new EnumArray(colBean,tableTitle);
  }

  public int getCurrRow() {
    return currRow;
  }
  public String getDataCol00() {
    return getDataCol(0);
  }
  public String getDataCol01() {
    return getDataCol(1);
  }
  public String getDataCol02() {
    return getDataCol(2);
  }
  public String getDataCol03() {
    return getDataCol(3);
  }
  public String getDataCol04() {
    return getDataCol(4);
  }
  public String getDataCol05() {
    return getDataCol(5);
  }
  public String getDataCol06() {
    return getDataCol(6);
  }
  public String getDataCol07() {
    return getDataCol(7);
  }
  public String getDataCol08() {
    return getDataCol(8);
  }
  public String getDataCol09() {
    return getDataCol(9);
  }
  public String getDataCol10() {
    return getDataCol(10);
  }
  public String getDataCol11() {
    return getDataCol(11);
  }
  public String getDataCol12() {
    return getDataCol(12);
  }
  public String getDataCol13() {
    return getDataCol(13);
  }
  public String getDataCol14() {
    return getDataCol(14);
  }
  public String getDataCol15() {
    return getDataCol(15);
  }
  public String getDataCol16() {
    return getDataCol(16);
  }
  public String getDataCol17() {
    return getDataCol(17);
  }
  public String getDataCol18() {
    return getDataCol(18);
  }
  public String getDataCol19() {
    return getDataCol(19);
  }
  public String getDataCol20() {
    return getDataCol(20);
  }

  public String getHeadCol00() {
    return getHeadCol(0);
  }
  public String getHeadCol01() {
    return getHeadCol(1);
  }
  public String getHeadCol02() {
    return getHeadCol(2);
  }
  public String getHeadCol03() {
    return getHeadCol(3);
  }
  public String getHeadCol04() {
    return getHeadCol(4);
  }
  public String getHeadCol05() {
    return getHeadCol(5);
  }
  public String getHeadCol06() {
    return getHeadCol(6);
  }
  public String getHeadCol07() {
    return getHeadCol(7);
  }
  public String getHeadCol08() {
    return getHeadCol(8);
  }
  public String getHeadCol09() {
    return getHeadCol(9);
  }
  public String getHeadCol10() {
    return getHeadCol(10);
  }
  public String getHeadCol11() {
    return getHeadCol(11);
  }
  public String getHeadCol12() {
    return getHeadCol(12);
  }
  public String getHeadCol13() {
    return getHeadCol(13);
  }
  public String getHeadCol14() {
    return getHeadCol(14);
  }
  public String getHeadCol15() {
    return getHeadCol(15);
  }
  public String getHeadCol16() {
    return getHeadCol(16);
  }
  public String getHeadCol17() {
    return getHeadCol(17);
  }
  public String getHeadCol18() {
    return getHeadCol(18);
  }
  public String getHeadCol19() {
    return getHeadCol(19);
  }
  public String getHeadCol20() {
    return getHeadCol(20);
  }

  final static class EnumArray implements java.util.Enumeration {
    private ColumnsBean cBean;
    private String tTitle;
    EnumArray(ColumnsBean clBean,String tbTitle) {
      cBean=clBean;
      tTitle=tbTitle;
      arrIdx=0;
    }
    public boolean hasMoreElements()  {
//      if (arrIdx > 100) return false;
      return arrIdx < cBean.getRaJdbTable().getRowCount();
    }
    public Object nextElement() {
      if (!hasMoreElements()) return null;
        return new JTablePrintWrapper(cBean,arrIdx++,tTitle);
    }
    private int arrIdx = 0;
  }

}

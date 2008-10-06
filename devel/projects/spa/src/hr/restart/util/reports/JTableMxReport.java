/****license*****************************************************************
**   file: JTableMxReport.java
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

public class JTableMxReport extends mxReport {
//hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  JTablePrintWrapper wrapper;
  com.borland.dx.dataset.Column[] tabCols;
  public JTableMxReport() {
    init();
  }
  private void init() {
    setDocument(mxDocument.getDefaultMxDocument());
    setPrinter(mxPrinter.getDefaultMxPrinter());//za sada
    mxRM mxrm = mxRM.getDefaultMxRM();
    hr.restart.baza.dM.getDataModule().getMxPrinterRM().open();
    mxrm.init(hr.restart.baza.dM.getDataModule().getMxPrinterRM());
    setRM(mxrm);
  }
  public void setDataObject(JTablePrintProvider jtpp) {
    super.setDataObject(jtpp);
    wrapper = jtpp.JTPW;
    tabCols = jtpp.columnsB.getColumnsInTable();
    createReport();
  }
  private void createReport() {
    hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
    String pgHead = "<#"+wrapper.getTableTitle().trim()+"|80|center#><$newline$>";
    String detail = "";
    for (int i=0;i<tabCols.length;i++) {
      String alignment = getStringFromDataAlignment(tabCols[i].getAlignment());
      String length = Integer.toString(tabCols[i].getWidth()+1);
//      pgHead = pgHead+"<#"+tabCols[i].getCaption()+"|"+length+"|"+alignment+"#>";
      pgHead = pgHead+"<#HeadCol"+vl.maskZeroInteger(new Integer(i),2)+"|"+length+"|"+alignment+"#> ";
      detail = detail+"<#DataCol"+vl.maskZeroInteger(new Integer(i),2)+"|"+length+"|"+alignment+"#> ";
    }
    setPgHeader(pgHead);
    setDetail(new String[] {detail});
  }
}


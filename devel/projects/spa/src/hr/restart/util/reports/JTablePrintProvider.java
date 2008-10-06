/****license*****************************************************************
**   file: JTablePrintProvider.java
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

import hr.restart.util.columnsbean.ColumnsBean;

import java.util.Enumeration;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class JTablePrintProvider implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  ColumnsBean columnsB; //static
  private String repTitle; //static
  JTablePrintWrapper JTPW;

  public JTablePrintProvider() {
  }

  public Enumeration getData() {
    return JTPW.getEnumeration();
  }

  public void close() {

  }

  public void setColumnsB(ColumnsBean newColumnsB) {
    columnsB=newColumnsB;
    JTPW = new JTablePrintWrapper(columnsB,0,repTitle);
  }

  public void setRepTitle(String newRepTitle) {
    if (newRepTitle != null) repTitle=newRepTitle;
    if (repTitle == null) repTitle = "Ispis tablice";
  }
}
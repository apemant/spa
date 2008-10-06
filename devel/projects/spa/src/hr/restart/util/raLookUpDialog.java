/****license*****************************************************************
**   file: raLookUpDialog.java
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

import hr.restart.swing.JraDialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import com.borland.dbswing.JdbTable;
import com.borland.dbswing.TableScrollPane;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class raLookUpDialog extends JraDialog {
  TableScrollPane tableScrollPane1 = new TableScrollPane();
  JdbTable jdbTable1 = new JdbTable() {
    public boolean isCellSelected(int row, int column) {
    return isRowSelected(row);
    }
  };
//  hr.restart.util.OKpanel okPan = new hr.restart.util.OKpanel();
  private com.borland.dx.sql.dataset.QueryDataSet raDataSet;
//  ColumnsBean columnsBean1 = new ColumnsBean();

  public raLookUpDialog() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    this.setModal(true);
    jdbTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
    jdbTable1.setColumnHeaderVisible(false);
    jdbTable1.setRowHeaderVisible(false);
//    columnsBean1.setRaJdbTable(jdbTable1);
//    columnsBean1.setAditionalCols(new int[] {2,3});
//    columnsBean1.setRmvAdditionalCols(false);
//    this.getContentPane().add(okPan, BorderLayout.SOUTH);
    this.getContentPane().add(tableScrollPane1, BorderLayout.CENTER);
//    this.getContentPane().add(columnsBean1, BorderLayout.NORTH);
    tableScrollPane1.getViewport().add(jdbTable1, null);
  }
  public void setRaDataSet(com.borland.dx.sql.dataset.QueryDataSet newRaDataSet) {
    raDataSet = newRaDataSet;
    jdbTable1.setDataSet(raDataSet);
  }
  public com.borland.dx.sql.dataset.QueryDataSet getRaDataSet() {
    return raDataSet;
  }
  void ShowCenter(boolean isCentered, int inWidth, int inHeigth) {
  /**
   * Metoda koja prikazuje frame na centru ekrana;
   * Ako je isCentered = true - centrira
   * Ako nije onda sa inWidth i inHeigth
  */
    this.pack();
    if (isCentered) {
      // center frame on screen (AI)
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension winSize = this.getSize();
      if (winSize.height > screenSize.height)
        winSize.height = screenSize.height;
      if (winSize.width > screenSize.width)
        winSize.width = screenSize.width;
      this.setLocation((screenSize.width - winSize.width) / 2, (screenSize.height - winSize.height) / 2);
    }
    else {
      this.setLocation(inWidth,inHeigth);
    }
    this.setVisible(true);
  }
}
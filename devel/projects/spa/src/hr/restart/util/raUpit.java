/****license*****************************************************************
**   file: raUpit.java
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

import java.awt.BorderLayout;

import com.borland.dx.dataset.StorageDataSet;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public abstract class raUpit extends hr.restart.util.raUpitLite {
  hr.restart.util.raJPTableView jptv;

  private void createJPTV(boolean nav) {
    jptv = new hr.restart.util.raJPTableView(nav) {
      public void mpTable_killFocus(java.util.EventObject e) {        
        okp.jPrekid.requestFocus();
      }
      public void mpTable_doubleClicked() {
        jptv_doubleClick();
      }
      void createColumnsBean() {
        super.createColumnsBean();
        columnsCreated();
      }
    };
  }

  protected void columnsCreated() {
    // for override
  }

  protected boolean createNavBar() {
    return false;
  }
  
  protected void upitCompleted() {
    rcc.setLabelLaF(this.okp.jReset, false);
    jptv.getMpTable().requestFocus();
  }

  public raUpit() {
    createJPTV(createNavBar());
    try {
      jInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jInit() throws Exception {
    this.getContentPane().add(jptv, BorderLayout.CENTER);
    this.jptv.initKeyListener(this);
    this.jptv.getNavBar().unregisterNavBarKeys(this);
  }
/**
 * Dohvat jpTableView-a
 * @return
 */
  public hr.restart.util.raJPTableView getJPTV() {
    return this.jptv;
  }
  public hr.restart.swing.JraTableInterface getTable() {
    return jptv.getMpTable();
  }
  private int oldRow = -1;
  protected void enableEvents(boolean en) {
    if (!en && oldRow == -1 && jptv.getStorageDataSet() != null)
      oldRow = jptv.getStorageDataSet().getRow();
    else if (en && oldRow != -1 && jptv.getStorageDataSet() != null) {
      jptv.getStorageDataSet().goToClosestRow(oldRow);
      oldRow = -1;
    }
    jptv.enableEvents(en);
//    jptv.setIgnoreRepaint(!en);
//    else
//      SwingUtilities.invokeLater(new Runnable() {
//        public void run() {
//          ((JraTable2) jptv.getMpTable()).showSumRow();
//          jptv.setIgnoreRepaint(false);
//          jptv.repaint();
//        }
//      });
//
  }
/**
 * Overrajdanje za double click
 */
  public void jptv_doubleClick() {
  }

  public void keyF6Press(){
    try {
      jptv_doubleClick();
    }
    catch (Exception ex) {
      System.out.println("stiskas na prazno");
    }
  }

  public void cancelPress() {
    super.cancelPress();
    getJPTV().clearDataSet();
  }

  public boolean isIspis() {
    return jptv.getMpTable().getDataSet()!=null;
  }
  
  public void setDataSet(StorageDataSet ds) {
    jptv.setDataSet(ds);
  }
  
  public void setDataSetAndSums(StorageDataSet ds, String[] cols) {
    jptv.setDataSetAndSums(ds, cols);
  }
}
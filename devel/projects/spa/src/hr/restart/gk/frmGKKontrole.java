/****license*****************************************************************
**   file: frmGKKontrole.java
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
package hr.restart.gk;

import hr.restart.util.OKpanel;
import hr.restart.util.raFrame;
import hr.restart.util.raJPTableView;
import hr.restart.util.raStatusBar;

import java.awt.BorderLayout;

public abstract class frmGKKontrole extends raFrame {
  private com.borland.dx.dataset.StorageDataSet dataSet;
  int steps = 10;
  String msg = "Radim ...";
  private boolean repair = false;
  raJPTableView jptv = new raJPTableView();
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      my_ok_action();
    }
    public void jPrekid_actionPerformed() {
      cancel_action();
    }
  };
  public frmGKKontrole() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    setOkProvjera();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(jptv,BorderLayout.CENTER);
    getContentPane().add(okp,BorderLayout.SOUTH);
    okp.registerOKPanelKeys(this);
    jptv.initKeyListener(this);
    if (!hr.restart.start.isMainFrame()) {
      java.awt.Dimension size = new java.awt.Dimension(hr.restart.start.getSCREENSIZE().width/2,jptv.getPreferredSize().height);
      System.out.println("size = "+size);
      jptv.setPreferredSize(size);
    }
  }
  public void setDataSet(com.borland.dx.dataset.StorageDataSet ds) {
    dataSet = ds;
    jptv.setDataSet(dataSet);
    jptv.fireTableDataChanged();
    jptv.getColumnsBean().initialize();
  }
  public void show() {
    before_show();
    repair = false;
    setOkProvjera();
    jptv.destroy_kum();
    super.show();
  }
  public void setOkProvjera() {
    okp.jBOK.setText("Provjera");
  }
  public void setOkPopravak() {
    okp.jBOK.setText("Popravak");
  }
  public void before_show() {
  }
  private void my_ok_action() {
    if (repair) {
      before_repair();
      new repareThread().start();
    } else {
      before_check();
      new checkThread().start();
    }
  }
  public void before_check() {
  }
  public void before_repair() {
  }
  public abstract void check_action();
  public abstract void repare_action();
  public void cancel_action() {
    hide();
  }

  raStatusBar status;

  private void startTask(int _steps, String _msg) {
    setEnabled(false);
    status = raStatusBar.getStatusBar();
    status.getProgressBar().setDelay(100);
    status.startTask(_steps,_msg);
  }
  private void finnishTask() {
    status.finnishTask();
    setEnabled(true);
  }
  class checkThread extends Thread {
    public void run() {
      startTask(steps,msg);
      try {
        check_action();
        if (dataSet != null) if (dataSet.getRowCount()>0) {
          setOkPopravak();
          repair = true;
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      finnishTask();
    }
  };
  class repareThread extends Thread {
    public void run() {
      startTask(steps,msg);
      try {
        repare_action();
        setOkProvjera();
        repair = false;
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      finnishTask();
    }
  };
}
/****license*****************************************************************
**   file: frmClassState.java
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
package hr.restart.sisfun;

import hr.restart.baza.dM;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raExtendedTable;
import hr.restart.util.raFrame;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;
import hr.restart.util.raNavAction;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class frmClassState extends raFrame {
  private raProfiler prof = raProfiler.getInstance();
  private raJPTableView jp = new raJPTableView();
  private StorageDataSet ds = new StorageDataSet() {
    public boolean refreshSupported() {
      return true;
    }
    public void refresh() {
      refreshData();
    }
  };

  JraTable2 mpt = new raExtendedTable() {
    public void killFocus(java.util.EventObject e) {
      jp.mpTable_killFocus(e);
    }
    public void tableDoubleClicked() {
      jp.mpTable_doubleClicked();
    }
  };


  private JPanel down = new JPanel();
  private JPanel left = new JPanel();
  private JLabel mem = new JLabel();
  private JLabel mem2 = new JLabel();

  public frmClassState() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setDataSet() {
    ds.setColumns(new Column[] {
      dM.createStringColumn("NAME", "Ime klase", 100),
      dM.createIntColumn("SIZE", "Zauze\u0107e"),
      dM.createIntColumn("TOTAL", "Ukupno"),
      dM.createIntColumn("INSTANCES", "Instanci"),
      dM.createIntColumn("DIFFERENCE", "Razlika"),
      dM.createIntColumn("ALLOCATIONS", "Promet")
    });
    ds.open();
  }

  public void setData() {
    prof.getState();
    ds.empty();
    for (Iterator i = prof.getClasses().keySet().iterator(); i.hasNext(); ) {
      String name = (String) i.next();
      ds.insertRow(false);
      ds.setString("NAME", name);
      raProfiler.ClassData cd = (raProfiler.ClassData) prof.getClasses().get(name);
      ds.setInt("SIZE", Math.round(1.0f * cd.size / cd.instances));
      ds.setInt("TOTAL", cd.size);
      ds.setInt("INSTANCES", cd.instances);
      ds.setInt("ALLOCATIONS", cd.allocations);
      ds.setInt("DIFFERENCE", cd.difference);
    }
    ds.post();
    ds.first();
    StringBuffer s = new StringBuffer();
    s.append(Math.round(1.0f * Runtime.getRuntime().totalMemory() / 1024));
    s.append(" ( ");
    s.append(Math.round(1.0f * Runtime.getRuntime().freeMemory() / 1024));
    s.append(" )  Kb");
    mem2.setText(s.toString());
  }

  public void refreshData() {
    /** @todo andrej implementirati u jpTableView nekako */
//    jp.setKumTak(true);
//    jp.setDataSet(null);
//    jp.setStoZbrojiti(new String[] {});
//    jp.setKumTak(false);
    jp.enableEvents(false);
    setData();
//    jp.setDataSet(ds);
    jp.enableEvents(true);
    jp.fireTableDataChanged();
  }

  public void show() {
    setData();
    super.show();
    jp.fireTableDataChanged();
  }

  private void jbInit() throws Exception {
    try {
      Field fmp = raJPTableView.class.getDeclaredField("mpTable");
      fmp.setAccessible(true);
      fmp.set(jp, mpt);
      Field fj = raJPTableView.class.getDeclaredField("jScrollPaneTable");
      fj.setAccessible(true);
      ((JraScrollPane) fj.get(jp)).getViewport().setView(mpt);
    } catch (Exception e) {
      e.printStackTrace();
    }

    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(jp, BorderLayout.CENTER);
    this.getContentPane().add(down, BorderLayout.SOUTH);
    mem.setText("Ukupno memorije (slobodno): ");
    down.setLayout(new BorderLayout());
    down.add(left, BorderLayout.WEST);
    left.add(mem);
    left.add(mem2);
    jp.setBorder(BorderFactory.createEtchedBorder());
    jp.getNavBar().getColBean().setSaveSettings(false);
    jp.initKeyListener(this);
    setDataSet();
    jp.setDataSet(ds);
    this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 320, 200);
    jp.getNavBar().addOption(new raNavAction("Zapamti stanje", raImages.IMGIMPORT, KeyEvent.VK_S | KeyEvent.CTRL_MASK) {
      public void actionPerformed(ActionEvent e) {
        prof.saveState();
        refreshData();
      }
    });
    jp.getNavBar().addOption(new raNavAction("GC", raImages.IMGDELALL, KeyEvent.VK_G | KeyEvent.CTRL_MASK) {
      public void actionPerformed(ActionEvent e) {
        System.gc();
        refreshData();
      }
    });
    jp.getNavBar().addOption(new raNavAction("Dump", raImages.IMGEXPORT, KeyEvent.VK_D | KeyEvent.CTRL_MASK) {
      public void actionPerformed(ActionEvent e) {
        prof.dumpClasses("dump.txt");
      }
    });
    jp.getNavBar().addOption(new raNavAction("Izlaz", raImages.IMGX, KeyEvent.VK_ESCAPE) {
      public void actionPerformed(ActionEvent e) {
        frmClassState.this.hide();
      }
    });
    jp.getNavBar().getColBean().setSaveSettings(false);
    jp.initKeyListener(this);
  }
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {}

    frmClassState fc = new frmClassState();
    fc.pack();
    fc.show();
  }
}


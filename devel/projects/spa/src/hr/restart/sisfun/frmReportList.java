/****license*****************************************************************
**   file: frmReportList.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Reportext;
import hr.restart.baza.raDataSet;
import hr.restart.util.Aus;
import hr.restart.util.OKpanel;
import hr.restart.util.raFrame;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;
import hr.restart.util.raNavAction;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class frmReportList extends raFrame {

  raJPTableView jp = new raJPTableView() {
    public void mpTable_doubleClicked() {
      if (!busy)
        OKPress();
    }
  };

//  JList lrep = new JList();
//  ReportListModel model = new ReportListModel();

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      if (!busy)
        OKPress();
    }
    public void jPrekid_actionPerformed() {
      frmReportList.this.hide();
    }
  };

  private QueryDataSet reps = new raDataSet();
  private boolean busy;
  String app;

  public frmReportList() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setApp(String app) {
    this.app = app;
  }

  private void OKPress() {
/*   if (lrep.getSelectedIndex() < 0) return;
    busy = true;
    new Thread() {
      public void run() {
        java.net.URL rep = null;
        String url = (String) model.getUrlAt(lrep.getSelectedIndex());
        try {
          if (url.toLowerCase().startsWith("hr/restart/util/reports"))
            rep = ClassLoader.getSystemResource(url);
          else rep = new java.net.URL(url);
          } catch (Exception e) {}
          if (rep != null)
            raPilot.executeReport(rep);
          busy = false;
      }
    }.start();*/
    busy = true;
    new Thread() {
      public void run() {
        java.net.URL rep = null;
        String url = reps.getString("URL");
        try {
          if (url.toLowerCase().startsWith("hr/restart/util/reports"))
            rep = ClassLoader.getSystemResource(url);
          else rep = new java.net.URL(url);
        } catch (Exception e) {}
        if (rep == null) try {
          rep = Aus.findFileAnywhere(url).toURL();
        } catch (Exception e) {}
        if (rep != null)
          raPilot.executeReport(rep, reps.getString("NASLOV"), frmReportList.this.getWindow());
        else JOptionPane.showMessageDialog(frmReportList.this.getWindow(),
            "Izvještaj ne postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
        busy = false;
      }
    }.start();
  }

  public void show() {
    if (isShowing()) return;
    if (app == null || app.length() == 0)
      Reportext.getDataModule().setFilter(reps, "");
    else
      Reportext.getDataModule().setFilter(reps, Condition.equal("APP", app));
    reps.open();
    System.out.println("count; "+reps.rowCount());
    if (reps.rowCount() == 0) {
      JOptionPane.showMessageDialog(this.getWindow(), "Ne postoji nijedan izvještaj!",
                                    "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }


//    System.out.println(reps);
//    model.populateList(reps);
//    lrep.setSelectedIndex(0);
    this.setTitle("Dodatni izvještaji");
    super.show();
  }

  public void setDataSet(QueryDataSet ds) {
    /** @todo andrej implementirati u jpTableView nekako */
    jp.setKumTak(true);
    jp.setDataSet(null);
    jp.setStoZbrojiti(new String[] {});
    jp.setKumTak(false);
    jp.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    /*lrep.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (!busy && e.getClickCount() == 2) {
          OKPress();
        }
      }
    });
    lrep.setBorder(BorderFactory.createLoweredBevelBorder());
    lrep.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    lrep.setCellRenderer(new DefaultListCellRenderer() {
      private boolean selected;
      public Component getListCellRendererComponent(JList l, Object v, int idx, boolean sel, boolean focus) {
        return super.getListCellRendererComponent(l, v, idx, selected = sel, false);
      }
      public void setEnabled(boolean enabled) {
        super.setEnabled(enabled || selected);
      }
    });
    lrep.setModel(model);
    pan.add(view = new JraScrollPane(lrep));
    pan.setBorder(BorderFactory.createLoweredBevelBorder());
    view.getViewport().setPreferredSize(new Dimension(320, 200));*/
    Reportext.getDataModule().setFilter(reps, "");
    setDataSet(reps);
    jp.getColumnsBean().setSaveSettings(false);
    jp.setVisibleCols(new int[] {1});
    jp.getNavBar().getColBean().initialize();
    jp.getNavBar().addOption(new raNavAction("Izlaz", raImages.IMGX, KeyEvent.VK_ESCAPE) {
      public void actionPerformed(ActionEvent e) {
        frmReportList.this.hide();
      }
    });
    this.getContentPane().add(jp, BorderLayout.CENTER);
    this.getContentPane().add(okp, BorderLayout.SOUTH);
    jp.initKeyListener(this);
    okp.registerOKPanelKeys(this);
  }
}

/*class ReportListModel extends AbstractListModel {
  Vector urls = new Vector();
  Vector descriptions = new Vector();
  public int getSize() {
    return descriptions.size();
  }
  public Object getElementAt(int idx) {
    return descriptions.get(idx);
  }
  public Object getUrlAt(int idx) {
    return urls.get(idx);
  }
  public void populateList(DataSet ds) {
    urls.clear();
    descriptions.clear();
    for (ds.first(); ds.inBounds(); ds.next()) {
      urls.add(ds.getString("URL"));
      descriptions.add(ds.getString("NASLOV"));
    }
    this.fireContentsChanged(this, 0, urls.size() - 1);
  }
}
*/
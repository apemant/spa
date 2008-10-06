/****license*****************************************************************
**   file: dlgErrors.java
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
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraFrame;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raExtendedTable;
import hr.restart.util.Aus;
import hr.restart.util.OKpanel;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;
import hr.restart.util.reports.JTablePrintRun;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.jb.util.TriStateProperty;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class dlgErrors {
  public static final String DEFAULT_COLUMN = "OPIS";

  private JPanel pan = new JPanel();
  private Window win;
  private Container parent;
  private String title;
  private boolean modal;
  
  private int width = 640, height = 400;
  
  JraTable2 mpt = new raExtendedTable() {

    public void killFocus(java.util.EventObject e) {
      okp.jPrekid.requestFocus();
    }

    public void tableDoubleClicked() {
      jp.mpTable_doubleClicked();
    }

    public void fireTableDataChanged() {
      super.fireTableDataChanged();
      setCount();
    }
  };
  
  public raJPTableView jp = new raJPTableView(mpt) {
    public void mpTable_killFocus(java.util.EventObject e) {
      okp.jPrekid.requestFocus();
    }
  };
  public OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };

  private JLabel countLabel = new JLabel();
  JPanel down;
  
  StorageDataSet errors;
  String[] cols;
  Column[] customs;

  JTablePrintRun printer = new JTablePrintRun();

  public dlgErrors(Container parent) {
    this(parent, "Greške", true);
  }

/*  public dlgErrors(Container parent, StorageDataSet ds, String title, boolean modal) {
    this(parent, title, modal);
    setDataSet(ds);
    jp.getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
  }*/
  
  public dlgErrors() {
    this(null, "Greške", false);
  }
  
  private void createDialog() {
    Container realparent = null;

    if (modal)
      if (parent instanceof JComponent)
        realparent = ((JComponent) parent).getTopLevelAncestor();
      else if (parent instanceof Window)
        realparent = parent;

    if (!modal) win = new JraFrame(title);
    else if (realparent instanceof Dialog)
      win = new JraDialog((Dialog) realparent, title, modal);
    else if (realparent instanceof Frame)
      win = new JraDialog((Frame) realparent, title, modal);
    else win = new JraDialog((Frame) null, title, modal);
    
    if (win instanceof JraFrame) 
      ((JraFrame) win).setContentPane(pan);
    else ((JraDialog) win).setContentPane(pan);
    okp.registerOKPanelKeys(win);
    win.setSize(width, height);
    win.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - width / 2, 200);
    win.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        cancelPress();
      }
    });
  }

  public dlgErrors(Container parent, String title, boolean modal) {
    this.parent = parent;
    this.title = title;
    this.modal = modal;
    try {
      jbInit();      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Window getOwner() {
    return win.getOwner();
  }

  private void jbInit() throws Exception {
    pan.setLayout(new BorderLayout());
    pan.add(jp, BorderLayout.CENTER);
    
    down = new JPanel(new BorderLayout());
//  down.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
    down.add(okp, BorderLayout.EAST);
    down.add(countLabel, BorderLayout.CENTER);
    pan.add(down, BorderLayout.SOUTH);
    jp.setCreateCB(false);
    jp.setBorder(BorderFactory.createEtchedBorder());
    okp.jBOK.setText("Ispis");
    okp.jBOK.setIcon(raImages.getImageIcon(raImages.IMGPRINT));
    okp.registerOKPanelKeys((JraTable2) jp.getMpTable());
  }
  
  public void setCount() {
    int r = jp.getStorageDataSet().rowCount();
    countLabel.setText(Aus.getNumDep(r, "  Prikazan ", "  Prikazana ", "  Prikazano ")+r+
         Aus.getNumDep(r, " slog.", " sloga.", " slogova."));
  }

  private void cancelPress() {
    if (win != null) {
      win.dispose();
      win = null;
    }
  }

  public void setSize(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public void check() {
    if (countErrors() == 0) hide();
    else show();
  }

  public boolean isDead() {
    return win == null;
  }

  public void hide() {
    cancelPress();
  }

  private JButton myButt;
  public void setActionButton(JButton butt) {
    down.add(myButt = butt, BorderLayout.WEST);
  }

  public boolean isActionButton(Object butt) {
    return (myButt == butt);
  }
  
  public void initWindow() {
    if (win == null) createDialog();
  }

  public void show() {
    if (win == null) createDialog();
    setCount();
    win.show();
  }
  
  public void show(Container parent) {
    this.parent = parent;
    createDialog();
    setCount();
    win.show();
  }

  public void setColumnWidth(int width) {
    setColumnWidth(DEFAULT_COLUMN, width);
  }

  public void setColumnWidth(String col, int width) {
    errors.getColumn(col).setWidth(width);
  }

  public void setColumnCaption(String caption) {
    setColumnCaption(DEFAULT_COLUMN, caption);
  }

  public void setColumnCaption(String col, String caption) {
    errors.getColumn(col).setCaption(caption);
  }

  public void setData(Column custom) {
    setData(null, (String[]) null, new Column[] {custom});
  }

  public void setData(Column[] customs) {
    setData(null, (String[]) null, customs);
  }

  public void setData(DataSet model) {
    setData(null, (String[]) null, (Column[]) null);
  }

  public void setData(DataSet model, String col) {
    setData(model, new String[] {col}, (Column[]) null);
  }

  public void setData(DataSet model, String col, Column custom) {
    setData(model, new String[] {col}, new Column[] {custom});
  }

  public void setData(DataSet model, String[] cols) {
    setData(model, cols, (Column[]) null);
  }

  public void setData(DataSet model, String[] cols, Column custom) {
    setData(model, cols, new Column[] {custom});
  }

  public void setData(DataSet model, String[] cols, Column[] customs) {
    errors = new StorageDataSet();
    this.cols = cols;
    this.customs = customs;
    int n1 = cols == null ? 0 : cols.length;
    int n2 = customs == null ? 0 : customs.length;
    Column[] cs = new Column[n1 + n2 + 1];
    cs[0] = dM.createStringColumn(DEFAULT_COLUMN, "Opis", 100);
    cs[0].setWidth(40);
    for (int i = 0; i < n1; i++) {
      cs[i + 1] = (Column) model.getColumn(cols[i]).clone();
      cs[i + 1].setVisible(TriStateProperty.TRUE);
      if (cs[i + 1].getPrecision() > 30)
        cs[i + 1].setWidth(20);
    }
    for (int i = 0; i < n2; i++) {
      cs[i + 1 + n1] = customs[i];
      if (cs[i + 1 + n1].getPrecision() > 30)
        cs[i + 1 + n1].setWidth(20);
    }
    errors.setColumns(cs);
    errors.open();
    setDataSet(errors);
  }
  private String trimToColumn(String col, String opis) {
    int w = errors.getColumn(col).getPrecision();
    if (opis.length() <= w) return opis;
    return opis.substring(0,w);
  }
  public void addError(String opis) {
    errors.insertRow(false);
    errors.setString(DEFAULT_COLUMN, trimToColumn(DEFAULT_COLUMN,opis));
  }

  public void addError(String opis, DataSet stavka) {
    errors.insertRow(false);
    errors.setString(DEFAULT_COLUMN, trimToColumn(DEFAULT_COLUMN,opis));
    if (cols != null && cols.length > 0)
      dM.copyColumns(stavka, errors, cols);
  }

  private void setAnything(int n, Object obj) {
    switch (customs[n].getDataType()) {
      case Variant.BIGDECIMAL:
        errors.setBigDecimal(customs[n].getColumnName(), (BigDecimal) obj);
        break;
      case Variant.STRING:
        errors.setString(customs[n].getColumnName(), trimToColumn(customs[n].getColumnName(),(String) obj));
        break;
      case Variant.TIMESTAMP:
        errors.setTimestamp(customs[n].getColumnName(), (Timestamp) obj);
        break;
      case Variant.INT:
        errors.setInt(customs[n].getColumnName(), ((Integer) obj).intValue());
        break;
      case Variant.SHORT:
        errors.setInt(customs[n].getColumnName(), ((Short) obj).shortValue());
        break;
    }
  }

  public void addError(String opis, DataSet stavka, Object obj) {
    addError(opis, stavka);
    setAnything(0, obj);
  }

  public void addError(String opis, DataSet stavka, Object[] objs) {
    addError(opis, stavka);
    for (int i = 0; i < customs.length; i++)
      setAnything(i, objs[i]);
  }

  public void addError(String opis, Object obj) {
    addError(opis);
    setAnything(0, obj);
  }

  public void addError(String opis, Object[] objs) {
    addError(opis);
    for (int i = 0; i < customs.length; i++)
      setAnything(i, objs[i]);
  }

  public void setDataSet(StorageDataSet ds) {
    /** @todo andrej implementirati u jpTableView nekako */
    jp.setKumTak(true);
    jp.setDataSet(null);
    jp.setStoZbrojiti(new String[] {});
    jp.setKumTak(false);
    jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    jp.setDataSet(ds);
  }

  public StorageDataSet getDataSet() {
    return jp.getStorageDataSet();
  }

  public int countErrors() {
    return jp.getStorageDataSet() == null ? 0 : jp.getStorageDataSet().rowCount();
  }

  private void OKPress() {
    jp.getNavBar().getColBean().setRaJdbTable(jp.getMpTable());
    printer.setInterTitle(getClass().getName());
    printer.setColB(jp.getNavBar().getColBean());
    if (win instanceof JraFrame)
      printer.setRTitle(((JraFrame) win).getTitle());
    else printer.setRTitle(((JraDialog) win).getTitle());
    printer.getReportRunner().lockOwner(win, null);
    printer.runIt();
  }
}

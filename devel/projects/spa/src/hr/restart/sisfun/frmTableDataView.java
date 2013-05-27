/****license*****************************************************************
**   file: frmTableDataView.java
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

import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.JraFrame;
import hr.restart.swing.JraKeyListener;
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTableInterface;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raExtendedTable;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.OKpanel;
import hr.restart.util.VarStr;
import hr.restart.util.raFileFilter;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;
import hr.restart.util.raNavAction;
import hr.restart.util.reports.JTablePrintRun;
import hr.restart.util.reports.raReportDescriptor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileFilter;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSetException;
import com.borland.dx.dataset.RowStatus;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.jb.util.ExceptionChain;

public class frmTableDataView extends JraFrame {
  raExtendedTable mpt = new raExtendedTable() {
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
    public void setTableColumnsUI() {
      super.setTableColumnsUI();
      if (jp.getColumnsBean() != null)
      	jp.getColumnsBean().updateColumnWidths();
    }
    public void changeSelection(int row, int col, boolean toggle, boolean extend) {
    	super.changeSelection(row, col, toggle, extend);
    	if (editor.open) editCell();
    }
  };
  public raJPTableView jp = new raJPTableView(mpt) {
    public void mpTable_doubleClicked() {
      if (editable) editCell();
      else doubleClick(jp);
    }
    public void navBar_afterRefresh() {
      setCount();
      changed = false;
    }
    /*public void init_kum() {
      super.init_kum();
      if (isShowing()) getColumnsBean().initialize();
    }*/
//    public void mpTable_killFocus(java.util.EventObject e) {
//      System.out.println("kill focus "+editor.open);
//      if (editor.open) editor.setFokus();
//      else super.mpTable_killFocus(e);
//    }
  };

  JFileChooser jf = new JFileChooser();
  private JLabel countLabel = new JLabel();

  public OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };

  StorageDataSet columns = new StorageDataSet() {
    public boolean saveChangesSupported() {
      return false;
    }
    public boolean refreshSupported() {
      return false;
    }
    public void saveChanges() {
      this.post();
    }
    public void refresh() {
    }
  };

  HashSet keys = new HashSet();
  HashSet invis = new HashSet();

  JPanel down;
  boolean editable = true, coldefs = true, reusable = false, changed = false;

  JTablePrintRun printer = new JTablePrintRun();
  raReportDescriptor custom;
  raNavAction ex;

  FileFilter filterCSV = new raFileFilter("Excel datoteke (*.csv)");
  FileFilter filterDAT = new raFileFilter("Kreator datoteke (*.dat)");

  public frmTableDataView() {
    this(false, false, false);
  }

  public frmTableDataView(boolean edit) {
    this(edit, edit, edit);
  }

  public frmTableDataView(boolean edit, boolean cols) {
    this(edit, cols, edit & cols);
  }
  
  public frmTableDataView(boolean edit, boolean cols, boolean sticky) {
    editable = edit;
    coldefs = cols;
    reusable = sticky;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public void setReusable(boolean sticky) {
    reusable = sticky;
  }

  public boolean isEditable() {
    return editable;
  }
/*  protected JRootPane createRootPane() {
    return new JRootPane() {
      public boolean isShowing() {
        boolean issh = super.isShowing();
        System.err.println("showing "+issh);
        new Throwable().printStackTrace();
        return issh;
      }
      public boolean isDoubleBuffered() {
        System.err.println("rootpane isDoubleBuffered");
        new Throwable().printStackTrace();
        try {
          Thread.sleep(2000);
        } catch (Exception e) {}

        return super.isDoubleBuffered();
      }
      public void paintComponent(Graphics g) {
        Aus.dumpClassName(g);
        System.err.println("rootpane paintcomponent");
        try {
          Thread.sleep(2000);
        } catch (Exception e) {}
        super.paintComponent(g);
        System.err.println("after rootpane paintcomponent");
        try {
          Thread.sleep(2000);
        } catch (Exception e) {}
      }
      public void paint(Graphics g) {
        System.err.println("rootpane paint");
    try {
      Thread.sleep(2000);
    } catch (Exception e) {}
        super.paint(g);
      }
    };
  }

  public void paint(Graphics g) {
    new Throwable().printStackTrace();
    System.err.println("frame paint");
    try {
      Thread.sleep(2000);
    } catch (Exception e) {}


//    g.translate(getInsets().left, getInsets().top);
//    RepaintManager root = RepaintManager.currentManager(getRootPane());
//    System.out.println(root.getDoubleBufferMaximumSize());
//    System.out.println(root.isDoubleBufferingEnabled());
//    System.out.println(getRootPane().isDoubleBuffered());
//    getRootPane().paint(g);
    shown = true;
    super.paint(g);
  }

  private boolean shown; */
  
  protected void doubleClick(raJPTableView jp2) {
    // for override
  }

  private void jbInit() throws Exception {
 /*   try {
      Field fmp = raJPTableView.class.getDeclaredField("mpTable");
      fmp.setAccessible(true);
      fmp.set(jp, mpt);
      Field fj = raJPTableView.class.getDeclaredField("jScrollPaneTable");
      fj.setAccessible(true);
      ((JraScrollPane) fj.get(jp)).getViewport().setView(mpt);
    } catch (Exception e) {
      e.printStackTrace();
    }*/




    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(jp, BorderLayout.CENTER);
    jp.setBorder(BorderFactory.createEtchedBorder());
    down = new JPanel(new BorderLayout());
//    down.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
    down.add(okp, BorderLayout.EAST);
    down.add(countLabel, BorderLayout.CENTER);
    this.getContentPane().add(down, BorderLayout.SOUTH);
    jp.getNavBar().getColBean().setSaveSettings(false);
    okp.jBOK.setText("Pohrani");
    okp.jBOK.setIcon(raImages.getImageIcon(raImages.IMGSAVE));
    okp.registerOKPanelKeys(this);
    okp.jPrekid.setFocusPainted(false);
    jf.addChoosableFileFilter(filterCSV);
    jf.addChoosableFileFilter(filterDAT);
    try {
      jf.setCurrentDirectory(new File("."));
    } catch (Exception e) {}

    this.setSize(640, 400);
    this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 320, 200);

    columns.setColumns(Kolone.getDataModule().getDataSet().cloneColumns());
    columns.open();
    cview.setDataSet(columns);

    if (editable) {
      jp.getNavBar().addOption(new raNavAction("Novi red", raImages.IMGADD, KeyEvent.VK_F2) {
        public void actionPerformed(ActionEvent e) {
          DataRow copy = new DataRow(jp.getStorageDataSet());
          jp.getStorageDataSet().getDataRow(copy);
          long nr = jp.getStorageDataSet().addRowReturnInternalRow(copy);
          jp.fireTableDataChanged();
          jp.getStorageDataSet().goToInternalRow(nr);
          changed = true;
//        jp.fireTableDataChanged();
        }
      });
      jp.getNavBar().addOption(new raNavAction("Izmjena polja", raImages.IMGCHANGE, KeyEvent.VK_F4) {
        public void actionPerformed(ActionEvent e) {
          frmTableDataView.this.editCell();
        }
      });
      jp.getNavBar().addOption(new raNavAction("Brisanje reda", raImages.IMGDELETE, KeyEvent.VK_F3) {
        public void actionPerformed(ActionEvent e) {
        	if (jp.getStorageDataSet().rowCount() > 0) changed = true;
          if (jp.getSelectCount() > 0) multiDelete();
          else if (jp.getStorageDataSet().rowCount() > 0) {
          	jp.getStorageDataSet().deleteRow();
          	jp.fireTableDataChanged();
          }
        }
      });
      jp.getNavBar().addOption(new raNavAction("Snimi promjene", raImages.IMGIMPORT, KeyEvent.VK_S, KeyEvent.CTRL_MASK) {
        public void actionPerformed(ActionEvent e) {
          saveAllChanges();
        }
      });
    }
    jp.getNavBar().addOption(new raNavAction("Ispis", raImages.IMGPRINT, KeyEvent.VK_F5) {
      public void actionPerformed(ActionEvent e) {
        frmTableDataView.this.print();
      }
    });
    if (coldefs)
    jp.getNavBar().addOption(new raNavAction("Kolone", raImages.IMGHISTORY, KeyEvent.VK_F8) {
      public void actionPerformed(ActionEvent e) {
        cview.config();
        cview.show();
      }
    });
    jp.getNavBar().addOption(ex = new raNavAction("Izlaz", raImages.IMGX, KeyEvent.VK_ESCAPE) {
      public void actionPerformed(ActionEvent e) {
//        dumpListeners();
        cancelPress();
      }
    });
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        cancelPress();
      }
    });
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    jp.initKeyListener(this);
  }
  
  public void setCustomReport(raReportDescriptor rd) {
    custom = rd;
  }
  
  void multiDelete() {
  	int row = jp.getDataSet().getRow();
  	jp.enableEvents(false);
    Object[] all = jp.getSelection();
    if (all.length == jp.getDataSet().rowCount())
    	jp.getStorageDataSet().deleteAllRows();
	  else {
	    Arrays.sort(all);
	    for (int i = all.length - 1; i >= 0; i--) {
	    	int dr = ((Integer) all[i]).intValue();
	    	jp.getStorageDataSet().goToRow(dr);
	    	jp.getStorageDataSet().deleteRow();
	    	if (dr < row) --row;
	    }
	  }
    if (row >= jp.getDataSet().rowCount())
    	row = jp.getDataSet().rowCount() - 1;
    if (row < 0) row = 0;
    if (jp.getDataSet().rowCount() > 0)
    	jp.getDataSet().goToRow(row);
    jp.enableEvents(true);
    jp.getSelectionTracker().clearSelection();
    jp.fireTableDataChanged();
  }
  
  public void detach() {
    reusable = false;
    if (!isShowing()) destroy();
  }
  
  private void destroy() {
    cview.jp2.rmKeyListener(cview);
    cview.dispose();
    jp.rmKeyListener(this);
    this.dispose();
  }

  private void saveAllChanges() {
    if (!jp.getStorageDataSet().saveChangesSupported()) {
      JOptionPane.showMessageDialog(this, 
          "Tablica se ne može mijenjati!", "Upozorenje",
          JOptionPane.WARNING_MESSAGE);
      return;
    }
    try {
      jp.getStorageDataSet().saveChanges();
      afterSaveChanges(jp.getStorageDataSet().getTableName());
      changed = false;
      JOptionPane.showMessageDialog(this, "Promjene uspješno snimljene!", "Poruka",
                                    JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
      VarStr err = new VarStr();
      err.append("Snimanje nije uspjelo! Greška:\n\n").append(e.getLocalizedMessage()).append("\n");
      if (e instanceof DataSetException) {
        ExceptionChain ec = ((DataSetException) e).getExceptionChain();
        while (ec != null && ec.hasExceptions()) {
          err.append("\nChained exception:\n").append(ec.getException().getLocalizedMessage());
          ec = ec.getNext();
        }
      }
      JOptionPane.showMessageDialog(this, new raMultiLineMessage(err.toString(), SwingConstants.LEADING),
                                    "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }

  public void afterSaveChanges(String tname) {
  }

  public void clearColumns() {
    columns.empty();
    keys.clear();
    invis.clear();
  }

  public boolean checkColumn(String colname) {
    for (columns.first(); columns.inBounds(); columns.next())
      if (columns.getString("IMEKOL").equalsIgnoreCase(colname) &&
          (columns.getShort("SQLTIP") == 2 ||
          columns.getShort("SQLTIP") == 4 ||
          columns.getShort("SQLTIP") == 5)) return true;

    return false;
  }

//  private void dumpListeners() {
//    System.out.println("direct:");
//    System.out.println(VarStr.join(this.getListeners(ComponentListener.class), '\n'));
//    System.out.println("content pane:");
//    System.out.println(VarStr.join(getContentPane().getListeners(ComponentListener.class), '\n'));
//    System.out.println("jp table view:");
//    System.out.println(VarStr.join(jp.getListeners(ComponentListener.class), '\n'));
//    System.out.println("jratable2:");
//    System.out.println(VarStr.join(((JraTable2) jp.getMpTable()).getListeners(ComponentListener.class), '\n'));
//  }

  public void addColumn(Column c) {
    columns.insertRow(false);
    columns.setString("IMEKOL", c.getColumnName());
    columns.setString("OPIS", c.getCaption().substring(0, Math.min(50, c.getCaption().length())));
//bug fix select * from odbiciarh (F10) -> com.borland.dx.dataset.ValidationException: Values for the OPIS column cannot be longer than 50 characters.
/*    try {
      int subst = c.getCaption().length()-1;
      subst = (subst>49)?49:subst;
      columns.setString("OPIS", c.getCaption().substring(0,subst));
    }
    catch (Exception ex) {
      try {
        columns.setString("OPIS", c.getColumnName().trim());
      }
      catch (Exception ex2) {
        columns.setString("OPIS", "UNKNOWN");
      }
    }*/
/* BTW see (U SLOBODNO VRIJEME):
    hr.restart.db.raConnectionFactory.getColumns(hr.restart.baza.dM.getDataModule().getDatabase1().getJdbcConnection(),"kolone");
    hr.restart.db.raConnectionFactory.getKeyColumns(hr.restart.baza.dM.getDataModule().getDatabase1().getJdbcConnection(),"kolone");
    hr.restart.db.raSQLset
    hr.restart.db.raFastSQLSet
    java.sql.Connection.getMetaData();
    java.sql.ResultSet.getMetaData();
    java.sql.Connection.createStatement();
    java.sql.Statement.executeQuery("query");
    com.borland.dx.sql.dataset.Database.resultSetToDataSet(resultSet);
*/
//    hr.restart.baza.dM.getDataModule().getDatabase1().getJdbcConnection().getMetaData();
    String tip = Variant.typeName(c.getDataType());
    if (c.getDataType() == Variant.STRING)
      columns.setString("TIP", tip + " " + c.getPrecision());
    else if (c.getDataType() == Variant.BIGDECIMAL)
      columns.setString("TIP", tip + " " + c.getScale());
    else
      columns.setString("TIP", tip);
//    columns.setShort("DULJINA", (short) c.getPrecision());
    columns.setShort("SIRINA", (short) c.getWidth());
    columns.setShort("SQLTIP", (short) c.getSqlType());
//bug fix SELECT godobr||'-'||mjobr||'-'||rbrobr FROM Odbiciarh -> NullPointerException
    columns.setString("TABLICA", c.getTableName() == null ? "" : c.getTableName().toLowerCase());
    if (c.isRowId()) keys.add(c.getColumnName());
    if (c.getVisible() == com.borland.jb.util.TriStateProperty.FALSE) invis.add(c.getColumnName());
    columns.post();
//    columns.setString("KLJUC", c.isRowId() ? "D" : "N");
//    columns.setString("VID", c.getVisible() == com.borland.jb.util.TriStateProperty.FALSE ? "N" : "D");
//    columns.setString("TABLICA", Valid.getTableName(((QueryDataSet)c.getDataSet()).getQuery().getQueryString()).toLowerCase());
  }

/*  private void replaceString(StringBuffer s, String orig, String chg) {
    int offset;
    while ((offset = s.toString().indexOf(orig)) != -1)
      s.replace(offset, offset + orig.length(), chg);
  } */

  private String getFieldNames() {
    VarStr line = new VarStr("(");
    JraTableInterface t = jp.getMpTable();

    for (int j = 0; j < t.getColumnCount(); j++) {
      line.append(t.getDataSet().getColumn(t.convertColumnIndexToModel(j)).getColumnName().toLowerCase());
      line.append(",");
    }
    line.chop().append(")");
    return line.toString();
  }

  private boolean dumpTable(TextFile f, boolean dump) {
    VarStr line = new VarStr(128);
    JraTableInterface t = jp.getMpTable();
    String sep = ";";
    Object elem;
    if (dump) sep = Aus.getDumpSeparator();

    try {
      for (int i = 0; i < t.getRowCount(); i++) {
        line.clear();
        for (int j = 0; j < t.getColumnCount(); j++) {
          elem = t.getValueAt(i, j);
          line.append(elem == null ? "" : elem).append(sep);
          if (dump) line.replaceAll("\n", "\\n");
        }
        f.out(line.toString());
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
  
  private boolean dumpCSV(TextFile f) {
    VarStr line = new VarStr(128);
    JraTable2 t = (JraTable2) jp.getMpTable();
    String sep = ",";
    String qq = "\"";
    Object elem;

    try {
      for (int i = 0; i < t.getRowCount(); i++) {
        line.clear();
        for (int j = 0; j < t.getColumnCount(); j++) {
          elem = t.getValueAt(i, j);
          String q = jp.getStorageDataSet().getColumn(t.getColumnName(j)).getDataType() == Variant.STRING ? qq : "";
          line.append(q).append(elem == null ? "" : elem).append(q).append(sep);
        }
        f.out(line.chop().toString());
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

//  private BufferedWriter openWriter(String fname, boolean displayError) {
//    String enc = hr.restart.baza.dM.getDataModule().getDatabase1().getConnection().getProperties().getProperty("charSet");
//    if (enc == null) enc = System.getProperty("file.encoding");
//    try {
//      BufferedWriter f = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname), enc));
//      return f;
//    } catch (Exception e) {
//      if (displayError)
//        JOptionPane.showMessageDialog(this, "Greška prilikom otvaranja datoteke!", "Greška", JOptionPane.ERROR_MESSAGE);
//      return null;
//    }
//  }

//  private boolean closeWriter(BufferedWriter f) {
//    try {
//      f.close();
//      return true;
//    } catch (Exception e) {
//      return false;
//    }
//  }

  protected void OKPress() {
    if (jp.getMpTable().getRowCount() == 0) {
      JOptionPane.showMessageDialog(this, "Tablica je prazna!", "Greška", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    if (jf.showSaveDialog(this) == jf.APPROVE_OPTION) {
      String fpath;
      String fname = jf.getSelectedFile().getName().toLowerCase();
      if (jf.getFileFilter() == filterCSV && !fname.endsWith(".csv") && fname.indexOf(".") == -1)
        fpath = jf.getSelectedFile().getAbsolutePath() + ".csv";
      else if (jf.getFileFilter() == filterDAT && !fname.endsWith(".dat") && fname.indexOf(".") == -1)
        fpath = jf.getSelectedFile().getAbsolutePath() + ".dat";
      else fpath = jf.getSelectedFile().getAbsolutePath();
      boolean dump = fpath.endsWith(".dat") && jf.getFileFilter() == filterDAT;
      TextFile f = TextFile.write(fpath);
      if (f == null) {
        JOptionPane.showMessageDialog(this, "Greška prilikom otvaranja datoteke!", "Greška", JOptionPane.ERROR_MESSAGE);
        return;
      }

      boolean ok = fpath.endsWith(".csv") ? dumpCSV(f) : dumpTable(f, dump);

      f.close();
      if (ok && dump) {
        f = TextFile.write(fpath.substring(0, fpath.lastIndexOf(".")) + ".def");
        if (f != null) {
          f.out(getFieldNames());
          f.out(TextFile.getEncoding());
          f.close();
        } else ok = false;
      }
      if (ok)
        JOptionPane.showMessageDialog(this, new raMultiLineMessage("Datoteka pohranjena:\n" + fpath,
            SwingConstants.LEADING), "Poruka", JOptionPane.INFORMATION_MESSAGE);
      else
        JOptionPane.showMessageDialog(this, "Greška prilikom snimanja!", "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void cancelPress() {
  	if (editable && changed && jp.getStorageDataSet().saveChangesSupported() 
  	    && JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(this,
  			"Promjene æe biti izgubljene. Zatvoriti ipak?", "Tablica promijenjena",
  			JOptionPane.OK_CANCEL_OPTION)) return;
    hide();
    if (!reusable) destroy();
  }
  
  public void hide() {
    jp.hidePopups();
    cview.hide();
    editor.close();
    super.hide();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        setDataSet(null);
      }
    });
  }

  public void setDataSet(StorageDataSet ds) {
    /** @todo andrej implementirati u jpTableView nekako */
    jp.setKumTak(true);
    jp.setDataSet(null);
    jp.setStoZbrojiti(new String[] {});
    jp.setKumTak(false);
    jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    jp.setDataSet(ds);
  }

  public void setSums(String[] cols) {
    jp.setKumTak(true);
    jp.setStoZbrojiti(cols);
//    jp.init_kum();
  }

  public void setVisibleCols(int[] vcols) {
    jp.setVisibleCols(vcols);
  }

  public void resizeLater() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (jp.getMpTable().getSize().width < jp.getSize().width)
          jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
      }
    });
  }

  public void print() {
    jp.getNavBar().getColBean().setRaJdbTable(jp.getMpTable());
    printer.setInterTitle(getClass().getName());
    printer.setColB(jp.getNavBar().getColBean());
    printer.setRTitle(this.getTitle());
    if (custom != null) {
      printer.getReportRunner().clearAllCustomReports();
      printer.getReportRunner().addReport(custom);
    }
    jp.enableEvents(false);
    try {
      printer.runIt();
    } finally {
      jp.enableEvents(true);
    }
  }

  public void setCount() {
    int r = jp.getStorageDataSet().rowCount();
    countLabel.setText(Aus.getNumDep(r, "  Prikazan ", "  Prikazana ", "  Prikazano ")+r+
         Aus.getNumDep(r, " slog.", " sloga.", " slogova."));
  }
  
  public void setSaveName(String name) {
    jp.getColumnsBean().setSaveSettings(true);
    jp.getColumnsBean().setSaveName(name);
  }

  public void show() {
//    if (shown) System.out.println("SHOWN????");
    if (!isShowing()) jp.getNavBar().getColBean().initialize();
    Point cbpl = jp.getNavBar().getColBean().getPreferredLocationOnScreen();
    if (cbpl != null) {
      if (jp.getColumnsBean().getSaveName() != null) pack();
      setLocation(cbpl);
    }
    
    if (editor.open) editor.close();
    setCount();
    changed = false;
//    SwingUtilities.invokeLater(new Runnable() {
//      public void run() {
//        checkDirty();
//      }
//    });
//    System.err.println("show");
//    try {
//      Thread.sleep(2000);
//    } catch (Exception e) {}
//
//    shown = false;
    super.show();
//    shown = true;
//    Aus.dumpClassName(getPeer());
//    System.out.println( getPeer());
    ex.setNavBorder(null);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jp.getColumnsBean().focusCombo();
      }
    });    
//    jp.fireTableDataChanged();
  }

//  public boolean isShowing() {
//    return shown && super.isShowing();
//  }

  void editCell() {
//    editor.initDataSet(jp.getDataSet());
    editor.open(true);
//    editor.config();
//    editor.show();
  }

  EditPanel editor = new EditPanel();

  class EditPanel extends JPanel {
    JPanel main = new JPanel();
    JLabel caption = new JLabel();
    int selc;

    boolean open;
    JraTextField text;
    Variant v = new Variant();
    Column tc;

    public EditPanel() {
      init();
    }

    private void initText() {
      text = new JraTextField() {
        public boolean maskCheck() {
        	if (!text.getDataBinder().isTextModified()) return true;
        	changed = true;
          return super.maskCheck();
        }
        public void addNotify() {
          super.addNotify();
          AWTKeyboard.unregisterComponent(this);
          AWTKeyboard.bindKeyStroke(this, AWTKeyboard.SPACE);
          AWTKeyboard.registerKeyListener(this, new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
              if (e.getKeyCode() == e.VK_ENTER) {
                e.consume();                
                try {
                  if (moveDown(true)) setFokus(); 
                  else editor.close();
                } catch (Exception ex){
                  e.consume();
                }
              }
            }
            public void keyPressed(KeyEvent e) {
              if  (e.getKeyCode()==e.VK_F10) {
                e.consume();
                try {
                  text.maskCheck();
                  editor.close();
                  jp.fireTableDataChanged();
                } catch (Exception ex){
                  e.consume();
                }
              } else if (e.getKeyCode() == e.VK_ESCAPE) {
                e.consume();
                jp.getStorageDataSet().cancel();
                editor.close();
              } else if (e.getKeyCode() == e.VK_ENTER) {
                e.consume();                
              } else if (e.getKeyCode() == e.VK_UP) {
                if (jp.getStorageDataSet().getRow() > 0) {
                  moveUp();
                  setFokus();
                }
              	e.consume();
              } else if (e.getKeyCode() == e.VK_DOWN) {
              	e.consume();
                if (jp.getStorageDataSet().getRow() < jp.getStorageDataSet().getRowCount() - 1) {
                  moveDown(false);
                  setFokus();
                }
              	e.consume();
              }
            }
          });
        }
        public void removeNotify() {
          super.removeNotify();
          AWTKeyboard.unregisterComponent(this);
        }
      };
      text.setPostOnRowPosted(false);
    }

    private void init() {
      FlowLayout f = (FlowLayout) main.getLayout();
      f.setVgap(0);
      f.setHgap(0);
      setLayout(new BorderLayout());
      setBorder(BorderFactory.createEmptyBorder(3, 15, 0, 5));
      add(Box.createHorizontalGlue(), BorderLayout.CENTER);
      initText();
      main.add(caption);
      main.add(text);
      add(main, BorderLayout.EAST);
    }

    boolean moveDown(boolean enter) {
      jp.requestFocus();
      if (enter && jp.getStorageDataSet().getStatus() == RowStatus.INSERTED) {
        int col = selc;
        while (++col < jp.getMpTable().getColumnCount()) {
          tc = ((JraTable2) jp.getMpTable()).getDataSetColumn(col);
          if (tc.getDataSet() != null && !tc.getDataSet().equals("")) {
            selc = col;
            prepareEditorColumn();
            jp.fireTableDataChanged();
            jp.getMpTable().scrollRectToVisible(jp.getMpTable().
                getCellRect(jp.getMpTable().getSelectedRow(), col, true));
            return true;
          }
        }
        tc = ((JraTable2) jp.getMpTable()).getDataSetColumn(selc);
        jp.fireTableDataChanged();
        return false;
      }
      int curr = jp.getStorageDataSet().getRow();
      if (jp.getStorageDataSet().next()) return true;
      jp.getStorageDataSet().goToClosestRow(curr);
      jp.fireTableDataChanged();
      return false;
/*      int curr = jp.getDataSet().getRow();
      while (jp.getMpTable().getSelectedRow() < jp.getMpTable().getRowCount() - 1) {
        jp.getDataSet().next();
        if (!tc.isRowId() || jp.getDataSet().getStatus() == RowStatus.INSERTED)
          return true;
      }
      jp.getDataSet().goToClosestRow(curr);
      return false; */
    }

    boolean moveUp() {
      jp.requestFocus();
      int curr = jp.getStorageDataSet().getRow();
      if (jp.getStorageDataSet().prior()) return true;
      jp.getStorageDataSet().goToClosestRow(curr);
      return false;
    }

    public void close() {
      if (open) {
        okp.registerOKPanelKeys(frmTableDataView.this);
        down.remove(editor);
        down.add(countLabel, BorderLayout.CENTER);
        open = false;
      }
      down.revalidate();
      down.repaint();
      
      jp.requestFocus();
    }

    public boolean open(boolean showErr) {
      JraTable2 t = (JraTable2) jp.getMpTable();
      selc = t.getSelectedColumn();
      tc = t.getDataSetColumn(selc);
//      System.out.println(caption.getMinimumSize());
//      System.out.println(caption.getPreferredSize());
      if (selc == -1) {
        if (showErr)
          JOptionPane.showMessageDialog(frmTableDataView.this, "Nije odabrana kolona!",
                                        "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (tc == null) {
        if (showErr)
          JOptionPane.showMessageDialog(frmTableDataView.this, "Odabrana je pogrešna kolona!",
                                        "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (jp.getStorageDataSet().saveChangesSupported() &&
          (tc.getTableName() == null || tc.getTableName().equals(""))) {
        if (showErr)
          JOptionPane.showMessageDialog(frmTableDataView.this, "Kalkulacije se ne mogu mijenjati!",
                                        "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (jp.getStorageDataSet().getStatus() == RowStatus.INSERTED) {
        jp.getStorageDataSet().setSort(null);
        mpt.resetSortColumns();
      }

      prepareEditorColumn();
      if (!open) {
        okp.unregisterOKPanelKeys(frmTableDataView.this);
        down.remove(countLabel);
        down.add(editor, BorderLayout.CENTER);
        open = true;
      }
      down.revalidate();
      down.repaint();
      setFokus();
      return true;
    }

    private void prepareEditorColumn() {
      caption.setText(tc.getCaption());
//      System.out.println(caption.getMinimumSize());
//      System.out.println(caption.getPreferredSize());
//
      caption.setPreferredSize(new Dimension(caption.getMinimumSize().width + 20, caption.getPreferredSize().height));
      initText();
      if (tc.getDataType() == Variant.STRING) {
        if (tc.getPrecision() <= 5)
          text.setPreferredSize(new Dimension(75, 21));
        else if (tc.getPrecision() <= 15)
          text.setPreferredSize(new Dimension(100, 21));
        else if (tc.getPrecision() <= 30)
          text.setPreferredSize(new Dimension(200, 21));
        else text.setPreferredSize(new Dimension(300, 21));
        text.setHorizontalAlignment(SwingConstants.LEADING);
      } else {
        text.setPreferredSize(new Dimension(100, 21));
        if (tc.getDataType() == Variant.TIMESTAMP)
          text.setHorizontalAlignment(SwingConstants.CENTER);
        else text.setHorizontalAlignment(SwingConstants.TRAILING);
      }

//      text.setmas
      text.setColumnName(tc.getColumnName());
      text.setDataSet(jp.getStorageDataSet());
      main.remove(1);
      main.add(text);

//      jp.getDataSet().getVariant(c.getColumnName(), v);
//      jp.getDataSet().setVariant(c.getColumnName(), v);
    }

/*    private void setFokusDelayed() {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          setFokus();
        }
      });
    } */

/*    void storeValue() {
      text.getDataSet().getVariant(text.getColumnName(), v);
    }

    void restoreValue() {
      text.getDataSet().setVariant(text.getColumnName(), v);
    }
*/
    public void setFokus() {
      JraKeyListener.enterNow = false;
      //storeValue();
      text.requestFocus();
    }

//    public void initDataSet(StorageDataSet original) {
//      ds.empty();
//      ds.close();
//      ds.setColumns(original.cloneColumns());
//    }
  }

  ColumnsFrame cview = new ColumnsFrame();

  class ColumnsFrame extends JraFrame {
    public raJPTableView jp2 = new raJPTableView();
    JTablePrintRun printer = new JTablePrintRun();
    public ColumnsFrame() {
//      super(frmTableDataView.this, false);
      init();
    }
    private void print() {
      jp2.getNavBar().getColBean().setRaJdbTable(jp2.getMpTable());
      printer.setInterTitle(getClass().getName());
      printer.setColB(jp2.getNavBar().getColBean());
      printer.setRTitle(this.getTitle());
      printer.runIt();
    }
    private void init() {
      this.getContentPane().add(jp2, BorderLayout.CENTER);
      jp2.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
      jp2.getNavBar().getColBean().setSaveSettings(false);
      jp2.addTableModifier(new raTableModifier() {
        Column ime;
        JLabel l;
        String s;
        Font bold, ital;
        public boolean doModify() {
          if (getTable() instanceof JraTable2) {
            if (null == (ime = ((JraTable2) getTable()).getDataSetColumn(getColumn()))) return false;
            if (!ime.getColumnName().equals("IMEKOL")) return false;
            else return true;
          } else return false;
        }
        public void modify() {
          if (renderComponent instanceof JLabel) {
            l = (JLabel) renderComponent;
            s = l.getText();
            if (bold == null) {
              bold = l.getFont().deriveFont(Font.BOLD);
              ital = l.getFont().deriveFont(Font.ITALIC);
            }
            if (keys.contains(s))
              l.setFont(bold);
            else if (invis.contains(s))
              l.setFont(ital);
          }
        }
      });
      this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
      this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 320, 80);
      this.setSize(640, 400);
      this.setTitle("Kolone");
      jp2.getNavBar().addOption(new raNavAction("Ispis", raImages.IMGPRINT, KeyEvent.VK_F5) {
        public void actionPerformed(ActionEvent e) {
          ColumnsFrame.this.print();
        }
      });
      jp2.getNavBar().addOption(new raNavAction("Izlaz", raImages.IMGX, KeyEvent.VK_ESCAPE) {
        public void actionPerformed(ActionEvent e) {
          ColumnsFrame.this.hide();
        }
      });
      jp2.initKeyListener(this);
    }

    private void setDataSet(StorageDataSet ds) {
      jp2.setKumTak(true);
      jp2.setDataSet(null);
      jp2.setStoZbrojiti(new String[] {});
      jp2.setKumTak(false);
      jp2.setDataSet(ds);
      jp2.setVisibleCols(new int[] {0,1,2,3,4,5});
      jp2.getNavBar().getColBean().initialize();
    }

//    public void toggleColumn() {
//      CBPierce.getComboBox(frmTableDataView.this.jp.getColumnsBean())
//              .setSelectedIndex(jp.getMpTable().getSelectedRow());
//      CBPierce.itemStateChanged(frmTableDataView.this.jp.getColumnsBean());
//
//      frmTableDataView.this.jp.getColumnsBean().rnvVisible.actionPerformed(null);
//    }

    public void config() {
      String orig = frmTableDataView.this.getTitle();
      if (orig.startsWith("Tablica"))
        setTitle("Kolone tablice " + orig.substring(9));
      else if (orig.startsWith("Tablice"))
        setTitle("Kolone tablica " + orig.substring(9));
      else setTitle(orig + " - kolone");
      int numrows = Math.max(Math.min(jp2.getStorageDataSet().rowCount(), 15), 2);
      this.setSize(this.getWidth(), numrows * 21 + 75);
      jp2.getStorageDataSet().setSort(null);
    }
//    public void refresh() {
//      jp.fireTableDataChanged();
//    }
  }
}



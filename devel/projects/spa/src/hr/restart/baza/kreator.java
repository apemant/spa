/****license*****************************************************************
**   file: kreator.java
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
package hr.restart.baza;

import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JDirectoryChooser;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraFrame;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCustomAttribDoh;
import hr.restart.util.Aus;
import hr.restart.util.Int2;
import hr.restart.util.OKpanel;
import hr.restart.util.ProcessInterruptException;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raGlob;
import hr.restart.util.raImages;
import hr.restart.util.raProcess;
import hr.restart.util.raStatusBar;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Locate;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:        Robno knjigovodstvo
 * Description:  Projekt je zamišljen kao multiuser-ska verzija robnog knjigovodstva
 * Copyright:    Copyright (c) 2001
 * Company:      Rest Art d.o.o.
 * @author Tomislav Vidakovi\u0107
 * @version 1.0
 */


public class kreator extends JraFrame {
  public static int INDEX_ANY = -1;
  public static int INDEX_NOP = 0;
  public static int INDEX_CREATE = 1;
  public static int INDEX_RECREATE = 2;
  public static int INDEX_DROP = 3;

  public static String[] indexComm = {"Ne diraj", "Kreiraj", "Rekreiraj", "Dropaj"};

  public static int TABLE_ANY = -1;
  public static int TABLE_NOP = 0;
  public static int TABLE_SAVE = 1;
  public static int TABLE_LOAD = 2;
  public static int TABLE_DELETE = 3;
  public static int TABLE_UNLOCK = 4;
  public static int TABLE_CHECK = 5;
  public static int TABLE_UPDATE = 6;
  public static int TABLE_CREATE = 7;
  public static int TABLE_RECREATE = 8;
  public static int TABLE_DROP = 9;

  public static String[] tableComm = {"Ne diraj", "Spremi", "Napuni", "Obriši",
    "Otklju\u010Daj", "Provjeri", "Ažuriraj", "Kreiraj", "Rekreiraj", "Dropaj"};

  static kreator kr;
  String subdir = "";
  File loadsave = null;
  VarStr command = null;
  boolean standalone = false, busy = false;
  JPanel jPanel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JraScrollPane jsp = new JraScrollPane();
  BorderLayout bljsp = new BorderLayout();
  JButton jBCopyRow = new JButton("Kopiraj red");
  JButton jBProperties = new JButton("Konekcija");
  JButton jbPath = new JButton("Putanja");
  Box jPDownPanel = Box.createHorizontalBox();
  Box jPControls = Box.createHorizontalBox();
  CopyDialog cdsel = new CopyDialog(this);
  hr.restart.util.OKpanel oKpanel1 = new hr.restart.util.OKpanel(){
      public void jBOK_actionPerformed() {
        pressOK();
      }

      public void jPrekid_actionPerformed() {
        pressCancel();
      }
  };

  Boolean istina = new Boolean(true);
  Boolean laz = new Boolean(false);
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  JTable jTable1;
  TableModelZaKreiranje tm;
//  hr.restart.baza.BazaOper baza = new hr.restart.baza.BazaOper();
  frmTableDataView view = new frmTableDataView(true);
  QueryDataSet tab = Tablice.getDataModule().getFilteredDataSet("");


  String [] kaption = {"Ime tabele","Klasa","Tabela","Indexe"};
  Boolean bl;
//  boolean trig;
  boolean shiftHeld;

  String noteStatus = null;

  String [] klase = {
  };

  public kreator() {
    try {
      findaj_klase();
      jbInit();
//      test_me();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public kreator(String comm) {
    this();
  }

  public static kreator getKreator() {
    if (kr == null) kr = new kreator();
    kr.subdir = "";
    return kr;
  }
  /**
   * Trebalo bi na\u0107i klase sve koje su instanca od KreirDrop
   */
  public void findaj_klase(){

//  for int i =0  to

//   Class[] klasse =  this.getClass().getDeclaredClasses();
//   for (int i=0;i< klasse.length;i++){
//      System.out.println(klasse[i].getName());
//   }
  }

  public void pressOK() {
    disablek();
    raProcess.runChild(this, "", " Provjeranje tablice tablica ... ", new Runnable() {
      public void run() {
        if (command != null)
          raProcess.disableInterrupt();
        runProcess();
      }
    });
    KreirDrop.removeNotifier();
    enablek();
  }

  private void test_me() {
/*    new Thread() {
      public void run() {
        raLocalTransaction trans = new raLocalTransaction() {
          public boolean transaction() throws Exception {
            System.err.println("1 getting row");
            Connection con = dM.getDataModule().getDatabase1().getJdbcConnection();
            Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = st.executeQuery("SELECT * FROM SEQ WHERE OPIS='100OTP2003'");
            System.err.println("1 going to sleep");
            Thread.sleep(4000);
            Random r = new Random();
            double br = r.nextInt(100);
            rs.updateDouble("BROJ", br);
            System.err.println("1 saving "+br);
            rs.updateRow();
            System.err.println("1 done");
            return true;
          }
        };
        trans.execTransaction();
      }
    }.start();
  }*/
/*    try {


    dM.getDataModule().database1.executeStatement(
"        alter procedure getPartner("+
"        partner int) returns (naziv char(50))"+
"        as"+
"        begin"+
"          select nazpar from partneri where cpar = :partner into :naziv;"+
"          suspend;"+
"        end"
    );
    } catch (Exception e) {
      e.printStackTrace();
    } */

  }

  private void disablek() {
    busy = true;
    jBCopyRow.setEnabled(false);
    jBProperties.setEnabled(false);
    jbPath.setEnabled(false);
    oKpanel1.jBOK.setEnabled(false);
    oKpanel1.jPrekid.setEnabled(false);
  }
  private void enablek() {
    busy = false;
    jBCopyRow.setEnabled(true);
    jBProperties.setEnabled(true);
    jbPath.setEnabled(true);
    oKpanel1.jBOK.setEnabled(true);
    oKpanel1.jPrekid.setEnabled(true);
  }
  private void jbInit() throws Exception {

    tm= new TableModelZaKreiranje();
    try {
      setTitle("Kreator - " + dM.getDataModule().getDatabase1().getConnection().getConnectionURL());
    } catch (Exception e) {
      setTitle("Kreator");
    }


    this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    jTable1= new JTable(tm);
    //jTable1.setAutoCreateColumnsFromModel(false);
    //jTable1.setModel(tm);
    jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    for (int k =0;k <tm.m_columns.length; k++) {

   /*   TableCellRenderer renderer ;
      if (k==4)
        renderer = new CheckCellRenderer();
      else {
        DefaultTableCellRenderer renderer1 = new DefaultTableCellRenderer();
        renderer=renderer1 ;

      renderer.setHorizontalAlignment(tm.m_columns[k].m_alignment); */

      //// editor
      TableColumn col = jTable1.getColumnModel().getColumn(k);
      col.setPreferredWidth(tm.m_columns[k].m_width);
      if (k==2)
        col.setCellEditor(new DefaultCellEditor(new JraComboBox(indexComm)));
      else if (k==3)
        col.setCellEditor(new DefaultCellEditor(new JraComboBox(tableComm)));
      else if (k!=4)
        col.setCellEditor(null);

      /*TableColumn column = new TableColumn(k,tm.m_columns[k].m_width, renderer, edit);
      jTable1.addColumn(column);*/

    }

    jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
      public Component getTableCellRendererComponent(JTable t, Object v,
          boolean sel, boolean foc, int row, int col) {
        return super.getTableCellRendererComponent(t, v, sel, false, row, col);
      }
    });


    JTableHeader header = jTable1.getTableHeader();
    header.setUpdateTableInRealTime(false);
    DefaultTableCellRenderer rend = new DefaultTableCellRenderer() {
      int sortFlag;
      public Component getTableCellRendererComponent(JTable t, Object v,
          boolean sel, boolean foc, int row, int col) {
        if (t != null) {
          JTableHeader header = t.getTableHeader();
          if (header != null) {
            setForeground(header.getForeground());
            setBackground(header.getBackground());
            setFont(header.getFont());
          }
          if (tm.getSortedColumn() == jTable1.convertColumnIndexToModel(col))
            sortFlag = tm.isAscending() ? 1 : -1;
          else sortFlag = 0;
        }
        setText((v == null) ? "" : v.toString());
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        return this;
      }
      public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (sortFlag != 0) {
          Image im = raImages.getImageIcon(sortFlag == 1 ? raImages.IMGDOWN : raImages.IMGUP).getImage();
          int spc = (this.getHeight() - im.getHeight(this)) / 2;
          g.drawImage(im, getWidth() - im.getWidth(this) - spc - 1, spc, this);
        }
      }
    };
    rend.setHorizontalAlignment(JLabel.CENTER);
    header.setDefaultRenderer(rend);

    this.getContentPane().setLayout(borderLayout1);
    jPanel1.setLayout(borderLayout3);
    this.getContentPane().add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(jsp,BorderLayout.CENTER);
//    jsp.getViewport().setLayout(bljsp);
    jsp.getViewport().add(jTable1, BorderLayout.CENTER);
    jTable1.setPreferredScrollableViewportSize(
        new Dimension(560,jTable1.getPreferredScrollableViewportSize().height));
//    oKpanel1.add(jBCopyRow, BorderLayout.WEST);
    jPControls.add(jBCopyRow);
    jPControls.add(jBProperties);
    jPControls.add(jbPath);
    //jPControls.setMaximumSize(jPControls.getPreferredSize());
    //oKpanel1.setMaximumSize(oKpanel1.getPreferredSize());
    jPDownPanel.add(jPControls);
    jPDownPanel.add(Box.createHorizontalGlue());
    jPDownPanel.add(oKpanel1);
    this.getContentPane().add(jPDownPanel, BorderLayout.SOUTH);

    oKpanel1.registerOKPanelKeys(this);

    jBCopyRow.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (shiftHeld)
          extendedCopyRow();
        else
          kopirajrow();
      }
    });
    jBCopyRow.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        shiftHeld = e.isShiftDown();
      }
      public void mouseReleased(MouseEvent e) {
        shiftHeld = e.isShiftDown();
      }
    });
    jBProperties.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dM.getDataModule().showParams();
//        JOptionPane.showMessageDialog(null, "Potrebito je iza\u0107i iz kretora!", "Upozorenje",JOptionPane.WARNING_MESSAGE);
        if (dM.getDataModule().reconnectIfNeeded()) {
//          baza= new hr.restart.baza.BazaOper();
//          System.out.println("New BazaOper().");
          tab = Tablice.getDataModule().getFilteredDataSet("");
        }
        setTitle("Kreator - " + dM.getDataModule().getDatabase1().getConnection().getConnectionURL());
      }
    });
    jbPath.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
//        testme();
        selectPath();
      }
    });
    tm.addTableModelListener(new TableModelListener() {
      public void tableChanged(TableModelEvent e) {
        if (e.getType() == e.UPDATE && e.getColumn() == 2 || e.getColumn() == 3)
          checkValidity(e.getFirstRow(), e.getColumn());
      }
    });
    jTable1.getTableHeader().addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (!busy) headerClicked(jTable1.getTableHeader().columnAtPoint(e.getPoint()));
      }
    });

    jTable1.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && jTable1.getSelectedColumn() == 0) {
          if (!busy) showTableView();
        }
      }
    });
    view.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

//    testme();
  }

  private void testme() {
    PlZnacRad.getDataModule();
    PlZnacRadData.getDataModule();
    ZpZemlje.getDataModule();
    Partneri.getDataModule();
    PlZnacRad.getDataModule().getQueryDataSet().refresh();
    jpCustomAttribDoh jp = new jpCustomAttribDoh();
    jp.setTables("PlZnacRad", "PlZnacRadData");
    jp.setAttrCols("CZNAC", "ZNACOPIS", "ZNACTIP", "ZNACREQ", "ZNACDOH");
    jp.setValueCols("CRADNIK", "VRI");
    jp.setDohvatCols("DOHATTR", "DOHCOLS");
    jp.setFields();
    jp.insert();
    JOptionPane.showMessageDialog(this, jp, "Provjera", JOptionPane.PLAIN_MESSAGE);
//    TextFile.setEncoding("UTF-8");
//    Orgstruktura.getDataModule().dumpTable(new File(""), "orgs");
//    TextFile.setSystemEncoding();
//    DataSet ds = Orgstruktura.getDataModule().loadData(new File(""), "orgs");
//    new sysoutTEST(false).prn(ds);
  }

  public void show() {
    setTitle("Kreator - " + dM.getDataModule().getDatabase1().getConnection().getConnectionURL());
    super.show();
  }

  private void selectPath() {
    SelectPathDialog spd = new SelectPathDialog(this);
    spd.loadsave = loadsave;
    spd.show();
    loadsave = spd.loadsave;
  }

  private void showTableView() {
    new Thread() {
      public void run() {
        QueryDataSet vset = new QueryDataSet();
        KreirDrop kdp = KreirDrop.getModule(tm.getValueAt(jTable1.getSelectedRow(), 9).toString());
        if (kdp != null) {
          view.hide();
          view.clearColumns();
          hr.restart.sisfun.raDelayWindow refr = hr.restart.sisfun.raDelayWindow.show(250);
          kdp.createFilteredDataSet(vset, "");
          vset.open();
          int[] cols = new int[vset.getColumnCount()];
          for (int i = 0; i < vset.getColumnCount(); i++) {
            view.addColumn(vset.getColumn(i));
            vset.getColumn(i).setVisible(com.borland.jb.util.TriStateProperty.TRUE);
            cols[i] = i;
          }
          view.setDataSet(vset);
          view.setTitle("Pregled tablice "+tm.getValueAt(jTable1.getSelectedRow(), 0));
          view.setVisibleCols(cols);

//            view.jp.getNavBar().getColBean().initialize();

          view.show();
          refr.close();
//                System.out.println(view.jp.getMpTable().getSize().width +" "+ view.jp.getSize().width);
//                System.out.println(view.jp.getMpTable().getVisibleRect());

          view.resizeLater();
        }
      }
    }.start();
  }

  private void headerClicked(int col) {
    if (col == -1) return;
//    System.out.println(col + "  " + jTable1.convertColumnIndexToModel(col));
    tm.sortColumn(jTable1.convertColumnIndexToModel(col));
  }

  private void extendedCopyRow() {
    int currentRow;
    if ((currentRow = jTable1.getSelectedRow()) == -1)
      return;
    cdsel.show();
    if (!cdsel.isOK()) return;
    String exp = cdsel.getTab().length() == 0 ? "*" : cdsel.getTab();
    raGlob wild = new raGlob(exp.toLowerCase());
    String app = cdsel.getApp();
    if (app.length() > 0) tab.open();
    boolean copy;
    for (int y = 0; y < jTable1.getRowCount(); y++) {
      copy = false;
      if (app.length() == 0)
        copy = wild.matches(tm.getValueAt(y, 0).toString().toLowerCase());
//        wild.printLastMatch();
      else if (lookupData.getlookupData().raLocate(tab, "IMETAB", tm.getValueAt(y, 0).toString()))
        copy = wild.matches(tab.getString("IMETAB").toLowerCase()) &&
               app.equalsIgnoreCase(tab.getString("APP"));
      if (copy)
        for (int x = 2; x <= 4; x++)
          tm.setValueAt(tm.getValueAt(currentRow, x), y, x);
    }
  }

  private void kopirajrow(){
    int currentRow;
    if ((currentRow = jTable1.getSelectedRow()) == -1)
      return;
    for (int y = 0; y < jTable1.getRowCount(); y++)
      for (int x = 2; x <= 4; x++)
        tm.setValueAt(tm.getValueAt(currentRow, x), y, x);
/*    jTable1.revalidate();
    jTable1.repaint(); */
  }

  private void checkValidity(int row, int col) {
    int[][] invalid = {
      {TABLE_CREATE, INDEX_RECREATE, INDEX_CREATE},
      {TABLE_CREATE, INDEX_DROP, INDEX_CREATE},
      {TABLE_RECREATE, INDEX_CREATE, INDEX_RECREATE},
      {TABLE_SAVE, INDEX_ANY, INDEX_NOP},
      {TABLE_LOAD, INDEX_ANY, INDEX_NOP},
      {TABLE_DELETE, INDEX_ANY, INDEX_NOP},
      {TABLE_CHECK, INDEX_ANY, INDEX_NOP},
      {TABLE_UNLOCK, INDEX_ANY, INDEX_NOP},
      {TABLE_UPDATE, INDEX_ANY, INDEX_NOP},
      {TABLE_DROP, INDEX_ANY, INDEX_DROP},

      {TABLE_CREATE, INDEX_NOP, INDEX_CREATE},
      {TABLE_RECREATE, INDEX_NOP, INDEX_RECREATE},
      {TABLE_NOP, INDEX_ANY, INDEX_NOP}};

    /*String[][] invalid = {
      {"Kreiraj", "Rekreiraj", "Kreiraj"},
      {"Rekreiraj", "Kreiraj", "Rekreiraj"},
      {"Spremi", "*", "Ne diraj"},
      {"Napuni", "*", "Ne diraj"},
      {"Obriši", "*", "Ne diraj"},
      {"Provjeri", "*", "Ne diraj"},
      {"Otklju\u010Daj", "*", "Ne diraj"},
      {"Ažuriraj", "*", "Ne diraj"},
      {"Dropaj", "*", "Ne diraj"},

      {"Kreiraj", "Ne diraj", "Kreiraj"},
      {"Rekreiraj", "Ne diraj", "Rekreiraj"},
      {"Ne diraj", "*", "Ne diraj"}}; */
    int check = (col == 3 ? invalid.length : 10);

    for (int i = 0; i < check; i++)
      if (tm.getValueAt(row, 3).equals(tableComm[invalid[i][0]]) &&
          (invalid[i][1] == INDEX_ANY ||
          tm.getValueAt(row, 2).equals(indexComm[invalid[i][1]])))
        tm.setValueAt(indexComm[invalid[i][2]], row, 2);

    if (tm.getValueAt(row, 2).equals(indexComm[INDEX_NOP]) &&
        tm.getValueAt(row, 3).equals(tableComm[TABLE_NOP]))
      tm.setValueAt(laz, row, 4);
    else {
      tm.setValueAt(istina, row, 4);
      tm.setValueAt("Nemam pojma", row, 1);
    }
  }

  public void initialize(startFrame sf) {
    hr.restart.sisfun.raDelayWindow dw = null;
    String imet;
    subdir = "defdata" + System.getProperty("file.separator");
    raStatusBar status = null;
    if (sf == null) dw = hr.restart.sisfun.raDelayWindow.show(null, "Inicijalizacija baze...", 0);
    else sf.statusMSG("Inicijalizacija baze...");
//    baza.DajKonekciju();
    try {
      KreirDrop kdp = (KreirDrop) Class.forName("hr.restart.baza.Parametri").newInstance();
      kdp.DropTable();
      kdp.KreirTable();
      kdp.KreirIdx();
      loadData(kdp);
      kdp.getQueryDataSet().refresh();
      if (sf == null) dw.setMessage("U\u010Ditavanje liste tablica ...", true, 0);
      else sf.statusMSG("U\u010Ditavanje liste tablica ...");
      kdp = (KreirDrop) Class.forName("hr.restart.baza.Tablice").newInstance();
      kdp.DropTable();
      kdp.KreirTable();
      kdp.KreirIdx();
      loadData(kdp);
      QueryDataSet tables = kdp.getQueryDataSet();
      tables.refresh();
      if (sf == null) dw.setMessage("Inicijalizacija tablica ...", false, 0);
      else {
        sf.statusMSG("Inicijalizacija tablica ...");
        status = raStatusBar.getStatusBar();
        status.startTask(tables.rowCount() + 2, "");
      }
      tables.first();
      while (tables.inBounds()) {
        imet = tables.getString("IMETAB");
        if (!imet.equals("Tablice") && !imet.equals("Parametri")) {
          if (sf == null) dw.setMessage("Inicijalizacija tablice " + imet + " ...", false, 0);
          else status.next("Inicijalizacija tablice " + imet + " ...");
          try {
            kdp = (KreirDrop) Class.forName(tables.getString("KLASATAB")).newInstance();
            kdp.DropTable();
            kdp.KreirTable();
            kdp.KreirIdx();
            loadData(kdp);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        tables.next();
      }
    } catch (Exception e) {
      e.printStackTrace();
//      System.out.println(e.getMessage());
    }
    if (status != null) status.finnishTask();
    if (sf == null) dw.close();
    else sf.statusMSG();
//    baza.ZatvoriKonekciju();
  }

  private int maxctab;
  private boolean tabChanged, checkTab, tabCreated;

  private void displayInsertError(String table, HashMap row) {
    KreirDrop mod = KreirDrop.getModuleByName(table);
    VarStr errlin = new VarStr(table).append(": neuspjesno dodavanje reda - ");
    for (Iterator i = row.keySet().iterator(); i.hasNext(); ) {
      String coln = (String) i.next();
      Column col = mod.getColumn(coln);
      if (col != null && col.isRowId())
        errlin.append(coln).append('=').append(row.get(coln)).append(';');
    }
    System.err.println(errlin.chop().toString());
    if (row.containsKey(KreirDrop.ERROR_KEY))
      System.err.println(row.get(KreirDrop.ERROR_KEY));
  }

  public void runProcess(){
    tabChanged = tabCreated = false;
    KreirDrop.installNotifier(new raTransferNotifier() {
      private int total, errs;
      public void rowTransfered(String table, int action, int row, Object data) {
        raProcess.checkClosing();
        int ev;
        switch (action) {
          case raTransferNotifier.INSERT_STARTED:
            total = errs = 0;
            break;
          case raTransferNotifier.DUMP_STARTED:
            total = row;
            break;
          case raTransferNotifier.ROW_INSERT_FAILED:
            if (++errs <= 100) {
              displayInsertError(table, (HashMap) data);
              if (errs == 100) System.err.println("Prikazano prvih 100 grešaka.");
            }
          case raTransferNotifier.ROW_INSERTED:
            ev = Math.min(17, (int) Math.round(Math.sqrt(++total)) + 1);
            if (row % ev == 0)
              raProcess.setMessage(table + ": napunjeno "+row+"("+total+") redova", false);
            break;
          case raTransferNotifier.ROW_STORED:
            ev = Math.min(17, (int) Math.round(Math.sqrt(total)) + 1);
            if (row % ev == 1)
              raProcess.setMessage(table + ": spremljeno "+row+"/"+total+" redova", false);
            break;
          case raTransferNotifier.DUMP_SEGMENT_FINISHED:
            raProcess.setMessage(table + ": spremljeno "+row+"/"+total+" redova", false);
            break;
          case raTransferNotifier.INSERT_FINISHED:
            noteStatus = " "+row+"/"+total+ " redova";
            break;
          case raTransferNotifier.DUMP_FINISHED:
            raProcess.setMessage(table + ": spremljeno "+row+"/"+total+" redova", false);
            noteStatus = " "+Aus.getRedova(total);
            break;
        }
      }
    });
    try {
      tab.refresh();
      Valid.getValid().execSQL("SELECT MAX(ctab) AS maxc FROM tablice");
      Valid.getValid().RezSet.open();
      maxctab = Valid.getValid().RezSet.getInt("MAXC");
      Valid.getValid().RezSet.close();
      Valid.getValid().RezSet = null;
    } catch (Exception e) {
      e.printStackTrace();
      maxctab = 0;
    }
//    dM.getDataModule().getDatabase1().closeConnection();
//    baza.DajKonekciju();
    try {
    for (int x = 0; x<tm.getRowCount();x++){
      raProcess.checkClosing();
      raProcess.setMessage("Tablica "+tm.getValueAt(x, 0), false);
      if (tm.getValueAt(x,4).equals(istina)){
        try {
          String status = "";
          int cidx = INDEX_NOP, ctab = TABLE_NOP;
          boolean tabtab = tm.getValueAt(x,0).toString().equalsIgnoreCase("tablice");
          KreirDrop kdp = KreirDrop.getModule(tm.getValueAt(x,9).toString());

          if (kdp == null) {
            System.out.println("HEJ! nema modula!!");
            Class klasica = Class.forName(tm.getValueAt(x,9).toString());
            kdp = (KreirDrop) klasica.newInstance();
          }

          checkTab = false;

          for (int c = 0; c < indexComm.length; c++)
            if (tm.getValueAt(x, 2).equals(indexComm[c])) cidx = c;
          for (int c = 0; c < tableComm.length; c++)
            if (tm.getValueAt(x, 3).equals(tableComm[c])) ctab = c;

          if (cidx == INDEX_DROP || cidx == INDEX_RECREATE ||
              ctab == TABLE_DROP || ctab == TABLE_RECREATE) {
            if (kdp.DropIdx()) tm.setValueAt("Indeksi dropani!", x, 1);
            else tm.setValueAt("Greška!", x, 1);
          }

          if (ctab == TABLE_DROP || ctab == TABLE_RECREATE) {
            raProcess.setMessage("Dropanje tablice "+tm.getValueAt(x, 0), false);
            if (!Valid.getValid().runSQL("DELETE FROM "+tm.getValueAt(x, 0)) || !kdp.DropTable()) {
              tm.setValueAt("Tablica zauzeta!",x,1);
              cidx = INDEX_NOP;
              ctab = TABLE_NOP;
              /*if (tm.getValueAt(x,3).equals("Dropaj"))
                continue; */
            } else if (ctab == TABLE_DROP) status = "Tablica dropana";
            if (ctab != TABLE_NOP && tabtab) tabCreated = true;
          }

          if (ctab == TABLE_CREATE || ctab == TABLE_RECREATE) {
            raProcess.setMessage("Kreiranje tablice "+tm.getValueAt(x, 0), false);
            if (kdp.KreirTable()) {
              status = ctab == TABLE_CREATE ? "Tablica iskreirana!" : "Tablica rekreirana!";
              if (ctab == TABLE_CREATE) checkTab = true;
              if (tabtab) tabCreated = true;
            } else {
              tm.setValueAt("Tablica zauzeta!",x,1);
              continue;
            }
          }

          if (ctab == TABLE_CREATE || ctab == TABLE_RECREATE ||
              cidx == INDEX_CREATE || cidx == INDEX_RECREATE) {
            if (kdp.KreirIdx()) {
              if (status.equals("")) status = "Indeksi iskreirani!";
            } else {
              if (status.equals("")) status = "Indeksi iskreirani!";
            }
          }
          if (tabtab)
            if (ctab == TABLE_LOAD || ctab == TABLE_DELETE || ctab == TABLE_UNLOCK)
              tabCreated = true;
          if (ctab == TABLE_SAVE)
            status = dumpData(kdp) + noteStatus;
          if (ctab == TABLE_LOAD)
            status = loadData(kdp) + noteStatus;
          if (ctab == TABLE_DELETE) {
            raProcess.setMessage("Brisanje tablice "+tm.getValueAt(x, 0), false);
            if (Valid.getValid().runSQL("DELETE FROM "+tm.getValueAt(x, 0)))
              status = "Tablica obrisana!";
            else status = "Greška kod brisanja!";
//            System.out.println(tm.getValueAt(x, 0)+" obrisan!");
          }
          if (ctab == TABLE_CHECK) {
            raProcess.setMessage("Provjera tablice "+tm.getValueAt(x, 0), false);
            status = ConsoleCreator.checkData(kdp, (String) tm.getValueAt(x,0));
            checkTab = true;
//            checkTableRow(x);
          }
          if (ctab == TABLE_UNLOCK) {
            raProcess.setMessage("Otkljuèavanje tablice "+tm.getValueAt(x, 0), false);
            if (Valid.getValid().runSQL("UPDATE "+tm.getValueAt(x, 0)+" SET LOKK='N'"))
              status = "Tablica otklju\u010Dana!";
            else status = "Greška kod otkljuèavanja!";
          }
          if (ctab == TABLE_UPDATE) {
            raProcess.setMessage("Provjera tablice "+tm.getValueAt(x, 0), false);
            status = ConsoleCreator.checkData(kdp, (String) tm.getValueAt(x,0));
            if (status.equals("Tablica je OK")) {
              status = "Tablica nepromijenjena!";
            } else if (ConsoleCreator.fastUpdate(kdp, (String) tm.getValueAt(x,0))) {
              status = "Tablica ažurirana! (q)";
            } else {
              if (!status.equals("Tablica ne postoji!"))
                status = dumpData(kdp);
              else checkTab = true;
              if (!status.endsWith("spremanja!")) {
                if (tabtab) tabCreated = true;
                raProcess.setMessage("Rekreiranje tablice "+tm.getValueAt(x, 0), false);
                if (!kdp.DropTable() && !checkTab) {
                  status = "Tablica zauzeta!";
                } else {
                  kdp.KreirTable();
                  kdp.KreirIdx();
                  if (status.endsWith("spremljen!")) {
                    status = loadData(kdp);
                    if (status.equals("Tablica napunjena!"))
                      status = "Tablica ažurirana!" + noteStatus;
                    else status = "Tablica rekreirana!";
                  } else if (checkTab) status = "Tablica kreirana!";
                  else status = "Tablica rekreirana!";
                }
              }
            }
          }
          System.out.println(status);
          if (tm.getValueAt(x,2).equals(indexComm[INDEX_NOP]) &&
              tm.getValueAt(x,3).equals(tableComm[TABLE_NOP]))
            status = "Nemam pojma";
          if (!status.equals("")) tm.setValueAt(status,x,1);
          if (checkTab) checkTableRow(x);
        } catch (ProcessInterruptException e) {
          throw (ProcessInterruptException) e.fillInStackTrace();
        } catch (Exception e) {
          e.printStackTrace();
//          System.out.println(e.getMessage());
        }
      }
    }
    } finally {
      try {
        raProcess.setMessage("Finaliziranje procesa...", true);
//        baza.ZatvoriKonekciju();
        if (tabChanged && !tabCreated) {
          tab.saveChanges();
          dM.getDataModule().getTablice().refresh();
        }
        if (tabCreated) tab.refresh();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  String lastapp = "";
  private void checkTableRow(int row) {
    if (maxctab == 0) return;

    if (!lookupData.getlookupData().raLocate(tab, "IMETAB",
        tm.getValueAt(row, 0).toString(), Locate.CASE_INSENSITIVE)) {
      System.out.println("Ubacujem tablicu u tablicu tablica");
      tab.insertRow(false);
      tab.setInt("CTAB", ++maxctab);
      tab.setString("KLASATAB", tm.getValueAt(row, 9).toString());
      tab.setString("IMETAB", tab.getString("KLASATAB").
                    substring(tab.getString("KLASATAB").lastIndexOf(".") + 1));
      tab.post();
      tabChanged = true;
    }
    if (!tab.getString("IMETAB").equals(tm.getValueAt(row, 0).toString()))
      System.out.println("Pogresno ime tablice: " + tm.getValueAt(row, 0));
    if (tab.getString("APP") == null || tab.getString("APP").length() == 0) {
//      Object app = JOptionPane.showInputDialog(this, "Aplikacija kojoj pripada tablica "+
//                                  tab.getString("IMETAB"), "Unos aplikacije",
//                                  JOptionPane.PLAIN_MESSAGE, null, null, lastapp);
//      if (app != null) {
        tab.setString("APP", "!undef!");
        tab.post();
        tabChanged = true;
//      }
    }
//    Valid.getValid().execSQL("SELECT MAX(ctab*1));
  }

  public void pressCancel(){
    this.hide();
    if (standalone)
      System.exit(0);
  }
  public static void main(String[] args) {

//    new IntParam();
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    getKreator();
    if (args.length == 1 && args[0].equalsIgnoreCase("initialize"))
      kr.initialize(null);
    else {
      kr.standalone = true;
      if (args.length > 0) {
        kr.command = VarStr.join(args, ' ');
        kr.addWindowListener(new WindowAdapter() {
          public void windowOpened(WindowEvent e) {
            kr.performCommand();
          }
        });
      }
      kr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      kr.pack();
      kr.show();
    }
  }
//  {"Ne diraj", "Spremi", "Napuni", "Obriši",
//    "Otklju\u010Daj", "Provjeri", "Ažuriraj", "Kreiraj", "Rekreiraj", "Dropaj"};
  private void performCommand() {
    String[] com = command.split();
    int ncom = -1;
    String[] comms = {"nop", "save", "load", "delete", "unlock", "check",
      "update", "create", "recreate", "drop"};
    for (int i = 0; i < comms.length; i++)
      if (comms[i].equalsIgnoreCase(com[0])) ncom = i;
    if (ncom <= 0) {
      command = null;
      System.err.println("Invalid command: "+com[0]);
      return;
    }
    for (int i = 1; i < com.length; i++) {
      raGlob g = new raGlob(com[i].toLowerCase());
      for (int x = 0; x < tm.getRowCount(); x++)
        if (g.matches(tm.getValueAt(x, 0).toString().toLowerCase()))
          tm.setValueAt(tableComm[ncom], x, 3);
    }
    if (com.length == 1)
      for (int x = 0; x < tm.getRowCount(); x++)
        tm.setValueAt(tableComm[ncom], x, 3);
    pressOK();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        pressCancel();
      }
    });
  }
  
/*  private void checkbd() {
    String cz = "09";
    DataSet zt = dM.getDataModule().getZavtr();
    zt.open();
    if (!lookupData.getlookupData().raLocate(zt, "CZT", cz)) {
      zt.insertRow(false);
      zt.setString("CZT", cz);
    } else {
      zt.deleteRow();
      zt.insertRow(false);
      zt.setString("CZT", cz);
    }
    zt.setBigDecimal("IZT", new BigDecimal(0));
    zt.setBigDecimal("IZT", new BigDecimal(3245.234363456).setScale(2, BigDecimal.ROUND_HALF_UP));
    System.out.println(zt);
    System.out.println(zt.getBigDecimal("IZT"));
    zt.saveChanges();
    zt = Zavtr.getDataModule().getFilteredDataSet("czt='"+cz+"'");
    zt.open();
    System.out.println(zt);
    System.out.println(zt.getBigDecimal("IZT"));
    Valid.getValid().execSQL("SELECT * FROM zavtr WHERE czt='"+cz+"'");
    Valid.getValid().RezSet.open();
    System.out.println(Valid.getValid().RezSet);
    System.out.println(Valid.getValid().RezSet.getBigDecimal("IZT"));
    Valid.getValid().execSQL("SELECT izt*1 as izt FROM zavtr WHERE czt='"+cz+"'");
    Valid.getValid().RezSet.open();
    System.out.println(Valid.getValid().RezSet);
    System.out.println(Valid.getValid().RezSet.getDouble("IZT"));
  } */

  public String loadData(KreirDrop kdp) {
    try {
      noteStatus = "";
      File dir = loadsave != null ? loadsave : new File(subdir);
      if (kdp.insertData(dir) > 0)
        return "Tablica napunjena!";
      else return "Datoteka prazna!";
    } catch (ProcessInterruptException e) {
      throw (ProcessInterruptException) e.fillInStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return "Datoteka ne postoji!";
    } catch (Exception e) {
      e.printStackTrace();
      return "Greška!";
    }
  }

  public String dumpData(KreirDrop kdp) {
    try {
      noteStatus = "";
      File dir = loadsave != null ? loadsave : new File(subdir);
      String tname = kdp.getColumns()[0].getTableName().toLowerCase();
      raProcess.setMessage("Provjera opsega tablice "+kdp.Naziv+" ...", false);
      int totalRows = kdp.getRowCount();
      if (totalRows * kdp.getColumns().length < ConsoleCreator.maxLoad) {
        raProcess.setMessage("Otvaranje tablice "+kdp.Naziv+" ...", false);
        DataSet ds = Util.getNewQueryDataSet("SELECT * FROM "+tname);
        if (kdp.dumpTable(ds, dir) > 0 || (ds.close() && false))
          return tname + ".dat - spremljen!";
        return "Tablica prazna!";
      }
      
      raProcess.setMessage("Segmentiranje tablice "+kdp.Naziv+" ...", false);
      Int2 sgt = kdp.findBestKeyForSegments();
      if (sgt == null) return "Greška kod spremanja!";
      
      String bestCol = kdp.getColumns()[sgt.one].getColumnName().toLowerCase();
      int bestNum = sgt.two;
      int minSegments = totalRows * kdp.getColumns().length / ConsoleCreator.maxLoad + 2;
      if (bestNum / 10 <= minSegments)
        return "Greška kod spremanja!";

      raProcess.setMessage("Odreðivanje uvjeta za " + 
          Aus.getNum(minSegments, "segment", "segmenta", "segmenata") + " ...", false);

      Condition[] conds = kdp.createSegments(bestCol, minSegments);
      if (kdp.dumpSegments(dir, conds) > 0)
        return tname + ".dat - spremljen!";
      return "Greška kod spremanja!";
    } catch (ProcessInterruptException e) {
      throw (ProcessInterruptException) e.fillInStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
      return "Greška kod spremanja!";
    }
  }

  public static class SelectPathDialog extends JraDialog {
    JraTextField path = new JraTextField(48) {
      public void focusGained(java.awt.event.FocusEvent e) {}
      public boolean maskCheck() { return true; }
    };
    JraButton doh = new JraButton();
    OKpanel okp = new OKpanel() {
      public void jBOK_actionPerformed() {
        OKPress();
      }
      public void jPrekid_actionPerformed() {
        CancelPress();
      }
    };

    JDirectoryChooser dc = new JDirectoryChooser();
    public File loadsave;
    File oldls;
    public boolean oksel;

    
    public SelectPathDialog(Frame owner, String title) {
      super(owner, title, true);
      try {
        init();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    public SelectPathDialog(Frame owner) {
      this(owner, "Putanja za spremanje i dohvat podataka");
    }
    
    public SelectPathDialog(Dialog owner, String title) {
      super(owner, title, true);
      try {
        init();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    private void init() throws Exception {
      doh.setText("Dohvat");
      JPanel up = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 4));
      up.add(path);
      okp.add(doh, BorderLayout.WEST);
      this.getContentPane().add(up, BorderLayout.NORTH);
      this.getContentPane().add(okp, BorderLayout.SOUTH);
      this.pack();
      this.setLocationRelativeTo(this.getOwner());
      this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      this.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          CancelPress();
        }
      });
      path.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          OKPress();
        }
      });
      path.addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == e.VK_F9) {
            selectFile();
          }
        }
      });
      doh.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          selectFile();
        }
      });
      okp.registerOKPanelKeys(this);
      dc.setFileSelectionMode(dc.DIRECTORIES_ONLY);
      dc.setDialogTitle("Odabir putanje");
    }

    private void selectFile() {
      try {
        File cur = new File(path.getText());
        if (cur.exists() && cur.isDirectory())
          dc.setCurrentDirectory(cur);
      } catch (Exception e) {}
      if (dc.showDialog(this) == dc.APPROVE_OPTION) {
        path.setText(dc.getSelectedFile().getAbsolutePath());
      }
    }

    private void CancelPress() {
      oksel = false;
      loadsave = oldls;
      if (loadsave!= null && loadsave.equals(Aus.getCurrentDirectory()))
          loadsave = null;
      dispose();
    }

    private void OKPress() {
      oksel = false;
      try {
        loadsave = new File(path.getText());
        if (loadsave.exists() && loadsave.isDirectory())
          oksel = true;
      } catch (Exception e) {}
      if (!oksel) {
        JOptionPane.showMessageDialog(this, "Pogrešna putanja!", "Greška",
                                      JOptionPane.ERROR_MESSAGE);
      } else {
        if (loadsave.equals(Aus.getCurrentDirectory()))
          loadsave = null;
        dispose();
      }
    }

    public void show() {
      oldls = loadsave;
      if (loadsave == null)
        loadsave = Aus.getCurrentDirectory();
      dc.setCurrentDirectory(loadsave);
      path.setText(loadsave.getAbsolutePath());
      super.show();
    }
  }
}

class CopyDialog extends JraDialog {
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlApp = new JLabel();
  JLabel jlImetab = new JLabel();
  JTextField jraImetab = new JTextField();
  JTextField jlrApp = new JTextField();
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      CancelPress();
    }
  };

  boolean ok;

  public CopyDialog(kreator kr) {
    super(kr, "Kriterij selekcije", true);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    this.setLocationRelativeTo(kr);
  }

  public void show() {
    ok = false;
    this.setLocationRelativeTo(kreator.kr);
    super.show();
  }

  private void OKPress() {
    ok = true;
    this.hide();
  }

  private void CancelPress() {
    this.hide();
  }

  public boolean isOK() {
    return ok;
  }

  public String getTab() {
    return jraImetab.getText().trim();
  }

  public String getApp() {
    return jlrApp.getText().trim();
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(270);
    lay.setHeight(85);

    jlApp.setText("Aplikacija");
    jlImetab.setText("Tablica");

    jpDetail.add(jlApp, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlImetab, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlrApp, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jraImetab, new XYConstraints(150, 45, 100, -1));

    okp.registerOKPanelKeys(this);
    jraImetab.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_ENTER)
          OKPress();
      }
    });
    jlrApp.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_ENTER)
          jraImetab.requestFocus();
      }
    });
    this.getContentPane().add(jpDetail, BorderLayout.CENTER);
    this.getContentPane().add(okp, BorderLayout.SOUTH);
    this.addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        jraImetab.requestFocus();
      }
    });
    this.pack();
  }
}


class TabeleZaKreiranje {

  public String ime_tabele;
  public String ime_klase;
  public String status ;
  public String kreat_tabela; /// opcije dropaj, kreiraj, rekreiraj
  public String kreat_index; /// opcije dropaj , kreiraj, rekreiraj
  public Boolean sto_sad;
  public TabeleZaKreiranje(String i_t,String i_k,String stat,String k_t,String k_i,Boolean s_s){

    ime_tabele   = i_t;
    ime_klase    = i_k;
    status=stat;
    kreat_tabela = k_t;
    kreat_index  = k_i;
    sto_sad = s_s;

  }
  public String getElement(int col) {
    if (col == 0) return ime_tabele;
    else if (col == 1) return status;
    else if (col == 2) return kreat_tabela;
    else if (col == 3) return kreat_index;
    else if (col == 4) return sto_sad.toString();
    else return "";
  }
}

class DynamicComparator implements Comparator {
  int column, inv = 1;
  public DynamicComparator(int col, boolean asc) {
    column = col;
    inv = !asc ? -1 : 1;
  }
//  public DynamicComparator(DynamicComparator c) {
//    this.column = c.column;
//    this.inverse = -1;
//  }
  public int compare(Object o1, Object o2) {
    if (!(o1 instanceof TabeleZaKreiranje) || !(o2 instanceof TabeleZaKreiranje))
      throw new RuntimeException("Invalid vector elements");
    return ((TabeleZaKreiranje) o1).getElement(column).compareToIgnoreCase(((TabeleZaKreiranje) o2).getElement(column)) * inv;
  }
}

class ColumnData  {
  public String m_title;
  public int m_width;
  public int m_alignment;
  public ColumnData(String title, int width, int alignment) {
    m_title = title;
    m_width = width;
    m_alignment = alignment;
  }
}


class TableModelZaKreiranje extends javax.swing.table.AbstractTableModel {

  static final public ColumnData m_columns[] = {
    new ColumnData( "Tablica", 150, JLabel.LEFT ),
    new ColumnData( "Status", 360, JLabel.LEFT ),
    new ColumnData( "Indekse", 120, JLabel.LEFT ),
    new ColumnData( "Tablice", 120, JLabel.LEFT ),
    new ColumnData( "Akcija", 60, JLabel.CENTER)};

  java.util.Vector tm_vector;
//  BazaOper baza ;
  int sortColumn = -1;
  boolean ascending;

  public TableModelZaKreiranje(){

    tm_vector = new  java.util.Vector();
//    baza= new BazaOper() ;
    fillVector(tm_vector);

  }

  public int getSortedColumn() {
    return sortColumn;
  }

  public boolean isAscending() {
    return ascending;
  }

  public void sortColumn(int col) {
    if (col >= 0 && col < 5)
      Collections.sort(tm_vector, new DynamicComparator(col,
          ascending = !(sortColumn == col && ascending)));
    sortColumn = col;
    this.fireTableDataChanged();
  }
/*
 * Ovo je privremeno dok se ne najde rjesenje za punjenje dinamicki vektora
 */
  public void fillVector(java.util.Vector vct){

    String [] imena_klasa = ConsoleCreator.getModuleClasses();

//    Arrays.sort(imena_klasa, new Comparator() {
//      public int compare(Object o1, Object o2) {
//        return ((String) o1).compareToIgnoreCase((String) o2);
//      }
//    });

    dM.getDataModule();
    for (int i = 0; i< imena_klasa.length;i++) {
      try {
        KreirDrop kd = KreirDrop.getModule(imena_klasa[i].toString());
        if (kd == null) {
          Class kljasa = Class.forName(imena_klasa[i].toString());
          kd= (hr.restart.baza.KreirDrop) kljasa.newInstance() ;
        }
//        trut=kd.TestTabele(baza) ;
/// ovo je sporo ko pakao
//        vct.addElement(new TabeleZaKreiranje(kd.Naziv,imena_klasa[i],trut ? "Tabela - kreirana":"Tabela - neiskreirana",
//            trut ?"Dropaj":"Kreiraj",trut ?"Dropaj":"Kreiraj",new java.lang.Boolean("false")));
    vct.addElement(new TabeleZaKreiranje(kd.Naziv,imena_klasa[i].toString(),"Nemam pojma","Ne diraj","Ne diraj",new java.lang.Boolean("false")));

      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Greška u " + e.getMessage()+imena_klasa[i].toString());
      }

    }
  }

  public int getRowCount(){
      return tm_vector== null ? 0: tm_vector.size();
  }
  public int getColumnCount(){
      return 5;
  }
  public Object getValueAt(int row,int column){

    if (row > getRowCount() || row < 0)
      return "";

    TabeleZaKreiranje red =(TabeleZaKreiranje)tm_vector.elementAt(row);
    switch (column) {

      case 0 : return red.ime_tabele;
//      case 1 : return red.ime_klase;
      case 1 : return red.status;
      case 2 : return red.kreat_tabela;
      case 3 : return red.kreat_index;
      case 4 : return red.sto_sad;
      case 9 : return red.ime_klase;
    }
        return "";
  }

  public String getColumnName(int column) {
    return m_columns[column].m_title;
  }

  public Class getColumnClass(int c) {
    return getValueAt(0, c).getClass();
  }

  public void setValueAt(Object value, int nRow, int nCol){
    if (nRow < 0 || nRow>=getRowCount())
      return ;

     TabeleZaKreiranje red =(TabeleZaKreiranje)tm_vector.elementAt(nRow);
     String svalue=value.toString();
     boolean changed = true;

     switch (nCol) {
       case 1 : if (red.status.equals(svalue)) changed = false; red.status = svalue ; break ;
       case 2 : if (red.kreat_tabela.equals(svalue)) changed = false; red.kreat_tabela = svalue ; break ;
       case 3 : if (red.kreat_index.equals(svalue)) changed = false; red.kreat_index = svalue ; break;
       case 4 : if (red.sto_sad.equals((Boolean) value)) changed = false; red.sto_sad = (Boolean) value; break;
       default: changed = false;
     }
     if (changed) fireTableCellUpdated(nRow, nCol);
    return ;
  }
  public boolean isCellEditable(int nRow, int nCol){

    return nCol==2 || nCol==3 || nCol==4;
  }
}
/*

class CheckCellRenderer extends JCheckBox implements TableCellRenderer {

  protected static javax.swing.border.Border m_noFocusBorder;

  public CheckCellRenderer() {
    super();
    m_noFocusBorder = new javax.swing.border.EmptyBorder(1, 2, 1, 2);
    setOpaque(true);
    setBorder(m_noFocusBorder);
  }

  public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus,
                                                  int row, int column) {

    if (value instanceof Boolean) {
      Boolean b = (Boolean)value;
      setSelected(b.booleanValue());
    }

    setBackground(isSelected && !hasFocus ?
    table.getSelectionBackground() : table.getBackground());
    setForeground(isSelected && !hasFocus ?
    table.getSelectionForeground() : table.getForeground());
    setFont(table.getFont());
    setBorder(hasFocus ? UIManager.getBorder("Table.focusCellHighlightBorder") : m_noFocusBorder);
    return this;
  }
}*/
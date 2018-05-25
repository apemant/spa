package hr.restart.gk;

import hr.restart.baza.Condition;
import hr.restart.baza.Gkrep;
import hr.restart.baza.Gkrepkonta;
import hr.restart.baza.dM;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.JraPanel;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraSplitPane;
import hr.restart.swing.JraTable2;
import hr.restart.swing.XYPanel;
import hr.restart.swing.raExtendedTable;
import hr.restart.swing.raInputDialog;
import hr.restart.swing.raTableModifier;
import hr.restart.util.*;
import hr.restart.util.columnsbean.ColumnsBean;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.RowFilterListener;
import com.borland.dx.dataset.RowFilterResponse;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

public class frmGkrepDef extends raFrame {
  
  raNavBar lnav = new raNavBar(raNavBar.EMPTY);
  raNavBar rnav = new raNavBar(raNavBar.EMPTY);
  JraSplitPane split = new JraSplitPane(JraSplitPane.HORIZONTAL_SPLIT);
  JList reports = new JList(new DefaultListModel());
  JTree struct = new JTree((TreeModel) null) {
    public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
      if (value instanceof DefaultMutableTreeNode)
        return getNode(value).toHtml();
      return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
    };
  };
  
  raNavAction rnvlAdd = new raNavAction("Novi izvještaj",raImages.IMGADD,KeyEvent.VK_F2) {
    public void actionPerformed(ActionEvent e) {
      addReport();
    }
  };
  raNavAction rnvlUpdate = new raNavAction("Izmjena naziva",raImages.IMGCHANGE,KeyEvent.VK_F4) {
    public void actionPerformed(ActionEvent e) {
      updateReport();
    }
  };
  raNavAction rnvlDelete = new raNavAction("Brisanje izvještaja",raImages.IMGDELETE,KeyEvent.VK_F3) {
    public void actionPerformed(ActionEvent e) {
      deleteReport();
    }
  };

  raNavAction rnvlExit = new raNavAction("Izlaz",raImages.IMGX,KeyEvent.VK_ESCAPE) {
    public void actionPerformed(ActionEvent e) {
      close();
    }
  };
  
  
  raNavAction rnvrAdd = new raNavAction("Nova pozicija",raImages.IMGADD,KeyEvent.VK_F5) {
    public void actionPerformed(ActionEvent e) {
      addNode();
    }
  };
  raNavAction rnvrAddKon = new raNavAction("Novi raspon konta",raImages.IMGCOPYCURR,KeyEvent.VK_F6) {
    public void actionPerformed(ActionEvent e) {
      addKonto();
    }
  };
  raNavAction rnvrUpdate = new raNavAction("Izmjena pozicije",raImages.IMGCHANGE,KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      updateNode();
    }
  };
  raNavAction rnvrDelete = new raNavAction("Brisanje pozicije",raImages.IMGDELETE,KeyEvent.VK_F8) {
    public void actionPerformed(ActionEvent e) {
      deleteNode();
    }
  };
  
  Action aAddPoz = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      addNode();
    }
  }; 
  
  Action aDeletePoz = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      deleteNode();
    }
  };
  
  Action aUpdatePoz = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      updateNode();
    }
  };
  
  Action aAddKonto = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      addKonto();
    }
  }; 
  
  Action aExpand = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      expandPath();
    }
  }; 
  
  raExtendedTable tab = new raExtendedTable() {
    public void rowChanged(int oldrow, int newrow, boolean toggle, boolean extend) {
      findTreeNode();
    }
    public void tableDoubleClicked() {
      checkTable2Click();
    };
    public void setRowSelectionInterval(int from, int to) {
      super.setRowSelectionInterval(from, to);
      findTreeNode();
    };
  };
  StorageDataSet data;
  
  StorageDataSet zag;
  XYPanel zpan;
  
  StorageDataSet poz;
  XYPanel ppan;
  
  StorageDataSet kon;
  XYPanel kpan;
  raComboBox rcb = new raComboBox();
  String[][] items;
  
  HashSet akonta = new HashSet();
  HashSet anakonta = new HashSet();
  HashSet vpoz = new HashSet();
  
  RowFilterListener expandState;

  public frmGkrepDef() {
    try {
      jbInit();
    } catch (Exception e) {
      // TODO: handle exception
    }
  }

  private boolean lazyinit = true;
  private void jbInit() throws Exception {
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(split);
    JraPanel lpan = new JraPanel(new BorderLayout());
    lpan.add(lnav, BorderLayout.NORTH);
    lpan.add(new JraScrollPane(reports));
    split.setLeftComponent(lpan);
    JraPanel rpan = new JraPanel(new BorderLayout());
    rpan.add(rnav, BorderLayout.NORTH);
    JraPanel view = new JraPanel(new BorderLayout());
    JraPanel tpan = new JraPanel(new BorderLayout());
    tpan.add(tab.getTableHeader(), BorderLayout.NORTH);
    tpan.add(tab);
    view.add(tpan);
    view.add(struct, BorderLayout.WEST);
    JraScrollPane msp = new JraScrollPane(view);
    rpan.add(msp);
    msp.getVerticalScrollBar().setUnitIncrement(tab.getRowHeight());
    split.setRightComponent(rpan);
    split.setPreferredSize(new Dimension(800,600));
    split.setDividerLocation(300);
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        close();
      }
    });
    reports.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    reports.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) updateTree();
      }
    });
    
    struct.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    struct.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        updateActions();
      }
    });
    struct.addTreeExpansionListener(new TreeExpansionListener() {
      public void treeExpanded(TreeExpansionEvent event) {
        checkExpandKeys(event.getPath(), true);
        refilterTable();
      }
      public void treeCollapsed(TreeExpansionEvent event) {
        refilterTable();
      }
    });
    struct.addTreeWillExpandListener(new TreeWillExpandListener() {
      public void treeWillExpand(TreeExpansionEvent event)
          throws ExpandVetoException {
        //
      }
      
      public void treeWillCollapse(TreeExpansionEvent event)
          throws ExpandVetoException {
        checkExpandKeys(event.getPath(), false);
      }
    });
    struct.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        checkPopup(e);
        checkDouble(e);
      }
      public void mousePressed(MouseEvent e) {
        checkPopup(e);
      }
      public void mouseReleased(MouseEvent e) {
        checkPopup(e);
      }
    });
    //struct.setCellRenderer(new MultiCellRenderer());
    struct.setToggleClickCount(0);
    struct.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
    struct.setCellRenderer(new DefaultTreeCellRenderer() {
      Font bold, normal;
      public Dimension getPreferredSize() {
        Dimension pref = super.getPreferredSize();
        if (pref != null && pref.width > 500) pref = new Dimension(500, pref.height);
        return pref;
      }
      public java.awt.Component getTreeCellRendererComponent(
          JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean foc) {
        JLabel comp = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, false);
        if (bold == null) {
          normal = comp.getFont();              
          bold = normal.deriveFont(Font.BOLD);
        }
        comp.setFont(row == 0 ? bold : normal);
        if (value instanceof DefaultMutableTreeNode) 
          if (getNode(value).bold.equals("D"))
            comp.setFont(bold);
        
        if (getPreferredSize().width < super.getPreferredSize().width)
          setToolTipText(getText());
        else setToolTipText(null);
        return comp;
      }
    });
    
    AWTKeyboard.registerKeyListener(getWindow(), new KeyAdapter() {
      public void keyPressed( KeyEvent e){
        tab.processTableKeyEvent(e);
      }
      public void keyTyped( KeyEvent e){
        tab.processTableKeyTyped(e);
      }
    });
  
    reports.setDragEnabled(true);
    struct.setDragEnabled(true);
    struct.setTransferHandler(new TreeTransferHandler());
    struct.setDropMode(DropMode.INSERT);
    ToolTipManager.sharedInstance().registerComponent(struct);
    
    lnav.addOption(rnvlAdd);
    lnav.addOption(rnvlUpdate);
    lnav.addOption(rnvlDelete);
    lnav.addOption(rnvlExit);
    
    rnav.addOption(rnvrAdd);
    rnav.addOption(rnvrAddKon);
    rnav.addOption(rnvrUpdate);
    rnav.addOption(rnvrDelete);
    
    zag = Aus.createSet("NAZIV:500");
    zpan = new XYPanel(zag).configHeight(25, 30).label("Naziv izvještaja").skip(50).text("NAZIV").expand();
    
    poz = Aus.createSet("NAZIV:500 AOP:6 KALK:500 NEG:1 BOLD:1 XLSPOZ:20");
    ppan = new XYPanel(poz).configHeight(25, 30).label("Naziv pozicije").text("NAZIV", 300).skip(50).label("AOP", 60).text("AOP").nl();
    ppan.label("Definicija").text("KALK", 515).nl();
    ppan.check("Dopustiti negativne iznose", "NEG").skip(300).label("XLS pozicija", 115).text("XLSPOZ", ppan.s12_wid).nl();
    ppan.check("Podebljan ispis pozicije", "BOLD").expand();

    kon = Aus.createSet("RASPON:500 KALK:500 PS:1");
    rcb.setRaColumn("KALK");
    rcb.setRaDataSet(kon);
    rcb.setRaItems(items = new String[][] {
        {"Duguje", "ID"},
        {"Potražuje", "IP"},
        {"Duguje - Potražuje", "ID-IP"},
        {"Duguje + Potražuje", "ID+IP"},
        {"-Duguje", "-ID"},
        {"-Potražuje", "-IP"},
        {"-Duguje + Potražuje", "-ID+IP"},
        {"-Duguje - Potražuje", "-ID-IP"}
    });
    kpan = new XYPanel(kon).configHeight(25, 30).label("Raspon konta").text("RASPON", 350).combo(rcb, 200).nl();
    kpan.check("Uraèunati poèetno stanje", "PS").expand();
    
    data = Aus.createSet("CKEY KONTO:1 {AOP}AOP:6 {Izraèun konta}KKALK:500 {Izraèun pozicije}PKALK:500 {XLS pozicija}XLSPOZ:20");
    data.getColumn("CKEY").setVisible(0);
    data.getColumn("KONTO").setVisible(0);
    data.getColumn("KKALK").setWidth(20);
    data.getColumn("PKALK").setWidth(25);
    data.getColumn("XLSPOZ").setWidth(12);
    tab.disableSort(true);
    tab.setDataSet(data);
    struct.setRowHeight(tab.getRowHeight());
    tab.getTableHeader().setPreferredSize(new Dimension(tab.getPreferredSize().width, tab.getRowHeight()));
    tab.addTableModifier(new ColorModifier());
    
    data.addRowFilterListener(expandState = new RowFilterListener() {
      public void filterRow(ReadRow readrow, RowFilterResponse response) {
        if (vpoz.contains(new Integer(readrow.getInt("CKEY")))) response.add();
        else response.ignore();
      }
    });
  }
  
  void deleteReport() {
    Report rep = (Report) reports.getSelectedValue();
    if (rep == null) return;
    
    if (JOptionPane.showConfirmDialog(getWindow(), "Želite li pobrisati izvještaj \"" + rep.naziv + "\" (pažnja!)", 
        "Brisanje izvještaja", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
    
    try {
      QueryDataSet all = Gkrep.getDataModule().openTempSet();
      Condition cond = Aus.getDataTreeList(rep.key, all, "CPOZ", "CPRIP");
      Assert.is(Valid.getValid().runSQL("DELETE FROM gkrep WHERE " + cond));
      updateAll(null);
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(getWindow(), "Neuspješno brisanje izvještaja!", "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  void updateReport() {
    Report rep = (Report) reports.getSelectedValue();
    if (rep == null) return;
    
    zag.empty();
    zag.insertRow(false);
    zag.setString("NAZIV", rep.naziv);
    if (new raInputDialog().show(getWindow(), zpan, "Izmjena naziva izvještaja") 
        && zag.getString("NAZIV").length() > 0 && !zag.getString("NAZIV").equals(rep.naziv)) {
      try {
        QueryDataSet ds = Gkrep.getDataModule().openTempSet(Condition.equal("CKEY", rep.key));
        Assert.is(ds.rowCount() == 1);
        ds.setString("NAZIV", zag.getString("NAZIV"));
        ds.saveChanges();
        reloadReports();
        rep.setNaziv(zag.getString("NAZIV"));
        reports.repaint();
        
        DefaultTreeModel model = (DefaultTreeModel) struct.getModel();
        getNode(model.getRoot()).naziv = zag.getString("NAZIV");
        model.nodeChanged((DefaultMutableTreeNode) model.getRoot());
      } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(getWindow(), "Neuspješna izmjena izvještaja!", "Greška", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
  
  void addReport() {
    zag.empty();
    zag.insertRow(false);
    if (new raInputDialog().show(getWindow(), zpan, "Novi izvještaj") && zag.getString("NAZIV").length() > 0) 
      try {
        QueryDataSet ds = Gkrep.getDataModule().openEmptySet();
        String max = Aus.q("SELECT MAX(cpoz) AS cpoz FROM gkrep").getString("CPOZ");
        max = Aus.leadzero(Aus.getAnyNumber(max) + 1, 6);
        int kmax = Gkrep.getDataModule().getMax("CKEY") + 1;
        ds.insertRow(false);
        ds.setInt("CKEY", kmax);
        ds.setString("CPOZ", max);
        ds.setString("CPRIP", max);
        ds.setInt("NIVO", 0);
        ds.setString("NAZIV", zag.getString("NAZIV"));
      
        ds.saveChanges();
        updateAll(max);
      } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(getWindow(), "Neuspješno dodavanje izvještaja!", "Greška", JOptionPane.ERROR_MESSAGE);
      }
  }
  
  QueryDataSet reloadReports() {
    QueryDataSet ds = Gkrep.getDataModule().openTempSet("CPOZ=CPRIP");
    ds.setSort(new SortDescriptor(new String[] {"CPOZ"}));
    if (raLoader.isLoaderLoaded("hr.restart.gk.frmBilUpit")) {
      frmBilUpit rep = (frmBilUpit) raLoader.load("hr.restart.gk.frmBilUpit");
      rep.rcb.setRaItems(ds, "CPOZ", "NAZIV");
      rep.rcb.this_itemStateChanged();
    }
    return ds;
  }
  
  void updateAll(String aktiv) {
    QueryDataSet ds = reloadReports();
    DefaultListModel m = new DefaultListModel();
    for (ds.first(); ds.inBounds(); ds.next())
      m.addElement(new Report(ds.getString("CPOZ"), ds.getString("NAZIV")));
    reports.setModel(m);
    if (aktiv != null) reports.setSelectedValue(new Report(aktiv, ""), true);
    reports.repaint();
  }
  
  Node getNode(Object tn) {
    return (Node) ((DefaultMutableTreeNode) tn).getUserObject();
  }
  
  Node getChild(TreeNode tn, int idx) {
    return (Node) ((DefaultMutableTreeNode) tn.getChildAt(idx)).getUserObject();
  }
  
  Node getNode(TreePath tp) {
    return getNode(tp.getLastPathComponent());
  }
  
  void updateTree() {
    Report rep = (Report) reports.getSelectedValue();
    if (rep == null) {
      data.empty();
      vpoz.clear();
      struct.setModel((TreeModel) null);
      rnvrAdd.setEnabled(false);
      rnvrUpdate.setEnabled(false);
      rnvrDelete.setEnabled(false);
    } else {
      rnvrAdd.setEnabled(true);
      rnvrAddKon.setEnabled(false);
      rnvrUpdate.setEnabled(false);
      rnvrDelete.setEnabled(false);
      QueryDataSet all = Gkrep.getDataModule().openTempSet();
      DataTree dt = DataTree.getSubTree(rep.key, all, "CPOZ", "CPRIP");
      dt.sortDeep();
      HashDataSet hash = new HashDataSet(all, "CPOZ");
      DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Node(hash.get(rep.key)));
      if (!dt.isLeaf()) buildSubTree(root, dt, hash);
      DefaultTreeModel model = new DefaultTreeModel(root, true);
      struct.setModel(model);
      data.empty();
      vpoz.clear();
      for (int i = 0; i < struct.getRowCount(); i++) { 
        struct.expandRow(i);
        DefaultMutableTreeNode tn = (DefaultMutableTreeNode) struct.getPathForRow(i).getLastPathComponent();
        Node n = (Node) tn.getUserObject();
        if (i > 0) {
          vpoz.add(n.ik);
          data.insertRow(false);
          data.setInt("CKEy", n.ik.intValue());
          data.setString("AOP", n.aop);
          data.setString(n.isKonto() ? "KKALK" : "PKALK", n.kalk);
          data.setString("KONTO", n.isKonto() ? "D" : "N");
          data.setString("XLSPOZ", n.xls); 
          data.post();
        }
      }
      tab.clearSelection();
      struct.clearSelection();
      struct.scrollRowToVisible(0);
    }
  }
    
  void buildSubTree(DefaultMutableTreeNode parent, DataTree dt, HashDataSet ds) {
    for (Iterator i = dt.branches.iterator(); i.hasNext(); ) {
      DataTree st = (DataTree) i.next();
      DefaultMutableTreeNode child = new Node(ds.get(st.key)).wrap();
      parent.add(child);
      if (!st.isLeaf()) buildSubTree(child, st, ds);
    }
  }
  
  void updateActions() {
    TreePath tp = struct.getSelectionPath();
    if (tp == null) {
      rnvrAdd.setEnabled(true);
      rnvrAddKon.setEnabled(false);
      rnvrUpdate.setEnabled(false);
      rnvrDelete.setEnabled(false);
      tab.clearSelection();
    } else {
      
      DefaultMutableTreeNode dt = (DefaultMutableTreeNode) tp.getLastPathComponent();
      Node n = (Node) dt.getUserObject();
      
      boolean hasPoz = false, hasKon = false;
      for (int i = 0; i < dt.getChildCount(); i++) {
        Node c = getChild(dt, i);
        if (c.isPoz()) hasPoz = true;
        else hasKon = true;
      }
      
      rnvrAdd.setEnabled(!hasKon && n.isPoz());
      rnvrAddKon.setEnabled(!hasPoz && n.isPoz());
      rnvrUpdate.setEnabled(tp.getPathCount() > 1);
      rnvrDelete.setEnabled(tp.getPathCount() > 1);
      if (tp.getPathCount() == 1) {
        tab.clearSelection();
        tab.repaint();
      } else {
        data.goToRow(struct.getRowForPath(tp)-1);
      }
    }
  }
  
  void checkExpandKeys(TreePath path, boolean expanded) {
    DefaultMutableTreeNode tn = (DefaultMutableTreeNode) path.getLastPathComponent();
    for (int i = 0; i < tn.getChildCount(); i++) {
      Node n = getChild(tn, i);
      if (expanded) vpoz.add(n.ik);
      else vpoz.remove(n.ik);
      TreePath sub = path.pathByAddingChild(tn.getChildAt(i));
      if (struct.isExpanded(sub)) checkExpandKeys(sub, expanded);
    }
  }
  
  void refilterTable() {
    data.refilter();
    TreePath tp = struct.getSelectionPath();
    if (tp == null || tp.getPathCount() == 1) {
      tab.clearSelection();
      tab.repaint();
    } else {
      data.goToRow(struct.getRowForPath(tp)-1);
    }
  }
  
  public void close() {
    if (!isMaximized()) saveSettings();
    lnav.unregisterNavBarKeys(this);
    rnav.unregisterNavBarKeys(this);
    hide();
  }
  
  boolean isMaximized() {
    Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
    Window c = getWindow();
    return (c.getWidth() >= scr.width * 0.95 && c.getHeight() >= scr.height * 0.90);
  }
  
  void saveSettings() {
    Properties colpr = FileHandler.getProperties(ColumnsBean.PROPERTIESFILE);

    Point loc = getWindow().getLocation();
    if (loc.x < 0) loc.x = 0;
    if (loc.y < 0) loc.y = 0;
    
    VarStr ret = new VarStr();
    ret.append(split.getDividerLocation()).append(':').append(split.getWidth()).append('|').append(split.getHeight());
    ret.append('@').append(loc.x).append('|').append(loc.y);
    
    colpr.setProperty(getMainSaveTag(), ret.toString());

    FileHandler.storeProperties(ColumnsBean.PROPERTIESFILE,colpr);
  }
  
  void loadSettings() {
    String sett = FileHandler.getProperties(ColumnsBean.PROPERTIESFILE).getProperty(getMainSaveTag());
    if (sett == null || sett.length() == 0) return;
    
    int p = sett.indexOf('@');
    if (p < 0) return;
    Point loc = new Point(
        Aus.getAnyNumber(sett.substring(p + 1)),
        Aus.getAnyNumber(sett.substring(sett.indexOf('|', p) + 1)));
    int div = Aus.getAnyNumber(sett);
    Dimension dim = new Dimension(
        Aus.getAnyNumber(sett.substring(sett.indexOf(':') + 1)),
        Aus.getAnyNumber(sett.substring(sett.indexOf('|') + 1)));
    split.setPreferredSize(dim);
    split.setDividerLocation(div);
    this.setLocation(loc);
  }
  
  String getMainSaveTag() {
    return "ColB-" + getClass().getName() + "-main";
  }
  
  public void show() {
    updateAll(null);
    if (lazyinit) {
      lazyinit = false;
      loadSettings();
      pack();
    }
    akonta.clear();
    anakonta.clear();
    DataSet k = dM.getDataModule().getKonta();
    k.open();
    for (k.first(); k.inBounds(); k.next()) {
      akonta.add(k.getString("BROJKONTA"));
      if (k.getString("VRSTAKONTA").equals("A"))
        anakonta.add(k.getString("BROJKONTA"));
    }
    
    generateKonta();
    
    lnav.registerNavBarKeys(this);
    rnav.registerNavBarKeys(this);
    super.show();
  }
  
  
  void addNode() {
    Report rep = (Report) reports.getSelectedValue();
    if (rep == null) return;
    
    DefaultTreeModel model = (DefaultTreeModel) struct.getModel();
    DefaultMutableTreeNode target = (DefaultMutableTreeNode) struct.getModel().getRoot();
    
    TreePath tp = struct.getSelectionPath();
    if (tp != null) target = (DefaultMutableTreeNode) tp.getLastPathComponent();
    else tp = new TreePath(target);
    
    poz.empty();
    poz.insertRow(false);
    poz.setString("NEG", "D");
    poz.setString("BOLD", "N");
    if (new raInputDialog() {
      protected boolean checkOk() {
        return checkXLS();
      }
    }.show(getWindow(), ppan, "Nova pozicija") && poz.getString("NAZIV").length() > 0) {
      try {
        QueryDataSet ds = Gkrep.getDataModule().openEmptySet();
        String max = Aus.q("SELECT MAX(cpoz) AS cpoz FROM gkrep").getString("CPOZ");
        max = Aus.leadzero(Aus.getAnyNumber(max) + 1, 6);
        int kmax = Gkrep.getDataModule().getMax("CKEY") + 1;
        ds.insertRow(false);
        ds.setInt("CKEY", kmax);
        ds.setString("CPOZ", max);
        ds.setString("CPRIP", ((Node) target.getUserObject()).key);
        ds.setInt("NIVO", tp == null ? 1 : tp.getPathCount());
        ds.setString("NAZIV", poz.getString("NAZIV"));
        ds.setString("AOP", poz.getString("AOP"));
        ds.setString("KALK", poz.getString("KALK"));
        ds.setString("NEG", poz.getString("NEG"));
        ds.setString("BOLD", poz.getString("BOLD"));
        ds.setString("XLSPOZ", poz.getString("XLSPOZ"));
        ds.saveChanges();
        
        DefaultMutableTreeNode node = new Node(ds).wrap();
        model.insertNodeInto(node, target, target.getChildCount());
        struct.expandPath(tp.pathByAddingChild(node));        
        addRowData(tp.pathByAddingChild(node), node);
        updateActions();
      } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(getWindow(), "Neuspješno dodavanje pozicije!", "Greška", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
  
  /*void addRowData(TreePath tp, DefaultMutableTreeNode tn) {
    Node node = (Node) tn.getUserObject();
    vpoz.add(node.key);
    try {
      int row = data.getRow();
      tab.stopFire();
      data.enableDataSetEvents(false);
      
      int nr = struct.getRowForPath(tp) - 1;
      System.out.println("dodajem " + nr + " ukupno " + data.getRowCount() + " bio na " + row);
      data.moveRow(i1)
      if (nr >= data.getRowCount()) {
        data.goToRow(data.getRowCount() - 1);
        data.insertRow(false);
      } else {
        data.goToRow(nr);
        System.out.println("sad sam na " + data.getRow());
        data.insertRow(true);
        System.out.println("a sad " + data.getRow());
      }
      data.setString("CPOZ", node.key);
      data.setString("AOP", node.aop);
      data.setString("KALK", node.kalk);
      data.post();
      data.goToRow(row);
    } finally {
      tab.startFire();
      data.enableDataSetEvents(true);
    }
    refilterTable();
  }*/
  
  void addRowData(TreePath tp, DefaultMutableTreeNode tn) {
    Node node = (Node) tn.getUserObject();
    vpoz.add(node.ik);
    try {
      //int row = data.getRow();
      tab.stopFire();
      data.enableDataSetEvents(false);
      data.removeRowFilterListener(expandState);
      data.empty();
      
      DefaultMutableTreeNode root = (DefaultMutableTreeNode) struct.getModel().getRoot();
      fillData(root);
    } finally {
      tab.startFire();
      data.enableDataSetEvents(true);
      try {
        data.addRowFilterListener(expandState);
      } catch (Exception e) {
        //
      }
    }
    refilterTable();
  }
  
  void fillData(DefaultMutableTreeNode tn) {
    for (int i = 0; i < tn.getChildCount(); i++) {
      DefaultMutableTreeNode ch = (DefaultMutableTreeNode) tn.getChildAt(i);
      Node n = (Node) ch.getUserObject();
      data.insertRow(false);
      data.setInt("CKEY", n.ik.intValue());
      data.setString("AOP", n.aop);
      data.setString(n.isKonto() ? "KKALK" : "PKALK", n.kalk);
      data.setString("KONTO", n.isKonto() ? "D" : "N");
      data.setString("XLSPOZ", n.xls);
      data.post();
      fillData(ch);
    }
  }
  
  void updateRowData(Node node) {
    if (data.getInt("CKEY") != node.ik.intValue()) {
      System.out.println("WEIRD BUG!!!");
      return;
    }
    
    data.setString("AOP", node.aop);
    data.setString(node.isKonto() ? "KKALK" : "PKALK", node.kalk);
    data.setString("XLSPOZ", node.xls);
    data.post();
    refilterTable();
  }
  
  void deleteRowData(TreePath tp) {
    try {
      tab.stopFire();
      data.enableDataSetEvents(false);
      data.removeRowFilterListener(expandState);
      data.refilter();
      
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
      removeData(node);
      
    } finally {
      tab.startFire();
      data.enableDataSetEvents(true);
      try {
        data.addRowFilterListener(expandState);
      } catch (Exception e) {
        //
      }
    }
  }
  
  void removeData(DefaultMutableTreeNode tn) {
     Node n = (Node) tn.getUserObject();
     if (lookupData.getlookupData().raLocate(data, "CKEY", n.ik.toString()))
       data.emptyRow();
     vpoz.remove(n.ik);
     for (int i = 0; i < tn.getChildCount(); i++)
       removeData((DefaultMutableTreeNode) tn.getChildAt(i));
  }
  
  void addKonto() {
    Report rep = (Report) reports.getSelectedValue();
    if (rep == null) return;
    
    DefaultTreeModel model = (DefaultTreeModel) struct.getModel();
    
    TreePath tp = struct.getSelectionPath();
    if (tp == null) return;
    
    DefaultMutableTreeNode target = (DefaultMutableTreeNode) tp.getLastPathComponent();
    Node n = (Node) target.getUserObject();
    
    if (n.isPoz()) {
      kon.empty();
      kon.insertRow(false);
      kon.setString("PS", "D");
      rcb.setSelectedIndex(0);
      if (new raInputDialog() {
        /*protected void beforeShow() {
          kpan.getCheck("PS").setSelected(true);
        }*/
        protected boolean checkOk() {
          rcb.this_itemStateChanged();
          if (Valid.getValid().isEmpty(kpan.getText("RASPON"))) return false;
          if (!checkValid(kon.getString("RASPON"))) return false;
          return true;
        }
      }.show(getWindow(), kpan, "Novi raspon konta") && kon.getString("RASPON").length() > 0) {
        try {
          QueryDataSet ds = Gkrep.getDataModule().openEmptySet();
          String max = Aus.q("SELECT MAX(cpoz) AS cpoz FROM gkrep").getString("CPOZ");
          max = Aus.leadzero(Aus.getAnyNumber(max) + 1, 6);
          int kmax = Gkrep.getDataModule().getMax("CKEY") + 1;
          ds.insertRow(false);
          ds.setInt("CKEY", kmax);
          ds.setString("CPOZ", max);
          ds.setString("CPRIP", n.key);
          ds.setInt("NIVO", tp.getPathCount());
          ds.setString("RASPON", kon.getString("RASPON"));
          ds.setString("KALK", kon.getString("KALK"));
          ds.setString("PS", kon.getString("PS"));
          ds.saveChanges();
          
          DefaultMutableTreeNode node = new Node(ds).wrap();
          model.insertNodeInto(node, target, target.getChildCount());
          struct.expandPath(tp.pathByAddingChild(node));  
          addRowData(tp.pathByAddingChild(node), node);
          updateActions();
          updateKontaDef(max, kon.getString("RASPON"));
        } catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(getWindow(), "Neuspješno dodavanje raspona konta!", "Greška", JOptionPane.ERROR_MESSAGE);
        }
      }
    }
  }
  
  boolean checkValid(String raspon) {
    String[] parts = new VarStr(raspon).splitTrimmed(',');
    
    for (int i = 0; i < parts.length; i++) {
      String[] range = new VarStr(parts[i]).splitTrimmed('-');
      if (range.length == 1) {
        if (!akonta.contains(range[0])) {
          JOptionPane.showMessageDialog(kpan.getText("RASPON"), 
              "Nepostojeæi konto (" + range[0] + ")!", "Greška", JOptionPane.ERROR_MESSAGE);
          return false;
        }
      } else if (range.length > 2) {
        JOptionPane.showMessageDialog(kpan.getText("RASPON"), 
            "Neispravan raspon (" + parts[i] + ")!", "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    return true;
  }
  
  void updateNode() {
    Report rep = (Report) reports.getSelectedValue();
    if (rep == null) return;
    
    DefaultTreeModel model = (DefaultTreeModel) struct.getModel();
        
    TreePath tp = struct.getSelectionPath();
    if (tp == null || tp.getPathCount() == 1) return;
    
    DefaultMutableTreeNode target = (DefaultMutableTreeNode) tp.getLastPathComponent();
    Node n = (Node) target.getUserObject();
    if (n.isPoz()) {
      poz.empty();
      poz.insertRow(false);
      poz.setString("NAZIV", n.naziv);
      poz.setString("AOP", n.aop);
      poz.setString("KALK", n.kalk);
      poz.setString("NEG", n.neg);
      poz.setString("BOLD", n.bold);
      poz.setString("XLSPOZ", n.xls);
      if (new raInputDialog() {
        protected boolean checkOk() {
          return checkXLS();
        }
      }.show(getWindow(), ppan, "Izmjena pozicija") && poz.getString("NAZIV").length() > 0) {
        try {
          QueryDataSet ds = Gkrep.getDataModule().openTempSet(Condition.equal("CKEY", n.ik.intValue()));
          ds.setString("NAZIV", n.naziv = poz.getString("NAZIV"));
          ds.setString("AOP", n.aop = poz.getString("AOP"));
          ds.setString("KALK", n.kalk = poz.getString("KALK"));
          ds.setString("NEG", n.neg = poz.getString("NEG"));
          ds.setString("BOLD", n.bold = poz.getString("BOLD"));
          ds.setString("XLSPOZ", n.xls = poz.getString("XLSPOZ"));
          ds.saveChanges();
          updateRowData(n);
          model.nodeChanged(target);
        } catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(getWindow(), "Neuspješna izmjena pozicije!", "Greška", JOptionPane.ERROR_MESSAGE);
        }
      }
    } else {
      kon.empty();
      kon.insertRow(false);
      kon.setString("RASPON", n.raspon);
      kon.setString("KALK", n.kalk);
      kon.setString("PS", n.ps);
      rcb.findCombo();
      if (new raInputDialog() {
        /*protected void beforeShow() {
          boolean isSel = kon.getString("PS").equals("D");
          kpan.getCheck("PS").setSelected(isSel);
        }*/
        protected boolean checkOk() {
          rcb.this_itemStateChanged();
          if (Valid.getValid().isEmpty(kpan.getText("RASPON"))) return false;
          if (!checkValid(kon.getString("RASPON"))) return false;
          return true;
        }
      }.show(getWindow(), kpan, "Izmjena raspona konta")) {
        try {
          QueryDataSet ds = Gkrep.getDataModule().openTempSet(Condition.equal("CKEY", n.ik.intValue()));
          ds.setString("RASPON", n.raspon = kon.getString("RASPON"));
          ds.setString("KALK", n.kalk = kon.getString("KALK"));
          ds.setString("PS", n.ps = kon.getString("PS"));
          ds.saveChanges();
          updateRowData(n);
          model.nodeChanged(target);
          updateKontaDef(ds.getString("CPOZ"), n.raspon);
        } catch (Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(getWindow(), "Neuspješna izmjena raspona konta!", "Greška", JOptionPane.ERROR_MESSAGE);
        }
      }
    }
  }
  
  void generateKonta() {
    raProcess.runChild(new Runnable() {
      public void run() {
        generateImpl();
      }
    });
  }
  
  void generateImpl() {
    QueryDataSet ds = Gkrepkonta.getDataModule().openTempSet();
    
    HashMap hpoz = new HashMap();
    for (ds.first(); ds.inBounds(); ds.next()) {
      HashSet kont = (HashSet) hpoz.get(ds.getString("CPOZ"));
      if (kont == null) hpoz.put(ds.getString("CPOZ"), kont = new HashSet());
      kont.add(ds.getString("BROJKONTA"));
    }
    
    ds = Gkrep.getDataModule().openTempSet(Condition.anyString("RASPON"));
    for (ds.first(); ds.inBounds(); ds.next()) {
      HashSet kont = (HashSet) hpoz.get(ds.getString("CPOZ"));
      if (kont == null) kont = new HashSet();
      updateKontaDef(ds.getString("CPOZ"), ds.getString("RASPON"), kont);
      hpoz.remove(ds.getString("CPOZ"));
    }
    
    for (Iterator i = hpoz.keySet().iterator(); i.hasNext(); ) {
      String cpoz = (String) i.next();
      HashSet kont = (HashSet) hpoz.get(cpoz);
      if (kont != null && kont.size() > 0)    
        Valid.getValid().runSQL("DELETE FROM gkrepkonta WHERE " + Condition.equal("CPOZ", cpoz).and(Condition.in("BROJKONTA", kont)));
    }
  }
  
  public void updateKontaDef(String cpoz, String raspon) {
    QueryDataSet ds = Gkrepkonta.getDataModule().openTempSet(Condition.equal("CPOZ", cpoz));
    HashSet exist = new HashSet();
    for (ds.first(); ds.inBounds(); ds.next())
      exist.add(ds.getString("BROJKONTA"));
    
    updateKontaDef(cpoz, raspon, exist);
  }
  
  
  public void updateKontaDef(String cpoz, String raspon, HashSet exist) {
    
    System.out.println("Existing " + exist);
    
    HashSet single = new HashSet();
    ArrayList from = new ArrayList();
    ArrayList to = new ArrayList();
    String[] parts = new VarStr(raspon).splitTrimmed(',');
    for (int i = 0; i < parts.length; i++) {
      String[] range = new VarStr(parts[i]).splitTrimmed('-');
      if (range.length > 1) {
        from.add(range[0]);
        to.add(range[1]);
      } else if (range[0].length() <= 3) {
        from.add(range[0]);
        to.add(range[0]);
      } else single.add(parts[i]); 
    }

    HashSet rasp = new HashSet();
    for (Iterator ia = anakonta.iterator(); ia.hasNext(); ) {
      String konto = (String) ia.next();
      if (single.contains(konto)) rasp.add(konto);
      else for (int i = 0; i < from.size(); i++) 
        if (konto.compareTo((String) from.get(i)) >= 0 &&
            within(konto, (String) to.get(i))) {
          rasp.add(konto);
          break;
        }
    }    
    if (exist.containsAll(rasp) && rasp.containsAll(exist)) return;
    
    HashSet ex = new HashSet(exist);
    ex.removeAll(rasp);
    rasp.removeAll(exist);
    
    System.out.println("To remove " + ex);
    System.out.println("To add " + rasp);
    
    Valid.getValid().runSQL("DELETE FROM gkrepkonta WHERE " + Condition.equal("CPOZ", cpoz).and(Condition.in("BROJKONTA", ex)));

    QueryDataSet ds = Gkrepkonta.getDataModule().openEmptySet();
    
    int kmax = Gkrepkonta.getDataModule().getMax("CKEY") + 1;
    for (Iterator i = rasp.iterator(); i.hasNext(); ) {
      String konto = (String) i.next();
      ds.insertRow(false);
      ds.setInt("CKEY", kmax++);
      ds.setString("CPOZ", cpoz);
      ds.setString("BROJKONTA", konto);
      ds.post();
    }
    
    ds.saveChanges();    
  }
  
  private boolean within(String konto, String endk) {
    if (konto.length() < endk.length()) 
      return konto.compareTo(endk) <= 0;
    return konto.substring(0, endk.length()).compareTo(endk) <= 0;
  }
  
  boolean checkXLS() {
    String def = poz.getString("XLSPOZ").trim();
    if (def.length() == 0) return true;
    
    int colon = def.indexOf(':');
    if (colon == 0) {
      JOptionPane.showMessageDialog(ppan.getText("XLSPOZ"), "Neispravan broj plahte za XLS!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    } else if (colon > 0) {
      if (!Aus.isDigit(def.substring(0, colon))) {
        JOptionPane.showMessageDialog(ppan.getText("XLSPOZ"), "Neispravan broj plahte za XLS!", "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      def = def.substring(colon + 1);
    }
    
    boolean alpha = true;    
    for (int i = 0; i < def.length(); i++) {
      char c = def.charAt(i);
      if ((alpha && (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z')) ||
          (i > 0 && c >= '0' && c <= '9')) {
        if (c >= '0' && c <= '9' && alpha)  
          alpha = false;
      } else {
        JOptionPane.showMessageDialog(ppan.getText("XLSPOZ"), "Neispravano definirana æelija za za XLS (pogrešan znak)!", 
            "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    if (alpha) {
      JOptionPane.showMessageDialog(ppan.getText("XLSPOZ"), "Neispravano definirana æelija za za XLS (nedostaje redak)!", 
          "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    return true;
  }
  
  void deleteNode() {
    Report rep = (Report) reports.getSelectedValue();
    if (rep == null) return;
    
    TreePath tp = struct.getSelectionPath();
    if (tp == null) return;
    
    if (tp.getPathCount() <= 1) {
      if (JOptionPane.showConfirmDialog(getWindow(), "Želite li obrisati sve pozicije iz izvještaja?",  
          "Poništavanje izvještaja", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
      
      try {
        QueryDataSet all = Gkrep.getDataModule().openTempSet();
        DataTree dt = DataTree.getSubTree(rep.key, all, "CPOZ", "CPRIP");
        HashSet keys = new HashSet();
        dt.fill(keys);
        keys.remove(rep.key);
        Assert.is(Valid.getValid().runSQL("DELETE FROM gkrep WHERE " + Condition.in("CPOZ", keys)));
        updateTree(); 
      } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(getWindow(), "Neuspješno brisanje izvještaja!", "Greška", JOptionPane.ERROR_MESSAGE);
      }
      return;
    }
    
    DefaultMutableTreeNode sel = (DefaultMutableTreeNode) tp.getLastPathComponent();
    Node n = (Node) sel.getUserObject();
        
    if (JOptionPane.showConfirmDialog(getWindow(), "Želite li obrisati " + (n.isPoz() ? "poziciju" :"raspon konta") + 
        " \"" + sel.getUserObject() + "\"?", "Brisanje " + (n.isPoz() ? "pozicije" : "raspona konta"), 
            JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
    
    try {
      /*QueryDataSet all = Gkrep.getDataModule().openTempSet();
      Condition cond = Aus.getDataTreeList(((Node) sel.getUserObject()).key, all, "CPOZ", "CPRIP");*/
      HashSet pozs = fillPoz(new HashSet(), sel);
      Assert.is(Valid.getValid().runSQL("DELETE FROM gkrep WHERE " + Condition.in("CKEY", pozs)));
      DefaultTreeModel model = (DefaultTreeModel) struct.getModel();
      deleteRowData(tp);
      model.removeNodeFromParent((DefaultMutableTreeNode) tp.getLastPathComponent());
      refilterTable();
      updateActions();
      Valid.getValid().runSQL("DELETE FROM gkrepkonta WHERE " + Condition.equal("CPOZ", n.key));
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(getWindow(), "Neuspješno brisanje izvještaja!", "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  boolean moveNode(DefaultMutableTreeNode from, DefaultMutableTreeNode to, int idx) {
    System.out.println("moving node");
    try {
      ArrayList chg = new ArrayList();
      Node fn = (Node) from.getUserObject();
      Node tn = (Node) to.getUserObject();
      DefaultMutableTreeNode pfrom = (DefaultMutableTreeNode) from.getParent();
      int fi = pfrom.getIndex(from);
      
      // reordering unutar èvora; jednostavnije
      if (tn.equals(pfrom.getUserObject())) {
        if (idx < 0) idx = to.getChildCount();
        if (fi > idx) {
          Node n = getChild(to, idx);
          String free = fn.key;
          fn.key = n.key;
          addChange(chg, from);
          for (int i = fi - 1; i >= idx; i--) {
            n = getChild(to, i);
            String okey = n.key;
            n.key = free;
            addChange(chg, to.getChildAt(i));
            free = okey;
          }
        } else {
          Node n = getChild(to, idx - 1);
          String free = fn.key;
          fn.key = n.key;
          addChange(chg, from);
          for (int i = fi + 1; i <= idx - 1; i++) {
            n = getChild(to, i);
            String okey = n.key;
            n.key = free;
            addChange(chg, to.getChildAt(i));
            free = okey;
          }
          idx--;
        }
      } else {
        String max = Aus.q("SELECT MAX(cpoz) AS cpoz FROM gkrep").getString("CPOZ");
        max = Aus.leadzero(Aus.getAnyNumber(max) + 1, 6);
        if (idx >= 0 && idx < to.getChildCount()) {
          Node n = getChild(to, idx);
          fn.key = n.key;
          fn.prip = tn.key;
          addChange(chg, from);
          for (int i = idx + 1; i < to.getChildCount(); i++) {
            Node next = getChild(to, i);
            n.key = next.key;
            addChange(chg, to.getChildAt(i - 1));
            n = next;
          }
          n.key = max;
          addChange(chg, to.getLastChild());
        } else {
          idx = to.getChildCount();
          fn.key = max;
          fn.prip = tn.key;
          addChange(chg, from);
        }        
      }
      HashSet keys = new HashSet();
      for (int i = 0; i < chg.size(); i++) keys.add(((Node) chg.get(i)).ik);
      QueryDataSet all = Gkrep.getDataModule().openTempSet(Condition.in("CKEY", keys));
      for (all.first(); all.inBounds(); all.next()) {
        Node n = (Node) chg.get(chg.indexOf(new Node(all)));
        all.setString("CPOZ", n.key);
        all.setString("CPRIP", n.prip);
      }
      all.saveChanges();
      DefaultTreeModel model = (DefaultTreeModel) struct.getModel();
      model.removeNodeFromParent(from);
      model.insertNodeInto(from, to, idx);
      struct.expandPath(new TreePath(from.getPath()));
      addRowData(null, from);

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
  
  void addChange(ArrayList chg, TreeNode tn) {
    Node n = getNode(tn);
    chg.add(n);
    for (int i = 0; i < tn.getChildCount(); i++) {
      Node c = getChild(tn, i);
      c.prip = n.key;
      chg.add(c);
    }
  }
  
  void expandPath() {
    Report rep = (Report) reports.getSelectedValue();
    if (rep == null) return;
    
    TreePath tp = struct.getSelectionPath();
    if (tp != null) expand(tp);
  }
  
  void expand(TreePath tp) {
    struct.expandPath(tp);
    DefaultMutableTreeNode target = (DefaultMutableTreeNode) tp.getLastPathComponent();
    for (int i = 0; i < target.getChildCount(); i++)
      expand(tp.pathByAddingChild(target.getChildAt(i)));
  }
  
  void findTreeNode() {
    Integer key = new Integer(data.getInt("CKEY"));
    DefaultMutableTreeNode root = (DefaultMutableTreeNode) struct.getModel().getRoot();
    findKey(root, key);
  }
  
  HashSet fillPoz(HashSet pozs, DefaultMutableTreeNode tn) {
    pozs.add(((Node) tn.getUserObject()).ik);
    for (int i = 0; i < tn.getChildCount(); i++)
      fillPoz(pozs, (DefaultMutableTreeNode) tn.getChildAt(i));
    return pozs;
  }
  
  boolean findKey(DefaultMutableTreeNode tn, Integer key) {
    Node n = (Node) tn.getUserObject();
    if (n.ik.equals(key)) {
      struct.setSelectionPath(new TreePath(tn.getPath()));
      return true;
    }
    for (int i = 0; i < tn.getChildCount(); i++)
      if (findKey((DefaultMutableTreeNode) tn.getChildAt(i), key)) return true;
    return false;
  }
  
  void checkDouble(MouseEvent e) {
    if (!e.isConsumed() && e.getClickCount() == 2) {
      e.consume();
      Report rep = (Report) reports.getSelectedValue();
      if (rep == null) return;
      
      DefaultTreeModel model = (DefaultTreeModel) struct.getModel();
          
      TreePath tp = struct.getSelectionPath();
      if (tp == null) return;
      if (!struct.getPathBounds(tp).contains(e.getPoint())) return;
      
      if (tp.getPathCount() == 1) {
        if (struct.isExpanded(tp)) struct.collapsePath(tp);
        else struct.expandPath(tp);
      } else updateNode();
    }
  }
  
  void checkTable2Click() {
    Report rep = (Report) reports.getSelectedValue();
    if (rep == null) return;
    
    DefaultTreeModel model = (DefaultTreeModel) struct.getModel();
        
    TreePath tp = struct.getSelectionPath();
    if (tp == null) return;
    
    if (tp.getPathCount() > 1) updateNode();
  }
  
  void checkPopup(MouseEvent e) {
    if (e.isConsumed() || !e.isPopupTrigger() || e.getSource() != struct) return;
    
    int row = struct.getClosestRowForLocation(e.getX(), e.getY());
    if (!struct.getRowBounds(row).contains(e.getX(), e.getY())) return;
    
    struct.setSelectionRow(row);
    
    TreePath tp = struct.getSelectionPath();
    DefaultMutableTreeNode dt = (DefaultMutableTreeNode) tp.getLastPathComponent();
    Node n = (Node) dt.getUserObject();
    
    boolean hasPoz = false, hasKon = false;
    for (int i = 0; i < dt.getChildCount(); i++) {
      Node c = getChild(dt, i);
      if (c.isPoz()) hasPoz = true;
      else hasKon = true;
    }
    
    JPopupMenu pop = new JPopupMenu();
    if (n.key.equals(n.prip)) {
      aAddPoz.putValue(Action.NAME, "Dodaj poziciju u izvještaj");
      aDeletePoz.putValue(Action.NAME, "Obriši sve pozicije iz izvještaja");
      aExpand.putValue(Action.NAME, "Pokaži sve pozicije u izvještaju");
      pop.add(aAddPoz);
      pop.add(aDeletePoz);
      pop.addSeparator();
      pop.add(aExpand);
    } else if (n.isPoz()) {
      aAddPoz.putValue(Action.NAME, "Dodaj poziciju unutar pozicije");
      aUpdatePoz.putValue(Action.NAME, "Izmijeni postavke pozicije");
      aAddKonto.putValue(Action.NAME, "Dodaj raspon konta unutar pozicije");
      aDeletePoz.putValue(Action.NAME, "Obriši poziciju i sve unutar nje");
      if (!hasKon) pop.add(aAddPoz);
      pop.add(aUpdatePoz);
      if (!hasPoz) pop.add(aAddKonto);
      pop.add(aDeletePoz);
    } else {
      aUpdatePoz.putValue(Action.NAME, "Izmijeni raspon konta");
      aDeletePoz.putValue(Action.NAME, "Obriši raspon konta");
      pop.add(aUpdatePoz);
      pop.add(aDeletePoz);
    }
    
    pop.show(struct, e.getX(), e.getY());
  }
  
  public class ColorModifier extends raTableModifier {
    Color colorK = Color.green.darker();
    Color colorA = Color.blue.darker();
    private Variant v = new Variant();
    Column vcol;
    HashMap vals = new HashMap();
    
    
    public ColorModifier() {
      for (int i = 0; i < items.length; i++)
        vals.put(items[i][1], items[i][0]);
    }

    public boolean doModify() {
      return true;
    }
    public void modify() {
      vcol = ((JraTable2) getTable()).getDataSetColumn(getColumn());
      tab.getDataSet().getVariant("KONTO", getRow(), v);
      boolean konto = v.getString().equals("D");
      if (konto && vcol.getColumnName().equalsIgnoreCase("KKALK")) 
        ((JLabel) renderComponent).setText((String) vals.get(((JLabel) renderComponent).getText()));
      
      ((JLabel) renderComponent).setHorizontalAlignment(SwingConstants.CENTER);
      
      if (isSelected()) return;
      
      if (vcol.getColumnName().equalsIgnoreCase("AOP"))
        renderComponent.setForeground(colorA);
      else if (vcol.getColumnName().equalsIgnoreCase("KKALK") && konto)
        renderComponent.setForeground(colorK);
    }  
  }

    
  /*class MultiCellRenderer implements TreeCellRenderer {
    JLabel naziv = new JLabel(" ");
    JLabel aop = new JLabel(" ");
    JPanel pozRenderer = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
    
    JLabel raspon = new JLabel(" ");
    JLabel def = new JLabel(" ");
    JPanel konRenderer = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        
    DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
    Color aopNonSel;
    Color defNonSel;

    public MultiCellRenderer() {
      naziv.setFont(defaultRenderer.getFont());
      pozRenderer.add(naziv);
      pozRenderer.add(Box.createHorizontalStrut(10));
      aop.setForeground(aopNonSel = Color.BLUE.darker());
      aop.setFont(defaultRenderer.getFont());
      pozRenderer.add(aop);
      
      raspon.setFont(defaultRenderer.getFont());
      konRenderer.add(raspon);
      konRenderer.add(Box.createHorizontalStrut(10));
      def.setForeground(defNonSel = Color.GREEN.darker());
      def.setFont(defaultRenderer.getFont());
      konRenderer.add(def);
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
        boolean expanded, boolean leaf, int row, boolean hasFocus) {

      if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
        Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
        if (userObject instanceof Node) {
          Node n = (Node) userObject;
          if (n.key.equals(n.prip)) return defaultRenderer.
              getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                   
          if (n.raspon == null || n.raspon.length() == 0) {
            naziv.setText(n.naziv);
            naziv.setIcon(expanded ? defaultRenderer.getOpenIcon() : defaultRenderer.getClosedIcon());
            aop.setText(n.aop);
            if (selected) {
              pozRenderer.setBackground(defaultRenderer.getBackgroundSelectionColor());
              naziv.setForeground(defaultRenderer.getTextSelectionColor());
              aop.setForeground(defaultRenderer.getTextSelectionColor());
            } else {
              pozRenderer.setBackground(defaultRenderer.getBackgroundNonSelectionColor());
              naziv.setForeground(defaultRenderer.getTextNonSelectionColor());
              aop.setForeground(aopNonSel);
            }
            return pozRenderer;
          }

          raspon.setText(n.raspon);
          raspon.setIcon(defaultRenderer.getLeafIcon());
          def.setText(n.kalk);
          if (selected) {
            konRenderer.setBackground(defaultRenderer.getBackgroundSelectionColor());
            raspon.setForeground(defaultRenderer.getTextSelectionColor());
            def.setForeground(defaultRenderer.getTextSelectionColor());
          } else {
            konRenderer.setBackground(defaultRenderer.getBackgroundNonSelectionColor());
            raspon.setForeground(defaultRenderer.getTextNonSelectionColor());
            def.setForeground(defNonSel);
          }
          return konRenderer;
        }
      }
      return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
    }
  }*/
  
  class ListTransferHandler extends TransferHandler {
    
  }
  
  class TreeTransferHandler extends TransferHandler {
    DataFlavor nodesFlavor;
    DataFlavor[] flavors = new DataFlavor[1];
    DefaultMutableTreeNode nodeToRemove;
  
    public TreeTransferHandler() {
        try {
            String mimeType = DataFlavor.javaJVMLocalObjectMimeType +
                              ";class=\"" +
                javax.swing.tree.DefaultMutableTreeNode[].class.getName() +
                              "\"";
            nodesFlavor = new DataFlavor(mimeType);
            flavors[0] = nodesFlavor;
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }
  
    public boolean canImport(TransferHandler.TransferSupport support) {

        if (!support.isDrop()) return false;
        
        if(!support.isDataFlavorSupported(nodesFlavor)) return false;

        JTree tree = (JTree)support.getComponent();        
        TreePath selp = tree.getSelectionPath();
        if (selp == null) return false;
        
        DefaultMutableTreeNode seln = (DefaultMutableTreeNode) selp.getLastPathComponent();
        Node snode = (Node) seln.getUserObject();
        
        // ne može micati sam izvještaj
        if (tree.getSelectionPath().getPathCount() == 1) return false;
        
        // pronaði sve relevantne nodove i lokacije
        JTree.DropLocation dl = (JTree.DropLocation)support.getDropLocation();
        DefaultMutableTreeNode dropn = (DefaultMutableTreeNode) dl.getPath().getLastPathComponent();
        Node dnode = (Node) dropn.getUserObject();
        
        // pozicija selektiranog noda kod parenta
        int selIdx = seln.getParent().getIndex(seln);
        int chIdx = dl.getChildIndex();
        if (chIdx < 0) chIdx = dropn.getChildCount();
        
        // naði vrstu noda destinacije        
        boolean hasPoz = false, hasKon = false;
        for (int i = 0; i < dropn.getChildCount(); i++) {
          Node c = getChild(dropn, i);
          if (c.isPoz()) hasPoz = true;
          else hasKon = true;
        }
        
        //parent mora biti pozicija
        if (!dnode.isPoz()) return false;
        
        // mièe li se išta
        if (dnode.equals(getNode(seln.getParent())) && (selIdx == chIdx || selIdx == chIdx - 1)) return false;

        // zabrani miješanje pozicija
        if (snode.isKonto() && hasPoz || snode.isPoz() && hasKon) return false;
        
        // ne može kopirati poziciju unutar sebe same
        if (snode.isPoz()) while (dropn != null) {
          if (snode.equals(dropn.getUserObject())) return false;
          dropn = (DefaultMutableTreeNode) dropn.getParent();
        }

        return true;
    }
  
    protected Transferable createTransferable(JComponent c) {
        JTree tree = (JTree)c;
        TreePath path = tree.getSelectionPath();
        return new NodesTransferable((DefaultMutableTreeNode) path.getLastPathComponent());
    }
 
  
    protected void exportDone(JComponent source, Transferable datas, int action) {
      /*
        if((action & MOVE) == MOVE) {
            JTree tree = (JTree)source;
            DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
            model.removeNodeFromParent(nodeToRemove);
        }*/
    }
  
    public int getSourceActions(JComponent c) {
        return MOVE;
    }
  
    public boolean importData(TransferHandler.TransferSupport support) {
        if(!canImport(support)) {
            return false;
        }
        // Extract transfer data.
        DefaultMutableTreeNode node = null;
        try {
            Transferable t = support.getTransferable();
            node = (DefaultMutableTreeNode) t.getTransferData(nodesFlavor);
        } catch(UnsupportedFlavorException ufe) {
            System.out.println("UnsupportedFlavor: " + ufe.getMessage());
        } catch(java.io.IOException ioe) {
            System.out.println("I/O error: " + ioe.getMessage());
        }
        
        // Get drop location info.
        JTree.DropLocation dl = (JTree.DropLocation)support.getDropLocation();
        int childIndex = dl.getChildIndex();
        TreePath dest = dl.getPath();
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode)dest.getLastPathComponent();
        //JTree tree = (JTree) support.getComponent();
        // Configure for drop mode.
        //if (childIndex == -1) childIndex = parent.getChildCount();

        return moveNode(node, parent, childIndex);
    }
  
    public String toString() {
        return getClass().getName();
    }
  
    public class NodesTransferable implements Transferable {
        DefaultMutableTreeNode node;
  
        public NodesTransferable(DefaultMutableTreeNode node) {
            this.node = node;
         }
  
        public Object getTransferData(DataFlavor flavor)
                                 throws UnsupportedFlavorException {
            if(!isDataFlavorSupported(flavor))
                throw new UnsupportedFlavorException(flavor);
            return node;
        }
  
        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }
  
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return nodesFlavor.equals(flavor);
        }
    }
  }
  
  static class Node {
    static DefaultTreeCellRenderer rend = new DefaultTreeCellRenderer();
    
    Integer ik;
    String key;
    String naziv;
    String prip;
    int nivo;
    String aop;
    String raspon;
    String kalk;
    String ps;
    String neg;
    String bold;
    String xls;
    
    public Node() {
      
    }
    
    public Node(DataSet ds) {
      ik = new Integer(ds.getInt("CKEY"));
      key = ds.getString("CPOZ");
      naziv = ds.getString("NAZIV");
      prip = ds.getString("CPRIP");
      nivo = ds.getInt("NIVO");
      aop = ds.getString("AOP");
      raspon = ds.getString("RASPON");
      kalk = ds.getString("KALK");
      ps = ds.getString("PS");
      neg = ds.getString("NEG");
      bold = ds.getString("BOLD");
      xls = ds.getString("XLSPOZ");
    }
    
    public DefaultMutableTreeNode wrap() {
      return new DefaultMutableTreeNode(this, isPoz());
    }

    public String toString() {
      return toHtml();
    }
    
    public boolean isPoz() {
      return !isKonto();
    }
    
    public boolean isKonto() {
      return raspon != null && raspon.length() > 0;
    }
    
    public String toHtml() {
      if (key.equals(prip)) return naziv;
      return isKonto() ? raspon : naziv;
      /*if (raspon == null || raspon.length() == 0) {
        if (aop == null || aop.length() == 0) return naziv;
        
        boolean dod = kalk != null && kalk.length() > 0;
        
        if (selected) return naziv + "   " + aop + (dod ? "   (" + kalk + ")" : "");
        return "<html>" + naziv + "&nbsp;&nbsp;&nbsp;<font color=\"blue\">" + aop + "</font>" +
        	    (dod ? "&nbsp;&nbsp;&nbsp;(" + kalk + ")" : "") + "</html>";
      }
      
      rend.setText(" ");
      int sw = rend.getPreferredSize().width;
      int wid = findMaxLength((DefaultMutableTreeNode) value)+25;
      int spaces = 0;
      rend.setText(raspon);
      int rw = rend.getPreferredSize().width;
      while (rw < wid - 5) {
        int sp = (wid - rw) / sw;
        if (sp == 0) sp = 1;
        spaces += sp;
        rend.setText(raspon + Aus.spc(spaces));
        rw = rend.getPreferredSize().width;
      }      
      if (selected) return raspon + Aus.spc(spaces) + kalk;
      
      VarStr spa = new VarStr();
      for (int i = 0; i < spaces; i++) spa.append("&nbsp;");      
      return "<html>" + raspon + spa + "<font color=\"green\">" + kalk + "</font></html>";*/
    }
    
    /*int findMaxLength(DefaultMutableTreeNode tn) {
      DefaultMutableTreeNode p = (DefaultMutableTreeNode) tn.getParent();
      int max = 0;
      for (int i = 0; i < p.getChildCount(); i++) {
        Node n = (Node) ((DefaultMutableTreeNode) p.getChildAt(i)).getUserObject();
        rend.setText(n.raspon);
        if (rend.getPreferredSize().width > max)
          max = rend.getPreferredSize().width;
      }
      return max;
    }*/
    
    public boolean equals(Object obj) {
      if (obj instanceof Node) return ik.intValue() == ((Node) obj).ik.intValue();
      return false;
    }
    
    public int hashCode() {
      return ik.intValue();
    }
  }
  
  static class Report {
    String key;
    String naziv;
    
    public Report(String key, String naziv) {
      this.key = key;
      this.naziv = naziv;
    }

    
    public String getKey() {
      return key;
    }

    
    public void setKey(String key) {
      this.key = key;
    }

    
    public String getNaziv() {
      return naziv;
    }

    
    public void setNaziv(String naziv) {
      this.naziv = naziv;
    }
    
    public String toString() {
      return naziv;
    }
    
    public boolean equals(Object obj) {
      if (obj instanceof Report)
        return key.equals(((Report) obj).key);
      return false;
    }
    
    public int hashCode() {
      return key.hashCode();
    }
  }
}

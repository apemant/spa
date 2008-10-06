/****license*****************************************************************
**   file: frmNormat.java
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
package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.baza.norme;
import hr.restart.baza.raDataSet;
import hr.restart.swing.JraKeyListener;
import hr.restart.swing.JraScrollPane;
import hr.restart.util.NavigationAdapter;
import hr.restart.util.lookupData;
import hr.restart.util.raFrame;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;
import hr.restart.util.raNavAction;
import hr.restart.util.startFrame;
import hr.restart.util.reports.JTablePrintRun;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

class Node {
  public String nazart;
  public int cart;
  public Node(int cart, String nazart) {
    this.nazart = nazart;
    this.cart = cart;
  }
  public String toString() {
    return nazart;
  }
}

public class frmNormat extends raFrame {
  static frmNormat fn;
  public raJPTableView jp = new raJPTableView() {
    public void mpTable_doubleClicked() {
      doubleClick();
    }
  };
  JTablePrintRun printer = new JTablePrintRun(getClass().getName());
  dM dm = dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
  raNavAction details, exit, tree;
  JTree t = new JTree();
  JraScrollPane tp = new JraScrollPane();
  JSplitPane sp = new JSplitPane();
  JPanel original;

  public frmNormat() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  QueryDataSet normarts = new raDataSet()/* {
    public void refreshed() {
      resync();
    }
  }*/;
  LinkedList stack = new LinkedList();
  int cartnor;

  public static boolean showArt(int cartnor) {
    if (fn == null) {
      fn = new frmNormat();
      startFrame.getStartFrame().centerFrame(fn, 0, "");
    }
    return fn.show(cartnor);
  }

  private void addSubtree(int cart, DefaultMutableTreeNode parent) {
    DataSet sn = dm.getSortedNorme();
    if (ld.raLocate(sn, "CARTNOR", String.valueOf(cart)))
      for (; sn.inBounds() && sn.getInt("CARTNOR") == cart; sn.next()) {
        DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(
          new Node(sn.getInt("CART"), sn.getString("NAZART")));
        parent.add(leaf);
        int currentRow = sn.getRow();
        addSubtree(sn.getInt("CART"), leaf);
        sn.goToRow(currentRow);
      }
  }

  private void createModel(int cartnor) {
    DefaultMutableTreeNode root = new DefaultMutableTreeNode(
        new Node(dm.getArtikli().getInt("CART"),dm.getArtikli().getString("NAZART")));
    addSubtree(cartnor, root);
    t.setModel(new DefaultTreeModel(root));
  }

  private boolean show(int cartnor) {
    if (!raVart.isNorma(cartnor)) return false;
    //if (!Aut.getAut().artTipa(cartnor, "P")) return false;
    this.setTitle("Sastav normativa "+cartnor+" - "+dm.getArtikli().getString("NAZART"));
    norme.getDataModule().setFilter(normarts, Condition.equal("cartnor", cartnor));
    enableCART();
    normarts.open();
    stack.clear();

    this.cartnor = cartnor;
    createModel(cartnor);
    checkActions();
//    jp.setVisibleCols(new int[] {Aut.getAut().getCARTdependable(1,2,3),4,5,6});
    this.show();
    return true;
  }

  private void setNormativ(int cartnor, int cart) {
    this.cartnor = cartnor;
    norme.getDataModule().setFilter(normarts, Condition.equal("cartnor", cartnor));
    normarts.open();
    ld.raLocate(dm.getArtikli(), "CART", Integer.toString(cartnor));
    checkActions();
    this.setTitle("Sastav normativa "+cartnor+" - "+dm.getArtikli().getString("NAZART"));
    if (cart != 0) ((AbstractTableModel) jp.getMpTable().getModel()).fireTableDataChanged();
    else jp.fireTableDataChanged();
    if (cart > 0)
      ld.raLocate(normarts, "CART", Integer.toString(cart));
  }

  private void print() {
    jp.getNavBar().getColBean().setRaJdbTable(jp.getMpTable());
    printer.setInterTitle(getClass().getName());
    printer.setColB(jp.getNavBar().getColBean());
    printer.setRTitle(this.getTitle());
    printer.runIt();
  }

  private void enableCART() {
//    normarts.getColumn("CARTNOR").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
//    normarts.getColumn("CART").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
//    normarts.getColumn("CART1").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
//    normarts.getColumn("BC").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
//    normarts.getColumn(Aut.getAut().getCARTdependable("CART", "CART1", "BC")).
//        setVisible(com.borland.jb.util.TriStateProperty.DEFAULT);
    setDataSet(normarts);
    jp.setVisibleCols(new int[] {Aut.getAut().getCARTdependable(1,2,3),4,5,6});
    jp.getColumnsBean().initialize();
  }

  private void setDataSet(QueryDataSet ds) {
    /** @todo andrej implementirati u jpTableView nekako */
    jp.setKumTak(true);
    jp.setDataSet(null);
    jp.setStoZbrojiti(new String[] {});
    jp.setKumTak(false);
    jp.setDataSet(ds);
  }

  private void expand() {
    if (normarts.isEmpty()) return;
    if (!ld.raLocate(dm.getSortedNorme(), "CARTNOR",
                       String.valueOf(normarts.getInt("CART")))) {
      checkActions();
      JOptionPane.showMessageDialog(this.getWindow(), "Artikl nije normiran!", "Greška",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }
    stack.addLast(new Integer(cartnor));
    setNormativ(normarts.getInt("CART"), 0);
  }

  private void doubleClick() {
    if (normarts.isEmpty()) return;
    if (!ld.raLocate(dm.getSortedNorme(), "CARTNOR",
      String.valueOf(normarts.getInt("CART")))) return;
    stack.addLast(new Integer(cartnor));
    setNormativ(normarts.getInt("CART"), 0);
  }

  private void combine() {
    if (stack.isEmpty()) this.hide();
    else setNormativ(((Integer) stack.removeLast()).intValue(), 0);
  }

  private void checkActions() {
    if (normarts.isEmpty())
      details.setEnabled(false);
    else if (!ld.raLocate(dm.getSortedNorme(), "CARTNOR",
        String.valueOf(normarts.getInt("CART"))))
      details.setEnabled(false);
    else details.setEnabled(true);
    if (!stack.isEmpty())
      exit.setIcon(raImages.getImageIcon(raImages.IMGBACK));
    else exit.setIcon(raImages.getImageIcon(raImages.IMGX));
    exit.setTexts();
  }

  private DefaultMutableTreeNode findNode(DefaultMutableTreeNode parent, int cart) {
    for (int i = 0; i < parent.getChildCount(); i++)
      if (((Node) ((DefaultMutableTreeNode) parent.getChildAt(i)).getUserObject()).cart == cart)
        return (DefaultMutableTreeNode) parent.getChildAt(i);
    throw new ConcurrentModificationException();
  }

  private void selectPath() {
    int n = 0;
    Object[] nodes = new Object[stack.size() + 2];
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) t.getModel().getRoot();
    nodes[n++] = node;
    if (stack.size() > 1)
      for (int i = 1; i < stack.size(); i++)
        nodes[n++] = node = findNode(node, ((Integer) stack.get(i)).intValue());
    if (stack.size() > 0)
      nodes[n++] = node = findNode(node, cartnor);
    nodes[n++] = findNode(node, normarts.getInt("CART"));

    TreePath tp = new TreePath(nodes);
    t.setSelectionPath(tp);
//    t.makeVisible(tp);

  }

  private void resync() {
    createModel(stack.size() > 0 ? ((Integer) stack.getFirst()).intValue() : cartnor);
    jp.fireTableDataChanged();
    try {
      selectPath();
    } catch (Exception e) {
      System.err.println("Path no longer exists");
    }
  }

  private void navigated() {
    checkActions();
    if (jp.isAncestorOf(sp))
      try {
        selectPath();
      } catch (ConcurrentModificationException e) {
        e.printStackTrace();
        this.hide();
      }
  }

  private void pathSelected(TreePath tp) {
    if (tp == null) return;
    stack.clear();
    if (tp.getPathCount() == 1) setNormativ(((Node) ((DefaultMutableTreeNode)
        tp.getLastPathComponent()).getUserObject()).cart, -1);
    else {
      DefaultMutableTreeNode node = null;
      for (int i = 0; i < tp.getPathCount() - 1; i++) {
        if (node != null)
          stack.addLast(new Integer(((Node) node.getUserObject()).cart));
        node = (DefaultMutableTreeNode) tp.getPathComponent(i);
      }
      setNormativ(((Node) node.getUserObject()).cart, ((Node) ((DefaultMutableTreeNode)
        tp.getLastPathComponent()).getUserObject()).cart);
    }
  }

  private void showTree() {
    if (jp.isAncestorOf(sp)) {
      tree.setIcon(raImages.getImageIcon(raImages.IMGALLBACK));
      jp.remove(sp);
      sp.remove(original);
      jp.add(original, BorderLayout.CENTER);
      details.setNavBorder(null);
    } else {
      tree.setIcon(raImages.getImageIcon(raImages.IMGALLFORWARD));
      jp.remove(original);
      sp.setRightComponent(original);
      jp.add(sp, BorderLayout.CENTER);
      sp.repaint();
      details.setNavBorder(null);
    }
  }

  private void jbInit() throws Exception {
    norme.getDataModule().setFilter(normarts, "");
    this.getContentPane().add(jp, BorderLayout.CENTER);
    jp.setDataSet(normarts);
    java.lang.reflect.Field f = raJPTableView.class.getDeclaredField("jPanelTable");
    f.setAccessible(true);
    original = (JPanel) f.get(jp);

    tp.setViewportView(t);
    DefaultTreeCellRenderer tcr = new DefaultTreeCellRenderer() {
      public Component getTreeCellRendererComponent(JTree t, Object o,
          boolean sel, boolean exp, boolean leaf, int row, boolean focus) {
        return super.getTreeCellRendererComponent(t, o, sel, exp, leaf, row, false);
      }
    };

    t.setCellRenderer(tcr);
    tcr.setLeafIcon(tcr.getClosedIcon());

    sp.setLeftComponent(tp);
    sp.setBorder(null);
    sp.setContinuousLayout(true);
//    enableCART();
//    jp.setVisibleCols(new int[] {Aut.getAut().getCARTdependable(1,2,3),4,5,6});
    jp.setBorder(BorderFactory.createEtchedBorder());
    jp.setPreferredSize(new Dimension(640, 320));
//    String saveName = getClass().getName();
//    jp.getColumnsBean().setSaveName(saveName);
    jp.getColumnsBean().setSaveSettings(false);
//    jp.setVisibleCols(new int[] {0,1,2,3});
//    jp.getColumnsBean().initialize();

    jp.getNavBar().addOption(tree = new raNavAction("Stablo", raImages.IMGALLBACK, KeyEvent.VK_F6) {
      public void actionPerformed(ActionEvent e) {
        showTree();
      }
    });
    jp.getNavBar().addOption(details = new raNavAction("Proširi", raImages.IMGHISTORY, KeyEvent.VK_ENTER) {
      public void actionPerformed(ActionEvent e) {
        JraKeyListener.enterNow = false;
        expand();
      }
    });
    jp.getNavBar().addOption(new raNavAction("Ispis", raImages.IMGPRINT, KeyEvent.VK_F5) {
      public void actionPerformed(ActionEvent e) {
        frmNormat.this.print();
      }
    });
    jp.getNavBar().addOption(exit = new raNavAction("Izlaz", raImages.IMGX, KeyEvent.VK_ESCAPE) {
      public void actionPerformed(ActionEvent e) {
        combine();
      }
    });
    jp.initKeyListener(this);

    jp.getNavBar().registerNavBarKeys(t);

    /*normarts.addNavigationListener(new NavigationListener() {
      public void navigated(NavigationEvent e) {
        
      }
    });*/
    new NavigationAdapter() {
      public void navigated(DataSet source) {
        frmNormat.this.navigated();
      }
    }.install(normarts);

    t.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        pathSelected(e.getNewLeadSelectionPath());
      }
    });
    InputMap im = sp.getInputMap(sp.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    KeyStroke F8 = KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0);
    KeyStroke F6 = KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0);
    while (im != null && (im.get(F8) != null || im.get(F6) != null)) {
      im.remove(F8);
      im.remove(F6);
      im = im.getParent();
    }
  }
}

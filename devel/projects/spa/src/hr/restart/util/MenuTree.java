/****license*****************************************************************
**   file: MenuTree.java
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

import hr.restart.help.raShortcutContainer;
import hr.restart.help.raShortcutItem;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class MenuTree extends JTree implements raShortcutContainer {
//sysoutTEST ST= new sysoutTEST(true);
  private javax.swing.JMenuBar[] jmenuBars;
  private java.awt.Component[] stripMenus;
  private DefaultMutableTreeNode rootnode;
  private DefaultMutableTreeNode selectedItem=null;
  private DefaultMutableTreeNode lastnode;
  private startFrame cmodule=null;
  private Hashtable menuLeafs = new Hashtable();
  private raShortcutContainer shortcutTarget;
  private raShortcutContainer recentTarget;
//  private int ic_w;
//  private int ic_h;

  public MenuTree(javax.swing.JMenuBar[] jmenuBarsC) {
    this();
    setJmenuBars(jmenuBarsC);
  }
  public MenuTree() {
    super(new DefaultMutableTreeNode());
    setRowHeight(-1);
    rootnode=(DefaultMutableTreeNode)this.getModel().getRoot();
//    ic_w = ((DefaultTreeCellRenderer)MenuTree.this.getCellRenderer()).getIcon().getIconWidth()*2;
//    ic_h = ((DefaultTreeCellRenderer)MenuTree.this.getCellRenderer()).getIcon().getIconHeight()*2;
    rootnode.setUserObject(new treeLabel("Sustav Poslovnih Aplikacija",
        raImages.getImageIcon(raImages.IMGRAICON,1.4)
//        new ImageIcon(raImages.getImageIcon(raImages.IMGRAICON).getImage().getScaledInstance(ic_w+2,ic_h,Image.SCALE_SMOOTH))
        ,SwingConstants.TRAILING));

    setCellRenderer(new treeRenderer());
    addListeners();
  }
  public DefaultMutableTreeNode getRootnode() {
    return rootnode;
  }
  private void addListeners() {
/*  //odkomentiraj ako hoces da enter na treeu izaziva akciju
    this.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        this_keyPressed(e);
      }
    });*/
    //zakomentiraj ako hoces da enter na treeu izaziva akciju
    this.addKeyListener(new hr.restart.swing.JraKeyListener());
    
    this.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        this_mouseClicked(e);
      }
    });
    this.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        this_valueChanged(e);
      }
    });
    this.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
      public void treeCollapsed(TreeExpansionEvent e) {
        selectedItem = null;
      }
      public void treeExpanded(TreeExpansionEvent e) {
        selectedItem = null;
      }
    });
  }
  /**
   * to je array menija koje ne treba dodati u tree
   */
  public void setStripMenus(java.awt.Component[] newstripMenus) {
    stripMenus = newstripMenus;
  }
  private boolean isMenuOK(java.awt.Component mnuElement) {
    String txt="";
    if (mnuElement instanceof javax.swing.AbstractButton) txt=((javax.swing.AbstractButton)mnuElement).getText();
    if (mnuElement instanceof javax.swing.JMenuBar) txt=((javax.swing.JMenuBar)mnuElement).getToolTipText();
    if (txt==null) txt="";

    return !txt.equals("");
  }
  public void expandAll() {
//    this.expandPath(new TreePath(lastnode.getPath()));
  }
  public void setJmenuBars(javax.swing.JMenuBar[] newJmenuBars) {
    jmenuBars=newJmenuBars;
    addBars(jmenuBars);
  }
/**
 * rootnode
 *    menubarnode
 *      menunode
 *        optionnode
 *
 */
  public void addBars(javax.swing.JMenuBar[] mBars) {
    for (int i=0;i<mBars.length;i++) {
      addMenuBar(mBars[i],rootnode);
    }
  }

  public void addMenuBar(javax.swing.JMenuBar mBar,startFrame newModule) {
    cmodule = newModule;
    addMenuBar(mBar);
  }
  public void addMenuBar(javax.swing.JMenuBar mBar) {
    addMenuBar(mBar,rootnode);
  }

  public void addMenuBar(javax.swing.JMenuBar mBar,DefaultMutableTreeNode nodetoadd) {
    if (!isMenuOK(mBar)) return ;
    treeLabel jl = new treeLabel(mBar.getToolTipText(),raImages.getModuleIcon(cmodule),SwingConstants.TRAILING);
    jl.app = hr.restart.mainFrame.findAPPBundleSec(cmodule.getClass().getName());
//    DefaultMutableTreeNode menubarnode = new DefaultMutableTreeNode(mBar.getToolTipText());
        DefaultMutableTreeNode menubarnode = new DefaultMutableTreeNode(jl);
    for (int i=0;i<mBar.getMenuCount();i++) {
      addMenu(mBar.getMenu(i),menubarnode);
    }
    nodetoadd.add(menubarnode);
  }

  public void addMenu(javax.swing.JMenu mMenu,DefaultMutableTreeNode nodetoadd) {
    if (!isMenuOK(mMenu)) return;
    DefaultMutableTreeNode menunode = new DefaultMutableTreeNode(mMenu.getText());
    for (int i=0;i<mMenu.getMenuComponentCount();i++) {
      if (mMenu.getMenuComponent(i) instanceof javax.swing.JMenu) {
        addMenu((javax.swing.JMenu)mMenu.getMenuComponent(i),menunode);
      } else if (mMenu.getMenuComponent(i) instanceof javax.swing.JMenuItem){
        addOption((javax.swing.JMenuItem)mMenu.getMenuComponent(i),menunode);
      }
    }
    nodetoadd.add(menunode);
  }

  public void addOption(javax.swing.JMenuItem mOption,DefaultMutableTreeNode nodetoadd) {
    if (!isMenuOK(mOption)) return;
    treeOption toption = getNewTreeOption(mOption,cmodule);
    DefaultMutableTreeNode optionnode = new DefaultMutableTreeNode(toption);
    nodetoadd.add(optionnode);
    menuLeafs.put(toption.toString(),toption);
    lastnode = optionnode;
  }

  public treeOption getNewTreeOption(JMenuItem mOption,startFrame cmodule) {
    return new treeOption(mOption,cmodule);
  }

  void execAction(java.awt.event.InputEvent e) {
    if (selectedItem!=null) {
      treeOption opt = (treeOption)selectedItem.getUserObject();
      execAction(opt);
    }
  }
  void execAction(treeOption opt) {
    opt.executeAction();
    if (opt.getModule()!=null)
    if (hr.restart.start.isMainFrame()) hr.restart.mainFrame.getMainFrame().showModule(opt.getModule());
  }

  void this_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) execAction(e);
  }

  void this_keyPressed(KeyEvent e) {
    if (e.getKeyCode()==e.VK_ENTER) execAction(e);
  }

  public boolean isShortcutTarget() {
     return false;
  }

  public raShortcutItem getSelectedShortcutItem() {
    if (selectedItem == null) return null;
    treeOption opt = (treeOption)selectedItem.getUserObject();
    return getShortcutItem(opt);
  }

  public raShortcutItem getShortcutItem(final treeOption opt) {
    raShortcutItem shItem = new raShortcutItem(opt) {
      public void actionPerformed(ActionEvent e) {
        execAction(opt);
      }
    };
    shItem.setText(opt.toString());
    return shItem;
  }

  public raShortcutItem getShortcutItem(String txt) {
    treeOption opt = (treeOption)menuLeafs.get(txt);
    if (opt == null) return null;
    return getShortcutItem(opt);
  }
  /**
   * za search
   */
  public HashSet getAllMenuShortcutItems() {
    Set keyset = menuLeafs.keySet();
    HashSet ret = new HashSet();
    for (Iterator i = keyset.iterator(); i.hasNext(); ) {
      ret.add(getShortcutItem(i.next().toString()));
    }
    return ret;
  }
  public class treeOption {
    javax.swing.JMenuItem this_option;
    private startFrame module=null;
    public treeOption(final javax.swing.JMenuItem item,startFrame appModule) {
      this_option=item;
      if (toAddListener()) this_option.addActionListener(new recentInserter(this));
      if (appModule!=null) module=appModule;
    }
    public JMenuItem getMenuItem() {
      return this_option;
    }
    public String toString() {
      return this_option.getText();
    }
    public void executeAction() {
      this_option.doClick();
    }
    public startFrame getModule() {
      return module;
    }
    boolean toAddListener() {
      return true;
    }
  }
  public class recentInserter implements ActionListener {
    treeOption treeOp;
    public recentInserter(treeOption _treeOp) {
      treeOp = _treeOp;
    }
    public void actionPerformed(ActionEvent e) {
      raShortcutItem sitem = getShortcutItem(treeOp);
      recentTarget.addItem(sitem);
    }
  }
  void this_valueChanged(TreeSelectionEvent e) {
    selectedItem = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
    if (!selectedItem.isLeaf()) selectedItem = null;
  }
  public void addItem(raShortcutItem item) {
  }
  public void removeItem(raShortcutItem item) {

  }
  public void removeItem(String appToRemove) {
    Enumeration _enum = rootnode.children();
    while (_enum.hasMoreElements()) {
      Object obj = _enum.nextElement();
      if (obj instanceof javax.swing.tree.DefaultMutableTreeNode) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)obj;
        if (node.getUserObject() instanceof treeLabel) {
          treeLabel tlabel = (treeLabel)node.getUserObject();
          //System.out.println("tlabel = "+tlabel.app);
          if (tlabel.app.equals(appToRemove)) {
            node.removeFromParent();
          }
        }
//        System.out.println("el: "+((treeLabel)obj).getText());
      }
    }
  }
  /**
   * Returns the shortcutTarget.
   * @return raShortcutContainer
   */
  public raShortcutContainer getShortcutTarget() {
    return shortcutTarget;
  }

  /**
   * Sets the shortcutTarget.
   * @param shortcutTarget The shortcutTarget to set
   */
  public void setShortcutTarget(raShortcutContainer _shortcutTarget) {
    shortcutTarget = _shortcutTarget;
  }

  public void setRecentTarget(raShortcutContainer _recentTarget) {
    recentTarget = _recentTarget;
  }
/*
  public void setUI(javax.swing.plaf.TreeUI ui) {
    System.out.println("pokusava setat ui");
    super.setUI(myUI);
  }
*/
  class treeRenderer extends DefaultTreeCellRenderer {
    public treeRenderer() {
      super();
      setClosedIcon(raImages.getImageIcon(raImages.IMGTCLOSED,0.6));
      setOpenIcon(raImages.getImageIcon(raImages.IMGTOPEN,0.6));
      setLeafIcon(raImages.getImageIcon(raImages.IMGTLEAF,0.6));
    }
    public Component getTreeCellRendererComponent(
        JTree tree, Object value, boolean selected, boolean expanded,
        boolean leaf, int row, boolean hasFocus) {
      treeLabel tl = null;
      if (value instanceof DefaultMutableTreeNode) {
        Object ob = ((DefaultMutableTreeNode)value).getUserObject();
        if (ob instanceof treeLabel) {
          tl = (treeLabel)ob;
        }
      }
      super.getTreeCellRendererComponent(tree,value,selected,expanded,leaf,row,hasFocus);
      if (tl!=null) {
        setText(tl.getText());
        setIcon(tl.getIcon());
      }
      return this;
//      return rendered;
    }
  }
  public class treeLabel extends JLabel {
    String app = "";
    public treeLabel(String s, ImageIcon i, int n) {
      super(s,i,n);
//      setIcon(new ImageIcon(i.getImage().getScaledInstance(ic_w,ic_h,Image.SCALE_SMOOTH)));
    }
  }
}
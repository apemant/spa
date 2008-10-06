/****license*****************************************************************
**   file: dlgCompanyTree.java
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

import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.KeyAction;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;

public class dlgCompanyTree {
  lookupData ld = lookupData.getlookupData(); 
  JTree tree;
  String lastRoot;
  JraDialog dlg;
  CheckRenderer rend;
  KeyAction popupAct;
  
  static Map instances;  // map of shared instances
  
  boolean ok;
  Rectangle windowBounds;
  
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      CancelPress();
    }
  };
  
  public static dlgCompanyTree get(String key) {
    if (instances == null) 
      instances = new HashMap();
    if (!instances.containsKey(key))
      instances.put(key, new dlgCompanyTree());
    return (dlgCompanyTree) instances.get(key);
  }
  
  protected dlgCompanyTree() {
    // can't instantiate
    
    initTree();
  }
  
  public void setRootCorg(String corg) {
    if (lastRoot == null || !corg.equals(lastRoot))
      initStructure(lastRoot = corg);
  }
  
  private void addSubtree(DataSet ds, DefaultMutableTreeNode parent) {
    String corg = ds.getString("CORG");
    if (ld.raLocate(ds, "PRIPADNOST", corg))
      for (; ds.inBounds() && ds.getString("PRIPADNOST").equals(corg); ds.next())
        if (!ds.getString("CORG").equals(corg)) {
          DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(new Node(ds));
          parent.add(leaf);
          int currentRow = ds.getRow();
          addSubtree(ds, leaf);
          ds.goToRow(currentRow);
        }
  }
  
  protected void CancelPress() {
    windowBounds = dlg.getBounds();
    dlg.dispose();
    AWTKeyboard.unregisterKeyStroke(AWTKeyboard.ESC, popupAct);
    AWTKeyboard.unregisterComponent(tree);
  }

  protected void OKPress() {
    ok = true;
    CancelPress();
  }

   void initStructure(String knj) {
    DataSet ds = OrgStr.getOrgStr().getOrgstrAndKnjig(knj);
    ds.setSort(new SortDescriptor(new String[] {"PRIPADNOST"}));
    ld.raLocate(ds, "CORG", knj);
    DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Node(ds));
    addSubtree(ds, root);
    ((Node) root.getUserObject()).selected = true;
    tree.setModel(new DefaultTreeModel(root));
    tree.expandRow(0);
    for (int i = 0; i < tree.getRowCount(); i++)
      tree.expandRow(i);
  }
  
  public boolean show(Container owner, String root, String title) {  
    if (owner instanceof JComponent)
      owner = ((JComponent) owner).getTopLevelAncestor();
    if (owner instanceof Frame)
      dlg = new JraDialog((Frame) owner, true);
    else if (owner instanceof Dialog)
      dlg = new JraDialog((Dialog) owner, true);
    else dlg = new JraDialog((Frame) null, true);

    setRootCorg(root);
    
    ok = false;
    dlg.getContentPane().add(new JraScrollPane(tree));
    dlg.getContentPane().add(okp, BorderLayout.SOUTH);
    okp.registerOKPanelKeys(dlg);
    AWTKeyboard.registerKeyStroke(tree, AWTKeyboard.SPACE, new KeyAction() {
      public boolean actionPerformed() {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            toggle(tree.getSelectionPath());
          }
        });
        return true;
      }
    });
    AWTKeyboard.registerKeyStroke(AWTKeyboard.ESC, popupAct = new KeyAction() {
      public boolean actionPerformed() {
        if (!raTreeSelectPopup.isDisplayed()) return false;
        raTreeSelectPopup.hideInstance();
        return true;
      }
    });
    dlg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    dlg.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        CancelPress();
      }
    });
    dlg.setTitle(title);
    //System.out.println(windowBounds);
    if (windowBounds == null) {
      dlg.setSize(480, 400);
      Aus.centerWindow(dlg);
      windowBounds = dlg.getBounds();
    } else 
      dlg.setBounds(windowBounds);
    System.out.println(windowBounds);

    dlg.show();
    return ok;
  }

  public Map getTransitions(String corg) {
    Map result = new HashMap();
    setRootCorg(corg);
    fillTransitions(result, (DefaultMutableTreeNode) tree.getModel().getRoot());
    return result;
  }

  private void fillTransitions(Map result, DefaultMutableTreeNode node) {
    Node n = (Node) node.getUserObject();
    if (!n.selected)
      result.put(n.corg, getSelectedParent(node).corg);
    for (int i = 0; i < node.getChildCount(); i++)
      fillTransitions(result, (DefaultMutableTreeNode) node.getChildAt(i));
  }

  private Node getSelectedParent(DefaultMutableTreeNode node) {
    while (!node.isRoot() && !((Node) node.getUserObject()).selected)
      node = (DefaultMutableTreeNode) node.getParent();
    return (Node) node.getUserObject();
  }

  private void initTree() {
    tree = new JTree();
    tree.setRowHeight(18);
    tree.setCellRenderer(rend = new CheckRenderer());
    tree.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if ((e.getModifiers() & e.BUTTON1_MASK) != 0) {
          int x = e.getX();
          int y = e.getY();
          TreePath path = tree.getPathForLocation(x, y);
          if (path != null) {
            Rectangle rect = tree.getPathBounds(path);
            if (rect != null && rend.insideCheckBox(x - rect.x, y - rect.y))           
              toggle(path);
          }
        }
      }
    });
    raTreeSelectPopup.installFor(tree);
  }

  private void unselect(DefaultMutableTreeNode node) {
    ((Node) node.getUserObject()).selected = false;
    ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
    for (int i = 0; i < node.getChildCount(); i++)
      unselect((DefaultMutableTreeNode) node.getChildAt(i));
  }
  
  private void select(DefaultMutableTreeNode node) {
    ((Node) node.getUserObject()).selected = true;
    ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
    if (node.getParent() != null)
      select((DefaultMutableTreeNode) node.getParent());
  }
  
  protected void toggle(TreePath path) {
    if (path == null) return;
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
    if (node.isRoot()) return;
    if (((Node) node.getUserObject()).selected) unselect(node);
    else select(node);
  }
}


class Node {
  public String corg, nazorg;
  public boolean selected;
  public Node(DataSet org) {
    corg = org.getString("CORG");
    nazorg = corg + " - " + org.getString("NAZIV");
    selected = true;
  }
  public String toString() {
    return nazorg;
  }
}

/**
 * @version 1.1 04/24/99
 */
class CheckRenderer extends JPanel implements TreeCellRenderer {
  protected JraCheckBox check;
  protected TreeLabel label;
  
  public CheckRenderer() {
    setLayout(null);
    add(check = new JraCheckBox());
    check.setFocusTraversable(false);
    check.setText(" ");
    add(label = new TreeLabel());
    check.setBackground(UIManager.getColor("Tree.textBackground"));
    label.setForeground(UIManager.getColor("Tree.textForeground"));
  }

  public Component getTreeCellRendererComponent(JTree tree, Object value,
               boolean isSelected, boolean expanded,
               boolean leaf, int row, boolean hasFocus) {
    String stringValue = tree.convertValueToText(value, isSelected,
            expanded, leaf, row, hasFocus);
    setEnabled(tree.isEnabled());
    check.setSelected(((Node)((DefaultMutableTreeNode) value).getUserObject()).selected);
    label.setFont(tree.getFont());
    label.setText(stringValue);
    label.setSelected(isSelected);
    if (isSelected)
      label.setForeground(UIManager.getColor("Tree.selectionForeground"));
    else label.setForeground(UIManager.getColor("Tree.textForeground"));
    label.setFocus(false);
    if (leaf) {
      label.setIcon(UIManager.getIcon("Tree.openIcon"));
    } else if (expanded) {
      label.setIcon(UIManager.getIcon("Tree.openIcon"));
    } else {
      label.setIcon(UIManager.getIcon("Tree.closedIcon"));
    }       
    return this;
  }
  
  public Dimension getPreferredSize() {
    Dimension d_check = check.getPreferredSize();
    Dimension d_label = label.getPreferredSize();
    return new Dimension(d_check.width  + d_label.width,
      (d_check.height < d_label.height ?
       d_label.height : d_check.height));
  }
  
  public boolean insideCheckBox(int x, int y) {
    Dimension d = check.getPreferredSize();
    return x >= 0 && x < d.width && y >= 0 && y < d.height; 
  }
  
  public void doLayout() {
    Dimension d_check = check.getPreferredSize();
    Dimension d_label = label.getPreferredSize();
    int y_check = 0;
    int y_label = 0;
    /*if (d_check.height < d_label.height) {
      y_check = (d_label.height - d_check.height)/2;
    } else {
      y_label = (d_check.height - d_label.height)/2;
    }*/
    check.setLocation(0,y_check);
    check.setBounds(0,y_check,d_check.width,d_check.height);
    label.setLocation(d_check.width,y_label);    
    label.setBounds(d_check.width,y_label,d_label.width,d_label.height);    
  }
   
  
  public void setBackground(Color color) {
    if (color instanceof ColorUIResource)
      color = null;
    super.setBackground(color);
  }
  
    
  public class TreeLabel extends JLabel {
    boolean isSelected;
    boolean hasFocus;
    
    public TreeLabel() {
    }

    public void setBackground(Color color) {
    if(color instanceof ColorUIResource)
        color = null;
    super.setBackground(color);
    } 
         
    public void paint(Graphics g) {
      String str;
      if ((str = getText()) != null) {
        if (0 < str.length()) {
          if (isSelected) {
            g.setColor(UIManager.getColor("Tree.selectionBackground"));
          } else {
            g.setColor(UIManager.getColor("Tree.textBackground"));
          }
          Dimension d = this.getPreferredSize();
          int imageOffset = 0;
          Icon currentI = getIcon();
          if (currentI != null) {
            imageOffset = currentI.getIconWidth() + Math.max(0, getIconTextGap() - 1);
          }
          g.fillRect(imageOffset, 0, d.width -1 - imageOffset, d.height);
          if (hasFocus) {
            g.setColor(UIManager.getColor("Tree.selectionBorderColor"));
            g.drawRect(imageOffset, 0, d.width -1 - imageOffset, d.height -1);     
         }
        }
      }
      super.paint(g);
    }
  
    public Dimension getPreferredSize() {
      Dimension retDimension = super.getPreferredSize();
      if (retDimension != null) {
        retDimension = new Dimension(retDimension.width + 3,
                 retDimension.height);
      }
      return retDimension;
    }
    
    public void setSelected(boolean isSelected) {
      this.isSelected = isSelected;
    }
    
    public void setFocus(boolean hasFocus) {
      this.hasFocus = hasFocus;
    }
  }
}

class raTreeSelectPopup extends JPopupMenu {

  JTree tree = null;
  DefaultMutableTreeNode selRow;
  
  static raTreeSelectPopup inst = new raTreeSelectPopup();
  private Action selBranch, unselBranch, selNodeEx, selBranchDir;

  public static void installFor(JTree t) {
    t.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        inst.checkPopup(e);
      }
      public void mousePressed(MouseEvent e) {
        inst.checkPopup(e);
      }
      public void mouseReleased(MouseEvent e) {
        inst.checkPopup(e);
      }
    });
  }
  
  public static boolean isDisplayed() {
    return inst.isVisible();
  }

  public static void hideInstance() {
    inst.setVisible(false);
  }
  
  public void checkPopup(MouseEvent e) {
    if (e.isPopupTrigger() && e.getSource() instanceof JTree) {
      tree = (JTree) e.getSource();
      TreePath path = tree.getPathForLocation(e.getX(), e.getY());
      if (path != null) {
        tree.setSelectionPath(path);
        selRow = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (selRow != null && !selRow.isLeaf()) {
          boolean sel = ((Node) selRow.getUserObject()).selected;
          boolean root = selRow.isRoot();
          selBranch.putValue(Action.NAME, root ? 
              "Oznaèi cijelu strukturu" :
              "Oznaèi èvor i sve ispod njega");
          selBranchDir.putValue(Action.NAME, root ? 
              "Oznaèi poèetni èvor i direktne nasljednike" :
              "Oznaèi èvor i direktne nasljednike");
          selNodeEx.putValue(Action.NAME, root ? 
              "Oznaèi samo poèetni èvor a poništi ostale" :
              "Oznaèi èvor a poništi sve ispod njega");
          unselBranch.setEnabled(!root && sel);
          show(tree, e.getX(), e.getY());
        }
      }
    }
  }

  private raTreeSelectPopup() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    add(selBranch = new AbstractAction("Oznaèi èvor i sve ispod njega") {
      public void actionPerformed(ActionEvent e) {
        selectBranch();
      }
    });
    add(selBranchDir = new AbstractAction("Oznaèi èvor i direktne nasljednike") {
      public void actionPerformed(ActionEvent e) {
        selectBranchDir();
      }
    });
    add(selNodeEx = new AbstractAction("Oznaèi èvor a poništi pripadajuæu granu") {
      public void actionPerformed(ActionEvent e) {
        selectNodeEx();
      }
    });
    add(unselBranch = new AbstractAction("Poništi èvor i sve ispod njega") {
      public void actionPerformed(ActionEvent e) {
        unselectBranch();
      }
    });
  }
  
  void set(DefaultMutableTreeNode node, boolean val) {
    ((Node) node.getUserObject()).selected = val;
    ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
    for (int i = 0; i < node.getChildCount(); i++)
      set((DefaultMutableTreeNode) node.getChildAt(i), val);
  }

  void select(DefaultMutableTreeNode node) {
    ((Node) node.getUserObject()).selected = true;
    ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
    if (node.getParent() != null)
      select((DefaultMutableTreeNode) node.getParent());
  }
  
  protected void selectBranch() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        set(selRow, true);
        select(selRow);
      }
    });
  }
  
  protected void selectBranchDir() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        for (int i = 0; i < selRow.getChildCount(); i++) {
          DefaultMutableTreeNode c = (DefaultMutableTreeNode) selRow.getChildAt(i);
          ((Node) c.getUserObject()).selected = true;
          ((DefaultTreeModel) tree.getModel()).nodeChanged(c);
        }
        select(selRow);
      }
    });
  }
  
  protected void unselectBranch() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        set(selRow, false);
      }
    });
  }
  
  protected void selectNodeEx() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        set(selRow, false);
        select(selRow);
      }
    });
  }
}


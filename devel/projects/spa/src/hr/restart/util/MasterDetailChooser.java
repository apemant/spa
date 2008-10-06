package hr.restart.util;

import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raExtendedTable;
import hr.restart.swing.raTableModifier;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;


public class MasterDetailChooser {
  
  JraDialog win;
  DataRow prev, next;
  Variant shared = new Variant();
  Variant temp = new Variant();
  
  JraTable2 mpt = new raExtendedTable() {
    HashSet sameRows = new HashSet();
    HashSet diffRows = new HashSet();
    HashSet sameCols = new HashSet();
    public void paintChildren(Graphics g) {
      super.paintChildren(g);
      if (!turboTable) return;
      // brisanje prokl. bijelih linija oko praznih polja
      if (sameRows.size() > 0 && sameCols.size() > 0) {
        Color old = g.getColor();
        g.setColor(getParent().getBackground());
        int cm = getColumnModel().getColumnMargin();
        if (cm > 0)
        for (Iterator ri = sameRows.iterator(); ri.hasNext(); ) {
          int row = ((Integer) ri.next()).intValue();
          for (Iterator ci = sameCols.iterator(); ci.hasNext(); ) {
            int col = ((Integer) ci.next()).intValue();
            Rectangle r = getCellRect(row, col, true);
            if (cm > 1) g.fillRect(r.x, r.y, cm/2, r.height);
            g.fillRect(r.x + r.width - cm + cm/2, r.y, cm - cm/2, r.height);
          }
        }
        g.setColor(old);
      }
      sameRows.clear();
      sameCols.clear();
      diffRows.clear();
    }
    public void killFocus(java.util.EventObject e) {
      okp.jPrekid.requestFocus();
    }
    public void tableDoubleClicked() {
      jp.mpTable_doubleClicked();
    }
    public boolean shouldDrawCell(int row, int column) {
      if (!turboTable) return true;
      
      Column col = getDataSetColumn(column);
      if (dlist.contains(col.getColumnName().toLowerCase())) return true;
      String sort = data.getSort() == null ? null : data.getSort().getKeys()[0];
      if (sort != null && !mlist.contains(sort.toLowerCase())) return true;
      if (row == 0 || row == data.getRowCount()) return true;
      Integer iRow = new Integer(row);
      if (sameRows.contains(iRow)) {
        sameCols.add(new Integer(column));
        return false;
      }
      if (diffRows.contains(iRow)) return true;
      data.getDataRow(row - 1, prev);
      data.getDataRow(row, next);
      boolean same = prev.equals(next);
      if (same) {
        sameRows.add(iRow);
        sameCols.add(new Integer(column));
      } else diffRows.add(iRow);
      return !same;
    }
  };
  public raJPTableView jp = new raJPTableView(mpt) {
    public void mpTable_doubleClicked() {
      accept = true;
      destroy();
    }
  };

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      accept = true;
      destroy();
    }
    public void jPrekid_actionPerformed() {
      destroy();
    }
  };
  
  boolean turboTable = true;
  boolean accept;
  StorageDataSet data;
  List mlist;
  List dlist;
  int[] vis;
  
  public MasterDetailChooser(StorageDataSet md, String saveName,
      String[] mcols, String[] dcols, int[] viscols) {
    data = md;
    prev = new DataRow(md, mcols);
    next = new DataRow(md, mcols);
    mlist = new ArrayList(mcols.length);
    dlist = new ArrayList(dcols.length);
    vis = viscols;
    
    for (int i = 0; i < mcols.length; i++)
      mlist.add(mcols[i].toLowerCase());
    for (int i = 0; i < dcols.length; i++)
      dlist.add(dcols[i].toLowerCase());
    HashSet all = new HashSet();
    all.addAll(mlist);
    all.addAll(dlist);
    for (int i = 0; i < md.getColumnCount(); i++)
      if (!all.contains(md.getColumn(i).getColumnName().toLowerCase()))
        md.getColumn(i).setVisible(0);
    
    jp.setBorder(BorderFactory.createEtchedBorder());
    jp.getNavBar().getColBean().setSaveSettings(true);
    jp.getNavBar().getColBean().setSaveName(saveName);
    okp.setEnterEnabled(true);
    
    turboTable = frmParam.getParam("sisfun", "mdChooser", "D", 
      "Sakriti identiène kolone mastera na master-detail chooseru (D,N)", true).
        equalsIgnoreCase("D");
  }
  
  public boolean isTurboTable() {
    return turboTable;
  }
  
  public void addModifier(raTableModifier mod) {
    jp.addTableModifier(mod);
  }
  
  public void removeModifier(raTableModifier mod) {
    jp.removeTableModifier(mod);
  }
  
  public boolean show(Container parent, String title) {
    Container realparent = null;

    if (parent instanceof JComponent)
      realparent = ((JComponent) parent).getTopLevelAncestor();
    else if (parent instanceof Window)
      realparent = parent;

    if (realparent instanceof Dialog)
      win = new JraDialog((Dialog) realparent, title, true);
    else if (realparent instanceof Frame)
      win = new JraDialog((Frame) realparent, title, true);
    else win = new JraDialog((Frame) null, title, true);
    
    win.getContentPane().setLayout(new BorderLayout());
    win.getContentPane().add(jp);
    win.getContentPane().add(okp, BorderLayout.SOUTH);
    win.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        destroy();
      }
    });
    win.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    
    jp.setKumTak(true);
    jp.setDataSet(null);
    jp.setStoZbrojiti(new String[] {});
    jp.setKumTak(false);
    jp.setDataSet(data);
    jp.getColumnsBean().setAditionalCols(vis);
    jp.getColumnsBean().initialize();
    
    okp.registerOKPanelKeys(win);
    jp.initKeyListener(win);
    
    win.pack();
    Point cbpl = jp.getColumnsBean().getPreferredLocationOnScreen();
    if (cbpl != null) win.setLocation(cbpl);
    else win.setLocationRelativeTo(null);
    
    win.setVisible(true);
    return accept;
  }
  
  void destroy() {
    if (win != null) {
      win.dispose();
      win = null;
    }
  }
}

/****license*****************************************************************
**   file: raNavBar.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraTable2;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JToolBar;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class raNavBar extends JToolBar {
  public static final int EMPTY = 0;
  public static final int DBNAVIGATE = 1;
  public static final int COLUMNSBEAN = 2;
  public static final int DBNAVCOLB = 3;

  // 28-08-2002 identifikatori standardnih raNavActiona (ab.f)
  public static final int ACTION_ADD = 0;
  public static final int ACTION_UPDATE = 1;
  public static final int ACTION_DELETE = 2;
  public static final int ACTION_PRINT = 3;
  public static final int ACTION_TOGGLE_TABLE = 4;
  public static final int ACTION_EXIT = 5;


  private java.awt.event.KeyEvent key;
  private JraTable2 navTable;
  private int[] navVisibleCols;
  private int initOption;
  private raJPTableView jpTabV = null;
  raJPNavContainer jpNavigate = new raJPNavContainer();
  hr.restart.util.columnsbean.ColumnsBean colBean = new hr.restart.util.columnsbean.ColumnsBean() {
    public void kumAction(String kumColName) {
      kumPressed(kumColName);
    }
    public void refreshAction() {
      super.refreshAction();
      afterRefresh();
    }
  };
  XYLayout tbLayout = new XYLayout();
  XYConstraints xyc = new XYConstraints(0,0,400,raNavAction.ACTSIZE);

  public raNavBar(int initOptionC) {
    setInitOption(initOptionC);
    initNavBar();
  }
//  public void paint(Graphics g) {
//    super.paint(g);
//    hr.restart.plaf.raPainter.paintGradient(g,this);
//  }
  public raNavBar() {
    setInitOption(DBNAVCOLB);
    initNavBar();
  }

  private void initNavBar() {
    try {
      jbInit();
    } catch (Exception e) {
    }
  }
  public int getInitOption() {
    return initOption;
  }

  public void setInitOption(int newOption) {
    initOption = newOption;
  }

  public void setNavTable(JraTable2 tabl) {
    navTable = tabl;
    colBean.setRaJdbTable(navTable);
  }
  public void setNavVisibleCols(int[] nvc) {
    navVisibleCols = nvc;
    colBean.setAditionalCols(navVisibleCols);
  }

  public void registerNavBarKeys(raFrame rfr) {
    if (rfr.getFrameMode() == rfr.FRAME) registerNavBarKeys(rfr.getJframe());
//    if (rfr.getFrameMode() == rfr.INTERNALFRAME) registerNavBarKeys(rfr.getIframe());
    if (rfr.getFrameMode() == rfr.DIALOG) registerNavBarKeys(rfr.getJdialog());
    if (rfr.getFrameMode() == rfr.PANEL) registerNavBarKeys(hr.restart.mainFrame.getMainFrame());
  }

  public void unregisterNavBarKeys(raFrame rfr) {
    if (rfr.getFrameMode() == rfr.FRAME) unregisterNavBarKeys(rfr.getJframe());
//    if (rfr.getFrameMode() == rfr.INTERNALFRAME) unregisterNavBarKeys(rfr.getIframe());
    if (rfr.getFrameMode() == rfr.DIALOG) unregisterNavBarKeys(rfr.getJdialog());
    if (rfr.getFrameMode() == rfr.PANEL) unregisterNavBarKeys(hr.restart.mainFrame.getMainFrame());
  }

  public void registerNavBarKeys(Component comp) {
    jpNavigate.registerNavKeys(comp);
    if (isCOLUMNSBEAN()) colBean.registerColumnsBeanKeys(comp);
  }

  public void unregisterNavBarKeys(Component comp) {
    jpNavigate.unregisterNavKeys(comp);
    if (isCOLUMNSBEAN()) colBean.unregisterColumnsBeanKeys(comp);
  }

  public hr.restart.util.columnsbean.ColumnsBean getColBean() {
    return colBean;
  }
  private void jbInit() throws Exception {    
    setFloatable(false);
    setBorder(BorderFactory.createEmptyBorder());
    setLayout(tbLayout);
    add(jpNavigate);
    if (isDBNAVIGATE()) createJpNavigate();
    if (isCOLUMNSBEAN()) createJpColBean();
    if (initOption == EMPTY) add(jpNavigate);
  }

  public boolean isDBNAVIGATE() {
    return ((initOption==DBNAVIGATE)||(initOption==DBNAVCOLB));
  }

  public boolean isCOLUMNSBEAN() {
    return ((initOption==COLUMNSBEAN)||(initOption==DBNAVCOLB));
  }

  public boolean isDBNAVCOLB() {
    return (initOption==DBNAVCOLB);
  }

  private void createJpNavigate() {
    addOption(rnvAdd);
    addOption(rnvUpdate);
    addOption(rnvDelete);
    addOption(rnvPrint);
    if (showToggleTable()) {
      addOption(rnvToggleTable);
    }
    addOption(rnvExit);
  }
  
  private static Boolean _showToggleTable = null;
  private static boolean showToggleTable() {
    if (_showToggleTable == null) {
      _showToggleTable = new Boolean(frmParam.getParam("sisfun","showToggleTable","N",
        "Prikazati uopce gumbic >>Promijeni tablicni prikaz<< (D/N)",true).equalsIgnoreCase("D"));
    }
    return _showToggleTable.booleanValue();
  }
  
  public int getOptionIndex(raNavAction rnv) {
    raNavAction[] options = getOptions();
    for (int i=0;i<options.length;i++) {
      if (options[i].equals(rnv)) return i;
    }
    return -1;
  }

  public raNavAction[] getOptions() {
    Component[] comps = jpNavigate.getComponents();
    java.util.HashMap optMap = new java.util.HashMap();
    int optIdx = 0;
    for (int i=0;i<comps.length;i++) {
      if (comps[i] instanceof raNavAction) {
        optMap.put(Integer.toString(optIdx),comps[i]);
        optIdx++;
      }
    }
    raNavAction[] retRnvs = new raNavAction[optIdx];
    for (int i=0;i<optIdx;i++) {
      retRnvs[i] = (raNavAction)optMap.get(Integer.toString(i));
    }
    return retRnvs;
  }

  private void createJpColBean() {
    addSeparator();
    add(colBean,xyc);
  }

  public boolean contains(raNavAction act) {
    return jpNavigate.contains(act);
  }

  public void addOption(raNavAction act) {
    jpNavigate.addOption(act,xyc);
  }
  public void addOption(raNavAction act, int pos) {
    jpNavigate.addOption(act,pos,xyc);
  }
/**
 * Defaultni navigacijski gumbeki:
 */
  raNavAction rnvAdd = new raNavAction("Novi",raImages.IMGADD,key.VK_F2) {
      public void actionPerformed(ActionEvent e) {
        add_action();
      }
  };
  raNavAction rnvUpdate = new raNavAction("Izmjena",raImages.IMGCHANGE,key.VK_F4) {
      public void actionPerformed(ActionEvent e) {
        update_action();
      }
  };
  raNavAction rnvDelete = new raNavAction("Brisanje",raImages.IMGDELETE,key.VK_F3) {
      public void actionPerformed(ActionEvent e) {
        delete_action();
      }
  };

  raNavAction rnvPrint = new raNavAction("Ispis",raImages.IMGPRINT,key.VK_F5) {
      public void actionPerformed(ActionEvent e) {
        print_action();
      }
  };

  raNavAction rnvExit = new raNavAction("Izlaz",raImages.IMGX,key.VK_ESCAPE) {
      public void actionPerformed(ActionEvent e) {
        exit_action();
      }
  };

  raNavAction rnvToggleTable = new raNavAction("Promijeni tabli\u010Dni prikaz",raImages.IMGTABLE,key.VK_T,key.CTRL_MASK) {
      public void actionPerformed(ActionEvent e) {
        table_action();
      }
  };

  public void removeOption(raNavAction rmv) {
    if (this.contains(rmv)) {
      rmv.unregisterKeyStroke();
      jpNavigate.remove(rmv);
      jpNavigate.clcSize(xyc);
    }
  }

  // 28-08-2002 static array standardnih 'gumbeka' (ab.f)
  // bitno je da se redoslijed podudara sa identifikatorima
  private raNavAction[] rnvStandardActions =
    new raNavAction[] {rnvAdd, rnvUpdate, rnvDelete, rnvPrint, rnvToggleTable, rnvExit};

    /** throws ArrayIndexOutOfBoundsException za krivi id, a to je dobro :)
     * 28-08-2002 izbacuje standardni gumbek (ab.f)
     * raMatPodaci.getNavBar().removeStandardOption(raNavBar.ACTION_ADD);  i sl.
     * throws ArrayIndexOutOfBoundsException za krivi id, a to je dobro :)
     * @param id index akcije definiran u raNavBar
     */
  public void removeStandardOption(int id) {
  	if (this.contains(rnvStandardActions[id])) {
      rnvStandardActions[id].unregisterKeyStroke();
  	  jpNavigate.remove(rnvStandardActions[id]);
  	  jpNavigate.clcSize(xyc);
  	}
  }
  /** 28-08-2002 izbacuje nekoliko standardnih gumbeka (ab.f)
   * raMatPodaci.getNavBar().removeStandardOptions(new int[] {raNavBar.ACTION_ADD, raNavBar.ACTION_UPDATE});  i sl.
   * @param ids indexi akcije definiran u raNavBar
   */
  public void removeStandardOptions(int[] ids) {
  	for (int i = 0; i < ids.length; i++)
  	  removeStandardOption(ids[i]);
  }
  /** 25-09-2003 geter za odredjenu standardnu opciju (ai.m)
   * throws ArrayIndexOutOfBoundsException za krivi id, a to je dobro :)
   * @param id index akcije definiran u raNavBar
   * @return akciju vezanu uz zadani index
   */
  public raNavAction getStandardOption(int id) {
    return rnvStandardActions[id];
  }

  public void add_action() {
    System.out.println(rnvAdd.getIdentifier());
  }

  public void update_action() {
    System.out.println(rnvUpdate.getIdentifier());
  }

  public void delete_action() {
    System.out.println(rnvDelete.getIdentifier());
  }

  public void print_action() {
    System.out.println(rnvPrint.getIdentifier());
  }

  public void exit_action() {
    System.out.println(rnvExit.getIdentifier());
  }

  public void table_action() {
    System.out.println(rnvToggleTable.getIdentifier());
  }

  public raJPNavContainer getNavContainer() {
    return jpNavigate;
  }

  public void setJpTabView(raJPTableView JpTabView) {
    jpTabV = JpTabView;
  }

  public void kumPressed(String kumColName) {
    if (jpTabV != null) {
      jpTabV.kumPressed(kumColName);
    }
  }

  public void afterRefresh() {}
}
/****license*****************************************************************
**   file: raUserPanel.java
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
package hr.restart.help;

import hr.restart.swing.JraKeyListener;
import hr.restart.swing.JraScrollPane;
import hr.restart.util.IntParam;
import hr.restart.util.MenuTree;
import hr.restart.util.raImages;
import hr.restart.util.raJPNavContainer;
import hr.restart.util.raNavAction;
import hr.restart.util.raScreenHandler;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class raUserPanel extends JTabbedPane {
//  private JPanel treePanel = new JPanel(new BorderLayout());
  public static String IDXTAG = "userpanelidx";
  public static String TABSPOSTAG = "helptabspos";
  public static String MENUTAG ="helptabsmenu";
  private MenuTree menuTree = new MenuTree();
  private TaskTree taskTree = new TaskTree();
  private raShortcutPanel shortcuts = new raShortcutPanel(menuTree,"shortcuts.properties", null,true);
  private raShortcutPanel recent = new raShortcutPanel(menuTree,"recent.properties", shortcuts.getShortcuts() ,false);
  private raSearchPanel searchPanel = new raSearchPanel();
  private raShortcutNavBar treeNavBar;
  private boolean menuDriven = false;
  private JMenuBar menuBar;
  private JMenu tabsMenu;
  private boolean refreshMenus = false;
/*
  private raMatPodaci rmp = new raMatPodaci() {
    public void SetFokus(char c) {
    }
    public boolean Validacija(char c) {
      return true;
    }
  };
*/
  public raUserPanel() {
    this(hr.restart.mainFrame.getMainFrame());
  }
  public raUserPanel(hr.restart.mainFrame mf) {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  
  void jbInit() throws Exception {
    setTabPlac();
    addKeyListener(new JraKeyListener());
    initTreePanel();
    addDefault();
    initMenu();
    refreshMenus = true;

    addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent e) {
//        tabChanged(tabBefore, getToolTipTextAt(getSelectedIndex()));//tooltipovi su sigurno origigi
        showSelectedTabInMenu();
      }
    });

  }
  private void setTabPlac() {
    int plac = BOTTOM;
      String prop = IntParam.getTag(TABSPOSTAG);
      String menu = IntParam.getTag(MENUTAG);
      if (menu.equals("")) {
        menu = "true";
        IntParam.setTag(MENUTAG, menu);
      }
      menuDriven = menu.equals("true");
      try {
        plac = Integer.parseInt(prop);
      } catch (Exception e) {
        plac = BOTTOM;
        IntParam.setTag(TABSPOSTAG, plac+"");
      }
      setTabPlacement(plac);    
  }
  private void initMenu() {
    if (!menuDriven) {
      menuBar = null;
      tabsMenu = null;
      return;
    }
    menuBar = new JMenuBar();
    tabsMenu = new JMenu("Akcije");
    addMenuItems();
    menuBar.add(tabsMenu);
    showSelectedTabInMenu();
  }
  private void addMenuItems() {
    if (!menuDriven) return;
    tabsMenu.removeAll();
    for (int i=0;i<getTabCount();i++) {
			final int tindex = i;
      raShortcutItem[] shortcutItems = getShortcutItemsAt(i);
      String mname = getToolTipTextAt(i);
      JMenuItem item;
      AbstractAction action = new AbstractAction(mname, getIconAt(i)) {
        public void actionPerformed(ActionEvent ev) {
          raUserPanel.this.setSelectedIndex(tindex);
        }
      };
boolean add_submenus = false; //Iskljucio jer ne radi i cudno se ponasa sve
      if (shortcutItems != null && shortcutItems.length > 0 && add_submenus) {
System.out.println("radim podmenije za "+mname);
        item = new JMenu(action);
        for (int ii=0;ii<shortcutItems.length;ii++) {
          final raShortcutItem it = shortcutItems[ii];
          JMenuItem menuShItem = new JMenuItem(it.getText(), it.getIcon());
          menuShItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
              it.actionPerformed(ev);
            }
          });
System.out.println("  adding menuShItem "+menuShItem.getText());
          item.add(menuShItem);
        }
      } else {
//System.out.println(mname+": shortcutItems = "+shortcutItems);
//if (shortcutItems!=null) System.out.println("  shortcutItems.length = "+shortcutItems.length);
        item = new JMenuItem(action);
      }
      tabsMenu.add(item);
    }
  }
  private raShortcutItem[] getShortcutItemsAt(int tabidx) {
    String mname = getToolTipTextAt(tabidx);
    raShortcutPanel rshp = (raShortcutPanel)shortcutPanels.get(mname);
    if (rshp != null && rshp.getShortcuts() != null) {
      return rshp.getShortcuts().getItems();
    } else {
      return null;
    }
  }
  private void showSelectedTabInMenu() {
    if (!menuDriven) return;
    tabsMenu.setText(getToolTipTextAt(getSelectedIndex()));
    tabsMenu.setIcon(getIconAt(getSelectedIndex()));
  }

  public JMenuBar getJMenuBar() {
    return menuBar;
  }
/*
  private String tabBefore = null;
  void tabChanged(String oldTab, String newTab) {
    if (oldTab != null && oldTab.equals(newTab)) return;
    if (oldTab != null) {
      System.out.println("micem listenere sa "+oldTab);
      registerBarKeys(oldTab, getTopLevelAncestor(), false);
    }
    if (newTab != null) {
      System.out.println("stavljam listener na "+newTab);
      registerBarKeys(newTab, getTopLevelAncestor(), true);
    }
    tabBefore = newTab;
  }
  private void registerBarKeys(String barKey, Component comp, boolean register) {
      raShortcutNavBar bar = (raShortcutNavBar)navBarMap.get(barKey);
      if (bar!=null) {
        if (register) {
          bar.registerNavKeys(comp);
        } else {
          bar.unregisterNavKeys(comp);
        }
      }
  }
  private HashMap navBarMap = new HashMap();
*/
  void initTreePanel() {
    treeNavBar = new raShortcutNavBar(menuTree);
    menuTree.setShortcutTarget(shortcuts.getShortcuts());
    menuTree.setRecentTarget(recent.getShortcuts());
  }
  void addDefault() {
    addUserTab("Kratice",shortcuts);
    addUserTab("Danas otvoreno",recent);
    addUserTab("Opcije",menuTree);
    addUserTab("Pomo\u0107",raImages.IMGHELP,new raHelpContainer());
    addUserTab("Traži",raImages.IMGFIND,searchPanel);
//    addUserTab("Task Tree", taskTree);
  }
  
  private HashMap shortcutPanels = new HashMap();
  public void loadShortcutPanels() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        for (Iterator i = shortcutPanels.keySet().iterator(); i.hasNext(); ) {
          Object key = i.next();
          raShortcutPanel item = (raShortcutPanel)shortcutPanels.get(key);
          item.getShortcuts().initFromProps();
        }
        setSelectedIndex(calcSelectedIndex());
      }
    });
  }

  private int calcSelectedIndex() {
    int idx;
    try {
      idx = Integer.parseInt(IntParam.getTag(IDXTAG));
      return idx;
    } catch (Exception ex) {
      idx = -1;
    }
    if (shortcuts.getShortcuts().getItemCount()>0) {
      return indexOfTab("Kratice");
    }
    idx = indexOfTab("Aplikacije");
    if (idx < 0) idx = indexOfTab("Opcije");
    return idx;
    
  }
   
  Component getSrolledComponent(Component comp, String title) {
    if (comp.equals(menuTree)) {
      //navBarMap.put(title, treeNavBar);
      treeNavBar.registerNavKeys(menuTree);
      return getScrollAndToolbarComp(comp,treeNavBar);
    } else if (comp instanceof raShortcutPanel) {
      raShortcutPanel rShPan = (raShortcutPanel)comp;
      rShPan.getNavBar().registerNavKeys(rShPan.getShortcuts());
      shortcutPanels.put(title, rShPan);
      //navBarMap.put(title, ((raShortcutPanel)comp).getNavBar());
      return getScrollAndToolbarComp(comp,rShPan.getNavBar());
    } else if (comp instanceof raHelpContainer) {
      raHelpContainer hlpCont = (raHelpContainer)comp;
      hlpCont.getNavBar().registerNavKeys(hlpCont.getShortcuts());
      return getScrollAndToolbarComp(comp,hlpCont.getNavBar());
    } else if (comp instanceof raSearchPanel) {
      raSearchPanel srchPan = (raSearchPanel)comp;
      return getScrollAndToolbarComp(comp,srchPan.getShortcutNavBar());
    } else {
      return getScrollAndToolbarComp(comp,new raJPNavContainer());
      //return new JraScrollPane(comp);
    }
  }
  public static boolean getAutoHideOption() {
    String sAHopt = IntParam.getTag("autohide");
    boolean ret;
    if (sAHopt.equals("")) {
      ret = (Toolkit.getDefaultToolkit().getScreenSize().width<1000);
    } else {
      ret = new Boolean(sAHopt).booleanValue();
    }
    System.out.println(" getAutoHideOption() "+ ret);
    return ret;
  }
  public static void setAutoHide(boolean b) {
    IntParam.setTag("autohide", new Boolean(b).toString());
  }
  private void toggleMenuAction(Component invoker) {
    JPopupMenu menu = new JPopupMenu("Tabovi");
    JCheckBoxMenuItem itGore = (JCheckBoxMenuItem)menu.add(new JCheckBoxMenuItem("Gore", getTabPlacement() == TOP));
    JCheckBoxMenuItem itDolje = (JCheckBoxMenuItem)menu.add(new JCheckBoxMenuItem("Dolje", getTabPlacement() == BOTTOM));
    JCheckBoxMenuItem itLijevo = (JCheckBoxMenuItem)menu.add(new JCheckBoxMenuItem("Lijevo", getTabPlacement() == LEFT));
    JCheckBoxMenuItem itDesno = (JCheckBoxMenuItem)menu.add(new JCheckBoxMenuItem("Desno", getTabPlacement() == RIGHT));
    menu.addSeparator();
    JCheckBoxMenuItem itMenu = (JCheckBoxMenuItem)menu.add(new JCheckBoxMenuItem("Izbornik", menuDriven));
    menu.addSeparator();
    JCheckBoxMenuItem itAutoHide = (JCheckBoxMenuItem)menu.add(new JCheckBoxMenuItem("Auto Hide", getAutoHideOption()));
    itGore.addActionListener(new PlacAction(TOP));
    itDolje.addActionListener(new PlacAction(BOTTOM));
    itLijevo.addActionListener(new PlacAction(LEFT));
    itDesno.addActionListener(new PlacAction(RIGHT));
    itMenu.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent ev) {
        menuDriven = ev.getStateChange()==ev.SELECTED;
        initMenu();
        changeTabPlac(raUserPanel.this.getTabPlacement());
        raUserDialog.getInstance().setJMenuBar(getJMenuBar());
        raUserDialog.getInstance().validate();
        /*javax.swing.SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            
          }
        });*/
      }
    });
    itAutoHide.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent ev) {
        setAutoHide(ev.getStateChange()==ev.SELECTED);
        if (getAutoHideOption()) {
          raUserDialog.getInstance().startSmoothShow();
        } else {
          raUserDialog.getInstance().stopSmoothShow();
        }
        raScreenHandler.recalcSizeOfMain();
      }
    });
    menu.show(invoker, 0, raNavAction.ACTSIZE);
  }
  /**
   * Method getScrollAndToolbarComp.
   * @param comp
   * @param r
   * @return Component
   */
  private Component getScrollAndToolbarComp(
      Component comp,
      /*raShortcutNavBar*/raJPNavContainer r) {
    JraScrollPane scroll = null;
    if (!(comp instanceof raHelpContainer)) {
      scroll = new JraScrollPane(comp);
    }
    JPanel navPanel = new JPanel() {
//    pogodak brand
/*      private Image img = raImages.getImageIcon(raImages.IMGPOGODAK).getImage();//288x70
      //   setBorder(BorderFactory.createLineBorder(Color.black,2));
      public void paint(Graphics g) {
        super.paint(g);
        double factor = getHeight()/70.0;
        //System.out.println("faktor = "+factor);
        double w = 288*factor;
        double x = getWidth() - w - 10;
        g.drawImage(img, (int)x, 0, (int)w, getHeight(), this);
      }
*/      
    };
    final raNavAction namnu = new raNavAction("Menu",raImages.IMGTABLE,KeyEvent.VK_M,KeyEvent.CTRL_MASK) {
      public void actionPerformed(ActionEvent e) {
        toggleMenuAction(this);
      }
    };
    r.addOption(namnu);
    navPanel.setLayout(null);
    int w = r.getComponentCount()*raNavAction.ACTSIZE;
    int h = raNavAction.ACTSIZE;
    r.setBounds(0,0,w,h);
    Dimension nsize = new Dimension(w,h);
    navPanel.add(r);
    navPanel.setPreferredSize(nsize);
    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.add(navPanel,BorderLayout.NORTH);
    contentPanel.add(scroll!=null?scroll:comp,BorderLayout.CENTER);
    return contentPanel;
  }
  
  public void setSelectedComponent(Component comp) {
    //                                ViewPort    JraScrollPane
    super.setSelectedComponent(comp.getParent().getParent());
  }
  private String getVertTitle(String title) {
    /*
    char[] _ctit = title.toCharArray();
    String _title = "<html><pre>";
    for (int i = 0; i < _ctit.length; i++) {
      _title = _title.concat(new Character(_ctit[i]).toString()).concat("\n");
    }
    _title=_title.concat("</pre></html>");
    System.out.println("_title = "+_title);
    return _title;
    */
    if (menuDriven) {
      return "";
    } else if (getTabPlacement()==RIGHT || getTabPlacement()==LEFT) {
      return title.substring(0,3).concat("...");
    } else {
      return title;
    }
  }
  public void addUserTab(String title,String raicon,Component component) {
    super.addTab(getVertTitle(title),raImages.getImageIcon(raicon),getSrolledComponent(component,title),title);
    if (refreshMenus) addMenuItems();
  }
  public void addUserTab(String title,Icon icon,Component component) {
    super.addTab(getVertTitle(title),icon,getSrolledComponent(component,title),title);
    if (refreshMenus) addMenuItems();
  }
  public void addUserTab(String title,Component component) {
    Icon icon = raImages.getSizedImage(raImages.getImageIcon(raImages.IMGTOPEN),16);
    addUserTab(title,icon,component);
  }
  public void initTree(startFrame sf) {
    menuTree.removeAll();
    menuTree.addMenuBar(sf.getJMenuBar(),sf);
  }
  public MenuTree getMenuTree() {
    return menuTree;
  }
  public TaskTree getTaskTree() {
	  return taskTree;
  }
/*  public raHelpPanel getHelpPanel() {
    return helpPanel;
  }*/
  public raSearchPanel getSearchPanel() {
    return searchPanel;
  }
  
  public boolean isMenuDriven() {
    return menuDriven;
  }
  
  public raShortcutContainer getDefaultShortcutTarget() {
    return shortcuts.getShortcuts();
  }
  
  private void changeTabPlac(int plac) {
    setTabPlacement(plac);
    for (int i=0;i<getTabCount();i++) {
      setTitleAt(i, getVertTitle(getToolTipTextAt(i)));
    }
  }
  
  class PlacAction implements ActionListener {
    int plac;
    public PlacAction(int _plac) {
      plac = _plac;
    }
    public void actionPerformed(ActionEvent e) {
      raUserPanel.this.changeTabPlac(plac);
    }
  }
}
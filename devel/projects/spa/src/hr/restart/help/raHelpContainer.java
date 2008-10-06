/****license*****************************************************************
**   file: raHelpContainer.java
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
/*
 * raHelpContainer.java
 *
 * Created on 2004. svibanj 31, 13:24
 */

package hr.restart.help;

import hr.restart.swing.JraScrollPane;
import hr.restart.swing.raGradientPainter;
import hr.restart.util.Aus;
import hr.restart.util.raFrame;
import hr.restart.util.raImages;
import hr.restart.util.raKeyAction;
import hr.restart.util.raKeyActionSupport;
import hr.restart.util.raNavAction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
/**
 *
 * @author  andrej
 */
public class raHelpContainer extends JPanel {
  private static raHelpContainer rhc;
  private raAbstractShortcutContainer shcontainer;
  private JraScrollPane shcontScroll;
  private JButton jbHelpBrw;
  private raShortcutNavBar shNavBar;
  private JTextArea jlTitle = new JTextArea() {
    public void paint(java.awt.Graphics g) {
      super.paint(Aus.forceAntiAlias(g));
    }
  };
  private Container helpBrowser = new raLiteBrowser().getContentPane();
  private raGradientPainter grpainter = new raGradientPainter(getBackground().darker(),Color.white);
  private JPanel titlePanel;
  private raHelpAware lastItem = null;
  /** Creates a new instance of raHelpContainer */
  public raHelpContainer() {
    rhc = this;
    jInit();
  }
  
  private void jInit() {
    shcontainer = new raAbstractShortcutContainer() {
      public boolean isShortcutTarget() {
        return false;
      }
    };
    shNavBar = new raShortcutNavBar(shcontainer);
    jlTitle.setText(getCommonHelpText());
    jlTitle.setOpaque(false);
    initTextArea(jlTitle);
    titlePanel = new JPanel(new BorderLayout()) {
      public void paint(Graphics g) {
        super.paint(g);
        grpainter.paintGradient(g, titlePanel,true,true);
      }
    };
    titlePanel.add(new raHelper(new Clock(false)),BorderLayout.NORTH);
    titlePanel.add(jlTitle,BorderLayout.CENTER);
    jbHelpBrw = new JButton("Upute");
    jbHelpBrw.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        toggleHelpBrowser();
      }
    });
    titlePanel.add(jbHelpBrw,BorderLayout.SOUTH);
    //titlePanel.setBackground(shcontainer.getBackground());
    shcontScroll = new JraScrollPane(shcontainer);
    shcontScroll.addAncestorListener(new AncestorListener() {
      public void ancestorAdded(AncestorEvent ev) {
        if (lastItem!=null && isVisible(lastItem) && shcontainer.getItemCount()==0) {
          initHP(lastItem);
        }
      }
      public void ancestorRemoved(AncestorEvent ev) {};
      public void ancestorMoved(AncestorEvent ev) {};
    });
    setLayout(new BorderLayout());
    add(shcontScroll, BorderLayout.CENTER);
    add(titlePanel, BorderLayout.NORTH);
  }
  public void updateUI() {
    super.updateUI();
    grpainter = new raGradientPainter(getBackground().darker(),Color.white);
  }
  
  
  private void toggleHelpBrowser() {
    if (shcontScroll.getParent() == null) {
      remove(helpBrowser);
      add(shcontScroll,BorderLayout.CENTER);
      jbHelpBrw.setText("Upute");
    } else {
      remove(shcontScroll);
      add(helpBrowser,BorderLayout.CENTER);
      jbHelpBrw.setText("Pomo\u0107 za ekran");
    }
    jlTitle.setText(getCommonHelpText());
    getTopLevelAncestor().repaint();
    validateTree();
  }
  private String getCommonHelpText() {
    if (shcontScroll!=null && shcontScroll.getParent() == null) {
      return "Op\u0107e upute";
    } else if (lastItem!=null && isVisible(lastItem) && shcontainer.getItemCount()>0) {
      return lastItem.getTitle();
    } else {
      return "Otvorite ekran preko izbornika ili kliknite na vec otvoreni";
    }
  }
  private void initTextArea(JTextArea jta) {
    jta.setLineWrap(true);
    jta.setWrapStyleWord(true);
    jta.setEditable(false);
    jta.setOpaque(false);
    raShortcutItem.setFancyFont(jta);
  }
  /**
   * Zvati samo ako bas moras:) (raUpitLite)
   * @param item raHelpAware
   */
  public void initWith(raHelpAware item) {
    if (isVisible(item)) initHP(item);
  }
  private void initHP(raHelpAware item) {
    raNavAction[] rnvs = item.getNavActions();
    raNavAction[] rnvscb = item.getColumnsBean()==null?null:item.getColumnsBean().getNavActions();
    raKeyAction[] rkys = item.getKeyActions();

    addItems(rnvs, "- AKCIJE -");
    addItems(rnvscb, "- TABLICA -");
    addItems(rkys, "- TIPKE -");
    shcontainer.setFixedCellHeight(tallest+3);
    jlTitle.setText(getCommonHelpText());
  }
  private int tallest = 0;
  private void addItems(Object[] rnvs, String separatorText) {
    if (rnvs != null) {
      if (separatorText != null && rnvs.length > 0) addSeparator(separatorText);
      for (int i=0;i<rnvs.length;i++) {
        raShortcutItem shItem = null;
        if (rnvs[i] instanceof raNavAction) {
          shItem = getShortcutItem((raNavAction)rnvs[i]);
          
        } else if (rnvs[i] instanceof raKeyAction) {
          if (((raKeyAction)rnvs[i]).isVisibleInHelper()) {
            shItem = getShortcutItem((raKeyAction)rnvs[i]);
          }
        }
        if (shItem != null) {
          shcontainer.addItem(shItem);
          int h = shItem.getIcon().getIconHeight();
          if (h>tallest) tallest = h;
        }
      }
    }
  }
  
  private void addSeparator(String text) {
    raShortcutItem sep = new raShortcutItem(text);
    sep.setText(text);
    sep.setForeground(sep.getForeground().darker());
    sep.setIcon(raImages.imgNull);
    shcontainer.addItem(sep);
  }
  private void clearIfNeeded() {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (!isVisible(lastItem)) {
          clearHP();
        }
      }
    });
  }

  private raShortcutItem getShortcutItem(final raNavAction act) {
    raShortcutItem ret = new raShortcutItem(act.getIdentifier()) {
      public void actionPerformed(ActionEvent arg0) {
        act.actionPerformed(arg0);
        clearIfNeeded();
      }
    };
    ret.setIcon(act.getIcon());
    ret.setText(act.getToolTipText());
    return ret;
  }
  private raShortcutItem getShortcutItem(final raKeyAction act) {
    
    raShortcutItem ret = new raShortcutItem(act.getActionText()){
      public void actionPerformed(ActionEvent arg0) {
        act.invoke();
        clearIfNeeded();
      }
    };
    if (act.getIcon() == null) {
      ret.setIcon(raImages.getImageIcon(raImages.IMGALIGNJUSTIFY));
    } else {
      ret.setIcon(act.getIcon());
    }
    ret.setText(act.getActionText());
    return ret;
  }
  /**
   * Ne koristiti (vidi raUpitLite)
   */
  public void clearHP() {
    shcontainer.removeAllItems();
    jlTitle.setText(getCommonHelpText());
    /** @todo icon, title bla bla */
  }
  public static raHelpContainer getInstance() {
    return rhc;
  }
  public raShortcutNavBar getNavBar() {
    return shNavBar;
  }
  public raAbstractShortcutContainer getShortcuts() {
    return shcontainer;
  }
  public static void registerHelp(raHelpAware helpItem) {
    getInstance().registerItem(helpItem);
  }
  private boolean isActive() {
    return shcontainer.isShowing();
  }

  private void registerItem(raHelpAware helpItem) {
    WindowListener[] larr = (WindowListener[])(helpItem.getListeners(WindowListener.class));
    if (larr.length > 0) {
      for (int i=0;i<larr.length;i++) {
        if (larr[i] instanceof raHelpContainer.HelpWinListener) {
          helpItem.removeWindowListener((raHelpContainer.HelpWinListener)larr[i]);
        }
      }
    }
    if (helpItem == null) return;
    helpItem.addWindowListener(new HelpWinListener(helpItem));
  }
  private boolean isVisible(raHelpAware item) {
    if (item instanceof raFrame) {
      return ((raFrame)item).isVisible();
      //return ((raFrame)item).getWindow().isVisible();
    }
    if (item.getColumnsBean() != null) {
      return item.getColumnsBean().isShowing();
    }
    if (item instanceof Window) {
      return ((Window)item).isVisible();
    } else if (item instanceof raKeyActionSupport){
      return ((raKeyActionSupport)item).isVisible();
    }
    return true;
  }
  
  class HelpWinListener extends WindowAdapter {
    raHelpAware item = null;
    public HelpWinListener(raHelpAware _item) {
      item = _item;
    }
    public void windowActivated(WindowEvent e) {
      lastItem = item;
      if (!isActive() && shcontainer.getItemCount()>0) clearHP();
      if (!isActive()) return;
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          clearHP();
          initWith(item);
        }
      });
    }
    public void windowClosing(WindowEvent e) {
      lastItem = item;
      if (shcontainer.getItemCount() == 0) return;
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          clearHP();
        }
      });
      
    }
  }
}

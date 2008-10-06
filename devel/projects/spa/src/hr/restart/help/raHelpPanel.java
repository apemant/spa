/****license*****************************************************************
**   file: raHelpPanel.java
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

import hr.restart.mainFrame;
import hr.restart.util.Aus;
import hr.restart.util.raImages;
import hr.restart.util.raKeyAction;
import hr.restart.util.raLLFrames;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raStatusBar;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 * @deprecated raHelpPanel -> raHelpContainer
 */

public class raHelpPanel extends JPanel {
//
  static int r = Color.gray.getRed();
  static int g = Color.gray.getGreen();
  static int b = Color.gray.getBlue();
  static Color backColor = Color.white;//new Color(r,g,b,128);
  static Color foreColor = Color.black;//Color.white;
  
//
  private raMatPodaci raMatPod;
  private GridLayout gridLayout = new GridLayout();
//  JPanel helpItemPanel = new JPanel(new BorderLayout());
  JPanel helpTextPanel = new JPanel();
  private boolean showKeysHelp = false;
  private boolean showTypingHelp = false;
//  private mainFrame mainFr;
  private String helpPanelPosition = BorderLayout.WEST;
  private JPanel lfPanel;
  private JTextArea jlRmpTitle = new JTextArea() {
    public void paint(java.awt.Graphics g) {
      super.paint(Aus.forceAntiAlias(g));
    }    
  };
  //private JLabel jlRmpIcon = new JLabel();
  private JPanel jpRmpTitle = new JPanel(new BorderLayout());
  private String defaultRmpTitle = "Op\u0107enite tipke";
  private String defaultHelpText = "Odaberite sa opciju na izbornicima ili tabu opcije ili kliknite na ekran koji vas interesira";
  private raNavAction defaultRnvAct;
  private raNavAction[] defaultNavActions = new raNavAction[] {defaultRnvAct};
  private helpItem helpText = new helpItem("Pomo\u0107 pri unosu");
  private PropertyChangeListener statusMsgChangeListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent ev) {
          helpText.setText(ev.getNewValue().toString());
        }
      };
  public raHelpPanel() {
//    mainFr = mainFrame.getMainFrame();
    basicInit();
  }
  public raHelpPanel(raMatPodaci rmp,mainFrame mf) {
    this(mf);
    setRaMatPodaci(rmp);
  }
  public raHelpPanel(raMatPodaci rmp) {
//    mainFr = mainFrame.getMainFrame();
    basicInit();
    setRaMatPodaci(rmp);
  }
  public raHelpPanel(mainFrame mf) {
//    mainFr = mf;
    basicInit();
  }
  private void basicInit() {
    setLayout(new BorderLayout());
    helpTextPanel.setLayout(gridLayout);

//    helpTextPanel.setBackground(backColor);
//    helpTextPanel.setForeground(foreColor);

    this.setPreferredSize(new Dimension(100,100));
//    raHelper helper = new raHelper(new Clock(false),mainFr);
    raHelper helper = new raHelper(new Clock(false));
//    helper.setHelpPanel(this);
    this.add(helper,BorderLayout.NORTH);
    initRmpTitle();
  }
  void initRmpTitle() {
    initTextArea(jlRmpTitle);
//    Font fnt = jlRmpTitle.getFont();
//    jlRmpTitle.setFont(fnt.deriveFont(Font.BOLD|Font.ITALIC,fnt.getSize2D()*1.2F));
//    jlRmpTitle.setBorder(BorderFactory.createLoweredBevelBorder());
//    jlRmpTitle.setHorizontalAlignment(SwingConstants.CENTER);
//    jlRmpTitle.setVerticalAlignment(SwingConstants.TOP);
    jlRmpTitle.setText(defaultRmpTitle);
    //jlRmpIcon.setIcon(getDefaultIcon());
    //jlRmpIcon.setVerticalAlignment(SwingConstants.TOP);
    //jpRmpTitle.add(jlRmpIcon,BorderLayout.WEST);
    jpRmpTitle.add(jlRmpTitle,BorderLayout.CENTER);
    helpTextPanel.add(jpRmpTitle);
  }
  public void setRaMatPodaci(raMatPodaci rmp) {
    raMatPod = rmp;
  }

  private void trackTextChange(boolean on) {
    raStatusBar status;
//    if (hr.restart.start.isMainFrame()) {
//      status = mainFr.getStatusBar();
//    } else {
      status = hr.restart.util.raLLFrames.getRaLLFrames().getMsgStartFrame().getStatusBar();
//    }
    status.removePropertyChangeListener("statusMsg",statusMsgChangeListener);
    if (on) status.addPropertyChangeListener("statusMsg",statusMsgChangeListener);
  }

  private void initTextArea(JTextArea jta) {
    jta.setLineWrap(true);
    jta.setWrapStyleWord(true);
    jta.setEditable(false);
    jta.setOpaque(false);
    raShortcutItem.setFancyFont(jta);
//    jta.setForeground(foreColor);
//    jta.setBackground(backColor);
//    jta.setHighlighter(null);
  }
  private void initLabel(JLabel jta) {
    jta.setOpaque(false);
//ai20040524    jta.setForeground(foreColor);
//ai20040524    jta.setBackground(backColor);
//    jta.setFont(jta.getFont().deriveFont(Font.BOLD));
  }

  public void initHP(raMatPodaci rmp) {
    if (rmp==null) return;
    if (rmp.equals(raMatPod)) return;
    forceInitHP(rmp);
  }
  public void setShowTypingHelp(boolean b) {
    showTypingHelp = b;
    if (showTypingHelp) showKeysHelp = false;
    initHP();
  }
  public boolean isShowTypingHelp() {
    return showTypingHelp;
  }
  public void setShowKeysHelp(boolean b) {
    showKeysHelp = b;
    if (showKeysHelp) showTypingHelp = false;
    initHP();
  }
  public boolean isShowKeysHelp() {
    return showKeysHelp;
  }

  public void setHelpPanelPosition(String position) {
    if (helpPanelPosition == BorderLayout.EAST || helpPanelPosition == BorderLayout.NORTH
        || helpPanelPosition == BorderLayout.SOUTH || helpPanelPosition == BorderLayout.WEST) {
      helpPanelPosition = position;
      forceInitHP(raMatPod);
    }
  }

  public String getHelpPanelPosition() {
    return helpPanelPosition;
  }

  public void clearHelp() {
    Component[] comps = helpTextPanel.getComponents();
    for (int i=0;i<comps.length;i++) {
      if (comps[i] instanceof helpItem) {
        helpTextPanel.remove(comps[i]);
      }
    }

//    trackTextChange(false);

    if (helpPanelPosition == BorderLayout.WEST) {
      this.remove(helpTextPanel);
      repaint();
    } else {
      if (lfPanel!=null) {
        lfPanel.remove(helpTextPanel);
        lfPanel.validate();
      }
    }
  }

  public void forceInitHP(raMatPodaci rmp) {
    setRaMatPodaci(rmp);
    initHP();
  }
  private ImageIcon getDefaultIcon() {
    ImageIcon navIcon = raImages.getImageIcon(raImages.IMGRAICON);
    if (raMatPod == null) return navIcon;
    startFrame currModule = raLLFrames.getRaLLFrames().findMsgStartFrame();
    if (currModule == null) return navIcon;
    navIcon = raImages.getModuleIcon(currModule);
    return navIcon;
  }
  private void showDefaultHelp() {
    clearHelp();
    gridLayout.setColumns(0);
    gridLayout.setRows(1);
    jlRmpTitle.setText(defaultHelpText);
    add(helpTextPanel,BorderLayout.CENTER);
//    jlRmpTitle.setIcon(getDefaultIcon());
  }
  public void initHP() {

    if (raMatPod == null) {
      showDefaultHelp();
      return;
    }
    if (showKeysHelp) {
      clearHelp();
      raKeyAction[] keyActions = raMatPod.getKeyActions();
      raNavAction[] cbActions = raMatPod.getJpTableView().getColumnsBean().getNavActions();
      raNavAction[] navActions = raMatPod.getNavBar().getNavContainer().getNavActions();

      if (helpPanelPosition == BorderLayout.WEST || helpPanelPosition == BorderLayout.EAST || helpPanelPosition == BorderLayout.CENTER) {
        gridLayout.setRows(keyActions.length + cbActions.length + navActions.length+1);
        gridLayout.setColumns(1);
      } else {
        gridLayout.setColumns(keyActions.length + cbActions.length + navActions.length);
        gridLayout.setRows(1);
      }
      addNavActions(navActions);
      addKeyActions(keyActions);
      addNavActions(cbActions);
      String rmpText = raMatPod.getTitle();
      if (rmpText==null) rmpText = "";
      if (rmpText.equals("")) {
        rmpText = defaultRmpTitle;
      }
      jlRmpTitle.setText(rmpText);
      //jlRmpIcon.setIcon(getDefaultIcon());
    }
    if (showTypingHelp) {
      clearHelp();
      gridLayout.setColumns(1);
      gridLayout.setRows(1);
      helpTextPanel.add(helpText);
//      trackTextChange(true);
    }
    if (showKeysHelp || showTypingHelp) {
//      if (helpPanelPosition == BorderLayout.WEST) {
        add(helpTextPanel,BorderLayout.CENTER);
        validate();
//      } else {
//        lfPanel = mainFr.leftPanel;
//        lfPanel.add(helpTextPanel,helpPanelPosition);
//        lfPanel.validate();
//      }
    } else {
      clearHelp();
      return;
    }

  }

  private void addNavActions(raNavAction[] acts) {
    for (int i=0;i<acts.length;i++) {
      helpTextPanel.add(new helpItem(acts[i]));
    }
  }

  private void addKeyActions(raKeyAction[] acts) {
    for (int i=0;i<acts.length;i++) {
      if (acts[i].isVisibleInHelper()) helpTextPanel.add(new helpItem(acts[i]));
    }
  }

  class helpItem extends raShortcutItem {
    public helpItem(raNavAction act) {
      setIcon(act.getIcon());
      setText(act.getToolTipText());
    }

    public helpItem(String cText) {
      setText(cText);
    }

    public helpItem(raKeyAction act) {
      setText(act.getActionText());
    }
  }
  class helpItem_old extends JPanel {
    Icon aIcon = raImages.getImageIcon(raImages.IMGOK);
    String aText;
//    JTextArea jText;
    JLabel jText;
    JLabel jlIcon;
    public helpItem_old(raNavAction act) {
      aIcon = act.getIcon();
      aText = act.getToolTipText();
      hInit();
    }

    public helpItem_old(String cText) {
      aText = cText;
      hInit();
    }

    public helpItem_old(raKeyAction act) {
      aText = act.getActionText();
      hInit();
    }
/*
    private void drawImage(java.awt.Graphics g,JComponent c) {
      if (aIcon!=null&&g!=null) {
        g.drawImage(aIcon.getImage(),0,0,c.getWidth(),c.getHeight(),c);
      }
    }
*/
    void hInit() {
      this.setLayout(new BorderLayout());
//      this.setOpaque(false);
//      setBackground(backColor);
//      setForeground(foreColor);
      jlIcon = new JLabel(aIcon) {
//        public void paint(java.awt.Graphics g) {
//          super.paint(g);
//          drawImage(g,this);
//        }
//        {
//          addComponentListener(new java.awt.event.ComponentAdapter() {
//            public void componentResized(java.awt.event.ComponentEvent e) {
//              setSize(getSize().height,getSize().height);
//            }
//          });
//        }
      };
      initLabel(jlIcon);
      jlIcon.setPreferredSize(new Dimension(aIcon.getIconWidth(),aIcon.getIconHeight()));
      this.add(jlIcon,BorderLayout.WEST);
//      jText = new JTextArea(aText);
      jText = new JLabel(aText);
      initLabel(jText);
      this.add(jText,BorderLayout.CENTER);
      this.setBorder(BorderFactory.createLineBorder(backColor));
//      this.setToolTipText(aText);
//      jText.setToolTipText(aText);
    }
    private void repaintParent() {
//      JPanel pradjed = hr.restart.mainFrame.getMainFrame().leftPanel;
/*      int x = pradjed.getX();
      int y = pradjed.getY();
      int w = pradjed.getWidth();
      int h = pradjed.getHeight();*/
//      pradjed.repaint();
//      RepaintManager.currentManager(jText).paintDirtyRegions();
    }
    public void setText(String txt) {
      jText.setText(txt);
//      RepaintManager.currentManager(jText)
//          .addDirtyRegion(hr.restart.mainFrame.getMainFrame().mainPanel,jText.getX(),jText.getY(),jText.getWidth(),jText.getHeight());
//            .markCompletelyDirty(hr.restart.mainFrame.getMainFrame().mainPanel);
    }

    public String getText() {
      return jText.getText();
    }

    public void setIcon(Icon ic) {
      jlIcon.setIcon(ic);
    }

    public Icon getIcon() {
      return jlIcon.getIcon();
    }

    public void setMessage(Icon ic, String txt) {
      if (ic != null) setIcon(ic);
      if (txt != null) setText(txt);
    }
  }
}
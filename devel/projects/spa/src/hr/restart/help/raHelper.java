/****license*****************************************************************
**   file: raHelper.java
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

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class raHelper extends JPanel {
  private Clock clock;
  JMenuItem jmKeysHelp;
  JMenuItem jmEntryHelp;
  private mainFrame mainFr;
//  private raHelpPanel helpPanel;
/*
  raPopMenuAction keysHelp = new raPopMenuAction("Tipke") {
    public void doAction() {
      toggleKeysHelp();
    }
  };

  raPopMenuAction entryHelp = new raPopMenuAction("Pomo\u0107 pri unosu") {
    public void doAction() {
      toggleTypingHelp();
    }
  };

  raPopMenuAction exitAct = new raPopMenuAction("Zatvori") {
    public void doAction() {
      closeSource();
    }
  };
*/
  private raPopMenuAction acDigital = new raPopMenuAction("Digital") {
    public void doAction() {
      setDigital(true);
    }
  };

  private raPopMenuAction acAnalog = new raPopMenuAction("Analog") {
    public void doAction() {
      setDigital(false);
    }
  };
  private raPopMenuAction acDatum = new raPopMenuAction("Datum") {
    public void doAction() {
      setShowDate(!getShowDate());
    }
  };
  private JMenu jmClock = new JMenu("Sat");
/*
  private raPopMenuAction acHPLijevo = new raPopMenuAction("Lijevo") {
    public void doAction() {
      setHelpPanelPosition(BorderLayout.WEST);
    }
  };
  private raPopMenuAction acHPDesno = new raPopMenuAction("Desno") {
    public void doAction() {
      setHelpPanelPosition(BorderLayout.EAST);
    }
  };
  private raPopMenuAction acHPGore = new raPopMenuAction("Gore") {
    public void doAction() {
      setHelpPanelPosition(BorderLayout.NORTH);
    }
  };
  private raPopMenuAction acHPDolje = new raPopMenuAction("Dolje") {
    public void doAction() {
      setHelpPanelPosition(BorderLayout.SOUTH);
    }
  };
  private raPopMenuAction acHPIzlaz = new raPopMenuAction("Zatvori") {
    public void doAction() {
      closeHelp();
    }
  };
  private JMenu jmHPOrientation = new JMenu();
*/
  raPopMenu defaultPopMenu = new raPopMenu();

  public raHelper(JComponent helpComponent,mainFrame mf) {
    mainFr = mf;
    setLayout(new BorderLayout());
    setOpaque(false);
    add(helpComponent,BorderLayout.CENTER);
    if (helpComponent instanceof hr.restart.help.Clock) {
      clock = (hr.restart.help.Clock)helpComponent;
      initClockMenu();
      clock.getAnalogClock().addMouseListener(new popUpMenuListener(defaultPopMenu));
      clock.getDigitalClock().addMouseListener(new popUpMenuListener(defaultPopMenu));
    }
//    initPopupMenu();
  }
  public raHelper(JComponent helpComponent) {
    this(helpComponent,mainFrame.getMainFrame());
  }

  private void initClockMenu() {
    jmClock.add(acDigital);
    jmClock.add(acAnalog);
    jmClock.add(acDatum);
    defaultPopMenu.add(jmClock);
  }
  /*
  private void initPopupMenu() {
    jmKeysHelp = defaultPopMenu.add(keysHelp);
//    jmEntryHelp = defaultPopMenu.add(entryHelp);
    defaultPopMenu.addSeparator();
    defaultPopMenu.add(exitAct);
    jmHPOrientation.add(acHPLijevo);
    jmHPOrientation.add(acHPDesno);
    jmHPOrientation.add(acHPGore);
    jmHPOrientation.add(acHPDolje);
    jmHPOrientation.add(acHPIzlaz);
  }
  public void setHelpPanel(raHelpPanel hp) {
    helpPanel = hp;
  }
  public void closeSource() {
    if (isMainFrame()) {
//      mainFr.toggleHelp();
    } else {
    }
  }
  public boolean isMainFrame() {
    return hr.restart.start.isMainFrame();
  }
  void setDefaultHelpPanel() {
    if (helpPanel!=null) return;
    if (isMainFrame()) {
//      helpPanel = mainFr.raHelpPaneMain;
    } else {
      helpPanel = null;
    }
  }
  public void toggleTypingHelp() {
    if (isHelpPanelSet()) {
      helpPanel.setShowTypingHelp(!helpPanel.isShowTypingHelp());
//      handlejJmHPOrientation(jmEntryHelp,helpPanel.isShowTypingHelp());
    }
  }

  public void toggleKeysHelp() {
    if (isHelpPanelSet()) {
      helpPanel.setShowKeysHelp(!helpPanel.isShowKeysHelp());
//      handlejJmHPOrientation(jmKeysHelp,helpPanel.isShowKeysHelp());
    }
  }

  public void closeHelp() {
    if (isItemReplaced(jmKeysHelp)) {
      toggleKeysHelp();
    } else if (isItemReplaced(jmEntryHelp)) {
      toggleTypingHelp();
    }
  }
  private boolean isItemReplaced(JMenuItem jmi) {
    return jmHPOrientation.getText().equals(jmi.getText());
  }

  private JMenuItem getReplacedItem() {
    if (isItemReplaced(jmKeysHelp)) return jmKeysHelp;
    if (isItemReplaced(jmEntryHelp)) return jmEntryHelp;
    return null;
  }

  private void handlejJmHPOrientation(JMenuItem itemReplace,boolean toShow) {
    if (defaultPopMenu.getComponentIndex(jmHPOrientation) > -1 && toShow) {//postoji
      int idx = defaultPopMenu.getComponentIndex(jmHPOrientation);
      defaultPopMenu.remove(jmHPOrientation);
      defaultPopMenu.add(getReplacedItem(),idx);
    }
    if (toShow) {
      int idx = defaultPopMenu.getComponentIndex(itemReplace);
      defaultPopMenu.remove(itemReplace);
      jmHPOrientation.setText(itemReplace.getText());
      defaultPopMenu.add(jmHPOrientation,idx);
    } else {
      int idx = defaultPopMenu.getComponentIndex(jmHPOrientation);
      defaultPopMenu.remove(jmHPOrientation);
      defaultPopMenu.add(itemReplace,idx);
    }
  }
  boolean isHelpPanelSet() {
    setDefaultHelpPanel();
    return (helpPanel!=null);
  }
  public void setHelpPanelPosition(String pos) {
    if (isHelpPanelSet())
    helpPanel.setHelpPanelPosition(pos);
  }
*/
  private void setDigital(boolean b) {
    if (clock!=null) clock.setDigital(b);
  }
  private void setShowDate(boolean b) {
    if (clock!=null) clock.setShowDate(b);
  }
  private boolean getShowDate() {
    if (clock==null) return false;
    return clock.getShowDate();
  }

}

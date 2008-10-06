/****license*****************************************************************
**   file: mainFrame.java
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
package hr.restart;

import hr.restart.swing.JraFrame;
import hr.restart.util.IntParam;
import hr.restart.util.raImages;
import hr.restart.util.raScreenHandler;
import hr.restart.util.raStatusBar;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author svi mi
 * @version 1.0
 */

public class mainFrame extends JraFrame {
  private static mainFrame fmainframe;
  JMenuBar defaultMenuBar = new JMenuBar();
  JMenu defaultMenu = new JMenu();
  String initTitle = "RestArt - poslovne aplikacije";
  private startFrame currentModule = null;
  private boolean hidden = true;
  private boolean helpOptionChecked = false;
  private raStatusBar statusbar = new raStatusBar();
//  public JDesktopPane mainDesk = new JDesktopPane();
  public JTabbedPane mainTabs = new JTabbedPane();
  private JPanel contPane = new JPanel(new BorderLayout()) {
    public void paint(Graphics g) {
      super.paint(g);
      drawImage(g,this);
    }
    public void paintComponent(java.awt.Graphics g) {
      super.paintComponent(g);
      drawImage(g,this);
    }
  };

  private Image iBackOrg;
  public javax.swing.ImageIcon imgBackground = null;//new javax.swing.ImageIcon(ClassLoader.getSystemResource("hr/restart/util/images/clogo.jpg"));

  public mainFrame() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public void jbInit() throws Exception {
    //title
    setTitle(initTitle);
    //menu
    defaultMenu.setText(getTitle());
    defaultMenuBar.add(defaultMenu);
    setJMenuBar(defaultMenuBar);
    //panels
    setContentPane(contPane);
    mainTabs.setOpaque(false);
    mainTabs.setTabPlacement(start.getMainTabPlacement());
    getContentPane().add(mainTabs,BorderLayout.CENTER);
    getContentPane().add(statusbar,BorderLayout.SOUTH);
    //slike
    setIconImage(raImages.getImageIcon(raImages.IMGRAICON).getImage());
    setBackgroundImage();
    //
  }
  public void show() {
    hidden = false;
    raScreenHandler.showingMainDialog(null);
    super.show();
  }
  public void hide() {
    hidden = true;
    super.hide();
    raScreenHandler.hidingMainDialog(null);
  }
  public static mainFrame getMainFrame() {
    if (fmainframe == null) {
      fmainframe= new mainFrame();
    }
    return fmainframe;
  }

  void showFull() {
    this.pack();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = this.getSize();
    this.setLocation(0,0);
    frameSize.setSize(screenSize.getSize().width,screenSize.height-50);
    try {
      setSize(frameSize);
      setVisible(true);
//      showLeft();
      show();
    } catch (Exception e){e.printStackTrace();}
  }

  public boolean isHidden() {
    return hidden;
  }

  public void showModule(hr.restart.util.startFrame SF) {
    if (!isVisible()) setVisible(true);
    if (SF.getJMenuBar().equals(getJMenuBar())) return;
    currentModule = SF;
    SF.makeDefMenu(SF.getRaJMenuBar());
    setJMenuBar(SF.getRaJMenuBar());
    setTitle(initTitle.concat(" ("+SF.getTitle()+")"));
//    initHelpArea(SF);
    helpOptionChecked = currentModule.isHelpOptionChecked();
    validate();
  }
  public void closeModule() {
    setJMenuBar(defaultMenuBar);
//    initHelpArea();
    validate();
    currentModule = null;
    helpOptionChecked = getCurrentHelpOptionState();
  }

  private boolean getCurrentHelpOptionState() {
    return false; /**@todo nadji opciju pomocnih i odredi joj isSelected*/
  }


  public static String findAPPBundleSec(String sec) {
    String[][] rcontents = start.getResContents();
    for (int i=0;i<rcontents.length;i++) {
      if ((rcontents[i][0].startsWith("APL")) && (rcontents[i][1].equals(sec)))
         return rcontents[i][0].substring(3);
    }
    return null;
  }

  public raStatusBar getStatusBar() {
    return statusbar;
  }

//image
  public void setBackgroundImage(String filen) {
    setBackgroundImage(filen,true);
  }

  private void setBackgroundImage(String filen,boolean writeini) {
    try {
      imgBackground = new javax.swing.ImageIcon(filen);
      iBackOrg = imgBackground.getImage();
    } catch (Exception e) {
      System.out.println("Fail to set background image : "+e);
      return;
    }
    repaint();
    if (writeini) hr.restart.util.IntParam.UpisiUIni(filen,"backimage");
  }

  void setBackgroundImage() {
    String inival = IntParam.VratiSadrzajTaga("backimage");
    if (!inival.equals("")) setBackgroundImage(inival,false);
  }

  private void drawImage(java.awt.Graphics g,JComponent c) {
    if (imgBackground!=null&&g!=null&&mainTabs.getTabCount()==0) {
      g.drawImage(iBackOrg,0,0,c.getWidth(),c.getHeight(),c);
    }
  }
// end image
}
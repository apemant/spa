/****license*****************************************************************
**   file: raTabDialog.java
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

import hr.restart.swing.JraFrame;
import hr.restart.swing.JraScrollPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class raTabDialog extends JraFrame {
  String origTitle;
  JTabbedPane jTabs = new JTabbedPane();
  JList jIconList;
  JraScrollPane jpIcons;
  JraScrollPane jpCenter = new JraScrollPane();
  Vector TabList = new Vector();
  OKpanel okp = new OKpanel() {
    public void jPrekid_actionPerformed() {
      action_cancel();
    }
    public void jBOK_actionPerformed() {
      action_ok();
    }
  };
  public raTabDialog(String title) {
    super(title);
    origTitle = (title==null||title.equals(""))?"Alati":title;
    try {
      jInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jInit() throws Exception {
    //this.getContentPane().add(jTabs, BorderLayout.CENTER);
    this.getContentPane().add(okp,BorderLayout.SOUTH);
    this.getContentPane().add(jpCenter, BorderLayout.CENTER);
    okp.registerOKPanelKeys(this);
  }
  public void addFrameTab(hr.restart.util.okFrame tab) {
    addFrameTab(null, tab, raImages.IMGTOPEN);
  }
  public void addFrameTab(hr.restart.util.okFrame tab, String imgDescription) {
    addFrameTab(null, tab, imgDescription);
  }
  public void addFrameTab(String title, hr.restart.util.okFrame tab, String imgDescription) {
    addFrameTab(title, tab, raImages.getImageIcon(imgDescription));
  }
  public void addFrameTab(hr.restart.util.okFrame tab, ImageIcon ico) {
    addFrameTab(tab.getTitle(), tab, ico);
  }
  public void addFrameTab(String title, hr.restart.util.okFrame tab, ImageIcon ico) {
    if (tab.getOKpanel()!=null) tab.getOKpanel().setVisible(false);
    if (title == null) title = tab.getTitle();
    addTask(title,ico,tab.getContentPane());
    if (tab instanceof navFrame) {
      raNavBar nbar = ((navFrame)tab).getNavBar();
      if (nbar!=null) nbar.registerNavBarKeys(this);
    }
    TabList.addElement(tab);
  }
  
  private void addTask(String title, ImageIcon ic, Container cont) {
    int idx = tasklist.size();
    tasklist.add(idx, new raTabDialog.TabTask(title, ic, cont));
  }
  
  public void addAction(String title, String classToLoad) {
    addAction(title, raImages.getModuleIcom("sisfun"), classToLoad);
  }
  public void addAction(ActionTask task) {
    int idx = tasklist.size();
    tasklist.add(idx, task);
  }
  
  public void addAction(String title, ImageIcon ic, String classToLoad) {
    addAction(new raTabDialog.ActionTask(title, ic, classToLoad));
  }
  
  public void taskInit() {
    jIconList = new JList(tasklist.toArray());
    jIconList.setCellRenderer(new raTabDialog.MyCellRenderer());
    jpIcons = new JraScrollPane(jIconList,JraScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JraScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    getContentPane().add(jpIcons, BorderLayout.WEST);
    jIconList.addMouseListener(new java.awt.event.MouseAdapter() {
       public void mousePressed(java.awt.event.MouseEvent e) {
         Object source = tasklist.get(jIconList.getSelectedIndex());
         if (source instanceof ActionTask) {
           ActionTask at = (ActionTask)source;
           createBackground(at);
           at.doAction();
         } else if (source instanceof TabTask) {
           createTask((raTabDialog.TabTask)source);
         }
       }
     });
  }
  private TabTask selectedTask = null;
  private void createBackground(ActionTask at) {
    setTitle(origTitle+" - "+at.title);
    JLabel jl = new JLabel(at.title+" (u posebnom prozoru)", at.icon, SwingConstants.CENTER);
    jpCenter.setViewportView(jl);
    selectedTask = null;
  }
  private void createTask(TabTask task) {
    setTitle(origTitle+" - "+task.title);
    task.cont.setSize(jpCenter.getViewport().getSize());
    jpCenter.setViewportView(task.cont);
    selectedTask = task;
  }
  private ArrayList tasklist = new ArrayList();
  
  private void action_cancel() {
    hide();
  }
  public void action_ok() {
    applyAllTabs();
    applyMessage();
    if (hideOnOK) hide();
  }
  public OKpanel getOKPanel() {
    return okp;
  }
  private boolean hideOnOK = true;
  public void setHideOnOK(boolean b) {
    hideOnOK = b;
  }
  private boolean doSaving(okFrame okf) {
    try {
      return okf.doSaving();
    }
    catch (Exception ex) {
      return false;
    }
  }
  public okFrame getSelectedTabComponent() {
    if (selectedTask == null) return null;
System.out.println("getSelectedTabComponent.title = "+selectedTask.title);
    try {
      Object tabComp = TabList.get(jIconList.getSelectedIndex());
      if (tabComp instanceof okFrame) return (okFrame)tabComp;
      return null;
    } catch (Exception ex) {
      System.out.println("getSelectedTabComponent.ex "+ex);
      return null;
    }
  }
  public void applyAllTabs() {
    for (int i=0;i<TabList.size();i++) {
      Object tabComp = TabList.get(i);
      if (tabComp instanceof hr.restart.util.okFrame) {
        okFrame tabOKF = (okFrame)tabComp;
        if (doSaving(tabOKF)) {
//System.out.println("Saving "+tabComp.getClass() + " - " + tabOKF.getTitle());
          tabOKF.action_jBOK();
        } else {
//System.out.println("NOT saving "+tabComp.getClass() + " - " + tabOKF.getTitle());
        }
      }

    }
  }

  public void pack() {
    super.pack();
    /*
    int whsize = getSize().width>getSize().height ? getSize().width : getSize().height;
    whsize = whsize + 20;
    setSize(whsize,whsize);
    */
    //setSize(getSize().width+20,getSize().height+20);
    setSize(new Dimension(700,450));
  }

  void applyMessage() {
    if (applyMessageText == null) return;
    if (applyMessageText.equals("")) return;
    JOptionPane.showMessageDialog(null,applyMessageText,"Poruka",JOptionPane.INFORMATION_MESSAGE);
  }

  public void setApplyMessageText(String newApplyMessageText) {
    applyMessageText = newApplyMessageText;
  }
  public String getApplyMessageText() {
    return applyMessageText;
  }
  private String applyMessageText;
//
  class MyCellRenderer extends JLabel implements ListCellRenderer {
    IconTask itask;
    public Component getListCellRendererComponent(
       JList list,
       Object value,            // value to display
       int index,               // cell index
       boolean isSelected,      // is the cell selected
       boolean cellHasFocus)    // the list and the cell have the focus
     {
         String s = value.toString();
         if (value instanceof IconTask) {
           itask = (IconTask)value;
           s = itask.title;
           setIcon(itask.icon);
           setHorizontalTextPosition(SwingConstants.CENTER);
           setVerticalTextPosition(SwingConstants.BOTTOM);
           setHorizontalAlignment(SwingConstants.CENTER);
         }
         setText(s);
   	   if (isSelected) {
//             setBackground(list.getSelectionBackground());
//	     setForeground(list.getSelectionForeground());
             setBorder(BorderFactory.createLineBorder(Color.black));
	   }
         else {
//	       setBackground(list.getBackground());
//	       setForeground(list.getForeground());
               setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	   }
	   setEnabled(list.isEnabled());
	   setFont(list.getFont());
         return this;
     }
 }
//
  class IconTask {
    String title;
    ImageIcon icon;
    public IconTask(String _title, ImageIcon _icon) {
      title = _title;
      icon = _icon;
      icon.setImage(icon.getImage().getScaledInstance(48,-1,Image.SCALE_FAST));
    }
  }
  class TabTask extends IconTask {
    Container cont;
    public TabTask(String _title, javax.swing.ImageIcon _icon, Container _cont) {
      super(_title, _icon);
      cont = _cont;
    }
  }
  class ActionTask extends IconTask {
    String classToLoad;
    public ActionTask(String _title, javax.swing.ImageIcon _icon, String _classToLoad) {
      super(_title, _icon);
      classToLoad = _classToLoad;
    }
    public void doAction() {
      if (classToLoad != null) {
        raLLFrames.getRaLLFrames().getMsgStartFrame().showFrame(classToLoad, title);
      }
    }
  }
}
/****license*****************************************************************
**   file: mygeter.java
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
package hr.restart.robno;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraFrame;
import hr.restart.util.raImages;
import hr.restart.util.raLLFrames;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.borland.dx.sql.dataset.QueryDataSet;


public class mygeter extends JraFrame {

  private JPanel panelcont = new JPanel();
  hr.restart.util.raJPTableView rjp = new hr.restart.util.raJPTableView();
  Box jPControls = Box.createHorizontalBox();

  private hr.restart.util.OKpanel okp = new hr.restart.util.OKpanel(){
    public void jBOK_actionPerformed(){
      pressOK();
    }
    public void jPrekid_actionPerformed(){
      pressCancel();
    }
  };

  public void setEnabledChildFrames(boolean how) {
    if (hr.restart.start.isMainFrame()) {
      hr.restart.mainFrame.getMainFrame().setEnabled(how);
      return;
    }
    JComponent jc ;
    startFrame sf = raLLFrames.getRaLLFrames().getMsgStartFrame();
    sf.setEnabled(how);
    java.util.LinkedList list = raLLFrames.getRaLLFrames().getChildFrames(sf);
    for (int i = 0; i< list.size(); i++) {
      Object obj = list.get(i);
      try {
       if (obj instanceof java.awt.Component) ((java.awt.Component)obj).setEnabled(how);
      } catch (Exception e) {
        System.out.println("Greška "+e);
      }
    }
  }

  public mygeter(String naslov) {
    this.setTitle(naslov);
    this.setIconImage(raImages.getImageIcon(raImages.IMGSPLASH).getImage());
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        this_windowClosing(e);
      }
    });
    getContentPane().setLayout(new BorderLayout());
    panelcont.setLayout(new BorderLayout());
    panelcont.setPreferredSize(new Dimension(430,480));
    getContentPane().add(panelcont,BorderLayout.CENTER);
    panelcont.add(rjp,BorderLayout.CENTER);
    panelcont.add(okp,BorderLayout.SOUTH);
  }

  public void init(QueryDataSet ds, int[] cols) {
    rjp.setDataSet(ds);
    rjp.setVisibleCols(cols);
    rjp.initKeyListener(this);
    rjp.getColumnsBean().eventInit();
    ((hr.restart.swing.JraTable2)rjp.getMpTable()).fireTableDataChanged(); /// da nema praznih kolona
  }

  public void pressOK(){
    setEnabledChildFrames(true);
    this.hide();
  }

  public void pressCancel(){
    setEnabledChildFrames(true);
    this.hide();
  }

  public void addButton(String text,String icon_name) {
    JraButton jdodbutton = new JraButton();
    if (icon_name==null) icon_name="";
    if (icon_name.equals("")) icon_name = raImages.IMGALLDOWN;

    jdodbutton.setIcon(raImages.getImageIcon(icon_name));
    jdodbutton.setText(text);
    jdodbutton.setSize(51,27);
    jdodbutton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent e) {
       jdodbutton_actionPerformed(e);
      }
    });
    jPControls.add(jdodbutton);
    okp.add(jPControls,BorderLayout.WEST);
    okp.repaint();
  }

  public void jdodbutton_actionPerformed(java.awt.event.ActionEvent e) {}

  void this_windowClosing(WindowEvent e) {
    pressCancel();
  }
}
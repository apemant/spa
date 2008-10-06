/****license*****************************************************************
**   file: dlgFileViewer.java
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

import hr.restart.swing.JraDialog;
import hr.restart.swing.JraScrollPane;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class dlgFileViewer extends JraDialog implements okFrame, loadFrame {
  JPanel jp = new JPanel();
  JPanel jpButtons = new JPanel(new BorderLayout());
  JButton jbReloadNoAlone = new JButton("Osvježi");
  JButton jbStandAlone = new JButton("Novi ekran");
  BorderLayout borderLayout1 = new BorderLayout();
  JraScrollPane jtextScroll = new JraScrollPane();
  JTextArea jtextArea = new JTextArea();
  boolean standAlone = false;
  private String fileName;
  OKpanel okpanel = new OKpanel() {
    public void jPrekid_actionPerformed() {
      action_jPrekid();
    }
    public void jBOK_actionPerformed() {
      action_jBOK();
    }
  };
  protected dlgFileViewer(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      fileName = title;
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public dlgFileViewer(String fileN) {
    this(null, fileN, false);
  }
  public dlgFileViewer(String fileN,boolean modal) {
    this(null, fileN, modal);
  }
  public dlgFileViewer(String fileN,boolean modal, boolean _standAlone) {
    this(null, fileN, modal);
    setStandAlone(_standAlone);
  }

  void jbInit() throws Exception {
    jp.setLayout(borderLayout1);
    jtextArea.setEditable(false);
    getContentPane().add(jp);
    okpanel.jBOK.setText("Osvježi");
    okpanel.jBOK.setVisible(false);
    jbReloadNoAlone.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        reload();
      }
    });
    jbStandAlone.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        startFrame.showLog();
      }
    });
    jpButtons.add(jbReloadNoAlone, BorderLayout.EAST);
    jpButtons.add(jbStandAlone, BorderLayout.WEST);
    jp.add(jtextScroll, BorderLayout.CENTER);
    jp.add(okpanel,BorderLayout.SOUTH);
    jtextScroll.getViewport().add(jtextArea, null);
  }
  public void action_jPrekid() {
    if (standAlone) hide();
  }
  public void hide() {
//    Util.removeLogWriteListener(lwlistener);
    super.hide();
  }
  public void action_jBOK() {
    if (standAlone) reload();
  }

  public OKpanel getOKpanel() {
    return okpanel;
  }
  public boolean doSaving() {
    return false;
  }
  public void reload() {
    jtextArea.setText(FileHandler.readFile(fileName));
    scrollToVisible();
  }
  private void scrollToVisible() {
    jtextArea.scrollRectToVisible(new Rectangle(1,jtextArea.getSize().height,1,1));
  }
/*  raLogWriteListener lwlistener = new raLogWriteListener() {
    public void logWritten(String line) {
      reload();
      //jtextArea.setText(jtextArea.getText()+line);
      //scrollToVisible();
    }
  };*/

  private boolean isLogViewer() {
    return fileName.equals(Util.getLogFileName());
  }

  public void setStandAlone(boolean p) {
    standAlone = p;
    okpanel.jBOK.setVisible(standAlone);
    if (standAlone) {
//      if (isLogViewer()) Util.addLogWriteListener(lwlistener);
      jp.remove(jpButtons);
    } else {
      jp.add(jpButtons, BorderLayout.SOUTH);
//      Util.removeLogWriteListener(lwlistener);
    }
  }
}
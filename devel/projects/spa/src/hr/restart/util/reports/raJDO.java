/****license*****************************************************************
**   file: raJDO.java
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
package hr.restart.util.reports;

import hr.restart.swing.JraButton;
import hr.restart.util.OKpanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raJDO extends JFrame {
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlDS = new JLabel();
  JTextField jtDS = new JTextField();
  JLabel jlProject = new JLabel();
  JTextField jtProject = new JTextField();
  JLabel jlClass = new JLabel();
  JTextField jtClass = new JTextField();
  JLabel jlFileName = new JLabel();
  String pd = java.io.File.separator;
  char pdc = java.io.File.separatorChar;
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      go();
    }
    public void jPrekid_actionPerformed() {
      exit();
    }
  };
  JLabel jlDM = new JLabel();
  JTextField jtDM = new JTextField();
  JraButton jrbGetPath = new JraButton();

  public raJDO() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    jp.setLayout(xYLayout1);
    jlDS.setText("DataSet");
    jlProject.setText("Project path");
    jlClass.setText("Class");
    this.setTitle("JDOCreator");
    jlFileName.setText("File name: ");
    jtDS.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtDS_focusLost(e);
      }
    });
    jtProject.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtProject_focusLost(e);
      }
    });
    jtClass.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtClass_focusLost(e);
      }
    });
    jlDM.setText("DataModule");
    jtDM.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtDM_focusLost(e);
      }
    });
    jtDM.setText("hr.restart.baza.dM");
    jrbGetPath.setText("...");
    jrbGetPath.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jrbGetPath_actionPerformed(e);
      }
    });
    this.getContentPane().add(jp, BorderLayout.CENTER);
    jp.add(jlFileName, new XYConstraints(15, 120, -1, -1));
    jp.add(jlDS, new XYConstraints(15, 45, -1, -1));
    jp.add(jtDS, new XYConstraints(100, 45, 300, -1));
    jp.add(jlProject, new XYConstraints(15, 70, -1, -1));
    jp.add(jtProject, new XYConstraints(100, 70, 300, -1));
    jp.add(jlClass, new XYConstraints(15, 95, -1, -1));
    jp.add(jtClass, new XYConstraints(100, 95, 300, -1));
    jp.add(jlDM, new XYConstraints(15, 20, -1, -1));
    jp.add(jtDM, new XYConstraints(100, 20, 300, -1));
    jp.add(jrbGetPath, new XYConstraints(405, 70, 21, 21));
    this.getContentPane().add(okp, BorderLayout.SOUTH);
  }
  public void start() {
    pack();
    Dimension screenSize = hr.restart.start.getSCREENSIZE();
    Dimension frameSize = getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    setSize(frameSize.width+100,frameSize.height);
    try {
      String defpth = ClassLoader.getSystemResource("").getPath().replace('/',pdc);
      defpth = defpth.substring(1,defpth.lastIndexOf(pd));
      defpth = defpth.substring(0,defpth.lastIndexOf(pd));
      jtProject.setText(defpth);
    } catch (Exception e) {}
    show();
  }
  private void go() {
    JDOPrintCreator JDOPC = new JDOPrintCreator(
        jtDM.getText(),
        jtDS.getText(),
        jlFileName.getText().substring(12),
        jtClass.getText()
      );
  }
  public void exit() {
    hide();
  }
  public static void main(String[] args) {
    raJDO rajdo = new raJDO();
    rajdo.start();
  }

  void jtDS_focusLost(FocusEvent e) {
    if (jtClass.getText().equals("")) jtClass.setText("hr.restart.<...>.rep"+jtDS.getText());
    setJlFileName();
  }
  void setJlFileName() {
    jlFileName.setText("File name:  "+jtProject.getText()+"src"+pd+jtClass.getText().replace('.',pdc)+".java");
  }
  void jtProject_focusLost(FocusEvent e) {
    setJlFileName();
  }

  void jtClass_focusLost(FocusEvent e) {
    setJlFileName();
  }
  void jtDM_focusGained(FocusEvent e) {

  }
  void jtDM_focusLost(FocusEvent e) {

  }

  void jrbGetPath_actionPerformed(ActionEvent e) {
    javax.swing.JFileChooser fchooser = new javax.swing.JFileChooser(jtProject.getText());
    int chosen = fchooser.showOpenDialog(this);
    if (chosen == fchooser.APPROVE_OPTION) {
      jtProject.setText(fchooser.getSelectedFile().toString());
      jtProject.setText(jtProject.getText().substring(0,jtProject.getText().lastIndexOf(pd)));
    }
  }
}
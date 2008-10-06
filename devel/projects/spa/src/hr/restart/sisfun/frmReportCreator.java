/****license*****************************************************************
**   file: frmReportCreator.java
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
package hr.restart.sisfun;

import hr.restart.swing.JraButton;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraKeyListener;
import hr.restart.util.OKpanel;
import hr.restart.util.raFileFilter;
import hr.restart.util.raFrame;
import hr.restart.util.reports.raElixirProperties;
import hr.restart.util.reports.raElixirPropertiesInstance;
import hr.restart.util.reports.raReportCreator;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sg.com.elixir.ReportRuntime;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmReportCreator extends raFrame {
  raElixirProperties ep = raElixirPropertiesInstance.get();
  JPanel jPanel1 = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JTextField jtfTemplate = new JTextField();
  JLabel jlTemplate = new JLabel();
  JLabel jlOutput = new JLabel();
  JTextField jtfOutput = new JTextField();
  JLabel jlPackage = new JLabel();
  JTextField jtfPackage = new JTextField();
  JraButton jbTemplate = new JraButton();
  JraButton jbOutput = new JraButton();
  JraComboBox jcbParts = new JraComboBox(new Object[] {"Cijeli template", ep.REPORT_HEADER,
      ep.PAGE_HEADER, ep.SECTION_HEADER + 0, ep.SECTION_HEADER + 1, ep.SECTION_HEADER + 2,
      ep.SECTION_HEADER + 3, ep.SECTION_HEADER + 4, ep.DETAIL, ep.SECTION_FOOTER + 4,
      ep.SECTION_FOOTER + 3, ep.SECTION_FOOTER + 2, ep.SECTION_FOOTER + 1,
      ep.SECTION_FOOTER + 0, ep.PAGE_FOOTER, ep.REPORT_FOOTER});
  JLabel jlParts = new JLabel();
  JFileChooser jft = new JFileChooser();
  JFileChooser jfo = new JFileChooser();
  String lastpack = "";

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };


  public frmReportCreator() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokus() {
    jtfTemplate.setText("");
    jtfOutput.setText("");
    jtfTemplate.requestFocus();
  }

  private void jbInit() throws Exception {
    xYLayout1.setWidth(645);
    xYLayout1.setHeight(110);
    jPanel1.setLayout(xYLayout1);
    jlTemplate.setText("Izvorni template");
    jlOutput.setText("Ciljni java source");
    jlPackage.setText("Ime paketa");
    jbTemplate.setText("...");
    jbOutput.setText("...");
    jlParts.setText("Kreirati");
    jPanel1.add(jtfTemplate, new XYConstraints(150, 45, 450, -1));
    jPanel1.add(jlTemplate, new XYConstraints(15, 45, -1, -1));
    jPanel1.add(jlOutput, new XYConstraints(15, 70, -1, -1));
    jPanel1.add(jtfOutput, new XYConstraints(150, 70, 450, -1));
    jPanel1.add(jlPackage, new XYConstraints(15, 20, -1, -1));
    jPanel1.add(jtfPackage, new XYConstraints(150, 20, 100, -1));
    jPanel1.add(jbTemplate, new XYConstraints(605, 45, 21, 21));
    jPanel1.add(jbOutput, new XYConstraints(605, 70, 21, 21));
    jPanel1.add(jcbParts, new XYConstraints(450, 20, 150, -1));
    jPanel1.add(jlParts, new XYConstraints(360, 20, -1, -1));
    jtfOutput.addKeyListener(new JraKeyListener());
    jtfPackage.addKeyListener(new JraKeyListener());
    jtfPackage.setText("robno");
    jft.setFileFilter(new raFileFilter("Elixir template (*.template)"));
    jfo.setFileFilter(new raFileFilter("Java source (*.java)"));

//    new FileFilter();
//    jfo.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    setPaths();

    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(jPanel1, BorderLayout.CENTER);
    this.getContentPane().add(okp, BorderLayout.SOUTH);

    jbTemplate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        getTemp();
      }
    });
    jbOutput.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        getOut();
      }
    });
    jtfPackage.addFocusListener(new FocusAdapter() {
      public void focusGained(FocusEvent e) {
        lastpack = jtfPackage.getText();
      }

      public void focusLost(FocusEvent e) {
        if (!lastpack.equals(jtfPackage.getText())) {
          setPaths();
        }
      }
    });
    jtfTemplate.addKeyListener(new JraKeyListener() {
      public void keyPressed(KeyEvent e) {
        if (!e.isConsumed() && e.getKeyCode() == e.VK_F9) {
          getTemp();
        } else if (!e.isConsumed())
          super.keyPressed(e);
      }
    });
    jtfOutput.addKeyListener(new JraKeyListener() {
      public void keyPressed(KeyEvent e) {
        if (!e.isConsumed() && e.getKeyCode() == e.VK_F9) {
          getOut();
        } else if (!e.isConsumed())
          super.keyPressed(e);
      }
    });
  }

  private void setPaths() {
    String pck = jtfPackage.getText();
    try {
      String dir = ClassLoader.getSystemResource("hr/restart/" + pck + "/reports/").getFile();
      jft.setCurrentDirectory(new File(dir));
      jfo.setCurrentDirectory(new File(getSourceDir(dir, "/")));
    } catch (Exception e) {
    }
  }

  private String getSourceDir(String classDir, String sep) {
    int clp = classDir.indexOf("classes");
    return classDir.substring(0, clp) + "src" + sep + "hr" + sep + "restart" + sep +
      ((jcbParts.getSelectedIndex() == 0) ? jtfPackage.getText() : "util" + sep + "reports") + sep;
  }

  private String getJavaName(String orig) {
    if (jcbParts.getSelectedIndex() == 0)
      return orig.substring(orig.lastIndexOf("/") + 1, orig.lastIndexOf(".")) + "OrigTemplate.java";
    else
      return "ra" + ((String) jcbParts.getSelectedItem()).replace(' ', '_') +".java";
  }


  private void getTemp() {
    if (jft.showOpenDialog(jPanel1) == jft.APPROVE_OPTION) {
      String f = jft.getSelectedFile().getAbsolutePath();
      if (f.endsWith(".template")) {
        jtfTemplate.setText(f);
        try {
          jtfOutput.setText(getSourceDir(f, "/") + getJavaName(f));
          jfo.setCurrentDirectory(new File(getSourceDir(f, "/")));
          jfo.setSelectedFile(new File(jtfOutput.getText()));
        } catch (Exception e) { System.out.println("error");}
      }
    }
  }

  private void getOut() {
    if (jfo.showSaveDialog(jPanel1) == jft.APPROVE_OPTION) {
      String f = jfo.getSelectedFile().getAbsolutePath();
      if (f.endsWith(".java"))
        jtfOutput.setText(jfo.getSelectedFile().getAbsolutePath());
    }
  }

  private void packageChanged() {
    lastpack = jtfPackage.getText();
  }

  private void OKPress() {
    try {
      ReportRuntime rt = new ReportRuntime(new Frame());
      rt.setReportTemplate(jtfTemplate.getText());
      String sel = jcbParts.getSelectedIndex() == 0 ? "" : (String) jcbParts.getSelectedItem();
      new raReportCreator(rt.getReportTemplate(), jtfPackage.getText(), jtfOutput.getText(), sel);
      this.hide();
    } catch (Exception e) {
      e.printStackTrace();
      jtfTemplate.requestFocus();
      JOptionPane.showMessageDialog(jPanel1, "Greška!", "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void cancelPress() {
    hide();
  }

  public void show() {
    pack();
    super.show();
    SetFokus();
  }
}
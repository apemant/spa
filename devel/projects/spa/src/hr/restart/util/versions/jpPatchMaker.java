/****license*****************************************************************
**   file: jpPatchMaker.java
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
package hr.restart.util.versions;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.IntParam;
import hr.restart.util.OKpanel;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.loadFrame;
import hr.restart.util.okFrame;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Timestamp;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpPatchMaker extends JPanel implements okFrame, loadFrame {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  StorageDataSet pmset;
  dlgCVSChooser chooser;
  raPatchMaker patchMaker;
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      action_jBOK();
    }
    public void jPrekid_actionPerformed() {
      action_jPrekid();
    }
  };
  JPanel jpDetail = new JPanel();
  
  XYLayout lay = new XYLayout();
  JLabel jlClass = new JLabel();
  JLabel jlDatefrom = new JLabel();
  JLabel jlSource = new JLabel();
  JLabel jlJavac = new JLabel("Compiler");
  JLabel jlJar = new JLabel("Jar utility");
  JLabel jlEnc = new JLabel("Encoding");
  JLabel jlOutput = new JLabel();
  JraTextField jraClass = new JraTextField();
  JraTextField jraDatefrom = new JraTextField();
  JraTextField jraSource = new JraTextField();
  JraTextField jraJavac = new JraTextField();
  JraTextField jraJar = new JraTextField();
  JraTextField jraEnc = new JraTextField();
  JraButton jbMake = new JraButton();
  JraButton jbCChoose = new JraButton();
  JraButton jbCChooseAll = new JraButton();
  public jpPatchMaker() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public void BindComponents(DataSet ds) {
    jraClass.setDataSet(ds);
    jraDatefrom.setDataSet(ds);
    jraSource.setDataSet(ds);
    jraJavac.setDataSet(ds);
    jraJar.setDataSet(ds);
    jraEnc.setDataSet(ds);
  }
  
  private void jbInit() throws Exception {
    createDataSet();
    jpDetail.setLayout(lay);
    lay.setWidth(540);
    lay.setHeight(250);
    
    jlClass.setText("Class Path");
    jlDatefrom.setText("Date from");
    jlSource.setText("Source Path (CVS)");
    jraClass.setColumnName("CLASS");
    jraDatefrom.setColumnName("DATEFROM");
    jraSource.setColumnName("SOURCE");
    jraJavac.setColumnName("JAVAC");
    jraJar.setColumnName("JAR");
    jraEnc.setColumnName("ENC");
    jbMake.setText("MAKE PATCH!");
    jbMake.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        makePatch();
      }
    });
    jbCChoose.setText("<- files from date");
    jbCChoose.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        chooseFiles();
      }
    });
    
    jbCChooseAll.setText("All files");
    jbCChooseAll.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        chooseAllFiles();
      }
    });

    jlOutput.setFont(jlOutput.getFont().deriveFont(Font.BOLD, jlOutput.getFont().getSize()+2));
    jpDetail.add(jlClass, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlDatefrom, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlSource, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlJavac, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jlJar, new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jlEnc, new XYConstraints(15, 145, -1, -1));
    jpDetail.add(jraClass, new XYConstraints(150, 45, 350, -1));
    jpDetail.add(getGetButton("CLASS"), new XYConstraints(505, 45, 21, 21));
    jpDetail.add(jraDatefrom, new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jbCChoose, new XYConstraints(255, 70, 150, 21));
    jpDetail.add(jbCChooseAll, new XYConstraints(410, 70, 90, 21));
    jpDetail.add(jraSource, new XYConstraints(150, 20, 350, -1));
    jpDetail.add(getGetButton("SOURCE"), new XYConstraints(505, 20, 21, 21));
    jpDetail.add(jraJavac, new XYConstraints(150, 95, 350, -1));
    jpDetail.add(jraJar, new XYConstraints(150, 120, 350, -1));
    jpDetail.add(jraEnc, new XYConstraints(150, 145, 150, -1));
    jpDetail.add(getGetButton("JAVAC"), new XYConstraints(505, 95, 21, 21));
    jpDetail.add(getGetButton("JAR"), new XYConstraints(505, 120, 21, 21));
    jpDetail.add(jbMake, new XYConstraints(15, 170, 515, -1));
    jpDetail.add(jlOutput, new XYConstraints(15, 215, 515, -1));
    BindComponents(pmset);
    this.add(jpDetail, BorderLayout.CENTER);
  }
  
  private JraButton getGetButton(final String colname) {
    JraButton b = new JraButton();
    b.setText("...");
    b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        getDir(colname);
      }
    });
    return b;
  }
  public void action_jBOK() {
    IntParam.setTag("patchmaker.source", pmset.getString("SOURCE"));
    IntParam.setTag("patchmaker.class", pmset.getString("CLASS"));
    IntParam.setTag("patchmaker.javac", pmset.getString("JAVAC"));
    IntParam.setTag("patchmaker.jar", pmset.getString("JAR"));
    IntParam.setTag("patchmaker.cvscommand", pmset.getString("CVSCOMM"));
  }
  
  public void action_jPrekid() {
  }
  
  public boolean doSaving() {
    if (!pmset.getString("SOURCE").equals(IntParam.getTag("patchmaker.source"))) return true;
    if (!pmset.getString("CLASS").equals(IntParam.getTag("patchmaker.class"))) return true;
    if (!pmset.getString("JAVAC").equals(IntParam.getTag("patchmaker.javac"))) return true;
    if (!pmset.getString("JAR").equals(IntParam.getTag("patchmaker.jar"))) return true;
    if (!pmset.getString("CVSCOMM").equals(IntParam.getTag("patchmaker.cvscommand"))) return true;
    return false;
  }
  
  public java.awt.Container getContentPane() {
    return this;
  }
  
  public OKpanel getOKpanel() {
    return okp;
  }
  
  public String getTitle() {
    return "PatchMaker";
  }
  
  void createDataSet() {
    pmset = new StorageDataSet();
    pmset.addColumn("SOURCE",Variant.STRING);
    pmset.addColumn("CLASS",Variant.STRING);
    pmset.addColumn(dm.createTimestampColumn("DATEFROM"));
    pmset.addColumn("JAVAC",Variant.STRING);
    pmset.addColumn("JAR",Variant.STRING);
    pmset.addColumn("CVSCOMM",Variant.STRING);
    pmset.addColumn("ENC",Variant.STRING);
    pmset.open();
  }
  
  public void reload() {
    pmset.setString("SOURCE", IntParam.getTag("patchmaker.source"));
    pmset.setString("CLASS", IntParam.getTag("patchmaker.class"));
    pmset.setString("JAVAC", IntParam.getTag("patchmaker.javac"));
    pmset.setString("JAR", IntParam.getTag("patchmaker.jar"));
    pmset.setString("CVSCOMM", getCVSCOMMAND());
    pmset.setTimestamp("DATEFROM", new Timestamp(System.currentTimeMillis()));
    pmset.setString("ENC", "Cp1250");
  }
  private String getCVSCOMMAND() {
    String ret = IntParam.getTag("patchmaker.cvscommand");
    if (ret.equals("")) {
      ret = "cvs -d :pserver:andrej:andrej@161.53.200.99:/sda1/cvsroot";
      IntParam.setTag("patchmaker.cvscommand",ret);
    }
    return ret;
  }
  private void createPatchMaker() {
    createPatchMaker(pmset.getTimestamp("DATEFROM"));
  }
  private void createPatchMaker(Timestamp from) {
//    if (patchMaker == null) { 
      patchMaker = new raPatchMaker(pmset.getString("SOURCE"), pmset.getString("CLASS"), 
        getFSDay(from),null, pmset.getString("ENC"));
      System.out.println("Creating patchmaker with encoding "+pmset.getString("ENC"));
//    }
  }
  private Timestamp getFSDay(Timestamp source) {
    if (source == null) return null;
    return Util.getUtil().getFirstSecondOfDay(source);
  }
  private void makePatch() {
    if (vl.isEmpty(jraDatefrom)) return;
    if (vl.isEmpty(jraSource)) return;
    if (vl.isEmpty(jraJavac)) return;
    if (vl.isEmpty(jraJar)) return;
    //createPatchMaker();
    patchMaker.setJar(pmset.getString("JAR"));
    patchMaker.setJavac(pmset.getString("JAVAC"));
    patchMaker.setOutputComp(jlOutput);
    File patchfile = patchMaker.make();
    if (patchfile.exists()) {
      File choosenDir = chooseDir(patchfile.getPath(), false);//chooseDir("", true);
      if (choosenDir != null) {
        //File dest = new File(choosenDir.getAbsolutePath()+File.separator+patchfile.getName());
        File dest;
        if (choosenDir.isDirectory()) {
          dest = new File(choosenDir.getAbsolutePath()+File.separator+patchfile.getName());
        } else {
          dest = choosenDir;
        }
        if (patchfile.renameTo(dest)) {
          JOptionPane.showMessageDialog(this, "Patch "+dest.getAbsolutePath()+" napravljen!");
        } else {
          JOptionPane.showMessageDialog(this, "Neuspjesno kreiranje patch-a "+dest.getAbsolutePath()+"!");
        }
      }
    }
    patchMaker.clean();
  }
  
  private void chooseFiles() {
    patchMaker = null;
    chooser = null;
    createPatchMaker();
    if (chooser == null) chooser = new dlgCVSChooser(patchMaker, this);
    chooser.pack();
    chooser.show();
  }
/*Sa ovime odabire  sve fileove sa cvs-a*/
  private void chooseAllFiles() {
    patchMaker = null;
    chooser = null;
    createPatchMaker(null);
    if (chooser == null) chooser = new dlgCVSChooser(patchMaker, this);
    chooser.pack();
    chooser.show();
  }
  
  private void getDir(String cname) {
    File chosen = chooseDir(pmset.getString(cname), cname.equals("SOURCE"));
    if (chosen!=null) {
      pmset.setString(cname, chosen.getAbsolutePath());
    }
  }
  
  public File chooseDir(String initialpath, boolean dironly) {
    JFileChooser chooser = new JFileChooser(initialpath);
    if (dironly) chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    else chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    File sf = new File(initialpath);
    if (sf.isFile()) chooser.setSelectedFile(sf);
    int ret = chooser.showOpenDialog(this);
    if (ret == JFileChooser.APPROVE_OPTION) {
      return chooser.getSelectedFile();
    }
    return null;
  }
}

/****license*****************************************************************
**   file: frmTemplateGenerator.java
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

import hr.restart.util.OKpanel;
import hr.restart.util.VarStr;
import hr.restart.util.raFrame;
import hr.restart.util.reports.raReportTemplate;

import java.awt.BorderLayout;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Enumeration;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sg.com.elixir.reportwriter.xml.IModel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;



public class frmTemplateGenerator extends raFrame {
  String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                  "<!DOCTYPE Model [\n"+
                  "<!ELEMENT Model (Property*,HiddenProperty*,Model*)>\n"+
                  "<!ATTLIST Model Name CDATA #REQUIRED>\n"+
                  "<!ELEMENT Property (#PCDATA)>\n"+
                  "<!ATTLIST Property Name CDATA #REQUIRED>\n"+
                  "<!ELEMENT HiddenProperty (#PCDATA)>\n"+
                  "<!ATTLIST HiddenProperty Name CDATA #REQUIRED>\n"+
                  "]>\n";

  JPanel jpMain = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlKlasa = new JLabel();
  JTextField jtfKlasa = new JTextField();

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };

  public frmTemplateGenerator() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {


    jlKlasa.setText("Template klasa");
    xYLayout1.setWidth(465);
    xYLayout1.setHeight(50);
    jpMain.setLayout(xYLayout1);
    jpMain.add(jlKlasa, new XYConstraints(15, 20, -1, -1));
    jpMain.add(jtfKlasa, new XYConstraints(150, 15, 300, -1));
    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(jpMain, BorderLayout.CENTER);
    this.getContentPane().add(okp, BorderLayout.SOUTH);
  }

  private void cancelPress() {
    hide();
  }

  private void OKPress() {
    String klasa = jtfKlasa.getText();
    if (!klasa.startsWith("hr"))
      klasa = "hr.restart.robno." + klasa;
    raReportTemplate rt = null;
    try {
      rt = (raReportTemplate) Class.forName(klasa).newInstance();
      rt.setReportProperties();
    } catch (Exception e) {}
    if (rt == null)
      JOptionPane.showMessageDialog(jtfKlasa, "Ne postoji java template object!",
         "Greška", JOptionPane.ERROR_MESSAGE);
    else {
      klasa = klasa.replace('.', '/');
      String f = ClassLoader.getSystemResource(klasa + ".class").getFile();
      String k = rt.getClass().getName();
      String o = f.substring(0, f.lastIndexOf("/")) + "/reports/" + k.substring(k.lastIndexOf(".") + 1, k.length() - 8) + ".template";

      saveXML(o, rt.getUnmodifiedReportTemplate());
      hide();
    }
  }

  String template = "                                                         "+
    "                                                                         ";
  private String spc(int n) {
    return template.substring(0, n);
  }

  private String getValue(IModel m, String name) {
    return new VarStr(m.getPropertyValue(name))
        .replaceAll("<", "&lt;").replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;").replaceAll("'", "&apos;").toString();
  }

  private void writeModelSec(OutputStreamWriter f, int spacing, IModel m) throws IOException {
    outString(f, spc(spacing) + "<Model Name=\""+m.getPropertyValue("Name")+"\">");
    IModel c;
    String name;
    Enumeration e;

    e = m.getPropertyNames();
    while (e.hasMoreElements()) {
      name = (String) e.nextElement();
      outString(f, spc(spacing + 5) + "<Property Name=\"" + name + "\">"
                           + getValue(m, name) + "</Property>");
    }
    for (int i = 0; i < 10; i++)
      writeModel(f, spacing + 5, m.getModel("Section" + i));
    outString(f, spc(spacing) + "</Model>");
  }

  private void writeModel(OutputStreamWriter f, int spacing, IModel m) throws IOException {
    outString(f, spc(spacing) + "<Model Name=\""+m.getPropertyValue("Name")+"\">");
    IModel c;
    String name;
    Enumeration e;
    e = m.getPropertyNames();
    while (e.hasMoreElements()) {
      name = (String) e.nextElement();
      outString(f, spc(spacing + 5) + "<Property Name=\"" + name + "\">"
                           + getValue(m, name) + "</Property>");
    }
    e = m.getModels();
    while (e.hasMoreElements())
      writeModel(f, spacing + 5, (IModel) e.nextElement());
    outString(f, spc(spacing) + "</Model>");
  }

  private void writeReport(OutputStreamWriter f, IModel m) throws IOException {
    String name;
    Enumeration e;
    outString(f, "<Model Name=\"Report Template\">");
    e = m.getPropertyNames();
    while (e.hasMoreElements()) {
      name = (String) e.nextElement();
      outString(f, spc(5) + "<Property Name=\"" + name + "\">"
                           + getValue(m, name) + "</Property>");
    }
    e = m.getHiddenPropertyNames();
    while (e.hasMoreElements()) {
      name = (String) e.nextElement();
      outString(f, spc(5) + "<HiddenProperty Name=\"" + name + "\">"
                           + m.getHiddenPropertyValue(name) + "</HiddenProperty>");
    }
    writeModel(f, 5, m.getModel("Parameters"));
    writeModel(f, 5, m.getModel("Page Setup"));
    writeModelSec(f, 5, m.getModel("Sections"));
    writeModel(f, 5, m.getModel("Report Header"));
    writeModel(f, 5, m.getModel("Page Header"));
    for (int i = 0; i < 10; i++)
      writeModel(f, 5, m.getModel("Section Header" + i));
    writeModel(f, 5, m.getModel("Detail"));
    for (int i = 9; i >= 0; i--)
      writeModel(f, 5, m.getModel("Section Footer" + i));
    writeModel(f, 5, m.getModel("Page Footer"));
    writeModel(f, 5, m.getModel("Report Footer"));

    outString(f, "</Model>");
  }

  private void outString(OutputStreamWriter f, String s) throws IOException {
//    f.write(s.getBytes());
//    f.write(13);
//    f.write(10);
    f.write(s);
    f.write("\r\n");
  }

  private void saveXML(String fname, IModel m) {
    OutputStreamWriter f;
//    System.out.println(System.getProperty("line.separator"));
//    System.setProperty("line.separator", "\012");
    try {
      f = new OutputStreamWriter(new FileOutputStream(fname), "UTF-8");
    } catch (Exception e) {
      return;
    }
    try {
      outString(f, header);
      writeReport(f, m);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      f.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}

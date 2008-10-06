/****license*****************************************************************
**   file: UISettingsDialog.java
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

import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraDialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.Border;

public class UISettingsDialog extends JraDialog implements okFrame, loadFrame {
  JPanel jp = new JPanel();
  GridLayout grid = new GridLayout();
  radioPanel rpMainTabs = new radioPanel("Pozicija glavnih tabova","maintabspos");
  radioPanel rpTabDetTabs = new radioPanel("Pozicija ostalih tabova","tabdetpos");
  JPanel jpSuper = new JPanel();
  GridLayout gridSup = new GridLayout();
  radioPanel2 rpRaMat = new radioPanel2("  Mati\u010Dni podaci","ramatversion");
  radioPanel2 rpMaster = new radioPanel2("  Zaglavlja","masterversion");
  radioPanel2 rpDetail = new radioPanel2("  Stavke","detailversion");
  JraComboBox fonts = new JraComboBox();
  JPanel jpChecks = new JPanel();
  //GridLayout gridCheck = new GridLayout();
  //JraCheckBox jcbResizableLayout = new JraCheckBox();
  //JraCheckBox jcbText_antialiasing = new JraCheckBox();
  ModeDialog modeDialog = new ModeDialog(null, "", false);
  OKpanel okp = new OKpanel() {
    public void jPrekid_actionPerformed() {
      action_jPrekid();
    }
    public void jBOK_actionPerformed() {
      action_jBOK();
    }
  };

  public UISettingsDialog() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    setTitle("Parametri izgleda");
    getContentPane().setLayout(new BorderLayout());

/*    jcbResizableLayout.setText("  Rastezljive ekranske komponente");
    jcbResizableLayout.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbText_antialiasing.setText("  Tekst antialiasing");
    jcbText_antialiasing.setHorizontalTextPosition(SwingConstants.LEFT);
    gridCheck.setColumns(1);
    gridCheck.setRows(2);
*/  
    initFonts();
    fonts.setSelectedItem(getParamFont());
    jpChecks.setLayout(new BoxLayout(jpChecks, BoxLayout.X_AXIS));
  jpChecks.add(new JLabel("Font"));
  jpChecks.add(Box.createHorizontalStrut(25));
  jpChecks.add(fonts);
  jpChecks.add(Box.createHorizontalGlue());
  jpChecks.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
/*    jpChecks.add(jcbResizableLayout);
    jpChecks.add(jcbText_antialiasing);
*/
    grid.setColumns(2);
    grid.setRows(1);
    jp.setLayout(grid);
    jp.add(rpMainTabs);
    jp.add(rpTabDetTabs);

    gridSup.setColumns(1);
    gridSup.setRows(4);
    jpSuper.setLayout(gridSup);
    jpSuper.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Izgled ekrana"));
//jpMode - panel za nacin rada
    modeDialog.jlist.setBorder(BorderFactory.createLoweredBevelBorder());
    JPanel jpMode = new JPanel(new BorderLayout());
    JLabel jlNacin = new JLabel("Naèin rada");
    jlNacin.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));
    jpMode.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));
    jpMode.add(jlNacin,BorderLayout.WEST);
    jpMode.add(modeDialog.jlist, BorderLayout.CENTER);

//
    jpSuper.add(jpMode);
    jpSuper.add(rpRaMat);
    jpSuper.add(rpMaster);
    jpSuper.add(rpDetail);
    getContentPane().add(jp,BorderLayout.CENTER);
    getContentPane().add(jpSuper,BorderLayout.NORTH);
//    getContentPane().add(jcbResizableLayout,BorderLayout.SOUTH);
    getContentPane().add(jpChecks,BorderLayout.SOUTH);
  }

  public boolean doSaving() {
    System.out.println("modeDialog.doSaving() = "+modeDialog.doSaving());
    boolean ret = (modeDialog.doSaving()||!(
        IntParam.getTag("maintabspos").equals(rpMainTabs.getSelectedValue())&&
        IntParam.getTag("tabdetpos").equals(rpTabDetTabs.getSelectedValue())&&
        IntParam.getTag("ramatversion").equals(rpRaMat.getSelectedValue())&&
        IntParam.getTag("masterversion").equals(rpMaster.getSelectedValue())&&
        IntParam.getTag("detailversion").equals(rpDetail.getSelectedValue()) &&
        getParamFont().equals(fonts.getSelectedItem())
        /*hr.restart.start.isRESIZABLELAYOUT() == jcbResizableLayout.isSelected()&&
        IntParam.getTag("text_antialiasing").equals("true") == jcbText_antialiasing.isSelected()*/));
    System.out.println("UISettingsDialog.doSaving() = "+ret);
    return ret;
  }

  public void action_jPrekid() {
//  hide();
  }

  public void action_jBOK() {
    rpMainTabs.save();
    rpTabDetTabs.save();
    rpRaMat.save();
    rpDetail.save();
    rpMaster.save();
    saveFont();
    /*IntParam.setTag("resizablelayout",new Boolean(jcbResizableLayout.isSelected()).toString());
    IntParam.setTag("text_antialiasing",new Boolean(jcbText_antialiasing.isSelected()).toString());*/
    modeDialog.action_jBOK();
  }

  public OKpanel getOKpanel() {
    return okp;
  }

  public void reload() {
    rpMainTabs.setSelectedValue();
    rpTabDetTabs.setSelectedValue();
    rpRaMat.setSelectedValue();
    rpMaster.setSelectedValue();
    rpDetail.setSelectedValue();
    fonts.setSelectedItem(getParamFont());
    //jcbResizableLayout.setSelected(hr.restart.start.isRESIZABLELAYOUT());
    //jcbText_antialiasing.setSelected(new Boolean(IntParam.getTag("text_antialiasing")).booleanValue());
    modeDialog.reload();
  }
  
  List avail;
  void initFonts() {
    Set fams = new HashSet(Arrays.asList(GraphicsEnvironment.
                    getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
    String[] opts = {"-1 Dialog", "Dialog", "+1 Dialog", "+2 Dialog",
            "Arial", "+1 Arial", "+3 Arial", "-1 Verdana", "Verdana", "+1 Verdana",
            "Tahoma", "+1 Tahoma", "+2 Tahoma", "+4 Franklin Gothic Medium"};
    avail = new ArrayList(Arrays.asList(opts));
    for (Iterator i = avail.iterator(); i.hasNext(); ) {
      String opt = (String) i.next();
      opt = opt.substring(opt.indexOf(' ') + 1);
      if (!fams.contains(opt)) i.remove();
    }
    avail.add(0, "Default");
    
    fonts.setModel(new DefaultComboBoxModel(avail.toArray()));
    fonts.setSelectedItem("Default");
    fonts.setPreferredSize(new Dimension(200, new JTextField().getPreferredSize().height));

    fonts.setRenderer(new FontListCellRenderer());
  }
  
  String getParamFont() {
    String fn = IntParam.getTag("font.family");
    int delta = Aus.getNumber(IntParam.getTag("font.delta"));
    if (delta < 0) fn = delta + " " + fn;
    if (delta > 0) fn = "+" + delta + " " + fn;
    if (avail.contains(fn))
      return fn;
    return "Default";
  }
  
  void saveFont() {
    String fn = (String) fonts.getSelectedItem();
    if (fn == null || fn.equals("Default")) return;
    
    int delta = 0;
    if (fn.startsWith("+") || fn.startsWith("-")) {
      delta = Aus.getAnyNumber(fn);
      fn = fn.substring(fn.indexOf(' ') + 1);
    }
    IntParam.setTag("font.delta", Integer.toString(delta));
    IntParam.setTag("font.family", fn);
  }
  
  class FontListCellRenderer extends DefaultListCellRenderer {
    Border emptyBorder = BorderFactory.createEmptyBorder(1, 6, 1, 1);
    Font deff = new JLabel().getFont();
    public Component getListCellRendererComponent(JList l, Object v, int idx, boolean sel, boolean foc) {
      super.getListCellRendererComponent(l, v, idx, sel && (idx >= 0), false);
      if (idx > 0) {
        String fn = v.toString();
        int size = 11;
        if (fn.startsWith("+") || fn.startsWith("-")) {
          size = size + Aus.getAnyNumber(fn);
          fn = fn.substring(fn.indexOf(' ') + 1);
        }
        setFont(Font.decode(fn + "-" + size));
      } else setFont(deff);
      setBorder(emptyBorder);
      return this;
    }  
  }

abstract class abstractRadioPanel extends JPanel {
  ButtonGroup  jbgroup = new ButtonGroup();
  String titl = "";
  String tag = "";
  public void setSelectedValue() {
    setSelectedValue(hr.restart.util.IntParam.getTag(tag));
  }

  public void setSelectedValue(String val) {
    if (val != null) {
      java.util.Enumeration rbutts = jbgroup.getElements();
      while (rbutts.hasMoreElements()) {
        JRadioButton rbutt = (JRadioButton)rbutts.nextElement();
        if (rbutt.getActionCommand().equals(val)) {
          rbutt.setSelected(true);
          return;
        }
      }
    }
    defaultSelected();
  }
  public abstract void defaultSelected();
  public String getSelectedValue() {
    return jbgroup.getSelection().getActionCommand();
  }

  public void save() {
    IntParam.setTag(tag,getSelectedValue());
  }
}

class radioPanel extends abstractRadioPanel {
  JRadioButton jrbUp = new JRadioButton();
  JRadioButton jrbDown = new JRadioButton();
  JRadioButton jrbRight = new JRadioButton();
  JRadioButton jrbLeft = new JRadioButton();
  public radioPanel(String ttle,String ttag) {
    try {
      titl = ttle;
      tag = ttag;
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    this.setLayout(new BorderLayout());
    this.setBorder(new javax.swing.border.TitledBorder(BorderFactory.createEtchedBorder(),titl));
    this.setPreferredSize(new Dimension(150, 70));
    jrbDown.setText("Dolje");
    jrbDown.setHorizontalAlignment(SwingConstants.CENTER);
    jrbDown.setActionCommand("DOWN");
    jrbRight.setHorizontalAlignment(SwingConstants.CENTER);
    jrbRight.setText("Desno");
    jrbRight.setActionCommand("RIGHT");
    jrbLeft.setText("Lijevo");
    jrbLeft.setHorizontalAlignment(SwingConstants.CENTER);
    jrbLeft.setActionCommand("LEFT");
    jrbUp.setHorizontalAlignment(SwingConstants.CENTER);
    jrbUp.setText("Gore");
    jrbUp.setActionCommand("UP");
    jbgroup.add(jrbUp);
    jbgroup.add(jrbLeft);
    jbgroup.add(jrbRight);
    jbgroup.add(jrbDown);
    this.add(jrbUp,  BorderLayout.NORTH);
    this.add(jrbDown,  BorderLayout.SOUTH);
    this.add(jrbRight,  BorderLayout.EAST);
    this.add(jrbLeft, BorderLayout.WEST);
  }
  public void defaultSelected() {
    jrbUp.setSelected(true);
  }
}

class radioPanel2 extends abstractRadioPanel {
  JLabel jlTitle = new JLabel();
  GridLayout gridLay = new GridLayout();
  JRadioButton jrbSuper2 = new JRadioButton();
  JRadioButton jrbSuper1 = new JRadioButton();
  public radioPanel2(String ttle,String ttag) {
    try {
      titl = ttle;
      tag = ttag;
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  void jbInit() throws Exception {
    jlTitle.setText(titl);
    this.setLayout(gridLay);
    gridLay.setColumns(3);
    jrbSuper2.setText("Tablica i unos");
    jrbSuper1.setText("Samo tablica");
    jrbSuper1.setActionCommand("1");
    jrbSuper2.setActionCommand("2");
    this.add(jlTitle, null);
    this.add(jrbSuper2, null);
    this.add(jrbSuper1, null);
    jbgroup.add(jrbSuper2);
    jbgroup.add(jrbSuper1);
  }
  public void defaultSelected() {
    jrbSuper1.setSelected(true);
  }
}
}
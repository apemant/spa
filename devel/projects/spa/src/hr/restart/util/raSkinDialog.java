/****license*****************************************************************
**   file: raSkinDialog.java
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

import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class raSkinDialog extends raListDialog implements loadFrame {
  private JPanel jpButtons = new JPanel();
  private String[] skinData;
  private JraButton jbOptions = new JraButton();
  private JraButton jbTest = new JraButton();
  private JraButton jbSave = new JraButton();
  private dlgLafOptions dlgLafOpt;
  private int bootSelectedIndex;
  private boolean options_entered;
  private boolean loadingInProgress = false;
  private raCBrowser cbrw;
  public raSkinDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    init();
  }
  private void init() {
    jpButtons.setLayout(new GridLayout(1,3));

    jbOptions.setText("Opcije");
    jbOptions.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbOptions_click();
      }
    });
    jbTest.setText("Test");
    jbTest.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbTest_click();
      }
    });
    jbSave.setText("Snimi");
    jbSave.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbSave_click();
      }
    });


    jpButtons.add(jbOptions);
    jpButtons.add(jbTest);
    jpButtons.add(jbSave);
    getContentPane().add(jpButtons,BorderLayout.SOUTH);
    setListData(new String[] {"Tražim definicije izgleda ..."});
  }
  public boolean doSaving() {
    if (loadingInProgress) return false;
    if (bootSelectedIndex != getListSelectedIndex() && !options_entered) {
      IntParam.setTag("additionalLaF","false");
    }
    return bootSelectedIndex != getListSelectedIndex();
  }
  public void reload() {
    options_entered = false;
    loadData();
  }
  private void loadData() {
    if (skinData == null) {
      loadingInProgress = true;
      cbrw = new raCBrowser(javax.swing.LookAndFeel.class,null,null,new String[] {"plaf"},new String[] {"Basic","Multi"});
      skinData = new String[cbrw.getClassNames().length];
      for (int i = 0; i < skinData.length; i++) {
        skinData[i] = cbrw.getClassNames()[i].substring(cbrw.getClassNames()[i].lastIndexOf(".")+1);
      }
      setListData(skinData);
      loadingInProgress = false;
    }
    bootSelectedIndex = Util.getUtil().indexOfArray(cbrw.getClassNames(),IntParam.getTag("lookandfeel"));
    System.out.println("bootSelectedIndex = "+bootSelectedIndex+" ("+IntParam.getTag("lookandfeel")+")");
    setListSelectedIndex(bootSelectedIndex);
  }
  public void action_jBOK() {
    IntParam.setTag("lookandfeel",cbrw.getClassNames()[getListSelectedIndex()]);
  }

  private void jbOptions_click() {
    if (jbOptions.getTopLevelAncestor() instanceof Dialog) {
      dlgLafOpt = new dlgLafOptions((Dialog)jbOptions.getTopLevelAncestor());
    } else {
      dlgLafOpt = new dlgLafOptions((Frame)jbOptions.getTopLevelAncestor());
    }
    options_entered = true;
    dlgLafOpt.reload();
    dlgLafOpt.pack();
    //dlgLafOpt.setLocation(jbOptions.getLocationOnScreen());
    startFrame.getStartFrame().centerFrame(dlgLafOpt,0,dlgLafOpt.getTitle());
    dlgLafOpt.show();
  }

  private void jbTest_click() {
    action_jBOK();
    if (makeLookAndFeel()) {
      JOptionPane.showMessageDialog(this,getTestPanel(),"L&F Test",JOptionPane.PLAIN_MESSAGE);
    } else {
      JOptionPane.showMessageDialog(this,"Postavljanje izgleda neuspješno!","Greška!",JOptionPane.ERROR_MESSAGE);
      IntParam.setTag("lookandfeel",cbrw.getClassNames()[bootSelectedIndex]);
    }
  }

  private void jbSave_click() {
    if (!makeLookAndFeel()) {
      JOptionPane.showMessageDialog(this,"Izgled nije ispravno definiran!","Greška!",JOptionPane.ERROR_MESSAGE);
      return;
    }
    String menuName = JOptionPane.showInputDialog(this, "Naziv menu opcije", "Snimi kao menu opciju", JOptionPane.PLAIN_MESSAGE);
    Properties themeprops = FileHandler.getProperties("themes.properties");
    String value = IntParam.getTag("lookandfeel");
    if (new Boolean(IntParam.getTag("additionalLaF")).booleanValue()) {
      Properties lafprops = FileHandler.getProperties("laf.properties");
      value = value+"#true#"+lafprops.getProperty("SetClass")+"#"+lafprops.getProperty("Theme");
    } else value = value+"#false";
    themeprops.put(menuName, value);
    FileHandler.storeProperties("themes.properties",themeprops);
  }

  public static JMenu getThemeMenu() {
    Properties themeprops = FileHandler.getProperties("themes.properties");
    if (themeprops.size() == 0) return null;
    JMenu jtm = new JMenu("Teme izgleda");
    for (Iterator i = themeprops.keySet().iterator(); i.hasNext(); ) {
      final String mname = i.next().toString();
      JMenuItem mitem = new JMenuItem(mname);
      mitem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          applyTheme(mname);
        }
      });
      jtm.add(mitem);
    }
    return jtm;
  }

  private static void applyTheme(String menuName) {
    //System.out.println("menuname je "+menuName);
    StringTokenizer themeProps = new StringTokenizer(FileHandler.getProperties("themes.properties").getProperty(menuName),"#");
    IntParam.setTag("lookandfeel",themeProps.nextToken());
    IntParam.setTag("additionalLaF",themeProps.nextToken());
    if (new Boolean(IntParam.getTag("additionalLaF")).booleanValue()) {
      Properties props = FileHandler.getProperties("laf.properties");
      props.setProperty("SetClass", themeProps.nextToken());
      props.setProperty("Theme", themeProps.nextToken());
      FileHandler.storeProperties("laf.properties", props);
    }
    if (makeLookAndFeel()) {
      applyNewLookAndFeel();
      JOptionPane.showMessageDialog(null,"Tema ce biti u potpunosti primjenjena nakon restartanja aplikacije","Tema",JOptionPane.INFORMATION_MESSAGE);
    } else {
      JOptionPane.showMessageDialog(null,"Izgled nije ispravno definiran!","Greška!",JOptionPane.ERROR_MESSAGE);
    }
  }
  public static void applyNewLookAndFeel() {
    LinkedList startFrames = raLLFrames.getRaLLFrames().getStartFrames();
    for (Iterator i = startFrames.iterator(); i.hasNext(); ) {
      Object item = i.next();
      try {
        startFrame sfr = (startFrame)item;
        System.out.println("SwingUtilities.updateComponentTreeUI("+sfr.getClass()+");");
        SwingUtilities.updateComponentTreeUI(sfr);
        LinkedList childFrames = raLLFrames.getRaLLFrames().getChildFrames(sfr);
        for (Iterator i2 = childFrames.iterator(); i2.hasNext(); ) {
          Component child = (Component)i2.next();
          if (child instanceof raFrame) {
            child = ((raFrame)child).getWindow();
          }
          System.out.println("SwingUtilities.updateComponentTreeUI("+child.getClass()+");");
          SwingUtilities.updateComponentTreeUI(child);
        }
        SwingUtilities.updateComponentTreeUI(hr.restart.help.raUserDialog.getInstance());
      } catch (Exception ex) {
        System.out.println("Greska pri postavljanju teme za "+item.getClass());
        System.out.println("ex = "+ex);
      }
    }
  }
  public static boolean makeLookAndFeel() {
    try {
      if (new Boolean(IntParam.getTag("additionalLaF")).booleanValue()) {
        Properties props = FileHandler.getProperties("laf.properties");
        raThemeSetter tsetter = (raThemeSetter)Class.forName(props.getProperty("SetClass")).newInstance();
        tsetter.set(IntParam.getTag("lookandfeel"),props.getProperty("Theme"));
      } else {
        UIManager.setLookAndFeel(IntParam.getTag("lookandfeel"));
      }
      return true;
    }
    catch (Throwable ex) {
      ex.printStackTrace();
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    return false;
  }
  private JPanel getTestPanel() {
    JPanel testPanel = new JPanel(new GridLayout(5,1,10,10));
    testPanel.add(new JLabel("Label"));
    testPanel.add(new hr.restart.swing.JraTextField("Text"));
    testPanel.add(new JCheckBox("Checkbox"));
    testPanel.add(new JRadioButton("RadioButton"));
    testPanel.add(new JComboBox(new String[] {"Item 0","Item 1","Item 2","Item 3","Item 4","Item 5","Item 6","Item 7","Item 8","Item 9"}));
//    JraScrollPane jscr = new JraScrollPane(testPanel,JraScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JraScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//    JPanel jPanel = new JPanel();
//    jPanel.add(jscr);
//    jPanel.setPreferredSize(new Dimension(300,100));
    return testPanel;
  }
  private class dlgLafOptions extends propsDialog {
    java.util.Properties options;
    String[] initKeys = new String[] {
      "Theme","SetClass"
      };
    JraCheckBox jcbAdditional = new JraCheckBox("Dodatne opcije");
    JraButton jbGet = new JraButton();
    dlgLafOptions(Dialog own) {
      super(own,"laf.properties","Opcije");
      initDlg();
    }
    dlgLafOptions(Frame own) {
      super(own,"laf.properties","Opcije");
      initDlg();
    }

    private void initDlg() {
      jcbAdditional.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          jpMain.setVisible(e.getStateChange() == e.SELECTED);
        }
      });
      getContentPane().add(jcbAdditional,BorderLayout.NORTH);
      jbGet.setText("Dohvat");
      jbGet.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          get_click();
        }
      });
      getDPropEdit().getContentPane().add(jbGet,BorderLayout.NORTH);
      getDPropEdit().setSize(getDPropEdit().getSize().width,getDPropEdit().getSize().height+10);
    }
    public void reload() {
      addInitKeys();
      super.reload();
    }
    public void pack() {
      setSize(400,200);
    }

    private void get_click() {
      if (getDPropEdit().jtkey.getText().equals("Theme")) {
        int ret = JOptionPane.showOptionDialog(this,"Dohvat klasa ili file-ova","Dohvat",
                                     JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,
                                     new String[] {"Klase","File-ovi"},"Klase");
        if (ret == JOptionPane.CLOSED_OPTION) return;
        if (ret == 0) {
          get_class();
        } else get_file();
      } else {
        get_class();
      }
    }
    private void get_file() {
      JFileChooser jThemeChooser = new JFileChooser();
      if (getDPropEdit().jtvalue.getText().equals("")) {
        jThemeChooser.setCurrentDirectory(new java.io.File(getDPropEdit().jtvalue.getText()));
      }
      int res = jThemeChooser.showOpenDialog(getContentPane());
      if (res == JFileChooser.APPROVE_OPTION) {
        getDPropEdit().jtvalue.setText(jThemeChooser.getSelectedFile().getPath());
      }
    }
    private void get_class() {
      final raCBrowser cbw;
      if (getDPropEdit().jtkey.getText().equals("Theme")) {
        String spackage = cbrw.getClasses()[raSkinDialog.this.getListSelectedIndex()].getPackage().getName();
        cbw = new raCBrowser(javax.swing.plaf.metal.MetalTheme.class,new String[] {spackage},null,null,null);
      } else {
        cbw = new raCBrowser(hr.restart.util.raThemeSetter.class,new String[] {"hr.restart.util"},null,null,null);
      }
      raListDialog getList = new raListDialog(this,"Dohvat klase",true) {
        public boolean doSaving() {
          return false;
        }
        public void pack() {
          super.pack();
          setSize(400,200);
        }
        public void action_jBOK() {
          hide();
          String res = cbw.getClassNames()[getListSelectedIndex()];
          getDPropEdit().jtvalue.setText(res);
        }

      };
      getList.setListData(cbw.getClassNames());
      startFrame.getStartFrame().centerFrame(getList,0,getList.getTitle());
      getList.show();
    }

    public void show() {
      jcbAdditional.setSelected(new Boolean(IntParam.getTag("additionalLaF")).booleanValue());
      jpMain.setVisible(jcbAdditional.isSelected());
      super.show();
    }
    public void action_jBOK() {
      super.action_jBOK();
      IntParam.setTag("additionalLaF",new Boolean(jcbAdditional.isSelected()).toString());
      hide();
    }
    private void addInitKeys() {
      Properties _props = FileHandler.getProperties("laf.properties");
      boolean changed = false;
      for (int i = 0; i < initKeys.length; i++) {
        if (_props.getProperty(initKeys[i]) == null) {
          _props.setProperty(initKeys[i],"");
          changed = true;
        }
      }
      if (changed) FileHandler.storeProperties("laf.properties",_props);
    }
  }
}
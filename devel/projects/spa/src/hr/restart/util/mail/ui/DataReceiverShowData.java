/****license*****************************************************************
**   file: DataReceiverShowData.java
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
/*
 * Created on Oct 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.util.mail.ui;

import hr.restart.baza.Tablice;
import hr.restart.baza.dM;
import hr.restart.sisfun.dlgErrors;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raUpitFat;
import hr.restart.util.mail.DataReceiverLoadedData;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.borland.dx.dataset.StorageDataSet;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataReceiverShowData extends raUpitFat {

  private StorageDataSet loadedEmailKeys;
  private StorageDataSet loadedModules;
  private DataReceiverLoadedData data;
  private JPanel panel = new JPanel();
  private JTextArea jta = new JTextArea(); 
  private int level = 0;
  private DataReceiverUI drui;
  private boolean okok;
  private boolean okok2;
  private HashMap visibleCols = new HashMap();
  private int[] navVisCols = null;
  private static DataReceiverShowData _instance;
  private dlgErrors dlge;
  /**
   * 
   */
  public DataReceiverShowData(DataReceiverUI _drui) {
    super();
    drui = _drui;
    initPanel();
    initSets();
    initErrors();
    _instance = this;
  }

  private void initErrors() {
    if (dlge != null) {
      dlge.hide();
      dlge = null;
    }
  }

  public static DataReceiverShowData getDRSDInstance() {
    return _instance;
  }
  
  public void addError(String errordesc) {
    if (dlge == null) {
      dlge = new dlgErrors(this, "Greške pri prijemu podataka", true);
      dlge.setData(dM.createStringColumn("F", 1));
    }
    dlge.addError(errordesc);
  }
  
  public void showErrors() {
    if (dlge != null) dlge.check();
  }
  /**
   * 
   */
  private void initPanel() {
    panel.setLayout(new BorderLayout());
    panel.setPreferredSize(new Dimension(500,200));
    panel.add(new JScrollPane(jta), BorderLayout.CENTER);
    //raCommonClass.getraCommonClass().setLabelLaF(panel, false);
    jta.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_O && e.getModifiers() == KeyEvent.CTRL_MASK) {
          drui.showOptions();
        } else {
//          System.out.println(e.getModifiers());
//          System.out.println(e.getKeyChar());
          
        }
      }
    });
    drui.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        String s;
        if ((s = (String)evt.getNewValue())!=null) {
          addText(s);
        }
      }
    });
    getJPTV().addTableModifier(
        new raTableColumnModifier("MODULE",new String[] {"IMETAB", "OPISTAB"}, new String[] {"MODULE"}, new String[]{"IMETAB"},Tablice.getDataModule().copyDataSet()) {
          public void modify() {
            super.modify();
          }
        });
    getJPTV().getColumnsBean().setSaveSettings(false);
    setJPan(panel);
  }


  /**
   * @param string
   */
  private void addText(String s) {
    jta.setText(jta.getText().concat("\n").concat(s));
  }


  /**
   * 
   */
  private void initSets() {
    loadedEmailKeys = new StorageDataSet();
    loadedEmailKeys.addColumn(dM.createStringColumn("EMAIL", "e-mail",100));
    loadedEmailKeys.open();
    loadedModules = new StorageDataSet();
    loadedModules.addColumn(dM.createStringColumn("MODULE", "Tablica",30));
    loadedModules.addColumn(dM.createStringColumn("EMAIL", "e-mail",100));
    loadedModules.open();
  }
  /**
   * 
   */
  private void loadEKeys() {
    String[] emails = data.getLoadedEmailKeys();
    loadedEmailKeys.empty();
    for (int i = 0; i < emails.length; i++) {
      loadedEmailKeys.insertRow(true);
      loadedEmailKeys.setString("EMAIL", emails[i]);
      loadedEmailKeys.post();
    }
  }
  private void loadModules() {
    String[] modules = data.getLoadedModules();
    loadedModules.empty();
    for (int i = 0; i < modules.length; i++) {
      loadedModules.insertRow(true);
      loadedModules.setString("MODULE", modules[i]);
      loadedModules.post();
    }
  }
  private void updModules(String eml) {
    for (loadedModules.first(); loadedModules.inBounds(); loadedModules.next()) {
      loadedModules.setString("EMAIL", eml);
      loadedModules.post();
    }
  }
  /* (non-Javadoc)
   * @see hr.restart.util.raUpitFat#navDoubleClickActionName()
   */
  public String navDoubleClickActionName() {
    return "Detalji";
  }
  public void jptv_doubleClick() {
    if (level == 1) {
      updModules(loadedEmailKeys.getString("EMAIL"));
      setDataSet(loadedModules);
      level++;
    } else if (level == 2) {
      navVisCols = (int[])visibleCols.get(loadedModules.getString("MODULE").toLowerCase());
      setDataSet(data.getLoadedSet(loadedModules.getString("EMAIL"), loadedModules.getString("MODULE")));
      level++;
    } else System.out.println("DBC: Level "+level+" not handled");
  }

  /* (non-Javadoc)
   * @see hr.restart.util.raUpitFat#navVisibleColumns()
   */
  public int[] navVisibleColumns() {
    try {
      System.out.println("TABLE :: "+getJPTV().getStorageDataSet().getTableName());
      System.out.println("navVisibleColumns ::: "+navVisCols);
    } catch (Exception e) {
      System.out.println("EKSEPSHN: "+e);
    }
    return navVisCols;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.raUpitLite#okPress()
   */
  public void okPress() {
    if (level == 0) {
      okok = drui.go();
    } else {
      okok2 = drui.commit();
      if (!okok2) {
        addText("Potvrda prekinuta!");
        JOptionPane.showMessageDialog(this, "Greška pri potvrdi podataka! Pošaljite izvješæe o grešci!","Greška", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
  public void afterOKPress() {
    if (okok2) {
      hide();
    }
    changeIcon(2);
    raCommonClass.getraCommonClass().setLabelLaF(this.okp.jBOK, true);
    raCommonClass.getraCommonClass().EnabDisabAllLater(panel, true);
    
  }

  /* (non-Javadoc)
   * @see hr.restart.util.raUpitLite#runFirstESC()
   */
  public boolean runFirstESC() {
    return level > 0;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.raUpitLite#firstESC()
   */
  public void firstESC() {
    navVisCols = null;
    if (level == 3) {
      setDataSet(loadedModules);
      level--;
    } else if (level == 2) {
      setDataSet(loadedEmailKeys);
      level--;
    } else if (level == 1) {
      if (JOptionPane.OK_OPTION == 
          JOptionPane.showConfirmDialog(this, "Sigurno želite poništiti prijem podataka e-mailom?", "Pitanje", JOptionPane.YES_NO_OPTION)) {
        setData(null);
        level = 0;
        hide();
      }
      
    } else {
      System.out.println("FE: Level "+level+" not handled");
    }
    
  }

  /**
   * @param set
   */
  public void setDataSet(StorageDataSet set) {
//    getJPTV().setDataSet(null);
    super.setDataSet(set);
    if (set!=null) upitCompleted();
    raCommonClass.getraCommonClass().setLabelLaF(this.okp.jBOK, true);
  }


  /* (non-Javadoc)
   * @see hr.restart.util.raUpitLite#componentShow()
   */
  public void componentShow() {
    okp.jBOK.setText("Prihvat");
    okp.jBOK.setIcon(raImages.getImageIcon(raImages.IMGALLDOWN));
    level = 0;
    jta.setText("");
    addText("Prijem podataka putem e-maila. \nPritisnite -Prihvat- za nastavak.");
    raCommonClass.getraCommonClass().setLabelLaF(this.okp.jBOK, true);
  }

  public DataReceiverLoadedData getData() {
    return data;
  }
  void addVisibleCols(String module, int[] viscols) {
    visibleCols.put(module.toLowerCase(), viscols);
  }
  
  public void setData(DataReceiverLoadedData data) {
    if (data == null) {
      level = 0;
      setDataSet(null);
      componentShow();
    } else {
	    this.data = data;
	    loadEKeys();
	    loadModules();
	    setDataSet(loadedEmailKeys);
	    level = 1;
    }
  }
}

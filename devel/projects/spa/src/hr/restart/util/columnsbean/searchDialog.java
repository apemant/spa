/****license*****************************************************************
**   file: searchDialog.java
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
package hr.restart.util.columnsbean;

import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raSelectTableModifier;
import hr.restart.util.OKpanel;
import hr.restart.util.Util;
import hr.restart.util.raImages;
import hr.restart.util.sysoutTEST;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class searchDialog extends JraDialog {
sysoutTEST ST = new sysoutTEST(false);
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.util.Res_");
  Util Ut = Util.getUtil();
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      performSearch();
    }
    public void jPrekid_actionPerformed() {
      hideSD();
    }
  };
  private ColumnsBean colb;
  Column sCol;
  TableDataSet filtSet;
  searchFilter srchFilt = new searchFilter();
  JPanel jp = new JPanel();
  XYLayout findLay = new XYLayout();
  JLabel jlfind = new JLabel();
  JraTextField jtFind = new JraTextField();
  JPanel jpOptions = new JPanel();
  TitledBorder optionsBorder;
  XYLayout optLay = new XYLayout();
  JraCheckBox jcbBEGINS = new JraCheckBox();
  JraCheckBox jcbEQUALS = new JraCheckBox();
  JraCheckBox jcbGREATER = new JraCheckBox();
  JraCheckBox jcbSMALLER = new JraCheckBox();
  JraCheckBox jcbNOTEQUAL = new JraCheckBox();
  JLabel jlNACIN = new JLabel();
  JraComboBox jcombNACIN = new JraComboBox(new String[] {"Filtriraj","Navigiraj"});
  JraCheckBox jcbCONTAINS = new JraCheckBox();
  ImageIcon SDImage = raImages.getImageIcon(raImages.IMGFIND);

  static Map colSettings = new HashMap();
  static SearchSettings shared;
  SearchSettings curr;
  
  public searchDialog(Frame frame) {
    super(frame,true);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public searchDialog(Dialog frame) {
    super(frame,true);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public searchDialog() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setModal(true);
/*    this.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        this_keyPressed(e);
      }
    });*/
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        setFocus(e);
      }
    });
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        hideSD();
      }
    });
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    optionsBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Opcije");
    jp.setLayout(findLay);
    jpOptions.setBorder(optionsBorder);
    jpOptions.setLayout(optLay);
    jcbBEGINS.setText("po\u010Dinje upisanim tekstom");
    jcbEQUALS.setText("je jednak upisanom");
    jcbGREATER.setText("je ve\u0107i od upisanog");
    jcbSMALLER.setText("je manji od upisanog");
    jcbCONTAINS.setText("sadri upisani tekst");
    jcbNOTEQUAL.setText("je razlièit od upisanog teksta");

    jlNACIN.setText("Na\u010Din pretrage");
    optionsBorder.setTitle("Trai podatak koji");
    this.getContentPane().add(okp, BorderLayout.SOUTH);
    this.getContentPane().add(jp, BorderLayout.CENTER);
    jp.add(jlfind, new XYConstraints(20, 15, -1, -1));
    jp.add(jtFind, new XYConstraints(20, 35, 250, -1));
    jp.add(jpOptions, new XYConstraints(20, 75, 250, 217));
    jpOptions.add(jcbBEGINS, new XYConstraints(10, 25, -1, -1));
    jpOptions.add(jcbEQUALS, new XYConstraints(10, 0, -1, -1));
    jpOptions.add(jcbGREATER, new XYConstraints(10, 75, -1, -1));
    jpOptions.add(jcbSMALLER, new XYConstraints(10, 100, -1, -1));
    jpOptions.add(jlNACIN, new XYConstraints(10, 155, -1, -1));
    jpOptions.add(jcombNACIN, new XYConstraints(100, 155, 130, -1));
    jpOptions.add(jcbCONTAINS, new XYConstraints(10, 50, -1, -1));
    jpOptions.add(jcbNOTEQUAL, new XYConstraints(10, 125, -1, -1));
    
    ItemListener mutualEx = new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == e.SELECTED) {
          if (e.getSource() == jcbEQUALS) {
            jcbNOTEQUAL.setSelected(false);
          }
          if (e.getSource() == jcbNOTEQUAL) {
            jcbEQUALS.setSelected(false);
          }
        }
      }
    };
    jcbEQUALS.addItemListener(mutualEx);
    jcbNOTEQUAL.addItemListener(mutualEx);
    
    okp.registerOKPanelKeys(this);
    shared = new SearchSettings(true);
  }

  public void go(ColumnsBean colbC) {
    colb = colbC;
    sCol = (Column)colb.raDataSet.getColumn(colb.getSelectedColumnName()).clone();
    createFiltSet();
    initUI();
    show();
  }
  void hideSD() {
    dispose();
  }

  void performSearch() {
    shared = curr;
    curr.memorize(this);
    if (jtFind.getText().equals("")) {
      removeSearchFilter();
      colb.raDataSet.setSort(colb.raDataSet.getSort());//a way to refresh
      hideSD();
    } else {
      if (jcombNACIN.getSelectedIndex()==0) {
        addSearchFilter();
        hideSD();
      } else {
        searchNavigate();
        okButSetDalje();
      }
    }
    if (colb.getRaJdbTable() instanceof hr.restart.swing.JraTable2) {
      JraTable2 tab = (JraTable2) colb.getRaJdbTable();
      raSelectTableModifier stm = tab.hasSelectionTrackerInstalled();
      if (stm != null && stm.isNatural()) stm.clearSelection();
      tab.fireTableDataChanged();
    }
  }
  void addSearchFilter() {
    try {
      removeSearchFilter();
      colb.raDataSet.addRowFilterListener(srchFilt);
      colb.raDataSet.refilter();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  void removeSearchFilter() {
    colb.raDataSet.removeRowFilterListener(srchFilt);
  }

  void okButSetOK() {
    okp.jBOK.setText(res.getString("jBOK"));
    okp.jBOK.repaint();
  }

  void okButSetDalje() {
    okp.jBOK.setText("Dalje");
    okp.jBOK.repaint();
  }

  void searchNavigate() {
    if (isLocateFirst()) colb.raDataSet.first();
    if (!colb.raDataSet.next()) colb.raDataSet.first();
    else colb.raDataSet.prior();
    enableDataEvents(false);
    do {
      if (srchFilt.isRow(colb.raDataSet)) break;
    } while (colb.raDataSet.next());
    enableDataEvents(true);
  }
  void enableDataEvents(boolean en) {
    if (colb.getRaJdbTable() instanceof hr.restart.swing.JraTable2) {
      hr.restart.swing.JraTable2 table2 = (hr.restart.swing.JraTable2)colb.getRaJdbTable();
      if (en) {
        table2.startFire();
//        table2.fireTableDataChanged();
      }
      else table2.stopFire();
    }
    colb.raDataSet.enableDataSetEvents(en);
  }
/*
  int getInteractiveLocateOption() {
    int retVal;
    if (isLocateFirst()) {
      retVal = Locate.FIRST;
    } else {
      retVal = Locate.NEXT;
    }
    if (sCol.getDataType() == Variant.STRING) {
      retVal = retVal|Locate.CASE_INSENSITIVE;
    }
    if (jcbBEGINS.isSelected()) {
      retVal = retVal|Locate.PARTIAL;
    }
    return retVal;
  }
*/
  boolean isLocateFirst() {
    return okp.jBOK.getText().equals(res.getString("jBOK"));
  }
  void createFiltSet() {
    filtSet = new TableDataSet();
    filtSet.setColumns(new Column[] {sCol});
  }

  void initUI() {
    Container owner = colb.getTopLevelAncestor();
    setSize(295 ,350);
    setLocation(owner.getLocation().x+((owner.getSize().width - this.getSize().width)/2),owner.getLocation().y+((owner.getSize().height - this.getSize().height)/2));
    setTitle("Pretraivanje - "+colb.getSaveName());
    jlfind.setText("Pretraivanje po podatku ".concat(sCol.getCaption()));
    
    if (colSettings.containsKey(sCol.getColumnName().toUpperCase()))
      curr = (SearchSettings) colSettings.get(sCol.getColumnName().toUpperCase());
    else colSettings.put(sCol.getColumnName().toUpperCase(),
        curr = new SearchSettings(shared, sCol.getDataType() == Variant.STRING));
    initTypeUI(sCol.getDataType() == Variant.STRING);
  }

  private void initTypeUI(boolean isString) {
    /*jcbEQUALS.setEnabled(true);
    jcbEQUALS.setSelected(true);
    jcbBEGINS.setEnabled(isString);
    jcbBEGINS.setSelected(isString);
    jcbCONTAINS.setEnabled(isString);
    jcbCONTAINS.setSelected(isString);
    jcbGREATER.setEnabled(!isString);
    jcbSMALLER.setEnabled(!isString);*/
    jcbBEGINS.setEnabled(isString);
    jcbCONTAINS.setEnabled(isString);
    
    curr.restore(this);
  }
  
  void initComponents() {
    jtFind.setDataSet(filtSet);
    jtFind.setColumnName(sCol.getColumnName());
    jtFind.setHorizontalAlignment(JTextField.LEFT);
    initJcombNACIN();
    jtFind.setText(curr.value);
//    initCheckBoxes();
//    jcbEQUALS.setSelected(true);
    okp.jBOK.setText(res.getString("jBOK"));
  }
/*
  void initCheckBoxes() {
    if (sCol.getDataType()==Variant.STRING) {
      jcbBEGINS.setEnabled(true);
      jcbCONTAINS.setEnabled(true);
    } else {
      jcbBEGINS.setEnabled(false);
      jcbCONTAINS.setEnabled(false);
    }
    jcbEQUALS.setSelected(true);
  }
*/
  void initJcombNACIN() {
    if (colb.raDataSet.getRowFilterListener() != null) {
      if (isSearchFilterAdded()) return;
      jcombNACIN.setSelectedIndex(1);
      jcombNACIN.setEnabled(false);
    }
  }

  boolean isSearchFilterAdded() {
    return colb.raDataSet.getRowFilterListener().equals(srchFilt);
  }

  void setFocus(ComponentEvent e) {
    initComponents();
    jtFind.requestFocus();
  }

  boolean isInSearchOptions(Variant v1,Variant v2) {
    if (v1.getType() == Variant.STRING) {
      String s1 = v1.toString().trim().toLowerCase();
      String s2 = v2.toString().trim().toLowerCase();
      if (s1.equals(s2) && curr.equal) return true;
      if (s1.startsWith(s2) && curr.begins) return true;
      if (s1.indexOf(s2) >= 0 && curr.contains) return true;
      if (!s1.equals(s2) && curr.neq) return true;
    } else {
      int compare = v1.compareTo(v2);
      if (compare == 0 && curr.equal) return true;
      if (compare > 0 && curr.greater) return true;
      if (compare < 0 && curr.smaller) return true;
      if (compare != 0 && curr.neq) return true;
    }
    return false;
  }

  class searchFilter implements com.borland.dx.dataset.RowFilterListener {
    public void filterRow(com.borland.dx.dataset.ReadRow row, com.borland.dx.dataset.RowFilterResponse resp) {
      if (isRow(row)) {
        resp.add();
      } else {
        resp.ignore();
      }
    }
    boolean isRow(com.borland.dx.dataset.ReadRow row) {
      Variant vrow = new Variant();
      Variant vfiltset = new Variant();
      row.getVariant(sCol.getColumnName(),vrow);
      filtSet.getVariant(sCol.getColumnName(),vfiltset);
//      System.out.println("Ut.compareBVariants("+vrow.toString()+","+vfiltset.toString()+","+sCol.getColumnName()+"...");
      return isInSearchOptions(vrow,vfiltset);
      //Ut.equalsBVariant(row,filtSet,sCol.getColumnName(),sCol.getColumnName());
    }
  }

/*  void this_keyPressed(KeyEvent e) {
    if (e.getKeyCode()==e.VK_F10) {
      performSearch();
    } else if (e.getKeyCode()==e.VK_ESCAPE) {
      hide();
    }
  }*/
  
}

class SearchSettings {
  boolean equal, begins, contains, greater, smaller, neq;
  String value;
  
  public SearchSettings(boolean isStr) {
    equal = !isStr;
    contains = isStr;
    begins = greater = smaller = false;
    value = "";
  }
  
  public SearchSettings(SearchSettings orig, boolean isStr) {
    equal = orig.equal;
    contains = orig.contains && isStr;
    begins = orig.begins && isStr;
    greater = orig.greater;
    smaller = orig.smaller;
    neq = orig.neq;
    value = orig.value;
  }
  
  public void memorize(searchDialog src) {
    equal = src.jcbEQUALS.isSelected();
    begins = src.jcbBEGINS.isSelected();
    contains = src.jcbCONTAINS.isSelected();
    greater = src.jcbGREATER.isSelected();
    smaller = src.jcbSMALLER.isSelected();
    neq = src.jcbNOTEQUAL.isSelected();
    value = src.jtFind.getText();
  }
  
  public void restore(searchDialog src) {
    boolean isStr = src.sCol.getDataType() == Variant.STRING;
    src.jcbEQUALS.setSelected(equal);
    src.jcbBEGINS.setSelected(begins && isStr);
    src.jcbCONTAINS.setSelected(contains && isStr);
    src.jcbGREATER.setSelected(greater);
    src.jcbSMALLER.setSelected(smaller);
    src.jcbNOTEQUAL.setSelected(neq);
    src.jtFind.setText(value);
  }
  
  public String toString() {
    return value + ": " + (equal ? "E" : " ") + (begins ? "B" : " ") +
       (contains ? "C" : " ") + (greater ? "G" : " ") + (smaller ? "S" : " ");
  }
} 

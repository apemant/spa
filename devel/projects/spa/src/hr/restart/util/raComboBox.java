/****license*****************************************************************
**   file: raComboBox.java
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

import hr.restart.swing.JraComboBox;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;

/**
 * <pre>
 * Title:        raComboBox de luxe
 * Description:  Pretvaranje String[][] u dbComboBox
 * Copyright:    Copyright (c) 2001
 * Company:      REST-ART
 * author        Rest-art development team
 * version       1.0
 *
 *
 *
 *
 * Standardni naputak za uporabu:
 *
 *   rcbVRSKL.setRaDataSet(dm.getSklad());
 *   rcbVRSKL.setRaColumn("VRSKL");
 *   rcbVRSKL.setRaItems(new String[][] {
 *       {"Skladi\u0161te","S"},
 *       {"Trgovina","T"},
 *   });
 *
 * </pre>
 */

 public class raComboBox extends JraComboBox implements com.borland.dx.dataset.ColumnAware {

//  private com.borland.dx.sql.dataset.QueryDataSet raDataSet;
  private com.borland.dx.dataset.DataSet raDataSet;
  private String[][] raItems;
  private String raColumn;
  
  private NavigationAdapter nav = new NavigationAdapter() {
    public void navigated(DataSet source) {
      findCombo();
    }
  };

  public raComboBox() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setEditable(false);
    this.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent e) {
        if (itemStateTrack) {
        	if (e.getStateChange() == e.SELECTED || getSelectedItem() == null)
        		this_itemStateChanged();
        }
      }
    });
    setPreferredSize(new java.awt.Dimension(100,21));
/*    this.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
System.out.println("actionPerformed");
        this_itemStateChanged();
      }
    });*/
    /*this.addKeyListener(new hr.restart.swing.JraKeyListener());*/    
  }

/**
 * Postavimo dataset
 */
  public void setRaDataSet(com.borland.dx.dataset.DataSet newRaDataSet) {
    if (raDataSet != null) nav.uninstall(raDataSet);
    raDataSet = newRaDataSet;
    if (raDataSet != null) nav.install(raDataSet);
  }
  
  public void setDataSet(com.borland.dx.dataset.DataSet newRaDataSet) {
    setRaDataSet(newRaDataSet);
  }
/**
 * Dohvat dataseta, zlu ne trebalo
 */
//  public com.borland.dx.sql.dataset.QueryDataSet getRaDataSet() {
  public com.borland.dx.dataset.DataSet getRaDataSet() {
    return raDataSet;
  }
  public com.borland.dx.dataset.DataSet getDataSet() {
    return getRaDataSet();
  }
/**
 * Postavimo kolonu gdje chemo da zapishemo
 */
  public void setRaColumn(String newRaColumn) {
    raColumn = newRaColumn;
  }
  public void setColumnName(String newRaColumn) {
    setRaColumn(newRaColumn);
  }
/**
 * Dohvatimo kolonu, zlu ne trebalo
 */
  public String getRaColumn() {
    return raColumn;
  }
  public String getColumnName() {
    return getRaColumn();
  }

  public String getDataValue() {
    return raItems[getSelectedIndex()][1];
  }
  
  public String getDataValue(int row) {
    return raItems[row][1];
  }
  
  public void setDataValue(String val) {
    for (int i = 0; i < getItemCount(); i++)
      if (raItems[i][1].equalsIgnoreCase(val))
        setSelectedIndex(i);
  }
/**
 * Postavimo items u formatu String[][] {
 * {item1, vrijednost u datasetu},
 * {item2, vrijednost u datasetu},
 * {...,...}
 * }
 */
  private boolean itemStateTrack = true;
  public void setRaItems(DataSet ds, String valueColumn, String descrColumn) {
    ds.open();
    ds.first();
    Variant v = new Variant();
    String[][] itm = new String[ds.rowCount()][2];
    for (int i = 0; ds.inBounds(); ds.next(), i++) {
      ds.getVariant(descrColumn, v);
      itm[i][0] = v.toString();
      ds.getVariant(valueColumn, v);
      itm[i][1] = v.toString();
    }
    setRaItems(itm);
  }

  public void setRaItems(String[][] newRaItems) {
    itemStateTrack = false;
    raItems = newRaItems;
    setSelectedIndex(-1);
    this.removeAllItems();
    if (raItems == null) return;
    for (int i=0;i<raItems.length;i++) {
      this.addItem(raItems[i][0]);
    }
    itemStateTrack = true;
    if (isShowing()) findCombo();
  }
  public void this_itemStateChanged() {
    if (raItems==null) return;
    if (!this.isShowing()) return;
    if (raDataSet == null) return;
//    raDataSet.open();
    raDataSet.setString(raColumn,raItems[getSelectedIndex()][1]);
  }
/**
 * Funkcija za pronalazhenje i postavljanje default vrijednosti u neshem raComboBox
 */

  public void requestFocusLater() {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        requestFocus();
      }
    });
  }
  public void findCombo() {
    if (raItems == null) return;
    for (int i=0;i<raItems.length;i++) {
      if (raItems[i][1].equals(raDataSet.getString(raColumn))) {
        setSelectedIndex(i);
        return;
      }
    }
    /*
    if (getSelectedIndex()>=0) {
      this_itemStateChanged();
    }
    */
    //    System.out.println("ERR radbComboBox: Nije nadjena vrijednost "+raColumn+"="+raDataSet.getString(raColumn));
  }
}
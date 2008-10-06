/****license*****************************************************************
**   file: raLookUpField.java
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

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

/**
 * <pre>
 * Title:        raLookUpField de luxe
 * Description:  Trazhilica za stranje kljucheve
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
 *
 *
 * Nadojebak zbog dohvata iz dataSeta
 *
 *   rcbVRSKL.findCombo();
 * </pre>
 */


public class raLookUpField extends JTextField {

  private com.borland.dx.sql.dataset.QueryDataSet raDataSet;
  private String raColumn;
  private com.borland.dx.sql.dataset.QueryDataSet raLookUpDataSet;
  private int raMode;
  private javax.swing.JTextField[] raFields;
  private String[] raDataColumns;
  private hr.restart.util.raLookUpField raMasterField;
  com.borland.dx.sql.dataset.QueryDataSet RezSet;

  public raLookUpField() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        this_focusLost();
      }
    });
    this.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        this_keyPressed(e);
      }
    });
  }

/**
 * Postavimo dataset
 */
  public void setRaDataSet(com.borland.dx.sql.dataset.QueryDataSet newRaDataSet) {
    raDataSet = newRaDataSet;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getRaDataSet() {
    return raDataSet;
  }
/**
 * Postavimo kolonu
 */
  public void setRaColumn(String newRaColumn) {
    raColumn = newRaColumn;
  }
  public String getRaColumn() {
    return raColumn;
  }
  protected void this_keyPressed(KeyEvent e) {
    if (e.getKeyCode() == e.VK_F9) {
      exeQuery();
    }
  }
  protected void this_focusLost() {
    findRezSet();
    raDataSet.setInt(raColumn,raLookUpDataSet.getInt(raColumn));
  }
  public void setRaLookUpDataSet(com.borland.dx.sql.dataset.QueryDataSet newRaLookUpDataSet) {
    raLookUpDataSet = newRaLookUpDataSet;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getRaLookUpDataSet() {
    return raLookUpDataSet;
  }
  public void setRaMode(int newRaMode) {
    raMode = newRaMode;
  }
  public int getRaMode() {
    return raMode;
  }
  public void findRezSet() {
    if (raMode==1) {
      exeQuery();
    }
  }
  public void setRaFields(javax.swing.JTextField[] newRaFields) {
    raFields = newRaFields;
  }
  public javax.swing.JTextField[] getRaFields() {
    return raFields;
  }
  public void setRaDataColumns(String[] newRaDataColumns) {
    raDataColumns = newRaDataColumns;
  }
  public String[] getRaDataColumns() {
    return raDataColumns;
  }
  public void setRaMasterField(hr.restart.util.raLookUpField newRaMasterField) {
    raMasterField = newRaMasterField;
    this.setRaDataSet(raMasterField.getRaDataSet());
    this.setRaLookUpDataSet(newRaMasterField.getRaLookUpDataSet());
  }
  public hr.restart.util.raLookUpField getRaMasterField() {
    return raMasterField;
  }
  public void exeQuery() {
    String qStr;
    RezSet = new com.borland.dx.sql.dataset.QueryDataSet();
    if (raMode==1) {
      qStr="SELECT * FROM "+raLookUpDataSet.getTableName()+" WHERE "+raColumn+" = "+findDelimiter()+this.getText().trim()+findDelimiter();
    }
    else {
      qStr="SELECT * FROM "+raLookUpDataSet.getTableName()+" WHERE "+raColumn+" LIKE "+findDelimiter()+this.getText().trim()+"%"+findDelimiter();
    }
//    RezSet.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(hr.restart.baza.dM.getDataModule().getDatabase1(),qStr));
    RezSet.open();
    if (RezSet.getRowCount() > 1) {
      hr.restart.util.raLookUpDialog look = new hr.restart.util.raLookUpDialog();
      look.setRaDataSet(RezSet);
      look.ShowCenter(true,250,250);
    }
    for (int i=0;i<raDataColumns.length;i++) {
      raFields[i].setText(RezSet.getString(raDataColumns[i]));
    }
    System.out.println(qStr);
    System.out.println(RezSet);
    RezSet = null;
  }
  public String findDelimiter() {
    if (raMode<10)
      return "'";
    else
      return "";
  }
}
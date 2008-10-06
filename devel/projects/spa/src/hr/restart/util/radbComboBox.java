/****license*****************************************************************
**   file: radbComboBox.java
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

import java.awt.event.ItemEvent;

import javax.swing.JComboBox;

/**
 * <pre>
 * Title:        radbComboBox de luxe
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
 *  rcbFRANKA.setRaDataSet(dm.getDoki());
 *  rcbFRANKA.setRaColumn("FRANK");
 *  rcbFRANKA.setRaSifraCol(2);
 *  rcbFRANKA.setRaNazivCol(3);
 *  rcbFRANKA.setRaLookUpDataSet(dm.getFranka());
 *
 *
 * Vazhna napomena: setRaSifraCol i set raNazivCol moraju biti prije setRaLookUpDataSet, jer inache imamo chooshpice
 *
 * </pre>
 */
public class radbComboBox extends JComboBox {

  private com.borland.dx.sql.dataset.QueryDataSet raDataSet;
  private String raColumn;
  private com.borland.dx.sql.dataset.QueryDataSet raLookUpDataSet;
  private String[][] raItems;
  private int raSifraCol;
  private int raNazivCol;

  public radbComboBox() {
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
      public void itemStateChanged(ItemEvent e) {
        this_itemStateChanged(e);
      }
    });
  }

/**
 * Postavimo dataset
 */
  public void setRaDataSet(com.borland.dx.sql.dataset.QueryDataSet newRaDataSet) {
    raDataSet = newRaDataSet;
    raDataSet.addNavigationListener(new com.borland.dx.dataset.NavigationListener() {
      public void navigated(com.borland.dx.dataset.NavigationEvent e) {
        raDataSet_navigated(e);
      }
    });
  }
/**
 * Dohvat dataseta, zlu ne trebalo
 */
  public com.borland.dx.sql.dataset.QueryDataSet getRaDataSet() {
    return raDataSet;
  }
/**
 * Postavimo kolonu gdje chemo da zapishemo
 */
  public void setRaColumn(String newRaColumn) {
    raColumn = newRaColumn;
  }
/**
 * Dohvatimo kolonu, zlu ne trebalo
 */
  public String getRaColumn() {
    return raColumn;
  }
/**
 * Postavimo Items-e na temelju podataka iz dataseta
 */
  public void setRaLookUpDataSet(com.borland.dx.sql.dataset.QueryDataSet newRaLookUpDataSet) {
    raLookUpDataSet = newRaLookUpDataSet;
    raLookUpDataSet.open();
    raLookUpDataSet.first();
    for (int x=0;x<raLookUpDataSet.rowCount();x++) {
      this.addItem(raLookUpDataSet.getString(raLookUpDataSet.getColumn(raNazivCol).getColumnName()));
      raLookUpDataSet.next();
    }
  }
/**
 * Dohvatimo DataSet u kojem su nam raItems
 */
  public com.borland.dx.sql.dataset.QueryDataSet getRaLookUpDataSet() {
    return raLookUpDataSet;
  }
/**
 * Na promjeni chemo i mi promjeniti dataset, tako da budemo u koraku s vremenom
 */
  final public void this_itemStateChanged(ItemEvent e) {
    raLookUpDataSet.first();
    raLookUpDataSet.goToRow(this.getSelectedIndex());
    raDataSet.open();
    if (raDataSet.rowCount()>0) {
      raDataSet.setString(raColumn,raLookUpDataSet.getString(raSifraCol));
    }
  }
/**
 * Funkcija za pronalazhenje i postavljanje default vrijednosti u neshem raComboBox
 * - ako je prazno postavi prvi racord
 * - ako nije onda ga trazhi
 */
  void raDataSet_navigated(com.borland.dx.dataset.NavigationEvent e) {
    if (raLookUpDataSet.rowCount()>0) {
      raLookUpDataSet.first();
      if (raDataSet.getString(raColumn).trim().equals("")) {
        this.setSelectedIndex(0);
      }
      else {
        raLookUpDataSet.interactiveLocate(raDataSet.getString(raColumn),raLookUpDataSet.getColumn(raSifraCol).getColumnName(),com.borland.dx.dataset.Locate.FIRST, false);
        this.setSelectedIndex(raLookUpDataSet.getRow());
      }
      if (raDataSet.rowCount()>0) {
        raDataSet.setString(raColumn,raLookUpDataSet.getString(raSifraCol));
      }
    }
  }
/**
 * Tu chemo da postavimo kolonu gdje nam je shifra koju zapisujemo u tabelu
 */
  public void setRaSifraCol(int newRaSifraCol) {
    raSifraCol = newRaSifraCol;
  }
/**
 * Dohvat kolone
 */
  public int getRaSifraCol() {
    return raSifraCol;
  }
/**
 * Tu chemo da postavimo kolonu gdje nam je naziv koji prikazujemo na ekranu
 */
  public void setRaNazivCol(int newRaNazivCol) {
    raNazivCol = newRaNazivCol;
  }
/**
 * Dohvat kolone
 */
  public int getRaNazivCol() {
    return raNazivCol;
  }
}
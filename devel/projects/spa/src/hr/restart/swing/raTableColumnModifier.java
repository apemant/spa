/****license*****************************************************************
**   file: raTableColumnModifier.java
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
package hr.restart.swing;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;

import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
public class raTableColumnModifier extends raTableModifier {
  private JraTable2 jtab;
  private Column dsCol;
  protected DataSet tableDs;
  private String dsColName="";
  private String[] dsColsReplace;
  private String[] dsColsKey;
  private String[] dsColsKeyS;
  private DataSet dsToSearch;
  protected Variant shared = new Variant();
  protected VarStr replacement = new VarStr();
  public String veznik = " - ", ostaliVeznici = " - "; 
/**
 * Najkopleksniji konstruktor gdje je
 * @param columnToModify ime kolone u datasetu umjesto kojeg prikazujemo druge vrijednosti
 * @param columnsReplace imena kolona cijim vrijednostima mijenjamo columnToModify
 * @param keyColumns imena kolona u tablicinom datasetu po cijim vrijednostima treba locirati slog u setToSearch
 * @param keyColumnsSearch imena kolona u setToSearch po kojim treba locirati slog po vrijednostima zadanim u keyColumns
 * @param setToSearch dataset po kojem trazimo vrijednosti ako je null vrijednost columnToModify zamjenjuje se stringovima zadanim u columnsReplace
 */
  public raTableColumnModifier(
                      String columnToModify,
                      String[] columnsReplace,
                      String[] keyColumns,
                      String[] keyColumnsSearch,
                      DataSet setToSearch) {
    dsColName = columnToModify;
    dsColsReplace = columnsReplace;
    dsToSearch = setToSearch;
    dsColsKey = keyColumns;
    dsColsKeyS = keyColumnsSearch;
  }
/**
 * @param columnToModify ime kolone u datasetu umjesto kojeg prikazujemo druge vrijednosti
 * @param columnsReplace imena kolona cijim vrijednostima mijenjamo columnToModify
 * @param keyColumns imena kolona u tablicinom datasetu po cijim vrijednostima treba locirati slog u setToSearch i koja su ista u oba dataseta
 * @param setToSearch dataset po kojem trazimo vrijednosti ako je null vrijednost columnToModify zamjenjuje se stringovima zadanim u columnsReplace
 */
  public raTableColumnModifier(
                      String columnToModify,
                      String[] columnsReplace,
                      String[] keyColumns,
                      DataSet setToSearch) {
    this(columnToModify,columnsReplace,keyColumns,keyColumns,setToSearch);
  }
/**
 * @param columnToModify ime kolone u datasetu umjesto kojeg prikazujemo druge vrijednosti i istovremeno i kolona za pretrazivanje
 * @param columnsReplace imena kolona cijim vrijednostima mijenjamo columnToModify
 * @param setToSearch dataset po kojem trazimo vrijednosti ako je null vrijednost columnToModify zamjenjuje se stringovima zadanim u columnsReplace
 */

  public raTableColumnModifier(
                      String columnToModify,
                      String[] columnsReplace,
                      DataSet setToSearch) {
    this(columnToModify,columnsReplace,new String[] {columnToModify},new String[] {columnToModify},setToSearch);
  }
  public boolean doModify() {
    if (getTable() instanceof JraTable2) {
      jtab = (JraTable2)getTable();
      dsCol = jtab.getDataSetColumn(getColumn());
      if (dsCol == null) return false;
      tableDs = dsCol.getDataSet();
      if (dsToSearch != null) dsToSearch.open();
      return dsCol.getColumnName().equals(dsColName);
    }
    return false;
  }
  public void modify() {
    if (dsToSearch == null) replaceNames();
    else replaceValues();
    if (renderComponent instanceof JLabel)
      ((JLabel)renderComponent).setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
  }
  /**
   * Poziva se za slucaj da je setToSearch = null. Zamjenjuje vrijednost kolone za vrijednosti u columnstoReplace
   * Dobra metoda za overridanje ako ne treba lookup nego samo opis nekog jednoslovnog parametra npr.
   * <pre>
   * new raTableColumnModifier("STATUS",new String[] {"NESTOBEZVEZE"},null) {
   *   public void replaceNames() {
   *     Variant v = prepareVariants()[0];
   *     if (v.getString.equals("O") setComponentText("Obra\u0111en");
   *     else setComponentText("Nije obra\u0111en);
   *   }
   * }
   * </pre>
   */
  public void replaceNames() {
    String lastVeznik = veznik;
    replacement.clear();
    if (dsColsReplace == null) return;
    for (int i=0;i<dsColsReplace.length;i++)
      replacement.append(dsColsReplace[i]).
        append(lastVeznik = (i == 0 ? veznik : ostaliVeznici));
    replacement.chop(lastVeznik.length());
    setComponentText(replacement.toString());
  }
  
  public String getComponentText() {
    if (renderComponent instanceof JLabel) {
      return ((JLabel)renderComponent).getText();
    } else if (renderComponent instanceof JTextComponent) {
      return ((JTextComponent)renderComponent).getText();
    } else {
      return "";
    }
  }
  /**
   * Traži zadane vrijednosti u setToSearch
   */
  public void replaceValues() {
    String lastVeznik = veznik;
    lookupData ld = lookupData.getlookupData();
    DataSet ds = dsCol.getDataSet();
//    Variant[] vars = prepareVariants();
    DataRow dr = ld.raLookup(dsToSearch, dsColsKeyS, ds, dsColsKey, getRow());
//    if (!ld.raLocate(dsToSearch,dsColsKeyS,vars)) return;
    if (dr == null) return;
    replacement.clear();
    for (int i=0;i<dsColsReplace.length;i++) {
      if (dr.hasColumn(dsColsReplace[i]) != null) {
        dr.getVariant(dsColsReplace[i],shared);
        if (shared.getAsObject() != null)
          replacement.append(formatShared(shared, dsColsReplace[i])).
            append(lastVeznik = (i == 0 ? veznik : ostaliVeznici));          
      }
    }
    replacement.chop(lastVeznik.length());
    setComponentText(replacement.toString());
  }
  /**
   * Metoda za last minute format vrijednosti jedne kolone koja ce biti umetnuta u tablicu (dsColsReplace) 
   * @param sh variant sa vrijednoscu za umetanje
   * @param colname ime kolone, clana arraya dsColsReplace, iz kojeg dobivamo sh  
   * @return String koji se umece u table cell, u stvari dio stringa ako dsColsReplace ima vise clanova
   */
  public String formatShared(Variant sh, String colname) {
    return shared.toString();
  }
/**
 * @return Variante od keyColumnsa napunjene vrijednostima tekuceg reda
 */
/*  public Variant[] prepareVariants() {
    Variant[] vars = new Variant[dsColsKey.length];
    for (int i=0;i<dsColsKey.length;i++) {
      Variant v = new Variant();
      tableDs.getVariant(dsColsKey[i],getRow(),v);
      vars[i] = v;
    }
    return vars;
  } */
  public String toString() {
    return
    "raTableModifier "+this.getClass()+"\n"
    +" To replace text in column "+dsColName+" with values from columns \n"
    +" "+dsColsReplace[0]+"... which can be found in dataset "+dsToSearch+"\n"
    +" with keys "+dsColsKey[0]+"...";
  }

  public String getModifiedColumn() {
    return dsColName;
  }
  public int getMaxModifiedTextLength() {
    int ret = 0;
    for (int i = 0; i < dsColsReplace.length; i++) {
      Column col = (dsToSearch!=null)?dsToSearch.hasColumn(dsColsReplace[i]):null;
      if (col == null) {
        ret = ret + dsColsReplace[i].length();
      } else {
        ret = ret + col.getWidth();
      }
    }
    ret = ret + dsColsReplace.length - 1;
    return ret;
  }
}


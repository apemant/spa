/****license*****************************************************************
**   file: raDataIntegrity.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.KreirDrop;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.util.VarStr;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raProcess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raDataIntegrity {
  private DataRow save;
  private raMatPodaci mp;
  private String[] key;
  private String[] cols;
  private String[] tables;
  private HashSet ignore;
  private HashMap filters;
  private HashMap foreign;

  private dlgErrors errs;
  private raUser usr = raUser.getInstance();
  private boolean returnValueHack;

  private QueryDataSet curr() {
    return mp.getRaQueryDataSet();
  }

  private String getTableName() {
    return mp.getRaQueryDataSet().getColumn(0).getTableName();
  }

  private void findKey() {
    ArrayList ids = new ArrayList();
    for (int i = 0; i < curr().getColumnCount(); i++)
      if (curr().getColumn(i).isRowId())
        ids.add(curr().getColumn(i).getColumnName());
    key = (String[]) ids.toArray(new String[ids.size()]);
    if (key.length == 0)
      throw new IllegalArgumentException("Ne postoji rowid, tablica " + getTableName());
    if (key.length > 1)
      System.err.println("visestruk kljuc, tablica "+getTableName()+
        " ("+VarStr.join(key, ", ")+")");
    else System.err.println(key[0]);
  }

  public static raDataIntegrity installFor(raMatPodaci mp, boolean editable) {
    if (mp.dataIntegrity() != null)
      throw new UnsupportedOperationException("raDataIntegrity already installed");
    return new raDataIntegrity(mp, editable);
  }

  public static raDataIntegrity installFor(raMatPodaci mp) {
    return installFor(mp, true);
  }

  private raDataIntegrity(raMatPodaci mp, boolean editable) {
    this.mp = mp;
    mp.setDataIntegrity(this);
    findKey();
    dM.getDataModule().loadModules();
    tables = null;
    filters = new HashMap();
    ignore = new HashSet();
    addIgnoreTable(getTableName());
    if (editable)
      setProtectedColumns(key);
  }

  public raDataIntegrity setKey(String key) {
    setKey(new String[] {key});
    return this;
  }

  public raDataIntegrity setKey(String[] key) {
    this.key = key;
    return this;
  }

  public raDataIntegrity setProtectedColumns(String[] colNames) {
    cols = colNames;
    save = new DataRow(curr(), cols);
    return this;
  }

  public raDataIntegrity setEditableColumns(String[] colNames) {
    HashSet all = new HashSet(Arrays.asList(curr().getColumnNames(curr().getColumnCount())));
    all.removeAll(Arrays.asList(colNames));
    return setProtectedColumns((String[]) all.toArray(new String[all.size()]));
  }

  public raDataIntegrity addTableFilter(String table, Condition c) {
    filters.put(table.toLowerCase(), c);
    return this;
  }

  public raDataIntegrity addIgnoreTable(String table) {
    ignore.add(table.toLowerCase());
    return this;
  }

  public raDataIntegrity addIgnoreTables(String[] tables) {
    for (int i = 0; i < tables.length; i++)
      ignore.add(tables[i].toLowerCase());
    return this;
  }
  
  public raDataIntegrity addOtherTable(String table, String[] fkey) {
    if (fkey.length != key.length)
      throw new UnsupportedOperationException("raDataIntegrity.addOtherTable - invalid key length");
    if (foreign == null) foreign = new HashMap();
    foreign.put(table, fkey);
    return this;
  }

  public raDataIntegrity setCheckTables(String[] tables) {
    this.tables = tables;
    return this;
  }

  public void saveRow() {
    if (save != null)
      DataSet.copyTo(cols, curr(), cols, save);
  }

  public boolean checkDelete() {
    return !keyAppears("Brisanje");
  }

  public boolean checkEdit() {
    return save != null || !keyAppears("Izmjenu");
  }

  public boolean checkCommit() {
    if (save == null) return true;
    DataRow check = new DataRow(curr(), cols);
    DataSet.copyTo(cols, curr(), cols, check);
//    System.out.println(check);
//    System.out.println(save);
    if (save.equals(check)) return true;
    else return !keyAppears("Izmjenu");
  }

  private boolean keyAppears(String action) {
    raProcess.runChild(mp.getWindow(), "Provjera integriteta", "Provjera u tijeku ...",
    new Runnable() {
      public void run() {
        if (keyAppearsImpl())
          raProcess.fail();
      }
    });
    if (raProcess.isFailed()) {
      String[] opt = {"OK", "Detalji"};
      if (JOptionPane.showOptionDialog(mp.getWindow(), action + " nije moguæe izvršiti "+
        "jer postoje dokumenti koji koriste taj podatak!", "Greška", 0,
        JOptionPane.ERROR_MESSAGE, null, opt, opt[0]) == 1)
          return checkDetails();
    } else errs.hide();
    return !raProcess.isCompleted();
  }

  private boolean checkDetails() {
    returnValueHack = true;
    if (usr.isRoot()) {
      JraButton force = new JraButton();
      force.setText("Zanemari integritet!");
      force.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (errs == null || errs.isDead() || !errs.isActionButton(e.getSource()))
            return;
          returnValueHack = false;
          errs.hide();
        }
      });
      errs.setActionButton(force);
    }
    errs.check();
    return returnValueHack;
  }

  private void createErrorTrace() {
    VarStr wae = new VarStr(Condition.whereAllEqual(key, curr()).toString());
    wae.replaceAll(Condition.AND_OP, ", ");
    String title = "Povrede integriteta u tablici "+getTableName()+
                   " za podatak "+wae;

    errs = new dlgErrors(mp.getWindow(), title, usr.isRoot());
    errs.setData(new Column[] {
      dM.createStringColumn("TABLICA", "Tablica", 50),
      dM.createIntColumn("BROJ", "Redaka")
    });
    errs.setColumnWidth("BROJ", 10);
  }

  boolean keyAppearsImpl() {
    int rows;
    boolean ref = false;
    createErrorTrace();
    KreirDrop[] kd = tables == null ? KreirDrop.getModulesWithColumns(key) :
                     KreirDrop.getModulesByNames(tables);
    for (int i = 0; i < kd.length; i++) {
      raProcess.checkClosing();
      String tableName = kd[i].Naziv.toLowerCase();
      if (ignore == null || !ignore.contains(tableName)) {
        Condition cond = Condition.whereAllEqual(key, curr());
        if (filters.containsKey(tableName))
          cond = cond.and((Condition) filters.get(tableName));
        System.out.println(cond);
        if ((rows = kd[i].getRowCount(cond)) > 0) {
          errs.addError("Podatak referenciran u drugoj tablici",
                        new Object[] {kd[i].Naziv, new Integer(rows)});
          ref = true;
        }
      } else if (ignore != null && !kd[i].Naziv.equalsIgnoreCase(getTableName()))
        errs.addError("Tablica preskoèena kao nevažna",
           new Object[] {kd[i].Naziv, new Integer(-1)});
    }
    if (foreign != null)
    for (Iterator i = foreign.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry me = (Map.Entry) i.next();
      Condition cond = Condition.whereAllEqual(
          (String[]) me.getValue(), curr(), key);
      System.out.println(cond);
      KreirDrop kdp = KreirDrop.getModuleByName((String) me.getKey());
      if ((rows = kdp.getRowCount(cond)) > 0) {
        errs.addError("Podatak referenciran u drugoj tablici",
                      new Object[] {kdp.Naziv, new Integer(rows)});
        ref = true;
      }
    }
    return ref;
  }
}

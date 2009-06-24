/****license*****************************************************************
**   file: DataSetComparator.java
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
 * Created on 2005.02.10
 */
package hr.restart.util;

import hr.restart.baza.dM;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;


/**
 * @author abf
 */
public class DataSetComparator {

  private static final int MAX_VALUE_LENGTH = 200;
  
  lookupData ld = lookupData.getlookupData();
  
  private boolean strictCols, horiz;
  private String[] keyCols, compareCols, c1c, c2c;
  private Map keyValues1, keyValues2;
  StorageDataSet returnSet;
  
  public DataSetComparator() {
    strictCols = true;
    horiz = true;
  }
  
  public DataSetComparator(boolean forceAllCols, boolean horizontal) {
    strictCols = forceAllCols;
    horiz = horizontal;
  }
  
  public DataSetComparator(boolean forceAllCols) {
    strictCols = forceAllCols;
    horiz = true;
  }
  
  public void setForceAllColumns(boolean force) {
    strictCols = force;
  }
  
  public void setHorizontalResult(boolean horizontal) {
    horiz = horizontal;
  }
  
  public StorageDataSet compare(DataSet one, DataSet two, String keyColumn) {
    return compare(one, two, new String[] {keyColumn});
  }
  public StorageDataSet compare(DataSet one, DataSet two, String[] keyColumns) {
    return compare(one, two, keyColumns, null);
  }
  public StorageDataSet compare(DataSet one, DataSet two, String[] keyColumns, String[] compCols) {
    keyCols = keyColumns;
    if (compCols == null) {
      checkCompatibility(one, two);
    } else {
      compareCols = compCols;
    }
    
    returnSet = horiz ? createHorizontalReturnSet(one, two) : createReturnSet();
    
    keyValues1 = getAllKeys(one);
    keyValues2 = getAllKeys(two);
      
    Set s1v = new HashSet(keyValues1.keySet()); 
    s1v.removeAll(keyValues2.keySet());
    Set s2v = new HashSet(keyValues2.keySet()); 
    s2v.removeAll(keyValues1.keySet());
    
    fillMissingRows(s1v, keyValues1, "VRI1", "Dodatni red u prvom DataSet-u", one);
    fillMissingRows(s2v, keyValues2, "VRI2", "Dodatni red u drugom DataSet-u", two);
    
    keyValues1.keySet().retainAll(keyValues2.keySet());
    for (Iterator i = new TreeSet(keyValues1.keySet()).iterator(); i.hasNext(); ) {
      String key = (String) i.next();
      String[] vals1 = (String[]) keyValues1.get(key);
      String[] vals2 = (String[]) keyValues2.get(key);
      if (ld.raLocate(one, keyCols, vals1) && ld.raLocate(two, keyCols, vals2))
        compareRows(one, two, vals1);
      else System.err.println("WEIRD bug! Key not found: "+key);
    }
    
    return returnSet;
  }
  
  Variant v1 = new Variant();
  Variant v2 = new Variant();
  private void compareRows(ReadRow one, ReadRow two, String[] rowKeyValues) {
    int diff = 0;
    for (int i = 0; i < compareCols.length; i++) {
      String cname = compareCols[i];
      one.getVariant(cname, v1);
      two.getVariant(cname, v2);
      //System.out.println(v1 + "  " + v2);
      if (!v1.equals(v2)) {
        //System.out.println("diff found");
        if (horiz) {
          if (diff == 0) {
            returnSet.insertRow(false);
            for (int c = 0; c < returnSet.getColumnCount(); c++)
              returnSet.setUnassignedNull(c);
            dM.copyColumns(one, returnSet, keyCols);
            DataSet.copyTo(compareCols, one, c1c, returnSet);
          }
          returnSet.setVariant(cname + "2", (Variant) v2.clone());
          ++diff;
        } else {
          returnSet.insertRow(false);
          VarStr kv = new VarStr();
          for (int c = 0; c < keyCols.length; c++)
            kv.append(keyCols[c]).append('=').append(rowKeyValues[c]).append(
                " ");
          returnSet.setString("OPIS", "Red: " + kv);
          returnSet.setString("KOLONA", cname);
          returnSet.setString("VRI1", v1.toString());
          returnSet.setString("VRI2", v2.toString());
          returnSet.post();
        }
      }
    }
    if (diff > 0) {
      returnSet.setString("OPIS", "Razlièitih vrijednosti u redu: " + diff);
      returnSet.post();
    }
  }
  
  private void fillMissingRows(Set extraKeys, Map rows, String col, String opis, DataSet ds) {
    for (Iterator it = new TreeSet(extraKeys).iterator(); it.hasNext(); ) {
      String[] vals = (String[]) rows.get(it.next());
      returnSet.insertRow(false);
      returnSet.setString("OPIS", opis);
      if (horiz) {
        for (int i = 1; i < returnSet.getColumnCount(); i++)
          returnSet.setUnassignedNull(i);
        ld.raLocate(ds, keyCols, vals);
        dM.copyColumns(ds, returnSet, keyCols);
        DataSet.copyTo(compareCols, ds, col.equals("VRI1") ? c1c : c2c, returnSet);
      } else {
        VarStr kv = new VarStr();
        for (int i = 0; i < keyCols.length; i++)
          kv.append(keyCols[i]).append('=').append(vals[i]).append(" ");
        returnSet.setString(col, kv.truncate(MAX_VALUE_LENGTH).toString());
      }
      returnSet.post();
    }
  }
  
  public static String getMainColumn() {
    return "OPIS";
  }
  
  public static String getMainCaption() {
    return "Opis razlike";
  }
  
  static final String[] otherCols = {"KOLONA", "VRI1", "VRI2"};
  public static String[] getOtherColumns() {
    return otherCols;
  }
  
  private StorageDataSet createReturnSet() {
    StorageDataSet ret = new StorageDataSet();
    ret.setColumns(new Column[] {
        dM.createStringColumn("OPIS", "Opis razlike", MAX_VALUE_LENGTH),
        dM.createStringColumn("KOLONA", "Kolona", 20),
        dM.createStringColumn("VRI1", "Prvi set", MAX_VALUE_LENGTH),
        dM.createStringColumn("VRI2", "Drugi set", MAX_VALUE_LENGTH),
    });
    ret.open();
    return ret;
  }
  
  private StorageDataSet createHorizontalReturnSet(DataSet one, DataSet two) {
    StorageDataSet ret = new StorageDataSet();
    ret.setColumns(new Column[] {
        dM.createStringColumn("OPIS", "Opis razlike", MAX_VALUE_LENGTH)
    });
    for (int i = 0; i < keyCols.length; i++)
      ret.addColumn((Column) one.getColumn(keyCols[i]).clone());
    c1c = new String[compareCols.length];
    c2c = new String[compareCols.length];
    for (int i = 0; i < compareCols.length; i++) {
      Column c1 = (Column) one.getColumn(compareCols[i]).clone();
      c1.setColumnName(c1.getColumnName() + "1");
      c1.setCaption("1." + c1.getCaption());
      ret.addColumn(c1);
      c1c[i] = c1.getColumnName();
      
      Column c2 = (Column) two.getColumn(compareCols[i]).clone();
      c2.setColumnName(c2.getColumnName() + "2");
      c2.setCaption("2." + c2.getCaption());
      ret.addColumn(c2);
      c2c[i] = c2.getColumnName();
    }
    ret.open();
    return ret;
  }
  
  Variant v = new Variant();
  private String[] getKeyValues(ReadRow row) {
    String[] vals = new String[keyCols.length];
    for (int i = 0; i < keyCols.length; i++) {
      row.getVariant(keyCols[i], v);
      vals[i] = v.toString();
    }
    return vals;
  }
  
  private Map getAllKeys(DataSet ds) {
    Map ret = new HashMap();    
    for (ds.first(); ds.inBounds(); ds.next()) {
      String[] vals = getKeyValues(ds);
      ret.put(VarStr.join(vals, "-:-").toString(), vals);
    }
    return ret;
  }
  
  private Set getVisibleColumns(DataSet ds) {
    Set cols = new HashSet();
    for (int i = 0; i < ds.getColumnCount(); i++)
      if (ds.getColumn(i).getVisible() != 0)
        cols.add(ds.getColumn(i).getColumnName().toUpperCase());
    return cols;
  }
  
  private void checkCompatibility(DataSet one, DataSet two) {
    Set s1 = getVisibleColumns(one);
    Set s2 = getVisibleColumns(two);

    Set s1v = new HashSet(s1); 
    s1v.removeAll(s2);  
    Set s2v = new HashSet(s2); 
    s2v.removeAll(s1);

    if (s1v.size() + s2v.size() > 0 && strictCols) {
      if (s1v.size() > 0) System.err.println("First DataSet extra columns: "+s1v);
      if (s2v.size() > 0) System.err.println("Second DataSet extra columns: "+s2v);
      throw new IllegalArgumentException("DataSets are not column-compatible");
    }
        
    for (int i = 0; i < keyCols.length; i++) {
      String col = keyCols[i].toUpperCase();
      if (!s1.contains(col))
        throw new IllegalArgumentException("First DataSets missing key column: "+col);
      if (!s2.contains(col))
        throw new IllegalArgumentException("Second DataSets missing key column: "+col);
    }
    
    s1.retainAll(s2);
    for (Iterator i = s1.iterator(); i.hasNext(); ) {      
      Column c1 = one.getColumn((String) i.next());
      if (c1.getDataType() != two.getColumn(c1.getColumnName()).getDataType()) {
        System.err.println("Column " + c1.getColumnName() + ":");
        System.err.println("  - first DataSet, type = " + Variant.typeName(c1.getDataType()));
        System.err.println("  - second DataSet, type = " + 
            Variant.typeName(two.getColumn(c1.getColumnName()).getDataType()));
        if (strictCols)
          throw new IllegalArgumentException("Column " + c1.getColumnName() + " is not compatible");
        i.remove();
      }
    }
    compareCols = (String[]) s1.toArray(new String[s1.size()]);
    if (compareCols.length == 0) 
      throw new IllegalArgumentException("No compatible columns in DataSets");
    System.out.println("Comparing columns: "+VarStr.join(compareCols, ' '));
  }
}

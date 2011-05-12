/****license*****************************************************************
**   file: raSelectTableModifier.java
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


import hr.restart.baza.Condition;
import hr.restart.sisfun.frmParam;
import hr.restart.util.VarStr;

import java.awt.Color;
import java.util.HashSet;
import java.util.TooManyListenersException;

import javax.swing.JComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.DataSetView;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.RowFilterListener;
import com.borland.dx.dataset.RowFilterResponse;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;


public class raSelectTableModifier extends raTableModifier {
  private Color m = null;
  private Color g = Color.green.darker().darker().darker();
  private Variant v = new Variant();
  private int type;
  protected DataSet orig;
  protected HashSet sel;
  protected String colName;
  protected String[] key;
  
  public static final boolean space;
  static {
    space = "D".equalsIgnoreCase(frmParam.getParam("sisfun", "spaceSel", 
            "N", "Oznaèavanje redova samo sa razmaknicom (D,N)?", true));
  }
  
  private VarStr buf = new VarStr();
  
  private RowFilterListener selFilter = new RowFilterListener() {
    public void filterRow(ReadRow row, RowFilterResponse ret) {
      if (isSelected(row)) ret.add();
      else ret.ignore();
    }
  };
  
  private DataSetView view;

  private Color halfTone(Color cFrom, Color cTo, float factor) {
    return new Color((int) (cFrom.getRed() * (1 - factor) + cTo.getRed() * factor),
                     (int) (cFrom.getGreen() * (1 - factor) + cTo.getGreen() * factor),
                     (int) (cFrom.getBlue() * (1 - factor) + cTo.getBlue() * factor));
  }
  
  public raSelectTableModifier(String keyCol) {
    this(null, keyCol);
  }

  public raSelectTableModifier(DataSet ds, String keyCol) {    
    key = null;
    orig = ds;
    colName = keyCol;
    sel = new HashSet();
  }
  
  public raSelectTableModifier(DataSet ds, String[] keyCols) {
    orig = ds;
    key = keyCols;
    sel = new HashSet();
  }
  
  public void refreshKeyList() {
    if (key == null && orig != null) {
      HashSet nl = new HashSet();
      for (int i = 0; i < orig.getRowCount(); i++) {
        orig.getVariant(colName, i, v);
        if (type == Variant.INT)
          nl.add(new Integer(v.getInt()));
        else if (type == Variant.STRING)
          nl.add(v.getString());
        else if (type == Variant.TIMESTAMP)
          nl.add(v.getTimestamp());
        else if (type == Variant.SHORT)
          nl.add(new Short(v.getShort()));
      }
      nl.retainAll(sel);
      sel = nl;
    }
  }

  public boolean doModify() {
    if (key == null) {
      if (sel.isEmpty()) return false;
      if (colName.length() == 0) 
        return sel.contains(new Integer(getRow()));
      
      getDataSet().getVariant(colName,getRow(),v);
      if (type == Variant.INT)
        return sel.contains(new Integer(v.getInt()));
      else if (type == Variant.STRING)
        return sel.contains(v.getString());
      else if (type == Variant.TIMESTAMP)
        return sel.contains(v.getTimestamp());
      else if (type == Variant.SHORT)
        return sel.contains(new Short(v.getShort()));
      else return false;
    }
    if (sel.isEmpty()) return false;
    
    return sel.contains(getRowKey(getRow()));
  }

  public void clearSelection() {
    sel.clear();
  }
  
  private String getRowKey(int row) {
    if (key == null) return null;
    
    buf.clear();
    for (int i = 0; i < key.length; i++) {
      getDataSet().getVariant(key[i], row, v);
      buf.append(v).append('|');
    }
    return buf.chop().toString();
  }
  
  public String getRowKey(ReadRow row) {
    if (key == null) return null;
    
    buf.clear();
    for (int i = 0; i < key.length; i++) {
      row.getVariant(key[i], v);
      buf.append(v).append('|');
    }
    return buf.chop().toString();
  }
  
  public boolean isSelected(DataSet row) {
    if (key == null && colName.length() == 0) 
      return sel.contains(new Integer(row.getRow()));
    return isSelected((ReadRow) row);
  }

  public boolean isSelected(ReadRow row) {
    if (key == null) {
      if (sel.isEmpty()) return false;
      if (type == Variant.INT)
        return sel.contains(new Integer(row.getInt(colName)));
      else if (type == Variant.STRING)
        return sel.contains(row.getString(colName));
      else if (type == Variant.SHORT)
        return sel.contains(new Short(row.getShort(colName)));
      else if (type == Variant.TIMESTAMP)
        return sel.contains(row.getTimestamp(colName));
      return true;
    }
    if (sel.isEmpty()) return false;
    return sel.contains(getRowKey(row));
  }
  
  public void toggleSelection(DataSet row) {
    if (!isSelected(row)) addToSelection(row);
    else removeFromSelection(row);
  }

  public void toggleSelection(ReadRow row) {
    if (!isSelected(row)) addToSelection(row);
    else removeFromSelection(row);
  }
  
  public void addToSelection(DataSet row) {
    if (key == null && colName.length() == 0)
      sel.add(new Integer(row.getRow()));
    else addToSelection((ReadRow) row);
  }
  
  public void addToSelection(String list) {
    if (list.trim().length() == 0) return;
    
    type = orig.getColumn(colName).getDataType();
    String[] l = new VarStr(list).splitTrimmed(',');
    try {
      for (int i = 0; i < l.length; i++)
        if (type == Variant.INT)
          sel.add(new Integer(Integer.parseInt(l[i])));
        else if (type == Variant.STRING)
          sel.add(l[i]);
        else if (type == Variant.SHORT)
          sel.add(new Short(Short.parseShort(l[i])));
    } catch (RuntimeException e) {
      // ignore
    }
    refreshKeyList();
  }

  public void addToSelection(ReadRow row) {
    if (key == null) {
      if (sel.isEmpty())
        type = row.getColumn(colName).getDataType();
      if (type == Variant.INT)
        sel.add(new Integer(row.getInt(colName)));
      else if (type == Variant.STRING)
        sel.add(row.getString(colName));
      else if (type == Variant.SHORT)
        sel.add(new Short(row.getShort(colName)));
      else if (type == Variant.TIMESTAMP)
        sel.add(row.getTimestamp(colName));
    } else sel.add(getRowKey(row)); 
  }

  public int countSelected() {
    return sel.size();
  }
  
  public void removeFromSelection(DataSet row) {
    if (key == null && colName.length() == 0)
      sel.remove(new Integer(row.getRow()));
    else removeFromSelection((ReadRow) row);
  }

  public void removeFromSelection(ReadRow row) {
    if (key == null) {
      if (type == Variant.INT)
        sel.remove(new Integer(row.getInt(colName)));
      else if (type == Variant.STRING)
        sel.remove(row.getString(colName));
      else if (type == Variant.SHORT)
        sel.remove(new Short(row.getShort(colName)));
      else if (type == Variant.TIMESTAMP)
        sel.remove(row.getTimestamp(colName));
    } else sel.remove(getRowKey(row));
  }

  public Condition getSelectionCondition() {
    if (key == null) {
      if (sel.isEmpty()) return null;
      if (sel.size() > 500) return Condition.none;
      return Condition.in(colName, sel.toArray());
    }
    if (sel.isEmpty()) return null;
    return Condition.none;
  }
  
  public boolean isNatural() {
    return key == null && colName.length() == 0;
  }
  
  public Object[] getSelection() {
    if (key != null) return null;
    if (sel.isEmpty()) return null;
    
    if (type == Variant.INT || colName.length() == 0)
      return sel.toArray(new Integer[sel.size()]);
    else if (type == Variant.STRING)
      return sel.toArray(new String[sel.size()]);
    else if (type == Variant.SHORT)
      return sel.toArray(new Short[sel.size()]);
    else if (type == Variant.TIMESTAMP)
      return sel.toArray(new java.sql.Timestamp[sel.size()]);
    else return null;
  }
  
  public DataSet getSelectedView() {
    return getSelectedView(orig);
  }
  
  public DataSet getSelectedView(DataSet full) {
    if (view != null) {
      view.removeRowFilterListener(selFilter);
      view.close();
    }
    if (full instanceof StorageDataSet) {
      view = new DataSetView();
      view.setStorageDataSet((StorageDataSet) full);
      try {
        view.addRowFilterListener(selFilter);
      } catch (TooManyListenersException e) {
        e.printStackTrace();
      }
      view.open();
      view.refilter();
      return view;
    } 
    throw new IllegalArgumentException("DataSet is not StorageDataSet");
  }
  
  public void destroySelectedView() {
    if (view != null) {
      view.removeRowFilterListener(selFilter);
      view.close();
    }
  }
  
  public void modify() {
    JComponent jRenderComp = (JComponent)renderComponent;
//     renderComponent
    if (!isSelected()) {
      if (m == null)  m = halfTone(Color.yellow, jRenderComp.getBackground(), 0.75f);
      jRenderComp.setBackground(m);
//      jRenderComp.setForeground(getTable().getSelectionForeground());
    } else {
//      jRenderComp.setForeground(colorG);
      jRenderComp.setBackground(g);
    }
  }
}

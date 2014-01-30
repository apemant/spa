/****license*****************************************************************
**   file: raHideDataModifier.java
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
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.util.VarStr;
import hr.restart.zapod.raUserChangeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import bsh.EvalError;
import bsh.Interpreter;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.Variant;


public class raHideDataModifier extends raTableModifier {

  
  Map allvis = new HashMap();
  List nvis = new ArrayList();
  Interpreter bsh = new Interpreter();
  
  class VisibleCols {
    Set cols = new HashSet();
    String[] key = null;
    Set keyset = null;
    
    public VisibleCols(String spec, String data) {
      cols.addAll(new VarStr(spec).splitAsListTrimmed(','));
      int p = data.indexOf(" => ");
      if (p > 0) {
        String keys = data.substring(0, p).trim();
        if (keys.startsWith("[") && keys.endsWith("]"))
          keys = keys.substring(1, keys.length() - 1);
        key = new VarStr(keys).splitTrimmed(',');
        try {
          Object ret = bsh.eval(data.substring(p + 4));
          if (ret instanceof DataSet) {
            keyset = new HashSet();
            DataSet ds = (DataSet) ret;
            for (ds.first(); ds.inBounds(); ds.next())
              keyset.add(getRowKey(key, ds));
          } else key = null;
        } catch (EvalError e) {
          e.printStackTrace();
          key = null;
        }
      }
    }
    
    public String toString() {
      return cols + " for " + (key == null ? "" : VarStr.join(key, ',').append(" in ").append(keyset.toString()).toString()); 
    }
  }
  
  private VarStr buf = new VarStr();
  private Variant v = new Variant();
  
  public static raHideDataModifier inst = new raHideDataModifier();
  
  String getRowKey(String[] key, ReadRow row) {
    if (key == null) return null;
    
    buf.clear();
    for (int i = 0; i < key.length; i++) {
      row.getVariant(key[i], v);
      buf.append(v).append('|');
    }
    return buf.chop().toString();
  }
  
  String getRowKey(String[] key, int row) {
    if (key == null) return null;
    
    buf.clear();
    for (int i = 0; i < key.length; i++) {
      getDataSet().getVariant(key[i], row, v);
      buf.append(v).append('|');
    }
    return buf.chop().toString();
  }
  
  
  public raHideDataModifier() {
    DataSet ds = dM.getDataModule().getFunkcije();
    ds.open();
    for (ds.first(); ds.inBounds(); ds.next()) {
      if (ds.getString("SPEC").startsWith("table-visible:")) {
        String spec = ds.getString("SPEC").substring("table-visible:".length());
        allvis.put(ds.getString("CFUNC"), new VisibleCols(spec, ds.getString("DATA")));
      }
    }
    updateVisList();
    raUser.getInstance().addUserChangeListener(new raUserChangeListener() {
      public void userChanged(String oldUser, String newUser) {
        updateVisList();
      }
    });
    
  }
  
  void updateVisList() {
    nvis.clear();
    for (Iterator i = allvis.keySet().iterator(); i.hasNext(); ) {
      String func = (String) i.next();
      if (!raUser.getInstance().canAccessFunction(func, "P"))
        nvis.add(func);
    }    
  }
  
  
  public boolean doModify() {
    if (nvis == null || nvis.size() == 0) return false;
    
    for (Iterator i = nvis.iterator(); i.hasNext(); ) {
      VisibleCols vc = (VisibleCols) allvis.get(i.next());
      if (vc == null) return false;
      
      if (!vc.cols.contains(((JraTable2) getTable()).getRealColumnName(getColumn()))) return false;

      if (vc.key == null) return true;
      
      for (int c = 0; c < vc.key.length; c++)
        if (getDataSet().hasColumn(vc.key[c]) == null) return false;
      if (vc.keyset.contains(getRowKey(vc.key, getRow()))) return true;
    }
    return false;
  }

  public void modify() {
    setComponentText("...");
  }
}
/****license*****************************************************************
**   file: norme.java
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
package hr.restart.baza;

import hr.restart.util.Aus;
import hr.restart.util.HashDataSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.borland.dx.dataset.DataModule;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.sql.dataset.QueryDataSet;

public class norme extends KreirDrop implements DataModule {
  
	private static norme inst = new norme();
	
	private Map normap;
	
	int dsSerial = -1;
	
	HashDataSet aktivhash;
  
  /*QueryDataSet normesorted = new raDataSet();
  
  {
  	createFilteredDataSet(normesorted, "");
  	Aus.setFilter(normesorted, "SELECT * FROM norme ORDER BY cartnor");
    //normesorted.setSort(new SortDescriptor(new String[] {"CARTNOR"}));
  	
  }*/
  
  public static norme getDataModule() {
    return inst;
  }

/*  public com.borland.dx.sql.dataset.QueryDataSet getSorted() {
    return normesorted;
  }*/
  
  private static void checkHash() {
    int now = dM.getSynchronizer().getSerialNumber("NORME");
    if (now != inst.dsSerial || inst.aktivhash == null) {
      inst.dsSerial = now;
      inst.normap = new HashMap();
      QueryDataSet ds = inst.getQueryDataSet();
      ds.open();
      for (ds.first(); ds.inBounds(); ds.next()) {
        Integer key = Integer.valueOf(ds.getInt("CARTNOR"));
        List rows = (List) inst.normap.get(key);
        if (rows == null) inst.normap.put(key, rows = new ArrayList());
        rows.add(new Row(ds));
      }
      inst.aktivhash = new HashDataSet(inst.getQueryDataSet(), new String[] {"CARTNOR", "CART"});
    }
  }
  
  public static boolean check(int norm) {
    checkHash();
    return inst.normap.containsKey(Integer.valueOf(norm));
  }
  
  public static int count(int norm) {
    if (!check(norm)) return 0;
    return ((List) inst.normap.get(Integer.valueOf(norm))).size();
  }
  
  public static BigDecimal kol(int norm, int rbr) {
    if (!check(norm)) return BigDecimal.ZERO;
    return ((Row) ((List) inst.normap.get(Integer.valueOf(norm))).get(rbr)).kol;
  }
  
  public static int cart(int norm, int rbr) {
    if (!check(norm)) return -1;
    List rows = (List) inst.normap.get(Integer.valueOf(norm));
    if (rbr < 0 || rbr >= rows.size()) return -1;
    return ((Row) rows.get(rbr)).cart;
  }
  
  public static ReadRow art(int norm, int rbr) {
    int cart = cart(norm, rbr);
    if (cart < 0 || !inst.aktivhash.loc(new String[] {Integer.toString(norm), Integer.toString(cart)})) return null;
    return inst.aktivhash.get();
  }

  public boolean isAutoRefresh() {
    return true;
  }
  
  public static class Row {
    int cart;
    BigDecimal kol;
    
    public Row(ReadRow ds) {
      cart = ds.getInt("CART");
      kol = ds.getBigDecimal("KOL");
    }
  }
}

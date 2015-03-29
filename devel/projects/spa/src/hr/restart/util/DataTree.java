/****license*****************************************************************
**   file: DataTree.java
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

import hr.restart.baza.Condition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Locate;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.StorageDataSet;

public class DataTree implements Comparable {
	 public String key;
   public ArrayList branches;
   public DataTree(String root) {
     key = root;
     branches = null;
   }
   
   public void addBranch(DataTree branch) {
     if (branches == null) branches = new ArrayList();
     branches.add(branch);
   }
   
   public List getBranches() {
     return branches;
   }
   
   public boolean isLeaf() {
     return branches == null || branches.size() == 0;
   }
   
   public void fill(Collection ret) {
     if (ret.add(key) && branches != null)
       for (Iterator i = branches.iterator(); i.hasNext(); )
         ((DataTree) i.next()).fill(ret);
   }
   
   public void sortDeep() {
     if (branches != null) {
    	 ArrayList br = branches;
       Collections.sort(br);
       branches = null;
       for (Iterator i = br.iterator(); i.hasNext(); )
         ((DataTree) i.next()).sortDeep();
       branches = br;
     }
   }
   
   public void dump() {
     System.out.print(key);
     if (!isLeaf()) {
       System.out.print(": [");
       for (Iterator i = branches.iterator(); i.hasNext(); )
         ((DataTree) i.next()).dump();
       System.out.print("]");
     }
     System.out.print(", ");
   }
   
   public boolean equals(Object obj) {
     return (obj instanceof DataTree && ((DataTree) obj).key.equals(key));
   }
   
   public int hashCode() {
     return key.hashCode();
   }
   
   public int compareTo(Object o) {
     return key.compareTo(((DataTree) o).key);
   }
   
   
   public static DataTree getSubTree(String root, DataSet ds, String keyCol, String pripCol) {
     ds.open();
     
     HashMap prips = new HashMap();
     for (ds.first(); ds.inBounds(); ds.next()) {
       String prip = ds.getString(pripCol);
       String key = ds.getString(keyCol);
       
       DataTree t = (DataTree) prips.get(key);
       if (t == null) prips.put(key, t = new DataTree(key));

       if (prip.equals(key)) continue;
       
       DataTree sub = (DataTree) prips.get(prip);
       if (sub == null) prips.put(prip, sub = new DataTree(prip));
       sub.addBranch(t);    
     }
     
     return (DataTree) prips.get(root);
   }
   
   public static Condition getSubCond(String root, DataSet ds, String keyCol, String pripCol) {
  	 DataTree t = getSubTree(root, ds, keyCol, pripCol);
  	 if (t == null) return Condition.nil;
  	 
  	 HashSet keys = new HashSet();
   	 t.fill(keys);
   	 return Condition.in(keyCol, keys);
   }
   
   public static void fillSubSet(DataSet out, String root, DataSet ds, String keyCol, String pripCol) {
  	 DataTree t = DataTree.getSubTree(root, ds, keyCol, pripCol);
  	 
  	 if (t != null) {
  		 HashDataSet ho = new HashDataSet(ds, keyCol);
  		 
  		 ArrayList all = new ArrayList();
       t.sortDeep();
       t.fill(all);
       for (Iterator i = all.iterator(); i.hasNext(); ) {
         out.insertRow(false);
         ho.get(i.next()).copyTo(out);
       }
  	 }
   }
   
   public static StorageDataSet getSubSet(String root, DataSet ds, String keyCol, String pripCol) {
  	 StorageDataSet out = new StorageDataSet();
  	 out.setColumns(((StorageDataSet) ds).cloneColumns());
  	 out.open();
  	 
  	 fillSubSet(out, root, ds, keyCol, pripCol);
  	 return out;
   }
   
   public static boolean isCircular(ReadRow row, DataSet ds, String keyCol, String pripCol) {
  	 return isCircular(row.getString(keyCol), row.getString(pripCol), ds, keyCol, pripCol);
   }
   
   public static boolean isCircular(String key, String prip, DataSet ds, String keyCol, String pripCol) {
  	 if (key.equals(prip)) return false;
  	 ds.open();
  	 
  	 DataRow row = new DataRow(ds, keyCol);
  	 row.setString(keyCol, prip);
  	 while (ds.locate(row, Locate.FIRST)) {
  		 if (ds.getString(keyCol).equals(ds.getString(pripCol))) return false;
  		 if (ds.getString(pripCol).equals(key)) return true;
  		 row.setString(keyCol, ds.getString(pripCol)); 
  	 }
  	 return false;
   }
}

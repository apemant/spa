/****license*****************************************************************
**   file: raVlasnikColumnModifier.java
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
package hr.restart.rn;

import hr.restart.baza.Kupci;
import hr.restart.baza.Partneri;
import hr.restart.baza.dM;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Util;
import hr.restart.util.lookupData;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raVlasnikColumnModifier extends raTableModifier {
  dM dm = dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
  Variant v = new Variant();
  
  public static final long refreshInterval = 20000;
  public long lastRefresh = 0;
  boolean caching = false;
  
  Map cachePar, cacheKup;
  
  int serialPar = -1, serialKup = -1;
  
  private synchronized boolean tasCaching() {
    if (!caching) return caching = true;
    return false;
  }
  
  synchronized void stopCaching() {
    caching = false;
  }
  
  void preCacheValues() {
  	System.out.println("Precaching...");
 
    int serPar = dm.getSynchronizer().getSerialNumber("Partneri");
    if (serPar != serialPar || cachePar == null) {
    	serialPar = serPar;
    	Map cache = new HashMap();
    	StorageDataSet par = Partneri.getDataModule().getScopedSet("cpar nazpar");
    	Util.fillAsyncData(par, "SELECT cpar, nazpar FROM partneri");
    	for (par.first(); par.inBounds(); par.next())
    		cache.put(new Integer(par.getInt("CPAR")),
    				par.getInt("CPAR") + " - " + par.getString("NAZPAR"));

    	System.out.println("cached " + par.rowCount() + " partners");
    	cachePar = cache;
    }
    
    int serKup = dm.getSynchronizer().getSerialNumber("Kupci");
    if (serKup != serialKup || cacheKup == null) {
    	serialKup = serKup;
    	Map cache = new HashMap();
    	StorageDataSet kup = Kupci.getDataModule().getScopedSet("ckupac ime prezime");
    	Util.fillAsyncData(kup, "SELECT ckupac, ime, prezime FROM kupci");
      for (kup.first(); kup.inBounds(); kup.next())
        cache.put(new Integer(kup.getInt("CKUPAC")), kup.getInt("CKUPAC") + 
        		" - " + kup.getString("IME") + " " + kup.getString("PREZIME"));
      
      System.out.println("cached " + kup.rowCount() + " kupacs");
      cacheKup = cache;
    }
  }
  
  public boolean doModify() {
    if (getTable() instanceof JraTable2) {
      JraTable2 jtab = (JraTable2)getTable();
      Column dsCol = jtab.getDataSetColumn(getColumn());
      if (dsCol == null) return false;
      return "CKUPAC".equalsIgnoreCase(dsCol.getColumnName());
    }
    return false;
  }
  
  public void modify() {
  	
  	if (System.currentTimeMillis() - lastRefresh > refreshInterval) {
      lastRefresh = System.currentTimeMillis();
      if (tasCaching()) {
        new Thread(new Runnable() {
          public void run() {
            try {
              preCacheValues();
            } finally {
              stopCaching();
            }
          }
        }).start();
      }
    }
  	
  	DataSet ds = ((JraTable2)getTable()).getDataSet();
    ds.getVariant("KUPPAR",getRow(),v);
    Map cache = "K".equalsIgnoreCase(v.getString()) ? cacheKup : cachePar;
    if (cache != null) {
      ds.getVariant("CKUPAC", getRow(), v);
      String rep = (String) cache.get(new Integer(v.getInt()));
      if (rep != null) setComponentText(rep);
    }
    if (renderComponent instanceof JLabel)
      ((JLabel)renderComponent).setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
  }
  
  public void setComponentText(String txt) {
    if (renderComponent instanceof JLabel) {
      ((JLabel)renderComponent).setText(txt);
    } else if (renderComponent instanceof JTextComponent) {
      ((JTextComponent)renderComponent).setText(txt);
    }
  }
  
  public int getMaxModifiedTextLength() {
    return 30;
  }
}

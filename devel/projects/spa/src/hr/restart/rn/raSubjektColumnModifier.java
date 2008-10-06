/****license*****************************************************************
**   file: raSubjektColumnModifier.java
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

import hr.restart.baza.RN_sifznac;
import hr.restart.baza.RN_subjekt;
import hr.restart.baza.RN_vrsub;
import hr.restart.baza.RN_znacsub;
import hr.restart.baza.dM;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Stopwatch;
import hr.restart.util.Util;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
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

public class raSubjektColumnModifier extends raTableModifier {
  dM dm = dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
  Variant v = new Variant();
  VarStr t = new VarStr();
  Set sif = new HashSet();
  //Map doh = new HashMap();
  String mainCol;
  boolean replace, displayVrsub;
  /*DataRow srchSub = new DataRow(dm.getRN_subjekt(), "CSUBRN");
  DataRow srchVrsub = new DataRow(dm.getRN_vrsub(), "CVRSUBJ");
  DataRow srchZnacsub = new DataRow(dm.getRN_znacsub(), 
      new String[] {"CSUBRN", "CVRSUBJ", "CZNAC", "CRADNAL"});
  DataRow srchSifznac = new DataRow(dm.getRN_sifznac(),
      new String[] {"CVRSUBJ", "CZNAC", "VRIZNAC"});
  DataRow resultSub = new DataRow(dm.getRN_subjekt());
  DataRow resultVrsub = new DataRow(dm.getRN_vrsub());
  DataRow resultZnacsub = new DataRow(dm.getRN_znacsub());
  DataRow resultSifznac = new DataRow(dm.getRN_sifznac(), "OPIS");*/
  
  public static final long refreshInterval = 20000;
  public long lastRefresh = 0;
  boolean caching = false;
  
  Map oldCache;
  
  int serialVrsub = -1, serialSub = -1, serialSif = -1, serialZnac = -1;
  
  Map vrsubs, sifs;
   
  String getZnacKey(ReadRow row) {
    return row.getString("CSUBRN") + "-" + row.getShort("CZNAC") + "-" + 
          row.getString("CRADNAL");
  }
  
  String getSifKey(ReadRow row) {
    return row.getShort("CVRSUBJ") + "-" +
      row.getShort("CZNAC") + "-" + row.getString("VRIZNAC");
  }
  
  private synchronized boolean tasCaching() {
    if (!caching) return caching = true;
    return false;
  }
  
  synchronized void stopCaching() {
    caching = false;
  }
  
  void preCacheValues() {
    Stopwatch tim = Stopwatch.start("caching");
    
    int serVrsub = -1, serSif, serSub, serZnac;
    boolean needRemap = oldCache == null;
    
    if (displayVrsub) {
      serVrsub = dm.getSynchronizer().getSerialNumber("RN_vrsub");
      if (serVrsub != serialVrsub || vrsubs == null) {
      	serialVrsub = serVrsub;
      	vrsubs = new HashMap();
      	StorageDataSet vrsub = RN_vrsub.getDataModule().getScopedSet("cvrsubj nazvrsubj");
      	Util.fillAsyncData(vrsub, "SELECT cvrsubj, nazvrsubj FROM RN_vrsub");
      	for (vrsub.first(); vrsub.inBounds(); vrsub.next())
      		vrsubs.put(new Short(vrsub.getShort("CVRSUBJ")), 
      				vrsub.getString("NAZVRSUBJ"));
      }
    }
    
    serSif = dm.getSynchronizer().getSerialNumber("RN_sifznac");
    if (serSif != serialSif) {
    	serialSif = serSif;
    	sifs = new HashMap();
    	StorageDataSet sif = RN_sifznac.getDataModule().getScopedSet("cvrsubj cznac vriznac opis");
    	Util.fillAsyncData(sif, "SELECT cvrsubj, cznac, vriznac, opis FROM RN_sifznac");
    	for (sif.first(); sif.inBounds(); sif.next())
    		sifs.put(getSifKey(sif), sif.getString("OPIS"));
    }
    
    serSub = dm.getSynchronizer().getSerialNumber("RN_subjekt");
    if (serSub != serialSub) needRemap = true;
    serialSub = serSub;

    serZnac = dm.getSynchronizer().getSerialNumber("RN_znacsub");
    if (serZnac != serialZnac) needRemap = true;
    serialZnac = serZnac;
    
    tim.report("checked: " + needRemap);
    
    if (!needRemap) return;

    Map newCache = new HashMap();
    Map vrsubj = new HashMap();
    StorageDataSet subj = RN_subjekt.getDataModule().getScopedSet("csubrn cvrsubj broj");
    Util.fillAsyncData(subj, "SELECT csubrn, cvrsubj, broj FROM RN_subjekt");
    for (subj.first(); subj.inBounds(); subj.next()) {
    	if (displayVrsub) newCache.put(subj.getString("CSUBRN"),
    			vrsubs.get(new Short(subj.getShort("CVRSUBJ"))) + 
    				" - " + subj.getString("BROJ"));
    	else newCache.put(subj.getString("CSUBRN"), subj.getString("BROJ"));
    	vrsubj.put(subj.getString("CSUBRN"), new Short(subj.getShort("CVRSUBJ")));
    }
    
    tim.report("subjekt finished");

    StorageDataSet znac = RN_znacsub.getDataModule().getReadonlySet();
    Util.fillAsyncData(znac, "SELECT * FROM RN_znacsub");
    for (znac.first(); znac.inBounds(); znac.next()) {
    	if (znac.getShort("CZNAC") <= 5 && vrsubj.containsKey(znac.getString("CSUBRN"))
          && ((Short) vrsubj.get(znac.getString("CSUBRN"))).shortValue() == 
          	znac.getShort("CVRSUBJ")) {
        /*if (isDoh(znacRow)) {
          String val = getForeignValue(znacRow);
          if (val != null) newCache.put(getZnacKey(znacRow), val);
        } else*/
    		String val = null;
    		if (isSif(znac)) val = (String) sifs.get(getSifKey(znac));
    		if (val == null) val = znac.getString("VRIZNAC");
    		newCache.put(getZnacKey(znac), val);
      }
    }
    tim.report("finished caching");
    oldCache = newCache;
  }
  
  public raSubjektColumnModifier()  {
    this("CSUBRN", false);
  }
  
  public raSubjektColumnModifier(String column, boolean replace)  {
  	mainCol = column;
    this.replace = replace;
    DataSet ds = dm.getRN_znacajke();
    ds.open();
    for (ds.first(); ds.inBounds(); ds.next()) {
      /*if (ds.getString("ZNACDOH").length() > 0)
        doh.put(ds.getShort("CVRSUBJ") + ":" + ds.getShort("CZNAC"), 
            new ForeignValueExtractor(ds.getString("ZNACDOH")));
      else */
    	if (ds.getString("ZNACSIF").equalsIgnoreCase("D"))
        sif.add(ds.getShort("CVRSUBJ") + ":" + ds.getShort("CZNAC"));
    }
  }
  
  public void setDisplayVrsub(boolean display) {
    displayVrsub = display;
  }
  
/*  private boolean isDoh(ReadRow znac) {
    return doh.containsKey(znac.getShort("CVRSUBJ") + ":" + znac.getShort("CZNAC"));
  }
*/  
  private boolean isSif(ReadRow znac) {
    return sif.contains(znac.getShort("CVRSUBJ") + ":" + znac.getShort("CZNAC"));
  }
  
  /*private String getForeignValue(ReadRow znac) {
    return ((ForeignValueExtractor) doh.get(znac.getShort("CVRSUBJ") + 
        ":" + znac.getShort("CZNAC"))).getValue(znac.getString("VRIZNAC"));
  }*/
  
  public boolean doModify() {
    if (getTable() instanceof JraTable2) {
      JraTable2 jtab = (JraTable2)getTable();
      Column dsCol = jtab.getDataSetColumn(getColumn());
      if (dsCol == null) return false;
      return mainCol.equalsIgnoreCase(dsCol.getColumnName());
    }
    return false;
  }
  
  public void modify() {
    ((JraTable2)getTable()).getDataSet().getVariant(mainCol, getRow(), v);
    String cs = v.toString();
    String crn = "";
    if (((JraTable2)getTable()).getDataSet().hasColumn("CRADNAL") != null) {
      ((JraTable2)getTable()).getDataSet().getVariant("CRADNAL", getRow(), v);
      crn = v.toString();
    }
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
    if (oldCache != null) {
      t.clear().append(cs).append(" - ");
      Object sval = (String) oldCache.get(cs);
      if (sval != null) {
        t.append(sval);
        for (int i = 1; i <= 5; i++) {
          Object zval = (String) oldCache.get(cs + "-" + i + "-" + crn);
          if (zval != null)
            t.append(" - ").append(zval);
        }
      }
      setComponentText(t.toString());
      if (renderComponent instanceof JLabel)
        ((JLabel)renderComponent).setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
      return;
    }
    //System.out.println(crn);
/*    synchronized (subLock) {
      dm.getRN_subjekt().open();
    }
    boolean foundSub = false;
    try {
      srchSub.setString("CSUBRN", cs);
      foundSub = dm.getRN_subjekt().lookup(srchSub, resultSub, Locate.FIRST);
    } catch (Exception e) {
      srchSub = new DataRow(dm.getRN_subjekt(), "CSUBRN");
      srchSub.setString("CSUBRN", cs);
      resultSub = new DataRow(dm.getRN_subjekt());
      foundSub = dm.getRN_subjekt().lookup(srchSub, resultSub, Locate.FIRST);
    }
    if (foundSub) {
      short vs = resultSub.getShort("CVRSUBJ");
      t.clear().append(cs).append(" - ");
      if (replace) t.clear();
      if (displayVrsub) {
        synchronized (vrsubLock) {
          dm.getRN_vrsub().open();
        }
        boolean foundVrsub = false;
        try {
          srchVrsub.setShort("CVRSUBJ", vs);
          foundVrsub = dm.getRN_vrsub().lookup(srchVrsub, resultVrsub, Locate.FIRST);
        } catch (Exception e) {
          srchVrsub = new DataRow(dm.getRN_vrsub(), "CVRSUBJ");
          srchVrsub.setShort("CVRSUBJ", vs);
          resultVrsub = new DataRow(dm.getRN_vrsub());
          foundVrsub = dm.getRN_vrsub().lookup(srchVrsub, resultVrsub, Locate.FIRST);
        }
        if (foundVrsub)
          t.append(resultVrsub.getString("NAZVRSUBJ")).append(" - ");
      }
      t.append(resultSub.getString("BROJ"));
      synchronized (sifLock) {
        dm.getRN_sifznac().open();
      }
      synchronized (znacLock) {
        dm.getRN_znacsub().open();
      }
      srchZnacsub = new DataRow(dm.getRN_znacsub(), 
          new String[] {"CSUBRN", "CVRSUBJ", "CZNAC", "CRADNAL"});
      srchSifznac = new DataRow(dm.getRN_sifznac(),
          new String[] {"CVRSUBJ", "CZNAC", "VRIZNAC"});
      resultZnacsub = new DataRow(dm.getRN_znacsub());
      resultSifznac = new DataRow(dm.getRN_sifznac(), "OPIS");
      
      srchZnacsub.setString("CSUBRN", cs);
      srchZnacsub.setShort("CVRSUBJ", vs);
      srchZnacsub.setString("CRADNAL", crn);
      for (int i = 1; i <= 5; i++) {
        srchZnacsub.setShort("CZNAC", (short) i);
        if (dm.getRN_znacsub().lookup(srchZnacsub, resultZnacsub, Locate.FIRST)) {
          if (isDoh(resultZnacsub)) {
            String val = getForeignValue(resultZnacsub);
            if (val != null) t.append(" - ").append(val);
          } else if (isSif(resultZnacsub)) {
            srchSifznac.setShort("CVRSUBJ", vs);
            srchSifznac.setShort("CZNAC", (short) i);
            srchSifznac.setString("VRIZNAC", resultZnacsub.getString("VRIZNAC"));
            if (dm.getRN_sifznac().lookup(srchSifznac, resultSifznac, Locate.FIRST))
              t.append(" - ").append(resultSifznac.getString("OPIS"));
          } else t.append(" - ").append(resultZnacsub.getString("VRIZNAC"));
        }
      }
      setComponentText(t.toString());
    }
    if (renderComponent instanceof JLabel)
      ((JLabel)renderComponent).setHorizontalAlignment(javax.swing.SwingConstants.LEADING); */
  }
  public void setComponentText(String txt) {
    if (renderComponent instanceof JLabel) {
      ((JLabel)renderComponent).setText(txt);
    } else if (renderComponent instanceof JTextComponent) {
      ((JTextComponent)renderComponent).setText(txt);
    }
  }
  public int getMaxModifiedTextLength() {
    return 40;
  }
  
  /*class ForeignValueExtractor {
    DataSet ds;
    String keyCol, valueCol;
    public ForeignValueExtractor(String def) {
      if (def.indexOf(":") > 0)
        def = def.substring(0, def.indexOf(":"));
      String parts[] = new VarStr(def).splitTrimmed('+');
      ds = null;
      try {
        java.lang.reflect.Method m = dM.class.getMethod(parts[2], null);
        ds = (DataSet) m.invoke(dM.getDataModule(), null);
      } catch (Exception e) {}
      keyCol = parts[0];
      valueCol = parts[1];
    }
    
    public String getValue(String key) {
      if (ds == null || key == null || key.length() == 0) return null;
      DataRow vri = null;
      try {
        vri = ld.raLookup(ds, keyCol, key);
      } catch (Exception e) {
        return null;
      }

      if (vri == null) return null;
      vri.getVariant(valueCol, v);
      return v.toString();
    }
  }*/
}

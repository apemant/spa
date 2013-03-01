/****license*****************************************************************
**   file: PartnerCache.java
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
package hr.restart.sk;

import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;

import java.util.HashMap;
import java.util.Map;

import com.borland.dx.dataset.DataSet;

public class PartnerCache {
  Map cache = null;
  static int nazwidth = 150;
  
  public PartnerCache(int _nazwidth) {
    nazwidth = _nazwidth;
    createCache(dM.getDataModule().getPartneri());
  }
  public PartnerCache() {
    this(150);
  }
  
  public PartnerCache(boolean kup) {
    this(kup, 150);
  }
  public PartnerCache(boolean kup, int _nazwidth) {
    nazwidth = _nazwidth;
    createCache(kup ? dM.getDataModule().getPartneriKup() : 
      dM.getDataModule().getPartneriDob());
  }
  
  private void createCache(DataSet ds) {
    cache = new HashMap();
    ds.open();
    for (ds.first(); ds.inBounds(); ds.next())
      cache.put(new Integer(ds.getInt("CPAR")), new Data(ds));
  }
  
  public boolean isCached(int cpar) {
    if (cache == null)
      throw new IllegalArgumentException("Cache is disposed");
    Integer p = new Integer(cpar);
    return cache.containsKey(p);
  }
  
  public String getName(int cpar) {
    if (cache == null)
      throw new IllegalArgumentException("Cache is disposed");
    Integer p = new Integer(cpar);
    if (!cache.containsKey(p)) return null;
    return ((Data) cache.get(p)).getName();
  }
  
  public String getNameNotNull(int cpar) {
    String name = getName(cpar);
    return name == null ? "" : name;
  }
  
  public Data getData(int cpar) {
    if (cache == null)
      throw new IllegalArgumentException("Cache is disposed");
    Integer p = new Integer(cpar);
    if (!cache.containsKey(p)) return null;
    return (Data) cache.get(p);
  }
  
  public void dispose() {
    cache.clear();
    cache = null;
  }
  
  public static class Data {
    private String naziv;
    private String mb;
    private String oib;
    private String grupa;
    private short zup;
    private int pbr;
    private int agent;
    public Data() {
      naziv = "Nepoznat partner";
      mb = oib = grupa = "";
      pbr = agent = -1;
      zup = -1;
    }
    public Data(DataSet ds) {
      String pripicuk = frmParam.getParam("sk", "nazparext","N","Prikazati naziv partnera zajedno sa adresom i telefonom (D/N)").equalsIgnoreCase("D")?
          "; "+ds.getString("ADR")+"; "+ds.getString("MJ")+/*"; "+ds.getString("MB")+*/"; "+ds.getString("TEL"):"";
      naziv = ds.getString("NAZPAR")+pripicuk;
      if (naziv.length()>nazwidth) naziv = naziv.substring(0,nazwidth);
      mb = ds.getString("MB");
      oib = ds.getString("OIB");
      zup = ds.getShort("CZUP");
      pbr = ds.getInt("PBR");
      agent = ds.getInt("CAGENT");
      grupa = ds.getString("CGRPAR");
    }
    
    public String getName() {
      return naziv;
    }
    
    public short getZup() {
      return zup;
    }
    
    public int getPbr() {
      return pbr;
    }
    
    public int getAgent() {
      return agent;
    }
    
    public String getMB() {
      return mb;
    }
    
    public String getOIB() {
      return oib;
    }
    
    public String getGrupa() {
      return grupa;
    }
  }
}
